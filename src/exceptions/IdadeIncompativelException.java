package exceptions;

public class IdadeIncompativelException extends Exception {

    public IdadeIncompativelException(String nomeAluno, int idade, int minimo, int maximo) {
        super("Aluno " + nomeAluno + " (" + idade + " anos) fora da faixa " + minimo + "-" + maximo
                + " anos desta modalidade (RN008).");
    }
}