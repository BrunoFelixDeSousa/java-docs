# 📝 Guia Completo de Logging no Quarkus

> **Do System.out.println caótico para um sistema de logging profissional e observável**  
> Aprenda a implementar logging de nível enterprise com JBoss Logging, integração ELK e melhores práticas.

---

## 📖 Índice Rápido

1. [Por que NÃO usar System.out.println?](#-por-que-não-usar-systemoutprintln)
2. [Níveis de Log - Quando Usar Cada Um](#-níveis-de-log)
3. [Setup Rápido - 3 Minutos](#-setup-rápido)
4. [Configuração Completa](#-configuração-completa)
5. [Uso no Código - Exemplos Práticos](#-uso-no-código)
6. [Boas Práticas - O que Fazer e Não Fazer](#-boas-práticas)
7. [Logs Estruturados (JSON)](#-logs-estruturados-json)
8. [Integração com ELK Stack](#-integração-com-elk-stack)
9. [Performance e Otimizações](#-performance-e-otimizações)
10. [Troubleshooting](#-troubleshooting)

---

## 🚫 Por que NÃO usar System.out.println?

### ❌ O Problema

```java
public class PedidoService {
    
    public void criar(Pedido pedido) {
        System.out.println("Criando pedido: " + pedido.getId());
        
        try {
            repository.save(pedido);
            System.out.println("Pedido salvo!");
            
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
            e.printStackTrace(); // Pior ainda!
        }
    }
}
```

**Por que isso é terrível em produção?**

| Problema | Impacto Real |
|----------|--------------|
| 🔥 **Sem controle de nível** | Não consegue filtrar (tudo ou nada) |
| 🔥 **Sem formatação** | Logs ilegíveis e inconsistentes |
| 🔥 **Sem contexto** | Qual thread? Qual timestamp? Qual servidor? |
| 🔥 **Performance ruim** | Sempre ativo, bloqueia I/O |
| 🔥 **Impossível analisar** | Ferramentas de análise não funcionam |
| 🔥 **Sem rotação de arquivos** | Disco cheio em produção! |

**Exemplo real de desastre:**

```
PRODUÇÃO - 1.000 requests/segundo:
- 50GB de logs/dia com System.out.println
- Disco cheio em 3 dias
- Aplicação travou
- Impossível encontrar erro crítico no meio do lixo
```

### ✅ A Solução: Framework de Logging

```java
import org.jboss.logging.Logger;

public class PedidoService {
    
    private static final Logger LOG = Logger.getLogger(PedidoService.class);
    
    public void criar(Pedido pedido) {
        LOG.infof("Criando pedido ID: %d para cliente: %d", 
                  pedido.getId(), pedido.getClienteId());
        
        try {
            repository.save(pedido);
            LOG.info("Pedido salvo com sucesso");
            
        } catch (Exception e) {
            LOG.error("Falha ao salvar pedido", e);
        }
    }
}
```

**Benefícios imediatos:**

| Recurso | Benefício |
|---------|-----------|
| ✅ **Níveis configuráveis** | DEV=DEBUG, PROD=INFO |
| ✅ **Formatação consistente** | Timestamp, thread, classe automático |
| ✅ **Contexto automático** | Trace ID, usuário, servidor |
| ✅ **Performance otimizada** | Lazy evaluation, async writes |
| ✅ **Análise poderosa** | Grafana, Kibana, alertas |
| ✅ **Rotação automática** | Arquivos gerenciados |

---

## 📊 Níveis de Log

### Hierarquia e Quando Usar

```
TRACE → DEBUG → INFO → WARN → ERROR
  ↑                              ↑
Mais detalhado              Mais crítico
```

### Guia Visual de Níveis

```java
public class ExemploNiveis {
    private static final Logger LOG = Logger.getLogger(ExemploNiveis.class);
    
    public void processarPedido(Pedido pedido) {
        
        // 🔍 TRACE - Detalhes minuciosos (apenas DEV)
        LOG.trace("Entrando em processarPedido()");
        LOG.tracef("Parâmetros: pedido=%s", pedido);
        
        // 🐛 DEBUG - Informações técnicas para debug
        LOG.debug("Validando dados do pedido");
        LOG.debugf("Status atual: %s, Total de itens: %d", 
                   pedido.getStatus(), pedido.getItens().size());
        
        // ℹ️ INFO - Fluxo normal da aplicação
        LOG.info("Processando pedido");
        LOG.infof("Pedido %d aprovado. Valor: R$ %.2f", 
                  pedido.getId(), pedido.getValor());
        
        // ⚠️ WARN - Problema não crítico, mas atípico
        LOG.warn("Cache de produtos expirado, recarregando...");
        LOG.warnf("Tentativa %d de 3 para processar pagamento", tentativa);
        
        // ❌ ERROR - Erro que afeta funcionalidade
        LOG.error("Falha ao processar pagamento", exception);
        LOG.errorf("Pedido %d rejeitado. Motivo: %s", 
                   pedido.getId(), motivo);
    }
}
```

### Tabela de Decisão Rápida

| Situação | Nível | Exemplo |
|----------|-------|---------|
| Valor de variável em loop | `TRACE` | `LOG.tracef("i=%d, total=%d", i, total)` |
| Entrada/saída de método | `TRACE` | `LOG.trace("→ calcularDesconto()")` |
| Estado de objeto complexo | `DEBUG` | `LOG.debugf("Usuario: %s", usuario)` |
| Query SQL executada | `DEBUG` | `LOG.debugf("SQL: %s", query)` |
| Operação importante iniciada | `INFO` | `LOG.info("Iniciando importação...")` |
| Operação concluída | `INFO` | `LOG.infof("Importados %d registros", total)` |
| Retry de operação | `WARN` | `LOG.warnf("Retry %d/3", tentativa)` |
| Configuração obsoleta | `WARN` | `LOG.warn("Config antiga detectada")` |
| Exception tratada | `WARN` | `LOG.warn("Timeout, tentando novamente")` |
| Exception não tratada | `ERROR` | `LOG.error("Falha crítica", ex)` |
| Integração falhou | `ERROR` | `LOG.error("API pagamento offline")` |

### Configuração de Níveis por Ambiente

```properties
# DESENVOLVIMENTO - Verbose
quarkus.log.level=DEBUG
quarkus.log.category."com.empresa".level=TRACE

# STAGING - Balanceado
quarkus.log.level=INFO
quarkus.log.category."com.empresa.service".level=DEBUG

# PRODUÇÃO - Essencial
quarkus.log.level=WARN
quarkus.log.category."com.empresa".level=INFO
```

---

## ⚡ Setup Rápido

### 3 Minutos para Começar

#### 1️⃣ **Dependências** (já incluído no Quarkus!)

```xml
<!-- JBoss Logging já vem com Quarkus -->
<!-- Nada a adicionar! ✅ -->
```

#### 2️⃣ **Configuração Básica** (`application.properties`)

```properties
# Nível global
quarkus.log.level=INFO

# Seus pacotes mais verbosos
quarkus.log.category."com.empresa".level=DEBUG

# Console colorido para DEV
quarkus.log.console.color=true
```

#### 3️⃣ **Usar no Código**

```java
import org.jboss.logging.Logger;

public class MeuService {
    
    // Criar logger (STATIC FINAL sempre!)
    private static final Logger LOG = Logger.getLogger(MeuService.class);
    
    public void processar() {
        LOG.info("Processando...");
        LOG.debugf("Detalhe: %s", valor);
    }
}
```

**Pronto! Você já tem logging funcional.** 🎉

---

## ⚙️ Configuração Completa

### Console (Desenvolvimento)

```properties
# ==============================================
# CONSOLE - Para desenvolvimento local
# ==============================================

# Habilitar console
quarkus.log.console.enable=true

# Nível do console
quarkus.log.console.level=DEBUG

# Formato da mensagem
quarkus.log.console.format=%d{HH:mm:ss} %-5p [%c{2.}] (%t) %s%e%n

# Explicação do formato:
# %d{HH:mm:ss}    = Hora (14:30:25)
# %-5p            = Nível alinhado à esquerda (INFO )
# [%c{2.}]        = Classe (últimos 2 pacotes)
# (%t)            = Thread
# %s              = Mensagem
# %e              = Exception (se houver)
# %n              = Nova linha

# Cores (facilita leitura em terminal)
quarkus.log.console.color=true

# Resultado:
# 14:30:25 INFO  [PedidoService] (executor-thread-1) Pedido 123 criado
```

### Arquivo (Produção)

```properties
# ==============================================
# ARQUIVO - Para produção e histórico
# ==============================================

# Habilitar arquivo
quarkus.log.file.enable=true

# Caminho do arquivo
quarkus.log.file.path=logs/application.log

# Nível do arquivo (ALL = tudo)
quarkus.log.file.level=ALL

# Formato mais detalhado para arquivo
quarkus.log.file.format=%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p [%c{3.}] (%t) %s%e%n

# ROTAÇÃO DE ARQUIVOS (evita disco cheio!)

# Tamanho máximo por arquivo
quarkus.log.file.rotation.max-file-size=10M

# Número de backups
quarkus.log.file.rotation.max-backup-index=5

# Rotação diária (opcional)
quarkus.log.file.rotation.file-suffix=.yyyy-MM-dd

# Resultado:
# logs/application.log           (atual, até 10MB)
# logs/application.log.1         (backup 1)
# logs/application.log.2         (backup 2)
# logs/application.log.2024-09-30 (rotação diária)
```

### Níveis por Pacote

```properties
# ==============================================
# CONTROLE GRANULAR POR PACOTE
# ==============================================

# Nível global (padrão)
quarkus.log.level=INFO

# Seu código mais verboso
quarkus.log.category."com.empresa".level=DEBUG
quarkus.log.category."com.empresa.service".level=TRACE

# Frameworks externos menos verbosos
quarkus.log.category."io.quarkus".level=WARN
quarkus.log.category."org.hibernate".level=ERROR
quarkus.log.category."org.jboss".level=WARN

# Específico para debug
quarkus.log.category."com.empresa.PedidoService".level=TRACE

# Resultado:
# ✅ Seus logs: TRACE (máximo detalhe)
# ✅ Quarkus: WARN (só avisos)
# ✅ Hibernate: ERROR (só erros críticos)
```

### Perfis de Ambiente

**`application-dev.properties` (Desenvolvimento):**

```properties
# ==============================================
# DESENVOLVIMENTO
# ==============================================

# Tudo muito verboso
quarkus.log.level=DEBUG
quarkus.log.category."com.empresa".level=TRACE

# Console com cores
quarkus.log.console.enable=true
quarkus.log.console.level=DEBUG
quarkus.log.console.color=true

# Arquivo desnecessário em DEV
quarkus.log.file.enable=false

# Formato simples e legível
quarkus.log.console.format=%d{HH:mm:ss} %p [%c{1}] %s%n
```

**`application-prod.properties` (Produção):**

```properties
# ==============================================
# PRODUÇÃO
# ==============================================

# Apenas essencial
quarkus.log.level=WARN
quarkus.log.category."com.empresa".level=INFO

# Console JSON para ferramentas de análise
quarkus.log.console.enable=true
quarkus.log.console.level=INFO
quarkus.log.console.json=true
quarkus.log.console.color=false

# Arquivo OBRIGATÓRIO em produção
quarkus.log.file.enable=true
quarkus.log.file.path=/var/log/quarkus/app.log
quarkus.log.file.level=INFO

# Rotação agressiva (ambiente de alto volume)
quarkus.log.file.rotation.max-file-size=50M
quarkus.log.file.rotation.max-backup-index=10

# Metadados para observabilidade
quarkus.log.console.json.additional-field."app.name".value=pedidos-service
quarkus.log.console.json.additional-field."app.version".value=1.2.3
quarkus.log.console.json.additional-field."environment".value=production
```

---

## 💻 Uso no Código

### Controller REST - Exemplo Completo

```java
package com.empresa.controller;

import org.jboss.logging.Logger;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/pedidos")
public class PedidoController {
    
    // ✅ SEMPRE static final
    private static final Logger LOG = Logger.getLogger(PedidoController.class);
    
    @Inject
    PedidoService service;
    
    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        // Log de entrada com parâmetro
        LOG.infof("→ Buscando pedido ID: %d", id);
        
        try {
            Pedido pedido = service.buscarPorId(id);
            
            // Log de sucesso
            LOG.infof("✓ Pedido %d encontrado (status: %s)", 
                      id, pedido.getStatus());
            
            return Response.ok(pedido).build();
            
        } catch (PedidoNaoEncontradoException e) {
            // WARN para situação esperada (não é erro crítico)
            LOG.warnf("⚠ Pedido %d não encontrado", id);
            return Response.status(404).build();
            
        } catch (Exception e) {
            // ERROR para situação inesperada
            LOG.errorf(e, "✗ Erro ao buscar pedido %d", id);
            return Response.status(500).build();
        }
    }
    
    @POST
    public Response criar(PedidoRequest request) {
        LOG.infof("→ Criando pedido para cliente: %d", request.getClienteId());
        
        // Debug com dados completos (não vai aparecer em PROD)
        LOG.debugf("Dados do pedido: %s", request);
        
        try {
            Pedido pedido = service.criar(request);
            
            LOG.infof("✓ Pedido %d criado. Valor: R$ %.2f", 
                      pedido.getId(), pedido.getValor());
            
            return Response.status(201).entity(pedido).build();
            
        } catch (ValidacaoException e) {
            LOG.warnf("⚠ Validação falhou: %s", e.getMessage());
            return Response.status(400)
                          .entity(Map.of("erro", e.getMessage()))
                          .build();
                          
        } catch (Exception e) {
            LOG.error("✗ Erro ao criar pedido", e);
            return Response.status(500).build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        LOG.infof("→ Deletando pedido ID: %d", id);
        
        try {
            service.deletar(id);
            
            // WARN para operação destrutiva (auditoria)
            LOG.warnf("⚠ Pedido %d DELETADO", id);
            
            return Response.noContent().build();
            
        } catch (Exception e) {
            LOG.errorf(e, "✗ Erro ao deletar pedido %d", id);
            return Response.status(500).build();
        }
    }
}
```

**Logs gerados:**

```
14:30:25 INFO  [PedidoController] → Buscando pedido ID: 123
14:30:25 INFO  [PedidoController] ✓ Pedido 123 encontrado (status: APROVADO)

14:32:10 INFO  [PedidoController] → Criando pedido para cliente: 456
14:32:10 DEBUG [PedidoController] Dados do pedido: PedidoRequest{cliente=456, valor=150.00}
14:32:10 INFO  [PedidoController] ✓ Pedido 789 criado. Valor: R$ 150.00

14:35:00 INFO  [PedidoController] → Deletando pedido ID: 999
14:35:00 WARN  [PedidoController] ⚠ Pedido 999 DELETADO
```

### Service - Com Medição de Performance

```java
package com.empresa.service;

import org.jboss.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PedidoService {
    
    private static final Logger LOG = Logger.getLogger(PedidoService.class);
    
    @Transactional
    public Pedido criar(PedidoRequest request) {
        long inicio = System.currentTimeMillis();
        
        LOG.infof("Criando pedido para cliente: %d", request.getClienteId());
        
        try {
            // 1. Validação
            validar(request);
            LOG.debug("Validação concluída");
            
            // 2. Criação da entidade
            Pedido pedido = new Pedido();
            pedido.setClienteId(request.getClienteId());
            pedido.setValor(request.getValor());
            pedido.setStatus(StatusPedido.PENDENTE);
            
            LOG.tracef("Objeto criado: %s", pedido);
            
            // 3. Persistência
            pedido.persist();
            LOG.debugf("Pedido persistido com ID: %d", pedido.getId());
            
            // 4. Processamento assíncrono
            processarAsync(pedido);
            LOG.debug("Processamento assíncrono iniciado");
            
            // 5. Log de performance
            long duracao = System.currentTimeMillis() - inicio;
            
            if (duracao > 1000) {
                LOG.warnf("⚠ SLOW_OPERATION - Criação levou %dms (> 1s)", duracao);
            } else {
                LOG.infof("✓ Pedido %d criado em %dms", pedido.getId(), duracao);
            }
            
            return pedido;
            
        } catch (Exception e) {
            long duracao = System.currentTimeMillis() - inicio;
            LOG.errorf(e, "✗ Falha após %dms", duracao);
            throw e;
        }
    }
    
    private void validar(PedidoRequest request) {
        LOG.debug("Validando request");
        
        if (request.getClienteId() == null) {
            LOG.warn("Tentativa de criar pedido sem cliente");
            throw new ValidacaoException("Cliente obrigatório");
        }
        
        if (request.getValor() <= 0) {
            LOG.warnf("Valor inválido: %.2f", request.getValor());
            throw new ValidacaoException("Valor deve ser > 0");
        }
    }
}
```

### Repository - Com Logs de Query

```java
package com.empresa.repository;

import org.jboss.logging.Logger;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PedidoRepository implements PanacheRepository<Pedido> {
    
    private static final Logger LOG = Logger.getLogger(PedidoRepository.class);
    
    public List<Pedido> buscarPorCliente(Long clienteId) {
        long inicio = System.currentTimeMillis();
        
        LOG.debugf("Query: pedidos do cliente %d", clienteId);
        
        List<Pedido> pedidos = find("clienteId", clienteId).list();
        
        long duracao = System.currentTimeMillis() - inicio;
        
        LOG.infof("Query concluída: %d pedidos em %dms", 
                  pedidos.size(), duracao);
        
        // Alerta para queries lentas
        if (duracao > 500) {
            LOG.warnf("⚠ SLOW_QUERY - Cliente %d levou %dms", 
                      clienteId, duracao);
        }
        
        return pedidos;
    }
    
    public List<Pedido> buscarComFiltros(FiltroDTO filtro) {
        // Log condicional (apenas se DEBUG ativo)
        if (LOG.isDebugEnabled()) {
            LOG.debugf("Buscando com filtros: %s", filtro);
        }
        
        // Construção da query...
        String hql = construirQuery(filtro);
        
        // Log da query gerada
        LOG.tracef("HQL gerado: %s", hql);
        
        return find(hql).list();
    }
}
```

---

## ✅ Boas Práticas

### 1. ❌ NÃO Logue Dados Sensíveis

```java
public class SegurancaExemplo {
    private static final Logger LOG = Logger.getLogger(SegurancaExemplo.class);
    
    public void login(String email, String senha) {
        
        // ❌ NUNCA FAÇA ISSO!
        LOG.infof("Login: email=%s, senha=%s", email, senha);
        LOG.debugf("Token JWT: %s", jwtToken);
        LOG.infof("Cartão: %s, CVV: %s", cartao, cvv);
        
        // ✅ FAÇA ASSIM
        LOG.infof("Tentativa de login: %s", email);
        LOG.debugf("Token recebido (tamanho: %d)", jwtToken.length());
        LOG.infof("Cartão: ****%s", cartao.substring(12));
    }
    
    public void processarPagamento(CartaoCredito cartao) {
        
        // ✅ Mascare dados sensíveis
        String numeroMascarado = mascararCartao(cartao.getNumero());
        LOG.infof("Processando pagamento: %s", numeroMascarado);
    }
    
    private String mascararCartao(String numero) {
        // 1234567890123456 → ****3456
        return "****" + numero.substring(12);
    }
}
```

**Dados que NUNCA devem ser logados:**

- ❌ Senhas (óbvio!)
- ❌ Tokens JWT completos
- ❌ Números de cartão de crédito
- ❌ CVV
- ❌ CPF/CNPJ completos
- ❌ Chaves de API
- ❌ Dados bancários

### 2. ✅ Use Interpolação (Não Concatenação)

```java
public class PerformanceExemplo {
    private static final Logger LOG = Logger.getLogger(PerformanceExemplo.class);
    
    public void processar(String item, int quantidade) {
        
        // ❌ RUIM - Concatenação sempre executada
        LOG.debug("Processando " + item + " quantidade: " + quantidade);
        // String é criada SEMPRE, mesmo se DEBUG estiver desabilitado!
        
        // ✅ BOM - Interpolação lazy
        LOG.debugf("Processando %s quantidade: %d", item, quantidade);
        // String só é criada SE debug estiver habilitado
        
        // ✅ AINDA MELHOR - Para operações custosas
        if (LOG.isDebugEnabled()) {
            String dadosComplexos = gerarRelatorioCompleto(); // custoso!
            LOG.debugf("Relatório: %s", dadosComplexos);
        }
    }
}
```

**Por quê?**

```java
// Com concatenação:
LOG.debug("Dados: " + dados);
// 1. Cria StringBuilder
// 2. Append "Dados: "
// 3. Append dados.toString()
// 4. Converte para String
// 5. Passa para logger
// 6. Logger verifica: DEBUG desabilitado
// 7. DESCARTA tudo! ❌ Trabalho perdido!

// Com interpolação:
LOG.debugf("Dados: %s", dados);
// 1. Logger verifica: DEBUG desabilitado
// 2. PARA! ✅ Nenhum trabalho desperdiçado!
```

### 3. ✅ Padronize Mensagens

```java
public class PadraoMensagens {
    private static final Logger LOG = Logger.getLogger(PadraoMensagens.class);
    
    public void processar(Pedido pedido) {
        
        // ✅ Use prefixos consistentes
        LOG.info("PEDIDO_CRIADO id={} cliente={} valor={}",
                 pedido.getId(), pedido.getClienteId(), pedido.getValor());
        
        LOG.warn("PEDIDO_FALHA id={} erro={}",
                 pedido.getId(), erro.getMessage());
        
        LOG.info("PERFORMANCE_QUERY operacao=buscar duracao={}ms", duracao);
        
        // Facilita análise:
        // grep "PEDIDO_CRIADO" logs/*.log
        // grep "PERFORMANCE_QUERY.*duracao=[5-9][0-9][0-9]" logs/*.log (> 500ms)
    }
}
```

**Padrões recomendados:**

```
EVENTO_ACAO           campo1=valor1 campo2=valor2
USUARIO_LOGIN         user_id=123 ip=192.168.1.1
PEDIDO_CRIADO         pedido_id=456 valor=150.00
PERFORMANCE_SLOW      operacao=query duracao=1500ms
ERROR_INTEGRATION     service=pagamento status=timeout
```

### 4. ✅ Log de Exceções Completo

```java
public class ExcecaoExemplo {
    private static final Logger LOG = Logger.getLogger(ExcecaoExemplo.class);
    
    public void processar() {
        try {
            // código
        } catch (Exception e) {
            
            // ❌ RUIM - Perde stack trace
            LOG.error("Erro: " + e.getMessage());
            
            // ❌ RUIM - Duplicado (exception já tem mensagem)
            LOG.error(e.getMessage(), e);
            
            // ✅ BOM - Mensagem contextual + exception completa
            LOG.error("Falha ao processar pedido", e);
            
            // ✅ MELHOR - Com contexto adicional
            LOG.errorf(e, "Falha ao processar pedido ID: %d", pedidoId);
        }
    }
}
```

### 5. ✅ Logger Static Final

```java
// ✅ CORRETO
public class MinhaClasse {
    private static final Logger LOG = Logger.getLogger(MinhaClasse.class);
}

// ❌ ERRADO - Não static (cria logger por instância)
public class MinhaClasse {
    private final Logger LOG = Logger.getLogger(MinhaClasse.class);
}

// ❌ ERRADO - Não final (pode ser reatribuído)
public class MinhaClasse {
    private static Logger LOG = Logger.getLogger(MinhaClasse.class);
}

// ❌ MUITO ERRADO - String hardcoded
public class MinhaClasse {
    private static final Logger LOG = Logger.getLogger("MinhaClasse");
}
```

### 6. ✅ Evite Log Excessivo

```java
public class LogExcessivo {
    private static final Logger LOG = Logger.getLogger(LogExcessivo.class);
    
    public void processar(List<Item> itens) {
        
        // ❌ RUIM - Loga cada item (pode ser 10.000!)
        for (Item item : itens) {
            LOG.info("Processando item: " + item.getId());
        }
        
        // ✅ BOM - Loga resumo
        LOG.infof("Processando %d itens", itens.size());
        
        // ✅ ALTERNATIVA - Log condicional
        for (int i = 0; i < itens.size(); i++) {
            Item item = itens.get(i);
            
            // Log apenas a cada 1000 itens
            if (i % 1000 == 0) {
                LOG.infof("Progresso: %d/%d itens", i, itens.size());
            }
        }
        
        LOG.infof("Processamento concluído: %d itens", itens.size());
    }
}
```

---

## 📊 Logs Estruturados (JSON)

### Por que JSON?

| Formato Texto | Formato JSON |
|---------------|--------------|
| Difícil parsear | Fácil parsear |
| Busca por regex | Query estruturada |
| Sem metadados | Metadados ricos |
| Ferramentas limitadas | Ferramentas poderosas (ELK, Grafana) |

### Configuração

```properties
# ==============================================
# LOGS JSON PARA PRODUÇÃO
# ==============================================

# Habilitar JSON
quarkus.log.console.json=true

# Não pretty-print (economiza espaço)
quarkus.log.console.json.pretty-print=false

# Campos adicionais personalizados
quarkus.log.console.json.additional-field."app.name".value=pedidos-service
quarkus.log.console.json.additional-field."app.version".value=1.2.3
quarkus.log.console.json.additional-field."environment".value=production
quarkus.log.console.json.additional-field."datacenter".value=us-east-1

# Timestamp ISO 8601
quarkus.log.console.json.date-format=yyyy-MM-dd'T'HH:mm:ss.SSSX
```

### Exemplo de Saída

**Código:**

```java
LOG.infof("Pedido %d criado para cliente %d. Valor: R$ %.2f",
          pedido.getId(), pedido.getClienteId(), pedido.getValor());
```

**Log em texto:**

```
2024-09-30 14:30:25 INFO [PedidoService] Pedido 123 criado para cliente 456. Valor: R$ 150.00
```

**Log em JSON:**

```json
{
  "timestamp": "2024-09-30T14:30:25.123-03:00",
  "sequence": 12345,
  "loggerClassName": "com.empresa.service.PedidoService",
  "loggerName": "com.empresa.service.PedidoService",
  "level": "INFO",
  "message": "Pedido 123 criado para cliente 456. Valor: R$ 150.00",
  "threadName": "executor-thread-1",
  "threadId": 42,
  "mdc": {},
  "ndc": "",
  "hostName": "app-server-01",
  "processName": "pedidos-service",
  "processId": 8765,
  "app.name": "pedidos-service",
  "app.version": "1.2.3",
  "environment": "production",
  "datacenter": "us-east-1"
}
```

**Benefícios:**

```javascript
// Consulta fácil no Kibana/Grafana:
level: "ERROR" AND app.name: "pedidos-service"

// Agregação por versão:
app.version: "1.2.3" AND level: "WARN"

// Performance por datacenter:
datacenter: "us-east-1" AND message: "SLOW_OPERATION"
```

---

## 📈 Integração com ELK Stack

### Arquitetura

```
┌─────────────────┐
│ Quarkus App     │ → Gera logs JSON
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Filebeat/       │ → Coleta logs
│ Fluentd         │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Logstash        │ → Processa/Filtra
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Elasticsearch   │ → Armazena/Indexa
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Kibana/Grafana  │ → Visualiza/Alerta
└─────────────────┘
```

### Docker Compose Completo

```yaml
version: '3.8'

services:
  # Elasticsearch - Armazenamento
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.11.0
    environment:
      - discovery.type=single-node
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - xpack.security.enabled=false
    ports:
      - "9200:9200"
    volumes:
      - elasticsearch-data:/usr/share/elasticsearch/data
    networks:
      - elk

  # Logstash - Processamento
  logstash:
    image: docker.elastic.co/logstash/logstash:8.11.0
    volumes:
      - ./logstash/pipeline:/usr/share/logstash/pipeline
      - ./logstash/config/logstash.yml:/usr/share/logstash/config/logstash.yml
    ports:
      - "5000:5000/tcp"
      - "5000:5000/udp"
      - "9600:9600"
    environment:
      LS_JAVA_OPTS: "-Xms256m -Xmx256m"
    depends_on:
      - elasticsearch
    networks:
      - elk

  # Kibana - Visualização
  kibana:
    image: docker.elastic.co/kibana/kibana:8.11.0
    ports:
      - "5601:5601"
    environment:
      ELASTICSEARCH_HOSTS: http://elasticsearch:9200
    depends_on:
      - elasticsearch
    networks:
      - elk

volumes:
  elasticsearch-data:

networks:
  elk:
    driver: bridge
```

### Configuração Logstash

**`logstash/pipeline/logstash.conf`:**

```ruby
input {
  tcp {
    port => 5000
    codec => json_lines
  }
}

filter {
  # Parse do JSON do Quarkus
  json {
    source => "message"
  }
  
  # Adiciona tag para erros
  if [level] == "ERROR" {
    mutate {
      add_tag => ["error"]
      add_field => { "[@metadata][alert]" => "true" }
    }
  }
  
  # Adiciona tag para operações lentas
  if [message] =~ /SLOW_OPERATION/ {
    mutate {
      add_tag => ["slow"]
    }
  }
  
  # Extrai duração de mensagens de performance
  grok {
    match => { "message" => "duracao=%{NUMBER:duration_ms:float}ms" }
  }
}

output {
  # Elasticsearch
  elasticsearch {
    hosts => ["elasticsearch:9200"]
    index => "quarkus-logs-%{+YYYY.MM.dd}"
  }
  
  # Console (debug)
  stdout {
    codec => rubydebug
  }
}
```

### Configuração do Quarkus para ELK

```properties
# ==============================================
# QUARKUS → ELK
# ==============================================

# JSON obrigatório
quarkus.log.console.json=true

# Handler TCP para Logstash
quarkus.log.handler.socket.enable=true
quarkus.log.handler.socket.endpoint=logstash:5000
quarkus.log.handler.socket.protocol=TCP

# Metadados para rastreio
quarkus.log.console.json.additional-field."service.name".value=pedidos-service
quarkus.log.console.json.additional-field."service.version".value=${app.version:1.0.0}
quarkus.log.console.json.additional-field."environment".value=${ENV:dev}
```

### Queries Úteis no Kibana

```
# Todos os erros das últimas 24h
level: "ERROR" AND @timestamp: [now-24h TO now]

# Operações lentas (> 1s)
message: "SLOW_OPERATION" AND duration_ms: >1000

# Erros de um serviço específico
level: "ERROR" AND service.name: "pedidos-service"

# Timeline de criação de pedidos
message: "PEDIDO_CRIADO"
```

---

## ⚡ Performance e Otimizações

### 1. Logs Condicionais

```java
public class OtimizacaoExemplo {
    private static final Logger LOG = Logger.getLogger(OtimizacaoExemplo.class);
    
    public void processar(ComplexObject obj) {
        
        // ❌ RUIM - toString() sempre executado
        LOG.debug("Objeto: " + obj.toString());
        // toString() é chamado SEMPRE, mesmo se DEBUG desabilitado!
        
        // ✅ BOM - Interpolação lazy
        LOG.debugf("Objeto: %s", obj);
        // toString() só é chamado SE debug habilitado
        
        // ✅ MELHOR - Para operações muito custosas
        if (LOG.isDebugEnabled()) {
            String json = serializarParaJson(obj); // operação cara
            LOG.debugf("JSON completo: %s", json);
        }
    }
}
```

### 2. Async Logging

```properties
# ==============================================
# LOGGING ASSÍNCRONO (Alta Performance)
# ==============================================

# Habilitar async
quarkus.log.async=true

# Tamanho da fila (ajuste conforme volume)
quarkus.log.async.queue-length=10000

# Comportamento em overflow
quarkus.log.async.overflow=BLOCK  # ou DISCARD
```

**Benefícios:**

```
Síncrono:  Request → Log (BLOQUEIA) → Response
Tempo: 100ms + 50ms log = 150ms

Assíncrono: Request → Fila → Response
            Fila → Log (background)
Tempo: 100ms (log não bloqueia!)
```

### 3. Sampling (Alto Volume)

```java
public class SamplingExemplo {
    private static final Logger LOG = Logger.getLogger(SamplingExemplo.class);
    private static final Random RANDOM = new Random();
    
    public void processar(Item item) {
        
        // Log apenas 10% dos requests
        if (RANDOM.nextInt(100) < 10) {
            LOG.debugf("Sample: processando item %d", item.getId());
        }
        
        // Ou: log apenas 1 a cada 100
        if (item.getId() % 100 == 0) {
            LOG.infof("Checkpoint: item %d processado", item.getId());
        }
    }
}
```

---

## 🔧 Troubleshooting

### Problema: Logs não aparecem

```properties
# ✅ Verificar nível global
quarkus.log.level=DEBUG

# ✅ Verificar nível do pacote
quarkus.log.category."com.empresa".level=DEBUG

# ✅ Verificar se console está habilitado
quarkus.log.console.enable=true
```

### Problema: Arquivo de log não é criado

```properties
# ✅ Habilitar explicitamente
quarkus.log.file.enable=true

# ✅ Verificar permissões do diretório
# mkdir -p logs && chmod 755 logs

# ✅ Caminho absoluto em produção
quarkus.log.file.path=/var/log/quarkus/app.log
```

### Problema: Logs do Hibernate não aparecem

```properties
# ✅ Habilitar logs do Hibernate
quarkus.log.category."org.hibernate".level=DEBUG

# ✅ Ver SQLs executados
quarkus.hibernate-orm.log.sql=true
quarkus.hibernate-orm.log.bind-parameters=true
```

### Problema: Performance ruim

```properties
# ✅ Habilitar async
quarkus.log.async=true

# ✅ Reduzir verbosidade em produção
quarkus.log.level=WARN
quarkus.log.category."com.empresa".level=INFO

# ✅ Desabilitar stack traces completos
quarkus.log.console.format=%d{HH:mm:ss} %-5p [%c{2.}] %s%n
```

---

## 📚 Referências e Recursos

### Documentação Oficial

- [Quarkus Logging Guide](https://quarkus.io/guides/logging)
- [JBoss Logging](https://github.com/jboss-logging/jboss-logging)
- [Elastic Stack](https://www.elastic.co/elastic-stack)

### Ferramentas Recomendadas

**Desenvolvimento:**
- **Logback**: Configuração avançada
- **SLF4J**: Compatibilidade
- **Logstash Logback Encoder**: JSON direto

**Produção:**
- **ELK Stack**: Elasticsearch + Logstash + Kibana
- **Grafana Loki**: Alternativa leve ao Elasticsearch
- **Fluentd**: Coleta de logs Kubernetes
- **Jaeger**: Tracing distribuído

### Checklist de Produção

- [ ] Nível INFO ou WARN em produção
- [ ] JSON habilitado
- [ ] Rotação de arquivos configurada
- [ ] Sem dados sensíveis nos logs
- [ ] Integração com ELK/Grafana
- [ ] Dashboards criados
- [ ] Alertas configurados
- [ ] Política de retenção definida
- [ ] Async logging habilitado
- [ ] Performance testada

---

## 🎯 Resumo Executivo

### Do ❌ para o ✅

| ❌ Evite | ✅ Faça |
|---------|---------|
| `System.out.println` | `Logger.info()` |
| `LOG.info("x=" + x)` | `LOG.infof("x=%d", x)` |
| `LOG.info(senha)` | Mascare dados sensíveis |
| Logs em todo loop | Log resumo final |
| Nível DEBUG em produção | INFO ou WARN |
| Logs sem contexto | JSON estruturado |
| Arquivos sem rotação | Rotação por tamanho/data |

### Quick Start (Copie e Cole)

```java
// 1. Import
import org.jboss.logging.Logger;

// 2. Declaração
private static final Logger LOG = Logger.getLogger(MinhaClasse.class);

// 3. Uso
LOG.info("Mensagem simples");
LOG.infof("Com parâmetros: %s %d", texto, numero);
LOG.error("Com exception", exception);

// 4. Condicional
if (LOG.isDebugEnabled()) {
    LOG.debugf("Operação custosa: %s", operacaoCara());
}
```

### Configuração Pronta

```properties
# DEV
quarkus.log.level=DEBUG
quarkus.log.console.color=true

# PROD
quarkus.log.level=INFO
quarkus.log.console.json=true
quarkus.log.file.enable=true
quarkus.log.file.rotation.max-file-size=50M
```

---

**Logging bem feito = Debugging fácil + Produção confiável + Time feliz! 🎉**
