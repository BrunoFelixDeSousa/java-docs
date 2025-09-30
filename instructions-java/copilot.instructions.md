---
applyTo: "**/*"
description: "Instruções fundamentais para desenvolvimento Java/Quarkus com Clean Architecture e Object Calisthenics"
---

# Instruções Fundamentais - Java/Quarkus Enterprise

Este documento estabelece os padrões fundamentais para desenvolvimento de software de alta qualidade usando Java 21+ e Quarkus 3+, aplicando Clean Architecture e Object Calisthenics.

## Referências

- [Object Calisthenics](https://williamdurand.fr/2013/06/03/object-calisthenics/) - 9 regras para melhor OO
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) - Arquitetura limpa
- [Java 21+ Features](https://openjdk.org/projects/jdk/21/) - Features modernas do Java (Virtual Threads, Pattern Matching, Sequenced Collections)
- [Quarkus](https://quarkus.io/) - Framework reativo

## Tecnologias Base

- **GraalVM JDK 21+**: JDK com compilação nativa e otimizações avançadas
  - Features modernas: records, pattern matching, text blocks, sealed interfaces, virtual threads, sequenced collections
  - Native Image para executáveis nativos de alta performance
  - GraalVM Compiler para otimizações JIT superiores
- **Quarkus 3+**: Framework principal com dependency injection, reactive programming, native compilation, suporte a virtual threads
- **Maven**: Gerenciador de dependências com profiles otimizados para GraalVM

### Java 21+ Features - Uso Obrigatório

#### Records - Value Objects

```java
// ✅ PADRÃO - Record para Value Objects
public record UserId(UUID value) {
    public UserId {
        Objects.requireNonNull(value, "UserId cannot be null");
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }
}

public record Money(BigDecimal amount, Currency currency) {
    public Money {
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(currency, "Currency cannot be null");

        if (amount.scale() > currency.getDefaultFractionDigits()) {
            throw new IllegalArgumentException("Invalid scale for currency");
        }
    }

    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    private void validateSameCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot operate with different currencies");
        }
    }
}

// ❌ EVITAR - Classes tradicionais para Value Objects simples
public class UserId {
    private final UUID value;

    public UserId(UUID value) {
        this.value = Objects.requireNonNull(value);
    }

    public UUID getValue() { return value; }

    @Override
    public boolean equals(Object obj) {
        // Boilerplate desnecessário com records
    }
}
```

#### Sealed Interfaces - Result Types

```java
// ✅ PADRÃO - Sealed interface para Results
public sealed interface CreateUserResult
    permits CreateUserResult.Success, CreateUserResult.ValidationError, CreateUserResult.SystemError {

    record Success(User user) implements CreateUserResult {}
    record ValidationError(List<String> errors) implements CreateUserResult {}
    record SystemError(String message, Throwable cause) implements CreateUserResult {}
}

// ✅ USO - Pattern matching
public Response handleCreateUser(CreateUserCommand command) {
    var result = userService.createUser(command);

    return switch (result) {
        case CreateUserResult.Success(var user) ->
            Response.status(201).entity(toDTO(user)).build();

        case CreateUserResult.ValidationError(var errors) ->
            Response.status(400).entity(new ErrorResponse(errors)).build();

        case CreateUserResult.SystemError(var message, var cause) -> {
            Log.error("System error creating user", cause);
            yield Response.status(500).entity(new ErrorResponse(message)).build();
        }
    };
}
```

#### Text Blocks - SQL e JSON

```java
// ✅ PADRÃO - Text blocks para strings multilinha
public class UserRepository {

    private static final String FIND_USERS_QUERY = """
        SELECT u.id, u.name, u.email, u.status, u.created_at,
               COUNT(o.id) as order_count,
               COALESCE(SUM(o.total_amount), 0) as total_spent
        FROM users u
        LEFT JOIN orders o ON o.user_id = u.id
        WHERE u.status = :status
          AND u.created_at >= :fromDate
        GROUP BY u.id, u.name, u.email, u.status, u.created_at
        ORDER BY u.created_at DESC
        """;

    public String generateWelcomeEmail(User user) {
        return """
            {
                "to": "%s",
                "subject": "Welcome %s!",
                "template": "welcome",
                "variables": {
                    "userName": "%s",
                    "activationLink": "%s"
                }
            }
            """.formatted(
                user.email().value(),
                user.name().value(),
                user.name().value(),
                generateActivationLink(user.id())
            );
    }
}

// ❌ EVITAR - String concatenation para multilinha
private static final String QUERY =
    "SELECT u.id, u.name, u.email " +
    "FROM users u " +
    "WHERE u.status = :status " +
    "ORDER BY u.created_at DESC";
```

#### Pattern Matching - instanceof

```java
// ✅ PADRÃO - Pattern matching com instanceof
public Money calculateDiscount(Customer customer, Money orderValue) {
    return switch (customer) {
        case PremiumCustomer premium -> {
            var discount = orderValue.multiply(premium.discountRate());
            yield discount.min(premium.maxDiscount());
        }
        case RegularCustomer regular ->
            orderValue.multiply(regular.standardDiscountRate());
        case null -> Money.ZERO;
        default -> throw new UnsupportedCustomerTypeException(customer.getClass());
    };
}

// ✅ PADRÃO - Pattern matching em validação
public void validatePayment(Object payment) {
    switch (payment) {
        case CreditCardPayment(var number, var cvv, var expiry) -> {
            validateCreditCard(number, cvv, expiry);
        }
        case PixPayment(var key) -> {
            validatePixKey(key);
        }
        case BankTransferPayment(var account, var bank) -> {
            validateBankAccount(account, bank);
        }
        case null -> throw new IllegalArgumentException("Payment cannot be null");
        default -> throw new UnsupportedPaymentMethodException(payment.getClass());
    }
}
```

### Quarkus 3+ Patterns

#### Dependency Injection

```java
// ✅ PADRÃO - Constructor injection
@ApplicationScoped
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final DomainEventPublisher eventPublisher;

    @Inject
    public UserService(
            UserRepository userRepository,
            EmailService emailService,
            DomainEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.eventPublisher = eventPublisher;
    }
}

// ✅ PADRÃO - Producer methods para configuration
@ApplicationScoped
public class Configuration {

    @Produces
    @ApplicationScoped
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    @Produces
    @ApplicationScoped
    public Clock clock() {
        return Clock.systemUTC();
    }
}

// ❌ EVITAR - Field injection
@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository; // Torna teste mais difícil
}
```

#### Configuration Properties

```java
// ✅ PADRÃO - Configuration interface
@ConfigMapping(prefix = "app.email")
public interface EmailConfig {
    String serviceUrl();
    String apiKey();
    Optional<String> fromAddress();

    @WithDefault("30")
    int timeoutSeconds();

    @WithDefault("3")
    int maxRetries();

    SslConfig ssl();

    interface SslConfig {
        @WithDefault("true")
        boolean enabled();

        Optional<String> keystore();
    }
}

// ✅ USO - Injection do config
@ApplicationScoped
public class EmailService {

    private final EmailConfig emailConfig;

    @Inject
    public EmailService(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    public void sendEmail(Email email) {
        var client = createHttpClient(
            emailConfig.serviceUrl(),
            emailConfig.timeoutSeconds()
        );
        // Implementation
    }
}
```

#### Reactive Programming

```java
// ✅ PADRÃO - Reactive endpoints
@Path("/api/v1/users")
@ApplicationScoped
public class ReactiveUserController {

    @Inject
    ReactiveUserService userService;

    @GET
    @Path("/search")
    public Multi<UserDTO> searchUsers(@QueryParam("query") String query) {
        return userService.searchUsers(query)
            .map(this::toDTO);
    }

    @POST
    public Uni<Response> createUser(CreateUserRequest request) {
        return userService.createUser(request)
            .map(user -> Response.status(201).entity(toDTO(user)).build())
            .onFailure(ValidationException.class)
            .recoverWithItem(ex -> Response.status(400)
                .entity(new ErrorResponse(ex.getMessage()))
                .build());
    }
}

// ✅ PADRÃO - Reactive service
@ApplicationScoped
public class ReactiveUserService {

    @Inject
    ReactiveUserRepository userRepository;

    public Multi<User> searchUsers(String query) {
        return userRepository.findByNameContaining(query)
            .select().where("status", UserStatus.ACTIVE);
    }

    public Uni<User> createUser(CreateUserRequest request) {
        return Uni.createFrom().item(() -> buildUser(request))
            .chain(user -> userRepository.persist(user))
            .invoke(user -> publishUserCreatedEvent(user));
    }
}
```

## Object Calisthenics - Princípios Fundamentais

Aplicar rigorosamente as 9 regras para código orientado a objetos de alta qualidade:

### 1. Um Nível de Indentação por Método

#### ✅ PADRÃO - Máximo 1 nível de indentação

```java
// ✅ Método com complexidade reduzida
public List<User> findActiveUsers(List<User> users) {
    return users.stream()
        .filter(this::isActiveUser)
        .collect(Collectors.toList());
}

private boolean isActiveUser(User user) {
    if (!user.isActive()) {
        return false;
    }

    return hasValidSubscription(user);
}

private boolean hasValidSubscription(User user) {
    return user.subscription()
        .map(Subscription::isValid)
        .orElse(false);
}

// ✅ Método com early returns
public Money calculateDiscount(Order order) {
    if (order.isEmpty()) {
        return Money.ZERO;
    }

    if (!order.customer().isPremium()) {
        return calculateStandardDiscount(order);
    }

    return calculatePremiumDiscount(order);
}
```

#### ❌ EVITAR - Indentação excessiva

```java
// ❌ Muitos níveis de indentação
public void processOrder(Order order) {
    if (order != null) {
        if (order.isValid()) {
            for (OrderItem item : order.items()) {
                if (item.isAvailable()) {
                    if (item.quantity() > 0) {
                        // Lógica aninhada demais
                        processItem(item);
                    }
                }
            }
        }
    }
}
```

### 2. Não Use ELSE - Early Returns e Guard Clauses

#### ✅ PADRÃO - Early returns

```java
public CreateUserResult createUser(CreateUserCommand command) {
    // Guard clauses
    if (command == null) {
        return new CreateUserResult.ValidationError("Command cannot be null");
    }

    if (!isValidEmail(command.email())) {
        return new CreateUserResult.ValidationError("Invalid email format");
    }

    if (userRepository.existsByEmail(command.email())) {
        return new CreateUserResult.ValidationError("Email already exists");
    }

    // Happy path sem else
    var user = createUserFromCommand(command);
    var savedUser = userRepository.save(user);
    publishUserCreatedEvent(savedUser);

    return new CreateUserResult.Success(savedUser);
}

// ✅ Pattern matching substitui if-else
public PaymentResult processPayment(Payment payment) {
    return switch (payment.type()) {
        case CREDIT_CARD -> processCreditCardPayment(payment);
        case PIX -> processPixPayment(payment);
        case BANK_TRANSFER -> processBankTransferPayment(payment);
    };
}
```

#### ❌ EVITAR - Estruturas if-else aninhadas

```java
// ❌ If-else aninhado
public String getDiscountMessage(Customer customer) {
    if (customer.isPremium()) {
        if (customer.hasActiveSubscription()) {
            return "Premium discount available";
        } else {
            return "Subscription expired";
        }
    } else {
        if (customer.hasOrders()) {
            return "Standard discount available";
        } else {
            return "No discount available";
        }
    }
}
```

### 3. Encapsule Primitivos - Value Objects Obrigatórios

#### ✅ PADRÃO - Value Objects para conceitos de domínio

```java
// ✅ Value Objects específicos
public record Email(String value) {
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public Email {
        validateEmail(value);
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    public String domain() {
        return value.substring(value.indexOf('@') + 1);
    }
}

public record UserName(String value) {
    public UserName {
        validateName(value);
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        if (name.length() < 2 || name.length() > 100) {
            throw new IllegalArgumentException("Name must be between 2 and 100 characters");
        }
    }

    public String initials() {
        return Arrays.stream(value.split("\\s+"))
            .map(word -> word.substring(0, 1).toUpperCase())
            .collect(Collectors.joining());
    }
}

// ✅ Uso - Expressividade máxima
public class User {
    private final UserId id;
    private final Email email;
    private final UserName name;

    public User changeEmail(Email newEmail) {
        validateEmailChange(newEmail);
        return new User(id, newEmail, name);
    }
}
```

#### ❌ EVITAR - Primitivos expostos

```java
// ❌ Uso de primitivos String/int/UUID diretamente
public class User {
    private String email; // Sem validação
    private String name;  // Sem comportamento
    private int age;      // Sem validação de limites
    private UUID id;      // Sem tipo específico

    // Método vulnerável a erros
    public void updateUser(String email, String name, int age) {
        this.email = email; // Pode ser inválido
        this.name = name;   // Pode ser null/empty
        this.age = age;     // Pode ser negativo
    }
}
```

### 4. Coleções de Primeira Classe - Wrapper Classes

#### ✅ PADRÃO - Wrapper para coleções

```java
// ✅ Coleção especializada
public class OrderItems {
    private final List<OrderItem> items;

    public OrderItems(List<OrderItem> items) {
        this.items = List.copyOf(Objects.requireNonNull(items));
    }

    public static OrderItems empty() {
        return new OrderItems(List.of());
    }

    public OrderItems add(OrderItem item) {
        validateItem(item);
        var newItems = new ArrayList<>(items);
        newItems.add(item);
        return new OrderItems(newItems);
    }

    public OrderItems remove(OrderItemId itemId) {
        var newItems = items.stream()
            .filter(item -> !item.id().equals(itemId))
            .collect(Collectors.toList());
        return new OrderItems(newItems);
    }

    public Money totalValue() {
        return items.stream()
            .map(OrderItem::totalPrice)
            .reduce(Money.ZERO, Money::add);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int count() {
        return items.size();
    }

    // Comportamentos específicos do domínio
    public boolean hasItem(ProductId productId) {
        return items.stream()
            .anyMatch(item -> item.productId().equals(productId));
    }

    public OrderItems updateQuantity(ProductId productId, int newQuantity) {
        var newItems = items.stream()
            .map(item -> item.productId().equals(productId)
                ? item.withQuantity(newQuantity)
                : item)
            .collect(Collectors.toList());
        return new OrderItems(newItems);
    }

    private void validateItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("OrderItem cannot be null");
        }

        if (hasItem(item.productId())) {
            throw new DuplicateItemException(item.productId());
        }
    }
}

// ✅ Uso expressivo
public class Order {
    private final OrderItems items;

    public Order addItem(ProductId productId, int quantity, Money unitPrice) {
        var newItem = new OrderItem(productId, quantity, unitPrice);
        var updatedItems = items.add(newItem);
        return new Order(id, customerId, updatedItems);
    }

    public Money calculateTotal() {
        return items.totalValue();
    }
}
```

#### ❌ EVITAR - Coleções primitivas expostas

```java
// ❌ List<> exposta sem comportamento
public class Order {
    private List<OrderItem> items; // Sem comportamento específico

    public List<OrderItem> getItems() {
        return items; // Exposição direta - perigoso
    }

    public void setItems(List<OrderItem> items) {
        this.items = items; // Sem validação
    }
}
```

### 5. Um Ponto por Linha - Evitar Method Chaining Excessivo

#### ✅ PADRÃO - Method chaining controlado

```java
// ✅ Uma operação complexa por linha
public List<UserDTO> findActiveUsers(UserSearchCriteria criteria) {
    return userRepository.findByCriteria(criteria)
        .stream()
        .filter(User::isActive)
        .filter(user -> matchesCriteria(user, criteria))
        .map(this::toDTO)
        .collect(Collectors.toList());
}

// ✅ Quebrar chains longos em variáveis intermediárias
public OrderSummary calculateOrderSummary(Order order) {
    var subtotal = order.items().totalValue();
    var tax = taxCalculator.calculateTax(subtotal, order.shippingAddress());
    var discount = discountCalculator.calculateDiscount(order.customer(), subtotal);
    var total = subtotal.add(tax).subtract(discount);

    return new OrderSummary(subtotal, tax, discount, total);
}
```

#### ❌ EVITAR - Method chaining excessivo

```java
// ❌ Muitos pontos em uma linha
var result = userRepository.findAll()
    .stream().filter(User::isActive).filter(u -> u.age() > 18)
    .map(User::email).map(Email::domain).distinct()
    .collect(Collectors.groupingBy(d -> d, Collectors.counting()));
```

### 6. Não Abrevie Nomes - Clareza Sempre

#### ✅ PADRÃO - Nomes expressivos e completos

```java
// ✅ Nomes claros e intencionais
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final EmailValidationService emailValidationService;
    private final WelcomeEmailSender welcomeEmailSender;

    public UserRegistrationResult registerNewUser(UserRegistrationCommand command) {
        var emailAddress = new Email(command.emailAddress());
        var fullName = new UserName(command.fullName());

        var validationResult = emailValidationService.validateEmail(emailAddress);
        if (!validationResult.isValid()) {
            return UserRegistrationResult.invalidEmail(validationResult.errors());
        }

        var existingUser = userRepository.findByEmail(emailAddress);
        if (existingUser.isPresent()) {
            return UserRegistrationResult.emailAlreadyExists(emailAddress);
        }

        var newUser = User.create(emailAddress, fullName);
        var savedUser = userRepository.save(newUser);

        welcomeEmailSender.sendWelcomeEmail(savedUser);

        return UserRegistrationResult.success(savedUser);
    }
}

// ✅ Variáveis com nomes descritivos
public Money calculateShippingCost(ShippingAddress destinationAddress, Package packageToShip) {
    var distanceInKilometers = distanceCalculator.calculate(
        warehouseAddress,
        destinationAddress
    );

    var weightInKilograms = packageToShip.totalWeight();
    var volumeInCubicCentimeters = packageToShip.totalVolume();

    var baseCostByDistance = shippingRates.getCostByDistance(distanceInKilometers);
    var additionalCostByWeight = shippingRates.getAdditionalCostByWeight(weightInKilograms);
    var additionalCostByVolume = shippingRates.getAdditionalCostByVolume(volumeInCubicCentimeters);

    return baseCostByDistance
        .add(additionalCostByWeight)
        .add(additionalCostByVolume);
}
```

#### ❌ EVITAR - Abreviações e nomes confusos

```java
// ❌ Nomes abreviados e confusos
public class UsrSvc {
    private UsrRepo repo;
    private EmailSvc emailSvc;

    public UsrResult regUsr(UsrCmd cmd) {
        var e = new Email(cmd.getE());
        var n = new UserName(cmd.getN());

        var vr = emailSvc.val(e);
        if (!vr.ok()) {
            return UsrResult.err(vr.errs());
        }

        var u = repo.findByE(e);
        if (u.isPresent()) {
            return UsrResult.dup(e);
        }

        // Código confuso pela nomenclatura
    }
}
```

### 7. Mantenha Entidades Pequenas - Máximo 50 Linhas

#### ✅ PADRÃO - Classes focadas e pequenas

```java
// ✅ Entity pequena e focada (< 50 linhas)
public class User {
    private final UserId id;
    private final Email email;
    private final UserName name;
    private final UserStatus status;

    public User(UserId id, Email email, UserName name) {
        this.id = Objects.requireNonNull(id);
        this.email = Objects.requireNonNull(email);
        this.name = Objects.requireNonNull(name);
        this.status = UserStatus.ACTIVE;
    }

    public User changeEmail(Email newEmail) {
        validateEmailChange(newEmail);
        return new User(id, newEmail, name, status);
    }

    public User deactivate() {
        validateCanDeactivate();
        return new User(id, email, name, UserStatus.INACTIVE);
    }

    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    private void validateEmailChange(Email newEmail) {
        if (email.equals(newEmail)) {
            throw new SameEmailException(newEmail);
        }
    }

    private void validateCanDeactivate() {
        if (!isActive()) {
            throw new UserAlreadyInactiveException(id);
        }
    }

    // Getters
    public UserId id() { return id; }
    public Email email() { return email; }
    public UserName name() { return name; }
    public UserStatus status() { return status; }
}

// ✅ Se precisar de mais comportamento, criar classes colaboradoras
public class UserProfileService {

    public UserProfile createProfile(User user, ProfileData profileData) {
        validateProfileData(profileData);
        return new UserProfile(user.id(), profileData);
    }

    public UserProfile updateProfile(UserProfile profile, ProfileUpdate update) {
        validateProfileUpdate(update);
        return profile.update(update);
    }
}
```

#### ❌ EVITAR - Classes grandes com muitas responsabilidades

```java
// ❌ Classe muito grande (> 50 linhas) com muitas responsabilidades
public class User {
    // Dados do usuário
    private UserId id;
    private Email email;
    private UserName name;
    private UserStatus status;

    // Endereço (deveria ser classe separada)
    private String street;
    private String city;
    private String zipCode;

    // Preferências (deveria ser classe separada)
    private boolean emailNotifications;
    private String language;
    private String timezone;

    // Métodos de usuário
    public void changeEmail(Email newEmail) { /* ... */ }
    public void deactivate() { /* ... */ }

    // Métodos de endereço (responsabilidade misturada)
    public void updateAddress(String street, String city, String zipCode) { /* ... */ }
    public boolean isInSameCity(User otherUser) { /* ... */ }

    // Métodos de preferências (responsabilidade misturada)
    public void enableEmailNotifications() { /* ... */ }
    public void changeLanguage(String language) { /* ... */ }
    public void updateTimezone(String timezone) { /* ... */ }

    // Métodos de validação (responsabilidade misturada)
    public boolean isValidForShipping() { /* ... */ }
    public boolean canReceivePromotions() { /* ... */ }

    // Mais de 50 linhas - viola Object Calisthenics
}
```

### 8. Máximo 2 Variáveis de Instância por Classe

#### ✅ PADRÃO - Classes com poucas variáveis de instância

```java
// ✅ Máximo 2 variáveis de instância
public class User {
    private final UserId id;
    private final UserProfile profile; // Agrupa email, name, status

    public User(UserId id, UserProfile profile) {
        this.id = Objects.requireNonNull(id);
        this.profile = Objects.requireNonNull(profile);
    }

    public User updateProfile(UserProfile newProfile) {
        return new User(id, newProfile);
    }
}

public record UserProfile(Email email, UserName name, UserStatus status) {
    public UserProfile changeEmail(Email newEmail) {
        return new UserProfile(newEmail, name, status);
    }

    public UserProfile deactivate() {
        return new UserProfile(email, name, UserStatus.INACTIVE);
    }
}

// ✅ Service com composição
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProcessor orderProcessor; // Agrupa validação + cálculos

    @Inject
    public OrderService(OrderRepository orderRepository, OrderProcessor orderProcessor) {
        this.orderRepository = orderRepository;
        this.orderProcessor = orderProcessor;
    }

    public Order createOrder(CreateOrderCommand command) {
        var processedOrder = orderProcessor.process(command);
        return orderRepository.save(processedOrder);
    }
}

public class OrderProcessor {
    private final OrderValidator validator;
    private final PriceCalculator priceCalculator;

    // Ainda dentro do limite de 2 variáveis
}
```

#### ❌ EVITAR - Muitas variáveis de instância

```java
// ❌ Mais de 2 variáveis de instância
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ValidationService validationService;
    private final AuditService auditService;
    private final NotificationService notificationService;
    private final SecurityService securityService;

    // Muitas dependências indicam que a classe faz demais
}
```

### 9. Sem Getters/Setters - Expor Comportamento

#### ✅ PADRÃO - Métodos que expressam comportamento

```java
// ✅ Comportamento expressivo sem getters/setters
public class BankAccount {
    private final AccountId id;
    private final Money balance;

    public BankAccount deposit(Money amount) {
        validateDepositAmount(amount);
        var newBalance = balance.add(amount);
        return new BankAccount(id, newBalance);
    }

    public BankAccount withdraw(Money amount) {
        validateWithdrawalAmount(amount);
        var newBalance = balance.subtract(amount);
        return new BankAccount(id, newBalance);
    }

    public boolean canWithdraw(Money amount) {
        return balance.isGreaterThanOrEqualTo(amount);
    }

    public boolean hasBalance() {
        return balance.isGreaterThan(Money.ZERO);
    }

    // ✅ Apenas para necessidades específicas (ex: persistence)
    Money balance() { return balance; } // package-private para repository
}

// ✅ Uso - Cliente usa comportamento, não estado
public class TransferService {

    public TransferResult transfer(BankAccount from, BankAccount to, Money amount) {
        if (!from.canWithdraw(amount)) {
            return TransferResult.insufficientFunds();
        }

        var updatedFrom = from.withdraw(amount);
        var updatedTo = to.deposit(amount);

        return TransferResult.success(updatedFrom, updatedTo);
    }
}
```

#### ❌ EVITAR - Getters/setters que expõem estado

```java
// ❌ Getters/setters expondo estado interno
public class BankAccount {
    private Money balance;

    public Money getBalance() {
        return balance; // Expõe estado
    }

    public void setBalance(Money balance) {
        this.balance = balance; // Permite manipulação externa
    }
}

// ❌ Cliente manipula estado ao invés de usar comportamento
public class TransferService {

    public void transfer(BankAccount from, BankAccount to, Money amount) {
        var fromBalance = from.getBalance(); // Pega estado
        var toBalance = to.getBalance();     // Pega estado

        from.setBalance(fromBalance.subtract(amount)); // Manipula estado
        to.setBalance(toBalance.add(amount));           // Manipula estado
    }
}
```

### Clean Architecture - Camadas e Dependências

Seguir rigorosamente a arquitetura limpa com separação clara de responsabilidades:

#### Camadas e Regras de Dependência

- **Domain** (Entities + Value Objects): Não depende de nada
- **Application** (Use Cases): Depende apenas do Domain
- **Infrastructure** (Repositories + External Services): Depende de Domain e Application
- **Presentation** (Controllers + DTOs): Depende de Domain e Application

#### Estrutura de Pacotes Detalhada

```
src/main/java/com/myproject/
├── domain/                    # 🔵 Camada de Domínio
│   ├── shared/               # Value Objects compartilhados
│   │   ├── Money.java
│   │   ├── Email.java
│   │   └── DomainEvent.java
│   ├── user/                 # Bounded Context: User
│   │   ├── User.java         # Aggregate Root
│   │   ├── UserId.java       # Value Object
│   │   ├── UserName.java     # Value Object
│   │   ├── UserRepository.java # Port (Interface)
│   │   ├── UserDomainService.java # Domain Service
│   │   └── events/
│   │       └── UserCreatedEvent.java
│   └── order/                # Bounded Context: Order
│       ├── Order.java
│       ├── OrderId.java
│       ├── OrderItems.java   # Collection wrapper
│       └── OrderRepository.java
├── application/              # 🟡 Camada de Aplicação
│   ├── user/
│   │   ├── CreateUserUseCase.java
│   │   ├── CreateUserCommand.java
│   │   ├── CreateUserResult.java
│   │   └── UserApplicationService.java
│   ├── order/
│   │   ├── CreateOrderUseCase.java
│   │   └── CreateOrderCommand.java
│   └── shared/
│       ├── UseCase.java      # Interface base
│       └── DomainEventPublisher.java # Port
├── infrastructure/           # 🟠 Camada de Infraestrutura
│   ├── persistence/
│   │   ├── jpa/
│   │   │   ├── UserEntity.java
│   │   │   ├── OrderEntity.java
│   │   │   └── OrderItemEntity.java
│   │   ├── UserRepositoryImpl.java
│   │   └── OrderRepositoryImpl.java
│   ├── messaging/
│   │   ├── EventPublisherImpl.java
│   │   └── MessageProducer.java
│   ├── external/
│   │   ├── PaymentServiceImpl.java
│   │   ├── EmailServiceImpl.java
│   │   └── clients/
│   │       └── PaymentClient.java
│   └── configuration/
│       ├── PersistenceConfiguration.java
│       └── MessagingConfiguration.java
└── presentation/            # 🟢 Camada de Apresentação
    ├── rest/
    │   ├── UserController.java
    │   ├── OrderController.java
    │   └── GlobalExceptionHandler.java
    ├── dto/
    │   ├── request/
    │   │   ├── CreateUserRequest.java
    │   │   └── CreateOrderRequest.java
    │   ├── response/
    │   │   ├── UserResponse.java
    │   │   ├── OrderResponse.java
    │   │   └── ErrorResponse.java
    │   └── mapper/
    │       ├── UserMapper.java
    │       └── OrderMapper.java
```

#### Exemplo de Implementação por Camada

##### ✅ Domain Layer

```java
// Domain Entity - Rica em comportamento
public class Order {
    private final OrderId id;
    private final CustomerId customerId;
    private final OrderItems items;
    private final OrderStatus status;
    private final List<DomainEvent> events = new ArrayList<>();

    // Factory method
    public static Order create(CustomerId customerId) {
        var order = new Order(
            OrderId.generate(),
            customerId,
            OrderItems.empty(),
            OrderStatus.DRAFT
        );

        order.addEvent(new OrderCreatedEvent(order.id(), customerId));
        return order;
    }

    // Business behavior
    public Order addItem(ProductId productId, int quantity, Money unitPrice) {
        validateCanAddItem();

        var newItem = new OrderItem(productId, quantity, unitPrice);
        var updatedItems = items.add(newItem);
        var updatedOrder = new Order(id, customerId, updatedItems, status);

        updatedOrder.addEvent(new ItemAddedToOrderEvent(id, productId, quantity));
        return updatedOrder;
    }

    public Order confirm() {
        validateCanConfirm();

        var confirmedOrder = new Order(id, customerId, items, OrderStatus.CONFIRMED);
        confirmedOrder.addEvent(new OrderConfirmedEvent(id, items.totalValue()));
        return confirmedOrder;
    }

    private void validateCanAddItem() {
        if (status != OrderStatus.DRAFT) {
            throw new OrderNotEditableException(id, status);
        }
    }
}
```

##### ✅ Application Layer

```java
// Use Case - Orquestracao sem regras de negócio
@ApplicationScoped
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final DomainEventPublisher eventPublisher;

    @Transactional
    public CreateOrderResult execute(CreateOrderCommand command) {
        // Validações de entrada
        var customerId = new CustomerId(command.customerId());

        // Verificar se customer existe
        var customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(customerId));

        // Usar factory method do domain
        var order = Order.create(customerId);

        // Persistir
        var savedOrder = orderRepository.save(order);

        // Publicar eventos
        savedOrder.getUncommittedEvents()
            .forEach(eventPublisher::publish);

        return new CreateOrderResult.Success(savedOrder);
    }
}
```

##### ✅ Infrastructure Layer

```java
// Repository Implementation - Adapter
@ApplicationScoped
public class OrderRepositoryImpl implements OrderRepository {

    @Inject
    EntityManager entityManager;

    @Override
    public Order save(Order order) {
        var entity = toEntity(order);
        entityManager.persist(entity);
        entityManager.flush();

        return toDomain(entity);
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return entityManager.find(OrderEntity.class, id.value())
            .map(this::toDomain);
    }

    // Mappers - Conversion between Domain and Infrastructure
    private OrderEntity toEntity(Order order) {
        var entity = new OrderEntity();
        entity.id = order.id().value();
        entity.customerId = order.customerId().value();
        entity.status = order.status().name();
        entity.items = order.items().stream()
            .map(this::toItemEntity)
            .collect(Collectors.toList());
        return entity;
    }

    private Order toDomain(OrderEntity entity) {
        var items = entity.items.stream()
            .map(this::toDomainItem)
            .collect(Collectors.toList());

        return new Order(
            new OrderId(entity.id),
            new CustomerId(entity.customerId),
            new OrderItems(items),
            OrderStatus.valueOf(entity.status)
        );
    }
}
```

##### ✅ Presentation Layer

```java
// Controller - Fino, apenas coordenação
@Path("/api/v1/orders")
@ApplicationScoped
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final OrderMapper orderMapper;

    @POST
    public Response createOrder(@Valid CreateOrderRequest request) {
        var command = orderMapper.toCommand(request);
        var result = createOrderUseCase.execute(command);

        return switch (result) {
            case CreateOrderResult.Success(var order) -> {
                var response = orderMapper.toResponse(order);
                yield Response.status(201).entity(response).build();
            }
            case CreateOrderResult.CustomerNotFound(var customerId) ->
                Response.status(404).entity(
                    new ErrorResponse("Customer not found: " + customerId)
                ).build();

            case CreateOrderResult.ValidationError(var errors) ->
                Response.status(400).entity(
                    new ErrorResponse("Validation failed", errors)
                ).build();
        };
    }
}
```

### Native Compilation - Quarkus Específico

```java
// ✅ PADRÃO - Configurações para native compilation
@RegisterForReflection({
    UserEntity.class,
    OrderEntity.class,
    CreateUserRequest.class,
    UserResponse.class
})
public class NativeConfiguration {
}

// ✅ Substituir reflection por configuração explícita
@ApplicationScoped
public class JsonConfiguration {

    @Produces
    @Singleton
    public ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // ✅ Configuração explícita para native
        mapper.registerSubtypes(
            CreateUserResult.Success.class,
            CreateUserResult.ValidationError.class,
            CreateUserResult.SystemError.class
        );

        return mapper;
    }
}

// ✅ Resource substitution para native
@Substitution
@TargetClass(SomeExternalLibraryClass.class)
public class SomeExternalLibrarySubstitution {

    @Alias
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.FromAlias)
    private static String configuredValue = System.getProperty("app.external.config");

    @Substitute
    public String getConfigValue() {
        return configuredValue;
    }
}
```

## Error Handling - Patterns Profissionais

### Result/Either Pattern - Tipo-Segurança para Erros

#### ✅ PADRÃO - Result types com sealed interfaces

```java
// ✅ Result pattern para operações que podem falhar
public sealed interface Result<T, E> permits Result.Success, Result.Failure {

    record Success<T, E>(T value) implements Result<T, E> {}
    record Failure<T, E>(E error) implements Result<T, E> {}

    // Factory methods
    static <T, E> Result<T, E> success(T value) {
        return new Success<>(value);
    }

    static <T, E> Result<T, E> failure(E error) {
        return new Failure<>(error);
    }

    // Utility methods
    default boolean isSuccess() {
        return this instanceof Success;
    }

    default boolean isFailure() {
        return this instanceof Failure;
    }

    default T getValueOrThrow() {
        return switch (this) {
            case Success<T, E> success -> success.value();
            case Failure<T, E> failure -> throw new ResultException(failure.error());
        };
    }

    default <U> Result<U, E> map(Function<T, U> mapper) {
        return switch (this) {
            case Success<T, E> success -> Result.success(mapper.apply(success.value()));
            case Failure<T, E> failure -> Result.failure(failure.error());
        };
    }

    default <U> Result<U, E> flatMap(Function<T, Result<U, E>> mapper) {
        return switch (this) {
            case Success<T, E> success -> mapper.apply(success.value());
            case Failure<T, E> failure -> Result.failure(failure.error());
        };
    }
}

// ✅ Domain-specific error types
public sealed interface UserError permits
    UserError.EmailAlreadyExists,
    UserError.InvalidEmailFormat,
    UserError.UserNotFound,
    UserError.InsufficientPermissions {

    record EmailAlreadyExists(Email email) implements UserError {}
    record InvalidEmailFormat(String email) implements UserError {}
    record UserNotFound(UserId userId) implements UserError {}
    record InsufficientPermissions(UserId userId, String operation) implements UserError {}
}

// ✅ Use Case com Result pattern
@ApplicationScoped
public class CreateUserUseCase {

    public Result<User, UserError> execute(CreateUserCommand command) {
        // Validação de formato
        var emailResult = validateEmailFormat(command.email());
        if (emailResult.isFailure()) {
            return Result.failure(emailResult.error());
        }

        var email = emailResult.value();

        // Verificação de duplicação
        if (userRepository.existsByEmail(email)) {
            return Result.failure(new UserError.EmailAlreadyExists(email));
        }

        // Criação bem-sucedida
        var user = User.create(email, new UserName(command.name()));
        var savedUser = userRepository.save(user);

        return Result.success(savedUser);
    }

    private Result<Email, UserError> validateEmailFormat(String emailString) {
        try {
            var email = new Email(emailString);
            return Result.success(email);
        } catch (IllegalArgumentException e) {
            return Result.failure(new UserError.InvalidEmailFormat(emailString));
        }
    }
}

// ✅ Controller usando Result pattern
@POST
public Response createUser(@Valid CreateUserRequest request) {
    var command = userMapper.toCommand(request);
    var result = createUserUseCase.execute(command);

    return switch (result) {
        case Result.Success<User, UserError> success -> {
            var response = userMapper.toResponse(success.value());
            yield Response.status(201).entity(response).build();
        }

        case Result.Failure<User, UserError> failure -> switch (failure.error()) {
            case UserError.EmailAlreadyExists(var email) ->
                Response.status(409).entity(new ErrorResponse(
                    "Email already exists",
                    List.of("Email " + email.value() + " is already registered")
                )).build();

            case UserError.InvalidEmailFormat(var email) ->
                Response.status(400).entity(new ErrorResponse(
                    "Invalid email format",
                    List.of("The email '" + email + "' is not in a valid format")
                )).build();

            case UserError.UserNotFound(var userId) ->
                Response.status(404).entity(new ErrorResponse(
                    "User not found",
                    List.of("User with ID " + userId.value() + " was not found")
                )).build();

            case UserError.InsufficientPermissions(var userId, var operation) ->
                Response.status(403).entity(new ErrorResponse(
                    "Insufficient permissions",
                    List.of("User " + userId.value() + " cannot perform operation: " + operation)
                )).build();
        };
    };
}
```

### Exception Handling - Quando Usar

#### ✅ PADRÃO - Exceptions para condições excepcionais

```java
// ✅ Runtime exceptions para violações de invariantes
public class DomainException extends RuntimeException {
    protected DomainException(String message) {
        super(message);
    }

    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

// ✅ Exceptions específicas do domínio
public class OrderNotEditableException extends DomainException {
    private final OrderId orderId;
    private final OrderStatus currentStatus;

    public OrderNotEditableException(OrderId orderId, OrderStatus currentStatus) {
        super(String.format("Order %s cannot be modified in status %s",
            orderId.value(), currentStatus));
        this.orderId = orderId;
        this.currentStatus = currentStatus;
    }

    public OrderId orderId() { return orderId; }
    public OrderStatus currentStatus() { return currentStatus; }
}

// ✅ Value Object que falha fast
public record Email(String value) {
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public Email {
        // Exception apropriada - violação de invariante
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
    }
}
```

### Validation Patterns

#### ✅ PADRÃO - Bean Validation com grupos

```java
// ✅ Validation groups para diferentes contextos
public interface CreateUser {}
public interface UpdateUser {}

public record CreateUserRequest(
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name,

    @NotBlank(message = "Email is required", groups = {CreateUser.class, UpdateUser.class})
    @Email(message = "Invalid email format", groups = {CreateUser.class, UpdateUser.class})
    String email,

    @NotNull(message = "Age is required", groups = CreateUser.class)
    @Min(value = 18, message = "Must be at least 18 years old", groups = CreateUser.class)
    @Max(value = 120, message = "Must be less than 120 years old", groups = CreateUser.class)
    Integer age
) {}

// ✅ Custom validator
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "Email already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

@ApplicationScoped
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Inject
    UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return true; // Let @NotBlank handle null/blank
        }

        try {
            var emailVO = new Email(email);
            return !userRepository.existsByEmail(emailVO);
        } catch (IllegalArgumentException e) {
            return true; // Let @Email handle format validation
        }
    }
}

// ✅ Validation no controller com grupos
@POST
public Response createUser(@Valid @ConvertGroup(to = CreateUser.class) CreateUserRequest request) {
    // Validation automática pelo Bean Validation
    var command = userMapper.toCommand(request);
    var result = createUserUseCase.execute(command);

    return handleResult(result);
}

@PUT
@Path("/{id}")
public Response updateUser(
        @PathParam("id") UUID id,
        @Valid @ConvertGroup(to = UpdateUser.class) UpdateUserRequest request) {
    // Diferentes validações para update
    var command = userMapper.toUpdateCommand(id, request);
    var result = updateUserUseCase.execute(command);

    return handleResult(result);
}
```

### Global Exception Handling

#### ✅ PADRÃO - Exception handlers centralizados

```java
// ✅ Global exception handler
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public Response toResponse(Exception exception) {
        return switch (exception) {
            case ValidationException ve -> handleValidationException(ve);
            case DomainException de -> handleDomainException(de);
            case ConstraintViolationException cve -> handleConstraintViolation(cve);
            case SecurityException se -> handleSecurityException(se);
            case IllegalArgumentException iae -> handleIllegalArgument(iae);
            default -> handleGenericException(exception);
        };
    }

    private Response handleValidationException(ValidationException ex) {
        LOG.warn("Validation error: {}", ex.getMessage());

        var errors = ex.getConstraintViolations().stream()
            .map(violation -> String.format("%s: %s",
                violation.getPropertyPath(), violation.getMessage()))
            .collect(Collectors.toList());

        return Response.status(400)
            .entity(new ErrorResponse("Validation failed", errors))
            .build();
    }

    private Response handleDomainException(DomainException ex) {
        LOG.warn("Domain error: {}", ex.getMessage());

        return switch (ex) {
            case OrderNotEditableException onee -> Response.status(409)
                .entity(new ErrorResponse("Order cannot be modified",
                    List.of(ex.getMessage())))
                .build();

            case UserAlreadyExistsException uaee -> Response.status(409)
                .entity(new ErrorResponse("User already exists",
                    List.of(ex.getMessage())))
                .build();

            default -> Response.status(400)
                .entity(new ErrorResponse("Business rule violation",
                    List.of(ex.getMessage())))
                .build();
        };
    }

    private Response handleGenericException(Exception ex) {
        var errorId = UUID.randomUUID().toString();
        LOG.error("Unexpected error [{}]: {}", errorId, ex.getMessage(), ex);

        return Response.status(500)
            .entity(new ErrorResponse(
                "Internal server error",
                List.of("Error ID: " + errorId + " - Please contact support")
            ))
            .build();
    }
}

// ✅ Error response padronizado
public record ErrorResponse(
    String message,
    List<String> details,
    LocalDateTime timestamp,
    String path
) {
    public ErrorResponse(String message, List<String> details) {
        this(message, details, LocalDateTime.now(), null);
    }

    public ErrorResponse withPath(String path) {
        return new ErrorResponse(message, details, timestamp, path);
    }
}
```

### Circuit Breaker Pattern

#### ✅ PADRÃO - Resilience com MicroProfile Fault Tolerance

```java
// ✅ Circuit breaker para serviços externos
@ApplicationScoped
public class ExternalPaymentService {

    @Inject
    @RestClient
    PaymentServiceClient paymentClient;

    @CircuitBreaker(
        requestVolumeThreshold = 4,
        failureRatio = 0.75,
        delay = 1000,
        successThreshold = 2
    )
    @Retry(
        maxRetries = 3,
        delay = 200,
        jitter = 50
    )
    @Timeout(2000)
    @Fallback(fallbackMethod = "fallbackPayment")
    public Result<PaymentResult, PaymentError> processPayment(PaymentRequest request) {
        try {
            var response = paymentClient.processPayment(request);
            return Result.success(new PaymentResult(response.transactionId(), response.status()));

        } catch (WebApplicationException e) {
            return switch (e.getResponse().getStatus()) {
                case 400 -> Result.failure(new PaymentError.InvalidRequest(request.toString()));
                case 402 -> Result.failure(new PaymentError.InsufficientFunds(request.amount()));
                case 404 -> Result.failure(new PaymentError.PaymentMethodNotFound(request.paymentMethodId()));
                default -> Result.failure(new PaymentError.ServiceError(e.getMessage()));
            };

        } catch (ProcessingException e) {
            return Result.failure(new PaymentError.NetworkError(e.getMessage()));
        }
    }

    public Result<PaymentResult, PaymentError> fallbackPayment(PaymentRequest request) {
        // Fallback strategy - maybe queue for later processing
        LOG.warn("Payment service unavailable, queueing payment for later processing");

        paymentQueue.enqueue(request);

        return Result.success(new PaymentResult(
            "QUEUED-" + UUID.randomUUID(),
            PaymentStatus.PENDING
        ));
    }
}

// ✅ Monitoring de circuit breaker
@ApplicationScoped
public class CircuitBreakerHealthCheck implements HealthCheck {

    @Inject
    @CircuitBreakerName("ExternalPaymentService/processPayment")
    CircuitBreaker circuitBreaker;

    @Override
    public HealthCheckResponse call() {
        var state = circuitBreaker.getState();

        return switch (state) {
            case CLOSED -> HealthCheckResponse.up("payment-service")
                .withData("circuit-breaker", "CLOSED")
                .withData("failure-rate", circuitBreaker.getFailureRate())
                .build();

            case OPEN -> HealthCheckResponse.down("payment-service")
                .withData("circuit-breaker", "OPEN")
                .withData("failure-rate", circuitBreaker.getFailureRate())
                .build();

            case HALF_OPEN -> HealthCheckResponse.up("payment-service")
                .withData("circuit-breaker", "HALF_OPEN")
                .withData("failure-rate", circuitBreaker.getFailureRate())
                .build();
        };
    }
}
```

## Logging Patterns - Observabilidade Profissional

### Structured Logging - Formato Consistente

#### ✅ PADRÃO - Logging estruturado com contexto

```java
// ✅ Logger com contexto estruturado
@ApplicationScoped
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public CreateUserResult createUser(CreateUserCommand command) {
        var correlationId = UUID.randomUUID().toString();

        LOG.info("Starting user creation process",
            kv("correlationId", correlationId),
            kv("action", "CREATE_USER"),
            kv("emailDomain", extractDomain(command.email())));

        try {
            var result = executeUserCreation(command);

            switch (result) {
                case CreateUserResult.Success success -> {
                    LOG.info("User created successfully",
                        kv("correlationId", correlationId),
                        kv("userId", success.user().id().value()),
                        kv("action", "CREATE_USER_SUCCESS"),
                        kv("processingTime", calculateProcessingTime()));
                }

                case CreateUserResult.ValidationError error -> {
                    LOG.warn("User creation failed - validation error",
                        kv("correlationId", correlationId),
                        kv("action", "CREATE_USER_VALIDATION_FAILED"),
                        kv("errors", String.join(", ", error.errors())),
                        kv("emailAttempted", maskEmail(command.email())));
                }

                case CreateUserResult.SystemError systemError -> {
                    LOG.error("User creation failed - system error",
                        kv("correlationId", correlationId),
                        kv("action", "CREATE_USER_SYSTEM_ERROR"),
                        kv("errorMessage", systemError.message()),
                        kv("emailAttempted", maskEmail(command.email())));
                }
            }

            return result;

        } catch (Exception e) {
            LOG.error("Unexpected error during user creation",
                kv("correlationId", correlationId),
                kv("action", "CREATE_USER_UNEXPECTED_ERROR"),
                kv("errorClass", e.getClass().getSimpleName()),
                kv("errorMessage", e.getMessage()),
                e);

            throw new UserServiceException("User creation failed", e);
        }
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***";
        }

        var parts = email.split("@");
        var localPart = parts[0];
        var domain = parts[1];

        var maskedLocal = localPart.length() > 3
            ? localPart.substring(0, 2) + "***" + localPart.substring(localPart.length() - 1)
            : "***";

        return maskedLocal + "@" + domain;
    }
}
```

### MDC (Mapped Diagnostic Context) - Rastreamento

#### ✅ PADRÃO - Contexto distribuído com MDC

```java
// ✅ Filter para configurar MDC
@Provider
@Priority(Priorities.AUTHENTICATION)
public class LoggingContextFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String REQUEST_ID_HEADER = "X-Request-ID";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        var correlationId = getOrGenerateCorrelationId(requestContext);
        var requestId = UUID.randomUUID().toString();
        var userId = extractUserId(requestContext);

        // Configurar MDC para toda a thread
        MDC.put("correlationId", correlationId);
        MDC.put("requestId", requestId);
        MDC.put("userId", userId);
        MDC.put("endpoint", requestContext.getUriInfo().getPath());
        MDC.put("method", requestContext.getMethod());
        MDC.put("userAgent", requestContext.getHeaderString("User-Agent"));

        // Adicionar ao response para facilitar debugging
        requestContext.setProperty("correlationId", correlationId);
        requestContext.setProperty("requestId", requestId);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        var correlationId = (String) requestContext.getProperty("correlationId");
        var requestId = (String) requestContext.getProperty("requestId");

        // Adicionar headers de resposta
        responseContext.getHeaders().add(CORRELATION_ID_HEADER, correlationId);
        responseContext.getHeaders().add(REQUEST_ID_HEADER, requestId);

        // Limpar MDC ao final
        MDC.clear();
    }

    private String getOrGenerateCorrelationId(ContainerRequestContext requestContext) {
        var existingId = requestContext.getHeaderString(CORRELATION_ID_HEADER);
        return existingId != null ? existingId : UUID.randomUUID().toString();
    }
}

// ✅ Service usando MDC automaticamente
@ApplicationScoped
public class OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);

    public Order createOrder(CreateOrderCommand command) {
        // MDC já configurado pelo filter - logs automaticamente incluem contexto
        LOG.info("Creating order for customer",
            kv("customerId", command.customerId()),
            kv("itemCount", command.items().size()),
            kv("action", "CREATE_ORDER_START"));

        var order = processOrder(command);

        LOG.info("Order created successfully",
            kv("orderId", order.id().value()),
            kv("totalValue", order.totalValue().amount()),
            kv("action", "CREATE_ORDER_SUCCESS"));

        return order;
    }
}
```

### Performance Logging - Métricas e Timing

#### ✅ PADRÃO - Performance logging detalhado

```java
// ✅ Interceptor para logging de performance
@Interceptor
@PerformanceLogged
@Priority(Interceptor.Priority.APPLICATION)
public class PerformanceLoggingInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(PerformanceLoggingInterceptor.class);

    @AroundInvoke
    public Object logPerformance(InvocationContext context) throws Exception {
        var startTime = System.nanoTime();
        var methodName = context.getMethod().getName();
        var className = context.getTarget().getClass().getSimpleName();

        LOG.debug("Method execution started",
            kv("class", className),
            kv("method", methodName),
            kv("action", "METHOD_START"));

        try {
            var result = context.proceed();
            var duration = Duration.ofNanos(System.nanoTime() - startTime);

            LOG.info("Method execution completed",
                kv("class", className),
                kv("method", methodName),
                kv("duration", duration.toMillis() + "ms"),
                kv("action", "METHOD_SUCCESS"));

            // Log warning para métodos lentos
            if (duration.toMillis() > 1000) {
                LOG.warn("Slow method execution detected",
                    kv("class", className),
                    kv("method", methodName),
                    kv("duration", duration.toMillis() + "ms"),
                    kv("threshold", "1000ms"),
                    kv("action", "SLOW_METHOD"));
            }

            return result;

        } catch (Exception e) {
            var duration = Duration.ofNanos(System.nanoTime() - startTime);

            LOG.error("Method execution failed",
                kv("class", className),
                kv("method", methodName),
                kv("duration", duration.toMillis() + "ms"),
                kv("errorClass", e.getClass().getSimpleName()),
                kv("errorMessage", e.getMessage()),
                kv("action", "METHOD_ERROR"),
                e);

            throw e;
        }
    }
}

// ✅ Annotation para marcar métodos
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PerformanceLogged {
}

// ✅ Uso em services críticos
@ApplicationScoped
@PerformanceLogged
public class PaymentService {

    public PaymentResult processPayment(PaymentRequest request) {
        // Automatically logged com timing
        return executePayment(request);
    }
}
```

### Security Logging - Auditoria e Compliance

#### ✅ PADRÃO - Security events logging

```java
// ✅ Security event logging
@ApplicationScoped
public class SecurityEventLogger {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityEventLogger.class);
    private static final Logger AUDIT_LOG = LoggerFactory.getLogger("AUDIT");

    public void logAuthenticationAttempt(String email, String ipAddress, boolean success) {
        if (success) {
            LOG.info("User authentication successful",
                kv("email", maskEmail(email)),
                kv("ipAddress", ipAddress),
                kv("action", "AUTH_SUCCESS"),
                kv("timestamp", Instant.now()));
        } else {
            LOG.warn("User authentication failed",
                kv("email", maskEmail(email)),
                kv("ipAddress", ipAddress),
                kv("action", "AUTH_FAILED"),
                kv("timestamp", Instant.now()));
        }

        // Audit log separado para compliance
        AUDIT_LOG.info("Authentication attempt",
            kv("event", "AUTHENTICATION"),
            kv("result", success ? "SUCCESS" : "FAILURE"),
            kv("subject", hashEmail(email)),
            kv("source", ipAddress),
            kv("timestamp", Instant.now()));
    }

    public void logAuthorizationFailure(String userId, String resource, String action, String ipAddress) {
        LOG.warn("Authorization denied",
            kv("userId", userId),
            kv("resource", resource),
            kv("action", action),
            kv("ipAddress", ipAddress),
            kv("event", "AUTHORIZATION_DENIED"),
            kv("timestamp", Instant.now()));

        AUDIT_LOG.warn("Authorization failure",
            kv("event", "AUTHORIZATION_DENIED"),
            kv("subject", userId),
            kv("resource", resource),
            kv("action", action),
            kv("source", ipAddress),
            kv("timestamp", Instant.now()));
    }

    public void logDataAccess(String userId, String dataType, String operation, List<String> recordIds) {
        AUDIT_LOG.info("Data access event",
            kv("event", "DATA_ACCESS"),
            kv("subject", userId),
            kv("dataType", dataType),
            kv("operation", operation),
            kv("recordCount", recordIds.size()),
            kv("recordIds", recordIds.stream().collect(Collectors.joining(","))),
            kv("timestamp", Instant.now()));
    }

    public void logSensitiveOperation(String userId, String operation, Map<String, Object> context) {
        LOG.info("Sensitive operation performed",
            kv("userId", userId),
            kv("operation", operation),
            kv("action", "SENSITIVE_OPERATION"));

        var contextJson = serializeContext(context);
        AUDIT_LOG.info("Sensitive operation audit",
            kv("event", "SENSITIVE_OPERATION"),
            kv("subject", userId),
            kv("operation", operation),
            kv("context", contextJson),
            kv("timestamp", Instant.now()));
    }

    private String hashEmail(String email) {
        // Hash para compliance sem expor dados pessoais
        return DigestUtils.sha256Hex(email);
    }
}

// ✅ Interceptor para operações sensíveis
@Interceptor
@SensitiveOperation
@Priority(Interceptor.Priority.APPLICATION + 10)
public class SensitiveOperationInterceptor {

    @Inject
    SecurityEventLogger securityEventLogger;

    @Inject
    SecurityContext securityContext;

    @AroundInvoke
    public Object logSensitiveOperation(InvocationContext context) throws Exception {
        var userId = securityContext.getUserPrincipal().getName();
        var operation = context.getMethod().getName();
        var contextData = extractContextData(context);

        securityEventLogger.logSensitiveOperation(userId, operation, contextData);

        return context.proceed();
    }

    private Map<String, Object> extractContextData(InvocationContext context) {
        var params = context.getParameters();
        var paramNames = context.getMethod().getParameterTypes();

        var contextMap = new HashMap<String, Object>();
        for (int i = 0; i < params.length; i++) {
            var param = params[i];
            if (param instanceof HasId<?> hasId) {
                contextMap.put("entityId", hasId.getId());
            }
        }

        return contextMap;
    }
}
```

### Correlation Logging - Distributed Tracing

#### ✅ PADRÃO - Distributed tracing setup

```java
// ✅ Tracing configuration
@ApplicationScoped
public class TracingConfiguration {

    @Produces
    @Singleton
    public Tracer tracer() {
        return GlobalOpenTelemetry.getTracer("my-application", "1.0.0");
    }
}

// ✅ Service com distributed tracing
@ApplicationScoped
public class UserService {

    @Inject
    Tracer tracer;

    @Inject
    UserRepository userRepository;

    public User createUser(CreateUserCommand command) {
        var span = tracer.spanBuilder("user.create")
            .setAttribute("user.email.domain", extractDomain(command.email()))
            .setAttribute("operation", "create_user")
            .startSpan();

        try (var scope = span.makeCurrent()) {
            LOG.info("Creating user",
                kv("traceId", span.getSpanContext().getTraceId()),
                kv("spanId", span.getSpanContext().getSpanId()));

            var user = User.create(
                new Email(command.email()),
                new UserName(command.name())
            );

            span.setAttribute("user.id", user.id().value().toString());
            span.setStatus(StatusCode.OK);

            var savedUser = userRepository.save(user);

            span.addEvent("user.saved",
                Attributes.of(
                    AttributeKey.stringKey("user.id"), savedUser.id().value().toString(),
                    AttributeKey.stringKey("user.status"), savedUser.status().name()
                ));

            return savedUser;

        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR, e.getMessage());
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
}
```

### Log Configuration - Structured Output

#### ✅ PADRÃO - Configuração otimizada

```properties
# application.properties - Logging configuration
# Console logging com formato estruturado
quarkus.log.console.format=%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p [%c{3.}] (%t) [%X{correlationId}] %s%e%n
quarkus.log.console.level=INFO
quarkus.log.console.color=false

# File logging para produção
quarkus.log.file.enable=true
quarkus.log.file.path=logs/application.log
quarkus.log.file.format=%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p [%c] (%t) [%X{correlationId},%X{requestId},%X{userId}] %s%e%n
quarkus.log.file.rotation.max-file-size=10M
quarkus.log.file.rotation.max-backup-index=5

# JSON logging para ELK stack
quarkus.log.handler.gelf.enabled=true
quarkus.log.handler.gelf.host=graylog.company.com
quarkus.log.handler.gelf.port=12201
quarkus.log.handler.gelf.include-full-mdc=true

# Log levels específicos
quarkus.log.category."com.myproject.domain".level=DEBUG
quarkus.log.category."com.myproject.application".level=INFO
quarkus.log.category."com.myproject.infrastructure.persistence".level=WARN
quarkus.log.category."org.hibernate".level=WARN
quarkus.log.category."AUDIT".level=INFO

# Async logging para performance
quarkus.log.async=true
quarkus.log.async.queue-length=512
quarkus.log.async.overflow=BLOCK
```

## Performance Patterns - Otimizações Profissionais

### Lazy Loading e Caching

#### ✅ PADRÃO - Cache strategy com Quarkus Cache

```java
// ✅ Repository com cache inteligente
@ApplicationScoped
public class UserRepository {

    @Inject
    EntityManager entityManager;

    // Cache por ID - long TTL para dados que raramente mudam
    @CacheResult(cacheName = "user-by-id")
    public Optional<User> findById(@CacheKey UserId id) {
        LOG.debug("Fetching user from database", kv("userId", id.value()));

        return entityManager.find(UserEntity.class, id.value())
            .map(this::toDomain);
    }

    // Cache invalidation quando user é modificado
    @CacheInvalidate(cacheName = "user-by-id")
    public User save(@CacheKey("id") User user) {
        var entity = toEntity(user);
        entityManager.merge(entity);
        entityManager.flush();

        // Invalidar caches relacionados
        cacheManager.getCache("user-stats").invalidate(user.id().value());

        return toDomain(entity);
    }

    // Cache por email com TTL menor (dados mais voláteis)
    @CacheResult(cacheName = "user-by-email", lockTimeout = 1000)
    public Optional<User> findByEmail(@CacheKey Email email) {
        var query = """
            SELECT u FROM UserEntity u
            WHERE u.email = :email
            AND u.status = 'ACTIVE'
            """;

        return entityManager.createQuery(query, UserEntity.class)
            .setParameter("email", email.value())
            .getResultStream()
            .findFirst()
            .map(this::toDomain);
    }

    // Cache com computation - expensive operations
    @CacheResult(cacheName = "user-stats")
    public UserStatistics calculateUserStatistics(@CacheKey UserId userId) {
        LOG.info("Computing user statistics - expensive operation",
            kv("userId", userId.value()));

        var totalOrders = countUserOrders(userId);
        var totalSpent = calculateTotalSpent(userId);
        var averageOrderValue = totalOrders > 0
            ? totalSpent.divide(BigDecimal.valueOf(totalOrders))
            : BigDecimal.ZERO;

        return new UserStatistics(totalOrders, totalSpent, averageOrderValue);
    }
}

// ✅ Cache configuration
@ApplicationScoped
public class CacheConfiguration {

    @CacheResult(cacheName = "user-by-id")
    @CacheName("user-by-id")
    public void configureUserCache() {
        // Configuration via application.properties
    }

    // Programmatic cache management
    @Inject
    @CacheName("user-stats")
    Cache cache;

    public void warmUpCache() {
        // Pre-load frequently accessed data
        var activeUsers = userRepository.findActiveUsers();

        activeUsers.parallelStream()
            .forEach(user -> {
                try {
                    userRepository.calculateUserStatistics(user.id());
                    Thread.sleep(10); // Rate limiting
                } catch (Exception e) {
                    LOG.warn("Failed to warm up cache for user",
                        kv("userId", user.id().value()), e);
                }
            });
    }
}

// ✅ Cache configuration properties
# application.properties
quarkus.cache.caffeine."user-by-id".initial-capacity=100
quarkus.cache.caffeine."user-by-id".maximum-size=1000
quarkus.cache.caffeine."user-by-id".expire-after-write=PT1H

quarkus.cache.caffeine."user-by-email".initial-capacity=50
quarkus.cache.caffeine."user-by-email".maximum-size=500
quarkus.cache.caffeine."user-by-email".expire-after-write=PT10M

quarkus.cache.caffeine."user-stats".initial-capacity=200
quarkus.cache.caffeine."user-stats".maximum-size=2000
quarkus.cache.caffeine."user-stats".expire-after-write=PT30M
```

### Async Processing - Non-blocking Operations

#### ✅ PADRÃO - Reactive processing

```java
// ✅ Reactive service para operações I/O intensivas
@ApplicationScoped
public class NotificationService {

    @Inject
    @RestClient
    ReactiveEmailClient emailClient;

    @Inject
    @RestClient
    ReactiveSmsClient smsClient;

    // Async notification com multiple channels
    public Uni<NotificationResult> sendNotification(NotificationRequest request) {
        var emailUni = sendEmail(request)
            .onFailure().recoverWithItem(this::handleEmailFailure);

        var smsUni = sendSms(request)
            .onFailure().recoverWithItem(this::handleSmsFailure);

        // Combine both operations - don't wait for both to succeed
        return Uni.combine().all().unis(emailUni, smsUni)
            .combinedWith((emailResult, smsResult) ->
                new NotificationResult(emailResult, smsResult));
    }

    private Uni<EmailResult> sendEmail(NotificationRequest request) {
        var emailRequest = new EmailRequest(
            request.recipientEmail(),
            request.subject(),
            request.template(),
            request.variables()
        );

        return emailClient.sendEmail(emailRequest)
            .invoke(response -> LOG.info("Email sent successfully",
                kv("recipient", request.recipientEmail()),
                kv("messageId", response.messageId())))
            .onFailure().invoke(failure -> LOG.error("Failed to send email",
                kv("recipient", request.recipientEmail()),
                kv("error", failure.getMessage()),
                failure));
    }

    private Uni<SmsResult> sendSms(NotificationRequest request) {
        if (request.phoneNumber() == null) {
            return Uni.createFrom().item(SmsResult.skipped("No phone number"));
        }

        var smsRequest = new SmsRequest(
            request.phoneNumber(),
            request.smsMessage()
        );

        return smsClient.sendSms(smsRequest)
            .invoke(response -> LOG.info("SMS sent successfully",
                kv("phoneNumber", maskPhoneNumber(request.phoneNumber())),
                kv("messageId", response.messageId())));
    }
}

// ✅ Async processing com background jobs
@ApplicationScoped
public class OrderProcessingService {

    @Inject
    @Channel("order-events")
    Emitter<OrderEvent> orderEventEmitter;

    @ConsumeEvent("order-created")
    @Blocking // Para operações que precisam bloquear
    public void processOrderCreated(OrderCreatedEvent event) {
        LOG.info("Processing order created event",
            kv("orderId", event.orderId().value()),
            kv("customerId", event.customerId().value()));

        try {
            // Operações síncronas necessárias
            validateInventory(event.orderId());
            reserveStock(event.orderId());

            // Emit próximo evento para pipeline assíncrono
            orderEventEmitter.send(new OrderValidatedEvent(event.orderId()));

        } catch (InsufficientStockException e) {
            orderEventEmitter.send(new OrderRejectedEvent(
                event.orderId(),
                "Insufficient stock"
            ));
        }
    }

    @ConsumeEvent("order-validated")
    public Uni<Void> processOrderValidated(OrderValidatedEvent event) {
        return processPayment(event.orderId())
            .chain(paymentResult -> {
                if (paymentResult.isSuccess()) {
                    return confirmOrder(event.orderId());
                } else {
                    return rejectOrder(event.orderId(), paymentResult.error());
                }
            })
            .replaceWithVoid();
    }
}
```

### Memory Optimization - Resource Management

#### ✅ PADRÃO - Efficient data processing

```java
// ✅ Stream processing para large datasets
@ApplicationScoped
public class ReportService {

    @Inject
    EntityManager entityManager;

    // Process large datasets without loading all in memory
    public void generateUserReport(UserReportRequest request) {
        var query = """
            SELECT u.id, u.name, u.email, u.registrationDate,
                   COUNT(o.id) as orderCount,
                   COALESCE(SUM(o.totalAmount), 0) as totalSpent
            FROM User u
            LEFT JOIN Order o ON o.customerId = u.id
            WHERE u.registrationDate >= :startDate
              AND u.registrationDate <= :endDate
            GROUP BY u.id, u.name, u.email, u.registrationDate
            """;

        try (var resultStream = entityManager.createNativeQuery(query)
                .setParameter("startDate", request.startDate())
                .setParameter("endDate", request.endDate())
                .getResultStream()) {

            var reportWriter = new ReportWriter(request.outputFile());

            resultStream
                .map(this::mapToUserReportLine)
                .filter(line -> line.totalSpent().compareTo(BigDecimal.ZERO) > 0)
                .forEach(reportWriter::writeLine);

            reportWriter.close();

            LOG.info("User report generated successfully",
                kv("outputFile", request.outputFile()),
                kv("dateRange", request.startDate() + " to " + request.endDate()));
        }
    }

    // ✅ Batch processing com pagination
    @Transactional
    public void processUsersInBatches(UserProcessor processor) {
        var batchSize = 100;
        var processed = 0;
        var hasMore = true;

        while (hasMore) {
            var users = entityManager.createQuery(
                "SELECT u FROM User u ORDER BY u.id", User.class)
                .setFirstResult(processed)
                .setMaxResults(batchSize)
                .getResultList();

            if (users.isEmpty()) {
                hasMore = false;
            } else {
                users.forEach(processor::process);
                processed += users.size();

                // Clear persistence context to free memory
                entityManager.clear();

                LOG.debug("Processed batch of users",
                    kv("batchSize", users.size()),
                    kv("totalProcessed", processed));
            }
        }

        LOG.info("User processing completed",
            kv("totalProcessed", processed));
    }
}

// ✅ Connection pooling configuration
@ApplicationScoped
public class DataSourceConfiguration {

    @ConfigProperty(name = "app.database.pool.initial-size", defaultValue = "5")
    int initialPoolSize;

    @ConfigProperty(name = "app.database.pool.max-size", defaultValue = "20")
    int maxPoolSize;

    @ConfigProperty(name = "app.database.pool.connection-timeout", defaultValue = "30")
    Duration connectionTimeout;

    @Produces
    @ApplicationScoped
    public DataSource dataSource() {
        var config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);

        // Pool configuration
        config.setMinimumIdle(initialPoolSize);
        config.setMaximumPoolSize(maxPoolSize);
        config.setConnectionTimeout(connectionTimeout.toMillis());
        config.setIdleTimeout(Duration.ofMinutes(10).toMillis());
        config.setMaxLifetime(Duration.ofMinutes(30).toMillis());

        // Connection validation
        config.setConnectionTestQuery("SELECT 1");
        config.setValidationTimeout(Duration.ofSeconds(5).toMillis());

        // Performance optimizations
        config.setLeakDetectionThreshold(Duration.ofMinutes(2).toMillis());
        config.setRegisterMbeans(true);

        return new HikariDataSource(config);
    }
}
```

### Resource Management - Cleanup Patterns

#### ✅ PADRÃO - Proper resource cleanup

```java
// ✅ Service com resource management
@ApplicationScoped
public class FileProcessingService {

    @PreDestroy
    public void cleanup() {
        LOG.info("Cleaning up file processing service resources");
        shutdownExecutors();
        closeOpenFiles();
    }

    public ProcessingResult processFile(String filePath) {
        // Try-with-resources para auto-cleanup
        try (var fileInputStream = new FileInputStream(filePath);
             var bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
             var processingContext = new ProcessingContext()) {

            return processLines(bufferedReader, processingContext);

        } catch (IOException e) {
            LOG.error("Failed to process file",
                kv("filePath", filePath),
                kv("error", e.getMessage()),
                e);
            throw new FileProcessingException("File processing failed", e);
        }
    }

    // ✅ Async resource management
    @Async
    public CompletableFuture<Void> processLargeFileAsync(String filePath) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return processFile(filePath);
            } catch (Exception e) {
                LOG.error("Async file processing failed",
                    kv("filePath", filePath),
                    e);
                throw new RuntimeException(e);
            }
        }, managedExecutor)
        .thenApply(result -> {
            LOG.info("File processing completed",
                kv("filePath", filePath),
                kv("linesProcessed", result.linesProcessed()));
            return null;
        });
    }

    // ✅ Executor management
    @Produces
    @ApplicationScoped
    public ManagedExecutorService managedExecutor() {
        return ManagedExecutorService.builder()
            .maxConcurrency(10)
            .build();
    }
}
```

### Monitoring e Métricas

#### ✅ PADRÃO - Performance monitoring

```java
// ✅ Metrics collection
@ApplicationScoped
public class MetricsCollector {

    @Inject
    MeterRegistry meterRegistry;

    private final Counter userCreationCounter;
    private final Timer userCreationTimer;
    private final Gauge activeUsersGauge;

    public MetricsCollector(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.userCreationCounter = Counter.builder("user.creation.total")
            .description("Total number of user creation attempts")
            .register(meterRegistry);

        this.userCreationTimer = Timer.builder("user.creation.duration")
            .description("User creation processing time")
            .register(meterRegistry);

        this.activeUsersGauge = Gauge.builder("users.active.count")
            .description("Number of active users")
            .register(meterRegistry, this, MetricsCollector::getActiveUserCount);
    }

    public void recordUserCreation(Duration processingTime, boolean success) {
        userCreationCounter.increment(
            Tags.of(
                Tag.of("status", success ? "success" : "failure")
            )
        );

        userCreationTimer.record(processingTime);
    }

    private double getActiveUserCount() {
        // Cached query to avoid expensive DB calls on every metric scrape
        return userRepository.countActiveUsers();
    }

    @Scheduled(every = "30s")
    public void updateCachedMetrics() {
        // Update cached values for expensive metrics
        var dbConnections = dataSource.getHikariPoolMXBean().getActiveConnections();
        meterRegistry.gauge("database.connections.active", dbConnections);

        var memoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        meterRegistry.gauge("jvm.memory.heap.used", memoryUsage.getUsed());
        meterRegistry.gauge("jvm.memory.heap.max", memoryUsage.getMax());
    }
}
```

## Regras de Nomenclatura

### Convenções Fundamentais

- **Classes**: PascalCase (`UserService`, `OrderEntity`, `CreateUserCommand`)
- **Métodos/Variáveis**: camelCase (`createUser`, `totalAmount`, `emailValidationService`)
- **Constantes**: UPPER_SNAKE_CASE (`MAX_RETRY_ATTEMPTS`, `DEFAULT_TIMEOUT_SECONDS`)
- **Pacotes**: lowercase (`domain.user`, `application.order`, `infrastructure.persistence`)
- **Enum Values**: UPPER_SNAKE_CASE (`OrderStatus.PENDING_PAYMENT`, `UserType.PREMIUM_CUSTOMER`)

### Domain-Specific Naming

```java
// ✅ PADRÃO - Nomes que expressam domínio
public class OrderAggregateRoot { } // Clear aggregate boundary
public class UserRepository { }      // Repository pattern clear
public class CreateUserUseCase { }   // Use case intention clear
public class UserCreatedEvent { }    // Event nature explicit

// ❌ EVITAR - Nomes genéricos ou técnicos
public class OrderManager { }        // What does it manage?
public class UserService { }         // Too generic
public class UserHandler { }         // What does it handle?
public class DataProcessor { }       // Technical, not domain
```

## Referencias Cruzadas

Este documento deve ser lido em conjunto com:

- **[Architecture Instructions](./architecture.instructions.md)** - Para padrões de Clean Architecture e DDD
- **[Testing Instructions](./testing.instructions.md)** - Para padrões de teste com Object Calisthenics
- **[Security Instructions](./security.instructions.md)** - Para patterns de segurança e compliance
- **[DevOps Instructions](./devops.instructions.md)** - Para CI/CD, containerização e observabilidade
- **[Documentation Instructions](./documentation.instructions.md)** - Para padrões de documentação técnica

## Checklists de Qualidade Obrigatórios

### ✅ Para Cada Classe

#### Value Objects e Entities

- [ ] **Imutabilidade**: Records ou campos final
- [ ] **Validação**: Fail-fast no construtor
- [ ] **Comportamento**: Métodos expressam ações de domínio, não getters/setters
- [ ] **Object Calisthenics**: Máximo 2 variáveis de instância
- [ ] **Tamanho**: Máximo 50 linhas
- [ ] **Nomes**: Expressam conceitos do domínio claramente

#### Services e Use Cases

- [ ] **Single Responsibility**: Uma responsabilidade clara
- [ ] **Constructor Injection**: Dependências injetadas via construtor
- [ ] **Result Types**: Uso de sealed interfaces para resultados
- [ ] **Error Handling**: Exceptions apenas para condições excepcionais
- [ ] **Logging**: Structured logging com contexto adequado
- [ ] **Performance**: Annotações de cache onde apropriado

#### Controllers e APIs

- [ ] **Thin Controllers**: Apenas coordenação, sem lógica de negócio
- [ ] **Validation**: Bean Validation nos DTOs
- [ ] **Error Handling**: Global exception handlers
- [ ] **Documentation**: OpenAPI/Swagger annotations
- [ ] **Security**: Autorização adequada
- [ ] **HTTP Status**: Códigos corretos para cada cenário

### ✅ Para Cada Método

#### Object Calisthenics Compliance

- [ ] **Indentação**: Máximo 1 nível de indentação
- [ ] **No ELSE**: Early returns ou pattern matching
- [ ] **One Dot**: Evitar method chaining excessivo
- [ ] **Nomes**: Não abreviados, expressivos
- [ ] **Single Assert**: Em testes, apenas um assert por método

#### Performance e Qualidade

- [ ] **Complexity**: Complexidade ciclomática < 10
- [ ] **Length**: Máximo 20 linhas por método
- [ ] **Parameters**: Máximo 3 parâmetros
- [ ] **Exception Safety**: Tratamento adequado de exceções
- [ ] **Resource Management**: Try-with-resources quando necessário

### ✅ Para Cada Arquivo

#### Estrutura e Organização

- [ ] **Package Structure**: Seguindo Clean Architecture
- [ ] **Imports**: Organizados, sem wildcards
- [ ] **Documentation**: JavaDoc para APIs públicas
- [ ] **Dependencies**: Apenas dependências necessárias
- [ ] **Testing**: Classe de teste correspondente existe
- [ ] **Coverage**: > 90% para domain, > 80% para application

#### Code Quality

- [ ] **SonarQube**: Sem code smells críticos
- [ ] **PMD/Checkstyle**: Seguindo regras do projeto
- [ ] **Security**: Sem vulnerabilidades conhecidas
- [ ] **Performance**: Sem anti-patterns de performance
- [ ] **Memory**: Sem memory leaks potenciais

### ✅ Para Feature Completa

#### Functionality

- [ ] **Happy Path**: Cenário principal implementado
- [ ] **Error Scenarios**: Todos os casos de erro tratados
- [ ] **Edge Cases**: Casos extremos considerados
- [ ] **Validation**: Input validation completa
- [ ] **Business Rules**: Todas as regras implementadas

#### Quality Assurance

- [ ] **Unit Tests**: Cobertura adequada com cenários relevantes
- [ ] **Integration Tests**: APIs e repositories testados
- [ ] **Performance Tests**: Para operações críticas
- [ ] **Security Tests**: Autorização e validação testadas
- [ ] **Documentation**: README atualizado, APIs documentadas

#### Observability

- [ ] **Logging**: Events relevantes logados com contexto
- [ ] **Metrics**: Métricas de negócio e técnicas
- [ ] **Tracing**: Distributed tracing configurado
- [ ] **Health Checks**: Endpoints de saúde implementados
- [ ] **Monitoring**: Alertas configurados

### ✅ Para Release/Deploy

#### Pre-Deploy Checklist

- [ ] **All Tests Pass**: Pipeline CI/CD green
- [ ] **Security Scan**: Vulnerabilities addressed
- [ ] **Performance Baseline**: No degradation detected
- [ ] **Database Migration**: Scripts tested and validated
- [ ] **Configuration**: Environment-specific configs ready
- [ ] **Rollback Plan**: Defined and tested

#### Post-Deploy Verification

- [ ] **Health Checks**: All endpoints healthy
- [ ] **Metrics**: Baseline metrics normal
- [ ] **Logs**: No critical errors in logs
- [ ] **Performance**: Response times within SLA
- [ ] **Business Metrics**: Key indicators stable
- [ ] **User Experience**: Critical user journeys working

## Templates e Exemplos Rápidos

### ✅ Value Object Template

```java
public record ValueObjectName(Type value) {
    public ValueObjectName {
        validateValue(value);
    }

    private void validateValue(Type value) {
        Objects.requireNonNull(value, "ValueObjectName cannot be null");
        // Additional validations
    }

    public static ValueObjectName from(String stringValue) {
        // Factory method with parsing/conversion
    }

    // Domain-specific behavior methods
    public boolean isSomeCondition() {
        return /* domain logic */;
    }
}
```

### ✅ Use Case Template

```java
@ApplicationScoped
public class SomeActionUseCase {

    private final SomeDomainRepository repository;
    private final SomeDomainService domainService;

    @Inject
    public SomeActionUseCase(SomeDomainRepository repository, SomeDomainService domainService) {
        this.repository = repository;
        this.domainService = domainService;
    }

    public Result<SuccessType, ErrorType> execute(SomeActionCommand command) {
        // Validation
        var validationResult = validateCommand(command);
        if (validationResult.isFailure()) {
            return Result.failure(validationResult.error());
        }

        // Business logic delegation to domain
        try {
            var domainResult = domainService.performAction(command);
            var persistedResult = repository.save(domainResult);

            return Result.success(persistedResult);

        } catch (DomainException e) {
            return Result.failure(mapToErrorType(e));
        }
    }
}
```

### ✅ Controller Template

```java
@Path("/api/v1/resource")
@ApplicationScoped
public class ResourceController {

    private final SomeActionUseCase useCase;
    private final ResourceMapper mapper;

    @POST
    @Operation(summary = "Create resource")
    public Response createResource(@Valid CreateResourceRequest request) {
        var command = mapper.toCommand(request);
        var result = useCase.execute(command);

        return switch (result) {
            case Result.Success<ResourceType, ErrorType> success ->
                Response.status(201).entity(mapper.toResponse(success.value())).build();

            case Result.Failure<ResourceType, ErrorType> failure ->
                mapErrorToResponse(failure.error());
        };
    }
}
```
