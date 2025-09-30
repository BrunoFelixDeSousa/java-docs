# Quarkus com Kubernetes e Docker

## 📋 Índice

1. [Conceitos Fundamentais](#conceitos-fundamentais)
2. [Docker](#docker)
3. [Kubernetes](#kubernetes)
4. [Quarkus e Kubernetes](#quarkus-e-kubernetes)
5. [Criando e Gerenciando Pods](#criando-e-gerenciando-pods)
6. [Deployments e Services](#deployments-e-services)
7. [ConfigMaps e Secrets](#configmaps-e-secrets)
8. [Práticas Recomendadas](#práticas-recomendadas)

---

## Conceitos Fundamentais

### O que é Containerização?

Containerização é uma tecnologia de virtualização leve que permite empacotar uma aplicação com todas as suas dependências em um ambiente isolado e portátil.

**Benefícios:**
- ✅ Portabilidade entre ambientes
- ✅ Isolamento de recursos
- ✅ Consistência entre desenvolvimento e produção
- ✅ Escalabilidade simplificada
- ✅ Uso eficiente de recursos

### Container vs VM

| Aspecto | Container | Virtual Machine |
|---------|-----------|-----------------|
| **Tamanho** | MB | GB |
| **Startup** | Segundos | Minutos |
| **Isolamento** | Processo | Sistema Operacional |
| **Overhead** | Baixo | Alto |
| **Densidade** | Alta | Baixa |

---

## Docker

### O que é Docker?

Docker é uma plataforma de containerização que permite criar, distribuir e executar aplicações em containers.

**Componentes principais:**
- **Docker Engine**: Runtime que executa containers
- **Docker Image**: Template imutável com a aplicação e dependências
- **Docker Container**: Instância em execução de uma imagem
- **Dockerfile**: Script para construir imagens
- **Docker Registry**: Repositório de imagens (Docker Hub, etc)

### Dockerfile para Quarkus

#### Dockerfile Multi-stage (Recomendado)

```dockerfile
## Stage 1: Build
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copia dependências primeiro (cache layer)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia código fonte e compila
COPY src ./src
RUN mvn package -DskipTests

## Stage 2: Runtime
FROM registry.access.redhat.com/ubi8/openjdk-17:1.14

ENV LANGUAGE='en_US:en'

# Copia o jar da aplicação
COPY --from=build --chown=185 /app/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build --chown=185 /app/target/quarkus-app/*.jar /deployments/
COPY --from=build --chown=185 /app/target/quarkus-app/app/ /deployments/app/
COPY --from=build --chown=185 /app/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT ["java", "-jar", "/deployments/quarkus-run.jar"]
```

#### Dockerfile Native (GraalVM)

```dockerfile
## Stage 1: Build Native
FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:23.0-java17 AS build
USER quarkus
WORKDIR /code

COPY --chown=quarkus:quarkus mvnw /code/mvnw
COPY --chown=quarkus:quarkus .mvn /code/.mvn
COPY --chown=quarkus:quarkus pom.xml /code/
COPY --chown=quarkus:quarkus src /code/src

RUN ./mvnw package -Pnative -DskipTests

## Stage 2: Runtime Native
FROM quay.io/quarkus/quarkus-micro-image:2.0
WORKDIR /work/

COPY --from=build /code/target/*-runner /work/application

RUN chmod 775 /work /work/application

EXPOSE 8080
USER 1001

CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
```

### Comandos Docker Essenciais

```bash
# Construir imagem
docker build -t meu-app:1.0 .

# Construir com build args
docker build --build-arg QUARKUS_PROFILE=prod -t meu-app:1.0 .

# Listar imagens
docker images

# Executar container
docker run -d -p 8080:8080 --name meu-container meu-app:1.0

# Executar com variáveis de ambiente
docker run -d -p 8080:8080 \
  -e QUARKUS_DATASOURCE_URL=jdbc:postgresql://db:5432/mydb \
  -e QUARKUS_DATASOURCE_USERNAME=user \
  -e QUARKUS_DATASOURCE_PASSWORD=pass \
  meu-app:1.0

# Listar containers em execução
docker ps

# Listar todos os containers
docker ps -a

# Ver logs do container
docker logs meu-container
docker logs -f meu-container  # Follow logs

# Acessar shell do container
docker exec -it meu-container /bin/bash

# Parar container
docker stop meu-container

# Remover container
docker rm meu-container

# Remover imagem
docker rmi meu-app:1.0

# Push para registry
docker tag meu-app:1.0 registry.exemplo.com/meu-app:1.0
docker push registry.exemplo.com/meu-app:1.0

# Inspecionar container
docker inspect meu-container

# Ver estatísticas de uso
docker stats meu-container
```

### Docker Compose para Desenvolvimento

```yaml
version: '3.8'

services:
  app:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      QUARKUS_DATASOURCE_URL: jdbc:postgresql://db:5432/mydb
      QUARKUS_DATASOURCE_USERNAME: quarkus
      QUARKUS_DATASOURCE_PASSWORD: quarkus
    depends_on:
      - db
      - redis
    networks:
      - app-network

  db:
    image: postgres:15
    environment:
      POSTGRES_DB: mydb
      POSTGRES_USER: quarkus
      POSTGRES_PASSWORD: quarkus
    ports:
      - "5432:5432"
    volumes:
      - postgres-data:/var/lib/postgresql/data
    networks:
      - app-network

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    networks:
      - app-network

volumes:
  postgres-data:

networks:
  app-network:
    driver: bridge
```

```bash
# Subir todos os serviços
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar serviços
docker-compose down

# Parar e remover volumes
docker-compose down -v
```

---

## Kubernetes

### O que é Kubernetes?

Kubernetes (K8s) é uma plataforma de orquestração de containers que automatiza o deployment, scaling e gerenciamento de aplicações containerizadas.

**Características principais:**
- 🔄 Auto-healing: Reinicia containers que falham
- 📈 Auto-scaling: Escala aplicações automaticamente
- 🔀 Load balancing: Distribui tráfego entre pods
- 🚀 Rolling updates: Atualiza aplicações sem downtime
- 🔐 Gerenciamento de secrets e configurações
- 📦 Storage orchestration: Gerencia volumes persistentes

### Arquitetura do Kubernetes

#### Control Plane (Master)

1. **API Server**: Interface REST para gerenciar o cluster
2. **etcd**: Banco de dados distribuído que armazena estado do cluster
3. **Scheduler**: Decide em qual node os pods serão executados
4. **Controller Manager**: Gerencia controllers (ReplicaSet, Deployment, etc)
5. **Cloud Controller Manager**: Integrações com cloud providers

#### Worker Nodes

1. **kubelet**: Agente que garante que containers estão rodando nos pods
2. **kube-proxy**: Gerencia regras de rede
3. **Container Runtime**: Docker, containerd, CRI-O, etc

### Objetos Principais do Kubernetes

#### 1. Pod

**Conceito**: Menor unidade deployável no Kubernetes. Agrupa um ou mais containers que compartilham rede e storage.

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: quarkus-app-pod
  labels:
    app: quarkus-app
    version: v1
spec:
  containers:
  - name: quarkus-app
    image: registry.exemplo.com/quarkus-app:1.0
    ports:
    - containerPort: 8080
      protocol: TCP
    env:
    - name: QUARKUS_PROFILE
      value: "prod"
    - name: QUARKUS_DATASOURCE_URL
      value: "jdbc:postgresql://postgres:5432/mydb"
    resources:
      requests:
        memory: "256Mi"
        cpu: "250m"
      limits:
        memory: "512Mi"
        cpu: "500m"
    livenessProbe:
      httpGet:
        path: /q/health/live
        port: 8080
      initialDelaySeconds: 30
      periodSeconds: 10
    readinessProbe:
      httpGet:
        path: /q/health/ready
        port: 8080
      initialDelaySeconds: 5
      periodSeconds: 5
```

**Características:**
- Containers no mesmo pod compartilham IP e volumes
- Containers podem se comunicar via localhost
- Pods são efêmeros - podem ser recriados a qualquer momento

#### 2. ReplicaSet

**Conceito**: Garante que um número específico de réplicas de pods esteja sempre rodando.

```yaml
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: quarkus-app-rs
spec:
  replicas: 3
  selector:
    matchLabels:
      app: quarkus-app
  template:
    metadata:
      labels:
        app: quarkus-app
    spec:
      containers:
      - name: quarkus-app
        image: registry.exemplo.com/quarkus-app:1.0
        ports:
        - containerPort: 8080
```

**Uso**: Raramente criado diretamente. Use Deployments em vez disso.

#### 3. Deployment

**Conceito**: Gerencia ReplicaSets e permite atualizações declarativas de pods.

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: quarkus-app
  labels:
    app: quarkus-app
spec:
  replicas: 3
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  selector:
    matchLabels:
      app: quarkus-app
  template:
    metadata:
      labels:
        app: quarkus-app
        version: v1
    spec:
      containers:
      - name: quarkus-app
        image: registry.exemplo.com/quarkus-app:1.0
        imagePullPolicy: Always
        ports:
        - containerPort: 8080
          name: http
        env:
        - name: JAVA_OPTS
          value: "-Xmx256m -Xms128m"
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /q/health/live
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 3
        readinessProbe:
          httpGet:
            path: /q/health/ready
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5
          timeoutSeconds: 3
          failureThreshold: 3
```

**Estratégias de Deploy:**

- **RollingUpdate** (padrão): Atualiza pods gradualmente
- **Recreate**: Para todos os pods antes de criar novos

#### 4. Service

**Conceito**: Abstração que expõe pods como um serviço de rede.

##### ClusterIP (Padrão)

```yaml
apiVersion: v1
kind: Service
metadata:
  name: quarkus-app-service
spec:
  type: ClusterIP
  selector:
    app: quarkus-app
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
```

##### NodePort

```yaml
apiVersion: v1
kind: Service
metadata:
  name: quarkus-app-nodeport
spec:
  type: NodePort
  selector:
    app: quarkus-app
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
    nodePort: 30080
```

##### LoadBalancer

```yaml
apiVersion: v1
kind: Service
metadata:
  name: quarkus-app-lb
spec:
  type: LoadBalancer
  selector:
    app: quarkus-app
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
```

**Tipos de Service:**
- **ClusterIP**: Expõe serviço internamente no cluster
- **NodePort**: Expõe serviço em uma porta em cada node
- **LoadBalancer**: Expõe serviço externamente usando load balancer
- **ExternalName**: Mapeia serviço para um DNS externo

#### 5. Ingress

**Conceito**: Gerencia acesso externo a serviços no cluster (HTTP/HTTPS).

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: quarkus-app-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
    cert-manager.io/cluster-issuer: "letsencrypt-prod"
spec:
  ingressClassName: nginx
  tls:
  - hosts:
    - app.exemplo.com
    secretName: app-tls
  rules:
  - host: app.exemplo.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: quarkus-app-service
            port:
              number: 80
```

#### 6. ConfigMap

**Conceito**: Armazena configurações não-sensíveis em pares chave-valor.

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: quarkus-app-config
data:
  application.properties: |
    quarkus.http.port=8080
    quarkus.datasource.db-kind=postgresql
    quarkus.log.level=INFO
  database.host: "postgres.default.svc.cluster.local"
  database.port: "5432"
  database.name: "mydb"
```

**Usando em Pod:**

```yaml
spec:
  containers:
  - name: quarkus-app
    image: registry.exemplo.com/quarkus-app:1.0
    envFrom:
    - configMapRef:
        name: quarkus-app-config
    volumeMounts:
    - name: config-volume
      mountPath: /config
  volumes:
  - name: config-volume
    configMap:
      name: quarkus-app-config
```

#### 7. Secret

**Conceito**: Armazena informações sensíveis (senhas, tokens, chaves).

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: quarkus-app-secret
type: Opaque
data:
  database.username: cXVhcmt1cw==  # base64: quarkus
  database.password: cXVhcmt1czEyMw==  # base64: quarkus123
stringData:
  api.key: "minha-chave-secreta"  # Não precisa base64
```

**Criando via kubectl:**

```bash
kubectl create secret generic quarkus-app-secret \
  --from-literal=database.username=quarkus \
  --from-literal=database.password=quarkus123
```

**Usando em Pod:**

```yaml
spec:
  containers:
  - name: quarkus-app
    image: registry.exemplo.com/quarkus-app:1.0
    env:
    - name: DB_USERNAME
      valueFrom:
        secretKeyRef:
          name: quarkus-app-secret
          key: database.username
    - name: DB_PASSWORD
      valueFrom:
        secretKeyRef:
          name: quarkus-app-secret
          key: database.password
```

#### 8. PersistentVolume e PersistentVolumeClaim

**PersistentVolume (PV)**: Recurso de storage no cluster.

```yaml
apiVersion: v1
kind: PersistentVolume
metadata:
  name: postgres-pv
spec:
  capacity:
    storage: 10Gi
  accessModes:
    - ReadWriteOnce
  persistentVolumeReclaimPolicy: Retain
  storageClassName: standard
  hostPath:
    path: /data/postgres
```

**PersistentVolumeClaim (PVC)**: Requisição de storage.

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: postgres-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 10Gi
  storageClassName: standard
```

**Usando em Pod:**

```yaml
spec:
  containers:
  - name: postgres
    image: postgres:15
    volumeMounts:
    - name: postgres-storage
      mountPath: /var/lib/postgresql/data
  volumes:
  - name: postgres-storage
    persistentVolumeClaim:
      claimName: postgres-pvc
```

---

## Quarkus e Kubernetes

### Extensões Quarkus para Kubernetes

```bash
# Adicionar extensão Kubernetes
./mvnw quarkus:add-extension -Dextensions="kubernetes"

# Adicionar extensão OpenShift
./mvnw quarkus:add-extension -Dextensions="openshift"

# Adicionar extensão Kubernetes Config
./mvnw quarkus:add-extension -Dextensions="kubernetes-config"

# Adicionar Jib (para build de imagens)
./mvnw quarkus:add-extension -Dextensions="container-image-jib"
```

### Configuração no application.properties

```properties
# Configurações Kubernetes
quarkus.kubernetes.deployment-target=kubernetes

# Nome da aplicação
quarkus.application.name=quarkus-app

# Configurações de imagem
quarkus.container-image.group=meu-registry
quarkus.container-image.name=quarkus-app
quarkus.container-image.tag=1.0.0
quarkus.container-image.registry=registry.exemplo.com
quarkus.container-image.push=true

# Replicas
quarkus.kubernetes.replicas=3

# Porta exposta
quarkus.kubernetes.ports.http.container-port=8080

# Service Type
quarkus.kubernetes.service-type=load-balancer

# Resources
quarkus.kubernetes.resources.requests.memory=256Mi
quarkus.kubernetes.resources.requests.cpu=250m
quarkus.kubernetes.resources.limits.memory=512Mi
quarkus.kubernetes.resources.limits.cpu=500m

# Health checks
quarkus.kubernetes.liveness-probe.http-action-path=/q/health/live
quarkus.kubernetes.liveness-probe.initial-delay=30s
quarkus.kubernetes.liveness-probe.period=10s

quarkus.kubernetes.readiness-probe.http-action-path=/q/health/ready
quarkus.kubernetes.readiness-probe.initial-delay=5s
quarkus.kubernetes.readiness-probe.period=5s

# Environment variables
quarkus.kubernetes.env.vars.quarkus-profile=prod
quarkus.kubernetes.env.secrets=quarkus-app-secret
quarkus.kubernetes.env.configmaps=quarkus-app-config

# Labels
quarkus.kubernetes.labels.app=quarkus-app
quarkus.kubernetes.labels.version=v1

# Annotations
quarkus.kubernetes.annotations."prometheus.io/scrape"=true
quarkus.kubernetes.annotations."prometheus.io/port"=8080
quarkus.kubernetes.annotations."prometheus.io/path"=/q/metrics

# Ingress
quarkus.kubernetes.ingress.expose=true
quarkus.kubernetes.ingress.host=app.exemplo.com
quarkus.kubernetes.ingress.tls.quarkus-app-tls.enabled=true

# Namespace
quarkus.kubernetes.namespace=production
```

### Build e Deploy Automático

```bash
# Build e gera manifestos Kubernetes
./mvnw clean package -Dquarkus.kubernetes.deploy=true

# Build nativo com deploy
./mvnw clean package -Pnative \
  -Dquarkus.native.container-build=true \
  -Dquarkus.kubernetes.deploy=true

# Os manifestos são gerados em:
# target/kubernetes/kubernetes.yml
# target/kubernetes/kubernetes.json
```

### Integração com Kubernetes Config

Permite carregar ConfigMaps e Secrets como propriedades do Quarkus.

```properties
# application.properties
quarkus.kubernetes-config.enabled=true
quarkus.kubernetes-config.config-maps=quarkus-app-config
quarkus.kubernetes-config.secrets.enabled=true
quarkus.kubernetes-config.secrets=quarkus-app-secret
```

```java
@ConfigProperty(name = "database.host")
String databaseHost;

@ConfigProperty(name = "database.username")
String databaseUsername;
```

---

## Criando e Gerenciando Pods

### Comandos kubectl Essenciais

#### Criação de Recursos

```bash
# Aplicar manifesto
kubectl apply -f deployment.yaml

# Aplicar diretório inteiro
kubectl apply -f k8s/

# Criar resource de forma imperativa
kubectl create deployment quarkus-app --image=registry.exemplo.com/quarkus-app:1.0

# Criar service
kubectl expose deployment quarkus-app --type=LoadBalancer --port=80 --target-port=8080
```

#### Visualização de Recursos

```bash
# Listar pods
kubectl get pods
kubectl get pods -o wide  # Mais informações
kubectl get pods -w  # Watch mode

# Listar em todos os namespaces
kubectl get pods --all-namespaces
kubectl get pods -A

# Listar deployments
kubectl get deployments
kubectl get deploy

# Listar services
kubectl get services
kubectl get svc

# Listar tudo
kubectl get all

# Descrever recurso (detalhes completos)
kubectl describe pod quarkus-app-xxxx
kubectl describe deployment quarkus-app
kubectl describe service quarkus-app-service

# Ver manifestos em YAML
kubectl get pod quarkus-app-xxxx -o yaml
kubectl get deployment quarkus-app -o yaml

# Ver em JSON
kubectl get pod quarkus-app-xxxx -o json
```

#### Logs e Debug

```bash
# Ver logs de pod
kubectl logs quarkus-app-xxxx

# Follow logs
kubectl logs -f quarkus-app-xxxx

# Logs de container específico
kubectl logs quarkus-app-xxxx -c quarkus-app

# Logs de todos os pods de um deployment
kubectl logs deployment/quarkus-app

# Logs anteriores (quando pod crashou)
kubectl logs quarkus-app-xxxx --previous

# Executar comando no pod
kubectl exec quarkus-app-xxxx -- ls /deployments

# Shell interativo
kubectl exec -it quarkus-app-xxxx -- /bin/bash

# Port forward para acesso local
kubectl port-forward pod/quarkus-app-xxxx 8080:8080
kubectl port-forward service/quarkus-app-service 8080:80
```

#### Atualização e Scaling

```bash
# Escalar deployment
kubectl scale deployment quarkus-app --replicas=5

# Autoscaling
kubectl autoscale deployment quarkus-app --min=2 --max=10 --cpu-percent=80

# Atualizar imagem
kubectl set image deployment/quarkus-app quarkus-app=registry.exemplo.com/quarkus-app:2.0

# Ver histórico de rollout
kubectl rollout history deployment/quarkus-app

# Ver status de rollout
kubectl rollout status deployment/quarkus-app

# Rollback para versão anterior
kubectl rollout undo deployment/quarkus-app

# Rollback para revisão específica
kubectl rollout undo deployment/quarkus-app --to-revision=2

# Pausar rollout
kubectl rollout pause deployment/quarkus-app

# Resumir rollout
kubectl rollout resume deployment/quarkus-app
```

#### Deleção de Recursos

```bash
# Deletar pod
kubectl delete pod quarkus-app-xxxx

# Deletar deployment
kubectl delete deployment quarkus-app

# Deletar service
kubectl delete service quarkus-app-service

# Deletar por arquivo
kubectl delete -f deployment.yaml

# Deletar tudo com label
kubectl delete all -l app=quarkus-app

# Force delete (cuidado!)
kubectl delete pod quarkus-app-xxxx --force --grace-period=0
```

#### Namespaces

```bash
# Listar namespaces
kubectl get namespaces
kubectl get ns

# Criar namespace
kubectl create namespace producao

# Usar namespace
kubectl config set-context --current --namespace=producao

# Aplicar recurso em namespace específico
kubectl apply -f deployment.yaml -n producao

# Deletar namespace
kubectl delete namespace producao
```

#### Contexts e Clusters

```bash
# Ver contextos
kubectl config get-contexts

# Trocar de contexto
kubectl config use-context meu-cluster

# Ver configuração atual
kubectl config view

# Ver cluster info
kubectl cluster-info
```

### Exemplo Completo de Workflow

```bash
# 1. Criar namespace
kubectl create namespace meu-app

# 2. Criar secrets
kubectl create secret generic db-secret \
  --from-literal=username=admin \
  --from-literal=password=secret123 \
  -n meu-app

# 3. Criar configmap
kubectl create configmap app-config \
  --from-literal=database.host=postgres.meu-app.svc.cluster.local \
  --from-literal=database.port=5432 \
  -n meu-app

# 4. Aplicar deployment
kubectl apply -f deployment.yaml -n meu-app

# 5. Verificar pods
kubectl get pods -n meu-app

# 6. Ver logs
kubectl logs -f deployment/quarkus-app -n meu-app

# 7. Criar service
kubectl apply -f service.yaml -n meu-app

# 8. Testar aplicação
kubectl port-forward service/quarkus-app-service 8080:80 -n meu-app
curl http://localhost:8080/hello

# 9. Escalar
kubectl scale deployment quarkus-app --replicas=3 -n meu-app

# 10. Atualizar versão
kubectl set image deployment/quarkus-app \
  quarkus-app=registry.exemplo.com/quarkus-app:2.0 \
  -n meu-app

# 11. Verificar rollout
kubectl rollout status deployment/quarkus-app -n meu-app
```

---

## Deployments e Services

### Deployment Completo

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: quarkus-app
  namespace: production
  labels:
    app: quarkus-app
    tier: backend
spec:
  replicas: 3
  revisionHistoryLimit: 10
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  selector:
    matchLabels:
      app: quarkus-app
  template:
    metadata:
      labels:
        app: quarkus-app
        version: v1
        tier: backend
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "8080"
        prometheus.io/path: "/q/metrics"
    spec:
      serviceAccountName: quarkus-app-sa
      securityContext:
        runAsNonRoot: true
        runAsUser: 1000
        fsGroup: 1000
      containers:
      - name: quarkus-app
        image: registry.exemplo.com/quarkus-app:1.0.0
        imagePullPolicy: Always
        ports:
        - name: http
          containerPort: 8080
          protocol: TCP
        env:
        - name: KUBERNETES_NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        - name: HOSTNAME
          valueFrom:
            fieldRef:
              fieldPath: metadata.name
        envFrom:
        - configMapRef:
            name: quarkus-app-config
        - secretRef:
            name: quarkus-app-secret
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /q/health/live
            port: 8080
            scheme: HTTP
          initialDelaySeconds: 30
          periodSeconds: 10
          timeoutSeconds: 5
          successThreshold: 1
          failureThreshold: 3
        readinessProbe:
          httpGet:
            path: /q/health/ready
            port: 8080
            scheme: HTTP
          initialDelaySeconds: 5
          periodSeconds: 5
          timeoutSeconds: 3
          successThreshold: 1
          failureThreshold: 3
        startupProbe:
          httpGet:
            path: /q/health/started
            port: 8080
          initialDelaySeconds: 0
          periodSeconds: 5
          timeoutSeconds: 3
          failureThreshold: 30
        volumeMounts:
        - name: config
          mountPath: /config
          readOnly: true
        - name: logs
          mountPath: /logs
        securityContext:
          allowPrivilegeEscalation: false
          readOnlyRootFilesystem: true
          capabilities:
            drop:
            - ALL
      volumes:
      - name: config
        configMap:
          name: quarkus-app-config
      - name: logs
        emptyDir: {}
      imagePullSecrets:
      - name: registry-secret
      affinity:
        podAntiAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
          - weight: 100
            podAffinityTerm:
              labelSelector:
                matchExpressions:
                - key: app
                  operator: In
                  values:
                  - quarkus-app
              topologyKey: kubernetes.io/hostname
```

### Service Completo

```yaml
apiVersion: v1
kind: Service
metadata:
  name: quarkus-app-service
  namespace: production
  labels:
    app: quarkus-app
  annotations:
    service.beta.kubernetes.io/aws-load-balancer-type: "nlb"
spec:
  type: LoadBalancer
  sessionAffinity: ClientIP
  sessionAffinityConfig:
    clientIP:
      timeoutSeconds: 10800
  selector:
    app: quarkus-app
  ports:
  - name: http
    protocol: TCP
    port: 80
    targetPort: 8080
  - name: management
    protocol: TCP
    port: 9090
    targetPort: 9090
```

### Ingress Completo

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: quarkus-app-ingress
  namespace: production
  annotations:
    kubernetes.io/ingress.class: "nginx"
    cert-manager.io/cluster-issuer: "letsencrypt-prod"
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
    nginx.ingress.kubernetes.io/force-ssl-redirect: "true"
    nginx.ingress.kubernetes.io/proxy-body-size: "10m"
    nginx.ingress.kubernetes.io/rate-limit: "100"
spec:
  tls:
  - hosts:
    - api.exemplo.com
    secretName: api-tls-secret
  rules:
  - host: api.exemplo.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: quarkus-app-service
            port:
              number: 80
```

---

## ConfigMaps e Secrets

### ConfigMap Completo

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: quarkus-app-config
  namespace: production
data:
  # Propriedades simples
  quarkus.log.level: "INFO"
  quarkus.http.port: "8080"
  
  # Arquivo de configuração completo
  application.properties: |
    # Database
    quarkus.datasource.db-kind=postgresql
    quarkus.datasource.jdbc.url=jdbc:postgresql://postgres:5432/mydb
    quarkus.datasource.jdbc.max-size=16
    
    # Hibernate
    quarkus.hibernate-orm.database.generation=update
    
    # Logging
    quarkus.log.console.enable=true
    quarkus.log.console.format=%d{HH:mm:ss} %-5p [%c{2.}] (%t) %s%e%n
    quarkus.log.console.level=INFO
    
    # CORS
    quarkus.http.cors=true
    quarkus.http.cors.origins=*
    
    # OpenAPI
    quarkus.swagger-ui.always-include=true
    quarkus.smallrye-openapi.path=/openapi
```

**Criando via kubectl:**

```bash
# De arquivo
kubectl create configmap app-config --from-file=application.properties

# De múltiplos arquivos
kubectl create configmap app-config \
  --from-file=config/app.properties \
  --from-file=config/db.properties

# De literais
kubectl create configmap app-config \
  --from-literal=database.host=postgres \
  --from-literal=database.port=5432

# De arquivo env
kubectl create configmap app-config --from-env-file=config.env
```

### Secret Completo

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: quarkus-app-secret
  namespace: production
type: Opaque
data:
  # Base64 encoded values
  database.username: cXVhcmt1cw==
  database.password: cXVhcmt1czEyMw==
  jwt.secret: bXktc3VwZXItc2VjcmV0LWtleQ==
stringData:
  # Plain text (auto-encoded)
  api.key: "abc123xyz"
  oauth.client.secret: "oauth-secret-value"
```

**Tipos de Secrets:**

```yaml
# TLS Secret
apiVersion: v1
kind: Secret
metadata:
  name: tls-secret
type: kubernetes.io/tls
data:
  tls.crt: <base64-cert>
  tls.key: <base64-key>

# Docker Registry Secret
apiVersion: v1
kind: Secret
metadata:
  name: registry-secret
type: kubernetes.io/dockerconfigjson
data:
  .dockerconfigjson: <base64-docker-config>

# Basic Auth Secret
apiVersion: v1
kind: Secret
metadata:
  name: basic-auth
type: kubernetes.io/basic-auth
stringData:
  username: admin
  password: secret123
```

**Criando via kubectl:**

```bash
# Generic secret
kubectl create secret generic db-secret \
  --from-literal=username=admin \
  --from-literal=password=secret123

# TLS secret
kubectl create secret tls tls-secret \
  --cert=path/to/tls.cert \
  --key=path/to/tls.key

# Docker registry
kubectl create secret docker-registry registry-secret \
  --docker-server=registry.exemplo.com \
  --docker-username=usuario \
  --docker-password=senha \
  --docker-email=email@exemplo.com

# De arquivo
kubectl create secret generic ssh-key \
  --from-file=ssh-privatekey=~/.ssh/id_rsa
```

---

## Práticas Recomendadas

### 1. Health Checks

Sempre implemente health checks no Quarkus:

```java
@ApplicationScoped
@Liveness
public class LivenessCheck implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("Application is live");
    }
}

@ApplicationScoped
@Readiness
public class ReadinessCheck implements HealthCheck {
    @Inject
    AgroalDataSource dataSource;
    
    @Override
    public HealthCheckResponse call() {
        try {
            dataSource.getConnection().close();
            return HealthCheckResponse.up("Database connection ready");
        } catch (SQLException e) {
            return HealthCheckResponse.down("Database connection failed");
        }
    }
}
```

### 2. Resources e Limits

Sempre defina requests e limits:

```yaml
resources:
  requests:
    memory: "256Mi"  # Garantido
    cpu: "250m"      # Garantido
  limits:
    memory: "512Mi"  # Máximo
    cpu: "500m"      # Máximo
```

### 3. Tagging de Imagens

Use tags semânticas, nunca `latest`:

```bash
# ❌ Errado
docker build -t meu-app:latest .

# ✅ Correto
docker build -t meu-app:1.2.3 .
docker build -t meu-app:1.2.3-$(git rev-parse --short HEAD) .
```

### 4. Multi-stage Builds

Sempre use multi-stage para imagens menores:

```dockerfile
FROM maven:3.8.6 AS build
# ... build stage

FROM openjdk:17-slim
COPY --from=build /app/target/*.jar app.jar
# ... runtime stage
```

### 5. Security Context

Configure security context:

```yaml
securityContext:
  runAsNonRoot: true
  runAsUser: 1000
  allowPrivilegeEscalation: false
  readOnlyRootFilesystem: true
  capabilities:
    drop:
    - ALL
```

### 6. Labels e Annotations

Use labels consistentes:

```yaml
metadata:
  labels:
    app.kubernetes.io/name: quarkus-app
    app.kubernetes.io/instance: quarkus-app-prod
    app.kubernetes.io/version: "1.0.0"
    app.kubernetes.io/component: backend
    app.kubernetes.io/part-of: my-system
    app.kubernetes.io/managed-by: kubectl
```

### 7. Namespace Separation

Separe ambientes por namespace:

```bash
kubectl create namespace development
kubectl create namespace staging
kubectl create namespace production
```

### 8. Resource Quotas

Defina quotas por namespace:

```yaml
apiVersion: v1
kind: ResourceQuota
metadata:
  name: compute-quota
  namespace: production
spec:
  hard:
    requests.cpu: "10"
    requests.memory: 20Gi
    limits.cpu: "20"
    limits.memory: 40Gi
    pods: "50"
```

### 9. Network Policies

Implemente network policies:

```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: quarkus-app-netpol
spec:
  podSelector:
    matchLabels:
      app: quarkus-app
  policyTypes:
  - Ingress
  - Egress
  ingress:
  - from:
    - podSelector:
        matchLabels:
          app: frontend
    ports:
    - protocol: TCP
      port: 8080
  egress:
  - to:
    - podSelector:
        matchLabels:
          app: postgres
    ports:
    - protocol: TCP
      port: 5432
```

### 10. Monitoring e Observabilidade

Configure métricas e traces:

```properties
# Prometheus
quarkus.micrometer.export.prometheus.enabled=true
quarkus.micrometer.export.prometheus.path=/q/metrics

# OpenTelemetry
quarkus.otel.exporter.otlp.endpoint=http://jaeger:4317
quarkus.otel.traces.enabled=true
```

### 11. Graceful Shutdown

Configure shutdown gracioso:

```properties
quarkus.shutdown.timeout=30s
quarkus.http.io-threads=8
```

```yaml
lifecycle:
  preStop:
    exec:
      command: ["/bin/sh", "-c", "sleep 15"]
terminationGracePeriodSeconds: 30
```

### 12. Auto-scaling (HPA)

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: quarkus-app-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: quarkus-app
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 80
  behavior:
    scaleDown:
      stabilizationWindowSeconds: 300
      policies:
      - type: Percent
        value: 50
        periodSeconds: 60
    scaleUp:
      stabilizationWindowSeconds: 0
      policies:
      - type: Percent
        value: 100
        periodSeconds: 30
      - type: Pods
        value: 2
        periodSeconds: 60
      selectPolicy: Max
```

---

## Exemplo Completo de Aplicação

### Estrutura de Diretórios

```
meu-app/
├── src/
│   └── main/
│       ├── java/
│       ├── resources/
│       └── docker/
│           ├── Dockerfile.jvm
│           └── Dockerfile.native
├── k8s/
│   ├── base/
│   │   ├── deployment.yaml
│   │   ├── service.yaml
│   │   ├── configmap.yaml
│   │   └── secret.yaml
│   ├── overlays/
│   │   ├── development/
│   │   ├── staging/
│   │   └── production/
│   └── kustomization.yaml
├── pom.xml
└── README.md
```

### deployment.yaml

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: quarkus-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: quarkus-app
  template:
    metadata:
      labels:
        app: quarkus-app
    spec:
      containers:
      - name: quarkus-app
        image: registry.exemplo.com/quarkus-app:latest
        ports:
        - containerPort: 8080
        envFrom:
        - configMapRef:
            name: quarkus-app-config
        - secretRef:
            name: quarkus-app-secret
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /q/health/live
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /q/health/ready
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5
```

### Script de Deploy

```bash
#!/bin/bash

# deploy.sh

set -e

ENVIRONMENT=${1:-development}
VERSION=${2:-latest}
REGISTRY="registry.exemplo.com"
APP_NAME="quarkus-app"

echo "🚀 Deploying ${APP_NAME}:${VERSION} to ${ENVIRONMENT}"

# 1. Build da aplicação
echo "📦 Building application..."
./mvnw clean package -DskipTests

# 2. Build da imagem Docker
echo "🐳 Building Docker image..."
docker build -t ${REGISTRY}/${APP_NAME}:${VERSION} .

# 3. Push da imagem
echo "⬆️  Pushing image to registry..."
docker push ${REGISTRY}/${APP_NAME}:${VERSION}

# 4. Apply Kubernetes manifests
echo "☸️  Applying Kubernetes manifests..."
kubectl apply -f k8s/overlays/${ENVIRONMENT}/

# 5. Update deployment image
echo "🔄 Updating deployment..."
kubectl set image deployment/${APP_NAME} \
  ${APP_NAME}=${REGISTRY}/${APP_NAME}:${VERSION} \
  -n ${ENVIRONMENT}

# 6. Wait for rollout
echo "⏳ Waiting for rollout..."
kubectl rollout status deployment/${APP_NAME} -n ${ENVIRONMENT}

# 7. Verify
echo "✅ Deployment completed!"
kubectl get pods -n ${ENVIRONMENT} -l app=${APP_NAME}

echo "🎉 Application is live!"
```

---

## Troubleshooting

### Pod não está iniciando

```bash
# Ver eventos do pod
kubectl describe pod <pod-name>

# Ver logs
kubectl logs <pod-name>

# Ver logs do container anterior
kubectl logs <pod-name> --previous

# Verificar probes
kubectl get pod <pod-name> -o yaml | grep -A 10 "livenessProbe\|readinessProbe"
```

### Problemas de Rede

```bash
# Testar conectividade entre pods
kubectl run -it --rm debug --image=busybox --restart=Never -- sh
# Dentro do pod:
wget -O- http://meu-service

# Ver endpoints do service
kubectl get endpoints meu-service

# Ver network policies
kubectl get networkpolicy
```

### Problemas de Performance

```bash
# Ver uso de recursos
kubectl top pods
kubectl top nodes

# Ver logs com timestamps
kubectl logs <pod-name> --timestamps

# Executar comando no pod
kubectl exec <pod-name> -- top
kubectl exec <pod-name> -- ps aux
```

### ImagePullBackOff

```bash
# Verificar imagePullSecrets
kubectl get pod <pod-name> -o yaml | grep imagePullSecrets -A 5

# Verificar secret
kubectl get secret registry-secret -o yaml

# Testar pull manual
docker pull <image-name>
```

---

## Recursos Adicionais

### Documentação Oficial

- [Kubernetes](https://kubernetes.io/docs/)
- [Quarkus Kubernetes](https://quarkus.io/guides/deploying-to-kubernetes)
- [Docker](https://docs.docker.com/)

### Ferramentas Úteis

- **kubectl**: CLI do Kubernetes
- **k9s**: Terminal UI para Kubernetes
- **lens**: IDE para Kubernetes
- **stern**: Multi-pod log tailing
- **kubectx/kubens**: Trocar contextos e namespaces
- **helm**: Package manager para Kubernetes
- **skaffold**: Workflow para desenvolvimento local

### Comandos Úteis

```bash
# Alias úteis
alias k='kubectl'
alias kgp='kubectl get pods'
alias kgs='kubectl get services'
alias kgd='kubectl get deployments'
alias kl='kubectl logs -f'
alias kx='kubectl exec -it'
alias kd='kubectl describe'

# Função para logs
klogs() {
  kubectl logs -f $(kubectl get pod -l app=$1 -o jsonpath='{.items[0].metadata.name}')
}

# Função para exec
kexec() {
  kubectl exec -it $(kubectl get pod -l app=$1 -o jsonpath='{.items[0].metadata.name}') -- /bin/bash
}
```

---

## Conclusão

Esta documentação cobre os principais conceitos e práticas para desenvolver aplicações Quarkus e deployar em Kubernetes. Lembre-se sempre de:

✅ Usar imagens otimizadas e multi-stage builds  
✅ Implementar health checks  
✅ Definir resources e limits  
✅ Configurar security contexts  
✅ Usar ConfigMaps e Secrets para configurações  
✅ Implementar monitoring e observabilidade  
✅ Testar em ambientes não-produtivos antes  
✅ Documentar suas configurações  
✅ Usar versionamento semântico  
✅ Implementar CI/CD  

Kubernetes é uma ferramenta poderosa que, quando bem utilizada, traz grande valor para o deployment e gerenciamento de aplicações modernas!
