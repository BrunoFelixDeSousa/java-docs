---
applyTo: "**/*.java"
description: "Padrões avançados para código Java 21+ com Object Calisthenics, Clean Architecture e performance enterprise"
---

# Padrões Java 21+ Enterprise - Object Calisthenics

Aplicar as [instruções gerais](./copilot.instructions.md) e [Clean Architecture](./architecture.instructions.md) a todo código Java.

## Objetivo

Estabelecer padrões enterprise para desenvolvimento Java 21+ seguindo Object Calisthenics, Clean Code e Clean Architecture, garantindo código altamente performático, legível, testável e de fácil manutenção.

## Referências

- [Object Calisthenics](https://williamdurand.fr/2013/06/03/object-calisthenics/) - 9 regras para OOP de qualidade
- [Java 21+ Features](https://openjdk.org/projects/jdk/21/) - Features modernas obrigatórias (Virtual Threads, Pattern Matching, Sequenced Collections)
- [Effective Java 3rd Edition](https://www.oreilly.com/library/view/effective-java-3rd/9780134686097/) - Best practices Java
- [Clean Code](https://www.oreilly.com/library/view/clean-code-a/9780136083238/) - Código limpo e legível
- [Java Performance](https://www.oreilly.com/library/view/java-performance-2nd/9781492056102/) - Otimizações enterprise

## Java 21+ Features Obrigatórias

### Virtual Threads - Concorrência Escalável

#### ✅ PADRÃO - Virtual Threads para operações I/O-bound

```java
// ✅ PADRÃO - Uso de Virtual Threads (JEP 444)
@ApplicationScoped
public class OrderProcessingService {

    public List<OrderResult> processOrdersBatch(List<Order> orders) {
        // Virtual threads são ideais para tarefas I/O-bound
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = orders.stream()
                .map(order -> executor.submit(() -> processOrder(order)))
                .toList();

            return futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new OrderProcessingException("Error processing order", e);
                    }
                })
                .toList();
        }
    }

    private OrderResult processOrder(Order order) {
        // Operações bloqueantes (DB, HTTP, etc.) são otimizadas com virtual threads
        var payment = paymentGateway.processPayment(order);
        var inventory = inventoryService.reserveItems(order);
        var notification = notificationService.sendConfirmation(order);

        return new OrderResult(payment, inventory, notification);
    }
}

// ✅ PADRÃO - Structured Concurrency (JEP 453)
public class UserDataAggregator {

    public UserProfile aggregateUserData(UserId userId) throws ExecutionException, InterruptedException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Future<User> userFuture = scope.fork(() -> userRepository.findById(userId));
            Future<List<Order>> ordersFuture = scope.fork(() -> orderRepository.findByUserId(userId));
            Future<UserPreferences> prefsFuture = scope.fork(() -> prefsRepository.findByUserId(userId));

            scope.join()           // Aguarda todas as tarefas
                 .throwIfFailed(); // Lança exceção se alguma falhou

            return new UserProfile(
                userFuture.resultNow(),
                ordersFuture.resultNow(),
                prefsFuture.resultNow()
            );
        }
    }
}
```

### Pattern Matching Avançado

#### ✅ PADRÃO - Record Patterns (JEP 440)

```java
// ✅ PADRÃO - Record patterns em switch
public BigDecimal calculateShipping(Order order) {
    return switch (order) {
        case Order(_, _, ShippingAddress(_, _, "BR"), var items)
            when items.totalWeight().lessThan(Weight.ofKg(2)) ->
            Money.of(15.00, BRL);

        case Order(_, _, ShippingAddress(_, _, "BR"), var items) ->
            Money.of(25.00, BRL).add(items.totalWeight().multiply(5.00));

        case Order(_, _, ShippingAddress(_, _, var country), _) ->
            internationalShippingCalculator.calculate(country, order);

        default -> throw new UnsupportedShippingException();
    };
}

// ✅ PADRÃO - Nested pattern matching
public String formatPayment(Payment payment) {
    return switch (payment) {
        case CreditCard(var number, var holder, ExpiryDate(var month, var year)) ->
            "Card: %s (%s) - %02d/%d".formatted(maskCard(number), holder, month, year);

        case Pix(var key, PaymentStatus.COMPLETED) ->
            "PIX: %s - PAID".formatted(key);

        case Pix(var key, PaymentStatus.PENDING) ->
            "PIX: %s - AWAITING PAYMENT".formatted(key);

        case BankTransfer(var account, var bank, var amount) ->
            "Transfer: %s (%s) - %s".formatted(account, bank, amount);

        default -> "Unknown payment method";
    };
}
```

### Sequenced Collections (JEP 431)

#### ✅ PADRÃO - Uso de Sequenced Collections

```java
// ✅ PADRÃO - SequencedCollection APIs
public class OrderHistory {
    private final SequencedSet<Order> orders = new LinkedHashSet<>();

    public void addOrder(Order order) {
        orders.addFirst(order); // Novo em Java 21
    }

    public Optional<Order> getLatestOrder() {
        return Optional.ofNullable(orders.getFirst()); // Novo em Java 21
    }

    public Optional<Order> getOldestOrder() {
        return Optional.ofNullable(orders.getLast()); // Novo em Java 21
    }

    public SequencedSet<Order> getRecentOrders(int limit) {
        return orders.stream()
            .limit(limit)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public SequencedSet<Order> reversed() {
        return orders.reversed(); // Novo em Java 21
    }
}
```

## Java 21+ Features Obrigatórias (Continuação)

### Records - Value Objects Imutáveis

#### ✅ PADRÃO - Records com validação e comportamento

```java
// ✅ PADRÃO - Record para Value Objects com validação rica
public record Email(String value) {
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");

    public Email {
        validateEmail(value);
    }

    private static void validateEmail(String email) {
        Objects.requireNonNull(email, "Email cannot be null");

        if (email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank");
        }

        if (email.length() > 254) {
            throw new IllegalArgumentException("Email too long: maximum 254 characters");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    // ✅ Comportamentos de domínio
    public String domain() {
        return value.substring(value.indexOf('@') + 1);
    }

    public boolean isFromDomain(String domain) {
        return domain().equalsIgnoreCase(domain);
    }

    public Email withDomain(String newDomain) {
        var localPart = value.substring(0, value.indexOf('@'));
        return new Email(localPart + "@" + newDomain);
    }
}

// ✅ PADRÃO - Record com factory methods
public record Money(BigDecimal amount, Currency currency) {
    private static final int SCALE = 2;

    public Money {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");

        if (amount.scale() > currency.getDefaultFractionDigits()) {
            amount = amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_UP);
        }
    }

    // ✅ Factory methods para cenários comuns
    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public static Money of(double amount, Currency currency) {
        return new Money(BigDecimal.valueOf(amount), currency);
    }

    public static Money ofCents(long cents, Currency currency) {
        var amount = BigDecimal.valueOf(cents).movePointLeft(SCALE);
        return new Money(amount, currency);
    }

    // ✅ Operações matemáticas imutáveis
    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money subtract(Money other) {
        validateSameCurrency(other);
        return new Money(amount.subtract(other.amount), currency);
    }

    public Money multiply(BigDecimal factor) {
        return new Money(amount.multiply(factor), currency);
    }

    public Money divide(BigDecimal divisor) {
        return new Money(amount.divide(divisor, RoundingMode.HALF_UP), currency);
    }

    // ✅ Comparações type-safe
    public boolean isGreaterThan(Money other) {
        validateSameCurrency(other);
        return amount.compareTo(other.amount) > 0;
    }

    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    private void validateSameCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                String.format("Currency mismatch: %s vs %s", currency, other.currency)
            );
        }
    }

    @Override
    public String toString() {
        var formatter = NumberFormat.getCurrencyInstance();
        formatter.setCurrency(currency);
        return formatter.format(amount);
    }
}

// ❌ EVITAR - Classes tradicionais para Value Objects simples
public class UserId {
    private final UUID value;

    public UserId(UUID value) {
        this.value = Objects.requireNonNull(value);
    }

    public UUID getValue() { return value; } // Desnecessário com records

    @Override
    public boolean equals(Object obj) {
        // Boilerplate que records fazem automaticamente
    }
}
```

### Sealed Classes/Interfaces - Type Safety

#### ✅ PADRÃO - Sealed interfaces para Result types

```java
// ✅ PADRÃO - Sealed interface para resultados type-safe
public sealed interface CreateUserResult
    permits CreateUserResult.Success,
            CreateUserResult.ValidationError,
            CreateUserResult.BusinessError,
            CreateUserResult.SystemError {

    record Success(User user, List<DomainEvent> events) implements CreateUserResult {}

    record ValidationError(List<ValidationFailure> failures) implements CreateUserResult {
        public ValidationError(String... errors) {
            this(Arrays.stream(errors)
                .map(error -> new ValidationFailure("validation", error, null))
                .collect(Collectors.toList()));
        }

        public boolean hasFieldError(String fieldName) {
            return failures.stream()
                .anyMatch(failure -> fieldName.equals(failure.field()));
        }
    }

    record BusinessError(String code, String message, Map<String, Object> context) implements CreateUserResult {
        public BusinessError(String code, String message) {
            this(code, message, Map.of());
        }
    }

    record SystemError(String message, Throwable cause, String correlationId) implements CreateUserResult {}

    // ✅ Utility methods para análise
    default boolean isSuccess() {
        return this instanceof Success;
    }

    default boolean isFailure() {
        return !isSuccess();
    }

    default User getUserOrThrow() {
        return switch (this) {
            case Success(var user, var events) -> user;
            case ValidationError(var failures) ->
                throw new ValidationException("Validation failed", failures);
            case BusinessError(var code, var message, var context) ->
                throw new BusinessException(code, message, context);
            case SystemError(var message, var cause, var correlationId) ->
                throw new SystemException(message, cause, correlationId);
        };
    }
}

// ✅ PADRÃO - Sealed interface para hierarquia de domínio
public sealed interface DomainEvent permits
    UserCreatedEvent,
    UserUpdatedEvent,
    UserDeactivatedEvent,
    OrderCreatedEvent,
    OrderConfirmedEvent,
    PaymentProcessedEvent {

    UUID eventId();
    Instant occurredAt();
    String eventType();
    UUID aggregateId();
    Long version();

    default String getEventType() {
        return this.getClass().getSimpleName();
    }
}

// ✅ Implementação de eventos específicos
public record UserCreatedEvent(
    UUID eventId,
    Instant occurredAt,
    UUID aggregateId,
    Long version,
    Email email,
    UserName name,
    Map<String, Object> metadata
) implements DomainEvent {

    public UserCreatedEvent(UUID aggregateId, Long version, Email email, UserName name) {
        this(
            UUID.randomUUID(),
            Instant.now(),
            aggregateId,
            version,
            email,
            name,
            Map.of(
                "source", "user-service",
                "version", "1.0",
                "environment", System.getProperty("app.environment", "dev")
            )
        );
    }

    @Override
    public String eventType() {
        return "UserCreated";
    }
}
```

### Pattern Matching - Switch Expressions Avançadas

#### ✅ PADRÃO - Pattern matching para lógica complexa

```java
// ✅ PADRÃO - Pattern matching com guard conditions
public BigDecimal calculateDiscount(Customer customer, Order order) {
    return switch (customer) {
        case PremiumCustomer(var membership) when membership.isActive() -> {
            var baseDiscount = order.totalValue().multiply(membership.discountRate());
            yield baseDiscount.min(membership.maxDiscount());
        }

        case RegularCustomer regular when regular.orderCount() > 10 ->
            order.totalValue().multiply(BigDecimal.valueOf(0.05)); // 5% desconto fidelidade

        case RegularCustomer regular when isFirstOrder(regular) ->
            order.totalValue().multiply(BigDecimal.valueOf(0.10)); // 10% primeira compra

        case CorporateCustomer(var contract) ->
            order.totalValue().multiply(contract.negotiatedDiscount());

        case null -> BigDecimal.ZERO;

        default -> order.totalValue().multiply(BigDecimal.valueOf(0.02)); // 2% padrão
    };
}

// ✅ PADRÃO - Pattern matching para processamento de pagamento
public PaymentResult processPayment(Payment payment) {
    return switch (payment) {
        case CreditCardPayment(var number, var cvv, var expiry) -> {
            validateCreditCard(number, cvv, expiry);
            yield processCreditCardPayment(number, payment.amount());
        }

        case PixPayment(var key, var bank) -> {
            validatePixKey(key);
            yield processPixPayment(key, payment.amount(), bank);
        }

        case BankTransferPayment(var accountNumber, var bankCode, var holderName) -> {
            validateBankAccount(accountNumber, bankCode);
            yield processBankTransfer(accountNumber, bankCode, holderName, payment.amount());
        }

        case DigitalWalletPayment(var walletType, var token) -> switch (walletType) {
            case PAYPAL -> processPaypalPayment(token, payment.amount());
            case APPLE_PAY -> processApplePayPayment(token, payment.amount());
            case GOOGLE_PAY -> processGooglePayPayment(token, payment.amount());
        };

        case null -> throw new IllegalArgumentException("Payment cannot be null");

        // Compilador força tratamento de todos os casos
    };
}

// ✅ PADRÃO - Pattern matching em validation
public ValidationResult validateUser(Object userData) {
    return switch (userData) {
        case CreateUserRequest(var name, var email, var age) when age < 18 ->
            ValidationResult.failure("User must be at least 18 years old");

        case CreateUserRequest(var name, var email, var age) when name.length() < 2 ->
            ValidationResult.failure("Name must have at least 2 characters");

        case CreateUserRequest(var name, var email, var age) when !isValidEmail(email) ->
            ValidationResult.failure("Invalid email format");

        case CreateUserRequest request -> ValidationResult.success(request);

        case UpdateUserRequest request -> validateUpdateRequest(request);

        case null -> ValidationResult.failure("Request cannot be null");

        default -> ValidationResult.failure("Unknown request type: " + userData.getClass());
    };
}
```

### Text Blocks - Queries e Templates Complexos

#### ✅ PADRÃO - Text blocks para SQL complexo

```java
// ✅ PADRÃO - SQL complexo com text blocks
public class UserReportRepository {

    private static final String MONTHLY_USER_REPORT_QUERY = """
        WITH user_metrics AS (
            SELECT
                u.id,
                u.email,
                u.name,
                u.created_at,
                u.status,
                COUNT(DISTINCT o.id) as total_orders,
                COALESCE(SUM(o.total_amount), 0) as total_spent,
                AVG(o.total_amount) as avg_order_value,
                MAX(o.created_at) as last_order_date,
                EXTRACT(DAYS FROM (NOW() - MAX(o.created_at))) as days_since_last_order
            FROM users u
            LEFT JOIN orders o ON o.user_id = u.id
                AND o.status = 'COMPLETED'
                AND o.created_at >= :startDate
                AND o.created_at <= :endDate
            WHERE u.created_at <= :endDate
            GROUP BY u.id, u.email, u.name, u.created_at, u.status
        ),
        customer_segments AS (
            SELECT
                *,
                CASE
                    WHEN total_spent >= :premiumThreshold THEN 'PREMIUM'
                    WHEN total_orders >= :loyalOrderCount THEN 'LOYAL'
                    WHEN days_since_last_order <= :activeThresholdDays THEN 'ACTIVE'
                    WHEN days_since_last_order > :churnThresholdDays THEN 'AT_RISK'
                    ELSE 'REGULAR'
                END as customer_segment
            FROM user_metrics
        )
        SELECT
            id,
            email,
            name,
            status,
            total_orders,
            total_spent,
            avg_order_value,
            last_order_date,
            days_since_last_order,
            customer_segment,
            RANK() OVER (ORDER BY total_spent DESC) as spending_rank,
            PERCENT_RANK() OVER (ORDER BY total_spent) as spending_percentile
        FROM customer_segments
        WHERE (:segment IS NULL OR customer_segment = :segment)
          AND (:minSpent IS NULL OR total_spent >= :minSpent)
          AND (:status IS NULL OR status = :status)
        ORDER BY total_spent DESC, total_orders DESC
        LIMIT :pageSize OFFSET :offset
        """;

    private static final String EMAIL_TEMPLATE = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <title>%s</title>
            <style>
                body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }
                .header { background-color: #f8f9fa; padding: 20px; border-radius: 8px; }
                .content { margin: 20px 0; line-height: 1.6; }
                .footer { font-size: 12px; color: #6c757d; margin-top: 30px; }
                .highlight { background-color: #fff3cd; padding: 10px; border-radius: 4px; }
            </style>
        </head>
        <body>
            <div class="header">
                <h1>Olá, %s!</h1>
                <p>%s</p>
            </div>
            <div class="content">
                %s
            </div>
            <div class="footer">
                <p>Este é um e-mail automático. Não responda a este endereço.</p>
                <p>© 2024 Nossa Empresa. Todos os direitos reservados.</p>
            </div>
        </body>
        </html>
        """;

    private static final String JSON_API_REQUEST_TEMPLATE = """
        {
            "apiVersion": "v2",
            "kind": "UserRegistration",
            "metadata": {
                "timestamp": "%s",
                "correlationId": "%s",
                "source": "user-service"
            },
            "spec": {
                "user": {
                    "email": "%s",
                    "name": "%s",
                    "preferences": {
                        "notifications": %s,
                        "marketing": %s,
                        "language": "%s"
                    }
                },
                "registration": {
                    "source": "%s",
                    "ipAddress": "%s",
                    "userAgent": "%s"
                }
            }
        }
        """;
}

// ✅ PADRÃO - Text blocks para scripts shell/docker
public class DeploymentScriptGenerator {

    public String generateDockerRunScript(ApplicationConfig config) {
        return """
            #!/bin/bash
            set -euo pipefail

            # Configuration
            APP_NAME="%s"
            IMAGE_TAG="%s"
            CONTAINER_NAME="${APP_NAME}-$(date +%%Y%%m%%d-%%H%%M%%S)"

            # Environment variables
            export DATABASE_URL="%s"
            export REDIS_URL="%s"
            export JWT_SECRET="%s"

            echo "Starting deployment of $APP_NAME:$IMAGE_TAG"

            # Pull latest image
            docker pull "$APP_NAME:$IMAGE_TAG"

            # Stop old container if exists
            if docker ps -q --filter "label=app=$APP_NAME" | grep -q .; then
                echo "Stopping existing containers..."
                docker stop $(docker ps -q --filter "label=app=$APP_NAME")
            fi

            # Start new container
            docker run -d \\
                --name "$CONTAINER_NAME" \\
                --label "app=$APP_NAME" \\
                --label "version=$IMAGE_TAG" \\
                --env DATABASE_URL \\
                --env REDIS_URL \\
                --env JWT_SECRET \\
                --publish %d:8080 \\
                --restart unless-stopped \\
                --health-cmd="curl -f http://localhost:8080/q/health || exit 1" \\
                --health-interval=30s \\
                --health-timeout=10s \\
                --health-start-period=40s \\
                --health-retries=3 \\
                "$APP_NAME:$IMAGE_TAG"

            echo "Container $CONTAINER_NAME started successfully"

            # Wait for health check
            echo "Waiting for application to be healthy..."
            timeout 120 bash -c 'until docker inspect "$1" --format="{{.State.Health.Status}}" | grep -q healthy; do sleep 2; done' _ "$CONTAINER_NAME"

            echo "Application is healthy and ready to serve traffic"
            """.formatted(
                config.applicationName(),
                config.imageTag(),
                config.databaseUrl(),
                config.redisUrl(),
                config.jwtSecret(),
                config.port()
            );
    }
}
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
