package service;

import exceptions.IdadeIncompativelException;
import exceptions.LimiteFaltasExcedidoException;
import exceptions.LimiteModalidadesException;
import exceptions.VagaIndisponivelException;
import model.Aluno;
import model.Inscricao;
import model.Turma;

public class InscricaoService {

    public Inscricao realizarInscricao(Aluno aluno, Turma turma)
            throws LimiteModalidadesException, IdadeIncompativelException, VagaIndisponivelException {

        if (!aluno.podeInscrever()) {
            throw new LimiteModalidadesException(aluno.getNome());
        }

        int idade = aluno.getIdade();
        if (!turma.getModalidade().idadeCompativel(idade)) {
            throw new IdadeIncompativelException(
                    aluno.getNome(),
                    idade,
                    turma.getModalidade().getIdadeMinima(),
                    turma.getModalidade().getIdadeMaxima());
        }

        if (!turma.temVaga()) {
            turma.adicionarListaEspera(aluno);
            throw new VagaIndisponivelException(turma.getNome());
        }

        turma.adicionarAluno(aluno);
        aluno.incrementarInscricoes();

        Inscricao inscricao = new Inscricao(aluno, turma);
        aluno.notificar("Inscricao confirmada na turma " + turma.getNome() + "!");

        return inscricao;
    }

    public void verificarECancelarPorFaltas(Inscricao inscricao) throws LimiteFaltasExcedidoException {
        if (inscricao.ultrapassouLimiteFaltas()) {
            throw new LimiteFaltasExcedidoException(
                    inscricao.getAluno().getNome(),
                    inscricao.getTurma().getNome(),
                    inscricao.getFaltasMes(),
                    inscricao.getTurma().getModalidade().getLimiteFaltasMensais());
        }
    }

    public void cancelarInscricao(Inscricao inscricao) {
        inscricao.cancelar();

        Turma turma = inscricao.getTurma();
        turma.getAlunosInscritos().remove(inscricao.getAluno());
        inscricao.getAluno().decrementarInscricoes();

        Aluno proximo = turma.proximoDaListaEspera();
        if (proximo != null) {
            turma.adicionarAluno(proximo);
            proximo.incrementarInscricoes();
            proximo.notificar("Uma vaga foi liberada na turma " + turma.getNome()
                    + " e voce foi inscrito automaticamente!");
        }
    }
}