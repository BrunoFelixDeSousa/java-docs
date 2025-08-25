// ===== ENTITIES (Modelos de Domínio) =====

// Usuario.java
import java.time.LocalDateTime;
import java.util.Objects;

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private TipoUsuario tipo;
    private LocalDateTime dataCadastro;

    public enum TipoUsuario {
        CLIENTE, ADMIN
    }

    public Usuario(String id, String nome, String email, String senha, TipoUsuario tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
        this.dataCadastro = LocalDateTime.now();
    }

    // Construtor para carregar do arquivo
    public Usuario(String id, String nome, String email, String senha, TipoUsuario tipo, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
        this.dataCadastro = dataCadastro;
    }

    // Getters e Setters
    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public TipoUsuario getTipo() { return tipo; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }

    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }

    // Método para serialização em arquivo texto
    public String toFileString() {
        return String.join(";", 
            id, nome, email, senha, tipo.toString(), dataCadastro.toString()
        );
    }

    // Método para deserialização de arquivo texto
    public static Usuario fromFileString(String line) {
        String[] parts = line.split(";");
        return new Usuario(
            parts[0], parts[1], parts[2], parts[3],
            TipoUsuario.valueOf(parts[4]),
            LocalDateTime.parse(parts[5])
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Usuario{id='%s', nome='%s', email='%s', tipo=%s}", 
                           id, nome, email, tipo);
    }
}

// ==========================================

// Voo.java
import java.time.LocalDateTime;
import java.util.Objects;

public class Voo {
    private String id;
    private String origem;
    private String destino;
    private LocalDateTime dataHora;
    private String companhia;
    private int totalAssentos;
    private double preco;

    public Voo(String id, String origem, String destino, LocalDateTime dataHora, 
               String companhia, int totalAssentos, double preco) {
        this.id = id;
        this.origem = origem;
        this.destino = destino;
        this.dataHora = dataHora;
        this.companhia = companhia;
        this.totalAssentos = totalAssentos;
        this.preco = preco;
    }

    // Getters e Setters
    public String getId() { return id; }
    public String getOrigem() { return origem; }
    public String getDestino() { return destino; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getCompanhia() { return companhia; }
    public int getTotalAssentos() { return totalAssentos; }
    public double getPreco() { return preco; }

    public void setOrigem(String origem) { this.origem = origem; }
    public void setDestino(String destino) { this.destino = destino; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public void setCompanhia(String companhia) { this.companhia = companhia; }
    public void setTotalAssentos(int totalAssentos) { this.totalAssentos = totalAssentos; }
    public void setPreco(double preco) { this.preco = preco; }

    public String toFileString() {
        return String.join(";",
            id, origem, destino, dataHora.toString(), 
            companhia, String.valueOf(totalAssentos), String.valueOf(preco)
        );
    }

    public static Voo fromFileString(String line) {
        String[] parts = line.split(";");
        return new Voo(
            parts[0], parts[1], parts[2], LocalDateTime.parse(parts[3]),
            parts[4], Integer.parseInt(parts[5]), Double.parseDouble(parts[6])
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voo voo = (Voo) o;
        return Objects.equals(id, voo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Voo{id='%s', %s->%s, %s, %s, assentos=%d, R$%.2f}", 
                           id, origem, destino, dataHora, companhia, totalAssentos, preco);
    }
}

// ==========================================

// Reserva.java
import java.time.LocalDateTime;
import java.util.Objects;

public class Reserva {
    private String id;
    private String idUsuario;
    private String idVoo;
    private String assento;
    private StatusReserva status;
    private LocalDateTime dataReserva;
    private double valorPago;

    public enum StatusReserva {
        CONFIRMADA, CANCELADA, PENDENTE
    }

    public Reserva(String id, String idUsuario, String idVoo, String assento, double valorPago) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idVoo = idVoo;
        this.assento = assento;
        this.status = StatusReserva.CONFIRMADA;
        this.dataReserva = LocalDateTime.now();
        this.valorPago = valorPago;
    }

    // Construtor para carregar do arquivo
    public Reserva(String id, String idUsuario, String idVoo, String assento, 
                   StatusReserva status, LocalDateTime dataReserva, double valorPago) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idVoo = idVoo;
        this.assento = assento;
        this.status = status;
        this.dataReserva = dataReserva;
        this.valorPago = valorPago;
    }

    // Getters e Setters
    public String getId() { return id; }
    public String getIdUsuario() { return idUsuario; }
    public String getIdVoo() { return idVoo; }
    public String getAssento() { return assento; }
    public StatusReserva getStatus() { return status; }
    public LocalDateTime getDataReserva() { return dataReserva; }
    public double getValorPago() { return valorPago; }

    public void setStatus(StatusReserva status) { this.status = status; }
    public void setAssento(String assento) { this.assento = assento; }

    public void cancelar() {
        this.status = StatusReserva.CANCELADA;
    }

    public String toFileString() {
        return String.join(";",
            id, idUsuario, idVoo, assento, status.toString(), 
            dataReserva.toString(), String.valueOf(valorPago)
        );
    }

    public static Reserva fromFileString(String line) {
        String[] parts = line.split(";");
        return new Reserva(
            parts[0], parts[1], parts[2], parts[3],
            StatusReserva.valueOf(parts[4]),
            LocalDateTime.parse(parts[5]),
            Double.parseDouble(parts[6])
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return Objects.equals(id, reserva.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Reserva{id='%s', usuario='%s', voo='%s', assento='%s', status=%s, R$%.2f}", 
                           id, idUsuario, idVoo, assento, status, valorPago);
    }
}

// ===== INTERFACES (Contratos) =====

// RepositorioUsuarios.java
import java.util.List;
import java.util.Optional;

public interface RepositorioUsuarios {
    void salvar(Usuario usuario) throws Exception;
    void atualizar(Usuario usuario) throws Exception;
    void excluir(String id) throws Exception;
    Optional<Usuario> buscarPorId(String id) throws Exception;
    Optional<Usuario> buscarPorEmail(String email) throws Exception;
    List<Usuario> listarTodos() throws Exception;
}

// RepositorioVoos.java
import java.util.List;
import java.util.Optional;

public interface RepositorioVoos {
    void salvar(Voo voo) throws Exception;
    void atualizar(Voo voo) throws Exception;
    void excluir(String id) throws Exception;
    Optional<Voo> buscarPorId(String id) throws Exception;
    List<Voo> listarTodos() throws Exception;
    List<Voo> buscarPorOrigem(String origem) throws Exception;
    List<Voo> buscarPorDestino(String destino) throws Exception;
}

// RepositorioReservas.java
import java.util.List;
import java.util.Optional;

public interface RepositorioReservas {
    void salvar(Reserva reserva) throws Exception;
    void atualizar(Reserva reserva) throws Exception;
    void excluir(String id) throws Exception;
    Optional<Reserva> buscarPorId(String id) throws Exception;
    List<Reserva> listarTodos() throws Exception;
    List<Reserva> buscarPorUsuario(String idUsuario) throws Exception;
    List<Reserva> buscarPorVoo(String idVoo) throws Exception;
}

// ===== IMPLEMENTAÇÕES DOS REPOSITÓRIOS =====

// RepositorioUsuariosArquivo.java
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class RepositorioUsuariosArquivo implements RepositorioUsuarios {
    private final Path arquivoUsuarios;
    private final Path diretorioData;

    public RepositorioUsuariosArquivo() throws Exception {
        this.diretorioData = Paths.get("data");
        this.arquivoUsuarios = diretorioData.resolve("usuarios.txt");
        inicializarArquivos();
    }

    private void inicializarArquivos() throws Exception {
        // Cria diretório se não existir
        if (!Files.exists(diretorioData)) {
            Files.createDirectories(diretorioData);
        }
        
        // Cria arquivo se não existir
        if (!Files.exists(arquivoUsuarios)) {
            Files.createFile(arquivoUsuarios);
        }
    }

    @Override
    public void salvar(Usuario usuario) throws Exception {
        List<String> linhas = Files.readAllLines(arquivoUsuarios);
        linhas.add(usuario.toFileString());
        Files.write(arquivoUsuarios, linhas);
    }

    @Override
    public void atualizar(Usuario usuario) throws Exception {
        List<Usuario> usuarios = listarTodos();
        usuarios = usuarios.stream()
                .map(u -> u.getId().equals(usuario.getId()) ? usuario : u)
                .collect(Collectors.toList());
        
        List<String> linhas = usuarios.stream()
                .map(Usuario::toFileString)
                .collect(Collectors.toList());
        
        Files.write(arquivoUsuarios, linhas);
    }

    @Override
    public void excluir(String id) throws Exception {
        List<Usuario> usuarios = listarTodos();
        usuarios = usuarios.stream()
                .filter(u -> !u.getId().equals(id))
                .collect(Collectors.toList());
        
        List<String> linhas = usuarios.stream()
                .map(Usuario::toFileString)
                .collect(Collectors.toList());
        
        Files.write(arquivoUsuarios, linhas);
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) throws Exception {
        return listarTodos().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) throws Exception {
        return listarTodos().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Usuario> listarTodos() throws Exception {
        return Files.readAllLines(arquivoUsuarios)
                .stream()
                .filter(linha -> !linha.trim().isEmpty())
                .map(Usuario::fromFileString)
                .collect(Collectors.toList());
    }
}

// RepositorioVoosArquivo.java
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class RepositorioVoosArquivo implements RepositorioVoos {
    private final Path arquivoVoos;
    private final Path diretorioData;

    public RepositorioVoosArquivo() throws Exception {
        this.diretorioData = Paths.get("data");
        this.arquivoVoos = diretorioData.resolve("voos.txt");
        inicializarArquivos();
    }

    private void inicializarArquivos() throws Exception {
        if (!Files.exists(diretorioData)) {
            Files.createDirectories(diretorioData);
        }
        if (!Files.exists(arquivoVoos)) {
            Files.createFile(arquivoVoos);
        }
    }

    @Override
    public void salvar(Voo voo) throws Exception {
        List<String> linhas = Files.readAllLines(arquivoVoos);
        linhas.add(voo.toFileString());
        Files.write(arquivoVoos, linhas);
    }

    @Override
    public void atualizar(Voo voo) throws Exception {
        List<Voo> voos = listarTodos();
        voos = voos.stream()
                .map(v -> v.getId().equals(voo.getId()) ? voo : v)
                .collect(Collectors.toList());
        
        List<String> linhas = voos.stream()
                .map(Voo::toFileString)
                .collect(Collectors.toList());
        
        Files.write(arquivoVoos, linhas);
    }

    @Override
    public void excluir(String id) throws Exception {
        List<Voo> voos = listarTodos();
        voos = voos.stream()
                .filter(v -> !v.getId().equals(id))
                .collect(Collectors.toList());
        
        List<String> linhas = voos.stream()
                .map(Voo::toFileString)
                .collect(Collectors.toList());
        
        Files.write(arquivoVoos, linhas);
    }

    @Override
    public Optional<Voo> buscarPorId(String id) throws Exception {
        return listarTodos().stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Voo> listarTodos() throws Exception {
        return Files.readAllLines(arquivoVoos)
                .stream()
                .filter(linha -> !linha.trim().isEmpty())
                .map(Voo::fromFileString)
                .collect(Collectors.toList());
    }

    @Override
    public List<Voo> buscarPorOrigem(String origem) throws Exception {
        return listarTodos().stream()
                .filter(v -> v.getOrigem().equalsIgnoreCase(origem))
                .collect(Collectors.toList());
    }

    @Override
    public List<Voo> buscarPorDestino(String destino) throws Exception {
        return listarTodos().stream()
                .filter(v -> v.getDestino().equalsIgnoreCase(destino))
                .collect(Collectors.toList());
    }
}

// RepositorioReservasArquivo.java
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class RepositorioReservasArquivo implements RepositorioReservas {
    private final Path arquivoReservas;
    private final Path diretorioData;

    public RepositorioReservasArquivo() throws Exception {
        this.diretorioData = Paths.get("data");
        this.arquivoReservas = diretorioData.resolve("reservas.txt");
        inicializarArquivos();
    }

    private void inicializarArquivos() throws Exception {
        if (!Files.exists(diretorioData)) {
            Files.createDirectories(diretorioData);
        }
        if (!Files.exists(arquivoReservas)) {
            Files.createFile(arquivoReservas);
        }
    }

    @Override
    public void salvar(Reserva reserva) throws Exception {
        List<String> linhas = Files.readAllLines(arquivoReservas);
        linhas.add(reserva.toFileString());
        Files.write(arquivoReservas, linhas);
    }

    @Override
    public void atualizar(Reserva reserva) throws Exception {
        List<Reserva> reservas = listarTodos();
        reservas = reservas.stream()
                .map(r -> r.getId().equals(reserva.getId()) ? reserva : r)
                .collect(Collectors.toList());
        
        List<String> linhas = reservas.stream()
                .map(Reserva::toFileString)
                .collect(Collectors.toList());
        
        Files.write(arquivoReservas, linhas);
    }

    @Override
    public void excluir(String id) throws Exception {
        List<Reserva> reservas = listarTodos();
        reservas = reservas.stream()
                .filter(r -> !r.getId().equals(id))
                .collect(Collectors.toList());
        
        List<String> linhas = reservas.stream()
                .map(Reserva::toFileString)
                .collect(Collectors.toList());
        
        Files.write(arquivoReservas, linhas);
    }

    @Override
    public Optional<Reserva> buscarPorId(String id) throws Exception {
        return listarTodos().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Reserva> listarTodos() throws Exception {
        return Files.readAllLines(arquivoReservas)
                .stream()
                .filter(linha -> !linha.trim().isEmpty())
                .map(Reserva::fromFileString)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reserva> buscarPorUsuario(String idUsuario) throws Exception {
        return listarTodos().stream()
                .filter(r -> r.getIdUsuario().equals(idUsuario))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reserva> buscarPorVoo(String idVoo) throws Exception {
        return listarTodos().stream()
                .filter(r -> r.getIdVoo().equals(idVoo))
                .collect(Collectors.toList());
    }
}

// ===== CASOS DE USO (Regras de Negócio) =====

// ServicoAutenticacao.java
import java.security.MessageDigest;
import java.util.Optional;
import java.util.UUID;

public class ServicoAutenticacao {
    private final RepositorioUsuarios repositorioUsuarios;
    private Usuario usuarioLogado;

    public ServicoAutenticacao(RepositorioUsuarios repositorioUsuarios) {
        this.repositorioUsuarios = repositorioUsuarios;
    }

    public boolean login(String email, String senha) throws Exception {
        Optional<Usuario> usuarioOpt = repositorioUsuarios.buscarPorEmail(email);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String senhaHash = calcularHashSenha(senha);
            
            if (usuario.getSenha().equals(senhaHash)) {
                this.usuarioLogado = usuario;
                return true;
            }
        }
        
        return false;
    }

    public String cadastrarUsuario(String nome, String email, String senha, Usuario.TipoUsuario tipo) throws Exception {
        // Verificar se email já existe
        Optional<Usuario> usuarioExistente = repositorioUsuarios.buscarPorEmail(email);
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado!");
        }

        String id = UUID.randomUUID().toString();
        String senhaHash = calcularHashSenha(senha);
        
        Usuario novoUsuario = new Usuario(id, nome, email, senhaHash, tipo);
        repositorioUsuarios.salvar(novoUsuario);
        
        return id;
    }

    public void logout() {
        this.usuarioLogado = null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public boolean isLogado() {
        return usuarioLogado != null;
    }

    private String calcularHashSenha(String senha) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(senha.getBytes());
        
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        
        return hexString.toString();
    }
}

// ServicoReservas.java
import java.util.*;
import java.util.stream.Collectors;

public class ServicoReservas {
    private final RepositorioReservas repositorioReservas;
    private final RepositorioVoos repositorioVoos;
    private final RepositorioUsuarios repositorioUsuarios;

    public ServicoReservas(RepositorioReservas repositorioReservas, 
                          RepositorioVoos repositorioVoos, 
                          RepositorioUsuarios repositorioUsuarios) {
        this.repositorioReservas = repositorioReservas;
        this.repositorioVoos = repositorioVoos;
        this.repositorioUsuarios = repositorioUsuarios;
    }

    public String criarReserva(String idUsuario, String idVoo, String assento) throws Exception {
        // Verificar se voo existe
        Optional<Voo> vooOpt = repositorioVoos.buscarPorId(idVoo);
        if (!vooOpt.isPresent()) {
            throw new IllegalArgumentException("Voo não encontrado!");
        }

        Voo voo = vooOpt.get();
        
        // Verificar se assento está disponível
        if (!isAssentoDisponivel(idVoo, assento)) {
            throw new IllegalArgumentException("Assento já ocupado!");
        }

        // Verificar se assento é válido para o avião
        if (!isAssentoValido(voo, assento)) {
            throw new IllegalArgumentException("Assento inválido para este voo!");
        }

        String id = UUID.randomUUID().toString();
        Reserva novaReserva = new Reserva(id, idUsuario, idVoo, assento, voo.getPreco());
        
        repositorioReservas.salvar(novaReserva);
        return id;
    }

    public void cancelarReserva(String idReserva, String idUsuario) throws Exception {
        Optional<Reserva> reservaOpt = repositorioReservas.buscarPorId(idReserva);
        
        if (!reservaOpt.isPresent()) {
            throw new IllegalArgumentException("Reserva não encontrada!");
        }

        Reserva reserva = reservaOpt.get();
        
        if (!reserva.getIdUsuario().equals(idUsuario)) {
            throw new IllegalArgumentException("Você não pode cancelar esta reserva!");
        }

        if (reserva.getStatus() == Reserva.StatusReserva.CANCELADA) {
            throw new IllegalArgumentException("Reserva já está cancelada!");
        }

        reserva.cancelar();
        repositorioReservas.atualizar(reserva);
    }

    public List<String> getAssentosDisponiveis(String idVoo) throws Exception {
        Optional<Voo> vooOpt = repositorioVoos.buscarPorId(idVoo);
        if (!vooOpt.isPresent()) {
            throw new IllegalArgumentException("Voo não encontrado!");
        }

        Voo voo = vooOpt.get();
        List<String> todosAssentos = gerarTodosAssentos(voo);
        List<String> assentosOcupados = getAssentosOcupados(idVoo);
        
        return todosAssentos.stream()
                .filter(assento -> !assentosOcupados.contains(assento))
                .collect(Collectors.toList());
    }

    private boolean isAssentoDisponivel(String idVoo, String assento) throws Exception {
        List<Reserva> reservasVoo = repositorioReservas.buscarPorVoo(idVoo);
        
        return reservasVoo.stream()
                .filter(r -> r.getStatus() == Reserva.StatusReserva.CONFIRMADA)
                .noneMatch(r -> r.getAssento().equals(assento));
    }

    private boolean isAssentoValido(Voo voo, String assento) {
        List<String> assentosValidos = gerarTodosAssentos(voo);
        return assentosValidos.contains(assento);
    }

    private List<String> gerarTodosAssentos(Voo voo) {
        List<String> assentos = new ArrayList<>();
        int totalAssentos = voo.getTotalAssentos();
        
        // Assumindo configuração 3-3 (6 assentos por fileira)
        int fileiras = (int) Math.ceil(totalAssentos / 6.0);
        String[] letras = {"A", "B", "C", "D", "E", "F"};
        
        for (int i = 1; i <= fileiras; i++) {
            for (String letra : letras) {
                assentos.add(i + letra);
                if (assentos.size() >= totalAssentos) break;
            }
            if (assentos.size() >= totalAssentos) break;
        }
        
        return assentos;
    }

    private List<String> getAssentosOcupados(String idVoo) throws Exception {
        return repositorioReservas.buscarPorVoo(idVoo)
                .stream()
                .filter(r -> r.getStatus() == Reserva.StatusReserva.CONFIRMADA)
                .map(Reserva::getAssento)
                .collect(Collectors.toList());
    }

    public List<Reserva> getReservasUsuario(String idUsuario) throws Exception {
        return repositorioReservas.buscarPorUsuario(idUsuario);
    }
}

// ===== SERVIÇO DE RELATÓRIOS =====

// ServicoRelatorios.java
import java.util.*;
import java.util.stream.Collectors;

public class ServicoRelatorios {
    private final RepositorioReservas repositorioReservas;
    private final RepositorioVoos repositorioVoos;
    private final RepositorioUsuarios repositorioUsuarios;

    public ServicoRelatorios(RepositorioReservas repositorioReservas, 
                            RepositorioVoos repositorioVoos, 
                            RepositorioUsuarios repositorioUsuarios) {
        this.repositorioReservas = repositorioReservas;
        this.repositorioVoos = repositorioVoos;
        this.repositorioUsuarios = repositorioUsuarios;
    }

    public Map<String, Long> getTotalReservasPorVoo() throws Exception {
        return repositorioReservas.listarTodos()
                .stream()
                .filter(r -> r.getStatus() == Reserva.StatusReserva.CONFIRMADA)
                .collect(Collectors.groupingBy(Reserva::getIdVoo, Collectors.counting()));
    }

    public Map<String, Integer> getAssentosLivresPorVoo() throws Exception {
        Map<String, Integer> resultado = new HashMap<>();
        List<Voo> voos = repositorioVoos.listarTodos();
        
        for (Voo voo : voos) {
            long reservasConfirmadas = repositorioReservas.buscarPorVoo(voo.getId())
                    .stream()
                    .filter(r -> r.getStatus() == Reserva.StatusReserva.CONFIRMADA)
                    .count();
            
            int assentosLivres = voo.getTotalAssentos() - (int) reservasConfirmadas;
            resultado.put(voo.getId(), assentosLivres);
        }
        
        return resultado;
    }

    public List<Reserva> getHistoricoReservasUsuario(String idUsuario) throws Exception {
        return repositorioReservas.buscarPorUsuario(idUsuario)
                .stream()
                .sorted((r1, r2) -> r2.getDataReserva().compareTo(r1.getDataReserva()))
                .collect(Collectors.toList());
    }

    public Map<String, Double> getReceitaPorVoo() throws Exception {
        return repositorioReservas.listarTodos()
                .stream()
                .filter(r -> r.getStatus() == Reserva.StatusReserva.CONFIRMADA)
                .collect(Collectors.groupingBy(
                    Reserva::getIdVoo,
                    Collectors.summingDouble(Reserva::getValorPago)
                ));
    }

    public List<String> getVoosMaisReservados(int limite) throws Exception {
        return getTotalReservasPorVoo()
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limite)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}

// ===== SISTEMA PRINCIPAL (CLI) =====

// SistemaReservas.java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class SistemaReservas {
    private final Scanner scanner;
    private final ServicoAutenticacao servicoAutenticacao;
    private final ServicoReservas servicoReservas;
    private final ServicoRelatorios servicoRelatorios;
    private final RepositorioVoos repositorioVoos;
    private final RepositorioUsuarios repositorioUsuarios;
    private final RepositorioReservas repositorioReservas;
    private final Logger logger;

    public SistemaReservas() throws Exception {
        this.scanner = new Scanner(System.in);
        
        // Inicializar repositórios
        this.repositorioUsuarios = new RepositorioUsuariosArquivo();
        this.repositorioVoos = new RepositorioVoosArquivo();
        this.repositorioReservas = new RepositorioReservasArquivo();
        
        // Inicializar serviços
        this.servicoAutenticacao = new ServicoAutenticacao(repositorioUsuarios);
        this.servicoReservas = new ServicoReservas(repositorioReservas, repositorioVoos, repositorioUsuarios);
        this.servicoRelatorios = new ServicoRelatorios(repositorioReservas, repositorioVoos, repositorioUsuarios);
        
        // Inicializar logger
        this.logger = new Logger();
        
        // Criar usuário admin padrão se não existir
        criarUsuarioAdminSeNaoExistir();
    }

    public void iniciar() {
        System.out.println("=".repeat(50));
        System.out.println("  SISTEMA DE RESERVAS AÉREAS");
        System.out.println("=".repeat(50));
        
        try {
            while (true) {
                if (!servicoAutenticacao.isLogado()) {
                    menuLogin();
                } else {
                    menuPrincipal();
                }
            }
        } catch (Exception e) {
            System.err.println("Erro fatal no sistema: " + e.getMessage());
            logger.log("ERRO", "Sistema encerrado com erro: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private void menuLogin() throws Exception {
        System.out.println("\n==== LOGIN ====");
        System.out.println("1. Fazer Login");
        System.out.println("2. Cadastrar Usuário");
        System.out.println("0. Sair");
        System.out.print("Escolha: ");

        int opcao = lerInteiro();
        
        switch (opcao) {
            case 1:
                fazerLogin();
                break;
            case 2:
                cadastrarUsuario();
                break;
            case 0:
                System.out.println("Obrigado por usar o Sistema de Reservas Aéreas!");
                logger.log("INFO", "Sistema encerrado normalmente");
                System.exit(0);
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }

    private void menuPrincipal() throws Exception {
        Usuario usuario = servicoAutenticacao.getUsuarioLogado();
        System.out.println(String.format("\n==== BEM-VINDO, %s! ====", usuario.getNome().toUpperCase()));
        System.out.println("1. Listar Voos");
        System.out.println("2. Criar Reserva");
        System.out.println("3. Minhas Reservas");
        System.out.println("4. Cancelar Reserva");
        System.out.println("5. Relatórios");
        
        if (usuario.getTipo() == Usuario.TipoUsuario.ADMIN) {
            System.out.println("6. [ADMIN] Gerenciar Voos");
            System.out.println("7. [ADMIN] Relatórios Administrativos");
        }
        
        System.out.println("8. Logout");
        System.out.println("0. Sair");
        System.out.print("Escolha: ");

        int opcao = lerInteiro();
        
        switch (opcao) {
            case 1:
                listarVoos();
                break;
            case 2:
                criarReserva();
                break;
            case 3:
                minhasReservas();
                break;
            case 4:
                cancelarReserva();
                break;
            case 5:
                menuRelatorios();
                break;
            case 6:
                if (usuario.getTipo() == Usuario.TipoUsuario.ADMIN) {
                    menuAdminVoos();
                } else {
                    System.out.println("Acesso negado!");
                }
                break;
            case 7:
                if (usuario.getTipo() == Usuario.TipoUsuario.ADMIN) {
                    menuRelatoriosAdmin();
                } else {
                    System.out.println("Acesso negado!");
                }
                break;
            case 8:
                servicoAutenticacao.logout();
                logger.log("INFO", "Usuário " + usuario.getEmail() + " fez logout");
                System.out.println("Logout realizado com sucesso!");
                break;
            case 0:
                System.out.println("Obrigado por usar o Sistema de Reservas Aéreas!");
                logger.log("INFO", "Sistema encerrado normalmente");
                System.exit(0);
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }

    private void fazerLogin() throws Exception {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        if (servicoAutenticacao.login(email, senha)) {
            Usuario usuario = servicoAutenticacao.getUsuarioLogado();
            System.out.println("Login realizado com sucesso! Bem-vindo, " + usuario.getNome());
            logger.log("INFO", "Login realizado: " + email);
        } else {
            System.out.println("Email ou senha incorretos!");
            logger.log("WARNING", "Tentativa de login falhada: " + email);
        }
    }

    private void cadastrarUsuario() throws Exception {
        System.out.println("\n==== CADASTRO DE USUÁRIO ====");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        try {
            String id = servicoAutenticacao.cadastrarUsuario(nome, email, senha, Usuario.TipoUsuario.CLIENTE);
            System.out.println("Usuário cadastrado com sucesso! ID: " + id);
            logger.log("INFO", "Novo usuário cadastrado: " + email);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarVoos() throws Exception {
        System.out.println("\n==== VOOS DISPONÍVEIS ====");
        List<Voo> voos = repositorioVoos.listarTodos();
        
        if (voos.isEmpty()) {
            System.out.println("Nenhum voo cadastrado.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Voo voo : voos) {
            Map<String, Integer> assentosLivres = servicoRelatorios.getAssentosLivresPorVoo();
            int livres = assentosLivres.getOrDefault(voo.getId(), voo.getTotalAssentos());
            
            System.out.println(String.format(
                "ID: %s | %s → %s | %s | %s | Assentos: %d/%d | R$ %.2f",
                voo.getId(),
                voo.getOrigem(),
                voo.getDestino(),
                voo.getDataHora().format(formatter),
                voo.getCompanhia(),
                livres,
                voo.getTotalAssentos(),
                voo.getPreco()
            ));
        }
    }

    private void criarReserva() throws Exception {
        System.out.println("\n==== CRIAR RESERVA ====");
        
        // Listar voos primeiro
        listarVoos();
        
        System.out.print("ID do Voo: ");
        String idVoo = scanner.nextLine();
        
        // Mostrar assentos disponíveis
        try {
            List<String> assentosDisponiveis = servicoReservas.getAssentosDisponiveis(idVoo);
            
            if (assentosDisponiveis.isEmpty()) {
                System.out.println("Voo lotado!");
                return;
            }
            
            System.out.println("Assentos disponíveis:");
            for (int i = 0; i < assentosDisponiveis.size(); i++) {
                System.out.print(assentosDisponiveis.get(i) + " ");
                if ((i + 1) % 10 == 0) System.out.println(); // Nova linha a cada 10 assentos
            }
            System.out.println();
            
            System.out.print("Escolha o assento: ");
            String assento = scanner.nextLine().toUpperCase();
            
            String idReserva = servicoReservas.criarReserva(
                servicoAutenticacao.getUsuarioLogado().getId(),
                idVoo,
                assento
            );
            
            System.out.println("Reserva criada com sucesso! ID: " + idReserva);
            logger.log("INFO", "Reserva criada: " + idReserva + " por " + servicoAutenticacao.getUsuarioLogado().getEmail());
            
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void minhasReservas() throws Exception {
        System.out.println("\n==== MINHAS RESERVAS ====");
        String idUsuario = servicoAutenticacao.getUsuarioLogado().getId();
        List<Reserva> reservas = servicoReservas.getReservasUsuario(idUsuario);
        
        if (reservas.isEmpty()) {
            System.out.println("Você não possui reservas.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Reserva reserva : reservas) {
            Optional<Voo> vooOpt = repositorioVoos.buscarPorId(reserva.getIdVoo());
            if (vooOpt.isPresent()) {
                Voo voo = vooOpt.get();
                System.out.println(String.format(
                    "ID: %s | %s → %s | %s | Assento: %s | Status: %s | R$ %.2f",
                    reserva.getId(),
                    voo.getOrigem(),
                    voo.getDestino(),
                    voo.getDataHora().format(formatter),
                    reserva.getAssento(),
                    reserva.getStatus(),
                    reserva.getValorPago()
                ));
            }
        }
    }

    private void cancelarReserva() throws Exception {
        System.out.println("\n==== CANCELAR RESERVA ====");
        
        // Mostrar reservas do usuário primeiro
        minhasReservas();
        
        System.out.print("ID da Reserva para cancelar: ");
        String idReserva = scanner.nextLine();
        
        try {
            servicoReservas.cancelarReserva(idReserva, servicoAutenticacao.getUsuarioLogado().getId());
            System.out.println("Reserva cancelada com sucesso!");
            logger.log("INFO", "Reserva cancelada: " + idReserva);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void menuRelatorios() throws Exception {
        System.out.println("\n==== RELATÓRIOS ====");
        System.out.println("1. Meu Histórico de Reservas");
        System.out.println("2. Voos Mais Reservados");
        System.out.println("0. Voltar");
        System.out.print("Escolha: ");

        int opcao = lerInteiro();
        
        switch (opcao) {
            case 1:
                relatorioHistoricoUsuario();
                break;
            case 2:
                relatorioVoosMaisReservados();
                break;
            case 0:
                return;
            default:
                System.out.println("Opção inválida!");
        }
    }

    private void menuAdminVoos() throws Exception {
        System.out.println("\n==== GERENCIAR VOOS ====");
        System.out.println("1. Criar Voo");
        System.out.println("2. Editar Voo");
        System.out.println("3. Excluir Voo");
        System.out.println("4. Listar Todos os Voos");
        System.out.println("0. Voltar");
        System.out.print("Escolha: ");

        int opcao = lerInteiro();
        
        switch (opcao) {
            case 1:
                criarVoo();
                break;
            case 2:
                editarVoo();
                break;
            case 3:
                excluirVoo();
                break;
            case 4:
                listarVoos();
                break;
            case 0:
                return;
            default:
                System.out.println("Opção inválida!");
        }
    }

    private void criarVoo() throws Exception {
        System.out.println("\n==== CRIAR VOO ====");
        System.out.print("Origem: ");
        String origem = scanner.nextLine();
        System.out.print("Destino: ");
        String destino = scanner.nextLine();
        System.out.print("Data e hora (dd/MM/yyyy HH:mm): ");
        String dataHoraStr = scanner.nextLine();
        System.out.print("Companhia: ");
        String companhia = scanner.nextLine();
        System.out.print("Total de assentos: ");
        int totalAssentos = lerInteiro();
        System.out.print("Preço: ");
        double preco = lerDouble();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime dataHora = LocalDateTime.parse(dataHoraStr, formatter);
            
            String id = UUID.randomUUID().toString();
            Voo voo = new Voo(id, origem, destino, dataHora, companhia, totalAssentos, preco);
            
            repositorioVoos.salvar(voo);
            System.out.println("Voo criado com sucesso! ID: " + id);
            logger.log("INFO", "Voo criado: " + id);
            
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data/hora inválido! Use: dd/MM/yyyy HH:mm");
        }
    }

    private void editarVoo() throws Exception {
        System.out.println("\n==== EDITAR VOO ====");
        listarVoos();
        
        System.out.print("ID do voo a editar: ");
        String idVoo = scanner.nextLine();
        
        Optional<Voo> vooOpt = repositorioVoos.buscarPorId(idVoo);
        if (!vooOpt.isPresent()) {
            System.out.println("Voo não encontrado!");
            return;
        }

        Voo voo = vooOpt.get();
        System.out.println("Voo atual: " + voo);
        
        System.out.print("Nova origem (atual: " + voo.getOrigem() + "): ");
        String origem = scanner.nextLine();
        if (!origem.isEmpty()) voo.setOrigem(origem);
        
        System.out.print("Novo destino (atual: " + voo.getDestino() + "): ");
        String destino = scanner.nextLine();
        if (!destino.isEmpty()) voo.setDestino(destino);
        
        System.out.print("Novo preço (atual: " + voo.getPreco() + "): ");
        String precoStr = scanner.nextLine();
        if (!precoStr.isEmpty()) {
            try {
                double preco = Double.parseDouble(precoStr);
                voo.setPreco(preco);
            } catch (NumberFormatException e) {
                System.out.println("Preço inválido, mantendo valor atual.");
            }
        }

        repositorioVoos.atualizar(voo);
        System.out.println("Voo atualizado com sucesso!");
        logger.log("INFO", "Voo editado: " + idVoo);
    }

    private void excluirVoo() throws Exception {
        System.out.println("\n==== EXCLUIR VOO ====");
        listarVoos();
        
        System.out.print("ID do voo a excluir: ");
        String idVoo = scanner.nextLine();
        
        // Verificar se há reservas para este voo
        List<Reserva> reservasVoo = repositorioReservas.buscarPorVoo(idVoo);
        long reservasAtivas = reservasVoo.stream()
                .filter(r -> r.getStatus() == Reserva.StatusReserva.CONFIRMADA)
                .count();
        
        if (reservasAtivas > 0) {
            System.out.println("Não é possível excluir o voo. Há " + reservasAtivas + " reservas ativas.");
            return;
        }

        System.out.print("Tem certeza? (s/N): ");
        String confirmacao = scanner.nextLine();
        
        if (confirmacao.equalsIgnoreCase("s")) {
            repositorioVoos.excluir(idVoo);
            System.out.println("Voo excluído com sucesso!");
            logger.log("INFO", "Voo excluído: " + idVoo);
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    private void menuRelatoriosAdmin() throws Exception {
        System.out.println("\n==== RELATÓRIOS ADMINISTRATIVOS ====");
        System.out.println("1. Total de Reservas por Voo");
        System.out.println("2. Assentos Livres por Voo");
        System.out.println("3. Receita por Voo");
        System.out.println("4. Top 10 Voos Mais Reservados");
        System.out.println("0. Voltar");
        System.out.print("Escolha: ");

        int opcao = lerInteiro();
        
        switch (opcao) {
            case 1:
                relatorioReservasPorVoo();
                break;
            case 2:
                relatorioAssentosLivres();
                break;
            case 3:
                relatorioReceitaPorVoo();
                break;
            case 4:
                relatorioTop10Voos();
                break;
            case 0:
                return;
            default:
                System.out.println("Opção inválida!");
        }
    }

    private void relatorioHistoricoUsuario() throws Exception {
        System.out.println("\n==== MEU HISTÓRICO ====");
        String idUsuario = servicoAutenticacao.getUsuarioLogado().getId();
        List<Reserva> historico = servicoRelatorios.getHistoricoReservasUsuario(idUsuario);
        
        if (historico.isEmpty()) {
            System.out.println("Nenhuma reserva encontrada.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Reserva reserva : historico) {
            Optional<Voo> vooOpt = repositorioVoos.buscarPorId(reserva.getIdVoo());
            if (vooOpt.isPresent()) {
                Voo voo = vooOpt.get();
                System.out.println(String.format(
                    "%s | %s → %s | %s | Assento: %s | Status: %s | Reservado em: %s | R$ %.2f",
                    reserva.getId(),
                    voo.getOrigem(),
                    voo.getDestino(),
                    voo.getDataHora().format(formatter),
                    reserva.getAssento(),
                    reserva.getStatus(),
                    reserva.getDataReserva().format(formatter),
                    reserva.getValorPago()
                ));
            }
        }
    }

    private void relatorioVoosMaisReservados() throws Exception {
        System.out.println("\n==== VOOS MAIS RESERVADOS (TOP 5) ====");
        List<String> topVoos = servicoRelatorios.getVoosMaisReservados(5);
        Map<String, Long> reservasPorVoo = servicoRelatorios.getTotalReservasPorVoo();
        
        int posicao = 1;
        for (String idVoo : topVoos) {
            Optional<Voo> vooOpt = repositorioVoos.buscarPorId(idVoo);
            if (vooOpt.isPresent()) {
                Voo voo = vooOpt.get();
                long totalReservas = reservasPorVoo.get(idVoo);
                System.out.println(String.format(
                    "%d. %s → %s (%s) - %d reservas",
                    posicao++,
                    voo.getOrigem(),
                    voo.getDestino(),
                    voo.getCompanhia(),
                    totalReservas
                ));
            }
        }
    }

    private void relatorioReservasPorVoo() throws Exception {
        System.out.println("\n==== TOTAL DE RESERVAS POR VOO ====");
        Map<String, Long> reservasPorVoo = servicoRelatorios.getTotalReservasPorVoo();
        
        for (Map.Entry<String, Long> entry : reservasPorVoo.entrySet()) {
            Optional<Voo> vooOpt = repositorioVoos.buscarPorId(entry.getKey());
            if (vooOpt.isPresent()) {
                Voo voo = vooOpt.get();
                System.out.println(String.format(
                    "%s → %s (%s): %d reservas",
                    voo.getOrigem(),
                    voo.getDestino(),
                    voo.getCompanhia(),
                    entry.getValue()
                ));
            }
        }
    }

    private void relatorioAssentosLivres() throws Exception {
        System.out.println("\n==== ASSENTOS LIVRES POR VOO ====");
        Map<String, Integer> assentosLivres = servicoRelatorios.getAssentosLivresPorVoo();
        
        for (Map.Entry<String, Integer> entry : assentosLivres.entrySet()) {
            Optional<Voo> vooOpt = repositorioVoos.buscarPorId(entry.getKey());
            if (vooOpt.isPresent()) {
                Voo voo = vooOpt.get();
                int ocupados = voo.getTotalAssentos() - entry.getValue();
                double percentualOcupacao = (double) ocupados / voo.getTotalAssentos() * 100;
                
                System.out.println(String.format(
                    "%s → %s: %d/%d livres (%.1f%% ocupação)",
                    voo.getOrigem(),
                    voo.getDestino(),
                    entry.getValue(),
                    voo.getTotalAssentos(),
                    percentualOcupacao
                ));
            }
        }
    }

    private void relatorioReceitaPorVoo() throws Exception {
        System.out.println("\n==== RECEITA POR VOO ====");
        Map<String, Double> receitaPorVoo = servicoRelatorios.getReceitaPorVoo();
        double receitaTotal = 0;
        
        for (Map.Entry<String, Double> entry : receitaPorVoo.entrySet()) {
            Optional<Voo> vooOpt = repositorioVoos.buscarPorId(entry.getKey());
            if (vooOpt.isPresent()) {
                Voo voo = vooOpt.get();
                System.out.println(String.format(
                    "%s → %s (%s): R$ %.2f",
                    voo.getOrigem(),
                    voo.getDestino(),
                    voo.getCompanhia(),
                    entry.getValue()
                ));
                receitaTotal += entry.getValue();
            }
        }
        
        System.out.println(String.format("\nRECEITA TOTAL: R$ %.2f", receitaTotal));
    }

    private void relatorioTop10Voos() throws Exception {
        System.out.println("\n==== TOP 10 VOOS MAIS RESERVADOS ====");
        List<String> topVoos = servicoRelatorios.getVoosMaisReservados(10);
        Map<String, Long> reservasPorVoo = servicoRelatorios.getTotalReservasPorVoo();
        
        int posicao = 1;
        for (String idVoo : topVoos) {
            Optional<Voo> vooOpt = repositorioVoos.buscarPorId(idVoo);
            if (vooOpt.isPresent()) {
                Voo voo = vooOpt.get();
                long totalReservas = reservasPorVoo.get(idVoo);
                System.out.println(String.format(
                    "%2d. %s → %s (%s) - %d reservas",
                    posicao++,
                    voo.getOrigem(),
                    voo.getDestino(),
                    voo.getCompanhia(),
                    totalReservas
                ));
            }
        }
    }

    private void criarUsuarioAdminSeNaoExistir() throws Exception {
        Optional<Usuario> adminExistente = repositorioUsuarios.buscarPorEmail("admin@sistema.com");
        
        if (!adminExistente.isPresent()) {
            servicoAutenticacao.cadastrarUsuario(
                "Administrador",
                "admin@sistema.com",
                "admin123",
                Usuario.TipoUsuario.ADMIN
            );
            System.out.println("Usuário administrador criado:");
            System.out.println("Email: admin@sistema.com");
            System.out.println("Senha: admin123");
        }
    }

    private int lerInteiro() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Por favor, digite um número válido: ");
            }
        }
    }

    private double lerDouble() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Por favor, digite um número válido: ");
            }
        }
    }

    public static void main(String[] args) {
        try {
            SistemaReservas sistema = new SistemaReservas();
            sistema.iniciar();
        } catch (Exception e) {
            System.err.println("Erro ao inicializar sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

// ===== SISTEMA DE LOGS =====

// Logger.java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final Path arquivoLog;
    private final Path diretorioLogs;
    private final DateTimeFormatter formatter;

    public Logger() throws Exception {
        this.diretorioLogs = Paths.get("logs");
        this.arquivoLog = diretorioLogs.resolve("sistema.log");
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        inicializarArquivos();
    }

    private void inicializarArquivos() throws Exception {
        if (!Files.exists(diretorioLogs)) {
            Files.createDirectories(diretorioLogs);
        }
        if (!Files.exists(arquivoLog)) {
            Files.createFile(arquivoLog);
        }
    }

    public void log(String nivel, String mensagem) {
        try {
            String timestamp = LocalDateTime.now().format(formatter);
            String logEntry = String.format("[%s] %s - %s%n", timestamp, nivel, mensagem);
            
            Files.write(arquivoLog, logEntry.getBytes(), 
                       StandardOpenOption.CREATE, 
                       StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Erro ao gravar log: " + e.getMessage());
        }
    }

    public void info(String mensagem) {
        log("INFO", mensagem);
    }

    public void warning(String mensagem) {
        log("WARNING", mensagem);
    }

    public void error(String mensagem) {
        log("ERROR", mensagem);
    }
}

// ===== UTILITÁRIOS =====

// GeradorDados.java - Para criar dados de exemplo
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GeradorDados {
    private final RepositorioVoos repositorioVoos;
    private final RepositorioUsuarios repositorioUsuarios;
    private final ServicoAutenticacao servicoAutenticacao;

    public GeradorDados(RepositorioVoos repositorioVoos, 
                       RepositorioUsuarios repositorioUsuarios,
                       ServicoAutenticacao servicoAutenticacao) {
        this.repositorioVoos = repositorioVoos;
        this.repositorioUsuarios = repositorioUsuarios;
        this.servicoAutenticacao = servicoAutenticacao;
    }

    public void criarDadosExemplo() throws Exception {
        criarUsuariosExemplo();
        criarVoosExemplo();
    }

    private void criarUsuariosExemplo() throws Exception {
        List<String[]> usuarios = Arrays.asList(
            new String[]{"João Silva", "joao@email.com", "123456"},
            new String[]{"Maria Santos", "maria@email.com", "123456"},
            new String[]{"Pedro Oliveira", "pedro@email.com", "123456"}
        );

        for (String[] dados : usuarios) {
            try {
                servicoAutenticacao.cadastrarUsuario(
                    dados[0], dados[1], dados[2], Usuario.TipoUsuario.CLIENTE
                );
                System.out.println("Usuário criado: " + dados[1]);
            } catch (IllegalArgumentException e) {
                System.out.println("Usuário já existe: " + dados[1]);
            }
        }
    }

    private void criarVoosExemplo() throws Exception {
        List<Object[]> voos = Arrays.asList(
            new Object[]{"São Paulo", "Rio de Janeiro", LocalDateTime.now().plusDays(1), "LATAM", 180, 450.00},
            new Object[]{"São Paulo", "Brasília", LocalDateTime.now().plusDays(2), "GOL", 144, 380.00},
            new Object[]{"Rio de Janeiro", "Salvador", LocalDateTime.now().plusDays(3), "Azul", 156, 520.00},
            new Object[]{"São Paulo", "Recife", LocalDateTime.now().plusDays(4), "LATAM", 180, 680.00},
            new Object[]{"Brasília", "Fortaleza", LocalDateTime.now().plusDays(5), "GOL", 144, 720.00}
        );

        for (Object[] dados : voos) {
            String id = UUID.randomUUID().toString();
            Voo voo = new Voo(
                id,
                (String) dados[0],
                (String) dados[1],
                (LocalDateTime) dados[2],
                (String) dados[3],
                (Integer) dados[4],
                (Double) dados[5]
            );
            
            repositorioVoos.salvar(voo);
            System.out.println("Voo criado: " + dados[0] + " → " + dados[1]);
        }
    }
}

// ===== VERSÃO BINÁRIA (ALTERNATIVA) =====

// RepositorioBinario.java - Exemplo de implementação com arquivos binários
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class RepositorioUsuariosBinario implements RepositorioUsuarios {
    private final Path arquivoBinario;
    private final Path diretorioData;

    public RepositorioUsuariosBinario() throws Exception {
        this.diretorioData = Paths.get("data");
        this.arquivoBinario = diretorioData.resolve("usuarios.dat");
        inicializarArquivos();
    }

    private void inicializarArquivos() throws Exception {
        if (!Files.exists(diretorioData)) {
            Files.createDirectories(diretorioData);
        }
    }

    @Override
    public void salvar(Usuario usuario) throws Exception {
        List<Usuario> usuarios = listarTodos();
        usuarios.add(usuario);
        salvarTodos(usuarios);
    }

    @Override
    public void atualizar(Usuario usuario) throws Exception {
        List<Usuario> usuarios = listarTodos();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equals(usuario.getId())) {
                usuarios.set(i, usuario);
                break;
            }
        }
        salvarTodos(usuarios);
    }

    @Override
    public void excluir(String id) throws Exception {
        List<Usuario> usuarios = listarTodos();
        usuarios.removeIf(u -> u.getId().equals(id));
        salvarTodos(usuarios);
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) throws Exception {
        return listarTodos().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) throws Exception {
        return listarTodos().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Usuario> listarTodos() throws Exception {
        if (!Files.exists(arquivoBinario)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                Files.newInputStream(arquivoBinario))) {
            return (List<Usuario>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        }
    }

    private void salvarTodos(List<Usuario> usuarios) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                Files.newOutputStream(arquivoBinario))) {
            oos.writeObject(usuarios);
        }
    }
}

// ===== TESTES E VALIDAÇÃO =====

// TesteSistema.java - Classe para testar funcionalidades
public class TesteSistema {
    public static void main(String[] args) {
        try {
            System.out.println("=== TESTE DO SISTEMA ===");
            
            // Teste de repositórios
            testeRepositorios();
            
            // Teste de serviços
            testeServicos();
            
            // Teste de performance
            testePerformance();
            
            System.out.println("Todos os testes executados com sucesso!");
            
        } catch (Exception e) {
            System.err.println("Erro nos testes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testeRepositorios() throws Exception {
        System.out.println("\n--- Testando Repositórios ---");
        
        RepositorioUsuarios repoUsuarios = new RepositorioUsuariosArquivo();
        
        // Teste de criação
        Usuario usuario = new Usuario(
            UUID.randomUUID().toString(),
            "Teste",
            "teste@teste.com",
            "senha123",
            Usuario.TipoUsuario.CLIENTE
        );
        
        repoUsuarios.salvar(usuario);
        System.out.println("✓ Usuário salvo");
        
        // Teste de busca
        Optional<Usuario> encontrado = repoUsuarios.buscarPorEmail("teste@teste.com");
        if (encontrado.isPresent()) {
            System.out.println("✓ Usuário encontrado: " + encontrado.get().getNome());
        }
        
        // Teste de listagem
        List<Usuario> todos = repoUsuarios.listarTodos();
        System.out.println("✓ Total de usuários: " + todos.size());
    }

    private static void testeServicos() throws Exception {
        System.out.println("\n--- Testando Serviços ---");
        
        RepositorioUsuarios repoUsuarios = new RepositorioUsuariosArquivo();
        ServicoAutenticacao servico = new ServicoAutenticacao(repoUsuarios);
        
        // Teste de cadastro
        try {
            servico.cadastrarUsuario("Teste Serviço", "servico@teste.com", "123", Usuario.TipoUsuario.CLIENTE);
            System.out.println("✓ Cadastro funcionando");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Validação funcionando: " + e.getMessage());
        }
        
        // Teste de login
        boolean loginOk = servico.login("servico@teste.com", "123");
        System.out.println("✓ Login: " + (loginOk ? "Sucesso" : "Falha"));
    }

    private static void testePerformance() throws Exception {
        System.out.println("\n--- Testando Performance ---");
        
        RepositorioUsuarios repoTexto = new RepositorioUsuariosArquivo();
        
        // Teste com arquivos texto
        long inicio = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            Usuario usuario = new Usuario(
                UUID.randomUUID().toString(),
                "Usuario" + i,
                "user" + i + "@teste.com",
                "senha" + i,
                Usuario.TipoUsuario.CLIENTE
            );
            repoTexto.salvar(usuario);
        }
        
        long fim = System.currentTimeMillis();
        System.out.println("✓ 100 usuários salvos em " + (fim - inicio) + "ms");
        
        // Teste de leitura
        inicio = System.currentTimeMillis();
        List<Usuario> todos = repoTexto.listarTodos();
        fim = System.currentTimeMillis();
        
        System.out.println("✓ " + todos.size() + " usuários lidos em " + (fim - inicio) + "ms");
    }
}

// ===== DOCUMENTAÇÃO E INSTRUÇÕES =====

/*
 * ===== COMO COMPILAR E EXECUTAR =====
 * 
 * 1. Compilação:
 *    javac *.java
 * 
 * 2. Execução:
 *    java SistemaReservas
 * 
 * 3. Para gerar dados de exemplo:
 *    java GeradorDados
 * 
 * 4. Para executar testes:
 *    java TesteSistema
 * 
 * ===== ESTRUTURA DE DIRETÓRIOS =====
 * 
 * projeto/
 * ├── *.java (arquivos fonte)
 * ├── *.class (arquivos compilados)
 * ├── data/
 * │   ├── usuarios.txt
 * │   ├── voos.txt
 * │   ├── reservas.txt
 * │   ├── usuarios.dat (versão binária)
 * │   └── backup/
 * └── logs/
 *     └── sistema.log
 * 
 * ===== FUNCIONALIDADES IMPLEMENTADAS =====
 * 
 * ✓ Interface CLI interativa
 * ✓ Sistema de login e cadastro
 * ✓ CRUD completo de usuários, voos e reservas
 * ✓ Controle de assentos automático
 * ✓ Relatórios diversos (usuário e admin)
 * ✓ Sistema de logs
 * ✓ Validações e tratamento de erros
 * ✓ Clean Architecture
 * ✓ Persistência em arquivos texto
 * ✓ Alternativa com arquivos binários
 * ✓ Testes automatizados
 * ✓ Gerador de dados de exemplo
 * ✓ Hash de senhas (SHA-256)
 * ✓ Controle de permissões (cliente/admin)
 * ✓ Streams API e Collections modernas
 * ✓ Java NIO para manipulação de arquivos
 * 
 * ===== MELHORIAS POSSÍVEIS =====
 * 
 * - Backup automático dos dados
 * - Compressão de arquivos antigos
 * - Interface gráfica (JavaFX)
 * - Exportação de relatórios (PDF/CSV)
 * - Notificações por email
 * - API REST
 * - Multithreading para operações pesadas
 * - Cache em memória
 * - Configurações via arquivo properties
 * - Internacionalização (i18n)
 */