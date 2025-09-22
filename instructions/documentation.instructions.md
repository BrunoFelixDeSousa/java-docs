---
applyTo: "**/*.md,**/*.java"
description: "Padrões para documentação de código, APIs e arquitetura"
---

# Padrões de Documentação

Aplicar as [instruções gerais](./copilot-instructions.md) para toda documentação.

## JavaDoc - Padrões

### Classes de Domínio
```java
/**
 * Representa um usuário no sistema.
 * 
 * <p>Esta classe implementa os princípios de Object Calisthenics:
 * - Máximo 2 variáveis de instância
 * - Sem getters/setters tradicionais
 * - Métodos pequenos e focados
 * 
 * <p><strong>Invariantes:</strong>
 * <ul>
 *   <li>ID não pode ser nulo</li>
 *   <li>Email deve ter formato válido</li>
 *   <li>Nome não pode ser vazio</li>
 * </ul>
 * 
 * @author Sistema de Gestão
 * @since 1.0.0
 * @see UserId
 * @see Email
 * @see UserName
 */
public class User {
    
    /**
     * Identificador único do usuário.
     * 
     * <p>Gerado automaticamente durante a criação e nunca alterado.
     */
    private final UserId id;
    
    /**
     * Cria um novo usuário com os dados fornecidos.
     * 
     * <p><strong>Validações aplicadas:</strong>
     * <ul>
     *   <li>Todos os parâmetros são obrigatórios (não-nulos)</li>
     *   <li>Email deve ter formato válido</li>
     *   <li>Nome deve ter pelo menos 2 caracteres</li>
     * </ul>
     * 
     * @param id identificador único do usuário
     * @param email endereço de email válido
     * @param name nome completo do usuário
     * @throws IllegalArgumentException se algum parâmetro for inválido
     * @throws NullPointerException se algum parâmetro for nulo
     */
    public User(UserId id, Email email, UserName name) {
        // implementação
    }
    
    /**
     * Altera o email do usuário.
     * 
     * <p><strong>Regras de Negócio:</strong>
     * <ul>
     *   <li>Novo email deve ser diferente do atual</li>
     *   <li>Novo email não pode estar em uso por outro usuário</li>
     *   <li>Retorna nova instância (imutabilidade)</li>
     * </ul>
     * 
     * @param newEmail novo endereço de email
     * @return nova instância de User com email alterado
     * @throws EmailAlreadyExistsException se o email já estiver em uso
     * @throws IllegalArgumentException se o email for inválido
     * @since 1.1.0
     */
    public User changeEmail(Email newEmail) {
        // implementação
    }
}
```

### Use Cases
```java
/**
 * Caso de uso responsável pela criação de novos usuários no sistema.
 * 
 * <p>Este use case implementa as seguintes validações e regras de negócio:
 * <ul>
 *   <li>Validação de formato de email</li>
 *   <li>Verificação de unicidade de email</li>
 *   <li>Geração automática de ID</li>
 *   <li>Publicação de evento de criação</li>
 * </ul>
 * 
 * <p><strong>Fluxo Principal:</strong>
 * <ol>
 *   <li>Validar dados de entrada</li>
 *   <li>Verificar se email já existe</li>
 *   <li>Criar nova instância de User</li>
 *   <li>Persistir no repositório</li>
 *   <li>Publicar evento UserCreatedEvent</li>
 * </ol>
 * 
 * @author Sistema de Gestão
 * @since 1.0.0
 * @see CreateUserCommand
 * @see CreateUserResult
 * @see User
 */
@ApplicationScoped
public class CreateUserUseCase {
    
    /**
     * Executa o caso de uso de criação de usuário.
     * 
     * <p><strong>Possíveis resultados:</strong>
     * <ul>
     *   <li>{@link CreateUserResult.Success} - Usuário criado com sucesso</li>
     *   <li>{@link CreateUserResult.ValidationError} - Erro de validação</li>
     *   <li>{@link CreateUserResult.SystemError} - Erro interno do sistema</li>
     * </ul>
     * 
     * @param command dados necessários para criar o usuário
     * @return resultado da operação encapsulado em {@link CreateUserResult}
     * @throws NullPointerException se command for nulo
     */
    public CreateUserResult execute(CreateUserCommand command) {
        // implementação
    }
}
```

### APIs REST
```java
/**
 * Controller REST para gerenciamento de usuários.
 * 
 * <p>Implementa os endpoints para operações CRUD de usuários,
 * seguindo os padrões REST e Clean Architecture.
 * 
 * <p><strong>Base Path:</strong> {@code /api/v1/users}
 * 
 * <p><strong>Códigos de Status HTTP:</strong>
 * <ul>
 *   <li>200 OK - Operação executada com sucesso</li>
 *   <li>201 Created - Recurso criado com sucesso</li>
 *   <li>400 Bad Request - Erro de validação</li>
 *   <li>404 Not Found - Recurso não encontrado</li>
 *   <li>500 Internal Server Error - Erro interno</li>
 * </ul>
 * 
 * @author Sistema de Gestão
 * @since 1.0.0
 */
@Path("/api/v1/users")
@ApplicationScoped
@Tag(name = "Users", description = "User management operations")
public class UserController {
    
    /**
     * Cria um novo usuário no sistema.
     * 
     * <p><strong>Exemplo de Request:</strong>
     * <pre>{@code
     * {
     *   "name": "João Silva",
     *   "email": "joao@example.com"
     * }
     * }</pre>
     * 
     * <p><strong>Exemplo de Response (201):</strong>
     * <pre>{@code
     * {
     *   "id": "123e4567-e89b-12d3-a456-426614174000",
     *   "name": "João Silva",
     *   "email": "joao@example.com",
     *   "status": "ACTIVE",
     *   "createdAt": "2024-01-15T10:30:00Z"
     * }
     * }</pre>
     * 
     * @param request dados do usuário a ser criado
     * @return resposta HTTP com dados do usuário criado ou erro
     * @since 1.0.0
     */
    @POST
    @Operation(summary = "Create a new user", description = "Creates a new user in the system")
    @APIResponses({
        @APIResponse(
            responseCode = "201", 
            description = "User created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "400", 
            description = "Validation error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "500", 
            description = "Internal server error"
        )
    })
    public Response createUser(@Valid CreateUserRequest request) {
        // implementação
    }
}
```

## README.md - Estrutura

```markdown
# Sistema de Gestão de Usuários

## 📋 Visão Geral

Sistema desenvolvido em Java 17+ com Quarkus 3+ seguindo os princípios de Clean Architecture e Object Calisthenics para gerenciamento de usuários.

## 🏗️ Arquitetura

### Estrutura de Camadas
```
src/main/java/
├── domain/           # Regras de negócio (Entities, Value Objects)
├── application/      # Casos de uso
├── infrastructure/   # Adapters (DB, External Services)
└── presentation/     # Controllers, DTOs
```

### Princípios Aplicados
- **Clean Architecture**: Separação clara de responsabilidades
- **Object Calisthenics**: 9 regras para código limpo
- **SOLID**: Princípios de design orientado a objetos
- **DDD**: Domain-Driven Design

## 🚀 Tecnologias

- **Java 17+**: Records, Pattern Matching, Text Blocks
- **Quarkus 3+**: Framework reativo para microsserviços
- **Maven**: Gerenciamento de dependências
- **PostgreSQL**: Banco de dados principal
- **JUnit 5**: Testes unitários
- **TestContainers**: Testes de integração
- **REST Assured**: Testes de API

## 📦 Dependências Principais

```xml
<dependencies>
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
</dependencies>
```

## 🏃‍♂️ Como Executar

### Desenvolvimento
```bash
# Subir banco de dados local
docker-compose up -d postgres

# Executar em modo dev
./mvnw compile quarkus:dev
```

### Testes
```bash
# Testes unitários
./mvnw test

# Testes de integração
./mvnw verify -Pintegration-tests

# Cobertura de código
./mvnw jacoco:report
```

### Produção
```bash
# Build nativo
./mvnw package -Pnative

# Executar
./target/user-management-1.0.0-runner
```

## 📚 Documentação da API

- **Swagger UI**: http://localhost:8080/q/swagger-ui
- **OpenAPI Spec**: http://localhost:8080/q/openapi

## 🧪 Exemplos de Uso

### Criar Usuário
```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com"
  }'
```

### Buscar Usuário
```bash
curl http://localhost:8080/api/v1/users/123e4567-e89b-12d3-a456-426614174000
```

## 🏗️ Padrões de Desenvolvimento

### Object Calisthenics
1. ✅ Um nível de indentação por método
2. ✅ Não use ELSE
3. ✅ Encapsule todos os primitivos
4. ✅ Coleções de primeira classe
5. ✅ Um ponto por linha
6. ✅ Não abrevie
7. ✅ Mantenha entidades pequenas
8. ✅ Máximo 2 variáveis de instância por classe
9. ✅ Sem getters/setters

### Clean Architecture
- **Entities**: Core business rules
- **Use Cases**: Application business rules  
- **Interface Adapters**: Convert data formats
- **Frameworks & Drivers**: External concerns

## 📊 Métricas de Qualidade

- **Cobertura de Testes**: > 80%
- **Complexidade Ciclomática**: < 10 por método
- **Linhas por Classe**: < 50 (Object Calisthenics)
- **Duplicação de Código**: < 3%

## 🔧 Configuração

### Banco de Dados
```properties
# application.properties
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=user
quarkus.datasource.password=password
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/userdb
```

### Logging
```properties
quarkus.log.level=INFO
quarkus.log.category."com.myproject".level=DEBUG
```

## 👥 Contribuição

1. Fork o projeto
2. Crie feature branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit as mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para branch (`git push origin feature/nova-funcionalidade`)
5. Abra Pull Request

### Code Review Checklist
- [ ] Object Calisthenics aplicado
- [ ] Clean Architecture respeitada
- [ ] Testes com cobertura > 80%
- [ ] JavaDoc atualizado
- [ ] README atualizado se necessário

## 📝 Changelog

### [1.0.0] - 2024-01-15
#### Adicionado
- Criação inicial do projeto
- Implementação de CRUD de usuários
- Testes unitários e de integração
- Documentação da API

## 📄 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.
```

## Documentação de Arquitetura (ADRs)

```markdown
# ADR-001: Uso de Clean Architecture

## Status
Aceito

## Contexto
Precisamos de uma arquitetura que permita:
- Testabilidade alta
- Baixo acoplamento
- Facilidade de manutenção
- Independência de frameworks

## Decisão
Adotar Clean Architecture com as seguintes camadas:
- Domain: Entities, Value Objects, Repository interfaces
- Application: Use Cases, Application Services
- Infrastructure: Repository implementations, External services
- Presentation: REST Controllers, DTOs

## Consequências
### Positivas
- Código altamente testável
- Regras de negócio isoladas
- Fácil substituição de componentes
- Independência de frameworks

### Negativas
- Maior complexidade inicial
- Mais arquivos e interfaces
- Curva de aprendizado
```

## Comentários de Código

### Para Regras de Negócio Complexas
```java
/**
 * Calcula o desconto aplicável baseado no perfil do cliente.
 * 
 * Regras de Negócio:
 * 1. Cliente Premium: 15% de desconto
 * 2. Cliente Regular ativo > 1 ano: 10% de desconto  
 * 3. Cliente Regular ativo < 1 ano: 5% de desconto
 * 4. Cliente inativo: sem desconto
 * 
 * @param customer cliente para cálculo
 * @return percentual de desconto (0-15)
 */
public int calculateDiscountPercentage(Customer customer) {
    // Early return para clientes inativos
    if (!customer.isActive()) {
        return 0;
    }
    
    // Cliente premium sempre recebe desconto máximo
    if (customer.isPremium()) {
        return 15;
    }
    
    // Cliente regular: desconto baseado no tempo de atividade
    return customer.isActiveForMoreThanOneYear() ? 10 : 5;
}
```

### Para Object Calisthenics
```java
// Object Calisthenics Rule #8: Máximo 2 variáveis de instância
public class OrderProcessor {
    private final OrderRepository repository;      // 1ª variável
    private final PaymentService paymentService;  // 2ª variável
    
    // ✅ Seguindo a regra: apenas 2 variáveis de instância
    // Se precisar de mais funcionalidade, extrair para outro serviço
}
```

## Padrões de Commit

```
feat: adiciona endpoint para criação de usuários
fix: corrige validação de email em User.changeEmail()
docs: atualiza README com instruções de deploy
test: adiciona testes para UserDomainService
refactor: aplica Object Calisthenics em OrderService
style: formata código seguindo checkstyle
perf: otimiza query de busca de usuários
build: atualiza dependência do Quarkus para 3.6.0
```