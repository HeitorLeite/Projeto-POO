import enums.NivelAcesso;
import enums.StatusInscricao;
import enums.StatusJustificativa;
import exceptions.IdadeIncompativelException;
import exceptions.LimiteModalidadesException;
import exceptions.VagaIndisponivelException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Admin;
import model.Aluno;
import model.Chamada;
import model.Inscricao;
import model.Justificativa;
import model.Modalidade;
import model.Polo;
import model.Professor;
import model.Turma;
import model.Usuario;
import service.InscricaoService;
import util.GerenciadorArquivo;

public class Main {

    static final String ARQUIVO_ALUNOS = "dados/alunos.csv";
    static final String ARQUIVO_ADMINS = "dados/admins.csv";
    static final String ARQUIVO_PROFESSORES = "dados/professores.csv";
    static final String ARQUIVO_MODALIDADES = "dados/modalidades.csv";
    static final String ARQUIVO_POLOS = "dados/polos.csv";
    static final String ARQUIVO_TURMAS = "dados/turmas.csv";
    static final String ARQUIVO_INSCRICOES = "dados/inscricoes.csv";
    static final String ARQUIVO_JUSTIFICATIVAS = "dados/justificativas.csv";
    static final String ARQUIVO_CHAMADA_ULTIMA = "dados/chamada_ultima.csv";

    static Scanner leia = new Scanner(System.in);

    static List<Aluno> alunos;
    static List<Admin> administradores;
    static List<Professor> professores;
    static List<Modalidade> modalidades;
    static List<Polo> polos;
    static List<Turma> turmas;
    static List<Inscricao> inscricoes;
    static List<Justificativa> justificativas;

    static InscricaoService inscricaoService = new InscricaoService();

    public static void main(String[] args) {
        System.out.println("Diretorio atual: " + System.getProperty("user.dir"));

        modalidades = carregarModalidades();
        polos = carregarPolos();
        professores = carregarProfessores();
        administradores = carregarAdministradores();
        alunos = carregarAlunos();
        turmas = carregarTurmas(modalidades, professores, polos);
        inscricoes = carregarInscricoes(alunos, turmas);
        justificativas = carregarJustificativas(alunos, inscricoes);

        menuEntrada();
    }

    static List<Modalidade> carregarModalidades() {
        List<Modalidade> lista = new ArrayList<>();
        for (String[] d : GerenciadorArquivo.carregar(ARQUIVO_MODALIDADES)) {
            if (d.length < 4)
                continue;
            Modalidade m = new Modalidade(d[0], Integer.parseInt(d[1].trim()),
                    Integer.parseInt(d[2].trim()), Integer.parseInt(d[3].trim()));
            if (d.length > 4 && !Boolean.parseBoolean(d[4].trim())) {
                m.inativar();
            }
            lista.add(m);
        }
        return lista;
    }

    static List<Polo> carregarPolos() {
        List<Polo> lista = new ArrayList<>();
        for (String[] d : GerenciadorArquivo.carregar(ARQUIVO_POLOS)) {
            if (d.length < 2)
                continue;
            Polo p = new Polo(d[0], d[1]);
            if (d.length > 2 && !Boolean.parseBoolean(d[2].trim())) {
                p.inativar();
            }
            lista.add(p);
        }
        return lista;
    }

    static List<Professor> carregarProfessores() {
        List<Professor> lista = new ArrayList<>();
        for (String[] d : GerenciadorArquivo.carregar(ARQUIVO_PROFESSORES)) {
            if (d.length < 8)
                continue;
            String registro = d.length > 8 ? d[8] : "-";
            String especialidade = d.length > 9 ? d[9] : "-";
            Professor p = new Professor(d[0], d[1], d[2], d[3], d[4],
                    LocalDate.parse(d[6]), d[7], registro, especialidade);
            lista.add(p);
        }
        return lista;
    }

    static List<Admin> carregarAdministradores() {
        List<Admin> lista = new ArrayList<>();
        for (String[] d : GerenciadorArquivo.carregar(ARQUIVO_ADMINS)) {
            if (d.length < 8)
                continue;
            NivelAcesso nivel = d.length > 8 ? parseNivelAcesso(d[8]) : NivelAcesso.PARCIAL;
            Admin a = new Admin(d[0], d[1], d[2], d[3], d[4], LocalDate.parse(d[6]), d[7], nivel);
            lista.add(a);
        }
        return lista;
    }

    static NivelAcesso parseNivelAcesso(String valor) {
        valor = valor.trim();
        if (valor.equals("0"))
            return NivelAcesso.PARCIAL;
        if (valor.equals("1"))
            return NivelAcesso.TOTAL;
        try {
            return NivelAcesso.valueOf(valor);
        } catch (IllegalArgumentException e) {
            return NivelAcesso.PARCIAL;
        }
    }

    static List<Aluno> carregarAlunos() {
        List<Aluno> lista = new ArrayList<>();
        for (String[] d : GerenciadorArquivo.carregar(ARQUIVO_ALUNOS)) {
            if (d.length < 6)
                continue;
            String responsavelNome = d.length > 7 ? d[7] : "-";
            String responsavelTelefone = d.length > 8 ? d[8] : "-";
            Aluno a = new Aluno(d[0], d[1], d[2], d[3], d[4], LocalDate.parse(d[6]),
                    responsavelNome, responsavelTelefone);
            lista.add(a);
        }
        return lista;
    }

    static List<Turma> carregarTurmas(List<Modalidade> modalidades, List<Professor> professores, List<Polo> polos) {
        List<Turma> lista = new ArrayList<>();

        for (String[] d : GerenciadorArquivo.carregar(ARQUIVO_TURMAS)) {
            if (d.length < 4)
                continue;

            Modalidade modalidade = buscarModalidade(modalidades, d[1]);
            if (modalidade == null) {
                System.out.println("Modalidade '" + d[1] + "' nao encontrada para a turma '" + d[0] + "'.");
                continue;
            }

            Polo polo = (d.length > 5) ? buscarPolo(polos, d[5]) : null;

            Turma turma = new Turma(d[0], modalidade, polo, d[2], Integer.parseInt(d[3].trim()));

            if (d.length > 4 && !d[4].isBlank()) {
                for (String cpf : d[4].split(";")) {
                    Professor professor = buscarProfessorPorCpf(professores, cpf.trim());
                    if (professor != null) {
                        turma.setProfessor(professor);
                    }
                }
            }

            if (d.length > 6 && !Boolean.parseBoolean(d[6].trim())) {
                turma.inativar();
            }

            lista.add(turma);
        }

        return lista;
    }

    static List<Inscricao> carregarInscricoes(List<Aluno> alunos, List<Turma> turmas) {
        List<Inscricao> lista = new ArrayList<>();

        for (String[] d : GerenciadorArquivo.carregar(ARQUIVO_INSCRICOES)) {
            if (d.length < 5)
                continue;

            Aluno aluno = buscarAlunoPorCpf(alunos, d[0]);
            Turma turma = buscarTurmaPorNome(turmas, d[1]);
            if (aluno == null || turma == null)
                continue;

            StatusInscricao status = StatusInscricao.valueOf(d[2]);
            LocalDate data = LocalDate.parse(d[3]);
            int faltas = Integer.parseInt(d[4].trim());

            Inscricao inscricao = new Inscricao(aluno, turma, status, data, faltas);
            lista.add(inscricao);

            if (status == StatusInscricao.ATIVA && !turma.getAlunosInscritos().contains(aluno)) {
                turma.adicionarAluno(aluno);
            }
        }

        return lista;
    }

    static List<Justificativa> carregarJustificativas(List<Aluno> alunos, List<Inscricao> inscricoes) {
        List<Justificativa> lista = new ArrayList<>();

        for (String[] d : GerenciadorArquivo.carregar(ARQUIVO_JUSTIFICATIVAS)) {
            if (d.length < 5)
                continue;

            Aluno aluno = buscarAlunoPorCpf(alunos, d[0]);
            Inscricao inscricao = buscarInscricao(inscricoes, d[0], d[1]);
            if (aluno == null || inscricao == null)
                continue;

            StatusJustificativa status = StatusJustificativa.valueOf(d[4]);
            lista.add(new Justificativa(aluno, inscricao, d[2], d[3], status));
        }

        return lista;
    }

    static Modalidade buscarModalidade(List<Modalidade> lista, String nome) {
        for (Modalidade m : lista) {
            if (m.getNome().equalsIgnoreCase(nome))
                return m;
        }
        return null;
    }

    static Polo buscarPolo(List<Polo> lista, String nome) {
        for (Polo p : lista) {
            if (p.getNome().equalsIgnoreCase(nome))
                return p;
        }
        return null;
    }

    static Professor buscarProfessorPorCpf(List<Professor> lista, String cpf) {
        for (Professor p : lista) {
            if (p.getCpf().equals(cpf))
                return p;
        }
        return null;
    }

    static Aluno buscarAlunoPorCpf(List<Aluno> lista, String cpf) {
        for (Aluno a : lista) {
            if (a.getCpf().equals(cpf))
                return a;
        }
        return null;
    }

    static Turma buscarTurmaPorNome(List<Turma> lista, String nome) {
        for (Turma t : lista) {
            if (t.getNome().equalsIgnoreCase(nome))
                return t;
        }
        return null;
    }

    static Inscricao buscarInscricao(List<Inscricao> lista, String alunoCpf, String turmaNome) {
        for (Inscricao i : lista) {
            if (i.getAluno().getCpf().equals(alunoCpf) && i.getTurma().getNome().equalsIgnoreCase(turmaNome)) {
                return i;
            }
        }
        return null;
    }

    static void persistirInscricoes() {
        GerenciadorArquivo.salvar(inscricoes, ARQUIVO_INSCRICOES);
    }

    static void persistirJustificativas() {
        GerenciadorArquivo.salvar(justificativas, ARQUIVO_JUSTIFICATIVAS);
    }

    static void menuEntrada() {

        int opcao = -1;

        while (opcao != 0) {

            System.out.println("-------------- SEJA BEM-VINDO! --------------");
            System.out.println("1. Fazer login");
            System.out.println("2. Cadastrar-se");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opcao: ");
            opcao = leia.nextInt();
            leia.nextLine();

            switch (opcao) {
                case 1:
                    loginUsuario();
                    break;

                case 2:
                    cadastroUsuario();
                    break;

                case 0:
                    System.out.println("Saindo... Tenha um bom dia!");
                    break;

                default:
                    System.out.println("Opcao invalida! Digite 1, 2 ou 0.");
            }
        }
    }

    static void loginUsuario() {

        System.out.println("-------------- LOGIN --------------");
        System.out.print("Digite seu CPF: ");
        String cpf = leia.nextLine();

        System.out.print("Digite sua senha: ");
        String senha = leia.nextLine();

        for (Aluno a : alunos) {
            if (a.autenticar(cpf, senha)) {
                System.out.println("Login realizado!");
                menuAluno(a);
                return;
            }
        }

        for (Admin admin : administradores) {
            if (admin.autenticar(cpf, senha)) {
                System.out.println("Login de administrador realizado!");
                menuAdministrador(admin);
                return;
            }
        }

        for (Professor p : professores) {
            if (p.autenticar(cpf, senha)) {
                System.out.println("Login de professor realizado!");
                menuProfessor(p);
                return;
            }
        }

        System.out.println("CPF ou senha invalidos!");
    }

    static void cadastroUsuario() {

        System.out.println("-------------- CADASTRO DE ALUNO --------------");

        System.out.print("Digite seu nome completo: ");
        String nome = leia.nextLine();

        LocalDate dataNascimento = lerData("Digite sua data de nascimento (dd-MM-yyyy): ");

        String cpf = lerCpfNovo();

        System.out.print("Digite seu telefone: ");
        String telefone = leia.nextLine();

        System.out.print("Digite seu email: ");
        String email = leia.nextLine();

        System.out.print("Nome do responsavel (deixe em branco se maior de 18): ");
        String responsavelNome = leia.nextLine();
        if (responsavelNome.trim().isEmpty()) {
            responsavelNome = "-";
        }

        System.out.print("Telefone do responsavel (deixe em branco se maior de 18): ");
        String responsavelTelefone = leia.nextLine();
        if (responsavelTelefone.trim().isEmpty()) {
            responsavelTelefone = "-";
        }

        System.out.print("Digite sua senha: ");
        String senha = leia.nextLine();

        Aluno novoAluno = new Aluno(nome, cpf, email, telefone, senha, dataNascimento,
                responsavelNome, responsavelTelefone);

        alunos.add(novoAluno);
        GerenciadorArquivo.adicionar(novoAluno, ARQUIVO_ALUNOS);

        System.out.println("Cadastro realizado com sucesso! Bem-vindo, " + nome + "!");
        novoAluno.exibirPerfil();
    }

    static void cadastroAdmin() {
        System.out.println("-------------- CADASTRO DE ADMINISTRADOR --------------");

        System.out.print("Digite seu nome completo: ");
        String nome = leia.nextLine();

        LocalDate dataNascimento = lerData("Digite sua data de nascimento (dd-MM-yyyy): ");

        String cpf = lerCpfNovo();

        System.out.print("Digite seu telefone: ");
        String telefone = leia.nextLine();

        System.out.print("Digite seu email: ");
        String email = leia.nextLine();

        System.out.print("Digite sua senha: ");
        String senha = leia.nextLine();

        System.out.print("Digite seu setor: ");
        String setor = leia.nextLine();

        NivelAcesso nivelAcesso = lerNivelAcesso();

        Admin novoAdmin = new Admin(nome, cpf, email, telefone, senha, dataNascimento, setor, nivelAcesso);

        administradores.add(novoAdmin);
        GerenciadorArquivo.adicionar(novoAdmin, ARQUIVO_ADMINS);

        System.out.println("Cadastro de administrador realizado com sucesso! Bem-vindo, " + nome + "!");
        novoAdmin.exibirPerfil();
    }

    static NivelAcesso lerNivelAcesso() {
        int opcao = -1;
        while (opcao != 1 && opcao != 2) {
            System.out.println("Nivel de acesso: 1-TOTAL  2-PARCIAL");
            System.out.print("Escolha: ");
            opcao = leia.nextInt();
            leia.nextLine();
        }
        return opcao == 1 ? NivelAcesso.TOTAL : NivelAcesso.PARCIAL;
    }

    static void cadastroProfessor() {
        System.out.println("-------------- CADASTRO DE PROFESSOR --------------");

        System.out.print("Digite o nome completo do professor: ");
        String nome = leia.nextLine();

        LocalDate dataNascimento = lerData("Digite a data de nascimento do professor (dd-MM-yyyy): ");

        String cpf = lerCpfNovo();

        System.out.print("Digite o telefone do professor: ");
        String telefone = leia.nextLine();

        System.out.print("Digite o email do professor: ");
        String email = leia.nextLine();

        System.out.print("Digite a senha do professor: ");
        String senha = leia.nextLine();

        System.out.print("Numero de registro profissional do professor: ");
        String registro = leia.nextLine();

        System.out.print("Especialidade do professor (ex: Educacao Fisica): ");
        String especialidade = leia.nextLine();

        if (modalidades.isEmpty()) {
            System.out
                    .println("Nenhuma modalidade cadastrada. Cadastre uma modalidade antes de registrar um professor.");
            return;
        }

        System.out.println("\nModalidades disponiveis:");
        for (int i = 0; i < modalidades.size(); i++) {
            Modalidade m = modalidades.get(i);
            System.out.printf("[%d] %s (faixa etaria: %d a %d anos)%n",
                    i + 1, m.getNome(), m.getIdadeMinima(), m.getIdadeMaxima());
        }

        int escolha = -1;
        while (escolha < 1 || escolha > modalidades.size()) {
            System.out.print("Escolha o numero da modalidade: ");
            escolha = leia.nextInt();
            leia.nextLine();

            if (escolha < 1 || escolha > modalidades.size()) {
                System.out.println("Opcao invalida! Tente novamente.");
            }
        }

        String modalidade = modalidades.get(escolha - 1).getNome();

        Professor novoProfessor = new Professor(nome, cpf, email, telefone, senha, dataNascimento,
                modalidade, registro, especialidade);

        professores.add(novoProfessor);
        GerenciadorArquivo.adicionar(novoProfessor, ARQUIVO_PROFESSORES);

        System.out.println("Cadastro de professor realizado com sucesso! Bem-vindo, " + nome + "!");
        novoProfessor.exibirPerfil();
    }

    static void cadastroModalidade() {
        System.out.println("-------------- CADASTRO DE MODALIDADE --------------");
        System.out.print("Digite o nome da modalidade: ");
        String nome = leia.nextLine();

        System.out.print("Digite a idade minima: ");
        int idadeMinima = leia.nextInt();

        System.out.print("Digite a idade maxima: ");
        int idadeMaxima = leia.nextInt();

        System.out.print("Digite o limite de faltas mensais: ");
        int limiteFaltas = leia.nextInt();
        leia.nextLine();

        Modalidade novaModalidade = new Modalidade(nome, idadeMinima, idadeMaxima, limiteFaltas);

        modalidades.add(novaModalidade);
        GerenciadorArquivo.adicionar(novaModalidade, ARQUIVO_MODALIDADES);
    }

    static void cadastroPolo() {
        System.out.println("-------------- CADASTRO DE POLO --------------");
        System.out.print("Nome do polo: ");
        String nome = leia.nextLine();

        System.out.print("Endereco do polo: ");
        String endereco = leia.nextLine();

        Polo novoPolo = new Polo(nome, endereco);
        polos.add(novoPolo);
        GerenciadorArquivo.adicionar(novoPolo, ARQUIVO_POLOS);

        System.out.println("Polo cadastrado com sucesso!");
    }

    static void verPolos() {
        System.out.println("\n===== POLOS CADASTRADOS =====");
        if (polos.isEmpty()) {
            System.out.println("Nenhum polo cadastrado.");
        } else {
            for (Polo p : polos) {
                p.exibirPolo();
            }
        }
        System.out.println("==============================");
    }

    static void cadastroTurma() {

        System.out.println("-------------- CADASTRO DE TURMA --------------");

        if (modalidades.isEmpty()) {
            System.out.println("Nenhuma modalidade cadastrada.");
            return;
        }

        if (professores.isEmpty()) {
            System.out.println("Nenhum professor cadastrado.");
            return;
        }

        if (polos.isEmpty()) {
            System.out.println("Nenhum polo cadastrado. Cadastre um polo antes de criar a turma.");
            return;
        }

        System.out.print("Nome da turma: ");
        String nomeTurma = leia.nextLine();

        System.out.print("Horario da turma (seg/ter 18h): ");
        String horario = leia.nextLine();

        System.out.print("Limite de alunos: ");
        int limiteAlunos = leia.nextInt();
        leia.nextLine();

        System.out.println("\nModalidades disponiveis:");
        for (int i = 0; i < modalidades.size(); i++) {
            System.out.println((i + 1) + " - " + modalidades.get(i).getNome());
        }

        int opcaoModalidade;
        do {
            System.out.print("Escolha a modalidade: ");
            opcaoModalidade = leia.nextInt();
            leia.nextLine();
        } while (opcaoModalidade < 1 || opcaoModalidade > modalidades.size());

        Modalidade modalidadeSelecionada = modalidades.get(opcaoModalidade - 1);

        System.out.println("\nPolos disponiveis:");
        for (int i = 0; i < polos.size(); i++) {
            System.out.println((i + 1) + " - " + polos.get(i).getNome());
        }

        int opcaoPolo;
        do {
            System.out.print("Escolha o polo: ");
            opcaoPolo = leia.nextInt();
            leia.nextLine();
        } while (opcaoPolo < 1 || opcaoPolo > polos.size());

        Polo poloSelecionado = polos.get(opcaoPolo - 1);

        System.out.println("\nProfessores disponiveis:");
        for (int i = 0; i < professores.size(); i++) {
            Professor p = professores.get(i);
            System.out.println((i + 1) + " - " + p.getNome() + " | Modalidade: " + p.getModalidade());
        }

        int opcaoProfessor;
        do {
            System.out.print("Escolha o professor responsavel: ");
            opcaoProfessor = leia.nextInt();
            leia.nextLine();
        } while (opcaoProfessor < 1 || opcaoProfessor > professores.size());

        Professor professorSelecionado = professores.get(opcaoProfessor - 1);

        Turma novaTurma = new Turma(nomeTurma, modalidadeSelecionada, poloSelecionado, horario, limiteAlunos);
        novaTurma.setProfessor(professorSelecionado);

        turmas.add(novaTurma);
        GerenciadorArquivo.adicionar(novaTurma, ARQUIVO_TURMAS);

        System.out.println("\nTurma cadastrada com sucesso!");
        System.out.println("Turma: " + nomeTurma);
        System.out.println("Modalidade: " + modalidadeSelecionada.getNome());
        System.out.println("Polo: " + poloSelecionado.getNome());
        System.out.println("Professor responsavel: " + professorSelecionado.getNome());
    }

    static void verModalidades() {
        System.out.println("\n===== MODALIDADES DISPONIVEIS =====");
        if (turmas.isEmpty()) {
            System.out.println("Nenhuma turma cadastrada.");
            return;
        }
        for (Turma t : turmas) {
            int vagasLivres = t.getLimiteAlunos() - t.getAlunosInscritos().size();
            System.out.printf("Turma: %s | Modalidade: %s | Polo: %s | Horario: %s | Vagas livres: %d/%d%n",
                    t.getNome(),
                    t.getModalidade().getNome(),
                    t.getPolo() != null ? t.getPolo().getNome() : "-",
                    t.getHorario(),
                    vagasLivres,
                    t.getLimiteAlunos());
        }
        System.out.println("===================================");
    }

    static void realizarInscricao(Aluno aluno) {
        if (turmas.isEmpty()) {
            System.out.println("Nenhuma turma disponivel no momento.");
            return;
        }

        System.out.println("\n===== TURMAS DISPONIVEIS =====");
        for (int i = 0; i < turmas.size(); i++) {
            Turma t = turmas.get(i);
            System.out.printf("[%d] %s | %s | Horario: %s | Vagas: %d/%d%n",
                    i + 1, t.getNome(), t.getModalidade().getNome(), t.getHorario(),
                    t.getAlunosInscritos().size(), t.getLimiteAlunos());
        }

        System.out.print("Escolha o numero da turma: ");
        int escolha = leia.nextInt();
        leia.nextLine();

        if (escolha < 1 || escolha > turmas.size()) {
            System.out.println("Opcao invalida.");
            return;
        }

        Turma turmaSelecionada = turmas.get(escolha - 1);

        try {
            Inscricao inscricao = inscricaoService.realizarInscricao(aluno, turmaSelecionada);
            inscricoes.add(inscricao);
            persistirInscricoes();
            System.out.println("Inscricao realizada com sucesso!");

        } catch (LimiteModalidadesException e) {
            System.out.println("ERRO: " + e.getMessage());

        } catch (IdadeIncompativelException e) {
            System.out.println("ERRO: " + e.getMessage());

        } catch (VagaIndisponivelException e) {
            System.out.println("AVISO: " + e.getMessage());

            // RN009: registra a inscricao em estado de espera
            Inscricao inscricaoEspera = new Inscricao(aluno, turmaSelecionada);
            inscricaoEspera.colocarEmEspera();
            inscricoes.add(inscricaoEspera);
            persistirInscricoes();
        }
    }

    static void verInscricoes(Aluno aluno) {
        System.out.println("\n===== MINHAS INSCRICOES =====");
        boolean encontrou = false;
        for (Inscricao i : inscricoes) {
            if (i.getAluno().getCpf().equals(aluno.getCpf())) {
                System.out.printf("- Turma: %-20s | Status: %s | Data: %s%n",
                        i.getTurma().getNome(), i.getStatus(), i.getDataInscricao());
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Voce nao possui inscricoes.");
        }
        System.out.println("=============================");
    }

    static void verFrequencia(Aluno aluno) {
        System.out.println("\n===== MINHA FREQUENCIA =====");
        boolean encontrou = false;
        for (Inscricao i : inscricoes) {
            if (i.getAluno().getCpf().equals(aluno.getCpf()) && i.getStatus() == StatusInscricao.ATIVA) {
                int limiteFaltas = i.getTurma().getModalidade().getLimiteFaltasMensais();
                System.out.printf("- %s | Faltas no mes: %d/%d%n",
                        i.getTurma().getNome(), i.getFaltasMes(), limiteFaltas);
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Nenhuma inscricao ativa encontrada.");
        }
        System.out.println("============================");
    }

    static void enviarJustificativa(Aluno aluno) {
        List<Inscricao> minhasInscricoes = new ArrayList<>();
        for (Inscricao i : inscricoes) {
            if (i.getAluno().getCpf().equals(aluno.getCpf()) && i.getStatus() == StatusInscricao.ATIVA
                    && i.getFaltasMes() > 0) {
                minhasInscricoes.add(i);
            }
        }

        if (minhasInscricoes.isEmpty()) {
            System.out.println("Voce nao possui faltas em inscricoes ativas para justificar.");
            return;
        }

        System.out.println("\n===== ESCOLHA A INSCRICAO =====");
        for (int i = 0; i < minhasInscricoes.size(); i++) {
            Inscricao insc = minhasInscricoes.get(i);
            System.out.printf("[%d] %s | Faltas: %d%n", i + 1, insc.getTurma().getNome(), insc.getFaltasMes());
        }

        System.out.print("Escolha o numero: ");
        int escolha = leia.nextInt();
        leia.nextLine();

        if (escolha < 1 || escolha > minhasInscricoes.size()) {
            System.out.println("Opcao invalida.");
            return;
        }

        Inscricao inscricaoEscolhida = minhasInscricoes.get(escolha - 1);

        System.out.print("Motivo da falta: ");
        String motivo = leia.nextLine();

        System.out.print("Caminho do arquivo comprobatorio (atestado, etc.): ");
        String caminhoArquivo = leia.nextLine();

        Justificativa justificativa = new Justificativa(aluno, inscricaoEscolhida, motivo, caminhoArquivo);
        justificativas.add(justificativa);
        persistirJustificativas();

        System.out.println("Justificativa enviada! Aguarde a avaliacao.");
    }

    static void avaliarJustificativas() {
        List<Justificativa> pendentes = new ArrayList<>();
        for (Justificativa j : justificativas) {
            if (j.getStatus() == StatusJustificativa.PENDENTE) {
                pendentes.add(j);
            }
        }

        if (pendentes.isEmpty()) {
            System.out.println("Nenhuma justificativa pendente.");
            return;
        }

        System.out.println("\n===== JUSTIFICATIVAS PENDENTES =====");
        for (int i = 0; i < pendentes.size(); i++) {
            Justificativa j = pendentes.get(i);
            System.out.println("[" + (i + 1) + "]");
            j.exibirJustificativa();
        }

        System.out.print("Escolha o numero para avaliar (0 para voltar): ");
        int escolha = leia.nextInt();
        leia.nextLine();

        if (escolha < 1 || escolha > pendentes.size()) {
            return;
        }

        Justificativa escolhida = pendentes.get(escolha - 1);

        System.out.print("1-Aprovar  2-Recusar: ");
        int decisao = leia.nextInt();
        leia.nextLine();

        if (decisao == 1) {
            escolhida.aprovar();
            persistirInscricoes();
        } else {
            escolhida.recusar();
        }

        persistirJustificativas();
    }

    static boolean cpfJaExiste(String cpf) {
        cpf = cpf.trim();

        for (Aluno a : alunos) {
            if (a.getCpf().trim().equals(cpf))
                return true;
        }
        for (Professor p : professores) {
            if (p.getCpf().trim().equals(cpf))
                return true;
        }
        for (Admin ad : administradores) {
            if (ad.getCpf().trim().equals(cpf))
                return true;
        }
        return false;
    }

    static String lerCpfNovo() {
        String cpf;
        while (true) {
            System.out.print("Digite seu CPF: ");
            cpf = leia.nextLine().trim();

            if (cpf.isEmpty()) {
                System.out.println("CPF nao pode ser vazio!");
                continue;
            }
            if (cpfJaExiste(cpf)) {
                System.out.println("Erro: esse CPF ja esta cadastrado (RN016)! Digite outro.");
                continue;
            }
            break;
        }
        return cpf;
    }

    static LocalDate lerData(String mensagem) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.print(mensagem);
        String data = leia.nextLine();
        try {
            return LocalDate.parse(data, formato);
        } catch (Exception erro) {
            System.out.println("Formato errado! Usando data padrao (hoje).");
            return LocalDate.now();
        }
    }

    static void verTurmas(Professor professor) {
        System.out.println("\n===== MINHAS TURMAS =====");

        boolean encontrou = false;

        for (Turma t : turmas) {
            if (t.getProfessoresResponsaveis().contains(professor)) {
                System.out.printf("Turma: %s | Modalidade: %s | Horario: %s | Alunos: %d/%d%n",
                        t.getNome(), t.getModalidade().getNome(), t.getHorario(),
                        t.getAlunosInscritos().size(), t.getLimiteAlunos());
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Voce nao esta responsavel por nenhuma turma.");
        }

        System.out.println("=========================");
    }

    static void listarTodosUsuarios() {
        List<Usuario> todos = new ArrayList<>();
        todos.addAll(alunos);
        todos.addAll(professores);
        todos.addAll(administradores);

        System.out.println("\n===== TODOS OS USUARIOS (polimorfismo) =====");
        if (todos.isEmpty()) {
            System.out.println("Nenhum usuario cadastrado.");
        } else {
            for (Usuario u : todos) {
                u.exibirPerfil();
            }
        }
        System.out.println("==============================================");
    }

    static void menuAluno(Aluno aluno) {

        int opcao = -1;

        while (opcao != 0) {
            System.out.println("---------- MENU DO ALUNO ----------");
            System.out.println("  Logado: " + aluno.getNome());
            System.out.println("  1. Ver meu perfil");
            System.out.println("  2. Ver modalidades disponiveis");
            System.out.println("  3. Realizar inscricao");
            System.out.println("  4. Ver minhas inscricoes");
            System.out.println("  5. Ver minha frequencia");
            System.out.println("  6. Enviar justificativa de falta");
            System.out.println("  0. Logout");
            System.out.println("--------------------------------------");
            System.out.print("Digite sua opcao: ");

            opcao = leia.nextInt();
            leia.nextLine();

            switch (opcao) {
                case 1:
                    aluno.exibirPerfil();
                    break;
                case 2:
                    verModalidades();
                    break;
                case 3:
                    realizarInscricao(aluno);
                    break;
                case 4:
                    verInscricoes(aluno);
                    break;
                case 5:
                    verFrequencia(aluno);
                    break;
                case 6:
                    enviarJustificativa(aluno);
                    break;
                case 0:
                    System.out.println("Logout realizado. Ate logo, " + aluno.getNome() + "!");
                    break;
                default:
                    System.out.println("Opcao invalida!");
            }
        }
    }

    static void menuAdministrador(Admin admin) {

        int opcao = -1;

        while (opcao != 0) {

            System.out.println("\n===== MENU ADMINISTRADOR =====");
            System.out.println("Logado: " + admin.getNome() + " (" + admin.getNivelAcesso() + ")");
            System.out.println("1. Ver perfil");
            System.out.println("2. Listar todos os usuarios");
            System.out.println("3. Cadastrar professor");
            System.out.println("4. Criar turma");
            System.out.println(
                    "5. Cadastrar novo administrador" + (admin.temAcessoTotal() ? "" : " [requer acesso TOTAL]"));
            System.out.println("6. Cadastrar nova modalidade");
            System.out.println("7. Cadastrar novo polo");
            System.out.println("8. Listar polos");
            System.out.println("9. Avaliar justificativas pendentes");
            System.out.println("0. Logout");
            System.out.print("Escolha: ");

            opcao = leia.nextInt();
            leia.nextLine();

            switch (opcao) {

                case 1:
                    admin.exibirPerfil();
                    break;

                case 2:
                    listarTodosUsuarios();
                    break;

                case 3:
                    cadastroProfessor();
                    break;

                case 4:
                    cadastroTurma();
                    break;

                case 5:
                    if (admin.temAcessoTotal()) {
                        cadastroAdmin();
                    } else {
                        System.out.println(
                                "Acesso negado: apenas administradores com nivel TOTAL podem cadastrar outros administradores.");
                    }
                    break;

                case 6:
                    cadastroModalidade();
                    break;

                case 7:
                    cadastroPolo();
                    break;

                case 8:
                    verPolos();
                    break;

                case 9:
                    avaliarJustificativas();
                    break;

                case 0:
                    System.out.println("Logout realizado.");
                    break;

                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    static void menuProfessor(Professor professor) {

        int opcao = -1;

        while (opcao != 0) {

            System.out.println("\n===== MENU PROFESSOR =====");
            System.out.println("Logado: " + professor.getNome());
            System.out.println("1. Ver perfil");
            System.out.println("2. Ver minhas turmas");
            System.out.println("3. Ver alunos de uma turma");
            System.out.println("4. Fazer chamada");
            System.out.println("0. Logout");
            System.out.print("Escolha: ");

            opcao = leia.nextInt();
            leia.nextLine();

            switch (opcao) {

                case 1:
                    professor.exibirPerfil();
                    break;

                case 2:
                    verTurmas(professor);
                    break;

                case 3:
                    professor.verAlunosDaTurma(professor, turmas);
                    break;

                case 4:
                    Chamada chamada = professor.registrarFaltaProfessor(professor, turmas, inscricoes, inscricaoService,
                            leia);
                    if (chamada != null) {
                        GerenciadorArquivo.salvarRelatorioPresenca(chamada.getPresenca(), ARQUIVO_CHAMADA_ULTIMA);
                        persistirInscricoes();
                        System.out.println("Chamada registrada e salva em " + ARQUIVO_CHAMADA_ULTIMA);
                    }
                    break;

                case 0:
                    System.out.println("Logout realizado. Ate logo, " + professor.getNome() + "!");
                    break;

                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }
}