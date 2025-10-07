# Documentação das Bibliotecas de Teste do Quarkus

## Introdução: Por que Testar é Fundamental?

Imagine que você está construindo uma casa. Você testaria se as fundações estão sólidas antes de construir o segundo andar, não é? No desenvolvimento de software, os testes são essas verificações de segurança que garantem que nossa aplicação funciona corretamente.

No ecossistema Quarkus, temos três bibliotecas principais que trabalham juntas para criar uma suíte de testes robusta:

1. **quarkus-junit5**: O motor de execução dos testes
2. **rest-assured**: A ferramenta para testar APIs REST
3. **quarkus-test-h2**: O banco de dados em memória para testes

## 1. Quarkus JUnit5 - O Motor dos Testes

### Conceito Básico

O JUnit5 é como o "diretor de orquestra" dos seus testes. Ele organiza, executa e reporta os resultados. O Quarkus JUnit5 é uma extensão especializada que entende como inicializar e gerenciar o contexto de uma aplicação Quarkus durante os testes.

### Por que JUnit5 e não JUnit4?

**JUnit4 (antigo):**

```java
@RunWith(SpringRunner.class) // Configuração externa
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OldTest {
    @Test
    public void test() { } // Métodos devem ser public
}

```

**JUnit5 (moderno):**

```java
@QuarkusTest // Configuração integrada
class ModernTest {
    @Test
    void test() { } // Métodos podem ser package-private
}

```

### Configuração Básica

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-junit5</artifactId>
    <scope>test</scope>
</dependency>

```

### Anatomia de um Teste Quarkus

```java
@QuarkusTest  // ← Anotação mágica que inicializa o contexto Quarkus
class MeuPrimeiroTest {

    @Inject  // ← Injeção de dependência funciona nos testes!
    MeuServico servico;

    @Test
    void deveCalcularCorretamente() {
        // Given (Dado)
        int numero1 = 5;
        int numero2 = 3;

        // When (Quando)
        int resultado = servico.somar(numero1, numero2);

        // Then (Então)
        assertEquals(8, resultado);
    }
}

```

### Ciclo de Vida dos Testes

```java
@QuarkusTest
class CicloDeVidaTest {

    @BeforeAll
    static void antesDetodos() {
        // Executado UMA vez antes de todos os testes
        System.out.println("Preparando ambiente de teste");
    }

    @BeforeEach
    void antesDeCada() {
        // Executado ANTES de cada teste
        System.out.println("Preparando teste individual");
    }

    @Test
    void teste1() {
        System.out.println("Executando teste 1");
    }

    @Test
    void teste2() {
        System.out.println("Executando teste 2");
    }

    @AfterEach
    void depoisDeCada() {
        // Executado DEPOIS de cada teste
        System.out.println("Limpando após teste");
    }

    @AfterAll
    static void depoisDeTodos() {
        // Executado UMA vez depois de todos os testes
        System.out.println("Limpando ambiente de teste");
    }
}

```

### Configurações Avançadas

### Testes com Profiles Específicos

```java
@QuarkusTest
@TestProfile(TestProfile.class)
class TesteComProfile {

    public static class TestProfile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of(
                "quarkus.datasource.db-kind", "h2",
                "quarkus.datasource.jdbc.url", "jdbc:h2:mem:testdb"
            );
        }
    }
}

```

### Testes Parametrizados

```java
@QuarkusTest
class TestesParametrizados {

    @ParameterizedTest
    @ValueSource(strings = {"João", "Maria", "Pedro"})
    void deveValidarNomes(String nome) {
        assertTrue(nome.length() > 0);
    }

    @ParameterizedTest
    @CsvSource({
        "1, 1, 2",
        "2, 3, 5",
        "5, 5, 10"
    })
    void deveCalcularSoma(int a, int b, int esperado) {
        assertEquals(esperado, a + b);
    }
}

```

## 2. **`REST Assured`** - Testando APIs como um Profissional

### Conceito Básico

REST Assured é como ter um cliente HTTP super inteligente que pode fazer requisições para sua API e verificar se as respostas estão corretas. É como um robô que testa sua aplicação web automaticamente.

### Por que REST Assured?

**Sem REST Assured (código verboso):**

```java
@Test
void testeManual() throws Exception {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8080/api/usuarios"))
        .GET()
        .build();

    HttpResponse<String> response = client.send(request,
        HttpResponse.BodyHandlers.ofString());

    assertEquals(200, response.statusCode());
    // Parsing manual do JSON...
}

```

**Com REST Assured (elegante):**

```java
@Test
void testeElegante() {
    given()
        .when()
            .get("/api/usuarios")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
}

```

### Configuração

```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <scope>test</scope>
</dependency>

```

### Estrutura Given-When-Then

Esta é a alma do REST Assured - uma estrutura que torna os testes legíveis como uma história:

```java
@QuarkusTest
class UsuarioApiTest {

    @Test
    void deveBuscarUsuarioPorId() {
        given()                          // DADO que...
            .pathParam("id", 1)          // temos um ID de usuário
        .when()                          // QUANDO...
            .get("/api/usuarios/{id}")   // fazemos uma requisição GET
        .then()                          // ENTÃO...
            .statusCode(200)             // esperamos status 200
            .body("nome", equalTo("João"))  // e nome igual a "João"
            .body("email", notNullValue()); // e email não nulo
    }
}

```

### Testando Diferentes Cenários HTTP

### GET - Buscando Dados

```java
@Test
void deveBuscarTodosUsuarios() {
    given()
    .when()
        .get("/api/usuarios")
    .then()
        .statusCode(200)
        .body("size()", greaterThan(0))
        .body("[0].nome", notNullValue())
        .body("[0].email", containsString("@"));
}

```

### POST - Criando Recursos

```java
@Test
void deveCriarNovoUsuario() {
    Usuario novoUsuario = new Usuario("Maria", "maria@email.com");

    given()
        .contentType(ContentType.JSON)
        .body(novoUsuario)
    .when()
        .post("/api/usuarios")
    .then()
        .statusCode(201)
        .body("id", notNullValue())
        .body("nome", equalTo("Maria"))
        .body("email", equalTo("maria@email.com"));
}

```

### PUT - Atualizando Recursos

```java
@Test
void deveAtualizarUsuario() {
    Usuario usuarioAtualizado = new Usuario("João Silva", "joao.silva@email.com");

    given()
        .contentType(ContentType.JSON)
        .body(usuarioAtualizado)
        .pathParam("id", 1)
    .when()
        .put("/api/usuarios/{id}")
    .then()
        .statusCode(200)
        .body("nome", equalTo("João Silva"));
}

```

### DELETE - Removendo Recursos

```java
@Test
void deveRemoverUsuario() {
    given()
        .pathParam("id", 1)
    .when()
        .delete("/api/usuarios/{id}")
    .then()
        .statusCode(204);

    // Verificar se foi realmente removido
    given()
        .pathParam("id", 1)
    .when()
        .get("/api/usuarios/{id}")
    .then()
        .statusCode(404);
}

```

### Validações Avançadas

### Validando Headers

```java
@Test
void deveValidarHeaders() {
    given()
    .when()
        .get("/api/usuarios")
    .then()
        .statusCode(200)
        .header("Content-Type", containsString("application/json"))
        .header("X-Total-Count", notNullValue());
}

```

### Validando JSON Complexo

```java
@Test
void deveValidarJsonComplexo() {
    given()
    .when()
        .get("/api/usuarios/1/pedidos")
    .then()
        .statusCode(200)
        .body("usuario.nome", equalTo("João"))
        .body("pedidos", hasSize(greaterThan(0)))
        .body("pedidos[0].valor", greaterThan(0.0f))
        .body("pedidos.findAll { it.status == 'ATIVO' }.size()", greaterThan(0));
}

```

### Extraindo Dados da Resposta

```java
@Test
void deveExtrairDadosDaResposta() {
    String email = given()
        .pathParam("id", 1)
    .when()
        .get("/api/usuarios/{id}")
    .then()
        .statusCode(200)
        .extract()
        .path("email");

    assertTrue(email.contains("@"));
}

```

### Autenticação e Autorização

```java
@Test
void deveTestarComAutenticacao() {
    String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...";

    given()
        .header("Authorization", token)
    .when()
        .get("/api/usuarios/privado")
    .then()
        .statusCode(200);
}

@Test
void deveRejeitarSemAutenticacao() {
    given()
    .when()
        .get("/api/usuarios/privado")
    .then()
        .statusCode(401);
}

```

## 3. Quarkus Test H2 - Banco de Dados para Testes

### Conceito Básico

O H2 é como um banco de dados "de brinquedo" que vive na memória da sua aplicação. É perfeito para testes porque:

- **Rápido**: Não precisa conectar com banco externo
- **Limpo**: Cada teste começa com dados frescos
- **Isolado**: Não interfere com dados de produção

### Por que H2 para Testes?

**Problema sem H2:**

```java
// Teste depende de dados específicos no banco
@Test
void deveBuscarUsuario() {
    // E se o usuário ID 1 não existir?
    // E se outro teste alterou os dados?
    Usuario usuario = repository.findById(1L);
    assertNotNull(usuario);
}

```

**Solução com H2:**

```java
// Teste controla os dados
@Test
@Transactional
void deveBuscarUsuario() {
    // Criamos os dados que precisamos
    Usuario usuario = new Usuario("João", "joao@email.com");
    repository.persist(usuario);

    // Testamos com dados conhecidos
    Usuario encontrado = repository.findById(usuario.getId());
    assertNotNull(encontrado);
}

```

### Configuração

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-test-h2</artifactId>
    <scope>test</scope>
</dependency>

```

### Configuração do Banco H2

**application-test.properties:**

```
# Configuração do H2 para testes
quarkus.datasource.db-kind=h2
quarkus.datasource.jdbc.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
quarkus.datasource.username=sa
quarkus.datasource.password=

# Hibernate vai recriar o schema a cada teste
quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.log.sql=true

```

### Testando Repositories

```java
@QuarkusTest
@Transactional
class UsuarioRepositoryTest {

    @Inject
    UsuarioRepository repository;

    @Test
    void deveSalvarUsuario() {
        // Given
        Usuario usuario = new Usuario("João", "joao@email.com");

        // When
        repository.persist(usuario);

        // Then
        assertNotNull(usuario.getId());
        assertTrue(usuario.getId() > 0);
    }

    @Test
    void deveBuscarPorEmail() {
        // Given
        Usuario usuario = new Usuario("Maria", "maria@email.com");
        repository.persist(usuario);

        // When
        Optional<Usuario> encontrado = repository.findByEmail("maria@email.com");

        // Then
        assertTrue(encontrado.isPresent());
        assertEquals("Maria", encontrado.get().getNome());
    }

    @Test
    void deveContarUsuarios() {
        // Given
        repository.persist(new Usuario("João", "joao@email.com"));
        repository.persist(new Usuario("Maria", "maria@email.com"));

        // When
        long total = repository.count();

        // Then
        assertEquals(2, total);
    }
}

```

### Testando Services com Transações

```java
@QuarkusTest
class UsuarioServiceTest {

    @Inject
    UsuarioService service;

    @Test
    @Transactional
    void deveCriarUsuarioComValidacao() {
        // Given
        CreateUsuarioRequest request = new CreateUsuarioRequest(
            "João Silva",
            "joao@email.com"
        );

        // When
        Usuario usuario = service.criarUsuario(request);

        // Then
        assertNotNull(usuario.getId());
        assertEquals("João Silva", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
    }

    @Test
    @Transactional
    void deveRejeitarEmailDuplicado() {
        // Given
        service.criarUsuario(new CreateUsuarioRequest("João", "joao@email.com"));

        // When & Then
        assertThrows(EmailJaExisteException.class, () -> {
            service.criarUsuario(new CreateUsuarioRequest("Maria", "joao@email.com"));
        });
    }
}

```

### Dados de Teste com @Sql

```java
@QuarkusTest
@Sql("/dados-teste.sql")  // Executa script SQL antes dos testes
class UsuarioServiceComDadosTest {

    @Inject
    UsuarioService service;

    @Test
    void deveEncontrarUsuariosCarregados() {
        List<Usuario> usuarios = service.listarTodos();
        assertTrue(usuarios.size() > 0);
    }
}

```

**dados-teste.sql:**

```sql
INSERT INTO usuario (id, nome, email, created_at) VALUES
(1, 'João Silva', 'joao@email.com', CURRENT_TIMESTAMP),
(2, 'Maria Santos', 'maria@email.com', CURRENT_TIMESTAMP),
(3, 'Pedro Oliveira', 'pedro@email.com', CURRENT_TIMESTAMP);

```

### Testando Queries Customizadas

```java
@QuarkusTest
@Transactional
class UsuarioQueryTest {

    @Inject
    EntityManager em;

    @Test
    void deveExecutarQueryCustomizada() {
        // Given
        Usuario usuario1 = new Usuario("João", "joao@gmail.com");
        Usuario usuario2 = new Usuario("Maria", "maria@hotmail.com");
        Usuario usuario3 = new Usuario("Pedro", "pedro@gmail.com");

        em.persist(usuario1);
        em.persist(usuario2);
        em.persist(usuario3);

        // When
        List<Usuario> usuariosGmail = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.email LIKE :domain",
            Usuario.class
        )
        .setParameter("domain", "%@gmail.com")
        .getResultList();

        // Then
        assertEquals(2, usuariosGmail.size());
    }
}

```

## 4. Integrando as Três Bibliotecas

### Teste de Integração Completo

```java
@QuarkusTest
@TestProfile(IntegrationTestProfile.class)
class UsuarioIntegrationTest {

    @Inject
    UsuarioRepository repository;

    @Test
    @Transactional
    void deveRealizarFluxoCompletoDeUsuario() {
        // 1. Criar usuário via API
        Usuario novoUsuario = new Usuario("João", "joao@email.com");

        Integer userId = given()
            .contentType(ContentType.JSON)
            .body(novoUsuario)
        .when()
            .post("/api/usuarios")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        // 2. Verificar se foi salvo no banco
        Usuario usuarioSalvo = repository.findById(userId.longValue());
        assertNotNull(usuarioSalvo);
        assertEquals("João", usuarioSalvo.getNome());

        // 3. Buscar via API
        given()
            .pathParam("id", userId)
        .when()
            .get("/api/usuarios/{id}")
        .then()
            .statusCode(200)
            .body("nome", equalTo("João"))
            .body("email", equalTo("joao@email.com"));

        // 4. Atualizar via API
        Usuario usuarioAtualizado = new Usuario("João Silva", "joao@email.com");

        given()
            .contentType(ContentType.JSON)
            .body(usuarioAtualizado)
            .pathParam("id", userId)
        .when()
            .put("/api/usuarios/{id}")
        .then()
            .statusCode(200)
            .body("nome", equalTo("João Silva"));

        // 5. Verificar atualização no banco
        Usuario usuarioVerificado = repository.findById(userId.longValue());
        assertEquals("João Silva", usuarioVerificado.getNome());

        // 6. Deletar via API
        given()
            .pathParam("id", userId)
        .when()
            .delete("/api/usuarios/{id}")
        .then()
            .statusCode(204);

        // 7. Verificar se foi removido
        given()
            .pathParam("id", userId)
        .when()
            .get("/api/usuarios/{id}")
        .then()
            .statusCode(404);
    }

    public static class IntegrationTestProfile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of(
                "quarkus.datasource.db-kind", "h2",
                "quarkus.datasource.jdbc.url", "jdbc:h2:mem:integration-test"
            );
        }
    }
}

```

### TestContainers para Testes Mais Realistas

```java
@QuarkusTest
@QuarkusTestResource(PostgreSQLTestResource.class)
class UsuarioTestContainerTest {

    @Inject
    UsuarioService service;

    @Test
    @Transactional
    void deveTestarComBancoReal() {
        // Este teste roda contra um PostgreSQL real em container
        Usuario usuario = service.criarUsuario(
            new CreateUsuarioRequest("João", "joao@email.com")
        );

        assertNotNull(usuario.getId());
    }

    public static class PostgreSQLTestResource implements QuarkusTestResourceLifecycleManager {
        private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:13")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");

        @Override
        public Map<String, String> start() {
            postgres.start();
            return Map.of(
                "quarkus.datasource.jdbc.url", postgres.getJdbcUrl(),
                "quarkus.datasource.username", postgres.getUsername(),
                "quarkus.datasource.password", postgres.getPassword()
            );
        }

        @Override
        public void stop() {
            postgres.stop();
        }
    }
}

```

## 5. Boas Práticas e Padrões

### Organize seus Testes

```
src/test/java/
├── unit/                    # Testes unitários
│   ├── service/
│   └── util/
├── integration/             # Testes de integração
│   ├── api/
│   └── repository/
└── resources/
    ├── application-test.properties
    └── dados-teste.sql

```

### Use Page Objects para Testes de API

```java
public class UsuarioApiPage {

    public ValidatableResponse criarUsuario(Usuario usuario) {
        return given()
            .contentType(ContentType.JSON)
            .body(usuario)
        .when()
            .post("/api/usuarios")
        .then();
    }

    public ValidatableResponse buscarUsuario(Long id) {
        return given()
            .pathParam("id", id)
        .when()
            .get("/api/usuarios/{id}")
        .then();
    }
}

@QuarkusTest
class UsuarioApiTest {

    private UsuarioApiPage api = new UsuarioApiPage();

    @Test
    void deveCriarUsuario() {
        Usuario usuario = new Usuario("João", "joao@email.com");

        api.criarUsuario(usuario)
            .statusCode(201)
            .body("nome", equalTo("João"));
    }
}

```

### Builders para Dados de Teste

```java
public class UsuarioTestBuilder {
    private String nome = "João";
    private String email = "joao@email.com";

    public static UsuarioTestBuilder umUsuario() {
        return new UsuarioTestBuilder();
    }

    public UsuarioTestBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public UsuarioTestBuilder comEmail(String email) {
        this.email = email;
        return this;
    }

    public Usuario build() {
        return new Usuario(nome, email);
    }
}

// Uso nos testes
@Test
void deveTestarComBuilder() {
    Usuario usuario = umUsuario()
        .comNome("Maria")
        .comEmail("maria@email.com")
        .build();

    // teste continua...
}

```

## 6. Troubleshooting e Dicas

### Problemas Comuns

### "Não consigo injetar dependências no teste"

```java
// ❌ Errado
@ExtendWith(MockitoExtension.class)  // Não use com @QuarkusTest
class MeuTest {
    @Inject
    MeuServico servico;  // Não vai funcionar
}

// ✅ Correto
@QuarkusTest
class MeuTest {
    @Inject
    MeuServico servico;  // Funciona!
}

```

### "H2 não está criando as tabelas"

```
# Adicione no application-test.properties
quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.log.sql=true  # Para debug

```

### "REST Assured não encontra o endpoint"

```java
// ✅ Certifique-se que o TestHTTPEndpoint está correto
@QuarkusTest
class ApiTest {
    @Test
    void teste() {
        given()
        .when()
            .get("/api/usuarios")  // Caminho deve começar com /
        .then()
            .statusCode(200);
    }
}

```

### Configuração de Logs para Debug

```
# application-test.properties
quarkus.log.category."io.restassured".level=DEBUG
quarkus.log.category."org.hibernate.SQL".level=DEBUG
quarkus.log.category."org.hibernate.type.descriptor.sql".level=TRACE

```

## Conclusão

Essas três bibliotecas formam um ecossistema poderoso para testes em Quarkus:

- **JUnit5**: Organiza e executa os testes
- **REST Assured**: Testa APIs de forma elegante
- **H2**: Fornece persistência rápida e isolada

Começe com testes simples e vá evoluindo. A chave é manter os testes:

- **Rápidos**: Para executar frequentemente
- **Confiáveis**: Que não falhem sem motivo
- **Legíveis**: Que sirvam como documentação
- **Isolados**: Que não dependam uns dos outros

Lembre-se: um bom teste é como uma boa história - tem começo (setup), meio (ação) e fim (verificação), e qualquer pessoa deveria conseguir entender o que está acontecendo.