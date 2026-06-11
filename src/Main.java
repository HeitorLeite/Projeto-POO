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
import model.Turma;
import service.AdminCSV;
import service.AlunoCSV;
import service.InscricaoService;

public class Main {

    static Scanner leia = new Scanner(System.in);
    static List<Aluno> alunos;
    static List<Turma> turmas = new ArrayList<>();
    static List<Inscricao> inscricoes = new ArrayList<>();
    static InscricaoService inscricaoService = new InscricaoService();
    static List<Admin> administradores = new ArrayList<>();

    public static void main(String[] args) {
        alunos = AlunoCSV.carregar();
        administradores = AdminCSV.carregar();
        popularDadosDemo();
        menuEntrada();
    }

    static void popularDadosDemo() {
        Modalidade natacao = new Modalidade("Natação", 5, 60, 3);
        Modalidade futebol = new Modalidade("Futebol", 10, 40, 3);
        Modalidade ginastica = new Modalidade("Ginástica", 6, 30, 3);

        turmas.add(new Turma("Natação Manhã", natacao, "08:00", 2));
        turmas.add(new Turma("Futebol Tarde", futebol, "15:00", 10));
        turmas.add(new Turma("Ginástica Manhã", ginastica, "09:00", 10));
    }

    static void menuEntrada() {

        int opcao = -1;

        while (opcao != 4) {

            System.out.println("-------------- SEJA BEM-VINDO! --------------");
            System.out.println("1. Fazer login");
            System.out.println("2. Cadastrar-se");
            System.out.println("3. Cadastrar administrador");
            System.out.println("4. Sair");
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
                    cadastroAdmin();
                    break;
                case 4:
                    System.out.println("Saindo... Tenha um bom dia!");
                    break;

                default:
                    System.out.println("Opcao invalida! Digite 1, 2, 3 ou 4.");
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
                    System.out.println("(funcionalidade a implementar)");
                    break;

                case 4:
                    System.out.println("(funcionalidade a implementar)");
                    break;

                case 0:
                    System.out.println("Logout realizado.");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}