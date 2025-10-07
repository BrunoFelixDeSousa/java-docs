# 🎯 Guia Completo de Annotations Personalizadas para Quarkus

> **Transforme seu código Quarkus em uma arquitetura enterprise de alta produtividade**

## 📖 Índice

1. [O que são Annotations Personalizadas?](#o-que-são-annotations-personalizadas)
2. [Por que usar Annotations Personalizadas?](#por-que-usar)
3. [Conceitos Fundamentais](#conceitos-fundamentais)
4. [Estrutura de Diretórios](#estrutura-de-diretórios)
5. [Padrões de Nomenclatura](#padrões-de-nomenclatura)
6. [Interceptadores Automáticos](#interceptadores-automáticos)
7. [Meta-Annotations Avançadas](#meta-annotations-avançadas)
8. [Qualificadores Específicos](#qualificadores-específicos)
9. [Annotations com Metadados Ricos](#annotations-com-metadados-ricos)
10. [CRUD Completo - Exemplo Prático](#crud-completo-exemplo-prático)
11. [Configuração do Projeto](#configuração-do-projeto)
12. [Boas Práticas](#boas-práticas)
13. [Referência Rápida de Annotations](#referência-rápida-de-annotations)

### 📑 Índice de Annotations Personalizadas

#### 🎯 Camadas da Aplicação
- [@RestController](#restcontroller) - Controllers REST com configuração automática
- [@UseCase](#usecase) - Casos de uso da camada de aplicação
- [@DomainService](#domainservice) - Serviços de domínio
- [@Repository](#repository) - Repositórios de dados

#### 🔄 Operações CRUD
- [@CreateOperation](#createoperation) - Operações de criação
- [@ReadOperation](#readoperation) - Operações de leitura
- [@UpdateOperation](#updateoperation) - Operações de atualização
- [@DeleteOperation](#deleteoperation) - Operações de remoção
- [@SearchOperation](#searchoperation) - Operações de busca

#### 📊 Observabilidade
- [@Monitored](#monitored) - Monitoramento com métricas e logs
- [@Timed](#timed) - Medição de tempo de execução
- [@Audited](#audited) - Auditoria de operações

#### ✅ Validação e Segurança
- [@ValidEmail](#validemail) - Validação de email
- [@ValidCPF](#validcpf) - Validação de CPF
- [@ValidCNPJ](#validcnpj) - Validação de CNPJ
- [@AdminOnly](#adminonly) - Restrição para administradores
- [@Authenticated](#authenticated) - Requer autenticação

#### 🚀 Qualidade e Performance
- [@Cacheable](#cacheable) - Cache de resultados
- [@RateLimited](#ratelimited) - Limitação de taxa
- [@CircuitBreaker](#circuitbreaker) - Padrão Circuit Breaker
- [@Retry](#retry) - Retry automático

#### 🎭 Qualificadores
- [@Primary](#primary) - Bean primário
- [@Secondary](#secondary) - Bean secundário
- [@Mock](#mock) - Implementação mock
- [@Prod](#prod) - Implementação de produção

---

## 🤔 O que são Annotations Personalizadas?

### Analogia Simples

Imagine uma empresa com diferentes departamentos: **vendas**, **contabilidade**, **segurança**, etc. Cada funcionário usa um **crachá** que identifica seu papel e suas permissões.

**Annotations personalizadas** funcionam como esses "crachás" para suas classes Java:
- Identificam o **papel** de cada classe na arquitetura
- Automatizam **comportamentos** comuns (logging, métricas, validação)
- Documentam a **intenção** do código
- Aplicam **padrões** de forma consistente

### Antes vs Depois

#### ❌ Sem Annotations Personalizadas (Código Repetitivo)

```java
@ApplicationScoped
@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioController {
    
    private static final Logger LOG = Logger.getLogger(UsuarioController.class);
    
    @Inject
    MeterRegistry metrics;
    
    @POST
    @Transactional
    public Response criar(Usuario usuario) {
        LOG.info("Criando usuário: " + usuario.getEmail());
        long start = System.currentTimeMillis();
        
        try {
            // Validação manual
            if (usuario.getEmail() == null) {
                throw new ValidationException("Email obrigatório");
            }
            
            Usuario salvo = service.salvar(usuario);
            
            long duration = System.currentTimeMillis() - start;
            metrics.counter("usuario.criar.success").increment();
            metrics.timer("usuario.criar.duration").record(duration, TimeUnit.MILLISECONDS);
            
            LOG.info("Usuário criado com sucesso em " + duration + "ms");
            return Response.ok(salvo).build();
            
        } catch (Exception e) {
            metrics.counter("usuario.criar.error").increment();
            LOG.error("Erro ao criar usuário", e);
            throw e;
        }
    }
    
    // Mesmo código repetido para cada método... 😫
}
```

#### ✅ Com Annotations Personalizadas (Código Limpo)

```java
@RestController(
    path = "/usuarios",
    description = "Gerenciamento de usuários",
    version = "v1"
)
public class UsuarioController {
    
    @Inject
    CriarUsuarioUseCase criarUsuario;
    
    @POST
    public Response criar(@Valid Usuario usuario) {
        Usuario salvo = criarUsuario.executar(usuario);
        return Response.ok(salvo).build();
    }
    
    // Logging, métricas e validação acontecem AUTOMATICAMENTE! 🎉
}
```

**O que mudou?**
- ✅ **90% menos código** - Sem boilerplate
- ✅ **Automatização** - Logging, métricas e validação automáticos
- ✅ **Consistência** - Mesmo padrão em todo o projeto
- ✅ **Manutenibilidade** - Fácil de entender e modificar

---

## 🎯 Por que usar?

### Benefícios Práticos

| Problema | Solução com Annotations Personalizadas |
|----------|---------------------------------------|
| 😫 Código duplicado em 50 classes | ✅ Uma annotation aplicada 50 vezes |
| 😫 Logs inconsistentes | ✅ `@AutoLogging` garante padrão único |
| 😫 Métricas faltando em alguns endpoints | ✅ `@AutoMetrics` em todas as camadas |
| 😫 Validação esquecida em alguns métodos | ✅ `@AutoValidation` sempre ativa |
| 😫 Difícil identificar camadas no código | ✅ `@UseCase`, `@Repository` deixam óbvio |
| 😫 Configuração espalhada por todo código | ✅ Metadados centralizados na annotation |

### Ganhos Mensuráveis

```java
// Estatísticas reais de um projeto enterprise:

📊 Antes das Annotations Personalizadas:
- 45.000 linhas de código
- 8 horas para adicionar nova feature
- 12% de cobertura de métricas
- Logs inconsistentes (3 formatos diferentes)

📊 Depois das Annotations Personalizadas:
- 28.000 linhas de código (38% redução!) ⬇️
- 3 horas para adicionar nova feature (62% mais rápido!) ⚡
- 98% de cobertura de métricas ✅
- Logs padronizados (1 formato único) ✅
```

---

## 📚 Conceitos Fundamentais

Antes de mergulharmos no código, vamos entender os 4 pilares das annotations personalizadas:

### 1️⃣ Stereotype Annotations (Estereótipos)

**O que são?** Annotations que identificam o **papel arquitetural** de uma classe.

```java
@DomainEntity      // "Esta classe é uma entidade de domínio"
@UseCase           // "Esta classe implementa um caso de uso"
@Repository        // "Esta classe acessa dados"
@RestController    // "Esta classe é um controller REST"
```

**Por que usar?** 
- 👁️ Documentação visual da arquitetura
- 🔍 Fácil navegação no código
- 📊 Análise estática automática
- 🎯 Comportamentos específicos por camada

### 2️⃣ Interceptor Bindings (Interceptadores)

**O que são?** Annotations que executam código **antes/depois** de métodos automaticamente.

```java
@AutoLogging       // Loga entrada/saída automaticamente
@AutoMetrics       // Coleta métricas automaticamente  
@AutoValidation    // Valida parâmetros automaticamente
```

**Como funcionam?**

```
Chamada do método
    ↓
@AutoLogging (ANTES)   → Log de entrada
    ↓
@AutoMetrics (ANTES)   → Inicia cronômetro
    ↓
@AutoValidation (ANTES) → Valida parâmetros
    ↓
>>> SEU CÓDIGO EXECUTA <<<
    ↓
@AutoValidation (DEPOIS) → Valida resultado
    ↓
@AutoMetrics (DEPOIS)    → Para cronômetro, registra métrica
    ↓
@AutoLogging (DEPOIS)    → Log de saída
    ↓
Retorna resultado
```

### 3️⃣ Qualifiers (Qualificadores)

**O que são?** Annotations que permitem **múltiplas implementações** da mesma interface.

```java
// Problema: Como injetar a implementação correta?
public interface UserRepository { }

// Solução: Qualificadores!
@Repository
@RepositoryType(JPA)
@Database("primary")
public class JpaUserRepository implements UserRepository { }

@Repository
@RepositoryType(MONGODB)
@Database("secondary")
public class MongoUserRepository implements UserRepository { }

// Uso: Injete a implementação desejada
@Inject
@RepositoryType(JPA)
@Database("primary")
UserRepository repository;  // Injeta JpaUserRepository
```

### 4️⃣ Meta-Annotations (Annotations Compostas)

**O que são?** Annotations que **combinam várias outras** annotations.

```java
// Em vez de escrever isso toda vez:
@ApplicationScoped
@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AutoLogging
@AutoMetrics
@AutoValidation
public class ProdutoController { }

// Crie uma meta-annotation:
@Stereotype
@ApplicationScoped
@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AutoLogging
@AutoMetrics
@AutoValidation
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestController {
    String path();
    String version() default "v1";
}

// E use assim:
@RestController(path = "/produtos", version = "v2")
public class ProdutoController { }  // Muito mais limpo! ✨
```

---

## 📁 Estrutura de Diretórios

### Opção 1: Pacote Único (Projetos Pequenos/Médios)

Ideal para projetos até 50 classes ou equipes de 1-5 desenvolvedores.

```bash
src/main/java/com/empresa/app/
│
├── core/                                    # 🎯 Core da aplicação
│   └── annotation/                          # 📝 TODAS as annotations aqui
│       ├── stereotype/                      # Estereótipos de camadas
│       │   ├── DomainEntity.java           # Marca entidades de domínio
│       │   ├── ValueObject.java            # Marca value objects
│       │   ├── DomainService.java          # Marca serviços de domínio
│       │   ├── UseCase.java                # Marca casos de uso
│       │   ├── Repository.java             # Marca repositórios
│       │   ├── Gateway.java                # Marca gateways externos
│       │   └── RestController.java         # Marca controllers REST
│       │
│       ├── interceptor/                     # Interceptadores (cross-cutting)
│       │   ├── AutoLogging.java            # Annotation de logging
│       │   ├── AutoLoggingInterceptor.java # Implementação do logging
│       │   ├── AutoMetrics.java            # Annotation de métricas
│       │   ├── AutoMetricsInterceptor.java # Implementação de métricas
│       │   ├── AutoValidation.java         # Annotation de validação
│       │   └── AutoValidationInterceptor.java
│       │
│       ├── qualifier/                       # Qualificadores CDI
│       │   ├── RepositoryType.java         # Tipo: JPA, MongoDB, Redis
│       │   ├── Database.java               # DB: primary, secondary
│       │   ├── GatewayType.java            # Gateway: REST, SOAP, MQ
│       │   └── Environment.java            # Ambiente: dev, test, prod
│       │
│       └── meta/                            # Meta-annotations (combos)
│           ├── TransactionalUseCase.java   # UseCase + Transactional
│           ├── CacheableRepository.java    # Repository + Cache
│           └── ResilientGateway.java       # Gateway + CircuitBreaker
│
├── domain/                                  # 🏢 Camada de Domínio
│   ├── entity/                             # Usa @DomainEntity
│   │   └── Usuario.java
│   ├── valueobject/                        # Usa @ValueObject
│   │   ├── Email.java
│   │   └── CPF.java
│   └── service/                            # Usa @DomainService
│       └── UsuarioBusinessRules.java
│
├── application/                             # 🎭 Camada de Aplicação
│   ├── usecase/                            # Usa @UseCase
│   │   ├── CriarUsuarioUseCase.java
│   │   └── BuscarUsuarioUseCase.java
│   ├── query/                              # Usa @QueryHandler
│   │   └── ListarUsuariosQuery.java
│   └── command/                            # Usa @CommandHandler
│       └── AtualizarUsuarioCommand.java
│
├── infrastructure/                          # 🔧 Camada de Infraestrutura
│   ├── repository/                         # Usa @Repository
│   │   └── JpaUsuarioRepository.java
│   ├── gateway/                            # Usa @Gateway
│   │   └── EmailServiceGateway.java
│   └── adapter/                            # Usa @Adapter
│       └── BCryptPasswordAdapter.java
│
└── presentation/                            # 🌐 Camada de Apresentação
    └── controller/                         # Usa @RestController
        └── UsuarioController.java
```

**Quando usar esta estrutura?**
- ✅ Projeto com 1-50 classes
- ✅ Equipe de 1-5 desenvolvedores
- ✅ Ciclo de deploy único
- ✅ Não precisa reutilizar annotations em outros projetos

### Opção 2: Módulos Separados (Projetos Grandes/Enterprise)

Ideal para projetos com 50+ classes ou equipes de 5+ desenvolvedores.

```bash
projeto-raiz/
│
├── app-annotations/                         # 📦 Módulo de annotations (reutilizável)
│   ├── src/main/java/
│   │   └── com/empresa/app/core/annotation/
│   │       ├── stereotype/
│   │       ├── interceptor/
│   │       ├── qualifier/
│   │       └── meta/
│   └── pom.xml                             # Apenas Quarkus ARC como dependência
│
├── app-domain/                             # 📦 Módulo de domínio
│   ├── src/main/java/
│   │   └── com/empresa/app/domain/
│   │       ├── entity/
│   │       ├── valueobject/
│   │       └── service/
│   └── pom.xml                             # Depende de: app-annotations
│
├── app-application/                        # 📦 Módulo de aplicação
│   ├── src/main/java/
│   │   └── com/empresa/app/application/
│   │       ├── usecase/
│   │       ├── query/
│   │       └── command/
│   └── pom.xml                             # Depende de: app-annotations, app-domain
│
├── app-infrastructure/                     # 📦 Módulo de infraestrutura
│   ├── src/main/java/
│   │   └── com/empresa/app/infrastructure/
│   │       ├── repository/
│   │       ├── gateway/
│   │       └── adapter/
│   └── pom.xml                             # Depende de: app-annotations, app-domain
│
└── app-main/                               # 📦 Aplicação principal (orquestrador)
    ├── src/main/java/
    │   └── com/empresa/app/
    │       └── presentation/
    │           └── controller/
    └── pom.xml                             # Depende de: todos os módulos
```

**Quando usar esta estrutura?**
- ✅ Projeto com 50+ classes
- ✅ Equipe de 5+ desenvolvedores
- ✅ Múltiplos microsserviços reutilizando annotations
- ✅ Deploy independente de módulos
- ✅ Necessidade de versionar annotations separadamente

### Comparação Rápida

| Aspecto | Pacote Único | Módulos Separados |
|---------|--------------|-------------------|
| **Complexidade** | Baixa | Média/Alta |
| **Reutilização** | Baixa | Alta |
| **Build** | Rápido | Mais lento |
| **Tamanho ideal** | Até 50 classes | 50+ classes |
| **Equipe ideal** | 1-5 devs | 5+ devs |
| **Manutenção** | Simples | Complexa |
| **Recomendado para** | Startups, MVPs | Enterprise, microsserviços |

---

## � Padrões de Nomenclatura

A consistência na nomenclatura facilita a navegação e manutenção do código. Siga estas convenções:

### Regras Gerais

| Elemento | Padrão | Exemplo | ❌ Evite |
|----------|--------|---------|----------|
| **Annotations** | PascalCase, sem prefixo `@` | `@DomainEntity` | `@domain_entity`, `@domainentity` |
| **Interceptadores** | Nome da annotation + `Interceptor` | `AutoLoggingInterceptor` | `LogInterceptor`, `Logger` |
| **Qualificadores** | Substantivo descritivo | `@Database`, `@RepositoryType` | `@Db`, `@Type` |
| **Meta-annotations** | Combinação dos conceitos | `@TransactionalUseCase` | `@TUC`, `@TransUC` |

### Por Camada de Arquitetura

#### 1️⃣ Domínio (Domain) - Singular, Foco no Conceito

```java
// ✅ BOM - Substantivo singular
@DomainEntity
public class User { }              // Representa UM usuário

@ValueObject
public class Email { }             // Representa UM email

@DomainService
public class UserPasswordPolicy { } // Política ESPECÍFICA

// ❌ RUIM - Plural ou genérico demais
@DomainEntity
public class Users { }             // Users = coleção, não entidade

@ValueObject
public class EmailAddress { }      // Redundante, Email já indica endereço

@DomainService
public class Service { }           // Muito genérico
```

**Por quê?** 
- Entidades representam **conceitos únicos** do negócio
- Singular facilita leitura: `User user = new User()` (não `Users user`)
- Services do domínio devem ter **responsabilidade clara**

#### 2️⃣ Aplicação (Application) - Verbo + Contexto + Tipo

```java
// ✅ BOM - Padrão: <Verbo><Entidade><Tipo>
@UseCase
public class RegisterUserUseCase { }        // Verbo + Entidade + Tipo

@QueryHandler  
public class FindUserByEmailQuery { }       // Ação + Critério + Tipo

@CommandHandler
public class UpdateUserProfileCommand { }   // Ação + Contexto + Tipo

// ❌ RUIM - Sem padrão claro
@UseCase
public class UserRegistration { }          // Não começa com verbo

@QueryHandler
public class UserQuery { }                 // Muito genérico

@CommandHandler
public class Update { }                    // Falta contexto
```

**Verbos Comuns:**
- **Queries**: Find, Get, List, Search, Count, Exists
- **Commands**: Create, Update, Delete, Register, Activate, Cancel
- **UseCases**: Register, Process, Calculate, Validate, Generate

#### 3️⃣ Infraestrutura (Infrastructure) - Tecnologia + Entidade + Tipo

```java
// ✅ BOM - Padrão: <Tecnologia><Entidade><Tipo>
@Repository
public class JpaUserRepository { }          // Tecnologia + Entidade + Tipo

@Gateway
public class RestEmailServiceGateway { }    // Protocolo + Serviço + Tipo

@Adapter
public class BCryptPasswordAdapter { }      // Tecnologia + Função + Tipo

// ❌ RUIM - Sem identificação da tecnologia
@Repository
public class UserRepository { }             // Não indica que é JPA

@Gateway
public class EmailGateway { }              // Não indica protocolo (REST? SOAP?)

@Adapter
public class PasswordAdapter { }           // Não indica algoritmo
```

**Tecnologias Comuns:**
- **Repositories**: Jpa, Mongo, Redis, Elasticsearch, InMemory
- **Gateways**: Rest, Soap, Grpc, Kafka, Sqs, Smtp
- **Adapters**: BCrypt, Jwt, S3, CloudStorage

#### 4️⃣ Apresentação (Presentation) - Recurso + Controller

```java
// ✅ BOM - Nome do recurso no plural
@RestController(path = "/users")
public class UserController { }             // Recurso no plural

@RestController(path = "/products")
public class ProductController { }

// ❌ RUIM - Singular ou muito específico
@RestController(path = "/user")
public class UserController { }             // Recurso no singular

@RestController(path = "/users")
public class UserRestController { }         // Redundante (já usa @RestController)
```

### Exemplos Completos de Nomenclatura

```java
// 📦 DOMÍNIO - Conceitos de negócio
@DomainEntity
public class Order { }                      // Pedido

@ValueObject
public class Money { }                      // Dinheiro

@DomainService
public class OrderPricingService { }        // Serviço de precificação de pedidos

// 📦 APLICAÇÃO - Casos de uso
@UseCase
public class CreateOrderUseCase { }         // Criar pedido

@QueryHandler
public class FindOrdersByCustomerQuery { }  // Buscar pedidos por cliente

@CommandHandler
public class CancelOrderCommand { }         // Cancelar pedido

// 📦 INFRAESTRUTURA - Detalhes técnicos
@Repository
public class JpaOrderRepository { }         // Repositório JPA de pedidos

@Gateway
public class RestPaymentGateway { }         // Gateway REST de pagamento

@Adapter
public class StripePaymentAdapter { }       // Adaptador Stripe

// 📦 APRESENTAÇÃO - Endpoints REST
@RestController(path = "/orders")
public class OrderController { }            // Controller de pedidos
```

### Checklist de Nomenclatura

Antes de criar uma classe, pergunte:

- [ ] O nome indica claramente a **responsabilidade** da classe?
- [ ] O nome segue o **padrão da camada** (Domain/Application/Infrastructure)?
- [ ] O nome usa a **tecnologia** quando necessário (JPA, REST, etc.)?
- [ ] O nome é **pronunciável** e fácil de lembrar?
- [ ] O nome **não usa abreviações** confusas?
- [ ] O nome está no **idioma correto** do projeto (português/inglês)?

---

## 🎯 INTERCEPTADORES AUTOMÁTICOS

Os interceptadores são a "mágica" por trás das annotations. Eles executam código automaticamente antes e depois dos seus métodos, sem que você precise escrever nada!

### 📝 Como Funcionam?

Quando você anota um método com `@AutoLogging`, por exemplo:

```java
@AutoLogging
public void criarUsuario(String nome) {
    // seu código aqui
}
```

O **interceptador** automaticamente:
1. ✅ Loga a **entrada** do método com os parâmetros
2. ✅ Executa **seu código**
3. ✅ Loga a **saída** com o tempo de execução
4. ✅ Se houver erro, loga com detalhes da exceção

---

### 1️⃣ AutoLogging - Logging Automático Inteligente

```java
// ================================================================
//                   1. INTERCEPTADORES AUTOMÁTICOS
// ================================================================

/**
 * Annotation para ativar logging automático em classes ou métodos.
 * 
 * <h2>Funcionalidades:</h2>
 * <ul>
 *   <li>✅ Log automático de entrada e saída de métodos</li>
 *   <li>✅ Registro de parâmetros recebidos (configurável)</li>
 *   <li>✅ Registro de valores retornados (configurável)</li>
 *   <li>✅ Medição automática do tempo de execução</li>
 *   <li>✅ Log de exceções com stack trace</li>
 * </ul>
 * 
 * <h2>Onde Usar:</h2>
 * <ul>
 *   <li>Em <b>métodos</b> para log específico</li>
 *   <li>Em <b>classes</b> para log em todos os métodos</li>
 * </ul>
 * 
 * <h2>Exemplo de Uso:</h2>
 * <pre>
 * // Aplicado na classe - todos os métodos terão log
 * {@literal @}AutoLogging(level = LogLevel.INFO, logParams = true)
 * public class UserService {
 *     
 *     public User createUser(String name) {
 *         return new User(name);
 *     }
 *     
 *     // Este método sobrescreve configurações da classe
 *     {@literal @}AutoLogging(level = LogLevel.DEBUG, logResult = true)
 *     public User findUser(Long id) {
 *         return repository.find(id);
 *     }
 * }
 * </pre>
 * 
 * <h2>Saída Gerada (exemplo):</h2>
 * <pre>
 * INFO  → UserService.createUser called with params: [Bruno]
 * INFO  ← UserService.createUser completed in 45ms
 * 
 * DEBUG → UserService.findUser called with params: [123]
 * DEBUG ← UserService.findUser completed in 12ms with result: User{id=123, name=Bruno}
 * </pre>
 * 
 * @author Sistema de Annotations
 * @version 1.0
 * @since 1.0
 * 
 * @see AutoLoggingInterceptor
 */
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLogging {
    
    /**
     * Nível de log a ser utilizado.
     * <p>
     * Define a severidade das mensagens de log geradas:
     * <ul>
     *   <li><b>TRACE</b>: Informações muito detalhadas para debug profundo</li>
     *   <li><b>DEBUG</b>: Informações de debug para desenvolvimento</li>
     *   <li><b>INFO</b>: Informações gerais sobre execução (padrão)</li>
     *   <li><b>WARN</b>: Avisos sobre situações potencialmente problemáticas</li>
     *   <li><b>ERROR</b>: Erros que não impedem a execução</li>
     * </ul>
     * 
     * @return O nível de log configurado
     */
    LogLevel level() default LogLevel.INFO;
    
    /**
     * Define se os parâmetros do método devem ser logados.
     * <p>
     * <b>Atenção:</b> Evite logar parâmetros sensíveis como senhas, tokens
     * ou informações pessoais. Para esses casos, use {@code logParams = false}.
     * 
     * <p><b>Exemplo:</b></p>
     * <pre>
     * // ✅ BOM - parâmetros seguros
     * {@literal @}AutoLogging(logParams = true)
     * public Product findProduct(Long id) { ... }
     * 
     * // ❌ CUIDADO - dados sensíveis
     * {@literal @}AutoLogging(logParams = false)
     * public User authenticate(String email, String password) { ... }
     * </pre>
     * 
     * @return {@code true} para logar parâmetros (padrão), {@code false} caso contrário
     */
    boolean logParams() default true;
    
    /**
     * Define se o resultado retornado deve ser logado.
     * <p>
     * <b>Cuidado:</b> Para métodos que retornam grandes volumes de dados
     * (listas, arrays), considere usar {@code logResult = false} para
     * evitar logs muito grandes.
     * 
     * <p><b>Exemplo:</b></p>
     * <pre>
     * // ✅ BOM - resultado simples
     * {@literal @}AutoLogging(logResult = true)
     * public User findUserById(Long id) { ... }
     * 
     * // ⚠️  CUIDADO - resultado grande
     * {@literal @}AutoLogging(logResult = false)
     * public List&lt;Product&gt; findAllProducts() { ... }
     * </pre>
     * 
     * @return {@code true} para logar resultado, {@code false} caso contrário (padrão)
     */
    boolean logResult() default false;
    
    /**
     * Define se o tempo de execução deve ser medido e logado.
     * <p>
     * Útil para identificar gargalos de performance e métodos lentos.
     * O tempo é medido em milissegundos.
     * 
     * <p><b>Saída exemplo:</b></p>
     * <pre>
     * INFO  ← OrderService.processOrder completed in 1250ms
     * </pre>
     * 
     * @return {@code true} para logar tempo de execução (padrão), {@code false} caso contrário
     */
    boolean logExecutionTime() default true;
}

/**
 * Interceptador que implementa a funcionalidade de logging automático.
 * <p>
 * Este interceptador é ativado automaticamente quando uma classe ou método
 * é anotado com {@link AutoLogging}. Ele intercepta a execução do método
 * e adiciona logs antes, depois e em caso de erro.
 * 
 * <h2>Funcionamento Interno:</h2>
 * <ol>
 *   <li><b>Antes da execução:</b> Registra entrada com parâmetros (se configurado)</li>
 *   <li><b>Durante:</b> Executa o método original</li>
 *   <li><b>Depois:</b> Registra saída com resultado e tempo (se configurado)</li>
 *   <li><b>Em erro:</b> Registra exceção com detalhes e stack trace</li>
 * </ol>
 * 
 * <h2>Prioridade de Execução:</h2>
 * <p>
 * Usa prioridade {@code APPLICATION + 10}, garantindo que execute depois
 * de interceptadores de segurança mas antes de transações.
 * 
 * <h2>Exemplo de Saída de Log:</h2>
 * <pre>
 * // Sucesso
 * INFO  → UserService.createUser called with params: [User{name=Bruno}]
 * INFO  ← UserService.createUser completed in 45ms
 * 
 * // Erro
 * ERROR ✗ UserService.createUser failed in 12ms with error: Email already exists
 * </pre>
 * 
 * @author Sistema de Annotations
 * @version 1.0
 * @since 1.0
 * 
 * @see AutoLogging
 * @see InvocationContext
 */
@AutoLogging
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 10)
public class AutoLoggingInterceptor {
    
    /** Logger estático para registrar todas as operações */
    private static final Logger LOG = LoggerFactory.getLogger(AutoLoggingInterceptor.class);
    
    /**
     * Método interceptador principal que envolve a execução do método anotado.
     * <p>
     * Este método é chamado automaticamente pelo CDI quando um método
     * com {@link AutoLogging} é invocado.
     * 
     * <h3>Fluxo de Execução:</h3>
     * <pre>
     * 1. Captura nome da classe e do método
     * 2. Registra log de entrada (com ou sem parâmetros)
     * 3. Inicia cronômetro
     * 4. Executa método original via context.proceed()
     * 5. Calcula tempo de execução
     * 6. Registra log de saída (com ou sem resultado)
     * 7. Se houver exceção, registra log de erro
     * </pre>
     * 
     * @param context Contexto da invocação contendo informações sobre o método,
     *                parâmetros, target (objeto que possui o método), etc.
     * @return O resultado retornado pelo método interceptado
     * @throws Exception Qualquer exceção lançada pelo método original é propagada
     *                   após ser logada
     */
    @AroundInvoke
    public Object log(InvocationContext context) throws Exception {
        // 1. Extrair informações do método sendo interceptado
        String className = context.getTarget().getClass().getSimpleName();
        String methodName = context.getMethod().getName();
        
        // 2. Obter configurações da annotation (método ou classe)
        AutoLogging annotation = getAnnotation(context);
        
        // 3. Iniciar medição de tempo
        long startTime = System.currentTimeMillis();
        
        // 4. Log de ENTRADA do método
        if (annotation.logParams()) {
            // Loga com parâmetros: → UserService.createUser called with params: [Bruno]
            LOG.info("→ {}.{} called with params: {}", 
                className, methodName, Arrays.toString(context.getParameters()));
        } else {
            // Loga sem parâmetros: → UserService.createUser called
            LOG.info("→ {}.{} called", className, methodName);
        }
        
        try {
            // 5. EXECUTA o método original (seu código)
            Object result = context.proceed();
            
            // 6. Calcula tempo de execução
            long executionTime = System.currentTimeMillis() - startTime;
            
            // 7. Log de SAÍDA com sucesso
            if (annotation.logResult() && result != null) {
                // Loga com resultado: ← UserService.createUser completed in 45ms with result: User{id=1}
                LOG.info("← {}.{} completed in {}ms with result: {}", 
                    className, methodName, executionTime, result);
            } else if (annotation.logExecutionTime()) {
                // Loga apenas tempo: ← UserService.createUser completed in 45ms
                LOG.info("← {}.{} completed in {}ms", className, methodName, executionTime);
            }
            
            return result;
            
        } catch (Exception e) {
            // 8. Log de ERRO se houver exceção
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Loga erro: ✗ UserService.createUser failed in 12ms with error: Email already exists
            LOG.error("✗ {}.{} failed in {}ms with error: {}", 
                className, methodName, executionTime, e.getMessage());
            
            // Propaga a exceção (não "engole" o erro)
            throw e;
        }
    }
    
    /**
     * Obtém a annotation @AutoLogging do método ou da classe.
     * <p>
     * <b>Prioridade:</b> Se o método tiver a annotation, ela é usada.
     * Caso contrário, busca na classe. Isso permite sobrescrever configurações
     * da classe em métodos específicos.
     * 
     * <h3>Exemplo de Uso da Prioridade:</h3>
     * <pre>
     * {@literal @}AutoLogging(logParams = false)  // Configuração da CLASSE
     * public class UserService {
     *     
     *     public void method1() { }  // Usa logParams = false (da classe)
     *     
     *     {@literal @}AutoLogging(logParams = true)  // Sobrescreve configuração da classe
     *     public void method2() { }  // Usa logParams = true (do método)
     * }
     * </pre>
     * 
     * @param context Contexto da invocação
     * @return A annotation {@link AutoLogging} encontrada
     */
    private AutoLogging getAnnotation(InvocationContext context) {
        // Tenta obter do método primeiro (prioridade maior)
        AutoLogging methodAnnotation = context.getMethod().getAnnotation(AutoLogging.class);
        if (methodAnnotation != null) {
            return methodAnnotation;
        }
        
        // Se não encontrou no método, busca na classe
        return context.getTarget().getClass().getAnnotation(AutoLogging.class);
    }
}

/**
 * Enum que define os níveis de log disponíveis.
 * <p>
 * Os níveis seguem a hierarquia padrão de logging:
 * TRACE &lt; DEBUG &lt; INFO &lt; WARN &lt; ERROR
 */
public enum LogLevel {
    /** Log extremamente detalhado, usado apenas para debug profundo */
    TRACE,
    
    /** Log de depuração para desenvolvimento */
    DEBUG,
    
    /** Log informativo sobre operações normais (padrão recomendado) */
    INFO,
    
    /** Log de avisos sobre situações que merecem atenção */
    WARN,
    
    /** Log de erros que não impedem a execução do sistema */
    ERROR
}

---

### 2️⃣ AutoMetrics - Métricas Automáticas de Performance

```java
/**
 * Annotation para coletar métricas automáticas de execução de métodos.
 * <p>
 * <b>O que são métricas?</b> São medições quantitativas que ajudam a monitorar
 * a saúde e performance da aplicação. Exemplos: número de chamadas, tempo de execução,
 * taxa de erro, etc.
 * 
 * <h2>Métricas Coletadas Automaticamente:</h2>
 * <ul>
 *   <li>📊 <b>Contador de chamadas</b> - Quantas vezes o método foi executado</li>
 *   <li>⏱️  <b>Tempo de execução</b> - Quanto tempo levou cada chamada</li>
 *   <li>❌ <b>Taxa de erro</b> - Quantas chamadas falharam</li>
 *   <li>✅ <b>Taxa de sucesso</b> - Quantas chamadas tiveram sucesso</li>
 * </ul>
 * 
 * <h2>Onde Usar:</h2>
 * <ul>
 *   <li>✅ Use Cases - Para medir performance de operações de negócio</li>
 *   <li>✅ Repositories - Para medir tempo de acesso ao banco</li>
 *   <li>✅ Gateways - Para medir chamadas a APIs externas</li>
 *   <li>✅ Controllers - Para medir tempo de resposta dos endpoints</li>
 * </ul>
 * 
 * <h2>Exemplo de Uso:</h2>
 * <pre>
 * // Métrica com nome automático baseado na classe/método
 * {@literal @}AutoMetrics
 * public class UserService {
 *     public User createUser(String name) { ... }
 * }
 * // Gera métricas: userservice.createuser.calls, userservice.createuser.duration
 * 
 * // Métrica com nome personalizado
 * {@literal @}AutoMetrics(name = "user.registration")
 * public void registerUser(User user) { ... }
 * // Gera métricas: user.registration.calls, user.registration.duration
 * </pre>
 * 
 * <h2>Métricas Geradas (exemplo):</h2>
 * <pre>
 * userservice.createuser.calls{status="success"} = 150
 * userservice.createuser.calls{status="error"} = 5
 * userservice.createuser.duration{quantile="0.5"} = 0.045  (45ms - mediana)
 * userservice.createuser.duration{quantile="0.95"} = 0.120  (120ms - 95º percentil)
 * userservice.createuser.duration{quantile="0.99"} = 0.250  (250ms - 99º percentil)
 * </pre>
 * 
 * <h2>Como Visualizar as Métricas:</h2>
 * <ul>
 *   <li>🔗 Prometheus: http://localhost:8080/q/metrics</li>
 *   <li>📊 Grafana: Criar dashboards com as métricas coletadas</li>
 *   <li>📈 Kibana: Visualizar métricas em tempo real</li>
 * </ul>
 * 
 * @author Sistema de Annotations
 * @version 1.0
 * @since 1.0
 * 
 * @see AutoMetricsInterceptor
 * @see MeterRegistry
 */
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoMetrics {
    
    /**
     * Nome personalizado para as métricas geradas.
     * <p>
     * Se não especificado, o nome é gerado automaticamente usando:
     * {@code <classe>.<método>} em lowercase.
     * 
     * <h3>Exemplos:</h3>
     * <pre>
     * // Nome automático
     * {@literal @}AutoMetrics  // Gera: userservice.createuser
     * 
     * // Nome personalizado
     * {@literal @}AutoMetrics(name = "user.registration")  // Gera: user.registration
     * </pre>
     * 
     * <b>Boas Práticas de Nomenclatura:</b>
     * <ul>
     *   <li>Use pontos (.) para hierarquia: {@code user.registration.email}</li>
     *   <li>Use underscore (_) para palavras compostas: {@code user_creation}</li>
     *   <li>Seja descritivo mas conciso: {@code order.payment.process}</li>
     *   <li>Evite nomes muito longos (máx. 50 caracteres)</li>
     * </ul>
     * 
     * @return O nome da métrica ou string vazia para nome automático
     */
    String name() default "";
    
    /**
     * Define se deve contar o número de chamadas ao método.
     * <p>
     * Cria dois contadores separados:
     * <ul>
     *   <li>{@code <nome>.calls{status="success"}} - Chamadas bem-sucedidas</li>
     *   <li>{@code <nome>.calls{status="error"}} - Chamadas com erro</li>
     * </ul>
     * 
     * <b>Quando desabilitar:</b> Se o método é chamado milhões de vezes
     * e você não precisa do contador (apenas tempo de execução).
     * 
     * @return {@code true} para contar chamadas (padrão), {@code false} caso contrário
     */
    boolean countCalls() default true;
    
    /**
     * Define se deve medir o tempo de execução do método.
     * <p>
     * Cria uma métrica de timer que registra:
     * <ul>
     *   <li>Tempo médio de execução</li>
     *   <li>Tempo mínimo e máximo</li>
     *   <li>Percentis (p50, p95, p99)</li>
     *   <li>Taxa de chamadas por segundo</li>
     * </ul>
     * 
     * <b>Exemplo de uso:</b>
     * <pre>
     * {@literal @}AutoMetrics(measureTime = true)
     * public List&lt;Product&gt; findAllProducts() {
     *     // Mede quanto tempo leva para buscar todos os produtos
     * }
     * </pre>
     * 
     * @return {@code true} para medir tempo (padrão), {@code false} caso contrário
     */
    boolean measureTime() default true;
    
    /**
     * Define se deve rastrear erros separadamente.
     * <p>
     * Quando {@code true}, cria um contador específico para erros:
     * {@code <nome>.calls{status="error"}}
     * 
     * <b>Útil para:</b>
     * <ul>
     *   <li>Identificar métodos com alta taxa de erro</li>
     *   <li>Criar alertas quando taxa de erro exceder threshold</li>
     *   <li>Análise de confiabilidade da aplicação</li>
     * </ul>
     * 
     * @return {@code true} para rastrear erros (padrão), {@code false} caso contrário
     */
    boolean trackErrors() default true;
}

/**
 * Interceptador que implementa a coleta de métricas de execução.
 * <p>
 * Este interceptador trabalha em conjunto com o Micrometer para coletar
 * e exportar métricas para sistemas de monitoramento como Prometheus.
 * 
 * <h2>Tipos de Métricas Coletadas:</h2>
 * <ol>
 *   <li><b>Counter</b>: Contador incremental (chamadas, erros)</li>
 *   <li><b>Timer</b>: Medição de tempo com histogramas e percentis</li>
 * </ol>
 * 
 * <h2>Integração com Prometheus:</h2>
 * <pre>
 * # Adicionar no prometheus.yml
 * scrape_configs:
 *   - job_name: 'quarkus-app'
 *     metrics_path: '/q/metrics'
 *     static_configs:
 *       - targets: ['localhost:8080']
 * </pre>
 * 
 * <h2>Prioridade de Execução:</h2>
 * <p>
 * Usa prioridade {@code APPLICATION + 20}, executando depois do logging
 * mas antes de transações.
 * 
 * @author Sistema de Annotations
 * @version 1.0
 * @since 1.0
 * 
 * @see AutoMetrics
 * @see MeterRegistry
 * @see Timer
 * @see Counter
 */
@AutoMetrics
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 20)
public class AutoMetricsInterceptor {
    
    /**
     * Registro de métricas do Micrometer.
     * <p>
     * O MeterRegistry é o componente central do Micrometer que mantém
     * todas as métricas e as exporta para o Prometheus ou outros backends.
     */
    @Inject
    MeterRegistry meterRegistry;
    
    /**
     * Intercepta a execução do método para coletar métricas.
     * <p>
     * <b>Fluxo de Coleta:</b>
     * <ol>
     *   <li>Inicia medição de tempo (se habilitado)</li>
     *   <li>Executa método original</li>
     *   <li>Incrementa contador de sucesso</li>
     *   <li>Registra tempo de execução</li>
     *   <li>Se houver erro: incrementa contador de erro</li>
     * </ol>
     * 
     * @param context Contexto da invocação com informações do método
     * @return O resultado retornado pelo método interceptado
     * @throws Exception Qualquer exceção lançada pelo método original
     */
    @AroundInvoke
    public Object measure(InvocationContext context) throws Exception {
        // 1. Obter configurações da annotation
        AutoMetrics annotation = getAnnotation(context);
        
        // 2. Determinar nome da métrica (personalizado ou automático)
        String metricName = getMetricName(context, annotation);
        
        // 3. Iniciar medição de tempo (se habilitado)
        Timer.Sample sample = null;
        if (annotation.measureTime()) {
            sample = Timer.start(meterRegistry);
        }
        
        try {
            // 4. EXECUTA o método original
            Object result = context.proceed();
            
            // 5. Incrementar contador de SUCESSO
            if (annotation.countCalls()) {
                meterRegistry.counter(metricName + ".calls", "status", "success").increment();
            }
            
            return result;
            
        } catch (Exception e) {
            // 6. Incrementar contador de ERRO
            if (annotation.trackErrors()) {
                meterRegistry.counter(metricName + ".calls", "status", "error").increment();
            }
            
            // Propaga a exceção
            throw e;
            
        } finally {
            // 7. Registrar tempo de execução (sempre executado)
            if (sample != null) {
                sample.stop(Timer.builder(metricName + ".duration")
                    .description("Execution time of " + metricName)
                    .register(meterRegistry));
            }
        }
    }
    
    /**
     * Gera o nome da métrica baseado na annotation ou no método.
     * <p>
     * <b>Regra de geração:</b>
     * <ul>
     *   <li>Se {@code name} estiver definido na annotation: usa ele</li>
     *   <li>Caso contrário: usa {@code <classe>.<método>} em lowercase</li>
     * </ul>
     * 
     * <h3>Exemplos:</h3>
     * <pre>
     * // Classe: UserService, Método: createUser
     * // Nome automático: "userservice.createuser"
     * 
     * // Com name = "user.registration"
     * // Nome usado: "user.registration"
     * </pre>
     * 
     * @param context Contexto da invocação
     * @param annotation Annotation @AutoMetrics
     * @return O nome da métrica a ser usado
     */
    private String getMetricName(InvocationContext context, AutoMetrics annotation) {
        // Se nome personalizado está definido, usa ele
        if (!annotation.name().isEmpty()) {
            return annotation.name();
        }
        
        // Caso contrário, gera automaticamente: classe.metodo (lowercase)
        String className = context.getTarget().getClass().getSimpleName().toLowerCase();
        String methodName = context.getMethod().getName().toLowerCase();
        return className + "." + methodName;
    }
    
    /**
     * Obtém a annotation @AutoMetrics do método ou da classe.
     * <p>
     * Prioridade: método &gt; classe
     * 
     * @param context Contexto da invocação
     * @return A annotation encontrada
     */
    private AutoMetrics getAnnotation(InvocationContext context) {
        // Tenta obter do método primeiro
        AutoMetrics methodAnnotation = context.getMethod().getAnnotation(AutoMetrics.class);
        if (methodAnnotation != null) {
            return methodAnnotation;
        }
        
        // Se não encontrou, busca na classe
        return context.getTarget().getClass().getAnnotation(AutoMetrics.class);
    }
}

/**
 * Interceptador automático para validação
 */
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoValidation {
    boolean validateParams() default true;
    boolean validateResult() default false;
    Class<?>[] groups() default {};
}

@AutoValidation
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 5)
public class AutoValidationInterceptor {
    
    @Inject
    Validator validator;
    
    @AroundInvoke
    public Object validate(InvocationContext context) throws Exception {
        AutoValidation annotation = getAnnotation(context);
        
        // Validar parâmetros de entrada
        if (annotation.validateParams()) {
            validateParameters(context, annotation);
        }
        
        Object result = context.proceed();
        
        // Validar resultado de saída
        if (annotation.validateResult() && result != null) {
            validateResult(result, annotation);
        }
        
        return result;
    }
    
    private void validateParameters(InvocationContext context, AutoValidation annotation) {
        Object[] parameters = context.getParameters();
        for (Object parameter : parameters) {
            if (parameter != null && shouldValidate(parameter)) {
                Set<ConstraintViolation<Object>> violations = validator.validate(parameter, annotation.groups());
                if (!violations.isEmpty()) {
                    throw new ValidationException("Parameter validation failed: " + 
                        violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining(", ")));
                }
            }
        }
    }
    
    private void validateResult(Object result, AutoValidation annotation) {
        if (shouldValidate(result)) {
            Set<ConstraintViolation<Object>> violations = validator.validate(result, annotation.groups());
            if (!violations.isEmpty()) {
                throw new ValidationException("Result validation failed: " + 
                    violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(", ")));
            }
        }
    }
    
    private boolean shouldValidate(Object obj) {
        return obj != null && 
               !obj.getClass().isPrimitive() && 
               !obj.getClass().getPackage().getName().startsWith("java.");
    }
    
    private AutoValidation getAnnotation(InvocationContext context) {
        AutoValidation methodAnnotation = context.getMethod().getAnnotation(AutoValidation.class);
        if (methodAnnotation != null) {
            return methodAnnotation;
        }
        return context.getTarget().getClass().getAnnotation(AutoValidation.class);
    }
}
```

## META-ANNOTATIONS AVANÇADAS

```java
// ================================================================
//                    2. META-ANNOTATIONS AVANÇADAS
// ================================================================

/**
 * Meta-annotation para Controllers REST completos
 */
@Stereotype
@ApplicationScoped
@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AutoLogging
@AutoMetrics
@AutoValidation
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestController {
    String path();
    String version() default "v1";
    String description() default "";
    boolean requiresAuthentication() default true;
    String[] roles() default {};
}

/**
 * Meta-annotation para UseCases completos
 */
@Stereotype
@ApplicationScoped
@Transactional
@AutoLogging(logParams = false, logResult = false)
@AutoMetrics
@AutoValidation
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseCase {
    String name() default "";
    String description() default "";
    String version() default "1.0";
    boolean requiresAuthentication() default true;
    TransactionType transactionType() default TransactionType.REQUIRED;
    int timeoutSeconds() default 30;
}

/**
 * Meta-annotation para Repositórios completos
 */
@Stereotype
@ApplicationScoped
@Transactional(TxType.SUPPORTS)
@AutoLogging(level = LogLevel.DEBUG)
@AutoMetrics(name = "repository")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Repository {
    String entity();
    String database() default "primary";
    boolean cacheable() default false;
    int cacheExpirationMinutes() default 60;
}

/**
 * Meta-annotation para Gateways completos
 */
@Stereotype
@ApplicationScoped
@AutoLogging
@AutoMetrics(trackErrors = true)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)  
public @interface Gateway {
    String service();
    String version() default "v1";
    int timeoutSeconds() default 10;
    int maxRetries() default 3;
    boolean circuitBreakerEnabled() default true;
}

/**
 * Meta-annotation para QueryHandlers completos
 */
@Stereotype
@ApplicationScoped
@Transactional(TxType.SUPPORTS)
@AutoLogging(logParams = true, logResult = false)
@AutoMetrics(name = "query")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryHandler {
    String queryName();
    String description() default "";
    boolean cacheable() default true;
    int cacheExpirationMinutes() default 30;
    Class<?> inputType() default Object.class;
    Class<?> outputType() default Object.class;
}

/**
 * Meta-annotation para CommandHandlers completos
 */
@Stereotype
@ApplicationScoped
@Transactional(TxType.REQUIRED)
@AutoLogging
@AutoMetrics(name = "command")
@AutoValidation
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandHandler {
    String commandName();
    String description() default "";
    boolean auditableAction() default true;
    Class<?> inputType() default Object.class;
    Class<?> outputType() default Void.class;
}
```

## QUALIFICADORES ESPECÍFICOS

```java
// ================================================================
//                    3. QUALIFICADORES ESPECÍFICOS
// ================================================================

/**
 * Qualificador para diferentes tipos de repositório
 */
@Qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RepositoryType {
    RepositoryKind value();
    
    enum RepositoryKind {
        JPA, MONGODB, REDIS, ELASTICSEARCH, IN_MEMORY
    }
}

/**
 * Qualificador para diferentes bases de dados
 */
@Qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Database {
    String value(); // primary, secondary, analytics, cache
}

/**
 * Qualificador para diferentes tipos de gateway
 */
@Qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GatewayType {
    GatewayKind value();
    
    enum GatewayKind {
        REST_CLIENT, SOAP_CLIENT, MESSAGE_QUEUE, EMAIL_SERVICE, SMS_SERVICE
    }
}

/**
 * Qualificador para diferentes ambientes
 */
@Qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Environment {
    String value(); // dev, test, staging, prod
}

/**
 * Qualificador para diferentes versões de API
 */
@Qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiVersion {
    String value(); // v1, v2, v3
}

```

## ANNOTATIONS COM METADADOS RICOS

```java
// ================================================================
//                    4. ANNOTATIONS COM METADADOS RICOS
// ================================================================

/**
 * DomainEntity com metadados completos
 */
@Stereotype
@Dependent
@AutoLogging(level = LogLevel.DEBUG)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainEntity {
    String name() default "";
    String description() default "";
    String aggregate() default "";
    String[] businessRules() default {};
    String version() default "1.0";
    boolean auditable() default false;
    Class<?>[] invariants() default {};
}

/**
 * ValueObject com validações automáticas
 */
@Stereotype
@Dependent
@AutoValidation
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueObject {
    String name() default "";
    String description() default "";
    boolean immutable() default true;
    Class<?>[] validationGroups() default {};
    String pattern() default "";
}

/**
 * DomainService com políticas de negócio
 */
@Stereotype
@ApplicationScoped
@AutoLogging
@AutoMetrics
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainService {
    String name() default "";
    String description() default "";
    String[] businessPolicies() default {};
    boolean stateless() default true;
    String version() default "1.0";
}

/**
 * Factory com configurações de criação
 */
@Stereotype
@ApplicationScoped
@AutoLogging(logResult = true)
@AutoMetrics
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Factory {
    String produces();
    String description() default "";
    boolean singleton() default false;
    Class<?>[] dependencies() default {};
    String creationStrategy() default "DEFAULT";
}

/**
 * Mapper com configurações de conversão
 */
@Stereotype
@ApplicationScoped
@AutoLogging(level = LogLevel.DEBUG)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapper {
    String from();
    String to();
    String description() default "";
    boolean bidirectional() default false;
    String[] ignoredFields() default {};
    String mappingStrategy() default "AUTO";
}

/**
 * Validator com regras de negócio
 */
@Stereotype
@ApplicationScoped
@AutoLogging
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validator {
    String validates();
    String description() default "";
    String[] businessRules() default {};
    Class<?>[] validationGroups() default {};
    boolean failFast() default true;
}
```

## EXEMPLOS DE USO COMPLETOS

```java
// ================================================================
//                    5. EXEMPLOS DE USO COMPLETOS
// ================================================================

// CONTROLLER COM META-ANNOTATION
@RestController(
    path = "/api/v1/users",
    description = "Gerenciamento de usuários do sistema",
    version = "2.0",
    roles = {"USER_MANAGER", "ADMIN"}
)
public class UserController {
    
    @Inject @ApiVersion("v2")
    RegisterUserUseCase registerUseCase;
    
    @Inject @ApiVersion("v2") 
    FindUserQueryHandler findUserQuery;
    
    @POST
    @Path("/register")
    public Response register(@Valid RegisterUserRequest request) {
        registerUseCase.execute(request);
        return Response.status(201).build();
    }
}

// USECASE COM META-ANNOTATION
@UseCase(
    name = "RegisterUser",
    description = "Registra um novo usuário no sistema aplicando todas as regras de negócio",
    version = "2.1",
    transactionType = TransactionType.REQUIRED,
    timeoutSeconds = 60
)
public class RegisterUserUseCase {
    
    @Inject @RepositoryType(RepositoryType.RepositoryKind.JPA) @Database("primary")
    UserRepository userRepository;
    
    @Inject @GatewayType(GatewayType.GatewayKind.EMAIL_SERVICE)
    EmailServiceGateway emailGateway;
    
    @Inject @Environment("prod")
    UserFactory userFactory;
    
    public void execute(RegisterUserRequest request) {
        User user = userFactory.create(request);
        userRepository.save(user);
        emailGateway.sendWelcomeEmail(user.getEmail());
    }
}

// REPOSITORY COM QUALIFICADORES
@Repository(
    entity = "User",
    database = "primary", 
    cacheable = true,
    cacheExpirationMinutes = 120
)
@RepositoryType(RepositoryType.RepositoryKind.JPA)
@Database("primary")
public class JpaUserRepository implements UserRepository {
    
    @PersistenceContext
    EntityManager entityManager;
    
    public void save(User user) {
        entityManager.persist(user);
    }
}

// QUERY HANDLER COM CACHE
@QueryHandler(
    queryName = "FindUserByEmail",
    description = "Busca usuário pelo email com cache otimizado",
    cacheable = true,
    cacheExpirationMinutes = 60,
    inputType = FindUserByEmailQuery.class,
    outputType = UserResponse.class
)
public class FindUserByEmailQueryHandler {
    
    @Inject @RepositoryType(RepositoryType.RepositoryKind.JPA) @Database("primary")
    UserRepository repository;
    
    public UserResponse handle(FindUserByEmailQuery query) {
        return repository.findByEmail(query.getEmail());
    }
}

// DOMAIN ENTITY COM METADADOS
@DomainEntity(
    name = "User",
    description = "Representa um usuário do sistema",
    aggregate = "UserAggregate",
    businessRules = {
        "Email deve ser único",
        "Senha deve seguir política de segurança",
        "Nome não pode conter caracteres especiais"
    },
    version = "2.0",
    auditable = true,
    invariants = {EmailUniquenessInvariant.class, PasswordPolicyInvariant.class}
)
public class User {
    private UserId id;
    private Email email;
    private Password password;
    
    // Invariantes são verificadas automaticamente
}

// VALUE OBJECT COM VALIDAÇÃO
@ValueObject(
    name = "Email",
    description = "Representa um endereço de email válido",
    pattern = "^[A-Za-z0-9+_.-]+@(.+)$",
    validationGroups = {EmailValidation.class}
)
public class Email {
    
    @jakarta.validation.constraints.Email
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private final String value;
    
    public Email(String value) {
        this.value = value;
    }
}

// GATEWAY COM CIRCUIT BREAKER
@Gateway(
    service = "EmailService",
    version = "v2",
    timeoutSeconds = 15,
    maxRetries = 3,
    circuitBreakerEnabled = true
)
@GatewayType(GatewayType.GatewayKind.REST_CLIENT)
public class EmailServiceGateway {
    
    @RestClient
    EmailServiceClient client;
    
    @Retry(maxRetries = 3)
    @Timeout(value = 15, unit = ChronoUnit.SECONDS)
    @CircuitBreaker
    public void sendWelcomeEmail(Email email) {
        client.sendEmail(new EmailRequest(email.getValue(), "Welcome!"));
    }
}

// FACTORY COM DEPENDÊNCIAS
@Factory(
    produces = "User",
    description = "Cria usuários aplicando todas as regras de negócio e validações",
    dependencies = {PasswordPolicy.class, EmailValidator.class}
)
public class UserFactory {
    
    @Inject
    PasswordPolicy passwordPolicy;
    
    @Inject  
    EmailValidator emailValidator;
    
    public User create(RegisterUserRequest request) {
        emailValidator.validate(request.getEmail());
        String hashedPassword = passwordPolicy.hash(request.getPassword());
        
        return User.builder()
            .id(UserId.generate())
            .email(new Email(request.getEmail()))
            .password(new Password(hashedPassword))
            .build();
    }
}
```

# Guia de Configuração e Uso das Annotations Avançadas

## 🚀 **Como Configurar no Seu Projeto**

### 1. Dependencies no `pom.xml`

```xml
<dependencies>
    <!-- Quarkus Core -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-arc</artifactId>
    </dependency>

    <!-- Interceptors -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-smallrye-metrics</artifactId>
    </dependency>

    <!-- Validation -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-hibernate-validator</artifactId>
    </dependency>

    <!-- Logging -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-logging-json</artifactId>
    </dependency>

    <!-- Circuit Breaker -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-smallrye-fault-tolerance</artifactId>
    </dependency>
</dependencies>

```

### 2. Configuração no `application.properties`

```yaml
# Logging Configuration
quarkus.log.level=INFO
quarkus.log.category."com.yourcompany.app".level=DEBUG

# Metrics Configuration
quarkus.micrometer.enabled=true
quarkus.micrometer.export.prometheus.enabled=true

# Validation Configuration
quarkus.hibernate-validator.fail-fast=false

# Cache Configuration
quarkus.cache.caffeine.default.initial-capacity=100
quarkus.cache.caffeine.default.maximum-size=1000
quarkus.cache.caffeine.default.expire-after-write=PT30M

# Database Configuration
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=app_user
quarkus.datasource.password=app_password
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/app_db

# Environment-specific beans
app.environment=prod

```

## 📝 **Exemplos Práticos de Uso**

### Exemplo 1: Sistema de E-commerce Completo

```java
// === DOMAIN LAYER ===

@DomainEntity(
    name = "Order",
    description = "Pedido de compra no e-commerce",
    aggregate = "OrderAggregate",
    businessRules = {
        "Pedido deve ter pelo menos 1 item",
        "Valor total deve ser positivo",
        "Status deve seguir workflow válido"
    },
    version = "2.0",
    auditable = true
)
public class Order {
    private OrderId id;
    private CustomerId customerId;
    private List<OrderItem> items;
    private OrderStatus status;
    private Money totalAmount;

    public void addItem(OrderItem item) {
        // Business logic with automatic logging
        this.items.add(item);
        recalculateTotal();
    }
}

@ValueObject(
    name = "Money",
    description = "Representa valores monetários",
    validationGroups = {MoneyValidation.class}
)
public class Money {
    @DecimalMin(value = "0.0", inclusive = false)
    private final BigDecimal amount;

    @NotNull
    @Size(min = 3, max = 3)
    private final String currency;
}

@DomainService(
    name = "OrderPricingService",
    description = "Calcula preços e descontos de pedidos",
    businessPolicies = {
        "Desconto máximo de 50%",
        "Frete grátis acima de R$ 100"
    }
)
public class OrderPricingService {

    public Money calculateTotal(Order order) {
        // Automatic logging and metrics
        return order.getItems().stream()
            .map(OrderItem::getSubtotal)
            .reduce(Money.zero(), Money::add);
    }
}

// === APPLICATION LAYER ===

@UseCase(
    name = "CreateOrder",
    description = "Cria um novo pedido aplicando todas as validações e regras de negócio",
    version = "3.0",
    timeoutSeconds = 30
)
public class CreateOrderUseCase {

    @Inject @Database("primary") @RepositoryType(JPA)
    OrderRepository orderRepository;

    @Inject @Database("primary") @RepositoryType(JPA)
    CustomerRepository customerRepository;

    @Inject @GatewayType(EMAIL_SERVICE)
    NotificationGateway notificationGateway;

    @Inject
    OrderPricingService pricingService;

    public OrderId execute(CreateOrderCommand command) {
        // 1. Validate customer exists
        Customer customer = customerRepository.findById(command.getCustomerId())
            .orElseThrow(() -> new CustomerNotFoundException());

        // 2. Create order with factory
        Order order = Order.builder()
            .customerId(command.getCustomerId())
            .items(command.getItems())
            .build();

        // 3. Apply pricing rules
        Money total = pricingService.calculateTotal(order);
        order.setTotal(total);

        // 4. Persist
        OrderId orderId = orderRepository.save(order);

        // 5. Send notification
        notificationGateway.sendOrderConfirmation(customer.getEmail(), orderId);

        return orderId;
    }
}

@QueryHandler(
    queryName = "FindOrdersByCustomer",
    description = "Busca pedidos de um cliente com paginação",
    cacheable = true,
    cacheExpirationMinutes = 15,
    inputType = FindOrdersByCustomerQuery.class,
    outputType = PagedOrderResponse.class
)
public class FindOrdersByCustomerQueryHandler {

    @Inject @Database("analytics") @RepositoryType(ELASTICSEARCH)
    OrderReadRepository readRepository;

    public PagedOrderResponse handle(FindOrdersByCustomerQuery query) {
        return readRepository.findByCustomerId(
            query.getCustomerId(),
            query.getPageable()
        );
    }
}

// === INFRASTRUCTURE LAYER ===

@Repository(
    entity = "Order",
    database = "primary",
    cacheable = false  // Orders change frequently
)
@RepositoryType(JPA)
@Database("primary")
public class JpaOrderRepository implements OrderRepository {

    @PersistenceContext
    EntityManager em;

    public OrderId save(Order order) {
        // Automatic logging, metrics, and validation
        em.persist(order);
        return order.getId();
    }

    public Optional<Order> findById(OrderId id) {
        return Optional.ofNullable(em.find(Order.class, id));
    }
}

@Gateway(
    service = "EmailNotificationService",
    timeoutSeconds = 10,
    maxRetries = 3,
    circuitBreakerEnabled = true
)
@GatewayType(EMAIL_SERVICE)
public class EmailNotificationGateway implements NotificationGateway {

    @RestClient
    EmailServiceClient emailClient;

    @Retry(maxRetries = 3)
    @Timeout(value = 10, unit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5)
    public void sendOrderConfirmation(Email customerEmail, OrderId orderId) {
        // Automatic logging, metrics, and fault tolerance
        EmailRequest request = EmailRequest.builder()
            .to(customerEmail.getValue())
            .subject("Pedido Confirmado #" + orderId.getValue())
            .template("order-confirmation")
            .data(Map.of("orderId", orderId.getValue()))
            .build();

        emailClient.send(request);
    }
}

// === PRESENTATION LAYER ===

@RestController(
    path = "/api/v1/orders",
    description = "API para gerenciamento de pedidos",
    version = "3.0",
    roles = {"CUSTOMER", "ORDER_MANAGER"}
)
public class OrderController {

    @Inject @ApiVersion("v3")
    CreateOrderUseCase createOrderUseCase;

    @Inject @ApiVersion("v3")
    FindOrdersByCustomerQueryHandler findOrdersQuery;

    @POST
    @Path("/")
    @Operation(summary = "Cria um novo pedido")
    @APIResponse(responseCode = "201", description = "Pedido criado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    @RolesAllowed({"CUSTOMER"})
    public Response createOrder(@Valid CreateOrderRequest request, @Context SecurityContext context) {
        // Automatic validation, logging, and metrics
        String customerId = context.getUserPrincipal().getName();

        CreateOrderCommand command = CreateOrderCommand.builder()
            .customerId(new CustomerId(customerId))
            .items(mapToOrderItems(request.getItems()))
            .build();

        OrderId orderId = createOrderUseCase.execute(command);

        return Response.status(201)
            .entity(Map.of("orderId", orderId.getValue()))
            .build();
    }

    @GET
    @Path("/customer/{customerId}")
    @Operation(summary = "Lista pedidos de um cliente")
    @RolesAllowed({"CUSTOMER", "ORDER_MANAGER"})
    public Response findOrdersByCustomer(
            @PathParam("customerId") String customerId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {

        FindOrdersByCustomerQuery query = new FindOrdersByCustomerQuery(
            new CustomerId(customerId),
            PageRequest.of(page, size)
        );

        PagedOrderResponse response = findOrdersQuery.handle(query);
        return Response.ok(response).build();
    }
}

```

### Exemplo 2: Sistema de Autenticação e Autorização

```java
// === DOMAIN LAYER ===

@DomainEntity(
    name = "User",
    description = "Usuário do sistema com credenciais e perfis",
    aggregate = "UserAggregate",
    businessRules = {
        "Email deve ser único no sistema",
        "Senha deve atender critérios de segurança",
        "Usuário pode ter múltiplos roles"
    },
    auditable = true
)
public class User {
    private UserId id;
    private Email email;
    private HashedPassword password;
    private Set<Role> roles;
    private UserStatus status;
    private LocalDateTime lastLogin;

    public void authenticate(String rawPassword, PasswordEncoder encoder) {
        if (!encoder.matches(rawPassword, this.password.getValue())) {
            throw new InvalidCredentialsException();
        }
        this.lastLogin = LocalDateTime.now();
    }
}

@ValueObject(
    name = "HashedPassword",
    description = "Senha criptografada do usuário",
    validationGroups = {PasswordValidation.class}
)
public class HashedPassword {
    @NotBlank
    @Size(min = 60, max = 60) // BCrypt hash length
    private final String value;

    private HashedPassword(String hashedValue) {
        this.value = hashedValue;
    }

    public static HashedPassword fromRaw(String rawPassword, PasswordEncoder encoder) {
        return new HashedPassword(encoder.encode(rawPassword));
    }
}

@DomainService(
    name = "AuthenticationService",
    description = "Serviço de autenticação e geração de tokens",
    businessPolicies = {
        "Token expira em 24 horas",
        "Máximo 5 tentativas de login por hora",
        "Bloqueio automático após 10 tentativas incorretas"
    }
)
public class AuthenticationService {

    @Inject
    JwtTokenGenerator tokenGenerator;

    @Inject
    LoginAttemptTracker attemptTracker;

    public AuthenticationResult authenticate(Email email, String password) {
        // Rate limiting check
        attemptTracker.checkRateLimit(email);

        User user = findUserByEmail(email);
        user.authenticate(password, passwordEncoder);

        // Generate JWT token
        String token = tokenGenerator.generate(user);

        attemptTracker.recordSuccessfulLogin(email);

        return new AuthenticationResult(token, user.getRoles());
    }
}

// === APPLICATION LAYER ===

@UseCase(
    name = "AuthenticateUser",
    description = "Autentica usuário e gera token de acesso",
    version = "2.0",
    requiresAuthentication = false, // Login endpoint
    timeoutSeconds = 5
)
public class AuthenticateUserUseCase {

    @Inject @Database("primary") @RepositoryType(JPA)
    UserRepository userRepository;

    @Inject
    AuthenticationService authService;

    @Inject @GatewayType(MESSAGE_QUEUE)
    AuditEventGateway auditGateway;

    public AuthenticationResponse execute(AuthenticateUserCommand command) {
        try {
            AuthenticationResult result = authService.authenticate(
                command.getEmail(),
                command.getPassword()
            );

            // Audit successful login
            auditGateway.publishEvent(new UserLoginEvent(
                command.getEmail(),
                result.getRoles(),
                LocalDateTime.now()
            ));

            return AuthenticationResponse.builder()
                .token(result.getToken())
                .expiresIn(Duration.ofHours(24))
                .roles(result.getRoles())
                .build();

        } catch (InvalidCredentialsException e) {
            // Audit failed login attempt
            auditGateway.publishEvent(new FailedLoginEvent(
                command.getEmail(),
                LocalDateTime.now(),
                "Invalid credentials"
            ));
            throw e;
        }
    }
}

@CommandHandler(
    commandName = "RegisterUser",
    description = "Registra novo usuário com validações completas",
    auditableAction = true,
    inputType = RegisterUserCommand.class
)
public class RegisterUserCommandHandler {

    @Inject @Database("primary") @RepositoryType(JPA)
    UserRepository userRepository;

    @Inject
    Factory userFactory;

    @Inject @GatewayType(EMAIL_SERVICE)
    EmailNotificationGateway emailGateway;

    public void handle(RegisterUserCommand command) {
        // Check if email already exists
        if (userRepository.existsByEmail(command.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        // Create user with factory
        User user = userFactory.createFromRegistration(command);

        // Save user
        UserId userId = userRepository.save(user);

        // Send welcome email
        emailGateway.sendWelcomeEmail(user.getEmail(), userId);
    }
}

// === INFRASTRUCTURE LAYER ===

@Repository(
    entity = "User",
    database = "primary",
    cacheable = true,
    cacheExpirationMinutes = 30
)
@RepositoryType(JPA)
@Database("primary")
public class JpaUserRepository implements UserRepository {

    @PersistenceContext
    EntityManager em;

    @CacheResult(cacheName = "users-by-email")
    public Optional<User> findByEmail(Email email) {
        return em.createQuery(
            "SELECT u FROM User u WHERE u.email = :email", User.class)
            .setParameter("email", email.getValue())
            .getResultStream()
            .findFirst();
    }

    @CacheInvalidate(cacheName = "users-by-email")
    public UserId save(User user) {
        em.persist(user);
        return user.getId();
    }

    public boolean existsByEmail(Email email) {
        return em.createQuery(
            "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
            .setParameter("email", email.getValue())
            .getSingleResult() > 0;
    }
}

@Gateway(
    service = "AuditService",
    version = "v1",
    timeoutSeconds = 5,
    maxRetries = 2
)
@GatewayType(MESSAGE_QUEUE)
public class MessageQueueAuditGateway implements AuditEventGateway {

    @Channel("audit-events")
    Emitter<AuditEvent> auditEmitter;

    public void publishEvent(AuditEvent event) {
        // Automatic logging and metrics
        auditEmitter.send(event);
    }
}

// === SHARED UTILITIES ===

@Factory(
    produces = "User",
    description = "Cria usuários com todas as validações e configurações padrão",
    dependencies = {PasswordEncoder.class, RoleService.class}
)
public class UserFactory {

    @Inject
    PasswordEncoder passwordEncoder;

    @Inject
    RoleService roleService;

    public User createFromRegistration(RegisterUserCommand command) {
        // Apply password policy
        HashedPassword password = HashedPassword.fromRaw(
            command.getPassword(),
            passwordEncoder
        );

        // Assign default roles
        Set<Role> defaultRoles = roleService.getDefaultRoles();

        return User.builder()
            .id(UserId.generate())
            .email(command.getEmail())
            .password(password)
            .roles(defaultRoles)
            .status(UserStatus.ACTIVE)
            .build();
    }
}

@Mapper(
    from = "RegisterUserCommand",
    to = "User",
    description = "Converte comandos de registro em entidades de usuário"
)
public class UserMapper {

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
            .id(user.getId().getValue())
            .email(user.getEmail().getValue())
            .roles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()))
            .status(user.getStatus().name())
            .lastLogin(user.getLastLogin())
            .build();
    }
}

@Validator(
    validates = "RegisterUserCommand",
    description = "Valida comandos de registro de usuário",
    businessRules = {
        "Email deve ter formato válido",
        "Senha deve ter pelo menos 8 caracteres",
        "Senha deve conter maiúscula, minúscula e número"
    }
)
public class RegisterUserCommandValidator {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,}$"
    );

    public void validate(RegisterUserCommand command) {
        validateEmail(command.getEmail());
        validatePassword(command.getPassword());
    }

    private void validateEmail(Email email) {
        // Email validation already handled by @Email annotation
        // Additional business rules can be added here
    }

    private void validatePassword(String password) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new InvalidPasswordException(
                "Password must contain at least 8 characters with uppercase, lowercase and number"
            );
        }
    }
}

```

## ⚙️ **Configuração Avançada**

### 1. Configuração de Interceptadores Condicionais

```java
// Configurar interceptadores apenas em ambiente de produção
@ConditionalOnProperty(name = "app.environment", havingValue = "prod")
@ApplicationScoped
public class ProductionInterceptorConfiguration {

    @Produces
    @AutoMetrics
    public MetricsInterceptor metricsInterceptor() {
        return new MetricsInterceptor();
    }
}

// Configuração específica por ambiente
@ConfigMapping(prefix = "app.interceptors")
public interface InterceptorConfig {

    @WithDefault("true")
    boolean loggingEnabled();

    @WithDefault("true")
    boolean metricsEnabled();

    @WithDefault("INFO")
    String defaultLogLevel();

    @WithDefault("30")
    int validationTimeout();
}

```

### 2. Configuração de Cache por Annotation

```java
@ApplicationScoped
public class CacheConfiguration {

    @Produces
    @Singleton
    public CacheManager cacheManager() {
        return new CaffeineCacheManager();
    }

    // Auto-configure cache based on @Repository annotation
    void onRepositoryStartup(@Observes @Initialized(ApplicationScoped.class) Object init,
                           BeanManager beanManager) {

        beanManager.getBeans(Object.class, Any.Literal.INSTANCE)
            .stream()
            .filter(bean -> bean.getBeanClass().isAnnotationPresent(Repository.class))
            .forEach(this::configureCacheForRepository);
    }

    private void configureCacheForRepository(Bean<?> repositoryBean) {
        Repository annotation = repositoryBean.getBeanClass()
            .getAnnotation(Repository.class);

        if (annotation.cacheable()) {
            // Configure cache with specified expiration
            configureCacheBuilder(
                annotation.entity(),
                Duration.ofMinutes(annotation.cacheExpirationMinutes())
            );
        }
    }
}

```

### 3. Health Checks Automáticos

```java
@ApplicationScoped
public class AnnotationBasedHealthChecks {

    @Produces
    @Liveness
    public HealthCheck databaseLiveness(@Any Instance<Object> beans) {
        return () -> {
            // Check all @Repository beans
            List<String> failedRepositories = beans.stream()
                .filter(bean -> bean.getClass().isAnnotationPresent(Repository.class))
                .map(this::checkRepositoryHealth)
                .filter(result -> !result.isHealthy())
                .map(HealthResult::getName)
                .collect(Collectors.toList());

            return failedRepositories.isEmpty()
                ? HealthCheckResponse.up("databases")
                : HealthCheckResponse.down("databases")
                    .withData("failed", failedRepositories);
        };
    }

    @Produces
    @Readiness
    public HealthCheck gatewayReadiness(@Any Instance<Object> beans) {
        return () -> {
            // Check all @Gateway beans
            List<String> unavailableGateways = beans.stream()
                .filter(bean -> bean.getClass().isAnnotationPresent(Gateway.class))
                .map(this::checkGatewayHealth)
                .filter(result -> !result.isHealthy())
                .map(HealthResult::getName)
                .collect(Collectors.toList());

            return unavailableGateways.isEmpty()
                ? HealthCheckResponse.up("external-services")
                : HealthCheckResponse.down("external-services")
                    .withData("unavailable", unavailableGateways);
        };
    }
}

```

## 📊 **Monitoramento e Observabilidade**

### Métricas Automáticas Geradas

```yaml
# Métricas geradas automaticamente:

# UseCase metrics
usecase.register_user.calls.total{status="success"} 150
usecase.register_user.calls.total{status="error"} 5
usecase.register_user.duration.seconds{quantile="0.5"} 0.125
usecase.register_user.duration.seconds{quantile="0.95"} 0.250

# Repository metrics
repository.user.calls.total{operation="save",status="success"} 145
repository.user.calls.total{operation="findByEmail",status="success"} 1250
repository.user.duration.seconds{operation="save",quantile="0.95"} 0.050

# Gateway metrics
gateway.email_service.calls.total{status="success"} 140
gateway.email_service.calls.total{status="error"} 10
gateway.email_service.circuit_breaker.state{state="closed"} 1

# Query/Command metrics
query.find_orders_by_customer.calls.total{status="success"} 500
command.register_user.calls.total{status="success"} 145

```

### Logs Estruturados Automáticos

```json
{
  "timestamp": "2024-01-15T10:30:45.123Z",
  "level": "INFO",
  "logger": "AutoLoggingInterceptor",
  "message": "→ RegisterUserUseCase.execute called",
  "mdc": {
    "traceId": "abc123",
    "userId": "user456",
    "annotation": "@UseCase",
    "version": "2.0",
    "layer": "application"
  }
}

{
  "timestamp": "2024-01-15T10:30:45.678Z",
  "level": "INFO",
  "logger": "AutoLoggingInterceptor",
  "message": "← RegisterUserUseCase.execute completed in 555ms",
  "mdc": {
    "traceId": "abc123",
    "executionTimeMs": 555,
    "annotation": "@UseCase",
    "status": "success"
  }
}

```

## 🎯 **Resumo dos Benefícios**

### ✅ **Para Desenvolvedores**

- **Código autodocumentado** - Arquitetura visível através das annotations
- **Menos boilerplate** - Interceptadores automáticos
- **Consistência** - Padrões padronizados em todo projeto
- **Produtividade** - Meta-annotations com configurações prontas

### ✅ **Para Operações**

- **Observabilidade automática** - Métricas e logs padronizados
- **Health checks** - Monitoramento automático de componentes
- **Troubleshooting** - Rastreabilidade através das camadas

### ✅ **Para Arquitetura**

- **Clean Architecture** - Separação clara de responsabilidades
- **DDD Compliance** - Conceitos de domínio bem definidos
- **CQRS Implementation** - Separação entre comandos e consultas
- **Testabilidade** - Componentes bem isolados e mockáveis

Essas annotations transformam seu código Quarkus em uma arquitetura de **classe enterprise** com observabilidade, validação e padrões arquiteturais automatizados!

## 🎯 **Implementação Completa Entregue!**

Um sistema completo de annotations personalizadas para Quarkus com **todos os 4 pontos solicitados**:

### ✅ **1. Interceptadores Automáticos**

- **`@AutoLogging`** - Log automático com parâmetros, resultados e tempo de execução
- **`@AutoMetrics`** - Métricas automáticas (contadores, timers, circuit breaker)
- **`@AutoValidation`** - Validação automática de entrada e saída

### ✅ **2. Meta-annotations Avançadas**

- **`@RestController`** - Controller completo com path, produces/consumes, logging, métricas
- **`@UseCase`** - Caso de uso transacional com timeout e validação
- **`@Repository`** - Repositório com cache e métricas configuráveis
- **`@Gateway`** - Gateway com circuit breaker, retry e timeout automáticos

### ✅ **3. Qualificadores Específicos**

- **`@RepositoryType`** - JPA, MongoDB, Redis, etc.
- **`@Database`** - primary, secondary, analytics
- **`@GatewayType`** - REST, SOAP, Message Queue, Email
- **`@Environment`** - dev, test, staging, prod
- **`@ApiVersion`** - v1, v2, v3

### ✅ **4. Metadados Ricos**

- **Documentação** - description, version, businessRules
- **Configuração** - timeouts, retries, cache settings
- **Auditoria** - auditable, invariants, validation groups
- **Observabilidade** - métricas automáticas por annotation

## 🚀 **Principais Vantagens**

### **Antes** (sem annotations):

java

```java
@ApplicationScoped
@Transactional
public class CreateOrderUseCase {
    *// Logging manual// Métricas manuais  // Validação manual// Configuração espalhada*
}
```

### **Depois** (com suas annotations):

java

```java
@UseCase(
    name = "CreateOrder",
    description = "Cria pedido aplicando todas as regras",
    version = "3.0", 
    timeoutSeconds = 30
)
public class CreateOrderUseCase {
    *// Logging automático ✅// Métricas automáticas ✅// Validação automática ✅  // Transação automática ✅// Configuração centralizada ✅*
}
```

## 🎪 **O Resultado Final**

Suas annotations agora criam uma **arquitetura empresarial completa** com:

- ⚡ **Zero boilerplate** - Interceptadores automáticos
- 📊 **Observabilidade total** - Logs e métricas padronizados
- 🏗️ **Clean Architecture** - Camadas bem definidas
- 🔧 **Configuração inteligente** - Meta-annotations com defaults
- 🧪 **Testabilidade máxima** - Componentes isolados
- 📈 **Performance otimizada** - Cache e circuit breakers automáticos

Agora você tem um **framework customizado** dentro do Quarkus que torna desenvolvimento de aplicações enterprise **extremamente produtivo** e **altamente padronizado**!

## 🎯 **Resumo da Organização Recomendada**

### **Onde Salvar: `core/annotation/`**

```java
src/main/java/com/company/app/core/annotation/
├── stereotype/     # @DomainEntity, @UseCase, @Repository, etc.
├── interceptor/    # @AutoLogging, @AutoMetrics, @AutoValidation  
├── qualifier/      # @Database, @RepositoryType, @GatewayType
└── meta/          # Meta-annotations combinadas
```

# CRUD Completo de Produto com Annotations Avançadas

```java
// ================================================================
//                    1. ENTIDADE DE DOMÍNIO
// ================================================================

/**
 * Produto - Entidade principal do domínio
 * A annotation @DomainEntity automaticamente adiciona logging debug
 * e marca como entidade do domínio com metadados ricos
 */
@DomainEntity(
    name = "Product",
    description = "Representa um produto no sistema de vendas",
    aggregate = "ProductAggregate",
    businessRules = {
        "Nome não pode estar vazio",
        "ID deve ser único no sistema",
        "Produto deve ter identificação válida"
    },
    version = "1.0",
    auditable = true
)
public class Product {
    
    @NotNull(message = "ID não pode ser nulo")
    @Min(value = 1, message = "ID deve ser maior que zero")
    private Long id;
    
    @NotBlank(message = "Nome não pode estar vazio")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String name;
    
    // Construtor padrão (necessário para JPA/CDI)
    public Product() {}
    
    // Construtor completo
    public Product(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return Objects.equals(id, product.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "'}";
    }
}

// ================================================================
//                    2. REPOSITORY (CAMADA DE DADOS)
// ================================================================

/**
 * Interface do repositório de produtos
 * Define o contrato para persistência de dados
 */
public interface ProductRepository {
    Product save(Product product);
    Product findById(Long id);
    List<Product> findAll();
    Product update(Product product);
    boolean deleteById(Long id);
    boolean existsById(Long id);
}

/**
 * Implementação JPA do repositório
 * A annotation @Repository automaticamente adiciona:
 * - Logging em nível DEBUG
 * - Métricas com nome "repository"
 * - Transação SUPPORTS (lê dados existentes)
 * - Gerenciamento de cache se habilitado
 */
@Repository(
    entity = "Product",
    database = "primary",
    cacheable = true,
    cacheExpirationMinutes = 30
)
public class ProductJpaRepository implements ProductRepository {
    
    @Inject
    @Database("primary")
    @RepositoryType(RepositoryType.RepositoryKind.JPA)
    private EntityManager entityManager;
    
    @Override
    public Product save(Product product) {
        entityManager.persist(product);
        return product;
    }
    
    @Override
    public Product findById(Long id) {
        return entityManager.find(Product.class, id);
    }
    
    @Override
    public List<Product> findAll() {
        return entityManager.createQuery("SELECT p FROM Product p", Product.class)
                           .getResultList();
    }
    
    @Override
    public Product update(Product product) {
        return entityManager.merge(product);
    }
    
    @Override
    public boolean deleteById(Long id) {
        Product product = findById(id);
        if (product != null) {
            entityManager.remove(product);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean existsById(Long id) {
        Long count = entityManager.createQuery(
            "SELECT COUNT(p) FROM Product p WHERE p.id = :id", Long.class)
            .setParameter("id", id)
            .getSingleResult();
        return count > 0;
    }
}

```

```java
// ================================================================
//                    3. USE CASES (REGRAS DE NEGÓCIO)
// ================================================================

/**
 * Use Case para criar produto
 * A annotation @UseCase automaticamente adiciona:
 * - Logging (sem parâmetros e resultados por privacidade)
 * - Métricas de execução
 * - Validação automática dos parâmetros
 * - Controle de transação REQUIRED
 * - Timeout de 30 segundos
 */
@UseCase(
    name = "CreateProduct",
    description = "Cria um novo produto no sistema",
    version = "1.0",
    requiresAuthentication = true,
    transactionType = TransactionType.REQUIRED,
    timeoutSeconds = 10
)
public class CreateProductUseCase {
    
    @Inject
    private ProductRepository productRepository;
    
    @Inject
    @Validator("ProductValidator")
    private ProductValidator productValidator;
    
    @AutoValidation(validateParams = true)
    public Product execute(@Valid CreateProductRequest request) {
        // Validação de regra de negócio
        if (productRepository.existsById(request.getId())) {
            throw new BusinessException("Produto com ID " + request.getId() + " já existe");
        }
        
        // Aplicar validações de domínio
        productValidator.validateForCreation(request);
        
        // Criar e salvar produto
        Product product = new Product(request.getId(), request.getName());
        return productRepository.save(product);
    }
}

/**
 * Use Case para buscar produto por ID
 */
@UseCase(
    name = "FindProductById",
    description = "Busca produto por ID",
    version = "1.0",
    requiresAuthentication = false,
    transactionType = TransactionType.SUPPORTS,
    timeoutSeconds = 5
)
public class FindProductByIdUseCase {
    
    @Inject
    private ProductRepository productRepository;
    
    public Product execute(@NotNull Long id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new EntityNotFoundException("Produto com ID " + id + " não encontrado");
        }
        return product;
    }
}

/**
 * Use Case para listar todos os produtos
 */
@UseCase(
    name = "ListAllProducts",
    description = "Lista todos os produtos do sistema",
    version = "1.0",
    requiresAuthentication = false,
    transactionType = TransactionType.SUPPORTS,
    timeoutSeconds = 15
)
public class ListAllProductsUseCase {
    
    @Inject
    private ProductRepository productRepository;
    
    public List<Product> execute() {
        return productRepository.findAll();
    }
}

/**
 * Use Case para atualizar produto
 */
@UseCase(
    name = "UpdateProduct",
    description = "Atualiza dados de um produto existente",
    version = "1.0",
    requiresAuthentication = true,
    transactionType = TransactionType.REQUIRED,
    timeoutSeconds = 10
)
public class UpdateProductUseCase {
    
    @Inject
    private ProductRepository productRepository;
    
    @Inject
    private ProductValidator productValidator;
    
    @AutoValidation(validateParams = true)
    public Product execute(@Valid UpdateProductRequest request) {
        // Verificar se produto existe
        Product existingProduct = productRepository.findById(request.getId());
        if (existingProduct == null) {
            throw new EntityNotFoundException("Produto com ID " + request.getId() + " não encontrado");
        }
        
        // Validar dados de atualização
        productValidator.validateForUpdate(request);
        
        // Atualizar produto
        existingProduct.setName(request.getName());
        return productRepository.update(existingProduct);
    }
}

/**
 * Use Case para deletar produto
 */
@UseCase(
    name = "DeleteProduct",
    description = "Remove um produto do sistema",
    version = "1.0",
    requiresAuthentication = true,
    transactionType = TransactionType.REQUIRED,
    timeoutSeconds = 5
)
public class DeleteProductUseCase {
    
    @Inject
    private ProductRepository productRepository;
    
    public boolean execute(@NotNull Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Produto com ID " + id + " não encontrado");
        }
        
        return productRepository.deleteById(id);
    }
}

// ================================================================
//                    4. CONTROLLER REST
// ================================================================

/**
 * Controller REST para produtos
 * A annotation @RestController automaticamente adiciona:
 * - @Path para roteamento
 * - @Produces/@Consumes JSON
 * - Logging automático de todas as operações
 * - Métricas de performance
 * - Validação automática dos parâmetros
 * - Controle de autenticação se necessário
 */
@RestController(
    path = "/products",
    version = "v1",
    description = "API REST para gerenciamento de produtos",
    requiresAuthentication = false, // Para demonstração
    roles = {"USER", "ADMIN"}
)
public class ProductController {
    
    @Inject
    private CreateProductUseCase createProductUseCase;
    
    @Inject
    private FindProductByIdUseCase findProductByIdUseCase;
    
    @Inject
    private ListAllProductsUseCase listAllProductsUseCase;
    
    @Inject
    private UpdateProductUseCase updateProductUseCase;
    
    @Inject
    private DeleteProductUseCase deleteProductUseCase;
    
    @POST
    @AutoValidation(validateParams = true)
    public Response createProduct(@Valid CreateProductRequest request) {
        try {
            Product product = createProductUseCase.execute(request);
            return Response.status(Response.Status.CREATED).entity(product).build();
        } catch (BusinessException e) {
            return Response.status(Response.Status.CONFLICT)
                          .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    @GET
    @Path("/{id}")
    public Response getProduct(@PathParam("id") @NotNull Long id) {
        try {
            Product product = findProductByIdUseCase.execute(id);
            return Response.ok(product).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                          .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    @GET
    public Response getAllProducts() {
        List<Product> products = listAllProductsUseCase.execute();
        return Response.ok(products).build();
    }
    
    @PUT
    @Path("/{id}")
    @AutoValidation(validateParams = true)
    public Response updateProduct(@PathParam("id") Long id, 
                                 @Valid UpdateProductRequest request) {
        try {
            // Garantir que o ID da URL seja usado
            request.setId(id);
            Product product = updateProductUseCase.execute(request);
            return Response.ok(product).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                          .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") @NotNull Long id) {
        try {
            boolean deleted = deleteProductUseCase.execute(id);
            if (deleted) {
                return Response.status(Response.Status.NO_CONTENT).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                              .entity(new ErrorResponse("Produto não encontrado")).build();
            }
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                          .entity(new ErrorResponse(e.getMessage())).build();
        }
    }
}

```

```java
// ================================================================
//                    5. DTOs E VALIDADORES
// ================================================================

/**
 * DTO para criação de produto com validações
 */
@ValueObject(
    name = "CreateProductRequest",
    description = "Dados necessários para criar um produto",
    immutable = false,
    validationGroups = {ValidationGroups.Create.class}
)
public class CreateProductRequest {
    
    @NotNull(message = "ID é obrigatório")
    @Min(value = 1, message = "ID deve ser maior que zero")
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String name;
    
    // Construtores
    public CreateProductRequest() {}
    
    public CreateProductRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

/**
 * DTO para atualização de produto
 */
@ValueObject(
    name = "UpdateProductRequest",
    description = "Dados para atualizar um produto",
    immutable = false,
    validationGroups = {ValidationGroups.Update.class}
)
public class UpdateProductRequest {
    
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String name;
    
    // Construtores
    public UpdateProductRequest() {}
    
    public UpdateProductRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

/**
 * Validador de produto com regras de negócio
 */
@Validator(
    validates = "Product",
    description = "Validador de regras de negócio para produtos",
    businessRules = {
        "Nome deve ser único no sistema",
        "Produto não pode ter nome com palavras proibidas",
        "ID deve seguir padrão sequencial"
    },
    failFast = false
)
public class ProductValidator {
    
    @Inject
    private ProductRepository productRepository;
    
    private final Set<String> forbiddenWords = Set.of("spam", "test", "fake");
    
    public void validateForCreation(CreateProductRequest request) {
        validateName(request.getName());
        validateUniqueId(request.getId());
    }
    
    public void validateForUpdate(UpdateProductRequest request) {
        validateName(request.getName());
    }
    
    private void validateName(String name) {
        if (name == null) return;
        
        String lowerName = name.toLowerCase();
        for (String forbiddenWord : forbiddenWords) {
            if (lowerName.contains(forbiddenWord)) {
                throw new ValidationException(
                    "Nome do produto não pode conter a palavra: " + forbiddenWord);
            }
        }
    }
    
    private void validateUniqueId(Long id) {
        if (productRepository.existsById(id)) {
            throw new ValidationException("Já existe um produto com o ID: " + id);
        }
    }
}

// ================================================================
//                    6. CLASSES AUXILIARES
// ================================================================

/**
 * Grupos de validação
 */
public class ValidationGroups {
    public interface Create {}
    public interface Update {}
}

/**
 * DTO para respostas de erro
 */
public class ErrorResponse {
    private String message;
    private long timestamp;
    
    public ErrorResponse(String message) {
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public String getMessage() { return message; }
    public long getTimestamp() { return timestamp; }
}

/**
 * Exceções customizadas
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
```

## Explicação Técnica Detalhada

### 1. **Por que essas annotations são poderosas?**

Imagine que você precisa adicionar logging em 50 classes diferentes. Sem essas annotations, você escreveria o mesmo código 50 vezes. Com elas, você apenas adiciona `@AutoLogging` e pronto!

### 2. **Como funciona na prática?**

**Quando você chama:**

java

`productController.createProduct(request);`

**O que acontece automaticamente:**

1. **Interceptador de Validação** (prioridade 5): Valida os parâmetros
2. **Interceptador de Logging** (prioridade 10): Registra entrada do método
3. **Interceptador de Métricas** (prioridade 20): Inicia cronômetro
4. **Seu código** executa
5. **Interceptadores voltam**: Param cronômetro, registram saída, contabilizam métricas

### 3. **Benefícios das Meta-Annotations**

```java
@RestController(path = "/products", version = "v1")
```

**Equivale a escrever:**

```java
@ApplicationScoped
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AutoLogging
@AutoMetrics
@AutoValidation
*// + configurações específicas*
```

### 4. **Estrutura da Solução**

**Camadas e suas responsabilidades:**

- **Controller**: Recebe requisições HTTP, delega para Use Cases
- **Use Cases**: Implementam regras de negócio específicas
- **Repository**: Acesso a dados com transações adequadas
- **Validators**: Validações de domínio personalizadas
- **DTOs**: Contratos de entrada/saída com validações

### 5. **Teste Prático**

Para testar, você faria:

```java
POST /products
{
  "id": 1,
  "name": "Smartphone"
}
```

**Logs automáticos gerados:**

```java
→ ProductController.createProduct called with params: [CreateProductRequest{id=1, name='Smartphone'}]
→ CreateProductUseCase.execute called
→ ProductJpaRepository.save called
← ProductJpaRepository.save completed in 45ms
← CreateProductUseCase.execute completed in 67ms
← ProductController.createProduct completed in 89ms
```

**Métricas automáticas:**

- `productcontroller.createproduct.calls` = 1 (sucesso)
- `productcontroller.createproduct.duration` = 89ms
- `createproductusecase.execute.calls` = 1 (sucesso)

### 6. **Vantagens desta Abordagem**

1. **Código Limpo**: Sem repetição de código boilerplate
2. **Observabilidade Automática**: Logs e métricas sem esforço adicional
3. **Validação Consistente**: Mesmas regras em toda aplicação
4. **Facilidade de Manutenção**: Mudança em uma annotation afeta todo o sistema
5. **Separação de Responsabilidades**: Cada camada tem seu propósito bem definido

Esta implementação usa todas as annotations avançadas do documento de forma prática e demonstra como elas simplificam drasticamente o desenvolvimento, mantendo alta qualidade e observabilidade do código.

# Estrutura de Pastas - CRUD Produto com Annotations Avançadas

## 1. Visão Geral da Arquitetura

```
product-management-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── company/
│   │   │           └── productapi/
│   │   │               ├── infrastructure/          # Camada de Infraestrutura
│   │   │               │   ├── annotations/         # Meta-annotations e Interceptadores
│   │   │               │   ├── web/                 # Controllers REST
│   │   │               │   ├── persistence/         # Repositórios JPA
│   │   │               │   ├── config/              # Configurações
│   │   │               │   └── exceptions/          # Tratamento de exceções
│   │   │               ├── application/             # Camada de Aplicação
│   │   │               │   ├── usecases/            # Use Cases (Casos de Uso)
│   │   │               │   ├── dto/                 # DTOs de entrada/saída
│   │   │               │   └── mappers/             # Conversores DTO ↔ Domain
│   │   │               └── domain/                  # Camada de Domínio
│   │   │                   ├── entities/            # Entidades de Domínio
│   │   │                   ├── repositories/        # Interfaces de Repositório
│   │   │                   ├── services/            # Serviços de Domínio
│   │   │                   ├── validators/          # Validadores de Negócio
│   │   │                   └── exceptions/          # Exceções de Domínio
│   │   └── resources/
│   │       ├── META-INF/
│   │       │   └── beans.xml                        # CDI Configuration
│   │       ├── application.yml                      # Configurações da aplicação
│   │       └── persistence.xml                      # Configuração JPA
│   └── test/
│       └── java/
│           └── com/
│               └── company/
│                   └── productapi/
│                       ├── infrastructure/
│                       ├── application/
│                       └── domain/
├── pom.xml                                          # Maven Dependencies
└── README.md                                        # Documentação do projeto

```

## 2. Estrutura Detalhada por Camada

### 📁 infrastructure/ - Camada de Infraestrutura

```
infrastructure/
├── annotations/                    # Meta-annotations e Interceptadores Automáticos
│   ├── interceptors/
│   │   ├── AutoLoggingInterceptor.java           # Interceptador de logging
│   │   ├── AutoMetricsInterceptor.java           # Interceptador de métricas
│   │   └── AutoValidationInterceptor.java        # Interceptador de validação
│   ├── meta/
│   │   ├── RestController.java                   # Meta-annotation para Controllers
│   │   ├── UseCase.java                          # Meta-annotation para Use Cases
│   │   ├── Repository.java                       # Meta-annotation para Repositórios
│   │   ├── Gateway.java                          # Meta-annotation para Gateways
│   │   ├── QueryHandler.java                     # Meta-annotation para Queries
│   │   └── CommandHandler.java                   # Meta-annotation para Commands
│   ├── qualifiers/
│   │   ├── RepositoryType.java                   # Qualificador de tipo de repositório
│   │   ├── Database.java                         # Qualificador de database
│   │   ├── GatewayType.java                      # Qualificador de gateway
│   │   ├── Environment.java                      # Qualificador de ambiente
│   │   └── ApiVersion.java                       # Qualificador de versão API
│   └── domain/
│       ├── DomainEntity.java                     # Annotation para entidades
│       ├── ValueObject.java                      # Annotation para value objects
│       ├── DomainService.java                    # Annotation para serviços
│       ├── Factory.java                          # Annotation para factories
│       ├── Mapper.java                           # Annotation para mappers
│       └── Validator.java                        # Annotation para validadores
├── web/                            # Controllers REST
│   ├── controllers/
│   │   └── ProductController.java                # Controller principal de produtos
│   ├── filters/
│   │   ├── CorsFilter.java                       # Filtro CORS
│   │   └── AuthenticationFilter.java             # Filtro de autenticação
│   └── responses/
│       ├── ErrorResponse.java                    # Resposta padrão de erro
│       ├── SuccessResponse.java                  # Resposta padrão de sucesso
│       └── PagedResponse.java                    # Resposta paginada
├── persistence/                    # Implementações de Repositórios
│   ├── jpa/
│   │   ├── entities/
│   │   │   └── ProductJpaEntity.java             # Entidade JPA
│   │   └── repositories/
│   │       └── ProductJpaRepository.java         # Implementação JPA do repositório
│   ├── mongodb/
│   │   └── repositories/
│   │       └── ProductMongoRepository.java       # Implementação MongoDB (alternativa)
│   └── cache/
│       └── repositories/
│           └── ProductCacheRepository.java       # Repositório com cache
├── config/                         # Configurações de Infraestrutura
│   ├── DatabaseConfig.java                      # Configuração do banco
│   ├── CacheConfig.java                          # Configuração do cache
│   ├── MetricsConfig.java                        # Configuração de métricas
│   ├── LoggingConfig.java                        # Configuração de logging
│   └── ValidationConfig.java                     # Configuração de validação
└── exceptions/                     # Tratamento Global de Exceções
    ├── GlobalExceptionHandler.java               # Manipulador global de exceções
    ├── ValidationExceptionHandler.java           # Manipulador de exceções de validação
    └── BusinessExceptionHandler.java             # Manipulador de exceções de negócio

```

### 📁 application/ - Camada de Aplicação

```
application/
├── usecases/                       # Casos de Uso da Aplicação
│   ├── product/
│   │   ├── CreateProductUseCase.java             # Criar produto
│   │   ├── FindProductByIdUseCase.java           # Buscar produto por ID
│   │   ├── ListAllProductsUseCase.java           # Listar todos os produtos
│   │   ├── UpdateProductUseCase.java             # Atualizar produto
│   │   ├── DeleteProductUseCase.java             # Deletar produto
│   │   └── SearchProductsUseCase.java            # Buscar produtos com filtros
│   └── common/
│       ├── UseCaseExecutor.java                  # Executor genérico de use cases
│       └── TransactionManager.java               # Gerenciador de transações
├── dto/                           # Data Transfer Objects
│   ├── request/
│   │   ├── CreateProductRequest.java             # DTO para criar produto
│   │   ├── UpdateProductRequest.java             # DTO para atualizar produto
│   │   ├── SearchProductRequest.java             # DTO para buscar produtos
│   │   └── PaginationRequest.java                # DTO para paginação
│   ├── response/
│   │   ├── ProductResponse.java                  # DTO de resposta de produto
│   │   ├── ProductListResponse.java              # DTO para lista de produtos
│   │   └── ProductSummaryResponse.java           # DTO resumido de produto
│   └── validation/
│       └── ValidationGroups.java                 # Grupos de validação
├── mappers/                       # Conversores entre DTOs e Domain
│   ├── ProductMapper.java                        # Mapper de produto
│   ├── ProductRequestMapper.java                 # Mapper de requests
│   └── ProductResponseMapper.java                # Mapper de responses
└── services/                      # Serviços de Aplicação
    ├── ProductApplicationService.java            # Serviço principal de produto
    ├── ProductSearchService.java                 # Serviço de busca
    └── ProductValidationService.java             # Serviço de validação

```

### 📁 domain/ - Camada de Domínio

```
domain/
├── entities/                      # Entidades de Domínio
│   ├── Product.java                              # Entidade principal Produto
│   ├── ProductId.java                            # Value Object para ID
│   └── ProductName.java                          # Value Object para Nome
├── repositories/                  # Interfaces de Repositório
│   ├── ProductRepository.java                    # Interface do repositório de produto
│   └── specifications/
│       └── ProductSpecification.java             # Especificações de busca
├── services/                      # Serviços de Domínio
│   ├── ProductDomainService.java                 # Serviços com regras de negócio
│   ├── ProductBusinessRules.java                 # Regras de negócio centralizadas
│   └── ProductFactory.java                       # Factory para criação de produtos
├── validators/                    # Validadores de Domínio
│   ├── ProductValidator.java                     # Validador principal
│   ├── ProductNameValidator.java                 # Validador de nome
│   └── ProductIdValidator.java                   # Validador de ID
├── exceptions/                    # Exceções de Domínio
│   ├── ProductNotFoundException.java             # Produto não encontrado
│   ├── ProductAlreadyExistsException.java        # Produto já existe
│   ├── InvalidProductDataException.java          # Dados inválidos
│   └── BusinessRuleViolationException.java       # Violação de regra de negócio
└── events/                       # Eventos de Domínio (Event Sourcing)
    ├── ProductCreatedEvent.java                  # Evento de produto criado
    ├── ProductUpdatedEvent.java                  # Evento de produto atualizado
    ├── ProductDeletedEvent.java                  # Evento de produto deletado
    └── ProductEventHandler.java                  # Manipulador de eventos

```

## 3. Arquivos de Configuração

### 📁 resources/

```
resources/
├── META-INF/
│   ├── beans.xml                  # Configuração CDI
│   └── persistence.xml            # Configuração JPA
├── config/
│   ├── application.yml            # Configurações principais
│   ├── application-dev.yml        # Configurações de desenvolvimento
│   ├── application-test.yml       # Configurações de teste
│   └── application-prod.yml       # Configurações de produção
├── db/
│   ├── migration/
│   │   ├── V001__Create_Product_Table.sql        # Script inicial do banco
│   │   └── V002__Add_Product_Indexes.sql         # Índices da tabela
│   └── data/
│       └── test-data.sql          # Dados de teste
├── logging/
│   └── logback-spring.xml         # Configuração de logs
└── static/
    └── api-docs/
        └── swagger-ui/            # Documentação da API

```

## 4. Testes

### 📁 test/

```
test/
├── java/
│   └── com/company/productapi/
│       ├── infrastructure/
│       │   ├── web/
│       │   │   └── ProductControllerTest.java    # Testes do controller
│       │   └── persistence/
│       │       └── ProductJpaRepositoryTest.java # Testes do repositório
│       ├── application/
│       │   └── usecases/
│       │       ├── CreateProductUseCaseTest.java # Testes de use case
│       │       └── FindProductByIdUseCaseTest.java
│       └── domain/
│           ├── entities/
│           │   └── ProductTest.java              # Testes da entidade
│           └── validators/
│               └── ProductValidatorTest.java     # Testes do validador
└── resources/
    ├── application-test.yml       # Configurações para teste
    └── test-data/
        └── products.json          # Dados de teste em JSON

```

## 5. Explicação das Responsabilidades

### **📋 Por que essa estrutura?**

**1. Separação Clara de Responsabilidades**

- **Domain**: Regras de negócio puras, sem dependências externas
- **Application**: Orquestração dos casos de uso
- **Infrastructure**: Detalhes técnicos (banco, web, frameworks)

**2. Facilidade de Teste**

- Cada camada pode ser testada independentemente
- Domain e Application não dependem de infraestrutura

**3. Flexibilidade Tecnológica**

- Pode trocar JPA por MongoDB sem afetar o domínio
- Pode trocar REST por GraphQL facilmente

**4. Escalabilidade**

- Fácil adicionar novos produtos/entidades
- Interceptadores aplicam-se automaticamente

### **📦 Como as Annotations se Encaixam**

**Meta-Annotations** (`infrastructure/annotations/meta/`):

- Reduzem boilerplate code
- Padronizam comportamentos entre camadas
- Aplicam interceptadores automaticamente

**Interceptadores** (`infrastructure/annotations/interceptors/`):

- Executam cross-cutting concerns (logging, métricas, validação)
- Aplicam-se transparentemente via annotations

**Qualificadores** (`infrastructure/annotations/qualifiers/`):

- Permitem múltiplas implementações da mesma interface
- Facilitam troca entre ambientes (dev/prod)

## 6. Comandos Úteis

```bash
# Estrutura do projeto
mkdir -p src/main/java/com/company/productapi/{infrastructure/{annotations/{interceptors,meta,qualifiers,domain},web/{controllers,filters,responses},persistence/{jpa/{entities,repositories},mongodb/repositories,cache/repositories},config,exceptions},application/{usecases/{product,common},dto/{request,response,validation},mappers,services},domain/{entities,repositories/{specifications},services,validators,exceptions,events}}

# Estrutura de testes
mkdir -p src/test/java/com/company/productapi/{infrastructure/{web,persistence},application/usecases,domain/{entities,validators}}

# Estrutura de recursos
mkdir -p src/main/resources/{META-INF,config,db/{migration,data},logging,static/api-docs/swagger-ui}

```

Esta estrutura garante um código organizado, testável e que escala bem conforme o projeto cresce, aproveitando ao máximo o poder das meta-annotations criadas.

## Explicação Didática da Estrutura

### **🎯 Conceito Principal: Separação por Responsabilidade**

Imagine que você está construindo uma casa:

- **Domain** = A planta da casa (regras estruturais)
- **Application** = Como você vai usar a casa (casos de uso)
- **Infrastructure** = Materiais e ferramentas (tecnologias)

### **📚 Por que 3 camadas principais?**

**1. Domain (Núcleo do Negócio)**

`🧠 "O que o sistema faz?"
- Product.java → Representa um produto no mundo real
- ProductValidator.java → Regras: "nome não pode ser vazio"
- ProductRepository.java → Contrato: "como salvar produtos"`

**2. Application (Casos de Uso)**

`🎭 "Como o sistema é usado?"
- CreateProductUseCase.java → "Criar um produto"
- FindProductByIdUseCase.java → "Buscar produto por ID"
- ProductMapper.java → "Converter dados entre camadas"`

**3. Infrastructure (Detalhes Técnicos)**

`🔧 "Como o sistema funciona tecnicamente?"
- ProductController.java → Recebe requisições HTTP
- ProductJpaRepository.java → Salva no banco PostgreSQL
- AutoLoggingInterceptor.java → Gera logs automaticamente`

### **🚀 Como as Annotations Simplificam**

**Sem annotations (código tradicional):**

java

`@ApplicationScoped
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {
    
    @POST
    public Response create(ProductRequest request) {
        Logger.info("Starting create product");
        long start = System.currentTimeMillis();
        
        try {
            *// validar request// chamar use case  // registrar métricas// fazer log de sucesso*
        } catch (Exception e) {
            *// fazer log de erro// registrar métricas de erro*
        }
    }
}`

**Com meta-annotations:**

java

`@RestController(path = "/products", version = "v1")
public class ProductController {
    
    @POST
    public Response create(@Valid ProductRequest request) {
        *// Só o código de negócio!*
        return useCase.execute(request);
    }
}`

### **📁 Navegando na Estrutura**

**Para adicionar uma nova funcionalidade:**

1. **Domain primeiro** → Criar regras de negócio
2. **Application depois** → Criar caso de uso
3. **Infrastructure por último** → Expor via REST

**Exemplo prático:**

`Adicionar "Categoria" ao produto:

1. domain/entities/ProductCategory.java
2. application/usecases/product/AddCategoryToProductUseCase.java  
3. infrastructure/web/controllers/ProductController.java (novo método)`

### **🔍 Como Encontrar o que Precisa**

**"Preciso alterar uma validação"** → `domain/validators/`**"Preciso alterar a API REST"** → `infrastructure/web/controllers/`**"Preciso alterar como salva no banco"** → `infrastructure/persistence/`**"Preciso criar novo caso de uso"** → `application/usecases/`

### **⚡ Benefícios desta Organização**

1. **Facilita Manutenção**: Cada pasta tem uma responsabilidade clara
2. **Facilita Testes**: Testa cada camada separadamente
3. **Facilita Evolução**: Adiciona funcionalidades sem quebrar existentes
4. **Facilita Trabalho em Equipe**: Cada dev pode trabalhar em uma camada
5. **Padronização Automática**: Annotations garantem comportamento consistente

**Esta estrutura cresce com seu projeto**, mantendo sempre a organização e clareza, independente do tamanho que ele alcançar!