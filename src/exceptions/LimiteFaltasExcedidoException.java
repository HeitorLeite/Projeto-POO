package exceptions;

public class LimiteFaltasExcedidoException extends Exception {

    public LimiteFaltasExcedidoException(String nomeAluno, String nomeTurma, int faltas, int limite) {
        super("Aluno " + nomeAluno + " ultrapassou o limite de faltas na turma " + nomeTurma
                + " (" + faltas + "/" + limite + "). Inscricao cancelada automaticamente (RN005).");
    }
}