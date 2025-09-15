# Guia Completo: Princípios SOLID em Java

## 🎯 O que são os Princípios SOLID?

Imagine que você está construindo uma casa. Os princípios SOLID são como as regras básicas da arquitetura que garantem que sua casa seja:
- **Forte** (não desaba)
- **Flexível** (pode ser reformada facilmente)
- **Organizável** (cada cômodo tem sua função)
- **Sustentável** (fácil de manter)

Em programação, SOLID é um acrônimo de 5 princípios que tornam o código mais limpo, flexível e fácil de manter.

---

## 📚 Os 5 Princípios SOLID

```
S - Single Responsibility Principle (Princípio da Responsabilidade Única)
O - Open/Closed Principle (Princípio Aberto/Fechado)
L - Liskov Substitution Principle (Princípio da Substituição de Liskov)
I - Interface Segregation Principle (Princípio da Segregação de Interface)
D - Dependency Inversion Principle (Princípio da Inversão de Dependência)
```

---

## 1️⃣ Single Responsibility Principle (SRP)

### 🤔 **O que significa?**
"Uma classe deve ter apenas um motivo para mudar"

Pense numa caneta: ela só serve para escrever. Se você precisar de uma borracha, não modifica a caneta - você pega uma borracha separada.

### ❌ **Exemplo RUIM - Violando o SRP**

```java
// Esta classe tem MUITAS responsabilidades!
public class Usuario {
    private String nome;
    private String email;
    private String senha;
    
    // Responsabilidade 1: Validar dados
    public boolean validarEmail() {
        return email.contains("@") && email.contains(".");
    }
    
    // Responsabilidade 2: Salvar no banco
    public void salvarNoBanco() {
        // Código para conectar ao banco
        // Código SQL para inserir
        System.out.println("Salvando no banco...");
    }
    
    // Responsabilidade 3: Enviar email
    public void enviarEmailBoasVindas() {
        // Código para configurar SMTP
        // Código para enviar email
        System.out.println("Enviando email...");
    }
    
    // Responsabilidade 4: Gerar relatório
    public void gerarRelatorio() {
        System.out.println("Relatório: " + nome + " - " + email);
    }
}
```

**Problemas:**
- Se mudar a validação de email, mexe na classe Usuario
- Se mudar o banco de dados, mexe na classe Usuario
- Se mudar o servidor de email, mexe na classe Usuario
- Classe grande e difícil de testar

### ✅ **Exemplo BOM - Seguindo o SRP**

```java
// Classe Usuario: apenas dados e lógica básica do usuário
public class Usuario {
    private String nome;
    private String email;
    private String senha;
    
    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
    
    // Getters e Setters
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
}

// Classe separada para validação
public class ValidadorUsuario {
    public boolean validarEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    public boolean validarSenha(String senha) {
        return senha != null && senha.length() >= 8;
    }
}

// Classe separada para persistência
public class RepositorioUsuario {
    public void salvar(Usuario usuario) {
        // Lógica específica do banco de dados
        System.out.println("Salvando usuário: " + usuario.getNome());
    }
    
    public Usuario buscarPorEmail(String email) {
        // Lógica de busca no banco
        return null; // Simplificado
    }
}

// Classe separada para email
public class ServicoEmail {
    public void enviarBoasVindas(Usuario usuario) {
        System.out.println("Enviando email de boas-vindas para: " + usuario.getEmail());
    }
}

// Classe separada para relatórios
public class GeradorRelatorio {
    public void gerarRelatorioUsuario(Usuario usuario) {
        System.out.println("Relatório: " + usuario.getNome() + " - " + usuario.getEmail());
    }
}
```

**Vantagens:**
- Cada classe tem uma responsabilidade clara
- Fácil de testar cada parte separadamente
- Se precisar mudar a validação, só mexe no ValidadorUsuario
- Código mais organizizado e reutilizável

---

## 2️⃣ Open/Closed Principle (OCP)

### 🤔 **O que significa?**
"Classes devem estar abertas para extensão, mas fechadas para modificação"

É como um smartphone: você pode adicionar apps novos (extensão), mas não precisa mexer no sistema operacional (modificação).

### ❌ **Exemplo RUIM - Violando o OCP**

```java
public class CalculadoraDesconto {
    public double calcular(String tipoCliente, double valor) {
        if (tipoCliente.equals("REGULAR")) {
            return valor * 0.05; // 5% desconto
        } else if (tipoCliente.equals("VIP")) {
            return valor * 0.10; // 10% desconto
        } else if (tipoCliente.equals("PREMIUM")) {
            return valor * 0.15; // 15% desconto
        }
        return 0;
    }
}
```

**Problema:** A cada novo tipo de cliente, preciso modificar a classe CalculadoraDesconto!

### ✅ **Exemplo BOM - Seguindo o OCP**

```java
// Interface que define o contrato
public interface CalculadoraDesconto {
    double calcular(double valor);
}

// Implementação para cliente regular
public class DescontoClienteRegular implements CalculadoraDesconto {
    @Override
    public double calcular(double valor) {
        return valor * 0.05; // 5% desconto
    }
}

// Implementação para cliente VIP
public class DescontoClienteVip implements CalculadoraDesconto {
    @Override
    public double calcular(double valor) {
        return valor * 0.10; // 10% desconto
    }
}

// Implementação para cliente Premium
public class DescontoClientePremium implements CalculadoraDesconto {
    @Override
    public double calcular(double valor) {
        return valor * 0.15; // 15% desconto
    }
}

// Classe que usa as calculadoras
public class ProcessadorPedido {
    public double calcularValorFinal(double valorOriginal, CalculadoraDesconto calculadora) {
        double desconto = calculadora.calcular(valorOriginal);
        return valorOriginal - desconto;
    }
}

// Exemplo de uso
public class ExemploOCP {
    public static void main(String[] args) {
        ProcessadorPedido processador = new ProcessadorPedido();
        
        double valor = 1000.0;
        
        // Cliente Regular
        CalculadoraDesconto regular = new DescontoClienteRegular();
        System.out.println("Regular: " + processador.calcularValorFinal(valor, regular));
        
        // Cliente VIP
        CalculadoraDesconto vip = new DescontoClienteVip();
        System.out.println("VIP: " + processador.calcularValorFinal(valor, vip));
        
        // Cliente Premium
        CalculadoraDesconto premium = new DescontoClientePremium();
        System.out.println("Premium: " + processador.calcularValorFinal(valor, premium));
    }
}
```

**Vantagens:**
- Para adicionar um novo tipo de cliente, só crio uma nova classe
- Não preciso modificar código existente
- Cada tipo de desconto fica isolado
- Fácil de testar e manter

---

## 3️⃣ Liskov Substitution Principle (LSP)

### 🤔 **O que significa?**
"Objetos de uma superclasse devem poder ser substituídos por objetos de suas subclasses sem quebrar a aplicação"

É como substituir uma pilha AA por outra pilha AA de marca diferente - deve funcionar perfeitamente.

### ❌ **Exemplo RUIM - Violando o LSP**

```java
public class Ave {
    public void voar() {
        System.out.println("A ave está voando");
    }
}

public class Pinguim extends Ave {
    @Override
    public void voar() {
        throw new UnsupportedOperationException("Pinguins não voam!");
    }
}

// Código que usa as aves
public class ExemploLSPRuim {
    public static void fazerAveVoar(Ave ave) {
        ave.voar(); // Vai quebrar se for um Pinguim!
    }
    
    public static void main(String[] args) {
        Ave pardal = new Ave();
        Ave pinguim = new Pinguim();
        
        fazerAveVoar(pardal); // Funciona
        fazerAveVoar(pinguim); // QUEBRA! Lança exceção
    }
}
```

### ✅ **Exemplo BOM - Seguindo o LSP**

```java
// Classe base mais geral
public abstract class Ave {
    protected String nome;
    
    public Ave(String nome) {
        this.nome = nome;
    }
    
    public void comer() {
        System.out.println(nome + " está comendo");
    }
    
    public void dormir() {
        System.out.println(nome + " está dormindo");
    }
}

// Interface específica para aves que voam
public interface AveVoadora {
    void voar();
}

// Interface específica para aves que nadam
public interface AveNadadora {
    void nadar();
}

// Pardal: ave que voa
public class Pardal extends Ave implements AveVoadora {
    public Pardal() {
        super("Pardal");
    }
    
    @Override
    public void voar() {
        System.out.println(nome + " está voando pelos céus");
    }
}

// Pinguim: ave que nada
public class Pinguim extends Ave implements AveNadadora {
    public Pinguim() {
        super("Pinguim");
    }
    
    @Override
    public void nadar() {
        System.out.println(nome + " está nadando no oceano");
    }
}

// Pato: ave que voa E nada
public class Pato extends Ave implements AveVoadora, AveNadadora {
    public Pato() {
        super("Pato");
    }
    
    @Override
    public void voar() {
        System.out.println(nome + " está voando sobre o lago");
    }
    
    @Override
    public void nadar() {
        System.out.println(nome + " está nadando no lago");
    }
}

// Exemplo de uso correto
public class ExemploLSPBom {
    public static void alimentarAve(Ave ave) {
        ave.comer(); // Funciona para QUALQUER ave
    }
    
    public static void fazerVoar(AveVoadora ave) {
        ave.voar(); // Só aceita aves que realmente voam
    }
    
    public static void fazerNadar(AveNadadora ave) {
        ave.nadar(); // Só aceita aves que realmente nadam
    }
    
    public static void main(String[] args) {
        Ave pardal = new Pardal();
        Ave pinguim = new Pinguim();
        Ave pato = new Pato();
        
        // Todas podem comer (comportamento comum)
        alimentarAve(pardal);
        alimentarAve(pinguim);
        alimentarAve(pato);
        
        // Só aves voadoras podem voar
        fazerVoar((AveVoadora) pardal);
        fazerVoar((AveVoadora) pato);
        // fazerVoar(pinguim); // Erro de compilação - pinguim não é AveVoadora
        
        // Só aves nadadoras podem nadar
        fazerNadar((AveNadadora) pinguim);
        fazerNadar((AveNadadora) pato);
        // fazerNadar(pardal); // Erro de compilação - pardal não é AveNadadora
    }
}
```

**Vantagens:**
- Qualquer Ave pode ser usada onde Ave é esperada
- Comportamentos específicos ficam em interfaces separadas
- Não há quebras ou exceções inesperadas
- Design mais flexível e correto

---

## 4️⃣ Interface Segregation Principle (ISP)

### 🤔 **O que significa?**
"Uma classe não deve ser forçada a implementar interfaces que ela não usa"

É como um controle remoto: você não quer todos os botões se só usa alguns. Melhor ter controles específicos.

### ❌ **Exemplo RUIM - Violando o ISP**

```java
// Interface muito "gorda" com muitas responsabilidades
public interface Funcionario {
    void trabalhar();
    void receberSalario();
    void gerenciarEquipe(); // Nem todo funcionário gerencia
    void fazerRelatorios(); // Nem todo funcionário faz relatórios
    void atenderClientes(); // Nem todo funcionário atende clientes
    void programar(); // Nem todo funcionário programa
}

// Desenvolvedor é forçado a implementar métodos que não usa
public class Desenvolvedor implements Funcionario {
    @Override
    public void trabalhar() {
        System.out.println("Desenvolvendo software");
    }
    
    @Override
    public void receberSalario() {
        System.out.println("Recebendo salário");
    }
    
    @Override
    public void gerenciarEquipe() {
        // Desenvolvedor junior não gerencia!
        throw new UnsupportedOperationException("Não gerencio equipe");
    }
    
    @Override
    public void fazerRelatorios() {
        // Desenvolvedor não faz relatórios!
        throw new UnsupportedOperationException("Não faço relatórios");
    }
    
    @Override
    public void atenderClientes() {
        // Desenvolvedor não atende clientes!
        throw new UnsupportedOperationException("Não atendo clientes");
    }
    
    @Override
    public void programar() {
        System.out.println("Programando em Java");
    }
}
```

### ✅ **Exemplo BOM - Seguindo o ISP**

```java
// Interfaces pequenas e específicas

// Interface base para todos os funcionários
public interface Trabalhador {
    void trabalhar();
    void receberSalario();
}

// Interface para quem gerencia
public interface Gerente {
    void gerenciarEquipe();
    void fazerRelatorios();
}

// Interface para quem atende clientes
public interface Atendente {
    void atenderClientes();
}

// Interface para quem programa
public interface Programador {
    void programar();
    void resolverBugs();
}

// Desenvolvedor: implementa apenas o que precisa
public class Desenvolvedor implements Trabalhador, Programador {
    private String nome;
    
    public Desenvolvedor(String nome) {
        this.nome = nome;
    }
    
    @Override
    public void trabalhar() {
        System.out.println(nome + " está trabalhando no desenvolvimento");
    }
    
    @Override
    public void receberSalario() {
        System.out.println(nome + " recebeu o salário");
    }
    
    @Override
    public void programar() {
        System.out.println(nome + " está programando");
    }
    
    @Override
    public void resolverBugs() {
        System.out.println(nome + " está corrigindo bugs");
    }
}

// Gerente de Projeto: implementa gerência mas não programa
public class GerenteProjeto implements Trabalhador, Gerente {
    private String nome;
    
    public GerenteProjeto(String nome) {
        this.nome = nome;
    }
    
    @Override
    public void trabalhar() {
        System.out.println(nome + " está gerenciando projetos");
    }
    
    @Override
    public void receberSalario() {
        System.out.println(nome + " recebeu o salário");
    }
    
    @Override
    public void gerenciarEquipe() {
        System.out.println(nome + " está gerenciando a equipe");
    }
    
    @Override
    public void fazerRelatorios() {
        System.out.println(nome + " está fazendo relatórios");
    }
}

// Atendente: atende clientes mas não programa nem gerencia
public class AtendimentoCliente implements Trabalhador, Atendente {
    private String nome;
    
    public AtendimentoCliente(String nome) {
        this.nome = nome;
    }
    
    @Override
    public void trabalhar() {
        System.out.println(nome + " está trabalhando no atendimento");
    }
    
    @Override
    public void receberSalario() {
        System.out.println(nome + " recebeu o salário");
    }
    
    @Override
    public void atenderClientes() {
        System.out.println(nome + " está atendendo clientes");
    }
}

// Tech Lead: programa E gerencia
public class TechLead implements Trabalhador, Programador, Gerente {
    private String nome;
    
    public TechLead(String nome) {
        this.nome = nome;
    }
    
    @Override
    public void trabalhar() {
        System.out.println(nome + " está liderando tecnicamente");
    }
    
    @Override
    public void receberSalario() {
        System.out.println(nome + " recebeu o salário");
    }
    
    @Override
    public void programar() {
        System.out.println(nome + " está programando (arquitetura)");
    }
    
    @Override
    public void resolverBugs() {
        System.out.println(nome + " está resolvendo bugs complexos");
    }
    
    @Override
    public void gerenciarEquipe() {
        System.out.println(nome + " está liderando a equipe técnica");
    }
    
    @Override
    public void fazerRelatorios() {
        System.out.println(nome + " está fazendo relatórios técnicos");
    }
}

// Exemplo de uso
public class ExemploISP {
    public static void main(String[] args) {
        Desenvolvedor dev = new Desenvolvedor("João");
        GerenteProjeto gerente = new GerenteProjeto("Maria");
        AtendimentoCliente atendente = new AtendimentoCliente("Carlos");
        TechLead techLead = new TechLead("Ana");
        
        // Todos podem trabalhar e receber salário
        dev.trabalhar();
        dev.receberSalario();
        
        // Só quem programa
        dev.programar();
        techLead.programar();
        
        // Só quem gerencia
        gerente.gerenciarEquipe();
        techLead.gerenciarEquipe();
        
        // Só quem atende
        atendente.atenderClientes();
    }
}
```

**Vantagens:**
- Cada classe implementa apenas o que realmente precisa
- Interfaces pequenas e focadas
- Fácil de testar e manter
- Não há métodos "vazios" ou que lançam exceções

---

## 5️⃣ Dependency Inversion Principle (DIP)

### 🤔 **O que significa?**
"Módulos de alto nível não devem depender de módulos de baixo nível. Ambos devem depender de abstrações"

É como um carregador USB: o celular não precisa saber que tipo específico de carregador é - só precisa que seja USB.

### ❌ **Exemplo RUIM - Violando o DIP**

```java
// Classe de baixo nível (implementação concreta)
public class EmailService {
    public void enviarEmail(String mensagem) {
        System.out.println("Enviando por email: " + mensagem);
    }
}

// Classe de alto nível dependendo diretamente da implementação
public class NotificacaoService {
    private EmailService emailService; // DEPENDÊNCIA DIRETA!
    
    public NotificacaoService() {
        this.emailService = new EmailService(); // ACOPLAMENTO FORTE!
    }
    
    public void notificar(String mensagem) {
        emailService.enviarEmail(mensagem);
    }
}
```

**Problemas:**
- Se quiser usar SMS em vez de email, preciso modificar NotificacaoService
- Difícil de testar (não posso mockar EmailService facilmente)
- Classes fortemente acopladas

### ✅ **Exemplo BOM - Seguindo o DIP**

```java
// Abstração (interface)
public interface ServicoNotificacao {
    void enviar(String mensagem);
}

// Implementações concretas (baixo nível)
public class EmailService implements ServicoNotificacao {
    @Override
    public void enviar(String mensagem) {
        System.out.println("📧 Enviando por EMAIL: " + mensagem);
    }
}

public class SMSService implements ServicoNotificacao {
    @Override
    public void enviar(String mensagem) {
        System.out.println("📱 Enviando por SMS: " + mensagem);
    }
}

public class PushNotificationService implements ServicoNotificacao {
    @Override
    public void enviar(String mensagem) {
        System.out.println("🔔 Enviando PUSH: " + mensagem);
    }
}

public class WhatsAppService implements ServicoNotificacao {
    @Override
    public void enviar(String mensagem) {
        System.out.println("💬 Enviando por WhatsApp: " + mensagem);
    }
}

// Classe de alto nível dependendo da abstração
public class NotificacaoService {
    private ServicoNotificacao servicoNotificacao; // DEPENDE DA ABSTRAÇÃO!
    
    // Injeção de dependência via construtor
    public NotificacaoService(ServicoNotificacao servicoNotificacao) {
        this.servicoNotificacao = servicoNotificacao;
    }
    
    public void notificarUsuario(String mensagem) {
        servicoNotificacao.enviar("🎉 " + mensagem);
    }
    
    // Permite trocar o serviço em runtime
    public void setServicoNotificacao(ServicoNotificacao novoServico) {
        this.servicoNotificacao = novoServico;
    }
}

// Factory para criar diferentes tipos de notificação
public class NotificationFactory {
    public static ServicoNotificacao criarServico(String tipo) {
        switch (tipo.toLowerCase()) {
            case "email":
                return new EmailService();
            case "sms":
                return new SMSService();
            case "push":
                return new PushNotificationService();
            case "whatsapp":
                return new WhatsAppService();
            default:
                return new EmailService(); // padrão
        }
    }
}

// Exemplo de uso
public class ExemploDIP {
    public static void main(String[] args) {
        // Posso escolher qualquer implementação
        ServicoNotificacao emailService = new EmailService();
        NotificacaoService notificador = new NotificacaoService(emailService);
        
        notificador.notificarUsuario("Bem-vindo ao sistema!");
        
        // Mudando para SMS sem modificar o NotificacaoService
        ServicoNotificacao smsService = new SMSService();
        notificador.setServicoNotificacao(smsService);
        notificador.notificarUsuario("Seu pedido foi enviado!");
        
        // Usando factory
        ServicoNotificacao pushService = NotificationFactory.criarServico("push");
        notificador.setServicoNotificacao(pushService);
        notificador.notificarUsuario("Nova mensagem disponível!");
        
        // Testando diferentes serviços
        String[] tipos = {"email", "sms", "push", "whatsapp"};
        for (String tipo : tipos) {
            ServicoNotificacao servico = NotificationFactory.criarServico(tipo);
            NotificacaoService notif = new NotificacaoService(servico);
            notif.notificarUsuario("Teste do serviço " + tipo);
        }
    }
}
```

**Vantagens:**
- NotificacaoService não depende de implementações específicas
- Fácil adicionar novos tipos de notificação
- Fácil de testar (posso criar mocks)
- Baixo acoplamento, alta flexibilidade

---

## 🏗️ Exemplo Prático Completo: Sistema de E-commerce

Vamos ver todos os princípios SOLID trabalhando juntos em um sistema real:

```java
// ======================== DOMAIN MODELS ========================

public class Produto {
    private String id;
    private String nome;
    private double preco;
    private int estoque;
    
    public Produto(String id, String nome, double preco, int estoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }
    
    // Getters
    public String getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public int getEstoque() { return estoque; }
    
    public void reduzirEstoque(int quantidade) {
        if (estoque >= quantidade) {
            estoque -= quantidade;
        } else {
            throw new IllegalArgumentException("Estoque insuficiente");
        }
    }
}

public class Pedido {
    private String id;
    private List<ItemPedido> itens;
    private StatusPedido status;
    private double valorTotal;
    
    public Pedido(String id) {
        this.id = id;
        this.itens = new ArrayList<>();
        this.status = StatusPedido.CRIADO;
        this.valorTotal = 0.0;
    }
    
    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        valorTotal += item.getSubtotal();
    }
    
    // Getters
    public String getId() { return id; }
    public List<ItemPedido> getItens() { return itens; }
    public StatusPedido getStatus() { return status; }
    public double getValorTotal() { return valorTotal; }
    
    public void setStatus(StatusPedido status) { this.status = status; }
}

public class ItemPedido {
    private Produto produto;
    private int quantidade;
    
    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }
    
    public double getSubtotal() {
        return produto.getPreco() * quantidade;
    }
    
    // Getters
    public Produto getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
}

public enum StatusPedido {
    CRIADO, CONFIRMADO, PROCESSANDO, ENVIADO, ENTREGUE, CANCELADO
}

// ======================== ABSTRAÇÕES (ISP + DIP) ========================

public interface RepositorioProduto {
    Produto buscarPorId(String id);
    void atualizar(Produto produto);
}

public interface RepositorioPedido {
    void salvar(Pedido pedido);
    Pedido buscarPorId(String id);
}

public interface ProcessadorPagamento {
    boolean processar(double valor, String dadosPagamento);
}

public interface ServicoNotificacao {
    void notificar(String mensagem);
}

public interface CalculadoraDesconto {
    double calcular(double valor);
}

// ======================== IMPLEMENTAÇÕES (SRP + OCP) ========================

// SRP: Só cuida de validações
public class ValidadorPedido {
    public void validar(Pedido pedido) {
        if (pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException("Pedido deve ter pelo menos um item");
        }
        
        for (ItemPedido item : pedido.getItens()) {
            if (item.getProduto().getEstoque() < item.getQuantidade()) {
                throw new IllegalArgumentException(
                    "Estoque insuficiente para " + item.getProduto().getNome()
                );
            }
        }
    }
}

// SRP: Só cuida da persistência de produtos
public class RepositorioProdutoMemoria implements RepositorioProduto {
    private Map<String, Produto> produtos = new HashMap<>();
    
    @Override
    public Produto buscarPorId(String id) {
        return produtos.get(id);
    }
    
    @Override
    public void atualizar(Produto produto) {
        produtos.put(produto.getId(), produto);
    }
    
    // Método para adicionar produtos (para exemplo)
    public void adicionar(Produto produto) {
        produtos.put(produto.getId(), produto);
    }
}

// SRP: Só cuida da persistência de pedidos
public class RepositorioPedidoMemoria implements RepositorioPedido {
    private Map<String, Pedido> pedidos = new HashMap<>();
    
    @Override
    public void salvar(Pedido pedido) {
        pedidos.put(pedido.getId(), pedido);
    }
    
    @Override
    public Pedido buscarPorId(String id) {
        return pedidos.get(id);
    }
}

// OCP: Diferentes implementações de desconto
public class DescontoClienteRegular implements CalculadoraDesconto {
    @Override
    public double calcular(double valor) {
        return valor * 0.05; // 5%
    }
}

public class DescontoClienteVip implements CalculadoraDesconto {
    @Override
    public double calcular(double valor) {
        return valor * 0.15; // 15%
    }
}

public class DescontoBlackFriday implements CalculadoraDesconto {
    @Override
    public double calcular(double valor) {
        return valor * 0.30; // 30%
    }
}

// OCP: Diferentes formas de pagamento
public class PagamentoCartaoCredito implements ProcessadorPagamento {
    @Override
    public boolean processar(double valor, String dadosPagamento) {
        System.out.println("💳 Processando pagamento de R$" + valor + " no cartão: " + dadosPagamento);
        // Simulação de processamento
        return Math.random() > 0.1; // 90% de sucesso
    }
}

public class PagamentoPix implements ProcessadorPagamento {
    @Override
    public boolean processar(double valor, String dadosPagamento) {
        System.out.println("🏦 Processando PIX de R$" + valor + " - Chave: " + dadosPagamento);
        return Math.random() > 0.05; // 95% de sucesso
    }
}

public class PagamentoBoleto implements ProcessadorPagamento {
    @Override
    public boolean processar(double valor, String dadosPagamento) {
        System.out.println("🧾 Gerando boleto de R$" + valor + " para: " + dadosPagamento);
        return true; // Boleto sempre é gerado com sucesso
    }
}

// SRP: Diferentes serviços de notificação
public class NotificacaoEmail implements ServicoNotificacao {
    @Override
    public void notificar(String mensagem) {
        System.out.println("📧 Email: " + mensagem);
    }
}

public class NotificacaoSMS implements ServicoNotificacao {
    @Override
    public void notificar(String mensagem) {
        System.out.println("📱 SMS: " + mensagem);
    }
}

public class NotificacaoWhatsApp implements ServicoNotificacao {
    @Override
    public void notificar(String mensagem) {
        System.out.println("💬 WhatsApp: " + mensagem);
    }
}

// ======================== SERVIÇOS DE APLICAÇÃO (DIP) ========================

// SRP: Só cuida da lógica de processar pedidos
public class ProcessadorPedido {
    private final RepositorioProduto repositorioProduto;
    private final RepositorioPedido repositorioPedido;
    private final ValidadorPedido validador;
    private final ProcessadorPagamento processadorPagamento;
    private final ServicoNotificacao servicoNotificacao;
    private final CalculadoraDesconto calculadoraDesconto;
    
    // DIP: Dependendo de abstrações, não implementações
    public ProcessadorPedido(
            RepositorioProduto repositorioProduto,
            RepositorioPedido repositorioPedido,
            ValidadorPedido validador,
            ProcessadorPagamento processadorPagamento,
            ServicoNotificacao servicoNotificacao,
            CalculadoraDesconto calculadoraDesconto) {
        
        this.repositorioProduto = repositorioProduto;
        this.repositorioPedido = repositorioPedido;
        this.validador = validador;
        this.processadorPagamento = processadorPagamento;
        this.servicoNotificacao = servicoNotificacao;
        this.calculadoraDesconto = calculadoraDesconto;
    }
    
    public boolean processar(Pedido pedido, String dadosPagamento) {
        try {
            System.out.println("🔍 Iniciando processamento do pedido: " + pedido.getId());
            
            // 1. Validar pedido
            validador.validar(pedido);
            System.out.println("✅ Pedido validado");
            
            // 2. Calcular desconto
            double valorOriginal = pedido.getValorTotal();
            double desconto = calculadoraDesconto.calcular(valorOriginal);
            double valorFinal = valorOriginal - desconto;
            
            System.out.println("💰 Valor original: R$" + valorOriginal);
            System.out.println("🎯 Desconto aplicado: R$" + desconto);
            System.out.println("💵 Valor final: R$" + valorFinal);
            
            // 3. Processar pagamento
            boolean pagamentoSucesso = processadorPagamento.processar(valorFinal, dadosPagamento);
            
            if (!pagamentoSucesso) {
                pedido.setStatus(StatusPedido.CANCELADO);
                servicoNotificacao.notificar("❌ Pagamento rejeitado para pedido " + pedido.getId());
                return false;
            }
            
            // 4. Atualizar estoque
            for (ItemPedido item : pedido.getItens()) {
                Produto produto = item.getProduto();
                produto.reduzirEstoque(item.getQuantidade());
                repositorioProduto.atualizar(produto);
            }
            
            // 5. Confirmar pedido
            pedido.setStatus(StatusPedido.CONFIRMADO);
            repositorioPedido.salvar(pedido);
            
            // 6. Notificar cliente
            servicoNotificacao.notificar(
                "🎉 Pedido " + pedido.getId() + " confirmado! Valor: R$" + valorFinal
            );
            
            System.out.println("✅ Pedido processado com sucesso!");
            return true;
            
        } catch (Exception e) {
            pedido.setStatus(StatusPedido.CANCELADO);
            servicoNotificacao.notificar("❌ Erro no pedido " + pedido.getId() + ": " + e.getMessage());
            System.out.println("❌ Erro: " + e.getMessage());
            return false;
        }
    }
}

// ======================== FACTORIES (OCP + DIP) ========================

public class PagamentoFactory {
    public static ProcessadorPagamento criar(String tipo) {
        switch (tipo.toLowerCase()) {
            case "credito":
            case "cartao":
                return new PagamentoCartaoCredito();
            case "pix":
                return new PagamentoPix();
            case "boleto":
                return new PagamentoBoleto();
            default:
                throw new IllegalArgumentException("Tipo de pagamento inválido: " + tipo);
        }
    }
}

public class NotificacaoFactory {
    public static ServicoNotificacao criar(String tipo) {
        switch (tipo.toLowerCase()) {
            case "email":
                return new NotificacaoEmail();
            case "sms":
                return new NotificacaoSMS();
            case "whatsapp":
                return new NotificacaoWhatsApp();
            default:
                return new NotificacaoEmail(); // padrão
        }
    }
}

public class DescontoFactory {
    public static CalculadoraDesconto criar(String tipoCliente) {
        switch (tipoCliente.toLowerCase()) {
            case "vip":
                return new DescontoClienteVip();
            case "blackfriday":
                return new DescontoBlackFriday();
            case "regular":
            default:
                return new DescontoClienteRegular();
        }
    }
}

// ======================== EXEMPLO DE USO COMPLETO ========================

public class ECommerceApp {
    public static void main(String[] args) {
        System.out.println("🛒 === SISTEMA DE E-COMMERCE - SOLID PRINCIPLES ===\n");
        
        // 1. Configurar repositórios (SRP)
        RepositorioProdutoMemoria repoProdutos = new RepositorioProdutoMemoria();
        RepositorioPedidoMemoria repoPedidos = new RepositorioPedidoMemoria();
        
        // Adicionar alguns produtos
        repoProdutos.adicionar(new Produto("1", "Notebook Dell", 2500.0, 10));
        repoProdutos.adicionar(new Produto("2", "Mouse Wireless", 80.0, 50));
        repoProdutos.adicionar(new Produto("3", "Teclado Mecânico", 200.0, 25));
        
        // 2. Criar pedido
        Pedido pedido1 = new Pedido("PED-001");
        pedido1.adicionarItem(new ItemPedido(repoProdutos.buscarPorId("1"), 1)); // Notebook
        pedido1.adicionarItem(new ItemPedido(repoProdutos.buscarPorId("2"), 2)); // 2 Mouses
        
        System.out.println("📦 Pedido criado: " + pedido1.getId());
        System.out.println("💰 Valor total: R$" + pedido1.getValorTotal());
        System.out.println();
        
        // 3. Processar pedido para CLIENTE REGULAR com CARTÃO
        System.out.println("--- PROCESSAMENTO 1: Cliente Regular + Cartão ---");
        ProcessadorPedido processador1 = new ProcessadorPedido(
            repoProdutos,
            repoPedidos,
            new ValidadorPedido(),
            PagamentoFactory.criar("cartao"),
            NotificacaoFactory.criar("email"),
            DescontoFactory.criar("regular")
        );
        
        processador1.processar(pedido1, "**** 1234");
        System.out.println();
        
        // 4. Criar outro pedido para cliente VIP
        Pedido pedido2 = new Pedido("PED-002");
        pedido2.adicionarItem(new ItemPedido(repoProdutos.buscarPorId("3"), 3)); // 3 Teclados
        
        System.out.println("--- PROCESSAMENTO 2: Cliente VIP + PIX ---");
        ProcessadorPedido processador2 = new ProcessadorPedido(
            repoProdutos,
            repoPedidos,
            new ValidadorPedido(),
            PagamentoFactory.criar("pix"),
            NotificacaoFactory.criar("whatsapp"),
            DescontoFactory.criar("vip")
        );
        
        processador2.processar(pedido2, "usuario@banco.com");
        System.out.println();
        
        // 5. Pedido Black Friday
        Pedido pedido3 = new Pedido("PED-003");
        pedido3.adicionarItem(new ItemPedido(repoProdutos.buscarPorId("1"), 2)); // 2 Notebooks
        
        System.out.println("--- PROCESSAMENTO 3: Black Friday + Boleto ---");
        ProcessadorPedido processador3 = new ProcessadorPedido(
            repoProdutos,
            repoPedidos,
            new ValidadorPedido(),
            PagamentoFactory.criar("boleto"),
            NotificacaoFactory.criar("sms"),
            DescontoFactory.criar("blackfriday")
        );
        
        processador3.processar(pedido3, "12345678901");
        System.out.println();
        
        // 6. Mostrar estoque atualizado
        System.out.println("📊 === ESTOQUE FINAL ===");
        System.out.println("Notebook Dell: " + repoProdutos.buscarPorId("1").getEstoque() + " unidades");
        System.out.println("Mouse Wireless: " + repoProdutos.buscarPorId("2").getEstoque() + " unidades");
        System.out.println("Teclado Mecânico: " + repoProdutos.buscarPorId("3").getEstoque() + " unidades");
        
        // 7. Demonstrar flexibilidade (OCP + DIP)
        System.out.println("\n🔄 === DEMONSTRANDO FLEXIBILIDADE ===");
        
        // Posso facilmente trocar implementações sem modificar o código
        Pedido pedidoTeste = new Pedido("PED-TEST");
        pedidoTeste.adicionarItem(new ItemPedido(repoProdutos.buscarPorId("2"), 1));
        
        ProcessadorPedido processadorFlexivel = new ProcessadorPedido(
            repoProdutos,
            repoPedidos,
            new ValidadorPedido(),
            new PagamentoCartaoCredito(), // Direto sem factory
            new NotificacaoSMS(),         // Direto sem factory
            new DescontoClienteVip()      // Direto sem factory
        );
        
        processadorFlexivel.processar(pedidoTeste, "**** 9876");
    }
}

// ======================== EXEMPLO DE TESTES (Benefício do DIP) ========================

// Mock para testes
class MockProcessadorPagamento implements ProcessadorPagamento {
    private boolean deveAprovar;
    
    public MockProcessadorPagamento(boolean deveAprovar) {
        this.deveAprovar = deveAprovar;
    }
    
    @Override
    public boolean processar(double valor, String dadosPagamento) {
        System.out.println("🧪 MOCK: Simulando pagamento de R$" + valor);
        return deveAprovar;
    }
}

class MockNotificacao implements ServicoNotificacao {
    private List<String> mensagensEnviadas = new ArrayList<>();
    
    @Override
    public void notificar(String mensagem) {
        mensagensEnviadas.add(mensagem);
        System.out.println("🧪 MOCK: " + mensagem);
    }
    
    public List<String> getMensagensEnviadas() {
        return mensagensEnviadas;
    }
}

public class TesteProcessadorPedido {
    public static void exemploTeste() {
        System.out.println("\n🧪 === EXEMPLO DE TESTE ===");
        
        // Configurar mocks
        RepositorioProdutoMemoria repoProdutos = new RepositorioProdutoMemoria();
        repoProdutos.adicionar(new Produto("TEST", "Produto Teste", 100.0, 5));
        
        RepositorioPedidoMemoria repoPedidos = new RepositorioPedidoMemoria();
        MockProcessadorPagamento mockPagamento = new MockProcessadorPagamento(false); // Simular falha
        MockNotificacao mockNotificacao = new MockNotificacao();
        
        // Criar processador com mocks
        ProcessadorPedido processador = new ProcessadorPedido(
            repoProdutos,
            repoPedidos,
            new ValidadorPedido(),
            mockPagamento,
            mockNotificacao,
            new DescontoClienteRegular()
        );
        
        // Criar pedido de teste
        Pedido pedido = new Pedido("TEST-001");
        pedido.adicionarItem(new ItemPedido(repoProdutos.buscarPorId("TEST"), 1));
        
        // Executar teste
        boolean resultado = processador.processar(pedido, "dados-teste");
        
        // Verificar resultados
        System.out.println("Resultado do processamento: " + (resultado ? "✅ Sucesso" : "❌ Falhou"));
        System.out.println("Status do pedido: " + pedido.getStatus());
        System.out.println("Mensagens enviadas: " + mockNotificacao.getMensagensEnviadas().size());
    }
}