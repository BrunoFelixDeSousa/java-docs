# Quick Reference - GraalVM JDK 21+ Features

## 🚀 Features Rápidas - Cheat Sheet

### GraalVM Native Image

```bash
# Build native image
./mvnw package -Pnative

# Run native binary
./target/myapp-runner

# Test native
./mvnw verify -Pnative
```

### Virtual Threads

```java
// ✅ DO: I/O-bound tasks
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    var futures = tasks.stream()
        .map(task -> executor.submit(() -> processTask(task)))
        .toList();
}

// ❌ DON'T: CPU-bound tasks
// Use ForkJoinPool ou thread pool tradicional
```

### Structured Concurrency

```java
// ✅ DO: Aggregate data from multiple sources
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Future<User> user = scope.fork(() -> fetchUser(id));
    Future<Orders> orders = scope.fork(() -> fetchOrders(id));
    
    scope.join().throwIfFailed();
    
    return new UserProfile(user.resultNow(), orders.resultNow());
}
```

### Pattern Matching

```java
// ✅ DO: Decompose complex structures
return switch (payment) {
    case CreditCard(var number, var cvv, ExpiryDate(var m, var y)) 
        when y >= 2024 -> process(number, cvv);
    case Pix(var key) -> processPix(key);
    case null -> throw new IllegalArgumentException();
    default -> throw new UnsupportedPaymentException();
};

// ❌ DON'T: Simple if/else
// if (payment instanceof CreditCard) { ... }
```

### Record Patterns

```java
// ✅ DO: Nested decomposition
switch (order) {
    case Order(var id, var items, Address(_, _, "BR"), Customer(var name, _)) ->
        processLocalOrder(id, items, name);
    case Order(var id, _, Address(_, _, var country), _) ->
        processInternationalOrder(id, country);
}
```

### Sequenced Collections

```java
// ✅ DO: Ordered operations
SequencedSet<Order> orders = new LinkedHashSet<>();
orders.addFirst(newOrder);        // Add to front
Order latest = orders.getFirst(); // Get first
Order oldest = orders.getLast();  // Get last
orders.reversed();                // Reversed view

// ❌ DON'T: When order doesn't matter
// Set<Order> orders = new HashSet<>(); // Better for unordered
```

### SequencedMap

```java
// ✅ DO: LRU Cache
SequencedMap<K, V> cache = new LinkedHashMap<>();
cache.putLast(key, value);           // Add to end
cache.pollFirstEntry();              // Remove oldest
var newest = cache.lastEntry();      // Get newest
cache.reversed();                    // Reversed view
```

## 📊 When to Use What

| Feature | Use When | Don't Use When |
|---------|----------|----------------|
| **GraalVM Native** | Serverless, containers, fast startup | Long-running batch, heavy reflection |
| **Virtual Threads** | I/O-bound, many concurrent tasks | CPU-bound, short tasks |
| **Structured Concurrency** | Related async tasks | Independent tasks |
| **Pattern Matching** | Complex type checks, decomposition | Simple if/else |
| **Record Patterns** | Nested data extraction | Flat structures |
| **Sequenced Collections** | Order matters, LRU/FIFO | Order irrelevant |

## 🔄 Migration Quick Wins

### Install GraalVM
```bash
# Windows
choco install graalvm-java21

# Linux/Mac
sdk install java 21-graalce
sdk use java 21-graalce
gu install native-image
```

### Thread Pools → Virtual Threads
```java
// BEFORE
ExecutorService pool = Executors.newFixedThreadPool(100);

// AFTER
ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
```

### instanceof → Pattern Matching
```java
// BEFORE
if (obj instanceof User) {
    User user = (User) obj;
    process(user.getName());
}

// AFTER
if (obj instanceof User(var name, _)) {
    process(name);
}
```

### LinkedHashSet → SequencedSet
```java
// BEFORE
LinkedHashSet<T> set = new LinkedHashSet<>();
T first = set.iterator().next();

// AFTER
SequencedSet<T> set = new LinkedHashSet<>();
T first = set.getFirst();
```

### Register for Native Image
```java
// BEFORE
// Class not accessible in Native Image

// AFTER
@RegisterForReflection
public class MyEntity {
    // Now works in Native Image
}
```

## ⚡ Performance Tips

### GraalVM Native Image
- ✅ Startup: ~20-50ms (vs 2-3s JVM)
- ✅ Memory: ~30-50MB (vs 300-500MB JVM)
- ✅ Use for: Serverless, containers, CLIs
- ❌ Build time: 3-5 min (vs 30s JVM)
- ⚠️ Needs reflection config

### Virtual Threads
- ✅ Use for blocking I/O (HTTP, DB, files)
- ✅ Can create millions of virtual threads
- ❌ Avoid for CPU-intensive work
- ❌ Don't use thread-local excessively

### Pattern Matching
- ✅ Compiler optimizes switch
- ✅ Exhaustiveness checking prevents bugs
- ⚠️ Keep patterns simple for readability

### Sequenced Collections
- ✅ LinkedHashSet/LinkedHashMap maintain insertion order
- ⚠️ Slight overhead vs HashSet/HashMap
- ✅ reversed() creates view, not copy

## 🎯 Common Patterns

### Async Data Aggregation
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Future<A> a = scope.fork(() -> fetchA());
    Future<B> b = scope.fork(() -> fetchB());
    Future<C> c = scope.fork(() -> fetchC());
    
    scope.join().throwIfFailed();
    
    return aggregate(a.resultNow(), b.resultNow(), c.resultNow());
}
```

### Result Type with Pattern Matching
```java
sealed interface Result<T> permits Success, Failure {
    record Success<T>(T value) implements Result<T> {}
    record Failure<T>(String error) implements Result<T> {}
}

return switch (result) {
    case Success(var value) -> processValue(value);
    case Failure(var error) -> handleError(error);
};
```

### LRU Cache with SequencedMap
```java
class LRUCache<K, V> {
    private final SequencedMap<K, V> cache = new LinkedHashMap<>();
    private final int maxSize;
    
    void put(K key, V value) {
        cache.remove(key);
        cache.putLast(key, value);
        if (cache.size() > maxSize) {
            cache.pollFirstEntry();
        }
    }
}
```

### Event Processing FIFO
```java
SequencedCollection<Event> queue = new ArrayDeque<>();
queue.addLast(event);           // Enqueue
Event next = queue.removeFirst(); // Dequeue
```

## 🛡️ Error Handling

### Virtual Threads
```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    var futures = tasks.stream()
        .map(task -> executor.submit(() -> {
            try {
                return processTask(task);
            } catch (Exception e) {
                return handleError(e);
            }
        }))
        .toList();
}
```

### Structured Concurrency
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    // Tasks...
    scope.join().throwIfFailed(); // Throws if any failed
} catch (ExecutionException e) {
    // Handle failure
}
```

### Pattern Matching
```java
return switch (value) {
    case Type1 t1 -> process(t1);
    case Type2 t2 -> process(t2);
    case null -> throw new IllegalArgumentException("Null not allowed");
    default -> throw new UnsupportedTypeException();
};
```

## 📚 Further Reading

- [GraalVM Official Docs](https://www.graalvm.org/latest/docs/)
- [Native Image Guide](https://www.graalvm.org/latest/reference-manual/native-image/)
- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444)
- [JEP 453: Structured Concurrency](https://openjdk.org/jeps/453)
- [JEP 440: Record Patterns](https://openjdk.org/jeps/440)
- [JEP 441: Pattern Matching for switch](https://openjdk.org/jeps/441)
- [JEP 431: Sequenced Collections](https://openjdk.org/jeps/431)
- [Quarkus Native](https://quarkus.io/guides/building-native-image)

---

**Versão**: GraalVM JDK 21+ (LTS)  
**Última Atualização**: 2024-09-29
