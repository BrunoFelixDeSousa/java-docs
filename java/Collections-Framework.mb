# Guia Completo do Collections Framework em Java

## Sumário
1. [Introdução](#1-introdução)
2. [Hierarquia e Interfaces Principais](#2-hierarquia-e-interfaces-principais)
3. [Interface Collection](#3-interface-collection)
4. [Interface List](#4-interface-list)
5. [Interface Set](#5-interface-set)
6. [Interface Queue](#6-interface-queue)
7. [Interface Map](#7-interface-map)
8. [Comparação de Performance](#8-comparação-de-performance)
9. [Utilitários Collections e Arrays](#9-utilitários-collections-e-arrays)
10. [Java Generics](#10-java-generics)
11. [Streams e Collections](#11-streams-e-collections)
12. [Benchmarks de Performance](#12-benchmarks-de-performance)
13. [Mini-projeto: Sistema de Biblioteca](#13-mini-projeto-sistema-de-biblioteca)

---

## 1. Introdução

### O que é o Collections Framework?

O **Collections Framework** do Java é um conjunto unificado de classes e interfaces que implementa estruturas de dados reutilizáveis. Imagine como uma "caixa de ferramentas" onde cada ferramenta (coleção) tem um propósito específico.

**Analogia Simples:** 
- Se você fosse organizar livros, poderia usar uma **lista ordenada** (ArrayList) para manter a ordem de leitura
- Ou um **conjunto único** (HashSet) para evitar duplicatas
- Ou um **mapa** (HashMap) para associar cada livro ao seu autor

### Por que usar Collections ao invés de arrays?

```java
// Problema com arrays tradicionais
String[] nomes = new String[10]; // Tamanho fixo!
nomes[0] = "João";
nomes[1] = "Maria";
// E se precisar de 11 nomes? Precisa recriar o array!

// Solução com Collections
List<String> nomes = new ArrayList<>(); // Tamanho dinâmico!
nomes.add("João");
nomes.add("Maria");
nomes.add("Pedro"); // Cresce automaticamente
```

### Principais Vantagens:
- **Tamanho dinâmico:** Crescem e diminuem conforme necessário
- **Algoritmos prontos:** Ordenação, busca, etc.
- **Type Safety:** Com Generics, evita erros de tipo
- **Padronização:** Interfaces consistentes

---

## 2. Hierarquia e Interfaces Principais

### Diagrama da Hierarquia

```
                    Iterable<E>
                        |
                   Collection<E>
                   /     |     \
               List<E>  Set<E>  Queue<E>
                  |       |       |
           [ArrayList] [HashSet] [PriorityQueue]
           [LinkedList] [TreeSet] [LinkedList]
           [Vector]   [LinkedHashSet]


                    Map<E,V> (separado)
                       |
                [HashMap, TreeMap, LinkedHashMap]
```

### Interfaces Fundamentais

| Interface | Permite Duplicatas? | Mantém Ordem? | Indexed Access? | Descrição |
|-----------|-------------------|---------------|-----------------|-----------|
| `Collection` | Depende | Depende | Não | Interface raiz |
| `List` | ✅ Sim | ✅ Sim | ✅ Sim | Lista ordenada |
| `Set` | ❌ Não | Depende | ❌ Não | Conjunto único |
| `Queue` | ✅ Sim | ✅ Sim | ❌ Não | Fila (FIFO) |
| `Map` | ❌ Chaves únicas | Depende | ✅ Por chave | Mapeamento chave-valor |

---

## 3. Interface Collection

### Conceito Base

A interface `Collection` é como um "contrato" que define operações básicas para grupos de objetos:

```java
public interface Collection<E> extends Iterable<E> {
    boolean add(E element);
    boolean remove(Object o);
    boolean contains(Object o);
    int size();
    boolean isEmpty();
    void clear();
    Iterator<E> iterator();
    // ... outros métodos
}
```

### Exemplo Prático: Operações Básicas

```java
import java.util.*;

public class CollectionBasics {
    public static void main(String[] args) {
        // Qualquer Collection pode usar estes métodos
        Collection<String> frutas = new ArrayList<>();
        
        // Adicionar elementos
        frutas.add("Maçã");
        frutas.add("Banana");
        frutas.add("Laranja");
        
        // Verificar conteúdo
        System.out.println("Agrupadas por tamanho: " + porTamanho);
        
        // min() e max()
        Optional<Integer> minimo = numeros.stream().min(Integer::compareTo);
        Optional<Integer> maximo = numeros.stream().max(Integer::compareTo);
        System.out.println("Mínimo: " + minimo.orElse(0));
        System.out.println("Máximo: " + maximo.orElse(0));
    }
}
```

### 11.3 Trabalhando com Objetos Complexos

```java
import java.util.*;
import java.util.stream.*;

class Pessoa {
    private String nome;
    private int idade;
    private String cidade;
    private double salario;
    
    public Pessoa(String nome, int idade, String cidade, double salario) {
        this.nome = nome;
        this.idade = idade;
        this.cidade = cidade;
        this.salario = salario;
    }
    
    // Getters
    public String getNome() { return nome; }
    public int getIdade() { return idade; }
    public String getCidade() { return cidade; }
    public double getSalario() { return salario; }
    
    @Override
    public String toString() {
        return String.format("%s (%d anos, %s, R$%.2f)", 
                           nome, idade, cidade, salario);
    }
}

public class StreamsComObjetos {
    public static void main(String[] args) {
        List<Pessoa> pessoas = Arrays.asList(
            new Pessoa("Ana", 25, "São Paulo", 5000.0),
            new Pessoa("Bruno", 30, "Rio de Janeiro", 6000.0),
            new Pessoa("Carlos", 35, "São Paulo", 7000.0),
            new Pessoa("Diana", 28, "Belo Horizonte", 5500.0),
            new Pessoa("Eduardo", 32, "São Paulo", 8000.0),
            new Pessoa("Fernanda", 26, "Rio de Janeiro", 4500.0)
        );
        
        System.out.println("=== PESSOAS ===");
        pessoas.forEach(System.out::println);
        
        // 1. Filtrar pessoas de São Paulo com mais de 30 anos
        System.out.println("\n=== SP + 30+ anos ===");
        List<Pessoa> spMais30 = pessoas.stream()
            .filter(p -> p.getCidade().equals("São Paulo"))
            .filter(p -> p.getIdade() > 30)
            .collect(Collectors.toList());
        spMais30.forEach(System.out::println);
        
        // 2. Ordenar por salário (decrescente)
        System.out.println("\n=== ORDENADOS POR SALÁRIO ===");
        pessoas.stream()
            .sorted(Comparator.comparing(Pessoa::getSalario).reversed())
            .forEach(System.out::println);
        
        // 3. Agrupar por cidade
        System.out.println("\n=== AGRUPADOS POR CIDADE ===");
        Map<String, List<Pessoa>> porCidade = pessoas.stream()
            .collect(Collectors.groupingBy(Pessoa::getCidade));
        porCidade.forEach((cidade, lista) -> {
            System.out.println(cidade + ": " + lista.size() + " pessoas");
        });
        
        // 4. Calcular estatísticas de salário
        System.out.println("\n=== ESTATÍSTICAS DE SALÁRIO ===");
        DoubleSummaryStatistics stats = pessoas.stream()
            .collect(Collectors.summarizingDouble(Pessoa::getSalario));
        
        System.out.printf("Média: R$%.2f%n", stats.getAverage());
        System.out.printf("Mínimo: R$%.2f%n", stats.getMin());
        System.out.printf("Máximo: R$%.2f%n", stats.getMax());
        System.out.printf("Soma: R$%.2f%n", stats.getSum());
        System.out.printf("Quantidade: %d%n", stats.getCount());
        
        // 5. Encontrar pessoa mais nova de cada cidade
        System.out.println("\n=== MAIS NOVO DE CADA CIDADE ===");
        Map<String, Optional<Pessoa>> maisNovosPorCidade = pessoas.stream()
            .collect(Collectors.groupingBy(
                Pessoa::getCidade,
                Collectors.minBy(Comparator.comparing(Pessoa::getIdade))
            ));
        
        maisNovosPorCidade.forEach((cidade, opcional) -> {
            opcional.ifPresent(pessoa -> 
                System.out.println(cidade + ": " + pessoa.getNome() + 
                                 " (" + pessoa.getIdade() + " anos)"));
        });
        
        // 6. Criar mapa nome -> salário
        System.out.println("\n=== MAPA NOME -> SALÁRIO ===");
        Map<String, Double> nomeSalario = pessoas.stream()
            .collect(Collectors.toMap(
                Pessoa::getNome,
                Pessoa::getSalario
            ));
        nomeSalario.forEach((nome, salario) -> 
            System.out.printf("%s: R$%.2f%n", nome, salario));
        
        // 7. Verificações com predicados
        System.out.println("\n=== VERIFICAÇÕES ===");
        boolean todosMaisDe20 = pessoas.stream()
            .allMatch(p -> p.getIdade() > 20);
        System.out.println("Todos > 20 anos: " + todosMaisDe20);
        
        boolean algumGanhaMaisDe7k = pessoas.stream()
            .anyMatch(p -> p.getSalario() > 7000);
        System.out.println("Alguém ganha > R$7000: " + algumGanhaMaisDe7k);
        
        boolean ninguemDoRecife = pessoas.stream()
            .noneMatch(p -> p.getCidade().equals("Recife"));
        System.out.println("Ninguém do Recife: " + ninguemDoRecife);
        
        // 8. Top 3 salários
        System.out.println("\n=== TOP 3 SALÁRIOS ===");
        pessoas.stream()
            .sorted(Comparator.comparing(Pessoa::getSalario).reversed())
            .limit(3)
            .forEach(p -> System.out.println(p.getNome() + ": R$" + p.getSalario()));
    }
}
```

### 11.4 Parallel Streams - Processamento Paralelo

```java
import java.util.*;
import java.util.stream.*;

public class ParallelStreamsExemplo {
    public static void main(String[] args) {
        // Criar lista grande para demonstrar diferença
        List<Integer> numerosMuitos = IntStream.rangeClosed(1, 10_000_000)
                                              .boxed()
                                              .collect(Collectors.toList());
        
        // Stream sequencial
        long inicio = System.currentTimeMillis();
        long somaSequencial = numerosMuitos.stream()
            .filter(n -> n % 2 == 0)
            .mapToLong(n -> n * n)
            .sum();
        long tempoSequencial = System.currentTimeMillis() - inicio;
        
        // Stream paralelo
        inicio = System.currentTimeMillis();
        long somaParalela = numerosMuitos.parallelStream()
            .filter(n -> n % 2 == 0)
            .mapToLong(n -> n * n)
            .sum();
        long tempoParalelo = System.currentTimeMillis() - inicio;
        
        System.out.println("Soma sequencial: " + somaSequencial);
        System.out.println("Tempo sequencial: " + tempoSequencial + "ms");
        System.out.println("Soma paralela: " + somaParalela);
        System.out.println("Tempo paralelo: " + tempoParalelo + "ms");
        System.out.println("Speedup: " + (double)tempoSequencial / tempoParalelo + "x");
        
        // ⚠️ CUIDADO: Parallel streams nem sempre são mais rápidos!
        demonstrarQuandoNaoUsar();
    }
    
    private static void demonstrarQuandoNaoUsar() {
        System.out.println("\n=== QUANDO NÃO USAR PARALLEL STREAMS ===");
        
        List<Integer> poucos = Arrays.asList(1, 2, 3, 4, 5);
        
        // Para poucos elementos, overhead é maior que benefício
        long inicio = System.currentTimeMillis();
        poucos.stream().map(n -> n * 2).collect(Collectors.toList());
        long tempoSeq = System.currentTimeMillis() - inicio;
        
        inicio = System.currentTimeMillis();
        poucos.parallelStream().map(n -> n * 2).collect(Collectors.toList());
        long tempoPar = System.currentTimeMillis() - inicio;
        
        System.out.println("Para poucos elementos:");
        System.out.println("Sequencial: " + tempoSeq + "ms");
        System.out.println("Paralelo: " + tempoPar + "ms");
        System.out.println("(Overhead do paralelismo não compensa)");
        
        // Regras para usar parallel streams:
        System.out.println("\n=== QUANDO USAR PARALLEL STREAMS ===");
        System.out.println("✅ Muitos elementos (milhares+)");
        System.out.println("✅ Operações CPU-intensivas");
        System.out.println("✅ Operações independentes (sem estado compartilhado)");
        System.out.println("✅ Estruturas de dados que se dividem bem (ArrayList, arrays)");
        System.out.println();
        System.out.println("❌ Poucos elementos");
        System.out.println("❌ Operações rápidas");
        System.out.println("❌ Operações com I/O");
        System.out.println("❌ LinkedList ou outras estruturas difíceis de dividir");
    }
}
```

---

## 12. Benchmarks de Performance

### 12.1 Medindo Performance com JMH (Conceitual)

```java
import java.util.*;
import java.util.concurrent.TimeUnit;

// Este é um exemplo conceitual de como usar JMH
// Para usar de verdade, adicione a dependência do JMH ao seu projeto

public class BenchmarkCollections {
    
    // Simular benchmark sem JMH (menos preciso, mas didático)
    public static void main(String[] args) {
        System.out.println("=== BENCHMARK DE COLLECTIONS ===\n");
        
        benchmarkInserção();
        benchmarkBusca();
        benchmarkIteração();
    }
    
    private static void benchmarkInserção() {
        System.out.println("=== INSERÇÃO DE 100,000 ELEMENTOS ===");
        int elementos = 100_000;
        
        // ArrayList
        long inicio = System.nanoTime();
        List<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < elementos; i++) {
            arrayList.add(i);
        }
        long tempoArrayList = System.nanoTime() - inicio;
        
        // LinkedList
        inicio = System.nanoTime();
        List<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < elementos; i++) {
            linkedList.add(i);
        }
        long tempoLinkedList = System.nanoTime() - inicio;
        
        // HashSet
        inicio = System.nanoTime();
        Set<Integer> hashSet = new HashSet<>();
        for (int i = 0; i < elementos; i++) {
            hashSet.add(i);
        }
        long tempoHashSet = System.nanoTime() - inicio;
        
        // TreeSet
        inicio = System.nanoTime();
        Set<Integer> treeSet = new TreeSet<>();
        for (int i = 0; i < elementos; i++) {
            treeSet.add(i);
        }
        long tempoTreeSet = System.nanoTime() - inicio;
        
        System.out.printf("ArrayList:  %6d ms%n", tempoArrayList / 1_000_000);
        System.out.printf("LinkedList: %6d ms%n", tempoLinkedList / 1_000_000);
        System.out.printf("HashSet:    %6d ms%n", tempoHashSet / 1_000_000);
        System.out.printf("TreeSet:    %6d ms%n", tempoTreeSet / 1_000_000);
        System.out.println();
    }
    
    private static void benchmarkBusca() {
        System.out.println("=== BUSCA EM 100,000 ELEMENTOS ===");
        int elementos = 100_000;
        Random random = new Random();
        
        // Preparar estruturas
        List<Integer> arrayList = new ArrayList<>();
        Set<Integer> hashSet = new HashSet<>();
        Set<Integer> treeSet = new TreeSet<>();
        
        for (int i = 0; i < elementos; i++) {
            arrayList.add(i);
            hashSet.add(i);
            treeSet.add(i);
        }
        
        // Elementos para buscar
        int[] elementosBusca = new int[1000];
        for (int i = 0; i < elementosBusca.length; i++) {
            elementosBusca[i] = random.nextInt(elementos);
        }
        
        // Benchmark ArrayList.contains()
        long inicio = System.nanoTime();
        for (int elemento : elementosBusca) {
            arrayList.contains(elemento);
        }
        long tempoArrayList = System.nanoTime() - inicio;
        
        // Benchmark HashSet.contains()
        inicio = System.nanoTime();
        for (int elemento : elementosBusca) {
            hashSet.contains(elemento);
        }
        long tempoHashSet = System.nanoTime() - inicio;
        
        // Benchmark TreeSet.contains()
        inicio = System.nanoTime();
        for (int elemento : elementosBusca) {
            treeSet.contains(elemento);
        }
        long tempoTreeSet = System.nanoTime() - inicio;
        
        System.out.printf("ArrayList:  %6d ms (O(n))%n", tempoArrayList / 1_000_000);
        System.out.printf("HashSet:    %6d ms (O(1))%n", tempoHashSet / 1_000_000);
        System.out.printf("TreeSet:    %6d ms (O(log n))%n", tempoTreeSet / 1_000_000);
        System.out.println();
    }
    
    private static void benchmarkIteração() {
        System.out.println("=== ITERAÇÃO EM 1,000,000 ELEMENTOS ===");
        int elementos = 1_000_000;
        
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        
        for (int i = 0; i < elementos; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }
        
        // ArrayList com for tradicional
        long inicio = System.nanoTime();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i);
        }
        long tempoArrayFor = System.nanoTime() - inicio;
        
        // ArrayList com enhanced for
        inicio = System.nanoTime();
        for (Integer valor : arrayList) {
            // Apenas acessa
        }
        long tempoArrayEnhanced = System.nanoTime() - inicio;
        
        // LinkedList com for tradicional (muito lento!)
        inicio = System.nanoTime();
        for (int i = 0; i < Math.min(10000, linkedList.size()); i++) { // Só 10k para não demorar
            linkedList.get(i);
        }
        long tempoLinkedFor = System.nanoTime() - inicio;
        
        // LinkedList com enhanced for
        inicio = System.nanoTime();
        for (Integer valor : linkedList) {
            // Apenas acessa
        }
        long tempoLinkedEnhanced = System.nanoTime() - inicio;
        
        System.out.printf("ArrayList for tradicional:  %6d ms%n", tempoArrayFor / 1_000_000);
        System.out.printf("ArrayList enhanced for:     %6d ms%n", tempoArrayEnhanced / 1_000_000);
        System.out.printf("LinkedList for (10k only):  %6d ms%n", tempoLinkedFor / 1_000_000);
        System.out.printf("LinkedList enhanced for:    %6d ms%n", tempoLinkedEnhanced / 1_000_000);
        System.out.println();
    }
}
```

### 12.2 Comparação Prática: Qual Collection Usar?

```java
import java.util.*;

public class GuiaEscolhaCollection {
    
    public static void main(String[] args) {
        demonstrarCenarios();
    }
    
    private static void demonstrarCenarios() {
        System.out.println("=== GUIA DE ESCOLHA DE COLLECTIONS ===\n");
        
        // Cenário 1: Cache de dados com acesso frequente por ID
        cenarioCache();
        
        // Cenário 2: Lista de tarefas com inserções frequentes
        cenarioTarefas();
        
        // Cenário 3: Ranking de jogadores
        cenarioRanking();
        
        // Cenário 4: Processamento de logs únicos
        cenarioLogs();
    }
    
    private static void cenarioCache() {
        System.out.println("=== CENÁRIO: CACHE DE USUÁRIOS ===");
        System.out.println("Necessidade: Busca rápida por ID, não importa ordem");
        System.out.println("Escolha: HashMap<Integer, Usuario>\n");
        
        Map<Integer, String> cacheUsuarios = new HashMap<>();
        
        // Carregar cache
        cacheUsuarios.put(1001, "João Silva");
        cacheUsuarios.put(1002, "Maria Santos");
        cacheUsuarios.put(1003, "Pedro Oliveira");
        
        // Busca O(1)
        long inicio = System.nanoTime();
        String usuario = cacheUsuarios.get(1002);
        long tempo = System.nanoTime() - inicio;
        
        System.out.println("Usuário encontrado: " + usuario);
        System.out.println("Tempo de busca: " + tempo + "ns (muito rápido!)");
        System.out.println("✅ HashMap é ideal para caches\n");
    }
    
    private static void cenarioTarefas() {
        System.out.println("=== CENÁRIO: LISTA DE TAREFAS ===");
        System.out.println("Necessidade: Inserções/remoções no início, meio e fim");
        System.out.println("Escolha: LinkedList (implementa List e Deque)\n");
        
        LinkedList<String> tarefas = new LinkedList<>();
        
        // Inserir no final (tarefa normal)
        tarefas.addLast("Fazer compras");
        tarefas.addLast("Estudar Java");
        
        // Inserir no início (tarefa urgente)
        tarefas.addFirst("URGENTE: Ligar para cliente");
        
        // Inserir no meio
        tarefas.add(2, "Responder emails");
        
        System.out.println("Lista de tarefas:");
        for (int i = 0; i < tarefas.size(); i++) {
            System.out.println((i + 1) + ". " + tarefas.get(i));
        }
        
        // Processar primeira tarefa (fila)
        String proxima = tarefas.removeFirst();
        System.out.println("\nProcessando: " + proxima);
        System.out.println("✅ LinkedList é ideal para inserções/remoções frequentes\n");
    }
    
    private static void cenarioRanking() {
        System.out.println("=== CENÁRIO: RANKING DE JOGADORES ===");
        System.out.println("Necessidade: Sempre ordenado por pontuação");
        System.out.println("Escolha: TreeMap<Integer, String> (pontos -> nome)\n");
        
        // TreeMap mantém chaves ordenadas
        TreeMap<Integer, String> ranking = new TreeMap<>(Collections.reverseOrder());
        
        ranking.put(1500, "ProGamer123");
        ranking.put(2000, "MasterPlayer");
        ranking.put(1200, "Newbie2024");
        ranking.put(1800, "SkillfulOne");
        
        System.out.println("🏆 RANKING DE JOGADORES:");
        int posicao = 1;
        for (Map.Entry<Integer, String> entry : ranking.entrySet()) {
            System.out.printf("%d°. %-15s - %d pontos%n", 
                            posicao++, entry.getValue(), entry.getKey());
        }
        
        // Adicionar novo jogador
        ranking.put(1700, "RisingStar");
        System.out.println("\nNovo jogador adicionado! Ranking atualizado automaticamente:");
        posicao = 1;
        for (Map.Entry<Integer, String> entry : ranking.entrySet()) {
            System.out.printf("%d°. %-15s - %d pontos%n", 
                            posicao++, entry.getValue(), entry.getKey());
        }
        System.out.println("✅ TreeMap mantém ordem automática\n");
    }
    
    private static void cenarioLogs() {
        System.out.println("=== CENÁRIO: PROCESSAMENTO DE LOGS ===");
        System.out.println("Necessidade: Eliminar duplicatas, busca rápida");
        System.out.println("Escolha: HashSet<String>\n");
        
        Set<String> ipsUnicos = new HashSet<>();
        
        // Simular processamento de logs
        String[] logs = {
            "192.168.1.1", "10.0.0.1", "192.168.1.1", 
            "172.16.0.1", "10.0.0.1", "8.8.8.8", 
            "192.168.1.1", "8.8.8.8"
        };
        
        System.out.println("Processando logs:");
        for (String ip : logs) {
            boolean adicionado = ipsUnicos.add(ip);
            System.out.println("IP " + ip + (adicionado ? " - NOVO" : " - DUPLICATA"));
        }
        
        System.out.println("\nIPs únicos encontrados: " + ipsUnicos.size());
        System.out.println("IPs: " + ipsUnicos);
        System.out.println("✅ HashSet elimina duplicatas automaticamente\n");
    }
}
```

---

## 13. Mini-projeto: Sistema de Biblioteca

Vamos criar um sistema completo que demonstra o uso prático de diferentes Collections:

```java
import java.util.*;
import java.util.stream.Collectors;

// Classe para representar um livro
class Livro {
    private String isbn;
    private String titulo;
    private String autor;
    private String categoria;
    private int anoPublicacao;
    private boolean disponivel;
    
    public Livro(String isbn, String titulo, String autor, String categoria, int ano) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.anoPublicacao = ano;
        this.disponivel = true;
    }
    
    // Getters e Setters
    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getCategoria() { return categoria; }
    public int getAnoPublicacao() { return anoPublicacao; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
    
    @Override
    public String toString() {
        return String.format("'%s' por %s (%d) - %s", 
                           titulo, autor, anoPublicacao, 
                           disponivel ? "DISPONÍVEL" : "EMPRESTADO");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Livro livro = (Livro) obj;
        return Objects.equals(isbn, livro.isbn);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}

// Classe para representar um usuário
class Usuario {
    private String id;
    private String nome;
    private String email;
    private List<String> livrosEmprestados; // ISBNs dos livros
    
    public Usuario(String id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.livrosEmprestados = new ArrayList<>();
    }
    
    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public List<String> getLivrosEmprestados() { return livrosEmprestados; }
    
    public void emprestarLivro(String isbn) {
        livrosEmprestados.add(isbn);
    }
    
    public void devolverLivro(String isbn) {
        livrosEmprestados.remove(isbn);
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - %d livros emprestados", 
                           nome, email, livrosEmprestados.size());
    }
}

// Sistema principal da biblioteca
class SistemaBiblioteca {
    // HashMap: Busca rápida de livros por ISBN
    private Map<String, Livro> livrosPorIsbn;
    
    // HashMap: Busca rápida de usuários por ID
    private Map<String, Usuario> usuariosPorId;
    
    // TreeMap: Livros ordenados por categoria para relatórios
    private Map<String, List<Livro>> livrosPorCategoria;
    
    // LinkedHashMap: Histórico de empréstimos (ordem cronológica)
    private Map<String, List<String>> historicoEmprestimos;
    
    // PriorityQueue: Fila de espera para livros populares
    private Map<String, Queue<String>> filasEspera;
    
    public SistemaBiblioteca() {
        this.livrosPorIsbn = new HashMap<>();
        this.usuariosPorId = new HashMap<>();
        this.livrosPorCategoria = new TreeMap<>();
        this.historicoEmprestimos = new LinkedHashMap<>();
        this.filasEspera = new HashMap<>();
    }
    
    // Adicionar livro ao sistema
    public void adicionarLivro(Livro livro) {
        livrosPorIsbn.put(livro.getIsbn(), livro);
        
        // Adicionar à categoria (TreeMap mantém categorias ordenadas)
        livrosPorCategoria.computeIfAbsent(livro.getCategoria(), k -> new ArrayList<>())
                         .add(livro);
        
        System.out.println("✅ Livro adicionado: " + livro.getTitulo());
    }
    
    // Cadastrar usuário
    public void cadastrarUsuario(Usuario usuario) {
        usuariosPorId.put(usuario.getId(), usuario);
        historicoEmprestimos.put(usuario.getId(), new ArrayList<>());
        System.out.println("✅ Usuário cadastrado: " + usuario.getNome());
    }
    
    // Emprestar livro
    public boolean emprestarLivro(String isbn, String usuarioId) {
        Livro livro = livrosPorIsbn.get(isbn);
        Usuario usuario = usuariosPorId.get(usuarioId);
        
        if (livro == null) {
            System.out.println("❌ Livro não encontrado!");
            return false;
        }
        
        if (usuario == null) {
            System.out.println("❌ Usuário não encontrado!");
            return false;
        }
        
        if (!livro.isDisponivel()) {
            // Adicionar à fila de espera
            filasEspera.computeIfAbsent(isbn, k -> new LinkedList<>()).offer(usuarioId);
            System.out.println("📚 Livro indisponível. Usuário adicionado à fila de espera.");
            System.out.println("Posição na fila: " + filasEspera.get(isbn).size());
            return false;
        }
        
        // Realizar empréstimo
        livro.setDisponivel(false);
        usuario.emprestarLivro(isbn);
        historicoEmprestimos.get(usuarioId).add(isbn);
        
        System.out.println("✅ Empréstimo realizado:");
        System.out.println("   Livro: " + livro.getTitulo());
        System.out.println("   Usuário: " + usuario.getNome());
        return true;
    }
    
    // Devolver livro
    public boolean devolverLivro(String isbn, String usuarioId) {
        Livro livro = livrosPorIsbn.get(isbn);
        Usuario usuario = usuariosPorId.get(usuarioId);
        
        if (livro == null || usuario == null) {
            System.out.println("❌ Livro ou usuário não encontrado!");
            return false;
        }
        
        if (!usuario.getLivrosEmprestados().contains(isbn)) {
            System.out.println("❌ Este usuário não possui este livro emprestado!");
            return false;
        }
        
        // Realizar devolução
        livro.setDisponivel(true);
        usuario.devolverLivro(isbn);
        
        System.out.println("✅ Devolução realizada:");
        System.out.println("   Livro: " + livro.getTitulo());
        System.out.println("   Usuário: " + usuario.getNome());
        
        // Verificar fila de espera
        Queue<String> fila = filasEspera.get(isbn);
        if (fila != null && !fila.isEmpty()) {
            String proximoUsuarioId = fila.poll();
            Usuario proximoUsuario = usuariosPorId.get(proximoUsuarioId);
            if (proximoUsuario != null) {
                emprestarLivro(isbn, proximoUsuarioId);
                System.out.println("📢 Próximo da fila foi notificado: " + proximoUsuario.getNome());
            }
        }
        
        return true;
    }
    
    // Buscar livros por título (usando Stream)
    public List<Livro> buscarPorTitulo(String termo) {
        return livrosPorIsbn.values().stream()
            .filter(livro -> livro.getTitulo().toLowerCase().contains(termo.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    // Buscar livros por autor
    public List<Livro> buscarPorAutor(String autor) {
        return livrosPorIsbn.values().stream()
            .filter(livro -> livro.getAutor().toLowerCase().contains(autor.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    // Listar livros por categoria (TreeMap já mantém ordem)
    public void listarPorCategoria() {
        System.out.println("\n📚 LIVROS POR CATEGORIA:");
        livrosPorCategoria.forEach((categoria, livros) -> {
            System.out.println("\n" + categoria.toUpperCase() + ":");
            livros.forEach(livro -> System.out.println("  • " + livro));
        });
    }
    
    // Relatório de usuários mais ativos (usando Stream)
    public void relatorioUsuariosAtivos() {
        System.out.println("\n👥 USUÁRIOS MAIS ATIVOS:");
        usuariosPorId.values().stream()
            .sorted((u1, u2) -> Integer.compare(
                historicoEmprestimos.get(u2.getId()).size(),
                historicoEmprestimos.get(u1.getId()).size()
            ))
            .limit(5)
            .forEach(usuario -> {
                int totalEmprestimos = historicoEmprestimos.get(usuario.getId()).size();
                System.out.printf("%-20s - %d empréstimos%n", 
                                usuario.getNome(), totalEmprestimos);
            });
    }
    
    // Livros mais populares (com base na fila de espera)
    public void livrosMaisPopulares() {
        System.out.println("\n🔥 LIVROS MAIS POPULARES:");
        filasEspera.entrySet().stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()))
            .limit(5)
            .forEach(entry -> {
                String isbn = entry.getKey();
                int filaSize = entry.getValue().size();
                Livro livro = livrosPorIsbn.get(isbn);
                System.out.printf("%-30s - %d pessoas na fila%n", 
                                livro.getTitulo(), filaSize);
            });
    }
    
    // Estatísticas gerais
    public void estatisticas() {
        System.out.println("\n📊 ESTATÍSTICAS DA BIBLIOTECA:");
        
        long totalLivros = livrosPorIsbn.size();
        long livrosDisponiveis = livrosPorIsbn.values().stream()
            .mapToLong(livro -> livro.isDisponivel() ? 1 : 0)
            .sum();
        long livrosEmprestados = totalLivros - livrosDisponiveis;
        
        System.out.println("Total de livros: " + totalLivros);
        System.out.println("Livros disponíveis: " + livrosDisponiveis);
        System.out.println("Livros emprestados: " + livrosEmprestados);
        System.out.println("Total de usuários: " + usuariosPorId.size());
        System.out.println("Categorias: " + livrosPorCategoria.size());
        
        // Categoria mais popular
        String categoriaMaisPopular = livrosPorCategoria.entrySet().stream()
            .max(Map.Entry.<String, List<Livro>>comparingByValue(
                (lista1, lista2) -> Integer.compare(lista1.size(), lista2.size())
            ))
            .map(Map.Entry::getKey)
            .orElse("Nenhuma");
        
        System.out.println("Categoria mais popular: " + categoriaMaisPopular);
    }
}

public class SistemaBibliotecaMain {
    public static void main(String[] args) {
        SistemaBiblioteca biblioteca = new SistemaBiblioteca();
        
        System.out.println("=== SISTEMA DE BIBLIOTECA ===\n");
        
        // 1. Cadastrar livros
        System.out.println("1. CADASTRANDO LIVROS:");
        biblioteca.adicionarLivro(new Livro("978-0134685991", "Effective Java", "Joshua Bloch", "Programação", 2017));
        biblioteca.adicionarLivro(new Livro("978-0596009205", "Head First Design Patterns", "Eric Freeman", "Programação", 2004));
        biblioteca.adicionarLivro(new Livro("978-0132350884", "Clean Code", "Robert Martin", "Programação", 2008));
        biblioteca.adicionarLivro(new Livro("978-8535902778", "1984", "George Orwell", "Ficção", 1949));
        biblioteca.adicionarLivro(new Livro("978-8535909555", "O Hobbit", "J.R.R. Tolkien", "Fantasia", 1937));
        biblioteca.adicionarLivro(new Livro("978-8573264609", "Sapiens", "Yuval Noah Harari", "História", 2011));
        biblioteca.adicionarLivro(new Livro("978-0307887436", "Gone Girl", "Gillian Flynn", "Thriller", 2012));
        
        // 2. Cadastrar usuários
        System.out.println("\n2. CADASTRANDO USUÁRIOS:");
        biblioteca.cadastrarUsuario(new Usuario("U001", "Ana Silva", "ana@email.com"));
        biblioteca.cadastrarUsuario(new Usuario("U002", "Bruno Santos", "bruno@email.com"));
        biblioteca.cadastrarUsuario(new Usuario("U003", "Carlos Oliveira", "carlos@email.com"));
        biblioteca.cadastrarUsuario(new Usuario("U004", "Diana Costa", "diana@email.com"));
        
        // 3. Realizar empréstimos
        System.out.println("\n3. REALIZANDO EMPRÉSTIMOS:");
        biblioteca.emprestarLivro("978-0134685991", "U001"); // Ana pega Effective Java
        biblioteca.emprestarLivro("978-8535902778", "U002");  // Bruno pega 1984
        biblioteca.emprestarLivro("978-0134685991", "U003");  // Carlos tenta pegar Effective Java (fila)
        biblioteca.emprestarLivro("978-8535909555", "U001");  // Ana pega O Hobbit
        biblioteca.emprestarLivro("978-0134685991", "U004");  // Diana tenta pegar Effective Java (fila)
        
        // 4. Buscar livros
        System.out.println("\n4. BUSCANDO LIVROS:");
        System.out.println("Busca por 'Java':");
        biblioteca.buscarPorTitulo("Java").forEach(livro -> 
            System.out.println("  • " + livro));
        
        System.out.println("\nBusca por autor 'Tolkien':");
        biblioteca.buscarPorAutor("Tolkien").forEach(livro -> 
            System.out.println("  • " + livro));
        
        // 5. Listar por categoria
        biblioteca.listarPorCategoria();
        
        // 6. Devolver livro
        System.out.println("\n5. DEVOLVENDO LIVROS:");
        biblioteca.devolverLivro("978-0134685991", "U001"); // Ana devolve Effective Java
        
        // 7. Relatórios
        biblioteca.relatorioUsuariosAtivos();
        biblioteca.livrosMaisPopulares();
        biblioteca.estatisticas();
        
        // 8. Demonstrar Collections utilizadas
        demonstrarCollectionsUtilizadas();
    }
    
    private static void demonstrarCollectionsUtilizadas() {
        System.out.println("\n=== COLLECTIONS UTILIZADAS NO PROJETO ===");
        
        System.out.println("🗂️  HashMap<String, Livro> livrosPorIsbn:");
        System.out.println("   • Busca rápida O(1) por ISBN");
        System.out.println("   • Chave única garante não duplicação");
        
        System.out.println("\n👤 HashMap<String, Usuario> usuariosPorId:");
        System.out.println("   • Busca rápida O(1) por ID do usuário");
        System.out.println("   • Ideal para autenticação e operações");
        
        System.out.println("\n📚 TreeMap<String, List<Livro>> livrosPorCategoria:");
        System.out.println("   • Categorias sempre em ordem alfabética");
        System.out.println("   • Facilita relatórios organizados");
        
        System.out.println("\n📋 LinkedHashMap<String, List<String>> historicoEmprestimos:");
        System.out.println("   • Mantém ordem cronológica de cadastro");
        System.out.println("   • Histórico preservado para auditoria");
        
        System.out.println("\n⏰ Queue<String> (LinkedList) para filas de espera:");
        System.out.println("   • FIFO - primeiro a pedir, primeiro a receber");
        System.out.println("   • Gerencia lista de espera de forma justa");
        
        System.out.println("\n🔄 Streams para operações funcionais:");
        System.out.println("   • Busca e filtros declarativos");
        System.out.println("   • Ordenação e transformação de dados");
        System.out.println("   • Código mais legível e conciso");
        
        System.out.println("\n💡 LIÇÕES APRENDIDAS:");
        System.out.println("✅ HashMap: Quando precisar de busca rápida por chave única");
        System.out.println("✅ TreeMap: Quando precisar de dados sempre ordenados");
        System.out.println("✅ LinkedHashMap: Quando precisar de ordem + performance");
        System.out.println("✅ ArrayList: Para listas com acesso frequente por índice");
        System.out.println("✅ LinkedList/Queue: Para operações FIFO/LIFO");
        System.out.println("✅ Streams: Para transformações funcionais elegantes");
    }
}
```

### Resumo Final: Dominando o Collections Framework

```java
public class ResumoFinal {
    public static void main(String[] args) {
        System.out.println("=== RESUMO: COLLECTIONS FRAMEWORK ===\n");
        
        // LISTA DE VERIFICAÇÃO - Você aprendeu:
        System.out.println("📋 CHECKLIST DE APRENDIZADO:");
        System.out.println("✅ Entender a hierarquia Collection/Map");
        System.out.println("✅ Escolher a Collection certa para cada situação");
        System.out.println("✅ Usar Generics corretamente");
        System.out.println("✅ Aplicar Streams para operações funcionais");
        System.out.println("✅ Conhecer complexidade Big-O das operações");
        System.out.println("✅ Implementar um projeto real com múltiplas Collections");
        
        // TABELA RESUMO DE QUANDO USAR CADA COLLECTION
        System.out.println("\n📊 GUIA RÁPIDO DE DECISÃO:");
        System.out.println("┌─────────────────┬─────────────────┬─────────────────┐");
        System.out.println("│ PRECISA DE...   │ USE             │ PORQUE          │");
        System.out.println("├─────────────────┼─────────────────┼─────────────────┤");
        System.out.println("│ Lista ordenada  │ ArrayList       │ Acesso O(1)     │");
        System.out.println("│ Fila/Pilha      │ LinkedList      │ Insert/Del O(1) │");
        System.out.println("│ Sem duplicatas  │ HashSet         │ Contains O(1)   │");
        System.out.println("│ Set ordenado    │ TreeSet         │ Auto-ordenação  │");
        System.out.println("│ Chave-valor     │ HashMap         │ Get/Put O(1)    │");
        System.out.println("│ Map ordenado    │ TreeMap         │ Chaves sorted   │");
        System.out.println("│ Prioridade      │ PriorityQueue   │ Min-heap        │");
        System.out.println("└─────────────────┴─────────────────┴─────────────────┘");
        
        System.out.println("\n🚀 PRÓXIMOS PASSOS:");
        System.out.println("1. Pratique implementando outros sistemas");
        System.out.println("2. Estude Collections personalizadas");
        System.out.println("3. Explore bibliotecas como Guava e Apache Commons");
        System.out.println("4. Aprenda sobre Concurrent Collections");
        System.out.println("5. Domine padrões como Builder e Factory com Collections");
        
        System.out.println("\n💪 VOCÊ AGORA É CAPAZ DE:");
        System.out.println("• Escolher a Collection ideal para qualquer cenário");
        System.out.println("• Otimizar performance conhecendo complexidades");
        System.out.println("• Escrever código funcional com Streams");
        System.out.println("• Implementar sistemas robustos e eficientes");
        System.out.println("• Debugar problemas relacionados a Collections");
        
        System.out.println("\n🎯 PARABÉNS! Você dominou o Collections Framework do Java!");
    }
}
```println("Tamanho: " + frutas.size()); // 3
        System.out.println("Contém Banana? " + frutas.contains("Banana")); // true
        
        // Iterar (3 formas diferentes)
        
        // Forma 1: Enhanced for loop
        for (String fruta : frutas) {
            System.out.println(fruta);
        }
        
        // Forma 2: Iterator tradicional
        Iterator<String> it = frutas.iterator();
        while (it.hasNext()) {
            String fruta = it.next();
            System.out.println(fruta);
        }
        
        // Forma 3: Streams (Java 8+)
        frutas.stream().forEach(System.out::println);
        
        // Remover elemento
        frutas.remove("Banana");
        System.out.println("Após remoção: " + frutas); // [Maçã, Laranja]
    }
}
```

---

## 4. Interface List

### Características da List
- **Permite duplicatas**
- **Mantém ordem de inserção**
- **Acesso por índice** (como arrays)
- **Permite inserção em posições específicas**

### 4.1 ArrayList - A Lista Dinâmica

**Conceito:** Imagine um array que pode crescer. Internamente, usa um array que é redimensionado quando necessário.

```java
import java.util.*;

public class ArrayListExemplo {
    public static void main(String[] args) {
        List<Integer> numeros = new ArrayList<>();
        
        // Adicionar elementos
        numeros.add(10);    // Índice 0
        numeros.add(20);    // Índice 1
        numeros.add(30);    // Índice 2
        
        // Inserir em posição específica
        numeros.add(1, 15); // Insere 15 no índice 1
        // Resultado: [10, 15, 20, 30]
        
        // Acesso por índice (muito rápido - O(1))
        System.out.println("Elemento no índice 2: " + numeros.get(2)); // 20
        
        // Modificar elemento
        numeros.set(0, 100); // Troca 10 por 100
        
        // Encontrar índice de elemento
        int indice = numeros.indexOf(20);
        System.out.println("Índice do 20: " + indice); // 2
        
        // Sublista
        List<Integer> sublista = numeros.subList(1, 3); // [15, 20]
        System.out.println("Sublista: " + sublista);
        
        System.out.println("Lista final: " + numeros); // [100, 15, 20, 30]
    }
}
```

### 4.2 LinkedList - A Lista Encadeada

**Conceito:** Cada elemento "aponta" para o próximo, como uma corrente. Boa para inserções/remoções frequentes no meio.

```java
import java.util.*;

public class LinkedListExemplo {
    public static void main(String[] args) {
        LinkedList<String> lista = new LinkedList<>();
        
        // Adicionar no final
        lista.add("Primeiro");
        lista.add("Segundo");
        lista.add("Terceiro");
        
        // LinkedList tem métodos extras para início e fim
        lista.addFirst("Novo Primeiro"); // Adiciona no início
        lista.addLast("Novo Último");    // Adiciona no final
        
        System.out.println("Lista: " + lista);
        // [Novo Primeiro, Primeiro, Segundo, Terceiro, Novo Último]
        
        // Acessar primeiro e último elementos
        System.out.println("Primeiro: " + lista.getFirst());
        System.out.println("Último: " + lista.getLast());
        
        // Remover primeiro e último
        lista.removeFirst();
        lista.removeLast();
        
        System.out.println("Após remoções: " + lista);
        // [Primeiro, Segundo, Terceiro]
        
        // LinkedList também implementa Queue
        Queue<String> fila = new LinkedList<>();
        fila.offer("A"); // Adiciona no final
        fila.offer("B");
        fila.offer("C");
        
        System.out.println("Removido da fila: " + fila.poll()); // Remove do início: A
        System.out.println("Fila restante: " + fila); // [B, C]
    }
}
```

### 4.3 ArrayList vs LinkedList - Quando Usar Cada Uma?

```java
import java.util.*;

public class ListComparison {
    public static void main(String[] args) {
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        
        // Cenário 1: Muitas inserções no final
        long inicio = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            arrayList.add(i); // ArrayList é mais rápida aqui
        }
        long fimArrayList = System.nanoTime();
        
        inicio = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            linkedList.add(i);
        }
        long fimLinkedList = System.nanoTime();
        
        System.out.println("Inserção no final:");
        System.out.println("ArrayList: " + (fimArrayList - inicio) / 1_000_000 + "ms");
        System.out.println("LinkedList: " + (fimLinkedList - inicio) / 1_000_000 + "ms");
        
        // Cenário 2: Acesso aleatório por índice
        Random random = new Random();
        
        inicio = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            int indice = random.nextInt(arrayList.size());
            arrayList.get(indice); // ArrayList é MUITO mais rápida
        }
        long tempoArrayAccess = System.nanoTime() - inicio;
        
        inicio = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            int indice = random.nextInt(linkedList.size());
            linkedList.get(indice); // LinkedList é lenta para acesso aleatório
        }
        long tempoLinkedAccess = System.nanoTime() - inicio;
        
        System.out.println("\nAcesso aleatório:");
        System.out.println("ArrayList: " + tempoArrayAccess / 1_000_000 + "ms");
        System.out.println("LinkedList: " + tempoLinkedAccess / 1_000_000 + "ms");
    }
}
```

**Regra de Ouro:**
- **ArrayList:** Use quando fizer muitas leituras (`get()`) e poucas inserções no meio
- **LinkedList:** Use quando fizer muitas inserções/remoções no início/meio e poucas leituras aleatórias

---

## 5. Interface Set

### Características do Set
- **NÃO permite duplicatas**
- **Operações de conjunto:** união, interseção, diferença
- **Implementações variam na ordenação**

### 5.1 HashSet - Conjunto Não Ordenado

**Conceito:** Usa uma tabela hash internamente. Extremamente rápido para busca, mas não mantém ordem.

```java
import java.util.*;

public class HashSetExemplo {
    public static void main(String[] args) {
        Set<String> cores = new HashSet<>();
        
        // Adicionar elementos
        cores.add("Vermelho");
        cores.add("Azul");
        cores.add("Verde");
        cores.add("Azul"); // Duplicata - será ignorada!
        
        System.out.println("Cores: " + cores); 
        // Ordem pode variar: [Verde, Vermelho, Azul]
        System.out.println("Tamanho: " + cores.size()); // 3, não 4!
        
        // Verificação muito rápida - O(1)
        System.out.println("Contém Amarelo? " + cores.contains("Amarelo")); // false
        System.out.println("Contém Azul? " + cores.contains("Azul")); // true
        
        // Operações de conjunto
        Set<String> coresPrimarias = Set.of("Vermelho", "Azul", "Amarelo");
        Set<String> coresSecundarias = Set.of("Verde", "Laranja", "Roxo");
        
        // União (todas as cores)
        Set<String> todasCores = new HashSet<>(coresPrimarias);
        todasCores.addAll(coresSecundarias);
        System.out.println("União: " + todasCores);
        
        // Interseção (cores em comum)
        Set<String> intersecao = new HashSet<>(cores);
        intersecao.retainAll(coresPrimarias);
        System.out.println("Interseção: " + intersecao); // [Vermelho, Azul]
        
        // Diferença (cores que estão em 'cores' mas não em 'coresPrimarias')
        Set<String> diferenca = new HashSet<>(cores);
        diferenca.removeAll(coresPrimarias);
        System.out.println("Diferença: " + diferenca); // [Verde]
    }
}
```

### 5.2 TreeSet - Conjunto Ordenado

**Conceito:** Mantém elementos em ordem natural (ou definida por Comparator). Usa uma árvore binária balanceada.

```java
import java.util.*;

public class TreeSetExemplo {
    public static void main(String[] args) {
        // Ordenação natural para números
        Set<Integer> numeros = new TreeSet<>();
        numeros.add(50);
        numeros.add(20);
        numeros.add(80);
        numeros.add(10);
        numeros.add(30);
        
        System.out.println("Números ordenados: " + numeros);
        // [10, 20, 30, 50, 80] - sempre em ordem!
        
        // Ordenação natural para strings (alfabética)
        Set<String> nomes = new TreeSet<>();
        nomes.add("Carlos");
        nomes.add("Ana");
        nomes.add("Bruno");
        nomes.add("Diana");
        
        System.out.println("Nomes ordenados: " + nomes);
        // [Ana, Bruno, Carlos, Diana]
        
        // TreeSet tem métodos extras para navegação
        TreeSet<Integer> numerosTree = new TreeSet<>(numeros);
        
        System.out.println("Menor elemento: " + numerosTree.first()); // 10
        System.out.println("Maior elemento: " + numerosTree.last());  // 80
        
        // Elementos menores que 40
        System.out.println("< 40: " + numerosTree.headSet(40)); // [10, 20, 30]
        
        // Elementos maiores ou iguais a 30
        System.out.println(">= 30: " + numerosTree.tailSet(30)); // [30, 50, 80]
        
        // Elementos entre 20 e 60
        System.out.println("20-60: " + numerosTree.subSet(20, 60)); // [20, 30, 50]
        
        // Maior elemento menor que 45
        System.out.println("floor(45): " + numerosTree.floor(45)); // 30
        
        // Menor elemento maior que 45  
        System.out.println("ceiling(45): " + numerosTree.ceiling(45)); // 50
    }
}
```

### 5.3 LinkedHashSet - Conjunto com Ordem de Inserção

```java
import java.util.*;

public class LinkedHashSetExemplo {
    public static void main(String[] args) {
        // Mantém ordem de inserção (diferente do HashSet)
        Set<String> linguagens = new LinkedHashSet<>();
        linguagens.add("Java");
        linguagens.add("Python");
        linguagens.add("JavaScript");
        linguagens.add("C++");
        linguagens.add("Python"); // Duplicata ignorada
        
        System.out.println("Linguagens (ordem de inserção): " + linguagens);
        // [Java, Python, JavaScript, C++] - mantém a ordem!
        
        // Comparar com HashSet
        Set<String> linguagensHash = new HashSet<>(linguagens);
        System.out.println("Linguagens (HashSet): " + linguagensHash);
        // Ordem pode variar a cada execução
    }
}
```

### 5.4 Comparação entre Set Implementations

| Implementação | Ordenação | Complexidade Insert/Search | Permite null | Uso Recomendado |
|---------------|-----------|----------------------------|--------------|-----------------|
| `HashSet` | ❌ Não | O(1) | ✅ Sim | Busca rápida, sem ordem |
| `TreeSet` | ✅ Natural/Comparator | O(log n) | ❌ Não | Dados ordenados |
| `LinkedHashSet` | ✅ Inserção | O(1) | ✅ Sim | Ordem + performance |

---

## 6. Interface Queue

### Conceito de Fila (Queue)

**Analogia:** Como uma fila de banco - primeiro a entrar, primeiro a sair (FIFO - First In, First Out).

### 6.1 PriorityQueue - Fila com Prioridade

```java
import java.util.*;

public class PriorityQueueExemplo {
    public static void main(String[] args) {
        // Fila de prioridade com ordem natural (menor primeiro)
        Queue<Integer> filaPrioridade = new PriorityQueue<>();
        
        filaPrioridade.offer(30); // offer() adiciona elemento
        filaPrioridade.offer(10);
        filaPrioridade.offer(50);
        filaPrioridade.offer(20);
        
        System.out.println("Fila: " + filaPrioridade); 
        // [10, 20, 50, 30] - ordem interna (heap), não necessariamente ordenada
        
        // Remover sempre retorna o menor elemento
        while (!filaPrioridade.isEmpty()) {
            System.out.println("Removido: " + filaPrioridade.poll());
        }
        // Saída: 10, 20, 30, 50 (em ordem crescente!)
        
        // Exemplo prático: Sistema de atendimento hospitalar
        Queue<Paciente> filaHospital = new PriorityQueue<>();
        
        filaHospital.offer(new Paciente("João", 2)); // Prioridade média
        filaHospital.offer(new Paciente("Maria", 1)); // Alta prioridade
        filaHospital.offer(new Paciente("Pedro", 3)); // Baixa prioridade
        filaHospital.offer(new Paciente("Ana", 1));   // Alta prioridade
        
        System.out.println("\nAtendimento por prioridade:");
        while (!filaHospital.isEmpty()) {
            Paciente p = filaHospital.poll();
            System.out.println("Atendendo: " + p);
        }
        // Maria (1), Ana (1), João (2), Pedro (3)
    }
}

class Paciente implements Comparable<Paciente> {
    private String nome;
    private int prioridade; // 1 = alta, 2 = média, 3 = baixa
    
    public Paciente(String nome, int prioridade) {
        this.nome = nome;
        this.prioridade = prioridade;
    }
    
    @Override
    public int compareTo(Paciente outro) {
        return Integer.compare(this.prioridade, outro.prioridade);
    }
    
    @Override
    public String toString() {
        return nome + " (prioridade: " + prioridade + ")";
    }
}
```

### 6.2 Deque - Fila Dupla

```java
import java.util.*;

public class DequeExemplo {
    public static void main(String[] args) {
        // Deque permite inserção/remoção em ambas as extremidades
        Deque<String> deque = new LinkedList<>();
        
        // Adicionar no início e no fim
        deque.addFirst("B");
        deque.addLast("C");
        deque.addFirst("A");
        deque.addLast("D");
        
        System.out.println("Deque: " + deque); // [A, B, C, D]
        
        // Pode usar como Stack (LIFO - Last In, First Out)
        Deque<String> stack = new ArrayDeque<>();
        stack.push("Primeiro");
        stack.push("Segundo");
        stack.push("Terceiro");
        
        System.out.println("Stack pop: " + stack.pop()); // Terceiro
        System.out.println("Stack pop: " + stack.pop()); // Segundo
        System.out.println("Stack restante: " + stack); // [Primeiro]
        
        // Pode usar como Queue normal
        Deque<String> fila = new ArrayDeque<>();
        fila.offer("Primeiro na fila");
        fila.offer("Segundo na fila");
        fila.offer("Terceiro na fila");
        
        System.out.println("Fila poll: " + fila.poll()); // Primeiro na fila
        System.out.println("Fila restante: " + fila); // [Segundo na fila, Terceiro na fila]
    }
}
```

---

## 7. Interface Map

### Conceito de Map

**Analogia:** Como um dicionário - você procura uma palavra (chave) e encontra sua definição (valor).

### 7.1 HashMap - Mapa Não Ordenado

```java
import java.util.*;

public class HashMapExemplo {
    public static void main(String[] args) {
        // Mapa chave-valor
        Map<String, Integer> idades = new HashMap<>();
        
        // Adicionar pares chave-valor
        idades.put("João", 25);
        idades.put("Maria", 30);
        idades.put("Pedro", 22);
        idades.put("Ana", 28);
        
        // Buscar por chave - O(1) em média
        System.out.println("Idade do João: " + idades.get("João")); // 25
        
        // Verificar existência
        System.out.println("Contém Carlos? " + idades.containsKey("Carlos")); // false
        System.out.println("Alguém tem 30 anos? " + idades.containsValue(30)); // true
        
        // Valor padrão se chave não existir
        int idadeCarlos = idades.getOrDefault("Carlos", 0);
        System.out.println("Idade do Carlos: " + idadeCarlos); // 0
        
        // Atualizar valor
        idades.put("João", 26); // Sobrescreve o valor anterior
        
        // Iterar sobre o Map (3 formas)
        
        // 1. Iterar sobre chaves
        System.out.println("\nIterando sobre chaves:");
        for (String nome : idades.keySet()) {
            System.out.println(nome + " tem " + idades.get(nome) + " anos");
        }
        
        // 2. Iterar sobre valores
        System.out.println("\nIdades:");
        for (Integer idade : idades.values()) {
            System.out.println(idade + " anos");
        }
        
        // 3. Iterar sobre pares chave-valor (mais eficiente)
        System.out.println("\nPares chave-valor:");
        for (Map.Entry<String, Integer> entry : idades.entrySet()) {
            String nome = entry.getKey();
            Integer idade = entry.getValue();
            System.out.println(nome + " -> " + idade);
        }
        
        // Métodos úteis do Java 8+
        idades.forEach((nome, idade) -> 
            System.out.println(nome + " nasceu em " + (2024 - idade)));
        
        // Operações condicionais
        idades.putIfAbsent("Carlos", 35); // Só adiciona se não existir
        idades.replace("Maria", 30, 31);  // Só substitui se valor atual for 30
        
        System.out.println("\nMap final: " + idades);
    }
}
```

### 7.2 TreeMap - Mapa Ordenado

```java
import java.util.*;

public class TreeMapExemplo {
    public static void main(String[] args) {
        // TreeMap mantém chaves em ordem natural
        Map<String, String> capitais = new TreeMap<>();
        
        capitais.put("Brasil", "Brasília");
        capitais.put("Argentina", "Buenos Aires");
        capitais.put("Chile", "Santiago");
        capitais.put("Peru", "Lima");
        capitais.put("Uruguai", "Montevidéu");
        
        System.out.println("Capitais ordenadas por país:");
        capitais.forEach((pais, capital) -> 
            System.out.println(pais + " -> " + capital));
        // Saída em ordem alfabética por país!
        
        // TreeMap tem métodos de navegação
        TreeMap<Integer, String> notas = new TreeMap<>();
        notas.put(85, "Ana");
        notas.put(92, "Bruno");
        notas.put(78, "Carlos");
        notas.put(95, "Diana");
        notas.put(88, "Eduardo");
        
        System.out.println("\nNotas em ordem:");
        notas.forEach((nota, aluno) -> 
            System.out.println(aluno + ": " + nota));
        
        // Métodos especiais do TreeMap
        System.out.println("Menor nota: " + notas.firstKey()); // 78
        System.out.println("Maior nota: " + notas.lastKey());  // 95
        
        // Notas abaixo de 90
        Map<Integer, String> notasBaixas = notas.headMap(90);
        System.out.println("Notas < 90: " + notasBaixas);
        
        // Notas acima ou igual a 90
        Map<Integer, String> notasAltas = notas.tailMap(90);
        System.out.println("Notas >= 90: " + notasAltas);
    }
}
```

### 7.3 LinkedHashMap - Mapa com Ordem de Inserção

```java
import java.util.*;

public class LinkedHashMapExemplo {
    public static void main(String[] args) {
        // Mantém ordem de inserção
        Map<String, Double> produtos = new LinkedHashMap<>();
        
        produtos.put("Notebook", 2500.00);
        produtos.put("Mouse", 50.00);
        produtos.put("Teclado", 150.00);
        produtos.put("Monitor", 800.00);
        
        System.out.println("Produtos na ordem de inserção:");
        produtos.forEach((produto, preco) -> 
            System.out.println(produto + ": R$ " + preco));
        
        // LinkedHashMap pode ser configurado para ordem de acesso
        // (LRU - Least Recently Used)
        Map<String, String> cache = new LinkedHashMap<String, String>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > 3; // Cache máximo de 3 elementos
            }
        };
        
        cache.put("página1", "conteudo1");
        cache.put("página2", "conteudo2");
        cache.put("página3", "conteudo3");
        System.out.println("Cache: " + cache);
        
        // Acessar página1 (move para o final)
        cache.get("página1");
        System.out.println("Após acessar página1: " + cache);
        
        // Adicionar página4 (remove a menos recentemente usada)
        cache.put("página4", "conteudo4");
        System.out.println("Após adicionar página4: " + cache);
        // página2 foi removida por ser a menos recentemente usada
    }
}
```

---

## 8. Comparação de Performance

### Tabela de Complexidades Big-O

| Operação | ArrayList | LinkedList | HashSet | TreeSet | HashMap | TreeMap |
|----------|-----------|------------|---------|---------|---------|---------|
| **add()** | O(1) amortizado | O(1) | O(1) | O(log n) | O(1) | O(log n) |
| **get(index)** | O(1) | O(n) | N/A | N/A | N/A | N/A |
| **get(key)** | N/A | N/A | N/A | N/A | O(1) | O(log n) |
| **contains()** | O(n) | O(n) | O(1) | O(log n) | O(1) | O(log n) |
| **remove()** | O(n) | O(1)* | O(1) | O(log n) | O(1) | O(log n) |
| **iterator** | O(1) | O(1) | O(1) | O(1) | O(1) | O(1) |

*O(1) se você tiver referência direta ao nó

### Exemplo Prático de Performance

```java
import java.util.*;

public class PerformanceTest {
    private static final int SIZE = 100_000;
    
    public static void main(String[] args) {
        System.out.println("Testando inserção de " + SIZE + " elementos:\n");
        
        // Teste 3: Busca por elemento
        testBusca();
        
        // Teste 4: Remoção de elementos
        testRemocao();
    }
    
    private static void testInsercaoFinal() {
        System.out.println("=== INSERÇÃO NO FINAL ===");
        
        // ArrayList
        List<Integer> arrayList = new ArrayList<>();
        long inicio = System.nanoTime();
        for (int i = 0; i < SIZE; i++) {
            arrayList.add(i);
        }
        long tempoArrayList = System.nanoTime() - inicio;
        
        // LinkedList
        List<Integer> linkedList = new LinkedList<>();
        inicio = System.nanoTime();
        for (int i = 0; i < SIZE; i++) {
            linkedList.add(i);
        }
        long tempoLinkedList = System.nanoTime() - inicio;
        
        System.out.printf("ArrayList:  %d ms%n", tempoArrayList / 1_000_000);
        System.out.printf("LinkedList: %d ms%n", tempoLinkedList / 1_000_000);
        System.out.println();
    }
    
    private static void testInsercaoInicio() {
        System.out.println("=== INSERÇÃO NO INÍCIO ===");
        
        // ArrayList (inserção no índice 0)
        List<Integer> arrayList = new ArrayList<>();
        long inicio = System.nanoTime();
        for (int i = 0; i < SIZE / 100; i++) { // Menos elementos para não demorar muito
            arrayList.add(0, i);
        }
        long tempoArrayList = System.nanoTime() - inicio;
        
        // LinkedList
        LinkedList<Integer> linkedList = new LinkedList<>();
        inicio = System.nanoTime();
        for (int i = 0; i < SIZE / 100; i++) {
            linkedList.addFirst(i);
        }
        long tempoLinkedList = System.nanoTime() - inicio;
        
        System.out.printf("ArrayList:  %d ms (inserindo %d elementos)%n", 
                         tempoArrayList / 1_000_000, SIZE / 100);
        System.out.printf("LinkedList: %d ms (inserindo %d elementos)%n", 
                         tempoLinkedList / 1_000_000, SIZE / 100);
        System.out.println();
    }
    
    private static void testBusca() {
        System.out.println("=== BUSCA POR ELEMENTO ===");
        
        // Preparar dados
        List<Integer> arrayList = new ArrayList<>();
        Set<Integer> hashSet = new HashSet<>();
        Set<Integer> treeSet = new TreeSet<>();
        
        for (int i = 0; i < SIZE; i++) {
            arrayList.add(i);
            hashSet.add(i);
            treeSet.add(i);
        }
        
        Random random = new Random();
        int elementoBusca = random.nextInt(SIZE);
        
        // ArrayList
        long inicio = System.nanoTime();
        boolean found = arrayList.contains(elementoBusca);
        long tempoArrayList = System.nanoTime() - inicio;
        
        // HashSet
        inicio = System.nanoTime();
        found = hashSet.contains(elementoBusca);
        long tempoHashSet = System.nanoTime() - inicio;
        
        // TreeSet
        inicio = System.nanoTime();
        found = treeSet.contains(elementoBusca);
        long tempoTreeSet = System.nanoTime() - inicio;
        
        System.out.printf("ArrayList:  %d ns%n", tempoArrayList);
        System.out.printf("HashSet:    %d ns%n", tempoHashSet);
        System.out.printf("TreeSet:    %d ns%n", tempoTreeSet);
        System.out.println();
    }
    
    private static void testRemocao() {
        System.out.println("=== REMOÇÃO DE ELEMENTOS ===");
        
        // Criar coleções com dados
        List<Integer> arrayList = new ArrayList<>();
        Set<Integer> hashSet = new HashSet<>();
        Set<Integer> treeSet = new TreeSet<>();
        
        for (int i = 0; i < SIZE / 10; i++) {
            arrayList.add(i);
            hashSet.add(i);
            treeSet.add(i);
        }
        
        Random random = new Random();
        int elementoRemover = random.nextInt(SIZE / 10);
        
        // ArrayList (busca + remoção)
        long inicio = System.nanoTime();
        arrayList.remove(Integer.valueOf(elementoRemover));
        long tempoArrayList = System.nanoTime() - inicio;
        
        // HashSet
        inicio = System.nanoTime();
        hashSet.remove(elementoRemover);
        long tempoHashSet = System.nanoTime() - inicio;
        
        // TreeSet
        inicio = System.nanoTime();
        treeSet.remove(elementoRemover);
        long tempoTreeSet = System.nanoTime() - inicio;
        
        System.out.printf("ArrayList:  %d ns%n", tempoArrayList);
        System.out.printf("HashSet:    %d ns%n", tempoHashSet);
        System.out.printf("TreeSet:    %d ns%n", tempoTreeSet);
        System.out.println();
    }
}
```

### Resumo: Quando Usar Cada Coleção

```java
public class EscolhaCerta {
    public static void main(String[] args) {
        // ✅ Use ArrayList quando:
        // - Precisar de acesso rápido por índice
        // - Fizer mais leituras que inserções/remoções
        // - Não se importar com duplicatas
        List<String> lista = new ArrayList<>();
        
        // ✅ Use LinkedList quando:
        // - Fizer muitas inserções/remoções no início/meio
        // - Usar como Queue ou Stack
        // - Não precisar de acesso aleatório
        Queue<String> fila = new LinkedList<>();
        
        // ✅ Use HashSet quando:
        // - Precisar de busca muito rápida
        // - Não permitir duplicatas
        // - Não se importar com ordem
        Set<String> conjunto = new HashSet<>();
        
        // ✅ Use TreeSet quando:
        // - Precisar de dados sempre ordenados
        // - Quiser operações de range (subSet, headSet, tailSet)
        // - Não permitir duplicatas
        Set<String> conjuntoOrdenado = new TreeSet<>();
        
        // ✅ Use HashMap quando:
        // - Precisar de busca por chave muito rápida
        // - Não se importar com ordem
        Map<String, Integer> mapa = new HashMap<>();
        
        // ✅ Use TreeMap quando:
        // - Precisar de chaves sempre ordenadas
        // - Quiser operações de range
        Map<String, Integer> mapaOrdenado = new TreeMap<>();
        
        // ✅ Use LinkedHashMap quando:
        // - Quiser performance do HashMap + ordem de inserção
        // - Implementar cache LRU
        Map<String, Integer> mapaComOrdem = new LinkedHashMap<>();
    }
}
```

---

## 9. Utilitários Collections e Arrays

### 9.1 Classe Collections

A classe `Collections` fornece métodos estáticos úteis para manipular coleções:

```java
import java.util.*;

public class CollectionsUtilExemplo {
    public static void main(String[] args) {
        List<Integer> numeros = Arrays.asList(5, 2, 8, 1, 9, 3);
        
        System.out.println("Lista original: " + numeros);
        
        // 1. Ordenação
        Collections.sort(numeros);
        System.out.println("Ordenada: " + numeros);
        
        // 2. Ordenação reversa
        Collections.sort(numeros, Collections.reverseOrder());
        System.out.println("Ordem decrescente: " + numeros);
        
        // 3. Embaralhar
        Collections.shuffle(numeros);
        System.out.println("Embaralhada: " + numeros);
        
        // 4. Busca binária (lista deve estar ordenada!)
        Collections.sort(numeros);
        int indice = Collections.binarySearch(numeros, 5);
        System.out.println("Índice do 5: " + indice);
        
        // 5. Min e Max
        System.out.println("Menor: " + Collections.min(numeros));
        System.out.println("Maior: " + Collections.max(numeros));
        
        // 6. Frequência de um elemento
        List<String> palavras = Arrays.asList("java", "python", "java", "c++", "java");
        int freq = Collections.frequency(palavras, "java");
        System.out.println("Frequência de 'java': " + freq); // 3
        
        // 7. Rotacionar lista
        List<String> lista = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        Collections.rotate(lista, 2);
        System.out.println("Lista rotacionada: " + lista); // [D, E, A, B, C]
        
        // 8. Inverter lista
        Collections.reverse(lista);
        System.out.println("Lista invertida: " + lista);
        
        // 9. Preencher lista
        List<String> vazia = new ArrayList<>(Arrays.asList("", "", "", ""));
        Collections.fill(vazia, "X");
        System.out.println("Lista preenchida: " + vazia); // [X, X, X, X]
        
        // 10. Copiar lista
        List<String> origem = Arrays.asList("A", "B", "C");
        List<String> destino = Arrays.asList("", "", "");
        Collections.copy(destino, origem);
        System.out.println("Lista copiada: " + destino); // [A, B, C]
        
        // 11. Substituir elementos
        List<Integer> nums = new ArrayList<>(Arrays.asList(1, 2, 3, 2, 4, 2));
        Collections.replaceAll(nums, 2, 99);
        System.out.println("Após substituição: " + nums); // [1, 99, 3, 99, 4, 99]
        
        // 12. Coleções imutáveis
        List<String> imutavel = Collections.unmodifiableList(Arrays.asList("A", "B", "C"));
        try {
            imutavel.add("D"); // Vai lançar exceção!
        } catch (UnsupportedOperationException e) {
            System.out.println("Lista é imutável!");
        }
        
        // 13. Coleções vazias imutáveis
        List<String> listaVazia = Collections.emptyList();
        Set<String> setVazio = Collections.emptySet();
        Map<String, String> mapVazio = Collections.emptyMap();
        
        // 14. Singleton (coleção com um elemento)
        Set<String> singleton = Collections.singleton("único");
        System.out.println("Singleton: " + singleton);
    }
}
```

### 9.2 Classe Arrays

```java
import java.util.*;

public class ArraysUtilExemplo {
    public static void main(String[] args) {
        // 1. Converter array para lista
        String[] arrayNomes = {"Ana", "Bruno", "Carlos"};
        List<String> listaNomes = Arrays.asList(arrayNomes);
        System.out.println("Array como lista: " + listaNomes);
        
        // ⚠️ Cuidado: a lista retornada tem tamanho fixo!
        try {
            listaNomes.add("Diana"); // Vai dar erro!
        } catch (UnsupportedOperationException e) {
            System.out.println("Lista de tamanho fixo!");
        }
        
        // Para lista mutável, use:
        List<String> listaMutavel = new ArrayList<>(Arrays.asList(arrayNomes));
        listaMutavel.add("Diana"); // Agora funciona!
        System.out.println("Lista mutável: " + listaMutavel);
        
        // 2. Ordenar array
        int[] numeros = {5, 2, 8, 1, 9, 3};
        Arrays.sort(numeros);
        System.out.println("Array ordenado: " + Arrays.toString(numeros));
        
        // 3. Busca binária em array (deve estar ordenado!)
        int indice = Arrays.binarySearch(numeros, 5);
        System.out.println("Índice do 5: " + indice);
        
        // 4. Preencher array
        int[] array = new int[5];
        Arrays.fill(array, 42);
        System.out.println("Array preenchido: " + Arrays.toString(array));
        
        // 5. Copiar array
        int[] original = {1, 2, 3, 4, 5};
        int[] copia = Arrays.copyOf(original, 3); // Copia 3 elementos
        System.out.println("Cópia parcial: " + Arrays.toString(copia));
        
        int[] copiaCompleta = Arrays.copyOf(original, 7); // Expande com zeros
        System.out.println("Cópia expandida: " + Arrays.toString(copiaCompleta));
        
        // 6. Comparar arrays
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        int[] array3 = {3, 2, 1};
        
        System.out.println("array1 == array2: " + Arrays.equals(array1, array2)); // true
        System.out.println("array1 == array3: " + Arrays.equals(array1, array3)); // false
        
        // 7. toString para arrays multidimensionais
        int[][] matriz = {{1, 2}, {3, 4}};
        System.out.println("Matriz: " + Arrays.deepToString(matriz));
        
        // 8. Comparar arrays multidimensionais
        int[][] matriz1 = {{1, 2}, {3, 4}};
        int[][] matriz2 = {{1, 2}, {3, 4}};
        System.out.println("Matrizes iguais: " + Arrays.deepEquals(matriz1, matriz2));
        
        // 9. Stream de array (Java 8+)
        String[] frutas = {"maçã", "banana", "laranja"};
        Arrays.stream(frutas)
              .map(String::toUpperCase)
              .forEach(System.out::println);
    }
}
```

---

## 10. Java Generics

### 10.1 Por que usar Generics?

**Sem Generics (Java < 1.5):**
```java
// Problemático - sem type safety
List lista = new ArrayList();
lista.add("String");
lista.add(123);
lista.add(new Date());

// Precisa fazer cast e pode dar erro em runtime
String texto = (String) lista.get(0); // OK
String numero = (String) lista.get(1); // ClassCastException em runtime!
```

**Com Generics (Java 1.5+):**
```java
// Type safety em compile time
List<String> lista = new ArrayList<String>();
lista.add("String");
// lista.add(123); // Erro de compilação!

// Não precisa de cast
String texto = lista.get(0); // Seguro e limpo
```

### 10.2 Sintaxe Básica de Generics

```java
import java.util.*;

public class GenericsBasico {
    public static void main(String[] args) {
        // 1. Generics com um tipo
        List<String> textos = new ArrayList<String>();
        Set<Integer> numeros = new HashSet<Integer>();
        
        // 2. Diamond operator (Java 7+)
        List<String> textosSimples = new ArrayList<>(); // Infere o tipo
        Map<String, Integer> mapa = new HashMap<>();
        
        // 3. Generics aninhados
        List<List<String>> listaDeListas = new ArrayList<>();
        Map<String, List<Integer>> mapaDeListas = new HashMap<>();
        
        // 4. Wildcards
        
        // ? extends (Producer/Upper bound) - só pode ler
        List<? extends Number> numerosProducer = new ArrayList<Integer>();
        // numerosProducer.add(10); // ERRO! Não pode adicionar
        Number numero = numerosProducer.get(0); // OK - pode ler
        
        // ? super (Consumer/Lower bound) - só pode escrever
        List<? super Integer> numerosConsumer = new ArrayList<Number>();
        numerosConsumer.add(10); // OK - pode adicionar Integer
        // Integer valor = numerosConsumer.get(0); // ERRO! Só retorna Object
        
        // Wildcard sem bound
        List<?> qualquerCoisa = new ArrayList<String>();
        // qualquerCoisa.add("teste"); // ERRO! Não pode adicionar nada
        Object obj = qualquerCoisa.get(0); // OK - retorna Object
        
        demonstrarPECS();
    }
    
    // Demonstrar princípio PECS: Producer Extends, Consumer Super
    private static void demonstrarPECS() {
        List<Integer> inteiros = Arrays.asList(1, 2, 3, 4, 5);
        List<Number> numeros = new ArrayList<>();
        
        // Producer Extends - lendo de uma coleção
        copiar(inteiros, numeros);
        System.out.println("Números copiados: " + numeros);
        
        // Consumer Super - escrevendo em uma coleção  
        List<Object> objetos = new ArrayList<>();
        copiar(inteiros, objetos);
        System.out.println("Como objetos: " + objetos);
    }
    
    // Método genérico usando PECS
    private static <T> void copiar(List<? extends T> origem, List<? super T> destino) {
        for (T item : origem) {
            destino.add(item);
        }
    }
}
```

### 10.3 Criando Classes Genéricas

```java
// Classe genérica simples
class Caixa<T> {
    private T conteudo;
    
    public void guardar(T item) {
        this.conteudo = item;
    }
    
    public T recuperar() {
        return conteudo;
    }
    
    public boolean estaVazia() {
        return conteudo == null;
    }
}

// Classe genérica com múltiplos tipos
class Par<K, V> {
    private K chave;
    private V valor;
    
    public Par(K chave, V valor) {
        this.chave = chave;
        this.valor = valor;
    }
    
    public K getChave() { return chave; }
    public V getValor() { return valor; }
    
    @Override
    public String toString() {
        return "Par{" + chave + " -> " + valor + "}";
    }
}

// Classe genérica com bounds
class NumericoCaixa<T extends Number> {
    private T numero;
    
    public NumericoCaixa(T numero) {
        this.numero = numero;
    }
    
    public double getValorDouble() {
        return numero.doubleValue(); // Pode usar métodos de Number
    }
    
    public boolean ehMaiorQue(NumericoCaixa<? extends Number> outra) {
        return this.numero.doubleValue() > outra.numero.doubleValue();
    }
}

public class ClassesGenericasExemplo {
    public static void main(String[] args) {
        // Usando Caixa genérica
        Caixa<String> caixaTexto = new Caixa<>();
        caixaTexto.guardar("Olá, Generics!");
        String texto = caixaTexto.recuperar();
        System.out.println("Conteúdo: " + texto);
        
        Caixa<Integer> caixaNumero = new Caixa<>();
        caixaNumero.guardar(42);
        Integer numero = caixaNumero.recuperar();
        System.out.println("Número: " + numero);
        
        // Usando Par genérico
        Par<String, Integer> pessoa = new Par<>("João", 25);
        System.out.println("Pessoa: " + pessoa);
        
        Par<Integer, String> codigo = new Par<>(404, "Not Found");
        System.out.println("Status: " + codigo);
        
        // Usando NumericoCaixa com bounds
        NumericoCaixa<Integer> caixaInt = new NumericoCaixa<>(10);
        NumericoCaixa<Double> caixaDouble = new NumericoCaixa<>(15.5);
        
        System.out.println("Int como double: " + caixaInt.getValorDouble());
        System.out.println("Double é maior que Int? " + caixaDouble.ehMaiorQue(caixaInt));
    }
}
```

### 10.4 Métodos Genéricos

```java
import java.util.*;

public class MetodosGenericos {
    
    // Método genérico estático
    public static <T> void trocar(List<T> lista, int i, int j) {
        T temp = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temp);
    }
    
    // Método genérico com bounds
    public static <T extends Comparable<T>> T maximo(T a, T b, T c) {
        T max = a;
        if (b.compareTo(max) > 0) max = b;
        if (c.compareTo(max) > 0) max = c;
        return max;
    }
    
    // Método genérico com wildcard
    public static double somarLista(List<? extends Number> numeros) {
        double soma = 0.0;
        for (Number num : numeros) {
            soma += num.doubleValue();
        }
        return soma;
    }
    
    // Método genérico que retorna lista
    public static <T> List<T> criarLista(T... elementos) {
        List<T> lista = new ArrayList<>();
        for (T elemento : elementos) {
            lista.add(elemento);
        }
        return lista;
    }
    
    public static void main(String[] args) {
        // Testando trocar
        List<String> nomes = new ArrayList<>(Arrays.asList("Ana", "Bruno", "Carlos"));
        System.out.println("Antes: " + nomes);
        trocar(nomes, 0, 2);
        System.out.println("Depois: " + nomes);
        
        // Testando máximo
        String maxString = maximo("banana", "maçã", "zebra");
        Integer maxInt = maximo(10, 20, 15);
        Double maxDouble = maximo(3.14, 2.71, 1.41);
        
        System.out.println("Maior string: " + maxString);
        System.out.println("Maior int: " + maxInt);
        System.out.println("Maior double: " + maxDouble);
        
        // Testando soma com diferentes tipos de Number
        List<Integer> inteiros = Arrays.asList(1, 2, 3, 4, 5);
        List<Double> decimais = Arrays.asList(1.1, 2.2, 3.3);
        
        System.out.println("Soma inteiros: " + somarLista(inteiros));
        System.out.println("Soma decimais: " + somarLista(decimais));
        
        // Testando criar lista
        List<String> frutas = criarLista("maçã", "banana", "laranja");
        List<Integer> nums = criarLista(1, 2, 3, 4, 5);
        
        System.out.println("Frutas: " + frutas);
        System.out.println("Números: " + nums);
    }
}
```

---

## 11. Streams e Collections

### 11.1 Introdução aos Streams (Java 8+)

**Conceito:** Stream é como uma "linha de produção" onde você pode processar dados de forma declarativa (diz O QUE quer, não COMO fazer).

```java
import java.util.*;
import java.util.stream.*;

public class StreamsBasico {
    public static void main(String[] args) {
        List<String> nomes = Arrays.asList(
            "Ana", "Bruno", "Carlos", "Diana", 
            "Eduardo", "Fernanda", "Gabriel"
        );
        
        // Forma tradicional (imperativa)
        System.out.println("=== FORMA TRADICIONAL ===");
        List<String> nomesComA = new ArrayList<>();
        for (String nome : nomes) {
            if (nome.startsWith("A")) {
                nomesComA.add(nome.toUpperCase());
            }
        }
        Collections.sort(nomesComA);
        System.out.println("Nomes com A: " + nomesComA);
        
        // Forma com Streams (declarativa)
        System.out.println("\n=== COM STREAMS ===");
        List<String> resultado = nomes.stream()
            .filter(nome -> nome.startsWith("A"))  // Filtrar
            .map(String::toUpperCase)              // Transformar
            .sorted()                              // Ordenar
            .collect(Collectors.toList());         // Coletar resultado
        
        System.out.println("Nomes com A: " + resultado);
        
        // Outras operações úteis
        System.out.println("\n=== OUTRAS OPERAÇÕES ===");
        
        // Contar elementos
        long quantidade = nomes.stream()
            .filter(nome -> nome.length() > 5)
            .count();
        System.out.println("Nomes com mais de 5 letras: " + quantidade);
        
        // Encontrar primeiro
        Optional<String> primeiro = nomes.stream()
            .filter(nome -> nome.startsWith("C"))
            .findFirst();
        primeiro.ifPresent(nome -> System.out.println("Primeiro com C: " + nome));
        
        // Verificar se todos/algum atendem condição
        boolean todosTemMaisDe2Letras = nomes.stream()
            .allMatch(nome -> nome.length() > 2);
        System.out.println("Todos têm mais de 2 letras: " + todosTemMaisDe2Letras);
        
        boolean algumComecaComZ = nomes.stream()
            .anyMatch(nome -> nome.startsWith("Z"));
        System.out.println("Algum começa com Z: " + algumComecaComZ);
    }
}
```

### 11.2 Operações Intermediárias vs Terminais

```java
import java.util.*;
import java.util.stream.*;

public class OperacoesStream {
    public static void main(String[] args) {
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        System.out.println("=== OPERAÇÕES INTERMEDIÁRIAS ===");
        
        // filter() - filtrar elementos
        List<Integer> pares = numeros.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        System.out.println("Pares: " + pares);
        
        // map() - transformar elementos
        List<Integer> quadrados = numeros.stream()
            .map(n -> n * n)
            .collect(Collectors.toList());
        System.out.println("Quadrados: " + quadrados);
        
        // distinct() - remover duplicatas
        List<Integer> comDuplicatas = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 4, 5);
        List<Integer> semDuplicatas = comDuplicatas.stream()
            .distinct()
            .collect(Collectors.toList());
        System.out.println("Sem duplicatas: " + semDuplicatas);
        
        // sorted() - ordenar
        List<String> palavras = Arrays.asList("zebra", "abelha", "gato", "cão");
        List<String> ordenadas = palavras.stream()
            .sorted()
            .collect(Collectors.toList());
        System.out.println("Palavras ordenadas: " + ordenadas);
        
        // limit() e skip()
        List<Integer> primeiros3 = numeros.stream()
            .limit(3)
            .collect(Collectors.toList());
        System.out.println("Primeiros 3: " + primeiros3);
        
        List<Integer> pular2 = numeros.stream()
            .skip(2)
            .limit(3)
            .collect(Collectors.toList());
        System.out.println("Pular 2, pegar 3: " + pular2);
        
        // flatMap() - achatar estruturas aninhadas
        List<List<String>> listaDeListas = Arrays.asList(
            Arrays.asList("a", "b"),
            Arrays.asList("c", "d", "e"),
            Arrays.asList("f")
        );
        
        List<String> achatada = listaDeListas.stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        System.out.println("Lista achatada: " + achatada);
        
        System.out.println("\n=== OPERAÇÕES TERMINAIS ===");
        
        // forEach() - executar ação para cada elemento
        System.out.print("Números: ");
        numeros.stream()
            .filter(n -> n <= 5)
            .forEach(n -> System.out.print(n + " "));
        System.out.println();
        
        // reduce() - reduzir a um valor
        Optional<Integer> soma = numeros.stream()
            .reduce((a, b) -> a + b);
        System.out.println("Soma: " + soma.orElse(0));
        
        int produto = numeros.stream()
            .filter(n -> n <= 4)
            .reduce(1, (a, b) -> a * b);
        System.out.println("Produto dos 4 primeiros: " + produto);
        
        // collect() com diferentes Collectors
        String texto = palavras.stream()
            .collect(Collectors.joining(", "));
        System.out.println("Texto unido: " + texto);
        
        Map<Integer, List<String>> porTamanho = palavras.stream()
            .collect(Collectors.groupingBy(String::length));
        System.out. 1: Inserção no final
        testInsercaoFinal();
        
        // Teste 2: Inserção no início
        testInsercaoInicio();
        
        // Teste