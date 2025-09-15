# Apache Commons Lang3 - Documentação Completa

## 📚 Índice
1. [Introdução](#introdução)
2. [Instalação e Setup](#instalação-e-setup)
3. [Conceitos Fundamentais](#conceitos-fundamentais)
4. [StringUtils - Manipulação de Strings](#stringutils)
5. [ArrayUtils - Manipulação de Arrays](#arrayutils)
6. [NumberUtils - Manipulação de Números](#numberutils)
7. [DateUtils - Manipulação de Datas](#dateutils)
8. [ObjectUtils - Manipulação de Objetos](#objectutils)
9. [SystemUtils - Informações do Sistema](#systemutils)
10. [Validate - Validações](#validate)
11. [Builders - Padrões de Construção](#builders)
12. [Exemplos Práticos](#exemplos-práticos)
13. [Melhores Práticas](#melhores-práticas)

---

## 🎯 Introdução

### O que é o Apache Commons Lang3?

Imagine que você está cozinhando e precisa cortar cebolas. Você poderia usar uma faca comum, mas existe um cortador de cebolas específico que faz o trabalho mais rápido e preciso. O Commons Lang3 é exatamente isso para programação Java - um conjunto de ferramentas especializadas que facilitam tarefas comuns.

**Definição Técnica**: Apache Commons Lang3 é uma biblioteca que estende as funcionalidades básicas do Java, fornecendo utilitários para manipulação de strings, arrays, números, datas e objetos de forma mais eficiente e segura.

### Por que usar?

```java
// ❌ Sem Commons Lang3 - código verboso e propenso a erros
public boolean isStringValid(String str) {
    return str != null && !str.trim().isEmpty();
}

// ✅ Com Commons Lang3 - simples e claro
public boolean isStringValid(String str) {
    return StringUtils.isNotBlank(str);
}
```

### Arquitetura Visual

```
┌─────────────────────────────────────────┐
│           Apache Commons Lang3           │
├─────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐      │
│  │ StringUtils │  │ ArrayUtils  │      │
│  └─────────────┘  └─────────────┘      │
│  ┌─────────────┐  ┌─────────────┐      │
│  │ NumberUtils │  │ DateUtils   │      │
│  └─────────────┘  └─────────────┘      │
│  ┌─────────────┐  ┌─────────────┐      │
│  │ ObjectUtils │  │ SystemUtils │      │
│  └─────────────┘  └─────────────┘      │
│  ┌─────────────┐  ┌─────────────┐      │
│  │  Validate   │  │  Builders   │      │
│  └─────────────┘  └─────────────┘      │
└─────────────────────────────────────────┘
```

---

## ⚙️ Instalação e Setup

### Maven
```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.13.0</version>
</dependency>
```

### Gradle
```gradle
implementation 'org.apache.commons:commons-lang3:3.13.0'
```

### Teste de Instalação
```java
import org.apache.commons.lang3.StringUtils;

public class TestInstallation {
    public static void main(String[] args) {
        System.out.println(StringUtils.capitalize("hello world"));
        // Output: "Hello world"
    }
}
```

---

## 🧠 Conceitos Fundamentais

### 1. Classes Utilitárias Estáticas
Todas as classes principais do Commons Lang3 são **utilitárias estáticas**, ou seja:
- Não precisam ser instanciadas
- Todos os métodos são `static`
- São thread-safe por natureza

```java
// ❌ Não faça isso - desnecessário
StringUtils stringUtils = new StringUtils(); // Erro de compilação!

// ✅ Faça assim
String result = StringUtils.capitalize("hello");
```

### 2. Null-Safe (Seguro contra Null)
A biblioteca foi projetada para lidar graciosamente com valores null:

```java
String texto = null;
// ❌ Java padrão - NullPointerException
int length = texto.length(); // 💥 Erro!

// ✅ Commons Lang3 - seguro
int length = StringUtils.length(texto); // Retorna 0
```

### 3. Padrão de Nomenclatura
```java
isEmpty()     // Verifica se está vazio
isNotEmpty()  // Verifica se NÃO está vazio
isBlank()     // Verifica se está vazio ou só espaços
isNotBlank()  // Verifica se NÃO está vazio nem só espaços
```

---

## 🔤 StringUtils

### Conceito Base
StringUtils é como uma "canivete suíço" para strings - tem uma ferramenta para cada situação comum.

### Métodos Fundamentais

#### 1. Verificações Básicas
```java
// isEmpty vs isBlank - diferença crucial!
StringUtils.isEmpty("")        // true
StringUtils.isEmpty("   ")     // false (espaços não são vazio)
StringUtils.isEmpty(null)      // true

StringUtils.isBlank("")        // true
StringUtils.isBlank("   ")     // true (espaços SÃO considerados em branco)
StringUtils.isBlank(null)      // true
StringUtils.isBlank("abc")     // false
```

#### 2. Limpeza e Formatação
```java
// Remoção de espaços
StringUtils.trim("  hello  ")           // "hello"
StringUtils.trimToEmpty("  hello  ")   // "hello"
StringUtils.trimToEmpty(null)          // ""

// Capitalização
StringUtils.capitalize("hello world")   // "Hello world"
StringUtils.uncapitalize("Hello World") // "hello World"

// Repetição
StringUtils.repeat("abc", 3)            // "abcabcabc"
StringUtils.repeat("-", 10)             // "----------"
```

#### 3. Busca e Substituição
```java
// Busca
StringUtils.contains("hello world", "world")     // true
StringUtils.containsIgnoreCase("HELLO", "hello") // true
StringUtils.startsWith("hello", "he")            // true
StringUtils.endsWith("world", "ld")              // true

// Substituição
StringUtils.replace("hello world", "world", "java")     // "hello java"
StringUtils.replaceOnce("aa bb aa", "aa", "cc")        // "cc bb aa"
```

#### 4. Divisão e Junção
```java
// Divisão inteligente (null-safe)
StringUtils.split("a,b,c", ",")           // ["a", "b", "c"]
StringUtils.split("a,,b", ",")            // ["a", "b"] (ignora vazios)
StringUtils.split(null, ",")              // null

// Junção
StringUtils.join(new String[]{"a", "b", "c"}, "-")  // "a-b-c"
StringUtils.join(Arrays.asList("x", "y"), " | ")    // "x | y"
```

### Exemplo Prático Completo
```java
public class ProcessadorTexto {
    public String limparFormatarTexto(String input) {
        // 1. Verificação de entrada
        if (StringUtils.isBlank(input)) {
            return StringUtils.EMPTY;
        }
        
        // 2. Limpeza
        String cleaned = StringUtils.trim(input);
        
        // 3. Formatação
        cleaned = StringUtils.capitalize(cleaned.toLowerCase());
        
        // 4. Remoção de múltiplos espaços
        cleaned = StringUtils.normalizeSpace(cleaned);
        
        return cleaned;
    }
}

// Teste
ProcessadorTexto processor = new ProcessadorTexto();
System.out.println(processor.limparFormatarTexto("  HELLO    WORLD  "));
// Output: "Hello world"
```

---

## 🔢 ArrayUtils

### Conceito Base
ArrayUtils é como ter um assistente especializado em organizar e manipular listas de itens.

### Operações Fundamentais

#### 1. Verificações
```java
// Verificação de array vazio/null
ArrayUtils.isEmpty(new int[0])      // true
ArrayUtils.isEmpty(null)            // true
ArrayUtils.isNotEmpty(new int[]{1}) // true

// Tamanho seguro
ArrayUtils.getLength(null)          // 0
ArrayUtils.getLength(new int[]{1,2}) // 2
```

#### 2. Busca
```java
int[] numeros = {1, 2, 3, 4, 5};

// Busca de elementos
ArrayUtils.contains(numeros, 3)     // true
ArrayUtils.indexOf(numeros, 3)      // 2 (índice)
ArrayUtils.lastIndexOf(numeros, 3)  // 2
```

#### 3. Manipulação
```java
// Adição (arrays são imutáveis em Java!)
int[] original = {1, 2, 3};
int[] novo = ArrayUtils.add(original, 4);        // {1, 2, 3, 4}
int[] novo2 = ArrayUtils.addAll(original, 4, 5); // {1, 2, 3, 4, 5}

// Remoção
int[] removido = ArrayUtils.remove(original, 1); // {1, 3} (remove índice 1)
int[] semValor = ArrayUtils.removeElement(original, 2); // {1, 3} (remove valor 2)

// Inversão
int[] invertido = ArrayUtils.reverse(new int[]{1, 2, 3}); // Modifica o array original!
```

#### 4. Conversão
```java
// Primitivos para Objects e vice-versa
Integer[] objects = ArrayUtils.toObject(new int[]{1, 2, 3});
int[] primitives = ArrayUtils.toPrimitive(new Integer[]{1, 2, 3});

// Array para String
String resultado = ArrayUtils.toString(new int[]{1, 2, 3}); // "{1,2,3}"
```

### Exemplo Prático: Sistema de Notas
```java
public class GerenciadorNotas {
    private double[] notas = new double[0];
    
    public void adicionarNota(double nota) {
        notas = ArrayUtils.add(notas, nota);
    }
    
    public boolean removerNota(double nota) {
        if (ArrayUtils.contains(notas, nota)) {
            notas = ArrayUtils.removeElement(notas, nota);
            return true;
        }
        return false;
    }
    
    public double calcularMedia() {
        if (ArrayUtils.isEmpty(notas)) {
            return 0.0;
        }
        
        double soma = 0;
        for (double nota : notas) {
            soma += nota;
        }
        return soma / notas.length;
    }
    
    public String getResumo() {
        return String.format("Notas: %s, Média: %.2f", 
                           ArrayUtils.toString(notas), 
                           calcularMedia());
    }
}
```

---

## 🔢 NumberUtils

### Conceito Base
NumberUtils é como ter uma calculadora inteligente que nunca gera erros de conversão.

### Conversões Seguras
```java
// Conversões que não falham nunca
NumberUtils.toInt("123")        // 123
NumberUtils.toInt("abc")        // 0 (valor padrão)
NumberUtils.toInt("abc", -1)    // -1 (valor padrão personalizado)
NumberUtils.toInt(null)         // 0

// Para outros tipos
NumberUtils.toDouble("123.45")  // 123.45
NumberUtils.toLong("123")       // 123L
NumberUtils.toFloat("123.45")   // 123.45f
```

### Verificações
```java
// Verificar se é número
NumberUtils.isCreatable("123")      // true
NumberUtils.isCreatable("123.45")   // true
NumberUtils.isCreatable("abc")      // false
NumberUtils.isCreatable("")         // false

// Verificações específicas
NumberUtils.isParsable("123")       // true (mais restritivo que isCreatable)
```

### Operações Matemáticas
```java
// Máximo e mínimo
NumberUtils.max(1, 2, 3)           // 3
NumberUtils.min(1, 2, 3)           // 1
NumberUtils.max(new int[]{5,2,8})  // 8

// Comparações seguras
NumberUtils.compare(1.0, 2.0)      // -1 (menor)
NumberUtils.compare(2.0, 2.0)      // 0 (igual)
NumberUtils.compare(3.0, 2.0)      // 1 (maior)
```

### Exemplo Prático: Calculadora Segura
```java
public class CalculadoraSegura {
    
    public double somar(String a, String b) {
        double numA = NumberUtils.toDouble(a, 0.0);
        double numB = NumberUtils.toDouble(b, 0.0);
        return numA + numB;
    }
    
    public String calcularMedia(String[] valores) {
        if (ArrayUtils.isEmpty(valores)) {
            return "0";
        }
        
        double soma = 0;
        int validos = 0;
        
        for (String valor : valores) {
            if (NumberUtils.isCreatable(valor)) {
                soma += NumberUtils.toDouble(valor);
                validos++;
            }
        }
        
        if (validos == 0) {
            return "0";
        }
        
        return String.format("%.2f", soma / validos);
    }
    
    public int encontrarMaior(String[] numeros) {
        int[] valores = new int[0];
        
        for (String num : numeros) {
            if (NumberUtils.isParsable(num)) {
                valores = ArrayUtils.add(valores, NumberUtils.toInt(num));
            }
        }
        
        return ArrayUtils.isEmpty(valores) ? 0 : NumberUtils.max(valores);
    }
}
```

---

## 📅 DateUtils

### Conceito Base
DateUtils é como ter um assistente pessoal especializado em gerenciar tempo e datas.

> **Nota**: DateUtils trabalha com `java.util.Date`. Para projetos novos, considere usar `java.time` (Java 8+).

### Operações Básicas
```java
Date agora = new Date();

// Adicionar/subtrair tempo
Date amanha = DateUtils.addDays(agora, 1);
Date semanaPassada = DateUtils.addWeeks(agora, -1);
Date proximoMes = DateUtils.addMonths(agora, 1);
Date proximoAno = DateUtils.addYears(agora, 1);

// Truncar (zerar partes da data)
Date inicioHoje = DateUtils.truncate(agora, Calendar.DAY_OF_MONTH);     // 00:00:00
Date inicioMes = DateUtils.truncate(agora, Calendar.MONTH);             // 1º dia do mês às 00:00:00
Date inicioAno = DateUtils.truncate(agora, Calendar.YEAR);              // 1º de janeiro às 00:00:00
```

### Comparações e Verificações
```java
Date data1 = new Date();
Date data2 = DateUtils.addDays(data1, 1);

// Verificar se é o mesmo dia (ignora horário)
boolean mesmodia = DateUtils.isSameDay(data1, data2);        // false

// Verificar se é mesmo instant
boolean mesmoInstante = DateUtils.isSameInstant(data1, data2); // false
```

### Parsing Flexível
```java
String[] formatosAceitos = {
    "yyyy-MM-dd",
    "dd/MM/yyyy",
    "MM-dd-yyyy",
    "yyyy/MM/dd HH:mm:ss"
};

// Tenta todos os formatos até encontrar um que funcione
Date data = DateUtils.parseDate("2023-12-25", formatosAceitos);
```

### Exemplo Prático: Sistema de Agendamento
```java
public class SistemaAgendamento {
    private static final String[] FORMATOS_DATA = {
        "yyyy-MM-dd",
        "dd/MM/yyyy",
        "dd-MM-yyyy"
    };
    
    public Date agendarReuniao(String dataTexto, int horasDepois) {
        try {
            Date data = DateUtils.parseDate(dataTexto, FORMATOS_DATA);
            
            // Define para início do dia
            data = DateUtils.truncate(data, Calendar.DAY_OF_MONTH);
            
            // Adiciona as horas
            return DateUtils.addHours(data, horasDepois);
            
        } catch (ParseException e) {
            throw new IllegalArgumentException("Data inválida: " + dataTexto);
        }
    }
    
    public boolean isHorarioComercial(Date data) {
        Date inicioComercial = DateUtils.setHours(
            DateUtils.truncate(data, Calendar.DAY_OF_MONTH), 9);
        Date fimComercial = DateUtils.setHours(
            DateUtils.truncate(data, Calendar.DAY_OF_MONTH), 18);
            
        return data.compareTo(inicioComercial) >= 0 && 
               data.compareTo(fimComercial) < 0;
    }
    
    public List<Date> proximasSemanas(int quantidade) {
        List<Date> semanas = new ArrayList<>();
        Date atual = DateUtils.truncate(new Date(), Calendar.WEEK_OF_YEAR);
        
        for (int i = 0; i < quantidade; i++) {
            semanas.add(DateUtils.addWeeks(atual, i));
        }
        
        return semanas;
    }
}
```

---

## 🎯 ObjectUtils

### Conceito Base
ObjectUtils é como ter um especialista em verificar se objetos estão "bem comportados".

### Verificações de Null
```java
// Verificação simples
ObjectUtils.isEmpty(null)           // true
ObjectUtils.isEmpty("")             // true
ObjectUtils.isEmpty(new Object())   // false
ObjectUtils.isNotEmpty("hello")     // true

// Múltiplas verificações
ObjectUtils.anyNull("a", null, "c") // true (pelo menos um é null)
ObjectUtils.allNull(null, null)     // true (todos são null)
ObjectUtils.allNotNull("a", "b")    // true (nenhum é null)
```

### Valores Padrão
```java
// Retorna primeiro valor não-null
String resultado = ObjectUtils.firstNonNull(null, "", "default"); // ""
String resultado2 = ObjectUtils.firstNonNull(null, null, "ok");   // "ok"

// Valor padrão se for null
String valor = ObjectUtils.defaultIfNull(null, "padrão");  // "padrão"
String valor2 = ObjectUtils.defaultIfNull("existe", "padrão"); // "existe"
```

### Comparações Seguras
```java
// Compare que funciona com nulls
ObjectUtils.equals(null, null)      // true
ObjectUtils.equals("a", "a")        // true
ObjectUtils.equals("a", null)       // false
ObjectUtils.notEqual("a", "b")      // true

// Comparação para ordenação
ObjectUtils.compare(null, "a")      // -1 (null é menor)
ObjectUtils.compare("a", "b")       // -1 (a < b)
```

### Exemplo Prático: Validador de Objeto
```java
public class ValidadorUsuario {
    
    public boolean isUsuarioValido(Usuario usuario) {
        // Verifica se objeto e campos obrigatórios não são null
        return ObjectUtils.allNotNull(
            usuario,
            usuario.getNome(),
            usuario.getEmail()
        );
    }
    
    public String getNomeExibicao(Usuario usuario) {
        if (usuario == null) {
            return "Usuário Desconhecido";
        }
        
        // Usa nome completo, ou nome, ou email como fallback
        return ObjectUtils.firstNonNull(
            usuario.getNomeCompleto(),
            usuario.getNome(),
            usuario.getEmail(),
            "Sem Nome"
        );
    }
    
    public boolean usuariosIguais(Usuario u1, Usuario u2) {
        // Comparação segura considerando nulls
        return ObjectUtils.equals(u1, u2) ||
               (ObjectUtils.allNotNull(u1, u2) && 
                ObjectUtils.equals(u1.getId(), u2.getId()));
    }
}

class Usuario {
    private String id;
    private String nome;
    private String nomeCompleto;
    private String email;
    
    // getters/setters...
}
```

---

## 💻 SystemUtils

### Conceito Base
SystemUtils é como ter um detetive que descobre tudo sobre o ambiente onde seu programa está rodando.

### Informações do Sistema Operacional
```java
// Sistema operacional
SystemUtils.IS_OS_WINDOWS    // true se for Windows
SystemUtils.IS_OS_LINUX      // true se for Linux
SystemUtils.IS_OS_MAC        // true se for macOS

// Versões específicas
SystemUtils.IS_OS_WINDOWS_10
SystemUtils.IS_OS_WINDOWS_11

// Nome e versão
String osName = SystemUtils.OS_NAME;        // "Windows 10"
String osVersion = SystemUtils.OS_VERSION;  // "10.0"
```

### Informações do Java
```java
// Versão do Java
String javaVersion = SystemUtils.JAVA_VERSION;           // "17.0.1"
float javaVersionFloat = SystemUtils.JAVA_VERSION_FLOAT; // 17.0

// Verificações de versão
boolean isJava8Plus = SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_8);
boolean isJava11Plus = SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_11);

// Diretórios importantes
String javaHome = SystemUtils.JAVA_HOME;              // Pasta de instalação do Java
String javaClassPath = SystemUtils.JAVA_CLASS_PATH;  // Classpath atual
```

### Informações do Usuário
```java
// Usuário atual
String userName = SystemUtils.USER_NAME;        // Nome do usuário logado
String userHome = SystemUtils.USER_HOME;        // Pasta home do usuário
String userDir = SystemUtils.USER_DIR;          // Diretório atual de trabalho

// Diretório temporário
String tempDir = SystemUtils.JAVA_IO_TMPDIR;    // Pasta temp do sistema
```

### Exemplo Prático: Relatório do Sistema
```java
public class RelatorioSistema {
    
    public String gerarRelatorio() {
        StringBuilder relatorio = new StringBuilder();
        
        relatorio.append("=== RELATÓRIO DO SISTEMA ===\n");
        
        // Informações do SO
        relatorio.append("\n[Sistema Operacional]\n");
        relatorio.append("Nome: ").append(SystemUtils.OS_NAME).append("\n");
        relatorio.append("Versão: ").append(SystemUtils.OS_VERSION).append("\n");
        relatorio.append("Arquitetura: ").append(SystemUtils.OS_ARCH).append("\n");
        
        // Informações do Java
        relatorio.append("\n[Java]\n");
        relatorio.append("Versão: ").append(SystemUtils.JAVA_VERSION).append("\n");
        relatorio.append("Home: ").append(SystemUtils.JAVA_HOME).append("\n");
        relatorio.append("Vendor: ").append(SystemUtils.JAVA_VENDOR).append("\n");
        
        // Informações do usuário
        relatorio.append("\n[Usuário]\n");
        relatorio.append("Nome: ").append(SystemUtils.USER_NAME).append("\n");
        relatorio.append("Home: ").append(SystemUtils.USER_HOME).append("\n");
        relatorio.append("Dir Trabalho: ").append(SystemUtils.USER_DIR).append("\n");
        
        return relatorio.toString();
    }
    
    public String recomendarConfiguracoes() {
        List<String> recomendacoes = new ArrayList<>();
        
        if (!SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_11)) {
            recomendacoes.add("⚠️ Considere atualizar para Java 11 ou superior");
        }
        
        if (SystemUtils.IS_OS_WINDOWS && SystemUtils.OS_VERSION.startsWith("6.")) {
            recomendacoes.add("⚠️ Windows muito antigo, considere atualizar");
        }
        
        if (recomendacoes.isEmpty()) {
            return "✅ Sistema adequado para execução";
        }
        
        return StringUtils.join(recomendacoes, "\n");
    }
    
    public File criarArquivoTemporario(String prefixo, String sufixo) throws IOException {
        String tempDir = SystemUtils.JAVA_IO_TMPDIR;
        return File.createTempFile(prefixo, sufixo, new File(tempDir));
    }
}
```

---

## ✅ Validate

### Conceito Base
Validate é como ter um segurança na porta do seu código - verifica se tudo está em ordem antes de prosseguir.

### Validações Básicas
```java
// Validação de null
Validate.notNull(objeto, "Objeto não pode ser null");

// Validação de verdadeiro/falso
Validate.isTrue(idade >= 18, "Idade deve ser maior ou igual a 18");

// Validações de strings
Validate.notEmpty(nome, "Nome não pode estar vazio");
Validate.notBlank(email, "Email não pode estar em branco");

// Validações de arrays/collections
Validate.notEmpty(lista, "Lista não pode estar vazia");
Validate.noNullElements(array, "Array não pode conter elementos null");
```

### Validações Numéricas
```java
// Validações de range
Validate.inclusiveBetween(0, 100, nota, "Nota deve estar entre 0 e 100");
Validate.exclusiveBetween(0, 120, idade, "Idade deve estar entre 1 e 119");

// Validações de índice
Validate.validIndex(array, index, "Índice inválido para o array");
```

### Exemplo Prático: Sistema de Cadastro
```java
public class SistemaCadastro {
    
    public void cadastrarUsuario(String nome, String email, int idade, String[] interesses) {
        // Validações de entrada - falha rápida com mensagens claras
        Validate.notBlank(nome, "Nome é obrigatório");
        Validate.notBlank(email, "Email é obrigatório");
        Validate.isTrue(email.contains("@"), "Email deve conter @");
        Validate.inclusiveBetween(16, 120, idade, "Idade deve estar entre 16 e 120 anos");
        Validate.notEmpty(interesses, "Pelo menos um interesse deve ser informado");
        Validate.noNullElements(interesses, "Nenhum interesse pode ser null");
        
        // Se chegou até aqui, todos os dados são válidos
        salvarUsuario(new Usuario(nome, email, idade, interesses));
    }
    
    public void atualizarNota(List<Double> notas, int indice, double novaNota) {
        Validate.notNull(notas, "Lista de notas não pode ser null");
        Validate.notEmpty(notas, "Lista de notas não pode estar vazia");
        Validate.validIndex(notas, indice, "Índice inválido para a lista de notas");
        Validate.inclusiveBetween(0.0, 10.0, novaNota, "Nota deve estar entre 0 e 10");
        
        notas.set(indice, novaNota);
    }
    
    public double calcularMediaPonderada(double[] notas, double[] pesos) {
        Validate.notNull(notas, "Notas não podem ser null");
        Validate.notNull(pesos, "Pesos não podem ser null");
        Validate.isTrue(notas.length == pesos.length, 
                       "Quantidade de notas deve ser igual à quantidade de pesos");
        Validate.isTrue(notas.length > 0, "Deve haver pelo menos uma nota");
        
        double somaNotas = 0;
        double somaPesos = 0;
        
        for (int i = 0; i < notas.length; i++) {
            Validate.inclusiveBetween(0.0, 10.0, notas[i], 
                                    String.format("Nota %d deve estar entre 0 e 10", i + 1));
            Validate.isTrue(pesos[i] > 0, 
                          String.format("Peso %d deve ser positivo", i + 1));
            
            somaNotas += notas[i] * pesos[i];
            somaPesos += pesos[i];
        }
        
        return somaNotas / somaPesos;
    }
    
    private void salvarUsuario(Usuario usuario) {
        // Implementação de salvamento...
    }
}
```

---

## 🏗️ Builders

### Conceito Base
Os Builders do Commons Lang3 são como assistentes especializados em criar representações de objetos de forma padronizada e eficiente.

### ToStringBuilder
```java
public class Pessoa {
    private String nome;
    private int idade;
    private String email;
    
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("nome", nome)
                .append("idade", idade)
                .append("email", email)
                .toString();
    }
    
    // Estilos diferentes
    public String toStringDetalhado() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("nome", nome)
                .append("idade", idade)
                .append("email", email)
                .toString();
    }
}

// Saídas dos diferentes estilos:
// DEFAULT_STYLE: Pessoa@15db9742[nome=João,idade=30,email=joao@email.com]
// SHORT_PREFIX_STYLE: Pessoa[nome=João,idade=30,email=joao@email.com]
// NO_FIELD_NAMES_STYLE: Pessoa@15db9742[João,30,joao@email.com]
// MULTI_LINE_STYLE: 
// Pessoa@15db9742[
//   nome=João
//   idade=30
//   email=joao@email.com
// ]
```

### EqualsBuilder e HashCodeBuilder
```java
public class Produto {
    private String codigo;
    private String nome;
    private double preco;
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Produto produto = (Produto) obj;
        
        return new EqualsBuilder()
                .append(codigo, produto.codigo)
                .append(nome, produto.nome)
                .append(preco, produto.preco)
                .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(codigo)
                .append(nome)
                .append(preco)
                .toHashCode();
    }
}
```

### CompareToBuilder
```java
public class Funcionario implements Comparable<Funcionario> {
    private String nome;
    private String departamento;
    private double salario;
    private Date dataAdmissao;
    
    @Override
    public int compareTo(Funcionario other) {
        return new CompareToBuilder()
                .append(departamento, other.departamento)      // 1º critério
                .append(salario, other.salario)                // 2º critério (reversed para ordem decrescente)
                .append(nome, other.nome)                      // 3º critério
                .append(dataAdmissao, other.dataAdmissao)      // 4º critério
                .toComparison();
    }
    
    // Para ordenação decrescente de salário
    public int compareToSalarioDecrescente(Funcionario other) {
        return new CompareToBuilder()
                .append(other.salario, salario)  // Invertido para decrescente
                .append(nome, other.nome)
                .toComparison();
    }
}
```

### Exemplo Prático Completo: Sistema de Produtos
```java
public class SistemaProdutos {
    
    public static class Produto implements Comparable<Produto> {
        private String codigo;
        private String nome;
        private String categoria;
        private double preco;
        private int estoque;
        private Date dataCadastro;
        
        public Produto(String codigo, String nome, String categoria, double preco, int estoque) {
            this.codigo = codigo;
            this.nome = nome;
            this.categoria = categoria;
            this.preco = preco;
            this.estoque = estoque;
            this.dataCadastro = new Date();
        }
        
        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("codigo", codigo)
                    .append("nome", nome)
                    .append("categoria", categoria)
                    .append("preco", String.format("%.2f", preco))
                    .append("estoque", estoque)
                    .toString();
        }
        
        public String toStringDetalhado() {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                    .append("codigo", codigo)
                    .append("nome", nome)
                    .append("categoria", categoria)
                    .append("preco", String.format("R$ %.2f", preco))
                    .append("estoque", estoque + " unidades")
                    .append("dataCadastro", dataCadastro)
                    .toString();
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            
            Produto produto = (Produto) obj;
            
            return new EqualsBuilder()
                    .append(codigo, produto.codigo)  // Código é único
                    .isEquals();
        }
        
        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(codigo)
                    .toHashCode();
        }
        
        @Override
        public int compareTo(Produto other) {
            // Ordenação: categoria -> preço -> nome
            return new CompareToBuilder()
                    .append(categoria, other.categoria)
                    .append(preco, other.preco)
                    .append(nome, other.nome)
                    .toComparison();
        }
        
        // Getters e setters...
    }
}
```

---

## 🎓 Exemplos Práticos

### 1. Sistema de Processamento de Arquivo CSV
```java
public class ProcessadorCSV {
    
    public List<String[]> processarArquivo(String conteudo) {
        Validate.notBlank(conteudo, "Conteúdo do arquivo não pode estar vazio");
        
        String[] linhas = StringUtils.split(conteudo, '\n');
        Validate.notEmpty(linhas, "Arquivo deve conter pelo menos uma linha");
        
        List<String[]> dados = new ArrayList<>();
        
        for (String linha : linhas) {
            if (StringUtils.isNotBlank(linha)) {
                String[] campos = StringUtils.split(linha, ',');
                
                // Limpa espaços em branco de cada campo
                for (int i = 0; i < campos.length; i++) {
                    campos[i] = StringUtils.trim(campos[i]);
                }
                
                dados.add(campos);
            }
        }
        
        return dados;
    }
    
    public Map<String, String> criarRelatorio(List<String[]> dados) {
        if (ArrayUtils.isEmpty(dados.toArray())) {
            return new HashMap<>();
        }
        
        Map<String, String> relatorio = new HashMap<>();
        
        relatorio.put("totalLinhas", String.valueOf(dados.size()));
        relatorio.put("totalColunas", String.valueOf(dados.get(0).length));
        
        // Encontrar campos não preenchidos
        int camposVazios = 0;
        for (String[] linha : dados) {
            for (String campo : linha) {
                if (StringUtils.isBlank(campo)) {
                    camposVazios++;
                }
            }
        }
        
        relatorio.put("camposVazios", String.valueOf(camposVazios));
        relatorio.put("percentualPreenchimento", 
                     String.format("%.2f%%", 
                     100.0 * (dados.size() * dados.get(0).length - camposVazios) / 
                     (dados.size() * dados.get(0).length)));
        
        return relatorio;
    }
}
```

### 2. Sistema de Configuração Flexível
```java
public class GerenciadorConfiguracao {
    private Properties propriedades;
    
    public GerenciadorConfiguracao(Properties propriedades) {
        this.propriedades = ObjectUtils.defaultIfNull(propriedades, new Properties());
    }
    
    public String getString(String chave, String valorPadrao) {
        String valor = propriedades.getProperty(chave);
        return StringUtils.defaultIfBlank(valor, valorPadrao);
    }
    
    public int getInt(String chave, int valorPadrao) {
        String valor = propriedades.getProperty(chave);
        return NumberUtils.toInt(valor, valorPadrao);
    }
    
    public double getDouble(String chave, double valorPadrao) {
        String valor = propriedades.getProperty(chave);
        return NumberUtils.toDouble(valor, valorPadrao);
    }
    
    public boolean getBoolean(String chave, boolean valorPadrao) {
        String valor = propriedades.getProperty(chave);
        if (StringUtils.isBlank(valor)) {
            return valorPadrao;
        }
        return StringUtils.equalsIgnoreCase(valor, "true") || 
               StringUtils.equalsIgnoreCase(valor, "yes") ||
               StringUtils.equalsIgnoreCase(valor, "1");
    }
    
    public String[] getArray(String chave, String separador, String[] valorPadrao) {
        String valor = propriedades.getProperty(chave);
        if (StringUtils.isBlank(valor)) {
            return ObjectUtils.defaultIfNull(valorPadrao, new String[0]);
        }
        
        String[] array = StringUtils.split(valor, separador);
        
        // Remove espaços em branco de cada elemento
        for (int i = 0; i < array.length; i++) {
            array[i] = StringUtils.trim(array[i]);
        }
        
        return array;
    }
    
    public String getResumoConfiguracao() {
        if (propriedades.isEmpty()) {
            return "Nenhuma configuração definida";
        }
        
        StringBuilder resumo = new StringBuilder();
        resumo.append("Configurações carregadas:\n");
        
        for (String chave : propriedades.stringPropertyNames()) {
            String valor = propriedades.getProperty(chave);
            // Oculta senhas
            if (StringUtils.containsIgnoreCase(chave, "password") || 
                StringUtils.containsIgnoreCase(chave, "senha")) {
                valor = StringUtils.repeat("*", valor.length());
            }
            
            resumo.append(String.format("  %s = %s%n", chave, valor));
        }
        
        return resumo.toString();
    }
}
```

### 3. Utilitário de Formatação de Dados
```java
public class FormatadorDados {
    
    public String formatarNome(String nome) {
        if (StringUtils.isBlank(nome)) {
            return "Nome não informado";
        }
        
        // Remove espaços extras e converte para formato título
        String nomeFormatado = StringUtils.normalizeSpace(nome.toLowerCase());
        
        String[] palavras = StringUtils.split(nomeFormatado, ' ');
        StringBuilder resultado = new StringBuilder();
        
        for (String palavra : palavras) {
            if (resultado.length() > 0) {
                resultado.append(" ");
            }
            
            // Preposições em minúsculo
            if (ArrayUtils.contains(new String[]{"de", "da", "do", "dos", "das"}, palavra)) {
                resultado.append(palavra);
            } else {
                resultado.append(StringUtils.capitalize(palavra));
            }
        }
        
        return resultado.toString();
    }
    
    public String formatarTelefone(String telefone) {
        if (StringUtils.isBlank(telefone)) {
            return "";
        }
        
        // Remove tudo que não for número
        String numeros = StringUtils.replacePattern(telefone, "[^0-9]", "");
        
        if (numeros.length() == 11) {
            // Celular: (XX) XXXXX-XXXX
            return String.format("(%s) %s-%s",
                    numeros.substring(0, 2),
                    numeros.substring(2, 7),
                    numeros.substring(7));
        } else if (numeros.length() == 10) {
            // Fixo: (XX) XXXX-XXXX
            return String.format("(%s) %s-%s",
                    numeros.substring(0, 2),
                    numeros.substring(2, 6),
                    numeros.substring(6));
        }
        
        return telefone; // Retorna original se não conseguir formatar
    }
    
    public String formatarCPF(String cpf) {
        if (StringUtils.isBlank(cpf)) {
            return "";
        }
        
        String numeros = StringUtils.replacePattern(cpf, "[^0-9]", "");
        
        if (numeros.length() == 11) {
            return String.format("%s.%s.%s-%s",
                    numeros.substring(0, 3),
                    numeros.substring(3, 6),
                    numeros.substring(6, 9),
                    numeros.substring(9));
        }
        
        return cpf;
    }
    
    public String formatarMoeda(double valor) {
        if (valor < 0) {
            return String.format("-R$ %.2f", Math.abs(valor));
        }
        return String.format("R$ %.2f", valor);
    }
    
    public String criarSlug(String texto) {
        if (StringUtils.isBlank(texto)) {
            return "";
        }
        
        // Remove acentos, converte para minúsculo e substitui espaços por hífen
        String slug = StringUtils.stripAccents(texto.toLowerCase());
        slug = StringUtils.replacePattern(slug, "[^a-z0-9\\s]", "");
        slug = StringUtils.replacePattern(slug, "\\s+", "-");
        slug = StringUtils.strip(slug, "-");
        
        return slug;
    }
}
```

---

## 📋 Melhores Práticas

### 1. **Use Validações no Início dos Métodos**
```java
// ✅ Bom - validação imediata
public void processarUsuario(String nome, int idade) {
    Validate.notBlank(nome, "Nome é obrigatório");
    Validate.inclusiveBetween(0, 150, idade, "Idade deve estar entre 0 e 150");
    
    // Resto da lógica...
}

// ❌ Ruim - validação tardia
public void processarUsuario(String nome, int idade) {
    // Muito código...
    if (StringUtils.isBlank(nome)) {
        throw new IllegalArgumentException("Nome é obrigatório");
    }
    // Mais código...
}
```

### 2. **Prefira Métodos Null-Safe**
```java
// ✅ Bom - métodos null-safe
int tamanho = StringUtils.length(texto);  // Nunca gera NPE
boolean vazio = ArrayUtils.isEmpty(array); // Nunca gera NPE

// ❌ Evite - propenso a NPE
int tamanho = texto.length();  // NPE se texto for null
boolean vazio = array.length == 0; // NPE se array for null
```

### 3. **Use Builders para Objetos Complexos**
```java
// ✅ Bom - consistente e legível
@Override
public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("nome", nome)
            .append("ativo", ativo)
            .toString();
}

// ❌ Ruim - manual e propenso a erros
@Override
public String toString() {
    return "Usuario{id=" + id + ", nome='" + nome + "', ativo=" + ativo + "}";
}
```

### 4. **Combine Utilitários para Máxima Eficiência**
```java
public class ProcessadorTextoAvancado {
    
    public List<String> processarLinhas(String texto) {
        // Combina múltiplos utilitários
        if (StringUtils.isBlank(texto)) {
            return new ArrayList<>();
        }
        
        String[] linhas = StringUtils.split(texto, '\n');
        List<String> resultado = new ArrayList<>();
        
        for (String linha : linhas) {
            String processada = StringUtils.normalizeSpace(
                    StringUtils.trim(linha));
            
            if (StringUtils.isNotBlank(processada)) {
                resultado.add(StringUtils.capitalize(processada));
            }
        }
        
        return resultado;
    }
}
```

### 5. **Use Valores Padrão Apropriados**
```java
// ✅ Bom - valores padrão explícitos
public String formatarNome(String nome) {
    return StringUtils.defaultIfBlank(nome, "Sem nome");
}

public int obterIdade(String idadeTexto) {
    return NumberUtils.toInt(idadeTexto, 0); // 0 como padrão faz sentido
}

// ❌ Cuidado - valores padrão implícitos podem confundir
public int obterIdade(String idadeTexto) {
    return NumberUtils.toInt(idadeTexto); // Retorna 0, mas não fica claro
}
```

### 6. **Documente Comportamentos Especiais**
```java
/**
 * Normaliza um nome removendo espaços extras e capitalizando adequadamente.
 * 
 * @param nome o nome a ser normalizado
 * @return nome formatado ou "Sem nome" se entrada for null/vazia
 * 
 * Exemplos:
 * - "  joão  silva  " → "João Silva"
 * - "" → "Sem nome" 
 * - null → "Sem nome"
 */
public String normalizarNome(String nome) {
    if (StringUtils.isBlank(nome)) {
        return "Sem nome";
    }
    
    return StringUtils.capitalize(
            StringUtils.normalizeSpace(nome.toLowerCase())
    );
}
```

### 7. **Teste Casos Extremos**
```java
public class TestesFormatador {
    
    @Test
    public void testFormataçãoComCasosExtemos() {
        FormatadorDados formatador = new FormatadorDados();
        
        // Casos normais
        assertEquals("João Silva", formatador.formatarNome("joão silva"));
        
        // Casos extremos - sempre teste estes!
        assertEquals("Sem nome", formatador.formatarNome(null));
        assertEquals("Sem nome", formatador.formatarNome(""));
        assertEquals("Sem nome", formatador.formatarNome("   "));
        assertEquals("A", formatador.formatarNome("a"));
        assertEquals("João da Silva", formatador.formatarNome("joão DA silva"));
    }
}
```

---

## 🎯 Resumo Final

### Quando Usar Cada Utilitário

| Utilitário | Use quando... | Exemplo típico |
|------------|---------------|----------------|
| **StringUtils** | Manipular texto | Validar entrada de usuário |
| **ArrayUtils** | Trabalhar com arrays | Adicionar/remover elementos |
| **NumberUtils** | Converter números com segurança | Processar formulários |
| **DateUtils** | Manipular datas (legacy) | Sistemas com java.util.Date |
| **ObjectUtils** | Verificações null-safe | Validações de objeto |
| **SystemUtils** | Informações do ambiente | Relatórios de sistema |
| **Validate** | Validações com falha rápida | Início de métodos públicos |
| **Builders** | Implementar toString/equals/hashCode | Classes de domínio |

### Fluxo de Decisão Visual

```
┌─────────────────┐
│ Preciso validar │
│    entrada?     │
└─────┬───────────┘
      │
      ├─ Sim ─→ Use Validate
      │
      └─ Não
         │
    ┌────▼─────────────┐
    │ Tipo de dados?   │
    └──────┬───────────┘
           │
           ├─ String ─→ StringUtils
           ├─ Array ─→ ArrayUtils  
           ├─ Número ─→ NumberUtils
           ├─ Data ─→ DateUtils
           ├─ Objeto ─→ ObjectUtils
           └─ Sistema ─→ SystemUtils
```

### Lembre-se Sempre

1. **Null-Safety**: Commons Lang3 é seu escudo contra NullPointerException
2. **Fail-Fast**: Use Validate para falhar rápido com mensagens claras
3. **Consistência**: Use Builders para implementações padronizadas
4. **Simplicidade**: Um método do Commons Lang3 vale por 10 linhas de código manual
5. **Legibilidade**: Código com Commons Lang3 é autodocumentado

Apache Commons Lang3 transformará seu código Java de verboso e propenso a erros em conciso e robusto. É uma biblioteca essencial que todo desenvolvedor Java deveria dominar.