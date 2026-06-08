import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import exceptions.IdadeIncompativel;
import exceptions.LimiteModalidades;
import exceptions.VagaIndisponivel;
import model.Aluno;
import model.Inscricao;
import model.Modalidade;
import model.Turma;
import service.AlunoCSV;
import service.InscricaoService;

public class Main {

    static Scanner leia = new Scanner(System.in);
    static List<Aluno> alunos;
    static List<Turma>      turmas     = new ArrayList<>();
    static List<Inscricao>  inscricoes = new ArrayList<>();
    static InscricaoService inscricaoService = new InscricaoService();

    public static void main(String[] args) {
        alunos = AlunoCSV.carregar();
        popularDadosDemo(); //Teste para criação de turmas manualmente
        menuEntrada();
    }

    static void popularDadosDemo() {
        Modalidade natacao  = new Modalidade("Natação",   5, 60, 3);
        Modalidade futebol  = new Modalidade("Futebol",  10, 40, 3);
        Modalidade ginastica = new Modalidade("Ginástica", 6, 30, 3);

        turmas.add(new Turma("Natação Manhã",    natacao,   "08:00", 2));
        turmas.add(new Turma("Futebol Tarde",    futebol,   "15:00", 10));
        turmas.add(new Turma("Ginástica Manhã",  ginastica, "09:00", 10));
    }

    static void menuEntrada() {

        int opcao = -1;

        while (opcao != 3) {

            System.out.println("-------------- SEJA BEM-VINDO! --------------");
            System.out.println("1. Fazer login");
            System.out.println("2. Cadastrar-se");
            System.out.println("3. Sair");
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
                    System.out.println("Opcao invalida! Digite 1, 2 ou 3");
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

        System.out.print("Telefone do responsavel (deixe em branco se maior de 18): ");
        String responsavelTelefone = leia.nextLine();

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
            responsavelTelefone
        );


        alunos.add(novoAluno);
        AlunoCSV.salvar(novoAluno);

        System.out.println("Cadastro realizado com sucesso! Bem-vindo, " + nome + "!");
        novoAluno.exibirPerfil();
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
                t.getLimiteAlunos()
            );
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
                    System.out.println("(funcionalidade a implementar)");
                    break;
                case 3:
                    realizarInscricao(aluno);
                    break;

                case 4:
                    System.out.println("(funcionalidade a implementar)");
                    break;

                case 5:
                    System.out.println("(funcionalidade a implementar)");
                    break;

                case 0:
                    System.out.println("Logout realizado. Ate logo, " + aluno.getNome() + "!");
                    break;

                default:
                    System.out.println("Op??o inv?lida!");
            }
        }
    }
}