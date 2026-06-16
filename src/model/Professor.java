package model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import service.InscricaoService;

public class Professor extends Usuario {

    private LocalDate dataNascimento;
    private String modalidade;
    private String registro;
    private String especialidade;

    public Professor(String nome, String cpf, String email, String telefone, String senha,
            LocalDate dataNascimento, String modalidade, String registro, String especialidade) {
        super(nome, cpf, email, telefone, senha);
        this.dataNascimento = dataNascimento;
        this.modalidade = modalidade;
        this.registro = registro;
        this.especialidade = especialidade;
    }

    @Override
    public void exibirPerfil() {
        System.out.println("\n========== PERFIL DO PROFESSOR ==========");
        System.out.println("Nome          : " + getNome());
        System.out.println("CPF           : " + getCpf());
        System.out.println("Email         : " + getEmail());
        System.out.println("Idade         : " + getIdade() + " anos");
        System.out.println("Registro      : " + registro);
        System.out.println("Especialidade : " + especialidade);
        System.out.println("Modalidade    : " + modalidade);
        System.out.println("==========================================");
    }

    public void verAlunosDaTurma(Professor professor, List<Turma> turmas) {

        System.out.println("\n========== ALUNOS DAS SUAS TURMAS ==========");

        boolean temTurma = false;

        for (Turma turma : turmas) {

            if (turma.getProfessoresResponsaveis().contains(professor)) {

                temTurma = true;
                System.out.println("\nTurma: " + turma.getNome() + " (" + turma.getHorario() + ")");

                if (turma.getAlunosInscritos().isEmpty()) {
                    System.out.println("  Nenhum aluno inscrito.");
                } else {
                    for (Aluno aluno : turma.getAlunosInscritos()) {
                        System.out.println("  - " + aluno.getNome() + " | CPF: " + aluno.getCpf());
                    }
                }
            }
        }

        if (!temTurma) {
            System.out.println("Voce nao esta vinculado a nenhuma turma.");
        }

        System.out.println("=============================================");
    }

    public Chamada registrarFaltaProfessor(Professor professor, List<Turma> turmas,
            List<Inscricao> inscricoes, InscricaoService inscricaoService, Scanner scanner) {

        List<Turma> turmasDoProfessor = new ArrayList<>();

        for (Turma turma : turmas) {
            if (turma.getProfessoresResponsaveis().contains(professor)) {
                turmasDoProfessor.add(turma);
            }
        }

        if (turmasDoProfessor.isEmpty()) {
            System.out.println("Voce nao esta vinculado a nenhuma turma.");
            return null;
        }

        System.out.println("\n========== SUAS TURMAS ==========");
        for (int i = 0; i < turmasDoProfessor.size(); i++) {
            Turma t = turmasDoProfessor.get(i);
            System.out.println((i + 1) + " - " + t.getNome() + " (" + t.getHorario() + ")");
        }
        System.out.println("==================================");

        System.out.print("Escolha a turma (numero): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha < 1 || escolha > turmasDoProfessor.size()) {
            System.out.println("Opcao invalida.");
            return null;
        }

        Turma turmaEscolhida = turmasDoProfessor.get(escolha - 1);
        List<Aluno> alunos = turmaEscolhida.getAlunosInscritos();

        if (alunos.isEmpty()) {
            System.out.println("Esta turma nao possui alunos inscritos.");
            return null;
        }

        String conteudo;
        do {
            System.out.print("Conteudo da aula (obrigatorio): ");
            conteudo = scanner.nextLine().trim();
            if (conteudo.isBlank()) {
                System.out.println("O conteudo da aula nao pode ser vazio (RN014).");
            }
        } while (conteudo.isBlank());

        Chamada chamada = new Chamada(turmaEscolhida, LocalDate.now(), conteudo);

        System.out.println("\n--- Registrar Chamada: " + turmaEscolhida.getNome() + " ---");
        System.out.println("Digite P (presente), F (falta) ou J (falta justificada)\n");

        for (int i = 0; i < alunos.size(); i++) {
            Aluno aluno = alunos.get(i);

            System.out.print(aluno.getNome() + ": ");
            String status = scanner.nextLine().trim().toUpperCase();

            while (!status.equals("P") && !status.equals("F") && !status.equals("J")) {
                System.out.print("Opcao invalida. Digite P, F ou J: ");
                status = scanner.nextLine().trim().toUpperCase();
            }

            if (status.equals("P")) {
                chamada.marcarPresente(i);
            } else if (status.equals("J")) {
                chamada.marcarJustificado(i);
            }
            
            if (status.equals("F")) {
                for (Inscricao inscricao : inscricoes) {
                    if (inscricao.getAluno().getCpf().equals(aluno.getCpf())
                            && inscricao.getTurma() == turmaEscolhida
                            && inscricao.getStatus() == enums.StatusInscricao.ATIVA) {

                        inscricao.registrarFalta();

                        try {
                            inscricaoService.verificarECancelarPorFaltas(inscricao);
                        } catch (exceptions.LimiteFaltasExcedidoException e) {
                            System.out.println("AVISO: " + e.getMessage());
                            inscricaoService.cancelarInscricao(inscricao);
                        }
                        break;
                    }
                }
            }
        }

        chamada.exibirChamada();

        return chamada;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public int getIdade() {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public String getModalidade() {
        return modalidade;
    }

    public String getRegistro() {
        return registro;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    @Override
    public String paraCSV() {
        return getNome() + ","
                + getCpf() + ","
                + getEmail() + ","
                + getTelefone() + ","
                + getSenha() + ","
                + "PROFESSOR" + ","
                + dataNascimento + ","
                + modalidade + ","
                + registro + ","
                + especialidade;
    }

    @Override
    public String getCabecalhoCSV() {
        return "nome,cpf,email,telefone,senha,tipo,dataNascimento,modalidade,registro,especialidade";
    }
}