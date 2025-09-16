# Object Calisthenics
## Documentação Completa com Exemplos Java

---

## 1. O que é Object Calisthenics? (Conceito Fundamental)

Imagine que você está aprendendo a tocar piano. No início, você pode tocar qualquer nota de qualquer jeito - mas isso não resulta em música bonita. Object Calisthenics é como um conjunto de **exercícios de disciplina** para programadores.

Assim como calistenia física fortalece músculos através de exercícios simples e repetitivos, **Object Calisthenics fortalece suas habilidades de programação orientada a objetos** através de 9 regras restritivas.

### Definição Técnica
Object Calisthenics são **9 regras de programação** criadas por Jeff Bay que, quando seguidas rigorosamente, forçam você a escrever código mais limpo, orientado a objetos e fácil de manter.

### Por que "Calisthenics"?
- **Calistenia física**: Exercícios que usam apenas o peso do corpo (flexões, abdominais)
- **Object Calisthenics**: Exercícios que usam apenas conceitos básicos de OO

---

## 2. As 9 Regras Fundamentais

```
1. Um nível de indentação por método
2. Não use ELSE
3. Envolva primitivos e strings
4. Coleções em primeira classe
5. Um ponto por linha
6. Não abrevie
7. Mantenha entidades pequenas
8. Não mais que duas variáveis de instância
9. Sem getters/setters/propriedades
```

**Importante**: Estas regras são exercícios. No mundo real, você pode relaxá-las quando necessário, mas primeiro deve **dominar** a disciplina.

---

## 3. Regra 1: Um Nível de Indentação por Método

### Conceito
Cada método deve ter **no máximo um nível de indentação**. Isso força métodos pequenos e focados.

### ❌ Código Problemático
```java
public class ProcessadorPedido {
    public void processarPedidos(List<Pedido> pedidos) {
        for (Pedido pedido : pedidos) {                    // Nível 1
            if (pedido.isValido()) {                       // Nível 2
                if (pedido.getValor() > 100) {             // Nível 3
                    if (pedido.getCliente().isPremium()) { // Nível 4
                        pedido.aplicarDesconto(0.1);
                        pedido.processarPagamento();
                        pedido.enviarNotificacao();
                    } else {
                        pedido.processarPagamento();
                        pedido.enviarNotificacao();
                    }
                } else {
                    pedido.processarPagamento();
                }
            } else {
                pedido.marcarComoInvalido();
                pedido.enviarNotificacaoErro();
            }
        }
    }
}
```

### ✅ Solução com Object Calisthenics
```java
public class ProcessadorPedido {
    public void processarPedidos(List<Pedido> pedidos) {
        for (Pedido pedido : pedidos) {              // Apenas 1 nível
            processarPedidoIndividual(pedido);
        }
    }
    
    private void processarPedidoIndividual(Pedido pedido) {
        if (pedido.isValido()) {                     // Apenas 1 nível
            processarPedidoValido(pedido);
        }
        if (!pedido.isValido()) {                    // Note: sem ELSE (regra 2)
            processarPedidoInvalido(pedido);
        }
    }
    
    private void processarPedidoValido(Pedido pedido) {
        if (pedido.isElegiveParaDesconto()) {        // Apenas 1 nível
            pedido.aplicarDesconto();
        }
        pedido.processarPagamento();
        pedido.enviarNotificacao();
    }
    
    private void processarPedidoInvalido(Pedido pedido) {
        pedido.marcarComoInvalido();                 // Apenas 1 nível
        pedido.enviarNotificacaoErro();
    }
}
```

### Benefícios
- **Legibilidade**: Cada método é fácil de entender
- **Testabilidade**: Métodos pequenos são fáceis de testar
- **Reutilização**: Métodos específicos podem ser reutilizados
- **Debugging**: Mais fácil de encontrar problemas

---

## 4. Regra 2: Não Use ELSE

### Conceito
Elimine completamente a palavra-chave `else`. Isso força você a pensar em **fluxos positivos** e usar técnicas como early return, polimorfismo e guard clauses.

### ❌ Código com ELSE
```java
public class CalculadoraDesconto {
    public double calcularDesconto(Cliente cliente, double valor) {
        if (cliente.getTipo() == TipoCliente.PREMIUM) {
            if (valor > 1000) {
                return valor * 0.15;
            } else {
                return valor * 0.10;
            }
        } else if (cliente.getTipo() == TipoCliente.REGULAR) {
            if (valor > 500) {
                return valor * 0.05;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
```

### ✅ Solução Sem ELSE - Técnica 1: Early Return
```java
public class CalculadoraDesconto {
    public double calcularDesconto(Cliente cliente, double valor) {
        if (cliente.getTipo() == TipoCliente.PREMIUM) {
            return calcularDescontoPremium(valor);
        }
        
        if (cliente.getTipo() == TipoCliente.REGULAR) {
            return calcularDescontoRegular(valor);
        }
        
        return 0;
    }
    
    private double calcularDescontoPremium(double valor) {
        if (valor > 1000) {
            return valor * 0.15;
        }
        
        return valor * 0.10;
    }
    
    private double calcularDescontoRegular(double valor) {
        if (valor > 500) {
            return valor * 0.05;
        }
        
        return 0;
    }
}
```

### ✅ Solução Sem ELSE - Técnica 2: Polimorfismo
```java
// Interface estratégia
public interface EstrategiaDesconto {
    double calcular(double valor);
}

// Implementações específicas
public class DescontoPremium implements EstrategiaDesconto {
    @Override
    public double calcular(double valor) {
        if (valor > 1000) {
            return valor * 0.15;
        }
        
        return valor * 0.10;
    }
}

public class DescontoRegular implements EstrategiaDesconto {
    @Override
    public double calcular(double valor) {
        if (valor > 500) {
            return valor * 0.05;
        }
        
        return 0;
    }
}

public class SemDesconto implements EstrategiaDesconto {
    @Override
    public double calcular(double valor) {
        return 0;
    }
}

// Calculadora usando polimorfismo
public class CalculadoraDesconto {
    private final Map<TipoCliente, EstrategiaDesconto> estrategias;
    
    public CalculadoraDesconto() {
        estrategias = Map.of(
            TipoCliente.PREMIUM, new DescontoPremium(),
            TipoCliente.REGULAR, new DescontoRegular()
        );
    }
    
    public double calcularDesconto(Cliente cliente, double valor) {
        EstrategiaDesconto estrategia = estrategias.getOrDefault(
            cliente.getTipo(), 
            new SemDesconto()
        );
        
        return estrategia.calcular(valor);
    }
}
```

---

## 5. Regra 3: Envolva Primitivos e Strings (Wrap Primitives)

### Conceito
**Nunca use tipos primitivos diretamente**. Sempre envolva-os em classes que representem o conceito do negócio. Isso adiciona significado e comportamento aos dados.

### ❌ Código com Primitivos Nus
```java
public class Pedido {
    private String cpf;           // String nua
    private String email;         // String nua  
    private double valor;         // Primitivo nu
    private int idade;           // Primitivo nu
    
    public Pedido(String cpf, String email, double valor, int idade) {
        // Validação espalhada
        if (cpf == null || cpf.length() != 11) {
            throw new IllegalArgumentException("CPF inválido");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("Valor inválido");
        }
        if (idade < 0 || idade > 120) {
            throw new IllegalArgumentException("Idade inválida");
        }
        
        this.cpf = cpf;
        this.email = email;
        this.valor = valor;
        this.idade = idade;
    }
    
    // Lógica de negócio espalhada
    public boolean podeComprarAlcool() {
        return idade >= 18;
    }
    
    public String getCpfFormatado() {
        return cpf.substring(0, 3) + "." + 
               cpf.substring(3, 6) + "." + 
               cpf.substring(6, 9) + "-" + 
               cpf.substring(9, 11);
    }
}
```

### ✅ Solução com Value Objects
```java
// Value Object para CPF
public class CPF {
    private final String numero;
    
    public CPF(String numero) {
        validar(numero);
        this.numero = limpar(numero);
    }
    
    private void validar(String numero) {
        if (numero == null) {
            throw new IllegalArgumentException("CPF não pode ser nulo");
        }
        
        String cpfLimpo = limpar(numero);
        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("CPF deve ter 11 dígitos");
        }
        
        if (!isValidoCPF(cpfLimpo)) {
            throw new IllegalArgumentException("CPF inválido");
        }
    }
    
    private String limpar(String numero) {
        return numero.replaceAll("[^0-9]", "");
    }
    
    private boolean isValidoCPF(String cpf) {
        // Implementar algoritmo de validação do CPF
        return true; // Simplificado
    }
    
    public String getNumero() {
        return numero;
    }
    
    public String getFormatado() {
        return numero.substring(0, 3) + "." + 
               numero.substring(3, 6) + "." + 
               numero.substring(6, 9) + "-" + 
               numero.substring(9, 11);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CPF cpf = (CPF) obj;
        return Objects.equals(numero, cpf.numero);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }
}

// Value Object para Email
public class Email {
    private final String endereco;
    
    public Email(String endereco) {
        validar(endereco);
        this.endereco = endereco.toLowerCase().trim();
    }
    
    private void validar(String endereco) {
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }
        
        if (!endereco.contains("@") || !endereco.contains(".")) {
            throw new IllegalArgumentException("Email deve ser válido");
        }
    }
    
    public String getEndereco() {
        return endereco;
    }
    
    public String getDominio() {
        return endereco.substring(endereco.indexOf("@") + 1);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Email email = (Email) obj;
        return Objects.equals(endereco, email.endereco);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(endereco);
    }
}

// Value Object para Dinheiro
public class Dinheiro {
    private final BigDecimal valor;
    private final String moeda;
    
    public Dinheiro(BigDecimal valor) {
        this(valor, "BRL");
    }
    
    public Dinheiro(BigDecimal valor, String moeda) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor não pode ser nulo");
        }
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor não pode ser negativo");
        }
        
        this.valor = valor;
        this.moeda = moeda;
    }
    
    public Dinheiro somar(Dinheiro outro) {
        validarMesmaMoeda(outro);
        return new Dinheiro(this.valor.add(outro.valor), this.moeda);
    }
    
    public Dinheiro aplicarDesconto(double percentual) {
        if (percentual < 0 || percentual > 1) {
            throw new IllegalArgumentException("Percentual deve estar entre 0 e 1");
        }
        
        BigDecimal desconto = valor.multiply(BigDecimal.valueOf(percentual));
        return new Dinheiro(valor.subtract(desconto), moeda);
    }
    
    public boolean isMaiorQue(Dinheiro outro) {
        validarMesmaMoeda(outro);
        return valor.compareTo(outro.valor) > 0;
    }
    
    private void validarMesmaMoeda(Dinheiro outro) {
        if (!moeda.equals(outro.moeda)) {
            throw new IllegalArgumentException("Moedas diferentes: " + moeda + " e " + outro.moeda);
        }
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public String getMoeda() {
        return moeda;
    }
}

// Value Object para Idade
public class Idade {
    private final int anos;
    
    public Idade(int anos) {
        if (anos < 0 || anos > 150) {
            throw new IllegalArgumentException("Idade deve estar entre 0 e 150 anos");
        }
        this.anos = anos;
    }
    
    public boolean isMaiorDeIdade() {
        return anos >= 18;
    }
    
    public boolean podeAposentar() {
        return anos >= 65;
    }
    
    public int getAnos() {
        return anos;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Idade idade = (Idade) obj;
        return anos == idade.anos;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(anos);
    }
}

// Pedido refatorado
public class Pedido {
    private final CPF cpf;
    private final Email email;
    private final Dinheiro valor;
    private final Idade idade;
    
    public Pedido(CPF cpf, Email email, Dinheiro valor, Idade idade) {
        this.cpf = cpf;
        this.email = email;
        this.valor = valor;
        this.idade = idade;
    }
    
    public boolean podeComprarAlcool() {
        return idade.isMaiorDeIdade();
    }
    
    public Dinheiro calcularDesconto(double percentual) {
        return valor.aplicarDesconto(percentual);
    }
    
    // Getters
    public CPF getCpf() { return cpf; }
    public Email getEmail() { return email; }
    public Dinheiro getValor() { return valor; }
    public Idade getIdade() { return idade; }
}
```

### Benefícios dos Value Objects
- **Validação centralizada**: Uma única fonte de verdade para regras
- **Comportamento rico**: Métodos específicos do domínio
- **Imutabilidade**: Objetos que não mudam após criação
- **Expressividade**: Código auto-documentado

---

## 6. Regra 4: Coleções em Primeira Classe

### Conceito
**Nunca exponha coleções diretamente**. Sempre envolva coleções em classes específicas que controlem como os elementos são manipulados.

### ❌ Código com Coleções Expostas
```java
public class Pedido {
    private List<Item> itens;
    private Set<String> tags;
    
    public Pedido() {
        this.itens = new ArrayList<>();
        this.tags = new HashSet<>();
    }
    
    // Perigoso: expõe a coleção diretamente
    public List<Item> getItens() {
        return itens;
    }
    
    public Set<String> getTags() {
        return tags;
    }
    
    // Lógica de negócio espalhada
    public double getValorTotal() {
        return itens.stream()
                   .mapToDouble(Item::getPreco)
                   .sum();
    }
}

// Cliente pode fazer coisas perigosas:
// pedido.getItens().clear(); // ❌ Remove todos os itens!
// pedido.getItens().add(null); // ❌ Adiciona item inválido!
```

### ✅ Solução com First-Class Collections
```java
// Coleção de primeira classe para Itens
public class ItensPedido {
    private final List<Item> itens;
    
    public ItensPedido() {
        this.itens = new ArrayList<>();
    }
    
    public ItensPedido(List<Item> itens) {
        this.itens = new ArrayList<>();
        for (Item item : itens) {
            adicionar(item);
        }
    }
    
    public void adicionar(Item item) {
        validarItem(item);
        itens.add(item);
    }
    
    public void remover(Item item) {
        if (!itens.contains(item)) {
            throw new IllegalArgumentException("Item não encontrado no pedido");
        }
        itens.remove(item);
    }
    
    public boolean contem(Item item) {
        return itens.contains(item);
    }
    
    public int quantidade() {
        return itens.size();
    }
    
    public boolean estaVazio() {
        return itens.isEmpty();
    }
    
    public Dinheiro calcularValorTotal() {
        BigDecimal total = itens.stream()
                                .map(Item::getPreco)
                                .map(Dinheiro::getValor)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return new Dinheiro(total);
    }
    
    public ItensPedido filtrarPorCategoria(Categoria categoria) {
        List<Item> itensFiltrados = itens.stream()
                                        .filter(item -> item.getCategoria().equals(categoria))
                                        .collect(Collectors.toList());
        
        return new ItensPedido(itensFiltrados);
    }
    
    public Item itemMaisCaro() {
        if (estaVazio()) {
            throw new IllegalStateException("Não há itens no pedido");
        }
        
        return itens.stream()
                   .max(Comparator.comparing(item -> item.getPreco().getValor()))
                   .orElseThrow();
    }
    
    // Retorna cópia defensiva
    public List<Item> todos() {
        return new ArrayList<>(itens);
    }
    
    private void validarItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo");
        }
        
        if (itens.contains(item)) {
            throw new IllegalArgumentException("Item já existe no pedido");
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ItensPedido that = (ItensPedido) obj;
        return Objects.equals(itens, that.itens);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(itens);
    }
}

// Coleção de primeira classe para Tags
public class TagsPedido {
    private final Set<String> tags;
    
    public TagsPedido() {
        this.tags = new HashSet<>();
    }
    
    public TagsPedido(Set<String> tags) {
        this.tags = new HashSet<>();
        for (String tag : tags) {
            adicionar(tag);
        }
    }
    
    public void adicionar(String tag) {
        validarTag(tag);
        tags.add(tag.toLowerCase().trim());
    }
    
    public void remover(String tag) {
        if (tag == null) return;
        tags.remove(tag.toLowerCase().trim());
    }
    
    public boolean contem(String tag) {
        if (tag == null) return false;
        return tags.contains(tag.toLowerCase().trim());
    }
    
    public int quantidade() {
        return tags.size();
    }
    
    public boolean estaVazio() {
        return tags.isEmpty();
    }
    
    public boolean contemTodasAs(TagsPedido outrosTags) {
        return tags.containsAll(outrosTags.tags);
    }
    
    public TagsPedido combinarCom(TagsPedido outrosTags) {
        Set<String> tagsCombinadas = new HashSet<>(this.tags);
        tagsCombinadas.addAll(outrosTags.tags);
        return new TagsPedido(tagsCombinadas);
    }
    
    // Retorna cópia defensiva
    public Set<String> todas() {
        return new HashSet<>(tags);
    }
    
    private void validarTag(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag não pode ser vazia");
        }
        
        if (tag.length() > 50) {
            throw new IllegalArgumentException("Tag não pode ter mais de 50 caracteres");
        }
    }
    
    @Override
    public String toString() {
        return String.join(", ", tags);
    }
}

// Pedido refatorado
public class Pedido {
    private final ItensPedido itens;
    private final TagsPedido tags;
    
    public Pedido() {
        this.itens = new ItensPedido();
        this.tags = new TagsPedido();
    }
    
    public void adicionarItem(Item item) {
        itens.adicionar(item);
    }
    
    public void removerItem(Item item) {
        itens.remover(item);
    }
    
    public void adicionarTag(String tag) {
        tags.adicionar(tag);
    }
    
    public Dinheiro getValorTotal() {
        return itens.calcularValorTotal();
    }
    
    public boolean temItens() {
        return !itens.estaVazio();
    }
    
    public boolean temTag(String tag) {
        return tags.contem(tag);
    }
    
    public ItensPedido getItens() {
        return itens; // Retorna o objeto coleção, não a lista
    }
    
    public TagsPedido getTags() {
        return tags; // Retorna o objeto coleção, não o set
    }
}
```

### Exemplo de Uso
```java
public class ExemploPedido {
    public static void main(String[] args) {
        Pedido pedido = new Pedido();
        
        // Adicionar itens de forma controlada
        Item notebook = new Item("Notebook", new Dinheiro(new BigDecimal("2000")));
        Item mouse = new Item("Mouse", new Dinheiro(new BigDecimal("50")));
        
        pedido.adicionarItem(notebook);
        pedido.adicionarItem(mouse);
        
        // Adicionar tags
        pedido.adicionarTag("eletrônicos");
        pedido.adicionarTag("trabalho");
        
        // Operações seguras
        System.out.println("Total: " + pedido.getValorTotal().getValor());
        System.out.println("Quantidade de itens: " + pedido.getItens().quantidade());
        System.out.println("Tem tag eletrônicos: " + pedido.temTag("eletrônicos"));
        
        // Não é possível fazer operações perigosas:
        // pedido.getItens().todos().clear(); // ❌ Não afeta o pedido original
        // pedido.getTags().todas().add("nova"); // ❌ Não afeta o pedido original
    }
}
```

---

## 7. Regra 5: Um Ponto Por Linha

### Conceito
**Nunca use mais de um ponto (.) por linha**. Isso evita o Law of Demeter (Lei do Menor Conhecimento) e reduz acoplamento.

### ❌ Código Violando a Lei de Demeter
```java
public class RelatorioVendas {
    public void gerarRelatorio(Pedido pedido) {
        // Muitos pontos = muito acoplamento
        String nomeCliente = pedido.getCliente().getPessoa().getNome();
        String email = pedido.getCliente().getPessoa().getContato().getEmail();
        String cidade = pedido.getCliente().getPessoa().getEndereco().getCidade();
        double desconto = pedido.getCliente().getPlano().getDesconto().getPercentual();
        
        // Se qualquer classe intermediária mudar, este código quebra
        System.out.println("Cliente: " + nomeCliente);
        System.out.println("Email: " + email);
        System.out.println("Cidade: " + cidade);
        System.out.println("Desconto: " + desconto);
    }
    
    public void atualizarStatus(Pedido pedido) {
        // Navegação profunda na estrutura
        pedido.getCliente().getPessoa().getContato().enviarNotificacao("Pedido atualizado");
        pedido.getPagamento().getCartao().getOperadora().notificarTransacao();
    }
}
```

### ✅ Solução com Um Ponto Por Linha
```java
// Método 1: Delegar responsabilidades
public class Pedido {
    private Cliente cliente;
    private Pagamento pagamento;
    
    // Delegação ao invés de exposição
    public String obterNomeCliente() {
        return cliente.obterNome();
    }
    
    public String obterEmailCliente() {
        return cliente.obterEmail();
    }
    
    public String obterCidadeCliente() {
        return cliente.obterCidade();
    }
    
    public double obterDescontoCliente() {
        return cliente.obterPercentualDesconto();
    }
    
    public void notificarCliente(String mensagem) {
        cliente.receberNotificacao(mensagem);
    }
    
    public void notificarPagamento() {
        pagamento.notificarOperadora();
    }
}

public class Cliente {
    private Pessoa pessoa;
    private Plano plano;
    
    public String obterNome() {
        return pessoa.obterNome();
    }
    
    public String obterEmail() {
        return pessoa.obterEmail();
    }
    
    public String obterCidade() {
        return pessoa.obterCidade();
    }
    
    public double obterPercentualDesconto() {
        return plano.obterPercentualDesconto();
    }
    
    public void receberNotificacao(String mensagem) {
        pessoa.receberNotificacao(mensagem);
    }
}

public class Pessoa {
    private String nome;
    private Contato contato;
    private Endereco endereco;
    
    public String obterNome() {
        return nome;
    }
    
    public String obterEmail() {
        return contato.obterEmail();
    }
    
    public String obterCidade() {
        return endereco.obterCidade();
    }
    
    public void receberNotificacao(String mensagem) {
        contato.enviarNotificacao(mensagem);
    }
}

// Relatório refatorado
public class RelatorioVendas {
    public void gerarRelatorio(Pedido pedido) {
        // Apenas um ponto por linha
        String nomeCliente = pedido.obterNomeCliente();
        String email = pedido.obterEmailCliente();
        String cidade = pedido.obterCidadeCliente();
        double desconto = pedido.obterDescontoCliente();
        
        System.out.println("Cliente: " + nomeCliente);
        System.out.println("Email: " + email);
        System.out.println("Cidade: " + cidade);
        System.out.println("Desconto: " + desconto);
    }
    
    public void atualizarStatus(Pedido pedido) {
        // Delegação clara
        pedido.notificarCliente("Pedido atualizado");
        pedido.notificarPagamento();
    }
}
```

### Método 2: Usar Fluent Interface
```java
// Builder/Fluent para navegação controlada
public class ConsultorPedido {
    private final Pedido pedido;
    
    public ConsultorPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    
    public ConsultaCliente cliente() {
        return new ConsultaCliente(pedido.getCliente());
    }
    
    public ConsultaPagamento pagamento() {
        return new ConsultaPagamento(pedido.getPagamento());
    }
}

public class ConsultaCliente {
    private final Cliente cliente;
    
    public ConsultaCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public String nome() {
        return cliente.obterNome();
    }
    
    public String email() {
        return cliente.obterEmail();
    }
    
    public String cidade() {
        return cliente.obterCidade();
    }
    
    public double desconto() {
        return cliente.obterPercentualDesconto();
    }
}

// Uso da fluent interface
public class RelatorioVendasFluent {
    public void gerarRelatorio(Pedido pedido) {
        ConsultorPedido consultor = new ConsultorPedido(pedido);
        
        // Apenas um ponto por linha, mas com fluência
        String nomeCliente = consultor.cliente().nome();
        String email = consultor.cliente().email();
        String cidade = consultor.cliente().cidade();
        double desconto = consultor.cliente().desconto();
        
        System.out.println("Cliente: " + nomeCliente);
        System.out.println("Email: " + email);
        System.out.println("Cidade: " + cidade);
        System.out.println("Desconto: " + desconto);
    }
}

---

## 8. Regra 6: Não Abrevie

### Conceito
**Nunca abrevie nomes**. Use nomes completos e expressivos que comunicem claramente a intenção.

### ❌ Código com Abreviações
```java
public class PrcPed {
    private List<Itm> itns;
    private Clt clt;
    private Dt dtCr;
    
    public double calcVlrTot() {
        double tot = 0;
        for (Itm i : itns) {
            tot += i.getPrc() * i.getQtd();
        }
        return tot;
    }
    
    public boolean vldPed() {
        return clt != null && 
               !itns.isEmpty() && 
               calcVlrTot() > 0;
    }
    
    public void prcPag() {
        if (vldPed()) {
            PrcPag pp = new PrcPag();
            pp.processar(this.calcVlrTot(), this.clt.getCrtId());
        }
    }
    
    // Métodos com parâmetros abreviados
    public void aplDesc(double pct, String tp) {
        for (Itm i : itns) {
            if (tp.equals("PR") && i.isFrg()) {
                i.setPrc(i.getPrc() * (1 - pct));
            }
        }
    }
}
```

### ✅ Solução com Nomes Expressivos
```java
public class ProcessadorPedido {
    private List<ItemPedido> itensPedido;
    private Cliente cliente;
    private LocalDateTime dataCriacao;
    
    public Dinheiro calcularValorTotal() {
        BigDecimal valorTotal = BigDecimal.ZERO;
        
        for (ItemPedido itemPedido : itensPedido) {
            Dinheiro precoItem = itemPedido.getPreco();
            int quantidade = itemPedido.getQuantidade();
            BigDecimal subtotal = precoItem.getValor().multiply(BigDecimal.valueOf(quantidade));
            valorTotal = valorTotal.add(subtotal);
        }
        
        return new Dinheiro(valorTotal);
    }
    
    public boolean validarPedido() {
        return cliente != null && 
               !itensPedido.isEmpty() && 
               calcularValorTotal().getValor().compareTo(BigDecimal.ZERO) > 0;
    }
    
    public void processarPagamento() {
        if (validarPedido()) {
            ProcessadorPagamento processadorPagamento = new ProcessadorPagamento();
            CartaoCredito cartaoCredito = cliente.obterCartaoCredito();
            Dinheiro valorTotal = calcularValorTotal();
            
            processadorPagamento.processar(valorTotal, cartaoCredito);
        }
    }
    
    // Método com parâmetros expressivos
    public void aplicarDesconto(double percentualDesconto, TipoDesconto tipoDesconto) {
        for (ItemPedido itemPedido : itensPedido) {
            if (tipoDesconto == TipoDesconto.PRODUTOS_FRAGEIS && itemPedido.isFragil()) {
                Dinheiro precoOriginal = itemPedido.getPreco();
                Dinheiro precoComDesconto = precoOriginal.aplicarDesconto(percentualDesconto);
                itemPedido.atualizarPreco(precoComDesconto);
            }
        }
    }
    
    public void adicionarItemPedido(ItemPedido itemPedido) {
        if (itemPedido == null) {
            throw new IllegalArgumentException("Item do pedido não pode ser nulo");
        }
        itensPedido.add(itemPedido);
    }
    
    public void removerItemPedido(ItemPedido itemPedido) {
        if (!itensPedido.contains(itemPedido)) {
            throw new IllegalArgumentException("Item não encontrado no pedido");
        }
        itensPedido.remove(itemPedido);
    }
    
    public int obterQuantidadeTotalItens() {
        return itensPedido.stream()
                         .mapToInt(ItemPedido::getQuantidade)
                         .sum();
    }
    
    public boolean contemItemFragil() {
        return itensPedido.stream()
                         .anyMatch(ItemPedido::isFragil);
    }
    
    public LocalDateTime obterDataCriacao() {
        return dataCriacao;
    }
    
    public Cliente obterCliente() {
        return cliente;
    }
    
    public List<ItemPedido> obterItensPedido() {
        return new ArrayList<>(itensPedido);
    }
}

// Enums e classes de apoio com nomes expressivos
public enum TipoDesconto {
    PRODUTOS_FRAGEIS,
    CLIENTES_PREMIUM,
    LIQUIDACAO_ESTOQUE,
    COMPRA_VOLUME
}

public class ItemPedido {
    private String nomeProduto;
    private Dinheiro preco;
    private int quantidade;
    private boolean fragil;
    private CategoriaProduto categoriaProduto;
    
    public ItemPedido(String nomeProduto, Dinheiro preco, int quantidade, boolean fragil) {
        this.nomeProduto = nomeProduto;
        this.preco = preco;
        this.quantidade = quantidade;
        this.fragil = fragil;
    }
    
    public Dinheiro calcularSubtotal() {
        BigDecimal subtotal = preco.getValor().multiply(BigDecimal.valueOf(quantidade));
        return new Dinheiro(subtotal);
    }
    
    public boolean isFragil() {
        return fragil;
    }
    
    public void atualizarPreco(Dinheiro novoPreco) {
        if (novoPreco == null) {
            throw new IllegalArgumentException("Preço não pode ser nulo");
        }
        this.preco = novoPreco;
    }
    
    // Getters com nomes expressivos
    public String getNomeProduto() { return nomeProduto; }
    public Dinheiro getPreco() { return preco; }
    public int getQuantidade() { return quantidade; }
    public CategoriaProduto getCategoriaProduto() { return categoriaProduto; }
}
```

### Benefícios de Nomes Expressivos
- **Autoexplicativo**: O código documenta a si mesmo
- **Menos bugs**: Fica mais difícil usar variável errada
- **Facilita refatoração**: IDEs conseguem renomear melhor
- **Onboarding mais rápido**: Novos desenvolvedores entendem mais rápido

---

## 9. Regra 7: Mantenha Entidades Pequenas

### Conceito
**Classes e métodos devem ser pequenos**. Limite sugerido:
- **Classes**: máximo 50 linhas
- **Métodos**: máximo 5 linhas
- **Pacotes**: máximo 10 arquivos

### ❌ Classe Grande e Complexa
```java
// Classe gigante com 200+ linhas
public class GerenciadorPedido {
    private String numeroPedido;
    private Cliente cliente;
    private List<ItemPedido> itens;
    private StatusPedido status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Endereco enderecoEntrega;
    private FormaPagamento formaPagamento;
    private double valorTotal;
    private double valorDesconto;
    private boolean pedidoUrgente;
    private String observacoes;
    private Transportadora transportadora;
    private String codigoRastreamento;
    
    // Método gigante com múltiplas responsabilidades
    public void processarPedidoCompleto() {
        // Validação (20 linhas)
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("Pedido deve conter itens");
        }
        for (ItemPedido item : itens) {
            if (item.getQuantidade() <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser positiva");
            }
            if (item.getPreco() <= 0) {
                throw new IllegalArgumentException("Preço deve ser positivo");
            }
        }
        
        // Cálculo de valores (15 linhas)
        valorTotal = 0;
        for (ItemPedido item : itens) {
            valorTotal += item.getPreco() * item.getQuantidade();
        }
        
        if (cliente.getTipo() == TipoCliente.PREMIUM) {
            valorDesconto = valorTotal * 0.1;
        } else if (cliente.getTipo() == TipoCliente.VIP) {
            valorDesconto = valorTotal * 0.15;
        } else {
            valorDesconto = 0;
        }
        
        valorTotal -= valorDesconto;
        
        // Processamento de pagamento (25 linhas)
        if (formaPagamento instanceof CartaoCredito) {
            CartaoCredito cartao = (CartaoCredito) formaPagamento;
            if (!validarCartao(cartao)) {
                throw new RuntimeException("Cartão inválido");
            }
            if (!processarPagamentoCartao(cartao, valorTotal)) {
                throw new RuntimeException("Pagamento rejeitado");
            }
        } else if (formaPagamento instanceof PIX) {
            PIX pix = (PIX) formaPagamento;
            if (!processarPagamentoPIX(pix, valorTotal)) {
                throw new RuntimeException("PIX rejeitado");
            }
        }
        
        // Gestão de estoque (20 linhas)
        for (ItemPedido item : itens) {
            EstoqueService estoqueService = new EstoqueService();
            if (!estoqueService.temDisponivel(item.getProdutoId(), item.getQuantidade())) {
                throw new RuntimeException("Produto sem estoque: " + item.getNome());
            }
            estoqueService.reservar(item.getProdutoId(), item.getQuantidade());
        }
        
        // Seleção de transportadora (15 linhas)
        if (pedidoUrgente) {
            transportadora = selecionarTransportadoraExpresså();
        } else {
            transportadora = selecionarTransportadoraEconomica();
        }
        
        codigoRastreamento = transportadora.criarEnvio(enderecoEntrega, itens);
        
        // Notificações (10 linhas)
        EmailService emailService = new EmailService();
        SMSService smsService = new SMSService();
        
        emailService.enviarConfirmacao(cliente.getEmail(), numeroPedido);
        if (cliente.isNotificacaoSMS()) {
            smsService.enviarConfirmacao(cliente.getTelefone(), numeroPedido);
        }
        
        // Atualização de status
        status = StatusPedido.CONFIRMADO;
        dataAtualizacao = LocalDateTime.now();
        
        // Logs e auditoria (10 linhas)
        LogService.log("Pedido processado: " + numeroPedido);
        AuditoriaService.registrar("PEDIDO_PROCESSADO", numeroPedido, cliente.getId());
    }
    
    // Muitos outros métodos gigantes...
}
```

### ✅ Solução com Entidades Pequenas
```java
// Classe principal focada apenas na coordenação
public class ProcessadorPedido {
    private final ValidadorPedido validador;
    private final CalculadoraValores calculadora;
    private final ProcessadorPagamento processadorPagamento;
    private final GerenciadorEstoque gerenciadorEstoque;
    private final SeletorTransportadora seletorTransportadora;
    private final NotificadorCliente notificador;
    private final RegistradorAuditoria auditor;
    
    public ProcessadorPedido(ValidadorPedido validador,
                           CalculadoraValores calculadora,
                           ProcessadorPagamento processadorPagamento,
                           GerenciadorEstoque gerenciadorEstoque,
                           SeletorTransportadora seletorTransportadora,
                           NotificadorCliente notificador,
                           RegistradorAuditoria auditor) {
        this.validador = validador;
        this.calculadora = calculadora;
        this.processadorPagamento = processadorPagamento;
        this.gerenciadorEstoque = gerenciadorEstoque;
        this.seletorTransportadora = seletorTransportadora;
        this.notificador = notificador;
        this.auditor = auditor;
    }
    
    // Método pequeno e focado
    public ResultadoProcessamento processar(Pedido pedido) {
        validador.validar(pedido);
        calculadora.calcular(pedido);
        processadorPagamento.processar(pedido);
        gerenciadorEstoque.reservar(pedido);
        
        return finalizarProcessamento(pedido);
    }
    
    private ResultadoProcessamento finalizarProcessamento(Pedido pedido) {
        Transportadora transportadora = seletorTransportadora.selecionar(pedido);
        String codigoRastreamento = transportadora.criarEnvio(pedido);
        
        notificador.notificar(pedido);
        auditor.registrar(pedido);
        
        return new ResultadoProcessamento(pedido, codigoRastreamento);
    }
}

// Cada classe com responsabilidade única e pequena
public class ValidadorPedido {
    public void validar(Pedido pedido) {
        validarCliente(pedido.getCliente());
        validarItens(pedido.getItens());
    }
    
    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }
    }
    
    private void validarItens(List<ItemPedido> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("Pedido deve conter itens");
        }
        
        for (ItemPedido item : itens) {
            validarItem(item);
        }
    }
    
    private void validarItem(ItemPedido item) {
        if (item.getQuantidade() <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        if (item.getPreco().getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser positivo");
        }
    }
}

public class CalculadoraValores {
    private final CalculadoraDesconto calculadoraDesconto;
    
    public CalculadoraValores(CalculadoraDesconto calculadoraDesconto) {
        this.calculadoraDesconto = calculadoraDesconto;
    }
    
    public void calcular(Pedido pedido) {
        Dinheiro subtotal = calcularSubtotal(pedido);
        Dinheiro desconto = calculadoraDesconto.calcular(pedido.getCliente(), subtotal);
        Dinheiro total = subtotal.subtrair(desconto);
        
        pedido.definirValores(subtotal, desconto, total);
    }
    
    private Dinheiro calcularSubtotal(Pedido pedido) {
        return pedido.getItens().stream()
                    .map(ItemPedido::calcularSubtotal)
                    .reduce(Dinheiro.ZERO, Dinheiro::somar);
    }
}

public class CalculadoraDesconto {
    public Dinheiro calcular(Cliente cliente, Dinheiro valor) {
        PercentualDesconto percentual = obterPercentualDesconto(cliente);
        return valor.aplicarDesconto(percentual);
    }
    
    private PercentualDesconto obterPercentualDesconto(Cliente cliente) {
        if (cliente.getTipo() == TipoCliente.VIP) {
            return new PercentualDesconto(15);
        }
        if (cliente.getTipo() == TipoCliente.PREMIUM) {
            return new PercentualDesconto(10);
        }
        return PercentualDesconto.ZERO;
    }
}

public class ProcessadorPagamento {
    public void processar(Pedido pedido) {
        FormaPagamento forma = pedido.getFormaPagamento();
        Dinheiro valor = pedido.getValorTotal();
        
        ResultadoPagamento resultado = forma.processar(valor);
        if (!resultado.foiAprovado()) {
            throw new PagamentoRejeitadoException(resultado.getMensagem());
        }
        
        pedido.confirmarPagamento(resultado);
    }
}

public class GerenciadorEstoque {
    private final ServicoEstoque servicoEstoque;
    
    public GerenciadorEstoque(ServicoEstoque servicoEstoque) {
        this.servicoEstoque = servicoEstoque;
    }
    
    public void reservar(Pedido pedido) {
        for (ItemPedido item : pedido.getItens()) {
            reservarItem(item);
        }
    }
    
    private void reservarItem(ItemPedido item) {
        if (!servicoEstoque.temDisponivel(item)) {
            throw new EstoqueInsuficienteException(item.getNomeProduto());
        }
        servicoEstoque.reservar(item);
    }
}

public class SeletorTransportadora {
    private final List<Transportadora> transportadoras;
    
    public SeletorTransportadora(List<Transportadora> transportadoras) {
        this.transportadoras = transportadoras;
    }
    
    public Transportadora selecionar(Pedido pedido) {
        if (pedido.isUrgente()) {
            return selecionarMaisRapida();
        }
        return selecionarMaisEconomica();
    }
    
    private Transportadora selecionarMaisRapida() {
        return transportadoras.stream()
                            .min(Comparator.comparing(Transportadora::getTempoEntrega))
                            .orElseThrow(() -> new RuntimeException("Nenhuma transportadora disponível"));
    }
    
    private Transportadora selecionarMaisEconomica() {
        return transportadoras.stream()
                            .min(Comparator.comparing(Transportadora::getPreco))
                            .orElseThrow(() -> new RuntimeException("Nenhuma transportadora disponível"));
    }
}

public class NotificadorCliente {
    private final ServicoEmail servicoEmail;
    private final ServicoSMS servicoSMS;
    
    public NotificadorCliente(ServicoEmail servicoEmail, ServicoSMS servicoSMS) {
        this.servicoEmail = servicoEmail;
        this.servicoSMS = servicoSMS;
    }
    
    public void notificar(Pedido pedido) {
        enviarEmail(pedido);
        
        if (pedido.getCliente().desejaNotificacaoSMS()) {
            enviarSMS(pedido);
        }
    }
    
    private void enviarEmail(Pedido pedido) {
        Email destinatario = pedido.getCliente().getEmail();
        String mensagem = construirMensagemConfirmacao(pedido);
        servicoEmail.enviar(destinatario, mensagem);
    }
    
    private void enviarSMS(Pedido pedido) {
        Telefone numero = pedido.getCliente().getTelefone();
        String mensagem = construirMensagemSMS(pedido);
        servicoSMS.enviar(numero, mensagem);
    }
    
    private String construirMensagemConfirmacao(Pedido pedido) {
        return "Pedido " + pedido.getNumero() + " confirmado!";
    }
    
    private String construirMensagemSMS(Pedido pedido) {
        return "Pedido " + pedido.getNumero() + " OK!";
    }
}

public class RegistradorAuditoria {
    private final ServicoLog servicoLog;
    private final ServicoAuditoria servicoAuditoria;
    
    public RegistradorAuditoria(ServicoLog servicoLog, ServicoAuditoria servicoAuditoria) {
        this.servicoLog = servicoLog;
        this.servicoAuditoria = servicoAuditoria;
    }
    
    public void registrar(Pedido pedido) {
        registrarLog(pedido);
        registrarAuditoria(pedido);
    }
    
    private void registrarLog(Pedido pedido) {
        String mensagem = "Pedido processado: " + pedido.getNumero();
        servicoLog.info(mensagem);
    }
    
    private void registrarAuditoria(Pedido pedido) {
        EventoAuditoria evento = new EventoAuditoria(
            "PEDIDO_PROCESSADO",
            pedido.getNumero(),
            pedido.getCliente().getId()
        );
        servicoAuditoria.registrar(evento);
    }
}
```

---

## 10. Regra 8: Não Mais que Duas Variáveis de Instância

### Conceito
**Classes devem ter no máximo 2 variáveis de instância**. Isso força alta coesão e baixo acoplamento.

### ❌ Classe com Muitas Variáveis de Instância
```java
public class Usuario {
    private String nome;              // 1
    private String email;             // 2
    private String telefone;          // 3
    private String cpf;               // 4
    private LocalDate dataNascimento; // 5
    private Endereco endereco;        // 6
    private String senha;             // 7
    private boolean ativo;            // 8
    private LocalDateTime ultimoLogin; // 9
    private List<String> permissoes;  // 10
    private TipoUsuario tipo;         // 11
    
    // Muitas responsabilidades misturadas
    public boolean validarCredenciais(String senhaInformada) {
        return senha.equals(hash(senhaInformada));
    }
    
    public boolean podeAcessar(String recurso) {
        return ativo && permissoes.contains(recurso);
    }
    
    public void atualizarUltimoLogin() {
        ultimoLogin = LocalDateTime.now();
    }
    
    public int calcularIdade() {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }
    
    public String formatarCPF() {
        return cpf.substring(0,3) + "." + cpf.substring(3,6) + "." + 
               cpf.substring(6,9) + "-" + cpf.substring(9,11);
    }
}
```

### ✅ Solução com Decomposição
```java
// Classe principal com apenas 2 variáveis de instância
public class Usuario {
    private final IdentidadeUsuario identidade;    // 1
    private final PerfilUsuario perfil;             // 2
    
    public Usuario(IdentidadeUsuario identidade, PerfilUsuario perfil) {
        this.identidade = identidade;
        this.perfil = perfil;
    }
    
    public boolean validarCredenciais(String senha) {
        return identidade.validarSenha(senha);
    }
    
    public boolean podeAcessar(String recurso) {
        return perfil.podeAcessar(recurso);
    }
    
    public void registrarLogin() {
        perfil.atualizarUltimoLogin();
    }
    
    public String obterNome() {
        return identidade.obterNome();
    }
    
    public Email obterEmail() {
        return identidade.obterEmail();
    }
    
    public int calcularIdade() {
        return identidade.calcularIdade();
    }
}

// Value Object para identidade (2 variáveis)
public class IdentidadeUsuario {
    private final DadosPessoais dadosPessoais;     // 1
    private final Credenciais credenciais;         // 2
    
    public IdentidadeUsuario(DadosPessoais dadosPessoais, Credenciais credenciais) {
        this.dadosPessoais = dadosPessoais;
        this.credenciais = credenciais;
    }
    
    public boolean validarSenha(String senha) {
        return credenciais.validar(senha);
    }
    
    public String obterNome() {
        return dadosPessoais.obterNome();
    }
    
    public Email obterEmail() {
        return dadosPessoais.obterEmail();
    }
    
    public int calcularIdade() {
        return dadosPessoais.calcularIdade();
    }
}

// Value Object para dados pessoais (2 variáveis)
public class DadosPessoais {
    private final InformacoesPessoais informacoes;  // 1
    private final Contato contato;                  // 2
    
    public DadosPessoais(InformacoesPessoais informacoes, Contato contato) {
        this.informacoes = informacoes;
        this.contato = contato;
    }
    
    public String obterNome() {
        return informacoes.obterNome();
    }
    
    public CPF obterCPF() {
        return informacoes.obterCPF();
    }
    
    public int calcularIdade() {
        return informacoes.calcularIdade();
    }
    
    public Email obterEmail() {
        return contato.obterEmail();
    }
    
    public Telefone obterTelefone() {
        return contato.obterTelefone();
    }
}

// Value Object para informações básicas (2 variáveis)
public class InformacoesPessoais {
    private final Nome nome;                        // 1
    private final DocumentosPessoais documentos;    // 2
    
    public InformacoesPessoais(Nome nome, DocumentosPessoais documentos) {
        this.nome = nome;
        this.documentos = documentos;
    }
    
    public String obterNome() {
        return nome.completo();
    }
    
    public CPF obterCPF() {
        return documentos.obterCPF();
    }
    
    public int calcularIdade() {
        return documentos.calcularIdade();
    }
}

// Value Object para documentos (2 variáveis)
public class DocumentosPessoais {
    private final CPF cpf;                         // 1
    private final DataNascimento dataNascimento;   // 2
    
    public DocumentosPessoais(CPF cpf, DataNascimento dataNascimento) {
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }
    
    public CPF obterCPF() {
        return cpf;
    }
    
    public int calcularIdade() {
        return dataNascimento.calcularIdade();
    }
}

// Value Object para contato (2 variáveis)
public class Contato {
    private final Email email;      // 1
    private final Telefone telefone; // 2
    
    public Contato(Email email, Telefone telefone) {
        this.email = email;
        this.telefone = telefone;
    }
    
    public Email obterEmail() {
        return email;
    }
    
    public Telefone obterTelefone() {
        return telefone;
    }
}

// Value Object para credenciais (2 variáveis)
public class Credenciais {
    private final SenhaHash senhaHash;             // 1
    private final HistoricoLogin historicoLogin;   // 2
    
    public Credenciais(SenhaHash senhaHash, HistoricoLogin historicoLogin) {
        this.senhaHash = senhaHash;
        this.historicoLogin = historicoLogin;
    }
    
    public boolean validar(String senha) {
        return senhaHash.validar(senha);
    }
    
    public void registrarLogin() {
        historicoLogin.registrar();
    }
    
    public LocalDateTime obterUltimoLogin() {
        return historicoLogin.obterUltimo();
    }
}

// Value Object para perfil (2 variáveis)
public class PerfilUsuario {
    private final StatusUsuario status;         // 1
    private final PermissoesUsuario permissoes; // 2
    
    public PerfilUsuario(StatusUsuario status, PermissoesUsuario permissoes) {
        this.status = status;
        this.permissoes = permissoes;
    }
    
    public boolean podeAcessar(String recurso) {
        return status.isAtivo() && permissoes.contem(recurso);
    }
    
    public void atualizarUltimoLogin() {
        status.registrarLogin();
    }
    
    public boolean isAtivo() {
        return status.isAtivo();
    }
    
    public TipoUsuario obterTipo() {
        return permissoes.obterTipo();
    }
}

// Value Object para status (2 variáveis)
public class StatusUsuario {
    private final boolean ativo;                   // 1
    private final UltimoLogin ultimoLogin;         // 2
    
    public StatusUsuario(boolean ativo, UltimoLogin ultimoLogin) {
        this.ativo = ativo;
        this.ultimoLogin = ultimoLogin;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public void registrarLogin() {
        ultimoLogin.atualizar();
    }
    
    public LocalDateTime obterUltimoLogin() {
        return ultimoLogin.obter();
    }
}

// Value Object para permissões (2 variáveis)
public class PermissoesUsuario {
    private final TipoUsuario tipo;                // 1
    private final ListaPermissoes listaPermissoes; // 2
    
    public PermissoesUsuario(TipoUsuario tipo, ListaPermissoes listaPermissoes) {
        this.tipo = tipo;
        this.listaPermissoes = listaPermissoes;
    }
    
    public boolean contem(String permissao) {
        return listaPermissoes.contem(permissao);
    }
    
    public TipoUsuario obterTipo() {
        return tipo;
    }
    
    public void adicionarPermissao(String permissao) {
        listaPermissoes.adicionar(permissao);
    }
}
```

### Exemplo de Uso
```java
public class ExemploUsuario {
    public static void main(String[] args) {
        // Construção através de builder para simplificar
        Usuario usuario = new UsuarioBuilder()
            .comNome("João Silva")
            .comEmail("joao@email.com")
            .comCPF("12345678901")
            .comTelefone("11999999999")
            .comDataNascimento(LocalDate.of(1990, 1, 1))
            .comSenha("senha123")
            .comTipo(TipoUsuario.ADMIN)
            .ativo()
            .build();
        
        // Uso simples
        if (usuario.validarCredenciais("senha123")) {
            usuario.registrarLogin();
            
            if (usuario.podeAcessar("ADMIN_PANEL")) {
                System.out.println("Acesso liberado para: " + usuario.obterNome());
            }
        }
    }
}
```

---

## 11. Regra 9: Sem Getters/Setters/Propriedades

### Conceito
**Evite getters e setters**. Em vez de expor dados, exponha comportamentos. Tell, Don't Ask.

### ❌ Código com Getters/Setters (Anemic Domain Model)
```java
public class ContaCorrente {
    private double saldo;
    private boolean ativa;
    private String titular;
    private LocalDateTime ultimaMovimentacao;
    
    // Getters e Setters expõem toda a estrutura interna
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
    
    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }
    
    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }
    
    public LocalDateTime getUltimaMovimentacao() { return ultimaMovimentacao; }
    public void setUltimaMovimentacao(LocalDateTime ultimaMovimentacao) { 
        this.ultimaMovimentacao = ultimaMovimentacao; 
    }
}

// Cliente precisa conhecer a estrutura interna e fazer as validações
public class ServicoTransferencia {
    public void transferir(ContaCorrente origem, ContaCorrente destino, double valor) {
        // Lógica de negócio espalhada no cliente
        if (!origem.isAtiva()) {
            throw new RuntimeException("Conta origem inativa");
        }
        if (!destino.isAtiva()) {
            throw new RuntimeException("Conta destino inativa");
        }
        if (origem.getSaldo() < valor) {
            throw new RuntimeException("Saldo insuficiente");
        }
        if (valor <= 0) {
            throw new RuntimeException("Valor deve ser positivo");
        }
        
        // Manipulação direta dos dados
        origem.setSaldo(origem.getSaldo() - valor);
        destino.setSaldo(destino.getSaldo() + valor);
        
        origem.setUltimaMovimentacao(LocalDateTime.now());
        destino.setUltimaMovimentacao(LocalDateTime.now());
    }
}
```

### ✅ Solução com Comportamentos (Rich Domain Model)
```java
public class ContaCorrente {
    private final NumeroConta numero;
    private final Titular titular;
    private Saldo saldo;
    private StatusConta status;
    private HistoricoMovimentacoes historico;
    
    public ContaCorrente(NumeroConta numero, Titular titular, Saldo saldoInicial) {
        this.numero = numero;
        this.titular = titular;
        this.saldo = saldoInicial;
        this.status = StatusConta.ativa();
        this.historico = new HistoricoMovimentacoes();
    }
    
    // Comportamentos ao invés de getters/setters
    public void debitar(Valor valor) {
        validarContaAtiva();
        validarSaldoSuficiente(valor);
        
        saldo = saldo.subtrair(valor);
        historico.registrarDebito(valor);
    }
    
    public void creditar(Valor valor) {
        validarContaAtiva();
        validarValorPositivo(valor);
        
        saldo = saldo.somar(valor);
        historico.registrarCredito(valor);
    }
    
    public void bloquear(MotivoBloqueioConta motivo) {
        status = StatusConta.bloqueada(motivo);
        historico.registrarBloqueio(motivo);
    }
    
    public void desbloquear() {
        status = StatusConta.ativa();
        historico.registrarDesbloqueio();
    }
    
    public boolean podeReceberTransferencia(Valor valor) {
        return status.permiteCredito() && valor.isMaiorQueZero();
    }
    
    public boolean podeRealizarTransferencia(Valor valor) {
        return status.permiteDebito() && 
               saldo.isSuficientePara(valor) && 
               valor.isMaiorQueZero();
    }
    
    public ExtratoResumido gerarExtratoResumido() {
        return new ExtratoResumido(
            numero,
            titular.obterNome(),
            saldo,
            status,
            historico.obterUltimasMovimentacoes(10)
        );
    }
    
    public RelatorioSaldo gerarRelatorioSaldo() {
        return new RelatorioSaldo(
            numero,
            saldo,
            historico.calcularMediaMovimentacao(),
            historico.obterMaiorMovimentacao()
        );
    }
    
    // Métodos de consulta específicos (quando necessário)
    public boolean estaAtiva() {
        return status.isAtiva();
    }
    
    public boolean pertenceA(Titular outroTitular) {
        return titular.equals(outroTitular);
    }
    
    public boolean temSaldoDisponivel(Valor valor) {
        return saldo.isSuficientePara(valor);
    }
    
    // Validações privadas
    private void validarContaAtiva() {
        if (!status.isAtiva()) {
            throw new ContaInativaException("Conta não está ativa para movimentação");
        }
    }
    
    private void validarSaldoSuficiente(Valor valor) {
        if (!saldo.isSuficientePara(valor)) {
            throw new SaldoInsuficienteException("Saldo insuficiente para a operação");
        }
    }
    
    private void validarValorPositivo(Valor valor) {
        if (!valor.isMaiorQueZero()) {
            throw new ValorInvalidoException("Valor deve ser positivo");
        }
    }
}

// Value Objects de apoio
public class Saldo {
    private final BigDecimal valor;
    
    public Saldo(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Saldo não pode ser nulo");
        }
        this.valor = valor;
    }
    
    public Saldo somar(Valor valor) {
        return new Saldo(this.valor.add(valor.obterQuantia()));
    }
    
    public Saldo subtrair(Valor valor) {
        return new Saldo(this.valor.subtract(valor.obterQuantia()));
    }
    
    public boolean isSuficientePara(Valor valor) {
        return this.valor.compareTo(valor.obterQuantia()) >= 0;
    }
    
    public boolean isPositivo() {
        return valor.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public BigDecimal obterValor() {
        return valor;
    }
}

public class StatusConta {
    private final TipoStatus tipo;
    private final MotivoBloqueioConta motivo;
    
    private StatusConta(TipoStatus tipo, MotivoBloqueioConta motivo) {
        this.tipo = tipo;
        this.motivo = motivo;
    }
    
    public static StatusConta ativa() {
        return new StatusConta(TipoStatus.ATIVA, null);
    }
    
    public static StatusConta bloqueada(MotivoBloqueioConta motivo) {
        return new StatusConta(TipoStatus.BLOQUEADA, motivo);
    }
    
    public boolean isAtiva() {
        return tipo == TipoStatus.ATIVA;
    }
    
    public boolean permiteCredito() {
        return tipo == TipoStatus.ATIVA;
    }
    
    public boolean permiteDebito() {
        return tipo == TipoStatus.ATIVA;
    }
}

// Serviço refatorado
public class ServicoTransferencia {
    public void transferir(ContaCorrente origem, ContaCorrente destino, Valor valor) {
        // Validações através de comportamentos
        if (!origem.podeRealizarTransferencia(valor)) {
            throw new TransferenciaNaoPermitidaException("Conta origem não pode realizar transferência");
        }
        
        if (!destino.podeReceberTransferencia(valor)) {
            throw new TransferenciaNaoPermitidaException("Conta destino não pode receber transferência");
        }
        
        // Operações através de comportamentos
        origem.debitar(valor);
        destino.creditar(valor);
    }
    
    public ExtratoComparativo gerarExtratoComparativo(ContaCorrente conta1, ContaCorrente conta2) {
        // Usa comportamentos específicos ao invés de getters
        ExtratoResumido extrato1 = conta1.gerarExtratoResumido();
        ExtratoResumido extrato2 = conta2.gerarExtratoResumido();
        
        return new ExtratoComparativo(extrato1, extrato2);
    }
}
```

### Quando Getters são Aceitáveis
```java
// ✅ Aceitável: Value Objects imutáveis
public class CPF {
    private final String numero;
    
    public CPF(String numero) {
        // validação...
        this.numero = numero;
    }
    
    // OK: expõe valor de um objeto imutável
    public String getNumero() {
        return numero;
    }
    
    public String getFormatado() {
        // comportamento + exposição
        return formatarCPF(numero);
    }
}

// ✅ Aceitável: DTOs para transferência de dados
public class PedidoDTO {
    private final String numeroPedido;
    private final String nomeCliente;
    private final BigDecimal valorTotal;
    
    public PedidoDTO(String numeroPedido, String nomeCliente, BigDecimal valorTotal) {
        this.numeroPedido = numeroPedido;
        this.nomeCliente = nomeCliente;
        this.valorTotal = valorTotal;
    }
    
    // OK: DTO é apenas estrutura de dados
    public String getNumeroPedido() { return numeroPedido; }
    public String getNomeCliente() { return nomeCliente; }
    public BigDecimal getValorTotal() { return valorTotal; }
}
```

---

## 12. Exemplo Completo: Sistema de Biblioteca

Vamos aplicar todas as 9 regras em um exemplo prático:

```java
// Aplicando as 9 regras em conjunto
public class SistemaBiblioteca {
    private final CatalogoBiblioteca catalogo;     // Regra 8: max 2 variáveis
    private final RegistroEmprestimos registro;    // Regra 8: max 2 variáveis
    
    public SistemaBiblioteca(CatalogoBiblioteca catalogo, RegistroEmprestimos registro) {
        this.catalogo = catalogo;
        this.registro = registro;
    }
    
    // Regra 1: apenas 1 nível de indentação
    public void realizarEmprestimo(Usuario usuario, ISBN isbn) {
        if (podeEmprestar(usuario, isbn)) {                    // Nível 1
            processarEmprestimo(usuario, isbn);
        }
    }
    
    // Regra 2: sem ELSE
    private boolean podeEmprestar(Usuario usuario, ISBN isbn) {
        if (!usuario.estaRegular()) {
            return false;
        }
        
        if (!catalogo.temDisponivel(isbn)) {
            return false;
        }
        
        if (usuario.atingiuLimiteEmprestimos()) {
            return false;
        }
        
        return true;
    }
    
    private void processarEmprestimo(Usuario usuario, ISBN isbn) {
        Livro livro = catalogo.obterLivro(isbn);               // Regra 5: 1 ponto por linha
        PrazoEmprestimo prazo = calcularPrazoEmprestimo(usuario);
        
        Emprestimo emprestimo = criarEmprestimo(usuario, livro, prazo);
        registro.registrar(emprestimo);                        // Regra 9: comportamento ao invés de setter
        catalogo.marcarComoEmprestado(isbn);                   // Regra 9: comportamento
    }
}

// Regra 3: Primitivos encapsulados
public class ISBN {
    private final String codigo;
    
    public ISBN(String codigo) {
        validarISBN(codigo);
        this.codigo = codigo;
    }
    
    private void validarISBN(String codigo) {
        if (codigo == null || codigo.length() != 13) {
            throw new IllegalArgumentException("ISBN deve ter 13 dígitos");
        }
    }
    
    public String obterCodigo() {
        return codigo;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ISBN isbn = (ISBN) obj;
        return Objects.equals(codigo, isbn.codigo);
    }
}

// Regra 4: Coleção de primeira classe
public class CatalogoBiblioteca {
    private final LivrosCatalogo livros;                      // Regra 8: max 2 variáveis
    private final IndiceDiBusca indiceBreca;                  // Regra 8: max 2 variáveis
    
    public CatalogoBiblioteca(LivrosCatalogo livros, IndiceDiBusca indiceDiBusca) {
        this.livros = livros;
        this.indiceBreca = indiceDiBusca;
    }
    
    // Regra 9: comportamentos ao invés de getters
    public boolean temDisponivel(ISBN isbn) {
        return livros.estaDisponivel(isbn);
    }
    
    public Livro obterLivro(ISBN isbn) {
        return livros.buscar(isbn);
    }
    
    public void marcarComoEmprestado(ISBN isbn) {
        livros.marcarEmprestado(isbn);
    }
    
    public ResultadosBusca buscarPorAutor(Autor autor) {
        return indiceBreca.buscarPorAutor(autor);
    }
}

// Regra 4: Coleção de primeira classe para livros
public class LivrosCatalogo {
    private final Map<ISBN, EstadoLivro> estadosLivros;
    
    public LivrosCatalogo() {
        this.estadosLivros = new HashMap<>();
    }
    
    public void adicionar(Livro livro) {
        ISBN isbn = livro.obterISBN();
        validarLivroNovo(isbn);
        
        estadosLivros.put(isbn, EstadoLivro.disponivel(livro));
    }
    
    public boolean estaDisponivel(ISBN isbn) {
        EstadoLivro estado = estadosLivros.get(isbn);
        return estado != null && estado.estaDisponivel();
    }
    
    public Livro buscar(ISBN isbn) {
        EstadoLivro estado = estadosLivros.get(isbn);
        if (estado == null) {
            throw new LivroNaoEncontradoException(isbn);
        }
        return estado.obterLivro();
    }
    
    public void marcarEmprestado(ISBN isbn) {
        EstadoLivro estado = estadosLivros.get(isbn);
        if (estado == null || !estado.estaDisponivel()) {
            throw new LivroNaoDisponivelException(isbn);
        }
        
        estadosLivros.put(isbn, estado.marcarComoEmprestado());
    }
    
    private void validarLivroNovo(ISBN isbn) {
        if (estadosLivros.containsKey(isbn)) {
            throw new LivroJaExisteException(isbn);
        }
    }
}

// Regra 7: Entidades pequenas
public class Usuario {
    private final IdentificacaoUsuario identificacao;         // Regra 8: max 2 variáveis
    private final SituacaoUsuario situacao;                   // Regra 8: max 2 variáveis
    
    public Usuario(IdentificacaoUsuario identificacao, SituacaoUsuario situacao) {
        this.identificacao = identificacao;
        this.situacao = situacao;
    }
    
    // Regra 9: comportamentos
    public boolean estaRegular() {
        return situacao.estaRegular();
    }
    
    public boolean atingiuLimiteEmprestimos() {
        return situacao.atingiuLimite();
    }
    
    public NumeroUsuario obterNumero() {
        return identificacao.obterNumero();
    }
    
    public NomeUsuario obterNome() {
        return identificacao.obterNome();
    }
}

// Regra 6: Nomes não abreviados + Regra 7: Entidade pequena
public class IdentificacaoUsuario {
    private final NumeroUsuario numeroUsuario;                // Regra 8: max 2 variáveis
    private final NomeUsuario nomeUsuario;                    // Regra 8: max 2 variáveis
    
    public IdentificacaoUsuario(NumeroUsuario numeroUsuario, NomeUsuario nomeUsuario) {
        this.numeroUsuario = numeroUsuario;
        this.nomeUsuario = nomeUsuario;
    }
    
    public NumeroUsuario obterNumero() {
        return numeroUsuario;
    }
    
    public NomeUsuario obterNome() {
        return nomeUsuario;
    }
}

// Value Objects seguindo todas as regras
public class NumeroUsuario {
    private final String valor;
    
    public NumeroUsuario(String valor) {
        validarNumeroUsuario(valor);
        this.valor = valor;
    }
    
    private void validarNumeroUsuario(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("Número do usuário não pode ser vazio");
        }
        
        if (valor.length() != 8) {
            throw new IllegalArgumentException("Número do usuário deve ter 8 dígitos");
        }
    }
    
    public String obterValor() {
        return valor;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NumeroUsuario that = (NumeroUsuario) obj;
        return Objects.equals(valor, that.valor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
}
```

---

## 13. Benefícios Alcançados

### Código Antes vs Depois das Regras

**❌ Antes (Código Procedural)**
- Classes gigantes com 10+ responsabilidades
- Métodos com 50+ linhas e lógica aninhada
- Primitivos nus espalhados por todo lugar
- Getters/setters expondo estrutura interna
- Nomes abreviados e confusos
- Alto acoplamento entre classes

**✅ Depois (Object Calisthenics)**
- Classes pequenas e focadas
- Métodos de 5 linhas com responsabilidade única  
- Value objects expressivos e validados
- Comportamentos ricos ao invés de dados expostos
- Nomes auto-explicativos
- Baixo acoplamento, alta coesão

### Métricas de Qualidade

```java
// Exemplo de medição
public class MetricasCodigo {
    
    // ❌ Código original: 
    // - Complexidade ciclomática: 25
    // - Linhas por método: 45
    // - Variáveis de instância: 12
    // - Níveis de indentação: 5
    
    // ✅ Com Object Calisthenics:
    // - Complexidade ciclomática: 3
    // - Linhas por método: 4
    // - Variáveis de instância: 2
    // - Níveis de indentação: 1
}
```

---

## 14. Quando Relaxar as Regras

### Situações Onde Flexibilidade é Aceitável

1. **Performance Crítica**
```java
// Aceitável: cache com múltiplas variáveis para performance
public class CacheOtimizado {
    private final Map<String, Object> dados;
    private final Map<String, Long> tempos;
    private final LRUEvictionPolicy politicaRemocao;
    private final CacheStatistics estatisticas;
    // Mais de 2 variáveis por necessidade de performance
}
```

2. **DTOs e Serialização**
```java
// Aceitável: DTO com getters para frameworks
@JsonSerializable
public class PedidoResponseDTO {
    private String id;
    private String status;
    private BigDecimal valor;
    
    // Getters necessários para serialização JSON
    public String getId() { return id; }
    public String getStatus() { return status; }
    public BigDecimal getValor() { return valor; }
}
```

3. **Integração com Frameworks**
```java
// Aceitável: Entity JPA com algumas concessões
@Entity
public class PedidoEntity {
    @Id
    private String id;
    
    @Embedded
    private DadosCliente cliente;  // Mantém encapsulamento onde possível
    
    @Embedded 
    private ValoresPedido valores; // Agrupa valores relacionados
    
    // Getters mínimos necessários para JPA
    public String getId() { return id; }
}
```

---

## 15. Exercícios Práticos

### Exercício 1: Refatore este código
```java
// ❌ Código para refatorar
public class Calculadora {
    public double calc(String op, double a, double b) {
        if (op.equals("sum")) {
            if (a > 0 && b > 0) {
                return a + b;
            } else {
                if (a < 0 || b < 0) {
                    throw new RuntimeException("Negative numbers not allowed");
                } else {
                    return 0;
                }
            }
        } else if (op.equals("sub")) {
            return a - b;
        } else if (op.equals("mul")) {
            return a * b;
        } else if (op.equals("div")) {
            if (b != 0) {
                return a / b;
            } else {
                throw new RuntimeException("Division by zero");
            }
        } else {
            throw new RuntimeException("Unknown operation");
        }
    }
}
```

### ✅ Solução Aplicando Object Calisthenics
```java
// Regra 3: Envolva primitivos
public class NumeroCalculadora {
    private final double valor;
    
    public NumeroCalculadora(double valor) {
        this.valor = valor;
    }
    
    public boolean isPositivo() {
        return valor > 0;
    }
    
    public boolean isZero() {
        return valor == 0;
    }
    
    public double obterValor() {
        return valor;
    }
}

public class TipoOperacao {
    private final String operacao;
    
    public TipoOperacao(String operacao) {
        validarOperacao(operacao);
        this.operacao = operacao;
    }
    
    private void validarOperacao(String operacao) {
        if (operacao == null || operacao.trim().isEmpty()) {
            throw new IllegalArgumentException("Operação não pode ser vazia");
        }
    }
    
    public boolean isSoma() {
        return "sum".equals(operacao);
    }
    
    public boolean isSubtracao() {
        return "sub".equals(operacao);
    }
    
    public boolean isMultiplicacao() {
        return "mul".equals(operacao);
    }
    
    public boolean isDivisao() {
        return "div".equals(operacao);
    }
}

// Regra 2: Sem ELSE + Regra 1: Um nível de indentação
public class CalculadoraRefatorada {
    private final OperacoesMatemamáticas operacoes;            // Regra 8: max 2 variáveis
    private final ValidadorCalculadora validador;              // Regra 8: max 2 variáveis
    
    public CalculadoraRefatorada(OperacoesMatemáticas operacoes, ValidadorCalculadora validador) {
        this.operacoes = operacoes;
        this.validador = validador;
    }
    
    // Regra 1: apenas 1 nível de indentação
    public NumeroCalculadora calcular(TipoOperacao tipoOperacao, NumeroCalculadora primeiroNumero, NumeroCalculadora segundoNumero) {
        if (tipoOperacao.isSoma()) {                           // Nível 1
            return calcularSoma(primeiroNumero, segundoNumero);
        }
        if (tipoOperacao.isSubtracao()) {                      // Nível 1
            return calcularSubtracao(primeiroNumero, segundoNumero);
        }
        if (tipoOperacao.isMultiplicacao()) {                  // Nível 1
            return calcularMultiplicacao(primeiroNumero, segundoNumero);
        }
        if (tipoOperacao.isDivisao()) {                        // Nível 1
            return calcularDivisao(primeiroNumero, segundoNumero);
        }
        
        throw new OperacaoNaoSuportadaException(tipoOperacao);
    }
    
    // Regra 2: sem ELSE
    private NumeroCalculadora calcularSoma(NumeroCalculadora primeiro, NumeroCalculadora segundo) {
        validador.validarSoma(primeiro, segundo);
        return operacoes.somar(primeiro, segundo);
    }
    
    private NumeroCalculadora calcularSubtracao(NumeroCalculadora primeiro, NumeroCalculadora segundo) {
        return operacoes.subtrair(primeiro, segundo);
    }
    
    private NumeroCalculadora calcularMultiplicacao(NumeroCalculadora primeiro, NumeroCalculadora segundo) {
        return operacoes.multiplicar(primeiro, segundo);
    }
    
    private NumeroCalculadora calcularDivisao(NumeroCalculadora primeiro, NumeroCalculadora segundo) {
        validador.validarDivisao(segundo);
        return operacoes.dividir(primeiro, segundo);
    }
}

// Regra 7: Entidades pequenas
public class ValidadorCalculadora {
    public void validarSoma(NumeroCalculadora primeiro, NumeroCalculadora segundo) {
        if (!primeiro.isPositivo()) {
            throw new NumeroNegativoException("Primeiro número deve ser positivo para soma");
        }
        if (!segundo.isPositivo()) {
            throw new NumeroNegativoException("Segundo número deve ser positivo para soma");
        }
    }
    
    public void validarDivisao(NumeroCalculadora divisor) {
        if (divisor.isZero()) {
            throw new DivisaoPorZeroException("Não é possível dividir por zero");
        }
    }
}

// Regra 7: Entidade pequena para operações
public class OperacoesMatemamáticas {
    public NumeroCalculadora somar(NumeroCalculadora primeiro, NumeroCalculadora segundo) {
        double resultado = primeiro.obterValor() + segundo.obterValor();
        return new NumeroCalculadora(resultado);
    }
    
    public NumeroCalculadora subtrair(NumeroCalculadora primeiro, NumeroCalculadora segundo) {
        double resultado = primeiro.obterValor() - segundo.obterValor();
        return new NumeroCalculadora(resultado);
    }
    
    public NumeroCalculadora multiplicar(NumeroCalculadora primeiro, NumeroCalculadora segundo) {
        double resultado = primeiro.obterValor() * segundo.obterValor();
        return new NumeroCalculadora(resultado);
    }
    
    public NumeroCalculadora dividir(NumeroCalculadora primeiro, NumeroCalculadora segundo) {
        double resultado = primeiro.obterValor() / segundo.obterValor();
        return new NumeroCalculadora(resultado);
    }
}
```

---

## 16. Padrões de Design Facilitados por Object Calisthenics

### Strategy Pattern Natural
```java
// Object Calisthenics facilita patterns como Strategy
public interface EstrategiaDesconto {
    Dinheiro calcular(Dinheiro valorOriginal);
}

// Cada estratégia é pequena e focada (Regra 7)
public class DescontoClienteRegular implements EstrategiaDesconto {
    private final PercentualDesconto percentual;              // Regra 8: max 2 variáveis
    private final ValorMinimoDesconto valorMinimo;            // Regra 8: max 2 variáveis
    
    public DescontoClienteRegular(PercentualDesconto percentual, ValorMinimoDesconto valorMinimo) {
        this.percentual = percentual;
        this.valorMinimo = valorMinimo;
    }
    
    @Override
    public Dinheiro calcular(Dinheiro valorOriginal) {        // Regra 1: 1 nível indentação
        if (valorOriginal.isMaiorQue(valorMinimo.obterValor())) {
            return valorOriginal.aplicarDesconto(percentual);
        }
        
        return valorOriginal;
    }
}

public class DescontoClientePremium implements EstrategiaDesconto {
    private final PercentualDesconto percentualBase;          // Regra 8: max 2 variáveis  
    private final BonusDesconto bonusDesconto;                // Regra 8: max 2 variáveis
    
    public DescontoClientePremium(PercentualDesconto percentualBase, BonusDesconto bonusDesconto) {
        this.percentualBase = percentualBase;
        this.bonusDesconto = bonusDesconto;
    }
    
    @Override
    public Dinheiro calcular(Dinheiro valorOriginal) {
        Dinheiro comDesconto = valorOriginal.aplicarDesconto(percentualBase);
        return bonusDesconto.aplicar(comDesconto);
    }
}

// Uso do padrão
public class CalculadoraDescontoEstrategia {
    private final MapaEstrategias estrategias;                // Regra 8: max 2 variáveis
    private final EstrategiaDesconto estrategiaPadrao;        // Regra 8: max 2 variáveis
    
    public CalculadoraDescontoEstrategia(MapaEstrategias estrategias, EstrategiaDesconto estrategiaPadrao) {
        this.estrategias = estrategias;
        this.estrategiaPadrao = estrategiaPadrao;
    }
    
    public Dinheiro calcularDesconto(Cliente cliente, Dinheiro valor) {
        EstrategiaDesconto estrategia = estrategias.obterPara(cliente.obterTipo());
        
        if (estrategia == null) {                             // Regra 2: sem ELSE
            return estrategiaPadrao.calcular(valor);
        }
        
        return estrategia.calcular(valor);
    }
}
```

### Command Pattern com Value Objects
```java
// Commands seguindo Object Calisthenics
public abstract class ComandoPedido {
    private final IdentificadorComando identificador;        // Regra 8: max 2 variáveis
    private final TimestampComando timestamp;                // Regra 8: max 2 variáveis
    
    protected ComandoPedido(IdentificadorComando identificador, TimestampComando timestamp) {
        this.identificador = identificador;
        this.timestamp = timestamp;
    }
    
    // Regra 9: comportamento ao invés de getter
    public abstract void executar(Pedido pedido);
    
    public boolean foiExecutadoDepois(ComandoPedido outroComando) {
        return timestamp.isDepois(outroComando.timestamp);
    }
}

public class ComandoProcessarPagamento extends ComandoPedido {
    private final FormaPagamento formaPagamento;              // Regra 8: max 2 variáveis
    private final ValorPagamento valorPagamento;              // Regra 8: max 2 variáveis
    
    public ComandoProcessarPagamento(IdentificadorComando identificador, 
                                   TimestampComando timestamp,
                                   FormaPagamento formaPagamento, 
                                   ValorPagamento valorPagamento) {
        super(identificador, timestamp);
        this.formaPagamento = formaPagamento;
        this.valorPagamento = valorPagamento;
    }
    
    @Override
    public void executar(Pedido pedido) {                    // Regra 1: 1 nível indentação
        if (pedido.podeProcessarPagamento()) {
            processarPagamento(pedido);
        }
    }
    
    private void processarPagamento(Pedido pedido) {         // Regra 2: sem ELSE
        ResultadoPagamento resultado = formaPagamento.processar(valorPagamento);
        pedido.registrarPagamento(resultado);
    }
}
```

### Factory Pattern Orientado a Objetos
```java
// Factory seguindo todas as regras
public class FabricaPedidos {
    private final ValidadorDadosPedido validador;            // Regra 8: max 2 variáveis
    private final GeradorNumeroPedido geradorNumero;         // Regra 8: max 2 variáveis
    
    public FabricaPedidos(ValidadorDadosPedido validador, GeradorNumeroPedido geradorNumero) {
        this.validador = validador;
        this.geradorNumero = geradorNumero;
    }
    
    // Regra 6: nomes não abreviados
    public Pedido criarPedidoRegular(DadosClientePedido dadosCliente, ItensPedido itens) {
        validador.validar(dadosCliente, itens);
        NumeroPedido numero = geradorNumero.gerar();
        
        return construirPedidoRegular(numero, dadosCliente, itens);
    }
    
    public Pedido criarPedidoExpresso(DadosClientePedido dadosCliente, ItensPedido itens, PrazoEntrega prazo) {
        validador.validarPedidoExpresso(dadosCliente, itens, prazo);
        NumeroPedido numero = geradorNumero.gerar();
        
        return construirPedidoExpresso(numero, dadosCliente, itens, prazo);
    }
    
    // Regra 7: métodos pequenos
    private Pedido construirPedidoRegular(NumeroPedido numero, DadosClientePedido dadosCliente, ItensPedido itens) {
        return new Pedido(numero, dadosCliente, itens, TipoPedido.REGULAR);
    }
    
    private Pedido construirPedidoExpresso(NumeroPedido numero, DadosClientePedido dadosCliente, ItensPedido itens, PrazoEntrega prazo) {
        ConfiguracaoExpresso configuracao = new ConfiguracaoExpresso(prazo);
        return new PedidoExpresso(numero, dadosCliente, itens, configuracao);
    }
}
```

---

## 17. Testabilidade com Object Calisthenics

### Testes Mais Simples e Focados
```java
// Testes tornam-se mais simples com classes pequenas
public class TesteCalculadoraDesconto {
    
    @Test
    public void deveCalcularDescontoClienteRegular() {
        // Arrange - Regra 3: primitivos encapsulados
        PercentualDesconto percentual = new PercentualDesconto(10);
        ValorMinimoDesconto valorMinimo = new ValorMinimoDesconto(new BigDecimal("100"));
        
        DescontoClienteRegular estrategia = new DescontoClienteRegular(percentual, valorMinimo);
        Dinheiro valorOriginal = new Dinheiro(new BigDecimal("200"));
        
        // Act - Regra 9: comportamento ao invés de dados
        Dinheiro resultado = estrategia.calcular(valorOriginal);
        
        // Assert
        Dinheiro esperado = new Dinheiro(new BigDecimal("180"));
        assertThat(resultado).isEqualTo(esperado);
    }
    
    @Test
    public void naoDeveAplicarDescontoParaValorBaixo() {
        // Arrange
        PercentualDesconto percentual = new PercentualDesconto(10);
        ValorMinimoDesconto valorMinimo = new ValorMinimoDesconto(new BigDecimal("100"));
        
        DescontoClienteRegular estrategia = new DescontoClienteRegular(percentual, valorMinimo);
        Dinheiro valorOriginal = new Dinheiro(new BigDecimal("50"));
        
        // Act
        Dinheiro resultado = estrategia.calcular(valorOriginal);
        
        // Assert
        assertThat(resultado).isEqualTo(valorOriginal);
    }
}

// Testes de Value Objects são simples
public class TesteCPF {
    
    @Test
    public void deveCriarCPFValido() {
        // Arrange & Act
        CPF cpf = new CPF("12345678901");
        
        // Assert
        assertThat(cpf.getNumero()).isEqualTo("12345678901");
        assertThat(cpf.getFormatado()).isEqualTo("123.456.789-01");
    }
    
    @Test
    public void deveRejeitarCPFInvalido() {
        // Act & Assert
        assertThatThrownBy(() -> new CPF("123"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("CPF deve ter 11 dígitos");
    }
    
    @Test
    public void deveCompararCPFsCorretamente() {
        // Arrange
        CPF cpf1 = new CPF("12345678901");
        CPF cpf2 = new CPF("12345678901");
        CPF cpf3 = new CPF("10987654321");
        
        // Assert
        assertThat(cpf1).isEqualTo(cpf2);
        assertThat(cpf1).isNotEqualTo(cpf3);
    }
}

// Mock mais focado devido às interfaces pequenas
public class TesteProcessadorPedido {
    
    @Mock private ValidadorPedido validador;
    @Mock private CalculadoraValores calculadora;
    @Mock private ProcessadorPagamento processadorPagamento;
    @Mock private GerenciadorEstoque gerenciadorEstoque;
    @Mock private SeletorTransportadora seletorTransportadora;
    @Mock private NotificadorCliente notificador;
    @Mock private RegistradorAuditoria auditor;
    
    private ProcessadorPedido processador;
    
    @BeforeEach
    void setUp() {
        processador = new ProcessadorPedido(
            validador, calculadora, processadorPagamento,
            gerenciadorEstoque, seletorTransportadora, 
            notificador, auditor
        );
    }
    
    @Test
    public void deveProcessarPedidoComSucesso() {
        // Arrange
        Pedido pedido = criarPedidoValido();
        Transportadora transportadora = mock(Transportadora.class);
        String codigoRastreamento = "ABC123";
        
        when(seletorTransportadora.selecionar(pedido)).thenReturn(transportadora);
        when(transportadora.criarEnvio(pedido)).thenReturn(codigoRastreamento);
        
        // Act
        ResultadoProcessamento resultado = processador.processar(pedido);
        
        // Assert
        assertThat(resultado.getCodigoRastreamento()).isEqualTo(codigoRastreamento);
        
        // Verify - cada colaborador tem responsabilidade específica
        verify(validador).validar(pedido);
        verify(calculadora).calcular(pedido);
        verify(processadorPagamento).processar(pedido);
        verify(gerenciadorEstoque).reservar(pedido);
        verify(notificador).notificar(pedido);
        verify(auditor).registrar(pedido);
    }
}
```

---

## 18. Ferramentas e Métricas para Object Calisthenics

### Plugins e Ferramentas de Análise
```java
// Configuração para medir complexidade
public class AnaliseComplexidade {
    
    // Ferramentas recomendadas:
    // - Checkstyle: regras customizadas para Object Calisthenics
    // - PMD: análise de complexidade ciclomática
    // - SpotBugs: detecção de code smells
    // - SonarQube: métricas de qualidade
    
    // Exemplo de regra Checkstyle personalizada
    /*
    <module name="MethodLength">
        <property name="max" value="5"/>
        <property name="countEmpty" value="false"/>
    </module>
    
    <module name="ParameterNumber">
        <property name="max" value="3"/>
    </module>
    
    <module name="ClassDataAbstractionCoupling">
        <property name="max" value="2"/>
    </module>
    */
}

// Annotation customizada para marcar violações intencionais
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ViolacaoIntencionalObjectCalisthenics {
    String motivo();
    String[] regrasVioladas();
}

// Uso da annotation
@ViolacaoIntencionalObjectCalisthenics(
    motivo = "Performance crítica requer cache com múltiplas variáveis",
    regrasVioladas = {"Regra 8: Máximo 2 variáveis de instância"}
)
public class CacheHighPerformance {
    private final Map<String, Object> cache;
    private final Map<String, Long> timestamps;
    private final Queue<String> lruQueue;
    private final AtomicLong hitCount;
    private final AtomicLong missCount;
    // Violação justificada para performance
}
```

### Métricas de Acompanhamento
```java
public class MetricasObjectCalisthenics {
    
    // Métricas importantes para acompanhar:
    
    // 1. Complexidade Ciclomática Média
    public void medirComplexidadeMedia() {
        // Target: < 3 por método
        // Ferramenta: PMD, SonarQube
    }
    
    // 2. Linhas por Método
    public void medirLinhasPorMetodo() {
        // Target: < 5 linhas por método
        // Ferramenta: Checkstyle
    }
    
    // 3. Número de Variáveis de Instância
    public void medirVariaveisDeInstancia() {
        // Target: ≤ 2 por classe
        // Ferramenta: regex customizado ou plugin
    }
    
    // 4. Profundidade de Indentação
    public void medirProfundidadeIndentacao() {
        // Target: ≤ 1 nível por método
        // Ferramenta: Checkstyle com regra customizada
    }
    
    // 5. Cobertura de Testes
    public void medirCoberturaTestes() {
        // Target: > 90% (mais fácil com classes pequenas)
        // Ferramenta: JaCoCo
    }
    
    // 6. Acoplamento Between Objects (CBO)
    public void medirAcoplamento() {
        // Target: < 10 dependências por classe
        // Ferramenta: Metrics plugin do IntelliJ
    }
    
    // 7. Lack of Cohesion Methods (LCOM)
    public void medirCoesao() {
        // Target: próximo de 0 (alta coesão)
        // Ferramenta: SonarQube, Metrics plugin
    }
}
```

---

## 19. Migração Gradual para Object Calisthenics

### Estratégia de Adoção Progressiva
```java
// Fase 1: Comece com código novo
public class NovaFuncionalidade {
    // Aplique todas as 9 regras em código novo
    private final ValidadorEntrada validador;                // Regra 8
    private final ProcessadorLogica processador;             // Regra 8
    
    public NovaFuncionalidade(ValidadorEntrada validador, ProcessadorLogica processador) {
        this.validador = validador;
        this.processador = processador;
    }
    
    public ResultadoProcessamento processar(DadosEntrada dados) {  // Regra 1
        if (validador.isValido(dados)) {
            return processador.processar(dados);
        }
        
        throw new DadosInvalidosException();                      // Regra 2: sem ELSE
    }
}

// Fase 2: Refatore classes existentes gradualmente
public class ClasseExistenteRefatorada {
    // Antes: 15 variáveis de instância, métodos de 50 linhas
    
    // Depois: extrair responsabilidades
    private final ResponsabilidadePrimaria responsabilidadePrimaria;     // Regra 8
    private final ResponsabilidadeSecundaria responsabilidadeSecundaria; // Regra 8
    
    public ClasseExistenteRefatorada(ResponsabilidadePrimaria primaria, ResponsabilidadeSecundaria secundaria) {
        this.responsabilidadePrimaria = primaria;
        this.responsabilidadeSecundaria = secundaria;
    }
    
    // Métodos grandes quebrados em pequenos
    public void operacaoComplexaRefatorada(ParametrosOperacao parametros) {  // Regra 1
        if (responsabilidadePrimaria.podeProsseguir(parametros)) {
            executarOperacaoPrimaria(parametros);
        }
    }
    
    private void executarOperacaoPrimaria(ParametrosOperacao parametros) {   // Regra 7
        ResultadoOperacao resultado = responsabilidadePrimaria.executar(parametros);
        responsabilidadeSecundaria.processar(resultado);
    }
}

// Fase 3: Criar adaptadores para código legado
public class AdaptadorCodigoLegado {
    private final SistemaLegado sistemaLegado;               // Regra 8
    private final ConvertorDados conversor;                  // Regra 8
    
    public AdaptadorCodigoLegado(SistemaLegado sistemaLegado, ConvertorDados conversor) {
        this.sistemaLegado = sistemaLegado;
        this.conversor = conversor;
    }
    
    // Interface limpa que esconde a complexidade do sistema legado
    public ResultadoModerno processar(DadosModernos dados) {
        DadosLegados dadosLegados = conversor.paraLegado(dados);
        String resultadoLegado = sistemaLegado.processarComplexo(dadosLegados);  // Método gigante do legado
        return conversor.paraModerno(resultadoLegado);
    }
}
```

### Plano de Migração
```java
public class PlanoMigracaoObjectCalisthenics {
    
    // Semana 1-2: Estabelecer métricas base
    public void estabelecerMetricas() {
        // - Configurar ferramentas de análise
        // - Medir estado atual do código
        // - Definir metas de melhoria
    }
    
    // Semana 3-4: Aplicar regras em código novo
    public void aplicarEmCodigoNovo() {
        // - Treinar equipe nas 9 regras
        // - Configurar code review checklist
        // - Aplicar em todas as novas features
    }
    
    // Mês 2: Refatorar classes críticas
    public void refatorarClassesCriticas() {
        // - Identificar classes com maior complexidade
        // - Refatorar uma classe por sprint
        // - Priorizar classes com mais bugs
    }
    
    // Mês 3-6: Migração gradual
    public void migracaoGradual() {
        // - 20% do tempo dedicado à refatoração
        // - Criar adaptadores para código legado
        // - Medir progresso mensalmente
    }
    
    // Mês 6+: Manutenção e evolução
    public void manutencaoEvolucao() {
        // - Revisar métricas regularmente
        // - Ajustar regras conforme necessário
        // - Compartilhar aprendizados
    }
}
```

---

## 20. Conclusão e Próximos Passos

### Resumo das 9 Regras

1. **Um nível de indentação por método** → Métodos focados e legíveis
2. **Não use ELSE** → Fluxo de código mais claro
3. **Envolva primitivos e strings** → Value objects expressivos
4. **Coleções em primeira classe** → Comportamentos ricos
5. **Um ponto por linha** → Baixo acoplamento
6. **Não abrevie** → Código auto-documentado
7. **Mantenha entidades pequenas** → Alta coesão
8. **Não mais que duas variáveis de instância** → Responsabilidade única
9. **Sem getters/setters/propriedades** → Tell, don't ask

### Benefícios Alcançados

✅ **Código mais limpo**: Cada classe tem responsabilidade clara  
✅ **Melhor testabilidade**: Classes pequenas são fáceis de testar  
✅ **Menor complexidade**: Métodos simples e lineares  
✅ **Maior expressividade**: Value objects comunicam intenção  
✅ **Facilita refatoração**: Baixo acoplamento permite mudanças seguras  
✅ **Reduz bugs**: Validações centralizadas e comportamentos controlados  
✅ **Acelera desenvolvimento**: Código previsível e consistente  

### Quando Object Calisthenics é Mais Efetivo

🎯 **Projetos greenfield** - Aplicar desde o início  
🎯 **Equipes em crescimento** - Estabelece padrões claros  
🎯 **Sistemas de domínio rico** - Modela regras de negócio complexas  
🎯 **Aplicações com alta demanda de qualidade** - Reduz defeitos  

### Próximos Passos

1. **Comece pequeno**: Escolha 3 regras para aplicar primeiro
2. **Pratique consistentemente**: Aplique em todo código novo
3. **Meça progresso**: Use ferramentas para acompanhar métricas
4. **Refatore gradualmente**: Melhore código existente aos poucos
5. **Compartilhe conhecimento**: Ensine a equipe através de exemplos

### Lembre-se

Object Calisthenics são **exercícios de disciplina**, não dogmas absolutos. O objetivo é desenvolver intuição para escrever código orientado a objetos de qualidade. Com o tempo, você naturalmente relaxará algumas regras quando apropriado, mas terá a base sólida para tomar essas decisões conscientemente.

**"A disciplina dos exercícios desenvolve a intuição para o belo código orientado a objetos."** - Jeff Bay