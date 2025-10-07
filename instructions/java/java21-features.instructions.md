---
applyTo: "**/*.java"
description: "Features exclusivas do Java 21+ - Virtual Threads, Pattern Matching avançado, Sequenced Collections"
---

# Java 21+ - Features Exclusivas e Avançadas

Este documento complementa as [instruções de código Java](./java-coding.instructions.md) focando nas features exclusivas do Java 21+.

## Referências

- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444) - Concorrência leve e escalável
- [JEP 440: Record Patterns](https://openjdk.org/jeps/440) - Pattern matching para records
- [JEP 441: Pattern Matching for switch](https://openjdk.org/jeps/441) - Pattern matching completo
- [JEP 431: Sequenced Collections](https://openjdk.org/jeps/431) - Ordem determinística em coleções
- [JEP 453: Structured Concurrency](https://openjdk.org/jeps/453) - Gerenciamento estruturado de threads

## Virtual Threads - Concorrência Escalável

### ✅ PADRÃO - Virtual Threads para I/O-bound Tasks

```java
// ✅ PADRÃO - Executor com Virtual Threads
@ApplicationScoped
public class OrderProcessingService {

    // Virtual thread executor para processamento em lote
    private final ExecutorService virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * Processa múltiplos pedidos em paralelo usando Virtual Threads.
     * Ideal para operações I/O-bound (chamadas HTTP, queries DB, etc.)
     */
    public List<OrderProcessingResult> processOrdersBatch(List<Order> orders) {
        try {
            var futures = orders.stream()
                .map(order -> virtualExecutor.submit(() -> processOrder(order)))
                .toList();

            return futures.stream()
                .map(this::getFutureResult)
                .toList();
        } catch (Exception e) {
            throw new BatchProcessingException("Failed to process orders batch", e);
        }
    }

    private OrderProcessingResult processOrder(Order order) {
        // Estas operações bloqueantes são executadas em virtual threads
        // Não bloqueiam threads do sistema operacional
        var payment = paymentService.processPayment(order);        // HTTP call
        var inventory = inventoryService.reserveItems(order);      // DB query
        var shipping = shippingService.scheduleShipment(order);    // External API
        var notification = notificationService.sendConfirmation(); // Email service

        return new OrderProcessingResult(payment, inventory, shipping, notification);
    }
}

// ✅ PADRÃO - Structured Concurrency
@ApplicationScoped
public class UserDataAggregationService {

    /**
     * Agregação estruturada de dados usando Structured Concurrency.
     * Todas as subtarefas são gerenciadas como uma única unidade.
     */
    public UserProfile aggregateUserProfile(UserId userId) throws ExecutionException, InterruptedException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Todas as tarefas são subordinadas ao scope
            Future<User> userFuture = scope.fork(() -> fetchUserData(userId));
            Future<List<Order>> ordersFuture = scope.fork(() -> fetchUserOrders(userId));
            Future<UserPreferences> prefsFuture = scope.fork(() -> fetchUserPreferences(userId));
            Future<PaymentMethods> paymentsFuture = scope.fork(() -> fetchPaymentMethods(userId));

            // Aguarda todas as tarefas completarem
            scope.join();

            // Verifica se alguma falhou - cancela todas se necessário
            scope.throwIfFailed();

            // Todas bem-sucedidas - agrega resultados
            return new UserProfile(
                userFuture.resultNow(),
                ordersFuture.resultNow(),
                prefsFuture.resultNow(),
                paymentsFuture.resultNow()
            );
        }
    }

    /**
     * Structured Concurrency com shutdown customizado.
     * Primeira resposta válida cancela as demais.
     */
    public Price getBestPrice(ProductId productId, List<Supplier> suppliers) throws ExecutionException, InterruptedException {
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<Price>()) {
            // Fork uma tarefa para cada fornecedor
            for (var supplier : suppliers) {
                scope.fork(() -> supplier.getPrice(productId));
            }

            // Aguarda primeira resposta bem-sucedida
            scope.join();

            // Retorna o melhor preço encontrado
            return scope.result();
        }
    }
}

// ✅ PADRÃO - Virtual Threads com CompletableFuture
@ApplicationScoped
public class AsyncOrderService {

    public CompletableFuture<OrderResult> processOrderAsync(Order order) {
        // CompletableFuture agora usa Virtual Threads automaticamente
        return CompletableFuture
            .supplyAsync(() -> validateOrder(order))
            .thenApplyAsync(validOrder -> processPayment(validOrder))
            .thenApplyAsync(paidOrder -> updateInventory(paidOrder))
            .thenApplyAsync(completedOrder -> sendNotification(completedOrder))
            .exceptionally(ex -> handleOrderError(ex));
    }

    public List<OrderResult> processMultipleOrders(List<Order> orders) {
        var futures = orders.stream()
            .map(this::processOrderAsync)
            .toList();

        // Combina todos os futures
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .toList())
            .join();
    }
}

// ❌ EVITAR - Thread pools tradicionais para I/O-bound
@ApplicationScoped
public class LegacyOrderService {

    // ❌ Thread pool tradicional desperdiça recursos com I/O-bound
    private final ExecutorService executor = Executors.newFixedThreadPool(100);

    public List<OrderResult> processOrders(List<Order> orders) {
        // Cada thread bloqueia durante I/O
        var futures = orders.stream()
            .map(order -> executor.submit(() -> processOrder(order)))
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
```

## Pattern Matching Avançado

### ✅ PADRÃO - Record Patterns com Decomposição

```java
// ✅ PADRÃO - Nested record patterns
public sealed interface Payment permits CreditCardPayment, PixPayment, BankTransferPayment {}

public record CreditCardPayment(
    CardNumber number,
    SecurityCode cvv,
    ExpiryDate expiry,
    CardHolder holder
) implements Payment {}

public record ExpiryDate(int month, int year) {
    public boolean isExpired() {
        var now = LocalDate.now();
        return year < now.getYear() || (year == now.getYear() && month < now.getMonthValue());
    }
}

public record CardHolder(String name, Address billingAddress) {}

// ✅ PADRÃO - Decomposição profunda em pattern matching
public ValidationResult validatePayment(Payment payment) {
    return switch (payment) {
        // Decompõe múltiplos níveis de records
        case CreditCardPayment(
            var number,
            var cvv,
            ExpiryDate(var month, var year),
            CardHolder(var name, Address(_, _, var country))
        ) when year < LocalDate.now().getYear() ->
            ValidationResult.failure("Card expired");

        case CreditCardPayment(_, _, _, CardHolder(var name, _))
            when name.length() < 3 ->
            ValidationResult.failure("Invalid card holder name");

        case CreditCardPayment card ->
            validateCreditCard(card);

        case PixPayment(var key) when !isValidPixKey(key) ->
            ValidationResult.failure("Invalid PIX key");

        case PixPayment pix ->
            ValidationResult.success();

        case BankTransferPayment transfer ->
            validateBankTransfer(transfer);
    };
}

// ✅ PADRÃO - Pattern matching com guards complexos
public ShippingCost calculateShipping(Order order) {
    return switch (order) {
        case Order(
            var id,
            var items,
            ShippingAddress(var street, var city, "BR"),
            var customer
        ) when items.totalWeight().lessThanOrEqual(Weight.ofKg(2)) ->
            new ShippingCost(Money.of(15.00, BRL), ShippingMethod.STANDARD);

        case Order(
            var id,
            var items,
            ShippingAddress(var street, var city, "BR"),
            PremiumCustomer premium
        ) when premium.hasFreeShipping() ->
            new ShippingCost(Money.zero(BRL), ShippingMethod.EXPRESS);

        case Order(_, var items, ShippingAddress(_, _, var country), _)
            when !SUPPORTED_COUNTRIES.contains(country) ->
            throw new UnsupportedShippingCountryException(country);

        case Order(_, var items, var address, _) ->
            internationalShippingCalculator.calculate(items, address);
    };
}

// ✅ PADRÃO - Pattern matching em coleções
public record ShoppingCart(List<CartItem> items, Discount discount) {

    public Money calculateTotal() {
        return switch (discount) {
            case Discount.Percentage(var rate) when items.size() >= 5 ->
                calculateSubtotal().multiply(BigDecimal.ONE.subtract(rate).multiply(1.1)); // 10% extra

            case Discount.Percentage(var rate) ->
                calculateSubtotal().multiply(BigDecimal.ONE.subtract(rate));

            case Discount.FixedAmount(var amount) when calculateSubtotal().isGreaterThan(amount) ->
                calculateSubtotal().subtract(amount);

            case Discount.FixedAmount amount ->
                Money.zero(calculateSubtotal().currency());

            case null ->
                calculateSubtotal();
        };
    }
}
```

### ✅ PADRÃO - Type Patterns com Generics

```java
// ✅ PADRÃO - Pattern matching com tipos genéricos
public <T> String formatResponse(Result<T> result) {
    return switch (result) {
        case Result.Success<T>(User user) ->
            "User created: %s (%s)".formatted(user.name(), user.email());

        case Result.Success<T>(Order order) ->
            "Order confirmed: #%s - %s".formatted(order.id(), order.total());

        case Result.Success<T>(T value) ->
            "Success: %s".formatted(value.toString());

        case Result.Failure<T>(ValidationError error) ->
            "Validation failed: %s".formatted(error.messages());

        case Result.Failure<T>(BusinessError error) ->
            "Business rule violation: %s".formatted(error.code());

        case Result.Failure<T>(Throwable throwable) ->
            "System error: %s".formatted(throwable.getMessage());
    };
}

// ✅ PADRÃO - Pattern matching para processamento polimórfico
public void processEvent(DomainEvent event) {
    switch (event) {
        case UserCreatedEvent(var eventId, var timestamp, var userId, _, var email, var name) -> {
            log.info("User created: {} ({}) at {}", name, email, timestamp);
            sendWelcomeEmail(email, name);
            updateUserStats(userId);
        }

        case OrderConfirmedEvent(_, _, var orderId, _, var total, var items) -> {
            log.info("Order confirmed: {} - Total: {}", orderId, total);
            reserveInventory(items);
            scheduleShipment(orderId);
        }

        case PaymentProcessedEvent(_, _, var paymentId, _, var amount, var method) -> {
            log.info("Payment processed: {} - {} via {}", paymentId, amount, method);
            updatePaymentStatus(paymentId);
            sendPaymentReceipt(paymentId);
        }

        default -> log.warn("Unhandled event type: {}", event.getClass());
    }
}
```

## Sequenced Collections - Ordem Determinística

### ✅ PADRÃO - SequencedCollection APIs

```java
// ✅ PADRÃO - SequencedSet para histórico ordenado
@ApplicationScoped
public class OrderHistoryService {

    public class UserOrderHistory {
        // LinkedHashSet implementa SequencedSet
        private final SequencedSet<Order> orders = new LinkedHashSet<>();

        public void addOrder(Order order) {
            orders.addFirst(order); // Novo em Java 21 - adiciona no início
        }

        public Optional<Order> getMostRecentOrder() {
            try {
                return Optional.of(orders.getFirst()); // Novo em Java 21
            } catch (NoSuchElementException e) {
                return Optional.empty();
            }
        }

        public Optional<Order> getFirstOrder() {
            try {
                return Optional.of(orders.getLast()); // Novo em Java 21
            } catch (NoSuchElementException e) {
                return Optional.empty();
            }
        }

        public SequencedSet<Order> getRecentOrders(int limit) {
            return orders.stream()
                .limit(limit)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        public SequencedSet<Order> getOrdersInReverseChronologicalOrder() {
            return orders.reversed(); // Novo em Java 21 - view reversa
        }

        public void removeOldestOrder() {
            orders.removeLast(); // Novo em Java 21
        }
    }
}

// ✅ PADRÃO - SequencedMap para cache LRU
public class LRUCache<K, V> {
    private final SequencedMap<K, V> cache = new LinkedHashMap<>();
    private final int maxSize;

    public LRUCache(int maxSize) {
        this.maxSize = maxSize;
    }

    public void put(K key, V value) {
        // Remove se já existe para reposicionar
        cache.remove(key);

        // Adiciona no final (mais recente)
        cache.putLast(key, value); // Novo em Java 21

        // Remove o mais antigo se excedeu capacidade
        if (cache.size() > maxSize) {
            cache.pollFirstEntry(); // Novo em Java 21
        }
    }

    public Optional<V> get(K key) {
        V value = cache.remove(key);
        if (value != null) {
            cache.putLast(key, value); // Reposiciona como mais recente
            return Optional.of(value);
        }
        return Optional.empty();
    }

    public Optional<Map.Entry<K, V>> getOldestEntry() {
        return Optional.ofNullable(cache.firstEntry()); // Novo em Java 21
    }

    public Optional<Map.Entry<K, V>> getNewestEntry() {
        return Optional.ofNullable(cache.lastEntry()); // Novo em Java 21
    }

    public SequencedMap<K, V> getReversedView() {
        return cache.reversed(); // Novo em Java 21
    }
}

// ✅ PADRÃO - SequencedCollection para processamento em ordem
public class EventProcessor {
    private final SequencedCollection<DomainEvent> eventQueue = new ArrayDeque<>();

    public void enqueueEvent(DomainEvent event) {
        eventQueue.addLast(event); // Adiciona no final da fila
    }

    public void prioritizeEvent(DomainEvent event) {
        eventQueue.addFirst(event); // Adiciona no início para processar antes
    }

    public Optional<DomainEvent> getNextEvent() {
        try {
            return Optional.of(eventQueue.removeFirst());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    public void processAllEvents() {
        // Processa na ordem FIFO
        while (!eventQueue.isEmpty()) {
            var event = eventQueue.removeFirst();
            processEvent(event);
        }
    }

    public void processEventsInReverse() {
        // View reversa para processamento LIFO
        for (var event : eventQueue.reversed()) {
            processEvent(event);
        }
    }
}
```

## String Templates (Preview em Java 21)

### ✅ PADRÃO - String Templates para segurança

```java
// ✅ PADRÃO - String templates (quando estabilizado)
// Nota: Preview feature em Java 21 - verificar status antes de usar

public class QueryBuilder {

    // String templates previnem SQL injection
    public String buildUserQuery(String email, UserStatus status) {
        // Sintaxe quando estabilizado (exemplo ilustrativo)
        /*
        return STR."""
            SELECT * FROM users
            WHERE email = \{email}  -- Escapado automaticamente
              AND status = \{status}
            """;
        */

        // Por enquanto, usar prepared statements tradicionais
        return """
            SELECT * FROM users
            WHERE email = ?
              AND status = ?
            """;
    }
}
```

## Boas Práticas - Java 21+

### ✅ Quando Usar Virtual Threads

```java
// ✅ USAR Virtual Threads para:
// - Operações I/O-bound (HTTP, Database, File I/O)
// - Alta concorrência com muitas tarefas bloqueantes
// - Processamento em lote de requisições

// ❌ NÃO USAR Virtual Threads para:
// - Operações CPU-bound (cálculos pesados)
// - Código síncrono simples
// - Tarefas de longa duração sem I/O
```

### ✅ Quando Usar Pattern Matching

```java
// ✅ USAR Pattern Matching para:
// - Decomposição de estruturas de dados complexas
// - Validação com múltiplas condições
// - Processamento polimórfico
// - Transformação de tipos

// ❌ NÃO USAR Pattern Matching para:
// - Lógica simples if/else
// - Comparações triviais
```

### ✅ Quando Usar Sequenced Collections

```java
// ✅ USAR Sequenced Collections para:
// - Histórico ordenado
// - Filas e pilhas
// - Cache LRU/MRU
// - Processamento em ordem específica

// ❌ NÃO USAR Sequenced Collections para:
// - Coleções sem necessidade de ordem
// - Sets onde ordem não importa
// - Performance crítica (overhead adicional)
```

## Migration Guide: Java 17 → Java 21

### Atualizações Recomendadas

```java
// ANTES (Java 17)
ExecutorService executor = Executors.newFixedThreadPool(100);
List<Future<Result>> futures = tasks.stream()
    .map(task -> executor.submit(() -> processTask(task)))
    .toList();

// DEPOIS (Java 21)
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    List<Future<Result>> futures = tasks.stream()
        .map(task -> executor.submit(() -> processTask(task)))
        .toList();
}

// ANTES (Java 17)
if (obj instanceof User) {
    User user = (User) obj;
    processUser(user);
}

// DEPOIS (Java 21)
if (obj instanceof User user) {
    processUser(user);
}

// ANTES (Java 17)
LinkedHashSet<Order> orders = new LinkedHashSet<>();
Order first = orders.iterator().next();

// DEPOIS (Java 21)
SequencedSet<Order> orders = new LinkedHashSet<>();
Order first = orders.getFirst();
```
