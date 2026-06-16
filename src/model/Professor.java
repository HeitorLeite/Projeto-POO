package model;

import interfaces.Persistivel;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import service.ChamadaCSV;

public class Professor extends Usuario implements Persistivel {

    private LocalDate dataNascimento;
    private String modalidade;

    public Professor(String nome, String cpf, String email, String telefone, String senha, LocalDate dataNascimento,
            String modalidade) {
        super(nome, cpf, email, telefone, senha);
        this.dataNascimento = dataNascimento;
        this.modalidade = modalidade;
    }

    @Override
    public void exibirPerfil() {
        System.out.println("\n========== PERFIL DO PROFESSOR ==========");
        System.out.println("Nome        : " + getNome());
        System.out.println("CPF         : " + getCpf());
        System.out.println("Email       : " + getEmail());
        System.out.println("Idade       : " + getIdade() + " anos");
        System.out.println("modalidade: " + getModalidade());
        System.out.println("========================================");
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


    public void registrarFaltaProfessor(Professor professor, List<Turma> turmas, Scanner scanner) {

    List<Turma> turmasDoProfessor = new ArrayList<>();

    for (Turma turma : turmas) {
        if (turma.getProfessoresResponsaveis().contains(professor)) {
            turmasDoProfessor.add(turma);
        }
    }

    if (turmasDoProfessor.isEmpty()) {
        System.out.println("Voce nao esta vinculado a nenhuma turma.");
        return;
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
        return;
    }

    Turma turmaEscolhida = turmasDoProfessor.get(escolha - 1);
    List<Aluno> alunos = turmaEscolhida.getAlunosInscritos();

    if (alunos.isEmpty()) {
        System.out.println("Esta turma nao possui alunos inscritos.");
        return;
    }

    String[][] presencas = new String[alunos.size()][2];

    System.out.println("\n--- Registrar Chamada: " + turmaEscolhida.getNome() + " ---");
    System.out.println("Digite P para Presente ou F para Falta\n");

    for (int i = 0; i < alunos.size(); i++) {
        Aluno aluno = alunos.get(i);

        System.out.print(aluno.getNome() + ": ");
        String status = scanner.nextLine().trim().toUpperCase();

        while (!status.equals("P") && !status.equals("F")) {
            System.out.print("Opcao invalida. Digite P ou F: ");
            status = scanner.nextLine().trim().toUpperCase();
        }

        presencas[i][0] = aluno.getCpf();
        presencas[i][1] = status;
    }

    Chamada chamada = new Chamada(turmaEscolhida);
    chamada.registrarPresenca(LocalDate.now(), presencas);

    ChamadaCSV.salvar(chamada);

    System.out.println("\nChamada registrada com sucesso!");
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

    @Override
    public String paraCSV() {
        return getNome() + ","
                + getCpf() + ","
                + getEmail() + ","
                + getTelefone() + ","
                + getSenha() + ","
                + "PROFESSOR" + ","
                + dataNascimento + ","
                + modalidade;
    }

    @Override
    public String getCabecalhoCSV() {
        return "nome,cPF,email,telefone,senha,tipo,dataNascimento,modalidade";
    }
}