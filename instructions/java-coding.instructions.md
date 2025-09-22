---
applyTo: "**/*.java"
description: "Padrões específicos para código Java 17+ com Object Calisthenics"
---

# Padrões Java 17+ com Object Calisthenics

Aplicar as [instruções gerais](./copilot-instructions.md) a todo código Java.

## Features Java 17+ Obrigatórias

### Records para Value Objects
```java
// ✅ USAR - Record para dados imutáveis
public record UserId(UUID value) {
    public UserId {
        Objects.requireNonNull(value, "UserId cannot be null");
    }
}

// ❌ EVITAR - Classes com getters/setters
public class UserId {
    private UUID value;
    // getters/setters...
}
```

### Pattern Matching
```java
// ✅ USAR - Pattern matching em switch expressions
public String formatResult(Result result) {
    return switch (result) {
        case Success(var data) -> "Success: " + data;
        case Error(var message) -> "Error: " + message;
    };
}
```

### Text Blocks para SQL/JSON
```java
// ✅ USAR - Text blocks para queries
private static final String QUERY = """
    SELECT u.id, u.name, u.email
    FROM users u
    WHERE u.active = true
    ORDER BY u.created_at DESC
    """;
```

## Object Calisthenics - Implementação

### Regra 1: Um Nível de Indentação
```java
// ✅ CORRETO - Extrair métodos para reduzir indentação
public void processOrders(List<Order> orders) {
    for (Order order : orders) {
        processValidOrder(order);
    }
}

private void processValidOrder(Order order) {
    if (!isValidOrder(order)) return;
    
    processPayment(order);
    updateInventory(order);
    sendConfirmation(order);
}
```

### Regra 2: Não Use ELSE
```java
// ✅ CORRETO - Early returns
public String calculateDiscount(Customer customer) {
    if (!customer.isActive()) {
        return "0%";
    }
    
    if (customer.isPremium()) {
        return "15%";
    }
    
    return "5%";
}
```

### Regra 3: Encapsule Primitivos
```java
// ✅ CORRETO - Value Objects
public record Email(String value) {
    public Email {
        validateEmail(value);
    }
    
    private void validateEmail(String email) {
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}

// ✅ CORRETO - Uso do Value Object
public class User {
    private final UserId id;
    private final Email email;
    
    public User(UserId id, Email email) {
        this.id = Objects.requireNonNull(id);
        this.email = Objects.requireNonNull(email);
    }
}
```

### Regra 4: Coleções de Primeira Classe
```java
// ✅ CORRETO - Wrapper para coleções
public class OrderItems {
    private final List<OrderItem> items;
    
    public OrderItems(List<OrderItem> items) {
        this.items = List.copyOf(items);
    }
    
    public BigDecimal totalValue() {
        return items.stream()
            .map(OrderItem::value)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public int size() {
        return items.size();
    }
}
```

### Regra 8: Máximo 2 Variáveis de Instância
```java
// ✅ CORRETO - Máximo 2 campos por classe
public class OrderService {
    private final OrderRepository repository;
    private final PaymentService paymentService;
    
    @Inject
    public OrderService(OrderRepository repository, PaymentService paymentService) {
        this.repository = repository;
        this.paymentService = paymentService;
    }
}
```

## Quarkus 3+ Específico

### Injeção de Dependência
```java
// ✅ CORRETO - Constructor injection
@ApplicationScoped
public class UserService {
    private final UserRepository repository;
    
    @Inject
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

### REST Endpoints
```java
// ✅ CORRETO - Endpoint seguindo Clean Architecture
@Path("/users")
@ApplicationScoped
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    
    @Inject
    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }
    
    @POST
    public Response createUser(CreateUserRequest request) {
        var command = toCommand(request);
        var result = createUserUseCase.execute(command);
        
        return switch (result) {
            case Success(var user) -> Response.ok(toResponse(user)).build();
            case ValidationError(var errors) -> Response.status(400).entity(errors).build();
            case SystemError(var message) -> Response.status(500).entity(message).build();
        };
    }
}
```

### Configuração Reativa
```java
// ✅ USAR - Programação reativa quando apropriado
@GET
public Uni<List<User>> getAllUsers() {
    return userRepository.listAll();
}
```

## Padrões de Exception
```java
// ✅ CORRETO - Exceptions específicas de domínio
public class UserNotFoundException extends DomainException {
    public UserNotFoundException(UserId userId) {
        super("User not found: " + userId.value());
    }
}

// ✅ CORRETO - Result pattern para operações que podem falhar
public sealed interface CreateUserResult
    permits Success, ValidationError, SystemError {
    
    record Success(User user) implements CreateUserResult {}
    record ValidationError(List<String> errors) implements CreateUserResult {}
    record SystemError(String message) implements CreateUserResult {}
}
```