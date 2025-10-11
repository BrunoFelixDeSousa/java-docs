# 🏗️ Quarkus - Documentação Completa

Framework Java moderno **supersônico** e **subatômico** para aplicações cloud-native.

> "Quarkus: Supersonic Subatomic Java" ⚡

---

## 🎯 Por que Quarkus?

- ✅ **Startup ultrarrápido** - Milissegundos vs segundos
- ✅ **Baixo consumo de memória** - ~15MB vs ~100MB+
- ✅ **Live Reload** - Produtividade máxima
- ✅ **GraalVM Native** - Compilação nativa AOT
- ✅ **Kubernetes-first** - Cloud-native por design
- ✅ **Developer Joy** - DX excepcional

---

## 📋 Índice

- [Por que Quarkus?](#-por-que-quarkus)
- [🗺️ Guia de Estudo - Ordem Recomendada](#-guia-de-estudo---ordem-recomendada)
  - [Fase 1: Fundamentos](#-fase-1-fundamentos-semana-1-2)
  - [Fase 2: Persistência e Validação](#-fase-2-persistência-e-validação-semana-3-4)
  - [Fase 3: Segurança](#-fase-3-segurança-semana-5-6)
  - [Fase 4: Programação Reativa](#-fase-4-programação-reativa-semana-7-8)
  - [Fase 5: Messaging e Eventos](#-fase-5-messaging-e-eventos-semana-9)
  - [Fase 6: DevOps e Observabilidade](#️-fase-6-devops-e-observabilidade-semana-10-11)
  - [Fase 7: Tópicos Avançados](#-fase-7-tópicos-avançados-semana-12)
- [🗺️ Trilhas Alternativas](#-trilhas-alternativas-de-estudo)
- [📊 Matriz de Pré-requisitos](#-matriz-de-pré-requisitos)
- [⏱️ Estimativa de Tempo](#️-estimativa-de-tempo-por-documento)
- [🎯 Checklist de Progresso](#-checklist-de-progresso)
- [💡 Dicas de Estudo](#-dicas-de-estudo)
- [Documentação por Categoria](#-documentação-por-categoria)
  - [Core Features](#️-core-features)
  - [Data & Persistence](#-data--persistence)
  - [Security & Auth](#-security--auth)
  - [Reactive & Messaging](#-reactive--messaging)
  - [DevOps & Cloud](#️-devops--cloud)
  - [AI & ML](#-ai--ml)
  - [Outras Features](#️-outras-features)
- [Recursos Adicionais](#-recursos-adicionais)

---

## 🚀 Início Rápido

**Novo no Quarkus?** Comece aqui:

1. 📖 Leia **[Por que Quarkus?](#-por-que-quarkus)**
2. 🗺️ Escolha sua **[Trilha de Estudo](#-guia-de-estudo---ordem-recomendada)**
3. ✅ Marque seu progresso no **[Checklist](#-checklist-de-progresso)**
4. 🎯 Siga a **[Ordem Recomendada](#-fase-1-fundamentos-semana-1-2)**

---

## 🗺️ Mapa de Aprendizado

```
┌────────────────────────────────────────────────────────────────┐
│                    JORNADA QUARKUS                             │
│                    (12+ semanas)                               │
└────────────────────────────────────────────────────────────────┘

    🎯 FASE 1: FUNDAMENTOS (Semana 1-2)
    ┌──────────────────────────────────┐
    │  ├─ Quarkus.md                   │
    │  ├─ getting-started.md           │
    │  ├─ annotations.md               │
    │  ├─ cdi-e-injecao.md             │
    │  ├─ configuracoes.md             │
    │  └─ logging.md                   │
    └──────────────────────────────────┘
              │
              ▼
    💾 FASE 2: PERSISTÊNCIA (Semana 3-4)
    ┌──────────────────────────────────┐
    │  ├─ panache.md                   │
    │  ├─ validator.md                 │
    │  ├─ cache.md                     │
    │  └─ redis.md                     │
    └──────────────────────────────────┘
              │
              ▼
    🔒 FASE 3: SEGURANÇA (Semana 5-6)
    ┌──────────────────────────────────┐
    │  ├─ auth.md                      │
    │  ├─ microprofile.md              │
    │  └─ keycloak.md ⭐               │
    └──────────────────────────────────┘
              │
              ▼
    ⚡ FASE 4: REATIVO (Semana 7-8)
    ┌──────────────────────────────────┐
    │  ├─ programação-reativa.md       │
    │  ├─ Mutiny.md ⭐                 │
    │  ├─ rest-client.md               │
    │  └─ padrao-RESTful.md            │
    └──────────────────────────────────┘
              │
              ▼
    📨 FASE 5: MESSAGING (Semana 9)
    ┌──────────────────────────────────┐
    │  ├─ kafka.md ⭐                  │
    │  └─ scheduling.md                │
    └──────────────────────────────────┘
              │
              ▼
    🛠️ FASE 6: DEVOPS (Semana 10-11)
    ┌──────────────────────────────────┐
    │  ├─ kubernates-docker.md ⭐      │
    │  ├─ testes.md                    │
    │  └─ kibana-e-observabilidade.md  │
    └──────────────────────────────────┘
              │
              ▼
    🎓 FASE 7: AVANÇADO (Semana 12+)
    ┌──────────────────────────────────┐
    │  ├─ annotations-personalizadas   │
    │  ├─ extensoes.md                 │
    │  ├─ kotlin.md                    │
    │  ├─ advanced.md                  │
    │  └─ langchain4j.md               │
    └──────────────────────────────────┘
              │
              ▼
    🏆 PROJETO FINAL: Microserviço Completo
    ┌──────────────────────────────────────────────────────┐
    │  ✅ REST API + GraphQL                               │
    │  ✅ PostgreSQL + Redis + Kafka                       │
    │  ✅ Keycloak Auth + JWT                              │
    │  ✅ Reactive Endpoints                               │
    │  ✅ Docker + Kubernetes                              │
    │  ✅ Metrics + Health Checks + Tracing                │
    │  ✅ Testes (Unit + Integration)                      │
    └──────────────────────────────────────────────────────┘

⭐ = Documentos mais complexos (reserve mais tempo)
```

---

## 📚 Documentação Principal

### Guias Fundamentais
📁 **[../frameworks/](../frameworks/)**

| Arquivo | Descrição | Status |
|---------|-----------|--------|
| [Quarkus.md](../frameworks/Quarkus.md) | Guia fundamental do Quarkus | ✅ |
| [quarkus-v2.md](../frameworks/quarkus-v2.md) | Versão atualizada | ✅ |

---

## ⚙️ Core Features

### 1. Annotations
📄 **[annotations.md](./annotations.md)**

Todas as annotations do Quarkus organizadas por categoria:

**REST**:
```java
@Path("/api/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoResource {
    
    @GET
    @Path("/{id}")
    public Produto getById(@PathParam("id") Long id) {
        return produtoService.findById(id);
    }
    
    @POST
    @Transactional
    public Response create(@Valid Produto produto) {
        produtoService.create(produto);
        return Response.status(201).entity(produto).build();
    }
}
```

---

### 2. Annotations Personalizadas
📄 **[annotations-personalizadas.md](./annotations-personalizadas.md)**  
📄 **[annotations-personalizadas-refined.md](./annotations-personalizadas-refined.md)**

Criar suas próprias annotations customizadas:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@InterceptorBinding
public @interface Logged {
    LogLevel value() default LogLevel.INFO;
}

@Logged
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor {
    @AroundInvoke
    public Object log(InvocationContext context) throws Exception {
        System.out.println("Executing: " + context.getMethod().getName());
        return context.proceed();
    }
}
```

---

### 3. CDI & Injeção de Dependência
📄 **[cdi-e-injecao.md](./cdi-e-injecao.md)**

**Escopos**:
- `@ApplicationScoped` - Singleton
- `@RequestScoped` - Por request
- `@SessionScoped` - Por sessão
- `@Dependent` - Por injeção

```java
@ApplicationScoped
public class ProdutoService {
    
    @Inject
    ProdutoRepository repository;
    
    public List<Produto> findAll() {
        return repository.listAll();
    }
}
```

---

### 4. Configurações
📄 **[configuracoes.md](./configuracoes.md)**

**application.properties**:
```properties
# Database
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=user
quarkus.datasource.password=pass
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/mydb

# Hibernate
quarkus.hibernate-orm.database.generation=update

# HTTP
quarkus.http.port=8080
quarkus.http.cors=true

# Profiles
%dev.quarkus.http.port=8080
%prod.quarkus.http.port=80
```

**Injetar Configurações**:
```java
@ConfigProperty(name = "app.name")
String appName;

@ConfigProperty(name = "app.timeout", defaultValue = "30")
int timeout;
```

---

### 5. Logging
📄 **[logging.md](./logging.md)**

```java
import org.jboss.logging.Logger;

@ApplicationScoped
public class MyService {
    
    private static final Logger LOG = Logger.getLogger(MyService.class);
    
    public void doSomething() {
        LOG.info("Doing something");
        LOG.debug("Debug info");
        LOG.error("Error occurred", exception);
    }
}
```

**Configuração**:
```properties
quarkus.log.level=INFO
quarkus.log.category."com.myapp".level=DEBUG
quarkus.log.console.format=%d{HH:mm:ss} %-5p [%c{2.}] (%t) %s%e%n
```

---

## 💾 Data & Persistence

### 6. Panache ORM
📄 **[panache.md](./panache.md)**

ORM simplificado com Active Record ou Repository pattern:

**Active Record**:
```java
@Entity
public class Produto extends PanacheEntity {
    public String nome;
    public BigDecimal preco;
    
    // Métodos customizados
    public static List<Produto> findByNome(String nome) {
        return find("nome", nome).list();
    }
    
    public static List<Produto> findExpensive() {
        return find("preco > ?1", new BigDecimal("1000")).list();
    }
}

// Uso
List<Produto> produtos = Produto.listAll();
Produto produto = Produto.findById(1L);
produto.persist();
produto.delete();
```

**Repository Pattern**:
```java
@ApplicationScoped
public class ProdutoRepository implements PanacheRepository<Produto> {
    
    public List<Produto> findByNome(String nome) {
        return find("nome", nome).list();
    }
    
    public Produto findByIdOptional(Long id) {
        return findByIdOptional(id).orElseThrow();
    }
}
```

---

## 🔒 Security & Auth

### 7. Authentication & Authorization
📄 **[auth.md](./auth.md)**

**JWT**:
```java
@Path("/api/secure")
@RolesAllowed("user")
public class SecureResource {
    
    @Inject
    JsonWebToken jwt;
    
    @GET
    public String secure() {
        return "Hello " + jwt.getName();
    }
    
    @GET
    @Path("/admin")
    @RolesAllowed("admin")
    public String admin() {
        return "Admin area";
    }
}
```

**OAuth2/OIDC**:
```properties
quarkus.oidc.auth-server-url=https://auth.example.com/realms/quarkus
quarkus.oidc.client-id=myapp
quarkus.oidc.credentials.secret=secret
```

---

### 8. Validator
📄 **[validator.md](./validator.md)**

Bean Validation (JSR 380):

```java
public class Produto {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100)
    private String nome;
    
    @NotNull
    @Positive(message = "Preço deve ser positivo")
    private BigDecimal preco;
    
    @Email
    private String email;
    
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")
    private String cpf;
}

@POST
public Response create(@Valid Produto produto) {
    // Se inválido, retorna 400 automaticamente
    return Response.ok(produto).build();
}
```

---

## ⚡ Reactive & Messaging

### 9. Mutiny (Programação Reativa)
📄 **[Mutiny.md](./Mutiny.md)**

```java
@Inject
@RestClient
ProdutoService produtoService;

@GET
@Path("/reactive")
public Uni<List<Produto>> getProducts() {
    return produtoService.findAll()
        .onFailure().recoverWithItem(Collections.emptyList());
}

// Múltiplas chamadas paralelas
public Uni<Response> getAllData() {
    Uni<List<Produto>> produtos = produtoRepository.findAll();
    Uni<List<Usuario>> usuarios = usuarioRepository.findAll();
    
    return Uni.combine().all().unis(produtos, usuarios)
        .asTuple()
        .map(tuple -> Response.ok(Map.of(
            "produtos", tuple.getItem1(),
            "usuarios", tuple.getItem2()
        )).build());
}
```

---

### 10. Kafka
📄 **[kafka.md](./kafka.md)**

**Producer**:
```java
@ApplicationScoped
public class ProdutoProducer {
    
    @Channel("produtos-out")
    Emitter<Produto> emitter;
    
    public void send(Produto produto) {
        emitter.send(produto);
    }
}
```

**Consumer**:
```java
@ApplicationScoped
public class ProdutoConsumer {
    
    @Incoming("produtos-in")
    public void receive(Produto produto) {
        System.out.println("Received: " + produto.nome);
    }
}
```

**application.properties**:
```properties
mp.messaging.outgoing.produtos-out.connector=smallrye-kafka
mp.messaging.outgoing.produtos-out.topic=produtos
mp.messaging.outgoing.produtos-out.value.serializer=io.quarkus.kafka.client.serialization.JsonbSerializer

mp.messaging.incoming.produtos-in.connector=smallrye-kafka
mp.messaging.incoming.produtos-in.topic=produtos
mp.messaging.incoming.produtos-in.value.deserializer=io.quarkus.kafka.client.serialization.JsonbDeserializer
```

---

## 🛠️ DevOps & Cloud

### 11. Kubernetes & Docker
📄 **[kubernates-docker.md](./kubernates-docker.md)**

**Dockerfile**:
```dockerfile
FROM quay.io/quarkus/ubi-quarkus-native-image:21.3-java17 AS build
COPY --chown=quarkus:quarkus mvnw /code/mvnw
COPY --chown=quarkus:quarkus .mvn /code/.mvn
COPY --chown=quarkus:quarkus pom.xml /code/
USER quarkus
WORKDIR /code
RUN ./mvnw -B org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline
COPY src /code/src
RUN ./mvnw package -Pnative

FROM quay.io/quarkus/quarkus-micro-image:1.0
COPY --from=build /code/target/*-runner /application
EXPOSE 8080
CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
```

**Kubernetes Deployment**:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: produtos-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: produtos
  template:
    metadata:
      labels:
        app: produtos
    spec:
      containers:
      - name: produtos
        image: myapp/produtos:1.0
        ports:
        - containerPort: 8080
```

---

### 12. Observabilidade (Kibana, Metrics)
📄 **[kibana-e-observabilidade.md](./kibana-e-observabilidade.md)**

**Health Checks**:
```java
@ApplicationScoped
@Liveness
public class DatabaseLiveness implements HealthCheck {
    
    @Inject
    DataSource dataSource;
    
    @Override
    public HealthCheckResponse call() {
        try (Connection conn = dataSource.getConnection()) {
            return HealthCheckResponse.up("Database connection OK");
        } catch (SQLException e) {
            return HealthCheckResponse.down("Database connection failed");
        }
    }
}
```

**Metrics**:
```java
@Counted(name = "produtoCreatedCount", description = "Produtos criados")
@Timed(name = "produtoCreatedTimer", description = "Tempo de criação")
public void create(Produto produto) {
    repository.persist(produto);
}
```

---

### 13. Extensões
📄 **[extensoes.md](./extensoes.md)**

Criar extensões customizadas do Quarkus:

```bash
# Criar extensão
mvn io.quarkus:quarkus-maven-plugin:create-extension \
    -DextensionId=my-extension \
    -DextensionName="My Extension"
```

---

## 🤖 AI & ML

### 14. LangChain4j
📄 **[langchain4j.md](./langchain4j.md)**

Integração com LLMs (OpenAI, Ollama, etc.):

```java
@ApplicationScoped
public class AIService {
    
    @Inject
    @RegisterAiService
    ChatLanguageModel model;
    
    public String chat(String message) {
        return model.generate(message);
    }
}

// Interface declarativa
@RegisterAiService
public interface Assistant {
    
    @SystemMessage("You are a helpful assistant")
    String chat(@UserMessage String userMessage);
}
```

---

## ⚙️ Outras Features

### 15. REST Client
📄 **[rest-client.md](./rest-client.md)**

```java
@Path("/api")
@RegisterRestClient(configKey = "produto-api")
public interface ProdutoRestClient {
    
    @GET
    @Path("/produtos")
    List<Produto> findAll();
    
    @POST
    @Path("/produtos")
    Produto create(Produto produto);
}

// Uso
@Inject
@RestClient
ProdutoRestClient client;

public void test() {
    List<Produto> produtos = client.findAll();
}
```

---

### 16. Cache
📄 **[cache.md](./cache.md)**

```java
@ApplicationScoped
public class ProdutoService {
    
    @CacheResult(cacheName = "produto-cache")
    public Produto findById(Long id) {
        return repository.findById(id);
    }
    
    @CacheInvalidate(cacheName = "produto-cache")
    public void update(Produto produto) {
        repository.persist(produto);
    }
}
```

---

### 17. Redis
📄 **[redis.md](./redis.md)**

```java
@Inject
RedisClient redisClient;

public void set(String key, String value) {
    redisClient.set(Arrays.asList(key, value));
}

public String get(String key) {
    return redisClient.get(key).toString();
}
```

---

### 18. Scheduling
📄 **[scheduling.md](./scheduling.md)**

```java
@ApplicationScoped
public class TaskScheduler {
    
    @Scheduled(every = "10s")
    void everyTenSeconds() {
        System.out.println("Running every 10 seconds");
    }
    
    @Scheduled(cron = "0 0 * * * ?")
    void everyHour() {
        System.out.println("Running every hour");
    }
}
```

---

### 19. Testes
📄 **[testes.md](./testes.md)**

```java
@QuarkusTest
public class ProdutoResourceTest {
    
    @Test
    public void testGetAll() {
        given()
          .when().get("/api/produtos")
          .then()
             .statusCode(200)
             .body("$.size()", is(3));
    }
    
    @Test
    public void testCreate() {
        Produto produto = new Produto("Notebook", new BigDecimal("2000"));
        
        given()
          .contentType(ContentType.JSON)
          .body(produto)
          .when().post("/api/produtos")
          .then()
             .statusCode(201);
    }
}
```

---

### 20. RESTful Patterns
📄 **[padrao-RESTful.md](./padrao-RESTful.md)**

**Padrões REST**:
- ✅ Resource naming
- ✅ HTTP methods (GET, POST, PUT, DELETE)
- ✅ Status codes
- ✅ HATEOAS
- ✅ Pagination
- ✅ Filtering & Sorting
- ✅ Versioning

---

### 21. Programação Reativa
📄 **[programação-reativa.md](./programação-reativa.md)**

Conceitos e padrões de programação reativa com Mutiny.

---

## 🔗 Recursos Adicionais

### Documentação Oficial
- [Quarkus Guides](https://quarkus.io/guides/)
- [Quarkus Blog](https://quarkus.io/blog/)
- [Quarkus Extensions](https://quarkus.io/extensions/)
- [Quarkus GitHub](https://github.com/quarkusio/quarkus)

### Ferramentas
- [Quarkus CLI](https://quarkus.io/guides/cli-tooling)
- [Quarkus Dev UI](http://localhost:8080/q/dev) - Dev mode only
- [Code.quarkus.io](https://code.quarkus.io) - Project generator
- [Quarkus QuickStarts](https://github.com/quarkusio/quarkus-quickstarts)

### Comunidade
- [Quarkus Discussions](https://github.com/quarkusio/quarkus/discussions)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/quarkus)
- [Quarkus Zulip Chat](https://quarkusio.zulipchat.com/)
- [Twitter/X](https://twitter.com/quarkusio)

### Vídeos e Tutoriais
- [Quarkus Insights (YouTube)](https://www.youtube.com/playlist?list=PLsM3ZE5tGAVbMz1LJqc8L5LpnfxPPKloO)
- [Red Hat Developers](https://developers.redhat.com/topics/quarkus)

---

**📍 Você está aqui**: frameworks/quarkus/README.md  
**⬆️ Voltar para**: [📁 Repositório Principal](../README.md)

## 📚 Guia de Estudo - Ordem Recomendada

### 🎯 **FASE 1: Fundamentos** (Semana 1-2)

> **Objetivo**: Entender os conceitos básicos do Quarkus e criar aplicações simples

#### **Semana 1: Primeiros Passos**
1. **[Quarkus.md](../frameworks/Quarkus.md)** - Visão geral e filosofia
2. **[getting-started.md](./guides/getting-started.md)** - Setup e primeiro projeto
3. **[annotations.md](./core/annotations.md)** - Annotations fundamentais (REST, CDI)

**Projeto Prático**: API REST simples com endpoints GET/POST

#### **Semana 2: Core Features**
4. **[cdi-e-injecao.md](./core/cdi-e-injecao.md)** - Injeção de dependências e escopos
5. **[configuracoes.md](./core/configuracoes.md)** - application.properties e profiles
6. **[logging.md](./core/logging.md)** - Logs estruturados

**Projeto Prático**: Refatorar API com services, repositories e configurações

---

### 🚀 **FASE 2: Persistência e Validação** (Semana 3-4)

> **Objetivo**: Trabalhar com banco de dados e validações

#### **Semana 3: Database**
7. **[panache.md](./data/panache.md)** - ORM simplificado (Active Record + Repository)
8. **[validator.md](./security/validator.md)** - Bean Validation (JSR 380)

**Projeto Prático**: CRUD completo com validações e persistência PostgreSQL

#### **Semana 4: Cache e Performance**
9. **[cache.md](./data/cache.md)** - Cache local
10. **[redis.md](./data/redis.md)** - Cache distribuído

**Projeto Prático**: Adicionar cache ao CRUD anterior

---

### 🔒 **FASE 3: Segurança** (Semana 5-6)

> **Objetivo**: Proteger aplicações com autenticação e autorização

#### **Semana 5: Auth Básico**
11. **[auth.md](./security/auth.md)** - JWT, OIDC, Security annotations
12. **[microprofile.md](./core/microprofile.md)** - MicroProfile Config, JWT, Health

**Projeto Prático**: API protegida com JWT e roles

#### **Semana 6: Keycloak**
13. **[keycloak.md](./security/keycloak.md)** - Keycloak + Docker + Quarkus

**Projeto Prático**: Integração completa com Keycloak (login, logout, refresh token)

---

### ⚡ **FASE 4: Programação Reativa** (Semana 7-8)

> **Objetivo**: Aplicações assíncronas e não-bloqueantes

#### **Semana 7: Mutiny**
14. **[programação-reativa.md](./reactive/programação-reativa.md)** - Conceitos de reatividade
15. **[Mutiny.md](./reactive/Mutiny.md)** - Uni, Multi, operadores

**Projeto Prático**: Converter endpoints para reativo (Uni/Multi)

#### **Semana 8: REST Client Reativo**
16. **[rest-client.md](./integrations/rest-client.md)** - Consumir APIs externas
17. **[padrao-RESTful.md](./integrations/padrao-RESTful.md)** - Best practices REST

**Projeto Prático**: Gateway agregando múltiplas APIs externas

---

### 📨 **FASE 5: Messaging e Eventos** (Semana 9)

> **Objetivo**: Comunicação assíncrona entre serviços

18. **[kafka.md](./messaging/kafka.md)** - Kafka Producer/Consumer
19. **[scheduling.md](./integrations/scheduling.md)** - Jobs agendados

**Projeto Prático**: Sistema de eventos com Kafka (pedidos, notificações)

---

### 🛠️ **FASE 6: DevOps e Observabilidade** (Semana 10-11)

> **Objetivo**: Deploy, monitoramento e troubleshooting

#### **Semana 10: Containerização**
20. **[kubernates-docker.md](./devops/kubernates-docker.md)** - Docker, Docker Compose, K8s
21. **[testes.md](./testing/testes.md)** - Testes unitários, integração, mocks

**Projeto Prático**: Dockerizar aplicação completa (app + DB + Kafka + Keycloak)

#### **Semana 11: Observabilidade**
22. **[kibana-e-observabilidade.md](./devops/kibana-e-observabilidade.md)** - Metrics, Health Checks, Tracing

**Projeto Prático**: Dashboard Grafana com métricas da aplicação

---

### 🎓 **FASE 7: Tópicos Avançados** (Semana 12+)

> **Objetivo**: Features especializadas e otimizações

#### **Extensibilidade**
23. **[annotations-personalizadas.md](./core/annotations-personalizadas.md)** - Criar annotations customizadas
24. **[annotations-personalizadas-refined.md](./core/annotations-personalizadas-refined.md)** - Versão refinada
25. **[extensoes.md](./integrations/extensoes.md)** - Criar extensões Quarkus

#### **Linguagens Alternativas**
26. **[kotlin.md](./guides/kotlin.md)** - Quarkus com Kotlin
27. **[advanced.md](./guides/advanced.md)** - Tópicos avançados

#### **AI e Integrações**
28. **[langchain4j.md](./integrations/langchain4j.md)** - LLMs e IA com Quarkus

**Projeto Final**: Microserviço completo usando todas as técnicas aprendidas

---

## 🗺️ Trilhas Alternativas de Estudo

### 🏃 **Trilha Rápida** (4 semanas - Desenvolvedor Experiente)

**Semana 1**: Fundamentos (1-6)  
**Semana 2**: Persistência + Segurança (7-13)  
**Semana 3**: Reativo + Messaging (14-19)  
**Semana 4**: DevOps (20-22)

### 🎯 **Trilha Backend API** (6 semanas)

Foco: APIs REST seguras e escaláveis

1. Fundamentos (1-6)
2. Persistência (7-10)
3. Segurança (11-13)
4. REST Client + Patterns (16-17)
5. Testes (21)
6. Docker + Observabilidade (20, 22)

### 📊 **Trilha Event-Driven** (6 semanas)

Foco: Arquitetura orientada a eventos

1. Fundamentos (1-6)
2. Persistência (7-8)
3. Reativo (14-15)
4. Kafka (18-19)
5. Docker (20)
6. Observabilidade (22)

### 🤖 **Trilha AI/ML** (5 semanas)

Foco: Aplicações com IA

1. Fundamentos (1-6)
2. Persistência (7-8)
3. Segurança (11-12)
4. LangChain4j (28)
5. Deploy (20)

---

## 📊 Matriz de Pré-requisitos

```
┌─────────────────────────────────────────────────────────┐
│ LEGENDA: ✅ Obrigatório  🔶 Recomendado  ⬜ Opcional    │
└─────────────────────────────────────────────────────────┘

Documento                        │ Pré-requisitos
─────────────────────────────────┼───────────────────────────────
getting-started.md               │ (nenhum)
annotations.md                   │ getting-started ✅
cdi-e-injecao.md                 │ annotations ✅
configuracoes.md                 │ cdi-e-injecao ✅
panache.md                       │ cdi-e-injecao ✅
validator.md                     │ panache 🔶
auth.md                          │ validator 🔶
keycloak.md                      │ auth ✅
Mutiny.md                        │ annotations ✅
rest-client.md                   │ Mutiny 🔶
kafka.md                         │ Mutiny ✅
kubernates-docker.md             │ getting-started ✅
kibana-e-observabilidade.md      │ kubernates-docker ✅
testes.md                        │ panache ✅, auth 🔶
annotations-personalizadas.md    │ cdi-e-injecao ✅
extensoes.md                     │ annotations-personalizadas 🔶
kotlin.md                        │ getting-started ✅
langchain4j.md                   │ rest-client ✅
```

---

## ⏱️ Estimativa de Tempo por Documento

| Documento | Dificuldade | Tempo Estimado | Prática |
|-----------|-------------|----------------|---------|
| getting-started.md | ⭐ | 2-3h | 1h |
| annotations.md | ⭐ | 3-4h | 2h |
| cdi-e-injecao.md | ⭐⭐ | 4-5h | 3h |
| configuracoes.md | ⭐ | 2-3h | 1h |
| logging.md | ⭐ | 1-2h | 30min |
| panache.md | ⭐⭐ | 5-6h | 4h |
| validator.md | ⭐⭐ | 3-4h | 2h |
| cache.md | ⭐ | 2-3h | 1h |
| redis.md | ⭐⭐ | 3-4h | 2h |
| auth.md | ⭐⭐⭐ | 6-8h | 4h |
| microprofile.md | ⭐⭐⭐ | 6-8h | 4h |
| keycloak.md | ⭐⭐⭐⭐ | 8-10h | 6h |
| programação-reativa.md | ⭐⭐⭐ | 4-5h | 2h |
| Mutiny.md | ⭐⭐⭐⭐ | 8-10h | 6h |
| rest-client.md | ⭐⭐ | 3-4h | 2h |
| padrao-RESTful.md | ⭐⭐ | 3-4h | 2h |
| kafka.md | ⭐⭐⭐⭐ | 8-10h | 6h |
| scheduling.md | ⭐ | 2-3h | 1h |
| kubernates-docker.md | ⭐⭐⭐⭐ | 10-12h | 8h |
| kibana-e-observabilidade.md | ⭐⭐⭐ | 6-8h | 4h |
| testes.md | ⭐⭐⭐ | 6-8h | 4h |
| annotations-personalizadas.md | ⭐⭐⭐⭐ | 8-10h | 6h |
| extensoes.md | ⭐⭐⭐⭐⭐ | 12-15h | 10h |
| kotlin.md | ⭐⭐⭐ | 6-8h | 4h |
| advanced.md | ⭐⭐⭐⭐ | 8-10h | 6h |
| langchain4j.md | ⭐⭐⭐ | 6-8h | 4h |

**Total Estimado**: ~150-180 horas (teoria + prática)

---

## 🎯 Checklist de Progresso

Marque conforme for completando:

### **Fase 1: Fundamentos**
- [ ] Quarkus.md
- [ ] getting-started.md
- [ ] annotations.md
- [ ] cdi-e-injecao.md
- [ ] configuracoes.md
- [ ] logging.md

### **Fase 2: Persistência**
- [ ] panache.md
- [ ] validator.md
- [ ] cache.md
- [ ] redis.md

### **Fase 3: Segurança**
- [ ] auth.md
- [ ] microprofile.md
- [ ] keycloak.md

### **Fase 4: Reativo**
- [ ] programação-reativa.md
- [ ] Mutiny.md
- [ ] rest-client.md
- [ ] padrao-RESTful.md

### **Fase 5: Messaging**
- [ ] kafka.md
- [ ] scheduling.md

### **Fase 6: DevOps**
- [ ] kubernates-docker.md
- [ ] kibana-e-observabilidade.md
- [ ] testes.md

### **Fase 7: Avançado**
- [ ] annotations-personalizadas.md
- [ ] annotations-personalizadas-refined.md
- [ ] extensoes.md
- [ ] kotlin.md
- [ ] advanced.md
- [ ] langchain4j.md

---

## 💡 Dicas de Estudo

### ✅ **Boas Práticas**
1. **Não pule etapas** - Siga a ordem recomendada
2. **Pratique sempre** - Código > Teoria
3. **Crie projetos** - Um projeto por fase
4. **Revise constantemente** - Volte aos fundamentos
5. **Use o Dev UI** - `http://localhost:8080/q/dev`

### ⚡ **Recursos Complementares**
- **Quarkus Guides**: https://quarkus.io/guides/
- **Quarkus Blog**: https://quarkus.io/blog/
- **YouTube**: Quarkus Insights
- **Discord**: Quarkus Community

### 🚫 **Erros Comuns**
- ❌ Pular fundamentos (CDI, annotations)
- ❌ Não praticar com código real
- ❌ Ignorar testes
- ❌ Não usar Docker desde cedo
- ❌ Esquecer de configurar profiles (dev/prod)

---

## � Documentação por Categoria

> **Nota**: Esta seção organiza todas as documentações por categoria. Para ordem de estudo, veja [Guia de Estudo](#-guia-de-estudo---ordem-recomendada).

---

## � Documentação Principal
