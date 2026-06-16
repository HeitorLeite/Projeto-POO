package model;

import enums.StatusInscricao;
import java.time.LocalDate;

public class Inscricao {

    private Aluno          aluno;
    private Turma          turma;
    private StatusInscricao status;
    private LocalDate      dataInscricao;
    private int            faltasMes;

    public Inscricao(Aluno aluno, Turma turma) {
        this.aluno         = aluno;
        this.turma         = turma;
        this.status        = StatusInscricao.ATIVA;
        this.dataInscricao = LocalDate.now();
        this.faltasMes     = 0;
    }

    public void registrarFalta() {
        faltasMes++;
        if (faltasMes == 2) {
            aluno.notificar("Voce ja tem 2 faltas em " + turma.getNome() + ". Cuidado com o cancelamento!");
        }
    }

    public boolean ultrapassouLimiteFaltas() {
        return faltasMes > turma.getModalidade().getLimiteFaltasMensais();
    }

    public void cancelar() {
        this.status = StatusInscricao.CANCELADA;
    }

    public void colocarEmEspera() {
        this.status = StatusInscricao.LISTA_ESPERA;
    }

    public Aluno           getAluno()         { return aluno; }
    public Turma           getTurma()         { return turma; }
    public StatusInscricao getStatus()        { return status; }
    public LocalDate       getDataInscricao() { return dataInscricao; }
    public int             getFaltasMes()     { return faltasMes; }
}