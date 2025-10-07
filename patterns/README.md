# 🎯 Padrões de Desenvolvimento

Princípios, padrões e boas práticas fundamentais para desenvolvimento de software de qualidade.

---

## 📋 Índice

- [SOLID](#-solid-principles)
- [Object Calisthenics](#️-object-calisthenics)
- [CQRS](#-cqrs-command-query-responsibility-segregation)

---

## 🔷 SOLID Principles

📄 **[solid.md](./solid.md)**

Os 5 princípios fundamentais da programação orientada a objetos:

### 1. **S** - Single Responsibility Principle (SRP)
> "Uma classe deve ter apenas uma razão para mudar"

```java
// ❌ Múltiplas responsabilidades
class Usuario {
    void salvar() { /* salva no banco */ }
    void enviarEmail() { /* envia email */ }
    void gerarRelatorio() { /* gera PDF */ }
}

// ✅ Responsabilidade única
class Usuario {
    private String nome;
    private String email;
}

class UsuarioRepository {
    void salvar(Usuario usuario) { /* salva no banco */ }
}

class EmailService {
    void enviarBoasVindas(Usuario usuario) { /* envia email */ }
}

class RelatorioService {
    void gerarRelatorioUsuarios() { /* gera PDF */ }
}
```

---

### 2. **O** - Open/Closed Principle (OCP)
> "Aberto para extensão, fechado para modificação"

```java
// ❌ Requer modificação para adicionar novos tipos
class CalculadoraDesconto {
    double calcular(String tipo, double valor) {
        if (tipo.equals("BLACK_FRIDAY")) return valor * 0.5;
        if (tipo.equals("NATAL")) return valor * 0.3;
        return valor;
    }
}

// ✅ Aberto para extensão via polimorfismo
interface EstrategiaDesconto {
    double calcular(double valor);
}

class DescontoBlackFriday implements EstrategiaDesconto {
    public double calcular(double valor) { return valor * 0.5; }
}

class DescontoNatal implements EstrategiaDesconto {
    public double calcular(double valor) { return valor * 0.3; }
}

class CalculadoraDesconto {
    double calcular(EstrategiaDesconto estrategia, double valor) {
        return estrategia.calcular(valor);
    }
}
```

---

### 3. **L** - Liskov Substitution Principle (LSP)
> "Objetos de uma superclasse devem ser substituíveis por objetos de suas subclasses"

```java
// ❌ Viola LSP (quadrado não é substituível por retângulo)
class Retangulo {
    protected int largura, altura;
    
    void setLargura(int largura) { this.largura = largura; }
    void setAltura(int altura) { this.altura = altura; }
    int getArea() { return largura * altura; }
}

class Quadrado extends Retangulo {
    @Override
    void setLargura(int lado) { 
        this.largura = lado;
        this.altura = lado; // Efeito colateral!
    }
}

// ✅ Respeita LSP
interface Forma {
    int getArea();
}

class Retangulo implements Forma {
    private int largura, altura;
    
    Retangulo(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
    }
    
    public int getArea() { return largura * altura; }
}

class Quadrado implements Forma {
    private int lado;
    
    Quadrado(int lado) { this.lado = lado; }
    
    public int getArea() { return lado * lado; }
}
```

---

### 4. **I** - Interface Segregation Principle (ISP)
> "Clientes não devem ser forçados a depender de interfaces que não usam"

```java
// ❌ Interface "gorda"
interface Trabalhador {
    void trabalhar();
    void comer();
    void dormir();
}

class Robo implements Trabalhador {
    public void trabalhar() { /* OK */ }
    public void comer() { /* Robô não come! */ }
    public void dormir() { /* Robô não dorme! */ }
}

// ✅ Interfaces segregadas
interface Trabalhavel {
    void trabalhar();
}

interface Alimentavel {
    void comer();
}

interface Descansavel {
    void dormir();
}

class Humano implements Trabalhavel, Alimentavel, Descansavel {
    public void trabalhar() { /* ... */ }
    public void comer() { /* ... */ }
    public void dormir() { /* ... */ }
}

class Robo implements Trabalhavel {
    public void trabalhar() { /* ... */ }
}
```

---

### 5. **D** - Dependency Inversion Principle (DIP)
> "Dependa de abstrações, não de implementações concretas"

```java
// ❌ Dependência de implementação concreta
class Usuario {
    private MySQLDatabase database; // Acoplado ao MySQL
    
    void salvar() {
        database.insert(this);
    }
}

// ✅ Dependência de abstração
interface Database {
    void insert(Usuario usuario);
}

class MySQLDatabase implements Database {
    public void insert(Usuario usuario) { /* MySQL */ }
}

class PostgreSQLDatabase implements Database {
    public void insert(Usuario usuario) { /* PostgreSQL */ }
}

class Usuario {
    private Database database; // Depende da abstração
    
    Usuario(Database database) {
        this.database = database;
    }
    
    void salvar() {
        database.insert(this);
    }
}
```

---

## 🏋️ Object Calisthenics

📄 **[Object-Calisthenics.md](./Object-Calisthenics.md)**

9 regras para código limpo e manutenível:

### 1. **Um nível de indentação por método**

```java
// ❌ Múltiplos níveis
class Pedido {
    void processar(List<Item> itens) {
        for (Item item : itens) {
            if (item.isValido()) {
                if (item.temEstoque()) {
                    if (item.temDesconto()) {
                        // ...
                    }
                }
            }
        }
    }
}

// ✅ Um nível
class Pedido {
    void processar(List<Item> itens) {
        itens.stream()
            .filter(Item::isValido)
            .filter(Item::temEstoque)
            .forEach(this::aplicarDesconto);
    }
    
    private void aplicarDesconto(Item item) {
        if (item.temDesconto()) {
            item.aplicarDesconto();
        }
    }
}
```

---

### 2. **Não use ELSE**

```java
// ❌ Com else
String verificarIdade(int idade) {
    if (idade >= 18) {
        return "Maior de idade";
    } else {
        return "Menor de idade";
    }
}

// ✅ Early return
String verificarIdade(int idade) {
    if (idade >= 18) {
        return "Maior de idade";
    }
    return "Menor de idade";
}
```

---

### 3. **Encapsule tipos primitivos (se tiver comportamento)**

```java
// ❌ Primitivo exposto
class Produto {
    private String cpf; // Validação espalhada
}

// ✅ Encapsulado
class CPF {
    private final String valor;
    
    CPF(String cpf) {
        if (!validar(cpf)) {
            throw new IllegalArgumentException("CPF inválido");
        }
        this.valor = cpf;
    }
    
    private boolean validar(String cpf) {
        // Lógica de validação centralizada
        return cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }
    
    String getValor() { return valor; }
}

class Cliente {
    private CPF cpf; // Sempre válido!
}
```

---

### 4. **Coleções de primeira classe**

```java
// ❌ Expor coleção diretamente
class Carrinho {
    private List<Item> itens = new ArrayList<>();
    
    List<Item> getItens() { return itens; } // Expõe implementação
}

// ✅ Encapsular coleção
class Carrinho {
    private final List<Item> itens = new ArrayList<>();
    
    void adicionar(Item item) { itens.add(item); }
    void remover(Item item) { itens.remove(item); }
    int tamanho() { return itens.size(); }
    
    // Retornar cópia ou stream
    Stream<Item> stream() { return itens.stream(); }
}
```

---

### 5. **Um ponto por linha**

```java
// ❌ Lei de Deméter violada
class Pedido {
    void processar() {
        double desconto = cliente.getEndereco().getCidade().getEstado().getDesconto();
    }
}

// ✅ Um ponto
class Pedido {
    void processar() {
        double desconto = cliente.getDescontoEstadual();
    }
}

class Cliente {
    double getDescontoEstadual() {
        return endereco.getDescontoEstadual();
    }
}
```

---

### 6. **Não abrevie**

```java
// ❌ Abreviações confusas
class PrdRep {
    List<Prd> findByCtg(int ctgId) { }
}

// ✅ Nomes completos
class ProdutoRepository {
    List<Produto> findByCategoria(int categoriaId) { }
}
```

---

### 7. **Mantenha entidades pequenas**

- **Classes**: < 50 linhas
- **Métodos**: < 10 linhas
- **Pacotes**: < 10 arquivos

---

### 8. **Não mais que 2 variáveis de instância**

```java
// ❌ Muitas variáveis
class Endereco {
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
}

// ✅ Composição
class Endereco {
    private Logradouro logradouro;
    private Localizacao localizacao;
}

class Logradouro {
    private String rua;
    private String numero;
    private String complemento;
}

class Localizacao {
    private String bairro;
    private String cidade;
    private String estado;
    private CEP cep;
}
```

---

### 9. **Sem getters/setters (Data Classes)**

```java
// ❌ Anêmico
class ContaBancaria {
    private double saldo;
    
    double getSaldo() { return saldo; }
    void setSaldo(double saldo) { this.saldo = saldo; }
}

// Uso expõe lógica
conta.setSaldo(conta.getSaldo() - 100);

// ✅ Comportamento rico
class ContaBancaria {
    private double saldo;
    
    void sacar(double valor) {
        if (valor > saldo) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        saldo -= valor;
    }
    
    void depositar(double valor) {
        saldo += valor;
    }
}

// Uso encapsulado
conta.sacar(100);
```

---

## 📊 CQRS (Command Query Responsibility Segregation)

📄 **[CQRS.md](./CQRS.md)**

Separação de responsabilidades entre leitura e escrita:

### Conceito
- **Commands**: Operações que modificam estado (Create, Update, Delete)
- **Queries**: Operações que apenas leem dados (Read)

```java
// Commands (modificam estado)
interface Command { }

record CriarProdutoCommand(String nome, BigDecimal preco) implements Command { }
record AtualizarProdutoCommand(Long id, String nome) implements Command { }
record DeletarProdutoCommand(Long id) implements Command { }

// Queries (apenas leitura)
interface Query<R> { }

record BuscarProdutoQuery(Long id) implements Query<Produto> { }
record ListarProdutosQuery(int page, int size) implements Query<List<Produto>> { }
record BuscarPorCategoriaQuery(Long categoriaId) implements Query<List<Produto>> { }

// Handlers
@ApplicationScoped
class CriarProdutoHandler {
    @Inject ProdutoRepository repository;
    
    void handle(CriarProdutoCommand command) {
        Produto produto = new Produto(command.nome(), command.preco());
        repository.persist(produto);
    }
}

@ApplicationScoped
class BuscarProdutoHandler {
    @Inject ProdutoRepository repository;
    
    Produto handle(BuscarProdutoQuery query) {
        return repository.findById(query.id());
    }
}

// Resource
@Path("/api/produtos")
public class ProdutoResource {
    
    @Inject CriarProdutoHandler criarHandler;
    @Inject BuscarProdutoHandler buscarHandler;
    
    @POST
    public Response criar(CriarProdutoCommand command) {
        criarHandler.handle(command);
        return Response.status(201).build();
    }
    
    @GET
    @Path("/{id}")
    public Produto buscar(@PathParam("id") Long id) {
        return buscarHandler.handle(new BuscarProdutoQuery(id));
    }
}
```

### Vantagens
- ✅ Modelos otimizados separadamente (escrita vs leitura)
- ✅ Escalabilidade independente
- ✅ Segurança granular
- ✅ Código mais limpo e manutenível

### Event Sourcing (CQRS avançado)
```java
// Event Store
interface Event {
    LocalDateTime occurredAt();
}

record ProdutoCriadoEvent(Long id, String nome, BigDecimal preco, LocalDateTime occurredAt) implements Event { }
record ProdutoAtualizadoEvent(Long id, String nome, LocalDateTime occurredAt) implements Event { }

@ApplicationScoped
class EventStore {
    private List<Event> events = new ArrayList<>();
    
    void append(Event event) {
        events.add(event);
    }
    
    List<Event> getEvents() {
        return List.copyOf(events);
    }
}

// Reconstruir estado a partir de eventos
class Produto {
    private Long id;
    private String nome;
    private BigDecimal preco;
    
    static Produto fromEvents(List<Event> events) {
        Produto produto = new Produto();
        for (Event event : events) {
            produto.apply(event);
        }
        return produto;
    }
    
    private void apply(Event event) {
        switch (event) {
            case ProdutoCriadoEvent e -> {
                this.id = e.id();
                this.nome = e.nome();
                this.preco = e.preco();
            }
            case ProdutoAtualizadoEvent e -> {
                this.nome = e.nome();
            }
            default -> { }
        }
    }
}
```

---

## 📚 Resumo

| Padrão | Foco | Quando Usar |
|--------|------|-------------|
| **SOLID** | Princípios OOP | Sempre - base para código limpo |
| **Object Calisthenics** | Regras práticas | Code reviews, refatoração |
| **CQRS** | Separação R/W | Sistemas complexos, alta escala |

---

## 🎯 Guia de Estudo

1. **Semana 1**: SOLID Principles
   - Ler [solid.md](./solid.md)
   - Praticar cada princípio isoladamente
   - Refatorar código existente

2. **Semana 2**: Object Calisthenics
   - Ler [Object-Calisthenics.md](./Object-Calisthenics.md)
   - Aplicar 3 regras por dia
   - Code review com as 9 regras

3. **Semana 3**: CQRS
   - Ler [CQRS.md](./CQRS.md)
   - Implementar exemplo simples
   - Experimentar Event Sourcing

---

**Voltar para**: [📁 Repositório Principal](../README.md)
