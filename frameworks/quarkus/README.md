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

- [Documentação Principal](#-documentação-principal)
- [Core Features](#️-core-features)
- [Data & Persistence](#-data--persistence)
- [Security & Auth](#-security--auth)
- [Reactive & Messaging](#-reactive--messaging)
- [DevOps & Cloud](#️-devops--cloud)
- [AI & ML](#-ai--ml)
- [Guia de Estudo](#-guia-de-estudo)

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

## 📚 Guia de Estudo

### Iniciantes (2-3 semanas)
1. **Dia 1-3**: [Quarkus.md](../frameworks/Quarkus.md) - Introdução
2. **Dia 4-7**: [annotations.md](./annotations.md) + [cdi-e-injecao.md](./cdi-e-injecao.md)
3. **Dia 8-10**: [panache.md](./panache.md) - ORM
4. **Dia 11-14**: [configuracoes.md](./configuracoes.md) + [logging.md](./logging.md)
5. **Dia 15-21**: Projeto prático simples

### Intermediários (3-4 semanas)
1. **Semana 1**: [auth.md](./auth.md) + [validator.md](./validator.md)
2. **Semana 2**: [rest-client.md](./rest-client.md) + [cache.md](./cache.md)
3. **Semana 3**: [Mutiny.md](./Mutiny.md) - Programação reativa
4. **Semana 4**: [kafka.md](./kafka.md) - Event streaming

### Avançados (2-3 semanas)
1. **Semana 1**: [kubernates-docker.md](./kubernates-docker.md) + [kibana-e-observabilidade.md](./kibana-e-observabilidade.md)
2. **Semana 2**: [extensoes.md](./extensoes.md) + [annotations-personalizadas.md](./annotations-personalizadas.md)
3. **Semana 3**: [langchain4j.md](./langchain4j.md) - IA

---

## 🔗 Recursos Adicionais

### Documentação Oficial
- [Quarkus Guides](https://quarkus.io/guides/)
- [Quarkus Blog](https://quarkus.io/blog/)
- [Quarkus Extensions](https://quarkus.io/extensions/)

### Ferramentas
- [Quarkus CLI](https://quarkus.io/guides/cli-tooling)
- [Quarkus Dev UI](http://localhost:8080/q/dev) - Dev mode only
- [Code.quarkus.io](https://code.quarkus.io) - Project generator

---

**Voltar para**: [📁 Repositório Principal](../README.md)
