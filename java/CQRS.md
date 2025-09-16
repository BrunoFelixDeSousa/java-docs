# CQRS (Command Query Responsibility Segregation)
## Documentação Completa com Exemplos Java/Quarkus

---

## 1. O que é CQRS? (Conceito Fundamental)

Imagine que você tem uma biblioteca. Tradicionalmente, você usaria o mesmo sistema para:
- **Emprestar livros** (modificar dados)
- **Consultar disponibilidade** (ler dados)

CQRS sugere **separar essas responsabilidades**:
- Um sistema otimizado para **comandos** (empréstimos, devoluções)
- Outro sistema otimizado para **consultas** (buscas, relatórios)

### Definição Técnica
CQRS é um padrão arquitetural que separa operações de **leitura** (queries) das operações de **escrita** (commands), permitindo que cada lado seja otimizado independentemente.

---

## 2. Por que usar CQRS?

### Problemas que CQRS resolve:

**🔴 Problema: Modelo único complexo**
```java
// Sem CQRS - Uma única classe para tudo
@Entity
public class Pedido {
    // 50+ campos para diferentes necessidades
    private String numero;
    private Cliente cliente;
    private List<Item> items;
    private Endereco entrega;
    private StatusPedido status;
    // ... muitos outros campos
    
    // Métodos para commands E queries misturados
    public void processar() { /* lógica complexa */ }
    public List<RelatorioDTO> gerarRelatorio() { /* lógica complexa */ }
}
```

**🟢 Solução: Separação de responsabilidades**
```java
// Command Model - Focado em regras de negócio
public class PedidoAggregate {
    private String id;
    private BigDecimal valor;
    private StatusPedido status;
    
    public void processar() {
        // Apenas lógica de negócio
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor inválido");
        }
        this.status = StatusPedido.PROCESSANDO;
    }
}

// Query Model - Focado em performance de leitura
public class PedidoView {
    private String numero;
    private String nomeCliente;
    private String status;
    private BigDecimal valorTotal;
    // Apenas campos necessários para consulta
}
```

### Benefícios:
1. **Performance independente**: Cada lado otimizado para seu propósito
2. **Escalabilidade**: Escale leitura e escrita independentemente
3. **Simplicidade**: Modelos mais simples e focados
4. **Flexibilidade**: Diferentes tecnologias para cada lado

---

## 3. Arquitetura CQRS

```
┌─────────────────┐    Commands    ┌──────────────────┐
│   Client/UI     │───────────────▶│  Command Side    │
│                 │                │                  │
└─────────────────┘                │  ┌─────────────┐ │
         │                         │  │ Command     │ │
         │                         │  │ Handlers    │ │
         │                         │  └─────────────┘ │
         │                         │         │        │
         │ Queries                 │         ▼        │
         │                         │  ┌─────────────┐ │
         └────────────────────────────▶│ Write DB    │ │
                                   │  └─────────────┘ │
                                   └──────────────────┘
                                            │
                                            │ Events
                                            ▼
┌─────────────────┐                ┌──────────────────┐
│   Query Side    │                │   Event Store    │
│                 │◀───────────────│                  │
│ ┌─────────────┐ │                └──────────────────┘
│ │ Query       │ │
│ │ Handlers    │ │
│ └─────────────┘ │
│        │        │
│        ▼        │
│ ┌─────────────┐ │
│ │  Read DB    │ │
│ └─────────────┘ │
└─────────────────┘
```

---

## 4. Implementação Prática com Java/Quarkus

### 4.1. Configuração do Projeto

```xml
<!-- pom.xml -->
<dependencies>
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-hibernate-orm-panache</artifactId>
    </dependency>
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-jdbc-postgresql</artifactId>
    </dependency>
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-resteasy-reactive-jackson</artifactId>
    </dependency>
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-smallrye-reactive-messaging-kafka</artifactId>
    </dependency>
</dependencies>
```

### 4.2. Command Side - Lado de Escrita

#### Commands (Comandos)
```java
// Comando base
public abstract class Command {
    private final String aggregateId;
    private final LocalDateTime timestamp;
    
    protected Command(String aggregateId) {
        this.aggregateId = aggregateId;
        this.timestamp = LocalDateTime.now();
    }
    
    // getters
}

// Comando específico
public class CriarPedidoCommand extends Command {
    private final String clienteId;
    private final List<ItemPedido> itens;
    private final String enderecoEntrega;
    
    public CriarPedidoCommand(String pedidoId, String clienteId, 
                             List<ItemPedido> itens, String enderecoEntrega) {
        super(pedidoId);
        this.clienteId = clienteId;
        this.itens = itens;
        this.enderecoEntrega = enderecoEntrega;
    }
    
    // getters
}

public class ProcessarPedidoCommand extends Command {
    public ProcessarPedidoCommand(String pedidoId) {
        super(pedidoId);
    }
}
```

#### Aggregate (Modelo de Domínio)
```java
@Entity
@Table(name = "pedidos")
public class PedidoAggregate extends PanacheEntityBase {
    @Id
    public String id;
    
    public String clienteId;
    
    @Enumerated(EnumType.STRING)
    public StatusPedido status;
    
    public BigDecimal valorTotal;
    
    @Column(name = "criado_em")
    public LocalDateTime criadoEm;
    
    @Column(name = "atualizado_em")
    public LocalDateTime atualizadoEm;
    
    // Construtor para criação
    public static PedidoAggregate criar(String id, String clienteId, 
                                       List<ItemPedido> itens) {
        PedidoAggregate pedido = new PedidoAggregate();
        pedido.id = id;
        pedido.clienteId = clienteId;
        pedido.status = StatusPedido.CRIADO;
        pedido.valorTotal = calcularValorTotal(itens);
        pedido.criadoEm = LocalDateTime.now();
        pedido.atualizadoEm = LocalDateTime.now();
        
        return pedido;
    }
    
    // Lógica de negócio
    public void processar() {
        if (this.status != StatusPedido.CRIADO) {
            throw new IllegalStateException("Pedido não pode ser processado no status atual");
        }
        
        if (this.valorTotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do pedido deve ser positivo");
        }
        
        this.status = StatusPedido.PROCESSANDO;
        this.atualizadoEm = LocalDateTime.now();
    }
    
    public void confirmar() {
        if (this.status != StatusPedido.PROCESSANDO) {
            throw new IllegalStateException("Apenas pedidos em processamento podem ser confirmados");
        }
        
        this.status = StatusPedido.CONFIRMADO;
        this.atualizadoEm = LocalDateTime.now();
    }
    
    private static BigDecimal calcularValorTotal(List<ItemPedido> itens) {
        return itens.stream()
            .map(item -> item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

enum StatusPedido {
    CRIADO, PROCESSANDO, CONFIRMADO, CANCELADO
}
```

#### Command Handlers
```java
@ApplicationScoped
public class PedidoCommandHandler {
    
    @Inject
    EventPublisher eventPublisher;
    
    @Transactional
    public void handle(CriarPedidoCommand command) {
        // Validações
        if (command.getItens().isEmpty()) {
            throw new IllegalArgumentException("Pedido deve conter pelo menos um item");
        }
        
        // Criar aggregate
        PedidoAggregate pedido = PedidoAggregate.criar(
            command.getAggregateId(),
            command.getClienteId(),
            command.getItens()
        );
        
        // Persistir
        pedido.persist();
        
        // Publicar evento
        PedidoCriadoEvent evento = new PedidoCriadoEvent(
            pedido.id,
            pedido.clienteId,
            pedido.valorTotal,
            pedido.criadoEm
        );
        
        eventPublisher.publish(evento);
    }
    
    @Transactional
    public void handle(ProcessarPedidoCommand command) {
        PedidoAggregate pedido = PedidoAggregate.findById(command.getAggregateId());
        
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        
        pedido.processar();
        
        // Publicar evento
        PedidoProcessandoEvent evento = new PedidoProcessandoEvent(
            pedido.id,
            pedido.atualizadoEm
        );
        
        eventPublisher.publish(evento);
    }
}
```

#### Event Publisher
```java
@ApplicationScoped
public class EventPublisher {
    
    @Channel("pedido-events")
    Emitter<String> eventEmitter;
    
    @Inject
    ObjectMapper objectMapper;
    
    public void publish(DomainEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            eventEmitter.send(eventJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao publicar evento", e);
        }
    }
}
```

### 4.3. Query Side - Lado de Leitura

#### Query Models (Views)
```java
@Entity
@Table(name = "pedido_view")
public class PedidoView extends PanacheEntityBase {
    @Id
    public String id;
    
    public String numeroPedido;
    public String nomeCliente;
    public String emailCliente;
    public String status;
    public BigDecimal valorTotal;
    public LocalDateTime criadoEm;
    public Integer totalItens;
    
    // Queries específicas
    public static List<PedidoView> findByCliente(String clienteId) {
        return list("nomeCliente = ?1", clienteId);
    }
    
    public static List<PedidoView> findByStatus(String status) {
        return list("status = ?1", status);
    }
    
    public static List<PedidoView> findPedidosRecentes() {
        return list("ORDER BY criadoEm DESC LIMIT 10");
    }
}

@Entity
@Table(name = "relatorio_vendas_view")
public class RelatorioVendasView extends PanacheEntityBase {
    @Id
    public String id;
    
    public LocalDate data;
    public BigDecimal vendaTotal;
    public Integer totalPedidos;
    public BigDecimal ticketMedio;
    
    public static List<RelatorioVendasView> findByPeriodo(LocalDate inicio, LocalDate fim) {
        return list("data BETWEEN ?1 AND ?2 ORDER BY data", inicio, fim);
    }
}
```

#### Query Handlers
```java
@ApplicationScoped
public class PedidoQueryHandler {
    
    public List<PedidoView> buscarPedidosCliente(String clienteId) {
        return PedidoView.findByCliente(clienteId);
    }
    
    public PedidoView buscarPedido(String pedidoId) {
        return PedidoView.findById(pedidoId);
    }
    
    public List<PedidoView> buscarPedidosPorStatus(String status) {
        return PedidoView.findByStatus(status);
    }
    
    public List<RelatorioVendasView> gerarRelatorioVendas(LocalDate inicio, LocalDate fim) {
        return RelatorioVendasView.findByPeriodo(inicio, fim);
    }
}
```

#### Event Handlers (Atualização das Views)
```java
@ApplicationScoped
public class PedidoEventHandler {
    
    @Incoming("pedido-events")
    @Transactional
    public void handlePedidoEvent(String eventJson) {
        try {
            JsonNode eventNode = objectMapper.readTree(eventJson);
            String eventType = eventNode.get("eventType").asText();
            
            switch (eventType) {
                case "PedidoCriado":
                    handlePedidoCriado(eventNode);
                    break;
                case "PedidoProcessando":
                    handlePedidoProcessando(eventNode);
                    break;
                // outros eventos...
            }
        } catch (Exception e) {
            // Log e tratamento de erro
        }
    }
    
    private void handlePedidoCriado(JsonNode event) {
        String pedidoId = event.get("pedidoId").asText();
        String clienteId = event.get("clienteId").asText();
        
        // Buscar dados complementares se necessário
        ClienteView cliente = ClienteView.findById(clienteId);
        
        // Criar/Atualizar view
        PedidoView view = new PedidoView();
        view.id = pedidoId;
        view.numeroPedido = gerarNumeroPedido(pedidoId);
        view.nomeCliente = cliente.nome;
        view.emailCliente = cliente.email;
        view.status = "CRIADO";
        view.valorTotal = new BigDecimal(event.get("valorTotal").asText());
        view.criadoEm = LocalDateTime.parse(event.get("criadoEm").asText());
        
        view.persist();
        
        // Atualizar relatórios
        atualizarRelatorioVendas(view);
    }
    
    private void handlePedidoProcessando(JsonNode event) {
        String pedidoId = event.get("pedidoId").asText();
        
        PedidoView view = PedidoView.findById(pedidoId);
        if (view != null) {
            view.status = "PROCESSANDO";
            view.persist();
        }
    }
    
    private void atualizarRelatorioVendas(PedidoView pedido) {
        LocalDate hoje = pedido.criadoEm.toLocalDate();
        
        RelatorioVendasView relatorio = RelatorioVendasView.find("data = ?1", hoje)
            .firstResult();
        
        if (relatorio == null) {
            relatorio = new RelatorioVendasView();
            relatorio.id = UUID.randomUUID().toString();
            relatorio.data = hoje;
            relatorio.vendaTotal = BigDecimal.ZERO;
            relatorio.totalPedidos = 0;
        }
        
        relatorio.vendaTotal = relatorio.vendaTotal.add(pedido.valorTotal);
        relatorio.totalPedidos++;
        relatorio.ticketMedio = relatorio.vendaTotal
            .divide(BigDecimal.valueOf(relatorio.totalPedidos), 2, RoundingMode.HALF_UP);
        
        relatorio.persist();
    }
}
```

### 4.4. API Controllers

#### Command Controller
```java
@Path("/api/pedidos/commands")
@ApplicationScoped
public class PedidoCommandController {
    
    @Inject
    PedidoCommandHandler commandHandler;
    
    @POST
    @Path("/criar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarPedido(CriarPedidoRequest request) {
        try {
            CriarPedidoCommand command = new CriarPedidoCommand(
                UUID.randomUUID().toString(),
                request.clienteId,
                request.itens,
                request.enderecoEntrega
            );
            
            commandHandler.handle(command);
            
            return Response.accepted()
                .entity(Map.of("pedidoId", command.getAggregateId()))
                .build();
                
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
    
    @POST
    @Path("/{pedidoId}/processar")
    public Response processarPedido(@PathParam("pedidoId") String pedidoId) {
        try {
            ProcessarPedidoCommand command = new ProcessarPedidoCommand(pedidoId);
            commandHandler.handle(command);
            
            return Response.accepted().build();
            
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
}
```

#### Query Controller
```java
@Path("/api/pedidos/queries")
@ApplicationScoped
public class PedidoQueryController {
    
    @Inject
    PedidoQueryHandler queryHandler;
    
    @GET
    @Path("/{pedidoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPedido(@PathParam("pedidoId") String pedidoId) {
        PedidoView pedido = queryHandler.buscarPedido(pedidoId);
        
        if (pedido == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        return Response.ok(pedido).build();
    }
    
    @GET
    @Path("/cliente/{clienteId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPedidosCliente(@PathParam("clienteId") String clienteId) {
        List<PedidoView> pedidos = queryHandler.buscarPedidosCliente(clienteId);
        return Response.ok(pedidos).build();
    }
    
    @GET
    @Path("/relatorio/vendas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response relatorioVendas(
            @QueryParam("inicio") @DateFormat("yyyy-MM-dd") LocalDate inicio,
            @QueryParam("fim") @DateFormat("yyyy-MM-dd") LocalDate fim) {
        
        List<RelatorioVendasView> relatorio = queryHandler.gerarRelatorioVendas(inicio, fim);
        return Response.ok(relatorio).build();
    }
}
```

---

## 5. Configuração e Deploy

### 5.1. Configuração do Kafka
```properties
# application.properties

# Database
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=postgres
quarkus.datasource.password=postgres
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/cqrs_db

# Hibernate
quarkus.hibernate-orm.database.generation=drop-and-create

# Kafka
kafka.bootstrap.servers=localhost:9092
mp.messaging.outgoing.pedido-events.connector=smallrye-kafka
mp.messaging.outgoing.pedido-events.topic=pedido-events
mp.messaging.incoming.pedido-events.connector=smallrye-kafka
mp.messaging.incoming.pedido-events.topic=pedido-events
mp.messaging.incoming.pedido-events.group.id=pedido-query-handler
```

### 5.2. Docker Compose
```yaml
version: '3.8'
services:
  postgres:
    image: postgres:13
    environment:
      POSTGRES_DB: cqrs_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
  
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
    
  kafka:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    environment:
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    ports:
      - "9092:9092"
```

---

## 6. Testes e Validação

### 6.1. Teste de Command
```java
@QuarkusTest
public class PedidoCommandTest {
    
    @Inject
    PedidoCommandHandler commandHandler;
    
    @Test
    @Transactional
    public void deveCriarPedidoComSucesso() {
        // Arrange
        List<ItemPedido> itens = List.of(
            new ItemPedido("produto-1", 2, new BigDecimal("50.00"))
        );
        
        CriarPedidoCommand command = new CriarPedidoCommand(
            "pedido-1",
            "cliente-1", 
            itens,
            "Rua A, 123"
        );
        
        // Act
        commandHandler.handle(command);
        
        // Assert
        PedidoAggregate pedido = PedidoAggregate.findById("pedido-1");
        assertThat(pedido).isNotNull();
        assertThat(pedido.status).isEqualTo(StatusPedido.CRIADO);
        assertThat(pedido.valorTotal).isEqualByComparingTo(new BigDecimal("100.00"));
    }
    
    @Test
    public void deveRejeitarPedidoSemItens() {
        // Arrange
        CriarPedidoCommand command = new CriarPedidoCommand(
            "pedido-2",
            "cliente-1",
            Collections.emptyList(),
            "Rua A, 123"
        );
        
        // Act & Assert
        assertThatThrownBy(() -> commandHandler.handle(command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Pedido deve conter pelo menos um item");
    }
}
```

### 6.2. Teste de Query
```java
@QuarkusTest
public class PedidoQueryTest {
    
    @Inject
    PedidoQueryHandler queryHandler;
    
    @BeforeEach
    @Transactional
    void setup() {
        // Criar dados de teste
        PedidoView pedido = new PedidoView();
        pedido.id = "pedido-teste";
        pedido.nomeCliente = "João Silva";
        pedido.status = "CRIADO";
        pedido.valorTotal = new BigDecimal("150.00");
        pedido.criadoEm = LocalDateTime.now();
        pedido.persist();
    }
    
    @Test
    public void deveBuscarPedidoPorId() {
        // Act
        PedidoView pedido = queryHandler.buscarPedido("pedido-teste");
        
        // Assert
        assertThat(pedido).isNotNull();
        assertThat(pedido.nomeCliente).isEqualTo("João Silva");
        assertThat(pedido.valorTotal).isEqualByComparingTo(new BigDecimal("150.00"));
    }
}
```

---

## 7. Monitoramento e Observabilidade

### 7.1. Métricas
```java
@ApplicationScoped
public class PedidoMetrics {
    
    @Inject
    @Metric(name = "pedidos_criados_total")
    Counter pedidosCriados;
    
    @Inject
    @Metric(name = "pedidos_processamento_duracao")
    Timer tempoProcessamento;
    
    public void incrementarPedidosCriados() {
        pedidosCriados.increment();
    }
    
    public Timer.Context iniciarMedicaoProcessamento() {
        return tempoProcessamento.time();
    }
}
```

### 7.2. Health Checks
```java
@ApplicationScoped
@Readiness
public class CQRSHealthCheck implements HealthCheck {
    
    @Override
    public HealthCheckResponse call() {
        boolean databaseUp = checkDatabase();
        boolean kafkaUp = checkKafka();
        
        if (databaseUp && kafkaUp) {
            return HealthCheckResponse.up("CQRS System");
        } else {
            return HealthCheckResponse.down("CQRS System")
                .withData("database", databaseUp)
                .withData("kafka", kafkaUp);
        }
    }
    
    private boolean checkDatabase() {
        try {
            PedidoAggregate.count();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean checkKafka() {
        // Implementar verificação do Kafka
        return true;
    }
}
```

---

## 8. Considerações Avançadas

### 8.1. Event Sourcing + CQRS
```java
@Entity
@Table(name = "event_store")
public class EventStore extends PanacheEntityBase {
    @Id
    public String id;
    
    public String aggregateId;
    public String eventType;
    public String eventData;
    public LocalDateTime timestamp;
    public Long version;
    
    public static List<EventStore> findEventsByAggregate(String aggregateId) {
        return list("aggregateId = ?1 ORDER BY version", aggregateId);
    }
}

// Reconstruir aggregate a partir de eventos
public class PedidoAggregateRepository {
    
    public PedidoAggregate loadAggregate(String aggregateId) {
        List<EventStore> events = EventStore.findEventsByAggregate(aggregateId);
        
        PedidoAggregate aggregate = new PedidoAggregate();
        
        for (EventStore event : events) {
            aggregate.apply(deserializeEvent(event));
        }
        
        return aggregate;
    }
}
```

### 8.2. Sagas/Process Managers
```java
@ApplicationScoped
public class ProcessamentoPedidoSaga {
    
    @Incoming("pedido-events")
    public void handle(PedidoCriadoEvent event) {
        // 1. Verificar estoque
        // 2. Processar pagamento
        // 3. Confirmar pedido
        // 4. Notificar cliente
    }
}
```

---

## 9. Vantagens e Desvantagens

### ✅ Vantagens
- **Performance**: Cada lado otimizado independentemente
- **Escalabilidade**: Escala leitura e escrita separadamente
- **Simplicidade**: Modelos focados e menos complexos
- **Flexibilidade**: Diferentes tecnologias para cada lado
- **Auditoria**: Histórico completo de mudanças via eventos

### ❌ Desvantagens
- **Complexidade inicial**: Mais código e infraestrutura
- **Consistência eventual**: Dados podem estar temporariamente inconsistentes
- **Overhead**: Mais componentes para manter
- **Curva de aprendizado**: Equipe precisa entender o padrão

---

## 10. Quando Usar CQRS

### ✅ Use quando:
- **Alta carga de leitura vs escrita** desbalanceada
- **Modelos complexos** com muitas responsabilidades
- **Necessidade de otimizações** específicas para cada operação
- **Relatórios complexos** que impactam performance
- **Múltiplas representações** dos mesmos dados

### ❌ Não use quando:
- **Aplicações simples** com operações básicas
- **Equipe inexperiente** em arquiteturas distribuídas
- **CRUD simples** sem complexidade de negócio
- **Poucos usuários** e baixa carga

---

## Conclusão

CQRS é um padrão poderoso que, quando aplicado corretamente, pode trazer grandes benefícios de performance, escalabilidade e manutenibilidade. A implementação com Java/Quarkus oferece todas as ferramentas necessárias para uma solução robusta e moderna.

Lembre-se: **simplicidade primeiro**. Comece com uma arquitetura tradicional e evolua para CQRS quando a complexidade justificar os benefícios.