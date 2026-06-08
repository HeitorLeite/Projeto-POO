package exceptions;

public class VagaIndisponivel extends Exception{
    public VagaIndisponivel(String nomeTurma){
        super("Turma " + nomeTurma + " está lotada. Aluno adicionado à lista de espera.");
    }
}