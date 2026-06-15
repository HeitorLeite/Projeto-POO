import enums.StatusInscricao;
import exceptions.IdadeIncompativel;
import exceptions.LimiteModalidades;
import exceptions.VagaIndisponivel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Admin;
import model.Aluno;
import model.Inscricao;
import model.Modalidade;
import model.Professor;
import model.Turma;
import service.AdminCSV;
import service.AlunoCSV;
import service.InscricaoService;
import service.ModalidadeCSV;
import service.ProfessorCSV;
import service.TurmaCSV;

public class Main {

    static Scanner leia = new Scanner(System.in);
    static List<Aluno> alunos;
    static List<Modalidade> modalidades = new ArrayList<>();
    static List<Turma> turmas = new ArrayList<>();
    static List<Inscricao> inscricoes = new ArrayList<>();
    static InscricaoService inscricaoService = new InscricaoService();
    static List<Admin> administradores = new ArrayList<>();
    static List<Professor> professores = new ArrayList<>();

    public static void main(String[] args) {
        alunos = AlunoCSV.carregar();
        administradores = AdminCSV.carregar();
        professores = ProfessorCSV.carregar();
        modalidades = ModalidadeCSV.carregar();
        turmas = TurmaCSV.carregar(modalidades, professores);
        menuEntrada();
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

                case 3:
                    System.out.println("Saindo... Tenha um bom dia!");
                    break;

                default:
                    System.out.println("Opcao invalida! Digite 1, 2, 3 ou 0.");
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
            if (a.getCpf().equals(cpf) && a.getSenha().equals(senha)) {
                System.out.println("Login realizado!");
                menuAluno(a);
                return;
            }
        }

        for (Admin admin : administradores) {
            if (admin.getCpf().equals(cpf) && admin.getSenha().equals(senha)) {
                System.out.println("Login de administrador realizado!");
                menuAdministrador(admin);
                return;
            }
        }

        for (Professor p : professores) {
            if (p.getCpf().equals(cpf) && p.getSenha().equals(senha)) {
                System.out.println("Login de professor realizado!");
                menuProfessor(p);
                return;
            }
        }
        System.out.println("CPF ou senha invalidos!");
    }

    static void cadastroUsuario() {

        System.out.println("-------------- CADASTRO DE USUARIO --------------");

        System.out.print("Digite seu nome completo: ");
        String nome = leia.nextLine();

        System.out.print("Digite sua data de nascimento (dd-MM-yyyy): ");
        String data = leia.nextLine();
        LocalDate dataNascimento;

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            dataNascimento = LocalDate.parse(data, formato);

        } catch (Exception erro) {
            System.out.println("Formato errado!");
            System.out.println("Usando data padr?o (hoje).");
            dataNascimento = LocalDate.now();
        }

        String cpf = "";

        while (true) {
            System.out.print("Digite seu CPF: ");
            cpf = leia.nextLine().trim();

            if (cpf.isEmpty()) {
                System.out.println("CPF nao pode ser vazio!");
                continue;
            }

            if (cpfJaExiste(cpf)) {
                System.out.println("Erro: esse CPF ja esta cadastrado! Digite outro.");
                continue;
            }

            break;
        }

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

        Aluno novoAluno = new Aluno(
                nome,
                cpf,
                email,
                telefone,
                senha,
                dataNascimento,
                responsavelNome,
                responsavelTelefone);

        alunos.add(novoAluno);
        AlunoCSV.salvar(novoAluno);

        System.out.println("Cadastro realizado com sucesso! Bem-vindo, " + nome + "!");
        novoAluno.exibirPerfil();
    }

    static void cadastroAdmin() {
        System.out.println("-------------- CADASTRO DE ADMINISTRADOR --------------");

        System.out.print("Digite seu nome completo: ");
        String nome = leia.nextLine();

        System.out.print("Digite sua data de nascimento (dd-MM-yyyy): ");
        String data = leia.nextLine();
        LocalDate dataNascimento;

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            dataNascimento = LocalDate.parse(data, formato);

        } catch (Exception erro) {
            System.out.println("Formato errado!");
            System.out.println("Usando data padr?o (hoje).");
            dataNascimento = LocalDate.now();
        }

        String cpf = "";

        while (true) {
            System.out.print("Digite seu CPF: ");
            cpf = leia.nextLine().trim();

            if (cpf.isEmpty()) {
                System.out.println("CPF nao pode ser vazio!");
                continue;
            }

            if (cpfJaExiste(cpf)) {
                System.out.println("Erro: esse CPF ja esta cadastrado! Digite outro.");
                continue;
            }

            break;
        }

        System.out.print("Digite seu telefone: ");
        String telefone = leia.nextLine();

        System.out.print("Digite seu email: ");
        String email = leia.nextLine();

        System.out.print("Digite sua senha: ");
        String senha = leia.nextLine();

        System.out.print("Digite seu setor: ");
        String setor = leia.nextLine();

        System.out.print("Digite o nivel de permissao (0-PARCIAL, 1-TOTAL): ");
        byte permissao = leia.nextByte();
        leia.nextLine();

        Admin novoAdmin = new Admin(
                nome,
                cpf,
                email,
                telefone,
                senha,
                dataNascimento,
                setor,
                permissao);

        administradores.add(novoAdmin);
        AdminCSV.salvar(novoAdmin);

        System.out.println("Cadastro de administrador realizado com sucesso! Bem-vindo, " + nome + "!");
        novoAdmin.exibirPerfil();
    }

    static void cadastroProfessor() {
        System.out.println("-------------- CADASTRO DE PROFESSOR --------------");

        System.out.print("Digite o nome completo do professor: ");
        String nome = leia.nextLine();

        System.out.print("Digite a data de nascimento do professor (dd-MM-yyyy): ");
        String data = leia.nextLine();
        LocalDate dataNascimento;

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            dataNascimento = LocalDate.parse(data, formato);
        } catch (Exception erro) {
            System.out.println("Formato errado!");
            System.out.println("Usando data padrao (hoje).");
            dataNascimento = LocalDate.now();
        }

        String cpf = "";

        while (true) {
            System.out.print("Digite o CPF do professor: ");
            cpf = leia.nextLine().trim();

            if (cpf.isEmpty()) {
                System.out.println("CPF nao pode ser vazio!");
                continue;
            }

            if (cpfJaExiste(cpf)) {
                System.out.println("Erro: esse CPF ja esta cadastrado! Digite outro.");
                continue;
            }

            break;
        }

        System.out.print("Digite o telefone do professor: ");
        String telefone = leia.nextLine();

        System.out.print("Digite o email do professor: ");
        String email = leia.nextLine();

        System.out.print("Digite a senha do professor: ");
        String senha = leia.nextLine();

        if (modalidades.isEmpty()) {
            System.out
                    .println("Nenhuma modalidade cadastrada. Cadastre uma modalidade antes de registrar um professor.");
            return;
        }

        System.out.println("\nModalidades disponíveis:");
        for (int i = 0; i < modalidades.size(); i++) {
            Modalidade m = modalidades.get(i);
            System.out.printf("[%d] %s (faixa etária: %d a %d anos)%n",
                    i + 1, m.getNome(), m.getIdadeMinima(), m.getIdadeMaxima());
        }

        int escolha = -1;
        while (escolha < 1 || escolha > modalidades.size()) {
            System.out.print("Escolha o número da modalidade: ");
            escolha = leia.nextInt();
            leia.nextLine();

            if (escolha < 1 || escolha > modalidades.size()) {
                System.out.println("Opção inválida! Tente novamente.");
            }
        }

        String modalidade = modalidades.get(escolha - 1).getNome();

        Professor novoProfessor = new Professor(
                nome,
                cpf,
                email,
                telefone,
                senha,
                dataNascimento,
                modalidade);

        professores.add(novoProfessor);
        ProfessorCSV.salvar(novoProfessor);

        System.out.println("Cadastro de professor realizado com sucesso! Bem-vindo, " + nome + "!");
        novoProfessor.exibirPerfil();
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

        System.out.print("Nome da turma: ");
        String nomeTurma = leia.nextLine();

        System.out.print("Horário da turma (seg/ter 18h): ");
        String horario = leia.nextLine();

        System.out.print("Limite de alunos: ");
        int limiteAlunos = leia.nextInt();
        leia.nextLine();

        System.out.println("\nModalidades disponíveis:");

        for (int i = 0; i < modalidades.size(); i++) {
            System.out.println(
                    (i + 1) + " - " +
                            modalidades.get(i).getNome());
        }

        int opcaoModalidade;

        do {
            System.out.print("Escolha a modalidade: ");
            opcaoModalidade = leia.nextInt();
            leia.nextLine();

        } while (opcaoModalidade < 1 || opcaoModalidade > modalidades.size());

        // Colocar para se a modalidade não existir aparecer a opção de cadastrar nova modalidade

        Modalidade modalidadeSelecionada = modalidades.get(opcaoModalidade - 1);

        System.out.println("\nProfessores disponíveis:");

        for (int i = 0; i < professores.size(); i++) {

            Professor p = professores.get(i);

            System.out.println(
                    (i + 1) + " - " +
                            p.getNome() +
                            " | Modalidade: " +
                            p.getModalidade());
        }

        int opcaoProfessor;

        do {
            System.out.print("Escolha o professor responsável: ");
            opcaoProfessor = leia.nextInt();
            leia.nextLine();

        } while (opcaoProfessor < 1 || opcaoProfessor > professores.size());

        Professor professorSelecionado = professores.get(opcaoProfessor - 1);

        Turma novaTurma = new Turma(
                nomeTurma,
                modalidadeSelecionada,
                horario,
                limiteAlunos);

        novaTurma.setProfessor(professorSelecionado);

        turmas.add(novaTurma);

        TurmaCSV.salvar(novaTurma);

        System.out.println("\nTurma cadastrada com sucesso!");
        System.out.println("Turma: " + nomeTurma);
        System.out.println("Modalidade: " + modalidadeSelecionada.getNome());
        System.out.println("Professor responsável: " + professorSelecionado.getNome());
    }

    static void verModalidades() {
        System.out.println("\n===== MODALIDADES DISPONÍVEIS =====");
        if (turmas.isEmpty()) {
            System.out.println("Nenhuma turma cadastrada.");
            return;
        }
        for (Turma t : turmas) {
            int vagasLivres = t.getLimiteAlunos() - t.getAlunosInscritos().size();
            System.out.printf("• %s | Modalidade: %s | Horário: %s | Vagas livres: %d/%d%n",
                    t.getNome(),
                    t.getModalidade().getNome(),
                    t.getHorario(),
                    vagasLivres,
                    t.getLimiteAlunos());
        }
        System.out.println("===================================");
    }

    static void realizarInscricao(Aluno aluno) {
        if (turmas.isEmpty()) {
            System.out.println("Nenhuma turma disponível no momento.");
            return;
        }

        System.out.println("\n===== TURMAS DISPONÍVEIS =====");
        for (int i = 0; i < turmas.size(); i++) {
            Turma t = turmas.get(i);
            System.out.printf("[%d] %s | %s | Horário: %s | Vagas: %d/%d%n",
                    i + 1,
                    t.getNome(),
                    t.getModalidade().getNome(),
                    t.getHorario(),
                    t.getAlunosInscritos().size(),
                    t.getLimiteAlunos());
        }

        System.out.print("Escolha o número da turma: ");
        int escolha = leia.nextInt();
        leia.nextLine();

        if (escolha < 1 || escolha > turmas.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        Turma turmaSelecionada = turmas.get(escolha - 1);

        try {
            Inscricao inscricao = inscricaoService.realizarInscricao(aluno, turmaSelecionada);
            inscricoes.add(inscricao);
            System.out.println("Inscrição realizada com sucesso!");

        } catch (LimiteModalidades e) {
            System.out.println("ERRO: " + e.getMessage());

        } catch (IdadeIncompativel e) {
            System.out.println("ERRO: " + e.getMessage());

        } catch (VagaIndisponivel e) {
            System.out.println("AVISO: " + e.getMessage());
        }
    }

    static void verInscricoes(Aluno aluno) {
        System.out.println("\n===== MINHAS INSCRIÇÕES =====");
        boolean encontrou = false;
        for (Inscricao i : inscricoes) {
            if (i.getAluno().getCpf().equals(aluno.getCpf())) {
                System.out.printf("• Turma: %-20s | Status: %s | Data: %s%n",
                        i.getTurma().getNome(),
                        i.getStatus(),
                        i.getDataInscricao());
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Você não possui inscrições.");
        }
        System.out.println("=============================");
    }

    static void verFrequencia(Aluno aluno) {
        System.out.println("\n===== MINHA FREQUÊNCIA =====");
        boolean encontrou = false;
        for (Inscricao i : inscricoes) {
            if (i.getAluno().getCpf().equals(aluno.getCpf())
                    && i.getStatus() == StatusInscricao.ATIVA) {
                int limiteFaltas = i.getTurma().getModalidade().getLimiteFaltasMensais();
                System.out.printf("• %s | Faltas no mês: %d/%d%n",
                        i.getTurma().getNome(),
                        i.getFaltasMes(),
                        limiteFaltas);
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Nenhuma inscrição ativa encontrada.");
        }
        System.out.println("============================");
    }

    static boolean cpfJaExiste(String cpf) {
        cpf = cpf.trim();

        for (Aluno a : alunos) {
            if (a.getCpf() != null && a.getCpf().trim().equals(cpf)) {
                return true;
            }
        }
        return false;
    }

    static void cadastroModalidade() {
        System.out.println("-------------- CADASTRO DE MODALIDADE --------------");
        System.out.print("Digite o nome da modalidade: ");
        String nome = leia.nextLine();

        System.out.print("Digite a idade mínima: ");
        int idadeMinima = leia.nextInt();

        System.out.print("Digite a idade máxima: ");
        int idadeMaxima = leia.nextInt();

        System.out.print("Digite o limite de faltas mensais: ");
        int limiteFaltas = leia.nextInt();

        Modalidade novaModalidade = new Modalidade(nome, idadeMinima, idadeMaxima, limiteFaltas);

        modalidades.add(novaModalidade);
        ModalidadeCSV.salvar(novaModalidade);
    }

    static void verTurmas(Professor professor){
        
    }

    static void menuAluno(Aluno aluno) {

        int opcao = -1;

        while (opcao != 0) {
            System.out.println("---------- MENU DO ALUNO ----------");
            System.out.println("  Logado: " + aluno.getNome());
            System.out.println("");
            System.out.println("  1. Ver meu perfil               ");
            System.out.println("  2. Ver modalidades dispon?veis  ");
            System.out.println("  3. Realizar inscricao           ");
            System.out.println("  4. Ver minhas inscricoes        ");
            System.out.println("  5. Ver minha frequencia         ");
            System.out.println("  0. Logout                       ");
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

                case 0:
                    System.out.println("Logout realizado. Ate logo, " + aluno.getNome() + "!");
                    break;

                default:
                    System.out.println("Op??o inv?lida!");
            }
        }
    }

    static void menuAdministrador(Admin admin) {

        int opcao = -1;

        while (opcao != 0) {

            System.out.println("\n===== MENU ADMINISTRADOR =====");
            System.out.println("Logado: " + admin.getNome());
            System.out.println("1. Ver perfil");
            System.out.println("2. Listar alunos");
            System.out.println("3. Cadastrar professor");
            System.out.println("4. Criar turma");
            System.out.println("5. Cadastrar novo administrador");
            System.out.println("6. Cadastrar nova modalidade");
            System.out.println("7. Listar professores");
            System.out.println("0. Logout");
            System.out.print("Escolha: ");

            opcao = leia.nextInt();
            leia.nextLine();

            switch (opcao) {

                case 1:
                    admin.exibirPerfil();
                    break;

                case 2:

                    if (alunos.isEmpty()) {
                        System.out.println("Nenhum aluno cadastrado.");
                    } else {

                        for (Aluno a : alunos) {
                            a.exibirPerfil();
                        }
                    }

                    break;

                case 3:
                    cadastroProfessor();
                    break;

                case 4:
                    cadastroTurma();
                    break;

                case 5:
                    cadastroAdmin();
                    break;

                case 6:
                    cadastroModalidade();
                    break;

                case 7:
                    if (professores.isEmpty()) {
                        System.out.println("Nenhum professor cadastrado.");
                    } else {
                        for (Professor p : professores) {
                            p.exibirPerfil();
                        }
                    }
                    break;

                case 0:
                    System.out.println("Logout realizado.");
                    break;

                default:
                    System.out.println("Opção inválida.");
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
            System.out.println("4. Registrar falta");
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
                    // verAlunosDaTurma(professor);
                    break;

                case 4:
                    // registrarFaltaProfessor(professor);
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