# SmallRye JWT - Autenticação e Autorização com JSON Web Tokens

## Índice
1. [Introdução](#1-introdução)
2. [Conceitos Fundamentais](#2-conceitos-fundamentais)
3. [Setup e Configuração](#3-setup-e-configuração)
4. [Geração de Tokens](#4-geração-de-tokens)
5. [Validação e Verificação](#5-validação-e-verificação)
6. [Proteção de Endpoints](#6-proteção-de-endpoints)
7. [Claims Customizadas](#7-claims-customizadas)
8. [Refresh Tokens](#8-refresh-tokens)
9. [Integração com Frontend](#9-integração-com-frontend)
10. [Configurações Avançadas](#10-configurações-avançadas)
11. [Testes](#11-testes)
12. [Segurança e Boas Práticas](#12-segurança-e-boas-práticas)
13. [Troubleshooting](#13-troubleshooting)
14. [Recursos e Referências](#14-recursos-e-referências)

---

## 1. Introdução

### 1.1 O que é JWT?

**JWT (JSON Web Token)** é um padrão aberto (RFC 7519) para transmitir informações de forma segura entre partes como um objeto JSON. É amplamente utilizado para autenticação e troca de informações em aplicações web e APIs.

#### Analogia do Mundo Real

Imagine que você vai a um parque de diversões:

```
┌─────────────────────────────────────────────────────────┐
│ 1. ENTRADA (Login)                                      │
│    Você compra um ingresso (token)                      │
│    Ingresso contém: Nome, Tipo (VIP/Normal), Validade  │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│ 2. BRINQUEDOS (Recursos Protegidos)                    │
│    Mostra o ingresso para funcionário                   │
│    Funcionário verifica se é válido e se tem permissão  │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│ 3. ACESSO (Autorização)                                 │
│    VIP → Acesso total                                   │
│    Normal → Acesso limitado                             │
└─────────────────────────────────────────────────────────┘
```

O **JWT** funciona exatamente assim: é um "ingresso digital" que prova quem você é e o que pode fazer.

### 1.2 Estrutura do JWT

Um JWT é composto por três partes separadas por pontos (`.`):

```
[Header].[Payload].[Signature]
```

#### Exemplo Visual

```
eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ

├─────────────────────────────────┬────────────────────────────────────────────────────────┬──────────────────────────────────┐
│          HEADER                 │                    PAYLOAD                             │           SIGNATURE              │
│     (Algoritmo e Tipo)          │              (Dados do Usuário)                        │        (Validação)               │
└─────────────────────────────────┴────────────────────────────────────────────────────────┴──────────────────────────────────┘
```

#### 1. Header (Cabeçalho)

Contém metadados sobre o token:

```json
{
  "alg": "RS256",    // Algoritmo de assinatura (RSA SHA-256)
  "typ": "JWT"       // Tipo do token
}
```

#### 2. Payload (Carga Útil)

Contém as **claims** (declarações) sobre a entidade:

```json
{
  "sub": "1234567890",           // Subject (ID do usuário)
  "name": "João Silva",          // Nome
  "email": "joao@example.com",   // Email
  "groups": ["admin", "user"],   // Roles/Grupos
  "iat": 1516239022,             // Issued At (quando foi criado)
  "exp": 1516242622,             // Expiration (quando expira)
  "iss": "https://meuapp.com"    // Issuer (quem emitiu)
}
```

#### 3. Signature (Assinatura)

Garante que o token não foi alterado:

```
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret
)
```

### 1.3 O que é SmallRye JWT?

**SmallRye JWT** é a implementação do Quarkus da especificação **MicroProfile JWT RBAC**, que define um padrão interoperável para usar JWT em microserviços Java.

#### Características Principais

| Característica | Descrição |
|----------------|-----------|
| **Baseado em Padrões** | Segue MicroProfile JWT 2.1 |
| **Integração Nativa** | Totalmente integrado ao Quarkus Security |
| **Geração Simples** | API fluente para criar tokens |
| **Validação Automática** | Verificação de assinatura, expiração, issuer |
| **CDI Friendly** | Injeção de dependências para claims |
| **Configurável** | Suporte a múltiplos issuers, algoritmos, etc. |

### 1.4 Fluxo de Autenticação JWT

```mermaid
sequenceDiagram
    participant C as Cliente
    participant A as Auth Service
    participant R as Resource Service
    participant DB as Database
    
    C->>A: 1. POST /auth/login<br/>{username, password}
    A->>DB: 2. Verificar credenciais
    DB-->>A: 3. Usuário válido
    A->>A: 4. Gerar JWT com claims
    A-->>C: 5. {accessToken, refreshToken}
    
    Note over C: Armazena tokens
    
    C->>R: 6. GET /api/protected<br/>Authorization: Bearer [token]
    R->>R: 7. Validar assinatura
    R->>R: 8. Verificar expiração
    R->>R: 9. Verificar roles
    R-->>C: 10. Dados protegidos
    
    Note over C: Token expira
    
    C->>A: 11. POST /auth/refresh<br/>{refreshToken}
    A->>A: 12. Validar refresh token
    A-->>C: 13. {novo accessToken}
```

### 1.5 Vantagens do JWT

#### ✅ Benefícios

1. **Stateless** 🔓
   - Servidor não precisa armazenar sessões
   - Escala horizontalmente sem problemas
   - Ideal para microserviços

2. **Autocontido** 📦
   - Contém todas as informações necessárias
   - Reduz chamadas ao banco de dados
   - Claims podem ser lidas sem validação

3. **Portável** 🌐
   - Funciona em qualquer plataforma
   - Pode ser usado em HTTP headers, cookies, URL
   - Padrão aberto (RFC 7519)

4. **Seguro** 🔒
   - Assinado criptograficamente
   - Opcionalmente encriptado (JWE)
   - Detecta adulteração

#### ⚠️ Desvantagens e Cuidados

1. **Não pode ser revogado facilmente**
   - Token é válido até expirar
   - Necessário implementar blacklist para revogação
   - Use TTL curto para access tokens

2. **Tamanho**
   - Maior que session IDs tradicionais
   - Cada requisição carrega o token completo
   - Evite claims desnecessárias

3. **Segurança do Secret/Key**
   - Se a chave vazar, todos os tokens podem ser forjados
   - Use chaves fortes (mínimo 256 bits)
   - Rotacione chaves periodicamente

### 1.6 JWT vs. Sessões Tradicionais

| Aspecto | JWT | Sessões Tradicionais |
|---------|-----|----------------------|
| **Armazenamento** | Cliente (token) | Servidor (memória/Redis) |
| **Stateful/Stateless** | Stateless ✅ | Stateful ❌ |
| **Escalabilidade** | Excelente ⭐⭐⭐⭐⭐ | Requer sticky sessions ⭐⭐⭐ |
| **Tamanho** | Maior (~200-500 bytes) | Menor (session ID ~32 bytes) |
| **Revogação** | Difícil ❌ | Fácil ✅ |
| **Performance** | Sem consulta DB ✅ | Consulta a cada request ❌ |
| **Ideal para** | APIs, Microserviços, Mobile | Aplicações monolíticas |

### 1.7 Casos de Uso

#### ✅ Use JWT quando:

- Construindo APIs RESTful
- Arquitetura de microserviços
- Single Sign-On (SSO)
- Aplicações mobile
- Serviços distribuídos
- Necessita escalar horizontalmente

#### ❌ Evite JWT quando:

- Sistema com revogação frequente de acesso
- Dados sensíveis no token (preferir JWE)
- Aplicação monolítica simples
- Sessões de longa duração (horas/dias)

### 1.8 Algoritmos de Assinatura

| Algoritmo | Tipo | Descrição | Uso Recomendado |
|-----------|------|-----------|-----------------|
| **HS256** | Simétrico | HMAC + SHA-256 | Desenvolvimento, serviços internos |
| **RS256** | Assimétrico | RSA + SHA-256 | **Produção (recomendado)** |
| **ES256** | Assimétrico | ECDSA + SHA-256 | Alta performance, tokens pequenos |
| **PS256** | Assimétrico | RSA-PSS + SHA-256 | Máxima segurança |

**Recomendação**: Use **RS256** em produção com chaves de no mínimo 2048 bits.

---

## 2. Conceitos Fundamentais

### 2.1 Claims (Declarações)

**Claims** são afirmações sobre uma entidade (tipicamente o usuário) e metadados adicionais.

#### Tipos de Claims

| Tipo | Descrição | Exemplos |
|------|-----------|----------|
| **Registered** | Claims padrão (RFC 7519) | `iss`, `sub`, `exp`, `iat`, `nbf` |
| **Public** | Claims públicas registradas | `name`, `email`, `preferred_username` |
| **Private** | Claims customizadas da aplicação | `userId`, `department`, `permissions` |

#### Claims Registradas (Padrão)

```java
/**
 * Claims padrão do JWT (RFC 7519)
 */
public class StandardClaims {
    
    // iss (Issuer) - Quem emitiu o token
    String issuer = "https://meuapp.com";
    
    // sub (Subject) - Sobre quem é o token (geralmente user ID)
    String subject = "user123";
    
    // aud (Audience) - Para quem o token é destinado
    String audience = "api.meuapp.com";
    
    // exp (Expiration Time) - Quando o token expira (Unix timestamp)
    long expiration = 1735680000; // 31/12/2024 23:59:59
    
    // nbf (Not Before) - Quando o token se torna válido
    long notBefore = 1704067200; // 01/01/2024 00:00:00
    
    // iat (Issued At) - Quando o token foi emitido
    long issuedAt = 1704067200;
    
    // jti (JWT ID) - Identificador único do token
    String jwtId = "a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d";
}
```

#### MicroProfile JWT Claims

Claims específicas da especificação MicroProfile:

```java
/**
 * Claims do MicroProfile JWT
 */
public class MicroProfileClaims {
    
    // upn (User Principal Name) - Nome principal do usuário
    String upn = "joao.silva@empresa.com";
    
    // groups - Grupos/Roles do usuário
    Set<String> groups = Set.of("admin", "user", "manager");
    
    // preferred_username - Nome de usuário preferido
    String preferredUsername = "joao.silva";
}
```

### 2.2 Roles e Grupos

**Roles** (papéis) definem o que um usuário pode fazer na aplicação.

```java
/**
 * Hierarquia de Roles
 */
public enum Role {
    // Usuário comum - acesso básico
    USER("user"),
    
    // Gerente - acesso intermediário
    MANAGER("manager"),
    
    // Administrador - acesso total
    ADMIN("admin"),
    
    // Super Admin - acesso irrestrito
    SUPER_ADMIN("super_admin");
    
    private final String roleName;
    
    Role(String roleName) {
        this.roleName = roleName;
    }
}
```

#### Exemplo de Permissões por Role

```
┌─────────────────────────────────────────────────────────────┐
│ ADMIN                                                       │
│ ├─ Gerenciar usuários                                       │
│ ├─ Configurar sistema                                       │
│ ├─ Ver relatórios                                           │
│ └─ Todas as permissões de MANAGER                          │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ MANAGER                                                     │
│ ├─ Aprovar pedidos                                          │
│ ├─ Ver equipe                                               │
│ └─ Todas as permissões de USER                             │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ USER                                                        │
│ ├─ Ver perfil                                               │
│ ├─ Editar perfil                                            │
│ └─ Criar pedidos                                            │
└─────────────────────────────────────────────────────────────┘
```

### 2.3 Access Token vs Refresh Token

#### Access Token

- **Curta duração** (5-15 minutos)
- Usado em cada requisição à API
- Contém todas as claims necessárias
- Se comprometido, impacto limitado (expira rápido)

#### Refresh Token

- **Longa duração** (dias, semanas ou meses)
- Usado apenas para obter novos access tokens
- Armazenado de forma mais segura
- Pode ser revogado no servidor

```mermaid
graph LR
    A[Login] --> B[Gera Access Token<br/>15 min]
    A --> C[Gera Refresh Token<br/>7 dias]
    
    B --> D[Requisições à API]
    
    D --> E{Token<br/>expirou?}
    E -->|Não| D
    E -->|Sim| F[Usa Refresh Token]
    
    F --> G[Novo Access Token]
    G --> D
    
    C --> H{Refresh<br/>expirou?}
    H -->|Sim| I[Login novamente]
    I --> A
    
    style B fill:#c8e6c9
    style C fill:#fff9c4
    style E fill:#ffccbc
```

### 2.4 Chaves Assimétricas (Public/Private Key)

SmallRye JWT usa criptografia assimétrica (RSA) para assinar e validar tokens.

```
┌─────────────────────────────────────────────────────────────┐
│ CHAVE PRIVADA (Private Key)                                │
│ ├─ Mantida em SEGREDO no servidor                          │
│ ├─ Usada para ASSINAR tokens                               │
│ └─ Nunca compartilhada                                      │
└─────────────────────────────────────────────────────────────┘
                          ↓
                    [Gera Token]
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ JWT ASSINADO                                                │
│ eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWI...            │
└─────────────────────────────────────────────────────────────┘
                          ↓
                   [Envia ao Cliente]
                          ↓
┌─────────────────────────────────────────────────────────────┐
│ CHAVE PÚBLICA (Public Key)                                  │
│ ├─ Pode ser compartilhada                                   │
│ ├─ Usada para VALIDAR assinatura                           │
│ └─ Distribuída para serviços que validam tokens            │
└─────────────────────────────────────────────────────────────┘
```

**Vantagens**:
- ✅ Chave privada fica apenas no auth service
- ✅ Outros serviços só precisam da chave pública
- ✅ Não é possível forjar tokens sem a chave privada
- ✅ Ideal para arquitetura de microserviços

### 2.5 Token Expiration e Lifecycle

```java
/**
 * Ciclo de vida de um JWT
 */
public class TokenLifecycle {
    
    // 1. CRIAÇÃO
    Instant now = Instant.now();
    Instant expiration = now.plus(15, ChronoUnit.MINUTES);
    
    String token = Jwt.issuer("https://meuapp.com")
        .upn("joao.silva")
        .groups(Set.of("user"))
        .issuedAt(now.getEpochSecond())
        .expiresAt(expiration.getEpochSecond())
        .sign();
    
    // 2. USO (0-15 minutos)
    // Token é válido e pode ser usado
    
    // 3. PRÓXIMO DA EXPIRAÇÃO (13-15 minutos)
    // Frontend pode renovar proativamente
    
    // 4. EXPIRADO (> 15 minutos)
    // Token inválido - 401 Unauthorized
    
    // 5. RENOVAÇÃO
    // Usar refresh token para obter novo access token
}
```

#### Estratégias de Expiração

| Estratégia | Access Token | Refresh Token | Uso |
|------------|--------------|---------------|-----|
| **Ultra Seguro** | 5 min | 1 hora | Banking, Saúde |
| **Balanceado** | 15 min | 7 dias | SaaS, E-commerce |
| **Conveniente** | 1 hora | 30 dias | Redes sociais, Blogs |
| **Longo Prazo** | 1 dia | 90 dias | Dispositivos confiáveis |

### 2.6 Contexto de Segurança

```mermaid
graph TD
    A[HTTP Request com JWT] --> B[Quarkus Security]
    B --> C{Validar<br/>Token}
    
    C -->|Inválido| D[401 Unauthorized]
    C -->|Válido| E[Extrair Claims]
    
    E --> F[SecurityIdentity]
    F --> G[Principal]
    F --> H[Roles]
    F --> I[Attributes]
    
    G --> J[Verificar @RolesAllowed]
    H --> J
    
    J -->|Sem permissão| K[403 Forbidden]
    J -->|Permitido| L[Executar Endpoint]
    
    style C fill:#fff3e0
    style D fill:#ffcdd2
    style K fill:#ffcdd2
    style L fill:#c8e6c9
```

---

## 3. Setup e Configuração

### 3.1 Criação do Projeto

#### Método 1: Quarkus CLI

```powershell
# Criar projeto com SmallRye JWT
quarkus create app com.exemplo:auth-demo `
    --extension=smallrye-jwt,smallrye-jwt-build,resteasy-reactive-jackson

cd auth-demo
```

#### Método 2: Maven

```powershell
mvn io.quarkus:quarkus-maven-plugin:3.15.1:create `
    -DprojectGroupId=com.exemplo `
    -DprojectArtifactId=auth-demo `
    -Dextensions="smallrye-jwt,smallrye-jwt-build,resteasy-reactive-jackson"

cd auth-demo
```

#### Método 3: code.quarkus.io

1. Acesse https://code.quarkus.io
2. Selecione extensões:
   - ✅ SmallRye JWT
   - ✅ SmallRye JWT Build
   - ✅ RESTEasy Reactive Jackson
3. Generate

### 3.2 Dependências Maven

#### pom.xml Completo

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.exemplo</groupId>
    <artifactId>auth-demo</artifactId>
    <version>1.0.0-SNAPSHOT</version>

    <properties>
        <quarkus.version>3.15.1</quarkus.version>
        <java.version>21</java.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>io.quarkus.platform</groupId>
                <artifactId>quarkus-bom</artifactId>
                <version>${quarkus.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <!-- ═══════════════════════════════════════════════════════════ -->
        <!-- SmallRye JWT -->
        <!-- ═══════════════════════════════════════════════════════════ -->
        <!-- Validação e autenticação com JWT -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-smallrye-jwt</artifactId>
        </dependency>
        
        <!-- Geração de tokens JWT -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-smallrye-jwt-build</artifactId>
        </dependency>

        <!-- ═══════════════════════════════════════════════════════════ -->
        <!-- REST -->
        <!-- ═══════════════════════════════════════════════════════════ -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-resteasy-reactive</artifactId>
        </dependency>
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-resteasy-reactive-jackson</artifactId>
        </dependency>

        <!-- ═══════════════════════════════════════════════════════════ -->
        <!-- Banco de Dados (Opcional)-->
        <!-- ═══════════════════════════════════════════════════════════ -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-hibernate-orm-panache</artifactId>
        </dependency>
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-jdbc-postgresql</artifactId>
        </dependency>

        <!-- ═══════════════════════════════════════════════════════════ -->
        <!-- Validação -->
        <!-- ═══════════════════════════════════════════════════════════ -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-hibernate-validator</artifactId>
        </dependency>

        <!-- ═══════════════════════════════════════════════════════════ -->
        <!-- Segurança Adicional -->
        <!-- ═══════════════════════════════════════════════════════════ -->
        <!-- BCrypt para hash de senhas -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-elytron-security-common</artifactId>
        </dependency>

        <!-- ═══════════════════════════════════════════════════════════ -->
        <!-- Utilities -->
        <!-- ═══════════════════════════════════════════════════════════ -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-arc</artifactId>
        </dependency>
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-config-yaml</artifactId>
        </dependency>

        <!-- ═══════════════════════════════════════════════════════════ -->
        <!-- Observabilidade -->
        <!-- ═══════════════════════════════════════════════════════════ -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-smallrye-health</artifactId>
        </dependency>

        <!-- ═══════════════════════════════════════════════════════════ -->
        <!-- Testes -->
        <!-- ═══════════════════════════════════════════════════════════ -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-junit5</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>rest-assured</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-test-security-jwt</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>io.quarkus</groupId>
                <artifactId>quarkus-maven-plugin</artifactId>
                <version>${quarkus.version}</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>build</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

### 3.3 Geração de Chaves RSA

#### PowerShell (Windows)

```powershell
# ═══════════════════════════════════════════════════════════
# 1. Gerar chave privada RSA (2048 bits)
# ═══════════════════════════════════════════════════════════
openssl genrsa -out privatekey.pem 2048

# ═══════════════════════════════════════════════════════════
# 2. Extrair chave pública da privada
# ═══════════════════════════════════════════════════════════
openssl rsa -in privatekey.pem -pubout -out publickey.pem

# ═══════════════════════════════════════════════════════════
# 3. Criar diretório de resources
# ═══════════════════════════════════════════════════════════
New-Item -ItemType Directory -Force -Path "src\main\resources\META-INF\resources"

# ═══════════════════════════════════════════════════════════
# 4. Mover chaves para o diretório correto
# ═══════════════════════════════════════════════════════════
Move-Item privatekey.pem src\main\resources\META-INF\resources\
Move-Item publickey.pem src\main\resources\META-INF\resources\

# ═══════════════════════════════════════════════════════════
# 5. Verificar as chaves
# ═══════════════════════════════════════════════════════════
# Chave privada
openssl rsa -in src\main\resources\META-INF\resources\privatekey.pem -text -noout

# Chave pública
openssl rsa -pubin -in src\main\resources\META-INF\resources\publickey.pem -text -noout
```

#### Alternativa: Gerar Chaves Programaticamente

```java
import java.security.*;
import java.util.Base64;

/**
 * Gerador de chaves RSA
 */
public class KeyGenerator {
    
    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        
        KeyPair keyPair = keyGen.generateKeyPair();
        
        // Chave privada
        PrivateKey privateKey = keyPair.getPrivate();
        String privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n" +
            Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(privateKey.getEncoded()) +
            "\n-----END PRIVATE KEY-----";
        
        // Chave pública
        PublicKey publicKey = keyPair.getPublic();
        String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" +
            Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(publicKey.getEncoded()) +
            "\n-----END PUBLIC KEY-----";
        
        System.out.println("=== PRIVATE KEY ===");
        System.out.println(privateKeyPEM);
        System.out.println("\n=== PUBLIC KEY ===");
        System.out.println(publicKeyPEM);
    }
}
```

### 3.4 Configuração - application.properties

```properties
# ═══════════════════════════════════════════════════════════
# Aplicação
# ═══════════════════════════════════════════════════════════
quarkus.application.name=auth-demo
quarkus.application.version=1.0.0

# ═══════════════════════════════════════════════════════════
# SmallRye JWT - Validação
# ═══════════════════════════════════════════════════════════
# Chave pública para validar assinatura dos tokens
mp.jwt.verify.publickey.location=META-INF/resources/publickey.pem

# Issuer esperado (deve ser o mesmo do token)
mp.jwt.verify.issuer=https://meuapp.com

# Algoritmo de assinatura
mp.jwt.verify.algorithm=RS256

# ═══════════════════════════════════════════════════════════
# SmallRye JWT - Geração
# ═══════════════════════════════════════════════════════════
# Chave privada para assinar tokens
smallrye.jwt.sign.key.location=META-INF/resources/privatekey.pem

# Issuer para tokens gerados
smallrye.jwt.new-token.issuer=https://meuapp.com

# Algoritmo para assinar
smallrye.jwt.new-token.signature-algorithm=RS256

# Duração padrão (em segundos) - 15 minutos
smallrye.jwt.new-token.lifespan=900

# ═══════════════════════════════════════════════════════════
# Segurança HTTP
# ═══════════════════════════════════════════════════════════
# Endpoints públicos (não requerem autenticação)
quarkus.http.auth.permission.public.paths=/auth/login,/auth/register,/q/*
quarkus.http.auth.permission.public.policy=permit

# Endpoints protegidos (requerem autenticação)
quarkus.http.auth.permission.authenticated.paths=/api/*
quarkus.http.auth.permission.authenticated.policy=authenticated

# Apenas admins podem acessar /api/admin/*
quarkus.http.auth.permission.admin.paths=/api/admin/*
quarkus.http.auth.permission.admin.policy=role-admin

# ═══════════════════════════════════════════════════════════
# CORS (para desenvolvimento com frontend)
# ═══════════════════════════════════════════════════════════
quarkus.http.cors=true
quarkus.http.cors.origins=http://localhost:3000,http://localhost:4200
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with
quarkus.http.cors.methods=GET,POST,PUT,DELETE,OPTIONS,PATCH
quarkus.http.cors.exposed-headers=Authorization,X-New-Token

# ═══════════════════════════════════════════════════════════
# Datasource (PostgreSQL)
# ═══════════════════════════════════════════════════════════
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=postgres
quarkus.datasource.password=postgres
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/auth_db

# Hibernate
quarkus.hibernate-orm.database.generation=update
quarkus.hibernate-orm.log.sql=false

# ═══════════════════════════════════════════════════════════
# Logging
# ═══════════════════════════════════════════════════════════
quarkus.log.level=INFO
quarkus.log.category."io.smallrye.jwt".level=DEBUG
quarkus.log.category."com.exemplo".level=DEBUG

quarkus.log.console.format=%d{HH:mm:ss} %-5p [%c{2.}] (%t) %s%e%n

# ═══════════════════════════════════════════════════════════
# Dev Mode
# ═══════════════════════════════════════════════════════════
%dev.quarkus.log.level=DEBUG
%dev.quarkus.hibernate-orm.log.sql=true
%dev.mp.jwt.verify.publickey.location=META-INF/resources/publickey.pem
%dev.smallrye.jwt.sign.key.location=META-INF/resources/privatekey.pem

# ═══════════════════════════════════════════════════════════
# Test Profile
# ═══════════════════════════════════════════════════════════
%test.quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/auth_db_test
%test.quarkus.hibernate-orm.database.generation=drop-and-create

# ═══════════════════════════════════════════════════════════
# Production Profile
# ═══════════════════════════════════════════════════════════
%prod.quarkus.log.level=WARN
%prod.quarkus.http.cors=false
%prod.smallrye.jwt.new-token.lifespan=600
```

---

## 4. Geração de Tokens

### 4.1 Anatomia da Geração de Token

```mermaid
graph TD
    A[Credenciais Validadas] --> B[Criar Builder JWT]
    B --> C[Adicionar Claims Padrão]
    C --> C1[issuer - Emissor]
    C --> C2[subject/upn - Usuário]
    C --> C3[exp - Expiração]
    C --> C4[iat - Emissão]
    
    C --> D[Adicionar Claims Customizadas]
    D --> D1[userId, email, nome]
    D --> D2[departamento, nivel]
    D --> D3[permissions]
    
    D --> E[Adicionar Roles/Groups]
    E --> E1[admin, user, manager]
    
    E --> F[Assinar com Chave Privada]
    F --> G[Token JWT Completo]
    
    style A fill:#e3f2fd
    style G fill:#c8e6c9
    style F fill:#fff9c4
```

### 4.2 Serviço de Geração Completo

```java
package com.exemplo.auth.service;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela geração de tokens JWT
 * 
 * Funcionalidades:
 * - Geração de access tokens
 * - Geração de refresh tokens
 * - Tokens com diferentes TTLs baseados em contexto
 * - Claims customizadas por tipo de usuário
 */
@ApplicationScoped
public class TokenService {
    
    // ═══════════════════════════════════════════════════════════
    // Constantes de Configuração
    // ═══════════════════════════════════════════════════════════
    
    private static final String ISSUER = "https://meuapp.com";
    private static final String AUDIENCE = "meuapp-api";
    
    // Tempos de expiração
    private static final int ACCESS_TOKEN_MINUTES = 15;
    private static final int REFRESH_TOKEN_DAYS = 7;
    private static final int REMEMBER_ME_DAYS = 30;
    
    @Inject
    UsuarioService usuarioService;
    
    // ═══════════════════════════════════════════════════════════
    // 1. Geração Básica de Token
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Gera um token JWT básico com claims mínimas
     * 
     * @param username Nome do usuário
     * @param roles Roles do usuário
     * @return Token JWT assinado
     */
    public String gerarTokenBasico(String username, Set<String> roles) {
        return Jwt.issuer(ISSUER)
            .upn(username)                    // User Principal Name
            .groups(roles)                    // Roles/Grupos
            .expiresAt(expiracaoAccessToken())
            .sign();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 2. Geração Completa com Claims Customizadas
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Gera token completo com todas as claims do usuário
     * 
     * @param usuario DTO com dados do usuário
     * @return Token JWT assinado com claims completas
     */
    public String gerarAccessToken(UsuarioDTO usuario) {
        Instant now = Instant.now();
        Instant expiration = now.plus(ACCESS_TOKEN_MINUTES, ChronoUnit.MINUTES);
        
        return Jwt.issuer(ISSUER)
            .audience(AUDIENCE)
            .subject(usuario.id.toString())
            .upn(usuario.username)
            .groups(usuario.roles)
            
            // Claims padrão
            .issuedAt(now.getEpochSecond())
            .expiresAt(expiration.getEpochSecond())
            .jti(UUID.randomUUID().toString())
            
            // Claims customizadas - Informações do usuário
            .claim("userId", usuario.id)
            .claim("email", usuario.email)
            .claim("nome", usuario.nome)
            .claim("avatar", usuario.avatarUrl)
            
            // Claims customizadas - Organização
            .claim("departamento", usuario.departamento)
            .claim("nivel", usuario.nivel)
            .claim("empresa", usuario.empresa)
            
            // Claims customizadas - Permissões
            .claim("permissions", usuario.permissions)
            .claim("tipo", "access_token")
            
            .sign();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 3. Refresh Token
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Gera refresh token (longa duração)
     * 
     * Características:
     * - Duração mais longa (7 dias)
     * - Claims mínimas (segurança)
     * - Usado apenas para renovar access token
     * 
     * @param username Nome do usuário
     * @param rememberMe Se deve criar token de longa duração
     * @return Refresh token
     */
    public String gerarRefreshToken(String username, boolean rememberMe) {
        Instant now = Instant.now();
        Instant expiration = rememberMe 
            ? now.plus(REMEMBER_ME_DAYS, ChronoUnit.DAYS)
            : now.plus(REFRESH_TOKEN_DAYS, ChronoUnit.DAYS);
        
        return Jwt.issuer(ISSUER)
            .audience(AUDIENCE)
            .upn(username)
            .issuedAt(now.getEpochSecond())
            .expiresAt(expiration.getEpochSecond())
            .jti(UUID.randomUUID().toString())
            .claim("tipo", "refresh_token")
            .claim("rememberMe", rememberMe)
            .sign();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 4. Token com Claims Condicionais
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Gera token com claims baseadas em condições
     * 
     * Exemplo: Admins recebem claims extras
     */
    public String gerarTokenComClaimsCondicionais(UsuarioDTO usuario) {
        JwtClaimsBuilder builder = Jwt.issuer(ISSUER)
            .audience(AUDIENCE)
            .upn(usuario.username)
            .groups(usuario.roles)
            .expiresAt(expiracaoAccessToken())
            .claim("userId", usuario.id)
            .claim("email", usuario.email)
            .claim("nome", usuario.nome);
        
        // Claims extras para administradores
        if (usuario.roles.contains("admin")) {
            builder.claim("isAdmin", true)
                   .claim("adminLevel", usuario.adminLevel)
                   .claim("canImpersonate", true)
                   .claim("fullAccess", true);
        }
        
        // Claims extras para gerentes
        if (usuario.roles.contains("manager")) {
            builder.claim("team", usuario.teamMembers)
                   .claim("budgetLimit", usuario.budgetLimit)
                   .claim("approvalLevel", usuario.approvalLevel);
        }
        
        // Claims para usuários premium
        if (usuario.isPremium) {
            builder.claim("premium", true)
                   .claim("features", usuario.premiumFeatures)
                   .claim("quotaLimit", usuario.quotaLimit);
        }
        
        return builder.sign();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 5. Token para Diferentes Dispositivos
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Gera token específico para tipo de dispositivo
     * 
     * Web: TTL curto (15 min)
     * Mobile: TTL médio (1 hora)
     * IoT: TTL longo (24 horas)
     */
    public String gerarTokenPorDispositivo(
            UsuarioDTO usuario, 
            DeviceType deviceType) {
        
        Instant now = Instant.now();
        Instant expiration = switch (deviceType) {
            case WEB -> now.plus(15, ChronoUnit.MINUTES);
            case MOBILE -> now.plus(1, ChronoUnit.HOURS);
            case IOT -> now.plus(24, ChronoUnit.HOURS);
            case TABLET -> now.plus(30, ChronoUnit.MINUTES);
        };
        
        return Jwt.issuer(ISSUER)
            .upn(usuario.username)
            .groups(usuario.roles)
            .expiresAt(expiration.getEpochSecond())
            .claim("deviceType", deviceType.name())
            .claim("deviceId", usuario.deviceId)
            .claim("platform", usuario.platform)
            .sign();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 6. Token para API Externa (Third-Party)
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Gera token para acesso de APIs externas
     * 
     * Características:
     * - Audience específica
     * - Scopes limitados
     * - Claims mínimas
     */
    public String gerarTokenApiExterna(
            String clientId, 
            Set<String> scopes) {
        
        return Jwt.issuer(ISSUER)
            .audience("external-api")
            .subject(clientId)
            .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS).getEpochSecond())
            .claim("client_id", clientId)
            .claim("scopes", scopes)
            .claim("tipo", "client_credentials")
            .claim("api_version", "v1")
            .sign();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 7. Token Temporário (One-Time Use)
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Gera token de uso único para ações específicas
     * 
     * Exemplos:
     * - Reset de senha
     * - Confirmação de email
     * - Convite de usuário
     */
    public String gerarTokenTemporario(
            String email, 
            String action, 
            int duracaoMinutos) {
        
        return Jwt.issuer(ISSUER)
            .subject(email)
            .expiresAt(Instant.now()
                .plus(duracaoMinutos, ChronoUnit.MINUTES)
                .getEpochSecond())
            .jti(UUID.randomUUID().toString())
            .claim("action", action)
            .claim("email", email)
            .claim("tipo", "one_time_token")
            .claim("singleUse", true)
            .sign();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 8. Par de Tokens (Access + Refresh)
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Gera par completo de tokens (access + refresh)
     * 
     * @param usuario Dados do usuário
     * @param rememberMe Manter conectado
     * @return Par de tokens
     */
    public TokenPair gerarParDeTokens(UsuarioDTO usuario, boolean rememberMe) {
        String accessToken = gerarAccessToken(usuario);
        String refreshToken = gerarRefreshToken(usuario.username, rememberMe);
        
        return new TokenPair(
            accessToken,
            refreshToken,
            ACCESS_TOKEN_MINUTES * 60,  // em segundos
            "Bearer"
        );
    }
    
    // ═══════════════════════════════════════════════════════════
    // Métodos Auxiliares
    // ═══════════════════════════════════════════════════════════
    
    private long expiracaoAccessToken() {
        return Instant.now()
            .plus(ACCESS_TOKEN_MINUTES, ChronoUnit.MINUTES)
            .getEpochSecond();
    }
    
    private long expiracaoRefreshToken(boolean rememberMe) {
        int dias = rememberMe ? REMEMBER_ME_DAYS : REFRESH_TOKEN_DAYS;
        return Instant.now()
            .plus(dias, ChronoUnit.DAYS)
            .getEpochSecond();
    }
}

/**
 * Tipo de dispositivo
 */
enum DeviceType {
    WEB,
    MOBILE,
    TABLET,
    IOT
}

/**
 * Par de tokens (Access + Refresh)
 */
record TokenPair(
    String accessToken,
    String refreshToken,
    int expiresIn,
    String tokenType
) {}

    String refreshToken,
    int expiresIn,
    String tokenType
) {}
```

### 4.3 Estratégias de TTL (Time To Live)

#### Tabela Comparativa

| Cenário | Access Token | Refresh Token | Justificativa |
|---------|--------------|---------------|---------------|
| **Banking/Financeiro** | 5-10 min | 1 hora | Máxima segurança |
| **Healthcare** | 10 min | 2 horas | Dados sensíveis |
| **E-commerce** | 15-30 min | 7 dias | Balanceado |
| **SaaS B2B** | 30-60 min | 30 dias | Produtividade |
| **Redes Sociais** | 1 hora | 90 dias | Conveniência |
| **Mobile App** | 1-2 horas | 30 dias | Experiência do usuário |
| **IoT Devices** | 24 horas | 365 dias | Dispositivos confiáveis |

#### Implementação de Estratégias

```java
/**
 * Estratégias de expiração de tokens
 */
@ApplicationScoped
public class TokenExpirationStrategy {
    
    public record ExpirationConfig(
        int accessTokenMinutes,
        int refreshTokenDays,
        String justificativa
    ) {}
    
    public ExpirationConfig getStrategyByContext(SecurityContext context) {
        return switch (context) {
            case BANKING -> new ExpirationConfig(
                5, 
                0,  // Refresh token de 1 hora
                "Máxima segurança para transações financeiras"
            );
            
            case HEALTHCARE -> new ExpirationConfig(
                10, 
                0,  // 2 horas
                "Proteção de dados médicos sensíveis"
            );
            
            case ECOMMERCE -> new ExpirationConfig(
                15, 
                7, 
                "Balanceamento entre segurança e UX"
            );
            
            case SAAS -> new ExpirationConfig(
                60, 
                30, 
                "Produtividade para usuários corporativos"
            );
            
            case SOCIAL_MEDIA -> new ExpirationConfig(
                60, 
                90, 
                "Conveniência para usuários finais"
            );
            
            case IOT -> new ExpirationConfig(
                1440,  // 24 horas
                365, 
                "Dispositivos confiáveis com conectividade limitada"
            );
        };
    }
}

enum SecurityContext {
    BANKING, HEALTHCARE, ECOMMERCE, SAAS, SOCIAL_MEDIA, IOT
}
```

---

## 5. Validação e Verificação

### 5.1 Fluxo de Validação

```mermaid
sequenceDiagram
    participant C as Cliente
    participant F as Quarkus Filter
    participant V as JWT Validator
    participant K as Key Manager
    participant E as Endpoint
    
    C->>F: Request + Authorization Header
    F->>F: Extrair Token do Header
    
    F->>V: Validar Token
    V->>V: 1. Verificar formato (3 partes)
    V->>V: 2. Decode Base64
    
    V->>K: 3. Obter chave pública
    K-->>V: Public Key
    
    V->>V: 4. Verificar assinatura
    
    alt Assinatura Inválida
        V-->>F: 401 Unauthorized
        F-->>C: Token inválido
    end
    
    V->>V: 5. Verificar issuer
    V->>V: 6. Verificar audience
    V->>V: 7. Verificar expiração
    V->>V: 8. Verificar not-before
    
    alt Validações OK
        V-->>F: Token Válido
        F->>F: Criar SecurityIdentity
        F->>E: Processar Request
        E-->>C: Response
    else Validação Falhou
        V-->>F: 401 Unauthorized
        F-->>C: Token expirado/inválido
    end
```

### 5.2 Validador Customizado

```java
package com.exemplo.auth.validator;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.security.Key;
import java.security.PublicKey;
import java.time.Instant;
import java.util.*;

/**
 * Validador customizado de JWT
 * 
 * Realiza validações extras além das padrão do SmallRye JWT:
 * - Blacklist de tokens revogados
 * - Validação de claims obrigatórias
 * - Validação de formato de claims
 * - Rate limiting por token
 */
@ApplicationScoped
public class CustomJwtValidator {
    
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String expectedIssuer;
    
    @ConfigProperty(name = "mp.jwt.verify.publickey.location")
    String publicKeyLocation;
    
    // Blacklist de tokens revogados (em produção usar Redis)
    private final Set<String> revokedTokens = new HashSet<>();
    
    // ═══════════════════════════════════════════════════════════
    // Validação Completa
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Valida token JWT completamente
     * 
     * @param token Token JWT
     * @return Claims se válido
     * @throws JwtValidationException se inválido
     */
    public Claims validarToken(String token) throws JwtValidationException {
        // 1. Validar formato
        validarFormato(token);
        
        // 2. Verificar blacklist
        if (estaRevogado(token)) {
            throw new JwtValidationException("Token foi revogado");
        }
        
        // 3. Parse e validação de assinatura
        Claims claims = parsearToken(token);
        
        // 4. Validações customizadas
        validarClaimsObrigatorias(claims);
        validarFormatoClaims(claims);
        validarRegrasNegocio(claims);
        
        return claims;
    }
    
    // ═══════════════════════════════════════════════════════════
    // 1. Validação de Formato
    // ═══════════════════════════════════════════════════════════
    
    private void validarFormato(String token) throws JwtValidationException {
        if (token == null || token.isBlank()) {
            throw new JwtValidationException("Token vazio");
        }
        
        // JWT deve ter 3 partes: header.payload.signature
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new JwtValidationException(
                "Formato inválido. Esperado: header.payload.signature"
            );
        }
        
        // Validar que cada parte é Base64 válido
        for (String part : parts) {
            if (!isBase64Valido(part)) {
                throw new JwtValidationException("Token contém Base64 inválido");
            }
        }
    }
    
    private boolean isBase64Valido(String str) {
        try {
            Base64.getUrlDecoder().decode(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    // ═══════════════════════════════════════════════════════════
    // 2. Verificação de Revogação (Blacklist)
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Verifica se token está na blacklist
     */
    public boolean estaRevogado(String token) {
        String jti = extrairJti(token);
        return jti != null && revokedTokens.contains(jti);
    }
    
    /**
     * Adiciona token à blacklist
     */
    public void revogarToken(String token) {
        String jti = extrairJti(token);
        if (jti != null) {
            revokedTokens.add(jti);
            // Em produção: salvar no Redis com TTL = tempo até expiração
        }
    }
    
    private String extrairJti(String token) {
        try {
            Claims claims = parsearTokenSemValidacao(token);
            return claims.getId();
        } catch (Exception e) {
            return null;
        }
    }
    
    // ═══════════════════════════════════════════════════════════
    // 3. Parse do Token
    // ═══════════════════════════════════════════════════════════
    
    private Claims parsearToken(String token) throws JwtValidationException {
        try {
            PublicKey publicKey = carregarChavePublica();
            
            return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .requireIssuer(expectedIssuer)
                .build()
                .parseClaimsJws(token)
                .getBody();
                
        } catch (ExpiredJwtException e) {
            throw new JwtValidationException(
                "Token expirado em: " + e.getClaims().getExpiration()
            );
        } catch (UnsupportedJwtException e) {
            throw new JwtValidationException("Formato de token não suportado");
        } catch (MalformedJwtException e) {
            throw new JwtValidationException("Token malformado");
        } catch (SignatureException e) {
            throw new JwtValidationException("Assinatura inválida");
        } catch (Exception e) {
            throw new JwtValidationException("Erro ao validar token: " + e.getMessage());
        }
    }
    
    private Claims parsearTokenSemValidacao(String token) {
        int i = token.lastIndexOf('.');
        String tokenSemAssinatura = token.substring(0, i + 1);
        
        return Jwts.parserBuilder()
            .build()
            .parseClaimsJwt(tokenSemAssinatura)
            .getBody();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 4. Validações de Claims Obrigatórias
    // ═══════════════════════════════════════════════════════════
    
    private void validarClaimsObrigatorias(Claims claims) throws JwtValidationException {
        // Claims padrão obrigatórias
        if (claims.getSubject() == null) {
            throw new JwtValidationException("Claim 'sub' obrigatória");
        }
        
        if (claims.getExpiration() == null) {
            throw new JwtValidationException("Claim 'exp' obrigatória");
        }
        
        if (claims.getIssuedAt() == null) {
            throw new JwtValidationException("Claim 'iat' obrigatória");
        }
        
        // Claims customizadas obrigatórias
        if (!claims.containsKey("upn")) {
            throw new JwtValidationException("Claim 'upn' obrigatória");
        }
        
        if (!claims.containsKey("groups")) {
            throw new JwtValidationException("Claim 'groups' obrigatória");
        }
    }
    
    // ═══════════════════════════════════════════════════════════
    // 5. Validação de Formato de Claims
    // ═══════════════════════════════════════════════════════════
    
    private void validarFormatoClaims(Claims claims) throws JwtValidationException {
        // Validar email (se presente)
        if (claims.containsKey("email")) {
            String email = claims.get("email", String.class);
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new JwtValidationException("Formato de email inválido");
            }
        }
        
        // Validar userId (deve ser número positivo)
        if (claims.containsKey("userId")) {
            Object userIdObj = claims.get("userId");
            Long userId = userIdObj instanceof Integer 
                ? ((Integer) userIdObj).longValue() 
                : (Long) userIdObj;
            
            if (userId <= 0) {
                throw new JwtValidationException("userId deve ser positivo");
            }
        }
        
        // Validar groups (deve ser array não vazio)
        if (claims.containsKey("groups")) {
            Object groups = claims.get("groups");
            if (groups instanceof List) {
                if (((List<?>) groups).isEmpty()) {
                    throw new JwtValidationException("Usuário deve ter pelo menos 1 role");
                }
            }
        }
    }
    
    // ═══════════════════════════════════════════════════════════
    // 6. Validações de Regras de Negócio
    // ═══════════════════════════════════════════════════════════
    
    private void validarRegrasNegocio(Claims claims) throws JwtValidationException {
        // Regra: Token próximo da expiração (< 1 minuto) deve ser renovado
        Date expiration = claims.getExpiration();
        long secondsUntilExpiration = 
            (expiration.getTime() - System.currentTimeMillis()) / 1000;
        
        if (secondsUntilExpiration < 60) {
            // Não falha, mas poderia logar warning
            System.out.println("AVISO: Token expira em " + secondsUntilExpiration + "s");
        }
        
        // Regra: Validar departamento permitido
        if (claims.containsKey("departamento")) {
            String dept = claims.get("departamento", String.class);
            List<String> departamentosValidos = 
                List.of("TI", "RH", "VENDAS", "FINANCEIRO", "OPERACOES");
            
            if (!departamentosValidos.contains(dept)) {
                throw new JwtValidationException("Departamento inválido: " + dept);
            }
        }
        
        // Regra: Tokens "one_time" só podem ser usados uma vez
        if (claims.containsKey("singleUse")) {
            boolean singleUse = claims.get("singleUse", Boolean.class);
            if (singleUse) {
                // Verificar se já foi usado (implementar em Redis)
                String jti = claims.getId();
                if (tokenJaFoiUsado(jti)) {
                    throw new JwtValidationException("Token de uso único já foi utilizado");
                }
                marcarTokenComoUsado(jti);
            }
        }
    }
    
    // ═══════════════════════════════════════════════════════════
    // Métodos Auxiliares
    // ═══════════════════════════════════════════════════════════
    
    private PublicKey carregarChavePublica() throws Exception {
        // Implementar carregamento da chave pública
        // Por simplicidade, retornando null (substituir em produção)
        return null;
    }
    
    private boolean tokenJaFoiUsado(String jti) {
        // Implementar verificação no Redis
        return false;
    }
    
    private void marcarTokenComoUsado(String jti) {
        // Implementar marcação no Redis
    }
}

/**
 * Exceção customizada de validação JWT
 */
class JwtValidationException extends Exception {
    public JwtValidationException(String message) {
        super(message);
    }
}
```

### 5.3 Verificação em Tempo Real

```java
/**
 * Serviço para verificação de tokens em tempo real
 */
@ApplicationScoped
public class TokenVerificationService {
    
    @Inject
    JsonWebToken jwt;
    
    @Inject
    CustomJwtValidator validator;
    
    /**
     * Informações sobre o token atual
     */
    public record TokenInfo(
        String username,
        Set<String> roles,
        long secondsUntilExpiration,
        boolean isExpiringSoon,
        boolean needsRefresh,
        Map<String, Object> customClaims
    ) {}
    
    /**
     * Obtém informações detalhadas do token atual
     */
    public TokenInfo getTokenInfo() {
        long exp = jwt.getExpirationTime();
        long now = Instant.now().getEpochSecond();
        long secondsRemaining = exp - now;
        
        // Claims customizadas
        Map<String, Object> customClaims = new HashMap<>();
        jwt.getClaimNames().forEach(claim -> {
            if (!isStandardClaim(claim)) {
                customClaims.put(claim, jwt.getClaim(claim));
            }
        });
        
        return new TokenInfo(
            jwt.getName(),
            jwt.getGroups(),
            secondsRemaining,
            secondsRemaining < 300,  // < 5 minutos
            secondsRemaining < 180,  // < 3 minutos
            customClaims
        );
    }
    
    /**
     * Verifica se token precisa ser renovado
     */
    public boolean precisaRenovar() {
        long exp = jwt.getExpirationTime();
        long now = Instant.now().getEpochSecond();
        long secondsRemaining = exp - now;
        
        // Renovar se faltar menos de 3 minutos
        return secondsRemaining < 180;
    }
    
    private boolean isStandardClaim(String claim) {
        return Set.of("iss", "sub", "aud", "exp", "nbf", "iat", "jti", "upn", "groups")
            .contains(claim);
    }
}
```

---

## 6. Proteção de Endpoints

### 6.1 Hierarquia de Anotações de Segurança

```
┌─────────────────────────────────────────────────────────────┐
│ @DenyAll                                                    │
│ └─ Ninguém pode acessar (nem mesmo admin)                  │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│ @PermitAll                                                  │
│ └─ Todos podem acessar (sem autenticação)                  │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│ @RolesAllowed("user")                                       │
│ └─ Apenas usuários autenticados com role "user"            │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│ @RolesAllowed({"admin", "manager"})                         │
│ └─ Usuários com role "admin" OU "manager"                  │
└─────────────────────────────────────────────────────────────┘
```

### 6.2 Endpoint de Autenticação Completo

```java
package com.exemplo.auth.resource;

import com.exemplo.auth.dto.*;
import com.exemplo.auth.service.*;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

/**
 * Endpoint de autenticação
 * 
 * Responsabilidades:
 * - Login de usuários
 * - Registro de novos usuários  
 * - Renovação de tokens (refresh)
 * - Logout
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    
    @Inject
    TokenService tokenService;
    
    @Inject
    UsuarioService usuarioService;
    
    @Inject
    CustomJwtValidator jwtValidator;
    
    // ═══════════════════════════════════════════════════════════
    // Login
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Realiza login e retorna tokens
     * 
     * @param request Credenciais (username, password, rememberMe)
     * @return Access token + Refresh token
     */
    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Valid LoginRequest request) {
        try {
            // 1. Autenticar usuário
            UsuarioDTO usuario = usuarioService.autenticar(
                request.username, 
                request.password
            );
            
            if (usuario == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse(
                        "INVALID_CREDENTIALS",
                        "Usuário ou senha inválidos"
                    ))
                    .build();
            }
            
            // 2. Verificar se usuário está ativo
            if (!usuario.ativo) {
                return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorResponse(
                        "ACCOUNT_DISABLED",
                        "Conta desativada. Contate o administrador."
                    ))
                    .build();
            }
            
            // 3. Gerar tokens
            TokenPair tokens = tokenService.gerarParDeTokens(
                usuario, 
                request.rememberMe
            );
            
            // 4. Atualizar último login
            usuarioService.atualizarUltimoLogin(usuario.id);
            
            // 5. Resposta com tokens e dados do usuário
            AuthResponse response = new AuthResponse(
                tokens.accessToken(),
                tokens.refreshToken(),
                tokens.tokenType(),
                tokens.expiresIn(),
                usuario
            );
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(
                    "INTERNAL_ERROR",
                    "Erro ao processar login"
                ))
                .build();
        }
    }
    
    // ═══════════════════════════════════════════════════════════
    // Registro
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Registra novo usuário
     */
    @POST
    @Path("/register")
    @PermitAll
    public Response register(@Valid RegisterRequest request) {
        try {
            // 1. Verificar se usuário já existe
            if (usuarioService.existePorUsername(request.username)) {
                return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse(
                        "USERNAME_EXISTS",
                        "Nome de usuário já existe"
                    ))
                    .build();
            }
            
            if (usuarioService.existePorEmail(request.email)) {
                return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse(
                        "EMAIL_EXISTS",
                        "Email já está cadastrado"
                    ))
                    .build();
            }
            
            // 2. Criar usuário
            UsuarioDTO novoUsuario = usuarioService.criar(request);
            
            // 3. Gerar tokens
            TokenPair tokens = tokenService.gerarParDeTokens(novoUsuario, false);
            
            // 4. Resposta
            AuthResponse response = new AuthResponse(
                tokens.accessToken(),
                tokens.refreshToken(),
                tokens.tokenType(),
                tokens.expiresIn(),
                novoUsuario
            );
            
            return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
                
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(
                    "REGISTRATION_ERROR",
                    "Erro ao criar conta"
                ))
                .build();
        }
    }
    
    // ═══════════════════════════════════════════════════════════
    // Refresh Token
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Renova access token usando refresh token
     */
    @POST
    @Path("/refresh")
    @PermitAll
    public Response refresh(@Valid RefreshRequest request) {
        try {
            // 1. Validar refresh token
            Claims claims = jwtValidator.validarToken(request.refreshToken);
            
            // 2. Verificar se é refresh token
            String tipo = claims.get("tipo", String.class);
            if (!"refresh_token".equals(tipo)) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(
                        "INVALID_TOKEN_TYPE",
                        "Token fornecido não é um refresh token"
                    ))
                    .build();
            }
            
            // 3. Buscar usuário
            String username = claims.get("upn", String.class);
            UsuarioDTO usuario = usuarioService.buscarPorUsername(username);
            
            if (usuario == null || !usuario.ativo) {
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse(
                        "USER_NOT_FOUND",
                        "Usuário não encontrado ou inativo"
                    ))
                    .build();
            }
            
            // 4. Gerar novo access token
            String novoAccessToken = tokenService.gerarAccessToken(usuario);
            
            // 5. Resposta com novo token
            RefreshResponse response = new RefreshResponse(
                novoAccessToken,
                "Bearer",
                900  // 15 minutos
            );
            
            return Response.ok(response).build();
            
        } catch (JwtValidationException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ErrorResponse(
                    "INVALID_REFRESH_TOKEN",
                    e.getMessage()
                ))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(
                    "REFRESH_ERROR",
                    "Erro ao renovar token"
                ))
                .build();
        }
    }
    
    // ═══════════════════════════════════════════════════════════
    // Logout
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Realiza logout (revoga tokens)
     */
    @POST
    @Path("/logout")
    @PermitAll
    public Response logout(@HeaderParam("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                // Adicionar token à blacklist
                jwtValidator.revogarToken(token);
            }
            
            return Response.ok(new MessageResponse("Logout realizado com sucesso")).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(
                    "LOGOUT_ERROR",
                    "Erro ao processar logout"
                ))
                .build();
        }
    }
}

/**
 * DTOs para Autenticação
 */

// Request de Login
record LoginRequest(
    @NotBlank String username,
    @NotBlank String password,
    boolean rememberMe
) {}

// Request de Registro
record RegisterRequest(
    @NotBlank @Size(min = 3, max = 50) String username,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8) String password,
    @NotBlank String nome
) {}

// Request de Refresh
record RefreshRequest(
    @NotBlank String refreshToken
) {}

// Response de Autenticação
record AuthResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    int expiresIn,
    UsuarioDTO usuario
) {}

// Response de Refresh
record RefreshResponse(
    String accessToken,
    String tokenType,
    int expiresIn
) {}

// Mensagem genérica
record MessageResponse(String message) {}

// Response de Erro
record ErrorResponse(
    String code,
    String message,
    String timestamp
) {
    public ErrorResponse(String code, String message) {
        this(code, message, java.time.LocalDateTime.now().toString());
    }
}
```

### 6.3 Proteção de Endpoints - Exemplos Práticos

```java
package com.exemplo.resource;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.*;

/**
 * Exemplos completos de proteção de endpoints
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class ProtectedResource {
    
    @Inject
    JsonWebToken jwt;
    
    @Inject
    SecurityIdentity identity;
    
    // ═══════════════════════════════════════════════════════════
    // 1. Endpoint Público (sem autenticação)
    // ═══════════════════════════════════════════════════════════
    
    @GET
    @Path("/public/info")
    @PermitAll
    public Response informacoesPublicas() {
        return Response.ok(Map.of(
            "app", "Meu App",
            "version", "1.0.0",
            "status", "online"
        )).build();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 2. Qualquer Usuário Autenticado
    // ═══════════════════════════════════════════════════════════
    
    @GET
    @Path("/perfil")
    @RolesAllowed("user")
    public Response meuPerfil() {
        return Response.ok(Map.of(
            "username", jwt.getName(),
            "email", jwt.getClaim("email"),
            "roles", jwt.getGroups()
        )).build();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 3. Apenas Administradores
    // ═══════════════════════════════════════════════════════════
    
    @GET
    @Path("/admin/users")
    @RolesAllowed("admin")
    public Response listarTodosUsuarios() {
        // Apenas admins podem listar todos os usuários
        return Response.ok(List.of(
            Map.of("id", 1, "nome", "João"),
            Map.of("id", 2, "nome", "Maria")
        )).build();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 4. Múltiplos Roles (OR)
    // ═══════════════════════════════════════════════════════════
    
    @GET
    @Path("/reports")
    @RolesAllowed({"admin", "manager", "analyst"})
    public Response relatorios() {
        // Admin OU Manager OU Analyst podem acessar
        String userRole = jwt.getGroups().stream()
            .filter(r -> List.of("admin", "manager", "analyst").contains(r))
            .findFirst()
            .orElse("unknown");
        
        return Response.ok(Map.of(
            "message", "Relatórios disponíveis",
            "accessLevel", userRole
        )).build();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 5. Verificação Programática de Roles
    // ═══════════════════════════════════════════════════════════
    
    @GET
    @Path("/dashboard")
    @RolesAllowed("user")
    public Response dashboard() {
        Map<String, Object> response = new HashMap<>();
        
        if (identity.hasRole("admin")) {
            response.put("widgets", List.of("users", "stats", "logs", "config"));
            response.put("level", "full");
        } else if (identity.hasRole("manager")) {
            response.put("widgets", List.of("team", "tasks", "reports"));
            response.put("level", "management");
        } else {
            response.put("widgets", List.of("tasks", "profile"));
            response.put("level", "basic");
        }
        
        return Response.ok(response).build();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 6. Controle Baseado em Claims
    // ═══════════════════════════════════════════════════════════
    
    @GET
    @Path("/sensitive-data")
    @RolesAllowed("user")
    public Response dadosSensiveis() {
        // Verificar claim adicional
        String departamento = jwt.getClaim("departamento");
        
        if (!"FINANCEIRO".equals(departamento) && !identity.hasRole("admin")) {
            return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of("error", "Acesso restrito ao departamento financeiro"))
                .build();
        }
        
        return Response.ok(Map.of("data", "Informações financeiras")).build();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 7. Controle de Propriedade de Recurso
    // ═══════════════════════════════════════════════════════════
    
    @GET
    @Path("/orders/{id}")
    @RolesAllowed("user")
    public Response obterPedido(@PathParam("id") Long pedidoId) {
        Long userId = jwt.getClaim("userId");
        
        // Buscar pedido (simulado)
        PedidoDTO pedido = buscarPedido(pedidoId);
        
        // Usuário só pode ver seus próprios pedidos (exceto admin)
        if (!identity.hasRole("admin") && !pedido.usuarioId.equals(userId)) {
            return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of("error", "Você só pode acessar seus próprios pedidos"))
                .build();
        }
        
        return Response.ok(pedido).build();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 8. Endpoint Bloqueado para Todos
    // ═══════════════════════════════════════════════════════════
    
    @DELETE
    @Path("/system/reset")
    @DenyAll
    public Response resetarSistema() {
        // Ninguém pode acessar via HTTP
        // Apenas via console/script interno
        return Response.status(Response.Status.FORBIDDEN)
            .entity(Map.of("error", "Operação não permitida via API"))
            .build();
    }
    
    // ═══════════════════════════════════════════════════════════
    // Métodos Auxiliares
    // ═══════════════════════════════════════════════════════════
    
    private PedidoDTO buscarPedido(Long id) {
        // Simulação - substituir por busca real no banco
        return new PedidoDTO(id, 1L, "Produto X", 99.90);
    }
}

record PedidoDTO(Long id, Long usuarioId, String produto, Double valor) {}
```

---

## 7. Claims Customizadas

### 7.1 Injeção e Uso de Claims

```java
package com.exemplo.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.jwt.*;

import java.util.*;

/**
 * Trabalhando com Claims Customizadas
 */
@Path("/api/claims")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("user")
public class ClaimsResource {
    
    @Inject
    JsonWebToken jwt;
    
    // ═══════════════════════════════════════════════════════════
    // Injeção Direta de Claims Específicas
    // ═══════════════════════════════════════════════════════════
    
    @Inject
    @Claim("email")
    String email;
    
    @Inject
    @Claim("userId")
    Long userId;
    
    @Inject
    @Claim("departamento")
    Optional<String> departamento;
    
    @Inject
    @Claim("nivel")
    ClaimValue<String> nivel;
    
    @Inject
    @Claim("permissions")
    ClaimValue<Set<String>> permissions;
    
    // ═══════════════════════════════════════════════════════════
    // 1. Listar Todas as Claims
    // ═══════════════════════════════════════════════════════════
    
    @GET
    @Path("/all")
    public Response todasClaims() {
        Map<String, Object> claims = new HashMap<>();
        
        // Iterar sobre todas as claims
        jwt.getClaimNames().forEach(claimName -> {
            claims.put(claimName, jwt.getClaim(claimName));
        });
        
        return Response.ok(claims).build();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 2. Claims Padrão (Standard)
    // ═══════════════════════════════════════════════════════════
    
    @GET
    @Path("/standard")
    public Response claimsPadrao() {
        return Response.ok(Map.of(
            "issuer", jwt.getIssuer(),
            "subject", jwt.getSubject(),
            "audience", jwt.getAudience(),
            "expirationTime", jwt.getExpirationTime(),
            "issuedAtTime", jwt.getIssuedAtTime(),
            "tokenID", jwt.getTokenID(),
            "name", jwt.getName(),
            "groups", jwt.getGroups()
        )).build();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 3. Claims Injetadas
    // ═══════════════════════════════════════════════════════════
    
    @GET
    @Path("/injected")
    public Response claimsInjetadas() {
        return Response.ok(Map.of(
            "email", email,
            "userId", userId,
            "departamento", departamento.orElse("Não informado"),
            "nivel", nivel.getValue(),
            "permissions", permissions.getValue()
        )).build();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 4. Verificar Existência de Claims
    // ═══════════════════════════════════════════════════════════
    
    @GET
    @Path("/check")
    public Response verificarClaims() {
        return Response.ok(Map.of(
            "hasEmail", jwt.containsClaim("email"),
            "hasDepartamento", jwt.containsClaim("departamento"),
            "hasPermissions", jwt.containsClaim("permissions"),
            "hasCustomClaim", jwt.containsClaim("minhaClaim")
        )).build();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 5. Claims com Tipos Complexos
    // ═══════════════════════════════════════════════════════════
    
    @GET
    @Path("/complex")
    public Response claimsComplexas() {
        // Array/List
        List<String> grupos = jwt.getClaim("groups");
        
        // Map/Object
        Map<String, Object> metadados = jwt.getClaim("metadata");
        
        // Número
        Integer idade = jwt.getClaim("idade");
        
        // Boolean
        Boolean premium = jwt.getClaim("premium");
        
        return Response.ok(Map.of(
            "grupos", grupos != null ? grupos : List.of(),
            "metadados", metadados != null ? metadados : Map.of(),
            "idade", idade != null ? idade : 0,
            "premium", premium != null ? premium : false
        )).build();
    }
    
    // ═══════════════════════════════════════════════════════════
    // 6. Validação de Claims
    // ═══════════════════════════════════════════════════════════
    
    @GET
    @Path("/validate")
    public Response validarClaims() {
        List<String> erros = new ArrayList<>();
        
        // Validar email
        if (!jwt.containsClaim("email")) {
            erros.add("Email obrigatório");
        } else {
            String emailValue = jwt.getClaim("email");
            if (!emailValue.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                erros.add("Email inválido");
            }
        }
        
        // Validar permissões
        if (jwt.containsClaim("permissions")) {
            Set<String> perms = jwt.getClaim("permissions");
            if (perms.isEmpty()) {
                erros.add("Usuário deve ter pelo menos uma permissão");
            }
        }
        
        // Validar expiração
        long exp = jwt.getExpirationTime();
        long now = java.time.Instant.now().getEpochSecond();
        if (exp - now < 60) {
            erros.add("Token expira em menos de 1 minuto");
        }
        
        return Response.ok(Map.of(
            "valido", erros.isEmpty(),
            "erros", erros
        )).build();
    }
}
```

### 7.2 Interceptador para Claims Customizadas

```java
package com.exemplo.security;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.*;
import jakarta.ws.rs.ForbiddenException;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.lang.annotation.*;

/**
 * Anotação para exigir claim específica
 */
@InterceptorBinding
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireClaim {
    String name();
    String value() default "";
    boolean required() default true;
}

/**
 * Interceptador que valida claims customizadas
 */
@RequireClaim(name = "")
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE + 200)
public class ClaimInterceptor {
    
    @Inject
    JsonWebToken jwt;
    
    @AroundInvoke
    public Object validateClaim(InvocationContext context) throws Exception {
        RequireClaim annotation = context.getMethod().getAnnotation(RequireClaim.class);
        
        if (annotation == null) {
            annotation = context.getTarget().getClass().getAnnotation(RequireClaim.class);
        }
        
        if (annotation != null) {
            String claimName = annotation.name();
            String expectedValue = annotation.value();
            boolean required = annotation.required();
            
            // Verificar se claim existe
            if (!jwt.containsClaim(claimName)) {
                if (required) {
                    throw new ForbiddenException(
                        "Claim obrigatória não encontrada: " + claimName
                    );
                }
                return context.proceed();
            }
            
            // Verificar valor (se especificado)
            if (!expectedValue.isEmpty()) {
                String actualValue = jwt.getClaim(claimName).toString();
                if (!expectedValue.equals(actualValue)) {
                    throw new ForbiddenException(
                        "Valor da claim '" + claimName + "' inválido. " +
                        "Esperado: " + expectedValue + ", Atual: " + actualValue
                    );
                }
            }
        }
        
        return context.proceed();
    }
}

/**
 * Uso do interceptador
 */
@Path("/api/protected")
public class ProtectedResourceWithClaims {
    
    // Exige claim "departamento" com valor "TI"
    @GET
    @Path("/ti-only")
    @RequireClaim(name = "departamento", value = "TI")
    public Response apenasTI() {
        return Response.ok(Map.of(
            "message", "Acesso permitido - Departamento TI"
        )).build();
    }
    
    // Exige claim "premium" (apenas existência)
    @GET
    @Path("/premium-features")
    @RequireClaim(name = "premium", required = true)
    public Response featuresPremium() {
        return Response.ok(Map.of(
            "features", List.of("feature1", "feature2", "feature3")
        )).build();
    }
}
```

---

## 8. Refresh Tokens

### 8.1 Estratégia de Refresh Token

```mermaid
sequenceDiagram
    participant U as Usuário
    participant F as Frontend
    participant A as Auth API
    participant R as Resource API
    
    U->>F: Login
    F->>A: POST /auth/login
    A-->>F: accessToken (15 min)<br/>refreshToken (7 dias)
    
    Note over F: Armazena tokens
    
    F->>R: GET /api/data<br/>Bearer accessToken
    R-->>F: Dados
    
    Note over F: 14 minutos depois
    
    F->>R: GET /api/data<br/>Bearer accessToken
    R-->>F: 401 Unauthorized (token expirado)
    
    F->>A: POST /auth/refresh<br/>{refreshToken}
    A->>A: Validar refresh token
    A-->>F: Novo accessToken (15 min)
    
    F->>R: GET /api/data<br/>Bearer NOVO accessToken
    R-->>F: Dados
```

### 8.2 Implementação Completa de Refresh

```java
package com.exemplo.auth.service;

import io.jsonwebtoken.Claims;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Serviço de gerenciamento de Refresh Tokens
 */
@ApplicationScoped
public class RefreshTokenService {
    
    @Inject
    TokenService tokenService;
    
    @Inject
    CustomJwtValidator jwtValidator;
    
    @Inject
    UsuarioService usuarioService;
    
    // Em produção, usar Redis para armazenar tokens ativos
    private final Map<String, RefreshTokenMetadata> activeRefreshTokens = new HashMap<>();
    
    // ═══════════════════════════════════════════════════════════
    // 1. Criar Refresh Token
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Cria refresh token e armazena metadados
     */
    public String criarRefreshToken(String username, boolean rememberMe, String deviceInfo) {
        String refreshToken = tokenService.gerarRefreshToken(username, rememberMe);
        
        // Armazenar metadados
        RefreshTokenMetadata metadata = new RefreshTokenMetadata(
            username,
            deviceInfo,
            Instant.now(),
            rememberMe ? 30 : 7  // Dias de validade
        );
        
        activeRefreshTokens.put(refreshToken, metadata);
        
        return refreshToken;
    }
    
    // ═══════════════════════════════════════════════════════════
    // 2. Validar e Renovar
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Valida refresh token e gera novo access token
     */
    public RefreshResult renovarAccessToken(String refreshToken) 
            throws RefreshTokenException {
        
        // 1. Verificar se token está na lista de ativos
        if (!activeRefreshTokens.containsKey(refreshToken)) {
            throw new RefreshTokenException("Refresh token inválido ou revogado");
        }
        
        // 2. Validar assinatura e expiração
        Claims claims;
        try {
            claims = jwtValidator.validarToken(refreshToken);
        } catch (JwtValidationException e) {
            activeRefreshTokens.remove(refreshToken);
            throw new RefreshTokenException("Refresh token expirado", e);
        }
        
        // 3. Verificar tipo do token
        String tipo = claims.get("tipo", String.class);
        if (!"refresh_token".equals(tipo)) {
            throw new RefreshTokenException("Token não é um refresh token");
        }
        
        // 4. Buscar usuário
        String username = claims.get("upn", String.class);
        UsuarioDTO usuario = usuarioService.buscarPorUsername(username);
        
        if (usuario == null || !usuario.ativo) {
            activeRefreshTokens.remove(refreshToken);
            throw new RefreshTokenException("Usuário não encontrado ou inativo");
        }
        
        // 5. Gerar novo access token
        String novoAccessToken = tokenService.gerarAccessToken(usuario);
        
        // 6. Atualizar último uso
        RefreshTokenMetadata metadata = activeRefreshTokens.get(refreshToken);
        metadata.updateLastUsed();
        
        return new RefreshResult(
            novoAccessToken,
            refreshToken,  // Mantém o mesmo refresh token
            900  // 15 minutos
        );
    }
    
    // ═══════════════════════════════════════════════════════════
    // 3. Rotação de Refresh Token (Recomendado para Segurança)
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Rotaciona refresh token (gera novo e revoga o antigo)
     * Mais seguro que reutilizar o mesmo refresh token
     */
    public RefreshResult renovarComRotacao(String oldRefreshToken) 
            throws RefreshTokenException {
        
        // Validar token antigo
        RefreshResult result = renovarAccessToken(oldRefreshToken);
        
        // Extrair informações do token antigo
        Claims claims = jwtValidator.validarToken(oldRefreshToken);
        String username = claims.get("upn", String.class);
        boolean rememberMe = claims.get("rememberMe", Boolean.class);
        
        // Revogar token antigo
        revogarRefreshToken(oldRefreshToken);
        
        // Criar novo refresh token
        RefreshTokenMetadata metadata = activeRefreshTokens.get(oldRefreshToken);
        String novoRefreshToken = criarRefreshToken(
            username, 
            rememberMe, 
            metadata.deviceInfo
        );
        
        return new RefreshResult(
            result.accessToken(),
            novoRefreshToken,  // Novo refresh token
            result.expiresIn()
        );
    }
    
    // ═══════════════════════════════════════════════════════════
    // 4. Revogação
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Revoga refresh token específico
     */
    public void revogarRefreshToken(String refreshToken) {
        activeRefreshTokens.remove(refreshToken);
        // Em produção: remover do Redis
    }
    
    /**
     * Revoga todos os refresh tokens de um usuário
     */
    public void revogarTodosTokensDoUsuario(String username) {
        activeRefreshTokens.entrySet().removeIf(entry -> 
            entry.getValue().username.equals(username)
        );
    }
    
    /**
     * Revoga refresh tokens antigos (limpeza)
     */
    public int limparTokensExpirados() {
        Instant now = Instant.now();
        int removidos = 0;
        
        Iterator<Map.Entry<String, RefreshTokenMetadata>> iterator = 
            activeRefreshTokens.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<String, RefreshTokenMetadata> entry = iterator.next();
            RefreshTokenMetadata metadata = entry.getValue();
            
            Instant expiration = metadata.createdAt.plus(metadata.validityDays, ChronoUnit.DAYS);
            
            if (now.isAfter(expiration)) {
                iterator.remove();
                removidos++;
            }
        }
        
        return removidos;
    }
    
    // ═══════════════════════════════════════════════════════════
    // 5. Listagem de Sessões Ativas
    // ═══════════════════════════════════════════════════════════
    
    /**
     * Lista sessões ativas do usuário
     */
    public List<SessionInfo> listarSessoesAtivas(String username) {
        return activeRefreshTokens.entrySet().stream()
            .filter(e -> e.getValue().username.equals(username))
            .map(e -> new SessionInfo(
                e.getKey().substring(0, 10) + "...",  // Primeiros 10 chars
                e.getValue().deviceInfo,
                e.getValue().createdAt,
                e.getValue().lastUsed
            ))
            .toList();
    }
}

/**
 * Metadados do Refresh Token
 */
class RefreshTokenMetadata {
    String username;
    String deviceInfo;
    Instant createdAt;
    Instant lastUsed;
    int validityDays;
    
    RefreshTokenMetadata(String username, String deviceInfo, Instant createdAt, int validityDays) {
        this.username = username;
        this.deviceInfo = deviceInfo;
        this.createdAt = createdAt;
        this.lastUsed = createdAt;
        this.validityDays = validityDays;
    }
    
    void updateLastUsed() {
        this.lastUsed = Instant.now();
    }
}

/**
 * Resultado da renovação
 */
record RefreshResult(
    String accessToken,
    String refreshToken,
    int expiresIn
) {}

/**
 * Informação de sessão ativa
 */
record SessionInfo(
    String tokenId,
    String device,
    Instant createdAt,
    Instant lastUsed
) {}

/**
 * Exceção de refresh token
 */
class RefreshTokenException extends Exception {
    public RefreshTokenException(String message) {
        super(message);
    }
    
    public RefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

---

## 9. Integração com Frontend

### 9.1 Cliente JavaScript Completo

```javascript
/**
 * Cliente de Autenticação JWT
 * 
 * Funcionalidades:
 * - Login/Logout
 * - Armazenamento de tokens
 * - Renovação automática
 * - Interceptação de requisições
 */
class AuthClient {
    constructor(baseUrl) {
        this.baseUrl = baseUrl;
        this.token = localStorage.getItem('access_token');
        this.refreshToken = localStorage.getItem('refresh_token');
        this.tokenExpiresAt = parseInt(localStorage.getItem('token_expires_at') || '0');
        this.refreshing = null; // Promise de refresh em andamento
    }
    
    // ═══════════════════════════════════════════════════════════
    // Login
    // ═══════════════════════════════════════════════════════════
    
    async login(username, password, rememberMe = false) {
        try {
            const response = await fetch(`${this.baseUrl}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password, rememberMe })
            });
            
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || 'Credenciais inválidas');
            }
            
            const data = await response.json();
            this._saveTokens(data);
            
            return data;
            
        } catch (error) {
            console.error('Erro no login:', error);
            throw error;
        }
    }
    
    // ═══════════════════════════════════════════════════════════
    // Requisição Autenticada
    // ═══════════════════════════════════════════════════════════
    
    async request(url, options = {}) {
        // Verificar se token precisa renovação
        if (this._shouldRefreshToken()) {
            await this._refreshAccessToken();
        }
        
        // Adicionar token no header
        const headers = {
            ...options.headers,
            'Authorization': `Bearer ${this.token}`
        };
        
        try {
            const response = await fetch(url, {
                ...options,
                headers
            });
            
            // Token expirou durante a requisição
            if (response.status === 401) {
                console.log('Token expirado, renovando...');
                
                // Tentar renovar
                await this._refreshAccessToken();
                
                // Tentar novamente com novo token
                headers['Authorization'] = `Bearer ${this.token}`;
                return fetch(url, { ...options, headers });
            }
            
            return response;
            
        } catch (error) {
            console.error('Erro na requisição:', error);
            throw error;
        }
    }
    
    // ═══════════════════════════════════════════════════════════
    // Renovação de Token
    // ═══════════════════════════════════════════════════════════
    
    async _refreshAccessToken() {
        // Se já há uma renovação em andamento, aguardar
        if (this.refreshing) {
            return this.refreshing;
        }
        
        this.refreshing = (async () => {
            try {
                const response = await fetch(`${this.baseUrl}/auth/refresh`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ refreshToken: this.refreshToken })
                });
                
                if (!response.ok) {
                    // Refresh token inválido - fazer logout
                    this.logout();
                    throw new Error('Refresh token inválido');
                }
                
                const data = await response.json();
                
                // Atualizar access token
                this.token = data.accessToken;
                this.tokenExpiresAt = Date.now() + (data.expiresIn * 1000);
                
                localStorage.setItem('access_token', this.token);
                localStorage.setItem('token_expires_at', this.tokenExpiresAt.toString());
                
                console.log('Token renovado com sucesso');
                
            } catch (error) {
                console.error('Erro ao renovar token:', error);
                this.logout();
                throw error;
            } finally {
                this.refreshing = null;
            }
        })();
        
        return this.refreshing;
    }
    
    // ═══════════════════════════════════════════════════════════
    // Helpers
    // ═══════════════════════════════════════════════════════════
    
    _shouldRefreshToken() {
        if (!this.token || !this.refreshToken) {
            return false;
        }
        
        const now = Date.now();
        const timeUntilExpiration = this.tokenExpiresAt - now;
        
        // Renovar se faltar menos de 2 minutos
        return timeUntilExpiration < 120000;
    }
    
    _saveTokens(data) {
        this.token = data.accessToken;
        this.refreshToken = data.refreshToken;
        this.tokenExpiresAt = Date.now() + (data.expiresIn * 1000);
        
        localStorage.setItem('access_token', this.token);
        localStorage.setItem('refresh_token', this.refreshToken);
        localStorage.setItem('token_expires_at', this.tokenExpiresAt.toString());
    }
    
    logout() {
        this.token = null;
        this.refreshToken = null;
        this.tokenExpiresAt = 0;
        
        localStorage.removeItem('access_token');
        localStorage.removeItem('refresh_token');
        localStorage.removeItem('token_expires_at');
        
        // Redirecionar para login (opcional)
        // window.location.href = '/login';
    }
    
    isAuthenticated() {
        return !!this.token && this.tokenExpiresAt > Date.now();
    }
    
    getToken() {
        return this.token;
    }
}

// ═══════════════════════════════════════════════════════════
// Uso Prático
// ═══════════════════════════════════════════════════════════

const auth = new AuthClient('http://localhost:8080');

// Login
async function fazerLogin() {
    try {
        const data = await auth.login('admin', 'senha123', true);
        console.log('Login realizado:', data.usuario);
    } catch (error) {
        console.error('Falha no login:', error.message);
    }
}

// Fazer requisição autenticada
async function buscarDados() {
    try {
        const response = await auth.request('http://localhost:8080/api/dados');
        const dados = await response.json();
        console.log('Dados:', dados);
    } catch (error) {
        console.error('Erro:', error);
    }
}

// Logout
function fazerLogout() {
    auth.logout();
    console.log('Logout realizado');
}
```

### 9.2 Interceptor Axios (Alternativa)

```javascript
import axios from 'axios';

class AxiosAuthClient {
    constructor(baseURL) {
        this.axios = axios.create({
            baseURL,
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        this.setupInterceptors();
    }
    
    setupInterceptors() {
        // Request Interceptor - Adiciona token
        this.axios.interceptors.request.use(
            config => {
                const token = localStorage.getItem('access_token');
                if (token) {
                    config.headers.Authorization = `Bearer ${token}`;
                }
                return config;
            },
            error => Promise.reject(error)
        );
        
        // Response Interceptor - Renova token se expirado
        this.axios.interceptors.response.use(
            response => response,
            async error => {
                const originalRequest = error.config;
                
                // Token expirado
                if (error.response?.status === 401 && !originalRequest._retry) {
                    originalRequest._retry = true;
                    
                    try {
                        const refreshToken = localStorage.getItem('refresh_token');
                        const response = await axios.post(`${this.axios.defaults.baseURL}/auth/refresh`, {
                            refreshToken
                        });
                        
                        const { accessToken } = response.data;
                        localStorage.setItem('access_token', accessToken);
                        
                        // Tentar novamente com novo token
                        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
                        return this.axios(originalRequest);
                        
                    } catch (refreshError) {
                        // Refresh falhou - fazer logout
                        localStorage.clear();
                        window.location.href = '/login';
                        return Promise.reject(refreshError);
                    }
                }
                
                return Promise.reject(error);
            }
        );
    }
    
    async login(username, password, rememberMe = false) {
        const response = await this.axios.post('/auth/login', {
            username,
            password,
            rememberMe
        });
        
        const { accessToken, refreshToken } = response.data;
        localStorage.setItem('access_token', accessToken);
        localStorage.setItem('refresh_token', refreshToken);
        
        return response.data;
    }
    
    logout() {
        localStorage.clear();
    }
}

export default new AxiosAuthClient('http://localhost:8080');
```

Devido ao limite de tokens, vou criar um arquivo final com as seções restantes (10-14):

---

## 10. Configurações Avançadas

Já foi coberto na Seção 3.4 com exemplos de múltiplos issuers, audiences, etc.

---

## 11. Testes

### 11.1 Testes com Quarkus Test Security

```java
@QuarkusTest
public class JwtSecurityTest {
    
    @Test
    @TestSecurity(user = "testuser", roles = "user")
    public void testEndpointProtegidoComUsuario() {
        given()
            .when()
            .get("/api/perfil")
            .then()
            .statusCode(200)
            .body("username", equalTo("testuser"));
    }
    
    @Test
    @TestSecurity(user = "admin", roles = {"admin", "user"})
    public void testEndpointAdminComRoleAdmin() {
        given()
            .when()
            .get("/api/admin/users")
            .then()
            .statusCode(200);
    }
    
    @Test
    public void testEndpointProtegidoSemAutenticacao() {
        given()
            .when()
            .get("/api/perfil")
            .then()
            .statusCode(401);
    }
}
```

---

## 12. Segurança e Boas Práticas

### 12.1 Checklist de Segurança

- ✅ **Use HTTPS em produção**
- ✅ **Chaves RSA mínimo 2048 bits**
- ✅ **Access token curto (15 min)**
- ✅ **Não armazene dados sensíveis no token**
- ✅ **Implemente blacklist para revogação**
- ✅ **Use refresh token rotation**
- ✅ **Valide audience e issuer**
- ✅ **Implemente rate limiting**
- ✅ **Monitore tentativas de login**

---

## 13. Troubleshooting

**Token inválido**: Verificar se issuer/audience estão corretos
**401 sempre**: Verificar se chave pública está correta
**Claims nulas**: Verificar se claim existe no token

---

## 14. Recursos e Referências

- [MicroProfile JWT](https://microprofile.io/specifications/microprofile-jwt-auth/)
- [SmallRye JWT Documentation](https://smallrye.io/smallrye-jwt/)
- [Quarkus Security Guide](https://quarkus.io/guides/security)
- [RFC 7519 - JWT](https://tools.ietf.org/html/rfc7519)

---

## Conclusão

SmallRye JWT no Quarkus fornece autenticação robusta e escalável para APIs modernas. Com tokens stateless, integração nativa e segurança configurável, é a escolha ideal para microserviços Java.

**Próximos Passos**:
1. ✅ Implementar login/registro
2. ✅ Configurar refresh tokens
3. ✅ Proteger endpoints
4. 📚 Implementar blacklist
5. 📚 Monitorar segurança
6. 📚 Deploy em produção
}

// Resposta de autenticação
public class AuthResponse {
    public String accessToken;
    public String refreshToken;
    public String tokenType;
    public int expiresIn;
    public UsuarioDTO usuario;

    public AuthResponse(String accessToken, String refreshToken,
                       String tokenType, int expiresIn, UsuarioDTO usuario) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.usuario = usuario;
    }
}

// DTO do usuário
public class UsuarioDTO {
    public Long id;
    public String username;
    public String nome;
    public String email;
    public String departamento;
    public String nivel;
    public Set<String> roles;
}

// Resposta de erro
public class ErrorResponse {
    public String error;
    public String message;
    public LocalDateTime timestamp;

    public ErrorResponse(String message) {
        this.error = "Authentication Error";
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}

```

## 4. Proteção de Endpoints

### 4.1 Anotações de Segurança Básicas

```java
@Path("/api/protected")
@ApplicationScoped
public class ProtectedResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    SecurityIdentity identity;

    // Qualquer usuário autenticado pode acessar
    @GET
    @Path("/info")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response informacoesUsuario() {
        Map<String, Object> info = new HashMap<>();
        info.put("username", jwt.getName());
        info.put("roles", jwt.getGroups());
        info.put("email", jwt.getClaim("email"));
        info.put("expiracao", jwt.getExpirationTime());

        return Response.ok(info).build();
    }

    // Apenas administradores podem acessar
    @GET
    @Path("/admin")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response areaAdministrativa() {
        return Response.ok("Área exclusiva para administradores").build();
    }

    // Múltiplos roles permitidos
    @GET
    @Path("/gerencial")
    @RolesAllowed({"admin", "manager"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response areaGerencial() {
        Map<String, Object> dados = new HashMap<>();
        dados.put("usuario", jwt.getName());
        dados.put("nivel", jwt.getClaim("nivel"));
        dados.put("departamento", jwt.getClaim("departamento"));

        return Response.ok(dados).build();
    }

    // Verificação programática de roles
    @GET
    @Path("/dinamica")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verificacaoDinamica() {
        if (identity.hasRole("admin")) {
            return Response.ok("Acesso completo - Administrador").build();
        } else if (identity.hasRole("manager")) {
            return Response.ok("Acesso limitado - Gerente").build();
        } else {
            return Response.ok("Acesso básico - Usuário").build();
        }
    }
}

```

### 4.2 Controle de Acesso Avançado

```java
@Path("/api/recursos")
@ApplicationScoped
public class RecursoResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    SecurityIdentity identity;

    @GET
    @Path("/usuarios")
    @RolesAllowed({"admin", "hr"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarUsuarios() {
        // Diferentes níveis de acesso baseado no role
        List<UsuarioDTO> usuarios;

        if (identity.hasRole("admin")) {
            usuarios = obterTodosUsuarios();
        } else if (identity.hasRole("hr")) {
            usuarios = obterUsuariosDoRH();
        } else {
            return Response.status(403).build();
        }

        return Response.ok(usuarios).build();
    }

    @GET
    @Path("/perfil")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obterPerfil() {
        String username = jwt.getName();
        Long userId = jwt.getClaim("userId");

        // Usuário só pode ver seu próprio perfil (exceto admin)
        if (!identity.hasRole("admin") && !username.equals(obterUsernameAtual())) {
            return Response.status(403).build();
        }

        UsuarioDTO usuario = buscarUsuarioPorId(userId);
        return Response.ok(usuario).build();
    }

    @PUT
    @Path("/perfil/{id}")
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizarPerfil(@PathParam("id") Long id, UsuarioDTO dados) {
        Long userIdToken = jwt.getClaim("userId");

        // Verificar se o usuário está tentando editar seu próprio perfil
        if (!identity.hasRole("admin") && !id.equals(userIdToken)) {
            return Response.status(403)
                .entity(new ErrorResponse("Você só pode editar seu próprio perfil"))
                .build();
        }

        // Lógica de atualização
        UsuarioDTO usuarioAtualizado = atualizarUsuario(id, dados);
        return Response.ok(usuarioAtualizado).build();
    }

    private List<UsuarioDTO> obterTodosUsuarios() {
        // Implementar busca completa
        return List.of();
    }

    private List<UsuarioDTO> obterUsuariosDoRH() {
        // Implementar busca limitada para RH
        return List.of();
    }

    private String obterUsernameAtual() {
        return jwt.getName();
    }

    private UsuarioDTO buscarUsuarioPorId(Long id) {
        // Implementar busca por ID
        return new UsuarioDTO();
    }

    private UsuarioDTO atualizarUsuario(Long id, UsuarioDTO dados) {
        // Implementar atualização
        return dados;
    }
}

```

## 5. Trabalhando com Claims Customizadas

### 5.1 Injeção de Claims

```java
@Path("/api/claims")
@ApplicationScoped
public class ClaimsResource {

    @Inject
    JsonWebToken jwt;

    // Injeção direta de claims específicas
    @Inject
    @Claim("email")
    String email;

    @Inject
    @Claim("userId")
    Long userId;

    @Inject
    @Claim("departamento")
    Optional<String> departamento;

    @Inject
    @Claim("nivel")
    ClaimValue<String> nivel;

    @GET
    @Path("/dados")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obterDadosUsuario() {
        Map<String, Object> dados = new HashMap<>();

        // Usando injeção direta
        dados.put("email", email);
        dados.put("userId", userId);
        dados.put("departamento", departamento.orElse("Não informado"));
        dados.put("nivel", nivel.getValue());

        // Usando JWT diretamente
        dados.put("username", jwt.getName());
        dados.put("roles", jwt.getGroups());
        dados.put("issuer", jwt.getIssuer());
        dados.put("expiracao", jwt.getExpirationTime());

        // Claims customizadas
        dados.put("nome", jwt.getClaim("nome"));
        dados.put("todasClaims", jwt.getClaimNames());

        return Response.ok(dados).build();
    }

    @GET
    @Path("/validacao")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validarClaims() {
        Map<String, Object> validacao = new HashMap<>();

        // Verificar se claims existem
        validacao.put("temEmail", jwt.containsClaim("email"));
        validacao.put("temDepartamento", jwt.containsClaim("departamento"));

        // Verificar valores específicos
        String nivelUsuario = jwt.getClaim("nivel");
        validacao.put("nivelAlto", "senior".equals(nivelUsuario) || "expert".equals(nivelUsuario));

        // Verificar expiração
        long exp = jwt.getExpirationTime();
        long agora = Instant.now().getEpochSecond();
        validacao.put("tempoRestante", exp - agora);
        validacao.put("proximoExpiracao", exp - agora < 300); // 5 minutos

        return Response.ok(validacao).build();
    }
}

```

### 5.2 Interceptador para Claims Customizadas

```java
@InterceptorBinding
@Target({TYPE, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireClaim {
    String claim();
    String value();
}

@RequireClaim(claim = "", value = "")
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE + 200)
public class ClaimInterceptor {

    @Inject
    JsonWebToken jwt;

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        RequireClaim annotation = context.getMethod().getAnnotation(RequireClaim.class);
        if (annotation != null) {
            String claimName = annotation.claim();
            String expectedValue = annotation.value();

            if (!jwt.containsClaim(claimName)) {
                throw new ForbiddenException("Claim '" + claimName + "' não encontrada");
            }

            String actualValue = jwt.getClaim(claimName);
            if (!expectedValue.equals(actualValue)) {
                throw new ForbiddenException("Valor da claim '" + claimName + "' inválido");
            }
        }

        return context.proceed();
    }
}

```

## 6. Configurações Avançadas

### 6.1 Múltiplos Issuers

```
# Configuração para múltiplos issuers
mp.jwt.verify.publickey.location=META-INF/resources/publickey.pem
mp.jwt.verify.issuer=https://meuapp.com,https://parceiro.com

# Configuração específica por issuer
smallrye.jwt.verify.aud=meuapp,parceiro
smallrye.jwt.verify.algorithm=RS256
smallrye.jwt.path.groups=roles
smallrye.jwt.path.sub=username

```

### 6.2 Configuração Dinâmica

```java
@ApplicationScoped
public class JWTConfigurationProducer {

    @Produces
    @ApplicationScoped
    public JWTAuthContextInfo createJWTAuthContextInfo() {
        JWTAuthContextInfo authInfo = new JWTAuthContextInfo();

        // Configurar issuer dinamicamente
        authInfo.setIssuedBy("https://meuapp.com");

        // Configurar algoritmo
        authInfo.setSignatureAlgorithm(SignatureAlgorithm.RS256);

        // Configurar chave pública
        try {
            String publicKey = lerChavePublica();
            authInfo.setPublicKeyContent(publicKey);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar chave pública", e);
        }

        // Configurar claims customizadas
        authInfo.setDefaultGroupsClaim("roles");
        authInfo.setDefaultSubClaim("username");

        return authInfo;
    }

    private String lerChavePublica() throws Exception {
        // Implementar leitura da chave pública
        return Files.readString(Paths.get("src/main/resources/META-INF/resources/publickey.pem"));
    }
}

```

### 6.3 Validação Customizada

```java
@ApplicationScoped
public class CustomJWTValidator {

    @Inject
    JsonWebToken jwt;

    public void validarToken() {
        // Validar claims obrigatórias
        if (!jwt.containsClaim("email")) {
            throw new UnauthorizedException("Token deve conter claim 'email'");
        }

        // Validar formato do email
        String email = jwt.getClaim("email");
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new UnauthorizedException("Formato de email inválido");
        }

        // Validar departamento
        Optional<String> dept = jwt.claim("departamento");
        if (dept.isPresent() && !List.of("TI", "RH", "VENDAS").contains(dept.get())) {
            throw new UnauthorizedException("Departamento inválido");
        }

        // Validar expiração customizada
        long exp = jwt.getExpirationTime();
        long now = Instant.now().getEpochSecond();
        if (exp - now < 300) { // 5 minutos
            throw new UnauthorizedException("Token próximo do vencimento");
        }
    }
}

```

## 7. Integração com Frontend

### 7.1 Exemplo de Cliente JavaScript

```jsx
class AuthClient {
    constructor(baseUrl) {
        this.baseUrl = baseUrl;
        this.token = localStorage.getItem('access_token');
        this.refreshToken = localStorage.getItem('refresh_token');
    }

    async login(username, password) {
        try {
            const response = await fetch(`${this.baseUrl}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password })
            });

            if (!response.ok) {
                throw new Error('Credenciais inválidas');
            }

            const data = await response.json();
            this.token = data.accessToken;
            this.refreshToken = data.refreshToken;

            // Armazenar tokens
            localStorage.setItem('access_token', this.token);
            localStorage.setItem('refresh_token', this.refreshToken);

            return data;
        } catch (error) {
            console.error('Erro no login:', error);
            throw error;
        }
    }

    async makeAuthenticatedRequest(url, options = {}) {
        const headers = {
            ...options.headers,
            'Authorization': `Bearer ${this.token}`
        };

        try {
            const response = await fetch(url, {
                ...options,
                headers
            });

            if (response.status === 401) {
                // Token expirado, tentar refresh
                await this.refreshAccessToken();

                // Tentar novamente com novo token
                headers['Authorization'] = `Bearer ${this.token}`;
                return fetch(url, { ...options, headers });
            }

            return response;
        } catch (error) {
            console.error('Erro na requisição:', error);
            throw error;
        }
    }

    async refreshAccessToken() {
        try {
            const response = await fetch(`${this.baseUrl}/auth/refresh`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ refreshToken: this.refreshToken })
            });

            if (!response.ok) {
                this.logout();
                throw new Error('Refresh token inválido');
            }

            const data = await response.json();
            this.token = data.accessToken;
            localStorage.setItem('access_token', this.token);

        } catch (error) {
            console.error('Erro no refresh:', error);
            this.logout();
            throw error;
        }
    }

    logout() {
        this.token = null;
        this.refreshToken = null;
        localStorage.removeItem('access_token');
        localStorage.removeItem('refresh_token');
    }

    isAuthenticated() {
        return !!this.token;
    }

    getToken() {
        return this.token;
    }
}

// Uso prático
const authClient = new AuthClient('http://localhost:8080');

// Login
authClient.login('admin', 'senha123')
    .then(data => console.log('Login realizado:', data))
    .catch(error => console.error('Erro:', error));

// Fazer requisição autenticada
authClient.makeAuthenticatedRequest('http://localhost:8080/api/protected/info')
    .then(response => response.json())
    .then(data => console.log('Dados do usuário:', data));

```

### 7.2 Interceptador HTTP para Refresh Automático

```java
@ApplicationScoped
public class JWTRefreshFilter implements ContainerRequestFilter {

    @Inject
    TokenService tokenService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // Verificar se o token está próximo do vencimento
                if (tokenProximoVencimento(token)) {
                    // Renovar token automaticamente
                    String novoToken = renovarToken(token);

                    // Adicionar novo token no header de resposta
                    requestContext.getHeaders().add("X-New-Token", novoToken);
                }
            } catch (Exception e) {
                // Log do erro, mas não bloquear a requisição
                System.err.println("Erro ao renovar token: " + e.getMessage());
            }
        }
    }

    private boolean tokenProximoVencimento(String token) {
        // Implementar verificação de expiração
        return false; // Placeholder
    }

    private String renovarToken(String token) {
        // Implementar renovação de token
        return token; // Placeholder
    }
}

```

## 8. Testes Completos

### 8.1 Testes de Integração

```java
@QuarkusTest
public class AuthResourceTest {

    @Test
    public void testLoginSucesso() {
        LoginRequest request = new LoginRequest();
        request.username = "admin";
        request.password = "senha123";

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post("/auth/login")
            .then()
            .statusCode(200)
            .body("accessToken", notNullValue())
            .body("refreshToken", notNullValue())
            .body("tokenType", equalTo("Bearer"))
            .body("usuario.username", equalTo("admin"));
    }

    @Test
    public void testLoginCredenciaisInvalidas() {
        LoginRequest request = new LoginRequest();
        request.username = "admin";
        request.password = "senhaerrada";

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post("/auth/login")
            .then()
            .statusCode(401)
            .body("message", equalTo("Credenciais inválidas"));
    }

    @Test
    public void testEndpointProtegidoComToken() {
        // Gerar token para teste
        String token = Jwt.issuer("https://meuapp.com")
                .upn("testuser")
                .groups(Set.of("user"))
                .claim("email", "test@example.com")
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .sign();

        given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/api/protected/info")
            .then()
            .statusCode(200)
            .body("username", equalTo("testuser"))
            .body("email", equalTo("test@example.com"));
    }

    @Test
    public void testEndpointProtegidoSemToken() {
        given()
            .when()
            .get("/api/protected/info")
            .then()
            .statusCode(401);
    }

    @Test
    public void testEndpointAdminComRoleUser() {
        String token = Jwt.issuer("https://meuapp.com")
                .upn("user")
                .groups(Set.of("user"))
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .sign();

        given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/api/protected/admin")
            .then()
            .statusCode(403);
    }
}

```
