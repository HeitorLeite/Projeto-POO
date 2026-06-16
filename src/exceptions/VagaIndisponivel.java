package exceptions;

public class VagaIndisponivel extends Exception{
    public VagaIndisponivel(String nomeTurma){
        super("Turma " + nomeTurma + " esta lotada. Aluno adicionado a lista de espera.");
    }
}