# Java 17 - Guia Completo de Features (LTS)

## 📋 Índice
1. [Sealed Classes](#sealed-classes)
2. [Records](#records)
3. [Pattern Matching for instanceof](#pattern-matching-for-instanceof)
4. [Text Blocks](#text-blocks)
5. [Switch Expressions](#switch-expressions)
6. [Helpful NullPointerExceptions](#helpful-nullpointerexceptions)
7. [Stream API Enhancements](#stream-api-enhancements)
8. [New Methods in Core Classes](#new-methods-in-core-classes)
9. [Foreign Function & Memory API (Incubator)](#foreign-function--memory-api)
10. [Enhanced Pseudo-Random Number Generators](#enhanced-pseudo-random-number-generators)

---

## 1. Sealed Classes

### Conceito
Sealed classes permitem controlar quais classes podem estender ou implementar uma classe/interface, fornecendo mais controle sobre hierarquias de tipos.

### Sintaxe e Exemplos

```java
// Definindo uma sealed class
public sealed class Shape permits Circle, Rectangle, Triangle {
    protected final String name;
    
    protected Shape(String name) {
        this.name = name;
    }
    
    public abstract double area();
}

// Subclasse final - não pode ser estendida
public final class Circle extends Shape {
    private final double radius;
    
    public Circle(double radius) {
        super("Circle");
        this.radius = radius;
    }
    
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
    
    public double getRadius() {
        return radius;
    }
}

// Subclasse sealed - pode ser estendida apenas por Square
public sealed class Rectangle extends Shape permits Square {
    private final double width;
    private final double height;
    
    public Rectangle(double width, double height) {
        super("Rectangle");
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double area() {
        return width * height;
    }
}

// Subclasse final
public final class Square extends Rectangle {
    public Square(double side) {
        super(side, side);
    }
}

// Subclasse non-sealed - pode ser estendida livremente
public non-sealed class Triangle extends Shape {
    private final double base;
    private final double height;
    
    public Triangle(double base, double height) {
        super("Triangle");
        this.base = base;
        this.height = height;
    }
    
    @Override
    public double area() {
        return (base * height) / 2;
    }
}

// Exemplos de uso
public class SealedClassExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Sealed Classes ===\n");
        
        // Criando instâncias
        Circle circle = new Circle(5.0);
        Rectangle rectangle = new Rectangle(4.0, 6.0);
        Square square = new Square(5.0);
        Triangle triangle = new Triangle(4.0, 3.0);
        
        // Calculando áreas
        System.out.println("Área do círculo: " + circle.area());
        System.out.println("Área do retângulo: " + rectangle.area());
        System.out.println("Área do quadrado: " + square.area());
        System.out.println("Área do triângulo: " + triangle.area());
        // Saída:
        // Área do círculo: 78.53981633974483
        // Área do retângulo: 24.0
        // Área do quadrado: 25.0
        // Área do triângulo: 6.0
        
        // Pattern matching com sealed classes
        Shape shape = circle;
        String description = describeShape(shape);
        System.out.println("\nDescrição: " + description);
        // Saída: Descrição: Círculo com raio 5.0
        
        demonstrateSealedInterfaces();
    }
    
    // Pattern matching exhaustivo com sealed classes
    static String describeShape(Shape shape) {
        return switch (shape) {
            case Circle c -> "Círculo com raio " + c.getRadius();
            case Rectangle r -> "Retângulo com área " + r.area();
            case Triangle t -> "Triângulo com área " + t.area();
            // Não precisa de default - o compilador sabe que cobriu todos os casos
        };
    }
    
    static void demonstrateSealedInterfaces() {
        System.out.println("\n=== Sealed Interfaces ===\n");
        
        // Interface selada
        sealed interface Payment permits CreditCard, DebitCard, Cash {
            double amount();
            String method();
        }
        
        record CreditCard(double amount, String cardNumber) implements Payment {
            @Override
            public String method() {
                return "Credit Card ending in " + cardNumber.substring(cardNumber.length() - 4);
            }
        }
        
        record DebitCard(double amount, String cardNumber) implements Payment {
            @Override
            public String method() {
                return "Debit Card ending in " + cardNumber.substring(cardNumber.length() - 4);
            }
        }
        
        record Cash(double amount) implements Payment {
            @Override
            public String method() {
                return "Cash";
            }
        }
        
        // Processamento exhaustivo
        Payment payment = new CreditCard(150.00, "1234-5678-9012-3456");
        String receipt = processPayment(payment);
        System.out.println(receipt);
        // Saída: Processado: R$ 150.0 via Credit Card ending in 3456
    }
    
    static String processPayment(Payment payment) {
        return switch (payment) {
            case CreditCard cc -> "Processado: R$ " + cc.amount() + " via " + cc.method();
            case DebitCard dc -> "Processado: R$ " + dc.amount() + " via " + dc.method();
            case Cash c -> "Recebido: R$ " + c.amount() + " em dinheiro";
        };
    }
}
```

---

## 2. Records

### Conceito
Records são classes imutáveis para transportar dados. O compilador gera automaticamente construtor, getters, equals(), hashCode() e toString().

### Exemplos Completos

```java
// Record básico
public record Person(String name, int age) {
    // Compilador gera automaticamente:
    // - Construtor canônico: Person(String name, int age)
    // - Getters: name() e age()
    // - equals(), hashCode(), toString()
}

// Record com validação
public record Employee(String name, String department, double salary) {
    
    // Compact constructor - validação
    public Employee {
        if (salary < 0) {
            throw new IllegalArgumentException("Salário não pode ser negativo");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        // Não precisa atribuir os campos - o compilador faz isso
    }
    
    // Métodos adicionais
    public Employee withRaise(double percentage) {
        return new Employee(name, department, salary * (1 + percentage / 100));
    }
    
    public boolean isManager() {
        return department.equals("Management");
    }
}

// Record com métodos estáticos
public record Point(int x, int y) {
    
    // Constante
    public static final Point ORIGIN = new Point(0, 0);
    
    // Factory method
    public static Point of(int x, int y) {
        return new Point(x, y);
    }
    
    // Método de instância
    public double distanceFromOrigin() {
        return Math.sqrt(x * x + y * y);
    }
    
    public Point translate(int dx, int dy) {
        return new Point(x + dx, y + dy);
    }
}

// Record genérico
public record Pair<T, U>(T first, U second) {
    
    public static <T, U> Pair<T, U> of(T first, U second) {
        return new Pair<>(first, second);
    }
    
    public Pair<U, T> swap() {
        return new Pair<>(second, first);
    }
}

public class RecordExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Records ===\n");
        
        basicRecordUsage();
        recordWithValidation();
        recordCollections();
        genericRecords();
    }
    
    static void basicRecordUsage() {
        System.out.println("=== Uso Básico ===\n");
        
        // Criação
        Person person = new Person("João Silva", 30);
        
        // Getters automáticos
        System.out.println("Nome: " + person.name());
        System.out.println("Idade: " + person.age());
        // Saída:
        // Nome: João Silva
        // Idade: 30
        
        // toString automático
        System.out.println("Person: " + person);
        // Saída: Person: Person[name=João Silva, age=30]
        
        // equals automático
        Person person2 = new Person("João Silva", 30);
        Person person3 = new Person("Maria Santos", 25);
        
        System.out.println("\nperson equals person2? " + person.equals(person2));
        System.out.println("person equals person3? " + person.equals(person3));
        // Saída:
        // person equals person2? true
        // person equals person3? false
        
        // hashCode automático
        System.out.println("\nHashCode person: " + person.hashCode());
        System.out.println("HashCode person2: " + person2.hashCode());
        // Saída: (mesmos valores)
    }
    
    static void recordWithValidation() {
        System.out.println("\n=== Validação em Records ===\n");
        
        // Criação válida
        Employee emp1 = new Employee("Ana Costa", "Engineering", 5000.0);
        System.out.println("Empregado criado: " + emp1);
        // Saída: Empregado criado: Employee[name=Ana Costa, department=Engineering, salary=5000.0]
        
        // Aumento de salário (retorna novo objeto - imutável)
        Employee emp2 = emp1.withRaise(10);
        System.out.println("Com aumento: " + emp2);
        System.out.println("Original: " + emp1);
        // Saída:
        // Com aumento: Employee[name=Ana Costa, department=Engineering, salary=5500.0]
        // Original: Employee[name=Ana Costa, department=Engineering, salary=5000.0]
        
        // Tentativa de criação inválida
        try {
            Employee invalid = new Employee("Carlos", "IT", -1000);
        } catch (IllegalArgumentException e) {
            System.out.println("\nErro esperado: " + e.getMessage());
            // Saída: Erro esperado: Salário não pode ser negativo
        }
    }
    
    static void recordCollections() {
        System.out.println("\n=== Records em Collections ===\n");
        
        List<Person> people = List.of(
            new Person("Alice", 28),
            new Person("Bob", 35),
            new Person("Charlie", 22),
            new Person("Diana", 31)
        );
        
        // Ordenação
        List<Person> sortedByAge = people.stream()
            .sorted((p1, p2) -> Integer.compare(p1.age(), p2.age()))
            .toList();
        
        System.out.println("Ordenado por idade:");
        sortedByAge.forEach(System.out::println);
        // Saída:
        // Ordenado por idade:
        // Person[name=Charlie, age=22]
        // Person[name=Alice, age=28]
        // Person[name=Diana, age=31]
        // Person[name=Bob, age=35]
        
        // Filtragem
        List<Person> adults = people.stream()
            .filter(p -> p.age() >= 30)
            .toList();
        
        System.out.println("\nAdultos (30+): " + adults.size());
        // Saída: Adultos (30+): 2
        
        // Agrupamento
        Map<Integer, List<Person>> byAge = people.stream()
            .collect(Collectors.groupingBy(Person::age));
        
        System.out.println("Agrupado por idade: " + byAge.size() + " grupos");
        // Saída: Agrupado por idade: 4 grupos
    }
    
    static void genericRecords() {
        System.out.println("\n=== Records Genéricos ===\n");
        
        // Criação
        Pair<String, Integer> nameAge = new Pair<>("João", 30);
        Pair<String, String> keyValue = Pair.of("language", "Java");
        
        System.out.println("Par 1: " + nameAge);
        System.out.println("Par 2: " + keyValue);
        // Saída:
        // Par 1: Pair[first=João, second=30]
        // Par 2: Pair[first=language, second=Java]
        
        // Swap
        Pair<Integer, String> swapped = nameAge.swap();
        System.out.println("Invertido: " + swapped);
        // Saída: Invertido: Pair[first=30, second=João]
        
        // Uso com Point
        Point p1 = Point.ORIGIN;
        Point p2 = Point.of(3, 4);
        
        System.out.println("\nOrigem: " + p1);
        System.out.println("Ponto: " + p2);
        System.out.println("Distância da origem: " + p2.distanceFromOrigin());
        // Saída:
        // Origem: Point[x=0, y=0]
        // Ponto: Point[x=3, y=4]
        // Distância da origem: 5.0
        
        Point p3 = p2.translate(1, 1);
        System.out.println("Transladado: " + p3);
        // Saída: Transladado: Point[x=4, y=5]
    }
}
```

---

## 3. Pattern Matching for instanceof

### Conceito
Simplifica o cast após verificação de tipo, eliminando código boilerplate.

```java
import java.util.*;

public class PatternMatchingExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Pattern Matching for instanceof ===\n");
        
        basicPatternMatching();
        complexPatternMatching();
        patternMatchingInConditions();
    }
    
    static void basicPatternMatching() {
        System.out.println("=== Básico ===\n");
        
        Object obj = "Hello World";
        
        // ANTES do Java 17
        if (obj instanceof String) {
            String str = (String) obj;  // Cast explícito
            System.out.println("Tamanho (old): " + str.length());
        }
        
        // COM Java 17 - Pattern Variable
        if (obj instanceof String str) {
            System.out.println("Tamanho (new): " + str.length());
            // 'str' já está disponível e com cast feito
        }
        // Saída:
        // Tamanho (old): 11
        // Tamanho (new): 11
        
        // Pattern variable em operações
        if (obj instanceof String s && s.length() > 5) {
            System.out.println("String longa: " + s.toUpperCase());
            // Saída: String longa: HELLO WORLD
        }
        
        // Negação
        if (!(obj instanceof String s)) {
            System.out.println("Não é string");
        } else {
            System.out.println("É string: " + s);
            // Saída: É string: Hello World
        }
    }
    
    static void complexPatternMatching() {
        System.out.println("\n=== Pattern Matching Complexo ===\n");
        
        List<Object> mixed = List.of(
            "Java",
            42,
            3.14,
            List.of(1, 2, 3),
            new Person("Alice", 30)
        );
        
        for (Object item : mixed) {
            processItem(item);
        }
        // Saída:
        // String de tamanho 4: JAVA
        // Número inteiro: 42 (par)
        // Número decimal: 3.14
        // Lista com 3 elementos
        // Pessoa: Alice, 30 anos
    }
    
    static void processItem(Object item) {
        if (item instanceof String s && !s.isEmpty()) {
            System.out.println("String de tamanho " + s.length() + ": " + s.toUpperCase());
        } else if (item instanceof Integer num && num % 2 == 0) {
            System.out.println("Número inteiro: " + num + " (par)");
        } else if (item instanceof Integer num) {
            System.out.println("Número inteiro: " + num + " (ímpar)");
        } else if (item instanceof Double d) {
            System.out.println("Número decimal: " + d);
        } else if (item instanceof List<?> list) {
            System.out.println("Lista com " + list.size() + " elementos");
        } else if (item instanceof Person p) {
            System.out.println("Pessoa: " + p.name() + ", " + p.age() + " anos");
        }
    }
    
    static void patternMatchingInConditions() {
        System.out.println("\n=== Pattern Matching em Condições ===\n");
        
        Object value = 100;
        
        // Múltiplas condições
        if (value instanceof Integer i && i > 0 && i < 1000) {
            System.out.println("Inteiro no intervalo: " + i);
            // Saída: Inteiro no intervalo: 100
        }
        
        // Comparações
        String result = getLength(value);
        System.out.println(result);
        // Saída: Não é string
        
        String text = "Java 17";
        result = getLength(text);
        System.out.println(result);
        // Saída: Tamanho: 7
    }
    
    static String getLength(Object obj) {
        return (obj instanceof String s) 
            ? "Tamanho: " + s.length() 
            : "Não é string";
    }
    
    record Person(String name, int age) {}
}
```

---

## 4. Text Blocks

### Conceito
Text blocks facilitam a criação de strings multilinha sem concatenação ou escape characters.

```java
public class TextBlockExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Text Blocks ===\n");
        
        basicTextBlocks();
        formattedTextBlocks();
        jsonAndXmlExamples();
        sqlExamples();
    }
    
    static void basicTextBlocks() {
        System.out.println("=== Básico ===\n");
        
        // ANTES - String multilinha tradicional
        String oldWay = "Linha 1\n" +
                       "Linha 2\n" +
                       "Linha 3";
        
        // COM Text Blocks
        String textBlock = """
                Linha 1
                Linha 2
                Linha 3
                """;
        
        System.out.println("Old way:");
        System.out.println(oldWay);
        System.out.println("\nText block:");
        System.out.println(textBlock);
        // Saída idêntica em ambos
        
        // Indentação automática
        String indented = """
                    Indentado
                        Mais indentado
                    Volta
                """;
        
        System.out.println("Com indentação:");
        System.out.println(indented);
        // Saída:
        // Com indentação:
        // Indentado
        //     Mais indentado
        // Volta
    }
    
    static void formattedTextBlocks() {
        System.out.println("\n=== Text Blocks Formatados ===\n");
        
        String name = "João";
        int age = 30;
        
        // Interpolação com formatted()
        String message = """
                Olá, %s!
                Você tem %d anos.
                Bem-vindo ao Java 17!
                """.formatted(name, age);
        
        System.out.println(message);
        // Saída:
        // Olá, João!
        // Você tem 30 anos.
        // Bem-vindo ao Java 17!
        
        // Com String.format
        String invoice = """
                =====================================
                         NOTA FISCAL
                =====================================
                Cliente: %-20s
                Total:   R$ %8.2f
                Data:    %s
                =====================================
                """.formatted("Maria Silva", 1234.56, "2025-09-29");
        
        System.out.println(invoice);
        // Saída:
        // =====================================
        //          NOTA FISCAL
        // =====================================
        // Cliente: Maria Silva        
        // Total:   R$  1234.56
        // Data:    2025-09-29
        // =====================================
    }
    
    static void jsonAndXmlExamples() {
        System.out.println("\n=== JSON e XML ===\n");
        
        // JSON
        String json = """
                {
                    "name": "João Silva",
                    "age": 30,
                    "email": "joao@example.com",
                    "address": {
                        "street": "Rua das Flores",
                        "city": "São Paulo",
                        "country": "Brasil"
                    },
                    "skills": ["Java", "Python", "JavaScript"]
                }
                """;
        
        System.out.println("JSON:");
        System.out.println(json);
        
        // XML
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <person>
                    <name>João Silva</name>
                    <age>30</age>
                    <email>joao@example.com</email>
                    <address>
                        <street>Rua das Flores</street>
                        <city>São Paulo</city>
                        <country>Brasil</country>
                    </address>
                </person>
                """;
        
        System.out.println("XML:");
        System.out.println(xml);
        
        // HTML
        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Java 17</title>
                </head>
                <body>
                    <h1>Text Blocks</h1>
                    <p>Facilitam muito a vida!</p>
                </body>
                </html>
                """;
        
        System.out.println("HTML:");
        System.out.println(html);
    }
    
    static void sqlExamples() {
        System.out.println("\n=== SQL Queries ===\n");
        
        // Query SQL complexa
        String query = """
                SELECT 
                    u.id,
                    u.name,
                    u.email,
                    COUNT(o.id) as order_count,
                    SUM(o.total) as total_spent
                FROM users u
                LEFT JOIN orders o ON u.id = o.user_id
                WHERE u.active = true
                    AND u.created_at > '2024-01-01'
                GROUP BY u.id, u.name, u.email
                HAVING COUNT(o.id) > 0
                ORDER BY total_spent DESC
                LIMIT 10;
                """;
        
        System.out.println("Query SQL:");
        System.out.println(query);
        
        // Query com parâmetros
        String userId = "123";
        String startDate = "2024-01-01";
        
        String paramQuery = """
                SELECT *
                FROM orders
                WHERE user_id = '%s'
                    AND order_date >= '%s'
                ORDER BY order_date DESC;
                """.formatted(userId, startDate);
        
        System.out.println("Query Parametrizada:");
        System.out.println(paramQuery);
    }
}
```

---

## 5. Switch Expressions

### Conceito
Switch como expressão que retorna valor, com sintaxe arrow e pattern matching.

```java
public class SwitchExpressionExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Switch Expressions ===\n");
        
        basicSwitchExpressions();
        switchWithYield();
        switchWithPatternMatching();
        switchWithNullHandling();
    }
    
    static void basicSwitchExpressions() {
        System.out.println("=== Switch Básico ===\n");
        
        // ANTES - switch statement
        String day = "MONDAY";
        int numLetters;
        switch (day) {
            case "MONDAY":
            case "FRIDAY":
            case "SUNDAY":
                numLetters = 6;
                break;
            case "TUESDAY":
                numLetters = 7;
                break;
            case "THURSDAY":
            case "SATURDAY":
                numLetters = 8;
                break;
            case "WEDNESDAY":
                numLetters = 9;
                break;
            default:
                throw new IllegalArgumentException("Invalid day: " + day);
        }
        System.out.println("Old way: " + numLetters);
        
        // COM switch expression
        int letters = switch (day) {
            case "MONDAY", "FRIDAY", "SUNDAY" -> 6;
            case "TUESDAY" -> 7;
            case "THURSDAY", "SATURDAY" -> 8;
            case "WEDNESDAY" -> 9;
            default -> throw new IllegalArgumentException("Invalid day: " + day);
        };
        System.out.println("New way: " + letters);
        // Saída:
        // Old way: 6
        // New way: 6
        
        // Tipo de dia
        String dayType = switch (day) {
            case "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY" -> "Weekday";
            case "SATURDAY", "SUNDAY" -> "Weekend";
            default -> "Unknown";
        };
        System.out.println(day + " é " + dayType);
        // Saída: MONDAY é Weekday
    }
    
    static void switchWithYield() {
        System.out.println("\n=== Switch com yield ===\n");
        
        int score = 85;
        
        String grade = switch (score / 10) {
            case 10, 9 -> "A";
            case 8 -> "B";
            case 7 -> "C";
            case 6 -> "D";
            default -> {
                System.out.println("Score baixo: " + score);
                yield "F";
            }
        };
        
        System.out.println("Nota: " + grade);
        // Saída: Nota: B
        
        // Exemplo mais complexo
        int month = 3;
        String season = switch (month) {
            case 12, 1, 2 -> {
                System.out.println("Verão no hemisfério sul");
                yield "Summer";
            }
            case 3, 4, 5 -> {
                System.out.println("Outono no hemisfério sul");
                yield "Autumn";
            }
            case 6, 7, 8 -> {
                System.out.println("Inverno no hemisfério sul");
                yield "Winter";
            }
            case 9, 10, 11 -> {
                System.out.println("Primavera no hemisfério sul");
                yield "Spring";
            }
            default -> {
                yield "Invalid month";
            }
        };
        
        System.out.println("Estação: " + season);
        // Saída:
        // Outono no hemisfério sul
        // Estação: Autumn
    }
    
    static void switchWithPatternMatching() {
        System.out.println("\n=== Pattern Matching no Switch ===\n");
        
        Object obj = 42;
        
        String result = switch (obj) {
            case Integer i -> "Inteiro: " + i;
            case String s -> "String: " + s;
            case Double d -> "Double: " + d;
            case null -> "Null value";
            default -> "Outro tipo: " + obj.getClass().getSimpleName();
        };
        
        System.out.println(result);
        // Saída: Inteiro: 42
        
        // Com guards (condições adicionais)
        Object value = 150;
        
        String category = switch (value) {
            case Integer i when i < 0 -> "Negativo";
            case Integer i when i == 0 -> "Zero";
            case Integer i when i > 0 && i <= 100 -> "Pequeno positivo";
            case Integer i when i > 100 -> "Grande positivo";
            case String s when s.isEmpty() -> "String vazia";
            case String s -> "String: " + s;
            default -> "Outro";
        };
        
        System.out.println("Categoria: " + category);
        // Saída: Categoria: Grande positivo
        
        // Com records
        demonstrateRecordPatterns();
    }
    
    static void demonstrateRecordPatterns() {
        sealed interface Shape permits Circle, Rectangle {}
        record Circle(double radius) implements Shape {}
        record Rectangle(double width, double height) implements Shape {}
        
        Shape shape = new Circle(5.0);
        
        double area = switch (shape) {
            case Circle(double r) -> Math.PI * r * r;
            case Rectangle(double w, double h) -> w * h;
        };
        
        System.out.println("Área: " + area);
        // Saída: Área: 78.53981633974483
    }
    
    static void switchWithNullHandling() {
        System.out.println("\n=== Tratamento de Null ===\n");
        
        String input = null;
        
        // Java 17 permite null cases
        String output = switch (input) {
            case null -> "Entrada nula";
            case "hello" -> "Olá!";
            case "bye" -> "Tchau!";
            default -> "Entrada: " + input;
        };
        
        System.out.println(output);
        // Saída: Entrada nula
        
        // Sem null case resultaria em NullPointerException
        input = "hello";
        output = switch (input) {
            case null -> "Entrada nula";
            case "hello" -> "Olá!";
            case "bye" -> "Tchau!";
            default -> "Entrada: " + input;
        };
        
        System.out.println(output);
        // Saída: Olá!
    }
    
    // Exemplo prático: Calculator
    static double calculate(String operation, double a, double b) {
        return switch (operation) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> {
                if (b == 0) {
                    System.out.println("Erro: divisão por zero");
                    yield 0.0;
                }
                yield a / b;
            }
            case "%" -> a % b;
            case "^" -> Math.pow(a, b);
            default -> throw new IllegalArgumentException("Operação inválida: " + operation);
        };
    }
}
```

---

## 6. Helpful NullPointerExceptions

### Conceito
Mensagens de NPE agora indicam exatamente qual variável foi null.

```java
public class HelpfulNPEExamples {
    
    static class Address {
        String street;
        String city;
        
        Address(String street, String city) {
            this.street = street;
            this.city = city;
        }
    }
    
    static class Person {
        String name;
        Address address;
        
        Person(String name, Address address) {
            this.name = name;
            this.address = address;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Helpful NullPointerExceptions ===\n");
        
        // Antes do Java 17: "NullPointerException" genérico
        // Com Java 17: mostra exatamente qual referência é null
        
        Person person = new Person("João", null);
        
        try {
            String city = person.address.city.toUpperCase();
        } catch (NullPointerException e) {
            System.out.println("NPE Message:");
            System.out.println(e.getMessage());
            // Saída: Cannot read field "city" because "person.address" is null
        }
        
        // Outro exemplo
        String[] arr = null;
        try {
            int len = arr.length;
        } catch (NullPointerException e) {
            System.out.println("\nNPE Message 2:");
            System.out.println(e.getMessage());
            // Saída: Cannot read the array length because "arr" is null
        }
        
        // Exemplo com método chain
        try {
            person.address.street.substring(0, 5).toUpperCase();
        } catch (NullPointerException e) {
            System.out.println("\nNPE Message 3:");
            System.out.println(e.getMessage());
            // Saída: Cannot invoke "String.substring(int, int)" because "person.address.street" is null
        }
    }
}
```

---

## 7. Stream API Enhancements

```java
import java.util.*;
import java.util.stream.*;

public class StreamEnhancements {
    
    public static void main(String[] args) {
        System.out.println("=== Stream API Enhancements ===\n");
        
        // toList() - novo método conveniente
        List<Integer> numbers = Stream.of(1, 2, 3, 4, 5)
                                     .filter(n -> n % 2 == 0)
                                     .toList();  // Antes: .collect(Collectors.toList())
        
        System.out.println("Números pares: " + numbers);
        // Saída: Números pares: [2, 4]
        
        // A lista retornada é imutável
        try {
            numbers.add(6);
        } catch (UnsupportedOperationException e) {
            System.out.println("Lista imutável: não pode adicionar elementos");
        }
        
        // mapMulti - alternativa ao flatMap
        List<String> words = List.of("Hello", "World");
        List<Character> chars = words.stream()
            .mapMulti((word, consumer) -> {
                for (char c : word.toCharArray()) {
                    consumer.accept(c);
                }
            })
            .toList();
        
        System.out.println("\nCaracteres: " + chars);
        // Saída: Caracteres: [H, e, l, l, o, W, o, r, l, d]
    }
}
```

---

## 8. Enhanced Pseudo-Random Number Generators

```java
import java.util.random.*;

public class RandomGeneratorExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Enhanced Random Generators ===\n");
        
        // RandomGenerator interface unificada
        RandomGenerator random = RandomGenerator.getDefault();
        
        System.out.println("Inteiro aleatório: " + random.nextInt());
        System.out.println("Inteiro 0-99: " + random.nextInt(100));
        System.out.println("Double 0.0-1.0: " + random.nextDouble());
        
        // Stream de números aleatórios
        List<Integer> randomNumbers = random.ints(5, 1, 101)
                                           .boxed()
                                           .toList();
        
        System.out.println("\n5 números aleatórios (1-100): " + randomNumbers);
        // Saída exemplo: 5 números aleatórios (1-100): [45, 78, 12, 93, 34]
        
        // Diferentes algoritmos
        RandomGenerator l64x128 = RandomGenerator.of("L64X128MixRandom");
        RandomGenerator xoshiro = RandomGenerator.of("Xoshiro256PlusPlus");
        
        System.out.println("\nL64X128: " + l64x128.nextInt(100));
        System.out.println("Xoshiro: " + xoshiro.nextInt(100));
    }
}
```

---

## 🎯 Resumo das Features do Java 17

| Feature | Descrição | Benefício |
|---------|-----------|-----------|
| **Sealed Classes** | Controle de hierarquia de tipos | Modelagem de domínio mais segura |
| **Records** | Classes imutáveis de dados | Menos boilerplate, código limpo |
| **Pattern Matching** | instanceof com cast automático | Código mais conciso |
| **Text Blocks** | Strings multilinha | Facilita JSON, SQL, HTML |
| **Switch Expressions** | Switch como expressão | Código funcional e seguro |
| **Helpful NPE** | Mensagens de erro detalhadas | Debug mais fácil |
| **Stream.toList()** | Método conveniente | Menos verbosidade |
| **Random API** | Interface unificada | Melhor flexibilidade |

---

## 📚 Melhorias de Outras Versões Incluídas

### Do Java 9-16:
- Modules (Java 9)
- var (Java 10)
- HTTP Client (Java 11)
- String methods: isBlank(), lines(), strip(), repeat() (Java 11)
- Files.readString(), Files.writeString() (Java 11)
- Collection.toArray(IntFunction) (Java 11)
- Pattern Matching for instanceof (Preview em 14-15, Final em 16)

### Características LTS:
- Java 17 é uma versão Long-Term Support (LTS)
- Suporte estendido por vários anos
- Ideal para produção
- Base sólida para frameworks modernos

---

## 📚 Referências

- [Oracle Java 17 Documentation](https://docs.oracle.com/en/java/javase/17/)
- [JEP 409: Sealed Classes](https://openjdk.org/jeps/409)
- [JEP 395: Records](https://openjdk.org/jeps/395)
- [JEP 406: Pattern Matching for switch](https://openjdk.org/jeps/406)

---

*Java 17 LTS trouxe maturidade às features modernas, oferecendo uma base sólida e produtiva para desenvolvimento enterprise.*
