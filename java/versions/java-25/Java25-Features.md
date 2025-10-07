# Java 25 - Guia Completo de Features (Preview - Non-LTS)

## 📋 Índice
1. [Flexible Constructor Bodies](#flexible-constructor-bodies)
2. [Primitive Types in Patterns](#primitive-types-in-patterns)
3. [Module Import Declarations](#module-import-declarations)
4. [Stream Gatherers](#stream-gatherers)
5. [Class-File API](#class-file-api)
6. [Vector API Improvements](#vector-api-improvements)
7. [Scoped Values (Third Preview)](#scoped-values-third-preview)
8. [Structured Concurrency (Third Preview)](#structured-concurrency-third-preview)
9. [Foreign Function & Memory API Enhancements](#foreign-function--memory-api-enhancements)
10. [Value Types (Project Valhalla - Preview)](#value-types-project-valhalla)

---

## 1. Flexible Constructor Bodies

### Conceito
Permite statements antes de `super()` ou `this()` em construtores, aumentando a flexibilidade.

```java
public class FlexibleConstructorExamples {
    
    // Exemplo 1: Validação antes de super()
    static class Employee {
        private final String name;
        private final int age;
        
        public Employee(String name, int age) {
            // ANTES: não era possível validar antes de super()
            // super(); // tinha que ser primeiro
            
            // COM Java 25: pode validar antes
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Nome inválido");
            }
            if (age < 18 || age > 65) {
                throw new IllegalArgumentException("Idade inválida: " + age);
            }
            
            // Agora chama super() depois da validação
            super();
            
            this.name = name;
            this.age = age;
        }
    }
    
    // Exemplo 2: Processamento de parâmetros antes de super()
    static class Logger {
        private final String prefix;
        
        public Logger(String rawPrefix) {
            // Processar o parâmetro antes de super()
            String processed = rawPrefix != null 
                ? rawPrefix.trim().toUpperCase() 
                : "DEFAULT";
            
            super();
            this.prefix = "[" + processed + "]";
        }
        
        public void log(String message) {
            System.out.println(prefix + " " + message);
        }
    }
    
    // Exemplo 3: Chamadas de métodos estáticos antes de super()
    static class Configuration {
        private final String configPath;
        private final Map<String, String> settings;
        
        public Configuration(String env) {
            // Computar valores antes de super()
            String resolvedPath = resolveConfigPath(env);
            Map<String, String> defaults = getDefaultSettings();
            
            super();
            
            this.configPath = resolvedPath;
            this.settings = new HashMap<>(defaults);
        }
        
        private static String resolveConfigPath(String env) {
            return switch (env) {
                case "dev" -> "/config/dev.properties";
                case "prod" -> "/config/prod.properties";
                default -> "/config/default.properties";
            };
        }
        
        private static Map<String, String> getDefaultSettings() {
            return Map.of(
                "timeout", "3000",
                "retries", "3",
                "debug", "false"
            );
        }
    }
    
    // Exemplo 4: Construtor encadeado com preparação
    static class Rectangle {
        private final double width;
        private final double height;
        
        public Rectangle(double width, double height) {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Dimensões inválidas");
            }
            super();
            this.width = width;
            this.height = height;
        }
        
        public Rectangle(double side) {
            // Validação antes de this()
            if (side <= 0) {
                throw new IllegalArgumentException("Lado inválido: " + side);
            }
            
            // Agora pode chamar this() depois
            this(side, side);
        }
        
        public double area() {
            return width * height;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Flexible Constructor Bodies ===\n");
        
        // Teste Employee
        try {
            Employee emp1 = new Employee("João Silva", 30);
            System.out.println("Empregado criado: OK");
            // Saída: Empregado criado: OK
            
            Employee emp2 = new Employee("", 25);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro esperado: " + e.getMessage());
            // Saída: Erro esperado: Nome inválido
        }
        
        // Teste Logger
        Logger logger1 = new Logger("  app  ");
        logger1.log("Sistema iniciado");
        // Saída: [APP] Sistema iniciado
        
        Logger logger2 = new Logger(null);
        logger2.log("Usando default");
        // Saída: [DEFAULT] Usando default
        
        // Teste Configuration
        Configuration devConfig = new Configuration("dev");
        System.out.println("\nConfig path: " + devConfig.configPath);
        // Saída: Config path: /config/dev.properties
        
        // Teste Rectangle
        Rectangle rect1 = new Rectangle(5.0, 10.0);
        Rectangle square = new Rectangle(7.0);
        
        System.out.println("\nÁrea retângulo: " + rect1.area());
        System.out.println("Área quadrado: " + square.area());
        // Saída:
        // Área retângulo: 50.0
        // Área quadrado: 49.0
    }
}
```

---

## 2. Primitive Types in Patterns

### Conceito
Pattern matching agora suporta tipos primitivos, não apenas tipos de referência.

```java
public class PrimitivePatternExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Primitive Types in Patterns ===\n");
        
        primitiveInstanceof();
        primitiveSwitchPatterns();
        numericConversions();
        practicalExamples();
    }
    
    static void primitiveInstanceof() {
        System.out.println("=== instanceof com Primitivos ===\n");
        
        Object value = 42;
        
        // Pattern matching com primitivos
        if (value instanceof int i) {
            System.out.println("Inteiro: " + i);
            System.out.println("Quadrado: " + (i * i));
        }
        // Saída:
        // Inteiro: 42
        // Quadrado: 1764
        
        // Com long
        Object longValue = 1000L;
        if (longValue instanceof long l) {
            System.out.println("Long: " + l);
            // Saída: Long: 1000
        }
        
        // Com double
        Object doubleValue = 3.14;
        if (doubleValue instanceof double d) {
            System.out.println("Double: " + d);
            System.out.println("Arredondado: " + Math.round(d));
        }
        // Saída:
        // Double: 3.14
        // Arredondado: 3
        
        // Com boolean
        Object boolValue = true;
        if (boolValue instanceof boolean b) {
            System.out.println("Boolean: " + b);
            // Saída: Boolean: true
        }
    }
    
    static void primitiveSwitchPatterns() {
        System.out.println("\n=== Switch com Primitivos ===\n");
        
        Object value = 100;
        
        String result = switch (value) {
            case int i when i < 0 -> 
                "Inteiro negativo: " + i;
            case int i when i == 0 -> 
                "Zero";
            case int i when i > 0 && i <= 100 -> 
                "Inteiro pequeno: " + i;
            case int i -> 
                "Inteiro grande: " + i;
            case long l -> 
                "Long: " + l;
            case double d -> 
                String.format("Double: %.2f", d);
            case float f -> 
                String.format("Float: %.2f", f);
            case boolean b -> 
                "Boolean: " + b;
            default -> 
                "Outro tipo";
        };
        
        System.out.println(result);
        // Saída: Inteiro pequeno: 100
        
        // Operações matemáticas diretas
        Object num = 25.5;
        
        double squared = switch (num) {
            case int i -> i * i;
            case long l -> l * l;
            case double d -> d * d;
            case float f -> f * f;
            default -> 0.0;
        };
        
        System.out.println("Quadrado de " + num + " = " + squared);
        // Saída: Quadrado de 25.5 = 650.25
    }
    
    static void numericConversions() {
        System.out.println("\n=== Conversões Numéricas ===\n");
        
        // Conversão automática em patterns
        Object value = (short) 42;
        
        if (value instanceof short s) {
            System.out.println("Short: " + s);
            int asInt = s; // Widening automático
            System.out.println("Como int: " + asInt);
        }
        // Saída:
        // Short: 42
        // Como int: 42
        
        // Narrowing com validação
        Object bigValue = 1000;
        
        String conversion = switch (bigValue) {
            case int i when i >= Byte.MIN_VALUE && i <= Byte.MAX_VALUE -> {
                byte b = (byte) i;
                yield "Cabe em byte: " + b;
            }
            case int i when i >= Short.MIN_VALUE && i <= Short.MAX_VALUE -> {
                short s = (short) i;
                yield "Cabe em short: " + s;
            }
            case int i -> 
                "Requer int: " + i;
            default -> 
                "Outro tipo";
        };
        
        System.out.println(conversion);
        // Saída: Cabe em short: 1000
    }
    
    static void practicalExamples() {
        System.out.println("\n=== Exemplos Práticos ===\n");
        
        // Calculadora com types diferentes
        System.out.println("Calculadora:");
        System.out.println("5 + 3 = " + calculate(5, 3, "+"));
        System.out.println("10.5 - 2.3 = " + calculate(10.5, 2.3, "-"));
        System.out.println("4L * 7L = " + calculate(4L, 7L, "*"));
        // Saída:
        // Calculadora:
        // 5 + 3 = 8.0
        // 10.5 - 2.3 = 8.2
        // 4L * 7L = 28.0
        
        // Processador de métricas
        List<Object> metrics = List.of(42, 3.14, 100L, true, 2.5f);
        
        System.out.println("\nProcessando métricas:");
        for (Object metric : metrics) {
            processMetric(metric);
        }
        // Saída:
        // Processando métricas:
        // Contador: 42
        // Taxa: 3.14
        // Timestamp: 100
        // Flag: true
        // Percentual: 2.50%
    }
    
    static double calculate(Object a, Object b, String op) {
        return switch (a) {
            case int i1 when b instanceof int i2 -> 
                performOperation(i1, i2, op);
            case double d1 when b instanceof double d2 -> 
                performOperation(d1, d2, op);
            case long l1 when b instanceof long l2 -> 
                performOperation(l1, l2, op);
            case float f1 when b instanceof float f2 -> 
                performOperation(f1, f2, op);
            default -> 
                0.0;
        };
    }
    
    static double performOperation(double a, double b, String op) {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            default -> 0.0;
        };
    }
    
    static void processMetric(Object metric) {
        switch (metric) {
            case int i -> 
                System.out.println("Contador: " + i);
            case double d -> 
                System.out.println("Taxa: " + d);
            case long l -> 
                System.out.println("Timestamp: " + l);
            case boolean b -> 
                System.out.println("Flag: " + b);
            case float f -> 
                System.out.printf("Percentual: %.2f%%\n", f);
            default -> 
                System.out.println("Métrica desconhecida");
        }
    }
}
```

---

## 3. Module Import Declarations

### Conceito
Importar todos os exports de um módulo de uma vez, simplificando imports.

```java
// ANTES - múltiplos imports
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// COM Java 25 - module import
import module java.base;

public class ModuleImportExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Module Import Declarations ===\n");
        
        // Agora pode usar todas as classes exportadas do java.base
        // sem importar cada uma individualmente
        
        List<String> list = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        
        list.add("Java");
        list.add("25");
        
        System.out.println("Lista: " + list);
        // Saída: Lista: [Java, 25]
        
        // Stream API disponível
        String joined = list.stream()
            .collect(Collectors.joining(" "));
        
        System.out.println("Joined: " + joined);
        // Saída: Joined: Java 25
        
        demonstrateCustomModule();
    }
    
    static void demonstrateCustomModule() {
        System.out.println("\n=== Custom Module Import ===\n");
        
        // Exemplo conceitual de um módulo customizado
        // module-info.java do módulo "myapp.data":
        /*
        module myapp.data {
            exports com.myapp.model;
            exports com.myapp.repository;
            exports com.myapp.service;
        }
        */
        
        // No código cliente:
        // import module myapp.data;
        
        // Agora todas as classes exportadas estão disponíveis:
        // - com.myapp.model.User
        // - com.myapp.model.Product
        // - com.myapp.repository.UserRepository
        // - com.myapp.service.UserService
        // etc.
        
        System.out.println("Module imports simplificam projetos modulares");
    }
}

// Exemplo de estrutura de módulo
class ModuleStructureExample {
    
    // module-info.java
    /*
    module myapp.core {
        // Exporta pacotes
        exports com.myapp.api;
        exports com.myapp.util;
        
        // Requer outros módulos
        requires java.sql;
        requires java.logging;
    }
    */
    
    // Uso em outra parte do código:
    /*
    import module myapp.core;
    
    // Todas as classes de com.myapp.api e com.myapp.util disponíveis
    public class Application {
        public static void main(String[] args) {
            ApiClient client = new ApiClient();
            StringUtils utils = new StringUtils();
        }
    }
    */
}
```

---

## 4. Stream Gatherers

### Conceito
API extensível para operações intermediárias customizadas em Streams, substituindo collectors complexos.

```java
import java.util.*;
import java.util.stream.*;

public class StreamGatherersExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Stream Gatherers ===\n");
        
        builtInGatherers();
        customGatherers();
        practicalExamples();
    }
    
    static void builtInGatherers() {
        System.out.println("=== Gatherers Built-in ===\n");
        
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // 1. windowFixed - janelas de tamanho fixo
        List<List<Integer>> windows = numbers.stream()
            .gather(Gatherers.windowFixed(3))
            .toList();
        
        System.out.println("Windows de 3: " + windows);
        // Saída: Windows de 3: [[1, 2, 3], [4, 5, 6], [7, 8, 9], [10]]
        
        // 2. windowSliding - janelas deslizantes
        List<List<Integer>> sliding = numbers.stream()
            .limit(5)
            .gather(Gatherers.windowSliding(2))
            .toList();
        
        System.out.println("Sliding de 2: " + sliding);
        // Saída: Sliding de 2: [[1, 2], [2, 3], [3, 4], [4, 5]]
        
        // 3. scan - acumulação progressiva
        List<Integer> accumulated = numbers.stream()
            .gather(Gatherers.scan(() -> 0, (acc, elem) -> acc + elem))
            .toList();
        
        System.out.println("Soma acumulada: " + accumulated);
        // Saída: Soma acumulada: [1, 3, 6, 10, 15, 21, 28, 36, 45, 55]
        
        // 4. fold - reduce com estado
        Optional<Integer> product = numbers.stream()
            .limit(5)
            .gather(Gatherers.fold(() -> 1, (acc, elem) -> acc * elem))
            .findFirst();
        
        System.out.println("Produto (1-5): " + product.get());
        // Saída: Produto (1-5): 120
    }
    
    static void customGatherers() {
        System.out.println("\n=== Gatherers Customizados ===\n");
        
        List<String> words = List.of("Java", "is", "awesome", "and", "powerful");
        
        // Custom Gatherer: apenas palavras longas
        Gatherer<String, ?, String> longWords = Gatherer.of(
            (state, element, downstream) -> {
                if (element.length() > 3) {
                    return downstream.push(element);
                }
                return true;
            }
        );
        
        List<String> filtered = words.stream()
            .gather(longWords)
            .toList();
        
        System.out.println("Palavras longas: " + filtered);
        // Saída: Palavras longas: [Java, awesome, powerful]
        
        // Custom Gatherer: enumerar elementos
        Gatherer<String, ?, String> enumerate = Gatherer.ofSequential(
            () -> new int[]{0}, // estado: contador
            (state, element, downstream) -> {
                int index = state[0]++;
                return downstream.push(index + ": " + element);
            }
        );
        
        List<String> enumerated = words.stream()
            .gather(enumerate)
            .toList();
        
        System.out.println("\nEnumerado:");
        enumerated.forEach(System.out::println);
        // Saída:
        // Enumerado:
        // 0: Java
        // 1: is
        // 2: awesome
        // 3: and
        // 4: powerful
    }
    
    static void practicalExamples() {
        System.out.println("\n=== Exemplos Práticos ===\n");
        
        // 1. Agrupamento dinâmico
        List<Integer> sales = List.of(10, 25, 30, 15, 40, 35, 20, 50, 45);
        
        // Agrupar vendas em lotes que somem até 100
        Gatherer<Integer, ?, List<Integer>> batchBySum = Gatherer.ofSequential(
            ArrayList::new, // estado: lista atual
            (batch, sale, downstream) -> {
                batch.add(sale);
                int sum = batch.stream().mapToInt(Integer::intValue).sum();
                
                if (sum >= 100) {
                    downstream.push(new ArrayList<>(batch));
                    batch.clear();
                }
                return true;
            },
            (batch, downstream) -> {
                if (!batch.isEmpty()) {
                    downstream.push(batch);
                }
            }
        );
        
        List<List<Integer>> batches = sales.stream()
            .gather(batchBySum)
            .toList();
        
        System.out.println("Lotes de vendas:");
        batches.forEach(batch -> {
            int sum = batch.stream().mapToInt(Integer::intValue).sum();
            System.out.println("  " + batch + " (soma: " + sum + ")");
        });
        // Saída:
        // Lotes de vendas:
        //   [10, 25, 30, 15, 40] (soma: 120)
        //   [35, 20, 50] (soma: 105)
        //   [45] (soma: 45)
        
        // 2. Detecção de duplicatas consecutivas
        List<String> logs = List.of("INFO", "INFO", "WARN", "ERROR", "ERROR", "ERROR", "INFO");
        
        Gatherer<String, ?, String> distinctConsecutive = Gatherer.ofSequential(
            () -> new String[]{null}, // estado: último elemento
            (state, element, downstream) -> {
                if (!element.equals(state[0])) {
                    state[0] = element;
                    return downstream.push(element);
                }
                return true;
            }
        );
        
        List<String> deduplicated = logs.stream()
            .gather(distinctConsecutive)
            .toList();
        
        System.out.println("\nLogs deduplicados: " + deduplicated);
        // Saída: Logs deduplicados: [INFO, WARN, ERROR, INFO]
        
        // 3. Média móvel
        List<Double> prices = List.of(10.0, 12.0, 11.0, 13.0, 15.0, 14.0, 16.0);
        
        List<Double> movingAvg = prices.stream()
            .gather(Gatherers.windowSliding(3))
            .map(window -> window.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0))
            .toList();
        
        System.out.println("\nMédia móvel (3): " + movingAvg);
        // Saída: Média móvel (3): [11.0, 12.0, 13.0, 14.0, 15.0]
    }
}
```

---

## 5. Class-File API

### Conceito
Nova API para ler, escrever e transformar arquivos .class, substituindo ASM/BCEL.

```java
import java.lang.classfile.*;
import java.lang.classfile.attribute.*;
import java.lang.constant.*;

public class ClassFileAPIExamples {
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== Class-File API ===\n");
        
        readClassFile();
        transformClassFile();
        createClassFile();
    }
    
    static void readClassFile() throws Exception {
        System.out.println("=== Lendo Class File ===\n");
        
        // Ler bytes de uma classe
        byte[] bytes = ClassFileAPIExamples.class
            .getResourceAsStream("ClassFileAPIExamples.class")
            .readAllBytes();
        
        // Parse da classe
        ClassFile cf = ClassFile.of();
        ClassModel classModel = cf.parse(bytes);
        
        // Informações da classe
        System.out.println("Classe: " + classModel.thisClass().asInternalName());
        System.out.println("Superclasse: " + classModel.superclass().map(c -> c.asInternalName()).orElse("none"));
        System.out.println("Versão: " + classModel.majorVersion() + "." + classModel.minorVersion());
        
        // Interfaces
        System.out.println("\nInterfaces:");
        classModel.interfaces().forEach(i -> 
            System.out.println("  " + i.asInternalName())
        );
        
        // Métodos
        System.out.println("\nMétodos:");
        classModel.methods().forEach(method -> {
            System.out.println("  " + method.methodName() + method.methodType());
        });
        
        // Campos
        System.out.println("\nCampos:");
        classModel.fields().forEach(field -> {
            System.out.println("  " + field.fieldName() + ": " + field.fieldType());
        });
    }
    
    static void transformClassFile() throws Exception {
        System.out.println("\n=== Transformando Class File ===\n");
        
        byte[] originalBytes = ClassFileAPIExamples.class
            .getResourceAsStream("ClassFileAPIExamples.class")
            .readAllBytes();
        
        ClassFile cf = ClassFile.of();
        
        // Transformar: adicionar logging em métodos
        byte[] transformed = cf.transform(
            cf.parse(originalBytes),
            ClassTransform.transformingMethods(
                methodModel -> {
                    // Adiciona print no início de cada método
                    return MethodTransform.ofStateful(
                        () -> new MethodTransformer() {
                            @Override
                            public void accept(MethodElement element) {
                                // Lógica de transformação
                            }
                        }
                    );
                }
            )
        );
        
        System.out.println("Classe transformada: " + transformed.length + " bytes");
    }
    
    static void createClassFile() throws Exception {
        System.out.println("\n=== Criando Class File ===\n");
        
        ClassFile cf = ClassFile.of();
        
        // Criar uma classe simples
        byte[] bytes = cf.build(
            ClassDesc.of("com.example", "GeneratedClass"),
            classBuilder -> {
                // Adicionar construtor
                classBuilder.withMethod(
                    ConstantDescs.INIT_NAME,
                    MethodTypeDesc.of(ConstantDescs.CD_void),
                    ClassFile.ACC_PUBLIC,
                    methodBuilder -> methodBuilder.withCode(codeBuilder -> {
                        codeBuilder.aload(0);
                        codeBuilder.invokespecial(
                            ConstantDescs.CD_Object,
                            ConstantDescs.INIT_NAME,
                            MethodTypeDesc.of(ConstantDescs.CD_void)
                        );
                        codeBuilder.return_();
                    })
                );
                
                // Adicionar método simples
                classBuilder.withMethod(
                    "greet",
                    MethodTypeDesc.of(ConstantDescs.CD_String),
                    ClassFile.ACC_PUBLIC,
                    methodBuilder -> methodBuilder.withCode(codeBuilder -> {
                        codeBuilder.ldc("Hello from generated class!");
                        codeBuilder.areturn();
                    })
                );
            }
        );
        
        System.out.println("Classe criada: " + bytes.length + " bytes");
    }
}
```

---

## 6. Value Types (Project Valhalla - Preview)

### Conceito
Value types são tipos primitivos customizados que não têm identidade, apenas estado.

```java
// Value class - sem identidade de objeto
public primitive class Point {
    private final int x;
    private final int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int x() { return x; }
    public int y() { return y; }
    
    public Point translate(int dx, int dy) {
        return new Point(x + dx, y + dy);
    }
    
    // equals baseado em valor, não identidade
    // gerado automaticamente para value types
}

public class ValueTypesExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Value Types ===\n");
        
        // Value types não têm identidade
        Point p1 = new Point(10, 20);
        Point p2 = new Point(10, 20);
        
        // Comparação por valor, não por referência
        System.out.println("p1 == p2: " + (p1 == p2));
        // Saída: p1 == p2: true (!)
        
        // Imutável por natureza
        Point p3 = p1.translate(5, 5);
        System.out.println("Original: (" + p1.x() + ", " + p1.y() + ")");
        System.out.println("Transladado: (" + p3.x() + ", " + p3.y() + ")");
        // Saída:
        // Original: (10, 20)
        // Transladado: (15, 25)
        
        demonstratePerformance();
        demonstrateArrays();
    }
    
    static void demonstratePerformance() {
        System.out.println("\n=== Performance ===\n");
        
        // Value types são mais eficientes em memória
        // Sem overhead de object header
        // Layout contíguo em arrays
        
        int size = 1_000_000;
        
        // Array de value types
        Point[] points = new Point[size];
        for (int i = 0; i < size; i++) {
            points[i] = new Point(i, i * 2);
        }
        
        // Acesso muito mais eficiente (cache-friendly)
        long sum = 0;
        for (Point p : points) {
            sum += p.x() + p.y();
        }
        
        System.out.println("Soma processada de " + size + " pontos");
        System.out.println("Value types são flat em arrays (sem indireção)");
    }
    
    static void demonstrateArrays() {
        System.out.println("\n=== Arrays de Value Types ===\n");
        
        // Layout contíguo em memória
        Point[] points = {
            new Point(0, 0),
            new Point(1, 1),
            new Point(2, 2)
        };
        
        // Todos os dados estão inline, sem ponteiros
        System.out.println("Array de 3 pontos:");
        for (int i = 0; i < points.length; i++) {
            System.out.println("  [" + i + "]: (" + points[i].x() + ", " + points[i].y() + ")");
        }
        // Saída:
        // Array de 3 pontos:
        //   [0]: (0, 0)
        //   [1]: (1, 1)
        //   [2]: (2, 2)
    }
}

// Outro exemplo: Complex number
primitive class Complex {
    private final double real;
    private final double imaginary;
    
    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }
    
    public double real() { return real; }
    public double imaginary() { return imaginary; }
    
    public Complex add(Complex other) {
        return new Complex(
            this.real + other.real,
            this.imaginary + other.imaginary
        );
    }
    
    public Complex multiply(Complex other) {
        return new Complex(
            this.real * other.real - this.imaginary * other.imaginary,
            this.real * other.imaginary + this.imaginary * other.real
        );
    }
    
    @Override
    public String toString() {
        return real + " + " + imaginary + "i";
    }
}
```

---

## 🎯 Resumo das Features do Java 25

| Feature | Descrição | Status | Benefício |
|---------|-----------|--------|-----------|
| **Flexible Constructors** | Statements antes de super() | Final | Maior flexibilidade |
| **Primitive Patterns** | Pattern matching com primitivos | Final | Mais casos cobertos |
| **Module Imports** | Import de módulos completos | Preview | Menos verbosidade |
| **Stream Gatherers** | Operações intermediárias customizadas | Final | Streams mais poderosos |
| **Class-File API** | Manipulação de bytecode | Final | Substitui ASM/BCEL |
| **Value Types** | Tipos sem identidade | Preview | Performance e memória |
| **Scoped Values** | ThreadLocal moderno | 3rd Preview | Compartilhamento seguro |
| **Structured Concurrency** | Gerenciamento de tasks | 3rd Preview | Concorrência estruturada |

---

## 📚 Roadmap e Features Futuras

### Project Loom (Concorrência)
- ✅ Virtual Threads (Java 21)
- ✅ Structured Concurrency (Preview)
- ✅ Scoped Values (Preview)

### Project Valhalla (Performance)
- 🔄 Value Types (Preview em 25)
- 🔮 Specialized Generics (futuro)
- 🔮 Null-restricted types (futuro)

### Project Amber (Produtividade)
- ✅ Records (Java 16)
- ✅ Pattern Matching (Java 21)
- ✅ Text Blocks (Java 15)
- 🔄 String Templates (Preview)

### Project Panama (Interoperabilidade)
- ✅ Foreign Function & Memory API (Java 22)
- ✅ Vector API (Incubator)
- 🔮 Native integration improvements

---

## 🚀 Quando Usar Java 25

### ✅ Usar se:
- Precisar das features mais recentes
- Desenvolvimento experimental
- Feedback para JDK team
- Non-production environments

### ❌ Evitar se:
- Produção crítica (usar LTS: 21 ou 17)
- Suporte de longo prazo necessário
- Frameworks ainda não suportam

### 📅 Ciclo de Releases:
- **Java 25**: Março 2025 (Non-LTS)
- **Java 26**: Setembro 2025 (Non-LTS)
- **Java 27**: Março 2026 (próximo LTS esperado)

---

## 💡 Melhores Práticas

### 1. Features Preview
```java
// Habilitar preview features
// javac --enable-preview --release 25 MyClass.java
// java --enable-preview MyClass

// Usar com cuidado em produção
```

### 2. Migração Gradual
```java
// Adotar features estáveis primeiro
// - Stream Gatherers ✅
// - Primitive Patterns ✅
// - Class-File API ✅

// Features Preview com cautela
// - String Templates (preview)
// - Value Types (preview)
```

### 3. Performance Testing
```java
// Testar value types vs classes
// Medir impacto de stream gatherers
// Benchmark virtual threads vs platform threads
```

---

## 📚 Referências

- [Oracle Java 25 Documentation](https://docs.oracle.com/en/java/javase/25/)
- [JEP Index](https://openjdk.org/jeps/0)
- [Project Valhalla](https://openjdk.org/projects/valhalla/)
- [Project Loom](https://openjdk.org/projects/loom/)
- [Project Amber](https://openjdk.org/projects/amber/)
- [Project Panama](https://openjdk.org/projects/panama/)

---

*Java 25 representa o estado da arte em inovação Java, trazendo features experimentais que moldarão o futuro da linguagem. Ideal para exploração e feedback, mas prefira LTS para produção.*
