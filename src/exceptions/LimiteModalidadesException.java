package exceptions;

public class LimiteModalidadesException extends Exception {

    public LimiteModalidadesException(String nomeAluno) {
        super("Aluno " + nomeAluno + " ja esta inscrito em 2 modalidades. Limite atingido (RN001).");
    }
}