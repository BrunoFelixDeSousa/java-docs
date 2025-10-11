# Kotlin com Quarkus - Guia Completo

[![Quarkus](https://img.shields.io/badge/Quarkus-3.x-blue.svg)](https://quarkus.io/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9%2B-purple.svg)](https://kotlinlang.org/)
[![Maven](https://img.shields.io/badge/Maven-3.8%2B-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

## 📋 Índice

1. [Introdução](#-introdução)
2. [Setup e Configuração](#-setup-e-configuração)
3. [REST APIs com Kotlin](#-rest-apis-com-kotlin)
4. [Data Classes e Panache](#-data-classes-e-panache)
5. [CDI e Injeção de Dependências](#-cdi-e-injeção-de-dependências)
6. [Coroutines e Reatividade](#-coroutines-e-reatividade)
7. [Extension Functions](#-extension-functions)
8. [Null Safety](#-null-safety)
9. [Testes com Kotlin](#-testes-com-kotlin)
10. [Best Practices](#-best-practices)
11. [Referência Rápida](#-referência-rápida)
12. [Recursos](#-recursos)

---

## 🎯 Introdução

### Por que Kotlin com Quarkus?

| Aspecto | Kotlin | Java |
|---------|--------|------|
| **Null Safety** | ✅ Nativo (`?`, `!!`, `?.`) | ⚠️ Requer `@Nullable` |
| **Concisão** | ✅ Data classes, properties | ❌ Getters/setters verbose |
| **Coroutines** | ✅ Nativo (suspend functions) | ⚠️ Requer bibliotecas |
| **Imutabilidade** | ✅ `val` vs `var` | ⚠️ `final` manual |
| **Interoperabilidade Java** | ✅ 100% compatível | ✅ 100% compatível |
| **Ecosystem Quarkus** | ✅ Suporte completo | ✅ Suporte completo |

### Vantagens do Kotlin

```kotlin
// ✅ Kotlin: Conciso e expressivo
data class User(
    val id: Long,
    val name: String,
    val email: String?
)

// ❌ Java: Verbose
public class User {
    private Long id;
    private String name;
    private String email;
    
    // Getters, setters, equals, hashCode, toString...
}
```

---

## ⚙️ Setup e Configuração

### Criar Projeto Quarkus com Kotlin

#### Usando Quarkus CLI

```bash
quarkus create app com.example:kotlin-quarkus \
    --extension='resteasy-reactive-jackson,hibernate-orm-panache-kotlin,jdbc-postgresql,kotlin' \
    --gradle-kotlin-dsl
```

#### Usando Maven

```bash
mvn io.quarkus:quarkus-maven-plugin:3.6.4:create \
    -DprojectGroupId=com.example \
    -DprojectArtifactId=kotlin-quarkus \
    -Dextensions="resteasy-reactive-jackson,hibernate-orm-panache-kotlin,jdbc-postgresql,kotlin"
```

### Configuração Maven (pom.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>kotlin-quarkus</artifactId>
    <version>1.0.0-SNAPSHOT</version>

    <properties>
        <compiler-plugin.version>3.11.0</compiler-plugin.version>
        <kotlin.version>1.9.21</kotlin.version>
        <quarkus.platform.version>3.6.4</quarkus.platform.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
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
        
        <!-- Quarkus RESTEasy Reactive -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-resteasy-reactive-jackson</artifactId>
        </dependency>
        
        <!-- Kotlin -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-kotlin</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-stdlib-jdk8</artifactId>
        </dependency>
        
        <!-- Hibernate ORM Panache Kotlin -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-hibernate-orm-panache-kotlin</artifactId>
        </dependency>
        
        <!-- PostgreSQL -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-jdbc-postgresql</artifactId>
        </dependency>
        
        <!-- Kotlin Coroutines -->
        <dependency>
            <groupId>org.jetbrains.kotlinx</groupId>
            <artifactId>kotlinx-coroutines-core</artifactId>
            <version>1.7.3</version>
        </dependency>
        
        <dependency>
            <groupId>org.jetbrains.kotlinx</groupId>
            <artifactId>kotlinx-coroutines-jdk8</artifactId>
            <version>1.7.3</version>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-junit5</artifactId>
            <scope>test</scope>
        </dependency>
        
        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>kotlin-extensions</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <sourceDirectory>src/main/kotlin</sourceDirectory>
        <testSourceDirectory>src/test/kotlin</testSourceDirectory>
        
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
            
            <!-- Kotlin Compiler -->
            <plugin>
                <groupId>org.jetbrains.kotlin</groupId>
                <artifactId>kotlin-maven-plugin</artifactId>
                <version>${kotlin.version}</version>
                <executions>
                    <execution>
                        <id>compile</id>
                        <goals>
                            <goal>compile</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>test-compile</id>
                        <goals>
                            <goal>test-compile</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <javaParameters>true</javaParameters>
                    <jvmTarget>17</jvmTarget>
                    <compilerPlugins>
                        <plugin>all-open</plugin>
                        <plugin>no-arg</plugin>
                    </compilerPlugins>
                    <pluginOptions>
                        <!-- Para classes CDI -->
                        <option>all-open:annotation=jakarta.enterprise.context.ApplicationScoped</option>
                        <option>all-open:annotation=jakarta.enterprise.context.RequestScoped</option>
                        <option>all-open:annotation=jakarta.ws.rs.Path</option>
                        
                        <!-- Para entidades JPA -->
                        <option>no-arg:annotation=jakarta.persistence.Entity</option>
                    </pluginOptions>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>org.jetbrains.kotlin</groupId>
                        <artifactId>kotlin-maven-allopen</artifactId>
                        <version>${kotlin.version}</version>
                    </dependency>
                    <dependency>
                        <groupId>org.jetbrains.kotlin</groupId>
                        <artifactId>kotlin-maven-noarg</artifactId>
                        <version>${kotlin.version}</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
</project>
```

### Configuração Gradle Kotlin DSL (build.gradle.kts)

```kotlin
plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.allopen") version "1.9.21"
    kotlin("plugin.noarg") version "1.9.21"
    id("io.quarkus") version "3.6.4"
}

repositories {
    mavenCentral()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-hibernate-orm-panache-kotlin")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:kotlin-extensions")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

allOpen {
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("jakarta.enterprise.context.RequestScoped")
    annotation("jakarta.ws.rs.Path")
}

noArg {
    annotation("jakarta.persistence.Entity")
}
```

### application.properties

```properties
# Database
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=postgres
quarkus.datasource.password=postgres
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/kotlindb

# Hibernate
quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.log.sql=true

# HTTP
quarkus.http.port=8080

# Dev mode
quarkus.live-reload.instrumentation=true
```

---

## 🚀 REST APIs com Kotlin

### Resource Simples

```kotlin
package com.example.resource

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

/**
 * Resource REST simples com Kotlin.
 */
@Path("/hello")
class HelloResource {
    
    /**
     * Endpoint de saudação.
     *
     * @return mensagem de boas-vindas
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): String = "Hello from Kotlin + Quarkus!"
}
```

### CRUD Completo com Data Classes

```kotlin
package com.example.model

/**
 * DTO para produto.
 *
 * Data class gera automaticamente:
 * - equals()
 * - hashCode()
 * - toString()
 * - copy()
 * - componentN() para desestruturação
 */
data class ProductDTO(
    val id: Long? = null,
    val name: String,
    val price: Double,
    val description: String? = null,
    val category: String
)

/**
 * DTO para criação de produto (sem ID).
 */
data class CreateProductDTO(
    val name: String,
    val price: Double,
    val description: String? = null,
    val category: String
)

/**
 * Response padrão da API.
 */
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
```

```kotlin
package com.example.resource

import com.example.model.ApiResponse
import com.example.model.CreateProductDTO
import com.example.model.ProductDTO
import com.example.service.ProductService
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

/**
 * REST API para gerenciamento de produtos.
 */
@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class ProductResource {
    
    @Inject
    lateinit var productService: ProductService
    
    /**
     * Lista todos os produtos.
     *
     * @return lista de produtos
     */
    @GET
    fun findAll(): Response {
        val products = productService.findAll()
        return Response.ok(
            ApiResponse(
                success = true,
                data = products
            )
        ).build()
    }
    
    /**
     * Busca produto por ID.
     *
     * @param id ID do produto
     * @return produto encontrado
     */
    @GET
    @Path("/{id}")
    fun findById(@PathParam("id") id: Long): Response {
        val product = productService.findById(id)
        
        return if (product != null) {
            Response.ok(
                ApiResponse(success = true, data = product)
            ).build()
        } else {
            Response.status(Response.Status.NOT_FOUND)
                .entity(
                    ApiResponse<ProductDTO>(
                        success = false,
                        message = "Product not found with id: $id"
                    )
                )
                .build()
        }
    }
    
    /**
     * Cria novo produto.
     *
     * @param dto dados do produto
     * @return produto criado
     */
    @POST
    @Transactional
    fun create(@Valid dto: CreateProductDTO): Response {
        val created = productService.create(dto)
        
        return Response.status(Response.Status.CREATED)
            .entity(
                ApiResponse(
                    success = true,
                    data = created,
                    message = "Product created successfully"
                )
            )
            .build()
    }
    
    /**
     * Atualiza produto existente.
     *
     * @param id ID do produto
     * @param dto novos dados
     * @return produto atualizado
     */
    @PUT
    @Path("/{id}")
    @Transactional
    fun update(
        @PathParam("id") id: Long,
        @Valid dto: ProductDTO
    ): Response {
        val updated = productService.update(id, dto)
        
        return if (updated != null) {
            Response.ok(
                ApiResponse(
                    success = true,
                    data = updated,
                    message = "Product updated successfully"
                )
            ).build()
        } else {
            Response.status(Response.Status.NOT_FOUND)
                .entity(
                    ApiResponse<ProductDTO>(
                        success = false,
                        message = "Product not found with id: $id"
                    )
                )
                .build()
        }
    }
    
    /**
     * Deleta produto.
     *
     * @param id ID do produto
     * @return resposta da operação
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    fun delete(@PathParam("id") id: Long): Response {
        val deleted = productService.delete(id)
        
        return if (deleted) {
            Response.ok(
                ApiResponse<Unit>(
                    success = true,
                    message = "Product deleted successfully"
                )
            ).build()
        } else {
            Response.status(Response.Status.NOT_FOUND)
                .entity(
                    ApiResponse<Unit>(
                        success = false,
                        message = "Product not found with id: $id"
                    )
                )
                .build()
        }
    }
    
    /**
     * Busca produtos por categoria.
     *
     * @param category categoria
     * @return produtos da categoria
     */
    @GET
    @Path("/category/{category}")
    fun findByCategory(@PathParam("category") category: String): Response {
        val products = productService.findByCategory(category)
        
        return Response.ok(
            ApiResponse(
                success = true,
                data = products
            )
        ).build()
    }
    
    /**
     * Busca produtos por range de preço.
     *
     * @param min preço mínimo
     * @param max preço máximo
     * @return produtos no range
     */
    @GET
    @Path("/price-range")
    fun findByPriceRange(
        @QueryParam("min") min: Double,
        @QueryParam("max") max: Double
    ): Response {
        val products = productService.findByPriceRange(min, max)
        
        return Response.ok(
            ApiResponse(
                success = true,
                data = products
            )
        ).build()
    }
}
```

---

## 💾 Data Classes e Panache

### Entidade Panache Kotlin

```kotlin
package com.example.entity

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

/**
 * Entidade Product usando Panache Kotlin.
 *
 * PanacheEntity fornece:
 * - Campo id automático
 * - Métodos find*, list*, stream*, count, delete
 */
@Entity
@Table(name = "products")
class Product : PanacheEntity() {
    
    @Column(nullable = false)
    lateinit var name: String
    
    @Column(nullable = false)
    var price: Double = 0.0
    
    @Column(length = 1000)
    var description: String? = null
    
    @Column(nullable = false)
    lateinit var category: String
    
    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
    
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
    
    companion object {
        /**
         * Busca produtos por categoria.
         *
         * @param category categoria
         * @return lista de produtos
         */
        fun findByCategory(category: String): List<Product> =
            list("category", category)
        
        /**
         * Busca produtos por range de preço.
         *
         * @param min preço mínimo
         * @param max preço máximo
         * @return lista de produtos
         */
        fun findByPriceRange(min: Double, max: Double): List<Product> =
            list("price >= ?1 and price <= ?2", min, max)
        
        /**
         * Busca produtos por nome (case-insensitive).
         *
         * @param name nome
         * @return lista de produtos
         */
        fun searchByName(name: String): List<Product> =
            list("LOWER(name) LIKE LOWER(?1)", "%$name%")
        
        /**
         * Conta produtos por categoria.
         *
         * @param category categoria
         * @return quantidade de produtos
         */
        fun countByCategory(category: String): Long =
            count("category", category)
        
        /**
         * Deleta produtos por categoria.
         *
         * @param category categoria
         * @return número de produtos deletados
         */
        fun deleteByCategory(category: String): Long =
            delete("category", category)
    }
}
```

### Repository Pattern com Panache

```kotlin
package com.example.repository

import com.example.entity.Product
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

/**
 * Repository para Product (padrão Repository).
 *
 * Alternativa ao Active Record do PanacheEntity.
 */
@ApplicationScoped
class ProductRepository : PanacheRepository<Product> {
    
    /**
     * Busca produtos ativos (preço > 0).
     *
     * @return produtos ativos
     */
    fun findActive(): List<Product> =
        list("price > 0")
    
    /**
     * Busca produtos caros (preço > limite).
     *
     * @param priceLimit limite de preço
     * @return produtos caros
     */
    fun findExpensive(priceLimit: Double): List<Product> =
        find("price > ?1", priceLimit).list()
    
    /**
     * Busca produtos com descrição.
     *
     * @return produtos com descrição
     */
    fun findWithDescription(): List<Product> =
        list("description IS NOT NULL")
}
```

### Service Layer

```kotlin
package com.example.service

import com.example.entity.Product
import com.example.model.CreateProductDTO
import com.example.model.ProductDTO
import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDateTime

/**
 * Serviço de negócio para produtos.
 */
@ApplicationScoped
class ProductService {
    
    /**
     * Lista todos os produtos.
     *
     * @return lista de DTOs
     */
    fun findAll(): List<ProductDTO> =
        Product.listAll()
            .map { it.toDTO() }
    
    /**
     * Busca produto por ID.
     *
     * @param id ID do produto
     * @return DTO do produto ou null
     */
    fun findById(id: Long): ProductDTO? =
        Product.findById(id)?.toDTO()
    
    /**
     * Cria novo produto.
     *
     * @param dto dados do produto
     * @return produto criado
     */
    fun create(dto: CreateProductDTO): ProductDTO {
        val product = Product().apply {
            name = dto.name
            price = dto.price
            description = dto.description
            category = dto.category
            createdAt = LocalDateTime.now()
        }
        
        product.persist()
        return product.toDTO()
    }
    
    /**
     * Atualiza produto.
     *
     * @param id ID do produto
     * @param dto novos dados
     * @return produto atualizado ou null
     */
    fun update(id: Long, dto: ProductDTO): ProductDTO? {
        val product = Product.findById(id) ?: return null
        
        product.apply {
            name = dto.name
            price = dto.price
            description = dto.description
            category = dto.category
            updatedAt = LocalDateTime.now()
        }
        
        return product.toDTO()
    }
    
    /**
     * Deleta produto.
     *
     * @param id ID do produto
     * @return true se deletado
     */
    fun delete(id: Long): Boolean =
        Product.deleteById(id)
    
    /**
     * Busca por categoria.
     *
     * @param category categoria
     * @return lista de produtos
     */
    fun findByCategory(category: String): List<ProductDTO> =
        Product.findByCategory(category)
            .map { it.toDTO() }
    
    /**
     * Busca por range de preço.
     *
     * @param min preço mínimo
     * @param max preço máximo
     * @return lista de produtos
     */
    fun findByPriceRange(min: Double, max: Double): List<ProductDTO> =
        Product.findByPriceRange(min, max)
            .map { it.toDTO() }
    
    /**
     * Converte entidade para DTO.
     */
    private fun Product.toDTO(): ProductDTO =
        ProductDTO(
            id = id,
            name = name,
            price = price,
            description = description,
            category = category
        )
}
```

---

## 🔌 CDI e Injeção de Dependências

### Scopes e Beans

```kotlin
package com.example.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.config.inject.ConfigProperty

/**
 * Bean Application Scoped (singleton).
 */
@ApplicationScoped
class CacheService {
    
    private val cache = mutableMapOf<String, Any>()
    
    /**
     * Adiciona item ao cache.
     */
    fun put(key: String, value: Any) {
        cache[key] = value
    }
    
    /**
     * Recupera item do cache.
     */
    fun get(key: String): Any? = cache[key]
    
    /**
     * Remove item do cache.
     */
    fun remove(key: String) {
        cache.remove(key)
    }
}

/**
 * Bean Request Scoped (nova instância por requisição).
 */
@RequestScoped
class RequestContext {
    
    var userId: Long? = null
    var correlationId: String? = null
    var startTime: Long = System.currentTimeMillis()
    
    /**
     * Calcula tempo de execução da requisição.
     */
    fun getExecutionTime(): Long =
        System.currentTimeMillis() - startTime
}

/**
 * Serviço que usa injeção de dependências.
 */
@ApplicationScoped
class BusinessService {
    
    @Inject
    lateinit var cacheService: CacheService
    
    @Inject
    lateinit var requestContext: RequestContext
    
    @ConfigProperty(name = "app.feature.enabled", defaultValue = "false")
    lateinit var featureEnabled: String
    
    /**
     * Processa operação com cache.
     */
    fun processWithCache(key: String, operation: () -> String): String {
        // Verificar cache
        val cached = cacheService.get(key)
        if (cached != null) {
            return cached as String
        }
        
        // Executar operação
        val result = operation()
        
        // Salvar no cache
        cacheService.put(key, result)
        
        return result
    }
}
```

### Producers e Qualifiers

```kotlin
package com.example.config

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import jakarta.inject.Named
import java.time.Clock
import java.time.ZoneId

/**
 * Producers de beans customizados.
 */
@ApplicationScoped
class BeanProducers {
    
    /**
     * Produz Clock UTC.
     */
    @Produces
    @Named("utcClock")
    fun utcClock(): Clock = Clock.systemUTC()
    
    /**
     * Produz Clock com timezone de São Paulo.
     */
    @Produces
    @Named("saoPauloClock")
    fun saoPauloClock(): Clock = 
        Clock.system(ZoneId.of("America/Sao_Paulo"))
}

/**
 * Uso dos Producers.
 */
@ApplicationScoped
class TimeService {
    
    @Inject
    @Named("utcClock")
    lateinit var utcClock: Clock
    
    @Inject
    @Named("saoPauloClock")
    lateinit var saoPauloClock: Clock
    
    /**
     * Retorna timestamp UTC.
     */
    fun getUtcTimestamp(): Long = utcClock.instant().toEpochMilli()
    
    /**
     * Retorna timestamp de São Paulo.
     */
    fun getSaoPauloTimestamp(): Long = saoPauloClock.instant().toEpochMilli()
}
```

---

## ⚡ Coroutines e Reatividade

### Suspend Functions com Quarkus

```kotlin
package com.example.service

import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.*
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.time.Duration

/**
 * Serviço assíncrono com Coroutines.
 */
@ApplicationScoped
class AsyncService {
    
    /**
     * Operação assíncrona simples.
     */
    suspend fun fetchData(id: Long): String = withContext(Dispatchers.IO) {
        // Simula chamada HTTP/DB
        delay(1000)
        "Data for ID: $id"
    }
    
    /**
     * Múltiplas operações em paralelo.
     */
    suspend fun fetchMultiple(ids: List<Long>): List<String> = coroutineScope {
        ids.map { id ->
            async {
                fetchData(id)
            }
        }.awaitAll()
    }
    
    /**
     * Operação com timeout.
     */
    suspend fun fetchWithTimeout(id: Long): String? =
        withTimeoutOrNull(5000) {
            fetchData(id)
        }
    
    /**
     * Chamadas sequenciais vs paralelas.
     */
    suspend fun compareExecutionTime() {
        val ids = listOf(1L, 2L, 3L, 4L, 5L)
        
        // Sequencial
        val sequentialTime = measureTimeMillis {
            ids.forEach { fetchData(it) }
        }
        
        // Paralelo
        val parallelTime = measureTimeMillis {
            fetchMultiple(ids)
        }
        
        println("Sequential: ${sequentialTime}ms")
        println("Parallel: ${parallelTime}ms")
    }
}

/**
 * Medição de tempo suspensa.
 */
suspend inline fun <T> measureTimeMillis(block: suspend () -> T): Long {
    val start = System.currentTimeMillis()
    block()
    return System.currentTimeMillis() - start
}
```

### REST Resource com Coroutines

```kotlin
package com.example.resource

import com.example.service.AsyncService
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking

/**
 * Resource assíncrono com Coroutines.
 */
@Path("/api/async")
@Produces(MediaType.APPLICATION_JSON)
class AsyncResource {
    
    @Inject
    lateinit var asyncService: AsyncService
    
    /**
     * Endpoint assíncrono com Uni (Mutiny).
     */
    @GET
    @Path("/data/{id}")
    fun getData(@PathParam("id") id: Long): Uni<String> =
        Uni.createFrom().item {
            runBlocking {
                asyncService.fetchData(id)
            }
        }
    
    /**
     * Endpoint com múltiplas chamadas paralelas.
     */
    @POST
    @Path("/batch")
    @Consumes(MediaType.APPLICATION_JSON)
    fun getBatch(ids: List<Long>): Uni<List<String>> =
        Uni.createFrom().item {
            runBlocking {
                asyncService.fetchMultiple(ids)
            }
        }
    
    /**
     * Endpoint com timeout.
     */
    @GET
    @Path("/with-timeout/{id}")
    fun getWithTimeout(@PathParam("id") id: Long): Uni<String?> =
        Uni.createFrom().item {
            runBlocking {
                asyncService.fetchWithTimeout(id)
            }
        }
}
```

---

## 🔧 Extension Functions

### Extensions Úteis

```kotlin
package com.example.extensions

import jakarta.ws.rs.core.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Extension functions para tipos comuns.
 */

/**
 * Converte String para LocalDateTime.
 */
fun String.toLocalDateTime(pattern: String = "yyyy-MM-dd'T'HH:mm:ss"): LocalDateTime =
    LocalDateTime.parse(this, DateTimeFormatter.ofPattern(pattern))

/**
 * Converte LocalDateTime para String.
 */
fun LocalDateTime.toFormattedString(pattern: String = "yyyy-MM-dd HH:mm:ss"): String =
    this.format(DateTimeFormatter.ofPattern(pattern))

/**
 * Valida se String é email válido.
 */
fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return emailRegex.matches(this)
}

/**
 * Trunca String com reticências.
 */
fun String.truncate(maxLength: Int): String =
    if (this.length <= maxLength) this
    else "${this.take(maxLength - 3)}..."

/**
 * Extension para Response Builder.
 */
fun Response.ResponseBuilder.addCorsHeaders(): Response.ResponseBuilder =
    this.header("Access-Control-Allow-Origin", "*")
        .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        .header("Access-Control-Allow-Headers", "Content-Type, Authorization")

/**
 * Cria Response de sucesso.
 */
fun <T> T.toSuccessResponse(): Response =
    Response.ok(this).build()

/**
 * Cria Response de erro.
 */
fun String.toErrorResponse(status: Response.Status = Response.Status.BAD_REQUEST): Response =
    Response.status(status)
        .entity(mapOf("error" to this))
        .build()

/**
 * Extension para List: agrupa e conta.
 */
fun <T> List<T>.countByGroup(selector: (T) -> String): Map<String, Int> =
    this.groupBy(selector)
        .mapValues { it.value.size }

/**
 * Extension para Map: inverte chave-valor.
 */
fun <K, V> Map<K, V>.invert(): Map<V, K> =
    this.entries.associate { (k, v) -> v to k }
```

### Uso das Extensions

```kotlin
package com.example.service

import com.example.extensions.*
import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDateTime

/**
 * Serviço que usa extension functions.
 */
@ApplicationScoped
class ExtensionExampleService {
    
    /**
     * Valida e formata email.
     */
    fun validateAndFormatEmail(email: String): String? =
        if (email.isValidEmail()) email.lowercase()
        else null
    
    /**
     * Formata data para exibição.
     */
    fun formatDate(date: LocalDateTime): String =
        date.toFormattedString("dd/MM/yyyy HH:mm")
    
    /**
     * Cria descrição curta.
     */
    fun createShortDescription(text: String, maxLength: Int = 100): String =
        text.truncate(maxLength)
    
    /**
     * Analisa lista de produtos.
     */
    fun analyzeProducts(products: List<String>): Map<String, Int> =
        products.countByGroup { it.first().toString() } // Agrupa por primeira letra
}
```

---

## 🛡️ Null Safety

### Tratamento de Nulidade

```kotlin
package com.example.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.NotFoundException

/**
 * Demonstração de Null Safety do Kotlin.
 */
@ApplicationScoped
class NullSafetyService {
    
    /**
     * Tipos nullable vs non-nullable.
     */
    fun nullSafetyExample() {
        // Tipo non-nullable
        val name: String = "John"  // ✅ OK
        // val name: String = null // ❌ Compilation error
        
        // Tipo nullable
        val nullableName: String? = null  // ✅ OK
        
        // Safe call operator
        val length = nullableName?.length  // length é Int? (null se nullableName for null)
        
        // Elvis operator
        val lengthOrZero = nullableName?.length ?: 0  // 0 se null
        
        // Not-null assertion
        // val definitelyNotNull = nullableName!!.length  // ⚠️ NullPointerException se null
    }
    
    /**
     * Busca usuário com null safety.
     */
    fun findUser(id: Long): User? {
        // Simula busca no banco
        return if (id > 0) User(id, "User $id") else null
    }
    
    /**
     * Processa usuário com safe call.
     */
    fun processUser(id: Long): String {
        val user = findUser(id)
        
        // Safe call chain
        return user?.name?.uppercase() ?: "USER NOT FOUND"
    }
    
    /**
     * Let function para executar bloco apenas se não-null.
     */
    fun processUserWithLet(id: Long): String? {
        val user = findUser(id)
        
        return user?.let {
            "Processing user: ${it.name}"
        }
    }
    
    /**
     * Also function para side effects.
     */
    fun saveUser(user: User?): User? =
        user?.also {
            println("Saving user: ${it.name}")
            // Lógica de persistência
        }
    
    /**
     * RequireNotNull para validação.
     */
    fun validateAndProcess(input: String?): String {
        val validInput = requireNotNull(input) { "Input cannot be null" }
        return validInput.uppercase()
    }
    
    /**
     * CheckNotNull para validação.
     */
    fun checkAndProcess(input: String?): String {
        val validInput = checkNotNull(input) { "Input must not be null" }
        return validInput.uppercase()
    }
    
    /**
     * Elvis with throw.
     */
    fun findUserOrThrow(id: Long): User =
        findUser(id) ?: throw NotFoundException("User not found with id: $id")
}

/**
 * Classe de exemplo.
 */
data class User(
    val id: Long,
    val name: String
)
```

### Smart Casts

```kotlin
package com.example.service

import jakarta.enterprise.context.ApplicationScoped

/**
 * Demonstração de Smart Casts.
 */
@ApplicationScoped
class SmartCastService {
    
    /**
     * Smart cast após null check.
     */
    fun processValue(value: Any?): String {
        // Após null check, Kotlin faz smart cast automaticamente
        if (value != null) {
            return value.toString()  // value é smart cast para Any (non-null)
        }
        return "null value"
    }
    
    /**
     * Smart cast com is.
     */
    fun describeValue(value: Any): String =
        when (value) {
            is String -> "String with ${value.length} characters"  // Smart cast para String
            is Int -> "Integer: $value"  // Smart cast para Int
            is List<*> -> "List with ${value.size} elements"  // Smart cast para List
            else -> "Unknown type: ${value::class.simpleName}"
        }
    
    /**
     * Smart cast em expressões booleanas.
     */
    fun validateAndProcess(input: String?): String? {
        // AND: se primeiro é false, segundo não executa
        if (input != null && input.isNotEmpty()) {
            return input.uppercase()  // input é smart cast para String
        }
        return null
    }
}
```

---

## 🧪 Testes com Kotlin

### Testes JUnit 5

```kotlin
package com.example.resource

import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import jakarta.ws.rs.core.MediaType

/**
 * Testes para ProductResource.
 */
@QuarkusTest
class ProductResourceTest {
    
    @Test
    @DisplayName("Deve listar todos os produtos")
    fun testFindAll() {
        Given {
            // Setup
        } When {
            get("/api/products")
        } Then {
            statusCode(200)
            contentType(MediaType.APPLICATION_JSON)
            body("success", equalTo(true))
            body("data", notNullValue())
        }
    }
    
    @Test
    @DisplayName("Deve criar produto")
    fun testCreate() {
        val newProduct = """
            {
                "name": "Laptop",
                "price": 2999.99,
                "description": "High-end laptop",
                "category": "Electronics"
            }
        """.trimIndent()
        
        Given {
            contentType(MediaType.APPLICATION_JSON)
            body(newProduct)
        } When {
            post("/api/products")
        } Then {
            statusCode(201)
            body("success", equalTo(true))
            body("data.name", equalTo("Laptop"))
            body("data.price", equalTo(2999.99f))
        }
    }
    
    @Test
    @DisplayName("Deve buscar produto por ID")
    fun testFindById() {
        // Criar produto primeiro
        val productId = createTestProduct()
        
        Given {
            // Setup
        } When {
            get("/api/products/$productId")
        } Then {
            statusCode(200)
            body("success", equalTo(true))
            body("data.id", equalTo(productId.toInt()))
        }
    }
    
    @Test
    @DisplayName("Deve retornar 404 para produto inexistente")
    fun testFindByIdNotFound() {
        Given {
            // Setup
        } When {
            get("/api/products/99999")
        } Then {
            statusCode(404)
            body("success", equalTo(false))
            body("message", containsString("not found"))
        }
    }
    
    @Test
    @DisplayName("Deve atualizar produto")
    fun testUpdate() {
        val productId = createTestProduct()
        
        val updatedProduct = """
            {
                "name": "Updated Product",
                "price": 1999.99,
                "category": "Electronics"
            }
        """.trimIndent()
        
        Given {
            contentType(MediaType.APPLICATION_JSON)
            body(updatedProduct)
        } When {
            put("/api/products/$productId")
        } Then {
            statusCode(200)
            body("success", equalTo(true))
            body("data.name", equalTo("Updated Product"))
        }
    }
    
    @Test
    @DisplayName("Deve deletar produto")
    fun testDelete() {
        val productId = createTestProduct()
        
        Given {
            // Setup
        } When {
            delete("/api/products/$productId")
        } Then {
            statusCode(200)
            body("success", equalTo(true))
        }
        
        // Verificar que foi deletado
        Given {
            // Setup
        } When {
            get("/api/products/$productId")
        } Then {
            statusCode(404)
        }
    }
    
    @Test
    @DisplayName("Deve buscar por categoria")
    fun testFindByCategory() {
        createTestProduct(category = "Books")
        createTestProduct(category = "Books")
        
        Given {
            // Setup
        } When {
            get("/api/products/category/Books")
        } Then {
            statusCode(200)
            body("success", equalTo(true))
            body("data.size()", greaterThan(0))
        }
    }
    
    /**
     * Helper para criar produto de teste.
     */
    private fun createTestProduct(
        name: String = "Test Product",
        price: Double = 99.99,
        category: String = "Test"
    ): Long {
        val product = """
            {
                "name": "$name",
                "price": $price,
                "category": "$category"
            }
        """.trimIndent()
        
        return Given {
            contentType(MediaType.APPLICATION_JSON)
            body(product)
        } When {
            post("/api/products")
        } Then {
            statusCode(201)
        } Extract {
            path<Int>("data.id").toLong()
        }
    }
}
```

### Testes de Integração

```kotlin
package com.example.service

import com.example.entity.Product
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

/**
 * Testes de integração para ProductService.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ProductServiceTest {
    
    @Inject
    lateinit var productService: ProductService
    
    @BeforeEach
    @Transactional
    fun setup() {
        // Limpar banco antes de cada teste
        Product.deleteAll()
    }
    
    @Test
    @Order(1)
    @DisplayName("Deve criar produto")
    @Transactional
    fun testCreate() {
        val dto = CreateProductDTO(
            name = "Test Product",
            price = 99.99,
            category = "Test"
        )
        
        val created = productService.create(dto)
        
        assertNotNull(created.id)
        assertEquals("Test Product", created.name)
        assertEquals(99.99, created.price)
    }
    
    @Test
    @Order(2)
    @DisplayName("Deve buscar todos os produtos")
    @Transactional
    fun testFindAll() {
        // Criar alguns produtos
        repeat(3) { i ->
            productService.create(
                CreateProductDTO(
                    name = "Product $i",
                    price = 10.0 * i,
                    category = "Test"
                )
            )
        }
        
        val products = productService.findAll()
        
        assertEquals(3, products.size)
    }
    
    @Test
    @Order(3)
    @DisplayName("Deve buscar produto por ID")
    @Transactional
    fun testFindById() {
        val created = productService.create(
            CreateProductDTO(
                name = "Find Me",
                price = 50.0,
                category = "Test"
            )
        )
        
        val found = productService.findById(created.id!!)
        
        assertNotNull(found)
        assertEquals("Find Me", found?.name)
    }
    
    @Test
    @Order(4)
    @DisplayName("Deve retornar null para ID inexistente")
    fun testFindByIdNotFound() {
        val found = productService.findById(99999L)
        
        assertNull(found)
    }
    
    @Test
    @Order(5)
    @DisplayName("Deve buscar por categoria")
    @Transactional
    fun testFindByCategory() {
        // Criar produtos de diferentes categorias
        productService.create(
            CreateProductDTO("Book 1", 20.0, category = "Books")
        )
        productService.create(
            CreateProductDTO("Book 2", 30.0, category = "Books")
        )
        productService.create(
            CreateProductDTO("Laptop", 1000.0, category = "Electronics")
        )
        
        val books = productService.findByCategory("Books")
        
        assertEquals(2, books.size)
        assertTrue(books.all { it.category == "Books" })
    }
    
    @Test
    @Order(6)
    @DisplayName("Deve buscar por range de preço")
    @Transactional
    fun testFindByPriceRange() {
        productService.create(CreateProductDTO("Cheap", 10.0, category = "Test"))
        productService.create(CreateProductDTO("Medium", 50.0, category = "Test"))
        productService.create(CreateProductDTO("Expensive", 100.0, category = "Test"))
        
        val mediumPriced = productService.findByPriceRange(25.0, 75.0)
        
        assertEquals(1, mediumPriced.size)
        assertEquals("Medium", mediumPriced.first().name)
    }
}
```

---

## ✅ Best Practices

### 1. Use Data Classes para DTOs

```kotlin
// ✅ Data class - conciso e gera métodos automaticamente
data class UserDTO(
    val id: Long?,
    val name: String,
    val email: String
)

// ❌ Classe normal - verbose
class UserDTO {
    var id: Long? = null
    var name: String = ""
    var email: String = ""
    
    // equals, hashCode, toString manualmente...
}
```

### 2. Prefira Imutabilidade (val)

```kotlin
// ✅ Imutável com val
val userName = "John"
val userList = listOf("A", "B", "C")

// ❌ Mutável com var (use apenas quando necessário)
var counter = 0
val mutableList = mutableListOf("A", "B")
```

### 3. Use Named Arguments

```kotlin
// ✅ Named arguments - código mais legível
createUser(
    name = "John Doe",
    email = "john@example.com",
    age = 30
)

// ❌ Argumentos posicionais - difícil de entender
createUser("John Doe", "john@example.com", 30)
```

### 4. Scope Functions

```kotlin
// ✅ apply para configuração
val product = Product().apply {
    name = "Laptop"
    price = 2999.99
    category = "Electronics"
}

// ✅ let para transformação
val result = user?.let {
    it.name.uppercase()
}

// ✅ also para side effects
val saved = product.also {
    logger.info("Saving product: ${it.name}")
}

// ✅ run para executar bloco
val result = run {
    val a = 10
    val b = 20
    a + b
}
```

### 5. When Expression

```kotlin
// ✅ When como expression (retorna valor)
val description = when (product.category) {
    "Electronics" -> "Tech product"
    "Books" -> "Reading material"
    "Clothing" -> "Apparel"
    else -> "Other"
}

// ✅ When com smart cast
fun describe(obj: Any): String = when (obj) {
    is String -> "String: ${obj.length} chars"
    is Int -> "Number: $obj"
    is List<*> -> "List: ${obj.size} items"
    else -> "Unknown"
}
```

### 6. Extension Functions para Código Limpo

```kotlin
// ✅ Extension function
fun String.isValidEmail(): Boolean =
    matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex())

// Uso
if (email.isValidEmail()) {
    // Processar
}

// ❌ Função utilitária separada
object EmailUtils {
    fun isValid(email: String): Boolean = // ...
}

if (EmailUtils.isValid(email)) { // Menos idiomático
    // Processar
}
```

---

## 📊 Referência Rápida

### Anotações Kotlin + Quarkus

| Anotação | Uso |
|----------|-----|
| `@ApplicationScoped` | Bean singleton |
| `@RequestScoped` | Bean por requisição |
| `@Inject` | Injeção de dependência |
| `@Path` | Endpoint REST |
| `@GET`, `@POST`, `@PUT`, `@DELETE` | Métodos HTTP |
| `@Transactional` | Transação JPA |
| `@ConfigProperty` | Propriedade de configuração |

### Operadores Kotlin Úteis

| Operador | Descrição | Exemplo |
|----------|-----------|---------|
| `?.` | Safe call | `user?.name` |
| `?:` | Elvis (default) | `name ?: "Unknown"` |
| `!!` | Not-null assertion | `user!!.name` |
| `as?` | Safe cast | `obj as? String` |
| `in` | Range check | `x in 1..10` |
| `..` | Range | `1..100` |

### Scope Functions

| Função | Retorna | Uso | Exemplo |
|--------|---------|-----|---------|
| `let` | Lambda result | Transformação | `user?.let { it.name }` |
| `run` | Lambda result | Executar bloco | `run { a + b }` |
| `apply` | Objeto | Configuração | `User().apply { name = "John" }` |
| `also` | Objeto | Side effects | `user.also { log(it) }` |
| `with` | Lambda result | Múltiplas chamadas | `with(user) { name }` |

---

## 📚 Recursos

### Documentação Oficial

- [Quarkus with Kotlin](https://quarkus.io/guides/kotlin)
- [Kotlin Language](https://kotlinlang.org/docs/home.html)
- [Panache Kotlin](https://quarkus.io/guides/hibernate-orm-panache-kotlin)

### Repositórios

- [Quarkus Quickstarts](https://github.com/quarkusio/quarkus-quickstarts)
- [Kotlin Examples](https://github.com/JetBrains/kotlin-examples)

### Plugins Maven/Gradle

```xml
<!-- Maven -->
<plugin>
    <groupId>org.jetbrains.kotlin</groupId>
    <artifactId>kotlin-maven-plugin</artifactId>
    <version>1.9.21</version>
</plugin>
```

```kotlin
// Gradle
plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.allopen") version "1.9.21"
}
```

---

## 🎯 Resumo

### Principais Pontos

1. **Setup**: Kotlin funciona perfeitamente com Quarkus (Maven ou Gradle)
2. **Data Classes**: Reduzem boilerplate para DTOs e entidades
3. **Null Safety**: Sistema de tipos previne NullPointerException
4. **Panache Kotlin**: API idiomática para persistência
5. **Coroutines**: Programação assíncrona elegante
6. **Extension Functions**: Código mais limpo e expressivo
7. **Interoperabilidade**: 100% compatível com bibliotecas Java
8. **Testes**: Rest-Assured Kotlin extensions para testes idiomáticos

### Quando Usar Kotlin com Quarkus

✅ **Use Kotlin quando**:
- Quer código mais conciso e expressivo
- Precisa de null safety nativo
- Quer usar coroutines para async
- Equipe já conhece Kotlin
- Projeto novo (sem legado Java)

⚠️ **Considere Java quando**:
- Equipe não conhece Kotlin
- Projeto legado grande em Java
- Ferramentas específicas de Java necessárias

---

**Voltar para**: [📁 Quarkus Guides](../README.md) | [📁 Quarkus](../../README.md) | [📁 Frameworks](../../../README.md)
