# Changelog - Instruções Java

## [2.0.0] - 2024-09-29

### 🚀 Atual##### 4. Novos Arquivos Criados

###### `graalvm.instructions.md` ⭐ **NOVO**
Documento dedicado ao GraalVM:

**Conteúdo:**
- Setup e instalação do GraalVM
- Configuração Maven para Native Image
- Reflection e configurações de metadata
- Profile-Guided Optimization (PGO)
- Docker multi-stage com GraalVM
- Testing em modo nativo
- Troubleshooting e soluções comuns
- Comparação JVM vs Native Image
- Best practices específicas do GraalVM

###### `java21-features.instructions.md` ⭐ **NOVO**ção Maior: Java 17+ → GraalVM JDK 21+

#### Mudanças Principais

##### 1. Atualização de Versão e Distribuição
- **ANTES**: Java 17+ (distribuição genérica)
- **DEPOIS**: GraalVM JDK 21+ (LTS)
- **Razão**: Aproveitar compilação nativa (Native Image), otimizações avançadas do GraalVM Compiler e features modernas do Java 21

##### 2. Novos Recursos Obrigatórios

###### GraalVM Native Image
- Compilação nativa para executáveis standalone
- Startup ultrarrápido (~20-50ms vs 2-3s)
- Baixo uso de memória (~30-50MB vs 300-500MB)
- Otimizações específicas para containers

###### Virtual Threads (JEP 444)
- Concorrência escalável para operações I/O-bound
- Substituição de thread pools tradicionais
- Structured Concurrency (JEP 453)
- Melhor performance com menos recursos

###### Pattern Matching Avançado
- Record Patterns (JEP 440)
- Pattern Matching for switch completo (JEP 441)
- Decomposição profunda de estruturas
- Guards em pattern matching

###### Sequenced Collections (JEP 431)
- APIs consistentes para coleções ordenadas
- Métodos `getFirst()`, `getLast()`, `reversed()`
- `addFirst()`, `addLast()`, `removeFirst()`, `removeLast()`
- `SequencedSet`, `SequencedMap`, `SequencedCollection`

##### 3. Arquivos Atualizados

###### `copilot.instructions.md`
- ✅ GraalVM JDK 21+ como distribuição oficial
- ✅ Native Image e GraalVM Compiler mencionados
- ✅ Benefícios de performance documentados

###### `java-coding.instructions.md`
- ✅ Atualizado título e descrição para Java 21+
- ✅ Adicionados exemplos de Virtual Threads
- ✅ Pattern Matching avançado com Record Patterns
- ✅ Sequenced Collections APIs
- ✅ Structured Concurrency patterns
- ✅ Referências atualizadas para OpenJDK 21

###### `copilot.instructions.md`
- ✅ Versão base alterada de Java 17+ para Java 21+
- ✅ Adicionadas features Java 21+ nas tecnologias base
- ✅ Virtual Threads mencionados como features obrigatórias
- ✅ Quarkus 3+ com suporte a Virtual Threads

###### `devops.instructions.md`
- ✅ Dockerfile usando `ghcr.io/graalvm/graalvm-community:21`
- ✅ Build multi-stage com Native Image
- ✅ GitHub Actions usando `graalvm/setup-graalvm@v1`
- ✅ Imagem de runtime otimizada (`quarkus-micro-image`)
- ✅ Configurações específicas para compilação nativa

###### `documentation.instructions.md`
- ✅ README templates atualizados para Java 21+
- ✅ Documentação de features: Virtual Threads, Sequenced Collections
- ✅ Exemplos de código modernizados

##### 4. Novo Arquivo Criado

###### `java21-features.instructions.md` ⭐ NOVO
Documento dedicado às features exclusivas do Java 21+:

**Conteúdo:**
- Virtual Threads - Padrões e uso correto
- Structured Concurrency - Gerenciamento de tarefas concorrentes
- Pattern Matching Avançado - Record Patterns e decomposição
- Sequenced Collections - Ordem determinística
- Guia de migração Java 17 → Java 21
- Quando usar (e quando NÃO usar) cada feature

#### Melhorias de Qualidade

##### Performance
- **Native Image**: Startup 60x mais rápido, memória 10x menor
- **GraalVM Compiler**: Otimizações JIT superiores ao HotSpot
- Virtual Threads reduzem uso de memória em aplicações I/O-bound
- Melhor throughput com menos overhead de threads
- Structured Concurrency facilita cleanup de recursos

##### Segurança de Tipos
- Pattern Matching elimina type casting manual
- Compilador força tratamento exaustivo de casos
- Record Patterns com validação em tempo de compilação

##### Manutenibilidade
- Código mais expressivo e legível
- Menos boilerplate
- APIs mais consistentes (Sequenced Collections)

#### Migration Path

##### Para Projetos Existentes (Java 17)

1. **Instalar GraalVM JDK 21**
   ```bash
   # Windows
   choco install graalvm-java21
   
   # Linux/Mac (SDKMAN)
   sdk install java 21-graalce
   sdk use java 21-graalce
   
   # Instalar Native Image
   gu install native-image
   ```

2. **Atualizar pom.xml**
   ```xml
   <properties>
       <maven.compiler.source>21</maven.compiler.source>
       <maven.compiler.target>21</maven.compiler.target>
   </properties>
   
   <profiles>
       <profile>
           <id>native</id>
           <properties>
               <quarkus.package.type>native</quarkus.package.type>
           </properties>
       </profile>
   </profiles>
   ```

3. **Build e Test Native Image**
   ```bash
   # Build modo nativo
   ./mvnw package -Pnative
   
   # Test em modo nativo
   ./mvnw verify -Pnative
   
   # Executar
   ./target/myapp-runner
   ```

4. **Substituir Thread Pools por Virtual Threads**
   ```java
   // ANTES
   ExecutorService executor = Executors.newFixedThreadPool(100);
   
   // DEPOIS
   ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
   ```

4. **Modernizar Pattern Matching**
   ```java
   // ANTES
   if (obj instanceof User) {
       User user = (User) obj;
       processUser(user);
   }
   
   // DEPOIS
   if (obj instanceof User user) {
       processUser(user);
   }
   ```

5. **Modernizar Pattern Matching**
   ```java
   // ANTES
   if (obj instanceof User) {
       User user = (User) obj;
       processUser(user);
   }
   
   // DEPOIS
   if (obj instanceof User user) {
       processUser(user);
   }
   ```

6. **Adotar Sequenced Collections**
   ```java
   // ANTES
   LinkedHashSet<Order> orders = new LinkedHashSet<>();
   Order first = orders.iterator().next();
   
   // DEPOIS
   SequencedSet<Order> orders = new LinkedHashSet<>();
   Order first = orders.getFirst();
   ```

7. **Configurar Reflection para Native Image**
   ```java
   @RegisterForReflection
   public class MyEntity {
       // Automaticamente registrado para reflection
   }
   ```

##### Para Novos Projetos

Seguir todas as instruções com GraalVM JDK 21+ e features desde o início:
- Usar GraalVM como JDK padrão
- Configurar profiles Maven para Native Image
- Usar Virtual Threads para I/O-bound
- Pattern Matching completo
- Sequenced Collections onde apropriado
- Records com decomposição
- Structured Concurrency
- Anotações `@RegisterForReflection` onde necessário

#### Compatibilidade

##### Backward Compatibility
- ✅ Código Java 17 roda no GraalVM JDK 21
- ✅ Migration incremental possível
- ✅ GraalVM é compatível com JDK padrão
- ⚠️ Native Image requer configuração adicional para reflection
- ⚠️ Preview features requerem flags especiais

##### Forward Compatibility
- ❌ Código com features Java 21+ NÃO roda em Java 17
- ❌ Native Image NÃO roda sem GraalVM
- ⚠️ Verificar suporte do framework (Quarkus 3+, Spring Boot 3+)
- ⚠️ Algumas bibliotecas podem não ser compatíveis com Native Image

#### Próximos Passos

1. **Revisar** todos os projetos existentes
2. **Planejar** migração gradual para GraalVM
3. **Experimentar** Native Image em ambiente de dev
4. **Treinar** equipe nas novas features e GraalVM
5. **Atualizar** pipelines CI/CD para GraalVM
6. **Monitorar** performance pós-migração (startup, memória)
7. **Configurar** reflection metadata para Native Image
8. **Otimizar** builds nativos com PGO

#### Referências Adicionadas

- [GraalVM Official Site](https://www.graalvm.org/)
- [GraalVM Native Image](https://www.graalvm.org/latest/reference-manual/native-image/)
- [OpenJDK 21](https://openjdk.org/projects/jdk/21/)
- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444)
- [JEP 440: Record Patterns](https://openjdk.org/jeps/440)
- [JEP 441: Pattern Matching for switch](https://openjdk.org/jeps/441)
- [JEP 431: Sequenced Collections](https://openjdk.org/jeps/431)
- [JEP 453: Structured Concurrency](https://openjdk.org/jeps/453)
- [Quarkus Native Guide](https://quarkus.io/guides/building-native-image)

---

## [1.0.0] - 2024-01-01

### Versão Inicial
- Instruções base para Java 17+
- Clean Architecture
- Object Calisthenics
- Padrões de teste
- Segurança e LGPD
- DevOps e CI/CD
