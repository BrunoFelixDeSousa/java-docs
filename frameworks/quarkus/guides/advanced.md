# Quarkus com Clean Architecture: Guia Completo

## Sumário
1. [Introdução ao Quarkus](#1-introdução-ao-quarkus)
2. [Configuração Inicial](#2-configuração-inicial)
3. [Conceitos Básicos do Quarkus](#3-conceitos-básicos-do-quarkus)
4. [Injeção de Dependência](#4-injeção-de-dependência)
5. [Persistência com Panache](#5-persistência-com-panache)
6. [Clean Architecture: Fundamentos](#6-clean-architecture-fundamentos)
7. [Implementando Clean Architecture no Quarkus](#7-implementando-clean-architecture-no-quarkus)
8. [Testes Unitários e de Integração](#8-testes-unitários-e-de-integração)
9. [Projeto Prático: Sistema de Usuários](#9-projeto-prático-sistema-de-usuários)
10. [Deploy e Boas Práticas](#10-deploy-e-boas-práticas)

---

## 1. Introdução ao Quarkus

### O que é Quarkus?

Imagine que você tem um carro esportivo (aplicação Java tradicional) que é potente, mas consome muito combustível e demora para ligar. O Quarkus é como transformar esse carro em um Tesla: mantém a potência, mas agora é eficiente e liga instantaneamente.

**Quarkus** é um framework Java nativo para Kubernetes, criado pela Red Hat, que promete:

- **Startup ultra-rápido**: Aplicações iniciam em milissegundos
- **Baixo consumo de memória**: Ideal para containers e microserviços  
- **Produtividade de desenvolvedor**: Hot reload, extensões prontas
- **Nativo para nuvem**: Kubernetes, Docker, serverless

### Por que usar Quarkus?

| Aspecto | Spring Boot | Quarkus |
|---------|-------------|---------|
| Tempo de startup | ~2-5 segundos | ~0.05 segundos |
| Uso de memória | ~100-200 MB | ~20-50 MB |
| Hot reload | Reinicialização completa | Mudanças instantâneas |
| Imagem nativa | Complexa (GraalVM) | Simplificada |

### Quando usar Quarkus?

✅ **Use Quarkus quando:**
- Desenvolver microserviços
- Precisar de startup rápido
- Trabalhar com containers/Kubernetes
- Quiser produtividade no desenvolvimento

❌ **Evite Quarkus quando:**
- Tiver uma aplicação monolítica legacy grande
- A equipe não conhecer conceitos de programação reativa
- Precisar de bibliotecas que não têm extensões Quarkus

---

## 2. Configuração Inicial

### Pré-requisitos

```bash
# Verificar versões necessárias
java -version    # Java 11 ou superior
mvn -version     # Maven 3.8+ ou Gradle 7+
```

### Criando o Projeto

**Opção 1: Via Maven Archetype**
```bash
mvn io.quarkus.platform:quarkus-maven-plugin:3.6.0:create \
    -DprojectGroupId=com.exemplo \
    -DprojectArtifactId=quarkus-clean-arch \
    -Dextensions="resteasy-jackson,hibernate-orm-panache,jdbc-h2"
```

**Opção 2: Via Quarkus CLI**
```bash
# Instalar Quarkus CLI
curl -Ls https://sh.jbang.dev | bash -s - trust add https://repo1.maven.org/maven2/io/quarkus/quarkus-cli/
curl -Ls https://sh.jbang.dev | bash -s - app install --fresh --force quarkus@quarkusio

# Criar projeto
quarkus create app com.exemplo:quarkus-clean-arch
```

### Estrutura Inicial do Projeto

```
quarkus-clean-arch/
├── pom.xml                          # Dependências e configurações Maven
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/exemplo/
│   │   │       └── GreetingResource.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── META-INF/resources/
│   └── test/
│       └── java/
├── target/                          # Artefatos compilados
└── README.md
```

### Configuração Base (application.properties)

```properties
# Configurações básicas
quarkus.application.name=quarkus-clean-arch
quarkus.application.version=1.0.0

# Configurações de desenvolvimento
quarkus.dev.ui.enabled=true
quarkus.log.console.enable=true
quarkus.log.level=INFO

# Configurações de banco H2 (desenvolvimento)
quarkus.datasource.db-kind=h2
quarkus.datasource.username=sa
quarkus.datasource.password=
quarkus.datasource.jdbc.url=jdbc:h2:mem:test
quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.log.sql=true
```

---

## 3. Conceitos Básicos do Quarkus

### 3.1 Primeiro Endpoint REST

Vamos começar simples: um endpoint que responde "Olá":

```java
package com.exemplo;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")  // Define a rota base
public class HelloResource {
    
    @GET  // Método HTTP GET
    @Produces(MediaType.TEXT_PLAIN)  // Tipo de resposta
    public String hello() {
        return "Olá, Quarkus!";
    }
    
    @GET
    @Path("/json")  // Subrota: /hello/json
    @Produces(MediaType.APPLICATION_JSON)
    public HelloResponse helloJson() {
        return new HelloResponse("Olá", "Quarkus com JSON!");
    }
}

// Classe para resposta JSON
public class HelloResponse {
    public String saudacao;
    public String mensagem;
    
    public HelloResponse() {} // Construtor padrão necessário para JSON
    
    public HelloResponse(String saudacao, String mensagem) {
        this.saudacao = saudacao;
        this.mensagem = mensagem;
    }
}
```

**Como testar:**
```bash
# Iniciar aplicação em modo dev (hot reload)
./mvnw quarkus:dev

# Em outro terminal:
curl http://localhost:8080/hello
curl http://localhost:8080/hello/json
```

### 3.2 Parâmetros e Validação

```java
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Path("/usuarios")
public class UsuarioResource {
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@PathParam("id") Long id) {
        // Simular busca de usuário
        if (id <= 0) {
            return Response.status(400)
                .entity("ID deve ser maior que zero")
                .build();
        }
        
        return Response.ok(new Usuario(id, "João Silva"))
            .build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response criar(@Valid NovoUsuario novoUsuario) {
        // Criar usuário
        Usuario usuario = new Usuario(1L, novoUsuario.nome);
        return Response.status(201).entity(usuario).build();
    }
}

// DTO para criação
public class NovoUsuario {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    public String nome;
    
    // Construtor padrão necessário
    public NovoUsuario() {}
}

// Entidade de resposta
public class Usuario {
    public Long id;
    public String nome;
    
    public Usuario() {}
    
    public Usuario(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
```

### 3.3 Tratamento de Erros

```java
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.validation.ConstraintViolationException;

@Provider  // Registra automaticamente no Quarkus
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {
    
    @Override
    public Response toResponse(Exception exception) {
        
        // Tratar erros de validação
        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException cv = (ConstraintViolationException) exception;
            return Response.status(400)
                .entity(new ErroResponse("Dados inválidos", cv.getMessage()))
                .build();
        }
        
        // Erro genérico
        return Response.status(500)
            .entity(new ErroResponse("Erro interno", "Algo deu errado"))
            .build();
    }
}

public class ErroResponse {
    public String tipo;
    public String mensagem;
    
    public ErroResponse() {}
    
    public ErroResponse(String tipo, String mensagem) {
        this.tipo = tipo;
        this.mensagem = mensagem;
    }
}
```

---

## 4. Injeção de Dependência

### 4.1 Conceitos CDI no Quarkus

O Quarkus usa **CDI (Contexts and Dependency Injection)** - pense nele como um "garçom inteligente" que sabe exatamente qual serviço você precisa e o entrega na hora certa.

```java
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

// @ApplicationScoped = uma instância para toda aplicação
@ApplicationScoped
public class UsuarioService {
    
    public Usuario buscarPorId(Long id) {
        // Lógica de negócio aqui
        return new Usuario(id, "João Silva");
    }
    
    public Usuario criar(String nome) {
        // Lógica para criar usuário
        return new Usuario(System.currentTimeMillis(), nome);
    }
}

@Path("/usuarios")
public class UsuarioResource {
    
    @Inject  // O Quarkus injeta automaticamente
    UsuarioService usuarioService;
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscar(@PathParam("id") Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return Response.ok(usuario).build();
    }
}
```

### 4.2 Escopos no CDI

```java
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Singleton;

// Uma instância por aplicação (mais comum)
@ApplicationScoped
public class ConfiguracaoService {
    private final String versao = "1.0.0";
    public String getVersao() { return versao; }
}

// Uma instância por requisição HTTP
@RequestScoped  
public class ContextoRequisicao {
    private String usuarioAtual;
    
    public void setUsuario(String usuario) {
        this.usuarioAtual = usuario;
    }
    
    public String getUsuario() {
        return usuarioAtual;
    }
}

// Uma única instância (singleton estrito)
@Singleton
public class ContadorGlobal {
    private int contador = 0;
    
    public synchronized int incrementar() {
        return ++contador;
    }
}
```

### 4.3 Qualificadores (Qualifiers)

Quando você tem múltiplas implementações da mesma interface:

```java
import jakarta.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// Definir qualificadores customizados
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface TipoNotificacao {
    Tipo value();
    
    enum Tipo {
        EMAIL, SMS, PUSH
    }
}

// Interface comum
public interface NotificacaoService {
    void enviar(String mensagem, String destinatario);
}

// Implementações específicas
@ApplicationScoped
@TipoNotificacao(TipoNotificacao.Tipo.EMAIL)
public class EmailService implements NotificacaoService {
    @Override
    public void enviar(String mensagem, String destinatario) {
        System.out.println("Enviando email para: " + destinatario);
    }
}

@ApplicationScoped
@TipoNotificacao(TipoNotificacao.Tipo.SMS)
public class SmsService implements NotificacaoService {
    @Override
    public void enviar(String mensagem, String destinatario) {
        System.out.println("Enviando SMS para: " + destinatario);
    }
}

// Uso nos controllers
@Path("/notificacoes")
public class NotificacaoResource {
    
    @Inject
    @TipoNotificacao(TipoNotificacao.Tipo.EMAIL)
    NotificacaoService emailService;
    
    @Inject
    @TipoNotificacao(TipoNotificacao.Tipo.SMS)
    NotificacaoService smsService;
}
```

---

## 5. Persistência com Panache

### 5.1 O que é Panache?

Panache é como ter um "assistente pessoal para banco de dados". Ele elimina o código repetitivo (boilerplate) do JPA e oferece uma API simples e fluente.

**Antes (JPA tradicional):**
```java
@Entity
public class Usuario {
    @Id @GeneratedValue
    private Long id;
    private String nome;
    
    // getters, setters, equals, hashCode...
}

@Repository
public class UsuarioRepository {
    @PersistenceContext
    EntityManager em;
    
    public Usuario findById(Long id) {
        return em.find(Usuario.class, id);
    }
    
    public List<Usuario> findAll() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class)
            .getResultList();
    }
    // Mais código repetitivo...
}
```

**Com Panache:**
```java
@Entity
public class Usuario extends PanacheEntity {
    public String nome;  // Campos públicos, sem getters/setters
    
    // Métodos de consulta customizados
    public static Usuario findByNome(String nome) {
        return find("nome", nome).firstResult();
    }
    
    public static List<Usuario> findByNomeContaining(String termo) {
        return find("nome like ?1", "%" + termo + "%").list();
    }
}
```

### 5.2 Entity com PanacheEntity

```java
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario extends PanacheEntity {
    
    @NotBlank
    @Column(nullable = false)
    public String nome;
    
    @Email
    @Column(unique = true, nullable = false)
    public String email;
    
    @Column(name = "data_criacao")
    public LocalDateTime dataCriacao;
    
    public Boolean ativo = true;
    
    // Construtor padrão necessário para JPA
    public Usuario() {}
    
    public Usuario(String nome, String email) {
        this.nome = nome;
        this.email = email;
        this.dataCriacao = LocalDateTime.now();
    }
    
    // Métodos de consulta customizados
    public static Usuario findByEmail(String email) {
        return find("email", email).firstResult();
    }
    
    public static List<Usuario> findAtivos() {
        return find("ativo", true).list();
    }
    
    public static List<Usuario> findByNomeContaining(String termo) {
        return find("nome like ?1", "%" + termo + "%").list();
    }
    
    public static long countAtivos() {
        return count("ativo", true);
    }
    
    // Método para desativar usuário
    public void desativar() {
        this.ativo = false;
        persist(); // Salva a alteração
    }
}
```

### 5.3 Repository Pattern com PanacheRepository

Para quem prefere separar a lógica de persistência:

```java
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {
    
    public Optional<Usuario> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
    
    public List<Usuario> findAtivos() {
        return find("ativo = true").list();
    }
    
    public List<Usuario> findByNomeContaining(String termo) {
        return find("lower(nome) like lower(?1)", "%" + termo + "%").list();
    }
    
    @Transactional
    public Usuario criarUsuario(String nome, String email) {
        Usuario usuario = new Usuario(nome, email);
        persist(usuario);
        return usuario;
    }
    
    @Transactional
    public void desativarPorEmail(String email) {
        update("ativo = false where email = ?1", email);
    }
    
    // Consultas com paginação
    public List<Usuario> findPaginado(int pagina, int tamanho) {
        return find("ativo = true")
            .page(pagina, tamanho)
            .list();
    }
}
```

### 5.4 Service Layer com Transações

```java
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsuarioService {
    
    @Inject
    UsuarioRepository usuarioRepository;
    
    public List<Usuario> listarTodos() {
        return usuarioRepository.listAll();
    }
    
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findByIdOptional(id);
    }
    
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    @Transactional
    public Usuario criar(String nome, String email) {
        // Verificar se email já existe
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        
        return usuarioRepository.criarUsuario(nome, email);
    }
    
    @Transactional
    public Optional<Usuario> atualizar(Long id, String nome, String email) {
        return usuarioRepository.findByIdOptional(id)
            .map(usuario -> {
                usuario.nome = nome;
                usuario.email = email;
                usuarioRepository.persist(usuario);
                return usuario;
            });
    }
    
    @Transactional
    public boolean deletar(Long id) {
        return usuarioRepository.deleteById(id);
    }
    
    @Transactional
    public void desativarPorEmail(String email) {
        usuarioRepository.desativarPorEmail(email);
    }
    
    public List<Usuario> pesquisar(String termo) {
        return usuarioRepository.findByNomeContaining(termo);
    }
}
```

---

## 6. Clean Architecture: Fundamentos

### 6.1 O que é Clean Architecture?

Imagine sua aplicação como uma cebola com camadas. **Clean Architecture** é uma forma de organizar código onde:

- **As camadas internas não conhecem as externas**
- **Regras de negócio ficam no centro, isoladas**
- **Infraestrutura (banco, web) fica na borda**

```
┌─────────────────────────────────────────────────────────┐
│                    FRAMEWORKS                           │
│  ┌─────────────────────────────────────────────────┐    │
│  │            INTERFACE ADAPTERS               │    │
│  │  ┌─────────────────────────────────────┐    │    │
│  │  │         APPLICATION BUSINESS        │    │    │
│  │  │  ┌─────────────────────────────┐    │    │    │
│  │  │  │    ENTERPRISE BUSINESS      │    │    │    │
│  │  │  │       (ENTITIES)            │    │    │    │
│  │  │  └─────────────────────────────┘    │    │    │
│  │  └─────────────────────────────────────┘    │    │
│  └─────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────┘
```

### 6.2 As 4 Camadas

#### 1. **Entities (Entidades)**
- Regras de negócio mais gerais
- Não dependem de nada externo
- Pure Java Objects (POJOs)

#### 2. **Use Cases (Casos de Uso)**
- Regras específicas da aplicação
- Orquestram as entidades
- Definem o "o que" a aplicação faz

#### 3. **Interface Adapters (Adaptadores)**
- Convertem dados entre casos de uso e mundo externo
- Controllers, Presenters, Gateways

#### 4. **Frameworks & Drivers**
- Banco de dados, Web framework, APIs externas
- "Detalhes" que podem ser trocados

### 6.3 Regra da Dependência

```
Camada Externa ──pode depender──> Camada Interna
Camada Interna ──NUNCA────────────> Camada Externa
```

**Exemplo prático:**
```java
// ❌ ERRADO - Entidade depende de framework
@Entity // JPA Annotation - framework externo!
public class Usuario {
    @Id @GeneratedValue
    private Long id;
}

// ✅ CORRETO - Entidade pura
public class Usuario {
    private Long id;
    private String nome;
    
    // Apenas regras de negócio
    public boolean podeEnviarEmail() {
        return nome != null && !nome.isEmpty();
    }
}
```

### 6.4 Portas e Adaptadores

**Porta (Interface)** = Contrato definido pela aplicação
**Adaptador** = Implementação específica da infraestrutura

```java
// PORTA - definida pelo caso de uso
public interface UsuarioRepository {
    void salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    List<Usuario> buscarTodos();
}

// ADAPTADOR - implementação com JPA/Panache
@ApplicationScoped
public class UsuarioJpaRepository implements UsuarioRepository {
    // Implementação específica...
}

// ADAPTADOR - implementação com MongoDB
@ApplicationScoped  
public class UsuarioMongoRepository implements UsuarioRepository {
    // Implementação diferente, mesmo contrato...
}
```

---

## 7. Implementando Clean Architecture no Quarkus

### 7.1 Estrutura de Pacotes

```
src/main/java/com/exemplo/
├── domain/                          # Camada de Domínio
│   ├── entities/                    # Entidades do negócio
│   │   └── Usuario.java
│   ├── repositories/                # Portas (interfaces)
│   │   └── UsuarioRepository.java
│   └── services/                    # Regras de domínio
│       └── UsuarioService.java
├── application/                     # Camada de Aplicação  
│   ├── usecases/                    # Casos de uso
│   │   ├── CriarUsuarioUseCase.java
│   │   ├── BuscarUsuarioUseCase.java
│   │   └── ListarUsuariosUseCase.java
│   └── dto/                         # DTOs de entrada/saída
│       ├── CriarUsuarioRequest.java
│       └── UsuarioResponse.java
├── infrastructure/                  # Camada de Infraestrutura
│   ├── persistence/                 # Adaptadores de banco
│   │   ├── entities/
│   │   │   └── UsuarioEntity.java
│   │   └── UsuarioJpaRepository.java
│   └── web/                         # Adaptadores web
│       ├── UsuarioController.java
│       └── mappers/
│           └── UsuarioMapper.java
└── config/                          # Configurações
    └── BeanConfiguration.java
```

### 7.2 Camada de Domínio

#### Entidade de Domínio
```java
package com.exemplo.domain.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private LocalDateTime dataCriacao;
    private boolean ativo;
    
    // Construtor para criação
    public Usuario(String nome, String email) {
        this.nome = Objects.requireNonNull(nome, "Nome é obrigatório");
        this.email = Objects.requireNonNull(email, "Email é obrigatório");
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
        
        validarEmail();
        validarNome();
    }
    
    // Construtor para reconstrução (vindo do banco)
    public Usuario(Long id, String nome, String email, 
                   LocalDateTime dataCriacao, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }
    
    // Regras de negócio
    public void ativar() {
        this.ativo = true;
    }
    
    public void desativar() {
        this.ativo = false;
    }
    
    public boolean podeRecuperarSenha() {
        return ativo && email != null;
    }
    
    public void atualizarDados(String novoNome, String novoEmail) {
        if (novoNome != null) {
            this.nome = novoNome;
            validarNome();
        }
        if (novoEmail != null) {
            this.email = novoEmail;
            validarEmail();
        }
    }
    
    private void validarEmail() {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Email inválido");
        }
    }
    
    private void validarNome() {
        if (nome.trim().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
    }
    
    // Getters (sem setters - imutabilidade)
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public boolean isAtivo() { return ativo; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) && 
               Objects.equals(email, usuario.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
```

#### Repository Interface (Porta)
```java
package com.exemplo.domain.repositories;

import com.exemplo.domain.entities.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    void salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorEmail(String email);
    List<Usuario> buscarTodos();
    List<Usuario> buscarAtivos();
    List<Usuario> buscarPorNome(String nome);
    void remover(Long id);
    boolean existePorEmail(String email);
}
```

### 7.3 Camada de Aplicação

#### DTOs
```java
package com.exemplo.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CriarUsuarioRequest {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    public String nome;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    public String email;
    
    // Construtores
    public CriarUsuarioRequest() {}
    
    public CriarUsuarioRequest(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }
}

public class UsuarioResponse {
    public Long id;
    public String nome;
    public String email;
    public String dataCriacao;
    public boolean ativo;
    
    public UsuarioResponse() {}
    
    public UsuarioResponse(Long id, String nome, String email, 
                          String dataCriacao, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }
}
```

#### Use Cases
```java
package com.exemplo.application.usecases;

import com.exemplo.application.dto.CriarUsuarioRequest;
import com.exemplo.application.dto.UsuarioResponse;
import com.exemplo.domain.entities.Usuario;
import com.exemplo.domain.repositories.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class CriarUsuarioUseCase {
    
    @Inject
    UsuarioRepository usuarioRepository;
    
    public UsuarioResponse executar(CriarUsuarioRequest request) {
        // Verificar se já existe usuário com este email
        if (usuarioRepository.existePorEmail(request.email)) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        
        // Criar entidade de domínio
        Usuario usuario = new Usuario(request.nome, request.email);
        
        // Persistir
        usuarioRepository.salvar(usuario);
        
        // Retornar response
        return mapearParaResponse(usuario);
    }
    
    private UsuarioResponse mapearParaResponse(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getDataCriacao().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            usuario.isAtivo()
        );
    }
}

@ApplicationScoped
public class BuscarUsuarioUseCase {
    
    @Inject
    UsuarioRepository usuarioRepository;
    
    public UsuarioResponse executar(Long id) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        
        return mapearParaResponse(usuario);
    }
    
    private UsuarioResponse mapearParaResponse(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getDataCriacao().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            usuario.isAtivo()
        );
    }
}

@ApplicationScoped
public class ListarUsuariosUseCase {
    
    @Inject
    UsuarioRepository usuarioRepository;
    
    public List<UsuarioResponse> executar() {
        return usuarioRepository.buscarTodos()
            .stream()
            .map(this::mapearParaResponse)
            .collect(Collectors.toList());
    }
    
    public List<UsuarioResponse> executarApenaAtivos() {
        return usuarioRepository.buscarAtivos()
            .stream()
            .map(this::mapearParaResponse)
            .collect(Collectors.toList());
    }
    
    private UsuarioResponse mapearParaResponse(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getDataCriacao().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            usuario.isAtivo()
        );
    }
}
```

### 7.4 Camada de Infraestrutura

#### Entidade JPA (Adaptador)
```java
package com.exemplo.infrastructure.persistence.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity extends PanacheEntity {
    
    @Column(nullable = false)
    public String nome;
    
    @Column(unique = true, nullable = false)
    public String email;
    
    @Column(name = "data_criacao", nullable = false)
    public LocalDateTime dataCriacao;
    
    public Boolean ativo = true;
    
    // Construtores
    public UsuarioEntity() {}
    
    public UsuarioEntity(String nome, String email, LocalDateTime dataCriacao, Boolean ativo) {
        this.nome = nome;
        this.email = email;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }
    
    // Métodos de consulta do Panache
    public static UsuarioEntity findByEmail(String email) {
        return find("email", email).firstResult();
    }
    
    public static List<UsuarioEntity> findAtivos() {
        return find("ativo", true).list();
    }
    
    public static List<UsuarioEntity> findByNomeContaining(String nome) {
        return find("lower(nome) like lower(?1)", "%" + nome + "%").list();
    }
}
```

#### Repository Adapter (Implementação)
```java
package com.exemplo.infrastructure.persistence;

import com.exemplo.domain.entities.Usuario;
import com.exemplo.domain.repositories.UsuarioRepository;
import com.exemplo.infrastructure.persistence.entities.UsuarioEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class UsuarioJpaRepository implements UsuarioRepository {
    
    @Override
    @Transactional
    public void salvar(Usuario usuario) {
        UsuarioEntity entity;
        
        if (usuario.getId() == null) {
            // Novo usuário
            entity = new UsuarioEntity(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDataCriacao(),
                usuario.isAtivo()
            );
            entity.persist();
            
            // Atualizar ID na entidade de domínio (usando reflection ou método)
            // Nota: Em implementação real, você pode usar um padrão diferente
        } else {
            // Usuário existente
            entity = UsuarioEntity.findById(usuario.getId());
            if (entity != null) {
                entity.nome = usuario.getNome();
                entity.email = usuario.getEmail();
                entity.ativo = usuario.isAtivo();
                entity.persist();
            }
        }
    }
    
    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        UsuarioEntity entity = UsuarioEntity.findById(id);
        return entity != null ? 
            Optional.of(mapearParaDominio(entity)) : 
            Optional.empty();
    }
    
    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        UsuarioEntity entity = UsuarioEntity.findByEmail(email);
        return entity != null ? 
            Optional.of(mapearParaDominio(entity)) : 
            Optional.empty();
    }
    
    @Override
    public List<Usuario> buscarTodos() {
        return UsuarioEntity.<UsuarioEntity>listAll()
            .stream()
            .map(this::mapearParaDominio)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Usuario> buscarAtivos() {
        return UsuarioEntity.findAtivos()
            .stream()
            .map(this::mapearParaDominio)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Usuario> buscarPorNome(String nome) {
        return UsuarioEntity.findByNomeContaining(nome)
            .stream()
            .map(this::mapearParaDominio)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void remover(Long id) {
        UsuarioEntity.deleteById(id);
    }
    
    @Override
    public boolean existePorEmail(String email) {
        return UsuarioEntity.count("email", email) > 0;
    }
    
    // Mapper: Entity → Domain
    private Usuario mapearParaDominio(UsuarioEntity entity) {
        return new Usuario(
            entity.id,
            entity.nome,
            entity.email,
            entity.dataCriacao,
            entity.ativo
        );
    }
}
```

#### Controller (Adaptador Web)
```java
package com.exemplo.infrastructure.web;

import com.exemplo.application.dto.CriarUsuarioRequest;
import com.exemplo.application.dto.UsuarioResponse;
import com.exemplo.application.usecases.BuscarUsuarioUseCase;
import com.exemplo.application.usecases.CriarUsuarioUseCase;
import com.exemplo.application.usecases.ListarUsuariosUseCase;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioController {
    
    @Inject
    CriarUsuarioUseCase criarUsuarioUseCase;
    
    @Inject
    BuscarUsuarioUseCase buscarUsuarioUseCase;
    
    @Inject
    ListarUsuariosUseCase listarUsuariosUseCase;
    
    @POST
    public Response criar(@Valid CriarUsuarioRequest request) {
        try {
            UsuarioResponse response = criarUsuarioUseCase.executar(request);
            return Response.status(201).entity(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(400)
                .entity(new ErroResponse("Dados inválidos", e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            UsuarioResponse response = buscarUsuarioUseCase.executar(id);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(404)
                .entity(new ErroResponse("Não encontrado", e.getMessage()))
                .build();
        }
    }
    
    @GET
    public Response listar(@QueryParam("apenasAtivos") @DefaultValue("false") boolean apenasAtivos) {
        List<UsuarioResponse> usuarios = apenasAtivos ? 
            listarUsuariosUseCase.executarApenaAtivos() : 
            listarUsuariosUseCase.executar();
            
        return Response.ok(usuarios).build();
    }
}

// Classe para respostas de erro
class ErroResponse {
    public String tipo;
    public String mensagem;
    
    public ErroResponse() {}
    
    public ErroResponse(String tipo, String mensagem) {
        this.tipo = tipo;
        this.mensagem = mensagem;
    }
}
```

---

## 8. Testes Unitários e de Integração

### 8.1 Configuração de Testes

**Dependências no pom.xml:**
```xml
<dependencies>
    <!-- Dependências principais -->
    
    <!-- Testes -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-junit5</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-test-h2</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

**Configuração de teste (application-test.properties):**
```properties
# Banco H2 em memória para testes
quarkus.datasource.db-kind=h2
quarkus.datasource.username=sa
quarkus.datasource.password=
quarkus.datasource.jdbc.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.sql-load-script=test-data.sql

# Log para debug nos testes
quarkus.log.level=INFO
quarkus.hibernate-orm.log.sql=true
```

### 8.2 Testes Unitários

#### Testando Entidades de Domínio
```java
package com.exemplo.domain.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {
    
    @Test
    @DisplayName("Deve criar usuário válido com sucesso")
    void deveCriarUsuarioValido() {
        // Given
        String nome = "João Silva";
        String email = "joao@exemplo.com";
        
        // When
        Usuario usuario = new Usuario(nome, email);
        
        // Then
        assertNotNull(usuario);
        assertEquals(nome, usuario.getNome());
        assertEquals(email, usuario.getEmail());
        assertTrue(usuario.isAtivo());
        assertNotNull(usuario.getDataCriacao());
    }
    
    @Test
    @DisplayName("Deve rejeitar email inválido")
    void deveRejeitarEmailInvalido() {
        // Given
        String nome = "João Silva";
        String emailInvalido = "email-inválido";
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Usuario(nome, emailInvalido)
        );
        
        assertEquals("Email inválido", exception.getMessage());
    }
    
    @Test
    @DisplayName("Deve rejeitar nome muito curto")
    void deveRejeitarNomeCurto() {
        // Given
        String nomeCurto = "A";
        String email = "joao@exemplo.com";
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Usuario(nomeCurto, email)
        );
        
        assertEquals("Nome deve ter pelo menos 2 caracteres", exception.getMessage());
    }
    
    @Test
    @DisplayName("Deve permitir ativar e desativar usuário")
    void devePermitirAtivarDesativar() {
        // Given
        Usuario usuario = new Usuario("João", "joao@exemplo.com");
        
        // When
        usuario.desativar();
        
        // Then
        assertFalse(usuario.isAtivo());
        
        // When
        usuario.ativar();
        
        // Then
        assertTrue(usuario.isAtivo());
    }
    
    @Test
    @DisplayName("Usuário ativo deve poder recuperar senha")
    void usuarioAtivoDevePodeRecuperarSenha() {
        // Given
        Usuario usuario = new Usuario("João", "joao@exemplo.com");
        
        // Then
        assertTrue(usuario.podeRecuperarSenha());
    }
    
    @Test
    @DisplayName("Usuário inativo não deve poder recuperar senha")
    void usuarioInativoNaoDevePodeRecuperarSenha() {
        // Given
        Usuario usuario = new Usuario("João", "joao@exemplo.com");
        usuario.desativar();
        
        // Then
        assertFalse(usuario.podeRecuperarSenha());
    }
}
```

#### Testando Use Cases com Mocks
```java
package com.exemplo.application.usecases;

import com.exemplo.application.dto.CriarUsuarioRequest;
import com.exemplo.application.dto.UsuarioResponse;
import com.exemplo.domain.entities.Usuario;
import com.exemplo.domain.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CriarUsuarioUseCaseTest {
    
    @Mock
    UsuarioRepository usuarioRepository;
    
    @InjectMocks
    CriarUsuarioUseCase criarUsuarioUseCase;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        // Given
        CriarUsuarioRequest request = new CriarUsuarioRequest("João Silva", "joao@exemplo.com");
        
        // Mock: email não existe
        when(usuarioRepository.existePorEmail(request.email)).thenReturn(false);
        
        // Mock: salvar usuário (simular ID gerado)
        doAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            // Simular que o banco definiu um ID
            // Em uma implementação real, você usaria reflection ou outro método
            return null;
        }).when(usuarioRepository).salvar(any(Usuario.class));
        
        // When
        UsuarioResponse response = criarUsuarioUseCase.executar(request);
        
        // Then
        assertNotNull(response);
        assertEquals(request.nome, response.nome);
        assertEquals(request.email, response.email);
        assertTrue(response.ativo);
        
        // Verificar interações
        verify(usuarioRepository).existePorEmail(request.email);
        verify(usuarioRepository).salvar(any(Usuario.class));
    }
    
    @Test
    @DisplayName("Deve rejeitar email já existente")
    void deveRejeitarEmailExistente() {
        // Given
        CriarUsuarioRequest request = new CriarUsuarioRequest("João Silva", "joao@exemplo.com");
        
        // Mock: email já existe
        when(usuarioRepository.existePorEmail(request.email)).thenReturn(true);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> criarUsuarioUseCase.executar(request)
        );
        
        assertEquals("Email já cadastrado", exception.getMessage());
        
        // Verificar que não tentou salvar
        verify(usuarioRepository).existePorEmail(request.email);
        verify(usuarioRepository, never()).salvar(any(Usuario.class));
    }
}
```

### 8.3 Testes de Integração

#### Testando Repository
```java
package com.exemplo.infrastructure.persistence;

import com.exemplo.domain.entities.Usuario;
import com.exemplo.domain.repositories.UsuarioRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UsuarioJpaRepositoryTest {
    
    @Inject
    UsuarioRepository usuarioRepository;
    
    @Test
    @Transactional
    @DisplayName("Deve salvar e buscar usuário por ID")
    void deveSalvarEBuscarPorId() {
        // Given
        Usuario usuario = new Usuario("João Silva", "joao@exemplo.com");
        
        // When
        usuarioRepository.salvar(usuario);
        
        // Then
        assertNotNull(usuario.getId());
        
        var usuarioEncontrado = usuarioRepository.buscarPorId(usuario.getId());
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals(usuario.getNome(), usuarioEncontrado.get().getNome());
        assertEquals(usuario.getEmail(), usuarioEncontrado.get().getEmail());
    }
    
    @Test
    @Transactional
    @DisplayName("Deve buscar usuário por email")
    void deveBuscarPorEmail() {
        // Given
        Usuario usuario = new Usuario("Maria Silva", "maria@exemplo.com");
        usuarioRepository.salvar(usuario);
        
        // When
        var usuarioEncontrado = usuarioRepository.buscarPorEmail("maria@exemplo.com");
        
        // Then
        assertTrue(usuarioEncontrado.isPresent());
        assertEquals("Maria Silva", usuarioEncontrado.get().getNome());
    }
    
    @Test
    @Transactional
    @DisplayName("Deve verificar se email existe")
    void deveVerificarSeEmailExiste() {
        // Given
        Usuario usuario = new Usuario("Carlos Silva", "carlos@exemplo.com");
        usuarioRepository.salvar(usuario);
        
        // When & Then
        assertTrue(usuarioRepository.existePorEmail("carlos@exemplo.com"));
        assertFalse(usuarioRepository.existePorEmail("naoexiste@exemplo.com"));
    }
    
    @Test
    @Transactional
    @DisplayName("Deve listar apenas usuários ativos")
    void deveListarApenasUsuariosAtivos() {
        // Given
        Usuario ativo = new Usuario("Ativo", "ativo@exemplo.com");
        Usuario inativo = new Usuario("Inativo", "inativo@exemplo.com");
        inativo.desativar();
        
        usuarioRepository.salvar(ativo);
        usuarioRepository.salvar(inativo);
        
        // When
        var usuariosAtivos = usuarioRepository.buscarAtivos();
        
        // Then
        assertEquals(1, usuariosAtivos.size());
        assertEquals("Ativo", usuariosAtivos.get(0).getNome());
    }
}
```

#### Testando Controller (API)
```java
package com.exemplo.infrastructure.web;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class UsuarioControllerTest {
    
    @Test
    @DisplayName("Deve criar usuário via API")
    void deveCriarUsuarioViaApi() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "nome": "João Silva",
                    "email": "joao@exemplo.com"
                }
                """)
        .when()
            .post("/api/usuarios")
        .then()
            .statusCode(201)
            .body("nome", equalTo("João Silva"))
            .body("email", equalTo("joao@exemplo.com"))
            .body("ativo", equalTo(true))
            .body("id", notNullValue());
    }
    
    @Test
    @DisplayName("Deve rejeitar usuário com dados inválidos")
    void deveRejeitarUsuarioComDadosInvalidos() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "nome": "",
                    "email": "email-invalido"
                }
                """)
        .when()
            .post("/api/usuarios")
        .then()
            .statusCode(400)
            .body("tipo", equalTo("Dados inválidos"));
    }
    
    @Test
    @DisplayName("Deve buscar usuário por ID")
    void deveBuscarUsuarioPorId() {
        // Primeiro criar usuário
        var response = given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "nome": "Maria Silva",
                    "email": "maria@exemplo.com"
                }
                """)
        .when()
            .post("/api/usuarios")
        .then()
            .statusCode(201)
            .extract()
            .path("id");
        
        // Buscar por ID
        given()
            .pathParam("id", response)
        .when()
            .get("/api/usuarios/{id}")
        .then()
            .statusCode(200)
            .body("nome", equalTo("Maria Silva"))
            .body("email", equalTo("maria@exemplo.com"));
    }
    
    @Test
    @DisplayName("Deve retornar 404 para usuário não encontrado")
    void deveRetornar404ParaUsuarioNaoEncontrado() {
        given()
            .pathParam("id", 99999)
        .when()
            .get("/api/usuarios/{id}")
        .then()
            .statusCode(404)
            .body("tipo", equalTo("Não encontrado"));
    }
    
    @Test
    @DisplayName("Deve listar todos os usuários")
    void deveListarTodosUsuarios() {
        // Criar alguns usuários primeiro
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "nome": "Alice",
                    "email": "alice@exemplo.com"
                }
                """)
        .when()
            .post("/api/usuarios");
            
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "nome": "Bob",
                    "email": "bob@exemplo.com"
                }
                """)
        .when()
            .post("/api/usuarios");
        
        // Listar usuários
        given()
        .when()
            .get("/api/usuarios")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(2));
    }
}
```

### 8.4 Testes com TestContainers (PostgreSQL)

Para testes mais realistas com banco PostgreSQL:

**Dependência adicional:**
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-test-postgres</artifactId>
    <scope>test</scope>
</dependency>
```

**Teste com PostgreSQL:**
```java
package com.exemplo.integration;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestProfile(PostgresTestProfile.class)
class UsuarioIntegrationTest {
    
    @Test
    void testeComPostgres() {
        // Testes rodando com PostgreSQL via TestContainers
    }
}

// Profile de teste personalizado
class PostgresTestProfile implements QuarkusTestProfile {
    
    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
            "quarkus.datasource.db-kind", "postgresql",
            "quarkus.datasource.username", "test",
            "quarkus.datasource.password", "test"
        );
    }
}
```

---

## 9. Projeto Prático: Sistema de Usuários

Vamos construir um sistema completo de gerenciamento de usuários aplicando todos os conceitos aprendidos.

### 9.1 Funcionalidades

O sistema terá as seguintes funcionalidades:
- ✅ Criar usuário
- ✅ Buscar usuário por ID
- ✅ Buscar usuário por email
- ✅ Listar usuários (com filtro ativo/inativo)
- ✅ Atualizar dados do usuário
- ✅ Desativar usuário
- ✅ Ativar usuário
- ✅ Pesquisar usuários por nome

### 9.2 Expandindo o Domínio

#### Entidade Usuario Completa
```java
package com.exemplo.domain.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private boolean ativo;
    
    // Construtor para criação
    public Usuario(String nome, String email, String telefone) {
        this.nome = Objects.requireNonNull(nome, "Nome é obrigatório");
        this.email = Objects.requireNonNull(email, "Email é obrigatório");
        this.telefone = telefone; // Opcional
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
        this.ativo = true;
        
        validarDados();
    }
    
    // Construtor para reconstrução (banco de dados)
    public Usuario(Long id, String nome, String email, String telefone,
                   LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.ativo = ativo;
    }
    
    // Métodos de negócio
    public void atualizarDados(String novoNome, String novoEmail, String novoTelefone) {
        if (novoNome != null && !novoNome.trim().equals(this.nome)) {
            this.nome = novoNome.trim();
        }
        
        if (novoEmail != null && !novoEmail.equals(this.email)) {
            this.email = novoEmail;
            validarEmail();
        }
        
        if (novoTelefone != null) {
            this.telefone = novoTelefone.trim().isEmpty() ? null : novoTelefone.trim();
        }
        
        this.dataAtualizacao = LocalDateTime.now();
        validarDados();
    }
    
    public void ativar() {
        if (!this.ativo) {
            this.ativo = true;
            this.dataAtualizacao = LocalDateTime.now();
        }
    }
    
    public void desativar() {
        if (this.ativo) {
            this.ativo = false;
            this.dataAtualizacao = LocalDateTime.now();
        }
    }
    
    public boolean podeReceberNotificacoes() {
        return ativo && (email != null || telefone != null);
    }
    
    public boolean foiAtualizadoRecentemente() {
        return dataAtualizacao.isAfter(dataCriacao.plusMinutes(5));
    }
    
    private void validarDados() {
        validarNome();
        validarEmail();
        if (telefone != null) {
            validarTelefone();
        }
    }
    
    private void validarNome() {
        if (nome == null || nome.trim().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
        if (nome.trim().length() > 100) {
            throw new IllegalArgumentException("Nome deve ter no máximo 100 caracteres");
        }
    }
    
    private void validarEmail() {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Email inválido");
        }
    }
    
    private void validarTelefone() {
        // Aceita formatos: (11) 99999-9999, 11 999999999, +55 11 999999999
        String telefoneNumeros = telefone.replaceAll("[^0-9]", "");
        if (telefoneNumeros.length() < 10 || telefoneNumeros.length() > 13) {
            throw new IllegalArgumentException("Telefone deve ter entre 10 e 13 dígitos");
        }
    }
    
    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public boolean isAtivo() { return ativo; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) && Objects.equals(email, usuario.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
    
    @Override
    public String toString() {
        return String.format("Usuario{id=%d, nome='%s', email='%s', ativo=%s}", 
                           id, nome, email, ativo);
    }
}
```

#### Repository Interface Expandida
```java
package com.exemplo.domain.repositories;

import com.exemplo.domain.entities.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    void salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorEmail(String email);
    List<Usuario> buscarTodos();
    List<Usuario> buscarAtivos();
    List<Usuario> buscarInativos();
    List<Usuario> buscarPorNome(String nome);
    List<Usuario> buscarPorTelefone(String telefone);
    List<Usuario> buscarComFiltro(String filtro, Boolean ativo);
    void remover(Long id);
    boolean existePorEmail(String email);
    boolean existePorTelefone(String telefone);
    long contarTotal();
    long contarAtivos();
    
    // Paginação
    List<Usuario> buscarPaginado(int pagina, int tamanho);
    List<Usuario> buscarAtivosPaginado(int pagina, int tamanho);
}
```

### 9.3 DTOs Expandidos

#### Request DTOs
```java
package com.exemplo.application.dto;

import jakarta.validation.constraints.*;

public class CriarUsuarioRequest {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    public String nome;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    public String email;
    
    @Pattern(regexp = "^[\\d\\s\\(\\)\\-\\+]*$", message = "Telefone deve conter apenas números e símbolos válidos")
    public String telefone;
    
    public CriarUsuarioRequest() {}
    
    public CriarUsuarioRequest(String nome, String email, String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }
}

public class AtualizarUsuarioRequest {
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    public String nome;
    
    @Email(message = "Email deve ser válido")
    public String email;
    
    @Pattern(regexp = "^[\\d\\s\\(\\)\\-\\+]*$", message = "Telefone deve conter apenas números e símbolos válidos")
    public String telefone;
    
    public AtualizarUsuarioRequest() {}
    
    public AtualizarUsuarioRequest(String nome, String email, String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }
}

public class FiltroUsuarioRequest {
    public String nome;
    public String email;
    public String telefone;
    public Boolean ativo;
    public Integer pagina = 0;
    public Integer tamanho = 20;
    
    public FiltroUsuarioRequest() {}
}
```

#### Response DTOs
```java
package com.exemplo.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class UsuarioResponse {
    public Long id;
    public String nome;
    public String email;
    public String telefone;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime dataCriacao;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime dataAtualizacao;
    
    public boolean ativo;
    public boolean podeReceberNotificacoes;
    public boolean foiAtualizadoRecentemente;
    
    public UsuarioResponse() {}
    
    public UsuarioResponse(Long id, String nome, String email, String telefone,
                          LocalDateTime dataCriacao, LocalDateTime dataAtualizacao,
                          boolean ativo, boolean podeReceberNotificacoes,
                          boolean foiAtualizadoRecentemente) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.ativo = ativo;
        this.podeReceberNotificacoes = podeReceberNotificacoes;
        this.foiAtualizadoRecentemente = foiAtualizadoRecentemente;
    }
}

public class ListaUsuariosResponse {
    public List<UsuarioResponse> usuarios;
    public int total;
    public int pagina;
    public int tamanho;
    public boolean temProximaPagina;
    
    public ListaUsuariosResponse() {}
    
    public ListaUsuariosResponse(List<UsuarioResponse> usuarios, int total, 
                                int pagina, int tamanho) {
        this.usuarios = usuarios;
        this.total = total;
        this.pagina = pagina;
        this.tamanho = tamanho;
        this.temProximaPagina = (pagina + 1) * tamanho < total;
    }
}
```

### 9.4 Use Cases Completos

#### Mapper Utility
```java
package com.exemplo.application.mappers;

import com.exemplo.application.dto.UsuarioResponse;
import com.exemplo.domain.entities.Usuario;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioMapper {
    
    public UsuarioResponse mapearParaResponse(Usuario usuario) {
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getTelefone(),
            usuario.getDataCriacao(),
            usuario.getDataAtualizacao(),
            usuario.isAtivo(),
            usuario.podeReceberNotificacoes(),
            usuario.foiAtualizadoRecentemente()
        );
    }
    
    public List<UsuarioResponse> mapearParaResponse(List<Usuario> usuarios) {
        return usuarios.stream()
            .map(this::mapearParaResponse)
            .collect(Collectors.toList());
    }
}
```

#### Use Cases Completos
```java
package com.exemplo.application.usecases;

import com.exemplo.application.dto.*;
import com.exemplo.application.mappers.UsuarioMapper;
import com.exemplo.domain.entities.Usuario;
import com.exemplo.domain.repositories.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class CriarUsuarioUseCase {
    
    @Inject
    UsuarioRepository usuarioRepository;
    
    @Inject
    UsuarioMapper usuarioMapper;
    
    public UsuarioResponse executar(CriarUsuarioRequest request) {
        // Verificações de negócio
        if (usuarioRepository.existePorEmail(request.email)) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        
        if (request.telefone != null && !request.telefone.trim().isEmpty()) {
            if (usuarioRepository.existePorTelefone(request.telefone)) {
                throw new IllegalArgumentException("Telefone já cadastrado");
            }
        }
        
        // Criar entidade
        Usuario usuario = new Usuario(request.nome, request.email, request.telefone);
        
        // Persistir
        usuarioRepository.salvar(usuario);
        
        return usuarioMapper.mapearParaResponse(usuario);
    }
}

@ApplicationScoped
public class AtualizarUsuarioUseCase {
    
    @Inject
    UsuarioRepository usuarioRepository;
    
    @Inject
    UsuarioMapper usuarioMapper;
    
    public UsuarioResponse executar(Long id, AtualizarUsuarioRequest request) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        
        // Verificar se novo email já existe (se foi alterado)
        if (request.email != null && !request.email.equals(usuario.getEmail())) {
            if (usuarioRepository.existePorEmail(request.email)) {
                throw new IllegalArgumentException("Email já cadastrado para outro usuário");
            }
        }
        
        // Verificar se novo telefone já existe (se foi alterado)
        if (request.telefone != null && !request.telefone.equals(usuario.getTelefone())) {
            if (usuarioRepository.existePorTelefone(request.telefone)) {
                throw new IllegalArgumentException("Telefone já cadastrado para outro usuário");
            }
        }
        
        // Atualizar dados
        usuario.atualizarDados(request.nome, request.email, request.telefone);
        
        // Persistir alterações
        usuarioRepository.salvar(usuario);
        
        return usuarioMapper.mapearParaResponse(usuario);
    }
}

@ApplicationScoped
public class BuscarUsuarioUseCase {
    
    @Inject
    UsuarioRepository usuarioRepository;
    
    @Inject
    UsuarioMapper usuarioMapper;
    
    public UsuarioResponse executarPorId(Long id) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        
        return usuarioMapper.mapearParaResponse(usuario);
    }
    
    public UsuarioResponse executarPorEmail(String email) {
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        
        return usuarioMapper.mapearParaResponse(usuario);
    }
}

@ApplicationScoped
public class ListarUsuariosUseCase {
    
    @Inject
    UsuarioRepository usuarioRepository;
    
    @Inject
    UsuarioMapper usuarioMapper;
    
    public ListaUsuariosResponse executar(FiltroUsuarioRequest filtro) {
        List<Usuario> usuarios;
        
        // Aplicar filtros
        if (filtro.nome != null || filtro.email != null || filtro.ativo != null) {
            String termoFiltro = construirTermoFiltro(filtro);
            usuarios = usuarioRepository.buscarComFiltro(termoFiltro, filtro.ativo);
        } else {
            usuarios = usuarioRepository.buscarTodos();
        }
        
        // Aplicar paginação (simulada - em produção, seria no banco)
        int inicio = filtro.pagina * filtro.tamanho;
        int fim = Math.min(inicio + filtro.tamanho, usuarios.size());
        
        List<Usuario> usuariosPaginados = usuarios.subList(inicio, fim);
        List<UsuarioResponse> usuariosResponse = usuarioMapper.mapearParaResponse(usuariosPaginados);
        
        return new ListaUsuariosResponse(usuariosResponse, usuarios.size(), 
                                        filtro.pagina, filtro.tamanho);
    }
    
    private String construirTermoFiltro(FiltroUsuarioRequest filtro) {
        if (filtro.nome != null && !filtro.nome.trim().isEmpty()) {
            return filtro.nome.trim();
        }
        if (filtro.email != null && !filtro.email.trim().isEmpty()) {
            return filtro.email.trim();
        }
        return "";
    }
}

@ApplicationScoped
public class GerenciarStatusUsuarioUseCase {
    
    @Inject
    UsuarioRepository usuarioRepository;
    
    @Inject
    UsuarioMapper usuarioMapper;
    
    public UsuarioResponse ativarUsuario(Long id) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        
        usuario.ativar();
        usuarioRepository.salvar(usuario);
        
        return usuarioMapper.mapearParaResponse(usuario);
    }
    
    public UsuarioResponse desativarUsuario(Long id) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        
        usuario.desativar();
        usuarioRepository.salvar(usuario);
        
        return usuarioMapper.mapearParaResponse(usuario);
    }
}
```

### 9.5 Implementação de Infraestrutura Completa

#### Entidade JPA Completa
```java
package com.exemplo.infrastructure.persistence.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios", indexes = {
    @Index(name = "idx_usuario_email", columnList = "email"),
    @Index(name = "idx_usuario_ativo", columnList = "ativo"),
    @Index(name = "idx_usuario_nome", columnList = "nome")
})
public class UsuarioEntity extends PanacheEntity {
    
    @Column(nullable = false, length = 100)
    public String nome;
    
    @Column(unique = true, nullable = false, length = 100)
    public String email;
    
    @Column(length = 20)
    public String telefone;
    
    @Column(name = "data_criacao", nullable = false)
    public LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao", nullable = false)
    public LocalDateTime dataAtualizacao;
    
    @Column(nullable = false)
    public Boolean ativo = true;
    
    // Construtores
    public UsuarioEntity() {}
    
    public UsuarioEntity(String nome, String email, String telefone,
                        LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, Boolean ativo) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.ativo = ativo;
    }
    
    // Métodos de consulta Panache
    public static UsuarioEntity findByEmail(String email) {
        return find("email", email).firstResult();
    }
    
    public static UsuarioEntity findByTelefone(String telefone) {
        return find("telefone", telefone).firstResult();
    }
    
    public static List<UsuarioEntity> findAtivos() {
        return find("ativo = true order by nome").list();
    }
    
    public static List<UsuarioEntity> findInativos() {
        return find("ativo = false order by nome").list();
    }
    
    public static List<UsuarioEntity> findByNomeContaining(String nome) {
        return find("lower(nome) like lower(?1) order by nome", "%" + nome + "%").list();
    }
    
    public static List<UsuarioEntity> findComFiltro(String filtro, Boolean ativo) {
        StringBuilder query = new StringBuilder("1=1");
        
        if (filtro != null && !filtro.trim().isEmpty()) {
            query.append(" and (lower(nome) like lower(?1) or lower(email) like lower(?1))");
        }
        
        if (ativo != null) {
            query.append(" and ativo = ?2");
        }
        
        query.append(" order by nome");
        
        if (filtro != null && !filtro.trim().isEmpty() && ativo != null) {
            return find(query.toString(), "%" + filtro + "%", ativo).list();
        } else if (filtro != null && !filtro.trim().isEmpty()) {
            return find(query.toString(), "%" + filtro + "%").list();
        } else if (ativo != null) {
            return find("ativo = ?1 order by nome", ativo).list();
        }
        
        return find("order by nome").list();
    }
    
    public static boolean existsByEmail(String email) {
        return count("email", email) > 0;
    }
    
    public static boolean existsByTelefone(String telefone) {
        return count("telefone", telefone) > 0;
    }
    
    public static long countAtivos() {
        return count("ativo", true);
    }
}
```

#### Repository Implementation Completa
```java
package com.exemplo.infrastructure.persistence;

import com.exemplo.domain.entities.Usuario;
import com.exemplo.domain.repositories.UsuarioRepository;
import com.exemplo.infrastructure.persistence.entities.UsuarioEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class UsuarioJpaRepository implements UsuarioRepository {
    
    @Override
    @Transactional
    public void salvar(Usuario usuario) {
        UsuarioEntity entity;
        
        if (usuario.getId() == null) {
            // Novo usuário
            entity = new UsuarioEntity(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getDataCriacao(),
                usuario.getDataAtualizacao(),
                usuario.isAtivo()
            );
            entity.persist();
            
            // Simular atualização do ID (em implementação real, use reflection ou padrão específico)
            // usuario.setId(entity.id);
            
        } else {
            // Usuário existente - atualizar
            entity = UsuarioEntity.findById(usuario.getId());
            if (entity != null) {
                entity.nome = usuario.getNome();
                entity.email = usuario.getEmail();
                entity.telefone = usuario.getTelefone();
                entity.dataAtualizacao = usuario.getDataAtualizacao();
                entity.ativo = usuario.isAtivo();
                entity.persist();
            }
        }
    }
    
    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        UsuarioEntity entity = UsuarioEntity.findById(id);
        return entity != null ? 
            Optional.of(mapearParaDominio(entity)) : 
            Optional.empty();
    }
    
    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        UsuarioEntity entity = UsuarioEntity.findByEmail(email);
        return entity != null ? 
            Optional.of(mapearParaDominio(entity)) : 
            Optional.empty();
    }
    
    @Override
    public List<Usuario> buscarTodos() {
        return UsuarioEntity.<UsuarioEntity>listAll()
            .stream()
            .map(this::mapearParaDominio)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Usuario> buscarAtivos() {
        return UsuarioEntity.findAtivos()
            .stream()
            .map(this::mapearParaDominio)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Usuario> buscarInativos() {
        return UsuarioEntity.findInativos()
            .stream()
            .map(this::mapearParaDominio)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Usuario> buscarPorNome(String nome) {
        return UsuarioEntity.findByNomeContaining(nome)
            .stream()
            .map(this::mapearParaDominio)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Usuario> buscarPorTelefone(String telefone) {
        UsuarioEntity entity = UsuarioEntity.findByTelefone(telefone);
        return entity != null ? 
            List.of(mapearParaDominio(entity)) : 
            List.of();
    }
    
    @Override
    public List<Usuario> buscarComFiltro(String filtro, Boolean ativo) {
        return UsuarioEntity.findComFiltro(filtro, ativo)
            .stream()
            .map(this::mapearParaDominio)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void remover(Long id) {
        UsuarioEntity.deleteById(id);
    }
    
    @Override
    public boolean existePorEmail(String email) {
        return UsuarioEntity.existsByEmail(email);
    }
    
    @Override
    public boolean existePorTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }
        return UsuarioEntity.existsByTelefone(telefone);
    }
    
    @Override
    public long contarTotal() {
        return UsuarioEntity.count();
    }
    
    @Override
    public long contarAtivos() {
        return UsuarioEntity.countAtivos();
    }
    
    @Override
    public List<Usuario> buscarPaginado(int pagina, int tamanho) {
        return UsuarioEntity.<UsuarioEntity>find("order by nome")
            .page(pagina, tamanho)
            .list()
            .stream()
            .map(this::mapearParaDominio)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Usuario> buscarAtivosPaginado(int pagina, int tamanho) {
        return UsuarioEntity.<UsuarioEntity>find("ativo = true order by nome")
            .page(pagina, tamanho)
            .list()
            .stream()
            .map(this::mapearParaDominio)
            .collect(Collectors.toList());
    }
    
    // Mapper: Entity → Domain
    private Usuario mapearParaDominio(UsuarioEntity entity) {
        return new Usuario(
            entity.id,
            entity.nome,
            entity.email,
            entity.telefone,
            entity.dataCriacao,
            entity.dataAtualizacao,
            entity.ativo
        );
    }
}
```

### 9.6 Controller Completo

```java
package com.exemplo.infrastructure.web;

import com.exemplo.application.dto.*;
import com.exemplo.application.usecases.*;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioController {
    
    @Inject
    CriarUsuarioUseCase criarUsuarioUseCase;
    
    @Inject
    BuscarUsuarioUseCase buscarUsuarioUseCase;
    
    @Inject
    ListarUsuariosUseCase listarUsuariosUseCase;
    
    @Inject
    AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    
    @Inject
    GerenciarStatusUsuarioUseCase gerenciarStatusUsuarioUseCase;
    
    @POST
    public Response criar(@Valid CriarUsuarioRequest request) {
        try {
            UsuarioResponse response = criarUsuarioUseCase.executar(request);
            return Response.status(201).entity(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(400)
                .entity(new ErroResponse("Dados inválidos", e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            UsuarioResponse response = buscarUsuarioUseCase.executarPorId(id);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(404)
                .entity(new ErroResponse("Não encontrado", e.getMessage()))
                .build();
        }
    }
    
    @GET
    public Response listar(@QueryParam("nome") String nome,
                          @QueryParam("email") String email,
                          @QueryParam("ativo") Boolean ativo,
                          @QueryParam("pagina") @DefaultValue("0") int pagina,
                          @QueryParam("tamanho") @DefaultValue("20") int tamanho) {
        
        FiltroUsuarioRequest filtro = new FiltroUsuarioRequest();
        filtro.nome = nome;
        filtro.email = email;
        filtro.ativo = ativo;
        filtro.pagina = pagina;
        filtro.tamanho = tamanho;
        
        ListaUsuariosResponse response = listarUsuariosUseCase.executar(filtro);
        return Response.ok(response).build();
    }
    
    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, @Valid AtualizarUsuarioRequest request) {
        try {
            UsuarioResponse response = atualizarUsuarioUseCase.executar(id, request);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(400)
                .entity(new ErroResponse("Erro na atualização", e.getMessage()))
                .build();
        }
    }
    
    @PATCH
    @Path("/{id}/ativar")
    public Response ativar(@PathParam("id") Long id) {
        try {
            UsuarioResponse response = gerenciarStatusUsuarioUseCase.ativarUsuario(id);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(404)
                .entity(new ErroResponse("Não encontrado", e.getMessage()))
                .build();
        }
    }
    
    @PATCH
    @Path("/{id}/desativar")
    public Response desativar(@PathParam("id") Long id) {
        try {
            UsuarioResponse response = gerenciarStatusUsuarioUseCase.desativarUsuario(id);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(404)
                .entity(new ErroResponse("Não encontrado", e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/email/{email}")
    public Response buscarPorEmail(@PathParam("email") String email) {
        try {
            UsuarioResponse response = buscarUsuarioUseCase.executarPorEmail(email);
            return Response.ok(response).build();
        } catch (IllegalArgumentException e) {
            return Response.status(404)
                .entity(new ErroResponse("Não encontrado", e.getMessage()))
                .build();
        }
    }
}
```

### 9.7 Scripts de Banco de Dados

#### Schema SQL (src/main/resources/import.sql)
```sql
-- Criação da tabela (apenas para referência, o Hibernate criará automaticamente)
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    data_criacao TIMESTAMP NOT NULL,
    data_atualizacao TIMESTAMP NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuarios(email);
CREATE INDEX IF NOT EXISTS idx_usuario_ativo ON usuarios(ativo);
CREATE INDEX IF NOT EXISTS idx_usuario_nome ON usuarios(nome);

-- Dados de teste
INSERT INTO usuarios (nome, email, telefone, data_criacao, data_atualizacao, ativo) VALUES
('João Silva', 'joao@exemplo.com', '(11) 99999-9999', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true),
('Maria Santos', 'maria@exemplo.com', '(11) 88888-8888', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true),
('Pedro Oliveira', 'pedro@exemplo.com', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('Ana Costa', 'ana@exemplo.com', '(11) 77777-7777', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true),
('Carlos Ferreira', 'carlos@exemplo.com', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true);
```

#### Configuração para PostgreSQL (Produção)
```properties
# application-prod.properties
# Configurações de produção
quarkus.profile=prod

# PostgreSQL
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=${DB_USERNAME:postgres}
quarkus.datasource.password=${DB_PASSWORD:password}
quarkus.datasource.jdbc.url=${DB_URL:jdbc:postgresql://localhost:5432/usuarios_db}

# Hibernate em produção
quarkus.hibernate-orm.database.generation=update
quarkus.hibernate-orm.log.sql=false

# Pool de conexões
quarkus.datasource.jdbc.min-size=5
quarkus.datasource.jdbc.max-size=20

# Logs em produção
quarkus.log.level=WARN
quarkus.log.category."com.exemplo".level=INFO
```

### 9.8 Testes Completos do Sistema

#### Teste de Integração Completo
```java
package com.exemplo.integration;

import com.exemplo.application.dto.CriarUsuarioRequest;
import com.exemplo.application.dto.AtualizarUsuarioRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SistemaUsuariosIntegrationTest {
    
    private static Long usuarioId;
    
    @Test
    @Order(1)
    @DisplayName("Deve criar um usuário completo")
    void deveCriarUsuarioCompleto() {
        usuarioId = given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "nome": "Teste Integração Silva",
                    "email": "teste.integracao@exemplo.com",
                    "telefone": "(11) 99999-1234"
                }
                """)
        .when()
            .post("/api/usuarios")
        .then()
            .statusCode(201)
            .body("nome", equalTo("Teste Integração Silva"))
            .body("email", equalTo("teste.integracao@exemplo.com"))
            .body("telefone", equalTo("(11) 99999-1234"))
            .body("ativo", equalTo(true))
            .body("podeReceberNotificacoes", equalTo(true))
            .body("id", notNullValue())
            .extract()
            .path("id");
    }
    
    @Test
    @Order(2)
    @DisplayName("Deve buscar usuário por ID")
    void deveBuscarUsuarioPorId() {
        given()
            .pathParam("id", usuarioId)
        .when()
            .get("/api/usuarios/{id}")
        .then()
            .statusCode(200)
            .body("id", equalTo(usuarioId.intValue()))
            .body("nome", equalTo("Teste Integração Silva"))
            .body("email", equalTo("teste.integracao@exemplo.com"));
    }
    
    @Test
    @Order(3)
    @DisplayName("Deve atualizar dados do usuário")
    void deveAtualizarDadosUsuario() {
        given()
            .pathParam("id", usuarioId)
            .contentType(ContentType.JSON)
            .body("""
                {
                    "nome": "Teste Integração Silva Atualizado",
                    "email": "teste.atualizado@exemplo.com",
                    "telefone": "(11) 88888-5678"
                }
                """)
        .when()
            .put("/api/usuarios/{id}")
        .then()
            .statusCode(200)
            .body("nome", equalTo("Teste Integração Silva Atualizado"))
            .body("email", equalTo("teste.atualizado@exemplo.com"))
            .body("telefone", equalTo("(11) 88888-5678"))
            .body("foiAtualizadoRecentemente", equalTo(true));
    }
    
    @Test
    @Order(4)
    @DisplayName("Deve desativar usuário")
    void deveDesativarUsuario() {
        given()
            .pathParam("id", usuarioId)
        .when()
            .patch("/api/usuarios/{id}/desativar")
        .then()
            .statusCode(200)
            .body("ativo", equalTo(false))
            .body("podeReceberNotificacoes", equalTo(false));
    }
    
    @Test
    @Order(5)
    @DisplayName("Deve reativar usuário")
    void deveReativarUsuario() {
        given()
            .pathParam("id", usuarioId)
        .when()
            .patch("/api/usuarios/{id}/ativar")
        .then()
            .statusCode(200)
            .body("ativo", equalTo(true))
            .body("podeReceberNotificacoes", equalTo(true));
    }
    
    @Test
    @Order(6)
    @DisplayName("Deve listar usuários com filtros")
    void deveListarUsuariosComFiltros() {
        // Listar todos
        given()
        .when()
            .get("/api/usuarios")
        .then()
            .statusCode(200)
            .body("usuarios", hasSize(greaterThan(0)))
            .body("total", greaterThan(0));
        
        // Filtrar por nome
        given()
            .queryParam("nome", "Teste")
        .when()
            .get("/api/usuarios")
        .then()
            .statusCode(200)
            .body("usuarios", hasSize(greaterThan(0)));
        
        // Filtrar apenas ativos
        given()
            .queryParam("ativo", true)
        .when()
            .get("/api/usuarios")
        .then()
            .statusCode(200)
            .body("usuarios", hasSize(greaterThan(0)));
    }
    
    @Test
    @Order(7)
    @DisplayName("Deve buscar usuário por email")
    void deveBuscarUsuarioPorEmail() {
        given()
            .pathParam("email", "teste.atualizado@exemplo.com")
        .when()
            .get("/api/usuarios/email/{email}")
        .then()
            .statusCode(200)
            .body("email", equalTo("teste.atualizado@exemplo.com"));
    }
    
    @Test
    @DisplayName("Deve validar regras de negócio")
    void deveValidarRegrasDeNegocio() {
        // Email duplicado
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "nome": "Outro Usuario",
                    "email": "teste.atualizado@exemplo.com"
                }
                """)
        .when()
            .post("/api/usuarios")
        .then()
            .statusCode(400)
            .body("mensagem", containsString("Email já cadastrado"));
        
        // Dados inválidos
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "nome": "A",
                    "email": "email-invalido"
                }
                """)
        .when()
            .post("/api/usuarios")
        .then()
            .statusCode(400);
        
        // Usuario inexistente
        given()
            .pathParam("id", 99999)
        .when()
            .get("/api/usuarios/{id}")
        .then()
            .statusCode(404);
    }
}
```

### 9.9 Como Executar o Projeto

#### Comandos Básicos
```bash
# 1. Clonar/criar projeto
git clone <seu-repositorio>
cd quarkus-clean-arch

# 2. Executar em modo desenvolvimento
./mvnw quarkus:dev

# 3. Executar testes
./mvnw test

# 4. Executar apenas testes de integração
./mvnw test -Dtest="*IntegrationTest"

# 5. Gerar build de produção
./mvnw package

# 6. Executar build de produção
java -jar target/quarkus-app/quarkus-run.jar

# 7. Gerar imagem nativa (requer GraalVM)
./mvnw package -Dnative

# 8. Executar imagem nativa
./target/quarkus-clean-arch-1.0.0-SNAPSHOT-runner
```

#### Docker
```dockerfile
# Dockerfile
FROM registry.access.redhat.com/ubi8/openjdk-17:1.18

ENV LANGUAGE='en_US:en'

COPY --chown=185 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 target/quarkus-app/*.jar /deployments/
COPY --chown=185 target/quarkus-app/app/ /deployments/app/
COPY --chown=185 target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
```

```yaml
# docker-compose.yml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DB_URL=jdbc:postgresql://postgres:5432/usuarios_db
      - DB_USERNAME=postgres
      - DB_PASSWORD=password
    depends_on:
      - postgres

  postgres:
    image: postgres:15-alpine
    environment:
      - POSTGRES_DB=usuarios_db
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

#### Comandos Docker
```bash
# Construir e executar
docker-compose up --build

# Apenas executar
docker-compose up

# Executar em background
docker-compose up -d

# Ver logs
docker-compose logs -f app

# Parar serviços
docker-compose down
```

---

## 10. Deploy e Boas Práticas

### 10.1 Build para Produção

#### Configurações de Build
```xml
<!-- pom.xml - Propriedades adicionais -->
<properties>
    <maven.compiler.release>17</maven.compiler.release>
    <quarkus.platform.version>3.6.0</quarkus.platform.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <failsafe.useModulePath>false</failsafe.useModulePath>
    <maven.compiler.parameters>true</maven.compiler.parameters>
</properties>

<!-- Profile para produção -->
<profiles>
    <profile>
        <id>native</id>
        <properties>
            <skipITs>false</skipITs>
            <quarkus.package.type>native</quarkus.package.type>
        </properties>
    </profile>
    
    <profile>
        <id>production</id>
        <properties>
            <quarkus.profile>prod</quarkus.profile>
        </properties>
        <build>
            <plugins>
                <plugin>
                    <groupId>io.quarkus</groupId>
                    <artifactId>quarkus-maven-plugin</artifactId>
                    <executions>
                        <execution>
                            <goals>
                                <goal>build</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```

#### Scripts de Build
```bash
#!/bin/bash
# build.sh - Script de build para produção

set -e

echo "🏗️  Iniciando build de produção..."

# Limpar build anterior
echo "🧹 Limpando build anterior..."
./mvnw clean

# Executar testes
echo "🧪 Executando testes..."
./mvnw test

# Build de produção
echo "📦 Criando build de produção..."
./mvnw package -Pproduction -DskipTests

# Verificar se o build foi criado
if [ -f "target/quarkus-app/quarkus-run.jar" ]; then
    echo "✅ Build criado com sucesso!"
    echo "📁 Localização: target/quarkus-app/quarkus-run.jar"
    
    # Mostrar tamanho do arquivo
    SIZE=$(du -h target/quarkus-app/quarkus-run.jar | cut -f1)
    echo "📏 Tamanho: $SIZE"
else
    echo "❌ Erro na criação do build"
    exit 1
fi
```

### 10.2 Monitoramento e Observabilidade

#### Health Checks
```java
package com.exemplo.infrastructure.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javax.sql.DataSource;

@Liveness
@ApplicationScoped
public class DatabaseLivenessCheck implements HealthCheck {
    
    @Inject
    DataSource dataSource;
    
    @Override
    public HealthCheckResponse call() {
        try {
            // Verificar conexão com banco
            dataSource.getConnection().close();
            return HealthCheckResponse.up("Database connection");
        } catch (Exception e) {
            return HealthCheckResponse.down("Database connection failed");
        }
    }
}

@Readiness
@ApplicationScoped
public class DatabaseReadinessCheck implements HealthCheck {
    
    @Inject
    DataSource dataSource;
    
    @Override
    public HealthCheckResponse call() {
        try {
            // Verificar se pode executar queries
            var connection = dataSource.getConnection();
            var statement = connection.prepareStatement("SELECT 1");
            statement.execute();
            connection.close();
            
            return HealthCheckResponse.up("Database ready");
        } catch (Exception e) {
            return HealthCheckResponse.down("Database not ready");
        }
    }
}
```

#### Métricas Customizadas
```java
package com.exemplo.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UsuarioMetrics {
    
    private final Counter usuariosCriados;
    private final Counter usuariosAtivados;
    private final Counter usuariosDesativados;
    private final Timer tempoConsultaUsuario;
    
    @Inject
    public UsuarioMetrics(MeterRegistry registry) {
        this.usuariosCriados = Counter.builder("usuarios.criados")
            .description("Total de usuários criados")
            .register(registry);
            
        this.usuariosAtivados = Counter.builder("usuarios.ativados")
            .description("Total de usuários ativados")
            .register(registry);
            
        this.usuariosDesativados = Counter.builder("usuarios.desativados")
            .description("Total de usuários desativados")
            .register(registry);
            
        this.tempoConsultaUsuario = Timer.builder("usuarios.consulta.tempo")
            .description("Tempo de consulta de usuários")
            .register(registry);
    }
    
    public void incrementarUsuariosCriados() {
        usuariosCriados.increment();
    }
    
    public void incrementarUsuariosAtivados() {
        usuariosAtivados.increment();
    }
    
    public void incrementarUsuariosDesativados() {
        usuariosDesativados.increment();
    }
    
    public Timer.Sample iniciarTimerConsulta() {
        return Timer.start();
    }
    
    public void pararTimerConsulta(Timer.Sample sample) {
        sample.stop(tempoConsultaUsuario);
    }
}

// Uso nos Use Cases
@ApplicationScoped
public class CriarUsuarioUseCaseComMetricas {
    
    @Inject
    UsuarioRepository usuarioRepository;
    
    @Inject
    UsuarioMapper usuarioMapper;
    
    @Inject
    UsuarioMetrics metrics;
    
    public UsuarioResponse executar(CriarUsuarioRequest request) {
        // Lógica de criação...
        Usuario usuario = new Usuario(request.nome, request.email, request.telefone);
        usuarioRepository.salvar(usuario);
        
        // Registrar métrica
        metrics.incrementarUsuariosCriados();
        
        return usuarioMapper.mapearParaResponse(usuario);
    }
}
```

#### Configurações de Monitoramento
```properties
# application.properties - Monitoramento
quarkus.micrometer.enabled=true
quarkus.micrometer.export.prometheus.enabled=true
quarkus.micrometer.binder.http-server.enabled=true
quarkus.micrometer.binder.jvm.enabled=true

# Health checks
quarkus.smallrye-health.enabled=true

# OpenAPI/Swagger
quarkus.swagger-ui.always-include=true
quarkus.swagger-ui.path=/swagger
```

### 10.3 Segurança

#### JWT Authentication
```java
package com.exemplo.infrastructure.security;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import java.security.Principal;

@Path("/api/secure/usuarios")
@ApplicationScoped
public class SecureUsuarioController {
    
    @Inject
    CriarUsuarioUseCase criarUsuarioUseCase;
    
    @Context
    SecurityContext securityContext;
    
    @POST
    @RolesAllowed({"admin", "user"})
    public Response criar(CriarUsuarioRequest request) {
        // Verificar permissões específicas
        Principal principal = securityContext.getUserPrincipal();
        String username = principal.getName();
        
        // Lógica com autorização...
        return Response.ok().build();
    }
    
    @GET
    @PermitAll  // Endpoint público
    public Response listarPublicos() {
        // Apenas dados públicos
        return Response.ok().build();
    }
    
    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")  // Apenas administradores
    public Response deletar(@PathParam("id") Long id) {
        // Apenas admins podem deletar
        return Response.ok().build();
    }
}
```

#### Configurações de Segurança
```properties
# JWT
mp.jwt.verify.publickey.location=META-INF/resources/publicKey.pem
mp.jwt.verify.issuer=https://example.com
quarkus.smallrye-jwt.enabled=true

# CORS
quarkus.http.cors=true
quarkus.http.cors.origins=http://localhost:3000,https://meuapp.com
quarkus.http.cors.methods=GET,PUT,POST,DELETE,OPTIONS
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with

# HTTPS (produção)
quarkus.http.ssl.certificate.files=certificate.crt
quarkus.http.ssl.certificate.key-files=private.key
quarkus.http.ssl-port=8443
quarkus.http.redirect-to-https=true
```

### 10.4 Deploy Kubernetes

#### Manifesto Kubernetes
```yaml
# k8s/namespace.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: usuarios-app

---
# k8s/configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
  namespace: usuarios-app
data:
  DB_URL: "jdbc:postgresql://postgres-service:5432/usuarios_db"
  DB_USERNAME: "postgres"

---
# k8s/secret.yaml
apiVersion: v1
kind: Secret
metadata:
  name: app-secrets
  namespace: usuarios-app
type: Opaque
data:
  DB_PASSWORD: cGFzc3dvcmQ=  # base64 de "password"

---
# k8s/deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: usuarios-app
  namespace: usuarios-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: usuarios-app
  template:
    metadata:
      labels:
        app: usuarios-app
    spec:
      containers:
      - name: app
        image: usuarios-app:latest
        ports:
        - containerPort: 8080
        env:
        - name: DB_URL
          valueFrom:
            configMapKeyRef:
              name: app-config
              key: DB_URL
        - name: DB_USERNAME
          valueFrom:
            configMapKeyRef:
              name: app-config
              key: DB_USERNAME
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: app-secrets
              key: DB_PASSWORD
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
        resources:
          requests:
            memory: "64Mi"
            cpu: "100m"
          limits:
            memory: "256Mi"
            cpu: "500m"

---
# k8s/service.yaml
apiVersion: v1
kind: Service
metadata:
  name: usuarios-app-service
  namespace: usuarios-app
spec:
  selector:
    app: usuarios-app
  ports:
  - port: 80
    targetPort: 8080
  type: ClusterIP

---
# k8s/ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: usuarios-app-ingress
  namespace: usuarios-app
  annotations:
    kubernetes.io/ingress.class: nginx
    cert-manager.io/cluster-issuer: letsencrypt-prod
spec:
  tls:
  - hosts:
    - api.meuapp.com
    secretName: usuarios-app-tls
  rules:
  - host: api.meuapp.com
    http:
      paths:
      - path: /api
        pathType: Prefix
        backend:
          service:
            name: usuarios-app-service
            port:
              number: 80
```

#### Scripts de Deploy
```bash
#!/bin/bash
# deploy.sh - Script de deploy para Kubernetes

set -e

NAMESPACE="usuarios-app"
IMAGE_TAG=${1:-latest}

echo "🚀 Iniciando deploy para Kubernetes..."
echo "📦 Imagem: usuarios-app:$IMAGE_TAG"
echo "🏷️  Namespace: $NAMESPACE"

# Criar namespace se não existir
echo "🏗️  Verificando namespace..."
kubectl create namespace $NAMESPACE --dry-run=client -o yaml | kubectl apply -f -

# Aplicar ConfigMaps e Secrets
echo "⚙️  Aplicando configurações..."
kubectl apply -f k8s/configmap.yaml -n $NAMESPACE
kubectl apply -f k8s/secret.yaml -n $NAMESPACE

# Atualizar imagem no deployment
echo "🔄 Atualizando deployment..."
sed "s|usuarios-app:latest|usuarios-app:$IMAGE_TAG|g" k8s/deployment.yaml | kubectl apply -f - -n $NAMESPACE

# Aplicar Service e Ingress
echo "🌐 Configurando serviços..."
kubectl apply -f k8s/service.yaml -n $NAMESPACE
kubectl apply -f k8s/ingress.yaml -n $NAMESPACE

# Aguardar rollout
echo "⏳ Aguardando deploy..."
kubectl rollout status deployment/usuarios-app -n $NAMESPACE --timeout=300s

# Verificar pods
echo "✅ Verificando pods..."
kubectl get pods -n $NAMESPACE -l app=usuarios-app

echo "🎉 Deploy concluído com sucesso!"
echo "🔗 URL: https://api.meuapp.com/api/usuarios"
```

### 10.5 Boas Práticas Finais

#### Estrutura de Logs
```java
package com.exemplo.infrastructure.logging;

import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@ApplicationScoped
public class LoggingService {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);
    
    public void logCriacaoUsuario(String email, Long id) {
        MDC.put("operacao", "criar_usuario");
        MDC.put("email", email);
        MDC.put("usuario_id", String.valueOf(id));
        
        logger.info("Usuario criado com sucesso");
        
        MDC.clear();
    }
    
    public void logErroNegocio(String operacao, String erro, String detalhes) {
        MDC.put("operacao", operacao);
        MDC.put("tipo_erro", "negocio");
        
        logger.warn("Erro de negócio: {} - {}", erro, detalhes);
        
        MDC.clear();
    }
    
    public void logErroTecnico(String operacao, Exception e) {
        MDC.put("operacao", operacao);
        MDC.put("tipo_erro", "tecnico");
        
        logger.error("Erro técnico na operação: " + operacao, e);
        
        MDC.clear();
    }
}
```

#### Configurações de Performance
```properties
# application.properties - Performance

# Pool de conexões otimizado
quarkus.datasource.jdbc.min-size=10
quarkus.datasource.jdbc.max-size=50
quarkus.datasource.jdbc.acquisition-timeout=5
quarkus.datasource.jdbc.validation-query-sql=SELECT 1

# HTTP otimizado
quarkus.http.io-threads=8
quarkus.http.worker-threads=200

# JVM otimizada
quarkus.native.additional-build-args=-H:+ReportExceptionStackTraces,-H:+PrintGCDetails

# Cache
quarkus.cache.enabled=true
quarkus.cache.caffeine.maximum-size=1000

# Compressão
quarkus.resteasy-jackson.enable-deflate-encoding=true
quarkus.resteasy-jackson.enable-gzip-encoding=true
```

#### Checklist Final

**✅ Desenvolvimento:**
- [ ] Clean Architecture implementada corretamente
- [ ] Testes unitários com cobertura > 80%
- [ ] Testes de integração funcionando
- [ ] Documentação da API (OpenAPI/Swagger)
- [ ] Logs estruturados
- [ ] Tratamento de erros consistente

**✅ Deploy:**
- [ ] Build de produção funcionando
- [ ] Imagem Docker otimizada
- [ ] Health checks configurados
- [ ] Métricas expostas
- [ ] Configurações externalizadas
- [ ] Secrets protegidos

**✅ Operação:**
- [ ] Monitoramento configurado
- [ ] Alertas definidos
- [ ] Backup de banco configurado
- [ ] Pipeline CI/CD funcionando
- [ ] Rollback strategy definida
- [ ] Documentação de troubleshooting

---

## Conclusão

Este guia apresentou uma implementação completa de **Quarkus com Clean Architecture**, seguindo o método Feynman de ensino progressivo. Partimos dos conceitos básicos e evoluímos para um sistema completo e testado.

### Principais Pontos Aprendidos:

1. **Quarkus** oferece startup ultra-rápido e baixo consumo de memória
2. **Clean Architecture** organiza o código em camadas independentes
3. **Separation of Concerns** mantém regras de negócio isoladas da infraestrutura  
4. **Testes** em múltiplas camadas garantem qualidade e confiança
5. **Deploy** moderno com containers e Kubernetes é viável e robusto

### Próximos Passos:

- Implemente autenticação/autorização completa
- Adicione cache distribuído (Redis)
- Configure monitoramento avançado (Prometheus + Grafana)
- Integre message brokers (Apache Kafka, RabbitMQ)
- Implemente Event Sourcing/CQRS para casos mais complexos
- Configure service mesh (Istio) para microserviços

### Recursos Adicionais:

**📚 Documentação Oficial:**
- [Quarkus Guides](https://quarkus.io/guides/)
- [Clean Architecture - Uncle Bob](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Microprofile Specifications](https://microprofile.io/)

**🛠️ Ferramentas Recomendadas:**
- **IDE:** IntelliJ IDEA ou VS Code com extensões Java
- **Testing:** JUnit 5, RestAssured, Testcontainers
- **Monitoring:** Micrometer, Prometheus, Grafana
- **Documentation:** OpenAPI, AsyncAPI
- **CI/CD:** GitHub Actions, Jenkins, GitLab CI

**📖 Livros Recomendados:**
- "Clean Architecture" - Robert C. Martin
- "Implementing Domain-Driven Design" - Vaughn Vernon  
- "Microservices Patterns" - Chris Richardson
- "Building Microservices" - Sam Newman

---

### Exemplo de Uso da API

Após seguir este guia, sua API estará funcionando assim:

```bash
# Criar usuário
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@exemplo.com",
    "telefone": "(11) 99999-9999"
  }'

# Resposta:
# {
#   "id": 1,
#   "nome": "João Silva", 
#   "email": "joao@exemplo.com",
#   "telefone": "(11) 99999-9999",
#   "dataCriacao": "2024-01-15T10:30:00",
#   "dataAtualizacao": "2024-01-15T10:30:00",
#   "ativo": true,
#   "podeReceberNotificacoes": true,
#   "foiAtualizadoRecentemente": false
# }

# Buscar usuário
curl http://localhost:8080/api/usuarios/1

# Listar usuários com filtro
curl "http://localhost:8080/api/usuarios?nome=João&ativo=true&pagina=0&tamanho=10"

# Atualizar usuário
curl -X PUT http://localhost:8080/api/usuarios/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Santos Silva",
    "email": "joao.santos@exemplo.com"
  }'

# Desativar usuário
curl -X PATCH http://localhost:8080/api/usuarios/1/desativar

# Health check
curl http://localhost:8080/q/health

# Métricas
curl http://localhost:8080/q/metrics

# Documentação da API
# http://localhost:8080/swagger
```

### Diagrama Final da Arquitetura

```
┌─────────────────────────────────────────────────────────────────────────┐
│                          PRESENTATION LAYER                             │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────────┐ │
│  │   REST API      │  │   GraphQL       │  │   Message Consumers     │ │
│  │  (Controllers)  │  │  (Resolvers)    │  │     (Event Handlers)    │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────────────┘ │
└─────────────────────────────┬───────────────────────────────────────────┘
                              │
┌─────────────────────────────▼───────────────────────────────────────────┐
│                         APPLICATION LAYER                               │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────────┐ │
│  │   Use Cases     │  │      DTOs       │  │       Mappers           │ │
│  │ (Business Logic)│  │ (Input/Output)  │  │   (Transformations)     │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────────────┘ │
└─────────────────────────────┬───────────────────────────────────────────┘
                              │
┌─────────────────────────────▼───────────────────────────────────────────┐
│                          DOMAIN LAYER                                   │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────────┐ │
│  │    Entities     │  │  Domain Services│  │     Repositories        │ │
│  │ (Business Rules)│  │  (Domain Logic) │  │     (Interfaces)        │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────────────┘ │
└─────────────────────────────┬───────────────────────────────────────────┘
                              │
┌─────────────────────────────▼───────────────────────────────────────────┐
│                       INFRASTRUCTURE LAYER                              │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────────┐ │
│  │   Database      │  │   External APIs │  │   Message Queues        │ │
│  │ (JPA/Panache)   │  │   (REST/SOAP)   │  │  (Kafka/RabbitMQ)       │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

### Performance Esperada

Com Quarkus e Clean Architecture bem implementados, você pode esperar:

| Métrica | Spring Boot | Quarkus JVM | Quarkus Native |
|---------|-------------|-------------|----------------|
| **Startup Time** | 2-5 segundos | 1-2 segundos | 0.05 segundos |
| **Memory (RSS)** | 150-300 MB | 100-200 MB | 20-50 MB |
| **First Request** | 500-1000ms | 100-300ms | 10-50ms |
| **Throughput** | 1000 req/s | 1200 req/s | 1500 req/s |
| **Docker Image** | 200-400 MB | 150-250 MB | 50-100 MB |

### Checklist de Validação Final

**✅ Arquitetura:**
- [ ] Dependências fluem da externa para interna
- [ ] Regras de negócio estão isoladas no domínio
- [ ] Use Cases orquestram sem conhecer infraestrutura
- [ ] Adapters implementam interfaces do domínio

**✅ Qualidade:**
- [ ] Testes unitários > 80% cobertura
- [ ] Testes de integração cobrem fluxos principais
- [ ] Validações de entrada funcionando
- [ ] Tratamento de erros consistente

**✅ Performance:**
- [ ] Startup < 2 segundos
- [ ] Primeiro request < 300ms
- [ ] Memory < 200MB
- [ ] Throughput > 1000 req/s

**✅ Operação:**
- [ ] Health checks respondendo
- [ ] Métricas expostas
- [ ] Logs estruturados
- [ ] Deploy automatizado

---

## Palavras Finais

Parabéns! 🎉 Você agora domina uma stack moderna e poderosa para desenvolvimento de aplicações Java. A combinação de **Quarkus + Clean Architecture** oferece:

- **Produtividade**: Hot reload, extensões prontas, menos boilerplate
- **Performance**: Startup rápido, baixo consumo de memória
- **Maintainability**: Código organizado, testável e evolutivo
- **Scalability**: Nativo para cloud, containers e microserviços

Este conhecimento te permitirá construir aplicações robustas, eficientes e que realmente fazem a diferença no mundo real. Continue praticando, experimentando e evoluindo!

**Happy Coding! 🚀**