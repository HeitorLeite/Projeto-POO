import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import model.Aluno;
import service.AlunoCSV;

public class Main {

    static Scanner leia = new Scanner(System.in);
    static List<Aluno> alunos;

    public static void main(String[] args) {
        alunos = AlunoCSV.carregar();

        menuEntrada();
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

    // RN016: verifica se CPF j? existe na lista de alunos
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
                    System.out.println("(funcionalidade a implementar)");
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