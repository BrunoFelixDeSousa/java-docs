# 🔧 Apache Commons

Biblioteca de utilitários para Java que complementa a biblioteca padrão com funcionalidades essenciais.

> "Apache Commons: The Java Standard Library's Missing Pieces"

---

## 📋 Índice

- [Collections4](#-collections4)
- [Lang3](#-lang3)
- [Validator](#-validator)
- [Como Usar](#-como-usar)

---

## 📦 Collections4

📄 **[collections4.md](./collections4.md)**

Estruturas de dados avançadas e utilitários para coleções.

### Principais Features

#### 1. **Bag (Multiset)**
Coleção que permite duplicatas e conta ocorrências:

```java
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;

Bag<String> bag = new HashBag<>();
bag.add("apple", 3);
bag.add("orange", 2);
bag.add("apple"); // 4 apples agora

System.out.println(bag.getCount("apple")); // 4
System.out.println(bag.size()); // 7 (total de elementos)
System.out.println(bag.uniqueSet().size()); // 2 (elementos únicos)
```

---

#### 2. **BidiMap**
Mapa bidirecional (chave ↔ valor):

```java
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

BidiMap<String, String> map = new DualHashBidiMap<>();
map.put("BR", "Brasil");
map.put("US", "Estados Unidos");

System.out.println(map.get("BR")); // "Brasil"
System.out.println(map.getKey("Brasil")); // "BR"

BidiMap<String, String> inverso = map.inverseBidiMap();
System.out.println(inverso.get("Brasil")); // "BR"
```

---

#### 3. **MultiValuedMap**
Mapa com múltiplos valores por chave:

```java
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

MultiValuedMap<String, String> map = new ArrayListValuedHashMap<>();
map.put("frutas", "maçã");
map.put("frutas", "banana");
map.put("frutas", "laranja");

Collection<String> frutas = map.get("frutas");
System.out.println(frutas); // [maçã, banana, laranja]
```

---

#### 4. **CollectionUtils**
Utilitários para operações com coleções:

```java
import org.apache.commons.collections4.CollectionUtils;

List<Integer> lista1 = Arrays.asList(1, 2, 3, 4, 5);
List<Integer> lista2 = Arrays.asList(4, 5, 6, 7, 8);

// União
Collection<Integer> union = CollectionUtils.union(lista1, lista2);
// [1, 2, 3, 4, 5, 6, 7, 8]

// Interseção
Collection<Integer> intersection = CollectionUtils.intersection(lista1, lista2);
// [4, 5]

// Diferença
Collection<Integer> difference = CollectionUtils.subtract(lista1, lista2);
// [1, 2, 3]

// Verificar vazio
boolean isEmpty = CollectionUtils.isEmpty(lista1); // false
boolean isNotEmpty = CollectionUtils.isNotEmpty(lista1); // true
```

---

#### 5. **ListUtils**
Operações específicas para listas:

```java
import org.apache.commons.collections4.ListUtils;

List<Integer> lista = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

// Particionar lista
List<List<Integer>> partitions = ListUtils.partition(lista, 3);
// [[1, 2, 3], [4, 5, 6], [7, 8, 9]]

// Lista vazia segura
List<String> empty = ListUtils.emptyIfNull(null); // []
```

---

#### 6. **MapUtils**
Utilitários para mapas:

```java
import org.apache.commons.collections4.MapUtils;

Map<String, Integer> map = new HashMap<>();
map.put("a", 1);
map.put("b", 2);

// Verificar vazio
boolean isEmpty = MapUtils.isEmpty(map); // false

// Obter com valor padrão
Integer value = MapUtils.getInteger(map, "c", 0); // 0

// Debug/Print
MapUtils.debugPrint(System.out, "Mapa", map);
```

---

## 🛠️ Lang3

📄 **[lang3.md](./lang3.md)**

Utilitários para tipos primitivos, strings, arrays e mais.

### Principais Features

#### 1. **StringUtils**
Operações avançadas com strings:

```java
import org.apache.commons.lang3.StringUtils;

// Verificar vazio/branco
StringUtils.isEmpty("")  // true
StringUtils.isBlank("  ")  // true
StringUtils.isNotEmpty("abc")  // true

// Capitalizar
StringUtils.capitalize("hello")  // "Hello"
StringUtils.upperCase("hello")  // "HELLO"

// Remover espaços
StringUtils.trim("  hello  ")  // "hello"
StringUtils.strip("  hello  ")  // "hello"

// Truncar
StringUtils.abbreviate("Hello World", 8)  // "Hello..."

// Reverter
StringUtils.reverse("abc")  // "cba"

// Verificar conteúdo
StringUtils.contains("abc", "b")  // true
StringUtils.startsWith("abc", "a")  // true
StringUtils.endsWith("abc", "c")  // true

// Substituir
StringUtils.replace("aba", "a", "z")  // "zbz"

// Repetir
StringUtils.repeat("ab", 3)  // "ababab"
```

---

#### 2. **ArrayUtils**
Operações com arrays:

```java
import org.apache.commons.lang3.ArrayUtils;

int[] array = {1, 2, 3};

// Adicionar elemento
int[] novo = ArrayUtils.add(array, 4); // {1, 2, 3, 4}

// Remover elemento
int[] removido = ArrayUtils.remove(array, 1); // {1, 3}

// Reverter
ArrayUtils.reverse(array); // {3, 2, 1}

// Verificar contém
boolean contains = ArrayUtils.contains(array, 2); // true

// Array vazio
int[] empty = ArrayUtils.EMPTY_INT_ARRAY;
```

---

#### 3. **ObjectUtils**
Operações com objetos:

```java
import org.apache.commons.lang3.ObjectUtils;

// Primeiro não nulo
String result = ObjectUtils.firstNonNull(null, null, "valor"); // "valor"

// Valor padrão se nulo
String value = ObjectUtils.defaultIfNull(null, "padrão"); // "padrão"

// Comparação segura
int cmp = ObjectUtils.compare("a", "b"); // -1

// Clone
MyObject clone = ObjectUtils.clone(original);
```

---

#### 4. **NumberUtils**
Operações com números:

```java
import org.apache.commons.lang3.math.NumberUtils;

// Parse seguro (retorna 0 se falhar)
int num = NumberUtils.toInt("123"); // 123
int zero = NumberUtils.toInt("abc"); // 0

// Parse com padrão
int padrao = NumberUtils.toInt("abc", -1); // -1

// Verificar número
boolean isNumber = NumberUtils.isCreatable("123"); // true

// Máximo/Mínimo
int max = NumberUtils.max(1, 5, 3); // 5
int min = NumberUtils.min(1, 5, 3); // 1
```

---

#### 5. **DateUtils**
Operações com datas (legacy - use java.time para novos projetos):

```java
import org.apache.commons.lang3.time.DateUtils;

Date hoje = new Date();

// Adicionar dias
Date amanha = DateUtils.addDays(hoje, 1);

// Truncar para início do dia
Date inicioDia = DateUtils.truncate(hoje, Calendar.DATE);

// Verificar mesmo dia
boolean mesoDia = DateUtils.isSameDay(hoje, amanha); // false
```

---

#### 6. **RandomStringUtils**
Gerar strings aleatórias:

```java
import org.apache.commons.lang3.RandomStringUtils;

// Alfanumérico
String random = RandomStringUtils.randomAlphanumeric(10); // "a3B9xZ1mK4"

// Apenas letras
String letters = RandomStringUtils.randomAlphabetic(5); // "aBcDe"

// Apenas números
String numbers = RandomStringUtils.randomNumeric(6); // "123456"

// ASCII imprimível
String ascii = RandomStringUtils.randomAscii(8);
```

---

#### 7. **SystemUtils**
Informações do sistema:

```java
import org.apache.commons.lang3.SystemUtils;

// Verificar OS
boolean isWindows = SystemUtils.IS_OS_WINDOWS;
boolean isLinux = SystemUtils.IS_OS_LINUX;
boolean isMac = SystemUtils.IS_OS_MAC;

// Versão Java
String javaVersion = SystemUtils.JAVA_VERSION; // "17.0.1"
boolean isJava17 = SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_17);

// Diretórios
File userHome = SystemUtils.getUserHome();
File temp = SystemUtils.getJavaIoTmpDir();
```

---

## ✅ Validator

📄 **[validator.md](./validator.md)**

Validação de dados comuns (CPF, CNPJ, Email, etc.).

### Principais Validators

#### 1. **EmailValidator**
```java
import org.apache.commons.validator.routines.EmailValidator;

EmailValidator validator = EmailValidator.getInstance();

boolean valido = validator.isValid("user@example.com"); // true
boolean invalido = validator.isValid("invalid-email"); // false
```

---

#### 2. **UrlValidator**
```java
import org.apache.commons.validator.routines.UrlValidator;

String[] schemes = {"http", "https"};
UrlValidator validator = new UrlValidator(schemes);

boolean valido = validator.isValid("https://example.com"); // true
boolean invalido = validator.isValid("ftp://example.com"); // false
```

---

#### 3. **DateValidator**
```java
import org.apache.commons.validator.routines.DateValidator;

DateValidator validator = DateValidator.getInstance();

Date date = validator.validate("2024-01-15", "yyyy-MM-dd");
boolean valido = validator.isValid("2024-01-15", "yyyy-MM-dd"); // true
```

---

#### 4. **CreditCardValidator**
```java
import org.apache.commons.validator.routines.CreditCardValidator;

CreditCardValidator validator = new CreditCardValidator();

boolean visa = validator.isValid("4532015112830366"); // Visa
boolean mastercard = validator.isValid("5425233430109903"); // Mastercard
```

---

#### 5. **IBANValidator** (para contas bancárias internacionais)
```java
import org.apache.commons.validator.routines.IBANValidator;

IBANValidator validator = IBANValidator.getInstance();

boolean valido = validator.isValid("DE89370400440532013000"); // true
```

---

## 📦 Como Usar

### Maven
```xml
<dependencies>
    <!-- Collections4 -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-collections4</artifactId>
        <version>4.4</version>
    </dependency>
    
    <!-- Lang3 -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
        <version>3.14.0</version>
    </dependency>
    
    <!-- Validator -->
    <dependency>
        <groupId>commons-validator</groupId>
        <artifactId>commons-validator</artifactId>
        <version>1.8.0</version>
    </dependency>
</dependencies>
```

### Gradle
```gradle
dependencies {
    implementation 'org.apache.commons:commons-collections4:4.4'
    implementation 'org.apache.commons:commons-lang3:3.14.0'
    implementation 'commons-validator:commons-validator:1.8.0'
}
```

---

## 🎯 Quando Usar

| Biblioteca | Use Para |
|------------|----------|
| **Collections4** | Estruturas de dados avançadas (Bag, BidiMap, MultiMap) |
| **Lang3** | Utilitários strings, arrays, números, objetos |
| **Validator** | Validação de email, URL, cartão de crédito, datas |

---

## 📚 Resumo Rápido

**Collections4**:
- `Bag` - Conta ocorrências
- `BidiMap` - Mapa bidirecional
- `MultiValuedMap` - Múltiplos valores por chave
- `CollectionUtils` - Operações com coleções

**Lang3**:
- `StringUtils` - Manipulação de strings
- `ArrayUtils` - Operações com arrays
- `ObjectUtils` - Utilitários para objetos
- `NumberUtils` - Parse e operações numéricas
- `RandomStringUtils` - Strings aleatórias

**Validator**:
- `EmailValidator` - Validar emails
- `UrlValidator` - Validar URLs
- `CreditCardValidator` - Validar cartões
- `DateValidator` - Validar datas

### 3. **Apache Commons CSV** - Processamento CSV
```java
// Para data processing e reports
CSVFormat.DEFAULT.parse(reader);
CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
```

### 4. **Apache Commons Math** - Matemática e Estatística
```java
// Para análises e cálculos complexos
DescriptiveStatistics stats = new DescriptiveStatistics();
RandomDataGenerator random = new RandomDataGenerator();
```

### 5. **Apache Commons Validator** - Validações Avançadas
```java
// Para validation enterprise
EmailValidator.getInstance().isValid(email);
UrlValidator.getInstance().isValid(url);
CreditCardValidator.getInstance().isValid(cardNumber);
```

### 6. **Apache Commons Configuration** - Gerenciamento de Configurações
```java
// Para configuration management
PropertiesConfiguration config = new PropertiesConfiguration("app.properties");
XMLConfiguration xmlConfig = new XMLConfiguration("config.xml");
```

### 7. **Apache Commons Pool** - Object Pooling
```java
// Para performance e resource management
GenericObjectPool<MyObject> pool = new GenericObjectPool<>(factory);
```

### 8. **Apache Commons DBCP** - Database Connection Pooling
```java
// Para database performance
BasicDataSource dataSource = new BasicDataSource();
dataSource.setUrl("jdbc:mysql://localhost/test");
```

### 9. **Apache Commons Email** - Envio de Emails
```java
// Para notification systems
HtmlEmail email = new HtmlEmail();
email.setHostName("mail.myserver.com");
email.send();
```

### 10. **Apache Commons Compress** - Compressão de Arquivos
```java
// Para file processing e storage optimization
TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
ZipFile zipFile = new ZipFile(file);
```

## 🎯 Recomendação Prioritária

Para sua documentação enterprise, sugiro adicionar na seguinte ordem de prioridade:

### **Prioridade ALTA** (Essenciais)
1. **Commons IO** - Fundamental para qualquer aplicação que trabalha com arquivos
2. **Commons Codec** - Essencial para security e encoding
3. **Commons Validator** - Complementa perfeitamente suas validation patterns

### **Prioridade MÉDIA** (Muito Úteis)
4. **Commons CSV** - Para data processing e reports
5. **Commons Configuration** - Para configuration management enterprise
6. **Commons Email** - Para notification systems

### **Prioridade BAIXA** (Casos Específicos)
7. **Commons Math** - Para aplicações com cálculos complexos
8. **Commons Pool/DBCP** - Para otimizações de performance
9. **Commons Compress** - Para processamento de arquivos

## 📋 Template Sugerido para Cada Nova Biblioteca

Seguindo o padrão das suas documentações existentes:

````markdown
# Apache Commons [Nome] - Documentação Completa

## 📚 Índice
1. [Introdução e Conceitos](#introdução)
2. [Instalação e Setup](#instalação)
3. [Conceitos Fundamentais](#conceitos)
4. [Operações Básicas](#básicas)
5. [Operações Avançadas](#avançadas)
6. [Casos de Uso Enterprise](#enterprise)
7. [Performance e Benchmarks](#performance)
8. [Melhores Práticas](#práticas)
9. [Exemplos Práticos](#exemplos)

## 🎯 Introdução

[Explicação conceitual com analogia do mundo real]

### Arquitetura Visual
```
[Diagrama da estrutura da biblioteca]
```

## ⚙️ Instalação e Setup

### Maven
```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-[nome]</artifactId>
    <version>[versão]</version>
</dependency>
```

[Continua seguindo o padrão das outras documentações...]
````

## 💡 Estrutura de Organização Sugerida

```
package-apache-commons/
├── collections4.md         ✅ (já existe)
├── lang3.md               ✅ (já existe)
├── io.md                  📝 (sugerido - prioridade alta)
├── codec.md               📝 (sugerido - prioridade alta)
├── validator.md           📝 (sugerido - prioridade alta)
├── csv.md                 📝 (sugerido - prioridade média)
├── configuration.md       📝 (sugerido - prioridade média)
├── email.md               📝 (sugerido - prioridade média)
├── math.md                📝 (sugerido - prioridade baixa)
├── pool.md                📝 (sugerido - casos específicos)
├── dbcp.md                📝 (sugerido - casos específicos)
└── compress.md            📝 (sugerido - casos específicos)
```