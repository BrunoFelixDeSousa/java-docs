# 📚 Java Docs - Repositório Completo de Documentação Técnica

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-8%20%7C%2017%20%7C%2021%20%7C%2025-orange.svg)](java/)
[![React](https://img.shields.io/badge/React-18+-61DAFB.svg)](frontend/react/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5+-3178C6.svg)](learning/concepts/typescript/)
[![Python](https://img.shields.io/badge/Python-3.x-3776AB.svg)](learning/python/)

> Documentação técnica completa para desenvolvimento **Java**, **React/Next.js**, **Quarkus**, **Python/ML** e muito mais!

---

## 📁 Estrutura do Repositório

```
java-docs/
├── java/              # Documentação Java (versões, collections, projetos)
├── frameworks/        # Frameworks backend (Quarkus)
├── frontend/          # Frontend (React/Next.js)
├── libraries/         # Bibliotecas (Apache Commons, DeepLearning)
├── patterns/          # Padrões (SOLID, Clean Arch, CQRS)
├── instructions/      # Instruções (Java, NestJS)
└── learning/          # Material de estudo (JS, Python, Conceitos)
```

---

## 📋 Índice

- [Java](#-java)
- [Frameworks](#️-frameworks)
- [Frontend](#️-frontend)
- [Libraries](#-libraries)
- [Patterns](#-patterns)
- [Instructions](#-instructions)
- [Learning](#-learning)
- [Estatísticas](#-estatísticas)
- [Como Usar](#-como-usar)

---

## ☕ Java

📁 **[java/](java/)** | 📄 **[README](java/README.md)**

### Versões do Java

| Versão | Status | Arquivo | Principais Features |
|--------|--------|---------|---------------------|
| **Java 8** | LTS | [features.md](java/versions/java-8/Java8-Features.md) | Lambdas, Streams, Optional, Date/Time API |
| **Java 17** | LTS | [features.md](java/versions/java-17/Java17-Features.md) | Records, Sealed Classes, Pattern Matching |
| **Java 21** | LTS | [features.md](java/versions/java-21/Java21-Features.md) | Virtual Threads, Sequenced Collections |
| **Java 25** | Latest | [features.md](java/versions/java-25/Java25-Features.md) | Stream Gatherers, Primitive Patterns |

### Collections & Projects
- 📦 **[Collections Framework](java/collections/Collections-Framework.md)** - Guia completo
- 🎯 **[Flight Reservation System](java/projects/Flight-Reservation-System-CLI.md)** - Projeto CLI

---

## 🏗️ Frameworks

### Quarkus
📁 **[frameworks/quarkus/](frameworks/quarkus/)** | 📄 **[README](frameworks/quarkus/README.md)**

Framework Java supersônico para cloud-native (20+ documentos organizados).

<details>
<summary><b>📖 Ver todos os tópicos Quarkus</b></summary>

#### Guides
- [Getting Started](frameworks/quarkus/guides/getting-started.md)
- [Advanced Guide](frameworks/quarkus/guides/advanced.md)

#### Core
- [Annotations](frameworks/quarkus/core/annotations.md), [CDI](frameworks/quarkus/core/cdi-e-injecao.md), [Config](frameworks/quarkus/core/configuracoes.md), [Logging](frameworks/quarkus/core/logging.md)

#### Data
- [Panache](frameworks/quarkus/data/panache.md), [Cache](frameworks/quarkus/data/cache.md), [Redis](frameworks/quarkus/data/redis.md)

#### Security
- [Auth](frameworks/quarkus/security/auth.md), [Validator](frameworks/quarkus/security/validator.md)

#### Messaging & Reactive
- [Kafka](frameworks/quarkus/messaging/kafka.md), [Mutiny](frameworks/quarkus/reactive/Mutiny.md)

#### Integrations
- [REST Client](frameworks/quarkus/integrations/rest-client.md), [LangChain4j](frameworks/quarkus/integrations/langchain4j.md), [Scheduling](frameworks/quarkus/integrations/scheduling.md)

#### DevOps
- [Kubernetes/Docker](frameworks/quarkus/devops/kubernates-docker.md), [Observability](frameworks/quarkus/devops/kibana-e-observabilidade.md)

#### Testing
- [Tests](frameworks/quarkus/testing/testes.md)

</details>

---

## ⚛️ Frontend

### React & Next.js
📁 **[frontend/react/](frontend/react/)** | 📄 **[README](frontend/react/README.md)**

**13 arquivos**, **~50.000 linhas** de documentação React/Next.js.

<details>
<summary><b>📖 Ver estrutura completa</b></summary>

#### Fundamentals (3 arquivos)
- [React Fundamentos](frontend/react/fundamentals/react-fundamentos.md) - JSX, Components, Props, State
- [React Hooks](frontend/react/fundamentals/react-hooks.md) - 14 hooks + custom hooks
- [React TypeScript](frontend/react/fundamentals/react-typescript.md) - TypeScript completo

#### Advanced (6 arquivos)
- [State Management](frontend/react/advanced/react-state-management.md) - 6 estratégias
- [Patterns](frontend/react/advanced/react-patterns.md) - 7 padrões essenciais
- [Performance](frontend/react/advanced/react-performance.md) - Otimização
- [Suspense](frontend/react/advanced/react-suspense.md) - Suspense & Error Boundaries
- [Router](frontend/react/advanced/react-router.md) - React Router v6
- [Forms](frontend/react/advanced/react-forms.md) - React Hook Form + Zod

#### Next.js (1 arquivo)
- [Next.js 14+](frontend/react/nextjs/next-js.md) - App Router completo

#### Architecture (2 arquivos)
- [ARCHITECTURE](frontend/react/architecture/ARCHITECTURE.md) - **86% completo** (12/14 seções)
  - ✅ Testing Strategy, Security Best Practices
  - 🔄 Faltam: Code Quality, Deployment
- [FOLDER_STRUCTURE](frontend/react/architecture/FOLDER_STRUCTURE.md) - Estrutura Next.js 14+

</details>

---

## 📚 Libraries

### Apache Commons
📁 **[libraries/apache-commons/](libraries/apache-commons/)** | 📄 **[README](libraries/apache-commons/readme.md)**

- [Collections4](libraries/apache-commons/collections4.md) - Bag, BidiMap, MultiMap
- [Lang3](libraries/apache-commons/lang3.md) - StringUtils, ArrayUtils, ObjectUtils
- [Validator](libraries/apache-commons/validator.md) - Email, URL, CreditCard validation

### Deep Learning
📁 **[libraries/deeplearning/](libraries/deeplearning/)**

- [DeepLearning4J](libraries/deeplearning/DeepLearning4J.md) - DL para JVM
- [ECJ](libraries/deeplearning/ECJ.md) - Evolutionary Computation

### Other
- [KSP-KRPC](libraries/other/ksp-krpc.md) - Kerbal Space Program API

---

## 🎨 Patterns

📁 **[patterns/](patterns/)** | 📄 **[README](patterns/README.md)**

- [SOLID](patterns/solid.md) - 5 princípios OOP
- [Object Calisthenics](patterns/Object-Calisthenics.md) - 9 regras para código limpo
- [CQRS](patterns/CQRS.md) - Command Query Responsibility Segregation
- [Clean Architecture](patterns/clean-architecture.md) - Arquitetura limpa
- [Hexagonal Architecture](patterns/hexagonal-architecture.md) - Ports & Adapters

---

## 📖 Instructions

### Java
📁 **[instructions/java/](instructions/java/)** | 📄 **[README](instructions/java/README.md)**

<details>
<summary><b>📋 Ver todas as instruções Java</b></summary>

- [Coding Standards](instructions/java/java-coding.instructions.md) - Java 21+ patterns
- [Java 21 Features](instructions/java/java21-features.instructions.md) - Virtual Threads
- [Architecture](instructions/java/architecture.instructions.md) - Clean Arch, DDD
- [Testing](instructions/java/testing.instructions.md) - Unit, Integration, E2E
- [Security](instructions/java/security.instructions.md) - OWASP Top 10
- [DevOps](instructions/java/devops.instructions.md) - Docker, CI/CD
- [GraalVM](instructions/java/graalvm.instructions.md) - Native compilation
- [Documentation](instructions/java/documentation.instructions.md) - JavaDoc
- [Copilot](instructions/java/copilot.instructions.md) - GitHub Copilot
- [Quick Reference](instructions/java/QUICK-REFERENCE.md)

</details>

### NestJS
📁 **[instructions/nestjs/](instructions/nestjs/)** | 📄 **[README](instructions/nestjs/README.md)**

<details>
<summary><b>📋 Ver todas as instruções NestJS</b></summary>

- [TypeScript Coding](instructions/nestjs/typescript-coding.instructions.md)
- [NestJS Architecture](instructions/nestjs/nestjs-architecture.instructions.md)
- [Domain Modeling](instructions/nestjs/domain-modeling.instructions.md) - DDD
- [Testing](instructions/nestjs/nestjs-testing.instructions.md)
- [Documentation](instructions/nestjs/documentation.instructions.md) - Swagger
- [Copilot](instructions/nestjs/copilot-instructions.md)

</details>

---

## 🎓 Learning

📁 **[learning/](learning/)** | 📄 **[README](learning/README.md)**

Mini-documentações de **25+ tecnologias**.

### Concepts
- [Data Structures](learning/concepts/data-structures/) - Estruturas de dados
- [Compilers](learning/concepts/compilers.md) - Teoria de compiladores
- [Three.js](learning/concepts/threejs/) - Biblioteca 3D
- [TypeScript](learning/concepts/typescript/) - TypeScript basics

### JavaScript
📁 **[learning/javascript/](learning/javascript/)**

- **Libraries**: [React Query](learning/javascript/libraries/react-query-mini-doc/), [Zod](learning/javascript/libraries/zod-mini-doc/)
- **Styling**: [TailwindCSS](learning/javascript/styling/tailwindcss-mini-doc/)
- **Frameworks**: [React Hook Form](learning/javascript/frameworks/react-hook-form-mini-doc/), [React Router](learning/javascript/frameworks/react-router-mini-doc/)
- **Testing**: [Jest](learning/javascript/testing/jest-mini-doc/)

### Python
📁 **[learning/python/](learning/python/)** | 📄 **[README](learning/python/README.md)**

<details>
<summary><b>🐍 Ver conteúdo Python</b></summary>

#### Language
- [Python Mini-Doc](learning/python/linguagem/python-mini-doc/)

#### ML & Computer Vision
- [ML.md](learning/python/ML-visao-computacional/ML.md) - Machine Learning
- **Libraries**: NumPy, Pandas, Scikit-learn, YOLO
- **Frameworks**:
  - [OpenCV](learning/python/ML-visao-computacional/opencv-mini-doc/)
  - [PyTorch](learning/python/ML-visao-computacional/pytorch-mini-doc/) (+ [Iris Notebook](learning/python/ML-visao-computacional/pytorch-mini-doc/Iris_PyTorch.ipynb))
  - [TensorFlow](learning/python/ML-visao-computacional/tensorflow-mini-doc/)

</details>

---

## 📊 Estatísticas

| Categoria | Tecnologias | Arquivos | Linhas (~) |
|-----------|-------------|----------|------------|
| **Java** | 4 versões + Collections + Projects | 10+ | ~15,000 |
| **Quarkus** | 20+ tópicos organizados | 22 | ~30,000 |
| **React/Next.js** | 13 documentos completos | 13 | ~50,000 |
| **Libraries** | Apache Commons, DeepLearning | 8 | ~8,000 |
| **Patterns** | SOLID, CQRS, Architectures | 5 | ~5,000 |
| **Instructions** | Java + NestJS | 18 | ~10,000 |
| **Learning** | 25+ mini-docs | 30+ | ~20,000 |
| **TOTAL** | **80+ tecnologias** | **100+** | **~138,000** |

---

## 🚀 Como Usar

### 1. Clone o repositório
```bash
git clone https://github.com/BrunoFelixDeSousa/java-docs.git
cd java-docs
```

### 2. Navegue pelas categorias
- 📂 `java/` - Para documentação Java
- 📂 `frameworks/quarkus/` - Para Quarkus
- 📂 `frontend/react/` - Para React/Next.js
- 📂 `patterns/` - Para padrões e arquiteturas
- 📂 `learning/` - Para mini-documentações

### 3. Leia os READMEs
Cada pasta principal tem um **README.md** com índice completo e exemplos de código.

### 4. Explore os arquivos
Todos os arquivos são **Markdown** com exemplos de código prontos para copiar.

---

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-documentacao`)
3. Commit suas mudanças (`git commit -m 'Add: nova documentação'`)
4. Push para a branch (`git push origin feature/nova-documentacao`)
5. Abra um Pull Request

---

## 📝 License

Este projeto está sob a licença MIT. Veja [LICENSE](LICENSE) para mais detalhes.

---

## 📧 Contato

**Bruno Felix de Sousa**

- GitHub: [@BrunoFelixDeSousa](https://github.com/BrunoFelixDeSousa)
- LinkedIn: [Bruno Felix](https://www.linkedin.com/in/bruno-felix-de-sousa)

---

<div align="center">

**⭐ Se este repositório te ajudou, deixe uma estrela!**

</div>
