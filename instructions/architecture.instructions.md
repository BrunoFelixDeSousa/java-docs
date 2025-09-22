---
applyTo: "src/main/java/**"
description: "Padrões de Clean Architecture e organização de código"
---

# Clean Architecture - Estrutura e Padrões

Aplicar as [instruções gerais](./copilot-instructions.md) e [padrões Java](./java-coding.instructions.md) seguindo Clean Architecture.

## Estrutura de Camadas

```java
src/main/java/com/myproject/
├── domain/                    # Entities + Value Objects + Domain Services
│   ├── user/
│   │   ├── User.java         # Entity
│   │   ├── UserId.java       # Value Object
│   │   ├── Email.java        # Value Object
│   │   └── UserRepository.java # Port (Interface)
│   └── order/
│       ├── Order.java
│       ├── OrderId.java
│       └── OrderRepository.java
├── application/              # Use Cases + Application Services
│   ├── user/
│   │   ├── CreateUserUseCase.java
│   │   ├── CreateUserCommand.java
│   │   └── CreateUserResult.java
│   └── order/
│       ├── CreateOrderUseCase.java
│       └── CreateOrderCommand.java
├── infrastructure/           # Adapters + External Services
│   ├── persistence/
│   │   ├── UserRepositoryImpl.java
│   │   └── OrderRepositoryImpl.java
│   ├── messaging/
│   │   └── EventPublisherImpl.java
│   └── external/
│       └── PaymentServiceImpl.java
└── presentation/            # Controllers + DTOs + Mappers
    ├── rest/
    │   ├── UserController.java
    │   └── OrderController.java
    ├── dto/
    │   ├── CreateUserRequest.java
    │   └── UserResponse.java
    └── mapper/
        └── UserMapper.java
```

## Domain Layer (Camada de Domínio)

### Entities
```java
// ✅ PADRÃO - Entity rica com comportamentos
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
    
    // Comportamento de domínio
    public User deactivate() {
        validateCanDeactivate();
        return new User(id, email, name, UserStatus.INACTIVE);
    }
    
    public User changeEmail(Email newEmail) {
        validateEmailChange(newEmail);
        return new User(id, newEmail, name, status);
    }
    
    private void validateCanDeactivate() {
        if (status == UserStatus.INACTIVE) {
            throw new UserAlreadyInactiveException(id);
        }
    }
    
    // Getters apenas para leitura
    public UserId id() { return id; }
    public Email email() { return email; }
    public UserName name() { return name; }
    public UserStatus status() { return status; }
}
```

### Value Objects
```java
// ✅ PADRÃO - Value Object com validação
public record UserId(UUID value) {
    public UserId {
        Objects.requireNonNull(value, "UserId cannot be null");
    }
    
    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }
}

public record Email(String value) {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    
    public Email {
        validateFormat(value);
    }
    
    private void validateFormat(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }
}
```

### Repository Interfaces (Ports)
```java
// ✅ PADRÃO - Interface no domínio, implementação na infraestrutura
public interface UserRepository {
    User save(User user);
    Optional<User> findById(UserId id);
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);
    List<User> findByStatus(UserStatus status);
    void delete(UserId id);
}
```

### Domain Services
```java
// ✅ PADRÃO - Serviço de domínio para regras complexas
@ApplicationScoped
public class UserDomainService {
    private final UserRepository userRepository;
    
    @Inject
    public UserDomainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public boolean canChangeEmail(User user, Email newEmail) {
        if (user.email().equals(newEmail)) {
            return false;
        }
        
        return !userRepository.existsByEmail(newEmail);
    }
    
    public void validateUniqueEmail(Email email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }
}
```

## Application Layer (Camada de Aplicação)

### Use Cases
```java
// ✅ PADRÃO - Use Case com Command/Result pattern
@ApplicationScoped
public class CreateUserUseCase {
    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final EventPublisher eventPublisher;
    
    @Inject
    public CreateUserUseCase(
            UserRepository userRepository,
            UserDomainService userDomainService,
            EventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
        this.eventPublisher = eventPublisher;
    }
    
    public CreateUserResult execute(CreateUserCommand command) {
        try {
            validateCommand(command);
            
            var email = new Email(command.email());
            userDomainService.validateUniqueEmail(email);
            
            var user = createUser(command, email);
            var savedUser = userRepository.save(user);
            
            publishUserCreatedEvent(savedUser);
            
            return new CreateUserResult.Success(savedUser);
            
        } catch (EmailAlreadyExistsException e) {
            return new CreateUserResult.ValidationError(List.of("Email already exists"));
        } catch (IllegalArgumentException e) {
            return new CreateUserResult.ValidationError(List.of(e.getMessage()));
        } catch (Exception e) {
            return new CreateUserResult.SystemError("Internal system error");
        }
    }
    
    private void validateCommand(CreateUserCommand command) {
        if (command.name() == null || command.name().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        
        if (command.email() == null || command.email().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
    }
    
    private User createUser(CreateUserCommand command, Email email) {
        return new User(
            UserId.generate(),
            email,
            new UserName(command.name())
        );
    }
    
    private void publishUserCreatedEvent(User user) {
        var event = new UserCreatedEvent(user.id(), user.email(), user.name());
        eventPublisher.publish(event);
    }
}
```

### Commands e Results
```java
// ✅ PADRÃO - Command como record
public record CreateUserCommand(
    String name,
    String email
) {
    public CreateUserCommand {
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(email, "Email cannot be null");
    }
}

// ✅ PADRÃO - Result com sealed interface
public sealed interface CreateUserResult 
    permits CreateUserResult.Success, CreateUserResult.ValidationError, CreateUserResult.SystemError {
    
    record Success(User user) implements CreateUserResult {}
    record ValidationError(List<String> errors) implements CreateUserResult {}
    record SystemError(String message) implements CreateUserResult {}
}
```

### Application Services (Orchestração)
```java
// ✅ PADRÃO - Service para orchestrar múltiplos use cases
@ApplicationScoped
public class UserApplicationService {
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeactivateUserUseCase deactivateUserUseCase;
    
    @Inject
    public UserApplicationService(
            CreateUserUseCase createUserUseCase,
            UpdateUserUseCase updateUserUseCase,
            DeactivateUserUseCase deactivateUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deactivateUserUseCase = deactivateUserUseCase;
    }
    
    @Transactional
    public CreateUserResult createUser(CreateUserCommand command) {
        return createUserUseCase.execute(command);
    }
    
    @Transactional
    public UpdateUserResult updateUser(UpdateUserCommand command) {
        return updateUserUseCase.execute(command);
    }
}
```

## Infrastructure Layer (Camada de Infraestrutura)

### Repository Implementation
```java
// ✅ PADRÃO - Implementação do repositório usando Panache
@ApplicationScoped
public class UserRepositoryImpl implements UserRepository {
    
    @Override
    public User save(User user) {
        var entity = toEntity(user);
        entity.persist();
        return toDomain(entity);
    }
    
    @Override
    public Optional<User> findById(UserId id) {
        return UserEntity.findByIdOptional(id.value())
            .map(entity -> toDomain((UserEntity) entity));
    }
    
    @Override
    public Optional<User> findByEmail(Email email) {
        return UserEntity.find("email", email.value())
            .firstResultOptional()
            .map(entity -> toDomain((UserEntity) entity));
    }
    
    @Override
    public boolean existsByEmail(Email email) {
        return UserEntity.count("email", email.value()) > 0;
    }
    
    @Override
    public List<User> findByStatus(UserStatus status) {
        return UserEntity.find("status", status.name())
            .stream()
            .map(entity -> toDomain((UserEntity) entity))
            .collect(Collectors.toList());
    }
    
    @Override
    public void delete(UserId id) {
        UserEntity.deleteById(id.value());
    }
    
    private UserEntity toEntity(User user) {
        var entity = new UserEntity();
        entity.id = user.id().value();
        entity.email = user.email().value();
        entity.name = user.name().value();
        entity.status = user.status().name();
        return entity;
    }
    
    private User toDomain(UserEntity entity) {
        return new User(
            new UserId(entity.id),
            new Email(entity.email),
            new UserName(entity.name),
            UserStatus.valueOf(entity.status)
        );
    }
}

// Entity JPA separada da Entity de domínio
@Entity
@Table(name = "users")
public class UserEntity extends PanacheEntityBase {
    @Id
    public UUID id;
    
    @Column(unique = true)
    public String email;
    
    public String name;
    public String status;
    
    @CreationTimestamp
    public LocalDateTime createdAt;
    
    @UpdateTimestamp
    public LocalDateTime updatedAt;
}
```

### External Services Implementation
```java
// ✅ PADRÃO - Implementação de serviço externo
@ApplicationScoped
public class EmailServiceImpl implements EmailService {
    
    @ConfigProperty(name = "email.service.url")
    String emailServiceUrl;
    
    @RestClient
    EmailClient emailClient;
    
    @Override
    public void sendWelcomeEmail(User user) {
        try {
            var emailRequest = EmailRequest.builder()
                .to(user.email().value())
                .subject("Welcome to our platform")
                .template("welcome")
                .variables(Map.of("userName", user.name().value()))
                .build();
            
            emailClient.sendEmail(emailRequest);
            
        } catch (Exception e) {
            // Log error but don't fail the main operation
            Log.errorf("Failed to send welcome email to user %s: %s", 
                user.id().value(), e.getMessage());
        }
    }
}

@RegisterRestClient(configKey = "email-client")
public interface EmailClient {
    
    @POST
    @Path("/send")
    void sendEmail(EmailRequest request);
}
```

### Event Publisher Implementation
```java
// ✅ PADRÃO - Publisher de eventos
@ApplicationScoped
public class EventPublisherImpl implements EventPublisher {
    
    @Inject
    @Channel("user-events")
    Emitter<UserEvent> userEventEmitter;
    
    @Override
    public void publish(DomainEvent event) {
        switch (event) {
            case UserCreatedEvent userCreated -> publishUserCreated(userCreated);
            case UserUpdatedEvent userUpdated -> publishUserUpdated(userUpdated);
            case UserDeactivatedEvent userDeactivated -> publishUserDeactivated(userDeactivated);
            default -> Log.warnf("Unknown event type: %s", event.getClass().getSimpleName());
        }
    }
    
    private void publishUserCreated(UserCreatedEvent event) {
        var userEvent = new UserEvent(
            event.userId().value(),
            "USER_CREATED",
            Map.of(
                "email", event.email().value(),
                "name", event.name().value()
            ),
            Instant.now()
        );
        
        userEventEmitter.send(userEvent);
    }
}
```

## Presentation Layer (Camada de Apresentação)

### REST Controllers
```java
// ✅ PADRÃO - Controller thin, delegando para Application Service
@Path("/api/v1/users")
@ApplicationScoped
@Tag(name = "Users", description = "User management operations")
public class UserController {
    
    private final UserApplicationService userApplicationService;
    private final UserMapper userMapper;
    
    @Inject
    public UserController(UserApplicationService userApplicationService, UserMapper userMapper) {
        this.userApplicationService = userApplicationService;
        this.userMapper = userMapper;
    }
    
    @POST
    @Operation(summary = "Create a new user")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "User created successfully"),
        @APIResponse(responseCode = "400", description = "Validation error"),
        @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response createUser(@Valid CreateUserRequest request) {
        var command = userMapper.toCommand(request);
        var result = userApplicationService.createUser(command);
        
        return switch (result) {
            case CreateUserResult.Success(var user) -> {
                var response = userMapper.toResponse(user);
                yield Response.status(201).entity(response).build();
            }
            case CreateUserResult.ValidationError(var errors) -> {
                var errorResponse = new ErrorResponse("Validation failed", errors);
                yield Response.status(400).entity(errorResponse).build();
            }
            case CreateUserResult.SystemError(var message) -> {
                var errorResponse = new ErrorResponse("System error", List.of(message));
                yield Response.status(500).entity(errorResponse).build();
            }
        };
    }
    
    @GET
    @Path("/{id}")
    @Operation(summary = "Get user by ID")
    public Response getUserById(@PathParam("id") UUID id) {
        var userId = new UserId(id);
        var result = userApplicationService.getUserById(userId);
        
        return switch (result) {
            case GetUserResult.Success(var user) -> {
                var response = userMapper.toResponse(user);
                yield Response.ok(response).build();
            }
            case GetUserResult.NotFound() -> Response.status(404).build();
            case GetUserResult.SystemError(var message) -> {
                var errorResponse = new ErrorResponse("System error", List.of(message));
                yield Response.status(500).entity(errorResponse).build();
            }
        };
    }
}
```

### DTOs
```java
// ✅ PADRÃO - Request DTO com validação
public record CreateUserRequest(
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email
) {}

// ✅ PADRÃO - Response DTO
public record UserResponse(
    UUID id,
    String name,
    String email,
    String status,
    LocalDateTime createdAt
) {}

// ✅ PADRÃO - Error Response
public record ErrorResponse(
    String message,
    List<String> errors,
    LocalDateTime timestamp
) {
    public ErrorResponse(String message, List<String> errors) {
        this(message, errors, LocalDateTime.now());
    }
}
```

### Mappers
```java
// ✅ PADRÃO - Mapper usando MapStruct
@Mapper(componentModel = "cdi")
public interface UserMapper {
    
    default CreateUserCommand toCommand(CreateUserRequest request) {
        return new CreateUserCommand(request.name(), request.email());
    }
    
    default UserResponse toResponse(User user) {
        return new UserResponse(
            user.id().value(),
            user.name().value(),
            user.email().value(),
            user.status().name(),
            LocalDateTime.now() // Ou buscar da entity se necessário
        );
    }
    
    default UpdateUserCommand toUpdateCommand(UUID id, UpdateUserRequest request) {
        return new UpdateUserCommand(
            new UserId(id),
            request.name(),
            request.email()
        );
    }
}
```

## Domain-Driven Design Patterns

### Aggregate Boundaries

#### Identificando Agregados
```java
// ✅ PADRÃO - Order como Aggregate Root
public class Order {
    private final OrderId id;
    private final CustomerId customerId;
    private final OrderItems items; // Entities dentro do agregado
    private final OrderStatus status;
    
    // Aggregate Root é o único ponto de entrada
    public Order addItem(Product product, int quantity) {
        validateCanAddItem();
        
        var newItem = new OrderItem(product.id(), quantity, product.price());
        var updatedItems = items.add(newItem);
        
        return new Order(id, customerId, updatedItems, status);
    }
    
    // Invariantes protegidas pelo Aggregate
    private void validateCanAddItem() {
        if (status != OrderStatus.DRAFT) {
            throw new OrderAlreadyConfirmedException(id);
        }
    }
    
    // Factory method para criação consistente
    public static Order create(CustomerId customerId) {
        return new Order(
            OrderId.generate(),
            customerId,
            OrderItems.empty(),
            OrderStatus.DRAFT
        );
    }
}

// ✅ PADRÃO - OrderItem como Entity dentro do Agregado
public class OrderItem {
    private final OrderItemId id;
    private final ProductId productId;
    private final int quantity;
    private final Money unitPrice;
    
    // Comportamentos específicos da entity
    public Money totalPrice() {
        return unitPrice.multiply(quantity);
    }
    
    public OrderItem changeQuantity(int newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        return new OrderItem(id, productId, newQuantity, unitPrice);
    }
}
```

#### Guidelines para Aggregate Design

**✅ QUANDO criar um novo Aggregate:**
- Entidades que têm ciclo de vida independente
- Diferentes regras de consistência
- Diferentes atores modificam a informação
- Diferentes frequências de mudança
- Limites de transação necessários

**❌ QUANDO NÃO criar um novo Aggregate:**
- Entidades sempre modificadas juntas
- Mesmas regras de negócio
- Mesmo contexto transacional
- Forte consistência entre entidades necessária

```java
// ✅ EXEMPLO - Dois agregados separados
public class User {
    // Aggregate Root para dados do usuário
    private final UserId id;
    private final Email email;
    // Não inclui OrderHistory - é outro agregado
}

public class OrderHistory {
    // Aggregate Root separado para histórico
    private final UserId userId; // Reference by ID
    private final List<OrderId> orderIds; // References, não entities
    
    public void addOrder(OrderId orderId) {
        // Lógica específica do histórico
    }
}
```

### Domain Events

#### Event-First Design
```java
// ✅ PADRÃO - Domain Events
public sealed interface DomainEvent permits UserEvent, OrderEvent {
    Instant occurredAt();
    UUID aggregateId();
}

public sealed interface UserEvent extends DomainEvent 
    permits UserCreatedEvent, UserEmailChangedEvent, UserDeactivatedEvent {
    
    UserId userId();
    
    @Override
    default UUID aggregateId() {
        return userId().value();
    }
}

public record UserCreatedEvent(
    UserId userId,
    Email email,
    UserName name,
    Instant occurredAt
) implements UserEvent {
    
    public static UserCreatedEvent from(User user) {
        return new UserCreatedEvent(
            user.id(),
            user.email(),
            user.name(),
            Instant.now()
        );
    }
}
```

#### Event-Driven Aggregate
```java
// ✅ PADRÃO - Aggregate que produz eventos
public class User {
    private final List<DomainEvent> events = new ArrayList<>();
    
    public User changeEmail(Email newEmail) {
        validateEmailChange(newEmail);
        
        var updatedUser = new User(id, newEmail, name, status);
        
        // Registrar evento
        updatedUser.addEvent(new UserEmailChangedEvent(
            id, email, newEmail, Instant.now()
        ));
        
        return updatedUser;
    }
    
    private void addEvent(DomainEvent event) {
        events.add(event);
    }
    
    public List<DomainEvent> getUncommittedEvents() {
        return List.copyOf(events);
    }
    
    public void markEventsAsCommitted() {
        events.clear();
    }
}
```

#### Event Publishing
```java
// ✅ PADRÃO - Event Publisher no Application Service
@ApplicationScoped
public class UserApplicationService {
    
    private final UserRepository userRepository;
    private final DomainEventPublisher eventPublisher;
    
    @Transactional
    public ChangeEmailResult changeEmail(ChangeEmailCommand command) {
        var user = userRepository.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException(command.userId()));
        
        var updatedUser = user.changeEmail(new Email(command.newEmail()));
        var savedUser = userRepository.save(updatedUser);
        
        // Publicar eventos após salvar
        savedUser.getUncommittedEvents()
            .forEach(eventPublisher::publish);
        
        savedUser.markEventsAsCommitted();
        
        return new ChangeEmailResult.Success(savedUser);
    }
}
```

### CQRS (Command Query Responsibility Segregation)

#### Command Side
```java
// ✅ PADRÃO - Command handler
@ApplicationScoped
public class CreateUserCommandHandler {
    
    private final UserRepository userRepository;
    private final DomainEventPublisher eventPublisher;
    
    @Transactional
    public CreateUserResult handle(CreateUserCommand command) {
        // Validações e regras de negócio
        validateCommand(command);
        
        // Criar agregado
        var user = User.create(
            new Email(command.email()),
            new UserName(command.name())
        );
        
        // Persistir
        var savedUser = userRepository.save(user);
        
        // Publicar eventos
        publishEvents(savedUser);
        
        return new CreateUserResult.Success(savedUser);
    }
}
```

#### Query Side
```java
// ✅ PADRÃO - Read Model otimizado
public record UserListView(
    UUID id,
    String name,
    String email,
    String status,
    LocalDateTime createdAt,
    int orderCount,
    BigDecimal totalSpent
) {}

@ApplicationScoped
public class UserQueryService {
    
    @Inject
    EntityManager entityManager;
    
    public List<UserListView> findAllUsers(UserFilter filter) {
        var query = """
            SELECT new com.myproject.query.UserListView(
                u.id, u.name, u.email, u.status, u.createdAt,
                COUNT(o.id), COALESCE(SUM(o.total), 0)
            )
            FROM UserEntity u
            LEFT JOIN OrderEntity o ON o.userId = u.id
            WHERE (:status IS NULL OR u.status = :status)
            GROUP BY u.id, u.name, u.email, u.status, u.createdAt
            ORDER BY u.createdAt DESC
            """;
        
        return entityManager.createQuery(query, UserListView.class)
            .setParameter("status", filter.status())
            .getResultList();
    }
}
```

### Event Sourcing (quando apropriado)

#### Event Store
```java
// ✅ PADRÃO - Event Store para agregados críticos
@ApplicationScoped
public class EventStore {
    
    @Inject
    EntityManager entityManager;
    
    public void saveEvents(UUID aggregateId, String aggregateType, 
                          List<DomainEvent> events, long expectedVersion) {
        
        // Verificar versão para evitar conflitos
        var currentVersion = getCurrentVersion(aggregateId);
        if (currentVersion != expectedVersion) {
            throw new ConcurrentModificationException(
                "Expected version " + expectedVersion + " but was " + currentVersion
            );
        }
        
        // Salvar eventos
        events.forEach(event -> {
            var eventEntity = new EventEntity();
            eventEntity.aggregateId = aggregateId;
            eventEntity.aggregateType = aggregateType;
            eventEntity.eventType = event.getClass().getSimpleName();
            eventEntity.eventData = serializeEvent(event);
            eventEntity.version = ++currentVersion;
            eventEntity.occurredAt = event.occurredAt();
            
            entityManager.persist(eventEntity);
        });
    }
    
    public List<DomainEvent> getEvents(UUID aggregateId) {
        var query = """
            SELECT e FROM EventEntity e 
            WHERE e.aggregateId = :aggregateId 
            ORDER BY e.version ASC
            """;
        
        return entityManager.createQuery(query, EventEntity.class)
            .setParameter("aggregateId", aggregateId)
            .getResultList()
            .stream()
            .map(this::deserializeEvent)
            .collect(Collectors.toList());
    }
}

// ✅ PADRÃO - Aggregate reconstitution
public class EventSourcedUser {
    
    public static User fromEvents(List<DomainEvent> events) {
        if (events.isEmpty()) {
            throw new IllegalArgumentException("Cannot reconstitute from empty events");
        }
        
        User user = null;
        
        for (DomainEvent event : events) {
            user = switch (event) {
                case UserCreatedEvent created -> new User(
                    created.userId(),
                    created.email(),
                    created.name()
                );
                case UserEmailChangedEvent emailChanged -> 
                    user.changeEmail(emailChanged.newEmail());
                case UserDeactivatedEvent deactivated -> 
                    user.deactivate();
                default -> user; // Ignorar eventos desconhecidos
            };
        }
        
        return user;
    }
}
```

### Specification Pattern

#### Domain Specifications
```java
// ✅ PADRÃO - Specification para regras complexas
public interface Specification<T> {
    boolean isSatisfiedBy(T candidate);
    
    default Specification<T> and(Specification<T> other) {
        return candidate -> this.isSatisfiedBy(candidate) && other.isSatisfiedBy(candidate);
    }
    
    default Specification<T> or(Specification<T> other) {
        return candidate -> this.isSatisfiedBy(candidate) || other.isSatisfiedBy(candidate);
    }
}

public class EligibleForPremiumSpecification implements Specification<User> {
    
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    
    @Override
    public boolean isSatisfiedBy(User user) {
        // Regra complexa para Premium
        if (!user.isActive()) {
            return false;
        }
        
        var registrationDate = userRepository.getRegistrationDate(user.id());
        if (ChronoUnit.DAYS.between(registrationDate, LocalDate.now()) < 30) {
            return false; // Menos de 30 dias
        }
        
        var totalSpent = orderRepository.getTotalSpentByUser(user.id());
        return totalSpent.isGreaterThanOrEqualTo(new Money(BigDecimal.valueOf(1000)));
    }
}

// ✅ USO - No Domain Service
@ApplicationScoped
public class UserPromotionService {
    
    @Inject
    EligibleForPremiumSpecification premiumSpecification;
    
    public boolean canPromoteToPremium(User user) {
        return premiumSpecification.isSatisfiedBy(user);
    }
    
    public List<User> findEligibleUsers(List<User> users) {
        return users.stream()
            .filter(premiumSpecification::isSatisfiedBy)
            .collect(Collectors.toList());
    }
}
```

## Regras de Dependência

### ✅ PERMITIDO
- **Domain** → Não depende de nada
- **Application** → Domain
- **Infrastructure** → Domain + Application
- **Presentation** → Domain + Application

### ❌ PROIBIDO
- **Domain** → Application, Infrastructure, Presentation
- **Application** → Infrastructure, Presentation
- **Infrastructure** → Presentation

## Configuração de Injeção de Dependência

```java
// ✅ PADRÃO - Configuration class para bindings
@ApplicationScoped
public class InfrastructureConfiguration {
    
    @Produces
    @ApplicationScoped
    public UserRepository userRepository(UserRepositoryImpl implementation) {
        return implementation;
    }
    
    @Produces
    @ApplicationScoped
    public EmailService emailService(EmailServiceImpl implementation) {
        return implementation;
    }
    
    @Produces
    @ApplicationScoped
    public EventPublisher eventPublisher(EventPublisherImpl implementation) {
        return implementation;
    }
}
```

## Validação de Arquitetura

```java
// ✅ Teste arquitetural com ArchUnit
@AnalyzeClasses(packages = "com.myproject")
public class ArchitectureTest {
    
    @Test
    void domainShouldNotDependOnOtherLayers() {
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..application..", "..infrastructure..", "..presentation..")
            .check(importedClasses);
    }
    
    @Test
    void applicationShouldOnlyDependOnDomain() {
        noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..infrastructure..", "..presentation..")
            .check(importedClasses);
    }
    
    @Test
    void infrastructureShouldNotDependOnPresentation() {
        noClasses()
            .that().resideInAPackage("..infrastructure..")
            .should().dependOnClassesThat()
            .resideInAPackage("..presentation..")
            .check(importedClasses);
    }
}
```