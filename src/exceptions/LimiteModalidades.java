package exceptions;

public class LimiteModalidades extends Exception {
    public LimiteModalidades(String nomeAluno){
        super("Aluno " + nomeAluno + " ja esta inscrito em 2 modalidades. Limite atingido");
    }
}