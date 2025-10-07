---
applyTo: "**/*.java,**/pom.xml,**/Dockerfile"
description: "Padrões específicos para GraalVM - Native Image, otimizações e configurações"
---

# GraalVM - Otimizações e Native Image

Instruções específicas para desenvolvimento com GraalVM JDK 21+, focando em compilação nativa e otimizações.

## Referências

- [GraalVM Documentation](https://www.graalvm.org/latest/docs/)
- [Native Image](https://www.graalvm.org/latest/reference-manual/native-image/)
- [Quarkus Native](https://quarkus.io/guides/building-native-image)
- [GraalVM Reachability Metadata](https://www.graalvm.org/latest/reference-manual/native-image/metadata/)

## Por Que GraalVM?

### Benefícios

✅ **Startup Ultrarrápido**: Native Image inicia em milissegundos (vs segundos com JVM)  
✅ **Baixo Uso de Memória**: ~10-20% do consumo de uma JVM tradicional  
✅ **Performance Previsível**: Sem warm-up, performance máxima desde o início  
✅ **Otimizações Avançadas**: GraalVM Compiler oferece melhor JIT que HotSpot  
✅ **Menor Superfície de Ataque**: Apenas código usado é incluído no binário  
✅ **Deploy Simplificado**: Executável nativo standalone, sem JVM

### Trade-offs

⚠️ **Build Time**: Compilação nativa é mais lenta (minutos vs segundos)  
⚠️ **Reflection**: Requer configuração explícita  
⚠️ **Dynamic Class Loading**: Limitado em Native Image  
⚠️ **Debugging**: Mais complexo que JVM tradicional

## Setup e Configuração

### Instalação GraalVM

```bash
# Windows (via chocolatey)
choco install graalvm-java21

# Linux/Mac (via SDKMAN)
sdk install java 21-graalce
sdk use java 21-graalce

# Verificar instalação
java -version
# Output deve mostrar: GraalVM CE 21+...

# Instalar Native Image
gu install native-image
```

### Maven Configuration

```xml
<!-- pom.xml -->
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <graalvm.version>21.0.0</graalvm.version>
    <quarkus.native.additional-build-args>
        -H:+ReportExceptionStackTraces,
        -H:+PrintClassInitialization,
        --initialize-at-build-time=org.slf4j,
        --verbose
    </quarkus.native.additional-build-args>
</properties>

<profiles>
    <!-- Profile para Native Image -->
    <profile>
        <id>native</id>
        <properties>
            <quarkus.package.type>native</quarkus.package.type>
            <quarkus.native.container-build>true</quarkus.native.container-build>
            <quarkus.native.builder-image>quay.io/quarkus/ubi-quarkus-graalvmce-builder-image:21</quarkus.native.builder-image>
        </properties>
    </profile>

    <!-- Profile para JVM mode com GraalVM Compiler -->
    <profile>
        <id>graalvm-jvm</id>
        <properties>
            <quarkus.package.type>uber-jar</quarkus.package.type>
        </properties>
        <build>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <configuration>
                        <compilerArgs>
                            <arg>--enable-preview</arg>
                        </compilerArgs>
                    </configuration>
                </plugin>
            </plugins>
        </build>
    </profile>
</profiles>
```

## Native Image - Padrões

### ✅ PADRÃO - Configuração de Reflection

```java
// src/main/resources/META-INF/native-image/reflect-config.json
[
  {
    "name": "com.myproject.domain.user.User",
    "allDeclaredConstructors": true,
    "allDeclaredMethods": true,
    "allDeclaredFields": true,
    "queryAllDeclaredMethods": true,
    "queryAllDeclaredConstructors": true
  },
  {
    "name": "com.myproject.infrastructure.persistence.UserEntity",
    "allDeclaredConstructors": true,
    "allDeclaredMethods": true,
    "allDeclaredFields": true
  }
]
```

### ✅ PADRÃO - Anotações para Native Image

```java
// ✅ PADRÃO - Registrar para reflection
@RegisterForReflection
public class UserEntity {
    @Id
    public UUID id;
    public String email;
    public String name;
}

// ✅ PADRÃO - Registrar múltiplas classes
@RegisterForReflection(targets = {
    User.class,
    Order.class,
    Product.class
})
public class ReflectionConfiguration {}

// ✅ PADRÃO - Registrar hierarquia completa
@RegisterForReflection(
    targets = DomainEvent.class,
    registerFullHierarchy = true
)
public class EventConfiguration {}
```

### ✅ PADRÃO - Recursos e Assets

```java
// src/main/resources/META-INF/native-image/resource-config.json
{
  "resources": {
    "includes": [
      {
        "pattern": ".*\\.properties"
      },
      {
        "pattern": ".*\\.json"
      },
      {
        "pattern": "META-INF/.*"
      },
      {
        "pattern": "db/migration/.*\\.sql"
      }
    ]
  },
  "bundles": [
    {
      "name": "messages"
    },
    {
      "name": "ValidationMessages"
    }
  ]
}
```

### ✅ PADRÃO - Inicialização em Build Time vs Runtime

```java
// ✅ PADRÃO - Classe para inicialização em build time
@AutomaticFeature
public class BuildTimeInitialization implements Feature {

    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        // Inicializar constantes em build time
        RuntimeClassInitialization.initializeAtBuildTime(
            "org.slf4j",
            "com.myproject.shared.constants"
        );

        // Inicializar em runtime (necessário para classes com side effects)
        RuntimeClassInitialization.initializeAtRunTime(
            "com.myproject.infrastructure.persistence",
            "io.quarkus.hibernate.orm"
        );
    }
}

// ✅ PADRÃO - Marcar classe para build-time initialization
public class Constants {
    // Calculado em build time, não em runtime
    public static final Map<String, String> ERROR_MESSAGES = Map.of(
        "VALIDATION_ERROR", "Validation failed",
        "NOT_FOUND", "Resource not found",
        "UNAUTHORIZED", "Unauthorized access"
    );

    public static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
}
```

## Performance Tuning

### ✅ PADRÃO - Otimizações de Compilação

```xml
<!-- pom.xml -->
<quarkus.native.additional-build-args>
    <!-- Otimizações de performance -->
    -O3,
    -march=native,
    --gc=G1,

    <!-- Análise estática agressiva -->
    --initialize-at-build-time=,
    -H:+ReportExceptionStackTraces,
    -H:+PrintClassInitialization,

    <!-- Reduzir tamanho do binário -->
    -H:+RemoveUnusedSymbols,
    -H:+StripDebugInfo,

    <!-- Melhor performance em runtime -->
    -H:+InlineBeforeAnalysis,
    -H:+OptimizeReturnedParameter,

    <!-- Habilitar PGO (Profile-Guided Optimization) -->
    -H:+EnablePGO,
    -H:PGOInstrument=profile.iprof
</quarkus.native.additional-build-args>
```

### ✅ PADRÃO - Profile-Guided Optimization

```bash
# 1. Build com instrumentação
./mvnw package -Pnative \
    -Dquarkus.native.additional-build-args="--pgo-instrument"

# 2. Executar com carga representativa
./target/myapp-runner

# 3. Gerar profile
# Encerrar aplicação (Ctrl+C) - gera default.iprof

# 4. Rebuild com profile
./mvnw package -Pnative \
    -Dquarkus.native.additional-build-args="--pgo=default.iprof"
```

## Docker com GraalVM

### ✅ PADRÃO - Dockerfile Multi-stage

```dockerfile
# Stage 1: Build com GraalVM Native Image
FROM ghcr.io/graalvm/graalvm-community:21 AS builder

# Instalar dependências nativas
RUN microdnf install -y gcc glibc-devel zlib-devel

WORKDIR /app

# Copiar arquivos do projeto
COPY pom.xml .
COPY src ./src

# Build native image
RUN ./mvnw package -Pnative -DskipTests \
    -Dquarkus.native.container-build=false

# Stage 2: Runtime mínimo
FROM quay.io/quarkus/quarkus-micro-image:2.0

WORKDIR /work

# Copiar binário nativo
COPY --from=builder /app/target/*-runner /work/application

# Configurar permissões
RUN chmod 775 /work/application

# Usuário não-root
USER 1001

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/q/health/live || exit 1

# Comando de execução
ENTRYPOINT ["./application"]

# Argumentos opcionais para tuning
CMD ["-Xmx64m", "-Xms64m"]
```

### ✅ PADRÃO - Docker Compose com GraalVM

```yaml
version: "3.8"

services:
  app-native:
    build:
      context: .
      dockerfile: Dockerfile
      target: native
    image: myapp:native
    container_name: myapp-native
    ports:
      - "8080:8080"
    environment:
      - QUARKUS_PROFILE=prod
      - DB_HOST=postgres
      - QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://postgres:5432/mydb
    depends_on:
      postgres:
        condition: service_healthy
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/q/health/live"]
      interval: 10s
      timeout: 3s
      retries: 3
      start_period: 5s
    restart: unless-stopped

  app-jvm:
    build:
      context: .
      dockerfile: Dockerfile.jvm
    image: myapp:jvm
    container_name: myapp-jvm
    ports:
      - "8081:8080"
    environment:
      - QUARKUS_PROFILE=prod
      - JAVA_OPTS=-XX:+UseG1GC -XX:MaxRAMPercentage=75.0
```

## Testing com Native Image

### ✅ PADRÃO - Native Tests

```java
// ✅ PADRÃO - Teste em modo nativo
@QuarkusIntegrationTest
@TestProfile(NativeTestProfile.class)
class UserServiceNativeIT {

    @Inject
    UserService userService;

    @Test
    void shouldCreateUser_InNativeMode() {
        // Given
        var command = new CreateUserCommand("John Doe", "john@example.com");

        // When
        var result = userService.createUser(command);

        // Then
        assertThat(result).isInstanceOf(CreateUserResult.Success.class);
    }
}

// ✅ PADRÃO - Profile para testes nativos
public class NativeTestProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
            "quarkus.datasource.devservices.enabled", "true",
            "quarkus.hibernate-orm.database.generation", "drop-and-create"
        );
    }
}
```

## Troubleshooting

### Common Issues e Soluções

```java
// ❌ PROBLEMA: ClassNotFoundException em runtime
// ✅ SOLUÇÃO: Registrar classe para reflection
@RegisterForReflection
public class ProblematicClass { }

// ❌ PROBLEMA: Recursos não encontrados
// ✅ SOLUÇÃO: Adicionar em resource-config.json
{
  "resources": {
    "includes": [{"pattern": "path/to/resource.*"}]
  }
}

// ❌ PROBLEMA: NoSuchMethodException com reflection
// ✅ SOLUÇÃO: Registrar métodos específicos
@RegisterForReflection(methods = {
    @MethodSignature(name = "processOrder", parameterTypes = {Order.class})
})
public class OrderProcessor { }

// ❌ PROBLEMA: Build time initialization error
// ✅ SOLUÇÃO: Forçar runtime initialization
RuntimeClassInitialization.initializeAtRunTime("com.problematic.Package");
```

## Monitoring e Observabilidade

### ✅ PADRÃO - Métricas para Native Image

```java
@ApplicationScoped
public class NativeImageMetrics {

    @Inject
    MeterRegistry registry;

    @PostConstruct
    void init() {
        // Métricas específicas de Native Image
        Gauge.builder("native.heap.used", this::getHeapUsed)
            .description("Native heap memory used")
            .baseUnit("bytes")
            .register(registry);

        Gauge.builder("native.image.size", this::getImageSize)
            .description("Native image size")
            .baseUnit("bytes")
            .register(registry);
    }

    private double getHeapUsed() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    private double getImageSize() {
        var imageFile = new File(ProcessHandle.current()
            .info()
            .command()
            .orElse(""));
        return imageFile.length();
    }
}
```

## Best Practices

### ✅ DO's

```java
// ✅ DO: Usar Records (otimizados para Native Image)
public record User(UUID id, Email email, UserName name) {}

// ✅ DO: Sealed interfaces (análise estática completa)
public sealed interface Result permits Success, Failure {}

// ✅ DO: Inicializar constantes em build time
public static final Map<String, String> CONSTANTS = Map.of(...);

// ✅ DO: Usar @RegisterForReflection quando necessário
@RegisterForReflection
public class DTOClass {}

// ✅ DO: Preferir injeção de dependências constructor-based
@ApplicationScoped
public class UserService {
    private final UserRepository repository;

    @Inject
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

### ❌ DON'Ts

```java
// ❌ DON'T: Reflection sem registro
Class<?> clazz = Class.forName("com.example.MyClass"); // Falhará em Native Image

// ❌ DON'T: Dynamic class loading
ClassLoader.getSystemClassLoader().loadClass("com.example.Dynamic");

// ❌ DON'T: JNI sem configuração
native void nativeMethod(); // Requer jni-config.json

// ❌ DON'T: Inicialização com side effects em build time
static {
    // Código com side effects deve ser runtime
    connectToDatabase();
}
```

## Comparação: JVM vs Native

### Métricas Típicas

| Métrica             | JVM Mode          | Native Image | Melhoria |
| ------------------- | ----------------- | ------------ | -------- |
| **Startup Time**    | ~2-3s             | ~0.02-0.05s  | **~60x** |
| **Memory (RSS)**    | ~300-500 MB       | ~30-50 MB    | **~10x** |
| **Image Size**      | ~200 MB (com JDK) | ~50-80 MB    | **~3x**  |
| **Build Time**      | ~30s              | ~3-5 min     | -10x     |
| **Peak Throughput** | 100%              | ~95-98%      | -2-5%    |

### Quando Usar Cada Modo

**Native Image** - Use quando:

- ✅ Startup rápido é crítico (serverless, CLI tools)
- ✅ Baixo uso de memória é essencial
- ✅ Deploy em containers (reduzir tamanho da imagem)
- ✅ Microsserviços com escala dinâmica

**JVM Mode** - Use quando:

- ✅ Peak throughput é mais importante que startup
- ✅ Muito uso de reflection/dynamic features
- ✅ Desenvolvimento/debugging frequente
- ✅ Aplicações de longa duração (batch jobs)

---

**GraalVM Version**: 21.0.0+  
**Última Atualização**: 2024-09-29
