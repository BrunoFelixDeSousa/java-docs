# Keycloak com Quarkus e Docker - Guia Completo

[![Quarkus](https://img.shields.io/badge/Quarkus-3.x-blue.svg)](https://quarkus.io/)
[![Keycloak](https://img.shields.io/badge/Keycloak-23.x-red.svg)](https://www.keycloak.org/)
[![Docker](https://img.shields.io/badge/Docker-24.x-blue.svg)](https://www.docker.com/)
[![OIDC](https://img.shields.io/badge/OIDC-OpenID%20Connect-green.svg)](https://openid.net/connect/)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

## 📋 Índice

1. [Introdução](#-introdução)
2. [Keycloak com Docker](#-keycloak-com-docker)
3. [Setup Quarkus + Keycloak](#-setup-quarkus--keycloak)
4. [Configuração do Realm](#-configuração-do-realm)
5. [Autenticação e Autorização](#-autenticação-e-autorização)
6. [Roles e Permissions](#-roles-e-permissions)
7. [REST API Protegida](#-rest-api-protegida)
8. [Clientes e Tokens](#-clientes-e-tokens)
9. [User Management](#-user-management)
10. [Docker Compose Completo](#-docker-compose-completo)
11. [Testes](#-testes)
12. [Best Practices](#-best-practices)
13. [Troubleshooting](#-troubleshooting)
14. [Referência Rápida](#-referência-rápida)
15. [Recursos](#-recursos)

---

## 🎯 Introdução

### O que é Keycloak?

**Keycloak** é uma solução open-source de **Identity and Access Management (IAM)** que fornece:

- **Single Sign-On (SSO)**: Login único para múltiplas aplicações
- **Identity Brokering**: Integração com provedores externos (Google, Facebook, GitHub)
- **User Federation**: LDAP, Active Directory
- **OAuth 2.0 / OpenID Connect**: Protocolos padrão de autenticação
- **SAML 2.0**: Suporte a SAML
- **User Management**: Admin console completo
- **Fine-grained Authorization**: Controle granular de permissões

### Arquitetura Keycloak + Quarkus

```
┌─────────────────────────────────────────────────────────┐
│                     Cliente (Browser)                    │
└────────────────────┬────────────────────────────────────┘
                     │ 1. Acessa aplicação
                     ↓
┌─────────────────────────────────────────────────────────┐
│              Quarkus Application (Backend)              │
│  ┌──────────────────────────────────────────────────┐  │
│  │  @RolesAllowed("user")                           │  │
│  │  REST Endpoints protegidos                        │  │
│  └──────────────────┬───────────────────────────────┘  │
└────────────────────┼────────────────────────────────────┘
                     │ 2. Valida token JWT
                     ↓
┌─────────────────────────────────────────────────────────┐
│                 Keycloak Server (IAM)                    │
│  ┌──────────────────────────────────────────────────┐  │
│  │  • Autentica usuários                            │  │
│  │  • Emite tokens JWT                              │  │
│  │  • Gerencia roles e permissions                  │  │
│  │  • Admin Console                                 │  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                     │ 3. Armazena dados
                     ↓
┌─────────────────────────────────────────────────────────┐
│              PostgreSQL Database                         │
└─────────────────────────────────────────────────────────┘
```

### Por que usar Keycloak?

| Aspecto | Sem Keycloak | Com Keycloak |
|---------|--------------|--------------|
| **Autenticação** | ❌ Implementar manualmente | ✅ Pronto (OAuth2/OIDC) |
| **User Management** | ❌ Criar CRUD de usuários | ✅ Admin Console |
| **SSO** | ❌ Login em cada app | ✅ Login único |
| **Social Login** | ❌ Integrar cada provider | ✅ Configurar no admin |
| **Password Policies** | ❌ Implementar validações | ✅ Políticas configuráveis |
| **2FA/MFA** | ❌ Implementar OTP | ✅ Suporte nativo |
| **Token Management** | ❌ Criar sistema de tokens | ✅ JWT automático |

---

## 🐳 Keycloak com Docker

### Docker Compose Básico

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    container_name: keycloak-postgres
    environment:
      POSTGRES_DB: keycloak
      POSTGRES_USER: keycloak
      POSTGRES_PASSWORD: keycloak
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - keycloak-network
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U keycloak"]
      interval: 10s
      timeout: 5s
      retries: 5

  keycloak:
    image: quay.io/keycloak/keycloak:23.0.3
    container_name: keycloak
    command: start-dev
    environment:
      # Database
      KC_DB: postgres
      KC_DB_URL: jdbc:postgresql://postgres:5432/keycloak
      KC_DB_USERNAME: keycloak
      KC_DB_PASSWORD: keycloak
      
      # Admin User
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: admin
      
      # Hostname
      KC_HOSTNAME: localhost
      KC_HOSTNAME_STRICT: false
      KC_HTTP_ENABLED: true
      
      # Logging
      KC_LOG_LEVEL: INFO
    ports:
      - "8180:8080"  # Admin Console e APIs
    depends_on:
      postgres:
        condition: service_healthy
    networks:
      - keycloak-network
    volumes:
      - ./keycloak-data:/opt/keycloak/data

volumes:
  postgres_data:

networks:
  keycloak-network:
    driver: bridge
```

### Iniciar Keycloak

```bash
# Subir containers
docker-compose up -d

# Verificar logs
docker-compose logs -f keycloak

# Verificar status
docker-compose ps
```

**Acessar Admin Console**: http://localhost:8180

- **Usuário**: `admin`
- **Senha**: `admin`

### Comandos Docker Úteis

```bash
# Parar containers
docker-compose down

# Parar e remover volumes (limpar dados)
docker-compose down -v

# Restart Keycloak
docker-compose restart keycloak

# Ver logs em tempo real
docker-compose logs -f

# Executar comando no container
docker-compose exec keycloak bash

# Verificar saúde do PostgreSQL
docker-compose exec postgres pg_isready -U keycloak
```

---

## ⚙️ Setup Quarkus + Keycloak

### Dependências Maven

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>quarkus-keycloak</artifactId>
    <version>1.0.0-SNAPSHOT</version>

    <properties>
        <quarkus.platform.version>3.6.4</quarkus.platform.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>io.quarkus.platform</groupId>
                <artifactId>quarkus-bom</artifactId>
                <version>${quarkus.platform.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <!-- Quarkus Core -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-arc</artifactId>
        </dependency>

        <!-- REST -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-resteasy-reactive-jackson</artifactId>
        </dependency>

        <!-- OIDC (OpenID Connect) -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-oidc</artifactId>
        </dependency>

        <!-- OIDC Token Propagation (para REST clients) -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-oidc-token-propagation</artifactId>
        </dependency>

        <!-- Security Annotations -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-security</artifactId>
        </dependency>

        <!-- Database -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-hibernate-orm-panache</artifactId>
        </dependency>

        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-jdbc-postgresql</artifactId>
        </dependency>

        <!-- Testing -->
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
            <artifactId>quarkus-test-security</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>io.quarkus</groupId>
                <artifactId>quarkus-maven-plugin</artifactId>
                <version>${quarkus.platform.version}</version>
                <extensions>true</extensions>
                <executions>
                    <execution>
                        <goals>
                            <goal>build</goal>
                            <goal>generate-code</goal>
                            <goal>generate-code-tests</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

### application.properties

```properties
# Application
quarkus.application.name=quarkus-keycloak-app
quarkus.http.port=8080

# Database
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=postgres
quarkus.datasource.password=postgres
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/appdb

quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.log.sql=true

# ===================================
# OIDC / Keycloak Configuration
# ===================================

# URL do Keycloak
quarkus.oidc.auth-server-url=http://localhost:8180/realms/quarkus-realm

# Client ID configurado no Keycloak
quarkus.oidc.client-id=quarkus-app

# Client Secret (se usar confidential client)
quarkus.oidc.credentials.secret=**********

# Tipo de aplicação
quarkus.oidc.application-type=service

# Token configuration
quarkus.oidc.token.issuer=http://localhost:8180/realms/quarkus-realm
quarkus.oidc.token.audience=account

# CORS (permitir chamadas do frontend)
quarkus.http.cors=true
quarkus.http.cors.origins=http://localhost:3000
quarkus.http.cors.methods=GET,POST,PUT,DELETE,OPTIONS
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with
quarkus.http.cors.exposed-headers=Content-Disposition
quarkus.http.cors.access-control-max-age=24H

# Logging
quarkus.log.level=INFO
quarkus.log.category."io.quarkus.oidc".level=DEBUG

# Dev Services (desabilitar para usar Keycloak externo)
quarkus.keycloak.devservices.enabled=false
```

### Criar Quarkus App com CLI

```bash
# Criar projeto com extensões Keycloak
quarkus create app com.example:quarkus-keycloak \
    --extension='resteasy-reactive-jackson,oidc,security,hibernate-orm-panache,jdbc-postgresql'

cd quarkus-keycloak

# Adicionar extensões adicionais
quarkus ext add oidc-token-propagation

# Executar em dev mode
quarkus dev
```

---

## 🔧 Configuração do Realm

### Criar Realm no Keycloak

1. **Acessar Admin Console**: http://localhost:8180
2. **Login**: admin / admin
3. **Criar Realm**:
   - Clicar em dropdown "master" (topo esquerdo)
   - Clicar em "Create Realm"
   - **Realm name**: `quarkus-realm`
   - **Enabled**: ON
   - Clicar em "Create"

### Configurar Client

1. **Navegar**: Clients → Create client
2. **General Settings**:
   - **Client type**: OpenID Connect
   - **Client ID**: `quarkus-app`
   - Clicar em "Next"

3. **Capability config**:
   - **Client authentication**: ON (para confidential client)
   - **Authorization**: ON
   - **Authentication flow**: 
     - ✅ Standard flow
     - ✅ Direct access grants
     - ✅ Service accounts roles
   - Clicar em "Next"

4. **Login settings**:
   - **Root URL**: `http://localhost:8080`
   - **Home URL**: `http://localhost:8080`
   - **Valid redirect URIs**: `http://localhost:8080/*`
   - **Valid post logout redirect URIs**: `http://localhost:8080`
   - **Web origins**: `http://localhost:8080`
   - Clicar em "Save"

5. **Obter Client Secret**:
   - Navegar para aba "Credentials"
   - Copiar **Client secret**
   - Adicionar em `application.properties`:
     ```properties
     quarkus.oidc.credentials.secret=SEU_CLIENT_SECRET_AQUI
     ```

### Criar Roles

1. **Realm Roles**:
   - Navegar: Realm roles → Create role
   - Criar roles:
     - **user**: Usuário comum
     - **admin**: Administrador
     - **manager**: Gerente

2. **Client Roles**:
   - Navegar: Clients → quarkus-app → Roles → Create role
   - Criar roles específicas do client

### Criar Usuários

1. **Navegar**: Users → Add user

2. **Usuário 1 - Admin**:
   ```
   Username: admin
   Email: admin@example.com
   First name: Admin
   Last name: User
   Email verified: ON
   ```
   - Clicar em "Create"
   - Aba "Credentials":
     - Set password: `admin123`
     - Temporary: OFF
   - Aba "Role mapping":
     - Assign roles: `admin`, `user`

3. **Usuário 2 - User Regular**:
   ```
   Username: john
   Email: john@example.com
   First name: John
   Last name: Doe
   Email verified: ON
   ```
   - Aba "Credentials": password = `john123`
   - Aba "Role mapping": `user`

4. **Usuário 3 - Manager**:
   ```
   Username: manager
   Email: manager@example.com
   First name: Manager
   Last name: User
   Email verified: ON
   ```
   - Aba "Credentials": password = `manager123`
   - Aba "Role mapping": `manager`, `user`

### Configurar Token Lifespan

1. **Navegar**: Realm settings → Tokens
2. **Configurar**:
   ```
   Access Token Lifespan: 15 minutes
   Access Token Lifespan For Implicit Flow: 15 minutes
   Client login timeout: 5 minutes
   Login timeout: 5 minutes
   Refresh Token Max Reuse: 0
   SSO Session Idle: 30 minutes
   SSO Session Max: 10 hours
   ```

---

## 🔐 Autenticação e Autorização

### Obter Token JWT

```bash
# Obter access token via Direct Access Grant (Resource Owner Password)
curl -X POST http://localhost:8180/realms/quarkus-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=quarkus-app" \
  -d "client_secret=SEU_CLIENT_SECRET" \
  -d "username=john" \
  -d "password=john123" \
  -d "grant_type=password"
```

**Resposta**:
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expires_in": 900,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "not-before-policy": 0,
  "session_state": "uuid-here",
  "scope": "profile email"
}
```

### Decodificar Token JWT

Acesse https://jwt.io e cole o `access_token`:

```json
{
  "exp": 1704744000,
  "iat": 1704743100,
  "jti": "uuid-here",
  "iss": "http://localhost:8180/realms/quarkus-realm",
  "aud": "account",
  "sub": "user-uuid",
  "typ": "Bearer",
  "azp": "quarkus-app",
  "session_state": "session-uuid",
  "acr": "1",
  "realm_access": {
    "roles": ["user"]
  },
  "resource_access": {
    "account": {
      "roles": ["manage-account", "view-profile"]
    }
  },
  "scope": "profile email",
  "email_verified": true,
  "name": "John Doe",
  "preferred_username": "john",
  "given_name": "John",
  "family_name": "Doe",
  "email": "john@example.com"
}
```

### Usar Token em Requisições

```bash
# Exportar token
export TOKEN="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."

# Fazer requisição autenticada
curl -X GET http://localhost:8080/api/protected \
  -H "Authorization: Bearer $TOKEN"
```

### Refresh Token

```bash
# Renovar access token usando refresh token
curl -X POST http://localhost:8180/realms/quarkus-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=quarkus-app" \
  -d "client_secret=SEU_CLIENT_SECRET" \
  -d "refresh_token=SEU_REFRESH_TOKEN" \
  -d "grant_type=refresh_token"
```

---

## 👥 Roles e Permissions

### SecurityIdentity

```java
package com.example.security;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Set;

/**
 * Resource para informações do usuário autenticado.
 */
@Path("/api/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserInfoResource {

    @Inject
    SecurityIdentity securityIdentity;

    /**
     * Retorna informações do usuário atual.
     *
     * @return dados do usuário
     */
    @GET
    @Path("/me")
    public UserInfo getCurrentUser() {
        return new UserInfo(
            securityIdentity.getPrincipal().getName(),
            securityIdentity.getRoles(),
            securityIdentity.getAttributes()
        );
    }

    /**
     * Verifica se usuário tem role específica.
     *
     * @return true se tem a role
     */
    @GET
    @Path("/is-admin")
    public boolean isAdmin() {
        return securityIdentity.hasRole("admin");
    }

    /**
     * DTO para informações do usuário.
     */
    public record UserInfo(
        String username,
        Set<String> roles,
        Map<String, Object> attributes
    ) {}
}
```

### Anotações de Segurança

```java
package com.example.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.annotation.security.DenyAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 * Resource com diferentes níveis de autorização.
 */
@Path("/api/products")
public class ProductResource {

    /**
     * Endpoint público (sem autenticação).
     */
    @GET
    @Path("/public")
    @PermitAll
    public Response getPublicProducts() {
        return Response.ok("Lista pública de produtos").build();
    }

    /**
     * Endpoint protegido (qualquer usuário autenticado).
     */
    @GET
    @Path("/all")
    @RolesAllowed({"user", "admin"})
    public Response getAllProducts() {
        return Response.ok("Todos os produtos").build();
    }

    /**
     * Endpoint apenas para admins.
     */
    @GET
    @Path("/admin")
    @RolesAllowed("admin")
    public Response getAdminProducts() {
        return Response.ok("Produtos administrativos").build();
    }

    /**
     * Endpoint negado para todos.
     */
    @GET
    @Path("/denied")
    @DenyAll
    public Response getDenied() {
        return Response.status(Response.Status.FORBIDDEN).build();
    }
    
    /**
     * Endpoint para múltiplas roles (OR logic).
     */
    @GET
    @Path("/manager-or-admin")
    @RolesAllowed({"manager", "admin"})
    public Response getManagerOrAdmin() {
        return Response.ok("Acesso para manager OU admin").build();
    }
}
```

---

## 🔒 REST API Protegida

### CRUD Completo com Autorização

```java
package com.example.resource;

import com.example.model.Product;
import com.example.service.ProductService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;

/**
 * REST API protegida para gerenciamento de produtos.
 */
@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProtectedProductResource {

    @Inject
    ProductService productService;

    @Inject
    SecurityIdentity securityIdentity;

    /**
     * Lista todos os produtos (usuários autenticados).
     *
     * @return lista de produtos
     */
    @GET
    @RolesAllowed({"user", "admin", "manager"})
    public Response findAll() {
        List<Product> products = productService.findAll();
        
        return Response.ok()
            .entity(products)
            .header("X-Authenticated-User", securityIdentity.getPrincipal().getName())
            .build();
    }

    /**
     * Busca produto por ID (usuários autenticados).
     *
     * @param id ID do produto
     * @return produto encontrado
     */
    @GET
    @Path("/{id}")
    @RolesAllowed({"user", "admin", "manager"})
    public Response findById(@PathParam("id") Long id) {
        Product product = productService.findById(id);
        
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Product not found")
                .build();
        }
        
        return Response.ok(product).build();
    }

    /**
     * Cria novo produto (apenas admin e manager).
     *
     * @param product dados do produto
     * @return produto criado
     */
    @POST
    @Transactional
    @RolesAllowed({"admin", "manager"})
    public Response create(@Valid Product product) {
        Product created = productService.create(product);
        
        return Response.status(Response.Status.CREATED)
            .entity(created)
            .build();
    }

    /**
     * Atualiza produto (apenas admin e manager).
     *
     * @param id ID do produto
     * @param product novos dados
     * @return produto atualizado
     */
    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed({"admin", "manager"})
    public Response update(@PathParam("id") Long id, @Valid Product product) {
        Product updated = productService.update(id, product);
        
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        return Response.ok(updated).build();
    }

    /**
     * Deleta produto (apenas admin).
     *
     * @param id ID do produto
     * @return resposta da operação
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("admin")
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = productService.delete(id);
        
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        return Response.noContent().build();
    }

    /**
     * Audit log - apenas admin.
     *
     * @return logs de auditoria
     */
    @GET
    @Path("/audit-log")
    @RolesAllowed("admin")
    public Response getAuditLog() {
        List<String> logs = productService.getAuditLog();
        return Response.ok(logs).build();
    }
}
```

### Service com Validação de Permissões

```java
package com.example.service;

import com.example.model.Product;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.ForbiddenException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço de produtos com lógica de negócio e auditoria.
 */
@ApplicationScoped
public class ProductService {

    @Inject
    EntityManager entityManager;

    @Inject
    SecurityIdentity securityIdentity;

    private final List<String> auditLog = new ArrayList<>();

    /**
     * Lista todos os produtos.
     *
     * @return lista de produtos
     */
    public List<Product> findAll() {
        logAudit("LIST_ALL", null);
        return Product.listAll();
    }

    /**
     * Busca produto por ID.
     *
     * @param id ID do produto
     * @return produto ou null
     */
    public Product findById(Long id) {
        logAudit("FIND_BY_ID", id);
        return Product.findById(id);
    }

    /**
     * Cria novo produto.
     *
     * @param product dados do produto
     * @return produto criado
     */
    public Product create(Product product) {
        product.setCreatedBy(getCurrentUsername());
        product.setCreatedAt(LocalDateTime.now());
        product.persist();
        
        logAudit("CREATE", product.getId());
        
        return product;
    }

    /**
     * Atualiza produto.
     *
     * @param id ID do produto
     * @param productData novos dados
     * @return produto atualizado
     */
    public Product update(Long id, Product productData) {
        Product product = Product.findById(id);
        
        if (product == null) {
            return null;
        }
        
        // Verificar se usuário pode editar
        if (!canEdit(product)) {
            throw new ForbiddenException("You can only edit your own products");
        }
        
        product.setName(productData.getName());
        product.setPrice(productData.getPrice());
        product.setDescription(productData.getDescription());
        product.setUpdatedBy(getCurrentUsername());
        product.setUpdatedAt(LocalDateTime.now());
        
        logAudit("UPDATE", id);
        
        return product;
    }

    /**
     * Deleta produto.
     *
     * @param id ID do produto
     * @return true se deletado
     */
    public boolean delete(Long id) {
        Product product = Product.findById(id);
        
        if (product == null) {
            return false;
        }
        
        product.delete();
        logAudit("DELETE", id);
        
        return true;
    }

    /**
     * Retorna audit log.
     *
     * @return lista de logs
     */
    public List<String> getAuditLog() {
        return new ArrayList<>(auditLog);
    }

    /**
     * Verifica se usuário pode editar produto.
     */
    private boolean canEdit(Product product) {
        // Admin pode editar tudo
        if (securityIdentity.hasRole("admin")) {
            return true;
        }
        
        // Manager pode editar tudo
        if (securityIdentity.hasRole("manager")) {
            return true;
        }
        
        // User só pode editar seus próprios produtos
        String currentUser = getCurrentUsername();
        return product.getCreatedBy().equals(currentUser);
    }

    /**
     * Obtém username do usuário atual.
     */
    private String getCurrentUsername() {
        return securityIdentity.getPrincipal().getName();
    }

    /**
     * Registra ação no audit log.
     */
    private void logAudit(String action, Long productId) {
        String log = String.format(
            "[%s] User: %s | Action: %s | Product ID: %s",
            LocalDateTime.now(),
            getCurrentUsername(),
            action,
            productId != null ? productId : "N/A"
        );
        auditLog.add(log);
    }
}
```

### Modelo com Auditoria

```java
package com.example.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade Product com campos de auditoria.
 */
@Entity
@Table(name = "products")
public class Product extends PanacheEntity {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(length = 500)
    private String description;

    @NotBlank(message = "Category is required")
    @Column(nullable = false, length = 50)
    private String category;

    // Auditoria
    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Product() {
    }

    public Product(String name, BigDecimal price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
```

---

## 🎫 Clientes e Tokens

### Keycloak Admin Client

```java
package com.example.client;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

/**
 * Cliente administrativo do Keycloak.
 */
@ApplicationScoped
public class KeycloakAdminClient {

    @ConfigProperty(name = "keycloak.admin.server-url")
    String serverUrl;

    @ConfigProperty(name = "keycloak.admin.realm")
    String realm;

    @ConfigProperty(name = "keycloak.admin.client-id")
    String clientId;

    @ConfigProperty(name = "keycloak.admin.username")
    String username;

    @ConfigProperty(name = "keycloak.admin.password")
    String password;

    private Keycloak keycloak;

    /**
     * Obtém instância do Keycloak.
     *
     * @return cliente Keycloak
     */
    public Keycloak getKeycloak() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .username(username)
                .password(password)
                .build();
        }
        return keycloak;
    }

    /**
     * Lista todos os usuários do realm.
     *
     * @return lista de usuários
     */
    public List<UserRepresentation> getUsers() {
        return getKeycloak()
            .realm(realm)
            .users()
            .list();
    }

    /**
     * Busca usuário por username.
     *
     * @param username username
     * @return usuário ou null
     */
    public UserRepresentation getUserByUsername(String username) {
        List<UserRepresentation> users = getKeycloak()
            .realm(realm)
            .users()
            .search(username, true);
        
        return users.isEmpty() ? null : users.get(0);
    }

    /**
     * Cria novo usuário.
     *
     * @param user dados do usuário
     */
    public void createUser(UserRepresentation user) {
        getKeycloak()
            .realm(realm)
            .users()
            .create(user);
    }

    /**
     * Deleta usuário.
     *
     * @param userId ID do usuário
     */
    public void deleteUser(String userId) {
        getKeycloak()
            .realm(realm)
            .users()
            .delete(userId);
    }
}
```

**application.properties** (adicionar):
```properties
# Keycloak Admin Client
keycloak.admin.server-url=http://localhost:8180
keycloak.admin.realm=quarkus-realm
keycloak.admin.client-id=admin-cli
keycloak.admin.username=admin
keycloak.admin.password=admin
```

### Token Introspection

```java
package com.example.resource;

import io.quarkus.oidc.OidcConfigurationMetadata;
import io.quarkus.oidc.TokenIntrospection;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Resource para informações do token.
 */
@Path("/api/token")
@Produces(MediaType.APPLICATION_JSON)
public class TokenResource {

    @Inject
    TokenIntrospection tokenIntrospection;

    @Inject
    OidcConfigurationMetadata oidcMetadata;

    /**
     * Retorna informações do token atual.
     *
     * @return dados do token
     */
    @GET
    @Path("/introspect")
    public TokenInfo introspectToken() {
        return new TokenInfo(
            tokenIntrospection.getString("preferred_username"),
            tokenIntrospection.getString("email"),
            tokenIntrospection.getBoolean("email_verified"),
            tokenIntrospection.getLong("exp"),
            tokenIntrospection.getString("scope")
        );
    }

    /**
     * Retorna metadata do OIDC.
     *
     * @return metadata
     */
    @GET
    @Path("/metadata")
    public OidcMetadataInfo getMetadata() {
        return new OidcMetadataInfo(
            oidcMetadata.getIssuer(),
            oidcMetadata.getAuthorizationUri(),
            oidcMetadata.getTokenUri(),
            oidcMetadata.getUserInfoUri(),
            oidcMetadata.getEndSessionUri()
        );
    }

    public record TokenInfo(
        String username,
        String email,
        Boolean emailVerified,
        Long expiresAt,
        String scope
    ) {}

    public record OidcMetadataInfo(
        String issuer,
        String authorizationEndpoint,
        String tokenEndpoint,
        String userinfoEndpoint,
        String endSessionEndpoint
    ) {}
}
```

---

## 👤 User Management

### Resource de Gerenciamento de Usuários

```java
package com.example.resource;

import com.example.client.KeycloakAdminClient;
import com.example.model.CreateUserRequest;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Collections;
import java.util.List;

/**
 * REST API para gerenciamento de usuários (apenas admin).
 */
@Path("/api/admin/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("admin")
public class UserManagementResource {

    @Inject
    KeycloakAdminClient keycloakAdmin;

    /**
     * Lista todos os usuários.
     *
     * @return lista de usuários
     */
    @GET
    public Response listUsers() {
        List<UserRepresentation> users = keycloakAdmin.getUsers();
        return Response.ok(users).build();
    }

    /**
     * Busca usuário por username.
     *
     * @param username username
     * @return usuário encontrado
     */
    @GET
    @Path("/{username}")
    public Response getUserByUsername(@PathParam("username") String username) {
        UserRepresentation user = keycloakAdmin.getUserByUsername(username);
        
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("User not found")
                .build();
        }
        
        return Response.ok(user).build();
    }

    /**
     * Cria novo usuário.
     *
     * @param request dados do usuário
     * @return resposta da criação
     */
    @POST
    public Response createUser(@Valid CreateUserRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEnabled(true);
        user.setEmailVerified(true);

        // Set password
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.password());
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));

        try {
            keycloakAdmin.createUser(user);
            return Response.status(Response.Status.CREATED)
                .entity("User created successfully")
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Error creating user: " + e.getMessage())
                .build();
        }
    }

    /**
     * Deleta usuário.
     *
     * @param userId ID do usuário
     * @return resposta da operação
     */
    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") String userId) {
        try {
            keycloakAdmin.deleteUser(userId);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Error deleting user: " + e.getMessage())
                .build();
        }
    }
}
```

### DTO para Criação de Usuário

```java
package com.example.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request para criação de usuário.
 */
public record CreateUserRequest(
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username,

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password,

    @NotBlank(message = "First name is required")
    String firstName,

    @NotBlank(message = "Last name is required")
    String lastName
) {}
```

---

## 🐳 Docker Compose Completo

### Stack Completo: App + Keycloak + PostgreSQL

```yaml
version: '3.8'

services:
  # PostgreSQL para Keycloak
  postgres-keycloak:
    image: postgres:16-alpine
    container_name: postgres-keycloak
    environment:
      POSTGRES_DB: keycloak
      POSTGRES_USER: keycloak
      POSTGRES_PASSWORD: keycloak
    volumes:
      - postgres-keycloak-data:/var/lib/postgresql/data
    networks:
      - app-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U keycloak"]
      interval: 10s
      timeout: 5s
      retries: 5

  # PostgreSQL para aplicação
  postgres-app:
    image: postgres:16-alpine
    container_name: postgres-app
    environment:
      POSTGRES_DB: quarkus_db
      POSTGRES_USER: quarkus
      POSTGRES_PASSWORD: quarkus
    volumes:
      - postgres-app-data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    networks:
      - app-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U quarkus"]
      interval: 10s
      timeout: 5s
      retries: 5

  # Keycloak
  keycloak:
    image: quay.io/keycloak/keycloak:23.0.3
    container_name: keycloak
    command: start-dev
    environment:
      KC_DB: postgres
      KC_DB_URL: jdbc:postgresql://postgres-keycloak:5432/keycloak
      KC_DB_USERNAME: keycloak
      KC_DB_PASSWORD: keycloak
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: admin
      KC_HEALTH_ENABLED: "true"
      KC_METRICS_ENABLED: "true"
      KC_HTTP_PORT: 8080
    ports:
      - "8180:8080"
    depends_on:
      postgres-keycloak:
        condition: service_healthy
    networks:
      - app-network
    healthcheck:
      test: ["CMD-SHELL", "exec 3<>/dev/tcp/localhost/8080 && echo -e 'GET /health/ready HTTP/1.1\\r\\nHost: localhost\\r\\n\\r\\n' >&3 && cat <&3 | grep -q '200 OK'"]
      interval: 30s
      timeout: 10s
      retries: 5
      start_period: 60s

  # Aplicação Quarkus
  quarkus-app:
    build:
      context: .
      dockerfile: src/main/docker/Dockerfile.jvm
    container_name: quarkus-app
    environment:
      QUARKUS_DATASOURCE_JDBC_URL: jdbc:postgresql://postgres-app:5432/quarkus_db
      QUARKUS_DATASOURCE_USERNAME: quarkus
      QUARKUS_DATASOURCE_PASSWORD: quarkus
      QUARKUS_OIDC_AUTH_SERVER_URL: http://keycloak:8080/realms/quarkus-realm
      QUARKUS_OIDC_CLIENT_ID: quarkus-app
      QUARKUS_OIDC_CREDENTIALS_SECRET: ${KEYCLOAK_CLIENT_SECRET}
      QUARKUS_HTTP_CORS: "true"
      QUARKUS_HTTP_CORS_ORIGINS: "http://localhost:3000,http://localhost:8080"
    ports:
      - "8080:8080"
    depends_on:
      postgres-app:
        condition: service_healthy
      keycloak:
        condition: service_healthy
    networks:
      - app-network
    healthcheck:
      test: ["CMD-SHELL", "curl -f http://localhost:8080/q/health || exit 1"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s

  # Frontend React (opcional)
  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    container_name: frontend
    environment:
      REACT_APP_API_URL: http://localhost:8080
      REACT_APP_KEYCLOAK_URL: http://localhost:8180
      REACT_APP_KEYCLOAK_REALM: quarkus-realm
      REACT_APP_KEYCLOAK_CLIENT_ID: frontend-client
    ports:
      - "3000:3000"
    depends_on:
      - quarkus-app
    networks:
      - app-network

networks:
  app-network:
    driver: bridge

volumes:
  postgres-keycloak-data:
  postgres-app-data:
```

### Dockerfile para Quarkus (JVM)

```dockerfile
####
# Dockerfile para Quarkus JVM Mode
####

FROM registry.access.redhat.com/ubi8/openjdk-17:1.18

ENV LANGUAGE='en_US:en'

# Configure the JAVA_OPTIONS
# You can add more JVM options here if needed
ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

# We make four distinct layers so if there are application changes the library layers can be re-used
COPY --chown=185 target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 target/quarkus-app/*.jar /deployments/
COPY --chown=185 target/quarkus-app/app/ /deployments/app/
COPY --chown=185 target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185

ENTRYPOINT [ "java", "-jar", "/deployments/quarkus-run.jar" ]
```

### Dockerfile para Quarkus (Native)

```dockerfile
####
# Dockerfile para Quarkus Native Mode
####

FROM registry.access.redhat.com/ubi8/ubi-minimal:8.9
WORKDIR /work/
RUN chown 1001 /work \
    && chmod "g+rwX" /work \
    && chown 1001:root /work

COPY --chown=1001:root target/*-runner /work/application

EXPOSE 8080
USER 1001

CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
```

### .env para Docker Compose

```env
# Keycloak
KEYCLOAK_CLIENT_SECRET=seu-client-secret-aqui

# PostgreSQL Keycloak
POSTGRES_KEYCLOAK_DB=keycloak
POSTGRES_KEYCLOAK_USER=keycloak
POSTGRES_KEYCLOAK_PASSWORD=keycloak

# PostgreSQL App
POSTGRES_APP_DB=quarkus_db
POSTGRES_APP_USER=quarkus
POSTGRES_APP_PASSWORD=quarkus

# Aplicação
QUARKUS_HTTP_PORT=8080
```

### Scripts de Gerenciamento

**start.sh** (Linux/Mac):
```bash
#!/bin/bash

echo "🚀 Starting full stack..."

# Build da aplicação
echo "📦 Building Quarkus application..."
./mvnw clean package -DskipTests

# Iniciar containers
echo "🐳 Starting Docker containers..."
docker-compose up -d

# Aguardar serviços ficarem prontos
echo "⏳ Waiting for services to be ready..."
sleep 30

# Verificar status
echo "✅ Checking services health..."
docker-compose ps

echo "
✅ Stack started successfully!

📍 Services:
- Keycloak:     http://localhost:8180 (admin/admin)
- Quarkus App:  http://localhost:8080
- Frontend:     http://localhost:3000
- PostgreSQL:   localhost:5432

📝 Next steps:
1. Configure Keycloak realm at http://localhost:8180
2. Get client secret from Keycloak
3. Update .env file with KEYCLOAK_CLIENT_SECRET
4. Restart app: docker-compose restart quarkus-app
"
```

**start.ps1** (Windows PowerShell):
```powershell
Write-Host "🚀 Starting full stack..." -ForegroundColor Green

# Build da aplicação
Write-Host "📦 Building Quarkus application..." -ForegroundColor Yellow
./mvnw clean package -DskipTests

# Iniciar containers
Write-Host "🐳 Starting Docker containers..." -ForegroundColor Yellow
docker-compose up -d

# Aguardar serviços ficarem prontos
Write-Host "⏳ Waiting for services to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Verificar status
Write-Host "✅ Checking services health..." -ForegroundColor Green
docker-compose ps

Write-Host "
✅ Stack started successfully!

📍 Services:
- Keycloak:     http://localhost:8180 (admin/admin)
- Quarkus App:  http://localhost:8080
- Frontend:     http://localhost:3000
- PostgreSQL:   localhost:5432

📝 Next steps:
1. Configure Keycloak realm at http://localhost:8180
2. Get client secret from Keycloak
3. Update .env file with KEYCLOAK_CLIENT_SECRET
4. Restart app: docker-compose restart quarkus-app
" -ForegroundColor Cyan
```

**stop.sh** (todos os sistemas):
```bash
#!/bin/bash

echo "🛑 Stopping full stack..."
docker-compose down

echo "🧹 Cleaning volumes (optional)..."
read -p "Do you want to remove volumes? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]
then
    docker-compose down -v
    echo "✅ Volumes removed"
fi

echo "✅ Stack stopped"
```

---

## 🧪 Testes

### Dependências de Teste

```xml
<!-- pom.xml - adicionar em <dependencies> -->

<!-- Quarkus Test -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-junit5</artifactId>
    <scope>test</scope>
</dependency>

<!-- REST Assured -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <scope>test</scope>
</dependency>

<!-- Quarkus Test Security -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-test-security</artifactId>
    <scope>test</scope>
</dependency>

<!-- Quarkus Test OIDC -->
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-test-oidc-server</artifactId>
    <scope>test</scope>
</dependency>

<!-- Mockito -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <scope>test</scope>
</dependency>
```

### Teste com @TestSecurity

```java
package com.example.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;

/**
 * Testes de segurança com @TestSecurity.
 */
@QuarkusTest
class ProductResourceTest {

    @Test
    void testPublicEndpoint() {
        given()
            .when().get("/api/products/public")
            .then()
                .statusCode(200)
                .body(containsString("Public access"));
    }

    @Test
    void testProtectedEndpointWithoutAuth() {
        given()
            .when().get("/api/products/all")
            .then()
                .statusCode(401); // Unauthorized
    }

    @Test
    @TestSecurity(user = "john", roles = {"user"})
    void testProtectedEndpointWithUserRole() {
        given()
            .when().get("/api/products/all")
            .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "john", roles = {"user"})
    void testAdminEndpointWithUserRole() {
        given()
            .when().get("/api/products/admin")
            .then()
                .statusCode(403); // Forbidden
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void testAdminEndpointWithAdminRole() {
        given()
            .when().get("/api/products/admin")
            .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "manager", roles = {"manager", "user"})
    void testCreateProductWithManagerRole() {
        String productJson = """
            {
                "name": "Test Product",
                "price": 99.99,
                "category": "Electronics"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(productJson)
            .when().post("/api/products")
            .then()
                .statusCode(201);
    }

    @Test
    @TestSecurity(user = "john", roles = {"user"})
    void testCreateProductWithUserRole() {
        String productJson = """
            {
                "name": "Test Product",
                "price": 99.99,
                "category": "Electronics"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(productJson)
            .when().post("/api/products")
            .then()
                .statusCode(403); // Forbidden
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    void testDeleteProductWithAdminRole() {
        given()
            .when().delete("/api/products/1")
            .then()
                .statusCode(204);
    }

    @Test
    @TestSecurity(user = "manager", roles = {"manager"})
    void testDeleteProductWithManagerRole() {
        given()
            .when().delete("/api/products/1")
            .then()
                .statusCode(403); // Only admin can delete
    }
}
```

### Teste com OIDC WireMock

```java
package com.example.resource;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

/**
 * Teste com WireMock para simular Keycloak.
 */
@QuarkusTest
@TestProfile(KeycloakWireMockTest.KeycloakTestProfile.class)
class KeycloakWireMockTest {

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void setup() {
        wireMockServer = new WireMockServer(8180);
        wireMockServer.start();

        // Mock OIDC configuration
        wireMockServer.stubFor(get(urlEqualTo("/realms/quarkus-realm/.well-known/openid-configuration"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "issuer": "http://localhost:8180/realms/quarkus-realm",
                        "authorization_endpoint": "http://localhost:8180/realms/quarkus-realm/protocol/openid-connect/auth",
                        "token_endpoint": "http://localhost:8180/realms/quarkus-realm/protocol/openid-connect/token",
                        "jwks_uri": "http://localhost:8180/realms/quarkus-realm/protocol/openid-connect/certs"
                    }
                    """)));

        // Mock token endpoint
        wireMockServer.stubFor(post(urlEqualTo("/realms/quarkus-realm/protocol/openid-connect/token"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "access_token": "mock-access-token",
                        "token_type": "Bearer",
                        "expires_in": 900,
                        "refresh_token": "mock-refresh-token"
                    }
                    """)));
    }

    @AfterAll
    static void tearDown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    void testObtainToken() {
        given()
            .contentType(ContentType.FORM)
            .formParam("client_id", "quarkus-app")
            .formParam("client_secret", "secret")
            .formParam("username", "john")
            .formParam("password", "john123")
            .formParam("grant_type", "password")
            .when().post("http://localhost:8180/realms/quarkus-realm/protocol/openid-connect/token")
            .then()
                .statusCode(200)
                .body("access_token", is("mock-access-token"));
    }

    /**
     * Profile de teste com WireMock.
     */
    public static class KeycloakTestProfile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of(
                "quarkus.oidc.auth-server-url", "http://localhost:8180/realms/quarkus-realm",
                "quarkus.oidc.client-id", "quarkus-app",
                "quarkus.oidc.credentials.secret", "secret"
            );
        }
    }
}
```

### Teste de Integração Completo

```java
package com.example.integration;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

/**
 * Teste de integração com Keycloak real.
 * Requer Keycloak rodando em localhost:8180.
 */
@QuarkusIntegrationTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductIntegrationTest {

    private static String adminToken;
    private static String userToken;
    private static Long createdProductId;

    @BeforeAll
    static void authenticate() {
        // Obter token de admin
        Response adminResponse = given()
            .contentType(ContentType.FORM)
            .formParam("client_id", "quarkus-app")
            .formParam("client_secret", "YOUR_CLIENT_SECRET")
            .formParam("username", "admin")
            .formParam("password", "admin123")
            .formParam("grant_type", "password")
            .when().post("http://localhost:8180/realms/quarkus-realm/protocol/openid-connect/token");

        adminToken = adminResponse.jsonPath().getString("access_token");

        // Obter token de usuário
        Response userResponse = given()
            .contentType(ContentType.FORM)
            .formParam("client_id", "quarkus-app")
            .formParam("client_secret", "YOUR_CLIENT_SECRET")
            .formParam("username", "john")
            .formParam("password", "john123")
            .formParam("grant_type", "password")
            .when().post("http://localhost:8180/realms/quarkus-realm/protocol/openid-connect/token");

        userToken = userResponse.jsonPath().getString("access_token");
    }

    @Test
    @Order(1)
    void testCreateProduct_asAdmin_shouldSucceed() {
        String productJson = """
            {
                "name": "Integration Test Product",
                "price": 149.99,
                "category": "Test Category",
                "description": "Created during integration test"
            }
            """;

        Response response = given()
            .header("Authorization", "Bearer " + adminToken)
            .contentType(ContentType.JSON)
            .body(productJson)
            .when().post("/api/products")
            .then()
                .statusCode(201)
                .body("name", is("Integration Test Product"))
                .body("price", is(149.99f))
                .extract().response();

        createdProductId = response.jsonPath().getLong("id");
    }

    @Test
    @Order(2)
    void testGetAllProducts_asUser_shouldSucceed() {
        given()
            .header("Authorization", "Bearer " + userToken)
            .when().get("/api/products")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    @Order(3)
    void testGetProductById_asUser_shouldSucceed() {
        given()
            .header("Authorization", "Bearer " + userToken)
            .when().get("/api/products/" + createdProductId)
            .then()
                .statusCode(200)
                .body("id", is(createdProductId.intValue()))
                .body("name", is("Integration Test Product"));
    }

    @Test
    @Order(4)
    void testUpdateProduct_asUser_shouldFail() {
        String updateJson = """
            {
                "name": "Updated Product",
                "price": 199.99,
                "category": "Updated Category"
            }
            """;

        given()
            .header("Authorization", "Bearer " + userToken)
            .contentType(ContentType.JSON)
            .body(updateJson)
            .when().put("/api/products/" + createdProductId)
            .then()
                .statusCode(403); // Forbidden
    }

    @Test
    @Order(5)
    void testUpdateProduct_asAdmin_shouldSucceed() {
        String updateJson = """
            {
                "name": "Updated Product",
                "price": 199.99,
                "category": "Updated Category",
                "description": "Updated during test"
            }
            """;

        given()
            .header("Authorization", "Bearer " + adminToken)
            .contentType(ContentType.JSON)
            .body(updateJson)
            .when().put("/api/products/" + createdProductId)
            .then()
                .statusCode(200)
                .body("name", is("Updated Product"))
                .body("price", is(199.99f));
    }

    @Test
    @Order(6)
    void testDeleteProduct_asUser_shouldFail() {
        given()
            .header("Authorization", "Bearer " + userToken)
            .when().delete("/api/products/" + createdProductId)
            .then()
                .statusCode(403); // Forbidden
    }

    @Test
    @Order(7)
    void testDeleteProduct_asAdmin_shouldSucceed() {
        given()
            .header("Authorization", "Bearer " + adminToken)
            .when().delete("/api/products/" + createdProductId)
            .then()
                .statusCode(204);
    }

    @Test
    @Order(8)
    void testGetDeletedProduct_shouldFail() {
        given()
            .header("Authorization", "Bearer " + adminToken)
            .when().get("/api/products/" + createdProductId)
            .then()
                .statusCode(404);
    }
}
```

### application.properties para Testes

```properties
# src/test/resources/application.properties

# Configuração de teste
quarkus.test.integration-test-profile=test

# OIDC Test
%test.quarkus.oidc.enabled=false
%test.quarkus.oidc.auth-server-url=http://localhost:8180/realms/quarkus-realm
%test.quarkus.oidc.client-id=quarkus-app
%test.quarkus.oidc.credentials.secret=test-secret

# Database H2 para testes
%test.quarkus.datasource.db-kind=h2
%test.quarkus.datasource.jdbc.url=jdbc:h2:mem:testdb
%test.quarkus.hibernate-orm.database.generation=drop-and-create
%test.quarkus.hibernate-orm.log.sql=true

# Logs
%test.quarkus.log.level=INFO
%test.quarkus.log.category."com.example".level=DEBUG
```

---

## ✅ Best Practices

### 1. Segurança de Tokens

#### ❌ Nunca Faça Isso
```javascript
// ERRADO: Armazenar token no localStorage
localStorage.setItem('token', accessToken); // Vulnerável a XSS!

// ERRADO: Token em URL
window.location.href = `/app?token=${accessToken}`; // Visível em logs!

// ERRADO: Token hardcoded
const TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."; // Nunca!
```

#### ✅ Faça Isso
```javascript
// CORRETO: Usar httpOnly cookies
document.cookie = `token=${accessToken}; Secure; HttpOnly; SameSite=Strict`;

// CORRETO: Armazenar em memória (React/Vue)
const [token, setToken] = useState(null);

// CORRETO: SessionStorage para SPA (menos persistente)
sessionStorage.setItem('token', accessToken);
```

### 2. Refresh Token Rotation

```java
package com.example.service;

import io.quarkus.oidc.TokenIntrospection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;

/**
 * Serviço para gerenciamento de refresh tokens.
 */
@ApplicationScoped
public class TokenRefreshService {

    @ConfigProperty(name = "quarkus.oidc.token.refresh-expired")
    boolean refreshExpired;

    @ConfigProperty(name = "quarkus.oidc.auth-server-url")
    String authServerUrl;

    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;

    @ConfigProperty(name = "quarkus.oidc.credentials.secret")
    String clientSecret;

    @Inject
    TokenIntrospection tokenIntrospection;

    /**
     * Verifica se token precisa ser renovado (renova 5 minutos antes de expirar).
     *
     * @param expiresAt timestamp de expiração
     * @return true se precisa renovar
     */
    public boolean shouldRefreshToken(Long expiresAt) {
        long now = Instant.now().getEpochSecond();
        long fiveMinutes = 5 * 60;
        return (expiresAt - now) < fiveMinutes;
    }

    /**
     * Renova access token usando refresh token.
     *
     * @param refreshToken refresh token
     * @return novo access token
     */
    public String refreshAccessToken(String refreshToken) {
        Client client = ClientBuilder.newClient();
        
        Form form = new Form()
            .param("grant_type", "refresh_token")
            .param("client_id", clientId)
            .param("client_secret", clientSecret)
            .param("refresh_token", refreshToken);

        try {
            var response = client.target(authServerUrl)
                .path("/protocol/openid-connect/token")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form));

            if (response.getStatus() == 200) {
                var json = response.readEntity(TokenResponse.class);
                return json.accessToken();
            }
            
            throw new RuntimeException("Failed to refresh token: " + response.getStatus());
        } finally {
            client.close();
        }
    }

    public record TokenResponse(
        String accessToken,
        String refreshToken,
        Integer expiresIn,
        String tokenType
    ) {}
}
```

### 3. CORS Seguro

```properties
# application.properties - Configuração CORS para produção

# ❌ NUNCA faça isso em produção
quarkus.http.cors.origins=*

# ✅ Especifique origins exatos
quarkus.http.cors=true
quarkus.http.cors.origins=https://app.example.com,https://admin.example.com
quarkus.http.cors.methods=GET,POST,PUT,DELETE
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with
quarkus.http.cors.exposed-headers=Content-Disposition
quarkus.http.cors.access-control-max-age=86400
quarkus.http.cors.access-control-allow-credentials=true
```

### 4. Rate Limiting

```java
package com.example.filter;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Filtro para rate limiting por usuário.
 */
@Provider
@Priority(2000)
public class RateLimitFilter implements ContainerRequestFilter {

    private static final int MAX_REQUESTS = 100;
    private static final Duration WINDOW = Duration.ofMinutes(1);
    
    private final Map<String, RequestCounter> counters = new ConcurrentHashMap<>();

    @Inject
    SecurityIdentity securityIdentity;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (securityIdentity.isAnonymous()) {
            return; // Não aplicar rate limit em endpoints públicos
        }

        String username = securityIdentity.getPrincipal().getName();
        RequestCounter counter = counters.computeIfAbsent(username, k -> new RequestCounter());

        if (!counter.allowRequest()) {
            requestContext.abortWith(
                Response.status(429) // Too Many Requests
                    .entity("Rate limit exceeded. Try again later.")
                    .header("X-RateLimit-Limit", MAX_REQUESTS)
                    .header("X-RateLimit-Remaining", 0)
                    .header("Retry-After", 60)
                    .build()
            );
        }
    }

    private static class RequestCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        private long windowStart = System.currentTimeMillis();

        synchronized boolean allowRequest() {
            long now = System.currentTimeMillis();
            
            // Resetar janela se passou o tempo
            if (now - windowStart > WINDOW.toMillis()) {
                count.set(0);
                windowStart = now;
            }

            return count.incrementAndGet() <= MAX_REQUESTS;
        }
    }
}
```

### 5. Logging Seguro

```java
package com.example.config;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Logger seguro que sanitiza informações sensíveis.
 */
@ApplicationScoped
public class SecureLogger {

    /**
     * Loga informação sanitizada (remove tokens, senhas, etc).
     *
     * @param message mensagem
     * @param args argumentos
     */
    public void info(String message, Object... args) {
        String sanitized = sanitize(String.format(message, args));
        Log.info(sanitized);
    }

    /**
     * Sanitiza string removendo informações sensíveis.
     *
     * @param input string de entrada
     * @return string sanitizada
     */
    private String sanitize(String input) {
        if (input == null) {
            return null;
        }

        return input
            // Remove tokens JWT
            .replaceAll("eyJ[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*", "***TOKEN***")
            // Remove senhas
            .replaceAll("(?i)(password|senha|secret)\\s*[:=]\\s*[^\\s,}]+", "$1=***")
            // Remove emails parcialmente
            .replaceAll("([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})", "$1@***")
            // Remove CPF/CNPJ
            .replaceAll("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", "***.***.***-**")
            .replaceAll("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", "**.***.***/****-**");
    }
}
```

### 6. Configuração de Produção

```properties
# application.properties - Profile de produção

%prod.quarkus.http.port=8080
%prod.quarkus.http.ssl-port=8443

# HTTPS obrigatório
%prod.quarkus.http.ssl.certificate.key-store-file=/path/to/keystore.p12
%prod.quarkus.http.ssl.certificate.key-store-password=${KEYSTORE_PASSWORD}
%prod.quarkus.http.insecure-requests=redirect

# OIDC
%prod.quarkus.oidc.auth-server-url=https://keycloak.production.com/realms/quarkus-realm
%prod.quarkus.oidc.client-id=quarkus-app
%prod.quarkus.oidc.credentials.secret=${KEYCLOAK_CLIENT_SECRET}
%prod.quarkus.oidc.tls.verification=required

# Token validation
%prod.quarkus.oidc.token.issuer=https://keycloak.production.com/realms/quarkus-realm
%prod.quarkus.oidc.token.audience=quarkus-app
%prod.quarkus.oidc.token.verify-access-token-with-user-info=true

# Session
%prod.quarkus.oidc.token.refresh-expired=true
%prod.quarkus.oidc.token.refresh-token-time-skew=10s

# Security Headers
%prod.quarkus.http.header."X-Frame-Options".value=DENY
%prod.quarkus.http.header."X-Content-Type-Options".value=nosniff
%prod.quarkus.http.header."X-XSS-Protection".value=1; mode=block
%prod.quarkus.http.header."Strict-Transport-Security".value=max-age=31536000; includeSubDomains
%prod.quarkus.http.header."Content-Security-Policy".value=default-src 'self'

# Logging
%prod.quarkus.log.level=WARN
%prod.quarkus.log.category."com.example".level=INFO
%prod.quarkus.log.console.json=true
```

---

## 🔧 Troubleshooting

### Erro: "401 Unauthorized"

**Problema**: Token inválido ou expirado.

```bash
# Verificar token
curl -X POST http://localhost:8180/realms/quarkus-realm/protocol/openid-connect/token/introspect \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=quarkus-app" \
  -d "client_secret=YOUR_SECRET" \
  -d "token=YOUR_TOKEN"

# Response
{
  "active": false,  # Token inválido!
  "exp": 1704744000
}
```

**Solução**:
1. Obter novo token
2. Verificar se token não expirou
3. Validar client_secret no Keycloak

### Erro: "403 Forbidden"

**Problema**: Usuário sem permissão.

```java
// Verificar roles do usuário
@GET
@Path("/debug/roles")
public Response debugRoles(@Context SecurityContext securityContext) {
    return Response.ok(Map.of(
        "principal", securityIdentity.getPrincipal().getName(),
        "roles", securityIdentity.getRoles(),
        "isAdmin", securityIdentity.hasRole("admin"),
        "isUser", securityIdentity.hasRole("user")
    )).build();
}
```

**Solução**:
1. Verificar roles no Keycloak Admin Console
2. Garantir que roles estão mapeadas no client
3. Verificar `@RolesAllowed` no endpoint

### Erro: "CORS policy"

**Problema**: Frontend não consegue acessar API.

```
Access to fetch at 'http://localhost:8080/api/products' from origin 
'http://localhost:3000' has been blocked by CORS policy
```

**Solução**:
```properties
# application.properties
quarkus.http.cors=true
quarkus.http.cors.origins=http://localhost:3000
quarkus.http.cors.methods=GET,POST,PUT,DELETE,OPTIONS
quarkus.http.cors.headers=accept,authorization,content-type
```

### Erro: "Connection refused to Keycloak"

**Problema**: Aplicação não consegue conectar ao Keycloak.

```bash
# Verificar se Keycloak está rodando
docker ps | grep keycloak

# Verificar logs do Keycloak
docker logs keycloak

# Testar conectividade
curl http://localhost:8180/realms/quarkus-realm/.well-known/openid-configuration
```

**Solução**:
1. Garantir que Keycloak está rodando
2. Verificar network no Docker Compose
3. Validar URL em `application.properties`

### Erro: "Invalid redirect URI"

**Problema**: Keycloak rejeitando redirect.

```
Invalid parameter: redirect_uri
```

**Solução** (Keycloak Admin Console):
1. Client → quarkus-app
2. Valid redirect URIs: `http://localhost:8080/*`
3. Web origins: `http://localhost:8080`
4. Salvar

### Erro: "Token signature verification failed"

**Problema**: Assinatura do token inválida.

```bash
# Verificar JWKS endpoint
curl http://localhost:8180/realms/quarkus-realm/protocol/openid-connect/certs

# Verificar issuer no token
echo "YOUR_TOKEN" | cut -d'.' -f2 | base64 -d | jq .iss
```

**Solução**:
```properties
# Garantir que issuer está correto
quarkus.oidc.token.issuer=http://localhost:8180/realms/quarkus-realm
quarkus.oidc.auth-server-url=http://localhost:8180/realms/quarkus-realm
```

### Erro: "Database connection failed"

**Problema**: Quarkus não conecta ao PostgreSQL.

```bash
# Verificar PostgreSQL
docker exec -it postgres-app psql -U quarkus -d quarkus_db -c "\dt"

# Testar conexão
docker exec -it postgres-app pg_isready -U quarkus
```

**Solução**:
```properties
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/quarkus_db
quarkus.datasource.username=quarkus
quarkus.datasource.password=quarkus
quarkus.datasource.jdbc.max-size=16
```

### Debug Mode

```properties
# Habilitar logs detalhados
quarkus.log.category."io.quarkus.oidc".level=DEBUG
quarkus.log.category."io.quarkus.security".level=DEBUG
quarkus.log.category."com.example".level=TRACE

# Logs HTTP
quarkus.http.access-log.enabled=true
quarkus.http.access-log.pattern=combined
```

---

## 📋 Referência Rápida

### Anotações de Segurança

| Anotação | Descrição | Exemplo |
|----------|-----------|---------|
| `@PermitAll` | Acesso público | `@GET @PermitAll` |
| `@RolesAllowed("role")` | Apenas role específica | `@POST @RolesAllowed("admin")` |
| `@RolesAllowed({"r1","r2"})` | Múltiplas roles (OR) | `@PUT @RolesAllowed({"admin","manager"})` |
| `@DenyAll` | Negar acesso a todos | `@DELETE @DenyAll` |
| `@Authenticated` | Qualquer usuário autenticado | `@GET @Authenticated` |

### Propriedades OIDC

| Propriedade | Descrição | Valor Padrão |
|-------------|-----------|--------------|
| `quarkus.oidc.auth-server-url` | URL do realm | - |
| `quarkus.oidc.client-id` | ID do client | - |
| `quarkus.oidc.credentials.secret` | Client secret | - |
| `quarkus.oidc.application-type` | Tipo (web-app/service/hybrid) | `service` |
| `quarkus.oidc.token.issuer` | Issuer esperado | Auto |
| `quarkus.oidc.token.audience` | Audience esperado | - |
| `quarkus.oidc.tls.verification` | Verificação TLS | `required` |

### cURL Commands

```bash
# Obter token
curl -X POST http://localhost:8180/realms/quarkus-realm/protocol/openid-connect/token \
  -d "client_id=quarkus-app" \
  -d "client_secret=SECRET" \
  -d "username=john" \
  -d "password=john123" \
  -d "grant_type=password"

# Refresh token
curl -X POST http://localhost:8180/realms/quarkus-realm/protocol/openid-connect/token \
  -d "client_id=quarkus-app" \
  -d "client_secret=SECRET" \
  -d "grant_type=refresh_token" \
  -d "refresh_token=REFRESH_TOKEN"

# Usar token
curl -H "Authorization: Bearer TOKEN" http://localhost:8080/api/protected

# Introspectar token
curl -X POST http://localhost:8180/realms/quarkus-realm/protocol/openid-connect/token/introspect \
  -d "client_id=quarkus-app" \
  -d "client_secret=SECRET" \
  -d "token=TOKEN"

# Logout
curl -X POST http://localhost:8180/realms/quarkus-realm/protocol/openid-connect/logout \
  -d "client_id=quarkus-app" \
  -d "client_secret=SECRET" \
  -d "refresh_token=REFRESH_TOKEN"
```

### Portas Padrão

| Serviço | Porta | URL |
|---------|-------|-----|
| Keycloak | 8180 | http://localhost:8180 |
| Quarkus App | 8080 | http://localhost:8080 |
| PostgreSQL Keycloak | (interno) | - |
| PostgreSQL App | 5432 | localhost:5432 |
| Frontend | 3000 | http://localhost:3000 |

### SecurityIdentity Methods

```java
// Obter username
String username = securityIdentity.getPrincipal().getName();

// Verificar role
boolean isAdmin = securityIdentity.hasRole("admin");

// Obter todas as roles
Set<String> roles = securityIdentity.getRoles();

// Verificar se é anônimo
boolean anonymous = securityIdentity.isAnonymous();

// Obter atributos
Object attribute = securityIdentity.getAttribute("email");

// Obter claim do JWT
String email = securityIdentity.<JsonWebToken>getCredential(JsonWebToken.class).getClaim("email");
```

---

## 📚 Recursos

### Documentação Oficial

- **Quarkus Security**: https://quarkus.io/guides/security
- **Quarkus OIDC**: https://quarkus.io/guides/security-oidc-code-flow-authentication
- **Keycloak Documentation**: https://www.keycloak.org/documentation
- **Keycloak Admin API**: https://www.keycloak.org/docs-api/latest/rest-api/

### Guides Quarkus

- **Security Overview**: https://quarkus.io/guides/security-overview
- **OIDC Bearer Token**: https://quarkus.io/guides/security-oidc-bearer-token-authentication
- **Testing Security**: https://quarkus.io/guides/security-testing
- **CORS Configuration**: https://quarkus.io/guides/http-reference#cors-filter

### GitHub Exemplos

- **Quarkus Quickstarts**: https://github.com/quarkusio/quarkus-quickstarts
- **Keycloak Quickstarts**: https://github.com/keycloak/keycloak-quickstarts
- **Quarkus Security Examples**: https://github.com/quarkusio/quarkus-quickstarts/tree/main/security-openid-connect-quickstart

### Keycloak Admin

- **Admin Console**: http://localhost:8180/admin (admin/admin)
- **Realm Configuration**: http://localhost:8180/admin/master/console/#/quarkus-realm
- **Account Console**: http://localhost:8180/realms/quarkus-realm/account

### Community

- **Quarkus Mailing List**: https://groups.google.com/forum/#!forum/quarkus-dev
- **Quarkus Zulip Chat**: https://quarkusio.zulipchat.com
- **Stack Overflow**: https://stackoverflow.com/questions/tagged/quarkus
- **Keycloak Users**: https://lists.jboss.org/mailman/listinfo/keycloak-user

### Videos e Tutoriais

- **Quarkus Insights**: https://www.youtube.com/c/Quarkusio
- **Keycloak Tutorials**: https://www.keycloak.org/videos
- **Red Hat Developers**: https://developers.redhat.com/topics/quarkus

### Tools

- **JWT Decoder**: https://jwt.io
- **Keycloak Docker**: https://quay.io/repository/keycloak/keycloak
- **Postman Collections**: https://www.postman.com/keycloak

---

## 📝 Licença

Este documento está sob a licença Apache 2.0.

---

**Criado com ❤️ para a comunidade Quarkus**

---

---

