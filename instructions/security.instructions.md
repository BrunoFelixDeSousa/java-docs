````instructions
---
applyTo: "**/*.java,**/application.properties,**/application.yml"
description: "Padrões de segurança, compliance e proteção de dados"
---

# Padrões de Segurança e Compliance

Aplicar as [instruções gerais](./copilot.instructions.md) e [padrões Java](./java-coding.instructions.md) seguindo práticas de segurança.

## LGPD/GDPR Compliance

### Dados Pessoais - Identificação e Proteção
```java
// ✅ PADRÃO - Anotação para dados sensíveis
@Entity
@Table(name = "users")
public class UserEntity extends PanacheEntityBase {
    @Id
    public UUID id;
    
    @PersonalData // Anotação customizada para dados pessoais
    @Column(nullable = false)
    public String email;
    
    @PersonalData
    public String fullName;
    
    @SensitiveData // Para dados extra sensíveis (CPF, etc.)
    @Convert(converter = EncryptedStringConverter.class)
    public String documentNumber;
    
    @CreationTimestamp
    public LocalDateTime createdAt;
    
    @Column(name = "consent_given_at")
    public LocalDateTime consentGivenAt;
    
    @Column(name = "data_retention_until")
    public LocalDateTime dataRetentionUntil;
}
```

### Anonimização e Pseudonimização
```java
// ✅ PADRÃO - Service para anonimização
@ApplicationScoped
public class DataAnonymizationService {
    
    private final HashFunction hashFunction = Hashing.sha256();
    
    /**
     * Pseudonimiza email para fins analíticos mantendo domínio.
     */
    public String pseudonymizeEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "anonymous@unknown.com";
        }
        
        var parts = email.split("@");
        var hashedLocal = hashFunction.hashString(parts[0], StandardCharsets.UTF_8);
        return hashedLocal.toString().substring(0, 8) + "@" + parts[1];
    }
    
    /**
     * Anonimiza dados para relatórios agregados.
     */
    public AnonymizedUser anonymizeUser(User user) {
        return new AnonymizedUser(
            user.id(), // Manter ID para consistência
            pseudonymizeEmail(user.email().value()),
            user.createdAt().getYear(), // Apenas ano
            user.city(), // Cidade ok para análise
            null, // Remover nome completo
            null  // Remover dados sensíveis
        );
    }
}
```

### Consent Management
```java
// ✅ PADRÃO - Domain Entity para consentimento
public class UserConsent {
    private final UserId userId;
    private final ConsentType type;
    private final ConsentStatus status;
    private final LocalDateTime grantedAt;
    private final LocalDateTime expiresAt;
    private final String purpose;
    private final String legalBasis;
    
    public UserConsent grantConsent(ConsentType type, String purpose) {
        validateCanGrant(type);
        
        return new UserConsent(
            this.userId,
            type,
            ConsentStatus.GRANTED,
            LocalDateTime.now(),
            calculateExpirationDate(type),
            purpose,
            determineLegalBasis(type)
        );
    }
    
    public boolean isValidForProcessing() {
        return status == ConsentStatus.GRANTED && 
               LocalDateTime.now().isBefore(expiresAt);
    }
}

// ✅ PADRÃO - Use case para gestão de consentimento
@ApplicationScoped
public class ManageConsentUseCase {
    
    public ValidateConsentResult validateForProcessing(UserId userId, ConsentType type) {
        var consent = consentRepository.findByUserAndType(userId, type);
        
        return consent
            .filter(UserConsent::isValidForProcessing)
            .map(c -> new ValidateConsentResult.Valid(c))
            .orElse(new ValidateConsentResult.Invalid("Consent not granted or expired"));
    }
}
```

## Autenticação e Autorização

### JWT Security
```java
// ✅ PADRÃO - JWT Token com claims seguros
@ApplicationScoped
public class JwtTokenService {
    
    @ConfigProperty(name = "app.jwt.secret")
    String jwtSecret;
    
    @ConfigProperty(name = "app.jwt.expiration", defaultValue = "3600") // 1 hora
    long jwtExpirationInSeconds;
    
    public String generateToken(User user, Set<Role> roles) {
        var now = Instant.now();
        var expiration = now.plusSeconds(jwtExpirationInSeconds);
        
        return Jwts.builder()
            .setSubject(user.id().value().toString())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiration))
            .claim("email", user.email().value())
            .claim("roles", roles.stream().map(Role::name).toList())
            .claim("session_id", UUID.randomUUID().toString()) // Para revogação
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }
    
    public Optional<TokenClaims> validateAndExtractClaims(String token) {
        try {
            var claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
            
            // Verificar se token não foi revogado
            if (isTokenRevoked(claims.get("session_id", String.class))) {
                return Optional.empty();
            }
            
            return Optional.of(new TokenClaims(
                UUID.fromString(claims.getSubject()),
                claims.get("email", String.class),
                claims.get("roles", List.class),
                claims.get("session_id", String.class)
            ));
            
        } catch (Exception e) {
            Log.warn("Invalid JWT token", e);
            return Optional.empty();
        }
    }
}
```

### Role-Based Access Control
```java
// ✅ PADRÃO - Anotação customizada para autorização
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {
    Role[] value();
    boolean requireAll() default false; // false = OR, true = AND
}

// ✅ PADRÃO - Interceptor para verificação de roles
@Interceptor
@RequiresRole(Role.USER) // Binding annotation
@Priority(Interceptor.Priority.APPLICATION + 10)
public class RoleCheckInterceptor {
    
    @Inject
    SecurityContext securityContext;
    
    @AroundInvoke
    public Object checkRole(InvocationContext context) throws Exception {
        var annotation = getRequiresRoleAnnotation(context);
        var userRoles = securityContext.getCurrentUser().roles();
        var requiredRoles = Set.of(annotation.value());
        
        boolean hasAccess = annotation.requireAll() 
            ? userRoles.containsAll(requiredRoles)
            : requiredRoles.stream().anyMatch(userRoles::contains);
        
        if (!hasAccess) {
            throw new ForbiddenException("Insufficient privileges");
        }
        
        return context.proceed();
    }
}

// ✅ USO - Controller com autorização
@Path("/admin/users")
@ApplicationScoped
public class AdminUserController {
    
    @GET
    @RequiresRole({Role.ADMIN, Role.USER_MANAGER})
    public List<UserResponse> getAllUsers() {
        // Apenas admins ou gerentes podem listar usuários
        return userService.getAllUsers();
    }
    
    @DELETE
    @Path("/{id}")
    @RequiresRole(value = {Role.ADMIN, Role.SUPER_USER}, requireAll = true)
    public Response deleteUser(@PathParam("id") UUID id) {
        // Requer AMBOS os roles
        userService.deleteUser(new UserId(id));
        return Response.noContent().build();
    }
}
```

## Validação e Sanitização de Dados

### Input Validation
```java
// ✅ PADRÃO - Validação customizada para SQL Injection
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SafeStringValidator.class)
public @interface SafeString {
    String message() default "String contains potentially unsafe characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    boolean allowHtml() default false;
    int maxLength() default 255;
}

public class SafeStringValidator implements ConstraintValidator<SafeString, String> {
    
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
        "(?i).*(union|select|insert|update|delete|drop|create|alter|exec|script).*"
    );
    
    private static final Pattern XSS_PATTERN = Pattern.compile(
        "(?i).*(<script|javascript:|onload=|onerror=|alert\\().*"
    );
    
    private boolean allowHtml;
    private int maxLength;
    
    @Override
    public void initialize(SafeString annotation) {
        this.allowHtml = annotation.allowHtml();
        this.maxLength = annotation.maxLength();
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        
        if (value.length() > maxLength) return false;
        
        if (SQL_INJECTION_PATTERN.matcher(value).matches()) return false;
        
        if (!allowHtml && XSS_PATTERN.matcher(value).matches()) return false;
        
        return true;
    }
}

// ✅ USO - DTO com validação
public record CreateUserRequest(
    @NotBlank
    @SafeString(maxLength = 100)
    String name,
    
    @Email
    @SafeString(maxLength = 255)
    String email,
    
    @SafeString(allowHtml = false, maxLength = 500)
    String bio
) {}
```

### Output Encoding
```java
// ✅ PADRÃO - Service para encoding seguro
@ApplicationScoped
public class SecurityEncodingService {
    
    /**
     * Escapa HTML para prevenir XSS em templates.
     */
    public String escapeHtml(String input) {
        if (input == null) return "";
        
        return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;");
    }
    
    /**
     * Sanitiza SQL query parameters.
     */
    public String sanitizeSqlParameter(String input) {
        if (input == null) return "";
        
        // Remove caracteres perigosos para SQL
        return input.replaceAll("[';\"\\-\\-/\\*\\*/]", "");
    }
    
    /**
     * Encoder para logs - remove dados sensíveis.
     */
    public String sanitizeForLogging(String input) {
        if (input == null) return "";
        
        // Mascarar emails
        String sanitized = input.replaceAll(
            "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b", 
            "***@***.***"
        );
        
        // Mascarar CPFs
        sanitized = sanitized.replaceAll(
            "\\b\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}\\b", 
            "***.***.**-**"
        );
        
        return sanitized;
    }
}
```

## Criptografia

### Encryption Service
```java
// ✅ PADRÃO - Service para criptografia simétrica
@ApplicationScoped
public class EncryptionService {
    
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    
    @ConfigProperty(name = "app.encryption.key")
    String encryptionKey;
    
    private SecretKeySpec secretKey;
    
    @PostConstruct
    public void init() {
        byte[] key = Base64.getDecoder().decode(encryptionKey);
        this.secretKey = new SecretKeySpec(key, "AES");
    }
    
    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            
            byte[] iv = new byte[GCM_IV_LENGTH];
            new SecureRandom().nextBytes(iv);
            
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            
            byte[] encryptedText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            
            // Combinar IV + dados criptografados
            byte[] encryptedWithIv = new byte[GCM_IV_LENGTH + encryptedText.length];
            System.arraycopy(iv, 0, encryptedWithIv, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedText, 0, encryptedWithIv, GCM_IV_LENGTH, encryptedText.length);
            
            return Base64.getEncoder().encodeToString(encryptedWithIv);
            
        } catch (Exception e) {
            throw new SecurityException("Encryption failed", e);
        }
    }
    
    public String decrypt(String encryptedText) {
        try {
            byte[] decodedText = Base64.getDecoder().decode(encryptedText);
            
            // Extrair IV
            byte[] iv = Arrays.copyOfRange(decodedText, 0, GCM_IV_LENGTH);
            byte[] cipherText = Arrays.copyOfRange(decodedText, GCM_IV_LENGTH, decodedText.length);
            
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            
            byte[] decryptedText = cipher.doFinal(cipherText);
            return new String(decryptedText, StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            throw new SecurityException("Decryption failed", e);
        }
    }
}

// ✅ PADRÃO - JPA Converter para dados criptografados
@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {
    
    @Inject
    EncryptionService encryptionService;
    
    @Override
    public String convertToDatabaseColumn(String attribute) {
        return attribute == null ? null : encryptionService.encrypt(attribute);
    }
    
    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData == null ? null : encryptionService.decrypt(dbData);
    }
}
```

### Password Hashing
```java
// ✅ PADRÃO - Service para hash de senhas
@ApplicationScoped
public class PasswordService {
    
    private static final int BCRYPT_ROUNDS = 12;
    
    public String hashPassword(String plainPassword) {
        validatePasswordStrength(plainPassword);
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }
    
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            Log.warn("Password verification failed", e);
            return false;
        }
    }
    
    private void validatePasswordStrength(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain lowercase letter");
        }
        
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain uppercase letter");
        }
        
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain number");
        }
        
        if (!password.matches(".*[!@#$%^&*].*")) {
            throw new IllegalArgumentException("Password must contain special character");
        }
    }
}
```

## Auditoria e Logging Seguro

### Audit Trail
```java
// ✅ PADRÃO - Entity para auditoria
@Entity
@Table(name = "audit_events")
public class AuditEvent extends PanacheEntityBase {
    @Id
    public UUID id;
    
    public UUID userId;
    public String action;
    public String entityType;
    public UUID entityId;
    
    @Column(columnDefinition = "jsonb")
    public String changes; // JSON com alterações
    
    public String ipAddress;
    public String userAgent;
    
    @CreationTimestamp
    public LocalDateTime timestamp;
    
    public String result; // SUCCESS, FAILURE, etc.
    public String errorMessage;
}

// ✅ PADRÃO - Interceptor para auditoria automática
@Interceptor
@Auditable
@Priority(Interceptor.Priority.APPLICATION + 20)
public class AuditInterceptor {
    
    @Inject
    AuditService auditService;
    
    @Inject
    SecurityContext securityContext;
    
    @AroundInvoke
    public Object audit(InvocationContext context) throws Exception {
        var startTime = Instant.now();
        var method = context.getMethod();
        var auditable = method.getAnnotation(Auditable.class);
        
        try {
            var result = context.proceed();
            
            auditService.recordSuccess(AuditInfo.builder()
                .userId(securityContext.getCurrentUserId())
                .action(auditable.action())
                .entityType(auditable.entityType())
                .method(method.getName())
                .parameters(sanitizeParameters(context.getParameters()))
                .result(result)
                .duration(Duration.between(startTime, Instant.now()))
                .build());
            
            return result;
            
        } catch (Exception e) {
            auditService.recordFailure(AuditInfo.builder()
                .userId(securityContext.getCurrentUserId())
                .action(auditable.action())
                .entityType(auditable.entityType())
                .method(method.getName())
                .parameters(sanitizeParameters(context.getParameters()))
                .error(e.getMessage())
                .duration(Duration.between(startTime, Instant.now()))
                .build());
            
            throw e;
        }
    }
}

// ✅ USO - Service com auditoria
@ApplicationScoped
public class UserService {
    
    @Auditable(action = "CREATE_USER", entityType = "User")
    public User createUser(CreateUserCommand command) {
        // Implementação
    }
    
    @Auditable(action = "DELETE_USER", entityType = "User")
    public void deleteUser(UserId userId) {
        // Implementação
    }
}
```

## Configurações de Segurança

### application.properties
```properties
# JWT Configuration
app.jwt.secret=${JWT_SECRET:change-this-secret-key-in-production}
app.jwt.expiration=3600

# Encryption
app.encryption.key=${ENCRYPTION_KEY:base64-encoded-256-bit-key}

# CORS
quarkus.http.cors=true
quarkus.http.cors.origins=https://yourdomain.com
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with
quarkus.http.cors.methods=GET,POST,PUT,DELETE

# Security Headers
quarkus.http.header.x-frame-options.value=DENY
quarkus.http.header.x-content-type-options.value=nosniff
quarkus.http.header.x-xss-protection.value=1; mode=block
quarkus.http.header.strict-transport-security.value=max-age=31536000; includeSubDomains

# Database - Nunca expor credenciais em plain text
quarkus.datasource.username=${DB_USERNAME:user}
quarkus.datasource.password=${DB_PASSWORD:password}

# SSL/TLS
quarkus.http.ssl-port=8443
quarkus.http.ssl.certificate.key-file=path/to/private.key
quarkus.http.ssl.certificate.file=path/to/certificate.crt

# Rate Limiting (com extension)
quarkus.rate-limiter.default.max-tokens=100
quarkus.rate-limiter.default.refill-period=PT1M

# Logging - Não logar dados sensíveis
quarkus.log.category."ROOT".level=INFO
quarkus.log.category."com.myproject".level=DEBUG
quarkus.log.console.format=%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p [%c] (%t) %s%e%n
```

## Testes de Segurança

### Security Test Utilities
```java
// ✅ PADRÃO - Utilitários para testes de segurança
public final class SecurityTestUtils {
    
    public static String createJwtToken(String userId, Set<Role> roles) {
        var now = Instant.now();
        
        return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusSeconds(3600)))
            .claim("roles", roles.stream().map(Role::name).toList())
            .signWith(SignatureAlgorithm.HS512, "test-secret")
            .compact();
    }
    
    public static List<String> generateSqlInjectionPayloads() {
        return List.of(
            "'; DROP TABLE users; --",
            "1' OR '1'='1",
            "UNION SELECT * FROM passwords",
            "admin'--",
            "' OR 1=1--"
        );
    }
    
    public static List<String> generateXssPayloads() {
        return List.of(
            "<script>alert('xss')</script>",
            "javascript:alert('xss')",
            "<img src=x onerror=alert('xss')>",
            "<svg onload=alert('xss')>",
            "';alert('xss');//"
        );
    }
}

// ✅ PADRÃO - Teste de SQL Injection
@QuarkusTest
class SecurityValidationTest {
    
    @ParameterizedTest
    @MethodSource("sqlInjectionPayloads")
    void should_RejectSqlInjectionAttempts(String maliciousInput) {
        var request = new CreateUserRequest(maliciousInput, "test@test.com", "bio");
        
        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/users")
        .then()
                .statusCode(400);
    }
    
    static Stream<String> sqlInjectionPayloads() {
        return SecurityTestUtils.generateSqlInjectionPayloads().stream();
    }
}
```

## Checklist de Segurança

### ✅ Para cada Release
- [ ] Dependências atualizadas (sem vulnerabilidades conhecidas)
- [ ] Secrets não hardcoded no código
- [ ] Validação de entrada em todos endpoints
- [ ] Autorização implementada em operações sensíveis
- [ ] Logs não contêm dados sensíveis
- [ ] Headers de segurança configurados
- [ ] HTTPS obrigatório em produção
- [ ] Rate limiting implementado
- [ ] Backup e disaster recovery testados
- [ ] Scan de segurança (SAST/DAST) executado
````