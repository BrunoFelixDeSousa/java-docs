# Instruções Java - GraalVM JDK 21+

Conjunto completo de instruções personalizadas para desenvolvimento enterprise com **GraalVM JDK 21+**, **Quarkus 3+**, **Clean Architecture** e **Object Calisthenics**.

## 📚 Estrutura dos Documentos

### Instruções Fundamentais

1. **[copilot.instructions.md](./copilot.instructions.md)** ⭐ **START HERE**
   - Instruções fundamentais para todo o projeto
   - Tecnologias base: GraalVM JDK 21+, Quarkus 3+
   - Padrões gerais de código
   - Dependency injection e configuração

2. **[java-coding.instructions.md](./java-coding.instructions.md)**
   - Padrões avançados de código Java 21+
   - Object Calisthenics (9 regras)
   - Records, Sealed Classes, Pattern Matching
   - Clean Code e best practices

3. **[architecture.instructions.md](./architecture.instructions.md)**
   - Clean Architecture completa
   - Bounded Contexts e DDD
   - Estrutura de camadas enterprise
   - Separação de responsabilidades

### Features Java 21+

4. **[java21-features.instructions.md](./java21-features.instructions.md)** 🆕
   - Virtual Threads e Structured Concurrency
   - Pattern Matching avançado com Record Patterns
   - Sequenced Collections APIs
   - Guia de migração Java 17 → 21
   - Quando usar (e não usar) cada feature

### GraalVM Específico

5. **[graalvm.instructions.md](./graalvm.instructions.md)** 🚀 **NOVO**
   - Setup e instalação do GraalVM
   - Native Image - compilação nativa
   - Configuração de reflection e metadata
   - Profile-Guided Optimization (PGO)
   - Docker e containerização
   - Performance tuning
   - Troubleshooting e soluções
   - JVM vs Native comparison

### Qualidade e Testing

6. **[testing.instructions.md](./testing.instructions.md)**
   - JUnit 5 + Mockito + AssertJ
   - Testcontainers para integração
   - Given/When/Then obrigatório
   - @Nested e @DisplayName
   - Testes nativos com GraalVM

7. **[security.instructions.md](./security.instructions.md)**
   - LGPD/GDPR compliance
   - Autenticação e autorização
   - JWT Security
   - Criptografia e proteção de dados
   - Role-based access control

### DevOps e Deployment

8. **[devops.instructions.md](./devops.instructions.md)**
   - Docker multi-stage com GraalVM
   - GitHub Actions CI/CD
   - Kubernetes deployment
   - Observabilidade e monitoring
   - Build optimization

9. **[documentation.instructions.md](./documentation.instructions.md)**
   - JavaDoc patterns
   - API documentation
   - README templates
   - Architecture Decision Records

### Referências Rápidas

10. **[QUICK-REFERENCE.md](./QUICK-REFERENCE.md)** 📖
    - Cheat sheet de features
    - Tabela "When to Use What"
    - Migration quick wins
    - Comandos comuns
    - Padrões frequentes

11. **[CHANGELOG.md](./CHANGELOG.md)** 📝
    - Histórico de mudanças
    - Migração Java 17+ → GraalVM JDK 21+
    - Breaking changes
    - Migration path detalhado

## 🎯 Como Usar

### Para Novos Projetos

1. Leia **[copilot.instructions.md](./copilot.instructions.md)** primeiro
2. Configure GraalVM seguindo **[graalvm.instructions.md](./graalvm.instructions.md)**
3. Estruture o projeto com **[architecture.instructions.md](./architecture.instructions.md)**
4. Aplique padrões de **[java-coding.instructions.md](./java-coding.instructions.md)**
5. Consulte **[QUICK-REFERENCE.md](./QUICK-REFERENCE.md)** durante desenvolvimento

### Para Projetos Existentes

1. Revise **[CHANGELOG.md](./CHANGELOG.md)** para entender mudanças
2. Siga o migration path em **[graalvm.instructions.md](./graalvm.instructions.md)**
3. Atualize código com **[java21-features.instructions.md](./java21-features.instructions.md)**
4. Modernize testes com **[testing.instructions.md](./testing.instructions.md)**
5. Configure CI/CD com **[devops.instructions.md](./devops.instructions.md)**

### Consulta Diária

- **Quick Reference**: [QUICK-REFERENCE.md](./QUICK-REFERENCE.md)
- **GraalVM Issues**: [graalvm.instructions.md](./graalvm.instructions.md) - Troubleshooting
- **Testing Patterns**: [testing.instructions.md](./testing.instructions.md)
- **Security**: [security.instructions.md](./security.instructions.md)

## 🚀 Stack Tecnológica

### Core
- **GraalVM JDK 21+** (LTS até 2029)
  - Native Image para executáveis nativos
  - GraalVM Compiler para otimizações JIT
  - Virtual Threads, Pattern Matching, Sequenced Collections
- **Quarkus 3+**
  - Otimizado para GraalVM Native Image
  - Suporte completo a Virtual Threads
  - Startup ultrarrápido

### Build & Deploy
- **Maven 3.9+** com profiles para Native Image
- **Docker** multi-stage builds
- **GitHub Actions** com graalvm/setup-graalvm
- **Kubernetes** para orquestração

### Testing
- **JUnit 5** - Framework de testes
- **Mockito** - Mocking
- **AssertJ** - Assertions fluentes
- **Testcontainers** - Testes de integração
- **Native Tests** - Testes em modo nativo

### Quality & Security
- **SonarQube** - Code quality
- **OWASP Dependency Check** - Vulnerabilidades
- **JaCoCo** - Code coverage
- **SpotBugs** - Static analysis

## 📊 Benefícios do GraalVM

### Performance

| Métrica | JVM Tradicional | GraalVM Native | Melhoria |
|---------|----------------|----------------|----------|
| **Startup** | 2-3 segundos | 20-50ms | **~60x** ⚡ |
| **Memória** | 300-500 MB | 30-50 MB | **~10x** 💾 |
| **Imagem** | ~200 MB | ~50-80 MB | **~3x** 📦 |
| **Build** | ~30s | ~3-5 min | -10x ⏱️ |

### Casos de Uso Ideais

✅ **Serverless Functions** - Startup instantâneo  
✅ **Microservices** - Baixo uso de memória  
✅ **Containers** - Imagens menores  
✅ **CLI Tools** - Executáveis standalone  
✅ **Cloud Native** - Escala rápida  

## 🔄 Migration Path

### Java 17 → GraalVM JDK 21

```bash
# 1. Instalar GraalVM
sdk install java 21-graalce
sdk use java 21-graalce
gu install native-image

# 2. Atualizar projeto
# - pom.xml: version 21, profile native
# - Adicionar @RegisterForReflection onde necessário

# 3. Build native
./mvnw package -Pnative

# 4. Test
./target/myapp-runner

# 5. Deploy
docker build -f Dockerfile.native -t myapp:native .
```

Ver detalhes completos em **[CHANGELOG.md](./CHANGELOG.md)**.

## 🎓 Ordem de Leitura Recomendada

### Iniciantes
1. [copilot.instructions.md](./copilot.instructions.md) - Fundamentos
2. [QUICK-REFERENCE.md](./QUICK-REFERENCE.md) - Referência rápida
3. [java-coding.instructions.md](./java-coding.instructions.md) - Padrões de código
4. [graalvm.instructions.md](./graalvm.instructions.md) - GraalVM basics

### Intermediários
1. [architecture.instructions.md](./architecture.instructions.md) - Clean Architecture
2. [java21-features.instructions.md](./java21-features.instructions.md) - Features avançadas
3. [testing.instructions.md](./testing.instructions.md) - Testing completo
4. [security.instructions.md](./security.instructions.md) - Segurança

### Avançados
1. [graalvm.instructions.md](./graalvm.instructions.md) - Native Image avançado
2. [devops.instructions.md](./devops.instructions.md) - CI/CD e deployment
3. [CHANGELOG.md](./CHANGELOG.md) - Entender evolução

## 📖 Recursos Adicionais

### Documentação Oficial
- [GraalVM Docs](https://www.graalvm.org/latest/docs/)
- [OpenJDK 21](https://openjdk.org/projects/jdk/21/)
- [Quarkus Guides](https://quarkus.io/guides/)

### JEPs Relevantes
- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444)
- [JEP 440: Record Patterns](https://openjdk.org/jeps/440)
- [JEP 441: Pattern Matching for switch](https://openjdk.org/jeps/441)
- [JEP 431: Sequenced Collections](https://openjdk.org/jeps/431)
- [JEP 453: Structured Concurrency](https://openjdk.org/jeps/453)

### Livros Recomendados
- **Effective Java 3rd Edition** - Joshua Bloch
- **Clean Architecture** - Robert C. Martin
- **Domain-Driven Design** - Eric Evans
- **Object Calisthenics** - Jeff Bay

## 🤝 Contribuindo

Para sugestões de melhorias:
1. Abra uma issue descrevendo a melhoria
2. Referencie o arquivo específico
3. Forneça exemplos práticos
4. Considere compatibilidade com GraalVM Native Image

## 📅 Versão e Atualizações

- **Versão Atual**: 2.0.0
- **Última Atualização**: 2024-09-29
- **JDK Base**: GraalVM JDK 21+ (LTS)
- **Framework**: Quarkus 3+

Ver histórico completo em **[CHANGELOG.md](./CHANGELOG.md)**.

---

**Mantenha essas instruções atualizadas conforme o ecossistema Java evolui!** 🚀
