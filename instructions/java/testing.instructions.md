---
applyTo: "**/*Test.java,**/test/**/*.java"
description: "Padrões para testes unitários, integração e end-to-end com foco em legibilidade e determinismo"
---

# Padrões de Teste - JUnit 5 + Testcontainers

Aplicar as [instruções gerais](./copilot-instructions.md) e [padrões Java](./java-coding.instructions.md) aos testes.

## Objetivo

O objetivo desta documentação é centralizar boas práticas para testes unitários, integração e end-to-end em Java, eliminando test smells e garantindo código de teste de alta qualidade, legível e determinístico.

## Referências

- [JUnit 5](https://junit.org/junit5/docs/current/user-guide/) - Framework de testes
- [Mockito](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html) - Framework de mocks
- [AssertJ](https://assertj.github.io/doc/) - Biblioteca de assertions fluentes
- [Testcontainers](https://www.testcontainers.org/) - Testes de integração com containers

## Estrutura e Organização de Testes

### Organização de Diretórios
```
src/
├── main/java/com/myproject/
│   ├── domain/
│   │   └── user/
│   │       ├── User.java
│   │       └── UserRepository.java
│   └── application/
│       └── user/
│           └── CreateUserUseCase.java
└── test/java/com/myproject/
    ├── domain/
    │   └── user/
    │       ├── UserTest.java
    │       └── UserRepositoryTest.java
    └── application/
        └── user/
            └── CreateUserUseCaseTest.java
```

### Nomenclatura e Estrutura com @Nested e @DisplayName

#### ❌ EVITAR - Nomenclatura confusa
```java
class UserTest {
    @Test
    void testCreateUserWithValidEmail() { }
    
    @Test 
    void testCreateUserWithInvalidEmail() { }
    
    @Test
    void testUserChangeEmail() { }
}
```

#### ✅ PADRÃO - Estrutura clara com agrupamento
```java
@DisplayName("User")
class UserTest {
    
    @Nested
    @DisplayName("When creating user")
    class WhenCreatingUser {
        
        @Test
        @DisplayName("Then should create user successfully when valid data provided")
        void shouldCreateUserSuccessfully_WhenValidDataProvided() {
            // Given
            var userId = new UserId(UUID.randomUUID());
            var email = new Email("user@domain.com");
            var name = new UserName("John Doe");
            
            // When
            var user = new User(userId, email, name);
            
            // Then
            assertThat(user.id()).isEqualTo(userId);
            assertThat(user.email()).isEqualTo(email);
            assertThat(user.name()).isEqualTo(name);
        }
        
        @Test
        @DisplayName("Then should throw exception when invalid email provided")
        void shouldThrowException_WhenInvalidEmailProvided() {
            // Given
            var userId = new UserId(UUID.randomUUID());
            var invalidEmail = "invalid-email";
            var name = new UserName("John Doe");
            
            // When & Then
            assertThatThrownBy(() -> new User(userId, new Email(invalidEmail), name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid email format");
        }
    }
    
    @Nested
    @DisplayName("When changing email")
    class WhenChangingEmail {
        
        private User user;
        
        @BeforeEach
        void setUp() {
            user = createValidUser();
        }
        
        @Test
        @DisplayName("Then should change email successfully when valid email provided")
        void shouldChangeEmailSuccessfully_WhenValidEmailProvided() {
            // Given
            var newEmail = new Email("newemail@domain.com");
            
            // When
            var updatedUser = user.changeEmail(newEmail);
            
            // Then
            assertThat(updatedUser.email()).isEqualTo(newEmail);
            assertThat(updatedUser).isNotSameAs(user); // Imutabilidade
        }
        
        private User createValidUser() {
            return new User(
                new UserId(UUID.randomUUID()),
                new Email("original@domain.com"),
                new UserName("John Doe")
            );
        }
    }
}
```

### Estrutura Given/When/Then Obrigatória
```java
@Test
@DisplayName("Then should calculate total value when multiple items provided")
void shouldCalculateTotalValue_WhenMultipleItemsProvided() {
    // Given - Preparação do cenário
    var item1 = new OrderItem(
        new ProductId(UUID.randomUUID()), 
        new Money(BigDecimal.valueOf(10))
    );
    var item2 = new OrderItem(
        new ProductId(UUID.randomUUID()), 
        new Money(BigDecimal.valueOf(20))
    );
    var orderItems = new OrderItems(List.of(item1, item2));
    
    // When - Execução da ação
    var totalValue = orderItems.totalValue();
    
    // Then - Verificação do resultado (APENAS UM ASSERT)
    assertThat(totalValue).isEqualTo(new Money(BigDecimal.valueOf(30)));
}
```

## Padrões de Setup e Lifecycle

### Uso Correto de @BeforeAll e @BeforeEach
```java
@DisplayName("CreateUserUseCase")
class CreateUserUseCaseTest {
    
    // ✅ PADRÃO - Para @BeforeAll sempre usar @TestInstance
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    static class StaticSetup {
        
        @BeforeAll
        void beforeAll() {
            // Configurações estáticas que não dependem de injeção
            System.setProperty("test.environment", "true");
        }
    }
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private EmailService emailService;
    
    @InjectMocks
    private CreateUserUseCase createUserUseCase;
    
    private CreateUserCommand validCommand;
    
    @BeforeEach
    void beforeEach() {
        // Limpeza de mocks e setup de dados comuns
        Mockito.clearInvocations(userRepository, emailService);
        validCommand = new CreateUserCommand("John Doe", "john@domain.com");
    }
    
    @Nested
    @DisplayName("When valid command provided")
    class WhenValidCommandProvided {
        
        @BeforeEach
        void setUp() {
            // Setup específico deste contexto
            when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
        }
        
        @Test
        @DisplayName("Then should create user successfully")
        void shouldCreateUserSuccessfully() {
            // Given
            var expectedUser = new User(
                new UserId(UUID.randomUUID()),
                new Email(validCommand.email()),
                new UserName(validCommand.name())
            );
            when(userRepository.save(any(User.class))).thenReturn(expectedUser);
            
            // When
            var result = createUserUseCase.execute(validCommand);
            
            // Then
            assertThat(result).isInstanceOf(CreateUserResult.Success.class);
        }
    }
}
```

### Nomenclatura de Métodos de Setup
```java
class ServiceTest {
    
    // ✅ Métodos de configuração geral
    @BeforeAll
    void beforeAll() { /* configuração única */ }
    
    @BeforeEach  
    void beforeEach() { /* limpeza e setup comum */ }
    
    // ✅ Métodos específicos por responsabilidade
    void setupValidUser() { /* configuração específica */ }
    
    void mockUserRepository() { /* apenas mocks */ }
    
    void mockAndActUserCreation() { /* mocks + ação */ }
    
    void actCreateUser() { /* apenas ação */ }
}
```

## Testes Unitários - Princípios Fundamentais

### Um Assert Por Teste (Object Calisthenics)
```java
// ✅ CORRETO - Um assert focado
@Test
@DisplayName("Then should return user id when user created")
void shouldReturnUserId_WhenUserCreated() {
    // Given
    var command = new CreateUserCommand("John", "john@test.com");
    var expectedUser = createValidUser();
    when(userRepository.save(any(User.class))).thenReturn(expectedUser);
    
    // When
    var result = createUserUseCase.execute(command);
    
    // Then
    assertThat(((CreateUserResult.Success) result).user().id())
        .isEqualTo(expectedUser.id());
}

// ❌ EVITAR - Múltiplos asserts
@Test
void shouldCreateUserWithAllFields() {
    var result = createUserUseCase.execute(command);
    var user = ((CreateUserResult.Success) result).user();
    
    // ❌ Múltiplos asserts violam Object Calisthenics
    assertThat(user.id()).isNotNull();
    assertThat(user.name()).isEqualTo("John");
    assertThat(user.email()).isEqualTo("john@test.com");
}
```

### Testes Determinísticos
```java
// ✅ CORRETO - Teste determinístico
@Test
@DisplayName("Then should generate different user ids when called multiple times")
void shouldGenerateDifferentUserIds_WhenCalledMultipleTimes() {
    // Given
    var command1 = new CreateUserCommand("User1", "user1@test.com");
    var command2 = new CreateUserCommand("User2", "user2@test.com");
    
    when(userRepository.save(any(User.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    
    // When
    var result1 = createUserUseCase.execute(command1);
    var result2 = createUserUseCase.execute(command2);
    
    // Then
    var user1 = ((CreateUserResult.Success) result1).user();
    var user2 = ((CreateUserResult.Success) result2).user();
    assertThat(user1.id()).isNotEqualTo(user2.id());
}

// ❌ EVITAR - Dependência de ordem ou estado externo
@Test
void shouldCreateUsersInSequence() {
    // ❌ Depende da ordem de execução
    createUserUseCase.execute(command1);
    var result = createUserUseCase.execute(command2);
    // Teste pode falhar dependendo da ordem
}
```

## Tratamento de Exceções

### Teste de Exceções com AssertJ
```java
@Nested
@DisplayName("When invalid email provided")
class WhenInvalidEmailProvided {
    
    @Test
    @DisplayName("Then should throw IllegalArgumentException with specific message")
    void shouldThrowIllegalArgumentException_WithSpecificMessage() {
        // Given
        var invalidEmail = "invalid-email-format";
        
        // When & Then
        assertThatThrownBy(() -> new Email(invalidEmail))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Invalid email format: " + invalidEmail);
    }
}

// ✅ Para métodos com lógica antes da exceção
@Test
@DisplayName("Then should throw exception after validation when user not found")
void shouldThrowException_AfterValidation_WhenUserNotFound() {
    // Given
    var nonExistentId = new UserId(UUID.randomUUID());
    when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());
    
    Exception caughtException = null;
    
    try {
        // When
        userService.deleteUser(nonExistentId);
        
        // Se chegou aqui, não lançou exceção - falha o teste
        fail("Expected UserNotFoundException to be thrown");
        
    } catch (UserNotFoundException e) {
        caughtException = e;
    }
    
    // Then
    assertThat(caughtException).isInstanceOf(UserNotFoundException.class);
    assertThat(caughtException.getMessage()).contains(nonExistentId.value().toString());
}
```

## Mock Management

### Limpeza de Mocks
```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private EmailService emailService;
    
    @BeforeEach
    void resetMocksAndSetup() {
        // ✅ OBRIGATÓRIO - Limpar mocks entre testes
        Mockito.clearInvocations(userRepository, emailService);
        Mockito.reset(userRepository, emailService);
        
        // Setup padrão se necessário
        when(userRepository.existsByEmail(any())).thenReturn(false);
    }
}
```

### Mocks Sem Lógica
```java
// ❌ EVITAR - Lógica nos mocks
when(userRepository.findById(any()))
    .thenAnswer(invocation -> {
        var id = invocation.getArgument(0);
        if (id.equals(existingUserId)) {
            return Optional.of(existingUser);
        } else {
            return Optional.empty();
        }
    });

// ✅ CORRETO - Mocks diretos, cenários separados
@Nested
@DisplayName("When user exists")
class WhenUserExists {
    
    @BeforeEach
    void setupExistingUser() {
        when(userRepository.findById(existingUserId))
            .thenReturn(Optional.of(existingUser));
    }
    
    @Test
    @DisplayName("Then should return user")
    void shouldReturnUser() {
        // Test implementation
    }
}

@Nested
@DisplayName("When user does not exist")
class WhenUserDoesNotExist {
    
    @BeforeEach
    void setupNonExistingUser() {
        when(userRepository.findById(nonExistentId))
            .thenReturn(Optional.empty());
    }
    
    @Test
    @DisplayName("Then should throw UserNotFoundException")
    void shouldThrowUserNotFoundException() {
        // Test implementation
    }
}
```

## Teste de Logs (Observabilidade)

### Validação de Logs para Ferramentas de Monitoramento
```java
@ExtendWith(MockitoExtension.class)
class LoggingIntegrationTest {
    
    @Mock
    private Logger logger;
    
    private UserService userService;
    
    @BeforeEach
    void setupLoggerMock() {
        // ✅ Mock do logger para validar logs do Kibana/APM
        userService = new UserService(userRepository, logger);
    }
    
    @Test
    @DisplayName("Then should log user creation for monitoring when user created successfully")
    void shouldLogUserCreationForMonitoring_WhenUserCreatedSuccessfully() {
        // Given
        var command = new CreateUserCommand("John Doe", "john@test.com");
        var createdUser = createValidUser();
        when(userRepository.save(any(User.class))).thenReturn(createdUser);
        
        // When
        userService.createUser(command);
        
        // Then - Validação específica para logs monitorados
        verify(logger).info(
            eq("User created successfully"), 
            eq(kv("userId", createdUser.id().value())),
            eq(kv("action", "USER_CREATED")),
            any() // timestamp
        );
    }
    
    @Test
    @DisplayName("Then should log error for alerting when user creation fails")
    void shouldLogErrorForAlerting_WhenUserCreationFails() {
        // Given
        var command = new CreateUserCommand("John", "invalid@email");
        var exception = new ValidationException("Email format invalid");
        when(userRepository.save(any())).thenThrow(exception);
        
        // When
        assertThatThrownBy(() -> userService.createUser(command));
        
        // Then - Log crítico para alertas
        verify(logger).error(
            eq("Failed to create user - ALERT_REQUIRED"),
            eq(kv("email", "inv***@email")), // Email mascarado
            eq(kv("error", exception.getMessage())),
            eq(kv("action", "USER_CREATION_FAILED"))
        );
    }
}
```

## Value Objects e Domain Testing

### Teste de Value Objects com Validações
```java
@DisplayName("Email Value Object")
class EmailTest {
    
    @Nested
    @DisplayName("When creating email")
    class WhenCreatingEmail {
        
        @Test
        @DisplayName("Then should create successfully when valid format provided")
        void shouldCreateSuccessfully_WhenValidFormatProvided() {
            // Given
            var validEmailString = "user@domain.com";
            
            // When
            var email = new Email(validEmailString);
            
            // Then
            assertThat(email.value()).isEqualTo(validEmailString);
        }
        
        @ParameterizedTest
        @ValueSource(strings = {"", " ", "no-at-symbol", "@domain.com", "user@"})
        @DisplayName("Then should throw exception when invalid formats provided")
        void shouldThrowException_WhenInvalidFormatsProvided(String invalidEmail) {
            // When & Then
            assertThatThrownBy(() -> new Email(invalidEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid email format");
        }
    }
    
    @Nested
    @DisplayName("When comparing emails")
    class WhenComparingEmails {
        
        @Test
        @DisplayName("Then should be equal when same email value")
        void shouldBeEqual_WhenSameEmailValue() {
            // Given
            var email1 = new Email("test@domain.com");
            var email2 = new Email("test@domain.com");
            
            // When & Then
            assertThat(email1).isEqualTo(email2);
        }
    }
}
```

## Use Cases Testing

### Mock Management e Verification
```java
@ExtendWith(MockitoExtension.class)
@DisplayName("CreateUserUseCase")
class CreateUserUseCaseTest {
    
    @Mock private UserRepository userRepository;
    @Mock private EmailService emailService;
    @Mock private DomainEventPublisher eventPublisher;
    
    @InjectMocks
    private CreateUserUseCase createUserUseCase;
    
    @BeforeEach
    void resetMocksAndSetup() {
        Mockito.clearInvocations(userRepository, emailService, eventPublisher);
        
        // Setup comum para cenários de sucesso
        when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
    }
    
    @Nested
    @DisplayName("When valid command provided")
    class WhenValidCommandProvided {
        
        private CreateUserCommand validCommand;
        private User expectedUser;
        
        @BeforeEach
        void setupValidScenario() {
            validCommand = new CreateUserCommand("John Doe", "john@domain.com");
            expectedUser = new User(
                new UserId(UUID.randomUUID()),
                new Email(validCommand.email()),
                new UserName(validCommand.name())
            );
            when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        }
        
        @Test
        @DisplayName("Then should return success result")
        void shouldReturnSuccessResult() {
            // When
            var result = createUserUseCase.execute(validCommand);
            
            // Then
            assertThat(result).isInstanceOf(CreateUserResult.Success.class);
        }
        
        @Test
        @DisplayName("Then should save user in repository")
        void shouldSaveUserInRepository() {
            // When
            createUserUseCase.execute(validCommand);
            
            // Then
            verify(userRepository).save(argThat(user -> 
                user.email().value().equals(validCommand.email()) &&
                user.name().value().equals(validCommand.name())
            ));
        }
        
        @Test
        @DisplayName("Then should send welcome email")
        void shouldSendWelcomeEmail() {
            // When
            createUserUseCase.execute(validCommand);
            
            // Then
            verify(emailService).sendWelcomeEmail(expectedUser);
        }
        
        @Test
        @DisplayName("Then should publish user created event")
        void shouldPublishUserCreatedEvent() {
            // When
            createUserUseCase.execute(validCommand);
            
            // Then
            verify(eventPublisher).publish(argThat(event -> 
                event instanceof UserCreatedEvent &&
                ((UserCreatedEvent) event).userId().equals(expectedUser.id())
            ));
        }
    }
    
    @Nested
    @DisplayName("When email already exists")
    class WhenEmailAlreadyExists {
        
        private CreateUserCommand commandWithExistingEmail;
        
        @BeforeEach
        void setupEmailExistsScenario() {
            commandWithExistingEmail = new CreateUserCommand("Jane Doe", "existing@domain.com");
            when(userRepository.existsByEmail(new Email(commandWithExistingEmail.email())))
                .thenReturn(true);
        }
        
        @Test
        @DisplayName("Then should return validation error")
        void shouldReturnValidationError() {
            // When
            var result = createUserUseCase.execute(commandWithExistingEmail);
            
            // Then
            assertThat(result).isInstanceOf(CreateUserResult.ValidationError.class);
        }
        
        @Test
        @DisplayName("Then should not save user")
        void shouldNotSaveUser() {
            // When
            createUserUseCase.execute(commandWithExistingEmail);
            
            // Then
            verify(userRepository, never()).save(any(User.class));
        }
        
        @Test
        @DisplayName("Then should not send welcome email")
        void shouldNotSendWelcomeEmail() {
            // When
            createUserUseCase.execute(commandWithExistingEmail);
            
            // Then
            verify(emailService, never()).sendWelcomeEmail(any(User.class));
        }
    }
}
```

## Integration Testing

### TestContainers Setup
```java
@QuarkusTest
@Testcontainers
@DisplayName("User Repository Integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withReuse(true); // Reutilizar entre execuções
    
    @Inject
    UserRepository userRepository;
    
    @BeforeAll
    void setupDatabase() {
        postgres.start();
    }
    
    @BeforeEach
    void cleanDatabase() {
        // ✅ Limpeza determinística entre testes
        userRepository.deleteAll();
    }
    
    @Nested
    @DisplayName("When saving user")
    class WhenSavingUser {
        
        @Test
        @DisplayName("Then should persist and retrieve user correctly")
        void shouldPersistAndRetrieveUserCorrectly() {
            // Given
            var user = new User(
                new UserId(UUID.randomUUID()),
                new Email("integration@test.com"),
                new UserName("Integration Test User")
            );
            
            // When
            var savedUser = userRepository.save(user);
            var retrievedUser = userRepository.findById(savedUser.id());
            
            // Then
            assertThat(retrievedUser).isPresent();
            assertThat(retrievedUser.get().email()).isEqualTo(user.email());
        }
    }
}
```

## REST API Testing

### RESTAssured com Estrutura Organizada
```java
@QuarkusTest
@DisplayName("User API")
class UserControllerTest {
    
    @BeforeEach
    void cleanDatabase() {
        // Limpar dados entre testes para garantir determinismo
        given()
            .when().delete("/test/cleanup")
            .then().statusCode(200);
    }
    
    @Nested
    @DisplayName("When creating user")
    class WhenCreatingUser {
        
        @Test
        @DisplayName("Then should return 201 when valid request provided")
        void shouldReturn201_WhenValidRequestProvided() {
            // Given
            var userRequest = """
                {
                    "name": "John Doe",
                    "email": "john@api.test.com"
                }
                """;
            
            // When & Then
            given()
                    .contentType(ContentType.JSON)
                    .body(userRequest)
            .when()
                    .post("/api/v1/users")
            .then()
                    .statusCode(201)
                    .body("id", notNullValue())
                    .body("name", equalTo("John Doe"))
                    .body("email", equalTo("john@api.test.com"))
                    .body("status", equalTo("ACTIVE"));
        }
        
        @Test
        @DisplayName("Then should return 400 when invalid email provided")
        void shouldReturn400_WhenInvalidEmailProvided() {
            // Given
            var invalidRequest = """
                {
                    "name": "John Doe",
                    "email": "invalid-email-format"
                }
                """;
            
            // When & Then
            given()
                    .contentType(ContentType.JSON)
                    .body(invalidRequest)
            .when()
                    .post("/api/v1/users")
            .then()
                    .statusCode(400)
                    .body("message", equalTo("Validation failed"))
                    .body("errors", hasItem(containsString("Invalid email format")));
        }
    }
    
    @Nested
    @DisplayName("When retrieving user")
    class WhenRetrievingUser {
        
        private UUID existingUserId;
        
        @BeforeEach
        void createTestUser() {
            // Setup de dados para os testes deste contexto
            var response = given()
                    .contentType(ContentType.JSON)
                    .body("""
                        {
                            "name": "Existing User",
                            "email": "existing@test.com"
                        }
                        """)
            .when()
                    .post("/api/v1/users")
            .then()
                    .statusCode(201)
                    .extract().response();
            
            existingUserId = UUID.fromString(response.path("id"));
        }
        
        @Test
        @DisplayName("Then should return user when valid ID provided")
        void shouldReturnUser_WhenValidIdProvided() {
            // When & Then
            given()
            .when()
                    .get("/api/v1/users/{id}", existingUserId)
            .then()
                    .statusCode(200)
                    .body("id", equalTo(existingUserId.toString()))
                    .body("name", equalTo("Existing User"))
                    .body("email", equalTo("existing@test.com"));
        }
        
        @Test
        @DisplayName("Then should return 404 when non-existent ID provided")
        void shouldReturn404_WhenNonExistentIdProvided() {
            // Given
            var nonExistentId = UUID.randomUUID();
            
            // When & Then
            given()
            .when()
                    .get("/api/v1/users/{id}", nonExistentId)
            .then()
                    .statusCode(404);
        }
    }
}
```

## Performance Testing

### JMH Benchmarks
```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@DisplayName("Email Validation Performance")
public class EmailValidationBenchmark {
    
    private String validEmail = "user@domain.com";
    private String invalidEmail = "invalid-email-format";
    
    @Benchmark
    @DisplayName("Valid email creation benchmark")
    public Email createValidEmail() {
        return new Email(validEmail);
    }
    
    @Benchmark  
    @DisplayName("Invalid email validation benchmark")
    public void validateInvalidEmail() {
        try {
            new Email(invalidEmail);
        } catch (IllegalArgumentException e) {
            // Expected exception for invalid email
        }
    }
}
```

## Test Data Management

### Test Data Builders
```java
// ✅ PADRÃO - Builder fluente para dados de teste
public final class UserTestDataBuilder {
    
    private String name = "Default User";
    private String email = "default@test.com";
    private UserStatus status = UserStatus.ACTIVE;
    private UUID id = UUID.randomUUID();
    
    private UserTestDataBuilder() {}
    
    public static UserTestDataBuilder aUser() {
        return new UserTestDataBuilder();
    }
    
    public UserTestDataBuilder withName(String name) {
        this.name = name;
        return this;
    }
    
    public UserTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }
    
    public UserTestDataBuilder withId(UUID id) {
        this.id = id;
        return this;
    }
    
    public UserTestDataBuilder inactive() {
        this.status = UserStatus.INACTIVE;
        return this;
    }
    
    public User build() {
        return new User(
            new UserId(id),
            new Email(email),
            new UserName(name),
            status
        );
    }
    
    public CreateUserCommand buildCommand() {
        return new CreateUserCommand(name, email);
    }
    
    public CreateUserRequest buildRequest() {
        return new CreateUserRequest(name, email);
    }
}

// ✅ USO - Testes mais legíveis
@Test
@DisplayName("Then should create premium user when valid data provided")
void shouldCreatePremiumUser_WhenValidDataProvided() {
    // Given
    var command = aUser()
        .withName("Premium User")
        .withEmail("premium@test.com")
        .buildCommand();
    
    // When & Then...
}
```

### Database Fixtures
```java
@Component
@TestProfile("test")
@DisplayName("Database Test Fixtures")
public class DatabaseTestFixtures {
    
    @Inject
    EntityManager entityManager;
    
    @Transactional
    public User createPersistedUser() {
        return createPersistedUser("Test User", "test@fixture.com");
    }
    
    @Transactional
    public User createPersistedUser(String name, String email) {
        var userEntity = new UserEntity();
        userEntity.id = UUID.randomUUID();
        userEntity.name = name;
        userEntity.email = email;
        userEntity.status = "ACTIVE";
        userEntity.createdAt = LocalDateTime.now();
        
        entityManager.persist(userEntity);
        entityManager.flush();
        
        return toDomainUser(userEntity);
    }
    
    @Transactional
    public List<User> createMultipleUsers(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> createPersistedUser("User " + i, "user" + i + "@fixture.com"))
            .collect(Collectors.toList());
    }
    
    @Transactional
    public void cleanupAllUsers() {
        entityManager.createQuery("DELETE FROM UserEntity").executeUpdate();
    }
    
    private User toDomainUser(UserEntity entity) {
        return new User(
            new UserId(entity.id),
            new Email(entity.email),
            new UserName(entity.name),
            UserStatus.valueOf(entity.status)
        );
    }
}
```

## Mutation Testing
```xml
<!-- ✅ PADRÃO - pom.xml configuração do PITest -->
<plugin>
    <groupId>org.pitest</groupId>
    <artifactId>pitest-maven</artifactId>
    <version>1.15.2</version>
    <configuration>
        <targetClasses>
            <param>com.myproject.domain.*</param>
            <param>com.myproject.application.*</param>
        </targetClasses>
        <targetTests>
            <param>com.myproject.*Test</param>
        </targetTests>
        <mutationThreshold>80</mutationThreshold>
        <coverageThreshold>90</coverageThreshold>
        <timestampedReports>false</timestampedReports>
        <mutators>
            <mutator>DEFAULTS</mutator>
        </mutators>
        <outputFormats>
            <outputFormat>XML</outputFormat>
            <outputFormat>HTML</outputFormat>
        </outputFormats>
    </configuration>
    <executions>
        <execution>
            <id>mutation-test</id>
            <goals>
                <goal>mutationCoverage</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Exemplo de Mutation Testing
```java
// ✅ PADRÃO - Teste que sobrevive a mutações
class EmailTest {
    
    @Test
    void should_RejectEmptyEmail() {
        // Mutation: trocar isEmpty() por !isEmpty()
        // Este teste detecta a mutação
        assertThrows(IllegalArgumentException.class, () -> new Email(""));
    }
    
    @Test 
    void should_RejectEmailWithoutAtSymbol() {
        // Mutation: trocar !contains("@") por contains("@")
        // Este teste detecta a mutação
        assertThrows(IllegalArgumentException.class, () -> new Email("invalid-email"));
    }
    
    @Test
    void should_AcceptValidEmail() {
        // Testa cenário positivo para detectar mutações nos validadores
        var email = new Email("valid@email.com");
        assertEquals("valid@email.com", email.value());
    }
}
```

## Contract Testing

### Consumer-Driven Contracts com Pact
```xml
<!-- ✅ PADRÃO - pom.xml Pact dependency -->
<dependency>
    <groupId>au.com.dius.pact.consumer</groupId>
    <artifactId>junit5</artifactId>
    <version>4.6.4</version>
    <scope>test</scope>
</dependency>
```

```java
// ✅ PADRÃO - Consumer test
@ExtendWith(PactConsumerTestExt.class)
class PaymentServiceContractTest {
    
    @Pact(consumer = "user-service", provider = "payment-service")
    public RequestResponsePact processPaymentContract(PactDslWithProvider builder) {
        return builder
            .given("payment service is available")
            .uponReceiving("a request to process payment")
            .path("/payments")
            .method("POST")
            .headers(Map.of("Content-Type", "application/json"))
            .body("""
                {
                  "orderId": "123e4567-e89b-12d3-a456-426614174000",
                  "amount": 99.99,
                  "currency": "USD"
                }
                """)
            .willRespondWith()
            .status(201)
            .headers(Map.of("Content-Type", "application/json"))
            .body("""
                {
                  "paymentId": "pay_123",
                  "status": "SUCCESS",
                  "transactionId": "tx_456"
                }
                """)
            .toPact();
    }
    
    @Test
    @PactTestFor(pactMethod = "processPaymentContract")
    void should_ProcessPaymentSuccessfully(MockServer mockServer) {
        // Given
        var paymentRequest = new PaymentRequest(
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            new Money(BigDecimal.valueOf(99.99)),
            Currency.USD
        );
        
        var paymentService = new PaymentServiceClient(mockServer.getUrl());
        
        // When
        var result = paymentService.processPayment(paymentRequest);
        
        // Then
        assertEquals("SUCCESS", result.status());
        assertEquals("pay_123", result.paymentId());
    }
}
```

## Test Pyramid Strategy

### Distribution de Testes
- **Unit Tests (70%)**: Domain logic, Value Objects, Use Cases
- **Integration Tests (20%)**: Repository, External services, Database
- **E2E Tests (10%)**: User journeys, Critical paths

### Test Categories
```java
// ✅ PADRÃO - Test categories para organização
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Test
@Tag("unit")
public @interface UnitTest {}

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Test
@Tag("integration")
public @interface IntegrationTest {}

// ✅ USO - Execução seletiva: mvn test -Dgroups="unit"
```

## Test Smells - O que Evitar

### Testes Não-Determinísticos
```java
// ❌ EVITAR - Dependência de tempo/data atual
@Test
void shouldCreateUserWithCurrentTimestamp() {
    var user = userService.createUser(command);
    assertThat(user.createdAt()).isEqualTo(LocalDateTime.now()); // Pode falhar!
}

// ✅ CORRETO - Mock ou injeção de Clock
@Test
@DisplayName("Then should create user with fixed timestamp when clock injected")
void shouldCreateUserWithFixedTimestamp_WhenClockInjected() {
    // Given
    var fixedTime = LocalDateTime.of(2024, 1, 15, 10, 30);
    var clock = Clock.fixed(fixedTime.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
    when(timeService.now()).thenReturn(fixedTime);
    
    // When
    var user = userService.createUser(command);
    
    // Then
    assertThat(user.createdAt()).isEqualTo(fixedTime);
}
```

### Testes com Dependências Externas
```java
// ❌ EVITAR - Chamadas reais para serviços externos
@Test
void shouldValidateEmailWithExternalService() {
    var isValid = emailValidationService.validateWithProvider("test@gmail.com");
    assertTrue(isValid); // Depende de rede/serviço externo
}

// ✅ CORRETO - Mock de serviços externos
@Test
@DisplayName("Then should return true when external service validates email")
void shouldReturnTrue_WhenExternalServiceValidatesEmail() {
    // Given
    var email = "test@gmail.com";
    when(externalEmailValidator.validate(email)).thenReturn(true);
    
    // When
    var result = emailValidationService.validateWithProvider(email);
    
    // Then
    assertThat(result).isTrue();
}
```

### Testes com Estado Compartilhado
```java
// ❌ EVITAR - Estado compartilhado entre testes
class UserServiceTest {
    private static List<User> createdUsers = new ArrayList<>(); // Estado compartilhado!
    
    @Test
    void shouldCreateFirstUser() {
        var user = userService.createUser(command1);
        createdUsers.add(user);
        assertThat(createdUsers).hasSize(1);
    }
    
    @Test
    void shouldCreateSecondUser() {
        var user = userService.createUser(command2);
        createdUsers.add(user);
        assertThat(createdUsers).hasSize(2); // Falha se executar isoladamente!
    }
}

// ✅ CORRETO - Cada teste é independente
@Nested
@DisplayName("When creating multiple users")
class WhenCreatingMultipleUsers {
    
    @Test
    @DisplayName("Then should create first user successfully")
    void shouldCreateFirstUserSuccessfully() {
        // Given
        var command = aUser().withEmail("first@test.com").buildCommand();
        
        // When
        var user = userService.createUser(command);
        
        // Then
        assertThat(user.email().value()).isEqualTo("first@test.com");
    }
    
    @Test
    @DisplayName("Then should create second user successfully")
    void shouldCreateSecondUserSuccessfully() {
        // Given
        var command = aUser().withEmail("second@test.com").buildCommand();
        
        // When
        var user = userService.createUser(command);
        
        // Then
        assertThat(user.email().value()).isEqualTo("second@test.com");
    }
}
```

### Testes Muito Genéricos
```java
// ❌ EVITAR - Teste genérico sem foco
@Test
void shouldWorkCorrectly() {
    var result = userService.processUser(someInput);
    assertNotNull(result); // Muito vago!
}

// ✅ CORRETO - Teste específico e focado
@Test
@DisplayName("Then should return user with active status when user created successfully")
void shouldReturnUserWithActiveStatus_WhenUserCreatedSuccessfully() {
    // Given
    var command = aUser().withName("Active User").buildCommand();
    
    // When
    var result = userService.createUser(command);
    
    // Then
    assertThat(result.status()).isEqualTo(UserStatus.ACTIVE);
}
```

## Configurações Avançadas

### Maven Surefire Configuration
```xml
<!-- ✅ PADRÃO - Configuração otimizada do Maven Surefire -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.2.2</version>
    <configuration>
        <!-- Execução paralela para performance -->
        <parallel>all</parallel>
        <threadCount>4</threadCount>
        <forkCount>1C</forkCount>
        <reuseForks>true</reuseForks>
        
        <!-- Filtragem por tags -->
        <groups>unit,integration</groups>
        <excludedGroups>e2e,performance</excludedGroups>
        
        <!-- Configuração de relatórios -->
        <reportFormat>xml</reportFormat>
        <includes>
            <include>**/*Test.java</include>
            <include>**/*Tests.java</include>
        </includes>
        
        <!-- System properties -->
        <systemPropertyVariables>
            <java.util.logging.manager>org.jboss.logmanager.LogManager</java.util.logging.manager>
            <test.environment>true</test.environment>
        </systemPropertyVariables>
        
        <!-- Retry para testes flaky (usar com parcimônia) -->
        <rerunFailingTestsCount>2</rerunFailingTestsCount>
    </configuration>
</plugin>
```

### Application Test Properties
```properties
# ✅ PADRÃO - application-test.properties otimizado
# Database - H2 em memória para testes rápidos
quarkus.datasource.db-kind=h2
quarkus.datasource.jdbc.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
quarkus.hibernate-orm.database.generation=drop-and-create

# Desabilitar recursos desnecessários em testes
quarkus.mailer.mock=true
quarkus.scheduler.enabled=false
quarkus.test.continuous-testing=disabled

# Logging otimizado para testes
quarkus.log.level=WARN
quarkus.log.category."com.myproject".level=DEBUG
quarkus.log.console.format=%d{HH:mm:ss} %-5p [%c{2.}] %s%e%n

# Timeouts ajustados para testes
quarkus.http.test-timeout=30s

# Cache desabilitado em testes
quarkus.cache.enabled=false
```

## Test Categories e Execution

### Anotações para Categorização
```java
// ✅ PADRÃO - Anotações customizadas para organização
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Test
@Tag("unit")
public @interface UnitTest {
    String value() default "";
}

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Test
@Tag("integration")
public @interface IntegrationTest {
    String value() default "";
}

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Test
@Tag("e2e")
public @interface EndToEndTest {
    String value() default "";
}

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Test
@Tag("performance")
public @interface PerformanceTest {
    String value() default "";
}

// ✅ USO - Categorização clara dos testes
@UnitTest
@DisplayName("Email validation unit tests")
class EmailUnitTest { }

@IntegrationTest
@DisplayName("User repository integration tests")
class UserRepositoryIntegrationTest { }

@EndToEndTest  
@DisplayName("User management end-to-end tests")
class UserManagementE2ETest { }
```

### Execução Seletiva via Maven
```bash
# ✅ Executar apenas testes unitários
mvn test -Dgroups="unit"

# ✅ Executar testes de integração
mvn test -Dgroups="integration"

# ✅ Executar todos exceto E2E
mvn test -DexcludedGroups="e2e,performance"

# ✅ Pipeline - diferentes fases
mvn test -Dgroups="unit" # Fase 1: Rápida
mvn test -Dgroups="integration" # Fase 2: Média
mvn test -Dgroups="e2e" # Fase 3: Lenta
```

## Checklist de Qualidade - Obrigatório

### ✅ Para Cada Teste
- [ ] Nome descritivo com padrão `should_ExpectedBehavior_When_Condition`
- [ ] Anotação `@DisplayName` com descrição clara
- [ ] Estrutura Given/When/Then bem definida
- [ ] **APENAS UM ASSERT POR TESTE** (Object Calisthenics)
- [ ] Mocks limpos com `@BeforeEach`
- [ ] Teste determinístico (sempre mesmo resultado)
- [ ] Sem dependências de outros testes
- [ ] Sem lógica complexa no teste

### ✅ Para Cada Classe de Teste
- [ ] Organizada com `@Nested` por contexto
- [ ] Estrutura de diretórios espelha código fonte
- [ ] Setup adequado com `@BeforeEach`/`@BeforeAll`
- [ ] Limpeza de recursos entre testes
- [ ] Categorizada com tags apropriadas
- [ ] Coverage > 85% para domínio, > 70% para aplicação

### ✅ Para Feature Completa
- [ ] Testes unitários para regras de negócio
- [ ] Testes de integração para componentes críticos  
- [ ] Testes de contrato para APIs externas
- [ ] Mutation testing score > 80%
- [ ] Testes de performance para endpoints críticos
- [ ] Validação de logs para observabilidade
- [ ] Testes de segurança para validação de entrada

### ✅ Para Release
- [ ] Todos os testes passando
- [ ] Coverage thresholds atingidos
- [ ] Mutation testing executado
- [ ] Testes flaky identificados e corrigidos
- [ ] Performance benchmarks executados
- [ ] Integration tests com ambiente similar a produção


````should_ExpectedBehavior_When_Condition
@Test
void should_ReturnUser_When_ValidIdProvided() { }

@Test
void should_ThrowUserNotFoundException_When_UserNotFound() { }

@Test
void should_CreateOrder_When_ValidDataProvided() { }
```

### Organização Given/When/Then
```java
@Test
void should_CalculateTotalValue_When_MultipleItemsProvided() {
    // Given
    var item1 = new OrderItem(new ProductId(UUID.randomUUID()), new Money(BigDecimal.valueOf(10)));
    var item2 = new OrderItem(new ProductId(UUID.randomUUID()), new Money(BigDecimal.valueOf(20)));
    var orderItems = new OrderItems(List.of(item1, item2));
    
    // When
    var totalValue = orderItems.totalValue();
    
    // Then
    assertEquals(new Money(BigDecimal.valueOf(30)), totalValue);
}
```

## Testes Unitários - Domínio

### Value Objects
```java
class EmailTest {
    
    @Test
    void should_CreateEmail_When_ValidFormatProvided() {
        // Given
        var validEmail = "user@domain.com";
        
        // When
        var email = new Email(validEmail);
        
        // Then
        assertEquals(validEmail, email.value());
    }
    
    @Test
    void should_ThrowException_When_InvalidEmailFormat() {
        // Given
        var invalidEmail = "invalid-email";
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "no-at-symbol", "@domain.com", "user@"})
    void should_ThrowException_When_InvalidEmailFormats(String invalidEmail) {
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }
}
```

### Entities
```java
class UserTest {
    
    @Test
    void should_CreateUser_When_ValidDataProvided() {
        // Given
        var userId = new UserId(UUID.randomUUID());
        var email = new Email("user@domain.com");
        var name = new UserName("John Doe");
        
        // When
        var user = new User(userId, email, name);
        
        // Then
        assertEquals(userId, user.id());
        assertEquals(email, user.email());
        assertEquals(name, user.name());
    }
    
    @Test
    void should_ChangeEmail_When_ValidEmailProvided() {
        // Given
        var user = createValidUser();
        var newEmail = new Email("newemail@domain.com");
        
        // When
        var updatedUser = user.changeEmail(newEmail);
        
        // Then
        assertEquals(newEmail, updatedUser.email());
        assertNotSame(user, updatedUser); // Imutabilidade
    }
    
    private User createValidUser() {
        return new User(
            new UserId(UUID.randomUUID()),
            new Email("original@domain.com"),
            new UserName("John Doe")
        );
    }
}
```

## Testes de Use Cases

### Mock de Dependências
```java
@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private EmailService emailService;
    
    @InjectMocks
    private CreateUserUseCase createUserUseCase;
    
    @Test
    void should_CreateUser_When_ValidCommandProvided() {
        // Given
        var command = new CreateUserCommand("John Doe", "john@domain.com");
        var expectedUser = new User(
            new UserId(UUID.randomUUID()),
            new Email(command.email()),
            new UserName(command.name())
        );
        
        when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        
        // When
        var result = createUserUseCase.execute(command);
        
        // Then
        assertThat(result).isInstanceOf(CreateUserResult.Success.class);
        var success = (CreateUserResult.Success) result;
        assertEquals(expectedUser, success.user());
        
        verify(userRepository).save(any(User.class));
        verify(emailService).sendWelcomeEmail(expectedUser);
    }
    
    @Test
    void should_ReturnValidationError_When_EmailAlreadyExists() {
        // Given
        var command = new CreateUserCommand("John Doe", "existing@domain.com");
        when(userRepository.existsByEmail(any(Email.class))).thenReturn(true);
        
        // When
        var result = createUserUseCase.execute(command);
        
        // Then
        assertThat(result).isInstanceOf(CreateUserResult.ValidationError.class);
        var error = (CreateUserResult.ValidationError) result;
        assertThat(error.errors()).contains("Email already exists");
        
        verify(userRepository, never()).save(any(User.class));
        verify(emailService, never()).sendWelcomeEmail(any(User.class));
    }
}
```

## Testes de Integração - Quarkus

### Configuração Base
```java
@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
class UserIntegrationTest {
    
    @Inject
    UserService userService;
    
    @Test
    @Order(1)
    void should_CreateUser_When_ValidDataProvided() {
        // Given
        var createUserDto = new CreateUserDto("John Doe", "john@integration.com");
        
        // When
        var createdUser = userService.createUser(createUserDto);
        
        // Then
        assertNotNull(createdUser.id());
        assertEquals("John Doe", createdUser.name());
        assertEquals("john@integration.com", createdUser.email());
    }
}
```

### Testes com TestContainers
```java
@QuarkusTest
@Testcontainers
class UserRepositoryIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    
    @ConfigProperty(name = "quarkus.datasource.jdbc.url")
    String datasourceUrl;
    
    @BeforeAll
    static void setUp() {
        postgres.start();
    }
    
    @Test
    void should_SaveAndRetrieveUser_When_ValidUserProvided() {
        // Given
        var user = createValidUser();
        
        // When
        var savedUser = userRepository.save(user);
        var retrievedUser = userRepository.findById(savedUser.id());
        
        // Then
        assertTrue(retrievedUser.isPresent());
        assertEquals(savedUser.email(), retrievedUser.get().email());
    }
}
```

## Testes REST API

### REST Assured
```java
@QuarkusTest
class UserControllerTest {
    
    @Test
    void should_CreateUser_When_ValidRequestProvided() {
        var request = """
            {
                "name": "John Doe",
                "email": "john@api.com"
            }
            """;
        
        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/users")
        .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("John Doe"))
                .body("email", equalTo("john@api.com"));
    }
    
    @Test
    void should_ReturnValidationError_When_InvalidEmailProvided() {
        var request = """
            {
                "name": "John Doe",
                "email": "invalid-email"
            }
            """;
        
        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/users")
        .then()
                .statusCode(400)
                .body("errors", hasItem("Invalid email format"));
    }
}
```

## Testes de Performance

### JMH (Benchmarks)
```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class EmailValidationBenchmark {
    
    private Email validEmail;
    private String validEmailString = "user@domain.com";
    
    @Setup
    public void setup() {
        validEmail = new Email(validEmailString);
    }
    
    @Benchmark
    public Email createEmail() {
        return new Email(validEmailString);
    }
    
    @Benchmark
    public boolean validateEmail() {
        return validEmail.isValid();
    }
}
```

## Configurações de Teste

### application-test.properties
```properties
# Database
quarkus.datasource.db-kind=h2
quarkus.datasource.jdbc.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
quarkus.hibernate-orm.database.generation=drop-and-create

# Logging
quarkus.log.level=INFO
quarkus.log.category."com.myproject".level=DEBUG

# Test specific
quarkus.test.continuous-testing=disabled
```

## Utilitários de Teste
```java
public final class TestDataBuilder {
    
    private TestDataBuilder() {}
    
    public static User.UserBuilder aUser() {
        return User.builder()
                .id(new UserId(UUID.randomUUID()))
                .email(new Email("test@domain.com"))
                .name(new UserName("Test User"));
    }
    
    public static Order.OrderBuilder anOrder() {
        return Order.builder()
                .id(new OrderId(UUID.randomUUID()))
                .customerId(new CustomerId(UUID.randomUUID()))
                .items(new OrderItems(List.of()));
    }
}
```