````instructions
---
applyTo: "**/.github/**,**/docker/**,**/k8s/**,**/*docker*,**/.gitlab-ci.yml"
description: "Padrões para CI/CD, containerização, observabilidade e deployment"
---

# Padrões DevOps e Observabilidade

Aplicar as [instruções gerais](./copilot.instructions.md) e [padrões de segurança](./security.instructions.md).

## Containerização

### Dockerfile Multi-stage
```dockerfile
# ✅ PADRÃO - Multi-stage build para otimização
FROM maven:3.9-openjdk-17-slim AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build da aplicação
RUN mvn clean package -DskipTests

# Imagem de produção mínima
FROM registry.access.redhat.com/ubi8/openjdk-17:latest

ENV LANGUAGE='en_US:en'

# Criar usuário não-root para segurança
USER 1001

COPY --from=builder --chown=1001 /app/target/quarkus-app/lib/ /deployments/lib/
COPY --from=builder --chown=1001 /app/target/quarkus-app/*.jar /deployments/
COPY --from=builder --chown=1001 /app/target/quarkus-app/app/ /deployments/app/
COPY --from=builder --chown=1001 /app/target/quarkus-app/quarkus/ /deployments/quarkus/

# Health check endpoint
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/q/health || exit 1

EXPOSE 8080
USER 1001

ENTRYPOINT ["java", "-jar", "/deployments/quarkus-run.jar"]
```

### Docker Compose para Desenvolvimento
```yaml
# ✅ PADRÃO - docker-compose.dev.yml
version: '3.8'

services:
  app:
    build:
      context: .
      dockerfile: Dockerfile
      target: builder
    ports:
      - "8080:8080"
      - "5005:5005" # Debug port
    environment:
      - QUARKUS_PROFILE=dev
      - DB_HOST=postgres
      - DB_USERNAME=devuser
      - DB_PASSWORD=devpassword
      - JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
    volumes:
      - ./src:/app/src:ro
    depends_on:
      - postgres
      - redis
      - localstack
    networks:
      - dev-network

  postgres:
    image: postgres:15-alpine
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=myproject
      - POSTGRES_USER=devuser
      - POSTGRES_PASSWORD=devpassword
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./sql/init.sql:/docker-entrypoint-initdb.d/init.sql
    networks:
      - dev-network

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    networks:
      - dev-network

  localstack:
    image: localstack/localstack:latest
    ports:
      - "4566:4566"
    environment:
      - SERVICES=s3,sqs,sns
      - DEFAULT_REGION=us-east-1
      - DATA_DIR=/tmp/localstack/data
    volumes:
      - localstack_data:/tmp/localstack
    networks:
      - dev-network

volumes:
  postgres_data:
  redis_data:
  localstack_data:

networks:
  dev-network:
    driver: bridge
```

## CI/CD Pipeline

### GitHub Actions
```yaml
# ✅ PADRÃO - .github/workflows/ci.yml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

env:
  REGISTRY: ghcr.io
  IMAGE_NAME: ${{ github.repository }}

jobs:
  test:
    runs-on: ubuntu-latest
    
    services:
      postgres:
        image: postgres:15
        env:
          POSTGRES_PASSWORD: testpassword
          POSTGRES_USER: testuser
          POSTGRES_DB: testdb
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
        ports:
          - 5432:5432
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Setup JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: maven
    
    - name: Cache SonarCloud packages
      uses: actions/cache@v3
      with:
        path: ~/.sonar/cache
        key: ${{ runner.os }}-sonar
        
    - name: Run tests
      env:
        QUARKUS_DATASOURCE_JDBC_URL: jdbc:postgresql://localhost:5432/testdb
        QUARKUS_DATASOURCE_USERNAME: testuser
        QUARKUS_DATASOURCE_PASSWORD: testpassword
      run: |
        mvn clean verify \
          -Dquarkus.test.profile=test \
          -Dquarkus.package.type=jar
    
    - name: Generate test report
      uses: dorny/test-reporter@v1
      if: success() || failure()
      with:
        name: Maven Tests
        path: target/surefire-reports/*.xml
        reporter: java-junit
    
    - name: Upload coverage reports
      uses: codecov/codecov-action@v3
      with:
        files: ./target/site/jacoco/jacoco.xml
        fail_ci_if_error: true

  security-scan:
    runs-on: ubuntu-latest
    needs: test
    
    steps:
    - uses: actions/checkout@v4
      with:
        fetch-depth: 0
    
    - name: Setup JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: maven
    
    - name: OWASP Dependency Check
      run: |
        mvn org.owasp:dependency-check-maven:check \
          -Dformat=XML \
          -DfailBuildOnCVSS=7
    
    - name: SonarCloud Analysis
      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
        SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
      run: |
        mvn sonar:sonar \
          -Dsonar.projectKey=${{ github.repository_owner }}_${{ github.event.repository.name }} \
          -Dsonar.organization=${{ github.repository_owner }} \
          -Dsonar.host.url=https://sonarcloud.io \
          -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml

  build-and-push:
    runs-on: ubuntu-latest
    needs: [test, security-scan]
    if: github.event_name == 'push'
    
    permissions:
      contents: read
      packages: write
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Setup Docker Buildx
      uses: docker/setup-buildx-action@v3
    
    - name: Login to Container Registry
      uses: docker/login-action@v3
      with:
        registry: ${{ env.REGISTRY }}
        username: ${{ github.actor }}
        password: ${{ secrets.GITHUB_TOKEN }}
    
    - name: Extract metadata
      id: meta
      uses: docker/metadata-action@v5
      with:
        images: ${{ env.REGISTRY }}/${{ env.IMAGE_NAME }}
        tags: |
          type=ref,event=branch
          type=ref,event=pr
          type=sha,prefix={{branch}}-
          type=raw,value=latest,enable={{is_default_branch}}
    
    - name: Build and push Docker image
      uses: docker/build-push-action@v5
      with:
        context: .
        push: true
        tags: ${{ steps.meta.outputs.tags }}
        labels: ${{ steps.meta.outputs.labels }}
        cache-from: type=gha
        cache-to: type=gha,mode=max

  deploy:
    runs-on: ubuntu-latest
    needs: build-and-push
    if: github.ref == 'refs/heads/main'
    environment: production
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Deploy to Kubernetes
      run: |
        echo "Deployment would happen here"
        # kubectl apply -f k8s/
```

## Kubernetes

### Deployment Manifests
```yaml
# ✅ PADRÃO - k8s/deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: myproject-app
  labels:
    app: myproject
    version: v1
spec:
  replicas: 3
  selector:
    matchLabels:
      app: myproject
      version: v1
  template:
    metadata:
      labels:
        app: myproject
        version: v1
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/path: "/q/metrics"
        prometheus.io/port: "8080"
    spec:
      securityContext:
        runAsNonRoot: true
        runAsUser: 1001
        fsGroup: 1001
      containers:
      - name: app
        image: ghcr.io/mycompany/myproject:latest
        ports:
        - containerPort: 8080
          protocol: TCP
        env:
        - name: QUARKUS_PROFILE
          value: "prod"
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: password
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: jwt-secret
              key: secret
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
          timeoutSeconds: 3
          failureThreshold: 3
        readinessProbe:
          httpGet:
            path: /q/health/ready
            port: 8080
          initialDelaySeconds: 10
          periodSeconds: 5
          timeoutSeconds: 3
          failureThreshold: 3
        securityContext:
          allowPrivilegeEscalation: false
          readOnlyRootFilesystem: true
          capabilities:
            drop:
              - ALL
        volumeMounts:
        - name: tmp-volume
          mountPath: /tmp
      volumes:
      - name: tmp-volume
        emptyDir: {}
      imagePullSecrets:
      - name: regcred
---
apiVersion: v1
kind: Service
metadata:
  name: myproject-service
  labels:
    app: myproject
spec:
  selector:
    app: myproject
  ports:
  - port: 80
    targetPort: 8080
    protocol: TCP
    name: http
  type: ClusterIP
---
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: myproject-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
    cert-manager.io/cluster-issuer: letsencrypt-prod
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
    nginx.ingress.kubernetes.io/force-ssl-redirect: "true"
spec:
  tls:
  - hosts:
    - api.myproject.com
    secretName: myproject-tls
  rules:
  - host: api.myproject.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: myproject-service
            port:
              number: 80
```

### ConfigMap e Secrets
```yaml
# ✅ PADRÃO - k8s/configmap.yml
apiVersion: v1
kind: ConfigMap
metadata:
  name: myproject-config
data:
  application.properties: |
    # Database
    quarkus.datasource.db-kind=postgresql
    quarkus.datasource.jdbc.url=jdbc:postgresql://postgres-service:5432/myproject
    
    # Observability
    quarkus.micrometer.enabled=true
    quarkus.micrometer.export.prometheus.enabled=true
    
    # Security
    quarkus.http.cors=true
    quarkus.http.cors.origins=https://myproject.com
    
    # Logging
    quarkus.log.level=INFO
    quarkus.log.category."com.myproject".level=DEBUG
---
apiVersion: v1
kind: Secret
metadata:
  name: db-credentials
type: Opaque
data:
  username: cG9zdGdyZXM=  # base64 encoded 'postgres'
  password: cGFzc3dvcmQ=  # base64 encoded 'password'
---
apiVersion: v1
kind: Secret
metadata:
  name: jwt-secret
type: Opaque
data:
  secret: bXlfc3VwZXJfc2VjcmV0X2p3dF9rZXk=  # base64 encoded JWT key
```

## Observabilidade

### Health Checks
```java
// ✅ PADRÃO - Custom health check
@ApplicationScoped
public class DatabaseHealthCheck implements HealthCheck {
    
    @Inject
    AgroalDataSource dataSource;
    
    @Override
    public HealthCheckResponse call() {
        try (var connection = dataSource.getConnection()) {
            var statement = connection.prepareStatement("SELECT 1");
            var resultSet = statement.executeQuery();
            
            if (resultSet.next() && resultSet.getInt(1) == 1) {
                return HealthCheckResponse.up("database");
            } else {
                return HealthCheckResponse.down("database");
            }
        } catch (Exception e) {
            return HealthCheckResponse.down("database", e.getMessage());
        }
    }
}

// ✅ PADRÃO - Custom readiness check
@Readiness
@ApplicationScoped
public class ExternalServiceReadiness implements HealthCheck {
    
    @RestClient
    PaymentServiceClient paymentService;
    
    @Override
    public HealthCheckResponse call() {
        try {
            var response = paymentService.healthCheck();
            return response.isHealthy() 
                ? HealthCheckResponse.up("payment-service")
                : HealthCheckResponse.down("payment-service");
        } catch (Exception e) {
            return HealthCheckResponse.down("payment-service", e.getMessage());
        }
    }
}
```

### Custom Metrics
```java
// ✅ PADRÃO - Métricas customizadas
@ApplicationScoped
public class UserMetrics {
    
    private final Counter userCreatedCounter;
    private final Timer userCreationTimer;
    private final Gauge activeUsersGauge;
    
    @Inject
    UserRepository userRepository;
    
    public UserMetrics(MeterRegistry meterRegistry) {
        this.userCreatedCounter = Counter.builder("users.created.total")
            .description("Total number of users created")
            .register(meterRegistry);
            
        this.userCreationTimer = Timer.builder("users.creation.duration")
            .description("Time taken to create a user")
            .register(meterRegistry);
            
        this.activeUsersGauge = Gauge.builder("users.active.count")
            .description("Number of active users")
            .register(meterRegistry, this, UserMetrics::getActiveUserCount);
    }
    
    public void recordUserCreated() {
        userCreatedCounter.increment();
    }
    
    public Timer.Sample startUserCreationTimer() {
        return Timer.start(userCreationTimer);
    }
    
    private double getActiveUserCount(UserMetrics self) {
        return userRepository.countByStatus(UserStatus.ACTIVE);
    }
}

// ✅ USO - No Use Case
@ApplicationScoped
public class CreateUserUseCase {
    
    @Inject
    UserMetrics userMetrics;
    
    public CreateUserResult execute(CreateUserCommand command) {
        var timerSample = userMetrics.startUserCreationTimer();
        
        try {
            // Lógica de criação...
            var user = createUser(command);
            
            userMetrics.recordUserCreated();
            return new CreateUserResult.Success(user);
            
        } finally {
            timerSample.stop();
        }
    }
}
```

### Distributed Tracing
```java
// ✅ PADRÃO - Tracing manual quando necessário
@ApplicationScoped
public class OrderService {
    
    @Inject
    @Tracer
    io.opentracing.Tracer tracer;
    
    @Inject
    PaymentService paymentService;
    
    @Traced(operationName = "process-order")
    public ProcessOrderResult processOrder(Order order) {
        var span = tracer.activeSpan();
        span.setTag("order.id", order.id().value().toString());
        span.setTag("order.value", order.totalValue().toString());
        
        try {
            // Span filho para pagamento
            try (var paymentSpan = tracer.buildSpan("process-payment")
                    .asChildOf(span)
                    .start()) {
                
                paymentSpan.setTag("payment.method", order.paymentMethod());
                var paymentResult = paymentService.processPayment(order);
                paymentSpan.setTag("payment.status", paymentResult.status());
                
                return new ProcessOrderResult.Success(order);
            }
            
        } catch (Exception e) {
            span.setTag("error", true);
            span.log(Map.of("error.message", e.getMessage()));
            throw e;
        }
    }
}
```

### Structured Logging
```java
// ✅ PADRÃO - Logging estruturado
@ApplicationScoped
public class StructuredLogger {
    
    private static final Logger LOG = LoggerFactory.getLogger(StructuredLogger.class);
    
    public void logUserCreated(User user) {
        LOG.info("User created successfully", 
            kv("userId", user.id().value()),
            kv("userEmail", maskEmail(user.email().value())),
            kv("timestamp", Instant.now()),
            kv("action", "USER_CREATED")
        );
    }
    
    public void logUserCreationFailed(CreateUserCommand command, Exception error) {
        LOG.error("Failed to create user",
            kv("email", maskEmail(command.email())),
            kv("error", error.getMessage()),
            kv("errorType", error.getClass().getSimpleName()),
            kv("timestamp", Instant.now()),
            kv("action", "USER_CREATION_FAILED")
        );
    }
    
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "***";
        
        var parts = email.split("@");
        return parts[0].substring(0, 1) + "***@" + parts[1];
    }
}
```

## Configuração de Ambiente

### application-prod.properties
```properties
# ✅ PADRÃO - Configuração de produção
# Database
quarkus.datasource.db-kind=postgresql
quarkus.datasource.jdbc.url=${DATABASE_URL}
quarkus.datasource.username=${DB_USERNAME}
quarkus.datasource.password=${DB_PASSWORD}
quarkus.datasource.jdbc.min-size=5
quarkus.datasource.jdbc.max-size=20

# HTTP
quarkus.http.port=8080
quarkus.http.host=0.0.0.0
quarkus.http.access-log.enabled=true

# Logging
quarkus.log.level=INFO
quarkus.log.category."com.myproject".level=INFO
quarkus.log.console.format=%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p [%c] (%t) %s%e%n
quarkus.log.console.json=true

# Metrics
quarkus.micrometer.enabled=true
quarkus.micrometer.export.prometheus.enabled=true
quarkus.micrometer.export.prometheus.path=/q/metrics

# Health
quarkus.smallrye-health.root-path=/q/health

# OpenAPI
quarkus.smallrye-openapi.path=/q/openapi
quarkus.swagger-ui.path=/q/swagger-ui

# Security
quarkus.http.cors=false  # Handled by ingress
quarkus.http.header.x-frame-options.value=DENY
quarkus.http.header.x-content-type-options.value=nosniff

# Native build optimizations
quarkus.native.resources.includes=**/*.properties,**/*.json,**/*.yml
quarkus.native.additional-build-args=-H:+ReportExceptionStackTraces
```

## Monitoring e Alerting

### Prometheus Rules
```yaml
# ✅ PADRÃO - monitoring/prometheus-rules.yml
apiVersion: monitoring.coreos.com/v1
kind: PrometheusRule
metadata:
  name: myproject-alerts
spec:
  groups:
  - name: myproject.rules
    rules:
    - alert: HighErrorRate
      expr: rate(http_server_requests_seconds_count{status=~"5.."}[5m]) > 0.1
      for: 2m
      labels:
        severity: critical
      annotations:
        summary: "High error rate detected"
        description: "Error rate is {{ $value }} errors per second"
    
    - alert: DatabaseConnectionFailure
      expr: up{job="myproject"} == 0
      for: 1m
      labels:
        severity: critical
      annotations:
        summary: "Database connection failed"
        description: "Application cannot connect to database"
    
    - alert: HighMemoryUsage
      expr: process_resident_memory_bytes / process_virtual_memory_max_bytes > 0.8
      for: 5m
      labels:
        severity: warning
      annotations:
        summary: "High memory usage"
        description: "Memory usage is above 80%"
    
    - alert: SlowResponseTime
      expr: histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[5m])) > 1
      for: 5m
      labels:
        severity: warning
      annotations:
        summary: "Slow response time"
        description: "95th percentile response time is {{ $value }}s"
```

### Grafana Dashboard
```json
{
  "dashboard": {
    "title": "MyProject Application Metrics",
    "panels": [
      {
        "title": "Request Rate",
        "type": "graph",
        "targets": [
          {
            "expr": "rate(http_server_requests_seconds_count[5m])",
            "legendFormat": "{{method}} {{uri}}"
          }
        ]
      },
      {
        "title": "Error Rate",
        "type": "graph", 
        "targets": [
          {
            "expr": "rate(http_server_requests_seconds_count{status=~\"5..\"}[5m])",
            "legendFormat": "5xx Errors"
          }
        ]
      },
      {
        "title": "Response Time",
        "type": "graph",
        "targets": [
          {
            "expr": "histogram_quantile(0.50, rate(http_server_requests_seconds_bucket[5m]))",
            "legendFormat": "p50"
          },
          {
            "expr": "histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[5m]))",
            "legendFormat": "p95"
          }
        ]
      }
    ]
  }
}
```

## Backup e Disaster Recovery

### Database Backup Script
```bash
#!/bin/bash
# ✅ PADRÃO - backup-database.sh

set -euo pipefail

# Configurações
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-myproject}"
DB_USER="${DB_USER:-postgres}"
BACKUP_DIR="${BACKUP_DIR:-/backups}"
RETENTION_DAYS="${RETENTION_DAYS:-7}"

# Timestamp para o backup
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="${BACKUP_DIR}/backup_${DB_NAME}_${TIMESTAMP}.sql.gz"

echo "Starting database backup at $(date)"

# Criar diretório se não existir
mkdir -p "${BACKUP_DIR}"

# Realizar backup
PGPASSWORD="${DB_PASSWORD}" pg_dump \
  --host="${DB_HOST}" \
  --port="${DB_PORT}" \
  --username="${DB_USER}" \
  --dbname="${DB_NAME}" \
  --format=custom \
  --no-owner \
  --no-privileges \
  --verbose | gzip > "${BACKUP_FILE}"

# Verificar se backup foi criado
if [[ -f "${BACKUP_FILE}" && -s "${BACKUP_FILE}" ]]; then
  echo "Backup created successfully: ${BACKUP_FILE}"
  echo "Backup size: $(du -h "${BACKUP_FILE}" | cut -f1)"
else
  echo "ERROR: Backup failed or is empty"
  exit 1
fi

# Limpar backups antigos
find "${BACKUP_DIR}" -name "backup_${DB_NAME}_*.sql.gz" -mtime +${RETENTION_DAYS} -delete

# Upload para S3 (opcional)
if [[ -n "${AWS_S3_BUCKET:-}" ]]; then
  aws s3 cp "${BACKUP_FILE}" "s3://${AWS_S3_BUCKET}/database-backups/"
  echo "Backup uploaded to S3"
fi

echo "Database backup completed at $(date)"
```

### CronJob para Backup
```yaml
# ✅ PADRÃO - k8s/backup-cronjob.yml
apiVersion: batch/v1
kind: CronJob
metadata:
  name: database-backup
spec:
  schedule: "0 2 * * *"  # Daily at 2 AM
  jobTemplate:
    spec:
      template:
        spec:
          containers:
          - name: backup
            image: postgres:15-alpine
            command: ["/bin/sh"]
            args:
            - -c
            - |
              pg_dump --host=$DB_HOST --username=$DB_USER --dbname=$DB_NAME --format=custom --no-owner --no-privileges | 
              gzip > /backup/backup_$(date +%Y%m%d_%H%M%S).sql.gz
            env:
            - name: PGPASSWORD
              valueFrom:
                secretKeyRef:
                  name: db-credentials
                  key: password
            - name: DB_HOST
              value: postgres-service
            - name: DB_USER
              valueFrom:
                secretKeyRef:
                  name: db-credentials
                  key: username
            - name: DB_NAME
              value: myproject
            volumeMounts:
            - name: backup-storage
              mountPath: /backup
          volumes:
          - name: backup-storage
            persistentVolumeClaim:
              claimName: backup-pvc
          restartPolicy: OnFailure
```

## Checklist DevOps

### ✅ Para cada Deploy
- [ ] Testes automatizados passando
- [ ] Security scan sem vulnerabilidades críticas
- [ ] Imagem Docker escaneada
- [ ] Health checks implementados
- [ ] Métricas e alertas configurados
- [ ] Backup testado e funcionando
- [ ] Rollback plan definido
- [ ] Documentação atualizada
- [ ] Logs estruturados implementados
- [ ] Limites de recursos definidos
````