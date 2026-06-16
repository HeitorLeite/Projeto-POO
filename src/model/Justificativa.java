package model;

import enums.StatusJustificativa;
import interfaces.Persistivel;

public class Justificativa implements Persistivel {

    private Aluno aluno;
    private Inscricao inscricao;
    private String motivo;
    private String caminhoArquivo;
    private StatusJustificativa status;

    public Justificativa(Aluno aluno, Inscricao inscricao, String motivo, String caminhoArquivo) {
        this.aluno = aluno;
        this.inscricao = inscricao;
        this.motivo = motivo;
        this.caminhoArquivo = caminhoArquivo;
        this.status = StatusJustificativa.PENDENTE;
    }

    public Justificativa(Aluno aluno, Inscricao inscricao, String motivo, String caminhoArquivo,
            StatusJustificativa status) {
        this.aluno = aluno;
        this.inscricao = inscricao;
        this.motivo = motivo;
        this.caminhoArquivo = caminhoArquivo;
        this.status = status;
    }

    public void aprovar() {
        this.status = StatusJustificativa.APROVADA;
        inscricao.descontarFaltaJustificada();
        aluno.notificar("Sua justificativa para a turma " + inscricao.getTurma().getNome() + " foi aprovada.");
    }

    public void recusar() {
        this.status = StatusJustificativa.RECUSADA;
        aluno.notificar("Sua justificativa para a turma " + inscricao.getTurma().getNome() + " foi recusada.");
    }

    public void exibirJustificativa() {
        System.out.println("\n========== JUSTIFICATIVA ==========");
        System.out.println("Aluno   : " + aluno.getNome());
        System.out.println("Turma   : " + inscricao.getTurma().getNome());
        System.out.println("Motivo  : " + motivo);
        System.out.println("Arquivo : " + caminhoArquivo);
        System.out.println("Status  : " + status + " (" + status.getDescricao() + ")");
        System.out.println("====================================");
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Inscricao getInscricao() {
        return inscricao;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    public StatusJustificativa getStatus() {
        return status;
    }

    @Override
    public String paraCSV() {
        return aluno.getCpf() + ","
                + inscricao.getTurma().getNome() + ","
                + motivo + ","
                + caminhoArquivo + ","
                + status;
    }

    @Override
    public String getCabecalhoCSV() {
        return "alunoCpf,turmaNome,motivo,caminhoArquivo,status";
    }
}