# Padrão RESTful com Quarkus - Guia Completo e Didático 🚀

## 📑 Índice

1. [🎯 Conceitos Fundamentais](#-conceitos-fundamentais)
2. [⚙️ Configuração Inicial](#️-configuração-inicial)
3. [📝 Anotações Principais](#-anotações-principais)
4. [🎨 Padrões de Desenvolvimento](#-padrões-de-desenvolvimento)
5. [🌍 CRUD Completo - Country API](#-crud-completo---country-api)
6. [🔥 Exception Mappers](#-exception-mappers)
7. [✅ Validações com Bean Validation](#-validações-com-bean-validation)
8. [📊 Boas Práticas RESTful](#-boas-práticas-restful)

---

## 🎯 Conceitos Fundamentais

### O Que É REST? - A Analogia da Biblioteca 📚

Imagine que você está organizando uma **biblioteca municipal**:

| **Conceito REST** | **Analogia da Biblioteca** | **Na Prática** |
|-------------------|----------------------------|----------------|
| **Recursos** 📖 | Cada livro tem um código único | `/livros/123` |
| **Verbos HTTP** 🔧 | Ações que você pode fazer | GET = consultar, POST = adicionar |
| **Representações** 🎨 | Diferentes formatos do livro | JSON, XML, HTML |
| **Stateless** 🔄 | Cada consulta é independente | Sem sessões no servidor |
| **URIs** 🔗 | Endereço único do livro | `/livros/123/autor` |

### 💡 Verbos HTTP - O Que Cada Um Faz?

```
📖 GET    → Ler/Buscar (biblioteca: consultar catálogo)
➕ POST   → Criar (biblioteca: adicionar livro novo)
✏️ PUT    → Atualizar completo (biblioteca: substituir livro)
🔧 PATCH  → Atualizar parcial (biblioteca: corrigir página)
🗑️ DELETE → Remover (biblioteca: descartar livro)
```

**Exemplo Visual:**
```http
GET    /livros          → Lista todos os livros
GET    /livros/123      → Busca livro específico
POST   /livros          → Adiciona livro novo
PUT    /livros/123      → Atualiza livro completo
PATCH  /livros/123      → Atualiza só título/autor
DELETE /livros/123      → Remove livro
```

### 🚀 O Que É Quarkus REST?

**Quarkus REST = JAX-RS turbinado!**

| **Framework Tradicional** ❌ | **Quarkus REST** ✅ |
|------------------------------|---------------------|
| Inicializa em 5-10 segundos | Inicializa em 0.1s ⚡ |
| Usa 200-500MB RAM | Usa 20-50MB RAM 🎯 |
| Não compila para nativo | Compila para binário nativo 🔥 |
| Bloqueante (síncrono) | Reativo (non-blocking) 🌊 |

**Benefícios:**
- ✅ **Compilação nativa** (GraalVM) → executável independente
- ✅ **Inicialização instantânea** → perfeito para serverless
- ✅ **Baixo consumo de memória** → mais containers no mesmo servidor
- ✅ **Programação reativa** → alta concorrência

---

## ⚙️ Configuração Inicial

### Dependências do `pom.xml` - Explicadas

```xml
<dependencies>
    <!-- ════════════════════════════════════════════════ -->
    <!-- 🔧 QUARKUS ARC - Injeção de Dependências (CDI)  -->
    <!-- ════════════════════════════════════════════════ -->
    <!-- O "coração" do Quarkus: gerencia ciclo de vida  -->
    <!-- dos beans e permite @Inject                      -->
    <!-- ════════════════════════════════════════════════ -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-arc</artifactId>
    </dependency>

    <!-- ════════════════════════════════════════════════ -->
    <!-- 🌐 QUARKUS REST - APIs RESTful (JAX-RS)         -->
    <!-- ════════════════════════════════════════════════ -->
    <!-- Cria endpoints HTTP com @Path, @GET, @POST etc. -->
    <!-- Versão reativa e otimizada do RESTEasy          -->
    <!-- ════════════════════════════════════════════════ -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-rest</artifactId>
    </dependency>

    <!-- ════════════════════════════════════════════════ -->
    <!-- 📦 QUARKUS REST JACKSON - Serialização JSON     -->
    <!-- ════════════════════════════════════════════════ -->
    <!-- Converte objetos Java ↔ JSON automaticamente    -->
    <!-- Trabalha com @Produces, @Consumes              -->
    <!-- ════════════════════════════════════════════════ -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-rest-jackson</artifactId>
    </dependency>

    <!-- ════════════════════════════════════════════════ -->
    <!-- 🗄️ QUARKUS HIBERNATE ORM PANACHE - ORM Simples -->
    <!-- ════════════════════════════════════════════════ -->
    <!-- JPA/Hibernate SEM boilerplate                   -->
    <!-- Active Record Pattern: findAll(), persist()     -->
    <!-- ════════════════════════════════════════════════ -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-hibernate-orm-panache</artifactId>
    </dependency>

    <!-- ════════════════════════════════════════════════ -->
    <!-- 🐘 QUARKUS JDBC POSTGRESQL - Driver PostgreSQL  -->
    <!-- ════════════════════════════════════════════════ -->
    <!-- Conecta com banco de dados PostgreSQL           -->
    <!-- ════════════════════════════════════════════════ -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-jdbc-postgresql</artifactId>
    </dependency>

    <!-- ════════════════════════════════════════════════ -->
    <!-- 📚 QUARKUS OPENAPI - Documentação Swagger       -->
    <!-- ════════════════════════════════════════════════ -->
    <!-- Gera documentação automática da API             -->
    <!-- Acesse em: http://localhost:8080/q/swagger-ui   -->
    <!-- ════════════════════════════════════════════════ -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-smallrye-openapi</artifactId>
    </dependency>
</dependencies>
```

**TL;DR - O Que Cada Dependência Faz:**

| Dependência | Função | Quando Usar |
|-------------|--------|-------------|
| `quarkus-arc` | Injeção de dependências | ✅ Sempre |
| `quarkus-rest` | Endpoints REST | ✅ APIs HTTP |
| `quarkus-rest-jackson` | JSON serialization | ✅ APIs REST |
| `quarkus-hibernate-orm-panache` | Banco de dados simplificado | ✅ CRUD com DB |
| `quarkus-jdbc-postgresql` | Driver PostgreSQL | ✅ Quando usar Postgres |
| `quarkus-smallrye-openapi` | Swagger automático | ✅ Documentação |

---

## 📝 Anotações Principais

### `@Path` - Definindo Rotas

**Conceito:** Define o caminho (URL) onde seu endpoint estará disponível.

```java
// ════════════════════════════════════════════════
// EXEMPLOS DE @Path
// ════════════════════════════════════════════════

@Path("/produtos")              
// → URL: http://localhost:8080/produtos

@Path("/produtos/{id}")         
// → URL: http://localhost:8080/produtos/123
// → {id} é um parâmetro dinâmico

@Path("/produtos/{id}/reviews") 
// → URL: http://localhost:8080/produtos/123/reviews
// → Rota aninhada (nested)

@Path("/api/v1/usuarios")
// → URL: http://localhost:8080/api/v1/usuarios
// → Bom para versionamento de API
```

### Métodos HTTP - Verbos REST

**TL;DR:** Cada verbo HTTP tem um propósito específico!

| Anotação | Verbo | Uso | Exemplo |
|----------|-------|-----|---------|
| `@GET` | GET | 📖 Buscar/listar recursos | Lista produtos |
| `@POST` | POST | ➕ Criar novo recurso | Adiciona produto |
| `@PUT` | PUT | ✏️ Atualizar completo | Substitui produto |
| `@PATCH` | PATCH | 🔧 Atualizar parcial | Muda só preço |
| `@DELETE` | DELETE | 🗑️ Remover recurso | Deleta produto |
| `@HEAD` | HEAD | 📋 Buscar só headers | Verifica se existe |
| `@OPTIONS` | OPTIONS | ❓ Métodos suportados | CORS preflight |

**Exemplo prático:**

```java
@Path("/produtos")
public class ProdutoResource {
    
    @GET                    // Lista todos
    public List<Produto> listar() { ... }
    
    @GET
    @Path("/{id}")          // Busca por ID
    public Produto buscar(@PathParam("id") Long id) { ... }
    
    @POST                   // Cria novo
    public Produto criar(Produto produto) { ... }
    
    @PUT
    @Path("/{id}")          // Atualiza completo
    public Produto atualizar(@PathParam("id") Long id, Produto produto) { ... }
    
    @PATCH
    @Path("/{id}/preco")    // Atualiza só preço
    public Produto atualizarPreco(@PathParam("id") Long id, BigDecimal preco) { ... }
    
    @DELETE
    @Path("/{id}")          // Remove
    public void deletar(@PathParam("id") Long id) { ... }
}
```

### Parâmetros - Onde Vêm os Dados?

**Visualização:**
```
http://localhost:8080/produtos/123?categoria=eletronicos&page=2
                      └─────┘ └─┘  └──────────────────────────┘
                       PATH    ID        QUERY PARAMS
```

#### 1️⃣ **`@PathParam`** - Parâmetro na URL

```java
// GET /produtos/123
@GET
@Path("/{id}")
public Produto buscar(@PathParam("id") Long id) {
    // id = 123
}

// GET /usuarios/joao/pedidos/456
@GET
@Path("/{username}/pedidos/{pedidoId}")
public Pedido buscarPedido(
    @PathParam("username") String username,
    @PathParam("pedidoId") Long pedidoId
) {
    // username = "joao"
    // pedidoId = 456
}
```

#### 2️⃣ **`@QueryParam`** - Query String (depois do `?`)

```java
// GET /produtos?categoria=eletronicos&preco_max=1000
@GET
public List<Produto> buscar(
    @QueryParam("categoria") String categoria,
    @QueryParam("preco_max") BigDecimal precoMax
) {
    // categoria = "eletronicos"
    // precoMax = 1000
}

// Com valor padrão
@GET
public List<Produto> buscar(
    @QueryParam("page") @DefaultValue("0") int page,
    @QueryParam("size") @DefaultValue("20") int size
) {
    // Se não informado: page=0, size=20
}
```

#### 3️⃣ **`@HeaderParam`** - Headers HTTP

```java
@GET
public List<Produto> buscar(
    @HeaderParam("Authorization") String token,
    @HeaderParam("Accept-Language") String language
) {
    // token = "Bearer abc123..."
    // language = "pt-BR"
}
```

#### 4️⃣ **`@FormParam`** - Formulário HTML

```java
@POST
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public Response criar(
    @FormParam("nome") String nome,
    @FormParam("email") String email
) {
    // Corpo: nome=João&email=joao@email.com
}
```

#### 5️⃣ **Corpo da Requisição** (sem anotação específica)

```java
@POST
@Consumes(MediaType.APPLICATION_JSON)
public Response criar(Produto produto) {
    // produto = objeto JSON convertido automaticamente
    // { "nome": "Notebook", "preco": 2500 }
}
```

### Content Types - Formatos de Dados

```java
// ════════════════════════════════════════════════
// @Produces - O QUE A API RETORNA
// ════════════════════════════════════════════════

@Produces(MediaType.APPLICATION_JSON)  
// → Retorna JSON (padrão para APIs REST)

@Produces(MediaType.APPLICATION_XML)   
// → Retorna XML

@Produces(MediaType.TEXT_PLAIN)        
// → Retorna texto puro

@Produces(MediaType.TEXT_HTML)         
// → Retorna HTML

// ════════════════════════════════════════════════
// @Consumes - O QUE A API ACEITA
// ════════════════════════════════════════════════

@Consumes(MediaType.APPLICATION_JSON)  
// → Aceita JSON no corpo da requisição

@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
// → Aceita dados de formulário

@Consumes(MediaType.MULTIPART_FORM_DATA)
// → Aceita upload de arquivos
```

**Exemplo combinado:**

```java
@POST
@Consumes(MediaType.APPLICATION_JSON)  // Aceita JSON
@Produces(MediaType.APPLICATION_JSON)  // Retorna JSON
public Produto criar(Produto produto) {
    // Recebe: { "nome": "Mouse", "preco": 50 }
    // Retorna: { "id": 1, "nome": "Mouse", "preco": 50 }
}
```

---

### 🆚 JAX-RS Clássico vs RESTEasy Reactive

**Quarkus oferece 2 estilos de anotações:**

| Aspecto | **JAX-RS Padrão** (Jakarta EE) | **RESTEasy Reactive** (Quarkus) |
|---------|--------------------------------|----------------------------------|
| **Path Param** | `@PathParam("id")` | `@RestPath` |
| **Query Param** | `@QueryParam("page")` | `@RestQuery` |
| **Header** | `@HeaderParam("Auth")` | `@RestHeader("Auth")` |
| **Cookie** | `@CookieParam("session")` | `@RestCookie` |
| **Form** | `@FormParam("email")` | `@RestForm` |
| **Verbosidade** | Precisa repetir nome | Usa nome do parâmetro |
| **Performance** | Padrão | Otimizado Quarkus |
| **Portabilidade** | ✅ Funciona em qualquer Java EE | ❌ Só Quarkus |

**Exemplo comparativo:**

```java
// ════════════════════════════════════════════════
// JAX-RS CLÁSSICO (mais verboso)
// ════════════════════════════════════════════════
@GET
@Path("/{id}")
public Response buscar(
    @PathParam("id") Long id,
    @QueryParam("incluir_detalhes") boolean incluirDetalhes,
    @HeaderParam("Authorization") String token
) { ... }

// ════════════════════════════════════════════════
// RESTEASY REACTIVE (mais limpo)
// ════════════════════════════════════════════════
@GET
@Path("/{id}")
public Response buscar(
    @RestPath Long id,                    // Nome vem do parâmetro!
    @RestQuery boolean incluirDetalhes,   // Nome vem do parâmetro!
    @RestHeader("Authorization") String token
) { ... }
```

**📌 Recomendação:**
- ✅ Use **RESTEasy Reactive** se seu projeto é **100% Quarkus**
- ✅ Use **JAX-RS clássico** se precisa **portabilidade** para outros servidores Java EE

---

## 4. Padrões

### 4.1. Retornar diretamente o objeto (ex.: `List<Country>`)

```java
@GET
@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
public List<Country> search(
        @QueryParam("region") String region,
        @QueryParam("population") Long population
) {
    return countryService.search(region, population);
}

```

➡️ **Características:**

- Simples e direto: o Quarkus serializa o objeto para JSON automaticamente.
- Ideal quando você **sempre retorna 200 OK** (sem variações de status).
- Menos verboso → menos código boilerplate.
- Bom para prototipagem e APIs simples.

---

### 4.2. Retornar um `Response`

```java
@GET
@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
public Response search(
        @QueryParam("region") String region,
        @QueryParam("population") Long population
) {
    var result = countryService.search(region, population);

    if (result.isEmpty()) {
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("Nenhum país encontrado")
                       .build();
    }

    return Response.ok(result).build();
}

```

Comparação Visual

```bash
PATH   -> /countries/name/Brazil
QUERY  -> /countries/search?region=Europe&population=1000000
BODY   -> JSON no corpo da requisição
```

➡️ **Características:**

- Mais flexível: você controla **status codes** (`200`, `201`, `400`, `404`, `500`, etc.).
- Pode adicionar **headers extras** (ex.: paginação, links HATEOAS, caching).
- Melhor quando a API precisa de respostas diferenciadas.

### Dica Extra: Usando `@BeanParam` (agrupar Query + Path)

Se você tiver muitos parâmetros, pode criar uma classe que encapsula tudo:

```java
public class CountryFilter {
    @QueryParam("region")
    public String region;

    @QueryParam("population")
    public Long population;

    @PathParam("name")
    public String name;
}
```

E no endpoint:

```java
@GET
@Path("/filter/{name}")
public List<Country> filter(@BeanParam CountryFilter filter) {
    return countryService.filter(filter);
}
```

Resumindo:

- `@PathParam` → valores da URL
- `@QueryParam` → valores de query string
- Objeto no parâmetro (com `@Consumes`) → corpo da requisição

---

### 4.3. Qual é o "padrão RESTful" melhor?

👉 O **mais “RESTful”** depende da complexidade do endpoint:

- **Simples (CRUD básico, retorno sempre 200/201)** → retornar objeto é suficiente.
- **API realista (com erros, paginação, headers, status variáveis)** → retornar `Response` é o mais **profissional** e flexível.

---

### 4.4. Padrão na prática

Muitos projetos usam uma mistura:

- **Endpoints CRUD simples** → retornam direto o objeto.
- **Endpoints que lidam com erros/paginação** → retornam `Response`.

Exemplo prático de paginação RESTful:

```java
@GET
@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
public Response search(
        @QueryParam("region") String region,
        @QueryParam("page") int page,
        @QueryParam("size") int size
) {
    var result = countryService.search(region, page, size);

    return Response.ok(result.getItems())
                   .header("X-Total-Count", result.getTotal())
                   .header("X-Page", page)
                   .header("X-Size", size)
                   .build();
}

```

---

# 🌍 CRUD Country API – com Paginação, Filtros e Try/Catch

---

## 1. **Repository com filtros**

```java
package org.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.example.model.Country;

import java.util.List;

@ApplicationScoped
public class CountryRepository implements PanacheRepository<Country> {

    public List<Country> search(String region, String name, int page, int size) {
        var query = new StringBuilder("1=1"); // sempre verdadeiro

        if (region != null && !region.isBlank()) {
            query.append(" and region = '").append(region).append("'");
        }
        if (name != null && !name.isBlank()) {
            query.append(" and lower(name) like '%").append(name.toLowerCase()).append("%'");
        }

        return find(query.toString())
                .page(page, size) // paginação
                .list();
    }

    public long countSearch(String region, String name) {
        var query = new StringBuilder("1=1");

        if (region != null && !region.isBlank()) {
            query.append(" and region = '").append(region).append("'");
        }
        if (name != null && !name.isBlank()) {
            query.append(" and lower(name) like '%").append(name.toLowerCase()).append("%'");
        }

        return find(query.toString()).count();
    }
}

```

---

## 2. **Service com paginação e filtros**

```java
package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.example.model.Country;
import org.example.repository.CountryRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CountryService {

    @Inject
    CountryRepository repository;

    public List<Country> findAll() {
        return repository.listAll();
    }

    public Optional<Country> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    public Country create(Country country) {
        repository.persist(country);
        return country;
    }

    public Optional<Country> update(Long id, Country country) {
        return repository.findByIdOptional(id).map(existing -> {
            existing.setName(country.getName());
            existing.setRegion(country.getRegion());
            existing.setPopulation(country.getPopulation());
            return existing;
        });
    }

    public boolean delete(Long id) {
        return repository.deleteById(id);
    }

    public List<Country> search(String region, String name, int page, int size) {
        return repository.search(region, name, page, size);
    }

    public long countSearch(String region, String name) {
        return repository.countSearch(region, name);
    }
}

```

---

## 3. **Resource com try/catch**

```java
package org.example.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.example.model.Country;
import org.example.service.CountryService;

import java.util.List;

@Path("/countries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CountryResource {

    @Inject
    CountryService service;

    @GET
    public Response findAll() {
        try {
            List<Country> countries = service.findAll();
            return Response.ok(countries).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        try {
            return service.findById(id)
                    .map(c -> Response.ok(c).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    public Response create(Country country, @Context UriInfo uriInfo) {
        try {
            Country saved = service.create(country);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(saved.getId().toString());
            return Response.created(builder.build()).entity(saved).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Country country) {
        try {
            return service.update(id, country)
                    .map(c -> Response.ok(c).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        try {
            if (service.delete(id)) {
                return Response.noContent().build(); // 204
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // 🔥 Novo endpoint com paginação + filtros
    @GET
    @Path("/search")
    public Response search(
            @QueryParam("region") String region,
            @QueryParam("name") String name,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size
    ) {
        try {
            List<Country> result = service.search(region, name, page, size);
            long total = service.countSearch(region, name);

            return Response.ok(result)
                    .header("X-Total-Count", total) // total de registros
                    .header("X-Page", page)
                    .header("X-Size", size)
                    .build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}

```

---

## 4. **Exemplo de chamadas**

### Buscar países da Europa, página 0, tamanho 5

```
GET /countries/search?region=Europe&page=0&size=5

```

➡️ Resposta:

```json
[
  { "id": 1, "name": "Germany", "region": "Europe", "population": 83000000 },
  { "id": 2, "name": "France", "region": "Europe", "population": 67000000 }
]

```

Headers da resposta:

```
X-Total-Count: 45
X-Page: 0
X-Size: 5

```

---

✅ Agora temos:

- CRUD completo.
- **Paginação** com headers (`X-Total-Count`, `X-Page`, `X-Size`).
- **Filtros** (`region` e `name`).
- **try/catch** em todos os métodos → respostas mais seguras.

---

---

# ⚡ ExceptionMapper para Validações

---

## 1. Criando uma classe de erro padrão

Assim temos uma estrutura uniforme para todos os erros:

```java
package org.example.exception;

public class ApiError {
    private String error;
    private int code;
    private String path;

    public ApiError(String error, int code, String path) {
        this.error = error;
        this.code = code;
        this.path = path;
    }

    public String getError() { return error; }
    public int getCode() { return code; }
    public String getPath() { return path; }
}

```

---

## 2. ExceptionMapper para erros de validação (`BadRequestException`)

Esse mapper captura **erros 400** de forma centralizada.

```java
package org.example.exception;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.servlet.http.HttpServletRequest;

@Provider
public class BadRequestMapper implements ExceptionMapper<BadRequestException> {

    @Context
    HttpServletRequest request;

    @Override
    public Response toResponse(BadRequestException ex) {
        ApiError error = new ApiError(
                ex.getMessage() != null ? ex.getMessage() : "Invalid request",
                Response.Status.BAD_REQUEST.getStatusCode(),
                request.getRequestURI()
        );

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .build();
    }
}

```

---

## 3. Ajustando validações no `Resource`

Agora podemos **lançar exceções** em vez de só try/catch.

Exemplo no `create`:

```java
@POST
public Response create(Country country, @Context UriInfo uriInfo) {
    if (country.getName() == null || country.getName().isBlank()) {
        throw new BadRequestException("Field 'name' is required");
    }
    if (country.getPopulation() != null && country.getPopulation() < 0) {
        throw new BadRequestException("Population must be >= 0");
    }

    Country saved = service.create(country);
    UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(saved.getId().toString());
    return Response.created(builder.build()).entity(saved).build();
}

```

---

## 4. Exemplo de resposta de erro

### Requisição inválida:

```
POST /countries
Content-Type: application/json

{
  "region": "South America"
}

```

➡️ Resposta:

```json
{
  "error": "Field 'name' is required",
  "code": 400,
  "path": "/countries"
}

```

---

## 5. Extra (validações automáticas com Bean Validation)

Se você quiser simplificar, pode usar **Hibernate Validator** (já vem no Quarkus):

```java
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class CountryDTO {
    @NotBlank(message = "Name is required")
    public String name;

    @NotBlank(message = "Region is required")
    public String region;

    @PositiveOrZero(message = "Population must be >= 0")
    public Long population;
}

```

E no recurso:

```java
@POST
public Response create(@Valid CountryDTO dto, @Context UriInfo uriInfo) {
    Country country = new Country();
    country.setName(dto.name);
    country.setRegion(dto.region);
    country.setPopulation(dto.population);

    Country saved = service.create(country);

    UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(saved.getId().toString());
    return Response.created(builder.build()).entity(saved).build();
}

```

➡️ O Quarkus dispara **ConstraintViolationException** automaticamente se o DTO for inválido.

Basta criar outro `ExceptionMapper` para ela, parecido com o `BadRequestMapper`.

---

✅ Agora sua API tem:

- **Tratamento uniforme de erros 400** com JSON bonitinho.
- Suporte a **validações manuais** (`throw BadRequestException`).
- Opcional: **validações automáticas com Bean Validation** (`@Valid`, `@NotBlank`, etc.).

---

Vamos criar um `ExceptionMapper` para **ConstraintViolationException**, que é lançado automaticamente quando usamos `@Valid` + anotações do **Bean Validation** (`@NotBlank`, `@Size`, `@Positive`, etc.).

Assim conseguimos retornar **todos os erros de validação** num JSON organizado.

---

# ⚡ ExceptionMapper para Bean Validation

---

## 1. DTO com validações

Exemplo de um `CountryDTO` validado automaticamente:

```java
package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class CountryDTO {

    @NotBlank(message = "Name is required")
    public String name;

    @NotBlank(message = "Region is required")
    public String region;

    @PositiveOrZero(message = "Population must be >= 0")
    public Long population;
}

```

---

## 2. Usando o DTO no Resource

Agora basta anotar com `@Valid`. Se os dados forem inválidos, o Quarkus lança `ConstraintViolationException` automaticamente.

```java
@POST
public Response create(@Valid CountryDTO dto, @Context UriInfo uriInfo) {
    Country country = new Country();
    country.setName(dto.name);
    country.setRegion(dto.region);
    country.setPopulation(dto.population);

    Country saved = service.create(country);

    UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(saved.getId().toString());
    return Response.created(builder.build()).entity(saved).build();
}

```

---

## 3. ExceptionMapper para ConstraintViolationException

```java
package org.example.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException ex) {

        List<ValidationError> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> new ValidationError(
                        v.getPropertyPath().toString(), // campo inválido
                        v.getMessage()))                 // mensagem da anotação
                .collect(Collectors.toList());

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                "Validation failed",
                Response.Status.BAD_REQUEST.getStatusCode(),
                errors
        );

        return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
    }

    // DTO interno para cada erro
    public static class ValidationError {
        public String field;
        public String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }

    // DTO de resposta
    public static class ValidationErrorResponse {
        public String error;
        public int code;
        public List<ValidationError> violations;

        public ValidationErrorResponse(String error, int code, List<ValidationError> violations) {
            this.error = error;
            this.code = code;
            this.violations = violations;
        }
    }
}

```

---

## 4. Exemplo de requisição inválida

### Request:

```
POST /countries
Content-Type: application/json

{
  "name": "",
  "region": "",
  "population": -10
}

```

### Response (400 Bad Request):

```json
{
  "error": "Validation failed",
  "code": 400,
  "violations": [
    {
      "field": "create.dto.name",
      "message": "Name is required"
    },
    {
      "field": "create.dto.region",
      "message": "Region is required"
    },
    {
      "field": "create.dto.population",
      "message": "Population must be >= 0"
    }
  ]
}

```

---

✅ Agora sua API tem:

- Validações automáticas com **Bean Validation**.
- `ExceptionMapper` para **400 Bad Request** genérico.
- `ExceptionMapper` para **ConstraintViolationException**, retornando **todos os campos inválidos** de forma clara.
