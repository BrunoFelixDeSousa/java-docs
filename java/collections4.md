# Documentação Completa: Apache Commons Collections 4

## 📋 Índice
1. [Introdução e Conceitos Fundamentais](#introdução-e-conceitos-fundamentais)
2. [Configuração e Instalação](#configuração-e-instalação)
3. [Principais Interfaces e Classes](#principais-interfaces-e-classes)
4. [Collections Básicas](#collections-básicas)
5. [Collections Avançadas](#collections-avançadas)
6. [Utilitários e Helpers](#utilitários-e-helpers)
7. [Transformações e Predicados](#transformações-e-predicados)
8. [Padrões de Uso e Melhores Práticas](#padrões-de-uso-e-melhores-práticas)
9. [Exemplos Práticos](#exemplos-práticos)
10. [Referência Completa de APIs](#referência-completa-de-apis)

---

## 1. Introdução e Conceitos Fundamentais

### O que é Apache Commons Collections 4?

Imagine que o Java Collections Framework (JCF) é como uma caixa de ferramentas básica para um carpinteiro. O Apache Commons Collections 4 é como ter uma oficina completa com ferramentas especializadas.

**Definição Técnica**: É uma extensão do Java Collections Framework que fornece:
- Estruturas de dados especializadas
- Utilitários para manipulação de coleções
- Implementações otimizadas para casos específicos
- Funcionalidades funcionais (predicados, transformadores)

### Por que usar?

**Problema**: O JCF padrão é limitado para casos específicos:
```java
// JCF padrão - limitado
Map<String, String> map = new HashMap<>();
// Como fazer um Map que expira automaticamente?
// Como fazer um Map bidirecional?
// Como fazer transformações funcionais?
```

**Solução com Commons Collections**:
```java
// Map com expiração automática
Map<String, String> expiringMap = PassiveExpiringMap.pasiveExpiringMap(60000);

// Map bidirecional
BidiMap<String, String> bidiMap = new DualHashBidiMap<>();
bidiMap.put("key", "value");
String key = bidiMap.getKey("value"); // Busca reversa!

// Transformações funcionais
Collection<String> upperCased = CollectionUtils.collect(names, String::toUpperCase);
```

### Arquitetura Conceitual

```
Apache Commons Collections 4
├── Collections Básicas (List, Set, Map estendidos)
├── Collections Especializadas (MultiMap, BidiMap, Bag, etc.)
├── Utilitários (CollectionUtils, ListUtils, MapUtils, etc.)
├── Funcional (Predicate, Transformer, Closure)
└── Comparators e Iteradores especializados
```

---

## 2. Configuração e Instalação

### Maven
```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-collections4</artifactId>
    <version>4.4</version>
</dependency>
```

### Gradle
```gradle
implementation 'org.apache.commons:commons-collections4:4.4'
```

### Verificação da Instalação
```java
import org.apache.commons.collections4.CollectionUtils;

public class TestSetup {
    public static void main(String[] args) {
        System.out.println("Commons Collections 4 instalado: " + 
            CollectionUtils.class.getPackage().getImplementationVersion());
    }
}
```

---

## 3. Principais Interfaces e Classes

### Hierarquia das Interfaces

```
Collection (Java)
├── Bag (Commons) - Permite duplicatas com contagem
├── MultiSet (Commons) - Alias para Bag
└── BoundedCollection (Commons) - Coleção com limite

Map (Java)
├── BidiMap (Commons) - Map bidirecional
├── MultiMap (Commons) - Map com múltiplos valores
├── OrderedMap (Commons) - Map que mantém ordem
├── SortedBidiMap (Commons) - BidiMap ordenado
└── Trie (Commons) - Map otimizado para prefixos
```

### Principais Classes de Implementação

| Interface | Implementação Principal | Uso |
|-----------|------------------------|-----|
| `Bag` | `HashBag`, `TreeBag` | Contagem de elementos |
| `BidiMap` | `DualHashBidiMap`, `TreeBidiMap` | Busca bidirecional |
| `MultiMap` | `MultiValueMap` | Múltiplos valores por chave |
| `Trie` | `PatriciaTrie` | Busca por prefixos |

---

## 4. Collections Básicas

### 4.1 Bag - Coleções com Contagem

**Conceito**: Um Bag é como um Set que permite duplicatas e conta quantas vezes cada elemento aparece.

#### Exemplo Básico
```java
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;

Bag<String> bag = new HashBag<>();
bag.add("apple", 3);  // Adiciona 3 maçãs
bag.add("banana", 2); // Adiciona 2 bananas
bag.add("apple");     // Adiciona mais 1 maçã

System.out.println(bag.getCount("apple"));  // Output: 4
System.out.println(bag.uniqueSet());        // [apple, banana]
System.out.println(bag.size());             // 6 (total de elementos)
```

#### Casos de Uso Práticos
```java
// 1. Contagem de palavras
public Map<String, Integer> contarPalavras(String texto) {
    Bag<String> bag = new HashBag<>();
    String[] palavras = texto.split("\\s+");
    
    for (String palavra : palavras) {
        bag.add(palavra.toLowerCase());
    }
    
    return bag.toMapOfCounts(); // Converte para Map<String, Integer>
}

// 2. Análise de logs
Bag<String> statusCodes = new HashBag<>();
logs.forEach(log -> statusCodes.add(log.getStatusCode()));
System.out.println("Erros 404: " + statusCodes.getCount("404"));
```

### 4.2 BidiMap - Maps Bidirecionais

**Conceito**: Permite busca tanto por chave quanto por valor, mantendo ambas as direções únicas.

```java
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

BidiMap<String, Integer> bidiMap = new DualHashBidiMap<>();
bidiMap.put("um", 1);
bidiMap.put("dois", 2);
bidiMap.put("três", 3);

// Busca normal (chave -> valor)
Integer valor = bidiMap.get("dois"); // 2

// Busca inversa (valor -> chave)
String chave = bidiMap.getKey(2); // "dois"

// Map inverso
BidiMap<Integer, String> inverso = bidiMap.inverseBidiMap();
String chaveInversa = inverso.get(1); // "um"
```

#### Caso de Uso: Sistema de IDs
```java
public class UserManager {
    private BidiMap<String, Long> userIdMap = new DualHashBidiMap<>();
    
    public void registerUser(String username, Long id) {
        userIdMap.put(username, id);
    }
    
    public Long getUserId(String username) {
        return userIdMap.get(username);
    }
    
    public String getUsername(Long id) {
        return userIdMap.getKey(id); // Busca reversa eficiente
    }
}
```

### 4.3 MultiMap - Múltiplos Valores por Chave

**Conceito**: Como um Map, mas cada chave pode ter múltiplos valores associados.

```java
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

MultiMap<String, String> multiMap = new MultiValueMap<>();
multiMap.put("frutas", "maçã");
multiMap.put("frutas", "banana");
multiMap.put("frutas", "laranja");

Collection<String> frutas = multiMap.get("frutas");
// [maçã, banana, laranja]

// Verificações
boolean hasFrutas = multiMap.containsKey("frutas"); // true
boolean hasMaca = multiMap.containsValue("maçã");   // true
```

#### Caso de Uso: Sistema de Tags
```java
public class ArticleTagManager {
    private MultiMap<String, String> tagMap = new MultiValueMap<>();
    
    public void addTag(String articleId, String tag) {
        tagMap.put(articleId, tag);
    }
    
    public Collection<String> getTags(String articleId) {
        return tagMap.get(articleId);
    }
    
    public Collection<String> findArticlesByTag(String tag) {
        return tagMap.entrySet().stream()
            .filter(entry -> entry.getValue().contains(tag))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
}
```

---

## 5. Collections Avançadas

### 5.1 Trie - Estrutura para Prefixos

**Conceito**: Estrutura de dados otimizada para busca de strings com prefixos comuns.

```java
import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;

Trie<String, String> trie = new PatriciaTrie<>();
trie.put("car", "automóvel");
trie.put("card", "cartão");
trie.put("care", "cuidado");
trie.put("careful", "cuidadoso");

// Busca por prefixo
SortedMap<String, String> prefixMap = trie.prefixMap("car");
// {car=automóvel, card=cartão, care=cuidado, careful=cuidadoso}

// Busca exata
String valor = trie.get("care"); // "cuidado"
```

#### Caso de Uso: Autocomplete
```java
public class AutocompleteService {
    private Trie<String, String> dictionary = new PatriciaTrie<>();
    
    public void addWord(String word, String definition) {
        dictionary.put(word.toLowerCase(), definition);
    }
    
    public List<String> getSuggestions(String prefix, int maxResults) {
        return dictionary.prefixMap(prefix.toLowerCase())
            .keySet()
            .stream()
            .limit(maxResults)
            .collect(Collectors.toList());
    }
}
```

### 5.2 Collections com Limites

```java
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.collections4.list.FixedSizeList;

// Fila circular com tamanho fixo
CircularFifoQueue<String> fifo = new CircularFifoQueue<>(3);
fifo.add("primeiro");
fifo.add("segundo");
fifo.add("terceiro");
fifo.add("quarto"); // Remove "primeiro" automaticamente

// Lista com tamanho fixo
List<String> baseList = Arrays.asList("a", "b", "c");
List<String> fixedList = FixedSizeList.fixedSizeList(baseList);
// fixedList.add("d"); // Lançará UnsupportedOperationException
```

---

## 6. Utilitários e Helpers

### 6.1 CollectionUtils - O Canivete Suíço

**Métodos Fundamentais**:

#### Operações Básicas
```java
import org.apache.commons.collections4.CollectionUtils;

List<String> lista1 = Arrays.asList("a", "b", "c");
List<String> lista2 = Arrays.asList("b", "c", "d");

// Interseção
Collection<String> intersection = CollectionUtils.intersection(lista1, lista2);
// [b, c]

// União
Collection<String> union = CollectionUtils.union(lista1, lista2);
// [a, b, c, d]

// Diferença
Collection<String> subtract = CollectionUtils.subtract(lista1, lista2);
// [a]

// Verificações
boolean isEmpty = CollectionUtils.isEmpty(lista1);        // false
boolean isNotEmpty = CollectionUtils.isNotEmpty(lista1);  // true
boolean isEqual = CollectionUtils.isEqualCollection(lista1, lista2); // false
```

#### Transformações Funcionais
```java
// Transformar elementos
List<String> names = Arrays.asList("joão", "maria", "pedro");
Collection<String> upperNames = CollectionUtils.collect(names, String::toUpperCase);
// [JOÃO, MARIA, PEDRO]

// Filtrar elementos
Collection<String> longNames = CollectionUtils.select(names, 
    name -> name.length() > 4);
// [maria, pedro]

// Rejeitar elementos
Collection<String> shortNames = CollectionUtils.selectRejected(names,
    name -> name.length() > 4);
// [joão]
```

### 6.2 MapUtils - Utilitários para Maps

```java
import org.apache.commons.collections4.MapUtils;

Map<String, Integer> map = new HashMap<>();
map.put("a", 1);
map.put("b", 2);

// Verificações seguras
boolean isEmpty = MapUtils.isEmpty(map);     // false
Integer value = MapUtils.getInteger(map, "a", 0); // 1 (com default)
String str = MapUtils.getString(map, "c", "default"); // "default"

// Debug e visualização
MapUtils.debugPrint(System.out, "Meu Map", map);
/*
Output:
Meu Map = 
{
    a = 1 java.lang.Integer
    b = 2 java.lang.Integer
} java.util.HashMap
*/
```

### 6.3 ListUtils - Utilitários para Listas

```java
import org.apache.commons.collections4.ListUtils;

List<Integer> list1 = Arrays.asList(1, 2, 3);
List<Integer> list2 = Arrays.asList(4, 5, 6);

// União de listas
List<Integer> union = ListUtils.union(list1, list2);
// [1, 2, 3, 4, 5, 6]

// Particionamento
List<List<Integer>> partitions = ListUtils.partition(
    Arrays.asList(1, 2, 3, 4, 5, 6, 7), 3);
// [[1, 2, 3], [4, 5, 6], [7]]

// Lista lazy (útil para grandes datasets)
List<String> lazyList = ListUtils.lazyList(new ArrayList<>(), 
    () -> "default");
String item = lazyList.get(10); // "default" (criado automaticamente)
```

---

## 7. Transformações e Predicados

### 7.1 Conceitos Funcionais

**Predicate**: Função que retorna boolean (testa condição)
**Transformer**: Função que converte um objeto em outro
**Closure**: Função que executa ação sem retorno

### 7.2 Predicados Avançados

```java
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.PredicateUtils;

// Predicados básicos
Predicate<Integer> isEven = n -> n % 2 == 0;
Predicate<String> isNotEmpty = s -> s != null && !s.isEmpty();

// Combinação de predicados
Predicate<Integer> between1And10 = PredicateUtils.allPredicate(
    n -> n >= 1,
    n -> n <= 10
);

// Uso prático
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
Collection<Integer> evenNumbers = CollectionUtils.select(numbers, isEven);
Collection<Integer> validNumbers = CollectionUtils.select(numbers, between1And10);
```

### 7.3 Transformadores

```java
import org.apache.commons.collections4.Transformer;

// Transformer personalizado
Transformer<String, Integer> stringLength = String::length;

// Uso com collections
List<String> words = Arrays.asList("hello", "world", "java");
Collection<Integer> lengths = CollectionUtils.collect(words, stringLength);
// [5, 5, 4]

// Transformer chain (pipeline)
Transformer<String, String> trimAndUpper = 
    TransformerUtils.chainedTransformer(
        String::trim,
        String::toUpperCase
    );

String result = trimAndUpper.transform("  hello  "); // "HELLO"
```

---

## 8. Padrões de Uso e Melhores Práticas

### 8.1 Padrões de Performance

#### Use a Implementação Correta
```java
// Para busca frequente por prefixos
Trie<String, String> trie = new PatriciaTrie<>(); // O(k) onde k = tamanho da chave

// Para contagem de elementos
Bag<String> bag = new HashBag<>(); // O(1) para operações básicas

// Para Map bidirecional com ordenação
BidiMap<String, Integer> bidi = new TreeBidiMap<>(); // O(log n)
```

#### Evite Conversões Desnecessárias
```java
// ❌ Ineficiente
List<String> list = Arrays.asList("a", "b", "c");
Set<String> set = new HashSet<>(list);
Collection<String> upper = CollectionUtils.collect(set, String::toUpperCase);

// ✅ Eficiente
Collection<String> upper = CollectionUtils.collect(list, String::toUpperCase);
```

### 8.2 Padrões de Segurança

#### Validação com Predicados
```java
public class UserValidator {
    private static final Predicate<String> VALID_EMAIL = email ->
        email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Predicate<String> VALID_USERNAME = username ->
        username != null && username.length() >= 3 && username.length() <= 20;
    
    public boolean isValidUser(String username, String email) {
        return VALID_USERNAME.evaluate(username) && VALID_EMAIL.evaluate(email);
    }
}
```

### 8.3 Padrões de Manutenibilidade

#### Factories para Collections Especializadas
```java
public class CollectionFactory {
    public static <T> Bag<T> createCountingBag() {
        return new HashBag<>();
    }
    
    public static <K, V> BidiMap<K, V> createBidirectionalMap() {
        return new DualHashBidiMap<>();
    }
    
    public static <T> CircularFifoQueue<T> createLimitedQueue(int size) {
        return new CircularFifoQueue<>(size);
    }
}
```

---

## 9. Exemplos Práticos

### 9.1 Sistema de Cache com Expiração

```java
import org.apache.commons.collections4.map.PassiveExpiringMap;

public class CacheService<K, V> {
    private final Map<K, V> cache;
    
    public CacheService(long expirationTimeMs) {
        this.cache = new PassiveExpiringMap<>(expirationTimeMs);
    }
    
    public void put(K key, V value) {
        cache.put(key, value);
    }
    
    public V get(K key) {
        return cache.get(key);
    }
    
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }
}

// Uso
CacheService<String, String> cache = new CacheService<>(60000); // 1 minuto
cache.put("user:123", "John Doe");
// Após 1 minuto, o valor expira automaticamente
```

### 9.2 Sistema de Análise de Texto

```java
public class TextAnalyzer {
    private Bag<String> wordCount = new HashBag<>();
    private Trie<String, Integer> wordIndex = new PatriciaTrie<>();
    
    public void analyzeText(String text) {
        String[] words = text.toLowerCase()
            .replaceAll("[^a-zA-Z\\s]", "")
            .split("\\s+");
        
        for (String word : words) {
            if (word.length() > 2) { // Ignora palavras muito pequenas
                wordCount.add(word);
                wordIndex.put(word, wordCount.getCount(word));
            }
        }
    }
    
    public List<String> getMostFrequentWords(int limit) {
        return wordCount.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
    
    public List<String> findWordsWithPrefix(String prefix) {
        return new ArrayList<>(wordIndex.prefixMap(prefix).keySet());
    }
    
    public int getWordFrequency(String word) {
        return wordCount.getCount(word.toLowerCase());
    }
}
```

### 9.3 Sistema de Relacionamentos (Grafo Simples)

```java
public class RelationshipGraph<T> {
    private MultiMap<T, T> relationships = new MultiValueMap<>();
    private BidiMap<T, String> nodeLabels = new DualHashBidiMap<>();
    
    public void addRelationship(T from, T to) {
        relationships.put(from, to);
    }
    
    public void setNodeLabel(T node, String label) {
        nodeLabels.put(node, label);
    }
    
    public Collection<T> getConnections(T node) {
        return relationships.get(node);
    }
    
    public T findNodeByLabel(String label) {
        return nodeLabels.getKey(label);
    }
    
    public boolean areConnected(T from, T to) {
        Collection<T> connections = relationships.get(from);
        return connections != null && connections.contains(to);
    }
    
    public int getConnectionCount(T node) {
        Collection<T> connections = relationships.get(node);
        return connections != null ? connections.size() : 0;
    }
}
```

---

## 10. Referência Completa de APIs

### 10.1 Interfaces Principais

| Interface | Descrição | Métodos Key |
|-----------|-----------|-------------|
| `Bag<E>` | Coleção com contagem | `getCount()`, `add(E, int)`, `remove(E, int)` |
| `BidiMap<K,V>` | Map bidirecional | `getKey()`, `inverseBidiMap()` |
| `MultiMap<K,V>` | Map com múltiplos valores | `put()`, `get()` retorna Collection |
| `Trie<K,V>` | Map otimizado para prefixos | `prefixMap()`, `firstKey()` |

### 10.2 Classes Utilitárias

| Classe | Métodos Principais | Exemplo de Uso |
|--------|-------------------|----------------|
| `CollectionUtils` | `isEmpty()`, `select()`, `collect()`, `union()` | Operações funcionais |
| `MapUtils` | `isEmpty()`, `getString()`, `debugPrint()` | Operações seguras em Maps |
| `ListUtils` | `partition()`, `union()`, `lazyList()` | Operações específicas em Lists |
| `SetUtils` | `union()`, `intersection()`, `difference()` | Operações de conjunto |

### 10.3 Implementações Especializadas

| Classe | Uso | Complexidade |
|--------|-----|--------------|
| `HashBag` | Bag com performance O(1) | get/put: O(1) |
| `TreeBag` | Bag ordenado | get/put: O(log n) |
| `DualHashBidiMap` | BidiMap com performance O(1) | get/put: O(1) |
| `PatriciaTrie` | Trie compactado | prefixMap: O(k) |
| `CircularFifoQueue` | Fila circular limitada | add/remove: O(1) |

### 10.4 Predicados e Transformadores

```java
// Predicados úteis
PredicateUtils.nullPredicate()           // Testa se é null
PredicateUtils.notNullPredicate()        // Testa se não é null
PredicateUtils.equalPredicate(value)     // Testa igualdade
PredicateUtils.allPredicate(pred1, pred2) // AND lógico
PredicateUtils.anyPredicate(pred1, pred2) // OR lógico

// Transformadores úteis
TransformerUtils.nullTransformer()       // Sempre retorna null
TransformerUtils.constantTransformer(obj) // Sempre retorna constante
TransformerUtils.chainedTransformer(t1, t2) // Pipeline de transformações
```

---

## 🎯 Resumo e Próximos Passos

### Pontos-Chave Para Relembrar:

1. **Commons Collections 4** estende o JCF com estruturas especializadas
2. **Bag** para contagem, **BidiMap** para busca bidirecional, **MultiMap** para múltiplos valores
3. **Utilitários** como CollectionUtils fornecem operações funcionais
4. **Trie** é ideal para busca por prefixos (autocomplete, dicionários)
5. Use **predicados e transformadores** para programação funcional

### Para Aprofundar:

1. Experimente cada exemplo prático
2. Meça performance comparando com implementações do JCF
3. Combine diferentes structures (ex: Bag dentro de MultiMap)
4. Explore os predicados avançados para validações complexas

### Recursos Adicionais:

- [Documentação Oficial](https://commons.apache.org/proper/commons-collections/)
- [JavaDoc Completo](https://commons.apache.org/proper/commons-collections/apidocs/)
- [Código Fonte](https://github.com/apache/commons-collections)
