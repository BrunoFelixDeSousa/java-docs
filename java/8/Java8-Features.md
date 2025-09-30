# Java 8 - Guia Completo de Features

## 📋 Índice
1. [Lambdas e Expressões Lambda](#lambdas-e-expressões-lambda)
2. [Interfaces Funcionais](#interfaces-funcionais)
3. [Method References](#method-references)
4. [Streams API](#streams-api)
5. [Optional](#optional)
6. [Default Methods](#default-methods)
7. [Nova API de Data e Hora](#nova-api-de-data-e-hora)
8. [Nashorn JavaScript Engine](#nashorn-javascript-engine)
9. [Annotations em Tipos](#annotations-em-tipos)
10. [Melhorias na Concorrência](#melhorias-na-concorrência)

---

## 1. Lambdas e Expressões Lambda

### Conceito
Lambdas permitem tratar funcionalidade como argumento de método, ou código como dados. São funções anônimas que simplificam a implementação de interfaces funcionais.

### Sintaxe
```java
// Sintaxe básica
(parametros) -> expressao
(parametros) -> { declaracoes; }
```

### Exemplos Práticos

```java
import java.util.*;
import java.util.function.*;

public class LambdaExamples {
    
    public static void main(String[] args) {
        
        // 1. Lambda simples sem parâmetros
        Runnable r1 = () -> System.out.println("Hello Lambda!");
        r1.run();
        // Saída: Hello Lambda!
        
        // 2. Lambda com um parâmetro (parênteses opcionais)
        Consumer<String> printer = message -> System.out.println(message);
        printer.accept("Usando Consumer");
        // Saída: Usando Consumer
        
        // 3. Lambda com múltiplos parâmetros
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        System.out.println("5 + 3 = " + add.apply(5, 3));
        // Saída: 5 + 3 = 8
        
        // 4. Lambda com bloco de código
        BiConsumer<String, Integer> display = (name, age) -> {
            System.out.println("Nome: " + name);
            System.out.println("Idade: " + age);
            System.out.println("---");
        };
        display.accept("João", 30);
        // Saída:
        // Nome: João
        // Idade: 30
        // ---
        
        // 5. Lambda em Collections - Comparator
        List<String> names = Arrays.asList("Carlos", "Ana", "Bruno", "Diana");
        
        // Antes do Java 8
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        
        // Com Lambda
        Collections.sort(names, (s1, s2) -> s1.compareTo(s2));
        System.out.println("Ordenado: " + names);
        // Saída: Ordenado: [Ana, Bruno, Carlos, Diana]
        
        // 6. Lambda com inferência de tipo
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        numbers.forEach(n -> System.out.print(n + " "));
        System.out.println();
        // Saída: 1 2 3 4 5
        
        // 7. Lambda capturando variáveis locais (effectively final)
        String prefix = "Número: ";
        numbers.forEach(n -> System.out.println(prefix + n));
        // Saída:
        // Número: 1
        // Número: 2
        // ...
        
        // 8. Lambda retornando valores
        Function<String, Integer> stringLength = s -> s.length();
        System.out.println("Tamanho de 'Hello': " + stringLength.apply("Hello"));
        // Saída: Tamanho de 'Hello': 5
    }
}
```

---

## 2. Interfaces Funcionais

### Conceito
Interface funcional é uma interface com exatamente um método abstrato. Podem ter múltiplos métodos default ou static.

### Anotação @FunctionalInterface

```java
import java.util.function.*;

// Interface funcional customizada
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b);
    
    // Métodos default são permitidos
    default void printResult(int result) {
        System.out.println("Resultado: " + result);
    }
    
    // Métodos static são permitidos
    static String getDescription() {
        return "Calculadora simples";
    }
}

// Implementação
public class FunctionalInterfaceExamples {
    
    public static void main(String[] args) {
        
        // 1. Implementando interface funcional customizada
        Calculator add = (a, b) -> a + b;
        Calculator multiply = (a, b) -> a * b;
        
        int sum = add.calculate(10, 5);
        add.printResult(sum);
        // Saída: Resultado: 15
        
        int product = multiply.calculate(10, 5);
        multiply.printResult(product);
        // Saída: Resultado: 50
        
        System.out.println(Calculator.getDescription());
        // Saída: Calculadora simples
        
        demonstratePredefinedFunctionalInterfaces();
    }
    
    // Interfaces funcionais pré-definidas do Java
    public static void demonstratePredefinedFunctionalInterfaces() {
        
        // 2. Predicate<T> - retorna boolean
        Predicate<Integer> isEven = n -> n % 2 == 0;
        System.out.println("10 é par? " + isEven.test(10));
        // Saída: 10 é par? true
        
        Predicate<String> isLong = s -> s.length() > 5;
        System.out.println("'Java' tem mais de 5 caracteres? " + isLong.test("Java"));
        // Saída: 'Java' tem mais de 5 caracteres? false
        
        // 3. Function<T, R> - transforma T em R
        Function<String, Integer> toLength = s -> s.length();
        System.out.println("Tamanho: " + toLength.apply("Programação"));
        // Saída: Tamanho: 11
        
        Function<Integer, String> toHex = i -> Integer.toHexString(i);
        System.out.println("255 em hexadecimal: " + toHex.apply(255));
        // Saída: 255 em hexadecimal: ff
        
        // 4. Consumer<T> - consome T sem retornar nada
        Consumer<String> logger = msg -> System.out.println("[LOG] " + msg);
        logger.accept("Aplicação iniciada");
        // Saída: [LOG] Aplicação iniciada
        
        // 5. Supplier<T> - fornece T sem receber parâmetros
        Supplier<Double> randomSupplier = () -> Math.random();
        System.out.println("Número aleatório: " + randomSupplier.get());
        // Saída: Número aleatório: 0.xxxxx (valor aleatório)
        
        // 6. BiFunction<T, U, R> - aceita dois parâmetros
        BiFunction<String, String, String> concat = (s1, s2) -> s1 + " " + s2;
        System.out.println(concat.apply("Hello", "World"));
        // Saída: Hello World
        
        // 7. UnaryOperator<T> - Function especializada onde entrada = saída
        UnaryOperator<Integer> square = x -> x * x;
        System.out.println("Quadrado de 7: " + square.apply(7));
        // Saída: Quadrado de 7: 49
        
        // 8. BinaryOperator<T> - BiFunction especializada onde ambos são do mesmo tipo
        BinaryOperator<Integer> max = (a, b) -> a > b ? a : b;
        System.out.println("Máximo entre 15 e 23: " + max.apply(15, 23));
        // Saída: Máximo entre 15 e 23: 23
    }
}
```

---

## 3. Method References

### Conceito
Method references são uma forma abreviada de lambdas que chamam métodos existentes.

### Tipos de Method References

```java
import java.util.*;
import java.util.function.*;

public class MethodReferenceExamples {
    
    public static void main(String[] args) {
        
        // 1. Referência a método estático: Classe::metodoEstatico
        Function<String, Integer> parseInt1 = Integer::parseInt;
        System.out.println("Parse: " + parseInt1.apply("123"));
        // Saída: Parse: 123
        
        // Equivalente a:
        Function<String, Integer> parseInt2 = s -> Integer.parseInt(s);
        
        // 2. Referência a método de instância de objeto específico: objeto::metodo
        String text = "Hello World";
        Supplier<String> upperCase1 = text::toUpperCase;
        System.out.println(upperCase1.get());
        // Saída: HELLO WORLD
        
        // Equivalente a:
        Supplier<String> upperCase2 = () -> text.toUpperCase();
        
        // 3. Referência a método de instância de tipo arbitrário: Tipo::metodo
        Function<String, String> toLower1 = String::toLowerCase;
        System.out.println(toLower1.apply("JAVA"));
        // Saída: java
        
        // Equivalente a:
        Function<String, String> toLower2 = s -> s.toLowerCase();
        
        // 4. Referência a construtor: Classe::new
        Supplier<List<String>> listSupplier1 = ArrayList::new;
        List<String> list = listSupplier1.get();
        
        // Equivalente a:
        Supplier<List<String>> listSupplier2 = () -> new ArrayList<>();
        
        demonstrateWithCollections();
        demonstrateComplexExamples();
    }
    
    public static void demonstrateWithCollections() {
        System.out.println("\n=== Method References com Collections ===");
        
        List<String> words = Arrays.asList("Java", "Python", "JavaScript", "C++");
        
        // 5. forEach com method reference
        System.out.println("Palavras:");
        words.forEach(System.out::println);
        // Saída:
        // Palavras:
        // Java
        // Python
        // JavaScript
        // C++
        
        // 6. Ordenação com method reference
        List<String> names = Arrays.asList("Charlie", "Alice", "Bob");
        names.sort(String::compareToIgnoreCase);
        System.out.println("Ordenado: " + names);
        // Saída: Ordenado: [Alice, Bob, Charlie]
        
        // 7. Transformação com map
        List<Integer> lengths = new ArrayList<>();
        words.stream()
             .map(String::length)
             .forEach(lengths::add);
        System.out.println("Tamanhos: " + lengths);
        // Saída: Tamanhos: [4, 6, 10, 3]
    }
    
    public static void demonstrateComplexExamples() {
        System.out.println("\n=== Exemplos Complexos ===");
        
        // 8. Array Constructor Reference
        Function<Integer, String[]> arrayCreator = String[]::new;
        String[] array = arrayCreator.apply(5);
        System.out.println("Array criado com tamanho: " + array.length);
        // Saída: Array criado com tamanho: 5
        
        // 9. Comparator com method references
        List<Person> people = Arrays.asList(
            new Person("Ana", 25),
            new Person("Bruno", 30),
            new Person("Carlos", 20)
        );
        
        // Comparando por idade
        people.sort(Comparator.comparing(Person::getAge));
        System.out.println("Por idade: " + people);
        // Saída: Por idade: [Person{name='Carlos', age=20}, Person{name='Ana', age=25}, Person{name='Bruno', age=30}]
        
        // Comparando por nome
        people.sort(Comparator.comparing(Person::getName));
        System.out.println("Por nome: " + people);
        // Saída: Por nome: [Person{name='Ana', age=25}, Person{name='Bruno', age=30}, Person{name='Carlos', age=20}]
        
        // 10. BiFunction com method reference
        BiFunction<String, String, Boolean> equals = String::equals;
        System.out.println("'Java' equals 'Java': " + equals.apply("Java", "Java"));
        // Saída: 'Java' equals 'Java': true
    }
    
    // Classe auxiliar
    static class Person {
        private String name;
        private int age;
        
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        public String getName() { return name; }
        public int getAge() { return age; }
        
        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }
}
```

---

## 4. Streams API

### Conceito
Stream é uma sequência de elementos que suporta operações agregadas sequenciais e paralelas.

### Operações Intermediárias e Terminais

```java
import java.util.*;
import java.util.stream.*;

public class StreamsExamples {
    
    public static void main(String[] args) {
        
        basicStreamOperations();
        intermediateOperations();
        terminalOperations();
        advancedStreamExamples();
        parallelStreams();
    }
    
    // Operações básicas
    public static void basicStreamOperations() {
        System.out.println("=== Operações Básicas ===\n");
        
        // 1. Criando streams
        Stream<String> stream1 = Stream.of("A", "B", "C");
        Stream<Integer> stream2 = Arrays.asList(1, 2, 3).stream();
        IntStream stream3 = IntStream.range(1, 5);
        
        // 2. Filter - filtrar elementos
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> evenNumbers = numbers.stream()
                                          .filter(n -> n % 2 == 0)
                                          .collect(Collectors.toList());
        System.out.println("Números pares: " + evenNumbers);
        // Saída: Números pares: [2, 4, 6, 8, 10]
        
        // 3. Map - transformar elementos
        List<String> names = Arrays.asList("ana", "bruno", "carlos");
        List<String> upperNames = names.stream()
                                      .map(String::toUpperCase)
                                      .collect(Collectors.toList());
        System.out.println("Nomes maiúsculos: " + upperNames);
        // Saída: Nomes maiúsculos: [ANA, BRUNO, CARLOS]
        
        // 4. ForEach - iterar sobre elementos
        System.out.print("Números: ");
        numbers.stream()
              .limit(5)
              .forEach(n -> System.out.print(n + " "));
        System.out.println();
        // Saída: Números: 1 2 3 4 5
    }
    
    // Operações intermediárias
    public static void intermediateOperations() {
        System.out.println("\n=== Operações Intermediárias ===\n");
        
        List<String> words = Arrays.asList("Java", "Python", "JavaScript", 
                                          "C++", "Ruby", "Go", "Rust");
        
        // 5. Distinct - remover duplicatas
        List<Integer> numbersWithDuplicates = Arrays.asList(1, 2, 2, 3, 3, 3, 4);
        List<Integer> distinct = numbersWithDuplicates.stream()
                                                     .distinct()
                                                     .collect(Collectors.toList());
        System.out.println("Únicos: " + distinct);
        // Saída: Únicos: [1, 2, 3, 4]
        
        // 6. Sorted - ordenar
        List<String> sorted = words.stream()
                                  .sorted()
                                  .collect(Collectors.toList());
        System.out.println("Ordenado: " + sorted);
        // Saída: Ordenado: [C++, Go, Java, JavaScript, Python, Ruby, Rust]
        
        // 7. Limit - limitar quantidade
        List<String> limited = words.stream()
                                   .limit(3)
                                   .collect(Collectors.toList());
        System.out.println("Primeiros 3: " + limited);
        // Saída: Primeiros 3: [Java, Python, JavaScript]
        
        // 8. Skip - pular elementos
        List<String> skipped = words.stream()
                                   .skip(2)
                                   .collect(Collectors.toList());
        System.out.println("Pulando 2: " + skipped);
        // Saída: Pulando 2: [JavaScript, C++, Ruby, Go, Rust]
        
        // 9. FlatMap - achatar streams
        List<List<Integer>> nestedList = Arrays.asList(
            Arrays.asList(1, 2),
            Arrays.asList(3, 4),
            Arrays.asList(5, 6)
        );
        List<Integer> flattened = nestedList.stream()
                                           .flatMap(List::stream)
                                           .collect(Collectors.toList());
        System.out.println("Achatado: " + flattened);
        // Saída: Achatado: [1, 2, 3, 4, 5, 6]
        
        // 10. Peek - debug/efeitos colaterais
        List<Integer> peeked = Arrays.asList(1, 2, 3, 4, 5).stream()
                                    .peek(n -> System.out.print("Processando " + n + ", "))
                                    .map(n -> n * 2)
                                    .collect(Collectors.toList());
        System.out.println("\nResultado: " + peeked);
        // Saída: 
        // Processando 1, Processando 2, Processando 3, Processando 4, Processando 5,
        // Resultado: [2, 4, 6, 8, 10]
    }
    
    // Operações terminais
    public static void terminalOperations() {
        System.out.println("\n=== Operações Terminais ===\n");
        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        
        // 11. Count - contar elementos
        long count = numbers.stream()
                           .filter(n -> n > 2)
                           .count();
        System.out.println("Números > 2: " + count);
        // Saída: Números > 2: 3
        
        // 12. Reduce - reduzir a um único valor
        Optional<Integer> sum = numbers.stream()
                                      .reduce((a, b) -> a + b);
        System.out.println("Soma: " + sum.get());
        // Saída: Soma: 15
        
        Integer product = numbers.stream()
                                .reduce(1, (a, b) -> a * b);
        System.out.println("Produto: " + product);
        // Saída: Produto: 120
        
        // 13. Min/Max
        Optional<Integer> min = numbers.stream().min(Integer::compare);
        Optional<Integer> max = numbers.stream().max(Integer::compare);
        System.out.println("Min: " + min.get() + ", Max: " + max.get());
        // Saída: Min: 1, Max: 5
        
        // 14. AnyMatch, AllMatch, NoneMatch
        boolean anyEven = numbers.stream().anyMatch(n -> n % 2 == 0);
        boolean allPositive = numbers.stream().allMatch(n -> n > 0);
        boolean noneNegative = numbers.stream().noneMatch(n -> n < 0);
        
        System.out.println("Algum par? " + anyEven);
        System.out.println("Todos positivos? " + allPositive);
        System.out.println("Nenhum negativo? " + noneNegative);
        // Saída:
        // Algum par? true
        // Todos positivos? true
        // Nenhum negativo? true
        
        // 15. FindFirst, FindAny
        Optional<Integer> first = numbers.stream()
                                        .filter(n -> n > 3)
                                        .findFirst();
        System.out.println("Primeiro > 3: " + first.get());
        // Saída: Primeiro > 3: 4
    }
    
    // Exemplos avançados
    public static void advancedStreamExamples() {
        System.out.println("\n=== Exemplos Avançados ===\n");
        
        List<Product> products = Arrays.asList(
            new Product("Laptop", 1200.0, "Electronics"),
            new Product("Mouse", 25.0, "Electronics"),
            new Product("Desk", 300.0, "Furniture"),
            new Product("Chair", 150.0, "Furniture"),
            new Product("Monitor", 400.0, "Electronics")
        );
        
        // 16. Grouping
        Map<String, List<Product>> byCategory = products.stream()
            .collect(Collectors.groupingBy(Product::getCategory));
        
        System.out.println("Agrupado por categoria:");
        byCategory.forEach((category, prods) -> {
            System.out.println(category + ": " + prods.size() + " produtos");
        });
        // Saída:
        // Agrupado por categoria:
        // Electronics: 3 produtos
        // Furniture: 2 produtos
        
        // 17. Partitioning
        Map<Boolean, List<Product>> partitioned = products.stream()
            .collect(Collectors.partitioningBy(p -> p.getPrice() > 200));
        
        System.out.println("\nCaros (> 200): " + partitioned.get(true).size());
        System.out.println("Baratos (<= 200): " + partitioned.get(false).size());
        // Saída:
        // Caros (> 200): 3
        // Baratos (<= 200): 2
        
        // 18. Summarizing
        DoubleSummaryStatistics stats = products.stream()
            .collect(Collectors.summarizingDouble(Product::getPrice));
        
        System.out.println("\nEstatísticas de preço:");
        System.out.println("Média: " + stats.getAverage());
        System.out.println("Total: " + stats.getSum());
        System.out.println("Min: " + stats.getMin());
        System.out.println("Max: " + stats.getMax());
        // Saída:
        // Estatísticas de preço:
        // Média: 415.0
        // Total: 2075.0
        // Min: 25.0
        // Max: 1200.0
        
        // 19. Joining strings
        String productNames = products.stream()
            .map(Product::getName)
            .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("\nProdutos: " + productNames);
        // Saída: Produtos: [Laptop, Mouse, Desk, Chair, Monitor]
        
        // 20. Custom Collector
        Double totalRevenue = products.stream()
            .collect(Collectors.summingDouble(Product::getPrice));
        System.out.println("Receita total: $" + totalRevenue);
        // Saída: Receita total: $2075.0
    }
    
    // Parallel Streams
    public static void parallelStreams() {
        System.out.println("\n=== Parallel Streams ===\n");
        
        List<Integer> numbers = IntStream.rangeClosed(1, 1000)
                                        .boxed()
                                        .collect(Collectors.toList());
        
        // 21. Stream sequencial
        long startSeq = System.currentTimeMillis();
        long sumSeq = numbers.stream()
                            .mapToLong(Integer::longValue)
                            .sum();
        long endSeq = System.currentTimeMillis();
        System.out.println("Sequential sum: " + sumSeq + " (" + (endSeq - startSeq) + "ms)");
        
        // 22. Stream paralelo
        long startPar = System.currentTimeMillis();
        long sumPar = numbers.parallelStream()
                            .mapToLong(Integer::longValue)
                            .sum();
        long endPar = System.currentTimeMillis();
        System.out.println("Parallel sum: " + sumPar + " (" + (endPar - startPar) + "ms)");
        // Saída: (tempos variam)
        // Sequential sum: 500500 (Xms)
        // Parallel sum: 500500 (Yms)
    }
    
    // Classe auxiliar
    static class Product {
        private String name;
        private double price;
        private String category;
        
        public Product(String name, double price, String category) {
            this.name = name;
            this.price = price;
            this.category = category;
        }
        
        public String getName() { return name; }
        public double getPrice() { return price; }
        public String getCategory() { return category; }
        
        @Override
        public String toString() {
            return name + "($" + price + ")";
        }
    }
}
```

---

## 5. Optional

### Conceito
Optional é um container que pode ou não conter um valor não-nulo. Evita NullPointerException.

```java
import java.util.*;

public class OptionalExamples {
    
    public static void main(String[] args) {
        
        basicOptional();
        optionalMethods();
        practicalExamples();
    }
    
    public static void basicOptional() {
        System.out.println("=== Optional Básico ===\n");
        
        // 1. Criando Optional
        Optional<String> empty = Optional.empty();
        Optional<String> nonEmpty = Optional.of("Hello");
        Optional<String> nullable = Optional.ofNullable(null);
        
        System.out.println("Empty presente? " + empty.isPresent());
        System.out.println("NonEmpty presente? " + nonEmpty.isPresent());
        System.out.println("Nullable presente? " + nullable.isPresent());
        // Saída:
        // Empty presente? false
        // NonEmpty presente? true
        // Nullable presente? false
        
        // 2. Obtendo valores
        String value1 = nonEmpty.get(); // Perigoso se vazio!
        String value2 = empty.orElse("Default");
        String value3 = nullable.orElseGet(() -> "Computed Default");
        
        System.out.println("Valor 1: " + value1);
        System.out.println("Valor 2: " + value2);
        System.out.println("Valor 3: " + value3);
        // Saída:
        // Valor 1: Hello
        // Valor 2: Default
        // Valor 3: Computed Default
    }
    
    public static void optionalMethods() {
        System.out.println("\n=== Métodos do Optional ===\n");
        
        Optional<String> opt = Optional.of("Java 8");
        
        // 3. ifPresent - executa ação se presente
        opt.ifPresent(s -> System.out.println("Valor: " + s));
        // Saída: Valor: Java 8
        
        // 4. map - transformar valor
        Optional<Integer> length = opt.map(String::length);
        System.out.println("Tamanho: " + length.get());
        // Saída: Tamanho: 6
        
        // 5. flatMap - transformar para outro Optional
        Optional<String> upper = opt.flatMap(s -> Optional.of(s.toUpperCase()));
        System.out.println("Maiúsculo: " + upper.get());
        // Saída: Maiúsculo: JAVA 8
        
        // 6. filter - filtrar valor
        Optional<String> filtered = opt.filter(s -> s.startsWith("Java"));
        System.out.println("Filtrado presente? " + filtered.isPresent());
        // Saída: Filtrado presente? true
        
        Optional<String> notFiltered = opt.filter(s -> s.startsWith("Python"));
        System.out.println("Não filtrado presente? " + notFiltered.isPresent());
        // Saída: Não filtrado presente? false
        
        // 7. orElseThrow - lançar exceção se vazio
        try {
            String value = Optional.<String>empty()
                .orElseThrow(() -> new RuntimeException("Valor ausente"));
        } catch (RuntimeException e) {
            System.out.println("Exceção: " + e.getMessage());
            // Saída: Exceção: Valor ausente
        }
    }
    
    public static void practicalExamples() {
        System.out.println("\n=== Exemplos Práticos ===\n");
        
        // 8. Encontrar usuário
        User user = findUserById(1).orElse(new User(0, "Guest"));
        System.out.println("Usuário: " + user);
        // Saída: Usuário: User{id=1, name='João'}
        
        User guest = findUserById(999).orElse(new User(0, "Guest"));
        System.out.println("Usuário não encontrado: " + guest);
        // Saída: Usuário não encontrado: User{id=0, name='Guest'}
        
        // 9. Encadeamento de Optional
        Optional<String> userName = findUserById(1)
            .map(User::getName)
            .map(String::toUpperCase);
        
        userName.ifPresent(name -> System.out.println("Nome em maiúsculo: " + name));
        // Saída: Nome em maiúsculo: JOÃO
        
        // 10. Optional em Streams
        List<Optional<String>> listOfOptionals = Arrays.asList(
            Optional.of("A"),
            Optional.empty(),
            Optional.of("B"),
            Optional.empty(),
            Optional.of("C")
        );
        
        List<String> filteredList = listOfOptionals.stream()
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(java.util.stream.Collectors.toList());
        
        System.out.println("Valores presentes: " + filteredList);
        // Saída: Valores presentes: [A, B, C]
        
        // 11. Evitando NPE
        String result = getNullableString()
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .orElse("String vazia ou nula");
        
        System.out.println("Resultado: " + result);
        // Saída: Resultado: String vazia ou nula
    }
    
    // Métodos auxiliares
    static Optional<User> findUserById(int id) {
        if (id == 1) {
            return Optional.of(new User(1, "João"));
        }
        return Optional.empty();
    }
    
    static Optional<String> getNullableString() {
        return Optional.ofNullable(null);
    }
    
    // Classe auxiliar
    static class User {
        private int id;
        private String name;
        
        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        
        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "'}";
        }
    }
}
```

---

## 6. Default Methods

### Conceito
Métodos default permitem adicionar novos métodos a interfaces sem quebrar implementações existentes.

```java
// Interface com métodos default
interface Vehicle {
    // Método abstrato
    void start();
    
    // Método default
    default void stop() {
        System.out.println("Veículo parando...");
    }
    
    default void honk() {
        System.out.println("Beep beep!");
    }
    
    // Método static
    static int getWheels() {
        return 4;
    }
}

// Implementação 1
class Car implements Vehicle {
    @Override
    public void start() {
        System.out.println("Carro ligando motor...");
    }
    
    // Pode sobrescrever método default
    @Override
    public void honk() {
        System.out.println("Buzina de carro: Fon fon!");
    }
}

// Implementação 2
class Bicycle implements Vehicle {
    @Override
    public void start() {
        System.out.println("Bicicleta pronta para pedalar!");
    }
    
    // Usa o método default stop()
    // Usa o método default honk()
}

public class DefaultMethodsExamples {
    
    public static void main(String[] args) {
        
        System.out.println("=== Default Methods ===\n");
        
        // Carro
        Car car = new Car();
        car.start();
        car.stop();
        car.honk();
        // Saída:
        // Carro ligando motor...
        // Veículo parando...
        // Buzina de carro: Fon fon!
        
        System.out.println();
        
        // Bicicleta
        Bicycle bike = new Bicycle();
        bike.start();
        bike.stop();
        bike.honk();
        // Saída:
        // Bicicleta pronta para pedalar!
        // Veículo parando...
        // Beep beep!
        
        System.out.println();
        
        // Método estático
        System.out.println("Rodas padrão: " + Vehicle.getWheels());
        // Saída: Rodas padrão: 4
        
        demonstrateMultipleInheritance();
        demonstrateCollectionDefaults();
    }
    
    // Herança múltipla de comportamento
    static void demonstrateMultipleInheritance() {
        System.out.println("\n=== Herança Múltipla ===\n");
        
        interface Flyable {
            default void fly() {
                System.out.println("Voando...");
            }
        }
        
        interface Swimmable {
            default void swim() {
                System.out.println("Nadando...");
            }
        }
        
        class Duck implements Flyable, Swimmable {
            public void quack() {
                System.out.println("Quack quack!");
            }
        }
        
        Duck duck = new Duck();
        duck.fly();
        duck.swim();
        duck.quack();
        // Saída:
        // Voando...
        // Nadando...
        // Quack quack!
    }
    
    // Métodos default em Collections
    static void demonstrateCollectionDefaults() {
        System.out.println("\n=== Default Methods em Collections ===\n");
        
        List<String> names = new ArrayList<>(Arrays.asList("Ana", "Bruno", "Carlos"));
        
        // forEach (default em Iterable)
        System.out.print("Nomes: ");
        names.forEach(name -> System.out.print(name + " "));
        System.out.println();
        // Saída: Nomes: Ana Bruno Carlos
        
        // replaceAll (default em List)
        names.replaceAll(String::toUpperCase);
        System.out.println("Maiúsculas: " + names);
        // Saída: Maiúsculas: [ANA, BRUNO, CARLOS]
        
        // removeIf (default em Collection)
        names.removeIf(name -> name.startsWith("B"));
        System.out.println("Sem B: " + names);
        // Saída: Sem B: [ANA, CARLOS]
        
        // Map defaults
        Map<String, Integer> scores = new HashMap<>();
        scores.put("João", 85);
        scores.put("Maria", 92);
        
        // getOrDefault
        int score = scores.getOrDefault("Pedro", 0);
        System.out.println("Score de Pedro: " + score);
        // Saída: Score de Pedro: 0
        
        // putIfAbsent
        scores.putIfAbsent("João", 100); // Não substitui
        scores.putIfAbsent("Pedro", 75); // Adiciona
        System.out.println("Scores: " + scores);
        // Saída: Scores: {João=85, Maria=92, Pedro=75}
        
        // computeIfAbsent
        scores.computeIfAbsent("Ana", k -> k.length() * 10);
        System.out.println("Com Ana: " + scores);
        // Saída: Com Ana: {João=85, Maria=92, Pedro=75, Ana=30}
        
        // merge
        scores.merge("João", 10, (old, val) -> old + val);
        System.out.println("João atualizado: " + scores.get("João"));
        // Saída: João atualizado: 95
    }
}
```

---

## 7. Nova API de Data e Hora

### Conceito
Java 8 introduziu a API `java.time` para substituir as problemáticas classes `Date` e `Calendar`.

```java
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;

public class DateTimeExamples {
    
    public static void main(String[] args) {
        
        localDateExamples();
        localTimeExamples();
        localDateTimeExamples();
        zonedDateTimeExamples();
        periodAndDurationExamples();
        formattingAndParsing();
    }
    
    // LocalDate - data sem hora
    static void localDateExamples() {
        System.out.println("=== LocalDate ===\n");
        
        // Criar datas
        LocalDate today = LocalDate.now();
        LocalDate specificDate = LocalDate.of(2024, 12, 25);
        LocalDate parsed = LocalDate.parse("2024-01-01");
        
        System.out.println("Hoje: " + today);
        System.out.println("Natal 2024: " + specificDate);
        System.out.println("Ano novo: " + parsed);
        // Saída: (exemplo)
        // Hoje: 2025-09-29
        // Natal 2024: 2024-12-25
        // Ano novo: 2024-01-01
        
        // Operações com datas
        LocalDate tomorrow = today.plusDays(1);
        LocalDate nextWeek = today.plusWeeks(1);
        LocalDate nextMonth = today.plusMonths(1);
        LocalDate nextYear = today.plusYears(1);
        
        System.out.println("Amanhã: " + tomorrow);
        System.out.println("Próxima semana: " + nextWeek);
        System.out.println("Próximo mês: " + nextMonth);
        System.out.println("Próximo ano: " + nextYear);
        
        // Extrair informações
        int year = today.getYear();
        Month month = today.getMonth();
        int day = today.getDayOfMonth();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        
        System.out.println("\nHoje é: " + dayOfWeek + ", " + day + " de " + month + " de " + year);
        // Saída: Hoje é: MONDAY, 29 de SEPTEMBER de 2025
        
        // Comparações
        boolean isBefore = parsed.isBefore(today);
        boolean isAfter = specificDate.isAfter(today);
        
        System.out.println("\n01/01/2024 é antes de hoje? " + isBefore);
        System.out.println("Natal é depois de hoje? " + isAfter);
    }
    
    // LocalTime - hora sem data
    static void localTimeExamples() {
        System.out.println("\n=== LocalTime ===\n");
        
        // Criar horas
        LocalTime now = LocalTime.now();
        LocalTime specificTime = LocalTime.of(14, 30, 0);
        LocalTime parsed = LocalTime.parse("09:45:30");
        
        System.out.println("Agora: " + now);
        System.out.println("14:30: " + specificTime);
        System.out.println("Parsed: " + parsed);
        
        // Operações
        LocalTime inOneHour = now.plusHours(1);
        LocalTime thirtyMinutesAgo = now.minusMinutes(30);
        
        System.out.println("Daqui 1 hora: " + inOneHour);
        System.out.println("30 minutos atrás: " + thirtyMinutesAgo);
        
        // Extrair informações
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        
        System.out.println("\nAgora são: " + hour + ":" + minute + ":" + second);
    }
    
    // LocalDateTime - data e hora
    static void localDateTimeExamples() {
        System.out.println("\n=== LocalDateTime ===\n");
        
        // Criar
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime specific = LocalDateTime.of(2024, 12, 25, 10, 30, 0);
        
        System.out.println("Agora: " + now);
        System.out.println("Natal às 10:30: " + specific);
        
        // Combinar LocalDate e LocalTime
        LocalDate date = LocalDate.of(2024, 1, 1);
        LocalTime time = LocalTime.of(12, 0);
        LocalDateTime combined = LocalDateTime.of(date, time);
        
        System.out.println("Combinado: " + combined);
        // Saída: Combinado: 2024-01-01T12:00
        
        // Operações
        LocalDateTime later = now.plusDays(5).plusHours(3);
        System.out.println("5 dias e 3 horas depois: " + later);
    }
    
    // ZonedDateTime - data, hora e fuso horário
    static void zonedDateTimeExamples() {
        System.out.println("\n=== ZonedDateTime ===\n");
        
        // Criar com fuso horário
        ZonedDateTime nowInSP = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        ZonedDateTime nowInNY = ZonedDateTime.now(ZoneId.of("America/New_York"));
        ZonedDateTime nowInTokyo = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        
        System.out.println("São Paulo: " + nowInSP);
        System.out.println("Nova York: " + nowInNY);
        System.out.println("Tóquio: " + nowInTokyo);
        
        // Converter entre fusos
        ZonedDateTime spToNy = nowInSP.withZoneSameInstant(ZoneId.of("America/New_York"));
        System.out.println("\nSão Paulo convertido para NY: " + spToNy);
        
        // Listar fusos disponíveis (primeiros 5)
        System.out.println("\nAlguns fusos horários:");
        ZoneId.getAvailableZoneIds().stream()
            .sorted()
            .limit(5)
            .forEach(System.out::println);
    }
    
    // Period e Duration
    static void periodAndDurationExamples() {
        System.out.println("\n=== Period e Duration ===\n");
        
        // Period - para datas (dias, meses, anos)
        LocalDate birthday = LocalDate.of(1990, 5, 15);
        LocalDate today = LocalDate.now();
        
        Period age = Period.between(birthday, today);
        System.out.println("Idade: " + age.getYears() + " anos, " + 
                          age.getMonths() + " meses, " + 
                          age.getDays() + " dias");
        
        Period twoMonths = Period.ofMonths(2);
        LocalDate future = today.plus(twoMonths);
        System.out.println("Daqui 2 meses: " + future);
        
        // Duration - para horas/minutos/segundos
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(17, 30);
        
        Duration workDay = Duration.between(start, end);
        System.out.println("\nDuração do expediente: " + workDay.toHours() + " horas e " + 
                          (workDay.toMinutes() % 60) + " minutos");
        // Saída: Duração do expediente: 8 horas e 30 minutos
        
        Duration twoHours = Duration.ofHours(2);
        LocalTime later = start.plus(twoHours);
        System.out.println("2 horas depois de 9:00: " + later);
        // Saída: 2 horas depois de 9:00: 11:00
    }
    
    // Formatação e Parsing
    static void formattingAndParsing() {
        System.out.println("\n=== Formatação e Parsing ===\n");
        
        LocalDateTime now = LocalDateTime.now();
        
        // Formatadores predefinidos
        String iso = now.format(DateTimeFormatter.ISO_DATE_TIME);
        String basic = now.format(DateTimeFormatter.BASIC_ISO_DATE);
        
        System.out.println("ISO: " + iso);
        System.out.println("Basic: " + basic);
        
        // Formatadores customizados
        DateTimeFormatter custom1 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter custom2 = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", 
                                                                new Locale("pt", "BR"));
        
        System.out.println("\nCustom 1: " + now.format(custom1));
        System.out.println("Custom 2: " + now.format(custom2));
        // Saída exemplo:
        // Custom 1: 29/09/2025 14:30:45
        // Custom 2: segunda-feira, 29 de setembro de 2025
        
        // Parsing
        String dateStr = "25/12/2024 18:00:00";
        LocalDateTime parsed = LocalDateTime.parse(dateStr, custom1);
        System.out.println("\nParsed: " + parsed);
        // Saída: Parsed: 2024-12-25T18:00
        
        // Formatadores com locale
        DateTimeFormatter brFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter usFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        
        LocalDate date = LocalDate.of(2024, 3, 15);
        System.out.println("\nFormato BR: " + date.format(brFormat));
        System.out.println("Formato US: " + date.format(usFormat));
        // Saída:
        // Formato BR: 15/03/2024
        // Formato US: 03/15/2024
    }
}
```

---

## 8. Melhorias na Concorrência

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class ConcurrencyExamples {
    
    public static void main(String[] args) throws Exception {
        
        completableFutureExamples();
        stampedLockExample();
        concurrentAdderExample();
    }
    
    // CompletableFuture
    static void completableFutureExamples() throws Exception {
        System.out.println("=== CompletableFuture ===\n");
        
        // 1. Criação básica
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            sleep(1000);
            return "Resultado assíncrono";
        });
        
        System.out.println("Processando...");
        System.out.println("Resultado: " + future1.get());
        // Saída:
        // Processando...
        // Resultado: Resultado assíncrono
        
        // 2. Encadeamento com thenApply
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> 5)
            .thenApply(n -> n * 2)
            .thenApply(n -> n + 10);
        
        System.out.println("\nResultado encadeado: " + future2.get());
        // Saída: Resultado encadeado: 20
        
        // 3. Combinando futures
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> future4 = CompletableFuture.supplyAsync(() -> "World");
        
        CompletableFuture<String> combined = future3.thenCombine(future4, 
            (s1, s2) -> s1 + " " + s2);
        
        System.out.println("Combinado: " + combined.get());
        // Saída: Combinado: Hello World
        
        // 4. Composição assíncrona
        CompletableFuture<Integer> composed = CompletableFuture.supplyAsync(() -> 10)
            .thenCompose(n -> CompletableFuture.supplyAsync(() -> n * 2));
        
        System.out.println("Composto: " + composed.get());
        // Saída: Composto: 20
        
        // 5. Tratamento de exceções
        CompletableFuture<String> withException = CompletableFuture.supplyAsync(() -> {
            if (true) throw new RuntimeException("Erro proposital");
            return "Nunca executado";
        }).exceptionally(ex -> "Tratado: " + ex.getMessage());
        
        System.out.println("Com exceção: " + withException.get());
        // Saída: Com exceção: Tratado: java.lang.RuntimeException: Erro proposital
        
        // 6. AllOf - esperar múltiplos futures
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            sleep(100);
            return "Task 1";
        });
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            sleep(200);
            return "Task 2";
        });
        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> {
            sleep(150);
            return "Task 3";
        });
        
        CompletableFuture<Void> allOf = CompletableFuture.allOf(f1, f2, f3);
        allOf.get();
        
        System.out.println("\nTodas completadas:");
        System.out.println(f1.get());
        System.out.println(f2.get());
        System.out.println(f3.get());
        // Saída:
        // Todas completadas:
        // Task 1
        // Task 2
        // Task 3
    }
    
    // StampedLock
    static void stampedLockExample() {
        System.out.println("\n=== StampedLock ===\n");
        
        class Point {
            private double x, y;
            private final StampedLock sl = new StampedLock();
            
            void move(double deltaX, double deltaY) {
                long stamp = sl.writeLock();
                try {
                    x += deltaX;
                    y += deltaY;
                } finally {
                    sl.unlockWrite(stamp);
                }
            }
            
            double distanceFromOrigin() {
                long stamp = sl.tryOptimisticRead();
                double currentX = x;
                double currentY = y;
                
                if (!sl.validate(stamp)) {
                    stamp = sl.readLock();
                    try {
                        currentX = x;
                        currentY = y;
                    } finally {
                        sl.unlockRead(stamp);
                    }
                }
                
                return Math.sqrt(currentX * currentX + currentY * currentY);
            }
        }
        
        Point point = new Point();
        point.move(3, 4);
        System.out.println("Distância da origem: " + point.distanceFromOrigin());
        // Saída: Distância da origem: 5.0
    }
    
    // LongAdder
    static void concurrentAdderExample() {
        System.out.println("\n=== LongAdder ===\n");
        
        LongAdder counter = new LongAdder();
        
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> counter.increment());
        }
        
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Contador final: " + counter.sum());
        // Saída: Contador final: 1000
    }
    
    static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

---

## 🎯 Resumo das Features

| Feature | Descrição | Benefício Principal |
|---------|-----------|---------------------|
| **Lambdas** | Funções anônimas | Código mais conciso e funcional |
| **Streams API** | Processamento declarativo de coleções | Operações agregadas elegantes |
| **Optional** | Container para valores nullable | Evita NullPointerException |
| **Default Methods** | Métodos em interfaces | Evolução de APIs sem quebras |
| **Date/Time API** | Nova API de data e hora | API imutável e thread-safe |
| **Method References** | Referências a métodos | Lambdas ainda mais concisos |
| **CompletableFuture** | Programação assíncrona | Operações não-bloqueantes |
| **Nashorn** | Engine JavaScript | Integração Java-JavaScript |

---

## 📚 Referências

- [Oracle Java 8 Documentation](https://docs.oracle.com/javase/8/docs/)
- [Java 8 Stream Tutorial](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)
- [Java 8 Date/Time](https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html)

---

*Java 8 revolucionou a linguagem trazendo paradigma funcional, APIs modernas e maior produtividade para desenvolvedores.*
