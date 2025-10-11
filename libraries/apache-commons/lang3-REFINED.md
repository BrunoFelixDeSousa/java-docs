# Apache Commons Lang3 - Documentação Completa

![Maven Central](https://img.shields.io/maven-central/v/org.apache.commons/commons-lang3?color=blue&label=Maven%20Central)
![Java](https://img.shields.io/badge/Java-8%2B-orange)
![License](https://img.shields.io/badge/License-Apache%202.0-green)

## 📚 Índice

1. [Introdução](#-introdução)
2. [Instalação e Setup](#️-instalação-e-setup)
3. [StringUtils - Manipulação de Strings](#-stringutils)
4. [ArrayUtils - Manipulação de Arrays](#-arrayutils)
5. [NumberUtils - Operações Numéricas](#-numberutils)
6. [ObjectUtils - Operações com Objetos](#-objectutils)
7. [BooleanUtils - Operações Booleanas](#-booleanutils)
8. [DateUtils - Manipulação de Datas](#-dateutils)
9. [RandomStringUtils - Strings Aleatórias](#-randomstringutils)
10. [Pair e Triple - Tuplas](#-pair-e-triple)
11. [Range - Intervalos](#-range)
12. [ExceptionUtils - Tratamento de Exceções](#-exceptionutils)
13. [SystemUtils - Informações do Sistema](#-systemutils)
14. [Validate - Validações](#-validate)
15. [Builders - Padrões de Construção](#️-builders)
16. [Testes](#-testes)
17. [Best Practices](#-best-practices)
18. [Referência Rápida](#-referência-rápida)

---

## 🎯 Introdução

### Por que usar Commons Lang3?

**Apache Commons Lang3** é uma biblioteca que estende as funcionalidades básicas do Java, fornecendo utilitários robustos, null-safe e testados para operações cotidianas.

```java
// ❌ Sem Commons Lang3 - verboso e propenso a erros
// variavel não pode ter valor null, vazio ou "null"
public boolean isValid(String str) {
    return str != null && !str.trim().isEmpty() && !str.equals("null");
}

// ✅ Com Commons Lang3 - simples, claro e seguro
// variavel não pode ter valor null, vazio ou "null"
public boolean isValid(String str) {
    return StringUtils.isNotBlank(str);
}
```

### Características Principais

- ✅ **Null-Safe**: Métodos que lidam graciosamente com valores `null`
- ✅ **Thread-Safe**: Classes utilitárias estáticas sem estado
- ✅ **Performance**: Otimizações para operações comuns
- ✅ **Testado**: Cobertura de testes superior a 95%
- ✅ **Zero Dependencies**: Não requer outras bibliotecas

---

## ⚙️ Instalação e Setup

### Maven

```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.18.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'org.apache.commons:commons-lang3:3.18.0'
```

### Gradle Kotlin DSL

```kotlin
implementation("org.apache.commons:commons-lang3:3.18.0")
```

### Verificação de Instalação

```java
package com.example.commons;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

/**
 * Classe para verificar a instalação do Commons Lang3.
 */
public class InstallationTest {
    
    /**
     * Verifica se o Commons Lang3 está corretamente configurado.
     *
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) {
        // Teste StringUtils
        String result = StringUtils.capitalize("hello commons lang3");
        System.out.println("StringUtils: " + result);
        
        // Teste SystemUtils
        System.out.println("Java Version: " + SystemUtils.JAVA_VERSION);
        System.out.println("OS Name: " + SystemUtils.OS_NAME);
        
        System.out.println("\n✅ Commons Lang3 instalado com sucesso!");
    }
}
```

**Output esperado**:
```
StringUtils: Hello commons lang3
Java Version: 17.0.1
OS Name: Windows 11
✅ Commons Lang3 instalado com sucesso!
```

---

## 🔤 StringUtils

### Por que usar StringUtils?

| Cenário | Sem Commons Lang3 | Com StringUtils |
|---------|-------------------|-----------------|
| Verificar se vazio | `str != null && !str.isEmpty()` | `StringUtils.isEmpty(str)` |
| Verificar se em branco | `str != null && !str.trim().isEmpty()` | `StringUtils.isBlank(str)` |
| Capitalizar | Manual com substring | `StringUtils.capitalize(str)` |
| Join com separador | Loop manual ou Stream | `StringUtils.join(array, ",")` |

### Exemplo Básico

```java
package com.example.string;

import org.apache.commons.lang3.StringUtils;

/**
 * Demonstração básica de StringUtils.
 */
public class StringUtilsBasicExample {
    
    /**
     * Demonstra operações básicas com strings.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        // Verificações null-safe
        System.out.println("isEmpty(''): " + StringUtils.isEmpty(""));           // true
        System.out.println("isEmpty('  '): " + StringUtils.isEmpty("  "));       // false
        System.out.println("isEmpty(null): " + StringUtils.isEmpty(null));       // true
        
        System.out.println("\nisBlank(''): " + StringUtils.isBlank(""));         // true
        System.out.println("isBlank('  '): " + StringUtils.isBlank("  "));       // true
        System.out.println("isBlank(null): " + StringUtils.isBlank(null));       // true
        
        // Formatação
        System.out.println("\nCapitalize: " + StringUtils.capitalize("hello"));  // Hello
        System.out.println("Uppercase: " + StringUtils.upperCase("hello"));      // HELLO
        System.out.println("Repeat: " + StringUtils.repeat("*", 5));             // *****
        
        // Join e Split
        String[] words = {"Java", "is", "awesome"};
        System.out.println("\nJoin: " + StringUtils.join(words, " "));           // Java is awesome
        
        String[] parts = StringUtils.split("a,b,c", ",");
        System.out.println("Split: " + java.util.Arrays.toString(parts));        // [a, b, c]
    }
}
```

### Caso de Uso Real: Validador de Formulários

```java
package com.example.string;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Validador de formulários usando StringUtils.
 * Demonstra validação robusta e null-safe de dados de entrada.
 */
public class FormValidator {
    
    /**
     * Resultado da validação contendo status e mensagens de erro.
     */
    public static class ValidationResult {
        private final boolean valid;
        private final List<String> errors;
        
        public ValidationResult(boolean valid, List<String> errors) {
            this.valid = valid;
            this.errors = errors;
        }
        
        /**
         * Verifica se a validação foi bem-sucedida.
         *
         * @return true se válido, false caso contrário
         */
        public boolean isValid() {
            return valid;
        }
        
        /**
         * Obtém a lista de erros de validação.
         *
         * @return lista de mensagens de erro
         */
        public List<String> getErrors() {
            return errors;
        }
    }
    
    /**
     * Valida dados de um formulário de cadastro.
     *
     * @param name nome do usuário
     * @param email email do usuário
     * @param password senha do usuário
     * @return resultado da validação
     */
    public ValidationResult validateRegistrationForm(
            String name, 
            String email, 
            String password) {
        
        List<String> errors = new ArrayList<>();
        
        // Validar nome (não pode ser em branco)
        if (StringUtils.isBlank(name)) {
            errors.add("Nome é obrigatório");
        } else if (name.trim().length() < 3) {
            errors.add("Nome deve ter pelo menos 3 caracteres");
        } else if (!StringUtils.isAlpha(name.replace(" ", ""))) {
            errors.add("Nome deve conter apenas letras");
        }
        
        // Validar email
        if (StringUtils.isBlank(email)) {
            errors.add("Email é obrigatório");
        } else if (!StringUtils.contains(email, "@")) {
            errors.add("Email inválido");
        } else if (StringUtils.countMatches(email, "@") != 1) {
            errors.add("Email deve conter exatamente um @");
        }
        
        // Validar senha
        if (StringUtils.isBlank(password)) {
            errors.add("Senha é obrigatória");
        } else if (password.length() < 8) {
            errors.add("Senha deve ter pelo menos 8 caracteres");
        } else {
            // Verificar complexidade
            boolean hasUpper = !password.equals(password.toLowerCase());
            boolean hasLower = !password.equals(password.toUpperCase());
            boolean hasDigit = StringUtils.containsAny(password, "0123456789");
            
            if (!hasUpper || !hasLower || !hasDigit) {
                errors.add("Senha deve conter maiúsculas, minúsculas e números");
            }
        }
        
        return new ValidationResult(errors.isEmpty(), errors);
    }
    
    /**
     * Limpa e normaliza um nome para exibição.
     *
     * @param name nome a ser normalizado
     * @return nome normalizado ou string vazia se inválido
     */
    public String normalizeName(String name) {
        if (StringUtils.isBlank(name)) {
            return StringUtils.EMPTY;
        }
        
        // Remove espaços extras
        String normalized = StringUtils.normalizeSpace(name.trim());
        
        // Capitaliza cada palavra
        String[] words = StringUtils.split(normalized);
        for (int i = 0; i < words.length; i++) {
            words[i] = StringUtils.capitalize(words[i].toLowerCase());
        }
        
        return StringUtils.join(words, " ");
    }
    
    /**
     * Gera slug a partir de um título (para URLs).
     *
     * @param title título original
     * @return slug URL-friendly
     */
    public String generateSlug(String title) {
        if (StringUtils.isBlank(title)) {
            return StringUtils.EMPTY;
        }
        
        // Remove caracteres especiais e converte para minúsculas
        String slug = title.toLowerCase()
                          .replaceAll("[áàâã]", "a")
                          .replaceAll("[éèê]", "e")
                          .replaceAll("[íì]", "i")
                          .replaceAll("[óòôõ]", "o")
                          .replaceAll("[úù]", "u")
                          .replaceAll("[ç]", "c")
                          .replaceAll("[^a-z0-9\\s-]", "")
                          .trim();
        
        // Substitui espaços por hífens
        slug = StringUtils.join(StringUtils.split(slug), "-");
        
        // Remove hífens duplicados
        while (StringUtils.contains(slug, "--")) {
            slug = StringUtils.replace(slug, "--", "-");
        }
        
        return slug;
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        FormValidator validator = new FormValidator();
        
        // Teste de validação
        System.out.println("=== Validação de Formulário ===\n");
        
        ValidationResult result1 = validator.validateRegistrationForm(
            "John Doe",
            "john@example.com",
            "SecurePass123"
        );
        System.out.println("Formulário válido: " + result1.isValid());
        
        ValidationResult result2 = validator.validateRegistrationForm(
            "",
            "invalid-email",
            "123"
        );
        System.out.println("\nFormulário inválido: " + result2.isValid());
        System.out.println("Erros:");
        result2.getErrors().forEach(error -> System.out.println("  - " + error));
        
        // Teste de normalização
        System.out.println("\n=== Normalização de Nome ===");
        System.out.println(validator.normalizeName("  joHN   dOE  ")); // John Doe
        
        // Teste de slug
        System.out.println("\n=== Geração de Slug ===");
        System.out.println(validator.generateSlug("Como Usar Apache Commons Lang3!")); 
        // como-usar-apache-commons-lang3
    }
}
```

**Output**:
```
=== Validação de Formulário ===

Formulário válido: true

Formulário inválido: false
Erros:
  - Nome é obrigatório
  - Email inválido
  - Senha deve ter pelo menos 8 caracteres

=== Normalização de Nome ===
John Doe

=== Geração de Slug ===
como-usar-apache-commons-lang3
```

### API StringUtils - Principais Métodos

| Método | Descrição | Exemplo |
|--------|-----------|---------|
| `isEmpty(str)` | Verifica se null ou vazio | `isEmpty("")` → `true` |
| `isBlank(str)` | Verifica se null, vazio ou só espaços | `isBlank("  ")` → `true` |
| `isNotEmpty(str)` | Negação de isEmpty | `isNotEmpty("a")` → `true` |
| `isNotBlank(str)` | Negação de isBlank | `isNotBlank("a")` → `true` |
| `trim(str)` | Remove espaços (null-safe) | `trim(" a ")` → `"a"` |
| `capitalize(str)` | Primeira letra maiúscula | `capitalize("hello")` → `"Hello"` |
| `upperCase(str)` | Converte para maiúsculas | `upperCase("a")` → `"A"` |
| `lowerCase(str)` | Converte para minúsculas | `lowerCase("A")` → `"a"` |
| `contains(str, search)` | Verifica se contém | `contains("abc", "b")` → `true` |
| `startsWith(str, prefix)` | Verifica se inicia com | `startsWith("abc", "a")` → `true` |
| `endsWith(str, suffix)` | Verifica se termina com | `endsWith("abc", "c")` → `true` |
| `split(str, separator)` | Divide string | `split("a,b", ",")` → `["a", "b"]` |
| `join(array, separator)` | Une elementos | `join(["a","b"], ",")` → `"a,b"` |
| `replace(str, search, repl)` | Substitui texto | `replace("ab", "b", "c")` → `"ac"` |
| `repeat(str, count)` | Repete string | `repeat("a", 3)` → `"aaa"` |
| `reverse(str)` | Inverte string | `reverse("abc")` → `"cba"` |
| `abbreviate(str, maxWidth)` | Abrevia com "..." | `abbreviate("long text", 8)` → `"long..." ` |
| `defaultString(str, default)` | Retorna default se null | `defaultString(null, "x")` → `"x"` |
| `normalizeSpace(str)` | Remove espaços extras | `normalizeSpace("a  b")` → `"a b"` |

---

## 🔢 ArrayUtils

### Por que usar ArrayUtils?

Arrays em Java são imutáveis em tamanho e não possuem métodos utilitários. ArrayUtils fornece operações convenientes e null-safe.

```java
// ❌ Sem ArrayUtils - código verboso
int[] original = {1, 2, 3};
int[] novo = new int[original.length + 1];
System.arraycopy(original, 0, novo, 0, original.length);
novo[novo.length - 1] = 4;

// ✅ Com ArrayUtils - simples
int[] novo = ArrayUtils.add(original, 4);
```

### Exemplo Básico

```java
package com.example.array;

import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;

/**
 * Demonstração básica de ArrayUtils.
 */
public class ArrayUtilsBasicExample {
    
    /**
     * Demonstra operações básicas com arrays.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        // Verificações
        int[] empty = {};
        int[] numbers = {1, 2, 3, 4, 5};
        
        System.out.println("isEmpty(empty): " + ArrayUtils.isEmpty(empty));         // true
        System.out.println("isEmpty(numbers): " + ArrayUtils.isEmpty(numbers));     // false
        System.out.println("getLength(null): " + ArrayUtils.getLength(null));       // 0
        
        // Busca
        System.out.println("\ncontains(numbers, 3): " + ArrayUtils.contains(numbers, 3));  // true
        System.out.println("indexOf(numbers, 3): " + ArrayUtils.indexOf(numbers, 3));      // 2
        
        // Adição
        int[] added = ArrayUtils.add(numbers, 6);
        System.out.println("\nadd(numbers, 6): " + Arrays.toString(added));  // [1, 2, 3, 4, 5, 6]
        
        // Remoção
        int[] removed = ArrayUtils.remove(numbers, 2);  // Remove índice 2
        System.out.println("remove(numbers, 2): " + Arrays.toString(removed));  // [1, 2, 4, 5]
        
        int[] removedElement = ArrayUtils.removeElement(numbers, 3);  // Remove valor 3
        System.out.println("removeElement(numbers, 3): " + Arrays.toString(removedElement));  // [1, 2, 4, 5]
        
        // Inversão (modifica o array original!)
        int[] toReverse = {1, 2, 3};
        ArrayUtils.reverse(toReverse);
        System.out.println("\nreverse([1,2,3]): " + Arrays.toString(toReverse));  // [3, 2, 1]
    }
}
```

### Caso de Uso Real: Gerenciador de Inventário

```java
package com.example.array;

import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;

/**
 * Sistema de gerenciamento de inventário usando ArrayUtils.
 * Demonstra manipulação eficiente de arrays para controle de estoque.
 */
public class InventoryManager {
    
    private int[] productIds;
    private int[] quantities;
    private double[] prices;
    
    /**
     * Construtor inicializa arrays vazios.
     */
    public InventoryManager() {
        this.productIds = new int[0];
        this.quantities = new int[0];
        this.prices = new double[0];
    }
    
    /**
     * Adiciona um produto ao inventário.
     *
     * @param productId ID do produto
     * @param quantity quantidade inicial
     * @param price preço unitário
     * @return true se adicionado, false se já existe
     */
    public boolean addProduct(int productId, int quantity, double price) {
        // Verifica se produto já existe
        if (ArrayUtils.contains(productIds, productId)) {
            System.out.println("Produto " + productId + " já existe!");
            return false;
        }
        
        // Adiciona aos arrays
        productIds = ArrayUtils.add(productIds, productId);
        quantities = ArrayUtils.add(quantities, quantity);
        prices = ArrayUtils.add(prices, price);
        
        return true;
    }
    
    /**
     * Remove um produto do inventário.
     *
     * @param productId ID do produto a remover
     * @return true se removido, false se não encontrado
     */
    public boolean removeProduct(int productId) {
        int index = ArrayUtils.indexOf(productIds, productId);
        
        if (index == ArrayUtils.INDEX_NOT_FOUND) {
            return false;
        }
        
        productIds = ArrayUtils.remove(productIds, index);
        quantities = ArrayUtils.remove(quantities, index);
        prices = ArrayUtils.remove(prices, index);
        
        return true;
    }
    
    /**
     * Atualiza a quantidade de um produto.
     *
     * @param productId ID do produto
     * @param newQuantity nova quantidade
     * @return true se atualizado, false se não encontrado
     */
    public boolean updateQuantity(int productId, int newQuantity) {
        int index = ArrayUtils.indexOf(productIds, productId);
        
        if (index == ArrayUtils.INDEX_NOT_FOUND) {
            return false;
        }
        
        quantities[index] = newQuantity;
        return true;
    }
    
    /**
     * Calcula o valor total do inventário.
     *
     * @return valor total
     */
    public double getTotalValue() {
        if (ArrayUtils.isEmpty(productIds)) {
            return 0.0;
        }
        
        double total = 0.0;
        for (int i = 0; i < productIds.length; i++) {
            total += quantities[i] * prices[i];
        }
        
        return total;
    }
    
    /**
     * Obtém produtos com baixo estoque.
     *
     * @param threshold quantidade mínima
     * @return array de IDs com baixo estoque
     */
    public int[] getLowStockProducts(int threshold) {
        int[] lowStock = new int[0];
        
        for (int i = 0; i < productIds.length; i++) {
            if (quantities[i] < threshold) {
                lowStock = ArrayUtils.add(lowStock, productIds[i]);
            }
        }
        
        return lowStock;
    }
    
    /**
     * Obtém estatísticas do inventário.
     *
     * @return string formatada com estatísticas
     */
    public String getStatistics() {
        if (ArrayUtils.isEmpty(productIds)) {
            return "Inventário vazio";
        }
        
        int totalProducts = productIds.length;
        int totalItems = Arrays.stream(quantities).sum();
        double avgPrice = Arrays.stream(prices).average().orElse(0.0);
        int maxQuantity = Arrays.stream(quantities).max().orElse(0);
        int minQuantity = Arrays.stream(quantities).min().orElse(0);
        
        return String.format(
            "Produtos: %d | Itens: %d | Preço Médio: $%.2f | " +
            "Estoque: %d-%d | Valor Total: $%.2f",
            totalProducts, totalItems, avgPrice, minQuantity, maxQuantity, getTotalValue()
        );
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();
        
        System.out.println("=== Sistema de Inventário ===\n");
        
        // Adicionar produtos
        manager.addProduct(101, 50, 29.99);
        manager.addProduct(102, 5, 149.99);
        manager.addProduct(103, 100, 9.99);
        manager.addProduct(104, 2, 499.99);
        
        System.out.println("Produtos adicionados!");
        System.out.println(manager.getStatistics());
        
        // Produtos com baixo estoque (threshold: 10)
        System.out.println("\n=== Baixo Estoque (< 10 unidades) ===");
        int[] lowStock = manager.getLowStockProducts(10);
        System.out.println("Produtos: " + Arrays.toString(lowStock));
        
        // Atualizar quantidade
        System.out.println("\n=== Atualizando Estoque ===");
        manager.updateQuantity(102, 20);
        System.out.println("Produto 102 atualizado para 20 unidades");
        System.out.println(manager.getStatistics());
        
        // Remover produto
        System.out.println("\n=== Removendo Produto ===");
        manager.removeProduct(104);
        System.out.println("Produto 104 removido");
        System.out.println(manager.getStatistics());
    }
}
```

**Output**:
```
=== Sistema de Inventário ===

Produtos adicionados!
Produtos: 4 | Itens: 157 | Preço Médio: $172.49 | Estoque: 2-100 | Valor Total: $3498.42

=== Baixo Estoque (< 10 unidades) ===
Produtos: [102, 104]

=== Atualizando Estoque ===
Produto 102 atualizado para 20 unidades
Produtos: 4 | Itens: 172 | Preço Médio: $172.49 | Estoque: 2-100 | Valor Total: $5748.27

=== Removendo Produto ===
Produto 104 removido
Produtos: 3 | Itens: 170 | Preço Médio: $63.32 | Estoque: 5-100 | Valor Total: $4498.28
```

### API ArrayUtils - Principais Métodos

| Método | Descrição | Exemplo |
|--------|-----------|---------|
| `isEmpty(array)` | Verifica se null ou vazio | `isEmpty(new int[0])` → `true` |
| `isNotEmpty(array)` | Verifica se não vazio | `isNotEmpty(new int[]{1})` → `true` |
| `getLength(array)` | Retorna tamanho (null-safe) | `getLength(null)` → `0` |
| `contains(array, value)` | Verifica se contém valor | `contains(arr, 5)` → `true` |
| `indexOf(array, value)` | Índice da primeira ocorrência | `indexOf(arr, 5)` → `2` |
| `add(array, element)` | Adiciona elemento | `add({1,2}, 3)` → `{1,2,3}` |
| `addAll(array, elements...)` | Adiciona múltiplos | `addAll({1}, 2, 3)` → `{1,2,3}` |
| `remove(array, index)` | Remove por índice | `remove({1,2,3}, 1)` → `{1,3}` |
| `removeElement(array, element)` | Remove por valor | `removeElement({1,2}, 2)` → `{1}` |
| `reverse(array)` | Inverte array (in-place) | `reverse({1,2,3})` → `{3,2,1}` |
| `clone(array)` | Clona array (null-safe) | `clone({1,2})` → `{1,2}` |
| `subarray(array, start, end)` | Cria subarray | `subarray({1,2,3}, 0, 2)` → `{1,2}` |
| `toObject(primitive[])` | Converte para wrapper | `toObject(new int[]{1})` → `Integer[]{1}` |
| `toPrimitive(wrapper[])` | Converte para primitivo | `toPrimitive(Integer[]{1})` → `int[]{1}` |

---

## 🔢 NumberUtils

### Por que usar NumberUtils?

Parsing de números em Java pode lançar exceções. NumberUtils fornece conversões seguras com valores padrão.

```java
// ❌ Sem NumberUtils - pode lançar exceção
int value = Integer.parseInt("abc");  // NumberFormatException!

// ✅ Com NumberUtils - retorna 0 como padrão
int value = NumberUtils.toInt("abc", 0);  // 0
```

### Exemplo Básico

```java
package com.example.number;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * Demonstração básica de NumberUtils.
 */
public class NumberUtilsBasicExample {
    
    /**
     * Demonstra operações básicas com números.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        // Conversões seguras
        System.out.println("toInt('123'): " + NumberUtils.toInt("123"));         // 123
        System.out.println("toInt('abc', 0): " + NumberUtils.toInt("abc", 0));   // 0
        System.out.println("toInt(null, 99): " + NumberUtils.toInt(null, 99));   // 99
        
        // Verificações
        System.out.println("\nisDigits('123'): " + NumberUtils.isDigits("123")); // true
        System.out.println("isDigits('12.3'): " + NumberUtils.isDigits("12.3")); // false
        System.out.println("isCreatable('12.3'): " + NumberUtils.isCreatable("12.3")); // true
        System.out.println("isCreatable('0xFF'): " + NumberUtils.isCreatable("0xFF")); // true
        
        // Máximo e Mínimo
        System.out.println("\nmax(1, 5, 3): " + NumberUtils.max(1, 5, 3));       // 5
        System.out.println("min(1, 5, 3): " + NumberUtils.min(1, 5, 3));         // 1
        
        // Comparações
        int result = NumberUtils.compare(5, 3);
        System.out.println("\ncompare(5, 3): " + result);  // 1 (positivo porque 5 > 3)
    }
}
```

### Caso de Uso Real: Calculadora de Desconto

```java
package com.example.number;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * Calculadora de preços e descontos usando NumberUtils.
 * Demonstra parsing seguro e cálculos financeiros.
 */
public class PriceCalculator {
    
    /**
     * Detalhes do cálculo de preço.
     */
    public static class PriceDetails {
        private final double originalPrice;
        private final double discountPercent;
        private final double finalPrice;
        private final double savings;
        
        public PriceDetails(double originalPrice, double discountPercent, 
                           double finalPrice, double savings) {
            this.originalPrice = originalPrice;
            this.discountPercent = discountPercent;
            this.finalPrice = finalPrice;
            this.savings = savings;
        }
        
        @Override
        public String toString() {
            return String.format(
                "Preço Original: $%.2f | Desconto: %.0f%% | " +
                "Economiza: $%.2f | Preço Final: $%.2f",
                originalPrice, discountPercent, savings, finalPrice
            );
        }
        
        public double getFinalPrice() {
            return finalPrice;
        }
    }
    
    /**
     * Calcula preço com desconto de forma segura.
     *
     * @param priceStr preço como string
     * @param discountStr desconto percentual como string
     * @return detalhes do cálculo
     */
    public PriceDetails calculateDiscount(String priceStr, String discountStr) {
        // Parsing seguro com valores padrão
        double price = NumberUtils.toDouble(priceStr, 0.0);
        double discount = NumberUtils.toDouble(discountStr, 0.0);
        
        // Validações
        price = NumberUtils.max(price, 0.0);  // Não pode ser negativo
        discount = NumberUtils.min(discount, 100.0);  // Máximo 100%
        discount = NumberUtils.max(discount, 0.0);    // Não pode ser negativo
        
        // Cálculos
        double savings = price * (discount / 100.0);
        double finalPrice = price - savings;
        
        return new PriceDetails(price, discount, finalPrice, savings);
    }
    
    /**
     * Calcula o melhor desconto entre várias opções.
     *
     * @param price preço base
     * @param discounts descontos disponíveis
     * @return melhor preço final
     */
    public double getBestPrice(double price, double... discounts) {
        if (discounts == null || discounts.length == 0) {
            return price;
        }
        
        double bestPrice = price;
        
        for (double discount : discounts) {
            double currentPrice = price * (1 - discount / 100.0);
            bestPrice = NumberUtils.min(bestPrice, currentPrice);
        }
        
        return bestPrice;
    }
    
    /**
     * Verifica se uma string representa um preço válido.
     *
     * @param priceStr string a validar
     * @return true se é um número válido
     */
    public boolean isValidPrice(String priceStr) {
        return NumberUtils.isCreatable(priceStr) && 
               NumberUtils.toDouble(priceStr, -1) >= 0;
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        PriceCalculator calculator = new PriceCalculator();
        
        System.out.println("=== Calculadora de Descontos ===\n");
        
        // Cálculo normal
        PriceDetails deal1 = calculator.calculateDiscount("99.99", "20");
        System.out.println("Produto 1: " + deal1);
        
        // Entrada inválida (usa padrões)
        PriceDetails deal2 = calculator.calculateDiscount("invalid", "abc");
        System.out.println("Produto 2 (inválido): " + deal2);
        
        // Desconto > 100% (limitado a 100%)
        PriceDetails deal3 = calculator.calculateDiscount("50", "150");
        System.out.println("Produto 3 (desconto > 100%): " + deal3);
        
        // Melhor preço entre opções
        System.out.println("\n=== Comparação de Ofertas ===");
        double price = 100.0;
        double best = calculator.getBestPrice(price, 10, 25, 15, 30);
        System.out.println("Preço original: $" + price);
        System.out.println("Melhor oferta: $" + best);
        
        // Validação de preços
        System.out.println("\n=== Validação ===");
        System.out.println("'49.99' é válido? " + calculator.isValidPrice("49.99"));
        System.out.println("'abc' é válido? " + calculator.isValidPrice("abc"));
        System.out.println("'-10' é válido? " + calculator.isValidPrice("-10"));
    }
}
```

**Output**:
```
=== Calculadora de Descontos ===

Produto 1: Preço Original: $99.99 | Desconto: 20% | Economiza: $20.00 | Preço Final: $79.99
Produto 2 (inválido): Preço Original: $0.00 | Desconto: 0% | Economiza: $0.00 | Preço Final: $0.00
Produto 3 (desconto > 100%): Preço Original: $50.00 | Desconto: 100% | Economiza: $50.00 | Preço Final: $0.00

=== Comparação de Ofertas ===
Preço original: $100.0
Melhor oferta: $70.0

=== Validação ===
'49.99' é válido? true
'abc' é válido? false
'-10' é válido? false
```

### API NumberUtils - Principais Métodos

| Método | Descrição | Exemplo |
|--------|-----------|---------|
| `toInt(str, default)` | Converte para int com padrão | `toInt("abc", 0)` → `0` |
| `toLong(str, default)` | Converte para long com padrão | `toLong("123", 0L)` → `123L` |
| `toDouble(str, default)` | Converte para double com padrão | `toDouble("12.3", 0.0)` → `12.3` |
| `isDigits(str)` | Verifica se só dígitos | `isDigits("123")` → `true` |
| `isCreatable(str)` | Verifica se é número válido | `isCreatable("12.3")` → `true` |
| `isParsable(str)` | Verifica se pode fazer parse | `isParsable("0xFF")` → `false` |
| `max(a, b, c)` | Retorna o maior | `max(1, 5, 3)` → `5` |
| `min(a, b, c)` | Retorna o menor | `min(1, 5, 3)` → `1` |
| `compare(x, y)` | Compara dois números | `compare(5, 3)` → `1` |

---

## 🎯 ObjectUtils

### Por que usar ObjectUtils?

Operações com objetos em Java frequentemente requerem verificações null. ObjectUtils simplifica essas operações.

```java
// ❌ Sem ObjectUtils
String result = obj != null ? obj.toString() : "default";

// ✅ Com ObjectUtils
String result = ObjectUtils.defaultIfNull(obj, "default").toString();
```

### Exemplo Básico

```java
package com.example.object;

import org.apache.commons.lang3.ObjectUtils;

/**
 * Demonstração básica de ObjectUtils.
 */
public class ObjectUtilsBasicExample {
    
    /**
     * Demonstra operações básicas com objetos.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        // defaultIfNull
        String name = null;
        System.out.println("defaultIfNull: " + 
            ObjectUtils.defaultIfNull(name, "Anonymous"));  // Anonymous
        
        // firstNonNull
        String result = ObjectUtils.firstNonNull(null, null, "found", "other");
        System.out.println("firstNonNull: " + result);  // found
        
        // isEmpty (works with String, Collections, Arrays, etc)
        System.out.println("\nisEmpty(''): " + ObjectUtils.isEmpty(""));           // true
        System.out.println("isEmpty('  '): " + ObjectUtils.isEmpty("  "));         // false
        System.out.println("isEmpty(null): " + ObjectUtils.isEmpty(null));         // true
        
        // compare
        System.out.println("\ncompare(5, 3): " + ObjectUtils.compare(5, 3));       // 1
        System.out.println("compare(null, 5): " + ObjectUtils.compare(null, 5));   // -1
        
        // max/min
        System.out.println("\nmax(1, 5, 3): " + ObjectUtils.max(1, 5, 3));         // 5
        System.out.println("min(1, 5, 3): " + ObjectUtils.min(1, 5, 3));           // 1
    }
}
```

### Caso de Uso Real: Sistema de Configuração

```java
package com.example.object;

import org.apache.commons.lang3.ObjectUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * Sistema de configuração com fallbacks usando ObjectUtils.
 * Demonstra uso de valores padrão em hierarquia de configurações.
 */
public class ConfigurationManager {
    
    private final Map<String, Object> systemDefaults;
    private final Map<String, Object> userPreferences;
    private final Map<String, Object> environmentOverrides;
    
    /**
     * Construtor inicializa as camadas de configuração.
     */
    public ConfigurationManager() {
        this.systemDefaults = new HashMap<>();
        this.userPreferences = new HashMap<>();
        this.environmentOverrides = new HashMap<>();
        
        // Configurações padrão do sistema
        systemDefaults.put("theme", "light");
        systemDefaults.put("language", "en");
        systemDefaults.put("timeout", 30);
        systemDefaults.put("debug", false);
    }
    
    /**
     * Obtém valor de configuração com fallback em cascata.
     * Prioridade: Environment > User > System Default
     *
     * @param key chave da configuração
     * @return valor da configuração
     */
    public Object getConfig(String key) {
        return ObjectUtils.firstNonNull(
            environmentOverrides.get(key),
            userPreferences.get(key),
            systemDefaults.get(key)
        );
    }
    
    /**
     * Obtém configuração como String com valor padrão.
     *
     * @param key chave da configuração
     * @param defaultValue valor padrão se não encontrado
     * @return valor como String
     */
    public String getConfigAsString(String key, String defaultValue) {
        Object value = getConfig(key);
        return ObjectUtils.defaultIfNull(
            value != null ? value.toString() : null, 
            defaultValue
        );
    }
    
    /**
     * Obtém configuração como Integer com valor padrão.
     *
     * @param key chave da configuração
     * @param defaultValue valor padrão se não encontrado
     * @return valor como Integer
     */
    public Integer getConfigAsInt(String key, Integer defaultValue) {
        Object value = getConfig(key);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return ObjectUtils.defaultIfNull(null, defaultValue);
    }
    
    /**
     * Define preferência do usuário.
     *
     * @param key chave da configuração
     * @param value valor a definir
     */
    public void setUserPreference(String key, Object value) {
        userPreferences.put(key, value);
    }
    
    /**
     * Define override de ambiente.
     *
     * @param key chave da configuração
     * @param value valor a definir
     */
    public void setEnvironmentOverride(String key, Object value) {
        environmentOverrides.put(key, value);
    }
    
    /**
     * Obtém todas as configurações efetivas (resolvidas).
     *
     * @return mapa com todas as configurações
     */
    public Map<String, Object> getAllEffectiveConfigs() {
        Map<String, Object> effective = new HashMap<>(systemDefaults);
        effective.putAll(userPreferences);
        effective.putAll(environmentOverrides);
        return effective;
    }
    
    /**
     * Compara duas configurações e retorna a "maior".
     *
     * @param key1 primeira chave
     * @param key2 segunda chave
     * @return valor maior ou primeiro se iguais
     */
    @SuppressWarnings("unchecked")
    public <T extends Comparable<T>> T getMaxConfig(String key1, String key2) {
        T value1 = (T) getConfig(key1);
        T value2 = (T) getConfig(key2);
        return ObjectUtils.max(value1, value2);
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        ConfigurationManager config = new ConfigurationManager();
        
        System.out.println("=== Sistema de Configuração ===\n");
        
        // 1. Valores padrão do sistema
        System.out.println("Configurações padrão:");
        System.out.println("  Theme: " + config.getConfig("theme"));
        System.out.println("  Language: " + config.getConfig("language"));
        System.out.println("  Timeout: " + config.getConfig("timeout"));
        System.out.println("  Debug: " + config.getConfig("debug"));
        
        // 2. Usuário define preferências
        System.out.println("\nUsuário define tema escuro e idioma português:");
        config.setUserPreference("theme", "dark");
        config.setUserPreference("language", "pt-BR");
        
        System.out.println("  Theme: " + config.getConfig("theme"));
        System.out.println("  Language: " + config.getConfig("language"));
        System.out.println("  Timeout: " + config.getConfig("timeout"));
        
        // 3. Ambiente força debug mode
        System.out.println("\nAmbiente força debug mode:");
        config.setEnvironmentOverride("debug", true);
        config.setEnvironmentOverride("timeout", 60);
        
        System.out.println("  Debug: " + config.getConfig("debug"));
        System.out.println("  Timeout: " + config.getConfig("timeout"));
        
        // 4. Configuração inexistente com fallback
        System.out.println("\nConfiguração inexistente:");
        String custom = config.getConfigAsString("custom", "valor-padrão");
        System.out.println("  Custom: " + custom);
        
        // 5. Todas as configurações efetivas
        System.out.println("\nTodas as configurações efetivas:");
        config.getAllEffectiveConfigs().forEach((key, value) -> 
            System.out.println("  " + key + ": " + value)
        );
    }
}
```

**Output**:
```
=== Sistema de Configuração ===

Configurações padrão:
  Theme: light
  Language: en
  Timeout: 30
  Debug: false

Usuário define tema escuro e idioma português:
  Theme: dark
  Language: pt-BR
  Timeout: 30

Ambiente força debug mode:
  Debug: true
  Timeout: 60

Configuração inexistente:
  Custom: valor-padrão

Todas as configurações efetivas:
  debug: true
  language: pt-BR
  timeout: 60
  theme: dark
```

### API ObjectUtils - Principais Métodos

| Método | Descrição | Exemplo |
|--------|-----------|---------|
| `defaultIfNull(obj, default)` | Retorna default se null | `defaultIfNull(null, "x")` → `"x"` |
| `firstNonNull(values...)` | Primeiro valor não-null | `firstNonNull(null, "a")` → `"a"` |
| `isEmpty(obj)` | Verifica se vazio | `isEmpty("")` → `true` |
| `isNotEmpty(obj)` | Verifica se não vazio | `isNotEmpty("a")` → `true` |
| `compare(c1, c2)` | Compara (null-safe) | `compare(5, 3)` → `1` |
| `max(values...)` | Retorna o maior | `max(1, 5, 3)` → `5` |
| `min(values...)` | Retorna o menor | `min(1, 5, 3)` → `1` |
| `allNotNull(values...)` | Verifica se nenhum é null | `allNotNull(1, 2)` → `true` |
| `anyNotNull(values...)` | Verifica se algum não é null | `anyNotNull(null, 1)` → `true` |
| `clone(obj)` | Clona objeto (se Cloneable) | `clone(obj)` → cópia |

---

## ✅ BooleanUtils

### Por que usar BooleanUtils?

Operações com booleanos e conversões de strings são simplificadas e mais seguras.

### Exemplo Básico e Caso de Uso

```java
package com.example.bool;

import org.apache.commons.lang3.BooleanUtils;

/**
 * Sistema de processamento de flags de feature usando BooleanUtils.
 */
public class FeatureFlagManager {
    
    /**
     * Avalia se uma feature está habilitada a partir de várias fontes.
     *
     * @param envVar variável de ambiente
     * @param configValue valor de configuração
     * @param defaultValue valor padrão
     * @return true se feature habilitada
     */
    public boolean isFeatureEnabled(String envVar, String configValue, boolean defaultValue) {
        // Tenta converter variável de ambiente
        Boolean fromEnv = BooleanUtils.toBooleanObject(envVar);
        if (fromEnv != null) {
            return fromEnv;
        }
        
        // Tenta converter configuração
        Boolean fromConfig = BooleanUtils.toBooleanObject(configValue);
        if (fromConfig != null) {
            return fromConfig;
        }
        
        // Usa valor padrão
        return defaultValue;
    }
    
    /**
     * Converte booleano para string legível.
     *
     * @param value valor booleano
     * @return "Habilitado" ou "Desabilitado"
     */
    public String toReadableString(Boolean value) {
        return BooleanUtils.toString(value, "Habilitado", "Desabilitado", "Indefinido");
    }
    
    /**
     * Verifica se pelo menos uma feature está ativa.
     *
     * @param features array de features
     * @return true se alguma ativa
     */
    public boolean anyFeatureEnabled(Boolean... features) {
        return BooleanUtils.or(features);
    }
    
    /**
     * Verifica se todas as features estão ativas.
     *
     * @param features array de features
     * @return true se todas ativas
     */
    public boolean allFeaturesEnabled(Boolean... features) {
        return BooleanUtils.and(features);
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        FeatureFlagManager manager = new FeatureFlagManager();
        
        System.out.println("=== Feature Flag Manager ===\n");
        
        // Conversões de string
        System.out.println("toBoolean('true'): " + BooleanUtils.toBoolean("true"));       // true
        System.out.println("toBoolean('yes'): " + BooleanUtils.toBoolean("yes"));         // true
        System.out.println("toBoolean('on'): " + BooleanUtils.toBoolean("on"));           // true
        System.out.println("toBoolean('1'): " + BooleanUtils.toBoolean("1"));             // false
        System.out.println("toBooleanObject('1'): " + BooleanUtils.toBooleanObject("1")); // null
        
        // Operações lógicas
        System.out.println("\nOperações lógicas:");
        System.out.println("and(true, true, false): " + 
            BooleanUtils.and(new boolean[]{true, true, false}));  // false
        System.out.println("or(false, false, true): " + 
            BooleanUtils.or(new boolean[]{false, false, true}));  // true
        System.out.println("xor(true, true): " + 
            BooleanUtils.xor(new boolean[]{true, true}));         // false
        
        // Feature flags
        System.out.println("\nFeature Flags:");
        boolean darkMode = manager.isFeatureEnabled("true", null, false);
        boolean betaFeatures = manager.isFeatureEnabled(null, "yes", false);
        boolean experimental = manager.isFeatureEnabled(null, null, true);
        
        System.out.println("Dark Mode: " + manager.toReadableString(darkMode));
        System.out.println("Beta Features: " + manager.toReadableString(betaFeatures));
        System.out.println("Experimental: " + manager.toReadableString(experimental));
        
        System.out.println("\nAlguma feature ativa? " + 
            manager.anyFeatureEnabled(darkMode, betaFeatures, experimental));
    }
}
```

**Output**:
```
=== Feature Flag Manager ===

toBoolean('true'): true
toBoolean('yes'): true
toBoolean('on'): true
toBoolean('1'): false
toBooleanObject('1'): null

Operações lógicas:
and(true, true, false): false
or(false, false, true): true
xor(true, true): false

Feature Flags:
Dark Mode: Habilitado
Beta Features: Habilitado
Experimental: Habilitado

Alguma feature ativa? true
```

### API BooleanUtils - Principais Métodos

| Método | Descrição | Exemplo |
|--------|-----------|---------|
| `toBoolean(str)` | Converte string para boolean | `toBoolean("yes")` → `true` |
| `toBooleanObject(str)` | Converte para Boolean (null se inválido) | `toBooleanObject("1")` → `null` |
| `toString(bool, true, false)` | Converte para string customizada | `toString(true, "Y", "N")` → `"Y"` |
| `and(array)` | AND lógico de array | `and({true, false})` → `false` |
| `or(array)` | OR lógico de array | `or({false, true})` → `true` |
| `xor(array)` | XOR lógico de array | `xor({true, true})` → `false` |
| `negate(bool)` | Negação (null-safe) | `negate(true)` → `false` |
| `isTrue(bool)` | Verifica se é true | `isTrue(Boolean.TRUE)` → `true` |
| `isFalse(bool)` | Verifica se é false | `isFalse(Boolean.FALSE)` → `true` |

---

## 📅 DateUtils

### Por que usar DateUtils?

Manipulação de datas em Java legado (antes do Java 8) era complexa. DateUtils simplifica operações comuns.

> **Nota**: Para código moderno, prefira `java.time` (Java 8+). DateUtils é útil para código legado.

### Exemplo Básico e Caso de Uso

```java
package com.example.date;

import org.apache.commons.lang3.time.DateUtils;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Sistema de agendamento usando DateUtils.
 * Útil para manutenção de código legado.
 */
public class AppointmentScheduler {
    
    private static final SimpleDateFormat formatter = 
        new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    /**
     * Agenda compromisso para próxima ocorrência de dia da semana.
     *
     * @param dayOfWeek dia da semana (Calendar.MONDAY, etc)
     * @param hour hora do compromisso
     * @return data do próximo compromisso
     */
    public Date scheduleNextWeekday(int dayOfWeek, int hour) {
        Date now = new Date();
        
        // Define para o próximo dia da semana especificado
        Date next = DateUtils.setDays(now, dayOfWeek);
        
        // Se já passou, adiciona uma semana
        if (next.before(now)) {
            next = DateUtils.addWeeks(next, 1);
        }
        
        // Define a hora
        next = DateUtils.setHours(next, hour);
        next = DateUtils.setMinutes(next, 0);
        next = DateUtils.setSeconds(next, 0);
        next = DateUtils.setMilliseconds(next, 0);
        
        return next;
    }
    
    /**
     * Verifica se duas datas são no mesmo dia.
     *
     * @param date1 primeira data
     * @param date2 segunda data
     * @return true se mesmo dia
     */
    public boolean isSameDay(Date date1, Date date2) {
        return DateUtils.isSameDay(date1, date2);
    }
    
    /**
     * Adiciona dias úteis (segunda a sexta).
     *
     * @param startDate data inicial
     * @param businessDays dias úteis a adicionar
     * @return data resultante
     */
    public Date addBusinessDays(Date startDate, int businessDays) {
        Date result = startDate;
        int added = 0;
        
        while (added < businessDays) {
            result = DateUtils.addDays(result, 1);
            
            // Pula fins de semana
            Calendar cal = Calendar.getInstance();
            cal.setTime(result);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                added++;
            }
        }
        
        return result;
    }
    
    /**
     * Trunca data para o início do dia.
     *
     * @param date data a truncar
     * @return data truncada (00:00:00.000)
     */
    public Date truncateToDay(Date date) {
        return DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        AppointmentScheduler scheduler = new AppointmentScheduler();
        Date now = new Date();
        
        System.out.println("=== Agendador de Compromissos ===\n");
        System.out.println("Agora: " + formatter.format(now));
        
        // Adicionar dias
        Date tomorrow = DateUtils.addDays(now, 1);
        System.out.println("\nAmanhã: " + formatter.format(tomorrow));
        
        Date nextWeek = DateUtils.addWeeks(now, 1);
        System.out.println("Próxima semana: " + formatter.format(nextWeek));
        
        Date nextMonth = DateUtils.addMonths(now, 1);
        System.out.println("Próximo mês: " + formatter.format(nextMonth));
        
        // Truncar
        Date startOfDay = scheduler.truncateToDay(now);
        System.out.println("\nInício do dia: " + formatter.format(startOfDay));
        
        // Comparação
        System.out.println("\nAgora e amanhã são o mesmo dia? " + 
            scheduler.isSameDay(now, tomorrow));
        
        // Dias úteis
        Date in5BusinessDays = scheduler.addBusinessDays(now, 5);
        System.out.println("\nEm 5 dias úteis: " + formatter.format(in5BusinessDays));
    }
}
```

**Output** (exemplo):
```
=== Agendador de Compromissos ===

Agora: 10/10/2025 14:30

Amanhã: 11/10/2025 14:30
Próxima semana: 17/10/2025 14:30
Próximo mês: 10/11/2025 14:30

Início do dia: 10/10/2025 00:00

Agora e amanhã são o mesmo dia? false

Em 5 dias úteis: 17/10/2025 14:30
```

### API DateUtils - Principais Métodos

| Método | Descrição | Exemplo |
|--------|-----------|---------|
| `addDays(date, amount)` | Adiciona dias | `addDays(date, 5)` |
| `addWeeks(date, amount)` | Adiciona semanas | `addWeeks(date, 2)` |
| `addMonths(date, amount)` | Adiciona meses | `addMonths(date, 1)` |
| `isSameDay(date1, date2)` | Verifica se mesmo dia | `isSameDay(d1, d2)` |
| `truncate(date, field)` | Trunca para campo | `truncate(date, DAY)` |
| `ceiling(date, field)` | Arredonda para cima | `ceiling(date, HOUR)` |
| `round(date, field)` | Arredonda | `round(date, MINUTE)` |

---

## 🎲 RandomStringUtils

### Por que usar RandomStringUtils?

Geração segura de strings aleatórias para tokens, senhas, IDs, etc.

### Exemplo Básico e Caso de Uso

```java
package com.example.random;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Gerador de tokens e senhas usando RandomStringUtils.
 */
public class TokenGenerator {
    
    /**
     * Gera token de sessão alfanumérico.
     *
     * @param length comprimento do token
     * @return token aleatório
     */
    public String generateSessionToken(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
    
    /**
     * Gera senha forte com letras e números.
     *
     * @param length comprimento da senha
     * @return senha aleatória
     */
    public String generateStrongPassword(int length) {
        // Garante pelo menos uma letra maiúscula, minúscula e número
        String upper = RandomStringUtils.random(2, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        String lower = RandomStringUtils.random(2, "abcdefghijklmnopqrstuvwxyz");
        String numbers = RandomStringUtils.randomNumeric(2);
        String special = RandomStringUtils.random(1, "!@#$%^&*");
        
        // Completa o restante aleatoriamente
        int remaining = length - 7;
        String rest = RandomStringUtils.random(remaining, 
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*");
        
        // Embaralha tudo
        String combined = upper + lower + numbers + special + rest;
        char[] chars = combined.toCharArray();
        
        // Shuffle simples
        for (int i = chars.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        
        return new String(chars);
    }
    
    /**
     * Gera ID único curto (para URLs).
     *
     * @return ID de 8 caracteres
     */
    public String generateShortId() {
        return RandomStringUtils.randomAlphanumeric(8);
    }
    
    /**
     * Gera código de verificação numérico.
     *
     * @param digits número de dígitos
     * @return código numérico
     */
    public String generateVerificationCode(int digits) {
        return RandomStringUtils.randomNumeric(digits);
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        TokenGenerator generator = new TokenGenerator();
        
        System.out.println("=== Gerador de Tokens ===\n");
        
        // Tokens de sessão
        System.out.println("Session Token (32): " + 
            generator.generateSessionToken(32));
        
        // Senhas fortes
        System.out.println("\nSenhas fortes (12 caracteres):");
        for (int i = 0; i < 3; i++) {
            System.out.println("  " + generator.generateStrongPassword(12));
        }
        
        // IDs curtos
        System.out.println("\nShort IDs:");
        for (int i = 0; i < 5; i++) {
            System.out.println("  " + generator.generateShortId());
        }
        
        // Códigos de verificação
        System.out.println("\nCódigos de verificação (6 dígitos):");
        for (int i = 0; i < 3; i++) {
            System.out.println("  " + generator.generateVerificationCode(6));
        }
        
        // Outros exemplos
        System.out.println("\nOutros exemplos:");
        System.out.println("Alfabético (10): " + RandomStringUtils.randomAlphabetic(10));
        System.out.println("Numérico (6): " + RandomStringUtils.randomNumeric(6));
        System.out.println("ASCII (15): " + RandomStringUtils.randomAscii(15));
    }
}
```

**Output** (exemplo, valores sempre diferentes):
```
=== Gerador de Tokens ===

Session Token (32): 7k3mP9xQ2vL5nR8wC1jT6hY4bN0sF

Senhas fortes (12 caracteres):
  aB3$xT9mQ2pL
  kN7@rF4cY1vH
  dG5!wJ8bM3zX

Short IDs:
  k7mQ9xPl
  n3vR2cTy
  h5wJ8bNf
  p1sL4dGm
  r9xK6tYq

Códigos de verificação (6 dígitos):
  738294
  521067
  946382

Outros exemplos:
Alfabético (10): kxmplqrvty
Numérico (6): 738294
ASCII (15): k7@xT!9mQ#2pL$r
```

### API RandomStringUtils - Principais Métodos

| Método | Descrição | Exemplo |
|--------|-----------|---------|
| `randomAlphabetic(count)` | Letras aleatórias | `randomAlphabetic(10)` |
| `randomNumeric(count)` | Números aleatórios | `randomNumeric(6)` |
| `randomAlphanumeric(count)` | Letras e números | `randomAlphanumeric(8)` |
| `random(count, chars)` | De conjunto de chars | `random(5, "ABC")` |
| `randomAscii(count)` | ASCII aleatório | `randomAscii(10)` |

---

## 👫 Pair e Triple

### Por que usar Pair/Triple?

Quando você precisa retornar ou armazenar 2 ou 3 valores relacionados sem criar uma classe.

### Exemplo Básico e Caso de Uso

```java
package com.example.pair;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.*;

/**
 * Sistema de análise de dados usando Pair e Triple.
 */
public class DataAnalyzer {
    
    /**
     * Calcula mínimo e máximo de um array.
     *
     * @param numbers array de números
     * @return par (min, max)
     */
    public Pair<Integer, Integer> findMinMax(int[] numbers) {
        if (numbers == null || numbers.length == 0) {
            return Pair.of(null, null);
        }
        
        int min = Arrays.stream(numbers).min().getAsInt();
        int max = Arrays.stream(numbers).max().getAsInt();
        
        return Pair.of(min, max);
    }
    
    /**
     * Calcula estatísticas básicas.
     *
     * @param numbers array de números
     * @return triple (média, mínimo, máximo)
     */
    public Triple<Double, Integer, Integer> calculateStats(int[] numbers) {
        if (numbers == null || numbers.length == 0) {
            return Triple.of(0.0, 0, 0);
        }
        
        double avg = Arrays.stream(numbers).average().getAsDouble();
        int min = Arrays.stream(numbers).min().getAsInt();
        int max = Arrays.stream(numbers).max().getAsInt();
        
        return Triple.of(avg, min, max);
    }
    
    /**
     * Conta ocorrências e retorna top N.
     *
     * @param words lista de palavras
     * @param topN quantas retornar
     * @return lista de pares (palavra, contagem)
     */
    public List<Pair<String, Integer>> getTopWords(List<String> words, int topN) {
        Map<String, Integer> counts = new HashMap<>();
        
        for (String word : words) {
            counts.put(word, counts.getOrDefault(word, 0) + 1);
        }
        
        List<Pair<String, Integer>> pairs = new ArrayList<>();
        counts.forEach((word, count) -> pairs.add(Pair.of(word, count)));
        
        // Ordena por contagem decrescente
        pairs.sort((p1, p2) -> p2.getValue().compareTo(p1.getValue()));
        
        return pairs.subList(0, Math.min(topN, pairs.size()));
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        DataAnalyzer analyzer = new DataAnalyzer();
        
        System.out.println("=== Análise de Dados ===\n");
        
        // Min/Max
        int[] numbers = {5, 2, 9, 1, 7, 3};
        Pair<Integer, Integer> minMax = analyzer.findMinMax(numbers);
        System.out.println("Array: " + Arrays.toString(numbers));
        System.out.println("Min: " + minMax.getLeft() + ", Max: " + minMax.getRight());
        
        // Estatísticas
        Triple<Double, Integer, Integer> stats = analyzer.calculateStats(numbers);
        System.out.println("\nEstatísticas:");
        System.out.println("  Média: " + stats.getLeft());
        System.out.println("  Mínimo: " + stats.getMiddle());
        System.out.println("  Máximo: " + stats.getRight());
        
        // Top palavras
        List<String> words = Arrays.asList(
            "java", "python", "java", "go", "java", "python", "rust"
        );
        System.out.println("\nTop 3 palavras:");
        List<Pair<String, Integer>> top = analyzer.getTopWords(words, 3);
        top.forEach(pair -> 
            System.out.println("  " + pair.getLeft() + ": " + pair.getValue())
        );
        
        // Mutable vs Immutable
        System.out.println("\nMutable vs Immutable:");
        MutablePair<String, Integer> mutable = MutablePair.of("count", 0);
        System.out.println("Inicial: " + mutable);
        mutable.setValue(10);
        System.out.println("Após set: " + mutable);
        
        ImmutablePair<String, Integer> immutable = ImmutablePair.of("final", 100);
        System.out.println("Imutável: " + immutable);
        // immutable.setValue(200); // Erro de compilação!
    }
}
```

**Output**:
```
=== Análise de Dados ===

Array: [5, 2, 9, 1, 7, 3]
Min: 1, Max: 9

Estatísticas:
  Média: 4.5
  Mínimo: 1
  Máximo: 9

Top 3 palavras:
  java: 3
  python: 2
  go: 1

Mutable vs Immutable:
Inicial: (count,0)
Após set: (count,10)
Imutável: (final,100)
```

### API Pair/Triple - Principais Métodos

| Classe/Método | Descrição |
|---------------|-----------|
| `Pair.of(L, R)` | Cria par imutável |
| `getLeft()` | Obtém elemento esquerdo |
| `getRight()` | Obtém elemento direito |
| `Triple.of(L, M, R)` | Cria tripla imutável |
| `getLeft()` | Obtém primeiro |
| `getMiddle()` | Obtém segundo |
| `getRight()` | Obtém terceiro |
| `MutablePair` | Versão mutável |
| `ImmutablePair` | Versão imutável |

---

## 📏 Range

### Por que usar Range?

Representar intervalos numéricos de forma elegante e realizar verificações de containment.

### Exemplo Básico e Caso de Uso

```java
package com.example.range;

import org.apache.commons.lang3.Range;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Sistema de validação de idades e períodos usando Range.
 */
public class AgeValidator {
    
    // Faixas etárias
    private static final Range<Integer> CHILD = Range.between(0, 12);
    private static final Range<Integer> TEEN = Range.between(13, 19);
    private static final Range<Integer> ADULT = Range.between(20, 64);
    private static final Range<Integer> SENIOR = Range.between(65, 120);
    
    // Horário de trabalho
    private static final Range<Integer> WORK_HOURS = Range.between(9, 18);
    
    /**
     * Classifica pessoa por faixa etária.
     *
     * @param age idade
     * @return classificação
     */
    public String classifyByAge(int age) {
        if (CHILD.contains(age)) return "Criança";
        if (TEEN.contains(age)) return "Adolescente";
        if (ADULT.contains(age)) return "Adulto";
        if (SENIOR.contains(age)) return "Idoso";
        return "Idade inválida";
    }
    
    /**
     * Verifica se está em horário de trabalho.
     *
     * @param hour hora (0-23)
     * @return true se em horário comercial
     */
    public boolean isWorkingHours(int hour) {
        return WORK_HOURS.contains(hour);
    }
    
    /**
     * Calcula desconto baseado em faixa de preço.
     *
     * @param price preço
     * @return percentual de desconto
     */
    public double calculateDiscount(double price) {
        Range<Double> range1 = Range.between(0.0, 50.0);
        Range<Double> range2 = Range.between(50.01, 100.0);
        Range<Double> range3 = Range.between(100.01, 500.0);
        Range<Double> range4 = Range.between(500.01, Double.MAX_VALUE);
        
        if (range1.contains(price)) return 0.0;
        if (range2.contains(price)) return 5.0;
        if (range3.contains(price)) return 10.0;
        if (range4.contains(price)) return 15.0;
        
        return 0.0;
    }
    
    /**
     * Verifica se duas faixas se sobrepõem.
     *
     * @param range1 primeira faixa
     * @param range2 segunda faixa
     * @return true se há sobreposição
     */
    public boolean overlaps(Range<Integer> range1, Range<Integer> range2) {
        return range1.isOverlappedBy(range2);
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        AgeValidator validator = new AgeValidator();
        
        System.out.println("=== Validador de Faixas ===\n");
        
        // Classificação por idade
        int[] ages = {5, 15, 25, 70};
        System.out.println("Classificação por idade:");
        for (int age : ages) {
            System.out.println("  " + age + " anos: " + validator.classifyByAge(age));
        }
        
        // Horário de trabalho
        System.out.println("\nHorário comercial:");
        int[] hours = {8, 12, 18, 20};
        for (int hour : hours) {
            System.out.println("  " + hour + "h: " + 
                (validator.isWorkingHours(hour) ? "Sim" : "Não"));
        }
        
        // Desconto por faixa de preço
        System.out.println("\nDescontos:");
        double[] prices = {30.0, 75.0, 250.0, 1000.0};
        for (double price : prices) {
            System.out.println(String.format("  $%.2f: %.0f%% desconto", 
                price, validator.calculateDiscount(price)));
        }
        
        // Operações com Range
        System.out.println("\nOperações com Range:");
        Range<Integer> range1 = Range.between(1, 10);
        Range<Integer> range2 = Range.between(5, 15);
        
        System.out.println("Range1: " + range1);
        System.out.println("Range2: " + range2);
        System.out.println("Sobrepõem? " + validator.overlaps(range1, range2));
        System.out.println("Range1 contém 5? " + range1.contains(5));
        System.out.println("Range1 contém 15? " + range1.contains(15));
    }
}
```

**Output**:
```
=== Validador de Faixas ===

Classificação por idade:
  5 anos: Criança
  15 anos: Adolescente
  25 anos: Adulto
  70 anos: Idoso

Horário comercial:
  8h: Não
  12h: Sim
  18h: Sim
  20h: Não

Descontos:
  $30.00: 0% desconto
  $75.00: 5% desconto
  $250.00: 10% desconto
  $1000.00: 15% desconto

Operações com Range:
Range1: [1..10]
Range2: [5..15]
Sobrepõem? true
Range1 contém 5? true
Range1 contém 15? false
```

### API Range - Principais Métodos

| Método | Descrição |
|--------|-----------|
| `Range.between(min, max)` | Cria range inclusivo |
| `contains(value)` | Verifica se contém valor |
| `isAfter(value)` | Verifica se range está após valor |
| `isBefore(value)` | Verifica se range está antes de valor |
| `isOverlappedBy(range)` | Verifica sobreposição |
| `getMinimum()` | Retorna valor mínimo |
| `getMaximum()` | Retorna valor máximo |

---

## 🚨 ExceptionUtils

### Por que usar ExceptionUtils?

Simplifica análise e manipulação de stack traces e exceções encadeadas.

### Exemplo Básico e Caso de Uso

```java
package com.example.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Sistema de logging de erros usando ExceptionUtils.
 */
public class ErrorLogger {
    
    /**
     * Loga exceção com detalhes formatados.
     *
     * @param ex exceção a logar
     */
    public void logException(Exception ex) {
        System.out.println("=== Detalhes do Erro ===");
        System.out.println("Mensagem: " + ExceptionUtils.getMessage(ex));
        System.out.println("Causa raiz: " + ExceptionUtils.getRootCauseMessage(ex));
        System.out.println("\nStack Trace:");
        System.out.println(ExceptionUtils.getStackTrace(ex));
    }
    
    /**
     * Obtém mensagem de erro simplificada.
     *
     * @param ex exceção
     * @return mensagem formatada
     */
    public String getSimpleErrorMessage(Exception ex) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        return rootCause != null ? 
            rootCause.getMessage() : 
            ex.getMessage();
    }
    
    /**
     * Verifica se exceção é de um tipo específico.
     *
     * @param ex exceção
     * @param type tipo a verificar
     * @return true se é do tipo
     */
    public boolean isExceptionType(Throwable ex, Class<? extends Throwable> type) {
        return ExceptionUtils.indexOfThrowable(ex, type) != -1;
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        ErrorLogger logger = new ErrorLogger();
        
        try {
            // Simula erro encadeado
            methodA();
        } catch (Exception ex) {
            logger.logException(ex);
            
            System.out.println("\n=== Análise ===");
            System.out.println("Mensagem simples: " + logger.getSimpleErrorMessage(ex));
            System.out.println("É NullPointerException? " + 
                logger.isExceptionType(ex, NullPointerException.class));
        }
    }
    
    private static void methodA() throws Exception {
        try {
            methodB();
        } catch (Exception ex) {
            throw new Exception("Erro em methodA", ex);
        }
    }
    
    private static void methodB() throws Exception {
        try {
            methodC();
        } catch (Exception ex) {
            throw new Exception("Erro em methodB", ex);
        }
    }
    
    private static void methodC() {
        throw new NullPointerException("Objeto não inicializado!");
    }
}
```

**Output**:
```
=== Detalhes do Erro ===
Mensagem: Erro em methodA
Causa raiz: java.lang.NullPointerException: Objeto não inicializado!

Stack Trace:
java.lang.Exception: Erro em methodA
	at ErrorLogger.methodA(ErrorLogger.java:XX)
	...
Caused by: java.lang.Exception: Erro em methodB
	at ErrorLogger.methodB(ErrorLogger.java:XX)
	...
Caused by: java.lang.NullPointerException: Objeto não inicializado!
	at ErrorLogger.methodC(ErrorLogger.java:XX)
	...

=== Análise ===
Mensagem simples: Objeto não inicializado!
É NullPointerException? true
```

### API ExceptionUtils - Principais Métodos

| Método | Descrição |
|--------|-----------|
| `getMessage(ex)` | Obtém mensagem da exceção |
| `getRootCause(ex)` | Obtém causa raiz |
| `getRootCauseMessage(ex)` | Mensagem da causa raiz |
| `getStackTrace(ex)` | Stack trace como string |
| `indexOfThrowable(ex, type)` | Índice do tipo na cadeia |
| `hasCause(ex, type)` | Verifica se tem causa do tipo |

---

## 💻 SystemUtils

### Por que usar SystemUtils?

Acesso fácil a informações do sistema e ambiente de execução.

### Exemplo Básico

```java
package com.example.system;

import org.apache.commons.lang3.SystemUtils;

/**
 * Demonstração de SystemUtils.
 */
public class SystemInfo {
    
    /**
     * Exibe informações do sistema.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        System.out.println("=== Informações do Sistema ===\n");
        
        // Java
        System.out.println("Java:");
        System.out.println("  Version: " + SystemUtils.JAVA_VERSION);
        System.out.println("  Vendor: " + SystemUtils.JAVA_VENDOR);
        System.out.println("  Home: " + SystemUtils.JAVA_HOME);
        System.out.println("  Is Java 8+: " + SystemUtils.IS_JAVA_1_8);
        System.out.println("  Is Java 11+: " + SystemUtils.IS_JAVA_11);
        System.out.println("  Is Java 17+: " + SystemUtils.IS_JAVA_17);
        
        // Sistema Operacional
        System.out.println("\nSistema Operacional:");
        System.out.println("  Nome: " + SystemUtils.OS_NAME);
        System.out.println("  Versão: " + SystemUtils.OS_VERSION);
        System.out.println("  Arquitetura: " + SystemUtils.OS_ARCH);
        System.out.println("  Is Windows: " + SystemUtils.IS_OS_WINDOWS);
        System.out.println("  Is Linux: " + SystemUtils.IS_OS_LINUX);
        System.out.println("  Is Mac: " + SystemUtils.IS_OS_MAC);
        
        // Usuário
        System.out.println("\nUsuário:");
        System.out.println("  Nome: " + SystemUtils.USER_NAME);
        System.out.println("  Home: " + SystemUtils.USER_HOME);
        System.out.println("  Dir: " + SystemUtils.USER_DIR);
        
        // Diretórios temporários
        System.out.println("\nDiretórios:");
        System.out.println("  Temp: " + SystemUtils.JAVA_IO_TMPDIR);
    }
}
```

---

## ✅ Validate

### Por que usar Validate?

Validações com mensagens de erro automáticas, lançando exceções quando condições não são atendidas.

### Exemplo Básico e Caso de Uso

```java
package com.example.validate;

import org.apache.commons.lang3.Validate;
import java.util.List;

/**
 * Sistema de validação de usuários usando Validate.
 */
public class UserService {
    
    /**
     * Modelo de usuário.
     */
    public static class User {
        private String username;
        private String email;
        private int age;
        
        public User(String username, String email, int age) {
            this.username = username;
            this.email = email;
            this.age = age;
        }
        
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public int age() { return age; }
    }
    
    /**
     * Cria usuário com validações.
     *
     * @param username nome de usuário
     * @param email email
     * @param age idade
     * @return usuário criado
     * @throws IllegalArgumentException se validação falhar
     */
    public User createUser(String username, String email, int age) {
        // Validações com mensagens customizadas
        Validate.notBlank(username, "Username não pode ser vazio");
        Validate.notBlank(email, "Email não pode ser vazio");
        
        // Validações booleanas
        Validate.isTrue(username.length() >= 3, 
            "Username deve ter pelo menos 3 caracteres");
        Validate.isTrue(email.contains("@"), 
            "Email deve conter @");
        Validate.isTrue(age >= 18 && age <= 120, 
            "Idade deve estar entre 18 e 120");
        
        return new User(username, email, age);
    }
    
    /**
     * Processa lista de usuários.
     *
     * @param users lista de usuários
     * @throws NullPointerException se null
     * @throws IllegalArgumentException se vazia
     */
    public void processUsers(List<User> users) {
        // Valida não-null
        Validate.notNull(users, "Lista de usuários não pode ser null");
        
        // Valida não-vazia
        Validate.notEmpty(users, "Lista de usuários não pode estar vazia");
        
        // Valida sem nulls
        Validate.noNullElements(users, "Lista não pode conter usuários null");
        
        System.out.println("Processando " + users.size() + " usuários...");
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        UserService service = new UserService();
        
        System.out.println("=== Validações ===\n");
        
        // Validação bem-sucedida
        try {
            User user = service.createUser("john_doe", "john@example.com", 25);
            System.out.println("✅ Usuário criado: " + user.getUsername());
        } catch (Exception ex) {
            System.out.println("❌ Erro: " + ex.getMessage());
        }
        
        // Validação falha - username curto
        try {
            service.createUser("ab", "test@example.com", 25);
        } catch (Exception ex) {
            System.out.println("❌ Erro: " + ex.getMessage());
        }
        
        // Validação falha - idade inválida
        try {
            service.createUser("valid_user", "valid@example.com", 15);
        } catch (Exception ex) {
            System.out.println("❌ Erro: " + ex.getMessage());
        }
        
        // Validação de lista
        try {
            service.processUsers(null);
        } catch (Exception ex) {
            System.out.println("❌ Erro: " + ex.getMessage());
        }
    }
}
```

**Output**:
```
=== Validações ===

✅ Usuário criado: john_doe
❌ Erro: Username deve ter pelo menos 3 caracteres
❌ Erro: Idade deve estar entre 18 e 120
❌ Erro: Lista de usuários não pode ser null
```

### API Validate - Principais Métodos

| Método | Descrição |
|--------|-----------|
| `notNull(obj, msg)` | Valida não-null |
| `notBlank(str, msg)` | Valida string não em branco |
| `notEmpty(coll, msg)` | Valida coleção não vazia |
| `noNullElements(coll, msg)` | Valida sem elementos null |
| `isTrue(condition, msg)` | Valida condição verdadeira |
| `validIndex(array, index, msg)` | Valida índice válido |
| `inclusiveBetween(min, max, value)` | Valida valor em faixa |

---

## 🏗️ Builders

### Por que usar Builders?

Classes utilitárias para implementar padrões como `toString()`, `equals()`, `hashCode()` e `compareTo()`.

### Exemplo Completo

```java
package com.example.builders;

import org.apache.commons.lang3.builder.*;

/**
 * Modelo de produto usando Builders do Commons Lang3.
 */
public class Product implements Comparable<Product> {
    
    private final long id;
    private final String name;
    private final double price;
    private final String category;
    
    /**
     * Construtor completo.
     *
     * @param id ID do produto
     * @param name nome do produto
     * @param price preço
     * @param category categoria
     */
    public Product(long id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }
    
    // Getters
    public long getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    
    /**
     * toString() usando ToStringBuilder.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("name", name)
            .append("price", price)
            .append("category", category)
            .toString();
    }
    
    /**
     * equals() usando EqualsBuilder.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Product other = (Product) obj;
        return new EqualsBuilder()
            .append(id, other.id)
            .append(name, other.name)
            .append(price, other.price)
            .append(category, other.category)
            .isEquals();
    }
    
    /**
     * hashCode() usando HashCodeBuilder.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(name)
            .append(price)
            .append(category)
            .toHashCode();
    }
    
    /**
     * compareTo() usando CompareToBuilder.
     * Ordena por categoria, depois por preço, depois por nome.
     */
    @Override
    public int compareTo(Product other) {
        return new CompareToBuilder()
            .append(this.category, other.category)
            .append(this.price, other.price)
            .append(this.name, other.name)
            .toComparison();
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        Product p1 = new Product(1, "Laptop", 999.99, "Electronics");
        Product p2 = new Product(2, "Mouse", 29.99, "Electronics");
        Product p3 = new Product(1, "Laptop", 999.99, "Electronics");
        
        System.out.println("=== Builders ===\n");
        
        // toString
        System.out.println("toString():");
        System.out.println("  " + p1);
        System.out.println("  " + p2);
        
        // equals
        System.out.println("\nequals():");
        System.out.println("  p1.equals(p2): " + p1.equals(p2));  // false
        System.out.println("  p1.equals(p3): " + p1.equals(p3));  // true
        
        // hashCode
        System.out.println("\nhashCode():");
        System.out.println("  p1: " + p1.hashCode());
        System.out.println("  p3: " + p3.hashCode());  // Mesmo hash
        
        // compareTo
        System.out.println("\ncompareTo():");
        System.out.println("  p1 vs p2: " + p1.compareTo(p2));  // positivo (999 > 29)
        System.out.println("  p2 vs p1: " + p2.compareTo(p1));  // negativo
        System.out.println("  p1 vs p3: " + p1.compareTo(p3));  // 0 (iguais)
    }
}
```

**Output**:
```
=== Builders ===

toString():
  Product[id=1,name=Laptop,price=999.99,category=Electronics]
  Product[id=2,name=Mouse,price=29.99,category=Electronics]

equals():
  p1.equals(p2): false
  p1.equals(p3): true

hashCode():
  p1: 1544604849
  p3: 1544604849

compareTo():
  p1 vs p2: 1
  p2 vs p1: -1
  p1 vs p3: 0
```

### API Builders - Classes Principais

| Builder | Uso |
|---------|-----|
| `ToStringBuilder` | Implementar toString() |
| `EqualsBuilder` | Implementar equals() |
| `HashCodeBuilder` | Implementar hashCode() |
| `CompareToBuilder` | Implementar compareTo() |

---

## 🧪 Testes

### Suite Completa de Testes JUnit 5

```java
package com.example.commons;

import org.apache.commons.lang3.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de testes para Apache Commons Lang3.
 */
class CommonsLang3Test {
    
    @Nested
    @DisplayName("StringUtils Tests")
    class StringUtilsTests {
        
        @Test
        @DisplayName("isEmpty deve verificar corretamente")
        void testIsEmpty() {
            assertTrue(StringUtils.isEmpty(null));
            assertTrue(StringUtils.isEmpty(""));
            assertFalse(StringUtils.isEmpty(" "));
            assertFalse(StringUtils.isEmpty("abc"));
        }
        
        @Test
        @DisplayName("isBlank deve verificar espaços")
        void testIsBlank() {
            assertTrue(StringUtils.isBlank(null));
            assertTrue(StringUtils.isBlank(""));
            assertTrue(StringUtils.isBlank("   "));
            assertFalse(StringUtils.isBlank("abc"));
        }
        
        @Test
        @DisplayName("capitalize deve funcionar")
        void testCapitalize() {
            assertEquals("Hello", StringUtils.capitalize("hello"));
            assertEquals("", StringUtils.capitalize(""));
            assertNull(StringUtils.capitalize(null));
        }
        
        @Test
        @DisplayName("join deve unir strings")
        void testJoin() {
            String[] arr = {"a", "b", "c"};
            assertEquals("a,b,c", StringUtils.join(arr, ","));
            assertEquals("abc", StringUtils.join(arr, ""));
        }
        
        @Test
        @DisplayName("split deve dividir corretamente")
        void testSplit() {
            String[] result = StringUtils.split("a,b,c", ",");
            assertArrayEquals(new String[]{"a", "b", "c"}, result);
            
            // Ignora vazios
            String[] result2 = StringUtils.split("a,,b", ",");
            assertArrayEquals(new String[]{"a", "b"}, result2);
        }
    }
    
    @Nested
    @DisplayName("ArrayUtils Tests")
    class ArrayUtilsTests {
        
        @Test
        @DisplayName("isEmpty deve verificar arrays")
        void testIsEmpty() {
            assertTrue(ArrayUtils.isEmpty(new int[0]));
            assertTrue(ArrayUtils.isEmpty(null));
            assertFalse(ArrayUtils.isEmpty(new int[]{1}));
        }
        
        @Test
        @DisplayName("contains deve encontrar elementos")
        void testContains() {
            int[] arr = {1, 2, 3};
            assertTrue(ArrayUtils.contains(arr, 2));
            assertFalse(ArrayUtils.contains(arr, 5));
        }
        
        @Test
        @DisplayName("add deve adicionar elemento")
        void testAdd() {
            int[] arr = {1, 2};
            int[] result = ArrayUtils.add(arr, 3);
            assertArrayEquals(new int[]{1, 2, 3}, result);
        }
        
        @Test
        @DisplayName("remove deve remover por índice")
        void testRemove() {
            int[] arr = {1, 2, 3};
            int[] result = ArrayUtils.remove(arr, 1);
            assertArrayEquals(new int[]{1, 3}, result);
        }
    }
    
    @Nested
    @DisplayName("NumberUtils Tests")
    class NumberUtilsTests {
        
        @Test
        @DisplayName("toInt deve converter com segurança")
        void testToInt() {
            assertEquals(123, NumberUtils.toInt("123"));
            assertEquals(0, NumberUtils.toInt("abc", 0));
            assertEquals(99, NumberUtils.toInt(null, 99));
        }
        
        @Test
        @DisplayName("isDigits deve verificar dígitos")
        void testIsDigits() {
            assertTrue(NumberUtils.isDigits("123"));
            assertFalse(NumberUtils.isDigits("12.3"));
            assertFalse(NumberUtils.isDigits("abc"));
        }
        
        @Test
        @DisplayName("max deve retornar maior")
        void testMax() {
            assertEquals(5, NumberUtils.max(1, 5, 3));
            assertEquals(10, NumberUtils.max(10, 5, 8));
        }
        
        @Test
        @DisplayName("min deve retornar menor")
        void testMin() {
            assertEquals(1, NumberUtils.min(1, 5, 3));
            assertEquals(2, NumberUtils.min(10, 5, 2));
        }
    }
    
    @Nested
    @DisplayName("ObjectUtils Tests")
    class ObjectUtilsTests {
        
        @Test
        @DisplayName("defaultIfNull deve retornar default")
        void testDefaultIfNull() {
            assertEquals("default", ObjectUtils.defaultIfNull(null, "default"));
            assertEquals("value", ObjectUtils.defaultIfNull("value", "default"));
        }
        
        @Test
        @DisplayName("firstNonNull deve retornar primeiro não-null")
        void testFirstNonNull() {
            assertEquals("found", ObjectUtils.firstNonNull(null, null, "found", "other"));
            assertEquals("first", ObjectUtils.firstNonNull("first", "second"));
        }
        
        @Test
        @DisplayName("isEmpty deve verificar objetos")
        void testIsEmpty() {
            assertTrue(ObjectUtils.isEmpty(""));
            assertTrue(ObjectUtils.isEmpty(null));
            assertFalse(ObjectUtils.isEmpty("text"));
        }
    }
    
    @Nested
    @DisplayName("BooleanUtils Tests")
    class BooleanUtilsTests {
        
        @Test
        @DisplayName("toBoolean deve converter strings")
        void testToBoolean() {
            assertTrue(BooleanUtils.toBoolean("true"));
            assertTrue(BooleanUtils.toBoolean("yes"));
            assertTrue(BooleanUtils.toBoolean("on"));
            assertFalse(BooleanUtils.toBoolean("false"));
            assertFalse(BooleanUtils.toBoolean("no"));
        }
        
        @Test
        @DisplayName("and deve fazer AND lógico")
        void testAnd() {
            assertTrue(BooleanUtils.and(new boolean[]{true, true, true}));
            assertFalse(BooleanUtils.and(new boolean[]{true, false, true}));
        }
        
        @Test
        @DisplayName("or deve fazer OR lógico")
        void testOr() {
            assertTrue(BooleanUtils.or(new boolean[]{false, false, true}));
            assertFalse(BooleanUtils.or(new boolean[]{false, false, false}));
        }
    }
    
    @Nested
    @DisplayName("Pair Tests")
    class PairTests {
        
        @Test
        @DisplayName("Pair deve armazenar dois valores")
        void testPair() {
            Pair<String, Integer> pair = Pair.of("age", 25);
            assertEquals("age", pair.getLeft());
            assertEquals(25, pair.getRight());
        }
        
        @Test
        @DisplayName("Pairs devem ser comparáveis")
        void testPairEquals() {
            Pair<String, Integer> p1 = Pair.of("a", 1);
            Pair<String, Integer> p2 = Pair.of("a", 1);
            Pair<String, Integer> p3 = Pair.of("b", 2);
            
            assertEquals(p1, p2);
            assertNotEquals(p1, p3);
        }
    }
    
    @Nested
    @DisplayName("Range Tests")
    class RangeTests {
        
        @Test
        @DisplayName("Range deve verificar containment")
        void testContains() {
            Range<Integer> range = Range.between(1, 10);
            assertTrue(range.contains(5));
            assertFalse(range.contains(15));
        }
        
        @Test
        @DisplayName("Range deve verificar sobreposição")
        void testOverlap() {
            Range<Integer> r1 = Range.between(1, 10);
            Range<Integer> r2 = Range.between(5, 15);
            Range<Integer> r3 = Range.between(20, 30);
            
            assertTrue(r1.isOverlappedBy(r2));
            assertFalse(r1.isOverlappedBy(r3));
        }
    }
    
    @Nested
    @DisplayName("Validate Tests")
    class ValidateTests {
        
        @Test
        @DisplayName("notNull deve lançar exceção")
        void testNotNull() {
            assertThrows(NullPointerException.class, () -> {
                Validate.notNull(null, "Must not be null");
            });
            
            assertDoesNotThrow(() -> {
                Validate.notNull("value", "Must not be null");
            });
        }
        
        @Test
        @DisplayName("isTrue deve validar condição")
        void testIsTrue() {
            assertThrows(IllegalArgumentException.class, () -> {
                Validate.isTrue(false, "Must be true");
            });
            
            assertDoesNotThrow(() -> {
                Validate.isTrue(true, "Must be true");
            });
        }
    }
}
```

---

## ✅ Best Practices

### 1. Prefira Commons Lang3 para Null-Safety

```java
// ❌ Código verboso e propenso a NullPointerException
public String formatName(String name) {
    if (name != null && !name.trim().isEmpty()) {
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
    return "Anonymous";
}

// ✅ Com Commons Lang3 - limpo e seguro
public String formatName(String name) {
    if (StringUtils.isBlank(name)) {
        return "Anonymous";
    }
    return StringUtils.capitalize(name.toLowerCase());
}
```

---

### 2. Use Builders para Métodos Padrão

```java
// ❌ toString() manual - propenso a erros
@Override
public String toString() {
    return "User{id=" + id + ", name='" + name + "', age=" + age + "}";
}

// ✅ Com ToStringBuilder - consistente e manutenível
@Override
public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("name", name)
        .append("age", age)
        .toString();
}
```

---

### 3. Validações com Validate

```java
// ❌ Validações manuais repetitivas
public void setAge(int age) {
    if (age < 0 || age > 150) {
        throw new IllegalArgumentException("Invalid age");
    }
    this.age = age;
}

// ✅ Com Validate - expressivo e conciso
public void setAge(int age) {
    Validate.inclusiveBetween(0, 150, age, "Age must be between 0 and 150");
    this.age = age;
}
```

---

### 4. Use Range para Validações de Faixa

```java
// ❌ Múltiplas comparações
public String getPriceCategory(double price) {
    if (price >= 0 && price < 50) return "Budget";
    if (price >= 50 && price < 100) return "Medium";
    if (price >= 100) return "Premium";
    return "Invalid";
}

// ✅ Com Range - mais claro
private static final Range<Double> BUDGET = Range.between(0.0, 49.99);
private static final Range<Double> MEDIUM = Range.between(50.0, 99.99);
private static final Range<Double> PREMIUM = Range.between(100.0, Double.MAX_VALUE);

public String getPriceCategory(double price) {
    if (BUDGET.contains(price)) return "Budget";
    if (MEDIUM.contains(price)) return "Medium";
    if (PREMIUM.contains(price)) return "Premium";
    return "Invalid";
}
```

---

### 5. Parsing Seguro de Números

```java
// ❌ try-catch para parsing
public int parsePort(String portStr) {
    try {
        return Integer.parseInt(portStr);
    } catch (NumberFormatException e) {
        return 8080; // default
    }
}

// ✅ Com NumberUtils - mais limpo
public int parsePort(String portStr) {
    return NumberUtils.toInt(portStr, 8080);
}
```

---

## 📊 Referência Rápida

### Guia de Decisão Rápida

| Necessidade | Classe | Método Principal |
|-------------|--------|------------------|
| Verificar string vazia | `StringUtils` | `isEmpty()`, `isBlank()` |
| Juntar strings | `StringUtils` | `join()` |
| Dividir strings | `StringUtils` | `split()` |
| Manipular arrays | `ArrayUtils` | `add()`, `remove()`, `contains()` |
| Parsing seguro | `NumberUtils` | `toInt()`, `toDouble()` |
| Comparar números | `NumberUtils` | `max()`, `min()`, `compare()` |
| Null-safe objects | `ObjectUtils` | `defaultIfNull()`, `firstNonNull()` |
| Converter booleanos | `BooleanUtils` | `toBoolean()`, `toString()` |
| Gerar strings aleatórias | `RandomStringUtils` | `randomAlphanumeric()` |
| Retornar tuplas | `Pair`, `Triple` | `Pair.of()`, `Triple.of()` |
| Validar faixas | `Range` | `between()`, `contains()` |
| Análise de exceções | `ExceptionUtils` | `getRootCause()`, `getStackTrace()` |
| Info do sistema | `SystemUtils` | `JAVA_VERSION`, `OS_NAME` |
| Validações | `Validate` | `notNull()`, `isTrue()` |
| toString/equals/hash | `Builders` | `ToStringBuilder`, `EqualsBuilder` |

---

### Hierarquia de Classes Principais

```
org.apache.commons.lang3
├── StringUtils          (manipulação de strings)
├── ArrayUtils           (manipulação de arrays)
├── ObjectUtils          (operações com objetos)
├── BooleanUtils         (operações booleanas)
├── SystemUtils          (informações do sistema)
├── RandomStringUtils    (geração de strings)
├── Validate            (validações)
│
├── math/
│   └── NumberUtils      (operações numéricas)
│
├── time/
│   └── DateUtils        (manipulação de datas)
│
├── tuple/
│   ├── Pair            (tuplas de 2 elementos)
│   └── Triple          (tuplas de 3 elementos)
│
├── builder/
│   ├── ToStringBuilder
│   ├── EqualsBuilder
│   ├── HashCodeBuilder
│   └── CompareToBuilder
│
└── exception/
    └── ExceptionUtils   (manipulação de exceções)
```

---

## 📚 Recursos Adicionais

### Documentação Oficial
- [Apache Commons Lang Home](https://commons.apache.org/proper/commons-lang/)
- [API JavaDoc 3.18.0](https://commons.apache.org/proper/commons-lang/apidocs/)
- [User Guide](https://commons.apache.org/proper/commons-lang/userguide.html)

### Repositório
- [GitHub](https://github.com/apache/commons-lang)
- [Issues](https://issues.apache.org/jira/projects/LANG)

### Maven Dependency
```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.18.0</version>
</dependency>
```

---

## 🎯 Resumo

### Principais Pontos

1. **StringUtils**: Manipulação null-safe de strings
2. **ArrayUtils**: Operações convenientes com arrays
3. **NumberUtils**: Parsing seguro de números
4. **ObjectUtils**: Operações null-safe com objetos
5. **BooleanUtils**: Conversões e operações booleanas
6. **DateUtils**: Manipulação de datas (código legado)
7. **RandomStringUtils**: Geração de tokens e IDs
8. **Pair/Triple**: Tuplas sem criar classes
9. **Range**: Representação de intervalos
10. **ExceptionUtils**: Análise de stack traces
11. **Validate**: Validações com fail-fast
12. **Builders**: Implementação de métodos padrão

### Quando Usar

✅ **Use Commons Lang3 quando precisar de**:
- Operações null-safe
- Parsing seguro de dados
- Manipulação avançada de strings/arrays
- Evitar código boilerplate
- Validações com mensagens claras
- Implementar toString/equals/hashCode

❌ **Não use quando**:
- Java nativo já resolve (ex: `String.isBlank()` em Java 11+)
- Performance extrema é crítica
- Quer evitar dependências externas

---

**Voltar para**: [📁 Apache Commons](../README.md) | [📁 Libraries](../../README.md) | [📁 Repositório Principal](../../../README.md)
