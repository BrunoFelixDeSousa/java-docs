# 📦 Apache Commons Collections 4 - Guia Completo

**Apache Commons Collections 4** é uma biblioteca que estende o Java Collections Framework com estruturas de dados especializadas, utilitários poderosos e funcionalidades funcionais.

> "Extend and augment the Java Collections Framework"

[![Maven Central](https://img.shields.io/badge/Maven%20Central-4.4-blue)](https://mvnrepository.com/artifact/org.apache.commons/commons-collections4)
[![Java](https://img.shields.io/badge/Java-8+-orange)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-Apache%202.0-green)](https://www.apache.org/licenses/LICENSE-2.0)

---

## 📋 Índice

- [Por que usar Commons Collections?](#-por-que-usar-commons-collections)
- [Instalação](#-instalação)
- [Bag - Coleções com Contagem](#-bag---coleções-com-contagem)
- [BidiMap - Maps Bidirecionais](#-bidimap---maps-bidirecionais)
- [MultiValuedMap - Múltiplos Valores](#-multivaluedmap---múltiplos-valores)
- [Trie - Busca por Prefixos](#-trie---busca-por-prefixos)
- [CollectionUtils](#-collectionutils)
- [MapUtils](#-maputils)
- [ListUtils](#-listutils)
- [Predicados e Transformadores](#-predicados-e-transformadores)
- [Collections com Expiração](#-collections-com-expiração)
- [Collections Limitadas](#-collections-limitadas)
- [Exemplos Completos](#-exemplos-completos)
- [Testing](#-testing)
- [Best Practices](#-best-practices)

---

## 🎯 Por que usar Commons Collections?

### ❌ Problema: JCF é Limitado

```java
// ❌ Java Collections Framework padrão
Map<String, String> map = new HashMap<>();

// Problemas:
// 1. Como fazer um Map que expira automaticamente?
// 2. Como fazer busca reversa (valor -> chave)?
// 3. Como armazenar múltiplos valores por chave?
// 4. Como contar ocorrências de elementos?
// 5. Como fazer busca eficiente por prefixos?
```

### ✅ Solução: Commons Collections 4

```java
// ✅ Map com expiração automática
Map<String, String> expiringMap = 
    new PassiveExpiringMap<>(60_000); // Expira em 1 minuto

// ✅ Map bidirecional (busca em ambas direções)
BidiMap<String, Integer> bidiMap = new DualHashBidiMap<>();
bidiMap.put("um", 1);
String key = bidiMap.getKey(1); // Busca reversa! "um"

// ✅ Múltiplos valores por chave
MultiValuedMap<String, String> multiMap = new ArrayListValuedHashMap<>();
multiMap.put("tags", "java");
multiMap.put("tags", "collections");

// ✅ Contagem de elementos
Bag<String> bag = new HashBag<>();
bag.add("apple", 3);
System.out.println(bag.getCount("apple")); // 3

// ✅ Busca por prefixos (autocomplete)
Trie<String, String> trie = new PatriciaTrie<>();
trie.put("car", "automóvel");
trie.put("care", "cuidado");
SortedMap<String, String> prefixed = trie.prefixMap("car");
```

### 🚀 Principais Vantagens

| Vantagem | Descrição |
|----------|-----------|
| **Estruturas Especializadas** | Bag, BidiMap, Trie, MultiValuedMap |
| **Utilitários Poderosos** | CollectionUtils, MapUtils, ListUtils |
| **Performance** | Implementações otimizadas |
| **Funcional** | Predicados, Transformadores, Closures |
| **Type-Safe** | Generics em todas APIs |
| **Battle-Tested** | Usado em milhares de projetos |

---

## 📦 Instalação

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

### Gradle Kotlin DSL

```kotlin
implementation("org.apache.commons:commons-collections4:4.4")
```

---

### Verificação da Instalação

```java
package com.example.setup;

import org.apache.commons.collections4.CollectionUtils;

/**
 * Classe para verificar se Apache Commons Collections 4 está instalado.
 */
public class SetupVerification {
    
    /**
     * Verifica a versão instalada do Commons Collections.
     *
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) {
        // Verificar se a biblioteca está disponível
        try {
            Package pkg = CollectionUtils.class.getPackage();
            String version = pkg.getImplementationVersion();
            
            System.out.println("✅ Apache Commons Collections 4 instalado!");
            System.out.println("📦 Versão: " + version);
            System.out.println("📚 Package: " + pkg.getName());
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao verificar instalação: " + e.getMessage());
        }
    }
}
```

**Output esperado**:
```
✅ Apache Commons Collections 4 instalado!
📦 Versão: 4.4
📚 Package: org.apache.commons.collections4
```

---

## 🎒 Bag - Coleções com Contagem

**Conceito**: Um `Bag` (também chamado `MultiSet`) é uma coleção que permite duplicatas e **conta quantas vezes** cada elemento aparece.

### Diferença: Set vs List vs Bag

```java
// Set - NÃO permite duplicatas
Set<String> set = new HashSet<>();
set.add("apple");
set.add("apple");
System.out.println(set.size()); // 1 ❌ Não conta duplicatas

// List - Permite duplicatas mas não conta automaticamente
List<String> list = new ArrayList<>();
list.add("apple");
list.add("apple");
System.out.println(list.size()); // 2
// Precisa contar manualmente com Collections.frequency()

// Bag - Permite duplicatas E conta automaticamente! ✅
Bag<String> bag = new HashBag<>();
bag.add("apple");
bag.add("apple");
System.out.println(bag.getCount("apple")); // 2 ✅
System.out.println(bag.size()); // 2
```

---

### Exemplo Básico

```java
package com.example.collections;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;

/**
 * Demonstração básica do uso de Bag.
 */
public class BagExample {
    
    /**
     * Exemplo de uso básico de Bag para contar frutas.
     */
    public static void main(String[] args) {
        // Criar bag
        Bag<String> fruitBag = new HashBag<>();
        
        // Adicionar elementos individualmente
        fruitBag.add("apple");
        fruitBag.add("banana");
        fruitBag.add("apple");
        
        // Adicionar múltiplas ocorrências de uma vez
        fruitBag.add("orange", 3);
        
        // Contar ocorrências
        System.out.println("Maçãs: " + fruitBag.getCount("apple"));     // 2
        System.out.println("Bananas: " + fruitBag.getCount("banana"));  // 1
        System.out.println("Laranjas: " + fruitBag.getCount("orange")); // 3
        
        // Total de elementos (contando duplicatas)
        System.out.println("Total de frutas: " + fruitBag.size()); // 6
        
        // Elementos únicos
        System.out.println("Tipos de frutas: " + fruitBag.uniqueSet()); 
        // [apple, banana, orange]
        
        // Remover ocorrências
        fruitBag.remove("apple", 1); // Remove 1 maçã
        System.out.println("Maçãs após remoção: " + fruitBag.getCount("apple")); // 1
        
        // Iterar com contagem
        for (String fruit : fruitBag.uniqueSet()) {
            System.out.println(fruit + ": " + fruitBag.getCount(fruit));
        }
    }
}
```

**Output**:
```
Maçãs: 2
Bananas: 1
Laranjas: 3
Total de frutas: 6
Tipos de frutas: [apple, banana, orange]
Maçãs após remoção: 1
apple: 1
banana: 1
orange: 3
```

---

### Caso de Uso Real: Contador de Palavras

```java
package com.example.collections;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Analisador de texto usando Bag para contar palavras.
 */
public class WordCounter {
    
    private final Bag<String> wordBag;
    
    /**
     * Construtor que inicializa o bag de palavras.
     */
    public WordCounter() {
        this.wordBag = new HashBag<>();
    }
    
    /**
     * Analisa um texto e conta as palavras.
     *
     * @param text texto a ser analisado
     */
    public void analyzeText(String text) {
        // Normalizar texto: lowercase, remover pontuação, split
        String[] words = text.toLowerCase()
            .replaceAll("[^a-zA-Z\\s]", "")
            .split("\\s+");
        
        // Adicionar palavras ao bag (ignora palavras muito curtas)
        for (String word : words) {
            if (word.length() > 2) { // Ignora "de", "em", etc
                wordBag.add(word);
            }
        }
    }
    
    /**
     * Retorna a frequência de uma palavra.
     *
     * @param word palavra a consultar
     * @return número de ocorrências
     */
    public int getWordFrequency(String word) {
        return wordBag.getCount(word.toLowerCase());
    }
    
    /**
     * Retorna as N palavras mais frequentes.
     *
     * @param limit número de palavras a retornar
     * @return lista das palavras mais frequentes
     */
    public List<Map.Entry<String, Integer>> getMostFrequentWords(int limit) {
        return wordBag.uniqueSet().stream()
            .map(word -> new AbstractMap.SimpleEntry<>(word, wordBag.getCount(word)))
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Retorna estatísticas do texto.
     *
     * @return mapa com estatísticas
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalWords", wordBag.size());
        stats.put("uniqueWords", wordBag.uniqueSet().size());
        stats.put("vocabularyRichness", 
            (double) wordBag.uniqueSet().size() / wordBag.size());
        
        return stats;
    }
    
    /**
     * Exemplo de uso do contador de palavras.
     */
    public static void main(String[] args) {
        WordCounter counter = new WordCounter();
        
        String texto = """
            Java é uma linguagem de programação orientada a objetos.
            Java é popular para desenvolvimento web e mobile.
            Programação em Java requer conhecimento de orientação a objetos.
            """;
        
        counter.analyzeText(texto);
        
        // Frequência de palavras específicas
        System.out.println("Frequência de 'java': " + 
            counter.getWordFrequency("java"));
        
        // Top 5 palavras mais frequentes
        System.out.println("\nTop 5 palavras mais frequentes:");
        counter.getMostFrequentWords(5)
            .forEach(entry -> System.out.println(
                entry.getKey() + ": " + entry.getValue() + " vezes"
            ));
        
        // Estatísticas
        System.out.println("\nEstatísticas:");
        counter.getStatistics().forEach((key, value) -> 
            System.out.println(key + ": " + value)
        );
    }
}
```

**Output**:
```
Frequência de 'java': 3

Top 5 palavras mais frequentes:
java: 3
objetos: 2
programação: 2
orientada: 1
linguagem: 1

Estatísticas:
totalWords: 18
uniqueWords: 13
vocabularyRichness: 0.7222222222222222
```

---

### Implementações de Bag

```java
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.*;

// HashBag - Performance O(1) para operações básicas
Bag<String> hashBag = new HashBag<>(); // Não ordenado

// TreeBag - Mantém elementos ordenados, O(log n)
Bag<String> treeBag = new TreeBag<>(); // Ordenado naturalmente

// TreeBag com Comparator customizado
Bag<String> customTreeBag = new TreeBag<>(String.CASE_INSENSITIVE_ORDER);

// SynchronizedBag - Thread-safe
Bag<String> syncBag = SynchronizedBag.synchronizedBag(new HashBag<>());

// UnmodifiableBag - Read-only
Bag<String> unmodBag = UnmodifiableBag.unmodifiableBag(hashBag);
```

---

### API Completa do Bag

```java
/**
 * Demonstração completa da API de Bag.
 */
public class BagApiDemo {
    
    public static void main(String[] args) {
        Bag<String> bag = new HashBag<>();
        
        // === ADIÇÃO ===
        bag.add("item");              // Adiciona 1 ocorrência
        bag.add("item", 5);           // Adiciona 5 ocorrências
        
        // === CONSULTA ===
        int count = bag.getCount("item");     // Retorna contagem
        boolean contains = bag.contains("item"); // Verifica existência
        int size = bag.size();                // Total (com duplicatas)
        Set<String> unique = bag.uniqueSet(); // Elementos únicos
        
        // === REMOÇÃO ===
        bag.remove("item");           // Remove 1 ocorrência
        bag.remove("item", 3);        // Remove 3 ocorrências
        bag.removeAll(Arrays.asList("item")); // Remove todas ocorrências
        
        // === ITERAÇÃO ===
        // Itera sobre elementos únicos
        for (String item : bag.uniqueSet()) {
            System.out.println(item + ": " + bag.getCount(item));
        }
        
        // Itera sobre todas ocorrências (com duplicatas)
        for (String item : bag) {
            System.out.println(item);
        }
        
        // === CONVERSÃO ===
        Map<String, Integer> mapOfCounts = bag.toMapOfCounts();
        
        // === OPERAÇÕES DE CONJUNTO ===
        Bag<String> other = new HashBag<>();
        bag.retainAll(other);  // Interseção
        bag.addAll(other);     // União
    }
}
```

---

## 🔄 BidiMap - Maps Bidirecionais

**Conceito**: Um `BidiMap` é um Map que permite busca **em ambas as direções**: chave → valor E valor → chave.

### Por que usar BidiMap?

```java
// ❌ Map normal - busca só em uma direção
Map<String, Integer> normalMap = new HashMap<>();
normalMap.put("um", 1);
normalMap.put("dois", 2);

Integer valor = normalMap.get("um"); // ✅ Funciona
// String chave = normalMap.getKey(1); // ❌ Não existe!

// Para busca reversa, precisa iterar manualmente:
String chaveEncontrada = null;
for (Map.Entry<String, Integer> entry : normalMap.entrySet()) {
    if (entry.getValue().equals(1)) {
        chaveEncontrada = entry.getKey();
        break;
    }
}
// Ineficiente! O(n)

// ✅ BidiMap - busca em AMBAS direções
BidiMap<String, Integer> bidiMap = new DualHashBidiMap<>();
bidiMap.put("um", 1);
bidiMap.put("dois", 2);

Integer valor = bidiMap.get("um");  // ✅ Chave -> Valor
String chave = bidiMap.getKey(1);   // ✅ Valor -> Chave (O(1)!)
```

---

### Exemplo Básico

```java
package com.example.collections;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

/**
 * Demonstração básica de BidiMap.
 */
public class BidiMapExample {
    
    /**
     * Exemplo de uso básico de BidiMap.
     */
    public static void main(String[] args) {
        // Criar BidiMap
        BidiMap<String, Integer> numberMap = new DualHashBidiMap<>();
        
        // Adicionar elementos
        numberMap.put("um", 1);
        numberMap.put("dois", 2);
        numberMap.put("três", 3);
        
        // Busca normal (chave -> valor)
        Integer valor = numberMap.get("dois");
        System.out.println("Valor de 'dois': " + valor); // 2
        
        // Busca reversa (valor -> chave) ✨
        String chave = numberMap.getKey(2);
        System.out.println("Chave do valor 2: " + chave); // "dois"
        
        // Map inverso
        BidiMap<Integer, String> inverso = numberMap.inverseBidiMap();
        String chaveInversa = inverso.get(1);
        System.out.println("Chave inversa de 1: " + chaveInversa); // "um"
        
        // Remover por chave
        numberMap.remove("um");
        
        // Remover por valor
        numberMap.removeValue(3);
        
        System.out.println("Map final: " + numberMap); // {dois=2}
    }
}
```

**Output**:
```
Valor de 'dois': 2
Chave do valor 2: dois
Chave inversa de 1: um
Map final: {dois=2}
```

---

### Caso de Uso Real: Sistema de IDs Bidirecionais

```java
package com.example.collections;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import java.util.Optional;

/**
 * Gerenciador de usuários com mapeamento bidirecional entre username e ID.
 */
public class UserManager {
    
    private final BidiMap<String, Long> userIdMap;
    private long nextId = 1L;
    
    /**
     * Construtor que inicializa o mapa bidirecional.
     */
    public UserManager() {
        this.userIdMap = new DualHashBidiMap<>();
    }
    
    /**
     * Registra um novo usuário.
     *
     * @param username nome do usuário
     * @return ID gerado para o usuário
     * @throws IllegalArgumentException se username já existe
     */
    public Long registerUser(String username) {
        if (userIdMap.containsKey(username)) {
            throw new IllegalArgumentException("Username já existe: " + username);
        }
        
        Long id = nextId++;
        userIdMap.put(username, id);
        return id;
    }
    
    /**
     * Busca ID pelo username.
     *
     * @param username nome do usuário
     * @return Optional com ID se encontrado
     */
    public Optional<Long> getUserId(String username) {
        return Optional.ofNullable(userIdMap.get(username));
    }
    
    /**
     * Busca username pelo ID (busca reversa).
     *
     * @param id ID do usuário
     * @return Optional com username se encontrado
     */
    public Optional<String> getUsername(Long id) {
        return Optional.ofNullable(userIdMap.getKey(id));
    }
    
    /**
     * Remove usuário pelo username.
     *
     * @param username nome do usuário
     * @return true se removido com sucesso
     */
    public boolean removeByUsername(String username) {
        return userIdMap.remove(username) != null;
    }
    
    /**
     * Remove usuário pelo ID (busca reversa).
     *
     * @param id ID do usuário
     * @return true se removido com sucesso
     */
    public boolean removeById(Long id) {
        return userIdMap.removeValue(id) != null;
    }
    
    /**
     * Retorna total de usuários registrados.
     *
     * @return número de usuários
     */
    public int getTotalUsers() {
        return userIdMap.size();
    }
    
    /**
     * Exemplo de uso do UserManager.
     */
    public static void main(String[] args) {
        UserManager manager = new UserManager();
        
        // Registrar usuários
        Long id1 = manager.registerUser("alice");
        Long id2 = manager.registerUser("bob");
        Long id3 = manager.registerUser("charlie");
        
        System.out.println("Alice ID: " + id1);
        System.out.println("Bob ID: " + id2);
        
        // Buscar por username
        manager.getUserId("alice")
            .ifPresent(id -> System.out.println("ID de Alice: " + id));
        
        // Buscar por ID (reverso)
        manager.getUsername(2L)
            .ifPresent(name -> System.out.println("Username do ID 2: " + name));
        
        // Remover por ID
        manager.removeById(3L);
        System.out.println("Total após remoção: " + manager.getTotalUsers());
    }
}
```

**Output**:
```
Alice ID: 1
Bob ID: 2
ID de Alice: 1
Username do ID 2: bob
Total após remoção: 2
```

---

### Implementações de BidiMap

```java
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.*;

// DualHashBidiMap - Usa 2 HashMaps internos, O(1) para ambas direções
BidiMap<String, Integer> dualHash = new DualHashBidiMap<>();

// TreeBidiMap - Ordenado, O(log n) para ambas direções
BidiMap<String, Integer> tree = new TreeBidiMap<>();

// UnmodifiableBidiMap - Read-only
BidiMap<String, Integer> unmod = UnmodifiableBidiMap.unmodifiableBidiMap(dualHash);
```

---

### Características Importantes

```java
/**
 * Demonstra características importantes de BidiMap.
 */
public class BidiMapCharacteristics {
    
    public static void main(String[] args) {
        BidiMap<String, Integer> map = new DualHashBidiMap<>();
        
        // 1. Valores também devem ser únicos!
        map.put("um", 1);
        map.put("one", 1); // ⚠️ Remove "um" automaticamente!
        System.out.println(map); // {one=1}
        
        // 2. inverseBidiMap() retorna view (não cópia)
        BidiMap<Integer, String> inverso = map.inverseBidiMap();
        inverso.put(2, "dois"); // Modifica o map original!
        System.out.println(map); // {one=1, dois=2}
        
        // 3. mapIterator() para iteração eficiente
        map.put("três", 3);
        for (var it = map.mapIterator(); it.hasNext(); ) {
            String key = it.next();
            Integer value = it.getValue();
            System.out.println(key + " -> " + value);
        }
    }
}
```

---

## 🗂️ MultiValuedMap - Múltiplos Valores

**Conceito**: Um `MultiValuedMap` é como um Map onde **cada chave pode ter múltiplos valores**.

### Por que usar MultiValuedMap?

```java
// ❌ Map normal - apenas 1 valor por chave
Map<String, String> normalMap = new HashMap<>();
normalMap.put("tags", "java");
normalMap.put("tags", "kotlin"); // Sobrescreve! ❌

// Solução manual: Map<String, List<String>>
Map<String, List<String>> mapOfLists = new HashMap<>();
mapOfLists.computeIfAbsent("tags", k -> new ArrayList<>()).add("java");
mapOfLists.computeIfAbsent("tags", k -> new ArrayList<>()).add("kotlin");
// Verboso e propenso a erros

// ✅ MultiValuedMap - API limpa e direta
MultiValuedMap<String, String> multiMap = new ArrayListValuedHashMap<>();
multiMap.put("tags", "java");
multiMap.put("tags", "kotlin");
Collection<String> tags = multiMap.get("tags"); // [java, kotlin]
```

---

### Exemplo Básico

```java
package com.example.collections;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

/**
 * Demonstração básica de MultiValuedMap.
 */
public class MultiValuedMapExample {
    
    /**
     * Exemplo de uso básico de MultiValuedMap.
     */
    public static void main(String[] args) {
        // Criar MultiValuedMap (valores em ArrayList)
        MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();
        
        // Adicionar múltiplos valores para mesma chave
        map.put("frutas", "maçã");
        map.put("frutas", "banana");
        map.put("frutas", "laranja");
        
        map.put("vegetais", "cenoura");
        map.put("vegetais", "brócolis");
        
        // Obter todos valores de uma chave
        Collection<String> frutas = map.get("frutas");
        System.out.println("Frutas: " + frutas); 
        // [maçã, banana, laranja]
        
        // Verificar existência
        boolean hasFrutas = map.containsKey("frutas"); // true
        boolean hasBanana = map.containsValue("banana"); // true
        
        // Remover valor específico
        map.removeMapping("frutas", "banana");
        System.out.println("Frutas após remoção: " + map.get("frutas"));
        // [maçã, laranja]
        
        // Remover todos valores de uma chave
        map.remove("vegetais");
        
        // Total de valores (não de chaves)
        System.out.println("Total de valores: " + map.size()); // 2
        
        // Total de chaves
        System.out.println("Total de chaves: " + map.keySet().size()); // 1
    }
}
```

**Output**:
```
Frutas: [maçã, banana, laranja]
Frutas após remoção: [maçã, laranja]
Total de valores: 2
Total de chaves: 1
```

---

### Caso de Uso Real: Sistema de Tags

```java
package com.example.collections;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Gerenciador de tags para artigos usando MultiValuedMap.
 */
public class ArticleTagManager {
    
    // HashSetValuedHashMap - não permite tags duplicadas
    private final MultiValuedMap<String, String> articleTags;
    private final MultiValuedMap<String, String> tagArticles; // Índice inverso
    
    /**
     * Construtor que inicializa os mapas.
     */
    public ArticleTagManager() {
        this.articleTags = new HashSetValuedHashMap<>();
        this.tagArticles = new HashSetValuedHashMap<>();
    }
    
    /**
     * Adiciona uma tag a um artigo.
     *
     * @param articleId ID do artigo
     * @param tag tag a adicionar
     */
    public void addTag(String articleId, String tag) {
        articleTags.put(articleId, tag.toLowerCase());
        tagArticles.put(tag.toLowerCase(), articleId);
    }
    
    /**
     * Adiciona múltiplas tags a um artigo.
     *
     * @param articleId ID do artigo
     * @param tags tags a adicionar
     */
    public void addTags(String articleId, String... tags) {
        for (String tag : tags) {
            addTag(articleId, tag);
        }
    }
    
    /**
     * Retorna todas tags de um artigo.
     *
     * @param articleId ID do artigo
     * @return coleção de tags
     */
    public Collection<String> getArticleTags(String articleId) {
        return articleTags.get(articleId);
    }
    
    /**
     * Retorna todos artigos com determinada tag.
     *
     * @param tag tag a buscar
     * @return coleção de IDs de artigos
     */
    public Collection<String> getArticlesByTag(String tag) {
        return tagArticles.get(tag.toLowerCase());
    }
    
    /**
     * Remove uma tag de um artigo.
     *
     * @param articleId ID do artigo
     * @param tag tag a remover
     * @return true se removido com sucesso
     */
    public boolean removeTag(String articleId, String tag) {
        boolean removed = articleTags.removeMapping(articleId, tag.toLowerCase());
        if (removed) {
            tagArticles.removeMapping(tag.toLowerCase(), articleId);
        }
        return removed;
    }
    
    /**
     * Busca artigos que têm TODAS as tags especificadas.
     *
     * @param tags tags obrigatórias
     * @return conjunto de IDs de artigos
     */
    public Set<String> findArticlesWithAllTags(String... tags) {
        if (tags.length == 0) return Collections.emptySet();
        
        // Começar com artigos da primeira tag
        Set<String> result = new HashSet<>(getArticlesByTag(tags[0]));
        
        // Fazer interseção com artigos das outras tags
        for (int i = 1; i < tags.length; i++) {
            result.retainAll(getArticlesByTag(tags[i]));
        }
        
        return result;
    }
    
    /**
     * Busca artigos que têm PELO MENOS UMA das tags especificadas.
     *
     * @param tags tags opcionais
     * @return conjunto de IDs de artigos
     */
    public Set<String> findArticlesWithAnyTag(String... tags) {
        Set<String> result = new HashSet<>();
        for (String tag : tags) {
            result.addAll(getArticlesByTag(tag));
        }
        return result;
    }
    
    /**
     * Retorna as tags mais populares.
     *
     * @param limit número de tags a retornar
     * @return lista de tags ordenadas por popularidade
     */
    public List<Map.Entry<String, Integer>> getPopularTags(int limit) {
        return tagArticles.keySet().stream()
            .map(tag -> new AbstractMap.SimpleEntry<>(
                tag, 
                tagArticles.get(tag).size()
            ))
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Exemplo de uso do gerenciador de tags.
     */
    public static void main(String[] args) {
        ArticleTagManager manager = new ArticleTagManager();
        
        // Adicionar tags a artigos
        manager.addTags("article1", "java", "spring", "backend");
        manager.addTags("article2", "java", "collections", "tutorial");
        manager.addTags("article3", "kotlin", "spring", "backend");
        
        // Buscar tags de um artigo
        System.out.println("Tags do article1: " + 
            manager.getArticleTags("article1"));
        
        // Buscar artigos por tag
        System.out.println("Artigos com tag 'java': " + 
            manager.getArticlesByTag("java"));
        
        // Buscar artigos com TODAS as tags
        System.out.println("Artigos com 'java' E 'spring': " + 
            manager.findArticlesWithAllTags("java", "spring"));
        
        // Buscar artigos com QUALQUER tag
        System.out.println("Artigos com 'kotlin' OU 'collections': " + 
            manager.findArticlesWithAnyTag("kotlin", "collections"));
        
        // Tags mais populares
        System.out.println("\nTags mais populares:");
        manager.getPopularTags(3)
            .forEach(entry -> System.out.println(
                entry.getKey() + ": " + entry.getValue() + " artigos"
            ));
    }
}
```

**Output**:
```
Tags do article1: [java, spring, backend]
Artigos com tag 'java': [article1, article2]
Artigos com 'java' E 'spring': [article1]
Artigos com 'kotlin' OU 'collections': [article2, article3]

Tags mais populares:
java: 2 artigos
spring: 2 artigos
backend: 2 artigos
```

---

### Implementações de MultiValuedMap

```java
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.*;

// ArrayListValuedHashMap - Valores em ArrayList (permite duplicatas)
MultiValuedMap<String, String> arrayList = new ArrayListValuedHashMap<>();

// HashSetValuedHashMap - Valores em HashSet (não permite duplicatas)
MultiValuedMap<String, String> hashSet = new HashSetValuedHashMap<>();

// ArrayListValuedHashMap com capacidade inicial
MultiValuedMap<String, String> withCapacity = 
    new ArrayListValuedHashMap<>(100);

// UnmodifiableMultiValuedMap - Read-only
MultiValuedMap<String, String> unmod = 
    UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap(arrayList);
```

---

## 🌳 Trie - Busca por Prefixos

**Conceito**: Um `Trie` (também chamado de Prefix Tree) é uma estrutura de dados otimizada para **busca eficiente por prefixos**.

### Por que usar Trie?

```java
// ❌ Map normal - busca por prefixo é ineficiente
Map<String, String> normalMap = new HashMap<>();
normalMap.put("car", "automóvel");
normalMap.put("card", "cartão");
normalMap.put("care", "cuidado");
normalMap.put("careful", "cuidadoso");

// Para buscar palavras com prefixo "car", precisa iterar TUDO
List<String> matches = new ArrayList<>();
for (String key : normalMap.keySet()) {
    if (key.startsWith("car")) {
        matches.add(key);
    }
}
// O(n) onde n = total de chaves ❌

// ✅ Trie - busca por prefixo é O(k) onde k = tamanho do prefixo
Trie<String, String> trie = new PatriciaTrie<>();
trie.put("car", "automóvel");
trie.put("card", "cartão");
trie.put("care", "cuidado");
trie.put("careful", "cuidadoso");

// Buscar todas palavras com prefixo "car"
SortedMap<String, String> prefixed = trie.prefixMap("car");
// {car=automóvel, card=cartão, care=cuidado, careful=cuidadoso}
// O(k) onde k = 3 (tamanho do prefixo) ✅
```

---

### Exemplo Básico

```java
package com.example.collections;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import java.util.SortedMap;

/**
 * Demonstração básica de Trie.
 */
public class TrieExample {
    
    /**
     * Exemplo de uso básico de Trie.
     */
    public static void main(String[] args) {
        // Criar Trie
        Trie<String, String> trie = new PatriciaTrie<>();
        
        // Adicionar palavras
        trie.put("apple", "maçã");
        trie.put("application", "aplicação");
        trie.put("apply", "aplicar");
        trie.put("banana", "banana");
        trie.put("band", "banda");
        
        // Busca exata (como Map normal)
        String valor = trie.get("apple");
        System.out.println("apple: " + valor); // maçã
        
        // Busca por prefixo ✨
        SortedMap<String, String> appWords = trie.prefixMap("app");
        System.out.println("\nPalavras com 'app':");
        appWords.forEach((k, v) -> System.out.println("  " + k + " -> " + v));
        
        // Outro prefixo
        SortedMap<String, String> banWords = trie.prefixMap("ban");
        System.out.println("\nPalavras com 'ban':");
        banWords.forEach((k, v) -> System.out.println("  " + k + " -> " + v));
        
        // Primeira e última chave
        System.out.println("\nPrimeira chave: " + trie.firstKey());
        System.out.println("Última chave: " + trie.lastKey());
    }
}
```

**Output**:
```
apple: maçã

Palavras com 'app':
  apple -> maçã
  application -> aplicação
  apply -> aplicar

Palavras com 'ban':
  banana -> banana
  band -> banda

Primeira chave: apple
Última chave: band
```

---

### Caso de Uso Real: Sistema de Autocomplete

```java
package com.example.collections;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Sistema de autocomplete usando Trie.
 * Ideal para busca em dicionários, comandos, produtos, etc.
 */
public class AutocompleteService {
    
    private final Trie<String, WordEntry> dictionary;
    
    /**
     * Entrada do dicionário com palavra, definição e frequência.
     */
    public static class WordEntry {
        private final String word;
        private final String definition;
        private int frequency; // Quantidade de buscas
        
        public WordEntry(String word, String definition) {
            this.word = word;
            this.definition = definition;
            this.frequency = 0;
        }
        
        public void incrementFrequency() {
            this.frequency++;
        }
        
        public int getFrequency() {
            return frequency;
        }
        
        public String getWord() {
            return word;
        }
        
        public String getDefinition() {
            return definition;
        }
        
        @Override
        public String toString() {
            return word + " (" + frequency + " buscas) - " + definition;
        }
    }
    
    /**
     * Construtor que inicializa o Trie.
     */
    public AutocompleteService() {
        this.dictionary = new PatriciaTrie<>();
    }
    
    /**
     * Adiciona uma palavra ao dicionário.
     *
     * @param word palavra
     * @param definition definição
     */
    public void addWord(String word, String definition) {
        String key = word.toLowerCase();
        dictionary.put(key, new WordEntry(word, definition));
    }
    
    /**
     * Retorna sugestões para um prefixo.
     *
     * @param prefix prefixo a buscar
     * @param maxResults número máximo de resultados
     * @return lista de sugestões ordenadas por frequência
     */
    public List<String> getSuggestions(String prefix, int maxResults) {
        String searchPrefix = prefix.toLowerCase();
        
        // Buscar todas palavras com o prefixo
        SortedMap<String, WordEntry> matches = dictionary.prefixMap(searchPrefix);
        
        // Incrementar frequência das palavras encontradas
        matches.values().forEach(WordEntry::incrementFrequency);
        
        // Retornar as mais frequentes
        return matches.values().stream()
            .sorted(Comparator.comparingInt(WordEntry::getFrequency).reversed())
            .limit(maxResults)
            .map(WordEntry::getWord)
            .collect(Collectors.toList());
    }
    
    /**
     * Busca definição de uma palavra.
     *
     * @param word palavra a buscar
     * @return Optional com definição se encontrada
     */
    public Optional<String> getDefinition(String word) {
        WordEntry entry = dictionary.get(word.toLowerCase());
        return Optional.ofNullable(entry).map(WordEntry::getDefinition);
    }
    
    /**
     * Retorna estatísticas do dicionário.
     *
     * @return mapa com estatísticas
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalWords", dictionary.size());
        stats.put("mostSearched", getMostSearchedWords(5));
        
        return stats;
    }
    
    /**
     * Retorna as palavras mais buscadas.
     *
     * @param limit número de palavras
     * @return lista de palavras
     */
    private List<WordEntry> getMostSearchedWords(int limit) {
        return dictionary.values().stream()
            .filter(entry -> entry.getFrequency() > 0)
            .sorted(Comparator.comparingInt(WordEntry::getFrequency).reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Exemplo de uso do sistema de autocomplete.
     */
    public static void main(String[] args) {
        AutocompleteService service = new AutocompleteService();
        
        // Adicionar palavras ao dicionário
        service.addWord("java", "Linguagem de programação orientada a objetos");
        service.addWord("javascript", "Linguagem de script para web");
        service.addWord("python", "Linguagem de programação de alto nível");
        service.addWord("jakarta", "Especificações Java EE");
        service.addWord("javalin", "Framework web leve para Java");
        
        // Buscar sugestões
        System.out.println("Sugestões para 'jav':");
        service.getSuggestions("jav", 5)
            .forEach(word -> System.out.println("  - " + word));
        
        System.out.println("\nSugestões para 'java' (mais específico):");
        service.getSuggestions("java", 5)
            .forEach(word -> System.out.println("  - " + word));
        
        // Simular algumas buscas
        service.getSuggestions("java", 3); // Incrementa frequência
        service.getSuggestions("java", 3);
        service.getSuggestions("pyt", 3);
        
        // Definição de palavra
        service.getDefinition("javascript")
            .ifPresent(def -> System.out.println("\nDefinição de 'javascript': " + def));
        
        // Estatísticas
        System.out.println("\nPalavras mais buscadas:");
        @SuppressWarnings("unchecked")
        List<WordEntry> mostSearched = (List<WordEntry>) 
            service.getStatistics().get("mostSearched");
        mostSearched.forEach(System.out::println);
    }
}
```

**Output**:
```
Sugestões para 'jav':
  - java
  - javascript
  - jakarta
  - javalin

Sugestões para 'java' (mais específico):
  - java
  - javascript
  - javalin

Definição de 'javascript': Linguagem de script para web

Palavras mais buscadas:
java (3 buscas) - Linguagem de programação orientada a objetos
javascript (2 buscas) - Linguagem de script para web
javalin (2 buscas) - Framework web leve para Java
python (1 buscas) - Linguagem de programação de alto nível
jakarta (1 buscas) - Especificações Java EE
```

---

### Métodos Importantes do Trie

```java
/**
 * Demonstração de métodos importantes do Trie.
 */
public class TrieApiDemo {
    
    public static void main(String[] args) {
        Trie<String, Integer> trie = new PatriciaTrie<>();
        
        // === ADIÇÃO (como Map) ===
        trie.put("key", 1);
        trie.putAll(Map.of("key2", 2, "key3", 3));
        
        // === BUSCA EXATA (como Map) ===
        Integer value = trie.get("key");
        boolean contains = trie.containsKey("key");
        
        // === BUSCA POR PREFIXO ✨ ===
        SortedMap<String, Integer> prefixed = trie.prefixMap("ke");
        // Retorna SortedMap com todas chaves que começam com "ke"
        
        // === NAVEGAÇÃO (como SortedMap) ===
        String firstKey = trie.firstKey();  // Menor chave
        String lastKey = trie.lastKey();    // Maior chave
        
        // === SUBMAP ===
        SortedMap<String, Integer> sub = trie.subMap("key1", "key9");
        
        // === HEADMAP / TAILMAP ===
        SortedMap<String, Integer> head = trie.headMap("key5");
        SortedMap<String, Integer> tail = trie.tailMap("key5");
    }
}
```

---

## 🛠️ CollectionUtils

**`CollectionUtils`** é a classe utilitária mais importante do Commons Collections. Fornece métodos estáticos para operações com coleções.

### Operações de Conjunto

```java
package com.example.collections;

import org.apache.commons.collections4.CollectionUtils;
import java.util.*;

/**
 * Demonstração de operações de conjunto com CollectionUtils.
 */
public class CollectionUtilsSetOps {
    
    /**
     * Demonstra operações de conjunto (union, intersection, subtract).
     */
    public static void main(String[] args) {
        List<String> list1 = Arrays.asList("a", "b", "c", "d");
        List<String> list2 = Arrays.asList("c", "d", "e", "f");
        
        // === UNIÃO (todos elementos) ===
        Collection<String> union = CollectionUtils.union(list1, list2);
        System.out.println("União: " + union);
        // [a, b, c, d, e, f]
        
        // === INTERSEÇÃO (elementos em comum) ===
        Collection<String> intersection = CollectionUtils.intersection(list1, list2);
        System.out.println("Interseção: " + intersection);
        // [c, d]
        
        // === SUBTRAÇÃO (elementos em list1 mas não em list2) ===
        Collection<String> subtract = CollectionUtils.subtract(list1, list2);
        System.out.println("Subtração (list1 - list2): " + subtract);
        // [a, b]
        
        // === DIFERENÇA SIMÉTRICA (elementos que NÃO estão em ambos) ===
        Collection<String> disjunction = CollectionUtils.disjunction(list1, list2);
        System.out.println("Diferença simétrica: " + disjunction);
        // [a, b, e, f]
    }
}
```

**Output**:
```
União: [a, b, c, d, e, f]
Interseção: [c, d]
Subtração (list1 - list2): [a, b]
Diferença simétrica: [a, b, e, f]
```

---

### Verificações e Validações

```java
package com.example.collections;

import org.apache.commons.collections4.CollectionUtils;
import java.util.*;

/**
 * Demonstração de verificações com CollectionUtils.
 */
public class CollectionUtilsChecks {
    
    /**
     * Demonstra verificações úteis.
     */
    public static void main(String[] args) {
        List<String> list1 = Arrays.asList("a", "b", "c");
        List<String> list2 = Arrays.asList("a", "b", "c");
        List<String> list3 = Arrays.asList("a", "b");
        List<String> emptyList = new ArrayList<>();
        List<String> nullList = null;
        
        // === VERIFICAR SE É VAZIO ===
        System.out.println("isEmpty (emptyList): " + 
            CollectionUtils.isEmpty(emptyList)); // true
        
        System.out.println("isEmpty (nullList): " + 
            CollectionUtils.isEmpty(nullList)); // true (null-safe!)
        
        System.out.println("isNotEmpty (list1): " + 
            CollectionUtils.isNotEmpty(list1)); // true
        
        // === IGUALDADE ===
        System.out.println("\nisEqualCollection (list1, list2): " + 
            CollectionUtils.isEqualCollection(list1, list2)); // true
        
        System.out.println("isEqualCollection (list1, list3): " + 
            CollectionUtils.isEqualCollection(list1, list3)); // false
        
        // === SUBCOLEÇÃO ===
        System.out.println("\nisSubCollection (list3, list1): " + 
            CollectionUtils.isSubCollection(list3, list1)); // true
        
        // === DISJUNÇÃO (sem elementos em comum) ===
        List<String> list4 = Arrays.asList("x", "y", "z");
        System.out.println("\ncontainsAny (list1, list4): " + 
            CollectionUtils.containsAny(list1, list4)); // false
        
        System.out.println("containsAll (list1, list3): " + 
            CollectionUtils.containsAll(list1, list3)); // true
    }
}
```

**Output**:
```
isEmpty (emptyList): true
isEmpty (nullList): true
isNotEmpty (list1): true

isEqualCollection (list1, list2): true
isEqualCollection (list1, list3): false

isSubCollection (list3, list1): true

containsAny (list1, list4): false
containsAll (list1, list3): true
```

---

### Transformações Funcionais

```java
package com.example.collections;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import java.util.*;

/**
 * Demonstração de transformações com CollectionUtils.
 */
public class CollectionUtilsTransform {
    
    /**
     * Demonstra transformações de elementos.
     */
    public static void main(String[] args) {
        List<String> names = Arrays.asList("joão", "maria", "pedro");
        
        // === TRANSFORMAR (map) ===
        Collection<String> upperNames = CollectionUtils.collect(
            names, 
            String::toUpperCase
        );
        System.out.println("Nomes em maiúsculo: " + upperNames);
        // [JOÃO, MARIA, PEDRO]
        
        // Transformar para Integer (tamanho)
        Collection<Integer> lengths = CollectionUtils.collect(
            names,
            String::length
        );
        System.out.println("Tamanhos: " + lengths);
        // [4, 5, 5]
        
        // === FILTRAR (filter) ===
        Collection<String> longNames = CollectionUtils.select(
            names,
            name -> name.length() > 4
        );
        System.out.println("\nNomes com mais de 4 letras: " + longNames);
        // [maria, pedro]
        
        // === REJEITAR (inverse filter) ===
        Collection<String> shortNames = CollectionUtils.selectRejected(
            names,
            name -> name.length() > 4
        );
        System.out.println("Nomes com 4 letras ou menos: " + shortNames);
        // [joão]
        
        // === CONTAR ELEMENTOS QUE SATISFAZEM CONDIÇÃO ===
        int count = CollectionUtils.countMatches(
            names,
            name -> name.startsWith("m")
        );
        System.out.println("\nNomes que começam com 'm': " + count);
        // 1
    }
}
```

**Output**:
```
Nomes em maiúsculo: [JOÃO, MARIA, PEDRO]
Tamanhos: [4, 5, 5]

Nomes com mais de 4 letras: [maria, pedro]
Nomes com 4 letras ou menos: [joão]

Nomes que começam com 'm': 1
```

---

### Operações Avançadas

```java
package com.example.collections;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import java.util.*;

/**
 * Operações avançadas com CollectionUtils.
 */
public class CollectionUtilsAdvanced {
    
    /**
     * Demonstra operações avançadas.
     */
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // === ENCONTRAR (find) ===
        Integer firstEven = CollectionUtils.find(numbers, n -> n % 2 == 0);
        System.out.println("Primeiro par: " + firstEven); // 2
        
        // === VERIFICAR SE EXISTE (exists) ===
        boolean hasEven = CollectionUtils.exists(numbers, n -> n % 2 == 0);
        System.out.println("Tem número par? " + hasEven); // true
        
        // === VERIFICAR SE TODOS SATISFAZEM (matchesAll) ===
        boolean allPositive = CollectionUtils.matchesAll(numbers, n -> n > 0);
        System.out.println("Todos positivos? " + allPositive); // true
        
        boolean allEven = CollectionUtils.matchesAll(numbers, n -> n % 2 == 0);
        System.out.println("Todos pares? " + allEven); // false
        
        // === FREQUÊNCIA ===
        List<String> fruits = Arrays.asList("apple", "banana", "apple", "orange", "apple");
        int freq = CollectionUtils.cardinality("apple", fruits);
        System.out.println("\nFrequência de 'apple': " + freq); // 3
        
        // === ADICIONAR TUDO (addAll) - null-safe ===
        Collection<String> collection = new ArrayList<>();
        CollectionUtils.addAll(collection, "a", "b", "c");
        System.out.println("Coleção: " + collection); // [a, b, c]
        
        // === ADICIONAR SE NÃO NULO (addIgnoreNull) ===
        CollectionUtils.addIgnoreNull(collection, null); // Não adiciona
        CollectionUtils.addIgnoreNull(collection, "d");   // Adiciona
        System.out.println("Após addIgnoreNull: " + collection); // [a, b, c, d]
    }
}
```

---

## 🗺️ MapUtils

**`MapUtils`** fornece utilitários para trabalhar com Maps de forma segura e eficiente.

```java
package com.example.collections;

import org.apache.commons.collections4.MapUtils;
import java.util.*;

/**
 * Demonstração de MapUtils.
 */
public class MapUtilsDemo {
    
    /**
     * Demonstra operações úteis com Maps.
     */
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "João");
        map.put("age", 30);
        map.put("height", 1.75);
        map.put("active", true);
        
        // === VERIFICAÇÕES NULL-SAFE ===
        System.out.println("isEmpty: " + MapUtils.isEmpty(map)); // false
        System.out.println("isNotEmpty: " + MapUtils.isNotEmpty(map)); // true
        
        Map<String, Object> nullMap = null;
        System.out.println("isEmpty (nullMap): " + MapUtils.isEmpty(nullMap)); // true
        
        // === GET COM TIPO E DEFAULT ===
        String name = MapUtils.getString(map, "name", "Unknown");
        System.out.println("\nName: " + name); // João
        
        Integer age = MapUtils.getInteger(map, "age", 0);
        System.out.println("Age: " + age); // 30
        
        Double height = MapUtils.getDouble(map, "height", 0.0);
        System.out.println("Height: " + height); // 1.75
        
        Boolean active = MapUtils.getBoolean(map, "active", false);
        System.out.println("Active: " + active); // true
        
        // Chave não existe - retorna default
        String city = MapUtils.getString(map, "city", "São Paulo");
        System.out.println("City: " + city); // São Paulo
        
        // === DEBUG PRINT ===
        System.out.println("\n=== Debug Print ===");
        MapUtils.debugPrint(System.out, "User Data", map);
        
        // === INVERTER MAP ===
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Alice", 100);
        scores.put("Bob", 85);
        scores.put("Charlie", 100);
        
        System.out.println("\n=== Inverter Map ===");
        Map<Integer, String> inverted = MapUtils.invertMap(scores);
        System.out.println("Original: " + scores);
        System.out.println("Invertido: " + inverted);
        // Note: valores duplicados sobrescrevem (100 -> Charlie)
    }
}
```

**Output**:
```
isEmpty: false
isNotEmpty: true
isEmpty (nullMap): true

Name: João
Age: 30
Height: 1.75
Active: true
City: São Paulo

=== Debug Print ===
User Data = 
{
    name = João java.lang.String
    active = true java.lang.Boolean
    age = 30 java.lang.Integer
    height = 1.75 java.lang.Double
} java.util.HashMap

=== Inverter Map ===
Original: {Bob=85, Alice=100, Charlie=100}
Invertido: {85=Bob, 100=Charlie}
```

---

## 📜 ListUtils

**`ListUtils`** fornece utilitários específicos para Lists.

```java
package com.example.collections;

import org.apache.commons.collections4.ListUtils;
import java.util.*;

/**
 * Demonstração de ListUtils.
 */
public class ListUtilsDemo {
    
    /**
     * Demonstra operações úteis com Lists.
     */
    public static void main(String[] args) {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(4, 5, 6);
        
        // === UNIÃO ===
        List<Integer> union = ListUtils.union(list1, list2);
        System.out.println("União: " + union);
        // [1, 2, 3, 4, 5, 6]
        
        // === INTERSEÇÃO ===
        List<Integer> list3 = Arrays.asList(2, 3, 4);
        List<Integer> intersection = ListUtils.intersection(list1, list3);
        System.out.println("Interseção: " + intersection);
        // [2, 3]
        
        // === PARTICIONAMENTO ===
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<List<Integer>> partitions = ListUtils.partition(numbers, 3);
        System.out.println("\nPartições de tamanho 3:");
        partitions.forEach(System.out::println);
        // [1, 2, 3]
        // [4, 5, 6]
        // [7, 8, 9]
        // [10]
        
        // === LAZY LIST ===
        List<String> lazyList = ListUtils.lazyList(
            new ArrayList<>(),
            () -> "default"
        );
        
        // Acessa índice que não existe - cria automaticamente!
        String value = lazyList.get(5);
        System.out.println("\nValor no índice 5: " + value); // default
        System.out.println("Tamanho da lazy list: " + lazyList.size()); // 6
        
        // === LISTA DE TAMANHO FIXO ===
        List<String> baseList = Arrays.asList("a", "b", "c");
        List<String> fixedList = ListUtils.fixedSizeList(baseList);
        
        fixedList.set(0, "z"); // OK - pode modificar
        System.out.println("\nLista modificada: " + fixedList);
        
        try {
            fixedList.add("d"); // Erro - não pode adicionar
        } catch (UnsupportedOperationException e) {
            System.out.println("Não pode adicionar a lista de tamanho fixo");
        }
        
        // === SELECT ===
        List<Integer> evenNumbers = ListUtils.select(
            numbers,
            n -> n % 2 == 0
        );
        System.out.println("\nNúmeros pares: " + evenNumbers);
        // [2, 4, 6, 8, 10]
    }
}
```

**Output**:
```
União: [1, 2, 3, 4, 5, 6]
Interseção: [2, 3]

Partições de tamanho 3:
[1, 2, 3]
[4, 5, 6]
[7, 8, 9]
[10]

Valor no índice 5: default
Tamanho da lazy list: 6

Lista modificada: [z, b, c]
Não pode adicionar a lista de tamanho fixo

Números pares: [2, 4, 6, 8, 10]
```

---

## ⏱️ Collections com Expiração

**`PassiveExpiringMap`** é um Map onde as entradas **expiram automaticamente** após um tempo.

```java
package com.example.collections;

import org.apache.commons.collections4.map.PassiveExpiringMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Demonstração de PassiveExpiringMap.
 * Útil para caches temporários.
 */
public class ExpiringMapExample {
    
    /**
     * Demonstra Map com expiração automática.
     */
    public static void main(String[] args) throws InterruptedException {
        // Criar map que expira em 2 segundos
        Map<String, String> cache = new PassiveExpiringMap<>(2000);
        
        // Adicionar valores
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        
        System.out.println("Imediatamente após adicionar:");
        System.out.println("key1: " + cache.get("key1")); // value1
        System.out.println("key2: " + cache.get("key2")); // value2
        System.out.println("Tamanho: " + cache.size());   // 2
        
        // Esperar 1 segundo
        System.out.println("\nApós 1 segundo:");
        Thread.sleep(1000);
        System.out.println("key1: " + cache.get("key1")); // value1
        System.out.println("Tamanho: " + cache.size());   // 2
        
        // Esperar mais 1.5 segundos (total 2.5s)
        System.out.println("\nApós 2.5 segundos:");
        Thread.sleep(1500);
        System.out.println("key1: " + cache.get("key1")); // null (expirou!)
        System.out.println("key2: " + cache.get("key2")); // null (expirou!)
        System.out.println("Tamanho: " + cache.size());   // 0
    }
}
```

---

### Caso de Uso: Sistema de Cache

```java
package com.example.collections;

import org.apache.commons.collections4.map.PassiveExpiringMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Sistema de cache com expiração automática.
 *
 * @param <K> tipo da chave
 * @param <V> tipo do valor
 */
public class CacheService<K, V> {
    
    private final Map<K, V> cache;
    private final long ttlMillis;
    
    /**
     * Construtor com tempo de vida em milissegundos.
     *
     * @param ttlMillis tempo de vida em ms
     */
    public CacheService(long ttlMillis) {
        this.cache = new PassiveExpiringMap<>(ttlMillis);
        this.ttlMillis = ttlMillis;
    }
    
    /**
     * Construtor com tempo de vida e unidade.
     *
     * @param ttl tempo de vida
     * @param unit unidade de tempo
     */
    public CacheService(long ttl, TimeUnit unit) {
        this(unit.toMillis(ttl));
    }
    
    /**
     * Adiciona valor ao cache.
     *
     * @param key chave
     * @param value valor
     */
    public void put(K key, V value) {
        cache.put(key, value);
    }
    
    /**
     * Busca valor no cache.
     *
     * @param key chave
     * @return Optional com valor se encontrado e não expirado
     */
    public Optional<V> get(K key) {
        return Optional.ofNullable(cache.get(key));
    }
    
    /**
     * Busca valor no cache ou carrega se não existir.
     *
     * @param key chave
     * @param loader função para carregar valor
     * @return valor (do cache ou carregado)
     */
    public V getOrLoad(K key, Supplier<V> loader) {
        return get(key).orElseGet(() -> {
            V value = loader.get();
            put(key, value);
            return value;
        });
    }
    
    /**
     * Verifica se chave existe no cache.
     *
     * @param key chave
     * @return true se existe e não expirou
     */
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }
    
    /**
     * Remove valor do cache.
     *
     * @param key chave
     */
    public void remove(K key) {
        cache.remove(key);
    }
    
    /**
     * Limpa todo o cache.
     */
    public void clear() {
        cache.clear();
    }
    
    /**
     * Retorna tamanho atual do cache (sem entradas expiradas).
     *
     * @return número de entradas válidas
     */
    public int size() {
        return cache.size();
    }
    
    /**
     * Exemplo de uso do cache.
     */
    public static void main(String[] args) throws InterruptedException {
        // Cache de 5 segundos
        CacheService<String, String> userCache = new CacheService<>(5, TimeUnit.SECONDS);
        
        // Adicionar dados
        userCache.put("user:1", "Alice");
        userCache.put("user:2", "Bob");
        
        // Buscar dados
        userCache.get("user:1")
            .ifPresent(name -> System.out.println("Usuário 1: " + name));
        
        // Buscar ou carregar
        String user3 = userCache.getOrLoad("user:3", () -> {
            System.out.println("Carregando user:3 do banco...");
            return "Charlie";
        });
        System.out.println("Usuário 3: " + user3);
        
        // Segunda chamada vem do cache
        String user3Again = userCache.getOrLoad("user:3", () -> {
            System.out.println("Carregando user:3 do banco...");
            return "Charlie";
        });
        System.out.println("Usuário 3 (cache): " + user3Again);
        
        System.out.println("\nTamanho do cache: " + userCache.size());
    }
}
```

**Output**:
```
Usuário 1: Alice
Carregando user:3 do banco...
Usuário 3: Charlie
Usuário 3 (cache): Charlie

Tamanho do cache: 3
```

---

## 🔒 Collections Limitadas

### CircularFifoQueue - Fila Circular

```java
package com.example.collections;

import org.apache.commons.collections4.queue.CircularFifoQueue;

/**
 * Demonstração de CircularFifoQueue.
 * Fila com tamanho máximo fixo - remove automaticamente o mais antigo.
 */
public class CircularQueueExample {
    
    /**
     * Demonstra fila circular de tamanho limitado.
     */
    public static void main(String[] args) {
        // Criar fila circular com capacidade 3
        CircularFifoQueue<String> queue = new CircularFifoQueue<>(3);
        
        // Adicionar elementos
        queue.add("primeiro");
        queue.add("segundo");
        queue.add("terceiro");
        
        System.out.println("Fila cheia: " + queue);
        // [primeiro, segundo, terceiro]
        
        System.out.println("Está cheia? " + queue.isAtFullCapacity()); // true
        System.out.println("Capacidade máxima: " + queue.maxSize());   // 3
        
        // Adicionar mais um - remove "primeiro" automaticamente
        queue.add("quarto");
        System.out.println("\nApós adicionar 'quarto': " + queue);
        // [segundo, terceiro, quarto]
        
        // Adicionar mais um - remove "segundo" automaticamente
        queue.add("quinto");
        System.out.println("Após adicionar 'quinto': " + queue);
        // [terceiro, quarto, quinto]
        
        // Peek (sem remover)
        System.out.println("\nPeek: " + queue.peek()); // terceiro
        
        // Poll (remove e retorna)
        String removed = queue.poll();
        System.out.println("Removed: " + removed);      // terceiro
        System.out.println("Fila após poll: " + queue); // [quarto, quinto]
    }
}
```

**Output**:
```
Fila cheia: [primeiro, segundo, terceiro]
Está cheia? true
Capacidade máxima: 3

Após adicionar 'quarto': [segundo, terceiro, quarto]
Após adicionar 'quinto': [terceiro, quarto, quinto]

Peek: terceiro
Removed: terceiro
Fila após poll: [quarto, quinto]
```

---

### Caso de Uso: Log Buffer

```java
package com.example.collections;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Buffer de logs mantendo apenas os N mais recentes.
 */
public class LogBuffer {
    
    private final CircularFifoQueue<LogEntry> buffer;
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Entrada de log.
     */
    public static class LogEntry {
        private final LocalDateTime timestamp;
        private final String level;
        private final String message;
        
        public LogEntry(String level, String message) {
            this.timestamp = LocalDateTime.now();
            this.level = level;
            this.message = message;
        }
        
        @Override
        public String toString() {
            return String.format("[%s] %s: %s",
                timestamp.format(FORMATTER),
                level,
                message
            );
        }
    }
    
    /**
     * Construtor com capacidade máxima.
     *
     * @param capacity número máximo de logs
     */
    public LogBuffer(int capacity) {
        this.buffer = new CircularFifoQueue<>(capacity);
    }
    
    /**
     * Adiciona log de INFO.
     *
     * @param message mensagem
     */
    public void info(String message) {
        buffer.add(new LogEntry("INFO", message));
    }
    
    /**
     * Adiciona log de WARNING.
     *
     * @param message mensagem
     */
    public void warn(String message) {
        buffer.add(new LogEntry("WARN", message));
    }
    
    /**
     * Adiciona log de ERROR.
     *
     * @param message mensagem
     */
    public void error(String message) {
        buffer.add(new LogEntry("ERROR", message));
    }
    
    /**
     * Retorna todos os logs atuais.
     *
     * @return lista de logs
     */
    public List<LogEntry> getAllLogs() {
        return buffer.stream().collect(Collectors.toList());
    }
    
    /**
     * Imprime todos os logs.
     */
    public void printLogs() {
        System.out.println("=== Log Buffer ===");
        buffer.forEach(System.out::println);
    }
    
    /**
     * Exemplo de uso do log buffer.
     */
    public static void main(String[] args) throws InterruptedException {
        // Buffer que mantém apenas últimas 5 entradas
        LogBuffer logger = new LogBuffer(5);
        
        // Adicionar logs
        logger.info("Aplicação iniciada");
        logger.info("Conectando ao banco de dados");
        logger.warn("Pool de conexões em 80%");
        logger.info("Servidor iniciado na porta 8080");
        logger.info("Processando requisição GET /api/users");
        
        logger.printLogs();
        
        // Adicionar mais logs - remove os mais antigos
        System.out.println("\nAdicionando mais logs...\n");
        logger.error("Erro ao processar requisição");
        logger.info("Tentando reconexão");
        
        logger.printLogs();
    }
}
```

---

## 🧪 Testing

### Testes com JUnit 5

```java
package com.example.collections;

import org.apache.commons.collections4.*;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite completa de testes para Apache Commons Collections 4.
 */
class CommonsCollectionsTest {
    
    @Nested
    @DisplayName("Bag Tests")
    class BagTests {
        
        @Test
        @DisplayName("Deve contar ocorrências corretamente")
        void testBagCounting() {
            Bag<String> bag = new HashBag<>();
            
            bag.add("apple", 3);
            bag.add("banana", 2);
            bag.add("apple");
            
            assertEquals(4, bag.getCount("apple"));
            assertEquals(2, bag.getCount("banana"));
            assertEquals(6, bag.size());
        }
        
        @Test
        @DisplayName("Deve retornar elementos únicos")
        void testUniqueSet() {
            Bag<String> bag = new HashBag<>();
            bag.add("apple", 5);
            bag.add("banana", 3);
            
            Set<String> unique = bag.uniqueSet();
            
            assertEquals(2, unique.size());
            assertTrue(unique.contains("apple"));
            assertTrue(unique.contains("banana"));
        }
        
        @Test
        @DisplayName("Deve remover ocorrências específicas")
        void testRemoveOccurrences() {
            Bag<String> bag = new HashBag<>();
            bag.add("item", 5);
            
            bag.remove("item", 2);
            
            assertEquals(3, bag.getCount("item"));
        }
    }
    
    @Nested
    @DisplayName("BidiMap Tests")
    class BidiMapTests {
        
        @Test
        @DisplayName("Deve permitir busca bidirecional")
        void testBidirectionalLookup() {
            BidiMap<String, Integer> map = new DualHashBidiMap<>();
            
            map.put("one", 1);
            map.put("two", 2);
            
            assertEquals(1, map.get("one"));
            assertEquals("one", map.getKey(1));
        }
        
        @Test
        @DisplayName("Deve manter valores únicos")
        void testUniqueValues() {
            BidiMap<String, Integer> map = new DualHashBidiMap<>();
            
            map.put("one", 1);
            map.put("uno", 1); // Sobrescreve "one"
            
            assertFalse(map.containsKey("one"));
            assertTrue(map.containsKey("uno"));
            assertEquals("uno", map.getKey(1));
        }
        
        @Test
        @DisplayName("Deve permitir remoção por valor")
        void testRemoveByValue() {
            BidiMap<String, Integer> map = new DualHashBidiMap<>();
            map.put("one", 1);
            map.put("two", 2);
            
            String removed = map.removeValue(1);
            
            assertEquals("one", removed);
            assertFalse(map.containsKey("one"));
            assertNull(map.getKey(1));
        }
        
        @Test
        @DisplayName("inverseBidiMap deve ser view")
        void testInverseMapIsView() {
            BidiMap<String, Integer> map = new DualHashBidiMap<>();
            map.put("one", 1);
            
            BidiMap<Integer, String> inverse = map.inverseBidiMap();
            inverse.put(2, "two");
            
            assertTrue(map.containsKey("two"));
            assertEquals(2, map.size());
        }
    }
    
    @Nested
    @DisplayName("MultiValuedMap Tests")
    class MultiValuedMapTests {
        
        @Test
        @DisplayName("Deve armazenar múltiplos valores por chave")
        void testMultipleValues() {
            MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();
            
            map.put("tags", "java");
            map.put("tags", "kotlin");
            map.put("tags", "scala");
            
            Collection<String> tags = map.get("tags");
            
            assertEquals(3, tags.size());
            assertTrue(tags.contains("java"));
            assertTrue(tags.contains("kotlin"));
            assertTrue(tags.contains("scala"));
        }
        
        @Test
        @DisplayName("Deve remover valor específico")
        void testRemoveMapping() {
            MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();
            map.put("key", "value1");
            map.put("key", "value2");
            
            boolean removed = map.removeMapping("key", "value1");
            
            assertTrue(removed);
            assertEquals(1, map.get("key").size());
            assertTrue(map.get("key").contains("value2"));
        }
        
        @Test
        @DisplayName("size() deve retornar total de valores")
        void testSize() {
            MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();
            map.put("key1", "a");
            map.put("key1", "b");
            map.put("key2", "c");
            
            assertEquals(3, map.size()); // Total de valores
            assertEquals(2, map.keySet().size()); // Total de chaves
        }
    }
    
    @Nested
    @DisplayName("Trie Tests")
    class TrieTests {
        
        @Test
        @DisplayName("Deve buscar por prefixo eficientemente")
        void testPrefixSearch() {
            Trie<String, String> trie = new PatriciaTrie<>();
            trie.put("car", "automóvel");
            trie.put("card", "cartão");
            trie.put("care", "cuidado");
            trie.put("banana", "banana");
            
            SortedMap<String, String> matches = trie.prefixMap("car");
            
            assertEquals(3, matches.size());
            assertTrue(matches.containsKey("car"));
            assertTrue(matches.containsKey("card"));
            assertTrue(matches.containsKey("care"));
            assertFalse(matches.containsKey("banana"));
        }
        
        @Test
        @DisplayName("Deve funcionar como SortedMap")
        void testSortedMapBehavior() {
            Trie<String, Integer> trie = new PatriciaTrie<>();
            trie.put("c", 3);
            trie.put("a", 1);
            trie.put("b", 2);
            
            assertEquals("a", trie.firstKey());
            assertEquals("c", trie.lastKey());
        }
    }
    
    @Nested
    @DisplayName("CollectionUtils Tests")
    class CollectionUtilsTests {
        
        @Test
        @DisplayName("Deve calcular união corretamente")
        void testUnion() {
            List<String> list1 = Arrays.asList("a", "b", "c");
            List<String> list2 = Arrays.asList("c", "d", "e");
            
            Collection<String> union = CollectionUtils.union(list1, list2);
            
            assertEquals(5, union.size());
            assertTrue(union.containsAll(Arrays.asList("a", "b", "c", "d", "e")));
        }
        
        @Test
        @DisplayName("Deve calcular interseção corretamente")
        void testIntersection() {
            List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
            List<Integer> list2 = Arrays.asList(3, 4, 5, 6);
            
            Collection<Integer> intersection = CollectionUtils.intersection(list1, list2);
            
            assertEquals(2, intersection.size());
            assertTrue(intersection.containsAll(Arrays.asList(3, 4)));
        }
        
        @Test
        @DisplayName("isEmpty deve ser null-safe")
        void testIsEmptyNullSafe() {
            List<String> nullList = null;
            List<String> emptyList = new ArrayList<>();
            List<String> nonEmpty = Arrays.asList("a");
            
            assertTrue(CollectionUtils.isEmpty(nullList));
            assertTrue(CollectionUtils.isEmpty(emptyList));
            assertFalse(CollectionUtils.isEmpty(nonEmpty));
        }
        
        @Test
        @DisplayName("select deve filtrar corretamente")
        void testSelect() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
            
            Collection<Integer> evens = CollectionUtils.select(
                numbers,
                n -> n % 2 == 0
            );
            
            assertEquals(3, evens.size());
            assertTrue(evens.containsAll(Arrays.asList(2, 4, 6)));
        }
        
        @Test
        @DisplayName("collect deve transformar elementos")
        void testCollect() {
            List<String> words = Arrays.asList("hello", "world");
            
            Collection<Integer> lengths = CollectionUtils.collect(
                words,
                String::length
            );
            
            assertEquals(Arrays.asList(5, 5), new ArrayList<>(lengths));
        }
    }
    
    @Nested
    @DisplayName("MapUtils Tests")
    class MapUtilsTests {
        
        @Test
        @DisplayName("getString deve retornar default se não existir")
        void testGetStringWithDefault() {
            Map<String, Object> map = new HashMap<>();
            map.put("name", "John");
            
            String name = MapUtils.getString(map, "name", "Unknown");
            String city = MapUtils.getString(map, "city", "Unknown");
            
            assertEquals("John", name);
            assertEquals("Unknown", city);
        }
        
        @Test
        @DisplayName("isEmpty deve ser null-safe")
        void testIsEmptyNullSafe() {
            Map<String, String> nullMap = null;
            Map<String, String> emptyMap = new HashMap<>();
            Map<String, String> nonEmpty = Map.of("key", "value");
            
            assertTrue(MapUtils.isEmpty(nullMap));
            assertTrue(MapUtils.isEmpty(emptyMap));
            assertFalse(MapUtils.isEmpty(nonEmpty));
        }
        
        @Test
        @DisplayName("getInteger deve fazer conversão de tipo")
        void testGetInteger() {
            Map<String, Object> map = new HashMap<>();
            map.put("age", 30);
            map.put("count", "42"); // String
            
            Integer age = MapUtils.getInteger(map, "age", 0);
            Integer count = MapUtils.getInteger(map, "count", 0);
            Integer missing = MapUtils.getInteger(map, "missing", 99);
            
            assertEquals(30, age);
            assertEquals(42, count);
            assertEquals(99, missing);
        }
    }
    
    @Nested
    @DisplayName("ListUtils Tests")
    class ListUtilsTests {
        
        @Test
        @DisplayName("partition deve dividir lista corretamente")
        void testPartition() {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
            
            List<List<Integer>> partitions = ListUtils.partition(numbers, 3);
            
            assertEquals(3, partitions.size());
            assertEquals(Arrays.asList(1, 2, 3), partitions.get(0));
            assertEquals(Arrays.asList(4, 5, 6), partitions.get(1));
            assertEquals(Arrays.asList(7), partitions.get(2));
        }
        
        @Test
        @DisplayName("lazyList deve criar elementos sob demanda")
        void testLazyList() {
            List<String> lazy = ListUtils.lazyList(
                new ArrayList<>(),
                () -> "default"
            );
            
            String value = lazy.get(5);
            
            assertEquals("default", value);
            assertEquals(6, lazy.size());
        }
    }
}
```

---

## ✅ Best Practices

### 1. Escolha a Estrutura de Dados Correta

```java
// ✅ Para contar ocorrências
Bag<String> bag = new HashBag<>(); // O(1) para getCount()

// ✅ Para busca bidirecional
BidiMap<String, Long> userIds = new DualHashBidiMap<>(); // O(1) ambas direções

// ✅ Para múltiplos valores por chave
MultiValuedMap<String, String> tags = new HashSetValuedHashMap<>(); // Sem duplicatas

// ✅ Para autocomplete/busca por prefixos
Trie<String, String> dictionary = new PatriciaTrie<>(); // O(k) onde k = tamanho prefixo

// ✅ Para cache temporário
Map<String, Data> cache = new PassiveExpiringMap<>(TimeUnit.MINUTES.toMillis(5));

// ✅ Para buffer circular
Queue<LogEntry> logBuffer = new CircularFifoQueue<>(100); // Últimos 100
```

---

### 2. Use Métodos Null-Safe

```java
// ❌ Sem null-safe
Map<String, String> map = getMapFromSomewhere(); // Pode ser null
if (map != null && !map.isEmpty()) {
    // ...
}

// ✅ Com MapUtils (null-safe)
if (MapUtils.isNotEmpty(map)) {
    // ...
}

// ❌ Sem default
String value = map != null ? map.get("key") : "default";

// ✅ Com default
String value = MapUtils.getString(map, "key", "default");
```

---

### 3. Prefira Operações Funcionais

```java
import org.apache.commons.collections4.CollectionUtils;

// ❌ Imperativo
List<String> filtered = new ArrayList<>();
for (String name : names) {
    if (name.length() > 4) {
        filtered.add(name.toUpperCase());
    }
}

// ✅ Funcional com CollectionUtils
Collection<String> result = CollectionUtils.collect(
    CollectionUtils.select(names, name -> name.length() > 4),
    String::toUpperCase
);

// ✅ Ou com Java Streams (geralmente melhor para Java 8+)
List<String> result = names.stream()
    .filter(name -> name.length() > 4)
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

---

### 4. Considere Thread-Safety

```java
import org.apache.commons.collections4.bag.SynchronizedBag;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

// ❌ HashBag não é thread-safe
Bag<String> unsafeBag = new HashBag<>();

// ✅ SynchronizedBag é thread-safe
Bag<String> safeBag = SynchronizedBag.synchronizedBag(new HashBag<>());

// ✅ Para BidiMap thread-safe
BidiMap<String, Integer> safeBidi = 
    Collections.synchronizedMap(new DualHashBidiMap<>());
```

---

### 5. Use Predicados Reutilizáveis

```java
import org.apache.commons.collections4.Predicate;

/**
 * Predicados reutilizáveis para validação de dados.
 */
public class Predicates {
    
    // Predicado para email válido
    public static final Predicate<String> VALID_EMAIL = email ->
        email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    // Predicado para string não vazia
    public static final Predicate<String> NOT_BLANK = s ->
        s != null && !s.trim().isEmpty();
    
    // Predicado para número positivo
    public static final Predicate<Integer> POSITIVE = n ->
        n != null && n > 0;
    
    // Combinar predicados
    public static <T> Predicate<T> and(Predicate<T> p1, Predicate<T> p2) {
        return t -> p1.evaluate(t) && p2.evaluate(t);
    }
    
    public static <T> Predicate<T> or(Predicate<T> p1, Predicate<T> p2) {
        return t -> p1.evaluate(t) || p2.evaluate(t);
    }
}

// Uso
List<String> emails = Arrays.asList("test@example.com", "", "invalid", "valid@test.com");

Collection<String> validEmails = CollectionUtils.select(emails, Predicates.VALID_EMAIL);
```

---

### 6. Documente Complexidade

```java
/**
 * Gerenciador de índices invertidos para busca eficiente.
 * Usa Trie para busca por prefixos com complexidade O(k).
 */
public class SearchIndex {
    
    private final Trie<String, Set<String>> index = new PatriciaTrie<>();
    
    /**
     * Indexa um documento.
     * 
     * Complexidade: O(m * k) onde:
     * - m = número de palavras no documento
     * - k = tamanho médio das palavras
     *
     * @param docId ID do documento
     * @param content conteúdo a indexar
     */
    public void indexDocument(String docId, String content) {
        // Implementação
    }
    
    /**
     * Busca documentos por prefixo.
     * 
     * Complexidade: O(k + n) onde:
     * - k = tamanho do prefixo
     * - n = número de documentos encontrados
     *
     * @param prefix prefixo a buscar
     * @return conjunto de IDs de documentos
     */
    public Set<String> searchByPrefix(String prefix) {
        // Implementação
        return null;
    }
}
```

---

### 7. Prefira Imutabilidade Quando Possível

```java
import org.apache.commons.collections4.bag.UnmodifiableBag;
import org.apache.commons.collections4.bidimap.UnmodifiableBidiMap;

public class ImmutableCollections {
    
    private final Bag<String> items;
    private final BidiMap<String, Integer> mapping;
    
    public ImmutableCollections() {
        Bag<String> mutableBag = new HashBag<>();
        mutableBag.add("item1");
        this.items = UnmodifiableBag.unmodifiableBag(mutableBag);
        
        BidiMap<String, Integer> mutableMap = new DualHashBidiMap<>();
        mutableMap.put("one", 1);
        this.mapping = UnmodifiableBidiMap.unmodifiableBidiMap(mutableMap);
    }
    
    /**
     * Retorna bag imutável.
     *
     * @return bag read-only
     */
    public Bag<String> getItems() {
        return items; // Já é imutável, seguro retornar
    }
}
```

---

## 📊 Referência Rápida

### Quando Usar Cada Estrutura

| Use Case | Estrutura | Complexidade |
|----------|-----------|--------------|
| Contar ocorrências | `HashBag` | O(1) |
| Busca bidirecional | `DualHashBidiMap` | O(1) |
| Múltiplos valores/chave | `ArrayListValuedHashMap` | O(1) |
| Autocomplete | `PatriciaTrie` | O(k) |
| Cache temporário | `PassiveExpiringMap` | O(1) + expiração |
| Buffer circular | `CircularFifoQueue` | O(1) |
| Bag ordenado | `TreeBag` | O(log n) |
| BidiMap ordenado | `TreeBidiMap` | O(log n) |

---

### Hierarquia de Interfaces

```
Collection
├── Bag (permite duplicatas com contagem)
│   ├── HashBag
│   ├── TreeBag
│   └── SynchronizedBag
│
Map
├── BidiMap (busca bidirecional)
│   ├── DualHashBidiMap
│   ├── TreeBidiMap
│   └── UnmodifiableBidiMap
│
├── MultiValuedMap (múltiplos valores)
│   ├── ArrayListValuedHashMap
│   ├── HashSetValuedHashMap
│   └── UnmodifiableMultiValuedMap
│
└── Trie (busca por prefixos)
    └── PatriciaTrie

Queue
└── CircularFifoQueue (fila circular limitada)
```

---

## 📚 Recursos Adicionais

### Documentação Oficial
- [Apache Commons Collections Home](https://commons.apache.org/proper/commons-collections/)
- [API JavaDoc 4.4](https://commons.apache.org/proper/commons-collections/apidocs/)
- [User Guide](https://commons.apache.org/proper/commons-collections/userguide.html)

### Repositório
- [GitHub](https://github.com/apache/commons-collections)
- [Issues](https://issues.apache.org/jira/projects/COLLECTIONS)

### Maven Dependency
```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-collections4</artifactId>
    <version>4.4</version>
</dependency>
```

---

## 🎯 Resumo

### Principais Pontos

1. **Commons Collections 4** estende o JCF com estruturas especializadas
2. **Bag** para contagem eficiente de ocorrências
3. **BidiMap** para busca bidirecional O(1)
4. **MultiValuedMap** para múltiplos valores por chave
5. **Trie** para busca eficiente por prefixos (autocomplete)
6. **CollectionUtils** fornece operações funcionais e de conjunto
7. **PassiveExpiringMap** para cache com expiração automática
8. **CircularFifoQueue** para buffers circulares

### Quando Usar

✅ **Use Commons Collections 4 quando precisar de**:
- Contagem de elementos (Bag)
- Busca reversa em Maps (BidiMap)
- Múltiplos valores por chave (MultiValuedMap)
- Autocomplete/busca por prefixos (Trie)
- Cache com expiração (PassiveExpiringMap)
- Operações de conjunto eficientes
- Programação funcional (Predicates, Transformers)

❌ **Não use quando**:
- Java Collections Framework básico já atende
- Java Streams resolve mais elegantemente
- Performance extrema é crítica (use estruturas especializadas)

---

**Voltar para**: [📁 Apache Commons](../README.md) | [📁 Libraries](../../README.md) | [📁 Repositório Principal](../../../README.md)
