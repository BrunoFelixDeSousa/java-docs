# Extensões Quarkus - Guia Completo e Didático 🧩

## 📑 Índice

- [Extensões Quarkus - Guia Completo e Didático 🧩](#extensões-quarkus---guia-completo-e-didático-)
  - [📑 Índice](#-índice)
  - [🎯 Introdução - O Que São Extensões?](#-introdução---o-que-são-extensões)
    - [💡 Por Que Isso é Genial?](#-por-que-isso-é-genial)
    - [🏗️ Como Funciona?](#️-como-funciona)
  - [🌐 Web e REST APIs](#-web-e-rest-apis)
    - [`quarkus-rest` (RESTEasy Reactive)](#quarkus-rest-resteasy-reactive)
    - [`quarkus-rest-client` - Cliente HTTP](#quarkus-rest-client---cliente-http)
    - [`quarkus-websockets` - Comunicação em Tempo Real](#quarkus-websockets---comunicação-em-tempo-real)
  - [🗄️ Banco de Dados e Persistência](#️-banco-de-dados-e-persistência)
    - [`quarkus-hibernate-orm-panache` - ORM Simplificado](#quarkus-hibernate-orm-panache---orm-simplificado)
    - [`quarkus-jdbc-postgresql` - Driver PostgreSQL](#quarkus-jdbc-postgresql---driver-postgresql)
  - [🔒 Segurança e Autenticação](#-segurança-e-autenticação)
    - [quarkus-smallrye-jwt](#quarkus-smallrye-jwt)
    - [quarkus-smallrye-jwt-build](#quarkus-smallrye-jwt-build)
    - [quarkus-security-jpa](#quarkus-security-jpa)
    - [quarkus-oidc](#quarkus-oidc)
  - [📊 Observabilidade e Monitoramento](#-observabilidade-e-monitoramento)
    - [quarkus-micrometer-registry-prometheus](#quarkus-micrometer-registry-prometheus)
    - [quarkus-smallrye-health](#quarkus-smallrye-health)
    - [quarkus-logging-json](#quarkus-logging-json)
    - [quarkus-opentelemetry](#quarkus-opentelemetry)
  - [🔄 Processamento Assíncrono e Mensageria](#-processamento-assíncrono-e-mensageria)
    - [quarkus-smallrye-reactive-messaging](#quarkus-smallrye-reactive-messaging)
    - [quarkus-smallrye-reactive-messaging-kafka](#quarkus-smallrye-reactive-messaging-kafka)
    - [quarkus-scheduler](#quarkus-scheduler)
  - [🏗️ Infraestrutura e Deployment](#️-infraestrutura-e-deployment)
    - [quarkus-container-image-docker](#quarkus-container-image-docker)
    - [quarkus-kubernetes](#quarkus-kubernetes)
    - [quarkus-smallrye-openapi](#quarkus-smallrye-openapi)
  - [🧪 Testes](#-testes)
    - [quarkus-junit5](#quarkus-junit5)
    - [quarkus-test-h2](#quarkus-test-h2)
  - [☁️ Cloud e Integração](#️-cloud-e-integração)
    - [quarkus-amazon-lambda](#quarkus-amazon-lambda)
    - [quarkus-redis-client](#quarkus-redis-client)
    - [quarkus-mailer](#quarkus-mailer)
  - [🎯 Como Escolher as Extensões Certas](#-como-escolher-as-extensões-certas)
    - [1. **Para APIs REST simples:**](#1-para-apis-rest-simples)
    - [2. **Para Microserviços completos:**](#2-para-microserviços-completos)
    - [3. **Para aplicações com alta concorrência:**](#3-para-aplicações-com-alta-concorrência)
    - [4. **Para deploy em nuvem:**](#4-para-deploy-em-nuvem)
  - [📋 Comando para Adicionar Extensões](#-comando-para-adicionar-extensões)
  - [💡 Dicas Importantes](#-dicas-importantes)
  - [📚 Introdução Simples](#-introdução-simples)
  - [🏗️ Estrutura Base do Projeto](#️-estrutura-base-do-projeto)
    - [1. Dependências Fundamentais (Obrigatórias)](#1-dependências-fundamentais-obrigatórias)
  - [🌐 Dependências por Categoria de Funcionalidade](#-dependências-por-categoria-de-funcionalidade)
    - [2. APIs REST e Web](#2-apis-rest-e-web)
    - [3. Banco de Dados e Persistência](#3-banco-de-dados-e-persistência)
    - [4. Segurança e Autenticação](#4-segurança-e-autenticação)
    - [5. Observabilidade e Monitoramento](#5-observabilidade-e-monitoramento)
    - [6. Messaging e Comunicação Assíncrona](#6-messaging-e-comunicação-assíncrona)
    - [7. Cache e Performance](#7-cache-e-performance)
    - [8. Testes](#8-testes)
  - [🔧 Plugins Maven Essenciais](#-plugins-maven-essenciais)
  - [🎯 Verificação de Compreensão (Método Feynman - Etapa 3)](#-verificação-de-compreensão-método-feynman---etapa-3)
  - [🚀 Detalhamento Técnico Avançado (Método Feynman - Etapa 4)](#-detalhamento-técnico-avançado-método-feynman---etapa-4)
    - [Perfis de Aplicação por Uso](#perfis-de-aplicação-por-uso)
    - [**Aplicação Web Completa**](#aplicação-web-completa)
    - [**Microserviço Reativo**](#microserviço-reativo)
    - [**API Gateway/Proxy**](#api-gatewayproxy)
    - [Otimizações Específicas para Java 21 + Quarkus 3.24](#otimizações-específicas-para-java-21--quarkus-324)
    - [Native Image Dependencies (GraalVM)](#native-image-dependencies-graalvm)
  - [📋 Checklist de Dependências por Funcionalidade](#-checklist-de-dependências-por-funcionalidade)
    - [✅ REST API Básica](#-rest-api-básica)
    - [✅ Banco de Dados](#-banco-de-dados)
    - [✅ Segurança](#-segurança)
    - [✅ Observabilidade](#-observabilidade)
    - [✅ Testes](#-testes-1)
  - [🎯 Comandos para Inicializar Projeto](#-comandos-para-inicializar-projeto)
  - [⚠️ Notas Importantes para Quarkus 3.24 + Java 21](#️-notas-importantes-para-quarkus-324--java-21)
  - [📚 Recursos Adicionais](#-recursos-adicionais)
  - [📚 Introdução Simples](#-introdução-simples-1)
  - [🏗️ 1. DEPENDÊNCIAS FUNDAMENTAIS](#️-1-dependências-fundamentais)
    - [`quarkus-core` + `quarkus-arc` (CDI)](#quarkus-core--quarkus-arc-cdi)
  - [🌐 2. APIs REST E WEB](#-2-apis-rest-e-web)
    - [`quarkus-rest` (JAX-RS)](#quarkus-rest-jax-rs)
    - [`quarkus-rest-jackson` (Serialização JSON)](#quarkus-rest-jackson-serialização-json)
    - [`quarkus-rest-client` (Cliente REST)](#quarkus-rest-client-cliente-rest)
  - [🗃️ 3. BANCO DE DADOS E PERSISTÊNCIA](#️-3-banco-de-dados-e-persistência)
    - [`quarkus-hibernate-orm` + `quarkus-hibernate-orm-panache`](#quarkus-hibernate-orm--quarkus-hibernate-orm-panache)
    - [`quarkus-jdbc-postgresql` (Driver PostgreSQL)](#quarkus-jdbc-postgresql-driver-postgresql)
    - [`quarkus-flyway` (Migrações)](#quarkus-flyway-migrações)
  - [🔒 4. SEGURANÇA E AUTENTICAÇÃO](#-4-segurança-e-autenticação)
    - [`quarkus-security` + `quarkus-smallrye-jwt`](#quarkus-security--quarkus-smallrye-jwt)
    - [`quarkus-oidc` (OAuth2/OpenID Connect)](#quarkus-oidc-oauth2openid-connect)
  - [📊 5. OBSERVABILIDADE E MONITORAMENTO](#-5-observabilidade-e-monitoramento)
    - [`quarkus-smallrye-health` (Health Checks)](#quarkus-smallrye-health-health-checks)
    - [`quarkus-micrometer-registry-prometheus` (Métricas)](#quarkus-micrometer-registry-prometheus-métricas)
  - [📨 6. MESSAGING E COMUNICAÇÃO ASSÍNCRONA](#-6-messaging-e-comunicação-assíncrona)
    - [`quarkus-smallrye-reactive-messaging-kafka`](#quarkus-smallrye-reactive-messaging-kafka-1)
    - [`quarkus-websockets-next` (WebSockets)](#quarkus-websockets-next-websockets)
  - [🧪 7. TESTES](#-7-testes)
    - [`quarkus-junit5` + `quarkus-junit5-mockito`](#quarkus-junit5--quarkus-junit5-mockito)
  - [🎯 Verificação de Compreensão (Método Feynman - Etapa 3)](#-verificação-de-compreensão-método-feynman---etapa-3-1)
    - [Exercício 1: API REST Básica](#exercício-1-api-rest-básica)
    - [Exercício 2: Segurança](#exercício-2-segurança)
    - [Exercício 3: Monitoramento](#exercício-3-monitoramento)
  - [🔧 Exemplo Completo: E-commerce Simples](#-exemplo-completo-e-commerce-simples)
  - [📚 Resumo das Dependências por Uso](#-resumo-das-dependências-por-uso)

---

## 🎯 Introdução - O Que São Extensões?

**Analogia da Caixa de Ferramentas 🧰:**

Imagine que você está construindo uma casa. Você não precisa de **todas** as ferramentas do mundo, certo? 

- Para fazer um bolo → precisa de **batedeira, forma, forno**
- Para construir uma API → precisa de **REST, JSON, banco de dados**
- Para um microserviço completo → adicione **health checks, métricas, segurança**

**Extensões do Quarkus = Ferramentas Específicas que você escolhe adicionar ao projeto**

### 💡 Por Que Isso é Genial?

| **Abordagem Tradicional** ❌ | **Quarkus com Extensões** ✅ |
|-------------------------------|------------------------------|
| Framework "all-in-one" pesado | Só inclui o que você usa |
| 100MB de dependências | 10-30MB otimizado |
| Startup lento (5-10s) | Startup rápido (0.1-1s) |
| Alto consumo de memória | Baixo consumo (Native: 20-50MB) |

### 🏗️ Como Funciona?

```
SEU PROJETO
│
├─ quarkus-rest          → Cria endpoints REST
├─ quarkus-hibernate     → Conversa com banco de dados
├─ quarkus-security      → Protege sua aplicação
└─ quarkus-smallrye-health → Mostra se está funcionando
```

**Cada extensão:**
1. 🔌 Se "pluga" no Quarkus
2. ⚙️ É otimizada em tempo de compilação
3. 🚀 Resulta em aplicação rápida e leve

---

## 🌐 Web e REST APIs

### `quarkus-rest` (RESTEasy Reactive)

**🎯 O que faz:** Cria APIs REST modernas, reativas e de alta performance.

**📦 Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest</artifactId>
</dependency>
```

**✨ Quando usar:**
- ✅ Criar endpoints HTTP (GET, POST, PUT, DELETE)
- ✅ Construir APIs RESTful
- ✅ Microserviços que expõem dados via JSON

**💻 Exemplo Prático - CRUD Completo:**

```java
@Path("/api/usuarios")  // 🌐 URL base: http://localhost:8080/api/usuarios
@ApplicationScoped
public class UsuarioResource {
    
    @Inject
    UsuarioService service;
    
    // ════════════════════════════════════════════
    // 📋 LISTAR TODOS - GET /api/usuarios
    // ════════════════════════════════════════════
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Usuario> listarTodos() {
        return service.listarTodos();
    }
    
    // ════════════════════════════════════════════
    // 🔍 BUSCAR POR ID - GET /api/usuarios/{id}
    // ════════════════════════════════════════════
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@PathParam("id") Long id) {
        return service.buscarPorId(id)
            .map(usuario -> Response.ok(usuario).build())
            .orElse(Response.status(404).build());
    }
    
    // ════════════════════════════════════════════
    // ➕ CRIAR - POST /api/usuarios
    // ════════════════════════════════════════════
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response criar(Usuario usuario) {
        Usuario criado = service.criar(usuario);
        return Response.status(201)  // 201 = Created
            .entity(criado)
            .build();
    }
    
    // ════════════════════════════════════════════
    // ✏️ ATUALIZAR - PUT /api/usuarios/{id}
    // ════════════════════════════════════════════
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizar(@PathParam("id") Long id, Usuario usuario) {
        return service.atualizar(id, usuario)
            .map(atualizado -> Response.ok(atualizado).build())
            .orElse(Response.status(404).build());
    }
    
    // ════════════════════════════════════════════
    // 🗑️ DELETAR - DELETE /api/usuarios/{id}
    // ════════════════════════════════════════════
    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        boolean deletado = service.deletar(id);
        return deletado 
            ? Response.noContent().build()  // 204 = No Content
            : Response.status(404).build();
    }
}
```

**🎨 Recursos Avançados:**

```java
// Query Parameters: GET /api/usuarios?ativo=true&limite=10
@GET
public List<Usuario> buscar(
    @QueryParam("ativo") @DefaultValue("true") boolean ativo,
    @QueryParam("limite") @DefaultValue("50") int limite) {
    
    return service.buscarComFiltro(ativo, limite);
}

// Headers customizados
@GET
@Path("/{id}")
public Response comHeader(@PathParam("id") Long id) {
    return Response.ok(usuario)
        .header("X-Custom-Header", "valor")
        .header("X-Total-Count", service.count())
        .build();
}

// Status codes personalizados
@POST
public Response criarComValidacao(Usuario usuario) {
    if (usuario.email == null) {
        return Response.status(400)  // Bad Request
            .entity(Map.of("erro", "Email obrigatório"))
            .build();
    }
    // ... criar
}
```

---

### `quarkus-rest-client` - Cliente HTTP

**🎯 O que faz:** Consome APIs REST de outros serviços de forma simples e reativa.

**📦 Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-client</artifactId>
</dependency>
```

**✨ Quando usar:**
- ✅ Chamar APIs externas (GitHub, Google Maps, serviços internos)
- ✅ Integração entre microserviços
- ✅ Consumir dados de terceiros

**💻 Exemplo Prático:**

```java
// ════════════════════════════════════════════
// 1️⃣ DEFINIR INTERFACE DO CLIENTE
// ════════════════════════════════════════════
@Path("/posts")
@RegisterRestClient(configKey = "jsonplaceholder-api")
public interface JsonPlaceholderClient {
    
    @GET
    List<Post> buscarTodos();
    
    @GET
    @Path("/{id}")
    Post buscarPorId(@PathParam("id") Long id);
    
    @POST
    Post criar(Post post);
    
    @DELETE
    @Path("/{id}")
    void deletar(@PathParam("id") Long id);
}

// ════════════════════════════════════════════
// 2️⃣ CONFIGURAR URL (application.properties)
// ════════════════════════════════════════════
// quarkus.rest-client.jsonplaceholder-api.url=https://jsonplaceholder.typicode.com

// ════════════════════════════════════════════
// 3️⃣ USAR O CLIENTE
// ════════════════════════════════════════════
@Path("/external")
@ApplicationScoped
public class ExternalResource {
    
    @RestClient
    JsonPlaceholderClient client;
    
    @GET
    @Path("/posts")
    public List<Post> buscarPostsExternos() {
        // Chamada HTTP automática!
        return client.buscarTodos();
    }
    
    @GET
    @Path("/posts/{id}")
    public Post buscarPost(@PathParam("id") Long id) {
        return client.buscarPorId(id);
    }
}
```

**⚙️ Configurações Avançadas:**

```properties
# Timeout
quarkus.rest-client.jsonplaceholder-api.connect-timeout=5000
quarkus.rest-client.jsonplaceholder-api.read-timeout=10000

# Headers padrão
quarkus.rest-client.jsonplaceholder-api.header.Authorization=Bearer token123
quarkus.rest-client.jsonplaceholder-api.header.User-Agent=MeuApp/1.0

# Proxy
quarkus.rest-client.jsonplaceholder-api.proxy-address=proxy.empresa.com:8080
```

---

### `quarkus-websockets` - Comunicação em Tempo Real

**🎯 O que faz:** Permite comunicação bidirecional e em tempo real entre cliente e servidor.

**📦 Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-websockets</artifactId>
</dependency>
```

**✨ Quando usar:**
- ✅ Chat em tempo real
- ✅ Notificações push
- ✅ Dashboards com atualização automática
- ✅ Jogos multiplayer

**💻 Exemplo - Chat Simples:**

```java
@ServerEndpoint("/chat/{username}")
@ApplicationScoped
public class ChatWebSocket {
    
    // Armazena todas as sessões ativas
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessions.put(username, session);
        broadcast(username + " entrou no chat");
    }
    
    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        String fullMessage = username + ": " + message;
        broadcast(fullMessage);
    }
    
    @OnClose
    public void onClose(@PathParam("username") String username) {
        sessions.remove(username);
        broadcast(username + " saiu do chat");
    }
    
    @OnError
    public void onError(Session session, Throwable error) {
        Log.error("Erro no WebSocket", error);
    }
    
    private void broadcast(String message) {
        sessions.values().forEach(session -> {
            session.getAsyncRemote().sendText(message);
        });
    }
}
```

---

## 🗄️ Banco de Dados e Persistência

### `quarkus-hibernate-orm-panache` - ORM Simplificado

**🎯 O que faz:** Simplifica drasticamente o trabalho com bancos de dados relacionais (PostgreSQL, MySQL, etc.)

**TL;DR:** JPA/Hibernate **SEM** o código chato e repetitivo!

**📦 Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-hibernate-orm-panache</artifactId>
</dependency>
```

**✨ Por que usar Panache?**

| **Hibernate Tradicional** ❌ | **Panache** ✅ |
|------------------------------|----------------|
| `EntityManager` boilerplate | Métodos prontos |
| `@Repository` + interfaces | Extend `PanacheEntity` |
| 50+ linhas de código | 5 linhas |
| Verboso e confuso | Simples e intuitivo |

**💻 Exemplo Prático - Antes vs Depois:**

**ANTES (Hibernate tradicional):**
```java
@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private BigDecimal preco;
    
    // Getters, setters, construtores...
}

@ApplicationScoped
public class ProdutoRepository {
    @Inject
    EntityManager em;
    
    public Produto findById(Long id) {
        return em.find(Produto.class, id);
    }
    
    public List<Produto> listAll() {
        return em.createQuery("SELECT p FROM Produto p", Produto.class)
                 .getResultList();
    }
    
    @Transactional
    public void persist(Produto produto) {
        em.persist(produto);
    }
    // ... mais código repetitivo
}
```

**DEPOIS (Panache):**
```java
@Entity
public class Produto extends PanacheEntity {
    // id já vem do PanacheEntity!
    
    public String nome;
    public BigDecimal preco;
    
    // Pronto! Métodos automáticos:
    // - findById(id)
    // - listAll()
    // - persist()
    // - delete()
    // - count()
}

// USO DIRETO:
Produto produto = Produto.findById(1L);
List<Produto> todos = Produto.listAll();
produto.persist();
```

**🔥 Active Record Pattern - Tudo na Entidade:**

```java
@Entity
@Table(name = "produtos")
public class Produto extends PanacheEntity {
    
    @Column(nullable = false, length = 100)
    public String nome;
    
    @Column(name = "preco_unitario", precision = 10, scale = 2)
    public BigDecimal preco;
    
    @Column(name = "estoque_disponivel")
    public Integer estoque;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    public Categoria categoria;
    
    @Column(name = "ativo")
    public Boolean ativo = true;
    
    @CreationTimestamp
    public LocalDateTime criadoEm;
    
    // ════════════════════════════════════════════
    // 🔍 QUERIES PERSONALIZADAS (Named Queries)
    // ════════════════════════════════════════════
    
    public static List<Produto> buscarPorCategoria(String nomeCategoria) {
        return find("categoria.nome", nomeCategoria).list();
    }
    
    public static List<Produto> buscarCaros(BigDecimal precoMinimo) {
        return find("preco >= ?1 and ativo = true", precoMinimo)
                .list();
    }
    
    public static List<Produto> buscarDisponiveis() {
        return find("estoque > 0 and ativo = true").list();
    }
    
    public static Optional<Produto> buscarPorNome(String nome) {
        return find("nome", nome).firstResultOptional();
    }
    
    // Queries com paginação
    public static PanacheQuery<Produto> buscarComPaginacao(int pagina, int tamanho) {
        return find("ativo = true")
                .page(Page.of(pagina, tamanho));
    }
    
    // Queries com ordenação
    public static List<Produto> buscarOrdenadoPorPreco() {
        return find("ativo = true", Sort.by("preco").descending())
                .list();
    }
    
    // ════════════════════════════════════════════
    // 📊 OPERAÇÕES AGREGADAS
    // ════════════════════════════════════════════
    
    public static long contarAtivos() {
        return count("ativo = true");
    }
    
    public static long contarPorCategoria(Long categoriaId) {
        return count("categoria.id = ?1 and ativo = true", categoriaId);
    }
    
    // ════════════════════════════════════════════
    // 🗑️ DELEÇÃO EM MASSA
    // ════════════════════════════════════════════
    
    public static long deletarInativos() {
        return delete("ativo = false");
    }
    
    public static long deletarSemEstoque() {
        return delete("estoque = 0");
    }
}
```

**📝 Uso no Resource (Controller):**

```java
@Path("/api/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoResource {
    
    // ════════════════════════════════════════════
    // 📋 LISTAR TODOS
    // ════════════════════════════════════════════
    @GET
    public List<Produto> listar() {
        return Produto.listAll();
    }
    
    // ════════════════════════════════════════════
    // 🔍 BUSCAR POR ID
    // ════════════════════════════════════════════
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Produto produto = Produto.findById(id);
        return produto != null 
            ? Response.ok(produto).build()
            : Response.status(404).build();
    }
    
    // ════════════════════════════════════════════
    // ➕ CRIAR
    // ════════════════════════════════════════════
    @POST
    @Transactional  // ⚠️ IMPORTANTE para operações de escrita!
    public Response criar(Produto produto) {
        produto.persist();  // Salva no banco
        return Response.status(201).entity(produto).build();
    }
    
    // ════════════════════════════════════════════
    // ✏️ ATUALIZAR
    // ════════════════════════════════════════════
    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizar(@PathParam("id") Long id, Produto dados) {
        Produto produto = Produto.findById(id);
        if (produto == null) {
            return Response.status(404).build();
        }
        
        // Atualizar campos
        produto.nome = dados.nome;
        produto.preco = dados.preco;
        produto.estoque = dados.estoque;
        
        // persist() automático no fim da transação!
        return Response.ok(produto).build();
    }
    
    // ════════════════════════════════════════════
    // 🗑️ DELETAR
    // ════════════════════════════════════════════
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {
        boolean deletado = Produto.deleteById(id);
        return deletado 
            ? Response.noContent().build()
            : Response.status(404).build();
    }
    
    // ════════════════════════════════════════════
    // 🔎 QUERIES CUSTOMIZADAS
    // ════════════════════════════════════════════
    @GET
    @Path("/categoria/{nome}")
    public List<Produto> buscarPorCategoria(@PathParam("nome") String nome) {
        return Produto.buscarPorCategoria(nome);
    }
    
    @GET
    @Path("/disponiveis")
    public List<Produto> buscarDisponiveis() {
        return Produto.buscarDisponiveis();
    }
    
    @GET
    @Path("/paginado")
    public Response buscarPaginado(
        @QueryParam("pagina") @DefaultValue("0") int pagina,
        @QueryParam("tamanho") @DefaultValue("20") int tamanho) {
        
        PanacheQuery<Produto> query = Produto.buscarComPaginacao(pagina, tamanho);
        
        return Response.ok(Map.of(
            "dados", query.list(),
            "totalPaginas", query.pageCount(),
            "totalItens", query.count()
        )).build();
    }
}
```

**🎯 Repository Pattern (Alternativa ao Active Record):**

```java
// Se preferir separar a lógica de persistência:

@ApplicationScoped
public class ProdutoRepository implements PanacheRepository<Produto> {
    
    public List<Produto> buscarPorNome(String nome) {
        return find("nome LIKE ?1", "%" + nome + "%").list();
    }
    
    public List<Produto> buscarAtivos() {
        return find("ativo = true").list();
    }
    
    public Optional<Produto> buscarPorSku(String sku) {
        return find("sku", sku).firstResultOptional();
    }
}

// Uso:
@Inject
ProdutoRepository repository;

List<Produto> produtos = repository.buscarAtivos();
```

---

### `quarkus-jdbc-postgresql` - Driver PostgreSQL

**🎯 O que faz:** Conecta sua aplicação ao banco de dados PostgreSQL.

**📦 Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-postgresql</artifactId>
</dependency>
```

**⚙️ Configuração (`application.properties`):**

```properties
# ════════════════════════════════════════════════
# 🐘 POSTGRESQL - CONFIGURAÇÃO COMPLETA
# ════════════════════════════════════════════════

# Tipo de banco
quarkus.datasource.db-kind=postgresql

# Credenciais
quarkus.datasource.username=postgres
quarkus.datasource.password=${DB_PASSWORD:postgres}  # Variável de ambiente

# URL de conexão
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/meu_banco

# Pool de conexões (Agroal)
quarkus.datasource.jdbc.min-size=5
quarkus.datasource.jdbc.max-size=20
quarkus.datasource.jdbc.acquisition-timeout=10s

# ════════════════════════════════════════════════
# 🏗️ HIBERNATE - GERAÇÃO DE SCHEMA
# ════════════════════════════════════════════════

# Estratégias:
# - none: Não faz nada (PRODUÇÃO)
# - create: Cria schema (perde dados!)
# - drop-and-create: Apaga e recria (DEV)
# - update: Atualiza schema (DEV/TEST)
# - validate: Só valida (PRODUÇÃO)

%dev.quarkus.hibernate-orm.database.generation=drop-and-create
%test.quarkus.hibernate-orm.database.generation=drop-and-create
%prod.quarkus.hibernate-orm.database.generation=none

# Logs SQL
%dev.quarkus.hibernate-orm.log.sql=true
%dev.quarkus.hibernate-orm.log.format-sql=true
%dev.quarkus.hibernate-orm.log.jdbc-warnings=true

# Dialeto (normalmente detectado automaticamente)
quarkus.hibernate-orm.dialect=org.hibernate.dialect.PostgreSQLDialect
```

**🐳 Docker Compose para PostgreSQL:**

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    container_name: postgres-dev
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: meu_banco
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - app-network

volumes:
  postgres_data:

networks:
  app-network:
```

**Outros drivers disponíveis:**

```xml
<!-- MySQL -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-mysql</artifactId>
</dependency>

<!-- MariaDB -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-mariadb</artifactId>
</dependency>

<!-- H2 (desenvolvimento/testes) -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-h2</artifactId>
</dependency>
```

---

## 🔒 Segurança e Autenticação

### quarkus-smallrye-jwt

**O que faz:** Validação e processamento de tokens JWT seguindo o padrão MicroProfile.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-jwt</artifactId>
</dependency>

```

**Exemplo de Uso:**

```java
@Path("/protegido")
@ApplicationScoped
public class RecursoProtegido {

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed("admin")
    public String acessoAdmin() {
        return "Usuário: " + jwt.getName();
    }
}

```

**Configuração:**

```
mp.jwt.verify.publickey.location=META-INF/resources/publickey.pem
mp.jwt.verify.issuer=https://meuservidor.com

```

### quarkus-smallrye-jwt-build

**O que faz:** Criação e assinatura de tokens JWT.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-jwt-build</artifactId>
</dependency>

```

**Exemplo de Uso:**

```java
String token = Jwt.issuer("https://meuapp.com")
    .upn("usuario@email.com")
    .groups(Set.of("admin", "user"))
    .expiresAt(System.currentTimeMillis() + 3600)
    .sign();

```

### quarkus-security-jpa

**O que faz:** Autenticação usando dados armazenados em banco via JPA.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-security-jpa</artifactId>
</dependency>

```

### quarkus-oidc

**O que faz:** Integração com provedores OpenID Connect (Keycloak, Auth0, etc.).

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-oidc</artifactId>
</dependency>

```

---

## 📊 Observabilidade e Monitoramento

### quarkus-micrometer-registry-prometheus

**O que faz:** Métricas de aplicação no formato Prometheus.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-micrometer-registry-prometheus</artifactId>
</dependency>

```

**Exemplo de Uso:**

```java
@ApplicationScoped
public class MetricasService {

    @Counted(name = "requests_total", description = "Total de requests")
    @Timed(name = "request_duration", description = "Duração dos requests")
    public String processarRequest() {
        return "Processado";
    }
}

```

### quarkus-smallrye-health

**O que faz:** Health checks para verificar saúde da aplicação.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-health</artifactId>
</dependency>

```

**Exemplo de Uso:**

```java
@ApplicationScoped
public class DatabaseHealthCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        // Verificar conexão com banco
        boolean isHealthy = verificarConexaoBanco();

        return HealthCheckResponse.named("database")
            .status(isHealthy)
            .withData("connections", 5)
            .build();
    }
}

```

### quarkus-logging-json

**O que faz:** Logs estruturados em formato JSON.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-logging-json</artifactId>
</dependency>

```

### quarkus-opentelemetry

**O que faz:** Tracing distribuído para monitorar requests entre microserviços.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-opentelemetry</artifactId>
</dependency>

```

---

## 🔄 Processamento Assíncrono e Mensageria

### quarkus-smallrye-reactive-messaging

**O que faz:** Processamento de streams de dados e integração com sistemas de mensageria.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-reactive-messaging</artifactId>
</dependency>

```

**Exemplo de Uso:**

```java
@ApplicationScoped
public class ProcessadorMensagens {

    @Incoming("pedidos-entrada")
    @Outgoing("pedidos-processados")
    public Pedido processarPedido(Pedido pedido) {
        // Processar pedido
        pedido.setStatus("PROCESSADO");
        return pedido;
    }
}

```

### quarkus-smallrye-reactive-messaging-kafka

**O que faz:** Integração com Apache Kafka para mensageria distribuída.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-reactive-messaging-kafka</artifactId>
</dependency>

```

**Configuração:**

```
mp.messaging.incoming.pedidos.connector=smallrye-kafka
mp.messaging.incoming.pedidos.topic=pedidos-topic
mp.messaging.incoming.pedidos.bootstrap.servers=localhost:9092

```

### quarkus-scheduler

**O que faz:** Agendamento de tarefas (cron jobs).

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-scheduler</artifactId>
</dependency>

```

**Exemplo de Uso:**

```java
@ApplicationScoped
public class TarefasAgendadas {

    @Scheduled(cron = "0 0 2 * * ?") // Todo dia às 2h
    public void limpezaDiaria() {
        log.info("Executando limpeza diária");
    }

    @Scheduled(every = "30s")
    public void verificacaoSaude() {
        // Verificação a cada 30 segundos
    }
}

```

---

## 🏗️ Infraestrutura e Deployment

### quarkus-container-image-docker

**O que faz:** Gera imagens Docker automaticamente durante o build.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-container-image-docker</artifactId>
</dependency>

```

**Uso:**

```bash
./mvnw clean package -Dquarkus.container-image.build=true

```

### quarkus-kubernetes

**O que faz:** Gera manifestos Kubernetes automaticamente.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-kubernetes</artifactId>
</dependency>
```

### quarkus-smallrye-openapi

**O que faz:** Documentação automática da API usando OpenAPI 3.0.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-openapi</artifactId>
</dependency>

```

**Ou via CLI:**

```bash
./mvnw quarkus:add-extension -Dextensions="smallrye-openapi"
```

**Resultado:** Swagger UI disponível em `http://localhost:8080/q/swagger-ui/`

Personalizar no application.properties

```bash
# application.properties

# Caminho do OpenAPI JSON/YAML
quarkus.smallrye-openapi.path=/openapi

# Swagger UI sempre habilitado (mesmo em prod)
quarkus.swagger-ui.always-include=true
quarkus.swagger-ui.path=/docs
```

Assim, a api fica em:

- `http://localhost:8080/openapi`
- `http://localhost:8080/docs`

---

## 🧪 Testes

### quarkus-junit5

**O que faz:** Suporte para testes unitários e de integração.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-junit5</artifactId>
    <scope>test</scope>
</dependency>

```

**Exemplo de Uso:**

```java
@QuarkusTest
class UsuarioResourceTest {

    @Test
    public void testListarUsuarios() {
        given()
          .when().get("/usuarios")
          .then()
             .statusCode(200)
             .body("size()", greaterThan(0));
    }
}

```

### quarkus-test-h2

**O que faz:** Banco H2 em memória para testes.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-test-h2</artifactId>
    <scope>test</scope>
</dependency>

```

---

## ☁️ Cloud e Integração

### quarkus-amazon-lambda

**O que faz:** Execução em AWS Lambda com cold start otimizado.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-amazon-lambda</artifactId>
</dependency>

```

### quarkus-redis-client

**O que faz:** Cliente reativo para Redis.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-redis-client</artifactId>
</dependency>

```

### quarkus-mailer

**O que faz:** Envio de emails com templates.

**Maven Dependency:**

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-mailer</artifactId>
</dependency>

```

---

## 🎯 Como Escolher as Extensões Certas

### 1. **Para APIs REST simples:**

- `quarkus-rest`
- `quarkus-hibernate-orm-panache`
- `quarkus-jdbc-postgresql`

### 2. **Para Microserviços completos:**

- Adicione: `quarkus-smallrye-health`
- Adicione: `quarkus-micrometer-registry-prometheus`
- Adicione: `quarkus-smallrye-openapi`

### 3. **Para aplicações com alta concorrência:**

- `quarkus-reactive-pg-client` (ao invés do JDBC)
- `quarkus-smallrye-reactive-messaging`

### 4. **Para deploy em nuvem:**

- `quarkus-container-image-docker`
- `quarkus-kubernetes`

---

## 📋 Comando para Adicionar Extensões

**Via Quarkus CLI:**

```bash
quarkus ext add hibernate-orm-panache jdbc-postgresql rest

```

**Via Maven:**

```bash
./mvnw quarkus:add-extension -Dextensions="hibernate-orm-panache,jdbc-postgresql,rest"

```

**Via Gradle:**

```bash
./gradlew addExtension --extensions="hibernate-orm-panache,jdbc-postgresql,rest"

```

---

## 💡 Dicas Importantes

1. **Menos é Mais:** Use apenas as extensões necessárias para manter a aplicação leve
2. **Reativo vs Tradicional:** Prefira extensões reativas para alta performance
3. **Desenvolvimento vs Produção:** Algumas extensões são apenas para dev (como h2)
4. **Compatibilidade:** Nem todas as extensões funcionam bem juntas - consulte a documentação

Este guia oferece uma base sólida para começar com Quarkus. Conforme você avança, pode explorar extensões mais específicas para suas necessidades.

---

## 📚 Introdução Simples

**O que são dependências?**
Pense nas dependências como "ingredientes" para sua aplicação. Assim como uma receita de bolo precisa de farinha, ovos e açúcar, sua aplicação Quarkus precisa de bibliotecas específicas para funcionar.

**Por que o Quarkus é especial?**
O Quarkus é como um "chef experiente" que sabe exatamente quais ingredientes usar e na quantidade certa, tornando sua aplicação mais rápida e eficiente.

---

## 🏗️ Estrutura Base do Projeto

### 1. Dependências Fundamentais (Obrigatórias)

```xml
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <maven.compiler.release>21</maven.compiler.release>
    <quarkus.platform.version>3.24.2</quarkus.platform.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <surefire-plugin.version>3.2.5</surefire-plugin.version>
    <failsafe-plugin.version>3.2.5</failsafe-plugin.version>
</properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.quarkus.platform</groupId>
            <artifactId>quarkus-bom</artifactId>
            <version>${quarkus.platform.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <!-- Core Quarkus - SEMPRE necessário -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-core</artifactId>
    </dependency>

    <!-- Injeção de Dependência (ArC) - SEMPRE necessário -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-arc</artifactId>
    </dependency>
</dependencies>

```

---

## 🌐 Dependências por Categoria de Funcionalidade

### 2. APIs REST e Web

```xml
<!-- REST API com JAX-RS -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest</artifactId>
</dependency>

<!-- Serialização JSON -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>

<!-- Validação de dados -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-hibernate-validator</artifactId>
</dependency>

<!-- Cliente REST -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-client</artifactId>
</dependency>

<!-- OpenAPI/Swagger -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-openapi</artifactId>
</dependency>

```

### 3. Banco de Dados e Persistência

```xml
<!-- Hibernate ORM (JPA) -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-hibernate-orm</artifactId>
</dependency>

<!-- Panache (simplifica JPA) -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-hibernate-orm-panache</artifactId>
</dependency>

<!-- Pool de conexões -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-agroal</artifactId>
</dependency>

<!-- Driver PostgreSQL -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-postgresql</artifactId>
</dependency>

<!-- OU Driver MySQL -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-mysql</artifactId>
</dependency>

<!-- OU Driver H2 (desenvolvimento) -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-h2</artifactId>
</dependency>

<!-- Flyway (migrações) -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-flyway</artifactId>
</dependency>

```

### 4. Segurança e Autenticação

```xml
<!-- Segurança básica -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-security</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-jwt</artifactId>
</dependency>

<!-- OIDC (OAuth2/OpenID Connect) -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-oidc</artifactId>
</dependency>

<!-- Autenticação básica -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-elytron-security-properties-file</artifactId>
</dependency>

```

### 5. Observabilidade e Monitoramento

```xml
<!-- Health Checks -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-health</artifactId>
</dependency>

<!-- Métricas -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-micrometer-registry-prometheus</artifactId>
</dependency>

<!-- OpenTelemetry -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-opentelemetry</artifactId>
</dependency>

<!-- Logging estruturado -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-logging-json</artifactId>
</dependency>

```

### 6. Messaging e Comunicação Assíncrona

```xml
<!-- Reactive Messaging -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-reactive-messaging</artifactId>
</dependency>

<!-- Kafka -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-reactive-messaging-kafka</artifactId>
</dependency>

<!-- RabbitMQ -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-reactive-messaging-rabbitmq</artifactId>
</dependency>

<!-- WebSockets -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-websockets-next</artifactId>
</dependency>

```

### 7. Cache e Performance

```xml
<!-- Cache -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-cache</artifactId>
</dependency>

<!-- Redis -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-redis-client</artifactId>
</dependency>

```

### 8. Testes

```xml
<!-- JUnit 5 + Quarkus Test -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-junit5</artifactId>
    <scope>test</scope>
</dependency>

<!-- Mockito -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-junit5-mockito</artifactId>
    <scope>test</scope>
</dependency>

<!-- TestContainers -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-test-h2</artifactId>
    <scope>test</scope>
</dependency>

<!-- REST Assured -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <scope>test</scope>
</dependency>

```

---

## 🔧 Plugins Maven Essenciais

```xml
<build>
    <plugins>
        <!-- Plugin principal do Quarkus -->
        <plugin>
            <groupId>io.quarkus.platform</groupId>
            <artifactId>quarkus-maven-plugin</artifactId>
            <version>${quarkus.platform.version}</version>
            <extensions>true</extensions>
            <executions>
                <execution>
                    <goals>
                        <goal>build</goal>
                        <goal>generate-code</goal>
                        <goal>generate-code-tests</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>

        <!-- Compilador Java -->
        <plugin>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.12.1</version>
            <configuration>
                <release>21</release>
                <parameters>true</parameters>
            </configuration>
        </plugin>

        <!-- Testes unitários -->
        <plugin>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>${surefire-plugin.version}</version>
            <configuration>
                <systemPropertyVariables>
                    <java.util.logging.manager>org.jboss.logmanager.LogManager</java.util.logging.manager>
                    <maven.home>${maven.home}</maven.home>
                </systemPropertyVariables>
            </configuration>
        </plugin>

        <!-- Testes de integração -->
        <plugin>
            <artifactId>maven-failsafe-plugin</artifactId>
            <version>${failsafe-plugin.version}</version>
            <executions>
                <execution>
                    <goals>
                        <goal>integration-test</goal>
                        <goal>verify</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <systemPropertyVariables>
                    <native.image.path>${project.build.directory}/${project.build.finalName}-runner</native.image.path>
                    <java.util.logging.manager>org.jboss.logmanager.LogManager</java.util.logging.manager>
                    <maven.home>${maven.home}</maven.home>
                </systemPropertyVariables>
            </configuration>
        </plugin>
    </plugins>
</build>

```

---

## 🎯 Verificação de Compreensão (Método Feynman - Etapa 3)

**Teste seu entendimento:**

1. **Por que usar `quarkus-bom`?**
    - Garante compatibilidade entre todas as extensões
    - Evita conflitos de versão
    - Simplifica o gerenciamento de dependências
2. **Qual a diferença entre `quarkus-rest` e `quarkus-rest-client`?**
    - `quarkus-rest`: para criar APIs REST (servidor)
    - `quarkus-rest-client`: para consumir APIs REST (cliente)
3. **Quando usar Panache?**
    - Quando você quer JPA mais simples
    - Para reduzir código boilerplate
    - Para operações de banco de dados mais intuitivas

---

## 🚀 Detalhamento Técnico Avançado (Método Feynman - Etapa 4)

### Perfis de Aplicação por Uso

### **Aplicação Web Completa**

```xml
<!-- Stack completo para web app -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-hibernate-orm-panache</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-postgresql</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-openapi</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-health</artifactId>
</dependency>

```

### **Microserviço Reativo**

```xml
<!-- Stack para aplicação reativa -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-reactive-routes</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-hibernate-reactive-panache</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-reactive-messaging-kafka</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-vertx</artifactId>
</dependency>

```

### **API Gateway/Proxy**

```xml
<!-- Stack para gateway -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-client</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-fault-tolerance</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-micrometer-registry-prometheus</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-jwt</artifactId>
</dependency>

```

### Otimizações Específicas para Java 21 + Quarkus 3.24

```xml
<!-- Configurações específicas para Java 21 -->
<plugin>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.12.1</version>
    <configuration>
        <release>21</release>
        <parameters>true</parameters>
        <compilerArgs>
            <arg>--enable-preview</arg> <!-- Se usar recursos preview -->
        </compilerArgs>
    </configuration>
</plugin>

```

### Native Image Dependencies (GraalVM)

```xml
<!-- Para compilação nativa -->
<profiles>
    <profile>
        <id>native</id>
        <activation>
            <property>
                <name>native</name>
            </property>
        </activation>
        <properties>
            <skipITs>false</skipITs>
            <quarkus.native.enabled>true</quarkus.native.enabled>
        </properties>
    </profile>
</profiles>

```

---

## 📋 Checklist de Dependências por Funcionalidade

### ✅ REST API Básica

- [x]  `quarkus-rest`
- [x]  `quarkus-rest-jackson`
- [x]  `quarkus-hibernate-validator`

### ✅ Banco de Dados

- [x]  `quarkus-hibernate-orm-panache`
- [x]  `quarkus-jdbc-[driver]`
- [x]  `quarkus-flyway` (migrações)

### ✅ Segurança

- [x]  `quarkus-security`
- [x]  `quarkus-oidc` ou `quarkus-smallrye-jwt`

### ✅ Observabilidade

- [x]  `quarkus-smallrye-health`
- [x]  `quarkus-micrometer-registry-prometheus`
- [x]  `quarkus-opentelemetry`

### ✅ Testes

- [x]  `quarkus-junit5`
- [x]  `quarkus-junit5-mockito`
- [x]  `rest-assured`

---

## 🎯 Comandos para Inicializar Projeto

```bash
# Criar projeto com dependências básicas
mvn io.quarkus.platform:quarkus-maven-plugin:3.24.2:create \
    -DprojectGroupId=com.exemplo \
    -DprojectArtifactId=minha-app \
    -DclassName="com.exemplo.RecursoExemplo" \
    -Dpath="/hello" \
    -Dextensions="quarkus-rest,quarkus-rest-jackson,quarkus-hibernate-orm-panache,quarkus-jdbc-postgresql"

# Adicionar extensão após criação
./mvnw quarkus:add-extension -Dextensions="smallrye-openapi"

# Executar em modo dev
./mvnw quarkus:dev

# Compilar para nativo
./mvnw package -Dnative

```

---

## ⚠️ Notas Importantes para Quarkus 3.24 + Java 21

1. **Compatibilidade**: O Quarkus suporta Java 21, mas mantém Java 17 como baseline mínima
2. **Performance**: Java 21 com Virtual Threads melhora significativamente aplicações reativas
3. **GraalVM**: Use GraalVM for JDK 21 para compilação nativa otimizada
4. **Dependências**: Sempre use o BOM do Quarkus para evitar conflitos de versão

---

## 📚 Recursos Adicionais

- [Documentação Oficial Quarkus](https://quarkus.io/guides/)
- [Extensões Disponíveis](https://code.quarkus.io/)
- [Quarkus Platform BOM](https://mvnrepository.com/artifact/io.quarkus.platform/quarkus-bom)
- [Migration Guide](https://github.com/quarkusio/quarkus/wiki/Migration-Guides)

---

## 📚 Introdução Simples

Imagine que você está construindo uma casa. Cada dependência do Quarkus é como uma ferramenta específica:

- **quarkus-rest** = martelo (para construir APIs)
- **quarkus-hibernate-orm** = furadeira (para trabalhar com banco de dados)
- **quarkus-security** = sistema de alarme (para proteger sua aplicação)

Vamos ver como usar cada "ferramenta" na prática!

---

## 🏗️ 1. DEPENDÊNCIAS FUNDAMENTAIS

### `quarkus-core` + `quarkus-arc` (CDI)

**O que faz:** Base do Quarkus + Sistema de Injeção de Dependência
**Quando usar:** SEMPRE - são obrigatórias

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-core</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-arc</artifactId>
</dependency>

```

**Exemplo prático:**

```java
// 1. Criando um serviço
@ApplicationScoped  // Esta anotação vem do quarkus-arc
public class ProdutoService {

    public List<String> listarProdutos() {
        return List.of("Notebook", "Mouse", "Teclado");
    }
}

// 2. Injetando o serviço em outro lugar
@Path("/produtos")
public class ProdutoResource {

    @Inject  // Injeção de dependência automática
    ProdutoService produtoService;

    @GET
    public List<String> buscarTodos() {
        return produtoService.listarProdutos();
    }
}

```

**Explicação simples:** O `@Inject` é como dizer "Quarkus, me dê uma instância do ProdutoService". O Quarkus automaticamente cria e gerencia essas instâncias para você.

---

## 🌐 2. APIs REST E WEB

### `quarkus-rest` (JAX-RS)

**O que faz:** Cria endpoints REST (APIs que respondem HTTP)
**Quando usar:** Para criar APIs REST, microserviços

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest</artifactId>
</dependency>

```

**Exemplo prático:**

```java
@Path("/api/usuarios")  // Define a URL base
@Produces(MediaType.APPLICATION_JSON)  // Resposta em JSON
@Consumes(MediaType.APPLICATION_JSON)  // Aceita JSON
public class UsuarioResource {

    // GET /api/usuarios
    @GET
    public Response listarTodos() {
        List<String> usuarios = List.of("João", "Maria", "Pedro");
        return Response.ok(usuarios).build();
    }

    // GET /api/usuarios/{id}
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Response.ok("Usuário ID: " + id).build();
    }

    // POST /api/usuarios
    @POST
    public Response criar(Usuario usuario) {
        // Lógica para salvar
        return Response.status(201).entity(usuario).build();
    }

    // PUT /api/usuarios/{id}
    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, Usuario usuario) {
        // Lógica para atualizar
        return Response.ok(usuario).build();
    }

    // DELETE /api/usuarios/{id}
    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        // Lógica para deletar
        return Response.noContent().build();
    }
}

```

### `quarkus-rest-jackson` (Serialização JSON)

**O que faz:** Converte objetos Java em JSON e vice-versa
**Quando usar:** Sempre que trabalhar com APIs REST

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-jackson</artifactId>
</dependency>

```

**Exemplo prático:**

```java
// 1. Classe modelo
public class Usuario {
    @JsonProperty("nome_completo")  // Nome diferente no JSON
    public String nome;

    @JsonIgnore  // Este campo não aparece no JSON
    public String senha;

    public LocalDateTime criadoEm;

    // Construtor, getters, setters...
}

// 2. Configuração personalizada de JSON
@ApplicationScoped
public class JacksonConfig {

    @Produces
    @Singleton
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());
    }
}

// 3. No Resource
@POST
public Response criarUsuario(Usuario usuario) {
    // O Quarkus automaticamente converte JSON -> Usuario
    // e Usuario -> JSON na resposta
    return Response.ok(usuario).build();
}

```

### `quarkus-rest-client` (Cliente REST)

**O que faz:** Consome APIs REST de outros serviços
**Quando usar:** Para chamar APIs externas

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-rest-client</artifactId>
</dependency>

```

**Exemplo prático:**

```java
// 1. Interface do cliente
@Path("/posts")
@RegisterRestClient(configKey = "jsonplaceholder-api")
public interface JsonPlaceholderClient {

    @GET
    List<Post> buscarTodos();

    @GET
    @Path("/{id}")
    Post buscarPorId(@PathParam("id") Long id);

    @POST
    Post criar(Post post);
}

// 2. Configuração no application.properties
// quarkus.rest-client.jsonplaceholder-api.url=https://jsonplaceholder.typicode.com

// 3. Usando o cliente
@Path("/external")
public class ExternalResource {

    @RestClient
    JsonPlaceholderClient client;

    @GET
    @Path("/posts")
    public List<Post> buscarPostsExternos() {
        return client.buscarTodos();
    }
}

```

---

## 🗃️ 3. BANCO DE DADOS E PERSISTÊNCIA

### `quarkus-hibernate-orm` + `quarkus-hibernate-orm-panache`

**O que faz:** ORM (mapeamento objeto-relacional) + simplificação do JPA
**Quando usar:** Para trabalhar com bancos de dados relacionais

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-hibernate-orm-panache</artifactId>
</dependency>

```

**Exemplo prático:**

```java
// 1. Entidade tradicional com Panache
@Entity
@Table(name = "produtos")
public class Produto extends PanacheEntity {
    // PanacheEntity já inclui o campo 'id'

    @Column(nullable = false)
    public String nome;

    @Column(name = "preco_unitario")
    public BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    public Categoria categoria;

    // Métodos personalizados
    public static List<Produto> buscarPorCategoria(String categoria) {
        return find("categoria.nome", categoria).list();
    }

    public static List<Produto> buscarCaros() {
        return find("preco > ?1", new BigDecimal("1000")).list();
    }
}

// 2. Repository Pattern (alternativa)
@ApplicationScoped
public class ProdutoRepository implements PanacheRepository<Produto> {

    public List<Produto> buscarPorNome(String nome) {
        return find("nome LIKE ?1", "%" + nome + "%").list();
    }

    public List<Produto> buscarAtivos() {
        return find("ativo", true).list();
    }
}

// 3. Usando no Resource
@Path("/produtos")
@Transactional  // Para operações de escrita
public class ProdutoResource {

    @Inject
    ProdutoRepository repository;

    @GET
    public List<Produto> listarTodos() {
        return Produto.listAll();  // Método do Panache
    }

    @GET
    @Path("/{id}")
    public Produto buscarPorId(@PathParam("id") Long id) {
        return Produto.findById(id);
    }

    @POST
    public Response criar(Produto produto) {
        produto.persist();  // Salva no banco
        return Response.status(201).entity(produto).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, Produto produto) {
        Produto existente = Produto.findById(id);
        if (existente == null) {
            return Response.status(404).build();
        }
        existente.nome = produto.nome;
        existente.preco = produto.preco;
        // persist() automático no final da transação
        return Response.ok(existente).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        boolean deleted = Produto.deleteById(id);
        return deleted ? Response.noContent().build()
                      : Response.status(404).build();
    }
}

```

### `quarkus-jdbc-postgresql` (Driver PostgreSQL)

**O que faz:** Conecta com banco PostgreSQL
**Quando usar:** Quando usar PostgreSQL como banco

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-postgresql</artifactId>
</dependency>

```

**Configuração no application.properties:**

```
# Configuração do banco
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=postgres
quarkus.datasource.password=senha123
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/meudb

# Hibernate
quarkus.hibernate-orm.database.generation=update
quarkus.hibernate-orm.log.sql=true

```

### `quarkus-flyway` (Migrações)

**O que faz:** Versionamento e migração de banco de dados
**Quando usar:** Para controlar mudanças no esquema do banco

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-flyway</artifactId>
</dependency>

```

**Exemplo prático:**

```sql
-- src/main/resources/db/migration/V1__Create_produtos_table.sql
CREATE TABLE produtos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    preco_unitario DECIMAL(10,2),
    ativo BOOLEAN DEFAULT true,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- V2__Add_categoria_table.sql
CREATE TABLE categorias (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
);

ALTER TABLE produtos ADD COLUMN categoria_id BIGINT REFERENCES categorias(id);

```

**Configuração:**

```
# Flyway
quarkus.flyway.migrate-at-start=true
quarkus.flyway.locations=classpath:db/migration

```

---

## 🔒 4. SEGURANÇA E AUTENTICAÇÃO

### `quarkus-security` + `quarkus-smallrye-jwt`

**O que faz:** Sistema de segurança + autenticação JWT
**Quando usar:** Para proteger APIs com tokens JWT

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-security</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-jwt</artifactId>
</dependency>

```

**Exemplo prático:**

```java
// 1. Protegendo recursos
@Path("/admin")
@RolesAllowed("admin")  // Só usuários com role 'admin'
public class AdminResource {

    @GET
    @Path("/usuarios")
    public List<Usuario> listarUsuarios() {
        return Usuario.listAll();
    }
}

// 2. Diferentes níveis de segurança
@Path("/api")
public class SecureResource {

    @Inject
    JsonWebToken jwt;  // Token JWT atual

    @Inject
    SecurityIdentity identity;  // Identidade do usuário

    @GET
    @Path("/publico")
    @PermitAll  // Acesso livre
    public String endpointPublico() {
        return "Qualquer um pode acessar";
    }

    @GET
    @Path("/autenticado")
    @RolesAllowed({"user", "admin"})  // Precisa estar logado
    public String endpointAutenticado() {
        return "Olá, " + identity.getPrincipal().getName();
    }

    @GET
    @Path("/admin-only")
    @RolesAllowed("admin")  // Só administradores
    public String somenteAdmin() {
        return "Área administrativa";
    }

    @GET
    @Path("/info-token")
    @RolesAllowed({"user", "admin"})
    public String infoToken() {
        return String.format("Usuário: %s, Grupos: %s, Expira em: %s",
            jwt.getName(),
            jwt.getGroups(),
            new Date(jwt.getExpirationTime()));
    }
}

// 3. Classe para gerar tokens (para testes)
@ApplicationScoped
public class TokenService {

    public String gerarToken(String usuario, Set<String> roles) {
        return Jwt.issuer("https://meuapp.com")
                .upn(usuario)
                .groups(roles)
                .expiresAt(Instant.now().plusSeconds(3600))  // 1 hora
                .sign();
    }
}

```

**Configuração JWT:**

```
# JWT
mp.jwt.verify.publickey.location=META-INF/resources/publicKey.pem
mp.jwt.verify.issuer=https://meuapp.com
quarkus.smallrye-jwt.enabled=true

```

### `quarkus-oidc` (OAuth2/OpenID Connect)

**O que faz:** Integração com provedores OAuth2 (Google, GitHub, Keycloak)
**Quando usar:** Para login social ou integração com Keycloak

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-oidc</artifactId>
</dependency>

```

**Exemplo prático:**

```java
// 1. Informações do usuário logado
@Path("/profile")
public class ProfileResource {

    @Inject
    SecurityIdentity identity;

    @Inject
    IdToken idToken;  // Token do OpenID Connect

    @GET
    @RolesAllowed("user")
    public Map<String, Object> meuPerfil() {
        return Map.of(
            "nome", identity.getPrincipal().getName(),
            "email", idToken.getClaim("email"),
            "avatar", idToken.getClaim("picture"),
            "roles", identity.getRoles()
        );
    }
}

```

**Configuração OIDC:**

```
# OIDC (exemplo com Keycloak)
quarkus.oidc.auth-server-url=http://localhost:8180/realms/myrealm
quarkus.oidc.client-id=backend-service
quarkus.oidc.credentials.secret=secret

```

---

## 📊 5. OBSERVABILIDADE E MONITORAMENTO

### `quarkus-smallrye-health` (Health Checks)

**O que faz:** Endpoints para verificar saúde da aplicação
**Quando usar:** Para monitoramento e orquestração (Kubernetes)

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-health</artifactId>
</dependency>

```

**Exemplo prático:**

```java
// 1. Health check personalizado
@ApplicationScoped
public class DatabaseHealthCheck implements HealthCheck {

    @Inject
    DataSource dataSource;

    @Override
    public HealthCheckResponse call() {
        try (Connection conn = dataSource.getConnection()) {
            // Testa conexão com banco
            conn.prepareStatement("SELECT 1").executeQuery();

            return HealthCheckResponse.up("database")
                    .withData("connection", "active")
                    .withData("url", conn.getMetaData().getURL())
                    .build();
        } catch (Exception e) {
            return HealthCheckResponse.down("database")
                    .withData("error", e.getMessage())
                    .build();
        }
    }
}

// 2. Health check de serviço externo
@Readiness  // Verifica se está pronto para receber tráfego
@ApplicationScoped
public class ExternalServiceHealthCheck implements HealthCheck {

    @RestClient
    ExternalApiClient client;

    @Override
    public HealthCheckResponse call() {
        try {
            client.ping();  // Chama API externa
            return HealthCheckResponse.up("external-api");
        } catch (Exception e) {
            return HealthCheckResponse.down("external-api")
                    .withData("error", e.getMessage());
        }
    }
}

// 3. Health check de inicialização
@Startup  // Verifica se inicializou corretamente
@ApplicationScoped
public class StartupHealthCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        // Verifica se recursos necessários estão disponíveis
        boolean initialized = checkRequiredResources();

        return initialized
            ? HealthCheckResponse.up("startup")
            : HealthCheckResponse.down("startup");
    }

    private boolean checkRequiredResources() {
        // Lógica de verificação
        return true;
    }
}

```

**Endpoints automáticos:**

```
GET /q/health          # Health geral
GET /q/health/live     # Liveness (processo vivo)
GET /q/health/ready    # Readiness (pronto para tráfego)
GET /q/health/started  # Startup (inicialização ok)

```

### `quarkus-micrometer-registry-prometheus` (Métricas)

**O que faz:** Coleta métricas da aplicação para Prometheus
**Quando usar:** Para monitoramento de performance

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-micrometer-registry-prometheus</artifactId>
</dependency>

```

**Exemplo prático:**

```java
// 1. Métricas personalizadas
@ApplicationScoped
public class ProdutoService {

    private final Counter produtosCriados;
    private final Timer tempoBusca;
    private final Gauge produtosAtivos;

    public ProdutoService(MeterRegistry registry) {
        this.produtosCriados = Counter.builder("produtos.criados.total")
                .description("Total de produtos criados")
                .register(registry);

        this.tempoBusca = Timer.builder("produtos.busca.tempo")
                .description("Tempo de busca de produtos")
                .register(registry);

        this.produtosAtivos = Gauge.builder("produtos.ativos.quantidade")
                .description("Quantidade de produtos ativos")
                .register(registry, this, ProdutoService::contarProdutosAtivos);
    }

    public Produto criarProduto(Produto produto) {
        produto.persist();
        produtosCriados.increment();  // Incrementa contador
        return produto;
    }

    public List<Produto> buscarTodos() {
        return tempoBusca.recordCallable(() -> {  // Mede tempo
            return Produto.listAll();
        });
    }

    private double contarProdutosAtivos() {
        return Produto.count("ativo = true");
    }
}

// 2. Usando anotações
@ApplicationScoped
public class UsuarioService {

    @Counted(value = "usuario.login.tentativas", description = "Tentativas de login")
    public boolean login(String usuario, String senha) {
        // Lógica de login
        return validarCredenciais(usuario, senha);
    }

    @Timed(value = "usuario.busca.tempo", description = "Tempo de busca de usuários")
    public List<Usuario> buscarUsuarios() {
        return Usuario.listAll();
    }
}

```

**Endpoint de métricas:**

```
GET /q/metrics  # Métricas no formato Prometheus

```

---

## 📨 6. MESSAGING E COMUNICAÇÃO ASSÍNCRONA

### `quarkus-smallrye-reactive-messaging-kafka`

**O que faz:** Integração com Apache Kafka para mensageria
**Quando usar:** Para comunicação assíncrona entre serviços

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-reactive-messaging-kafka</artifactId>
</dependency>

```

**Exemplo prático:**

```java
// 1. Produtor de mensagens
@ApplicationScoped
public class PedidoProducer {

    @Channel("pedidos-out")  // Canal de saída
    Emitter<Pedido> pedidoEmitter;

    public void enviarPedido(Pedido pedido) {
        pedidoEmitter.send(pedido)
                .whenComplete((success, failure) -> {
                    if (failure != null) {
                        Log.error("Erro ao enviar pedido", failure);
                    } else {
                        Log.info("Pedido enviado: " + pedido.id);
                    }
                });
    }
}

// 2. Consumidor de mensagens
@ApplicationScoped
public class PedidoConsumer {

    @Inject
    PedidoService pedidoService;

    @Incoming("pedidos-in")  // Canal de entrada
    @Outgoing("notificacoes-out")  // Pode reenviar para outro canal
    public Uni<Notificacao> processarPedido(Pedido pedido) {
        return Uni.createFrom()
                .item(() -> {
                    // Processa pedido
                    pedidoService.processar(pedido);

                    // Cria notificação
                    return new Notificacao(
                        "Pedido " + pedido.id + " processado",
                        pedido.clienteEmail
                    );
                })
                .onFailure().invoke(ex ->
                    Log.error("Erro ao processar pedido " + pedido.id, ex)
                );
    }

    // Consumidor simples (sem reenvio)
    @Incoming("notificacoes-in")
    public CompletionStage<Void> enviarNotificacao(Notificacao notificacao) {
        return emailService.enviar(notificacao)
                .thenRun(() -> Log.info("Notificação enviada"));
    }
}

```

**Configuração Kafka:**

```
# Kafka
kafka.bootstrap.servers=localhost:9092

# Canal de saída
mp.messaging.outgoing.pedidos-out.connector=smallrye-kafka
mp.messaging.outgoing.pedidos-out.topic=pedidos
mp.messaging.outgoing.pedidos-out.value.serializer=io.quarkus.kafka.client.serialization.ObjectMapperSerializer

# Canal de entrada
mp.messaging.incoming.pedidos-in.connector=smallrye-kafka
mp.messaging.incoming.pedidos-in.topic=pedidos
mp.messaging.incoming.pedidos-in.value.deserializer=io.quarkus.kafka.client.serialization.ObjectMapperDeserializer
mp.messaging.incoming.pedidos-in.value.deserializer.type=com.exemplo.Pedido

```

### `quarkus-websockets-next` (WebSockets)

**O que faz:** Comunicação bidirecional em tempo real
**Quando usar:** Para chat, notificações push, atualizações em tempo real

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-websockets-next</artifactId>
</dependency>

```

**Exemplo prático:**

```java
// 1. WebSocket simples
@WebSocket(path = "/chat")
public class ChatWebSocket {

    private final Map<String, WebSocketConnection> connections = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(WebSocketConnection connection) {
        String userId = connection.pathParam("userId");
        connections.put(userId, connection);

        // Notifica outros usuários
        broadcast("Usuário " + userId + " entrou no chat");
    }

    @OnTextMessage
    public void onMessage(String message, WebSocketConnection connection) {
        String userId = connection.pathParam("userId");
        String fullMessage = userId + ": " + message;

        // Envia para todos os conectados
        broadcast(fullMessage);
    }

    @OnClose
    public void onClose(WebSocketConnection connection) {
        String userId = connection.pathParam("userId");
        connections.remove(userId);

        broadcast("Usuário " + userId + " saiu do chat");
    }

    @OnError
    public void onError(WebSocketConnection connection, Throwable throwable) {
        Log.error("Erro no WebSocket", throwable);
    }

    private void broadcast(String message) {
        connections.values().forEach(conn -> {
            try {
                conn.sendText(message);
            } catch (Exception e) {
                Log.warn("Erro ao enviar mensagem", e);
            }
        });
    }
}

// 2. WebSocket com injeção de dependências
@WebSocket(path = "/notifications/{userId}")
public class NotificationWebSocket {

    @Inject
    NotificationService notificationService;

    @OnOpen
    public void onOpen(@PathParam String userId, WebSocketConnection connection) {
        // Registra usuário para receber notificações
        notificationService.register(userId, connection);

        // Envia notificações pendentes
        List<Notification> pending = notificationService.getPending(userId);
        pending.forEach(notification -> {
            connection.sendText(Json.encode(notification));
        });
    }

    @OnTextMessage
    public String onMessage(String message, @PathParam String userId) {
        // Processa comando do cliente
        return notificationService.processCommand(userId, message);
    }
}

// 3. Serviço para enviar notificações
@ApplicationScoped
public class NotificationService {

    private final Map<String, WebSocketConnection> userConnections = new ConcurrentHashMap<>();

    public void register(String userId, WebSocketConnection connection) {
        userConnections.put(userId, connection);
    }

    public void sendToUser(String userId, Notification notification) {
        WebSocketConnection connection = userConnections.get(userId);
        if (connection != null && connection.isOpen()) {
            connection.sendText(Json.encode(notification));
        } else {
            // Salva para envio posterior
            savePendingNotification(userId, notification);
        }
    }

    public void broadcast(Notification notification) {
        userConnections.values().forEach(conn -> {
            if (conn.isOpen()) {
                conn.sendText(Json.encode(notification));
            }
        });
    }
}

```

---

## 🧪 7. TESTES

### `quarkus-junit5` + `quarkus-junit5-mockito`

**O que faz:** Framework de testes integrado com Quarkus + mocks
**Quando usar:** Para testes unitários e de integração

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-junit5</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-junit5-mockito</artifactId>
    <scope>test</scope>
</dependency>

```

**Exemplo prático:**

```java
// 1. Teste de integração completo
@QuarkusTest
class ProdutoResourceTest {

    @Test
    public void testListarProdutos() {
        given()
            .when().get("/api/produtos")
            .then()
            .statusCode(200)
            .contentType(MediaType.APPLICATION_JSON)
            .body("size()", greaterThan(0));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})  // Simula usuário logado
    public void testCriarProduto() {
        Produto produto = new Produto();
        produto.nome = "Produto Teste";
        produto.preco = new BigDecimal("99.99");

        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(produto)
            .when().post("/api/produtos")
            .then()
            .statusCode(201)
            .body("nome", equalTo("Produto Teste"))
            .body("id", notNullValue());
    }

    @Test
    public void testBuscarProdutoInexistente() {
        given()
            .when().get("/api/produtos/999999")
            .then()
            .statusCode(404);
    }
}

// 2. Teste com mocks
@QuarkusTest
class ProdutoServiceTest {

    @InjectMock
    ProdutoRepository repository;  // Mock automático

    @Inject
    ProdutoService service;

    @Test
    public void testBuscarProdutosCaros() {
        // Configura mock
        List<Produto> produtosCaros = List.of(
            criarProduto("iPhone", new BigDecimal("5000")),
            criarProduto("MacBook", new BigDecimal("8000"))
        );

        when(repository.buscarCaros()).thenReturn(produtosCaros);

        // Executa teste
        List<Produto> resultado = service.buscarProdutosCaros();

        // Verifica resultado
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).nome).isEqualTo("iPhone");

        // Verifica se o mock foi chamado
        verify(repository).buscarCaros();
    }

    private Produto criarProduto(String nome, BigDecimal preco) {
        Produto produto = new Produto();
        produto.nome = nome;
        produto.preco = preco;
        return produto;
    }
}

// 3. Teste com perfil específico
@QuarkusTest
@TestProfile(TestProfile.class)  // Perfil personalizado
class IntegrationTest {

    @Test
    public void testComPerfilCustomizado() {
        // Teste com configurações específicas
    }

    public static class TestProfile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of(
                "quarkus.datasource.db-kind", "h2",
                "quarkus.datasource.jdbc.url", "jdbc:h2:mem:test"
            );
        }
    }
}

// 4. Teste nativo
@QuarkusIntegrationTest
class NativeIntegrationTest {

    @Test
    public void testEndpointNativo() {
        given()
            .when().get("/api/health")
            .then()
            .statusCode(200);
    }
}

```

---

## 🎯 Verificação de Compreensão (Método Feynman - Etapa 3)

**Teste seu entendimento com estes exercícios:**

### Exercício 1: API REST Básica

Crie um endpoint que:

- Aceita POST em `/api/tasks`
- Recebe um JSON com `{title: "string", completed: boolean}`
- Salva no banco usando Panache
- Retorna a task criada

### Exercício 2: Segurança

Como você protegeria o endpoint acima para que:

- Apenas usuários autenticados podem criar tasks
- Apenas o owner pode ver suas próprias tasks

### Exercício 3: Monitoramento

Adicione métricas para contar:

- Quantas tasks foram criadas
- Tempo médio para criar uma task

---

## 🔧 Exemplo Completo: E-commerce Simples

Aqui está um exemplo completo integrando várias dependências:

```java
// 1. Entidade Produto
@Entity
public class Produto extends PanacheEntity {
    public String nome;
    public BigDecimal preco;
    public Integer estoque;
    public LocalDateTime criadoEm = LocalDateTime.now();

    public static List<Produto> buscarDisponiveis() {
        return find("estoque > 0").list();
    }
}

// 2. Resource com todas as funcionalidades
@Path("/api/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoResource {

    @Inject
    MeterRegistry metrics;

    private final Counter produtosCriados;
    private final Timer tempoBusca;

    public ProdutoResource(MeterRegistry registry) {
        this.produtosCriados = Counter.builder("produtos.criados")
                .register(registry);
        this.tempoBusca = Timer.builder("produtos.busca.tempo")
                .register(registry);
    }

    @GET
    @PermitAll  // Público
    public List<Produto> listar() {
        return tempoBusca.recordCallable(() ->
            Produto.buscarDisponiveis()
        );
    }

    @POST
    @RolesAllowed("admin")  // Só admin pode criar
    @Transactional
    public Response criar(Produto produto) {
        produto.persist();
        produtosCriados.increment();
        return Response.status(201).entity(produto).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"admin", "user"})
    @Transactional
    public Response atualizar(@PathParam("id") Long id, Produto produto) {
        Produto existente = Produto.findById(id);
        if (existente == null) {
            return Response.status(404).build();
        }

        existente.nome = produto.nome;
        existente.preco = produto.preco;
        existente.estoque = produto.estoque;

        return Response.ok(existente).build();
    }
}

// 3. Health Check personalizado
@ApplicationScoped
public class ProdutoHealthCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        long totalProdutos = Produto.count();
        long produtosDisponiveis = Produto.count("estoque > 0");

        return HealthCheckResponse.builder()
                .name("produtos")
                .status(produtosDisponiveis > 0 ? Status.UP : Status.DOWN)
                .withData("total", totalProdutos)
                .withData("disponiveis", produtosDisponiveis)
                .build();
    }
}

// 4. Teste completo
@QuarkusTest
@TestTransaction
class ProdutoIntegrationTest {

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testFluxoCompleto() {
        // 1. Criar produto
        Produto produto = new Produto();
        produto.nome = "iPhone 15";
        produto.preco = new BigDecimal("5000");
        produto.estoque = 10;

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(produto)
                .when().post("/api/produtos")
                .then()
                .statusCode(201)
                .extract().response();

        Long id = response.jsonPath().getLong("id");

        // 2. Buscar produto criado
        given()
                .when().get("/api/produtos")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("find { it.id == " + id + " }.nome", equalTo("iPhone 15"));

        // 3. Verificar health check
        given()
                .when().get("/q/health")
                .then()
                .statusCode(200)
                .body("status", equalTo("UP"))
                .body("checks.find { it.name == 'produtos' }.status", equalTo("UP"));
    }
}

```

---

## 📚 Resumo das Dependências por Uso

| **Funcionalidade** | **Dependência**                                                      | **Quando Usar**             |
| ------------------ | -------------------------------------------------------------------- | --------------------------- |
| **API REST**       | `quarkus-rest` + `quarkus-rest-jackson`                              | Sempre para APIs REST       |
| **Banco de Dados** | `quarkus-hibernate-orm-panache`                                      | Para dados relacionais      |
| **Segurança**      | `quarkus-security` + `quarkus-smallrye-jwt`                          | Para proteger APIs          |
| **Monitoramento**  | `quarkus-smallrye-health` + `quarkus-micrometer-registry-prometheus` | Para observabilidade        |
| **Testes**         | `quarkus-junit5` + `quarkus-junit5-mockito`                          | Para todos os testes        |
| **Messaging**      | `quarkus-smallrye-reactive-messaging-kafka`                          | Para comunicação assíncrona |
| **WebSockets**     | `quarkus-websockets-next`                                            | Para tempo real             |

Este guia prático mostra como cada dependência funciona na prática. Cada exemplo é funcional e pode ser usado como base para seus projetos!