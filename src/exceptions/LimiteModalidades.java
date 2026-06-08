package exceptions;

public class LimiteModalidades extends Exception {
    public LimiteModalidades(String nomeAluno){
        super("Aluno " + nomeAluno + " já está inscrito em 2 modalidades. Limite atingido");
    }
}