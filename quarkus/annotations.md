# 📋 Guia Completo de Annotations do Quarkus

> Referência abrangente de todas as annotations do Quarkus com exemplos práticos e casos de uso reais.

## Índice
1. [Injeção de Dependência e CDI](#1--injeção-de-dependência-e-cdi)
2. [REST e JAX-RS](#2--rest-e-jax-rs)
3. [Configuração](#3--configuração)
4. [Persistência](#4--persistência-hibernatepanache)
5. [Segurança](#5--segurança)
6. [Observabilidade](#6--observabilidade)
7. [Processamento Assíncrono](#7--processamento-assíncrono)
8. [Messaging](#8--messaging)
9. [Testes](#9--testes)
10. [Transações](#10--transações)
11. [Agendamento](#11--agendamento)
12. [Build e Deploy](#12--build-e-deploy)
13. [Interceptadores e Eventos](#13--interceptadores-e-eventos)
14. [Reactive](#14--reactive)
15. [Extensões Específicas](#15--extensões-específicas)

---

## 1. 🔧 **Injeção de Dependência e CDI**

### Escopos de Beans

| Annotation           | Ciclo de Vida                                 | Thread-Safe | Quando Usar                                      | Exemplo                          |
| -------------------- | --------------------------------------------- | ----------- | ------------------------------------------------ | -------------------------------- |
| `@ApplicationScoped` | Uma instância por aplicação (singleton lazy)  | ✅ Sim      | Serviços stateless, DAOs, configurações          | Serviço de negócio               |
| `@RequestScoped`     | Uma instância por requisição HTTP             | ✅ Sim      | Dados específicos da request, contexto de pedido | Carrinho temporário              |
| `@SessionScoped`     | Uma instância por sessão de usuário (HTTP)    | ✅ Sim      | Estado do usuário logado, preferências           | Carrinho de compras persistente  |
| `@Dependent`         | Nova instância a cada injeção (default)       | ⚠️ Depende  | Objetos sem estado compartilhado, DTOs           | Value Objects, builders          |
| `@Singleton`         | Uma instância eager (criada na inicialização) | ⚠️ Não      | Configurações críticas, cache global             | Pool de conexões, configurações  |
| `@ConversationScoped`| Uma instância por conversação (JSF)           | ✅ Sim      | Fluxos multi-etapas (wizard)                     | Checkout multi-página            |

```java
// @ApplicationScoped - Serviço stateless (RECOMENDADO)
@ApplicationScoped
public class UserService {
    @Inject EntityManager em;
    
    public User findById(Long id) {
        return em.find(User.class, id);
    }
}

// @RequestScoped - Dados da requisição atual
@RequestScoped
public class RequestContext {
    private String requestId;
    private LocalDateTime timestamp;
    
    @PostConstruct
    void init() {
        this.requestId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }
}

// @Singleton - Inicialização eager
@Singleton
@Startup  // Força criação no startup
public class AppInitializer {
    @Inject Logger log;
    
    @PostConstruct
    void init() {
        log.info("Aplicação iniciada!");
    }
}

// @Dependent - Nova instância sempre
@Dependent
public class OrderBuilder {
    private List<Item> items = new ArrayList<>();
    
    public OrderBuilder addItem(Item item) {
        items.add(item);
        return this;
    }
    
    public Order build() {
        return new Order(items);
    }
}
```

### Injeção de Dependências

| Annotation     | Descrição                            | Uso                                         | Exemplo                            |
| -------------- | ------------------------------------ | ------------------------------------------- | ---------------------------------- |
| `@Inject`      | Injeta dependência via CDI           | Campos, construtores, métodos               | `@Inject DatabaseService service;` |
| `@Named`       | Define nome do bean para qualificação| Múltiplas implementações                    | `@Named("mysql")`                  |
| `@Qualifier`   | Cria anotação qualificadora          | Tipagem forte para injeção                  | `@Database(Type.MYSQL)`            |
| `@Default`     | Qualificador padrão                  | Implementação default quando há alternativas| Aplicado automaticamente           |
| `@Alternative`  | Bean alternativo (ativado no config) | Implementação para testes, ambientes        | Impl de teste                      |
| `@Produces`    | Produz beans via factory method      | Criação complexa, third-party objects       | Factory de EntityManager           |
| `@Disposes`    | Cleanup de beans produzidos          | Fechar conexões, liberar recursos           | Cleanup de conexões                |
| `@Vetoed`      | Impede bean de ser gerenciado por CDI| Classes utilitárias, POJOs simples          | DTO sem CDI                        |

```java
// Injeção básica
@ApplicationScoped
public class OrderService {
    @Inject UserService userService;        // Injeção por campo
    @Inject EmailService emailService;
    
    // Injeção por construtor (RECOMENDADO)
    private final PaymentService paymentService;
    
    @Inject
    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}

// Qualificadores personalizados
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
public @interface Database {
    Type value();
    enum Type { MYSQL, POSTGRES, MONGODB }
}

@ApplicationScoped
@Database(Database.Type.MYSQL)
public class MySQLUserRepository implements UserRepository {
    // Implementação MySQL
}

@ApplicationScoped
@Database(Database.Type.POSTGRES)
public class PostgresUserRepository implements UserRepository {
    // Implementação Postgres
}

// Injeção com qualificador
@ApplicationScoped
public class UserService {
    @Inject
    @Database(Database.Type.MYSQL)
    UserRepository repository;
}

// Producer methods
@ApplicationScoped
public class DatabaseProducer {
    
    @Produces
    @ApplicationScoped
    public EntityManager produceEntityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
        return emf.createEntityManager();
    }
    
    public void close(@Disposes EntityManager em) {
        if (em.isOpen()) {
            em.close();
        }
    }
    
    // Producer com qualificador
    @Produces
    @Named("maxRetries")
    public Integer produceMaxRetries() {
        return 3;
    }
}

// Alternativas (ativadas em application.properties)
@Alternative
@Priority(1)
@ApplicationScoped
public class MockEmailService implements EmailService {
    @Override
    public void send(String to, String subject, String body) {
        System.out.println("MOCK: Enviando email para " + to);
    }
}
```

## 2. 🌐 **REST e JAX-RS**

### Recursos REST

| Annotation | Método HTTP | Descrição                      | Idempotente | Safe | Exemplo de Uso           |
| ---------- | ----------- | ------------------------------ | ----------- | ---- | ------------------------ |
| `@Path`    | -           | Define caminho do recurso/método| -          | -    | `@Path("/usuarios")`     |
| `@GET`     | GET         | Buscar/listar recursos         | ✅ Sim      | ✅ Sim| Consultas, listagens     |
| `@POST`    | POST        | Criar novo recurso             | ❌ Não      | ❌ Não| Criar usuário, pedido    |
| `@PUT`     | PUT         | Substituir recurso completo    | ✅ Sim      | ❌ Não| Atualizar usuário        |
| `@DELETE`  | DELETE      | Remover recurso                | ✅ Sim      | ❌ Não| Deletar usuário          |
| `@PATCH`   | PATCH       | Atualizar parcialmente         | ❌ Não      | ❌ Não| Atualizar apenas email   |
| `@HEAD`    | HEAD        | Apenas headers (sem body)      | ✅ Sim      | ✅ Sim| Verificar existência     |
| `@OPTIONS` | OPTIONS     | Capabilities, CORS preflight   | ✅ Sim      | ✅ Sim| CORS, descoberta de API  |

```java
// Recurso REST completo
@Path("/api/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class UsuarioResource {
    
    @Inject UserService service;
    
    // GET - Listar todos (com paginação)
    @GET
    @Operation(summary = "Lista todos os usuários")
    public Response listar(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        
        List<User> users = service.findAll(page, size);
        long total = service.count();
        
        return Response.ok(users)
                .header("X-Total-Count", total)
                .build();
    }
    
    // GET - Buscar por ID
    @GET
    @Path("/{id}")
    @Operation(summary = "Busca usuário por ID")
    @APIResponse(responseCode = "200", description = "Usuário encontrado")
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    public Response buscar(@PathParam("id") Long id) {
        return service.findById(id)
                .map(user -> Response.ok(user).build())
                .orElse(Response.status(Status.NOT_FOUND).build());
    }
    
    // POST - Criar novo
    @POST
    @Operation(summary = "Cria novo usuário")
    @APIResponse(responseCode = "201", description = "Usuário criado")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    public Response criar(@Valid UsuarioDTO dto, @Context UriInfo uriInfo) {
        User user = service.create(dto);
        
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(user.getId().toString())
                .build();
        
        return Response.created(location).entity(user).build();
    }
    
    // PUT - Atualizar completo
    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualiza usuário completamente")
    public Response atualizar(@PathParam("id") Long id, @Valid UsuarioDTO dto) {
        return service.update(id, dto)
                .map(user -> Response.ok(user).build())
                .orElse(Response.status(Status.NOT_FOUND).build());
    }
    
    // PATCH - Atualizar parcial
    @PATCH
    @Path("/{id}")
    @Operation(summary = "Atualiza usuário parcialmente")
    public Response atualizarParcial(@PathParam("id") Long id, Map<String, Object> updates) {
        return service.partialUpdate(id, updates)
                .map(user -> Response.ok(user).build())
                .orElse(Response.status(Status.NOT_FOUND).build());
    }
    
    // DELETE - Remover
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Remove usuário")
    @APIResponse(responseCode = "204", description = "Usuário removido")
    @APIResponse(responseCode = "404", description = "Usuário não encontrado")
    public Response deletar(@PathParam("id") Long id) {
        boolean deleted = service.delete(id);
        return deleted 
                ? Response.noContent().build()
                : Response.status(Status.NOT_FOUND).build();
    }
    
    // HEAD - Verificar existência
    @HEAD
    @Path("/{id}")
    public Response existe(@PathParam("id") Long id) {
        return service.exists(id)
                ? Response.ok().build()
                : Response.status(Status.NOT_FOUND).build();
    }
    
    // OPTIONS - CORS e capabilities
    @OPTIONS
    public Response options() {
        return Response.ok()
                .header("Allow", "GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS")
                .build();
    }
}
```

### Parâmetros de Requisição

| Annotation                       | Origem   | Fonte               | Conversão Automática | Validação | Exemplo de Uso                                            |
| -------------------------------- | -------- | ------------------- | -------------------- | --------- | --------------------------------------------------------- |
| `@PathParam`                     | JAX-RS   | Segmento da URL     | ✅ Sim               | ❌ Não    | `@PathParam("id") Long id`                                |
| `@QueryParam`                    | JAX-RS   | Query string        | ✅ Sim               | ❌ Não    | `@QueryParam("nome") String nome`                         |
| `@FormParam`                     | JAX-RS   | Form URL-encoded    | ✅ Sim               | ❌ Não    | `@FormParam("email") String email`                        |
| `@HeaderParam`                   | JAX-RS   | Cabeçalho HTTP      | ✅ Sim               | ❌ Não    | `@HeaderParam("Authorization") String token`              |
| `@CookieParam`                   | JAX-RS   | Cookie              | ✅ Sim               | ❌ Não    | `@CookieParam("sessionId") String session`                |
| `@MatrixParam`                   | JAX-RS   | URI matrix params   | ✅ Sim               | ❌ Não    | `@MatrixParam("version") String version`                  |
| `@BeanParam`                     | JAX-RS   | Múltiplas fontes    | ✅ Sim               | ✅ Sim    | `@BeanParam UserFilter filter`                            |
| `@Context`                       | JAX-RS   | Contexto JAX-RS     | ❌ Não               | ❌ Não    | `@Context UriInfo uriInfo`                                |
| `@DefaultValue`                  | JAX-RS   | Valor padrão        | ✅ Sim               | ❌ Não    | `@DefaultValue("10") @QueryParam("size") int size`        |
| **Extensões RESTEasy / Quarkus** |          |                     |                      |           |                                                           |
| `@RestPath`                      | Quarkus  | Segmento da URL     | ✅ Sim               | ✅ Sim    | `@RestPath Long id`                                       |
| `@RestQuery`                     | Quarkus  | Query string        | ✅ Sim               | ✅ Sim    | `@RestQuery String nome`                                  |
| `@RestHeader`                    | Quarkus  | Cabeçalho HTTP      | ✅ Sim               | ✅ Sim    | `@RestHeader("X-App-Version") String version`             |
| `@RestCookie`                    | Quarkus  | Cookie              | ✅ Sim               | ✅ Sim    | `@RestCookie("sessionId") String session`                 |
| `@RestForm`                      | Quarkus  | Form/Multipart      | ✅ Sim               | ✅ Sim    | `@RestForm String email` / `@RestForm FileUpload arquivo` |
| `@RestMatrix`                    | Quarkus  | URI matrix params   | ✅ Sim               | ✅ Sim    | `@RestMatrix String version`                              |

```java
// Exemplo completo de parâmetros
@Path("/api/produtos")
public class ProdutoResource {
    
    // PathParam - ID na URL
    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        // GET /api/produtos/123
        return Response.ok().build();
    }
    
    // QueryParam - Filtros e paginação
    @GET
    public Response listar(
            @QueryParam("categoria") String categoria,
            @QueryParam("precoMin") @DefaultValue("0") BigDecimal precoMin,
            @QueryParam("precoMax") BigDecimal precoMax,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("sort") @DefaultValue("nome") String sort) {
        // GET /api/produtos?categoria=eletronicos&precoMin=100&precoMax=1000&page=0&size=10
        return Response.ok().build();
    }
    
    // HeaderParam - Autenticação e metadados
    @GET
    @Path("/secret")
    public Response dadosProtegidos(
            @HeaderParam("Authorization") String authToken,
            @HeaderParam("X-Request-ID") String requestId,
            @HeaderParam("Accept-Language") @DefaultValue("pt-BR") String language) {
        // Authorization: Bearer xxx
        // X-Request-ID: abc-123
        return Response.ok().build();
    }
    
    // CookieParam - Sessão
    @GET
    @Path("/carrinho")
    public Response verCarrinho(
            @CookieParam("JSESSIONID") String sessionId,
            @CookieParam("preferencias") String preferencias) {
        // Cookie: JSESSIONID=xxx; preferencias=dark-mode
        return Response.ok().build();
    }
    
    // FormParam - Formulário URL-encoded
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("remember") @DefaultValue("false") boolean remember) {
        // POST application/x-www-form-urlencoded
        // username=admin&password=123&remember=true
        return Response.ok().build();
    }
    
    // MatrixParam - Parâmetros na URL
    @GET
    @Path("/filtrar/{categoria}")
    public Response filtrarComMatrix(
            @PathParam("categoria") String categoria,
            @MatrixParam("cor") String cor,
            @MatrixParam("tamanho") String tamanho) {
        // GET /api/produtos/filtrar/camisetas;cor=azul;tamanho=M
        return Response.ok().build();
    }
    
    // BeanParam - Agrupa múltiplos parâmetros
    @GET
    @Path("/busca-avancada")
    public Response buscaAvancada(@BeanParam ProdutoFilter filter) {
        // Usa todos os parâmetros definidos em ProdutoFilter
        return Response.ok().build();
    }
    
    // Context - Informações de contexto
    @GET
    @Path("/info")
    public Response info(
            @Context UriInfo uriInfo,
            @Context HttpHeaders headers,
            @Context SecurityContext securityContext,
            @Context Request request) {
        
        String baseUri = uriInfo.getBaseUri().toString();
        String userAgent = headers.getHeaderString("User-Agent");
        String username = securityContext.getUserPrincipal().getName();
        
        return Response.ok().build();
    }
    
    // RestForm - Multipart file upload (Quarkus)
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(
            @RestForm("file") FileUpload file,
            @RestForm("description") String description,
            @RestForm("tags") List<String> tags) {
        // Multipart form com arquivo e campos
        return Response.ok().build();
    }
    
    // Combinação: RestPath e RestQuery (Quarkus idiomático)
    @GET
    @Path("/{categoria}/buscar")
    public Response buscarQuarkus(
            @RestPath String categoria,
            @RestQuery String nome,
            @RestQuery @DefaultValue("0") int page) {
        // GET /api/produtos/eletronicos/buscar?nome=notebook&page=0
        return Response.ok().build();
    }
}

// BeanParam - DTO de filtros
public class ProdutoFilter {
    
    @QueryParam("nome")
    private String nome;
    
    @QueryParam("categoria")
    private String categoria;
    
    @QueryParam("precoMin")
    @DefaultValue("0")
    private BigDecimal precoMin;
    
    @QueryParam("precoMax")
    private BigDecimal precoMax;
    
    @QueryParam("emEstoque")
    @DefaultValue("true")
    private boolean emEstoque;
    
    @QueryParam("page")
    @DefaultValue("0")
    private int page;
    
    @QueryParam("size")
    @DefaultValue("20")
    private int size;
    
    @HeaderParam("X-User-ID")
    private Long userId;
    
    // Getters e setters
}
```

### Conteúdo e Negociação de Mídia

| Annotation  | Nível            | Descrição                      | Media Types Comuns                    | Exemplo                                 |
| ----------- | ---------------- | ------------------------------ | ------------------------------------- | --------------------------------------- |
| `@Consumes` | Classe ou Método | Tipo de mídia que aceita       | JSON, XML, Form, Multipart            | `@Consumes(MediaType.APPLICATION_JSON)` |
| `@Produces` | Classe ou Método | Tipo de mídia que produz       | JSON, XML, Plain Text, HTML           | `@Produces(MediaType.APPLICATION_JSON)` |
| `@Provider` | Classe           | Registra provider customizado  | MessageBodyReader/Writer, ExceptionMapper | `@Provider public class...`          |

```java
// Content negotiation completa
@Path("/api/relatorios")
public class RelatorioResource {
    
    // Produz JSON (padrão)
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Relatorio getJson(@PathParam("id") Long id) {
        return relatorioService.find(id);
    }
    
    // Produz XML
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Relatorio getXml(@PathParam("id") Long id) {
        return relatorioService.find(id);
    }
    
    // Produz múltiplos formatos (content negotiation)
    @GET
    @Path("/{id}/export")
    @Produces({
        MediaType.APPLICATION_JSON,
        MediaType.APPLICATION_XML,
        "application/pdf",
        "text/csv"
    })
    public Response export(@PathParam("id") Long id, @HeaderParam("Accept") String accept) {
        Relatorio relatorio = relatorioService.find(id);
        
        // Retorna formato baseado no Accept header
        if (accept.contains("pdf")) {
            byte[] pdf = pdfGenerator.generate(relatorio);
            return Response.ok(pdf, "application/pdf").build();
        } else if (accept.contains("csv")) {
            String csv = csvGenerator.generate(relatorio);
            return Response.ok(csv, "text/csv").build();
        }
        // JAX-RS escolhe automaticamente JSON ou XML
        return Response.ok(relatorio).build();
    }
    
    // Aceita JSON e XML
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Response criar(Relatorio relatorio) {
        // Aceita ambos formatos no body
        Relatorio saved = relatorioService.save(relatorio);
        return Response.status(Status.CREATED).entity(saved).build();
    }
    
    // Formulário URL-encoded
    @POST
    @Path("/simples")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarSimples(
            @FormParam("titulo") String titulo,
            @FormParam("descricao") String descricao) {
        Relatorio relatorio = new Relatorio(titulo, descricao);
        return Response.ok(relatorio).build();
    }
    
    // Multipart form (upload de arquivo)
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(@RestForm("file") FileUpload file) {
        // Processa arquivo enviado
        return Response.ok().build();
    }
    
    // Texto plano
    @GET
    @Path("/{id}/descricao")
    @Produces(MediaType.TEXT_PLAIN)
    public String getDescricao(@PathParam("id") Long id) {
        return relatorioService.find(id).getDescricao();
    }
    
    // HTML
    @GET
    @Path("/{id}/view")
    @Produces(MediaType.TEXT_HTML)
    public String viewHtml(@PathParam("id") Long id) {
        Relatorio rel = relatorioService.find(id);
        return "<html><body><h1>" + rel.getTitulo() + "</h1></body></html>";
    }
    
    // Octet stream (download binário)
    @GET
    @Path("/{id}/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("id") Long id) {
        byte[] data = relatorioService.generateBinary(id);
        return Response.ok(data)
                .header("Content-Disposition", "attachment; filename=relatorio.bin")
                .build();
    }
}

// Custom Media Types
public class CustomMediaTypes {
    public static final String APPLICATION_EXCEL = "application/vnd.ms-excel";
    public static final String APPLICATION_PDF = "application/pdf";
    public static final String TEXT_CSV = "text/csv";
}

// Provider customizado
@Provider
@Produces(CustomMediaTypes.TEXT_CSV)
public class CsvMessageBodyWriter implements MessageBodyWriter<List<?>> {
    
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, 
                               Annotation[] annotations, MediaType mediaType) {
        return List.class.isAssignableFrom(type) && 
               mediaType.toString().equals(CustomMediaTypes.TEXT_CSV);
    }
    
    @Override
    public void writeTo(List<?> list, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException {
        
        Writer writer = new OutputStreamWriter(entityStream, StandardCharsets.UTF_8);
        // Converter lista para CSV
        for (Object obj : list) {
            writer.write(obj.toString() + "\n");
        }
        writer.flush();
    }
}
```

## 3. ⚙️ **Configuração**

### Config Properties

| Annotation                | Nível          | Descrição                            | Conversão        | Default        | Exemplo                                  |
| ------------------------- | -------------- | ------------------------------------ | ---------------- | -------------- | ---------------------------------------- |
| `@ConfigProperty`         | Campo/Parâmetro| Injeta propriedade individual        | ✅ Automática    | ✅ Suportado   | `@ConfigProperty(name = "app.nome")`     |
| `@ConfigMapping`          | Interface/Classe| Mapeia grupo de propriedades        | ✅ Automática    | ✅ Suportado   | `@ConfigMapping(prefix = "database")`    |
| `@ConfigRoot`             | Classe         | Raiz de configuração (extensões)     | ✅ Automática    | ✅ Suportado   | Para desenvolvedores de extensões        |
| `@WithName`               | Método         | Nome customizado da propriedade      | -                | -              | Em interface `@ConfigMapping`            |
| `@WithDefault`            | Método         | Valor padrão na interface config     | -                | -              | Em interface `@ConfigMapping`            |
| `@WithConverter`          | Método/Campo   | Conversor customizado                | ✅ Custom        | -              | `@WithConverter(DurationConverter.class)`|

```java
// application.properties
/*
app.name=MinhaApp
app.version=1.0.0
app.max-retries=3
app.timeout=30s

database.url=jdbc:postgresql://localhost:5432/mydb
database.username=admin
database.password=secret
database.pool.min-size=5
database.pool.max-size=20

feature.email.enabled=true
feature.email.smtp-host=smtp.gmail.com
feature.email.smtp-port=587

api.keys.google=abc123
api.keys.aws=xyz789
*/

// 1. ConfigProperty - Propriedades individuais
@ApplicationScoped
public class AppService {
    
    // Injeção simples
    @ConfigProperty(name = "app.name")
    String appName;
    
    // Com valor padrão
    @ConfigProperty(name = "app.max-retries", defaultValue = "3")
    int maxRetries;
    
    // Optional (não lança exceção se não existir)
    @ConfigProperty(name = "app.optional-feature")
    Optional<String> optionalFeature;
    
    // Tipos suportados automaticamente
    @ConfigProperty(name = "app.timeout")
    Duration timeout;  // 30s -> Duration de 30 segundos
    
    @ConfigProperty(name = "feature.email.enabled")
    boolean emailEnabled;
    
    @ConfigProperty(name = "database.pool.max-size")
    Integer maxPoolSize;
    
    // Lista de valores (separados por vírgula)
    @ConfigProperty(name = "app.allowed-origins", defaultValue = "http://localhost:3000")
    List<String> allowedOrigins;
    
    // Construtor com injeção
    @Inject
    public AppService(
            @ConfigProperty(name = "app.name") String name,
            @ConfigProperty(name = "app.version") String version) {
        System.out.println("App: " + name + " v" + version);
    }
    
    public void execute() {
        System.out.println("Executando " + appName);
        System.out.println("Max retries: " + maxRetries);
        System.out.println("Timeout: " + timeout.getSeconds() + "s");
        
        optionalFeature.ifPresent(feature -> 
            System.out.println("Feature opcional: " + feature)
        );
    }
}

// 2. ConfigMapping - Agrupamento type-safe
@ConfigMapping(prefix = "database")
public interface DatabaseConfig {
    
    // Propriedade: database.url
    String url();
    
    // Propriedade: database.username
    String username();
    
    // Propriedade: database.password
    String password();
    
    // Grupo aninhado: database.pool.*
    PoolConfig pool();
    
    // Com nome customizado
    @WithName("connection-timeout")
    Duration connectionTimeout();
    
    // Com valor padrão
    @WithDefault("true")
    boolean autoCommit();
    
    // Opcional
    Optional<String> schema();
    
    interface PoolConfig {
        @WithName("min-size")
        @WithDefault("5")
        int minSize();
        
        @WithName("max-size")
        @WithDefault("20")
        int maxSize();
        
        @WithDefault("30s")
        Duration maxLifetime();
    }
}

// Uso do ConfigMapping
@ApplicationScoped
public class DatabaseService {
    
    @Inject
    DatabaseConfig dbConfig;
    
    public void connect() {
        System.out.println("Conectando em: " + dbConfig.url());
        System.out.println("Usuário: " + dbConfig.username());
        System.out.println("Pool min: " + dbConfig.pool().minSize());
        System.out.println("Pool max: " + dbConfig.pool().maxSize());
        System.out.println("Auto-commit: " + dbConfig.autoCommit());
    }
}

// 3. ConfigMapping com Map
@ConfigMapping(prefix = "api.keys")
public interface ApiKeysConfig {
    
    // Mapeia todas as propriedades api.keys.*
    Map<String, String> keys();
    
    // Uso: config.keys().get("google")
    // Retorna: abc123
}

// 4. ConfigMapping com List
@ConfigMapping(prefix = "feature")
public interface FeatureConfig {
    
    EmailConfig email();
    
    interface EmailConfig {
        boolean enabled();
        
        @WithName("smtp-host")
        String smtpHost();
        
        @WithName("smtp-port")
        int smtpPort();
        
        @WithDefault("false")
        boolean useTls();
        
        // Lista: feature.email.recipients=admin@example.com,user@example.com
        @WithDefault("admin@example.com")
        List<String> recipients();
    }
}

// 5. Conversor customizado
public class CustomConfig {
    
    @ConfigProperty(name = "app.custom-value")
    @WithConverter(CustomValueConverter.class)
    CustomValue customValue;
}

@Priority(100)
public class CustomValueConverter implements Converter<CustomValue> {
    
    @Override
    public CustomValue convert(String value) {
        // Lógica de conversão customizada
        return new CustomValue(value);
    }
}

// 6. Configuração programática
@ApplicationScoped
public class ConfigReader {
    
    @Inject
    Config config;  // org.eclipse.microprofile.config.Config
    
    public void readConfig() {
        // Ler propriedade
        String appName = config.getValue("app.name", String.class);
        
        // Ler com Optional
        Optional<String> optional = config.getOptionalValue("app.feature", String.class);
        
        // Ler com default
        int maxRetries = config.getOptionalValue("app.max-retries", Integer.class)
                               .orElse(3);
        
        // Iterar todas as propriedades
        config.getPropertyNames().forEach(name -> {
            System.out.println(name + " = " + config.getValue(name, String.class));
        });
        
        // Obter config sources
        config.getConfigSources().forEach(source -> {
            System.out.println("Source: " + source.getName());
            System.out.println("Ordinal: " + source.getOrdinal());
        });
    }
}

// 7. Perfis (profiles)
/*
# application.properties (default)
app.environment=development

# %dev.property para dev profile
%dev.database.url=jdbc:h2:mem:testdb
%dev.database.username=sa

# %prod.property para prod profile
%prod.database.url=jdbc:postgresql://prod-server:5432/proddb
%prod.database.username=produser

# %test.property para test profile  
%test.database.url=jdbc:h2:mem:testdb
%test.database.username=test
*/

// Ativar profile: quarkus dev (dev profile automático)
// Ou via: -Dquarkus.profile=prod

// 8. Configuração dinâmica (runtime)
@ApplicationScoped
public class DynamicConfigService {
    
    @ConfigProperty(name = "app.feature-flag")
    Provider<Boolean> featureFlagProvider;  // Provider permite mudança em runtime
    
    public void checkFeature() {
        // Sempre pega valor atual
        if (featureFlagProvider.get()) {
            System.out.println("Feature ativada!");
        }
    }
}
```

## 4. 🗄️ **Persistência (Hibernate/Panache)**

### JPA Básico

| Annotation        | Descrição                  | Escopo      | Herança | Exemplo                                |
| ----------------- | -------------------------- | ----------- | ------- | -------------------------------------- |
| `@Entity`         | Marca classe como entidade | Classe      | ✅ Sim  | Mapeamento objeto-relacional           |
| `@Table`          | Define nome da tabela      | Classe      | ❌ Não  | `@Table(name = "tb_usuarios")`         |
| `@Id`             | Chave primária             | Campo       | ✅ Sim  | Identificador único                    |
| `@GeneratedValue` | Geração automática de ID   | Campo       | ✅ Sim  | `@GeneratedValue(strategy = IDENTITY)` |
| `@Column`         | Mapeamento de coluna       | Campo       | ✅ Sim  | `@Column(name = "nome_completo")`      |
| `@Temporal`       | Tipo temporal (deprecated) | Campo       | ✅ Sim  | Use tipos do Java 8+ (LocalDate)       |
| `@Enumerated`     | Mapeamento de enum         | Campo       | ✅ Sim  | `@Enumerated(EnumType.STRING)`         |
| `@Transient`      | Campo não persistido       | Campo       | ✅ Sim  | Calculado, cache temporário            |
| `@Embedded`       | Objeto embutido            | Campo       | ✅ Sim  | Compor objeto sem tabela separada      |
| `@Embeddable`     | Classe embutível           | Classe      | ❌ Não  | Componente reutilizável                |
| `@Lob`            | Large Object (BLOB/CLOB)   | Campo       | ✅ Sim  | Arquivos, textos grandes               |
| `@Version`        | Controle de versão (OCC)   | Campo       | ✅ Sim  | Optimistic Concurrency Control         |

```java
// Entidade básica
@Entity
@Table(name = "usuarios", 
       indexes = @Index(name = "idx_email", columnList = "email"),
       uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nome_completo", nullable = false, length = 100)
    private String nomeCompleto;
    
    @Column(unique = true, nullable = false, length = 150)
    private String email;
    
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "ultima_atualizacao")
    private LocalDateTime ultimaAtualizacao;
    
    @Enumerated(EnumType.STRING)  // Salva como String, não ordinal
    @Column(length = 20)
    private Status status;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TipoUsuario tipo;
    
    @Lob  // Para textos longos
    @Column(columnDefinition = "TEXT")
    private String biografia;
    
    @Transient  // Não persiste no banco
    private String senhaTemporaria;
    
    @Embedded  // Componente embutido
    private Endereco endereco;
    
    @Version  // Controle de versão otimista
    private Long version;
    
    // Lifecycle callbacks
    @PrePersist
    void onCreate() {
        this.dataCriacao = LocalDateTime.now();
        this.status = Status.ATIVO;
    }
    
    @PreUpdate
    void onUpdate() {
        this.ultimaAtualizacao = LocalDateTime.now();
    }
    
    // Getters e setters
}

// Classe embutível
@Embeddable
public class Endereco {
    
    @Column(length = 9)
    private String cep;
    
    @Column(length = 200)
    private String logradouro;
    
    @Column(length = 10)
    private String numero;
    
    @Column(length = 100)
    private String complemento;
    
    @Column(length = 100)
    private String bairro;
    
    @Column(length = 100)
    private String cidade;
    
    @Column(length = 2)
    private String estado;
    
    // Construtores, getters e setters
}

// Enums
public enum Status {
    ATIVO, INATIVO, BLOQUEADO, PENDENTE
}

public enum TipoUsuario {
    ADMIN, CLIENTE, VENDEDOR, SUPORTE
}

// Estratégias de geração de ID
@Entity
public class ExemplosId {
    
    // IDENTITY - Auto-increment do banco (MySQL, Postgres)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIdentity;
    
    // SEQUENCE - Sequence do banco (Postgres, Oracle)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @SequenceGenerator(name = "seq_usuario", sequenceName = "usuario_seq", allocationSize = 1)
    private Long idSequence;
    
    // TABLE - Tabela geradora de IDs (portável)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tab_generator")
    @TableGenerator(name = "tab_generator", table = "id_generator", 
                    pkColumnName = "gen_name", valueColumnName = "gen_value")
    private Long idTable;
    
    // UUID - Identificador único universal
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid")
    private UUID idUuid;
}
```

### Relacionamentos JPA

| Annotation     | Cardinalidade | Lado Proprietário | Cascade | Lazy Load | Exemplo                            |
| -------------- | ------------- | ----------------- | ------- | --------- | ---------------------------------- |
| `@OneToOne`    | 1:1           | Com @JoinColumn   | ✅ Sim  | ⚠️ Eager  | Usuário ↔ Perfil                   |
| `@OneToMany`   | 1:N           | Lado "Many"       | ✅ Sim  | ✅ Lazy   | Usuário → Pedidos                  |
| `@ManyToOne`   | N:1           | Lado "Many"       | ✅ Sim  | ⚠️ Eager  | Pedidos → Usuário                  |
| `@ManyToMany`  | N:N           | Com mappedBy      | ✅ Sim  | ✅ Lazy   | Usuários ↔ Grupos                  |
| `@JoinColumn`  | -             | Define FK         | -       | -         | `@JoinColumn(name = "usuario_id")` |
| `@JoinTable`   | -             | Tabela associativa| -       | -         | Para ManyToMany                    |

### Panache

| Annotation          | Tipo          | Descrição                      | Base Class         | Exemplo                  |
| ------------------- | ------------- | ------------------------------ | ------------------ | ------------------------ |
| `@Entity` (Panache) | Entidade      | Active Record pattern          | `PanacheEntity`    | Métodos estáticos built-in |
| -                   | Repository    | Repository pattern             | `PanacheRepository`| Classe separada          |
| -                   | Kotlin Entity | Panache para Kotlin            | `PanacheCompanion` | Suporte a Kotlin         |

```java
// 1. ACTIVE RECORD PATTERN - Entidade Panache
@Entity
public class Produto extends PanacheEntity {
    // id, persist(), delete() já incluídos por PanacheEntity
    
    public String nome;
    public String descricao;
    public BigDecimal preco;
    
    @Enumerated(EnumType.STRING)
    public StatusProduto status;
    
    @Column(name = "data_criacao")
    public LocalDateTime dataCriacao;
    
    // Métodos de negócio na própria entidade
    public void ativar() {
        this.status = StatusProduto.ATIVO;
        this.persist();  // Método herdado
    }
    
    public void desativar() {
        this.status = StatusProduto.INATIVO;
        this.persist();
    }
    
    // Queries nomeadas (type-safe)
    public static List<Produto> findByNome(String nome) {
        return list("nome", nome);
    }
    
    public static List<Produto> findAtivos() {
        return list("status", StatusProduto.ATIVO);
    }
    
    public static List<Produto> findByPrecoMenorQue(BigDecimal preco) {
        return list("preco < ?1", preco);
    }
    
    public static Produto findBySku(String sku) {
        return find("sku", sku).firstResult();
    }
    
    // Queries JPQL complexas
    public static List<Produto> findEmPromocao() {
        return list("status = ?1 and preco < ?2", 
                    StatusProduto.ATIVO, new BigDecimal("100"));
    }
    
    // Paginação
    public static List<Produto> findAll(int page, int size) {
        return findAll()
                .page(page, size)
                .list();
    }
    
    // Count
    public static long countAtivos() {
        return count("status", StatusProduto.ATIVO);
    }
    
    // Delete em massa
    public static long deleteInativos() {
        return delete("status", StatusProduto.INATIVO);
    }
    
    // Stream para processar grandes volumes
    public static void processarTodos() {
        streamAll()
                .forEach(produto -> {
                    // Processar um por um
                    System.out.println(produto.nome);
                });
    }
}

// Uso do Active Record
@Path("/produtos")
@ApplicationScoped
public class ProdutoResource {
    
    @GET
    @Transactional
    public List<Produto> listar() {
        return Produto.listAll();  // Método estático
    }
    
    @GET
    @Path("/{id}")
    public Produto buscar(@PathParam("id") Long id) {
        return Produto.findById(id);  // Método estático
    }
    
    @POST
    @Transactional
    public Response criar(Produto produto) {
        produto.persist();  // Método de instância
        return Response.status(Status.CREATED).entity(produto).build();
    }
    
    @PUT
    @Path("/{id}")
    @Transactional
    public Produto atualizar(@PathParam("id") Long id, Produto dados) {
        Produto produto = Produto.findById(id);
        if (produto == null) {
            throw new WebApplicationException("Produto não encontrado", 404);
        }
        produto.nome = dados.nome;
        produto.preco = dados.preco;
        // persist() automático com @Transactional
        return produto;
    }
    
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {
        boolean deleted = Produto.deleteById(id);  // Método estático
        return deleted 
                ? Response.noContent().build()
                : Response.status(Status.NOT_FOUND).build();
    }
    
    @GET
    @Path("/buscar")
    public List<Produto> buscar(@QueryParam("nome") String nome) {
        return Produto.findByNome(nome);  // Método customizado
    }
}

// 2. REPOSITORY PATTERN - Panache Repository
@ApplicationScoped
public class ClienteRepository implements PanacheRepository<Cliente> {
    // Métodos CRUD já incluídos por PanacheRepository
    
    // Queries customizadas
    public Cliente findByEmail(String email) {
        return find("email", email).firstResult();
    }
    
    public List<Cliente> findByNomeContaining(String termo) {
        return list("LOWER(nome) LIKE LOWER(?1)", "%" + termo + "%");
    }
    
    public List<Cliente> findAtivos() {
        return list("status", StatusCliente.ATIVO);
    }
    
    public Optional<Cliente> findByCpf(String cpf) {
        return find("cpf", cpf).firstResultOptional();
    }
    
    // Paginação
    public List<Cliente> findAll(int page, int size) {
        return findAll()
                .page(page, size)
                .list();
    }
    
    // Sort
    public List<Cliente> findAllOrdenado() {
        return listAll(Sort.by("nome").and("dataCriacao", Sort.Direction.Descending));
    }
    
    // Named queries
    @NamedQuery(name = "Cliente.findVips",
                query = "SELECT c FROM Cliente c WHERE c.tipo = 'VIP'")
    public List<Cliente> findVips() {
        return find("#Cliente.findVips").list();
    }
    
    // Queries nativas
    public List<Cliente> findByNativeQuery(String estado) {
        return getEntityManager()
                .createNativeQuery("SELECT * FROM clientes WHERE estado = ?1", Cliente.class)
                .setParameter(1, estado)
                .getResultList();
    }
}

// Entidade para Repository Pattern
@Entity
public class Cliente {
    @Id @GeneratedValue
    public Long id;
    
    public String nome;
    
    @Column(unique = true)
    public String email;
    
    public String cpf;
    
    @Enumerated(EnumType.STRING)
    public StatusCliente status;
    
    // Sem métodos estáticos - delegado para Repository
}

// Uso do Repository
@Path("/clientes")
@ApplicationScoped
public class ClienteResource {
    
    @Inject
    ClienteRepository repository;
    
    @GET
    public List<Cliente> listar() {
        return repository.listAll();
    }
    
    @GET
    @Path("/{id}")
    public Cliente buscar(@PathParam("id") Long id) {
        return repository.findById(id);
    }
    
    @POST
    @Transactional
    public Response criar(Cliente cliente) {
        repository.persist(cliente);
        return Response.status(Status.CREATED).entity(cliente).build();
    }
    
    @GET
    @Path("/email/{email}")
    public Cliente buscarPorEmail(@PathParam("email") String email) {
        return repository.findByEmail(email);
    }
}

// 3. Panache com ID customizado
@Entity
public class Livro extends PanacheEntityBase {
    
    @Id
    @Column(length = 13)
    public String isbn;  // ISBN como chave primária
    
    public String titulo;
    public String autor;
    
    // Métodos estáticos funcionam normalmente
    public static Livro findByIsbn(String isbn) {
        return findById(isbn);
    }
}

// 4. Queries avançadas com Panache
@Entity
public class Pedido extends PanacheEntity {
    
    public LocalDateTime data;
    public BigDecimal total;
    
    @ManyToOne
    public Cliente cliente;
    
    @Enumerated(EnumType.STRING)
    public StatusPedido status;
    
    // Queries com Join
    public static List<Pedido> findByCliente(Long clienteId) {
        return list("cliente.id", clienteId);
    }
    
    // Queries com parâmetros nomeados
    public static List<Pedido> findByPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return list("data >= :inicio and data <= :fim", 
                    Parameters.with("inicio", inicio).and("fim", fim));
    }
    
    // Agregação
    public static BigDecimal totalVendas() {
        return find("SELECT SUM(total) FROM Pedido").project(BigDecimal.class).firstResult();
    }
    
    // DTO Projection
    public static List<PedidoResumo> findResumos() {
        return find("SELECT new com.example.PedidoResumo(p.id, p.data, p.total) FROM Pedido p")
                .project(PedidoResumo.class)
                .list();
    }
    
    // Update em massa
    public static int cancelarAntigos(LocalDateTime dataLimite) {
        return update("status = ?1 where data < ?2 and status = ?3", 
                      StatusPedido.CANCELADO, dataLimite, StatusPedido.PENDENTE);
    }
}

// 5. Panache Kotlin (para referência)
/*
@Entity
class Produto : PanacheEntity() {
    lateinit var nome: String
    var preco: BigDecimal = BigDecimal.ZERO
    
    companion object : PanacheCompanion<Produto> {
        fun findByNome(nome: String) = list("nome", nome)
        fun findAtivos() = list("status", Status.ATIVO)
    }
}
*/
```
    private Long id;
    
    private String nome;
    
    // Lado proprietário (tem a FK)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id", unique = true)
    private Perfil perfil;
}

@Entity
public class Perfil {
    @Id @GeneratedValue
    private Long id;
    
    private String bio;
    private String avatar;
    
    // Lado inverso (mappedBy aponta para o campo no Usuario)
    @OneToOne(mappedBy = "perfil")
    private Usuario usuario;
}

// ONE TO MANY / MANY TO ONE - Bidirecional
@Entity
public class Cliente {
    @Id @GeneratedValue
    private Long id;
    
    private String nome;
    
    // Lado "One" (não tem FK)
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos = new ArrayList<>();
    
    // Métodos helper para sincronizar ambos lados
    public void addPedido(Pedido pedido) {
        pedidos.add(pedido);
        pedido.setCliente(this);
    }
    
    public void removePedido(Pedido pedido) {
        pedidos.remove(pedido);
        pedido.setCliente(null);
    }
}

@Entity
public class Pedido {
    @Id @GeneratedValue
    private Long id;
    
    private BigDecimal total;
    private LocalDateTime data;
    
    // Lado "Many" (tem a FK) - SEMPRE proprietário
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    // Outro ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_entrega_id")
    private Endereco enderecoEntrega;
    
    // OneToMany dentro de Pedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();
}

@Entity
public class ItemPedido {
    @Id @GeneratedValue
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    
    private Integer quantidade;
    private BigDecimal precoUnitario;
}

// MANY TO MANY - Bidirecional
@Entity
public class Estudante {
    @Id @GeneratedValue
    private Long id;
    
    private String nome;
    
    // Lado proprietário (define a tabela de junção)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "estudante_curso",
        joinColumns = @JoinColumn(name = "estudante_id"),
        inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private Set<Curso> cursos = new HashSet<>();
    
    // Métodos helper
    public void addCurso(Curso curso) {
        cursos.add(curso);
        curso.getEstudantes().add(this);
    }
    
    public void removeCurso(Curso curso) {
        cursos.remove(curso);
        curso.getEstudantes().remove(this);
    }
}

@Entity
public class Curso {
    @Id @GeneratedValue
    private Long id;
    
    private String nome;
    private String codigo;
    
    // Lado inverso
    @ManyToMany(mappedBy = "cursos")
    private Set<Estudante> estudantes = new HashSet<>();
}

// MANY TO MANY com atributos extras (tabela associativa como entidade)
@Entity
public class Usuario {
    @Id @GeneratedValue
    private Long id;
    
    private String nome;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsuarioGrupo> grupos = new HashSet<>();
}

@Entity
public class Grupo {
    @Id @GeneratedValue
    private Long id;
    
    private String nome;
    
    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsuarioGrupo> usuarios = new HashSet<>();
}

// Entidade associativa com atributos
@Entity
@Table(name = "usuario_grupo")
public class UsuarioGrupo {
    
    @EmbeddedId
    private UsuarioGrupoId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("grupoId")
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;
    
    // Atributos extras da associação
    @Enumerated(EnumType.STRING)
    private TipoMembro tipo;  // ADMIN, MEMBRO, CONVIDADO
    
    @Column(name = "data_entrada")
    private LocalDateTime dataEntrada;
    
    @PrePersist
    void onCreate() {
        this.dataEntrada = LocalDateTime.now();
    }
}

// Chave composta
@Embeddable
public class UsuarioGrupoId implements Serializable {
    
    @Column(name = "usuario_id")
    private Long usuarioId;
    
    @Column(name = "grupo_id")
    private Long grupoId;
    
    // equals() e hashCode() obrigatórios
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioGrupoId that = (UsuarioGrupoId) o;
        return Objects.equals(usuarioId, that.usuarioId) &&
               Objects.equals(grupoId, that.grupoId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, grupoId);
    }
}

// Cascade Types
@Entity
public class ExemplosCascade {
    
    // ALL - Todas operações propagam
    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> todosOsCascades;
    
    // PERSIST - Apenas persist()
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Item> apenasPersist;
    
    // MERGE - Apenas merge()
    @OneToMany(cascade = CascadeType.MERGE)
    private List<Item> apenasMerge;
    
    // REMOVE - Apenas remove()
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Item> apenasRemove;
    
    // REFRESH - Apenas refresh()
    @OneToMany(cascade = CascadeType.REFRESH)
    private List<Item> apenasRefresh;
    
    // DETACH - Apenas detach()
    @OneToMany(cascade = CascadeType.DETACH)
    private List<Item> apenasDetach;
    
    // Múltiplos cascades
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Item> persistEMerge;
    
    // orphanRemoval - Remove órfãos (sem pai)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> comRemocaoOrfaos;
}

// Fetch Types
@Entity
public class ExemplosFetch {
    
    // EAGER - Carrega imediatamente (JOIN)
    @ManyToOne(fetch = FetchType.EAGER)
    private Categoria categoriaEager;
    
    // LAZY - Carrega sob demanda (proxy)
    @ManyToOne(fetch = FetchType.LAZY)
    private Categoria categoriaLazy;
    
    // N+1 problem - EVITE!
    @OneToMany(fetch = FetchType.EAGER)  // ❌ RUIM
    private List<Item> itens;
    
    // Solução: LAZY + fetch join na query
    @OneToMany(fetch = FetchType.LAZY)  // ✅ BOM
    private List<Item> itensLazy;
    
    // Query com fetch join:
    // SELECT p FROM Produto p LEFT JOIN FETCH p.itensLazy WHERE p.id = :id
}
```

### Panache

| Annotation          | Descrição        | Exemplo                  |
| ------------------- | ---------------- | ------------------------ |
| `@Entity` (Panache) | Entidade Panache | Herda de `PanacheEntity` |

## 5. 🔒 **Segurança**

### Autorização e Autenticação

| Annotation         | Nível          | Tipo         | Herança | Descrição                           | Exemplo                                 |
| ------------------ | -------------- | ------------ | ------- | ----------------------------------- | --------------------------------------- |
| `@RolesAllowed`    | Classe/Método  | Autorização  | ❌ Não  | Acesso apenas para papéis específicos| `@RolesAllowed({"admin", "manager"})`   |
| `@PermitAll`       | Classe/Método  | Autorização  | ❌ Não  | Permite acesso a todos              | Endpoints públicos                      |
| `@DenyAll`         | Classe/Método  | Autorização  | ❌ Não  | Nega acesso a todos                 | Endpoint desabilitado                   |
| `@Authenticated`   | Classe/Método  | Autenticação | ❌ Não  | Requer apenas autenticação          | Qualquer usuário logado                 |
| `@SecurityScheme`  | Classe         | OpenAPI      | ❌ Não  | Define esquema de segurança (docs)  | JWT, BasicAuth, OAuth2                  |
| `@SecurityRequirement` | Método     | OpenAPI      | ❌ Não  | Documenta requisito de segurança    | Para documentação OpenAPI               |

```java
// application.properties
/*
# Configuração de segurança
quarkus.security.jaxrs.deny-unauthenticated-access=false
quarkus.security.users.embedded.enabled=true
quarkus.security.users.embedded.plain-text=true
quarkus.security.users.embedded.users.admin=admin123
quarkus.security.users.embedded.users.user=user123
quarkus.security.users.embedded.roles.admin=admin,user
quarkus.security.users.embedded.roles.user=user

# JWT
mp.jwt.verify.publickey.location=META-INF/resources/publicKey.pem
mp.jwt.verify.issuer=https://quarkus.io/issuer
*/

// Controle de acesso completo
@Path("/api/admin")
@ApplicationScoped
public class AdminResource {
    
    @Inject
    JsonWebToken jwt;
    
    @Inject
    SecurityIdentity securityIdentity;
    
    // Apenas para admin
    @GET
    @Path("/users")
    @RolesAllowed("admin")
    @Operation(summary = "Lista todos usuários (apenas admin)")
    public List<User> listarUsuarios() {
        return User.listAll();
    }
    
    // Admin ou manager
    @GET
    @Path("/reports")
    @RolesAllowed({"admin", "manager"})
    public List<Report> gerarRelatorios() {
        return Report.listAll();
    }
    
    // Qualquer usuário autenticado
    @GET
    @Path("/profile")
    @Authenticated
    public User getPerfil() {
        String username = securityIdentity.getPrincipal().getName();
        return User.find("username", username).firstResult();
    }
    
    // Informações do usuário autenticado
    @GET
    @Path("/me")
    @Authenticated
    public UserInfo getMe() {
        return new UserInfo(
            securityIdentity.getPrincipal().getName(),
            securityIdentity.getRoles(),
            securityIdentity.isAnonymous()
        );
    }
    
    // Acesso público
    @GET
    @Path("/public")
    @PermitAll
    public String publicEndpoint() {
        return "Acessível por todos";
    }
    
    // Bloqueado para todos
    @GET
    @Path("/disabled")
    @DenyAll
    public String disabledEndpoint() {
        return "Nunca será acessado";
    }
    
    // Checagem programática de papéis
    @GET
    @Path("/conditional")
    @Authenticated
    public Response conditionalAccess() {
        if (securityIdentity.hasRole("admin")) {
            return Response.ok("Dados admin").build();
        } else if (securityIdentity.hasRole("user")) {
            return Response.ok("Dados user").build();
        } else {
            return Response.status(Status.FORBIDDEN).build();
        }
    }
    
    // Informações do JWT
    @GET
    @Path("/jwt-info")
    @Authenticated
    public Map<String, Object> getJwtInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("username", jwt.getName());
        info.put("groups", jwt.getGroups());
        info.put("issuer", jwt.getIssuer());
        info.put("subject", jwt.getSubject());
        info.put("expiresAt", jwt.getExpirationTime());
        info.put("claims", jwt.getClaimNames());
        return info;
    }
}

// Segurança a nível de classe
@Path("/api/products")
@RolesAllowed("user")  // Aplica a todos os métodos
@ApplicationScoped
public class ProductResource {
    
    @GET
    public List<Product> list() {
        // Requer papel "user"
        return Product.listAll();
    }
    
    @POST
    @RolesAllowed("admin")  // Sobrescreve o da classe
    public Response create(Product product) {
        // Requer papel "admin"
        product.persist();
        return Response.status(Status.CREATED).entity(product).build();
    }
    
    @GET
    @Path("/public")
    @PermitAll  // Sobrescreve o da classe
    public List<Product> publicList() {
        // Público, mesmo com @RolesAllowed na classe
        return Product.list("featured", true);
    }
}

// Autenticação customizada
@ApplicationScoped
public class CustomAuthenticationMechanism implements HttpAuthenticationMechanism {
    
    @Override
    public Uni<SecurityIdentity> authenticate(RoutingContext context,
                                               IdentityProviderManager identityProviderManager) {
        
        // Obter token do header
        String authHeader = context.request().getHeader("X-Auth-Token");
        
        if (authHeader == null) {
            return Uni.createFrom().nullItem();
        }
        
        // Validar token e criar identidade
        TokenAuthenticationRequest request = new TokenAuthenticationRequest(
            new TokenCredential(authHeader, "bearer")
        );
        
        return identityProviderManager.authenticate(request);
    }
    
    @Override
    public Uni<ChallengeData> getChallenge(RoutingContext context) {
        return Uni.createFrom().item(
            new ChallengeData(401, "WWW-Authenticate", "Bearer")
        );
    }
}

// Identity Provider customizado
@ApplicationScoped
public class CustomIdentityProvider implements IdentityProvider<TokenAuthenticationRequest> {
    
    @Override
    public Class<TokenAuthenticationRequest> getRequestType() {
        return TokenAuthenticationRequest.class;
    }
    
    @Override
    public Uni<SecurityIdentity> authenticate(TokenAuthenticationRequest request,
                                               AuthenticationRequestContext context) {
        
        String token = request.getToken().getToken();
        
        // Validar token (ex: banco, cache, JWT)
        return validateToken(token)
            .onItem().transform(userInfo -> {
                if (userInfo == null) {
                    throw new AuthenticationFailedException();
                }
                
                return QuarkusSecurityIdentity.builder()
                    .setPrincipal(new QuarkusPrincipal(userInfo.getUsername()))
                    .addRoles(userInfo.getRoles())
                    .addCredential(request.getToken())
                    .build();
            });
    }
    
    private Uni<UserInfo> validateToken(String token) {
        // Lógica de validação
        return Uni.createFrom().item(new UserInfo("user", Set.of("user")));
    }
}

// Augmentor de segurança (adiciona informações extras)
@ApplicationScoped
public class CustomSecurityIdentityAugmentor implements SecurityIdentityAugmentor {
    
    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, 
                                          AuthenticationRequestContext context) {
        
        // Adicionar papéis extras baseado em lógica de negócio
        return Uni.createFrom().item(() -> {
            QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder(identity);
            
            // Adicionar papel "premium" se usuário for premium
            if (isPremiumUser(identity.getPrincipal().getName())) {
                builder.addRole("premium");
            }
            
            // Adicionar permissões extras
            builder.addPermissionChecker(permission -> {
                // Lógica customizada de permissão
                return Uni.createFrom().item(true);
            });
            
            return builder.build();
        });
    }
    
    private boolean isPremiumUser(String username) {
        // Checar no banco, cache, etc
        return User.find("username = ?1 and premium = true", username)
                   .count() > 0;
    }
}

// ExceptionMapper para erros de autenticação
@Provider
public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationFailedException> {
    
    @Override
    public Response toResponse(AuthenticationFailedException exception) {
        return Response.status(Status.UNAUTHORIZED)
                .entity(Map.of(
                    "error", "Authentication failed",
                    "message", "Invalid credentials"
                ))
                .build();
    }
}

@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {
    
    @Override
    public Response toResponse(ForbiddenException exception) {
        return Response.status(Status.FORBIDDEN)
                .entity(Map.of(
                    "error", "Access denied",
                    "message", "Insufficient permissions"
                ))
                .build();
    }
}

// Documentação OpenAPI com segurança
@ApplicationScoped
@SecurityScheme(
    securitySchemeName = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "JWT Bearer Token authentication"
)
@SecurityScheme(
    securitySchemeName = "basicAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "basic",
    description = "Basic HTTP authentication"
)
public class OpenAPIConfig {
}

@Path("/api/secure")
public class SecureResource {
    
    @GET
    @Authenticated
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get secure data")
    @APIResponse(responseCode = "200", description = "Success")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    public String getSecureData() {
        return "Secure data";
    }
}
```

## 6. 📊 **Observabilidade**

### Métricas

| Annotation | Descrição              | Exemplo                             |
| ---------- | ---------------------- | ----------------------------------- |
| `@Counted` | Conta execuções        | `@Counted(name = "login_attempts")` |
| `@Timed`   | Mede tempo de execução | `@Timed(name = "db_query_time")`    |
| `@Gauge`   | Valor atual            | `@Gauge(name = "active_users")`     |
| `@Metered` | Taxa de eventos        | Requests por segundo                |

### Health Checks

| Annotation   | Descrição                  | Exemplo        |
| ------------ | -------------------------- | -------------- |
| `@Readiness` | Health check de prontidão  | Sistema pronto |
| `@Liveness`  | Health check de vivacidade | Sistema vivo   |

### OpenAPI

| Annotation     | Descrição           | Exemplo                                      |
| -------------- | ------------------- | -------------------------------------------- |
| `@Operation`   | Documenta operação  | `@Operation(summary = "Lista usuários")`     |
| `@Parameter`   | Documenta parâmetro | `@Parameter(description = "ID do usuário")`  |
| `@APIResponse` | Documenta resposta  | `@APIResponse(responseCode = "200")`         |
| `@Schema`      | Documenta schema    | `@Schema(description = "Modelo de usuário")` |
| `@Tag`         | Agrupa operações    | `@Tag(name = "Usuários")`                    |

## 7. 🔄 **Processamento Assíncrono**

| Annotation            | Descrição                 | Exemplo               |
| --------------------- | ------------------------- | --------------------- |
| `@Asynchronous`       | Execução assíncrona       | Método não bloqueante |
| `@RunOnVirtualThread` | Executa em virtual thread | Para I/O intensivo    |

## 8. 📨 **Messaging**

| Annotation  | Descrição         | Exemplo                     |
| ----------- | ----------------- | --------------------------- |
| `@Incoming` | Consome mensagens | `@Incoming("pedidos")`      |
| `@Outgoing` | Produz mensagens  | `@Outgoing("notificacoes")` |
| `@Channel`  | Injeta channel    | Reactive Messaging          |

## 9. 🧪 **Testes**

| Annotation             | Descrição        | Exemplo                               |
| ---------------------- | ---------------- | ------------------------------------- |
| `@QuarkusTest`         | Teste integrado  | Inicia aplicação para teste           |
| `@TestProfile`         | Perfil de teste  | `@TestProfile(DevProfile.class)`      |
| `@QuarkusTestResource` | Recurso de teste | TestContainers, mocks                 |
| `@InjectMock`          | Injeta mock      | `@InjectMock DatabaseService service` |

## 10. 🔄 **Transações**

| Annotation                  | Descrição                 | Exemplo                    |
| --------------------------- | ------------------------- | -------------------------- |
| `@Transactional`            | Controle transacional     | `@Transactional(REQUIRED)` |
| `@TransactionConfiguration` | Configuração de transação | Timeout, isolamento        |

## 11. ⏱️ **Agendamento**

| Annotation   | Descrição              | Exemplo                     |
| ------------ | ---------------------- | --------------------------- |
| `@Scheduled` | Agendamento de tarefas | `@Scheduled(every = "30s")` |

## 12. 🔧 **Build e Deploy**

| Annotation               | Descrição              | Exemplo            |
| ------------------------ | ---------------------- | ------------------ |
| `@RegisterForReflection` | Registra para reflexão | Native compilation |
| `@NativeImageTest`       | Teste em imagem nativa | `@NativeImageTest` |

## 13. 🎯 **Interceptadores e Eventos**

| Annotation            | Descrição                   | Exemplo                                  |
| --------------------- | --------------------------- | ---------------------------------------- |
| `@Interceptor`        | Define interceptador        | Cross-cutting concerns                   |
| `@InterceptorBinding` | Binding de interceptador    | `@Logged`, `@Secured`                    |
| `@Observes`           | Observa eventos CDI         | `void handle(@Observes UserEvent event)` |
| `@ObservesAsync`      | Observa eventos assíncronos | Event handling assíncrono                |

## 14. 🌊 **Reactive**

| Annotation               | Descrição               | Exemplo                 |
| ------------------------ | ----------------------- | ----------------------- |
| `@RestStreamElementType` | Tipo de elemento stream | Para Server-Sent Events |
| `@SseElementType`        | Tipo SSE                | Server-Sent Events      |

## 15. 🔌 **Extensões Específicas**

### Cache

| Annotation            | Descrição        | Exemplo                                    |
| --------------------- | ---------------- | ------------------------------------------ |
| `@CacheResult`        | Cachea resultado | `@CacheResult(cacheName = "usuarios")`     |
| `@CacheInvalidate`    | Invalida cache   | `@CacheInvalidate(cacheName = "usuarios")` |
| `@CacheInvalidateAll` | Limpa todo cache | Remove todas entradas                      |

### Validation

| Annotation  | Descrição                 | Exemplo                        |
| ----------- | ------------------------- | ------------------------------ |
| `@Valid`    | Valida objeto             | `@Valid Usuario usuario`       |
| `@NotNull`  | Campo não nulo            | `@NotNull String nome`         |
| `@NotBlank` | String não vazia          | `@NotBlank String email`       |
| `@Size`     | Tamanho da coleção/string | `@Size(min = 3, max = 50)`     |
| `@Email`    | Formato de email          | `@Email String email`          |
| `@Pattern`  | Expressão regular         | `@Pattern(regexp = "\\d{11}")` |

## 💡 **Dicas de Uso e Boas Práticas**

### Combinações Comuns por Caso de Uso

#### 1. REST API Completa
```java
@Path("/api/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Tag(name = "Usuários", description = "Gerenciamento de usuários")
public class UsuarioResource {
    
    @Inject UsuarioService service;
    @Inject SecurityIdentity identity;
    
    @GET
    @RolesAllowed("user")
    @Operation(summary = "Lista usuários com paginação")
    public Response listar(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {
        // Implementação
        return Response.ok().build();
    }
    
    @POST
    @Transactional
    @RolesAllowed("admin")
    @Operation(summary = "Cria novo usuário")
    public Response criar(@Valid Usuario usuario) {
        // Implementação
        return Response.status(Status.CREATED).build();
    }
}
```

#### 2. Serviço de Negócio
```java
@ApplicationScoped
@Transactional  // Todas operações são transacionais
public class PedidoService {
    
    @Inject EntityManager em;
    @Inject EventBus eventBus;
    
    @ConfigProperty(name = "pedido.max-itens", defaultValue = "100")
    int maxItens;
    
    @Timed(name = "processar_pedido")
    @Counted(name = "pedidos_processados")
    public Pedido processar(PedidoDTO dto) {
        // Lógica de negócio
        return pedido;
    }
    
    @Asynchronous
    public CompletionStage<Void> notificarCliente(Long pedidoId) {
        // Processamento assíncrono
        return CompletableFuture.completedFuture(null);
    }
}
```

#### 3. Entidade JPA com Panache
```java
@Entity
@Table(name = "produtos")
public class Produto extends PanacheEntity {
    
    @Column(nullable = false, length = 200)
    public String nome;
    
    @Column(precision = 10, scale = 2)
    public BigDecimal preco;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    public Categoria categoria;
    
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<ImagemProduto> imagens = new ArrayList<>();
    
    @Version
    public Long version;
    
    // Queries type-safe
    public static List<Produto> findByCategoria(Long categoriaId) {
        return list("categoria.id", categoriaId);
    }
    
    public static List<Produto> findDispon iveis() {
        return list("estoque > 0 and ativo = true");
    }
}
```

#### 4. Configuração Type-Safe
```java
@ConfigMapping(prefix = "app")
public interface AppConfig {
    
    String name();
    String version();
    
    @WithDefault("true")
    boolean enabled();
    
    DatabaseConfig database();
    SecurityConfig security();
    
    interface DatabaseConfig {
        String url();
        String username();
        String password();
        
        @WithDefault("10")
        int poolSize();
    }
    
    interface SecurityConfig {
        @WithDefault("30m")
        Duration sessionTimeout();
        
        @WithDefault("false")
        boolean strictMode();
    }
}
```

#### 5. Teste Integrado
```java
@QuarkusTest
@TestProfile(TestProfile.class)
public class UsuarioResourceTest {
    
    @Inject UsuarioService service;
    
    @InjectMock
    EmailService emailService;
    
    @Test
    @TestSecurity(user = "admin", roles = "admin")
    public void testCriarUsuario() {
        // Testa com usuário admin
        given()
            .contentType(ContentType.JSON)
            .body(new Usuario("João"))
        .when()
            .post("/api/usuarios")
        .then()
            .statusCode(201);
    }
    
    @Test
    public void testListarSemAutenticacao() {
        given()
        .when()
            .get("/api/usuarios")
        .then()
            .statusCode(401);
    }
}
```

### Hierarquia de Importância

#### ⭐⭐⭐ Essenciais (Usar em todo projeto)
- `@ApplicationScoped` - Escopo de beans
- `@Inject` - Injeção de dependências
- `@Path`, `@GET`, `@POST`, `@PUT`, `@DELETE` - REST endpoints
- `@Produces`, `@Consumes` - Negociação de conteúdo
- `@Transactional` - Controle transacional
- `@Entity`, `@Id` - Persistência básica
- `@ConfigProperty` - Configuração

#### ⭐⭐ Importantes (Usar frequentemente)
- `@QueryParam`, `@PathParam` - Parâmetros REST
- `@Valid` - Validação de entrada
- `@RolesAllowed`, `@Authenticated` - Segurança
- `@OneToMany`, `@ManyToOne` - Relacionamentos
- `@Scheduled` - Tarefas agendadas
- `@Counted`, `@Timed` - Métricas
- `@Operation`, `@APIResponse` - Documentação OpenAPI

#### ⭐ Específicas (Usar quando necessário)
- `@Incoming`, `@Outgoing` - Messaging
- `@CacheResult` - Cache
- `@Asynchronous`, `@RunOnVirtualThread` - Processamento assíncrono
- `@Observes` - Eventos CDI
- `@Readiness`, `@Liveness` - Health checks
- `@RegisterForReflection` - Native compilation

### Anti-Padrões a Evitar

#### ❌ NÃO FAÇA
```java
// Múltiplos @ApplicationScoped criando singletons manualmente
@ApplicationScoped
public class BadService {
    private static BadService instance;  // ❌ Redundante!
    
    public static BadService getInstance() {  // ❌ Não precisa!
        return instance;
    }
}

// Injeção em campos estáticos
@ApplicationScoped
public class BadInjection {
    @Inject
    static DatabaseService db;  // ❌ Não funciona!
}

// @Singleton com estado mutável sem sincronização
@Singleton
public class BadSingleton {
    private int counter = 0;  // ❌ Não thread-safe!
    
    public void increment() {
        counter++;  // ❌ Race condition!
    }
}

// Fetch EAGER em coleções
@Entity
public class BadEntity {
    @OneToMany(fetch = FetchType.EAGER)  // ❌ N+1 problem!
    private List<Item> items;
}

// Transações longas
@Transactional
public void processarTudo() {  // ❌ Transação muito longa!
    for (int i = 0; i < 10000; i++) {
        processarItem(i);  // Segura lock do BD
        Thread.sleep(1000);  // ❌❌❌
    }
}
```

#### ✅ FAÇA ASSIM
```java
// Use CDI corretamente
@ApplicationScoped
public class GoodService {
    // Apenas injeção, sem getInstance()
}

// Injeção em campos de instância ou construtor
@ApplicationScoped
public class GoodInjection {
    private final DatabaseService db;
    
    @Inject
    public GoodInjection(DatabaseService db) {  // ✅ Imutável!
        this.db = db;
    }
}

// @ApplicationScoped com operações stateless
@ApplicationScoped
public class GoodScoped {
    @Inject EntityManager em;  // ✅ Thread-safe (request-scoped internamente)
    
    public User find(Long id) {  // ✅ Stateless
        return em.find(User.class, id);
    }
}

// Fetch LAZY + query específica
@Entity
public class GoodEntity {
    @OneToMany(fetch = FetchType.LAZY)  // ✅ Lazy por padrão
    private List<Item> items;
}

// Query com JOIN FETCH quando precisar
@ApplicationScoped
public class GoodRepository {
    @Inject EntityManager em;
    
    public User findWithItems(Long id) {
        return em.createQuery(
            "SELECT u FROM User u LEFT JOIN FETCH u.items WHERE u.id = :id",
            User.class)
            .setParameter("id", id)
            .getSingleResult();
    }
}

// Transações curtas e específicas
@ApplicationScoped
public class GoodTransactional {
    
    @Transactional
    public void salvarItem(Item item) {  // ✅ Operação atômica rápida
        item.persist();
    }
    
    public void processarLote(List<Item> items) {
        items.forEach(item -> {
            processarItem(item);
            salvarItem(item);  // ✅ Transação individual
        });
    }
}
```

### Referências Rápidas

- 📘 [Documentação Oficial Quarkus](https://quarkus.io/guides/)
- 📕 [CDI Specification](https://jakarta.ee/specifications/cdi/)
- 📗 [JAX-RS Specification](https://jakarta.ee/specifications/restful-ws/)
- 📙 [JPA Specification](https://jakarta.ee/specifications/persistence/)
- 📔 [MicroProfile Specification](https://microprofile.io/)

---

**Última atualização**: 2025-09-29  
**Versão Quarkus**: 3.x+  
**Java**: 17+