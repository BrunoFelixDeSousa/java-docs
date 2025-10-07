# Hibernate ORM com Panache — Guia Completo de Persistência Simplificada

## Índice

1. [O que é Panache?](#1-o-que-é-panache)
2. [Conceitos Fundamentais](#2-conceitos-fundamentais)
3. [Setup do Projeto](#3-setup-do-projeto)
4. [Active Record Pattern](#4-active-record-pattern)
5. [Repository Pattern](#5-repository-pattern)
6. [Queries Avançadas](#6-queries-avançadas)
7. [Relacionamentos JPA](#7-relacionamentos-jpa)
8. [Transações e Performance](#8-transações-e-performance)
9. [Testes e Qualidade](#9-testes-e-qualidade)
10. [Padrões e Boas Práticas](#10-padrões-e-boas-práticas)
11. [Troubleshooting](#11-troubleshooting)
12. [Recursos e Referências](#12-recursos-e-referências)

---

## 1. O que é Panache?

### 1.1. Definição

**Panache** é uma biblioteca do Quarkus que simplifica radicalmente o uso do Hibernate ORM, eliminando boilerplate e oferecendo APIs elegantes para persistência de dados. 

Criado pela equipe do Quarkus (Red Hat), Panache oferece:
- 🚀 **70% menos código** comparado ao JPA tradicional
- 🎯 **Dois padrões arquiteturais**: Active Record e Repository
- ⚡ **Integração nativa** com CDI, transações e validações
- 🔧 **Compatibilidade total** com Hibernate/JPA
- 📊 **Type-safe queries** sem magic strings

### 1.2. Por que usar Panache?

#### Comparação de Código

**❌ JPA Tradicional (Verbose)**
```java
@Stateless
public class PersonService {
    @PersistenceContext
    EntityManager em;
    
    public List<Person> findByName(String name) {
        TypedQuery<Person> query = em.createQuery(
            "SELECT p FROM Person p WHERE p.name = :name", 
            Person.class
        );
        query.setParameter("name", name);
        return query.getResultList();
    }
    
    public void save(Person person) {
        em.getTransaction().begin();
        try {
            em.persist(person);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }
}
```

**✅ Panache (Conciso)**
```java
@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {
    
    public List<Person> findByName(String name) {
        return find("name", name).list();
    }
    
    // save() já está disponível via PanacheRepository
    // Transação gerenciada automaticamente via @Transactional
}
```

**📊 Redução:**
- **Linhas de código**: 25 → 8 (68% menor)
- **Conceitos**: EntityManager, TypedQuery, Transaction → find(), list()
- **Complexidade**: Alta → Baixa

### 1.3. Arquitetura do Panache

```
┌─────────────────────────────────────────────────────────────┐
│                    QUARKUS APPLICATION                      │
│                                                             │
│  ┌──────────────────┐              ┌────────────────────┐   │
│  │  REST Resource   │──────────────│  Service Layer     │   │
│  │  @Path("/api")   │              │  Business Logic    │   │
│  └──────────────────┘              └─────────┬──────────┘   │
│                                               │             │
│  ┌────────────────────────────────────────────┼───────────┐ │
│  │              PANACHE LAYER                │            │ │
│  │                                           ▼            │ │
│  │  ┌──────────────────┐      ┌──────────────────────┐    │ │
│  │  │  PanacheEntity   │      │ PanacheRepository    │    │ │
│  │  │  (Active Record) │      │   (Repository)       │    │ │
│  │  └────────┬─────────┘      └──────────┬───────────┘    │ │
│  └───────────┼────────────────────────────┼───────────────┘ │
│              │                            │                 │
│  ┌───────────┴────────────────────────────┴───────────────┐ │
│  │              HIBERNATE ORM CORE                        │ │
│  │  - Session Management                                  │ │
│  │  - Lazy Loading                                        │ │
│  │  - Dirty Checking                                      │ │
│  │  - Query Generation                                    │ │
│  └──────────────────────────┬─────────────────────────────┘ │
│                             │                               │
└─────────────────────────────┼───────────────────────────────┘
                              │
                  ┌───────────┴──────────────┐
                  │                          │
                  ▼                          ▼
        ┌──────────────────┐      ┌──────────────────┐
        │    PostgreSQL    │      │      MySQL       │
        │  (ou qualquer    │      │   H2, Oracle,    │
        │   JDBC driver)   │      │   SQL Server...  │
        └──────────────────┘      └──────────────────┘
```

### 1.4. Dois Padrões, Uma Escolha

Panache oferece dois estilos de API para você escolher:

#### Padrão 1: Active Record (PanacheEntity)

**Conceito:** A entidade **contém** os métodos de persistência.

```java
@Entity
public class Person extends PanacheEntity {
    public String name;
    public int age;
    
    // Métodos herdados: persist(), delete(), findById()...
    
    public static List<Person> findByAge(int age) {
        return find("age", age).list();
    }
}

// Uso:
Person person = new Person();
person.name = "João";
person.persist();  // ← Método da própria entidade

List<Person> adults = Person.findByAge(18);  // ← Método estático
```

**Prós:**
- ✅ Menos arquivos (entidade = repository)
- ✅ Código ultra-conciso
- ✅ Ideal para CRUD simples
- ✅ RAD (Rapid Application Development)

**Contras:**
- ❌ Entidade mistura persistência e domínio
- ❌ Dificulta testes (mock de métodos estáticos)
- ❌ Viola Single Responsibility Principle

#### Padrão 2: Repository (PanacheRepository)

**Conceito:** Separação entre entidade POJO e lógica de persistência.

```java
@Entity
public class Person {
    @Id
    @GeneratedValue
    public Long id;
    public String name;
    public int age;
    
    // Apenas campos e lógica de domínio
}

@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {
    
    public List<Person> findByAge(int age) {
        return find("age", age).list();
    }
    
    // Métodos herdados: persist(), delete(), findById()...
}

// Uso:
@Inject
PersonRepository repository;

Person person = new Person();
person.name = "João";
repository.persist(person);  // ← Método do repository

List<Person> adults = repository.findByAge(18);
```

**Prós:**
- ✅ Separação de responsabilidades (SRP)
- ✅ Facilita testes (mock do repository)
- ✅ Alinhado com DDD e Clean Architecture
- ✅ Entidades POJO puras

**Contras:**
- ❌ Mais arquivos para gerenciar
- ❌ Pouco mais verboso

### 1.5. Comparação Visual

```mermaid
graph TB
    subgraph "Active Record Pattern"
        AR1[REST Controller] -->|injeta| AR2[Entity + Repository]
        AR2 -->|extends| AR3[PanacheEntity]
        AR3 -->|usa| AR4[Hibernate ORM]
        AR4 --> AR5[(Database)]
        
        style AR2 fill:#90EE90
        style AR3 fill:#FFD700
    end
    
    subgraph "Repository Pattern"
        RP1[REST Controller] -->|injeta| RP2[Repository]
        RP2 -->|implements| RP3[PanacheRepository]
        RP2 -->|gerencia| RP4[Entity POJO]
        RP3 -->|usa| RP5[Hibernate ORM]
        RP5 --> RP6[(Database)]
        
        style RP2 fill:#87CEEB
        style RP4 fill:#FFB6C1
    end
```

### 1.6. Quando usar cada padrão?

| Critério | Active Record | Repository |
|----------|---------------|------------|
| **Complexidade** | Baixa/Média | Média/Alta |
| **Tamanho do Projeto** | Pequeno/Médio | Médio/Grande |
| **Arquitetura** | Pragmática | Clean/Hexagonal |
| **DDD** | ❌ Dificulta | ✅ Facilita |
| **Testabilidade** | ⚠️ Moderada | ✅ Excelente |
| **Protótipos/MVPs** | ✅ Ideal | ⚠️ Overkill |
| **Sistemas Empresariais** | ⚠️ Limitado | ✅ Recomendado |

**Recomendação Geral:**
- 🚀 **Protótipos, APIs CRUD, MVPs**: `PanacheEntity`
- 🏢 **Sistemas empresariais, DDD, equipes grandes**: `PanacheRepository`
- 🎯 **Híbrido**: Comece com Active Record, migre para Repository conforme necessário

---

## 2. Conceitos Fundamentais
### 2.1. Entendendo o Hibernate ORM

Antes de mergulhar no Panache, é essencial entender o que o Hibernate faz.

#### O Problema: Impedância Objeto-Relacional

```
┌─────────────────────────────────────────────────────────────┐
│              MUNDO ORIENTADO A OBJETOS (Java)               │
│                                                             │
│  Person person = new Person();                              │
│  person.setName("João");                                    │
│  person.setAge(30);                                         │
│  person.getAddress().setCity("São Paulo");                  │
│                                                             │
│  ↓ Como salvar isso em tabelas SQL? ↓                       │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                MUNDO RELACIONAL (SQL)                       │
│                                                             │
│  INSERT INTO persons (name, age) VALUES ('João', 30);       │
│  INSERT INTO addresses (person_id, city)                    │
│    VALUES (1, 'São Paulo');                                 │
└─────────────────────────────────────────────────────────────┘
```

**Hibernate ORM resolve isso através de:**
- **Mapeamento**: Anotações `@Entity`, `@Table`, `@Column`
- **Persistência**: Salvar objetos automaticamente
- **Queries**: JPQL (SQL orientado a objetos)
- **Lazy Loading**: Carregar dados sob demanda
- **Caching**: Otimizar performance

#### Ciclo de Vida de uma Entidade

```java
// 1. TRANSIENT (não gerenciado)
Person person = new Person();
person.name = "João";

// 2. MANAGED (gerenciado pelo EntityManager)
em.persist(person);  // ou person.persist() com Panache

// 3. DETACHED (desconectado)
em.detach(person);

// 4. REMOVED (marcado para deleção)
em.remove(person);
```

### 2.2. Operações CRUD com Panache

#### CREATE (Criar)

```java
// Active Record
Person person = new Person();
person.name = "João";
person.age = 30;
person.persist();  // INSERT INTO persons...

// Repository
@Inject PersonRepository repository;
Person person = new Person();
person.name = "João";
repository.persist(person);
```

#### READ (Ler)

```java
// Buscar por ID
Person person = Person.findById(1L);

// Buscar todos
List<Person> all = Person.listAll();

// Buscar com filtro
List<Person> adults = Person.find("age >= 18").list();

// Buscar um único resultado
Person john = Person.find("name", "João").firstResult();
```

#### UPDATE (Atualizar)

```java
// Hibernate faz UPDATE automático (Dirty Checking)
@Transactional
public void updateAge(Long id, int newAge) {
    Person person = Person.findById(id);
    person.age = newAge;  // ← Modificação detectada automaticamente
    // Sem necessidade de persist() ou update()!
}
```

#### DELETE (Deletar)

```java
// Deletar por instância
Person person = Person.findById(1L);
person.delete();

// Deletar por ID
Person.deleteById(1L);

// Deletar em lote
Person.delete("age < 18");  // DELETE FROM persons WHERE age < 18
```

### 2.3. Métodos Herdados do Panache

Ao usar `PanacheEntity` ou `PanacheRepository`, você automaticamente herda:

#### Métodos de Persistência

```java
// Salvar
persist()           // Salva a entidade
persist(entity)     // Salva entidade passada
persistAndFlush()   // Salva e força flush imediato

// Deletar
delete()            // Deleta a entidade
delete(String query, Object... params)  // Deleta por query
deleteById(Object id)                    // Deleta por ID
deleteAll()         // Deleta TODAS as entidades

// Verificar existência
isPersistent()      // Verifica se entidade está gerenciada
```

#### Métodos de Consulta

```java
// Buscar
findById(Object id)              // Busca por ID
find(String query, Object... params)  // Query parametrizada
findAll()                        // Busca todas
listAll()                        // Lista todas
streamAll()                      // Stream de todas

// Contar
count()                          // Conta todas
count(String query, Object... params)  // Conta com filtro

// Verificar
exists(Object id)                // Verifica se ID existe
```

#### Métodos com Paginação e Ordenação

```java
// Paginação
Person.findAll()
    .page(Page.of(0, 20))  // Página 0, 20 itens
    .list();

// Ordenação
Person.findAll()
    .page(Page.of(0, 20))
    .sorted(Sort.by("name").ascending())
    .list();

// Combinado
Person.find("age >= ?1", 18)
    .page(Page.of(0, 10))
    .list();
```

### 2.4. Sintaxe de Queries do Panache

Panache aceita várias formas de queries:

#### 1. Query Simples (campo = valor)

```java
// Forma abreviada
Person.find("name", "João").list();

// Equivalente JPQL
Person.find("name = ?1", "João").list();

// Múltiplos campos
Person.find("name = ?1 and age = ?2", "João", 30).list();
```

#### 2. Queries Nomeadas (Named Parameters)

```java
Person.find("name = :name and age >= :minAge", 
    Parameters.with("name", "João").and("minAge", 18))
    .list();
```

#### 3. Queries com Operadores

```java
// Comparação
find("age > 18")
find("age >= 18")
find("age < 65")
find("age != 30")

// LIKE
find("name LIKE ?1", "Jo%")

// IN
find("status IN (?1, ?2)", "ACTIVE", "PENDING")

// BETWEEN
find("age BETWEEN 18 AND 65")

// IS NULL
find("deletedAt IS NULL")

// ORDER BY
find("status = ?1 ORDER BY name ASC", "ACTIVE")
```

#### 4. JPQL Completo

```java
Person.find("""
    SELECT p FROM Person p 
    JOIN p.address a 
    WHERE a.city = ?1 
    ORDER BY p.name
    """, "São Paulo").list();
```

### 2.5. Transações no Quarkus

#### Transações Automáticas

```java
@Path("/persons")
public class PersonResource {
    
    @POST
    @Transactional  // ← Inicia transação, commita ao fim, rollback em exceção
    public Response create(Person person) {
        person.persist();
        return Response.ok().build();
    }
}
```

**Comportamento:**
1. `@Transactional` inicia transação
2. Código executa
3. Se sucesso → `commit()`
4. Se exceção → `rollback()`
5. Transação fecha automaticamente

#### Transações Manuais (Evitar)

```java
// ❌ NÃO RECOMENDADO - Use @Transactional
@Inject
TransactionManager tm;

public void save(Person person) {
    try {
        tm.begin();
        person.persist();
        tm.commit();
    } catch (Exception e) {
        tm.rollback();
        throw e;
    }
}
```

#### Configurando Transações

```java
@Transactional(
    value = TxType.REQUIRED,     // Padrão: cria ou reutiliza
    rollbackOn = Exception.class, // Rollback em qualquer exceção
    dontRollbackOn = ValidationException.class  // Exceto validação
)
public void complexOperation() {
    // ...
}
```

**Tipos de Transação:**
```java
TxType.REQUIRED      // Padrão - cria nova ou usa existente
TxType.REQUIRES_NEW  // Sempre cria nova (suspende existente)
TxType.MANDATORY     // Exige transação existente (erro se não houver)
TxType.SUPPORTS      // Usa se existir, continua sem se não houver
TxType.NOT_SUPPORTED // Suspende transação existente
TxType.NEVER         // Erro se houver transação
```

### 2.6. Validações com Bean Validation

```java
import jakarta.validation.constraints.*;

@Entity
public class Person extends PanacheEntity {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    public String name;
    
    @Min(value = 0, message = "Idade não pode ser negativa")
    @Max(value = 150, message = "Idade inválida")
    public int age;
    
    @Email(message = "Email inválido")
    @NotNull
    public String email;
    
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", 
             message = "CPF inválido")
    public String cpf;
    
    @Future(message = "Data deve ser futura")
    public LocalDate expirationDate;
}

// Uso no REST
@POST
@Transactional
public Response create(@Valid Person person) {  // ← @Valid dispara validação
    person.persist();
    return Response.ok().build();
}
```

**Validações Disponíveis:**
- `@NotNull`: Não pode ser null
- `@NotBlank`: String não pode ser vazia/apenas espaços
- `@NotEmpty`: Coleção/Array não pode ser vazia
- `@Size(min, max)`: Tamanho de string/coleção
- `@Min(value)`: Valor mínimo numérico
- `@Max(value)`: Valor máximo numérico
- `@Email`: Formato de email
- `@Pattern(regexp)`: Regex customizado
- `@Past/@Future`: Datas passadas/futuras
- `@Positive/@Negative`: Números positivos/negativos

---

## 3. Setup do Projeto

### 3.1. Criando o Projeto

#### Opção 1: Quarkus CLI

```powershell
# Instalar Quarkus CLI (se necessário)
iex "& { $(irm https://ps.jbang.dev) } app install --fresh --force quarkus@quarkusio"

# Criar projeto
quarkus create app com.example:panache-demo `
    --extension='hibernate-orm-panache,jdbc-postgresql,rest-jackson' `
    --java=21
```

#### Opção 2: Maven

```powershell
mvn io.quarkus.platform:quarkus-maven-plugin:3.15.1:create `
    -DprojectGroupId=com.example `
    -DprojectArtifactId=panache-demo `
    -Dextensions="hibernate-orm-panache,jdbc-postgresql,rest-jackson" `
    -DjavaVersion=21
```

#### Opção 3: Code.quarkus.io

1. Acesse https://code.quarkus.io
2. Configure:
   - **Group:** com.example
   - **Artifact:** panache-demo
   - **Java Version:** 21
3. Adicione extensões:
   - Hibernate ORM with Panache
   - JDBC Driver - PostgreSQL
   - REST
   - REST Jackson
4. Generate e baixe

### 3.2. Estrutura do Projeto

```
panache-demo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           ├── entity/
│   │   │           │   ├── Person.java
│   │   │           │   ├── Address.java
│   │   │           │   └── Order.java
│   │   │           ├── repository/
│   │   │           │   ├── PersonRepository.java
│   │   │           │   └── OrderRepository.java
│   │   │           ├── service/
│   │   │           │   └── PersonService.java
│   │   │           └── resource/
│   │   │               └── PersonResource.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── import.sql
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── PersonResourceTest.java
├── pom.xml
└── docker-compose.yml
```

### 3.3. Dependências (pom.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.example</groupId>
    <artifactId>panache-demo</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    
    <properties>
        <quarkus.platform.version>3.15.1</quarkus.platform.version>
        <maven.compiler.release>21</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
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
        <!-- Panache ORM -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-hibernate-orm-panache</artifactId>
        </dependency>
        
        <!-- PostgreSQL Driver -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-jdbc-postgresql</artifactId>
        </dependency>
        
        <!-- REST -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-rest</artifactId>
        </dependency>
        
        <!-- JSON Serialization -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-rest-jackson</artifactId>
        </dependency>
        
        <!-- Bean Validation -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-hibernate-validator</artifactId>
        </dependency>
        
        <!-- SmallRye Health -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-smallrye-health</artifactId>
        </dependency>
        
        <!-- OpenAPI/Swagger -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-smallrye-openapi</artifactId>
        </dependency>
        
        <!-- Testing -->
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
        
        <!-- H2 para testes -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-jdbc-h2</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>io.quarkus.platform</groupId>
                <artifactId>quarkus-maven-plugin</artifactId>
                <version>${quarkus.platform.version}</version>
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
        </plugins>
    </build>
</project>
```

### 3.4. Configuração (application.properties)

```properties
# ============================================================================
# APPLICATION
# ============================================================================
quarkus.application.name=panache-demo
quarkus.application.version=1.0.0

# ============================================================================
# DATASOURCE - PostgreSQL
# ============================================================================
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=postgres
quarkus.datasource.password=postgres
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/panache_db

# Pool de Conexões
quarkus.datasource.jdbc.min-size=5
quarkus.datasource.jdbc.max-size=20
quarkus.datasource.jdbc.acquisition-timeout=10

# ============================================================================
# HIBERNATE ORM
# ============================================================================
# Estratégia de criação de schema
quarkus.hibernate-orm.database.generation=drop-and-create
# Opções: none, create, drop-and-create, drop, update, validate

# SQL Logging
quarkus.hibernate-orm.log.sql=true
quarkus.hibernate-orm.log.format-sql=true
quarkus.hibernate-orm.log.bind-parameters=true

# Script de inicialização
quarkus.hibernate-orm.sql-load-script=import.sql

# Dialeto (auto-detectado, mas pode ser explícito)
quarkus.hibernate-orm.dialect=org.hibernate.dialect.PostgreSQLDialect

# Performance
quarkus.hibernate-orm.jdbc.statement-batch-size=50
quarkus.hibernate-orm.jdbc.statement-fetch-size=100

# Segunda via de cache (opcional)
quarkus.hibernate-orm.cache.enabled=false

# ============================================================================
# LOGGING
# ============================================================================
quarkus.log.level=INFO
quarkus.log.category."com.example".level=DEBUG
quarkus.log.category."org.hibernate".level=INFO
quarkus.log.category."org.hibernate.SQL".level=DEBUG
quarkus.log.category."org.hibernate.type.descriptor.sql.BasicBinder".level=TRACE

# Formato de log
quarkus.log.console.format=%d{HH:mm:ss} %-5p [%c{2.}] (%t) %s%e%n

# ============================================================================
# DEV MODE
# ============================================================================
%dev.quarkus.hibernate-orm.database.generation=drop-and-create
%dev.quarkus.hibernate-orm.log.sql=true
%dev.quarkus.datasource.devservices.enabled=true
%dev.quarkus.datasource.devservices.image-name=postgres:16-alpine

# ============================================================================
# TEST MODE
# ============================================================================
%test.quarkus.datasource.db-kind=h2
%test.quarkus.datasource.jdbc.url=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1
%test.quarkus.hibernate-orm.database.generation=drop-and-create
%test.quarkus.hibernate-orm.log.sql=false

# ============================================================================
# PRODUCTION MODE
# ============================================================================
%prod.quarkus.hibernate-orm.database.generation=validate
%prod.quarkus.hibernate-orm.log.sql=false
%prod.quarkus.datasource.jdbc.url=${DATABASE_URL}
%prod.quarkus.datasource.username=${DATABASE_USER}
%prod.quarkus.datasource.password=${DATABASE_PASSWORD}

# ============================================================================
# OPENAPI / SWAGGER
# ============================================================================
quarkus.swagger-ui.always-include=true
quarkus.swagger-ui.path=/swagger-ui

# ============================================================================
# HEALTH CHECKS
# ============================================================================
quarkus.smallrye-health.root-path=/health
```

### 3.5. Docker Compose (docker-compose.yml)

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    container_name: panache-postgres
    ports:
      - "5432:5432"
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: panache_db
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5
    networks:
      - panache-network

  pgadmin:
    image: dpage/pgadmin4:latest
    container_name: panache-pgadmin
    ports:
      - "5050:80"
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@example.com
      PGADMIN_DEFAULT_PASSWORD: admin
    depends_on:
      postgres:
        condition: service_healthy
    networks:
      - panache-network

volumes:
  postgres_data:
    driver: local

networks:
  panache-network:
    driver: bridge
```

**Comandos:**
```powershell
# Iniciar PostgreSQL
docker-compose up -d postgres

# Ver logs
docker-compose logs -f postgres

# Parar tudo
docker-compose down

# Parar e remover volumes
docker-compose down -v
```

### 3.6. Script de Inicialização (import.sql)

```sql
-- src/main/resources/import.sql
-- Executado automaticamente quando database.generation != none

-- Inserir pessoas
INSERT INTO person (id, name, email, age, status, created_at) 
VALUES 
  (nextval('person_seq'), 'João Silva', 'joao@example.com', 30, 'ACTIVE', CURRENT_TIMESTAMP),
  (nextval('person_seq'), 'Maria Santos', 'maria@example.com', 25, 'ACTIVE', CURRENT_TIMESTAMP),
  (nextval('person_seq'), 'Pedro Oliveira', 'pedro@example.com', 35, 'INACTIVE', CURRENT_TIMESTAMP);

-- Inserir endereços
INSERT INTO address (id, street, city, state, zip_code, person_id)
VALUES
  (nextval('address_seq'), 'Rua A, 123', 'São Paulo', 'SP', '01234-567', 1),
  (nextval('address_seq'), 'Av. B, 456', 'Rio de Janeiro', 'RJ', '20000-000', 2);
```

### 3.7. Executando o Projeto

```powershell
# Modo desenvolvimento (hot reload)
./mvnw quarkus:dev

# Acessar aplicação
# - REST API: http://localhost:8080
# - Swagger UI: http://localhost:8080/swagger-ui
# - Health Check: http://localhost:8080/health
# - Dev UI: http://localhost:8080/q/dev

# Build para produção
./mvnw package

# Executar JAR
java -jar target/quarkus-app/quarkus-run.jar

# Build nativo (requer GraalVM)
./mvnw package -Dnative

# Executar nativo
./target/panache-demo-1.0.0-SNAPSHOT-runner
```

---

## 4. Active Record Pattern
```java
package com.example.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Entidade Customer usando Active Record Pattern.
 * 
 * TRADE-OFF: A entidade herda métodos de persistência (find, persist, delete).
 * Viola parcialmente Object Calisthenics (regra: não usar métodos estáticos),
 * mas justifica-se pela simplicidade em 80% dos casos de CRUD.
 */
@Entity
@Table(
    name = "customers",
    indexes = @Index(name = "idx_customer_email", columnList = "email")
)
public class Customer extends PanacheEntity {
    private static final Logger LOG = LoggerFactory.getLogger(Customer.class);
    
    @NotBlank
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100)
    public String name;
    
    @Email
    @NotBlank
    @Column(nullable = false, unique = true, length = 150)
    public String email;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    public CustomerStatus status = CustomerStatus.ACTIVE;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    public LocalDateTime updatedAt;
    
    // ====================
    // Custom Queries
    // ====================
    
    public static List<Customer> findByStatus(CustomerStatus status) {
        LOG.debug("Buscando customers com status: {}", status);
        return find("status", status).list();
    }
    
    public static Optional<Customer> findByEmail(String email) {
        LOG.debug("Buscando customer por email: {}", email);
        return find("email", email).firstResultOptional();
    }
    
    public static List<Customer> findActiveCustomersCreatedAfter(LocalDateTime date) {
        LOG.debug("Buscando customers ativos criados após: {}", date);
        return find(
            "status = ?1 and createdAt > ?2", 
            CustomerStatus.ACTIVE, 
            date
        ).list();
    }
    
    // ====================
    // Business Methods
    // ====================
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        LOG.info("Customer {} atualizado", this.id);
    }
    
    public void activate() {
        if (this.status == CustomerStatus.ACTIVE) {
            LOG.warn("Customer {} já está ativo", this.id);
            return;
        }
        this.status = CustomerStatus.ACTIVE;
        LOG.info("Customer {} ativado", this.id);
    }
    
    public void deactivate() {
        if (this.status == CustomerStatus.INACTIVE) {
            LOG.warn("Customer {} já está inativo", this.id);
            return;
        }
        this.status = CustomerStatus.INACTIVE;
        LOG.info("Customer {} desativado", this.id);
    }
    
    @Override
    public String toString() {
        return "Customer{id=%d, name='%s', email='%s', status=%s}"
            .formatted(id, name, email, status);
    }
}

enum CustomerStatus {
    ACTIVE,
    INACTIVE,
    BLOCKED
}
```

---

### 3.3 Pattern Repository com PanacheRepository

**Repository Pattern** separa a lógica de persistência da entidade, seguindo o princípio de responsabilidade única (SRP). A entidade vira um **POJO puro** e o Repository gerencia todas as operações de banco de dados.

#### 3.3.1 Entidade POJO (Sem PanacheEntity)

```java
package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade Product como POJO puro (sem herança de Panache).
 * 
 * VANTAGENS:
 * - Separação clara de responsabilidades (SRP)
 * - Facilita testes unitários (mock do repository)
 * - Compatível com DDD (Aggregate Root)
 * - Encapsulamento com getters/setters privados
 */
@Entity
@Table(
    name = "products",
    indexes = {
        @Index(name = "idx_product_sku", columnList = "sku"),
        @Index(name = "idx_product_category", columnList = "category")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_product_sku", columnNames = "sku")
    }
)
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(min = 3, max = 200)
    @Column(nullable = false, length = 200)
    private String name;
    
    @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String sku;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ProductCategory category;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.AVAILABLE;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // ====================
    // Lifecycle Callbacks
    // ====================
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // ====================
    // Business Methods
    // ====================
    
    /**
     * Verifica se o produto está disponível para venda.
     */
    public boolean isAvailable() {
        return this.status == ProductStatus.AVAILABLE && this.stockQuantity > 0;
    }
    
    /**
     * Reduz o estoque (operação de venda).
     * 
     * @param quantity Quantidade a ser reduzida
     * @throws IllegalStateException se não houver estoque suficiente
     */
    public void reduceStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        if (this.stockQuantity < quantity) {
            throw new IllegalStateException(
                "Estoque insuficiente. Disponível: " + this.stockQuantity
            );
        }
        this.stockQuantity -= quantity;
        
        if (this.stockQuantity == 0) {
            this.status = ProductStatus.OUT_OF_STOCK;
        }
    }
    
    /**
     * Adiciona estoque (reposição).
     */
    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        this.stockQuantity += quantity;
        
        if (this.status == ProductStatus.OUT_OF_STOCK) {
            this.status = ProductStatus.AVAILABLE;
        }
    }
    
    /**
     * Ativa o produto.
     */
    public void activate() {
        if (this.stockQuantity > 0) {
            this.status = ProductStatus.AVAILABLE;
        } else {
            throw new IllegalStateException("Produto sem estoque não pode ser ativado");
        }
    }
    
    /**
     * Desativa o produto.
     */
    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
    }
    
    // ====================
    // Getters & Setters
    // ====================
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSku() {
        return sku;
    }
    
    public void setSku(String sku) {
        this.sku = sku;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public ProductCategory getCategory() {
        return category;
    }
    
    public void setCategory(ProductCategory category) {
        this.category = category;
    }
    
    public ProductStatus getStatus() {
        return status;
    }
    
    public void setStatus(ProductStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    // ====================
    // equals & hashCode
    // ====================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(sku, product.sku);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(sku);
    }
    
    @Override
    public String toString() {
        return "Product{id=%d, name='%s', sku='%s', price=%s, stock=%d, status=%s}"
            .formatted(id, name, sku, price, stockQuantity, status);
    }
}

// ====================
// Enums
// ====================

enum ProductCategory {
    ELECTRONICS,
    CLOTHING,
    BOOKS,
    HOME_APPLIANCES,
    SPORTS,
    TOYS,
    FOOD,
    OTHER
}

enum ProductStatus {
    AVAILABLE,      // Disponível para venda
    OUT_OF_STOCK,   // Sem estoque
    INACTIVE,       // Desativado manualmente
    DISCONTINUED    // Descontinuado
}
```

#### 3.3.2 Repository Implementation

```java
package com.example.repository;

import com.example.entity.Product;
import com.example.entity.ProductCategory;
import com.example.entity.ProductStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para Product usando PanacheRepository.
 * 
 * RESPONSABILIDADES:
 * - Queries customizadas
 * - Agregações e estatísticas
 * - Operações em lote
 * - Consultas complexas com JOIN
 * 
 * VANTAGENS:
 * - Facilita testes (mock do repository)
 * - Centraliza lógica de persistência
 * - Compatível com arquiteturas limpas
 */
@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    
    private static final Logger LOG = LoggerFactory.getLogger(ProductRepository.class);
    
    // ═══════════════════════════════════════════════════════════
    // Queries Básicas
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Busca produto por SKU.
     */
    public Optional<Product> findBySku(String sku) {
        LOG.debug("Buscando produto por SKU: {}", sku);
        return find("sku", sku).firstResultOptional();
    }
    
    /**
     * Lista produtos por categoria.
     */
    public List<Product> findByCategory(ProductCategory category) {
        LOG.debug("Buscando produtos da categoria: {}", category);
        return find("category", Sort.by("name"), category).list();
    }
    
    /**
     * Lista produtos por status.
     */
    public List<Product> findByStatus(ProductStatus status) {
        LOG.debug("Buscando produtos com status: {}", status);
        return find("status", status).list();
    }
    
    // ═══════════════════════════════════════════════════════════
    // Queries Avançadas
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Busca produtos disponíveis com estoque.
     */
    public List<Product> findAvailableProducts() {
        LOG.debug("Buscando produtos disponíveis");
        return find(
            "status = ?1 and stockQuantity > 0",
            Sort.by("name"),
            ProductStatus.AVAILABLE
        ).list();
    }
    
    /**
     * Busca produtos por faixa de preço.
     */
    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        LOG.debug("Buscando produtos entre {} e {}", minPrice, maxPrice);
        return find(
            "price >= :minPrice and price <= :maxPrice",
            Sort.by("price"),
            Parameters.with("minPrice", minPrice).and("maxPrice", maxPrice)
        ).list();
    }
    
    /**
     * Busca produtos com estoque baixo.
     */
    public List<Product> findLowStockProducts(int threshold) {
        LOG.debug("Buscando produtos com estoque <= {}", threshold);
        return find(
            "stockQuantity <= ?1 and status = ?2",
            Sort.by("stockQuantity"),
            threshold,
            ProductStatus.AVAILABLE
        ).list();
    }
    
    /**
     * Busca produtos criados após determinada data.
     */
    public List<Product> findProductsCreatedAfter(LocalDateTime date) {
        LOG.debug("Buscando produtos criados após: {}", date);
        return find(
            "createdAt > ?1",
            Sort.by("createdAt").descending(),
            date
        ).list();
    }
    
    /**
     * Busca por nome (LIKE case-insensitive).
     */
    public List<Product> searchByName(String searchTerm) {
        LOG.debug("Buscando produtos por nome: {}", searchTerm);
        return find(
            "LOWER(name) LIKE LOWER(?1)",
            Sort.by("name"),
            "%" + searchTerm + "%"
        ).list();
    }
    
    // ═══════════════════════════════════════════════════════════
    // Agregações e Estatísticas
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Conta produtos por categoria.
     */
    public long countByCategory(ProductCategory category) {
        LOG.debug("Contando produtos da categoria: {}", category);
        return count("category", category);
    }
    
    /**
     * Conta produtos disponíveis.
     */
    public long countAvailableProducts() {
        LOG.debug("Contando produtos disponíveis");
        return count("status = ?1 and stockQuantity > 0", ProductStatus.AVAILABLE);
    }
    
    /**
     * Soma total do estoque (em unidades).
     */
    public Long getTotalStockQuantity() {
        LOG.debug("Calculando total de unidades em estoque");
        return find("SELECT SUM(p.stockQuantity) FROM Product p")
            .project(Long.class)
            .firstResult();
    }
    
    /**
     * Calcula valor total do inventário.
     */
    public BigDecimal getTotalInventoryValue() {
        LOG.debug("Calculando valor total do inventário");
        BigDecimal total = find(
            "SELECT SUM(p.price * p.stockQuantity) FROM Product p"
        ).project(BigDecimal.class).firstResult();
        
        return total != null ? total : BigDecimal.ZERO;
    }
    
    /**
     * Obtém preço médio por categoria.
     */
    public BigDecimal getAveragePriceByCategory(ProductCategory category) {
        LOG.debug("Calculando preço médio da categoria: {}", category);
        return find(
            "SELECT AVG(p.price) FROM Product p WHERE p.category = ?1",
            category
        ).project(BigDecimal.class).firstResult();
    }
    
    // ═══════════════════════════════════════════════════════════
    // Operações em Lote
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Desativa todos os produtos sem estoque.
     */
    public long deactivateOutOfStockProducts() {
        LOG.info("Desativando produtos sem estoque");
        return update(
            "status = ?1 WHERE stockQuantity = 0 and status != ?2",
            ProductStatus.OUT_OF_STOCK,
            ProductStatus.DISCONTINUED
        );
    }
    
    /**
     * Aplica desconto em produtos de uma categoria.
     */
    public long applyDiscountToCategory(ProductCategory category, BigDecimal discountPercentage) {
        LOG.info("Aplicando desconto de {}% na categoria: {}", discountPercentage, category);
        return update(
            "price = price * (1 - ?1 / 100) WHERE category = ?2",
            discountPercentage,
            category
        );
    }
    
    /**
     * Deleta produtos descontinuados há mais de X dias.
     */
    public long deleteOldDiscontinuedProducts(int daysOld) {
        LOG.warn("Deletando produtos descontinuados há mais de {} dias", daysOld);
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        return delete(
            "status = ?1 and updatedAt < ?2",
            ProductStatus.DISCONTINUED,
            cutoffDate
        );
    }
    
    // ═══════════════════════════════════════════════════════════
    // Queries com Paginação
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Lista produtos com paginação.
     */
    public List<Product> findAllPaged(int pageIndex, int pageSize) {
        LOG.debug("Buscando página {} com {} itens", pageIndex, pageSize);
        return findAll(Sort.by("name"))
            .page(pageIndex, pageSize)
            .list();
    }
    
    /**
     * Busca produtos de uma categoria com paginação.
     */
    public List<Product> findByCategoryPaged(ProductCategory category, int pageIndex, int pageSize) {
        LOG.debug("Buscando produtos da categoria {} (página {})", category, pageIndex);
        return find("category", Sort.by("name"), category)
            .page(pageIndex, pageSize)
            .list();
    }
}
```

#### 3.3.3 Service Layer (Opcional)

```java
package com.example.service;

import com.example.entity.Product;
import com.example.entity.ProductCategory;
import com.example.repository.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service para lógica de negócio de produtos.
 * 
 * Responsabilidades:
 * - Orquestração de operações complexas
 * - Validações de negócio
 * - Transações
 */
@ApplicationScoped
public class ProductService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);
    
    @Inject
    ProductRepository productRepository;
    
    /**
     * Cria novo produto.
     */
    @Transactional
    public Product createProduct(Product product) {
        LOG.info("Criando produto: {}", product.getSku());
        
        // Validação de negócio
        productRepository.findBySku(product.getSku()).ifPresent(existing -> {
            throw new IllegalStateException("SKU já existe: " + product.getSku());
        });
        
        productRepository.persist(product);
        LOG.info("Produto criado com ID: {}", product.getId());
        return product;
    }
    
    /**
     * Vende produto (reduz estoque).
     */
    @Transactional
    public void sellProduct(Long productId, int quantity) {
        LOG.info("Vendendo {} unidades do produto ID: {}", quantity, productId);
        
        Product product = productRepository.findByIdOptional(productId)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        
        product.reduceStock(quantity);
        // Hibernate faz UPDATE automático (dirty checking)
    }
    
    /**
     * Repõe estoque.
     */
    @Transactional
    public void restockProduct(Long productId, int quantity) {
        LOG.info("Repondo {} unidades do produto ID: {}", quantity, productId);
        
        Product product = productRepository.findByIdOptional(productId)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        
        product.addStock(quantity);
    }
    
    /**
     * Lista produtos disponíveis de uma categoria.
     */
    public List<Product> getAvailableProductsByCategory(ProductCategory category) {
        LOG.debug("Listando produtos disponíveis da categoria: {}", category);
        return productRepository.findByCategory(category).stream()
            .filter(Product::isAvailable)
            .toList();
    }
}
```

#### 3.3.4 REST Resource

```java
package com.example.resource;

import com.example.entity.Product;
import com.example.entity.ProductCategory;
import com.example.service.ProductService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

/**
 * REST API para produtos usando Repository Pattern.
 */
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Products", description = "Operações de produtos")
public class ProductResource {
    
    @Inject
    ProductService productService;
    
    @POST
    @Operation(summary = "Cria novo produto")
    public Response create(@Valid Product product) {
        Product created = productService.createProduct(product);
        return Response.created(URI.create("/products/" + created.getId()))
            .entity(created)
            .build();
    }
    
    @GET
    @Path("/category/{category}")
    @Operation(summary = "Lista produtos por categoria")
    public List<Product> listByCategory(@PathParam("category") ProductCategory category) {
        return productService.getAvailableProductsByCategory(category);
    }
    
    @PATCH
    @Path("/{id}/sell")
    @Operation(summary = "Vende produto (reduz estoque)")
    public Response sell(
        @PathParam("id") Long id,
        @QueryParam("quantity") @DefaultValue("1") int quantity
    ) {
        productService.sellProduct(id, quantity);
        return Response.ok().build();
    }
    
    @PATCH
    @Path("/{id}/restock")
    @Operation(summary = "Repõe estoque")
    public Response restock(
        @PathParam("id") Long id,
        @QueryParam("quantity") int quantity
    ) {
        productService.restockProduct(id, quantity);
        return Response.ok().build();
    }
}
```

---

### 3.4 PanacheRepositoryBase (ID Customizado)

Por padrão, `PanacheRepository` assume que o ID é `Long`. Para usar **tipos customizados** (UUID, String, etc.), use `PanacheRepositoryBase<Entity, IdType>`.

#### 3.4.1 Entidade com UUID

```java
package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade Order usando UUID como chave primária.
 * 
 * VANTAGENS DO UUID:
 * - IDs gerados na aplicação (não depende do banco)
 * - Segurança (IDs não sequenciais)
 * - Facilita merge de bancos distribuídos
 * - Útil para APIs públicas (oculta volume de dados)
 */
@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @NotBlank
    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;
    
    @NotNull
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @NotBlank
    @Column(name = "customer_name", nullable = false, length = 200)
    private String customerName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status = OrderStatus.PENDING;
    
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal totalAmount = java.math.BigDecimal.ZERO;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // ====================
    // Lifecycle Callbacks
    // ====================
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        // Gera número de pedido se não fornecido
        if (this.orderNumber == null || this.orderNumber.isBlank()) {
            this.orderNumber = "ORD-" + System.currentTimeMillis();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // ====================
    // Business Methods
    // ====================
    
    /**
     * Confirma o pedido.
     */
    public void confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Apenas pedidos pendentes podem ser confirmados");
        }
        this.status = OrderStatus.CONFIRMED;
    }
    
    /**
     * Envia o pedido.
     */
    public void ship() {
        if (this.status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Apenas pedidos confirmados podem ser enviados");
        }
        this.status = OrderStatus.SHIPPED;
    }
    
    /**
     * Entrega o pedido.
     */
    public void deliver() {
        if (this.status != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Apenas pedidos enviados podem ser entregues");
        }
        this.status = OrderStatus.DELIVERED;
    }
    
    /**
     * Cancela o pedido.
     */
    public void cancel() {
        if (this.status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Pedidos entregues não podem ser cancelados");
        }
        this.status = OrderStatus.CANCELLED;
    }
    
    /**
     * Verifica se o pedido pode ser modificado.
     */
    public boolean canBeModified() {
        return this.status == OrderStatus.PENDING;
    }
    
    // ====================
    // Getters & Setters
    // ====================
    
    public UUID getId() {
        return id;
    }
    
    public String getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public java.math.BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(java.math.BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    // ====================
    // equals & hashCode
    // ====================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Order{id=%s, orderNumber='%s', status=%s, totalAmount=%s}"
            .formatted(id, orderNumber, status, totalAmount);
    }
}

// ====================
// Enum
// ====================

enum OrderStatus {
    PENDING,      // Aguardando confirmação
    CONFIRMED,    // Confirmado
    SHIPPED,      // Enviado
    DELIVERED,    // Entregue
    CANCELLED     // Cancelado
}
```

#### 3.4.2 Repository com PanacheRepositoryBase

```java
package com.example.repository;

import com.example.entity.Order;
import com.example.entity.OrderStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository para Order usando UUID como ID.
 * 
 * PanacheRepositoryBase<Entity, IdType> permite usar tipos customizados de ID.
 */
@ApplicationScoped
public class OrderRepository implements PanacheRepositoryBase<Order, UUID> {
    
    private static final Logger LOG = LoggerFactory.getLogger(OrderRepository.class);
    
    // ═══════════════════════════════════════════════════════════
    // Queries Básicas
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Busca pedido por número.
     */
    public Optional<Order> findByOrderNumber(String orderNumber) {
        LOG.debug("Buscando pedido: {}", orderNumber);
        return find("orderNumber", orderNumber).firstResultOptional();
    }
    
    /**
     * Lista pedidos de um cliente.
     */
    public List<Order> findByCustomerId(Long customerId) {
        LOG.debug("Buscando pedidos do cliente: {}", customerId);
        return find("customerId", Sort.by("createdAt").descending(), customerId).list();
    }
    
    /**
     * Lista pedidos por status.
     */
    public List<Order> findByStatus(OrderStatus status) {
        LOG.debug("Buscando pedidos com status: {}", status);
        return find("status", Sort.by("createdAt").descending(), status).list();
    }
    
    // ═══════════════════════════════════════════════════════════
    // Queries Avançadas
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Busca pedidos pendentes há mais de X horas.
     */
    public List<Order> findPendingOrdersOlderThan(int hoursOld) {
        LOG.debug("Buscando pedidos pendentes há mais de {} horas", hoursOld);
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(hoursOld);
        return find(
            "status = :status and createdAt < :cutoff",
            Sort.by("createdAt"),
            Parameters.with("status", OrderStatus.PENDING)
                .and("cutoff", cutoffTime)
        ).list();
    }
    
    /**
     * Busca pedidos por faixa de valor.
     */
    public List<Order> findByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        LOG.debug("Buscando pedidos entre {} e {}", minAmount, maxAmount);
        return find(
            "totalAmount >= :min and totalAmount <= :max",
            Sort.by("totalAmount").descending(),
            Parameters.with("min", minAmount).and("max", maxAmount)
        ).list();
    }
    
    /**
     * Busca pedidos de um cliente em determinado período.
     */
    public List<Order> findByCustomerAndDateRange(
        Long customerId,
        LocalDateTime startDate,
        LocalDateTime endDate
    ) {
        LOG.debug("Buscando pedidos do cliente {} entre {} e {}", 
            customerId, startDate, endDate);
        return find(
            "customerId = :customerId and createdAt >= :start and createdAt <= :end",
            Sort.by("createdAt").descending(),
            Parameters.with("customerId", customerId)
                .and("start", startDate)
                .and("end", endDate)
        ).list();
    }
    
    // ═══════════════════════════════════════════════════════════
    // Agregações
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Conta pedidos por status.
     */
    public long countByStatus(OrderStatus status) {
        LOG.debug("Contando pedidos com status: {}", status);
        return count("status", status);
    }
    
    /**
     * Calcula total de vendas de um cliente.
     */
    public BigDecimal getTotalSalesByCustomer(Long customerId) {
        LOG.debug("Calculando total de vendas do cliente: {}", customerId);
        BigDecimal total = find(
            "SELECT SUM(o.totalAmount) FROM Order o WHERE o.customerId = ?1 and o.status != ?2",
            customerId,
            OrderStatus.CANCELLED
        ).project(BigDecimal.class).firstResult();
        
        return total != null ? total : BigDecimal.ZERO;
    }
    
    /**
     * Calcula valor médio dos pedidos.
     */
    public BigDecimal getAverageOrderValue() {
        LOG.debug("Calculando valor médio dos pedidos");
        BigDecimal avg = find(
            "SELECT AVG(o.totalAmount) FROM Order o WHERE o.status != ?1",
            OrderStatus.CANCELLED
        ).project(BigDecimal.class).firstResult();
        
        return avg != null ? avg : BigDecimal.ZERO;
    }
    
    /**
     * Conta pedidos ativos de um cliente.
     */
    public long countActiveOrdersByCustomer(Long customerId) {
        LOG.debug("Contando pedidos ativos do cliente: {}", customerId);
        return count(
            "customerId = ?1 and status IN (?2, ?3, ?4)",
            customerId,
            OrderStatus.PENDING,
            OrderStatus.CONFIRMED,
            OrderStatus.SHIPPED
        );
    }
    
    // ═══════════════════════════════════════════════════════════
    // Operações em Lote
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Cancela pedidos pendentes antigos.
     */
    public long cancelOldPendingOrders(int daysOld) {
        LOG.warn("Cancelando pedidos pendentes há mais de {} dias", daysOld);
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        return update(
            "status = ?1 WHERE status = ?2 and createdAt < ?3",
            OrderStatus.CANCELLED,
            OrderStatus.PENDING,
            cutoffDate
        );
    }
    
    /**
     * Atualiza status de pedidos enviados para entregue.
     */
    public long markShippedAsDelivered(List<UUID> orderIds) {
        LOG.info("Marcando {} pedidos como entregues", orderIds.size());
        return update(
            "status = ?1 WHERE id IN (?2) and status = ?3",
            OrderStatus.DELIVERED,
            orderIds,
            OrderStatus.SHIPPED
        );
    }
}
```

#### 3.4.3 Service com UUID

```java
package com.example.service;

import com.example.entity.Order;
import com.example.entity.OrderStatus;
import com.example.repository.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Service para lógica de negócio de pedidos.
 */
@ApplicationScoped
public class OrderService {
    
    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);
    
    @Inject
    OrderRepository orderRepository;
    
    /**
     * Cria novo pedido.
     */
    @Transactional
    public Order createOrder(Order order) {
        LOG.info("Criando pedido para cliente: {}", order.getCustomerId());
        
        // Validações de negócio
        if (order.getTotalAmount().signum() <= 0) {
            throw new IllegalArgumentException("Valor do pedido deve ser positivo");
        }
        
        orderRepository.persist(order);
        LOG.info("Pedido criado: {} (ID: {})", order.getOrderNumber(), order.getId());
        return order;
    }
    
    /**
     * Processa pedido (transição de status).
     */
    @Transactional
    public void processOrder(UUID orderId) {
        LOG.info("Processando pedido: {}", orderId);
        
        Order order = orderRepository.findByIdOptional(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        
        if (order.getStatus() == OrderStatus.PENDING) {
            order.confirm();
            LOG.info("Pedido {} confirmado", orderId);
        }
    }
    
    /**
     * Envia pedido.
     */
    @Transactional
    public void shipOrder(UUID orderId) {
        LOG.info("Enviando pedido: {}", orderId);
        
        Order order = orderRepository.findByIdOptional(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        
        order.ship();
        LOG.info("Pedido {} enviado", orderId);
    }
    
    /**
     * Cancela pedido.
     */
    @Transactional
    public void cancelOrder(UUID orderId, String reason) {
        LOG.warn("Cancelando pedido {}: {}", orderId, reason);
        
        Order order = orderRepository.findByIdOptional(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        
        order.cancel();
        LOG.info("Pedido {} cancelado", orderId);
    }
}
```

#### 3.4.4 REST Resource

```java
package com.example.resource;

import com.example.entity.Order;
import com.example.service.OrderService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.UUID;

/**
 * REST API para pedidos usando UUID.
 */
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    
    @Inject
    OrderService orderService;
    
    @POST
    public Response create(@Valid Order order) {
        Order created = orderService.createOrder(order);
        return Response.created(URI.create("/orders/" + created.getId()))
            .entity(created)
            .build();
    }
    
    @POST
    @Path("/{id}/process")
    public Response process(@PathParam("id") UUID id) {
        orderService.processOrder(id);
        return Response.ok().build();
    }
    
    @POST
    @Path("/{id}/ship")
    public Response ship(@PathParam("id") UUID id) {
        orderService.shipOrder(id);
        return Response.ok().build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response cancel(
        @PathParam("id") UUID id,
        @QueryParam("reason") @DefaultValue("Cancelado pelo cliente") String reason
    ) {
        orderService.cancelOrder(id, reason);
        return Response.noContent().build();
    }
}
```

---

## 4. Exercícios de Verificação

### Exercício 1: Implementação Básica
Crie uma entidade `Book` usando **PanacheEntity** com os campos:
- `title` (String, obrigatório)
- `author` (String, obrigatório)
- `isbn` (String, único)
- `publishYear` (Integer)
- `available` (Boolean, padrão `true`)

Implemente:
1. Método estático `findByAuthor(String author)`
2. Método estático `findAvailableBooks()`
3. Método de instância `borrow()` que marca `available = false`
4. REST endpoint para emprestar livro (`PATCH /books/{id}/borrow`)

---

### Exercício 2: Repository Pattern
Converta a entidade `Book` do Exercício 1 para usar **PanacheRepository**:
1. Remova herança de `PanacheEntity`
2. Crie `BookRepository implements PanacheRepository<Book>`
3. Implemente query que busca livros publicados em determinada década
4. Adicione aggregação: `countBooksByAuthor(String author)`

---

### Exercício 3: UUID e Relacionamentos
Crie sistema de empréstimos com UUID:
1. Entidade `Loan` (UUID como ID) com:
   - `bookId` (Long, FK para Book)
   - `borrowerName` (String)
   - `loanDate` (LocalDateTime)
   - `returnDate` (LocalDateTime, nullable)
2. `LoanRepository extends PanacheRepositoryBase<Loan, UUID>`
3. Queries:
   - Empréstimos ativos (sem `returnDate`)
   - Empréstimos atrasados (> 15 dias)
   - Histórico por usuário
4. Operação bulk: marcar empréstimos como retornados

---

## 5. Aprofundamento

### 5.1 Comparativo: Active Record vs Repository
```mermaid
graph LR
    subgraph "Active Record (PanacheEntity)"
        A1[Entidade] -->|herda| A2[Métodos CRUD]
        A2 --> A3[persist/delete/find]
        A4[Menos Código] --> A5[70% menos linhas]
        A6[Trade-off] --> A7[Entidade + Persistência]
    end
    
    subgraph "Repository (PanacheRepository)"
        B1[Entidade POJO] -.-> B2[Repository]
        B2 --> B3[Métodos CRUD]
        B4[Separação] --> B5[SRP/Clean Arch]
        B6[Testável] --> B7[Mock fácil]
    end
    
    style A7 fill:#FFB6C1
    style B5 fill:#90EE90
```
### 5.2 Quando Usar Cada Padrão

| Critério                    | PanacheEntity (Active Record)  | PanacheRepository                 |
| --------------------------- | ------------------------------ | --------------------------------- |
| **Complexidade do Domínio** | Baixa/Média (CRUD simples)     | Média/Alta (DDD, lógica complexa) |
| **Tamanho do Projeto**      | Pequeno/Médio                  | Médio/Grande                      |
| **Arquitetura**             | Pragmática, RAD                | Clean Architecture, Hexagonal     |
| **Testabilidade**           | Dificulta mock da entidade     | Facilita mock do repository       |
| **Encapsulamento**          | Campos públicos (violação OOP) | Getters/Setters tradicionais      |
| **Performance**             | Igual (mesma engine Hibernate) | Igual                             |
| **Curva de Aprendizado**    | Menor (menos conceitos)        | Maior (mais abstrações)           |

**Recomendação Geral:**
- **Protótipos, MVPs, APIs CRUD**: PanacheEntity
- **Sistemas empresariais, equipes grandes**: PanacheRepository
- **DDD com Aggregates**: PanacheRepository + Private Setters

---

### 5.3 Query Methods e Performance

#### Named Queries (Compile-time)
```java
@Entity
@NamedQueries({
    @NamedQuery(
        name = "Product.findExpensive",
        query = "SELECT p FROM Product p WHERE p.price > :threshold"
    )
})
public class Product extends PanacheEntity {
    public static List<Product> findExpensive(BigDecimal threshold) {
        return find("#Product.findExpensive", 
            Parameters.with("threshold", threshold)).list();
    }
}
```

**Vantagem:** Validação em compile-time, melhor cache de queries.

#### Panache Query Shortcuts
```java
// Ordenação
Product.find("category", Sort.by("price").descending(), category).list();

// Paginação
Product.find("active", true).page(Page.of(0, 10)).list();

// Stream (batch processing)
Product.streamAll()
    .filter(p -> p.getStockQuantity() > 0)
    .forEach(p -> LOG.info("Product: {}", p));
```

---

### 5.4 Transações e Pitfalls

#### Problema: LazyInitializationException
```java
// ❌ ERRADO - fora da transação
@GET
@Path("/{id}/items")
public List<OrderItem> getItems(@PathParam("id") Long id) {
    Order order = Order.findById(id);
    return order.items; // LazyInitializationException!
}

// ✅ CORRETO - fetch explícito
@GET
@Path("/{id}/items")
public List<OrderItem> getItems(@PathParam("id") Long id) {
    return Order.find(
        "SELECT o FROM Order o JOIN FETCH o.items WHERE o.id = ?1", 
        id
    ).firstResult().items;
}
```

#### Problema: N+1 Queries
```java
// ❌ ERRADO - 1 query + N queries
List<Order> orders = Order.listAll();
orders.forEach(o -> LOG.info("Customer: {}", o.customer.name)); // N queries!

// ✅ CORRETO - JOIN FETCH
List<Order> orders = Order.find(
    "SELECT DISTINCT o FROM Order o JOIN FETCH o.customer"
).list();
```

---

### 5.5 Alternativas e Comparação

#### JPA Tradicional (EntityManager)
```java
@Inject
EntityManager em;

public List<Product> findByCategory(ProductCategory category) {
    return em.createQuery(
        "SELECT p FROM Product p WHERE p.category = :cat", 
        Product.class
    )
    .setParameter("cat", category)
    .getResultList();
}
```
**Trade-off:** Mais verboso, mas controle total sobre contexto de persistência.

#### Spring Data JPA
```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryAndActiveTrue(ProductCategory category);
}
```
**Comparação:**
- **Spring Data:** Inferência de query por nome do método (mais mágica)
- **Panache:** Queries explícitas (menos surpresas)
- **Performance:** Equivalente

#### jOOQ (Type-safe SQL)

```mermaid
sequenceDiagram
    participant C as Client
    participant R as REST Resource
    participant S as Service (Opcional)
    participant PR as PanacheRepository
    participant H as Hibernate ORM
    participant DB as PostgreSQL
    
    C->>R: POST /products
    activate R
    R->>R: Validação (@Valid)
    
    alt Using Repository Pattern
        R->>PR: persist(product)
        activate PR
        PR->>H: EntityManager.persist()
    else Using Active Record
        R->>H: product.persist()
    end
    
    activate H
    H->>H: Flush & Dirty Checking
    H->>H: SQL Generation
    H->>DB: INSERT INTO products...
    activate DB
    DB-->>H: ID Generated
    deactivate DB
    
    H-->>PR: Entity Managed
    deactivate H
    PR-->>R: Product with ID
    deactivate PR
    
    R->>R: Response.created()
    R-->>C: 201 Created + Location Header
    deactivate R
    
    Note over H,DB: @Transactional garante ACID
    Note over PR,H: Session aberta durante request
    Note over R,PR: Injection via CDI
```
---

## 9. Exemplo Completo: Sistema de E-commerce
```java
package com.example.ecommerce;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// ====================
// ENTITIES
// ====================

@Entity
@Table(name = "customers")
class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
    
    // Getters/Setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<Order> getOrders() { return orders; }
}

@Entity
@Table(name = "orders")
class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;
    
    @Column(name = "total_amount")
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Business Logic
    public void addItem(Product product, Integer quantity) {
        if (!product.hasStock(quantity)) {
            throw new IllegalStateException("Estoque insuficiente");
        }
        
        OrderItem item = new OrderItem();
        item.setOrder(this);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPrice(product.getPrice());
        
        this.items.add(item);
        this.calculateTotal();
    }
    
    public void calculateTotal() {
        this.totalAmount = items.stream()
            .map(OrderItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public void checkout() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Pedido vazio");
        }
        
        // Reserva estoque
        items.forEach(item -> 
            item.getProduct().removeStock(item.getQuantity())
        );
        
        this.status = OrderStatus.CONFIRMED;
    }
    
    // Getters/Setters
    public Long getId() { return id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

@Entity
@Table(name = "order_items")
class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;
    
    private Integer quantity;
    
    private BigDecimal price;
    
    public BigDecimal getSubtotal() {
        return price.multiply(new BigDecimal(quantity));
    }
    
    // Getters/Setters
    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}

@Entity
@Table(name = "products")
class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private BigDecimal price;
    private Integer stock = 0;
    
    public boolean hasStock(Integer quantity) {
        return this.stock >= quantity;
    }
    
    public void removeStock(Integer quantity) {
        if (!hasStock(quantity)) {
            throw new IllegalStateException("Estoque insuficiente");
        }
        this.stock -= quantity;
    }
    
    // Getters/Setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}

enum OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}

// ====================
// REPOSITORIES
// ====================

@ApplicationScoped
class CustomerRepository implements PanacheRepository<Customer> {
    public Customer findByEmail(String email) {
        return find("email", email).firstResult();
    }
}

@ApplicationScoped
class OrderRepository implements PanacheRepository<Order> {
    private static final Logger LOG = LoggerFactory.getLogger(OrderRepository.class);
    
    public List<Order> findByCustomer(Long customerId) {
        return find(
            "SELECT o FROM Order o JOIN FETCH o.items WHERE o.customer.id = ?1",
            customerId
        ).list();
    }
    
    public BigDecimal calculateRevenueByPeriod(LocalDateTime start, LocalDateTime end) {
        LOG.info("Calculando receita entre {} e {}", start, end);
        return getEntityManager()
            .createQuery(
                """
                SELECT COALESCE(SUM(o.totalAmount), 0)
                FROM Order o
                WHERE o.status = :status
                AND o.createdAt BETWEEN :start AND :end
                """,
                BigDecimal.class
            )
            .setParameter("status", OrderStatus.DELIVERED)
            .setParameter("start", start)
            .setParameter("end", end)
            .getSingleResult();
    }
}

@ApplicationScoped
class ProductRepository implements PanacheRepository<Product> {
    public List<Product> findInStock() {
        return list("stock > 0");
    }
}

// ====================
// DTOs
// ====================

record CreateOrderRequest(
    @NotNull Long customerId,
    @NotNull List<OrderItemRequest> items
) {}

record OrderItemRequest(
    @NotNull Long productId,
    @Positive Integer quantity
) {}

record OrderResponse(
    Long id,
    String customerName,
    List<OrderItemResponse> items,
    BigDecimal totalAmount,
    OrderStatus status
) {
    static OrderResponse from(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getCustomer().getName(),
            order.getItems().stream()
                .map(OrderItemResponse::from)
                .toList(),
            order.getTotalAmount(),
            order.getStatus()
        );
    }
}

record OrderItemResponse(
    String productName,
    Integer quantity,
    BigDecimal price,
    BigDecimal subtotal
) {
    static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
            item.getProduct().getName(),
            item.getQuantity(),
            item.getPrice(),
            item.getSubtotal()
        );
    }
}

// ====================
// SERVICES
// ====================

@ApplicationScoped
class OrderService {
    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);
    
    @Inject
    OrderRepository orderRepository;
    
    @Inject
    CustomerRepository customerRepository;
    
    @Inject
    ProductRepository productRepository;
    
    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        LOG.info("Criando pedido para customer {}", request.customerId());
        
        Customer customer = customerRepository.findById(request.customerId());
        if (customer == null) {
            throw new NotFoundException("Cliente não encontrado");
        }
        
        Order order = new Order();
        order.setCustomer(customer);
        
        for (OrderItemRequest itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productId());
            if (product == null) {
                throw new NotFoundException("Produto não encontrado: " + itemReq.productId());
            }
            
            order.addItem(product, itemReq.quantity());
        }
        
        order.checkout();
        orderRepository.persist(order);
        
        LOG.info("Pedido {} criado com sucesso. Total: {}", 
            order.getId(), order.getTotalAmount());
        
        return order;
    }
}

// ====================
// REST RESOURCE
// ====================

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class OrderResource {
    private static final Logger LOG = LoggerFactory.getLogger(OrderResource.class);
    
    @Inject
    OrderService orderService;
    
    @Inject
    OrderRepository orderRepository;
    
    @POST
    public Response createOrder(@Valid CreateOrderRequest request) {
        LOG.info("Recebendo pedido: {}", request);
        
        try {
            Order order = orderService.createOrder(request);
            OrderResponse response = OrderResponse.from(order);
            
            return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
                
        } catch (NotFoundException e) {
            LOG.warn("Erro ao criar pedido: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                .entity(e.getMessage())
                .build();
                
        } catch (IllegalStateException e) {
            LOG.warn("Erro de validação: {}", e.getMessage());
            return Response.status(Response.Status.CONFLICT)
                .entity(e.getMessage())
                .build();
        }
    }
    
    @GET
    @Path("/customer/{customerId}")
    public List<OrderResponse> findByCustomer(@PathParam("customerId") Long customerId) {
        LOG.info("Buscando pedidos do cliente {}", customerId);
        
        return orderRepository.findByCustomer(customerId)
            .stream()
            .map(OrderResponse::from)
            .toList();
    }
    
    @GET
    @Path("/revenue")
    public Response getRevenue(
        @QueryParam("start") String start,
        @QueryParam("end") String end
    ) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        
        BigDecimal revenue = orderRepository.calculateRevenueByPeriod(startDate, endDate);
        
        return Response.ok()
            .entity(STR."Revenue: \{revenue}")
            .build();
    }
}
```
---

## 10. Troubleshooting Comum

### Problema 1: `Could not determine type for: java.time.LocalDateTime`
**Solução:**
```properties
# application.properties
quarkus.hibernate-orm.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### Problema 2: `TransactionRequiredException`
**Causa:** Operação de escrita fora de transação.
**Solução:**
```java
@POST
@Transactional  // ← Adicione esta anotação
public Response create(Product product) {
    productRepository.persist(product);
    return Response.ok().build();
}
```

### Problema 3: `LazyInitializationException`
**Causa:** Acesso a relacionamento lazy fora da sessão.
**Solução:**
```java
// Opção 1: JOIN FETCH
Order.find("SELECT o FROM Order o JOIN FETCH o.items WHERE o.id = ?1", id)

// Opção 2: EAGER
@ManyToOne(fetch = FetchType.EAGER)

// Opção 3: DTO Projection
```

### Problema 4: Performance em `listAll()`
**Causa:** Carregando milhares de registros sem paginação.
**Solução:**
```java
@GET
public List<Product> listAll(
    @QueryParam("page") @DefaultValue("0") int page,
    @QueryParam("size") @DefaultValue("20") int size
) {
    return Product.findAll(Sort.by("name"))
        .page(Page.of(page, size))
        .list();
}
```

### Problema 5: Testes com `PanacheQuery not mocked`
**Solução:**
```java
@QuarkusTest
class ProductRepositoryTest {
    @Inject
    ProductRepository repository;
    
    // ✅ Use injeção real em @QuarkusTest
    // Ou use @QuarkusTestResource com banco H2
}

// Para mock unit test:
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepository repository;
    
    @InjectMocks
    ProductService service;
    
    @Test
    void test() {
        when(repository.findById(1L)).thenReturn(new Product());
        // ...
    }
}
```

---

## Conclusão

Panache revoluciona o desenvolvimento com Hibernate no Quarkus ao:
- **Reduzir código boilerplate em até 70%**
- **Oferecer dois padrões arquiteturais** (Active Record vs Repository)
- **Manter 100% de compatibilidade** com JPA/Hibernate
- **Facilitar testes e manutenção**

**Escolha consciente:**
- **PanacheEntity**: Agilidade, protótipos, CRUDs simples
- **PanacheRepository**: Arquitetura limpa, DDD, projetos enterprise
- **PanacheRepositoryBase**: IDs customizados (UUID, String, etc)

A documentação oficial e a comunidade Quarkus são recursos valiosos para aprofundamento contínuo.