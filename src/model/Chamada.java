package model;

import java.time.LocalDate;
import java.util.List;

public class Chamada {

    private Turma turma;
    private LocalDate data;
    private String conteudo;
    private String[][] presenca;

    public Chamada(Turma turma, LocalDate data, String conteudo) {
        this.turma = turma;
        this.data = data;
        this.conteudo = conteudo;

        List<Aluno> alunos = turma.getAlunosInscritos();
        this.presenca = new String[alunos.size()][2];

        for (int i = 0; i < alunos.size(); i++) {
            presenca[i][0] = alunos.get(i).getNome();
            presenca[i][1] = "F";
        }
    }

    public void marcarPresente(int indice) {
        if (indice >= 0 && indice < presenca.length) {
            presenca[indice][1] = "P";
        }
    }

    public void marcarJustificado(int indice) {
        if (indice >= 0 && indice < presenca.length) {
            presenca[indice][1] = "J";
        }
    }

    public int contarStatus(String status) {
        int total = 0;
        for (String[] linha : presenca) {
            if (linha[1].equals(status)) {
                total++;
            }
        }
        return total;
    }

    public String getStatusAluno(String nome) {
        for (String[] linha : presenca) {
            if (linha[0].equalsIgnoreCase(nome)) {
                return linha[1];
            }
        }
        return null;
    }

    public void exibirChamada() {
        System.out.println("\n===== CHAMADA: " + turma.getNome() + " | " + data + " =====");
        System.out.println("Conteudo da aula: " + conteudo);
        System.out.printf("%-25s | %s%n", "Aluno", "Status");
        System.out.println("--------------------------------------");
        for (String[] linha : presenca) {
            System.out.printf("%-25s | %s%n", linha[0], linha[1]);
        }
        System.out.println("--------------------------------------");
        System.out.printf("Presentes: %d | Faltas: %d | Justificadas: %d%n",
                contarStatus("P"), contarStatus("F"), contarStatus("J"));
        System.out.println("========================================");
    }

    public Turma getTurma() {
        return turma;
    }

    public LocalDate getData() {
        return data;
    }

    public String getConteudo() {
        return conteudo;
    }

    public String[][] getPresenca() {
        return presenca;
    }
}