# LangChain4j com Quarkus - Guia Completo de IA Generativa 🤖

## 📑 Índice

1. [🎯 Introdução ao LangChain4j](#-introdução-ao-langchain4j)
2. [⚙️ Configuração Inicial](#️-configuração-inicial)
3. [💬 Chat e Conversação](#-chat-e-conversação)
4. [🧠 RAG - Retrieval Augmented Generation](#-rag---retrieval-augmented-generation)
5. [🔧 AI Services e Tools](#-ai-services-e-tools)
6. [📚 Embedding e Vetorização](#-embedding-e-vetorização)
7. [💾 Memory e Contexto](#-memory-e-contexto)
8. [🎨 Prompt Engineering](#-prompt-engineering)
9. [🔄 Streaming e Respostas Reativas](#-streaming-e-respostas-reativas)
10. [✅ Boas Práticas e Produção](#-boas-práticas-e-produção)

---

## 🎯 Introdução ao LangChain4j

### O Que É LangChain4j?

**LangChain4j = Framework Java para construir aplicações com IA Generativa**

**Analogia do Chef Pessoal 👨‍🍳:**

| **Sem LangChain4j** ❌ | **Com LangChain4j** ✅ |
|------------------------|------------------------|
| Você precisa aprender a cozinhar | Você tem um chef que entende suas ordens |
| Chamar APIs complexas manualmente | Interface Java simples e intuitiva |
| Gerenciar contexto manualmente | Memória e contexto automáticos |
| Processar respostas brutas | Respostas estruturadas em POJOs |
| Reinventar a roda sempre | Componentes prontos e testados |

### 🌟 Recursos Principais

```
┌─────────────────────────────────────────────────┐
│            LANGCHAIN4J + QUARKUS                 │
├─────────────────────────────────────────────────┤
│  💬 Chat & Conversação                          │
│  🧠 RAG (Retrieval Augmented Generation)        │
│  🔧 AI Tools (Function Calling)                 │
│  📚 Embedding Stores (Vetores)                  │
│  💾 Memória Persistente                         │
│  🎨 Prompt Templates                            │
│  🔄 Streaming                                   │
│  ⚡ Integração Nativa Quarkus                   │
└─────────────────────────────────────────────────┘
```

### 💡 Casos de Uso

| Caso de Uso | Exemplo Real |
|-------------|--------------|
| **Chatbot Inteligente** | Suporte ao cliente 24/7 |
| **Análise de Documentos** | Resumir contratos, extrair informações |
| **Geração de Código** | Assistente de programação |
| **Q&A sobre Dados** | Perguntas sobre documentação interna |
| **Classificação de Texto** | Análise de sentimento, categorização |
| **Extração de Informações** | Dados estruturados de texto livre |

---

## ⚙️ Configuração Inicial

### Dependências Maven

```xml
<properties>
    <langchain4j.version>0.34.0</langchain4j.version>
</properties>

<dependencies>
    <!-- ════════════════════════════════════════════════ -->
    <!-- 🤖 LANGCHAIN4J - Core do Framework              -->
    <!-- ════════════════════════════════════════════════ -->
    <dependency>
        <groupId>io.quarkiverse.langchain4j</groupId>
        <artifactId>quarkus-langchain4j-core</artifactId>
    </dependency>

    <!-- ════════════════════════════════════════════════ -->
    <!-- 🌐 OPENAI - Provider de IA (GPT-4, GPT-3.5)     -->
    <!-- ════════════════════════════════════════════════ -->
    <dependency>
        <groupId>io.quarkiverse.langchain4j</groupId>
        <artifactId>quarkus-langchain4j-openai</artifactId>
    </dependency>

    <!-- ════════════════════════════════════════════════ -->
    <!-- 🗄️ REDIS - Embedding Store (Vetores)           -->
    <!-- ════════════════════════════════════════════════ -->
    <dependency>
        <groupId>io.quarkiverse.langchain4j</groupId>
        <artifactId>quarkus-langchain4j-redis</artifactId>
    </dependency>

    <!-- ════════════════════════════════════════════════ -->
    <!-- 📄 EASY RAG - Processamento de Documentos       -->
    <!-- ════════════════════════════════════════════════ -->
    <dependency>
        <groupId>io.quarkiverse.langchain4j</groupId>
        <artifactId>quarkus-langchain4j-easy-rag</artifactId>
    </dependency>

    <!-- ════════════════════════════════════════════════ -->
    <!-- 🌐 REST - Para APIs                             -->
    <!-- ════════════════════════════════════════════════ -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-rest-jackson</artifactId>
    </dependency>

    <!-- ════════════════════════════════════════════════ -->
    <!-- 📊 WEBSOCKETS - Para Streaming                  -->
    <!-- ════════════════════════════════════════════════ -->
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-websockets-next</artifactId>
    </dependency>
</dependencies>
```

### Configuração (`application.properties`)

```properties
# ════════════════════════════════════════════════════════════
# 🔑 OPENAI - Configuração da API
# ════════════════════════════════════════════════════════════

# API Key (use variável de ambiente em produção!)
quarkus.langchain4j.openai.api-key=${OPENAI_API_KEY}

# Modelo a usar (gpt-4, gpt-3.5-turbo, gpt-4-turbo)
quarkus.langchain4j.openai.chat-model.model-name=gpt-4-turbo

# Temperatura (0.0 = determinístico, 2.0 = muito criativo)
quarkus.langchain4j.openai.chat-model.temperature=0.7

# Máximo de tokens na resposta
quarkus.langchain4j.openai.chat-model.max-tokens=1000

# Timeout
quarkus.langchain4j.openai.timeout=60s

# ════════════════════════════════════════════════════════════
# 📊 LOGGING - Para debug
# ════════════════════════════════════════════════════════════
quarkus.langchain4j.openai.log-requests=true
quarkus.langchain4j.openai.log-responses=true

# ════════════════════════════════════════════════════════════
# 🗄️ REDIS - Embedding Store (se usar RAG)
# ════════════════════════════════════════════════════════════
quarkus.langchain4j.redis.dimension=1536
quarkus.redis.hosts=redis://localhost:6379
```

### Docker Compose (Redis para RAG)

```yaml
version: '3.8'

services:
  # ════════════════════════════════════════════════
  # 🗄️ REDIS STACK - Com suporte a vetores
  # ════════════════════════════════════════════════
  redis:
    image: redis/redis-stack:latest
    container_name: redis-langchain4j
    ports:
      - "6379:6379"      # Redis
      - "8001:8001"      # Redis Insight (UI)
    volumes:
      - redis-data:/data
    environment:
      - REDIS_ARGS=--save 60 1000

volumes:
  redis-data:
```

---

## 💬 Chat e Conversação

### Exemplo Básico - Chat Simples

```java
package com.empresa.ai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

/**
 * Serviço de IA para chat básico.
 * 
 * <p>O Quarkus automaticamente cria uma implementação desta interface
 * que se comunica com o modelo de IA configurado (OpenAI GPT-4).</p>
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@RegisterAiService
public interface ChatService {

    /**
     * Envia uma mensagem para a IA e recebe uma resposta.
     * 
     * <p>Exemplo de uso:</p>
     * <pre>{@code
     * String resposta = chatService.chat("Qual é a capital do Brasil?");
     * // Resposta: "A capital do Brasil é Brasília."
     * }</pre>
     * 
     * @param message a mensagem do usuário
     * @return a resposta da IA
     */
    String chat(String message);

    /**
     * Chat com instruções de sistema personalizadas.
     * 
     * <p>O {@code @SystemMessage} define o comportamento da IA.</p>
     * <p>O {@code @UserMessage} é o template da mensagem do usuário.</p>
     * 
     * <p>Exemplo:</p>
     * <pre>{@code
     * String piada = chatService.contarPiada("programação");
     * // A IA contará uma piada sobre programação
     * }</pre>
     * 
     * @param tema o tema da piada
     * @return uma piada sobre o tema
     */
    @SystemMessage("Você é um comediante especializado em piadas de tecnologia.")
    @UserMessage("Conte uma piada sobre {tema}")
    String contarPiada(String tema);
}
```

### Resource REST para Chat

```java
package com.empresa.ai.resource;

import com.empresa.ai.service.ChatService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

/**
 * Endpoint REST para interação com chat de IA.
 * 
 * <p>Expõe endpoints para conversação com modelos de linguagem.</p>
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@Path("/api/chat")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChatResource {

    @Inject
    ChatService chatService;

    /**
     * Envia uma mensagem para a IA.
     * 
     * <p>Exemplo de requisição:</p>
     * <pre>{@code
     * POST /api/chat
     * {
     *   "message": "Explique programação reativa em Java"
     * }
     * }</pre>
     * 
     * @param request objeto contendo a mensagem
     * @return resposta da IA
     */
    @POST
    public ChatResponse chat(ChatRequest request) {
        String response = chatService.chat(request.getMessage());
        return new ChatResponse(response);
    }

    /**
     * Solicita uma piada sobre um tema específico.
     * 
     * @param tema o tema da piada
     * @return piada gerada pela IA
     */
    @GET
    @Path("/piada/{tema}")
    public ChatResponse piada(@PathParam("tema") String tema) {
        String piada = chatService.contarPiada(tema);
        return new ChatResponse(piada);
    }

    /**
     * DTO para requisição de chat.
     */
    public record ChatRequest(String message) {}

    /**
     * DTO para resposta de chat.
     */
    public record ChatResponse(String response) {}
}
```

### Chat com Resposta Estruturada (POJO)

```java
/**
 * Extrai informações estruturadas de texto livre.
 * 
 * <p>A IA retorna um objeto Java diretamente!</p>
 */
@RegisterAiService
public interface ExtractionService {

    /**
     * Extrai informações de uma pessoa de texto livre.
     * 
     * <p>Exemplo:</p>
     * <pre>{@code
     * String texto = "João Silva tem 30 anos e mora em São Paulo. " +
     *                "Ele é engenheiro de software e trabalha na Acme Corp.";
     * 
     * Person pessoa = service.extractPerson(texto);
     * // pessoa.name() = "João Silva"
     * // pessoa.age() = 30
     * // pessoa.city() = "São Paulo"
     * // pessoa.profession() = "Engenheiro de Software"
     * // pessoa.company() = "Acme Corp"
     * }</pre>
     * 
     * @param text texto contendo informações da pessoa
     * @return objeto Person preenchido
     */
    @UserMessage("Extraia as informações da pessoa do seguinte texto: {text}")
    Person extractPerson(String text);

    /**
     * Representa uma pessoa extraída do texto.
     */
    record Person(
        String name,
        Integer age,
        String city,
        String profession,
        String company
    ) {}
}
```

---

## 🧠 RAG - Retrieval Augmented Generation

### O Que É RAG?

**RAG = Buscar informações relevantes antes de gerar resposta**

**Analogia da Prova com Consulta 📚:**

```
SEM RAG ❌ (apenas memória):
Pergunta: "Qual o faturamento da empresa em 2024?"
IA: "Não tenho essa informação específica..."

COM RAG ✅ (busca + resposta):
1. Busca nos documentos da empresa
2. Encontra: "Faturamento 2024: R$ 10 milhões"
3. IA: "De acordo com os documentos, o faturamento 
        da empresa em 2024 foi de R$ 10 milhões."
```

### Configuração RAG - Ingestão de Documentos

```java
package com.empresa.ai.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Serviço responsável por carregar e indexar documentos.
 * 
 * <p>Processa documentos (PDF, TXT, etc.) e os armazena como
 * vetores (embeddings) no Redis para busca semântica.</p>
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@ApplicationScoped
public class DocumentIngestionService {

    private static final Logger LOG = Logger.getLogger(DocumentIngestionService.class);

    @Inject
    EmbeddingModel embeddingModel;

    @Inject
    EmbeddingStore<TextSegment> embeddingStore;

    /**
     * Carrega documentos na inicialização da aplicação.
     * 
     * <p>Este método é executado automaticamente quando a aplicação inicia.</p>
     * 
     * @param event evento de inicialização
     */
    public void loadDocuments(@Observes StartupEvent event) {
        LOG.info("🔄 Iniciando carregamento de documentos...");

        try {
            // Caminho dos documentos
            Path documentsPath = Paths.get("src/main/resources/documents");

            // ════════════════════════════════════════════════
            // 1️⃣ CARREGAR DOCUMENTOS
            // ════════════════════════════════════════════════
            List<Document> documents = FileSystemDocumentLoader.loadDocuments(
                documentsPath,
                new TextDocumentParser()
            );

            LOG.infof("📄 %d documentos carregados", documents.size());

            // ════════════════════════════════════════════════
            // 2️⃣ DIVIDIR EM SEGMENTOS (chunks)
            // ════════════════════════════════════════════════
            // Divide documentos grandes em pedaços menores
            DocumentSplitter splitter = DocumentSplitters.recursive(
                500,  // Tamanho máximo do segmento
                50    // Sobreposição entre segmentos
            );

            // ════════════════════════════════════════════════
            // 3️⃣ CRIAR EMBEDDINGS E ARMAZENAR
            // ════════════════════════════════════════════════
            EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(splitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

            // Processa e armazena
            ingestor.ingest(documents);

            LOG.info("✅ Documentos indexados com sucesso!");

        } catch (Exception e) {
            LOG.error("❌ Erro ao carregar documentos", e);
        }
    }

    /**
     * Indexa um documento individual.
     * 
     * <p>Útil para adicionar documentos dinamicamente.</p>
     * 
     * @param documentPath caminho do documento
     */
    public void ingestDocument(Path documentPath) {
        LOG.infof("📄 Indexando documento: %s", documentPath.getFileName());

        Document document = FileSystemDocumentLoader.loadDocument(
            documentPath,
            new TextDocumentParser()
        );

        DocumentSplitter splitter = DocumentSplitters.recursive(500, 50);

        EmbeddingStoreIngestor.builder()
            .documentSplitter(splitter)
            .embeddingModel(embeddingModel)
            .embeddingStore(embeddingStore)
            .build()
            .ingest(document);

        LOG.info("✅ Documento indexado!");
    }
}
```

### RAG - Serviço de Q&A

```java
package com.empresa.ai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

/**
 * Serviço de Q&A com RAG (Retrieval Augmented Generation).
 * 
 * <p>Responde perguntas consultando a base de conhecimento
 * indexada (documentos armazenados como vetores).</p>
 * 
 * <p>O Quarkus automaticamente:</p>
 * <ul>
 *   <li>Busca segmentos relevantes no embedding store</li>
 *   <li>Injeta o contexto na mensagem para a IA</li>
 *   <li>Gera resposta baseada no contexto encontrado</li>
 * </ul>
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@RegisterAiService(
    retrievalAugmentor = @RegisterAiService.RetrievalAugmentorSupplier(
        value = RetrievalAugmentorConfig.class
    )
)
public interface DocumentQAService {

    /**
     * Responde perguntas sobre os documentos indexados.
     * 
     * <p>Exemplo:</p>
     * <pre>{@code
     * // Assumindo que você indexou a documentação do Quarkus
     * String resposta = qaService.ask(
     *     "Como configurar banco de dados no Quarkus?"
     * );
     * 
     * // A IA buscará nos documentos e responderá com base neles
     * }</pre>
     * 
     * @param question pergunta do usuário
     * @return resposta baseada nos documentos
     */
    @SystemMessage("""
        Você é um assistente especializado em responder perguntas
        baseando-se APENAS nas informações fornecidas no contexto.
        
        Se a resposta não estiver no contexto, diga:
        "Não encontrei essa informação nos documentos disponíveis."
        
        Sempre cite as fontes quando possível.
        """)
    @UserMessage("""
        Contexto: {context}
        
        Pergunta: {question}
        
        Responda de forma clara e objetiva.
        """)
    String ask(String question);
}
```

### Configuração do Retrieval Augmentor

```java
package com.empresa.ai.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.function.Supplier;

/**
 * Configuração do Retrieval Augmentor para RAG.
 * 
 * <p>Define como buscar e processar informações relevantes
 * antes de gerar a resposta da IA.</p>
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@ApplicationScoped
public class RetrievalAugmentorConfig implements Supplier<RetrievalAugmentor> {

    @Inject
    EmbeddingStore<TextSegment> embeddingStore;

    @Inject
    EmbeddingModel embeddingModel;

    /**
     * Cria o retrieval augmentor.
     * 
     * @return configuração do RAG
     */
    @Override
    public RetrievalAugmentor get() {
        // ════════════════════════════════════════════════
        // CONTENT RETRIEVER - Busca documentos relevantes
        // ════════════════════════════════════════════════
        EmbeddingStoreContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
            .embeddingStore(embeddingStore)
            .embeddingModel(embeddingModel)
            .maxResults(5)        // Top 5 segmentos mais relevantes
            .minScore(0.7)        // Mínimo de 70% de similaridade
            .build();

        // ════════════════════════════════════════════════
        // RETRIEVAL AUGMENTOR - Orquestra o processo RAG
        // ════════════════════════════════════════════════
        return DefaultRetrievalAugmentor.builder()
            .contentRetriever(retriever)
            .build();
    }
}
```

---

## 🔧 AI Services e Tools

### O Que São Tools?

**Tools = Funções que a IA pode chamar para realizar ações**

**Analogia do Assistente com Ferramentas 🛠️:**

```
SEM TOOLS ❌:
Você: "Qual a previsão do tempo?"
IA: "Não posso consultar APIs externas..."

COM TOOLS ✅:
Você: "Qual a previsão do tempo?"
IA: [chama tool getWeather("São Paulo")]
Tool: Retorna {temp: 25, condition: "ensolarado"}
IA: "Está 25°C e ensolarado em São Paulo!"
```

### Implementando Tools

```java
package com.empresa.ai.tools;

import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Tools (ferramentas) que a IA pode usar para realizar ações.
 * 
 * <p>Cada método anotado com {@code @Tool} pode ser chamado
 * automaticamente pela IA quando necessário.</p>
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@ApplicationScoped
public class BusinessTools {

    private static final Logger LOG = Logger.getLogger(BusinessTools.class);

    /**
     * Calcula o preço final após aplicar desconto.
     * 
     * <p>A IA pode chamar esta ferramenta quando o usuário perguntar
     * sobre preços com desconto.</p>
     * 
     * <p>Exemplo de uso pela IA:</p>
     * <pre>
     * Usuário: "Quanto fica um produto de R$ 100 com 10% de desconto?"
     * IA: [chama calculateDiscount(100, 10)]
     * Retorno: 90.00
     * IA: "Com 10% de desconto, o produto de R$ 100 fica R$ 90,00."
     * </pre>
     * 
     * @param price preço original do produto
     * @param discountPercentage percentual de desconto (0-100)
     * @return preço final após desconto
     */
    @Tool("Calcula o preço final após aplicar desconto percentual")
    public BigDecimal calculateDiscount(
        BigDecimal price,
        double discountPercentage
    ) {
        LOG.infof("🔧 Tool chamada: calculateDiscount(%.2f, %.2f%%)",
            price, discountPercentage);

        BigDecimal discount = price.multiply(
            BigDecimal.valueOf(discountPercentage / 100)
        );

        return price.subtract(discount);
    }

    /**
     * Consulta estoque de um produto.
     * 
     * @param productId ID do produto
     * @return quantidade em estoque
     */
    @Tool("Consulta a quantidade em estoque de um produto")
    public int getProductStock(String productId) {
        LOG.infof("🔧 Tool chamada: getProductStock(%s)", productId);

        // Simula consulta ao banco de dados
        Map<String, Integer> mockStock = Map.of(
            "PROD001", 50,
            "PROD002", 0,
            "PROD003", 100
        );

        return mockStock.getOrDefault(productId, 0);
    }

    /**
     * Verifica se uma data é útil (não fim de semana).
     * 
     * @param date data a verificar
     * @return true se for dia útil
     */
    @Tool("Verifica se uma data é dia útil (segunda a sexta)")
    public boolean isBusinessDay(LocalDate date) {
        LOG.infof("🔧 Tool chamada: isBusinessDay(%s)", date);

        int dayOfWeek = date.getDayOfWeek().getValue();
        return dayOfWeek >= 1 && dayOfWeek <= 5; // 1=Mon, 5=Fri
    }

    /**
     * Calcula prazo de entrega baseado na localização.
     * 
     * @param city cidade de destino
     * @return dias úteis para entrega
     */
    @Tool("Calcula o prazo de entrega em dias úteis para uma cidade")
    public int calculateDeliveryDays(String city) {
        LOG.infof("🔧 Tool chamada: calculateDeliveryDays(%s)", city);

        // Simula cálculo de prazo
        return switch (city.toLowerCase()) {
            case "são paulo", "sp" -> 2;
            case "rio de janeiro", "rj" -> 3;
            case "belo horizonte", "bh" -> 4;
            default -> 7;
        };
    }
}
```

### Serviço que Usa Tools

```java
package com.empresa.ai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

/**
 * Assistente de e-commerce com acesso a ferramentas.
 * 
 * <p>A IA pode chamar os métodos de {@link BusinessTools}
 * automaticamente quando necessário para responder perguntas.</p>
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@RegisterAiService(tools = BusinessTools.class)
public interface EcommerceAssistant {

    /**
     * Responde perguntas sobre produtos e pedidos.
     * 
     * <p>Exemplos de perguntas que a IA pode responder:</p>
     * <ul>
     *   <li>"Quanto fica um produto de R$ 200 com 15% de desconto?"</li>
     *   <li>"Tem o produto PROD001 em estoque?"</li>
     *   <li>"Quantos dias demora para entregar em São Paulo?"</li>
     *   <li>"Dia 25/12/2024 é dia útil?"</li>
     * </ul>
     * 
     * @param question pergunta do cliente
     * @return resposta com informações calculadas
     */
    @SystemMessage("""
        Você é um assistente de e-commerce prestativo e profissional.
        
        Use as ferramentas disponíveis para consultar informações
        precisas sobre estoque, preços, prazos e descontos.
        
        Sempre seja claro e objetivo nas respostas.
        """)
    String assist(String question);
}
```

---

## 📚 Embedding e Vetorização

### O Que São Embeddings?

**Embedding = Representação numérica (vetor) do significado de um texto**

**Analogia das Coordenadas 🗺️:**

```
Texto: "cachorro"  → Vetor: [0.8, 0.2, 0.9, ...]
Texto: "cão"       → Vetor: [0.79, 0.21, 0.88, ...]  ← Muito similar!
Texto: "gato"      → Vetor: [0.7, 0.3, 0.85, ...]   ← Parecido (animais)
Texto: "carro"     → Vetor: [0.1, 0.9, 0.2, ...]    ← Muito diferente
```

### Gerando Embeddings Manualmente

```java
package com.empresa.ai.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Serviço para trabalhar com embeddings e busca semântica.
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@ApplicationScoped
public class EmbeddingService {

    @Inject
    EmbeddingModel embeddingModel;

    @Inject
    EmbeddingStore<TextSegment> embeddingStore;

    /**
     * Adiciona um texto ao embedding store.
     * 
     * <p>Exemplo:</p>
     * <pre>{@code
     * embeddingService.addText(
     *     "O Quarkus é um framework Java nativo para Kubernetes"
     * );
     * }</pre>
     * 
     * @param text texto a ser indexado
     * @return ID do embedding armazenado
     */
    public String addText(String text) {
        // ════════════════════════════════════════════════
        // 1️⃣ CRIAR SEGMENTO DE TEXTO
        // ════════════════════════════════════════════════
        TextSegment segment = TextSegment.from(text);

        // ════════════════════════════════════════════════
        // 2️⃣ GERAR EMBEDDING (vetor)
        // ════════════════════════════════════════════════
        Response<Embedding> response = embeddingModel.embed(segment);
        Embedding embedding = response.content();

        // ════════════════════════════════════════════════
        // 3️⃣ ARMAZENAR NO EMBEDDING STORE
        // ════════════════════════════════════════════════
        String id = embeddingStore.add(embedding, segment);

        return id;
    }

    /**
     * Busca textos similares.
     * 
     * <p>Exemplo:</p>
     * <pre>{@code
     * List<String> similares = embeddingService.findSimilar(
     *     "framework para containers",
     *     5  // top 5 resultados
     * );
     * 
     * // Pode retornar:
     * // - "O Quarkus é um framework Java nativo para Kubernetes"
     * // - "Spring Boot é um framework para aplicações Java"
     * // - ...
     * }</pre>
     * 
     * @param query texto de busca
     * @param maxResults número máximo de resultados
     * @return textos mais similares
     */
    public List<String> findSimilar(String query, int maxResults) {
        // ════════════════════════════════════════════════
        // 1️⃣ GERAR EMBEDDING DA QUERY
        // ════════════════════════════════════════════════
        Embedding queryEmbedding = embeddingModel.embed(query).content();

        // ════════════════════════════════════════════════
        // 2️⃣ BUSCAR EMBEDDINGS SIMILARES
        // ════════════════════════════════════════════════
        List<EmbeddingMatch<TextSegment>> matches = 
            embeddingStore.findRelevant(queryEmbedding, maxResults);

        // ════════════════════════════════════════════════
        // 3️⃣ EXTRAIR TEXTOS DOS MATCHES
        // ════════════════════════════════════════════════
        return matches.stream()
            .map(match -> match.embedded().text())
            .toList();
    }

    /**
     * Calcula similaridade entre dois textos.
     * 
     * @param text1 primeiro texto
     * @param text2 segundo texto
     * @return score de similaridade (0.0 a 1.0)
     */
    public double calculateSimilarity(String text1, String text2) {
        Embedding emb1 = embeddingModel.embed(text1).content();
        Embedding emb2 = embeddingModel.embed(text2).content();

        // Calcula similaridade por cosseno
        return cosineSimilarity(emb1.vector(), emb2.vector());
    }

    /**
     * Calcula similaridade por cosseno entre dois vetores.
     */
    private double cosineSimilarity(float[] vector1, float[] vector2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            norm1 += vector1[i] * vector1[i];
            norm2 += vector2[i] * vector2[i];
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
```

---

## 💾 Memory e Contexto

### Chat com Memória de Conversação

```java
package com.empresa.ai.service;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

/**
 * Chat com memória de conversação.
 * 
 * <p>A IA lembra de mensagens anteriores na conversa,
 * permitindo contexto contínuo.</p>
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@RegisterAiService
public interface ConversationalChatService {

    /**
     * Chat com memória por usuário.
     * 
     * <p>Exemplo de uso:</p>
     * <pre>{@code
     * // Primeira mensagem
     * String resp1 = chat.chat("user123", "Meu nome é João");
     * // IA: "Prazer em conhecê-lo, João!"
     * 
     * // Segunda mensagem (IA lembra do nome!)
     * String resp2 = chat.chat("user123", "Qual é meu nome?");
     * // IA: "Seu nome é João."
     * }</pre>
     * 
     * @param userId ID do usuário (para separar conversas)
     * @param message mensagem do usuário
     * @return resposta da IA
     */
    @SystemMessage("Você é um assistente prestativo que lembra do contexto da conversa.")
    String chat(@MemoryId String userId, @UserMessage String message);
}
```

### Configuração de Memória Customizada

```java
package com.empresa.ai.config;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import io.quarkus.arc.lookup.LookupIfProperty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Singleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Configuração de memória de chat.
 * 
 * <p>Mantém histórico de mensagens para cada usuário.</p>
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@ApplicationScoped
public class ChatMemoryConfig implements Supplier<ChatMemory> {

    // Armazena memórias por usuário
    private final Map<Object, ChatMemory> memories = new ConcurrentHashMap<>();

    /**
     * Cria memória de chat com janela de 20 mensagens.
     * 
     * <p>Mantém as últimas 20 mensagens na memória.
     * Mensagens mais antigas são descartadas.</p>
     * 
     * @return instância de chat memory
     */
    @Override
    public ChatMemory get() {
        return MessageWindowChatMemory.builder()
            .maxMessages(20)  // Últimas 20 mensagens
            .build();
    }
}
```

---

## 🎨 Prompt Engineering

### Templates de Prompts

```java
package com.empresa.ai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;

/**
 * Serviço com prompt engineering avançado.
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@RegisterAiService
public interface PromptEngineeringService {

    /**
     * Gera código com instruções específicas.
     * 
     * <p>Usa template de prompt com variáveis.</p>
     * 
     * @param language linguagem de programação
     * @param task descrição da tarefa
     * @param requirements requisitos adicionais
     * @return código gerado
     */
    @SystemMessage("""
        Você é um programador especialista em {{language}}.
        
        Gere código seguindo estas diretrizes:
        - Use boas práticas e padrões de projeto
        - Adicione JavaDoc completo
        - Inclua tratamento de erros
        - Escreva código legível e manutenível
        """)
    @UserMessage("""
        Tarefa: {{task}}
        
        Requisitos adicionais:
        {{requirements}}
        
        Gere o código completo com:
        1. JavaDoc detalhado
        2. Validações necessárias
        3. Tratamento de exceções
        4. Testes unitários básicos
        """)
    String generateCode(
        @V("language") String language,
        @V("task") String task,
        @V("requirements") String requirements
    );

    /**
     * Analisa sentimento de texto.
     * 
     * @param text texto a analisar
     * @return análise estruturada
     */
    @SystemMessage("""
        Você é um analisador de sentimentos expert.
        
        Analise o texto e retorne no formato:
        {
          "sentiment": "positive|negative|neutral",
          "score": 0.0-1.0,
          "keywords": ["palavra1", "palavra2"],
          "summary": "resumo da análise"
        }
        """)
    @UserMessage("Analise o sentimento do seguinte texto: {{text}}")
    SentimentAnalysis analyzeSentiment(@V("text") String text);

    /**
     * Resultado da análise de sentimento.
     */
    record SentimentAnalysis(
        String sentiment,
        double score,
        String[] keywords,
        String summary
    ) {}
}
```

---

## 🔄 Streaming e Respostas Reativas

### Streaming com Server-Sent Events

```java
package com.empresa.ai.service;

import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

/**
 * Serviço de chat com streaming.
 * 
 * <p>Retorna tokens conforme são gerados,
 * permitindo feedback em tempo real.</p>
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@RegisterAiService
public interface StreamingChatService {

    /**
     * Chat com resposta em streaming.
     * 
     * <p>Tokens são enviados conforme gerados pela IA.</p>
     * 
     * @param message mensagem do usuário
     * @return stream de tokens
     */
    TokenStream chatStream(@UserMessage String message);
}
```

### Resource com Streaming

```java
package com.empresa.ai.resource;

import com.empresa.ai.service.StreamingChatService;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

/**
 * Endpoint REST com streaming de respostas.
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@Path("/api/chat/stream")
public class StreamingChatResource {

    @Inject
    StreamingChatService chatService;

    /**
     * Endpoint de chat com Server-Sent Events.
     * 
     * <p>Exemplo de uso com JavaScript:</p>
     * <pre>{@code
     * const eventSource = new EventSource(
     *     '/api/chat/stream?message=Conte uma história'
     * );
     * 
     * eventSource.onmessage = (event) => {
     *     console.log(event.data); // Cada token
     * };
     * }</pre>
     * 
     * @param message mensagem do usuário
     * @return stream de tokens
     */
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> chatStream(@QueryParam("message") String message) {
        return Multi.createFrom().emitter(emitter -> {
            chatService.chatStream(message)
                .onNext(emitter::emit)
                .onComplete(c -> emitter.complete())
                .onError(emitter::fail)
                .start();
        });
    }
}
```

---

## ✅ Boas Práticas e Produção

### 1️⃣ Configuração Segura

```properties
# ════════════════════════════════════════════════════════════
# ❌ NUNCA faça isso (API key hardcoded)
# ════════════════════════════════════════════════════════════
# quarkus.langchain4j.openai.api-key=sk-abc123...

# ════════════════════════════════════════════════════════════
# ✅ Use variáveis de ambiente
# ════════════════════════════════════════════════════════════
quarkus.langchain4j.openai.api-key=${OPENAI_API_KEY}

# ════════════════════════════════════════════════════════════
# ✅ Timeouts adequados
# ════════════════════════════════════════════════════════════
quarkus.langchain4j.openai.timeout=60s
quarkus.langchain4j.openai.max-retries=3

# ════════════════════════════════════════════════════════════
# ✅ Rate limiting
# ════════════════════════════════════════════════════════════
quarkus.langchain4j.openai.max-requests-per-minute=50
```

### 2️⃣ Tratamento de Erros

```java
package com.empresa.ai.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

/**
 * Tratamento global de erros de IA.
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@Provider
public class AIExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(AIExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        LOG.error("Erro na chamada de IA", exception);

        // Erro de rate limit
        if (exception.getMessage().contains("rate_limit_exceeded")) {
            return Response.status(429)
                .entity(new ErrorResponse(
                    "Limite de requisições excedido. Tente novamente em alguns minutos."
                ))
                .build();
        }

        // Erro de timeout
        if (exception.getMessage().contains("timeout")) {
            return Response.status(504)
                .entity(new ErrorResponse(
                    "Tempo limite excedido. A IA está demorando para responder."
                ))
                .build();
        }

        // Erro genérico
        return Response.status(500)
            .entity(new ErrorResponse(
                "Erro ao processar sua solicitação."
            ))
            .build();
    }

    record ErrorResponse(String message) {}
}
```

### 3️⃣ Monitoramento e Métricas

```java
package com.empresa.ai.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Serviço de métricas para chamadas de IA.
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@ApplicationScoped
public class AIMetricsService {

    @Inject
    MeterRegistry registry;

    private Counter successCounter;
    private Counter errorCounter;
    private Timer responseTimer;

    /**
     * Registra uma chamada bem-sucedida.
     */
    public void recordSuccess(String operation, long durationMs) {
        getSuccessCounter().increment();
        getResponseTimer().record(
            java.time.Duration.ofMillis(durationMs)
        );
    }

    /**
     * Registra um erro.
     */
    public void recordError(String operation, String errorType) {
        getErrorCounter().increment();
    }

    private Counter getSuccessCounter() {
        if (successCounter == null) {
            successCounter = Counter.builder("ai.calls.success")
                .description("Successful AI calls")
                .register(registry);
        }
        return successCounter;
    }

    private Counter getErrorCounter() {
        if (errorCounter == null) {
            errorCounter = Counter.builder("ai.calls.error")
                .description("Failed AI calls")
                .register(registry);
        }
        return errorCounter;
    }

    private Timer getResponseTimer() {
        if (responseTimer == null) {
            responseTimer = Timer.builder("ai.response.time")
                .description("AI response time")
                .register(registry);
        }
        return responseTimer;
    }
}
```

### 4️⃣ Cache de Respostas

```java
package com.empresa.ai.service;

import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Serviço com cache de respostas da IA.
 * 
 * <p>Evita chamadas repetidas para a mesma pergunta.</p>
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@ApplicationScoped
public class CachedChatService {

    /**
     * Chat com cache de 1 hora.
     * 
     * <p>Perguntas idênticas retornam resposta em cache
     * sem chamar a API novamente.</p>
     * 
     * @param question pergunta do usuário
     * @return resposta (em cache se disponível)
     */
    @CacheResult(cacheName = "ai-responses")
    public String chat(String question) {
        // Chamada real à IA
        // (só executada se não houver cache)
        return callAI(question);
    }

    private String callAI(String question) {
        // Implementação real
        return "Resposta da IA";
    }
}
```

**Configuração do cache:**

```properties
# Cache de respostas
quarkus.cache.caffeine.ai-responses.expire-after-write=1H
quarkus.cache.caffeine.ai-responses.maximum-size=1000
```

### 5️⃣ Testes Unitários

```java
package com.empresa.ai.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do chat service.
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@QuarkusTest
class ChatServiceTest {

    @Inject
    ChatService chatService;

    @Test
    void deveChatarComSucesso() {
        // Given
        String mensagem = "Olá!";

        // When
        String resposta = chatService.chat(mensagem);

        // Then
        assertNotNull(resposta);
        assertFalse(resposta.isEmpty());
    }

    @Test
    void deveContarPiadaSobreTema() {
        // Given
        String tema = "programação";

        // When
        String piada = chatService.contarPiada(tema);

        // Then
        assertNotNull(piada);
        assertTrue(piada.toLowerCase().contains("programação") ||
                   piada.toLowerCase().contains("código"));
    }
}
```

---

## 🎓 Exemplo Completo - Sistema de Suporte

```java
package com.empresa.support;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

/**
 * Sistema completo de suporte ao cliente com IA.
 * 
 * <p>Combina:</p>
 * <ul>
 *   <li>RAG - Consulta base de conhecimento</li>
 *   <li>Tools - Acesso a sistemas internos</li>
 *   <li>Memory - Contexto da conversa</li>
 *   <li>Prompt Engineering - Respostas profissionais</li>
 * </ul>
 * 
 * @author Seu Nome
 * @since 1.0.0
 */
@RegisterAiService(
    tools = SupportTools.class,
    retrievalAugmentor = @RegisterAiService.RetrievalAugmentorSupplier(
        value = SupportRetrievalConfig.class
    )
)
public interface SupportAssistant {

    /**
     * Atende cliente com suporte completo.
     * 
     * <p>Exemplo de conversa:</p>
     * <pre>{@code
     * // Cliente 1
     * assistant.assist("cliente123", "Como faço para trocar minha senha?");
     * // IA busca na base de conhecimento e responde com passo a passo
     * 
     * assistant.assist("cliente123", "E se eu esquecer a senha?");
     * // IA lembra do contexto e continua a ajuda
     * 
     * assistant.assist("cliente123", "Qual o status do meu pedido #12345?");
     * // IA usa tool para consultar o sistema de pedidos
     * }</pre>
     * 
     * @param customerId ID do cliente
     * @param message mensagem/pergunta do cliente
     * @return resposta do assistente
     */
    @SystemMessage("""
        Você é um assistente de suporte ao cliente profissional e empático.
        
        Diretrizes:
        1. Sempre seja educado e prestativo
        2. Use a base de conhecimento para respostas precisas
        3. Use as ferramentas disponíveis quando necessário
        4. Se não souber a resposta, encaminhe para atendimento humano
        5. Mantenha tom profissional mas amigável
        6. Resuma informações técnicas de forma clara
        
        Base de conhecimento disponível: {{context}}
        """)
    @UserMessage("Cliente pergunta: {{message}}")
    String assist(
        @MemoryId String customerId,
        @dev.langchain4j.service.V("message") String message
    );
}
```

---

## 🚀 Conclusão

### 📋 Checklist de Implementação

- [ ] ✅ Configurar dependências LangChain4j
- [ ] ✅ Adicionar API key em variável de ambiente
- [ ] ✅ Criar AI Service básico
- [ ] ✅ Implementar RAG se necessário
- [ ] ✅ Adicionar Tools para ações
- [ ] ✅ Configurar memória de conversação
- [ ] ✅ Implementar tratamento de erros
- [ ] ✅ Adicionar métricas e monitoramento
- [ ] ✅ Configurar cache
- [ ] ✅ Escrever testes
- [ ] ✅ Documentar com JavaDoc

### 📚 Recursos Adicionais

- [LangChain4j Docs](https://docs.langchain4j.dev/)
- [Quarkus LangChain4j Guide](https://docs.quarkiverse.io/quarkus-langchain4j/)
- [OpenAI API Reference](https://platform.openai.com/docs/api-reference)
- [Prompt Engineering Guide](https://www.promptingguide.ai/)

### 🎯 Próximos Passos

1. **Explorar outros providers** - Hugging Face, Ollama (local)
2. **Implementar guardrails** - Validação de conteúdo
3. **Otimizar custos** - Cache, modelos menores
4. **Melhorar RAG** - Reranking, hybrid search
5. **Adicionar observabilidade** - Langfuse, Langsmith

**Com LangChain4j e Quarkus, você tem tudo para construir aplicações de IA de produção! 🚀**
