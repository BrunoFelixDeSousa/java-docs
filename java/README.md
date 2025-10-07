# ☕ Java - Documentação Completa

Documentação abrangente sobre Java, desde fundamentos até features modernas das versões LTS e latest.

---

## 📋 Índice

- [Versões do Java](#-versões-do-java)
- [Collections Framework](#-collections-framework)
- [Projetos Práticos](#-projetos-práticos)
- [Compiladores](#-compiladores)

---

## 🔢 Versões do Java

### Java 8 (LTS - 2014)
📄 **[Java8-Features.md](./8/Java8-Features.md)**

**Features Principais**:
- ✅ **Lambdas** - Programação funcional em Java
- ✅ **Streams API** - Processamento de coleções declarativo
- ✅ **Optional** - Evitar NullPointerException
- ✅ **Date/Time API** - Nova API de data e hora (java.time)
- ✅ **Method References** - Referências a métodos
- ✅ **Default Methods** - Métodos padrão em interfaces
- ✅ **Functional Interfaces** - @FunctionalInterface

**Exemplo de Lambda**:
```java
// Antes do Java 8
List<String> names = Arrays.asList("Ana", "Bruno", "Carlos");
names.sort(new Comparator<String>() {
    public int compare(String a, String b) {
        return a.compareTo(b);
    }
});

// Com Java 8
names.sort((a, b) -> a.compareTo(b));
names.sort(String::compareTo); // Method reference
```

---

### Java 17 (LTS - 2021)
📄 **[Java17-Features.md](./17/Java17-Features.md)**

**Features Principais**:
- ✅ **Sealed Classes** - Controle de hierarquia de classes
- ✅ **Records** - Classes de dados imutáveis
- ✅ **Pattern Matching for instanceof** - Menos casting
- ✅ **Text Blocks** - Strings multilinha
- ✅ **Switch Expressions** - Switch como expressão

**Exemplo de Record**:
```java
// Antes do Java 17
public class Person {
    private final String name;
    private final int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    // equals, hashCode, toString...
}

// Com Java 17
public record Person(String name, int age) {}
```

---

### Java 21 (LTS - 2023)
📄 **[Java21-Features.md](./21/Java21-Features.md)**

**Features Principais**:
- ✅ **Virtual Threads** - Threads leves para alta concorrência
- ✅ **Sequenced Collections** - Ordem garantida em coleções
- ✅ **String Templates** (Preview) - Interpolação de strings
- ✅ **Record Patterns** - Pattern matching com records
- ✅ **Pattern Matching for switch** - Switch pattern matching

**Exemplo de Virtual Threads**:
```java
// Criar milhares de threads sem overhead
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 10_000; i++) {
        executor.submit(() -> {
            // Tarefa de I/O
            Thread.sleep(Duration.ofSeconds(1));
            return "Done";
        });
    }
} // Aguarda todas as threads
```

---

### Java 25 (Latest - 2024)
📄 **[Java25-Features.md](./25/Java25-Features.md)**

**Features Principais**:
- ✅ **Primitive Patterns** (Preview) - Pattern matching com primitivos
- ✅ **Stream Gatherers** - Operações customizadas em Streams
- ✅ **Value Types** (Preview) - Tipos por valor
- ✅ **Flexible Constructor Bodies** (Preview) - Construtores mais flexíveis

**Exemplo de Stream Gatherers**:
```java
// Criar operações customizadas em Streams
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

List<List<Integer>> batches = numbers.stream()
    .gather(Gatherers.windowFixed(3))
    .toList();
// [[1,2,3], [4,5,6], [7,8,9], [10]]
```

---

## 📦 Collections Framework

📄 **[Collections-Framework.md](./Collections-Framework.md)**

### Estruturas Disponíveis

| Interface | Implementações | Uso Principal |
|-----------|----------------|---------------|
| **List** | ArrayList, LinkedList, Vector | Sequência ordenada com duplicatas |
| **Set** | HashSet, LinkedHashSet, TreeSet | Sem duplicatas |
| **Queue** | PriorityQueue, ArrayDeque | FIFO (fila) |
| **Deque** | ArrayDeque, LinkedList | Fila dupla |
| **Map** | HashMap, LinkedHashMap, TreeMap | Chave-valor |

### Comparações de Performance

**ArrayList vs LinkedList**:
```java
// ArrayList - Melhor para acesso aleatório
List<String> arrayList = new ArrayList<>();
arrayList.get(1000); // O(1) - rápido

// LinkedList - Melhor para inserções/remoções
List<String> linkedList = new LinkedList<>();
linkedList.add(0, "first"); // O(1) - rápido
```

**HashMap vs TreeMap**:
```java
// HashMap - Sem ordem, mais rápido
Map<String, Integer> hashMap = new HashMap<>();
hashMap.put("key", 1); // O(1)

// TreeMap - Ordenado, mais lento
Map<String, Integer> treeMap = new TreeMap<>();
treeMap.put("key", 1); // O(log n)
```

### Guia de Escolha

```
Precisa de ordem de inserção?
├─ SIM → LinkedHashSet, LinkedHashMap
└─ NÃO
    ├─ Precisa de ordem natural?
    │  └─ SIM → TreeSet, TreeMap
    └─ NÃO → HashSet, HashMap (mais rápido)

Precisa de duplicatas?
├─ SIM → List (ArrayList ou LinkedList)
└─ NÃO → Set

Precisa de FIFO?
└─ SIM → Queue (ArrayDeque, PriorityQueue)

Precisa de acesso aleatório?
├─ SIM → ArrayList
└─ NÃO (muitas inserções/remoções) → LinkedList
```

---

## 🚀 Projetos Práticos

📁 **[projetos/](./projetos/)**

### Flight Reservation System CLI
📄 **[Flight-Reservation-System-CLI.md](./projetos/Flight-Reservation-System-CLI.md)**

**Descrição**: Sistema completo de reserva de voos com interface CLI.

**Tecnologias**:
- Java 17+
- Collections Framework
- Padrões de Design (Factory, Strategy, Observer)
- Gestão de dados em memória

**Features**:
- ✈️ Busca de voos
- 🎫 Criação de reservas
- 👤 Gestão de passageiros
- 💺 Seleção de assentos
- 📊 Relatórios

**Arquitetura**:
```
flight-reservation-system/
├── domain/
│   ├── Flight.java
│   ├── Passenger.java
│   ├── Reservation.java
│   └── Seat.java
├── service/
│   ├── FlightService.java
│   ├── ReservationService.java
│   └── PassengerService.java
├── repository/
│   ├── FlightRepository.java
│   └── ReservationRepository.java
├── ui/
│   └── CLI.java
└── Main.java
```

**Padrões Utilizados**:
- **Factory Pattern** - Criação de objetos
- **Strategy Pattern** - Algoritmos de busca
- **Observer Pattern** - Notificações
- **Repository Pattern** - Acesso a dados

---

## 🔧 Compiladores

📄 **[compiladores.md](./compiladores.md)**

Documentação sobre compiladores Java e teoria de compiladores.

**Tópicos**:
- Fases de compilação
- Análise léxica, sintática e semântica
- Geração de código
- Otimizações
- JIT Compiler
- GraalVM

---

## 📚 Recursos Adicionais

### Documentação Oficial
- [Java SE Documentation](https://docs.oracle.com/en/java/javase/)
- [Java Tutorials](https://docs.oracle.com/javase/tutorial/)
- [JDK Release Notes](https://www.oracle.com/java/technologies/javase-downloads.html)

### Livros Recomendados
- **Effective Java** (Joshua Bloch)
- **Clean Code** (Robert C. Martin)
- **Java Concurrency in Practice** (Brian Goetz)

### Ferramentas
- **IntelliJ IDEA** - IDE recomendada
- **Maven/Gradle** - Build tools
- **JUnit 5** - Testing framework
- **JMH** - Benchmarking

---

## 🎯 Próximos Passos

1. **Iniciantes**: Comece com [Java8-Features.md](./8/Java8-Features.md)
2. **Intermediários**: Estude [Collections-Framework.md](./Collections-Framework.md)
3. **Avançados**: Explore [Java21-Features.md](./21/Java21-Features.md)
4. **Projetos**: Implemente o [Flight Reservation System](./projetos/Flight-Reservation-System-CLI.md)

---

**Voltar para**: [📁 Repositório Principal](../README.md)
