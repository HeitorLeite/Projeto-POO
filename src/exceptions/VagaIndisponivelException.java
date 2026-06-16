package exceptions;

public class VagaIndisponivelException extends Exception {

    public VagaIndisponivelException(String nomeTurma) {
        super("Turma " + nomeTurma + " esta lotada. Aluno adicionado a lista de espera (RN009/RN012).");
    }
}