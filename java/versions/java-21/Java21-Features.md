# Java 21 - Guia Completo de Features (LTS)

## 📋 Índice
1. [Virtual Threads (Project Loom)](#virtual-threads-project-loom)
2. [Sequenced Collections](#sequenced-collections)
3. [Record Patterns](#record-patterns)
4. [Pattern Matching for switch](#pattern-matching-for-switch)
5. [String Templates (Preview)](#string-templates-preview)
6. [Unnamed Patterns and Variables](#unnamed-patterns-and-variables)
7. [Scoped Values (Preview)](#scoped-values-preview)
8. [Structured Concurrency (Preview)](#structured-concurrency-preview)
9. [Foreign Function & Memory API](#foreign-function--memory-api)
10. [Vector API (Incubator)](#vector-api-incubator)

---

## 1. Virtual Threads (Project Loom)

### Conceito
Virtual threads são threads leves gerenciadas pela JVM, permitindo milhões de threads concorrentes sem overhead do sistema operacional.

### Exemplos Completos

```java
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public class VirtualThreadsExamples {
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== Virtual Threads ===\n");
        
        basicVirtualThreads();
        comparePlatformVsVirtual();
        virtualThreadsWithExecutor();
        structuredTaskScope();
    }
    
    // Criação básica de virtual threads
    static void basicVirtualThreads() throws Exception {
        System.out.println("=== Criação Básica ===\n");
        
        // Método 1: Thread.ofVirtual()
        Thread vThread1 = Thread.ofVirtual().start(() -> {
            System.out.println("Virtual Thread 1: " + Thread.currentThread());
        });
        vThread1.join();
        // Saída: Virtual Thread 1: VirtualThread[#21]/runnable@ForkJoinPool-1-worker-1
        
        // Método 2: Thread.startVirtualThread()
        Thread vThread2 = Thread.startVirtualThread(() -> {
            System.out.println("Virtual Thread 2: " + Thread.currentThread());
            System.out.println("É virtual? " + Thread.currentThread().isVirtual());
        });
        vThread2.join();
        // Saída:
        // Virtual Thread 2: VirtualThread[#22]/runnable@ForkJoinPool-1-worker-2
        // É virtual? true
        
        // Método 3: Thread.ofVirtual().unstarted()
        Thread vThread3 = Thread.ofVirtual().unstarted(() -> {
            System.out.println("Virtual Thread 3 (unstarted, then started)");
        });
        vThread3.start();
        vThread3.join();
        // Saída: Virtual Thread 3 (unstarted, then started)
        
        // Thread platform tradicional para comparação
        Thread platformThread = Thread.ofPlatform().start(() -> {
            System.out.println("Platform Thread: " + Thread.currentThread());
            System.out.println("É virtual? " + Thread.currentThread().isVirtual());
        });
        platformThread.join();
        // Saída:
        // Platform Thread: Thread[#23,Thread-0,5,main]
        // É virtual? false
    }
    
    // Comparação de Performance
    static void comparePlatformVsVirtual() throws Exception {
        System.out.println("\n=== Comparação Platform vs Virtual ===\n");
        
        int numTasks = 10_000;
        
        // Platform Threads (limitado)
        long platformStart = System.currentTimeMillis();
        try (var executor = Executors.newFixedThreadPool(200)) {
            for (int i = 0; i < numTasks; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {}
                });
            }
        }
        long platformTime = System.currentTimeMillis() - platformStart;
        System.out.println("Platform threads (" + numTasks + " tasks): " + platformTime + "ms");
        
        // Virtual Threads (escala facilmente)
        long virtualStart = System.currentTimeMillis();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < numTasks; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {}
                });
            }
        }
        long virtualTime = System.currentTimeMillis() - virtualStart;
        System.out.println("Virtual threads (" + numTasks + " tasks): " + virtualTime + "ms");
        System.out.println("Virtual threads são ~" + (platformTime / virtualTime) + "x mais rápidos");
        // Saída exemplo:
        // Platform threads (10000 tasks): 5234ms
        // Virtual threads (10000 tasks): 187ms
        // Virtual threads são ~28x mais rápidos
    }
    
    // Executor com Virtual Threads
    static void virtualThreadsWithExecutor() throws Exception {
        System.out.println("\n=== Executor com Virtual Threads ===\n");
        
        // Executor que cria uma virtual thread por tarefa
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            
            // Submeter múltiplas tarefas
            List<Future<String>> futures = new ArrayList<>();
            
            for (int i = 1; i <= 5; i++) {
                final int taskNum = i;
                Future<String> future = executor.submit(() -> {
                    Thread.sleep(taskNum * 100);
                    return "Tarefa " + taskNum + " completada em " + 
                           Thread.currentThread().getName();
                });
                futures.add(future);
            }
            
            // Coletar resultados
            for (Future<String> future : futures) {
                System.out.println(future.get());
            }
            // Saída:
            // Tarefa 1 completada em (nome da virtual thread)
            // Tarefa 2 completada em (nome da virtual thread)
            // ...
        }
        
        // Factory customizada
        ThreadFactory factory = Thread.ofVirtual()
            .name("custom-vthread-", 0)
            .factory();
        
        try (var executor = Executors.newThreadPerTaskExecutor(factory)) {
            executor.submit(() -> {
                System.out.println("Thread com nome customizado: " + 
                                  Thread.currentThread().getName());
            }).get();
            // Saída: Thread com nome customizado: custom-vthread-0
        }
    }
    
    // Structured Task Scope
    static void structuredTaskScope() throws Exception {
        System.out.println("\n=== Structured Concurrency ===\n");
        
        record Weather(String city, int temperature) {}
        
        // Simulação de API calls
        Callable<Weather> fetchWeatherSP = () -> {
            Thread.sleep(100);
            return new Weather("São Paulo", 25);
        };
        
        Callable<Weather> fetchWeatherRJ = () -> {
            Thread.sleep(150);
            return new Weather("Rio de Janeiro", 28);
        };
        
        Callable<Weather> fetchWeatherBH = () -> {
            Thread.sleep(120);
            return new Weather("Belo Horizonte", 23);
        };
        
        // Usando StructuredTaskScope.ShutdownOnFailure
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            
            Future<Weather> sp = scope.fork(fetchWeatherSP);
            Future<Weather> rj = scope.fork(fetchWeatherRJ);
            Future<Weather> bh = scope.fork(fetchWeatherBH);
            
            scope.join();          // Aguarda todas
            scope.throwIfFailed(); // Lança exceção se alguma falhou
            
            // Processa resultados
            System.out.println("Temperaturas:");
            System.out.println("  " + sp.resultNow());
            System.out.println("  " + rj.resultNow());
            System.out.println("  " + bh.resultNow());
            // Saída:
            // Temperaturas:
            //   Weather[city=São Paulo, temperature=25]
            //   Weather[city=Rio de Janeiro, temperature=28]
            //   Weather[city=Belo Horizonte, temperature=23]
        }
        
        // ShutdownOnSuccess - primeira a completar
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<Weather>()) {
            
            scope.fork(fetchWeatherSP);
            scope.fork(fetchWeatherRJ);
            scope.fork(fetchWeatherBH);
            
            scope.join();
            
            Weather fastest = scope.result();
            System.out.println("\nResposta mais rápida: " + fastest);
            // Saída: Resposta mais rápida: Weather[city=São Paulo, temperature=25]
        }
    }
    
    // Exemplo prático: Web Scraper
    static class WebScraper {
        
        record PageData(String url, int wordCount, Duration fetchTime) {}
        
        public static List<PageData> scrapePages(List<String> urls) throws Exception {
            
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                
                List<Future<PageData>> futures = urls.stream()
                    .map(url -> executor.submit(() -> scrapePage(url)))
                    .toList();
                
                return futures.stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
            }
        }
        
        private static PageData scrapePage(String url) throws Exception {
            Instant start = Instant.now();
            
            // Simula fetch HTTP
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 500));
            
            // Simula contagem de palavras
            int wordCount = ThreadLocalRandom.current().nextInt(100, 1000);
            
            Duration fetchTime = Duration.between(start, Instant.now());
            
            return new PageData(url, wordCount, fetchTime);
        }
    }
}
```

---

## 2. Sequenced Collections

### Conceito
Nova interface para coleções com ordem definida, fornecendo acesso aos primeiros e últimos elementos.

```java
import java.util.*;

public class SequencedCollectionsExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Sequenced Collections ===\n");
        
        sequencedListExamples();
        sequencedSetExamples();
        sequencedMapExamples();
    }
    
    static void sequencedListExamples() {
        System.out.println("=== SequencedList ===\n");
        
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        
        // Métodos novos
        System.out.println("Lista: " + list);
        System.out.println("Primeiro: " + list.getFirst());
        System.out.println("Último: " + list.getLast());
        // Saída:
        // Lista: [A, B, C, D, E]
        // Primeiro: A
        // Último: E
        
        // Adicionar no início/fim
        list.addFirst("Z");
        list.addLast("Y");
        System.out.println("\nApós addFirst e addLast: " + list);
        // Saída: Após addFirst e addLast: [Z, A, B, C, D, E, Y]
        
        // Remover primeiro/último
        String first = list.removeFirst();
        String last = list.removeLast();
        System.out.println("Removidos: " + first + ", " + last);
        System.out.println("Lista atual: " + list);
        // Saída:
        // Removidos: Z, Y
        // Lista atual: [A, B, C, D, E]
        
        // Reversed view
        List<String> reversed = list.reversed();
        System.out.println("\nReversed: " + reversed);
        System.out.println("Original: " + list);
        // Saída:
        // Reversed: [E, D, C, B, A]
        // Original: [A, B, C, D, E]
        
        // Modificar reversed modifica original
        reversed.set(0, "X");
        System.out.println("\nApós reversed.set(0, 'X'):");
        System.out.println("Reversed: " + reversed);
        System.out.println("Original: " + list);
        // Saída:
        // Após reversed.set(0, 'X'):
        // Reversed: [X, D, C, B, A]
        // Original: [A, B, C, D, X]
    }
    
    static void sequencedSetExamples() {
        System.out.println("\n=== SequencedSet ===\n");
        
        SequencedSet<Integer> set = new LinkedHashSet<>(
            List.of(1, 2, 3, 4, 5)
        );
        
        System.out.println("Set: " + set);
        System.out.println("Primeiro: " + set.getFirst());
        System.out.println("Último: " + set.getLast());
        // Saída:
        // Set: [1, 2, 3, 4, 5]
        // Primeiro: 1
        // Último: 5
        
        // Add first/last (mantém ordem e unicidade)
        set.addFirst(0);
        set.addLast(6);
        System.out.println("\nApós add: " + set);
        // Saída: Após add: [0, 1, 2, 3, 4, 5, 6]
        
        // Reversed
        SequencedSet<Integer> reversedSet = set.reversed();
        System.out.println("Reversed: " + reversedSet);
        // Saída: Reversed: [6, 5, 4, 3, 2, 1, 0]
        
        // TreeSet também é SequencedSet
        TreeSet<String> treeSet = new TreeSet<>(List.of("dog", "cat", "bird", "ant"));
        System.out.println("\nTreeSet: " + treeSet);
        System.out.println("Primeiro (alfabético): " + treeSet.getFirst());
        System.out.println("Último (alfabético): " + treeSet.getLast());
        // Saída:
        // TreeSet: [ant, bird, cat, dog]
        // Primeiro (alfabético): ant
        // Último (alfabético): dog
    }
    
    static void sequencedMapExamples() {
        System.out.println("\n=== SequencedMap ===\n");
        
        SequencedMap<String, Integer> map = new LinkedHashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);
        
        System.out.println("Map: " + map);
        // Saída: Map: {one=1, two=2, three=3, four=4}
        
        // First/Last entry
        Map.Entry<String, Integer> firstEntry = map.firstEntry();
        Map.Entry<String, Integer> lastEntry = map.lastEntry();
        
        System.out.println("Primeira entry: " + firstEntry);
        System.out.println("Última entry: " + lastEntry);
        // Saída:
        // Primeira entry: one=1
        // Última entry: four=4
        
        // Put first/last
        map.putFirst("zero", 0);
        map.putLast("five", 5);
        System.out.println("\nApós putFirst/putLast: " + map);
        // Saída: Após putFirst/putLast: {zero=0, one=1, two=2, three=3, four=4, five=5}
        
        // Reversed map
        SequencedMap<String, Integer> reversed = map.reversed();
        System.out.println("Reversed: " + reversed);
        // Saída: Reversed: {five=5, four=4, three=3, two=2, one=1, zero=0}
        
        // Sequenced views
        SequencedSet<String> keys = map.sequencedKeySet();
        SequencedCollection<Integer> values = map.sequencedValues();
        SequencedSet<Map.Entry<String, Integer>> entries = map.sequencedEntrySet();
        
        System.out.println("\nKeys: " + keys);
        System.out.println("Values: " + values);
        System.out.println("First key: " + keys.getFirst());
        System.out.println("Last value: " + values.getLast());
        // Saída:
        // Keys: [zero, one, two, three, four, five]
        // Values: [0, 1, 2, 3, 4, 5]
        // First key: zero
        // Last value: 5
    }
    
    // Exemplo prático: LRU Cache
    static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int maxSize;
        
        public LRUCache(int maxSize) {
            super(16, 0.75f, true); // access-order
            this.maxSize = maxSize;
        }
        
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxSize;
        }
        
        // Usar métodos sequenced
        public Map.Entry<K, V> getOldest() {
            return this.firstEntry();
        }
        
        public Map.Entry<K, V> getNewest() {
            return this.lastEntry();
        }
    }
}
```

---

## 3. Record Patterns

### Conceito
Desconstrução de records em pattern matching, permitindo extrair componentes diretamente.

```java
public class RecordPatternsExamples {
    
    // Records para exemplos
    record Point(int x, int y) {}
    record Circle(Point center, double radius) {}
    record Rectangle(Point topLeft, Point bottomRight) {}
    
    sealed interface Shape permits Circle, Rectangle {}
    
    record Employee(String name, int age, Address address) {}
    record Address(String street, String city, String zipCode) {}
    
    public static void main(String[] args) {
        System.out.println("=== Record Patterns ===\n");
        
        basicRecordPatterns();
        nestedRecordPatterns();
        recordPatternsInSwitch();
        genericRecordPatterns();
    }
    
    static void basicRecordPatterns() {
        System.out.println("=== Básico ===\n");
        
        Point point = new Point(10, 20);
        
        // ANTES - acesso explícito
        if (point instanceof Point) {
            int x = point.x();
            int y = point.y();
            System.out.println("Ponto antigo: (" + x + ", " + y + ")");
        }
        
        // COM Record Patterns - desconstrução
        if (point instanceof Point(int x, int y)) {
            System.out.println("Ponto novo: (" + x + ", " + y + ")");
            // Saída: Ponto novo: (10, 20)
        }
        
        // Com guards
        if (point instanceof Point(int x, int y) && x > 0 && y > 0) {
            System.out.println("Ponto no quadrante positivo");
            // Saída: Ponto no quadrante positivo
        }
    }
    
    static void nestedRecordPatterns() {
        System.out.println("\n=== Patterns Aninhados ===\n");
        
        Circle circle = new Circle(new Point(5, 10), 7.5);
        
        // Desconstrução aninhada
        if (circle instanceof Circle(Point(int x, int y), double r)) {
            System.out.println("Círculo:");
            System.out.println("  Centro: (" + x + ", " + y + ")");
            System.out.println("  Raio: " + r);
            // Saída:
            // Círculo:
            //   Centro: (5, 10)
            //   Raio: 7.5
        }
        
        // Nested com guard
        if (circle instanceof Circle(Point(int cx, int cy), double radius) 
            && radius > 5.0) {
            System.out.println("\nCírculo grande centrado em (" + cx + ", " + cy + ")");
            // Saída: Círculo grande centrado em (5, 10)
        }
        
        // Múltiplos níveis
        Employee emp = new Employee(
            "João Silva", 
            30, 
            new Address("Av. Paulista", "São Paulo", "01310-100")
        );
        
        if (emp instanceof Employee(String name, int age, 
                                    Address(String street, String city, String zip))) {
            System.out.println("\nEmpregado:");
            System.out.println("  Nome: " + name);
            System.out.println("  Idade: " + age);
            System.out.println("  Cidade: " + city);
            // Saída:
            // Empregado:
            //   Nome: João Silva
            //   Idade: 30
            //   Cidade: São Paulo
        }
    }
    
    static void recordPatternsInSwitch() {
        System.out.println("\n=== Record Patterns em Switch ===\n");
        
        Object shape = new Circle(new Point(0, 0), 5.0);
        
        String description = switch (shape) {
            case Circle(Point(int x, int y), double r) -> 
                String.format("Círculo em (%d,%d) com raio %.1f", x, y, r);
            
            case Rectangle(Point(int x1, int y1), Point(int x2, int y2)) -> 
                String.format("Retângulo de (%d,%d) a (%d,%d)", x1, y1, x2, y2);
            
            case null -> "Forma nula";
            
            default -> "Forma desconhecida";
        };
        
        System.out.println(description);
        // Saída: Círculo em (0,0) com raio 5.0
        
        // Exemplo com cálculo
        double area = calculateArea(shape);
        System.out.println("Área: " + area);
        // Saída: Área: 78.53981633974483
    }
    
    static double calculateArea(Object shape) {
        return switch (shape) {
            case Circle(Point(int x, int y), double r) -> 
                Math.PI * r * r;
            
            case Rectangle(Point(int x1, int y1), Point(int x2, int y2)) -> 
                Math.abs((x2 - x1) * (y2 - y1));
            
            case null, default -> 0.0;
        };
    }
    
    static void genericRecordPatterns() {
        System.out.println("\n=== Generic Record Patterns ===\n");
        
        record Pair<T, U>(T first, U second) {}
        record Box<T>(T value) {}
        
        Object obj = new Pair<>("Name", 42);
        
        if (obj instanceof Pair(String name, Integer age)) {
            System.out.println("Par: " + name + ", " + age);
            // Saída: Par: Name, 42
        }
        
        // Nested generic
        Object nested = new Box<>(new Pair<>("Key", "Value"));
        
        if (nested instanceof Box(Pair(String k, String v))) {
            System.out.println("Box contém: " + k + " = " + v);
            // Saída: Box contém: Key = Value
        }
        
        // Lista de pares
        List<Pair<String, Integer>> pairs = List.of(
            new Pair<>("Alice", 25),
            new Pair<>("Bob", 30),
            new Pair<>("Charlie", 35)
        );
        
        System.out.println("\nPessoas:");
        for (var pair : pairs) {
            if (pair instanceof Pair(String name, Integer age)) {
                System.out.println("  " + name + ": " + age + " anos");
            }
        }
        // Saída:
        // Pessoas:
        //   Alice: 25 anos
        //   Bob: 30 anos
        //   Charlie: 35 anos
    }
}
```

---

## 4. Pattern Matching for switch (Final)

### Conceito
Switch expressions com pattern matching completo, incluindo guards e null handling.

```java
public class SwitchPatternMatchingExamples {
    
    sealed interface Payment {}
    record Cash(double amount) implements Payment {}
    record CreditCard(double amount, String number, String cvv) implements Payment {}
    record DebitCard(double amount, String number, String pin) implements Payment {}
    record DigitalWallet(double amount, String provider, String email) implements Payment {}
    
    public static void main(String[] args) {
        System.out.println("=== Pattern Matching for Switch ===\n");
        
        guardedPatterns();
        nullHandling();
        exhaustiveness();
        practicalExample();
    }
    
    static void guardedPatterns() {
        System.out.println("=== Guarded Patterns ===\n");
        
        Object value = 150;
        
        String result = switch (value) {
            case Integer i when i < 0 -> 
                "Negativo: " + i;
            case Integer i when i == 0 -> 
                "Zero";
            case Integer i when i > 0 && i <= 100 -> 
                "Pequeno positivo: " + i;
            case Integer i when i > 100 && i <= 1000 -> 
                "Médio positivo: " + i;
            case Integer i -> 
                "Grande positivo: " + i;
            case String s when s.isEmpty() -> 
                "String vazia";
            case String s -> 
                "String: " + s;
            case null -> 
                "Null";
            default -> 
                "Outro tipo";
        };
        
        System.out.println(result);
        // Saída: Médio positivo: 150
        
        // Com múltiplas condições
        Object obj = "Hello World";
        
        String classification = switch (obj) {
            case String s when s.length() < 5 -> "Curta";
            case String s when s.length() >= 5 && s.length() <= 10 -> "Média";
            case String s when s.length() > 10 -> "Longa";
            case Integer i when i % 2 == 0 -> "Par";
            case Integer i -> "Ímpar";
            default -> "Outro";
        };
        
        System.out.println("Classificação: " + classification);
        // Saída: Classificação: Longa
    }
    
    static void nullHandling() {
        System.out.println("\n=== Null Handling ===\n");
        
        String value = null;
        
        // Null case explícito
        String result1 = switch (value) {
            case null -> "É null";
            case String s -> "String: " + s;
        };
        System.out.println(result1);
        // Saída: É null
        
        // Null combinado com default
        Integer num = null;
        
        String result2 = switch (num) {
            case null, default -> "Null ou outro";
            case Integer i when i > 0 -> "Positivo";
            case Integer i -> "Zero ou negativo";
        };
        System.out.println(result2);
        // Saída: Null ou outro
        
        // Total pattern (aceita qualquer valor incluindo null)
        Object anything = null;
        
        String result3 = switch (anything) {
            case String s -> "String";
            case Integer i -> "Integer";
            case null, default -> "Null ou outro tipo";
        };
        System.out.println(result3);
        // Saída: Null ou outro tipo
    }
    
    static void exhaustiveness() {
        System.out.println("\n=== Exhaustiveness ===\n");
        
        // Com sealed types, o compilador verifica exaustividade
        Payment payment = new CreditCard(100.0, "1234-5678", "123");
        
        String processingMethod = switch (payment) {
            case Cash(double amount) -> 
                "Processar R$ " + amount + " em dinheiro";
            
            case CreditCard(double amount, String number, String cvv) -> 
                "Processar R$ " + amount + " no cartão " + 
                number.substring(number.length() - 4);
            
            case DebitCard(double amount, String number, String pin) -> 
                "Processar R$ " + amount + " no débito";
            
            case DigitalWallet(double amount, String provider, String email) -> 
                "Processar R$ " + amount + " via " + provider;
            
            // Não precisa de default - sealed type garante exaustividade
        };
        
        System.out.println(processingMethod);
        // Saída: Processar R$ 100.0 no cartão 5678
        
        // Com guards
        Payment largePayment = new Cash(5000.0);
        
        boolean requiresApproval = switch (largePayment) {
            case Cash(double amt) when amt > 1000 -> true;
            case CreditCard(double amt, String n, String c) when amt > 5000 -> true;
            case DebitCard(double amt, String n, String p) when amt > 3000 -> true;
            case DigitalWallet(double amt, String pr, String e) when amt > 2000 -> true;
            default -> false;
        };
        
        System.out.println("Requer aprovação? " + requiresApproval);
        // Saída: Requer aprovação? true
    }
    
    static void practicalExample() {
        System.out.println("\n=== Exemplo Prático: Processador de Comandos ===\n");
        
        sealed interface Command {}
        record CreateUser(String name, String email) implements Command {}
        record DeleteUser(int userId) implements Command {}
        record UpdateUser(int userId, String newName, String newEmail) implements Command {}
        record ListUsers(int page, int pageSize) implements Command {}
        
        Command cmd = new UpdateUser(123, "João Silva", "joao@example.com");
        
        String result = switch (cmd) {
            case CreateUser(String name, String email) -> {
                // Validação
                if (email.contains("@")) {
                    yield "Criar usuário: " + name + " (" + email + ")";
                } else {
                    yield "Email inválido";
                }
            }
            
            case DeleteUser(int id) when id > 0 -> 
                "Deletar usuário #" + id;
            
            case UpdateUser(int id, String name, String email) 
                when id > 0 && email.contains("@") -> 
                "Atualizar usuário #" + id + ": " + name + " (" + email + ")";
            
            case ListUsers(int page, int size) when page > 0 && size > 0 -> 
                "Listar usuários: página " + page + ", " + size + " por página";
            
            default -> 
                "Comando inválido";
        };
        
        System.out.println(result);
        // Saída: Atualizar usuário #123: João Silva (joao@example.com)
    }
}
```

---

## 5. String Templates (Preview)

### Conceito
Interpolação de strings segura e expressiva (Feature Preview em Java 21).

```java
public class StringTemplatesExamples {
    
    public static void main(String[] args) {
        System.out.println("=== String Templates (Preview) ===\n");
        
        basicTemplates();
        customProcessors();
        sqlTemplates();
    }
    
    static void basicTemplates() {
        System.out.println("=== Básico ===\n");
        
        String name = "João";
        int age = 30;
        double salary = 5500.50;
        
        // STR - String Template Processor padrão
        String message = STR."Olá, \{name}! Você tem \{age} anos.";
        System.out.println(message);
        // Saída: Olá, João! Você tem 30 anos.
        
        // Com expressões
        String info = STR."Salário: R$ \{salary}, Anual: R$ \{salary * 12}";
        System.out.println(info);
        // Saída: Salário: R$ 5500.5, Anual: R$ 66006.0
        
        // Multi-linha
        String report = STR."""
                Nome: \{name}
                Idade: \{age}
                Salário: R$ \{String.format("%.2f", salary)}
                """;
        System.out.println(report);
        // Saída:
        // Nome: João
        // Idade: 30
        // Salário: R$ 5500.50
        
        // Expressões complexas
        record Person(String name, int age) {}
        Person person = new Person("Maria", 25);
        
        String description = STR."Pessoa: \{person.name()}, \{person.age()} anos";
        System.out.println(description);
        // Saída: Pessoa: Maria, 25 anos
    }
    
    static void customProcessors() {
        System.out.println("\n=== FMT Processor ===\n");
        
        double value = 1234.5678;
        
        // FMT - Formatação
        String formatted = FMT."Valor: %.2f\{value}";
        System.out.println(formatted);
        // Saída: Valor: 1234.57
        
        int quantity = 5;
        double price = 29.99;
        
        String invoice = FMT."""
                Quantidade: %d\{quantity}
                Preço unitário: R$ %.2f\{price}
                Total: R$ %.2f\{quantity * price}
                """;
        System.out.println(invoice);
        // Saída:
        // Quantidade: 5
        // Preço unitário: R$ 29.99
        // Total: R$ 149.95
    }
    
    static void sqlTemplates() {
        System.out.println("\n=== RAW Templates ===\n");
        
        // RAW - para processamento customizado
        String userId = "123";
        String status = "active";
        
        // Simula SQL preparado (evita SQL injection)
        StringTemplate template = RAW."""
                SELECT * FROM users
                WHERE id = \{userId}
                AND status = '\{status}'
                """;
        
        System.out.println("Template parts:");
        System.out.println(template.fragments());
        System.out.println("\nTemplate values:");
        System.out.println(template.values());
        // Saída mostra fragmentos e valores separados
        
        // Uso seguro com prepared statements
        String query = STR."""
                SELECT * FROM users
                WHERE id = ?
                AND status = ?
                """;
        List<Object> params = List.of(userId, status);
        
        System.out.println("\nQuery: " + query);
        System.out.println("Params: " + params);
    }
    
    // Custom Processor
    static StringTemplate.Processor<String, RuntimeException> UPPER = 
        st -> st.interpolate().toUpperCase();
    
    static void customProcessor() {
        String name = "joão";
        String result = UPPER."Nome: \{name}";
        System.out.println(result);
        // Saída: NOME: JOÃO
    }
}
```

---

## 6. Unnamed Patterns and Variables

### Conceito
Usar `_` para indicar variáveis não utilizadas, tornando o código mais claro.

```java
import java.util.*;

public class UnnamedPatternsExamples {
    
    record Point(int x, int y, int z) {}
    record Person(String name, int age, String email) {}
    
    public static void main(String[] args) {
        System.out.println("=== Unnamed Patterns and Variables ===\n");
        
        unnamedInPatterns();
        unnamedInLambdas();
        unnamedInTryCatch();
    }
    
    static void unnamedInPatterns() {
        System.out.println("=== Unnamed em Patterns ===\n");
        
        Point point = new Point(10, 20, 30);
        
        // Interesse apenas no x
        if (point instanceof Point(int x, _, _)) {
            System.out.println("X: " + x);
            // y e z são ignorados
            // Saída: X: 10
        }
        
        // Interesse apenas no z
        if (point instanceof Point(_, _, int z)) {
            System.out.println("Z: " + z);
            // Saída: Z: 30
        }
        
        // Em switch
        Person person = new Person("João", 30, "joao@example.com");
        
        String result = switch (person) {
            case Person(String name, _, _) -> 
                "Nome: " + name; // Ignora age e email
        };
        
        System.out.println(result);
        // Saída: Nome: João
        
        // Lista de pessoas - interesse apenas no nome
        List<Person> people = List.of(
            new Person("Alice", 25, "alice@example.com"),
            new Person("Bob", 30, "bob@example.com"),
            new Person("Charlie", 35, "charlie@example.com")
        );
        
        System.out.println("\nNomes:");
        for (Person p : people) {
            if (p instanceof Person(String name, _, _)) {
                System.out.println("  " + name);
            }
        }
        // Saída:
        // Nomes:
        //   Alice
        //   Bob
        //   Charlie
    }
    
    static void unnamedInLambdas() {
        System.out.println("\n=== Unnamed em Lambdas ===\n");
        
        List<String> list = List.of("A", "B", "C");
        
        // forEach - não usa o elemento
        list.forEach(_ -> System.out.println("Item"));
        // Saída:
        // Item
        // Item
        // Item
        
        // Map operations - ignora chave ou valor
        Map<String, Integer> map = Map.of(
            "one", 1,
            "two", 2,
            "three", 3
        );
        
        System.out.println("\nApenas valores:");
        map.forEach((_, value) -> System.out.println(value));
        // Saída:
        // Apenas valores:
        // 1
        // 2
        // 3
        
        System.out.println("\nApenas chaves:");
        map.forEach((key, _) -> System.out.println(key));
        // Saída:
        // Apenas chaves:
        // one
        // two
        // three
    }
    
    static void unnamedInTryCatch() {
        System.out.println("\n=== Unnamed em Try-Catch ===\n");
        
        // Exception não usada
        try {
            int result = 10 / 0;
        } catch (ArithmeticException _) {
            System.out.println("Erro de divisão");
            // Não usa a exception
        }
        // Saída: Erro de divisão
        
        // Múltiplos recursos AutoCloseable não usados
        try (var _ = new AutoCloseable() {
                @Override
                public void close() {
                    System.out.println("Resource 1 closed");
                }
            };
            var _ = new AutoCloseable() {
                @Override
                public void close() {
                    System.out.println("Resource 2 closed");
                }
            }) {
            System.out.println("Usando recursos");
        } catch (Exception _) {
            // Ignora exceção
        }
        // Saída:
        // Usando recursos
        // Resource 2 closed
        // Resource 1 closed
    }
}
```

---

## 🎯 Resumo das Features do Java 21

| Feature | Descrição | Benefício |
|---------|-----------|-----------|
| **Virtual Threads** | Threads leves da JVM | Escalabilidade massiva |
| **Sequenced Collections** | APIs para coleções ordenadas | Acesso consistente first/last |
| **Record Patterns** | Desconstrução de records | Código mais conciso |
| **Pattern Matching Switch** | Switch com patterns completo | Expressividade e segurança |
| **String Templates** | Interpolação segura | Strings dinâmicas elegantes |
| **Unnamed Patterns** | `_` para valores ignorados | Intenção clara no código |
| **Structured Concurrency** | Concorrência estruturada | Gerenciamento de tarefas |
| **Scoped Values** | Alternativa a ThreadLocal | Compartilhamento imutável |

---

## 📚 Features em Preview/Incubator

### String Templates (Preview)
- Interpolação de strings
- Processadores customizados
- Segurança contra injection

### Structured Concurrency (Preview)
- StructuredTaskScope
- Gerenciamento hierárquico de tasks
- Shutdown coordenado

### Scoped Values (Preview)
- Compartilhamento imutável entre threads
- Alternativa melhor que ThreadLocal

### Foreign Function & Memory API
- Acesso a código nativo
- Gerenciamento de memória off-heap

---

## 🚀 Migração para Java 21

### Principais Mudanças:
1. Virtual Threads habilitadas por padrão
2. Sequenced Collections em código existente
3. Pattern matching eliminando instanceof+cast
4. Records em todo lugar

### Benefícios Imediatos:
- **Performance**: Virtual threads melhoram throughput
- **Produtividade**: Menos boilerplate code
- **Manutenção**: Código mais expressivo e seguro
- **Escalabilidade**: Milhões de threads concorrentes

---

## 📚 Referências

- [Oracle Java 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444)
- [JEP 431: Sequenced Collections](https://openjdk.org/jeps/431)
- [JEP 440: Record Patterns](https://openjdk.org/jeps/440)
- [JEP 441: Pattern Matching for switch](https://openjdk.org/jeps/441)

---

*Java 21 LTS representa um salto quântico em produtividade, performance e expressividade, tornando Java uma linguagem moderna e competitiva para qualquer tipo de aplicação.*
