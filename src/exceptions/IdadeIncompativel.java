package exceptions;
public class IdadeIncompativel extends Exception{
    public IdadeIncompativel(String nomeAluno, int idade, int minimo, int maximo){
         super("Aluno " + nomeAluno + " (" + idade + " anos) fora da faixa " + minimo + "-" + maximo + " anos desta modalidade.");
    }
}