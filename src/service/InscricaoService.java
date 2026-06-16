package service;

import exceptions.IdadeIncompativel;
import exceptions.LimiteModalidades;
import exceptions.VagaIndisponivel;
import model.Aluno;
import model.Inscricao;
import model.Turma;

public class InscricaoService {

    public Inscricao realizarInscricao(Aluno aluno, Turma turma)
            throws LimiteModalidades, IdadeIncompativel, VagaIndisponivel {

        if (!aluno.podeInscrever()) {
            throw new LimiteModalidades(aluno.getNome());
        }

        int idade = aluno.getIdade();
        if (!turma.getModalidade().idadeCompativel(idade)) {
            throw new IdadeIncompativel(
                aluno.getNome(),
                idade,
                turma.getModalidade().getIdadeMinima(),
                turma.getModalidade().getIdadeMaxima()
            );
        }

        if (!turma.temVaga()) {
            turma.adicionarListaEspera(aluno);
            Inscricao inscricaoEspera = new Inscricao(aluno, turma);
            inscricaoEspera.colocarEmEspera();
            throw new VagaIndisponivel(turma.getNome());
        }

        turma.adicionarAluno(aluno);
        aluno.incrementarInscricoes();

        Inscricao inscricao = new Inscricao(aluno, turma);
        aluno.notificar("Inscricao confirmada na turma " + turma.getNome() + "!");

        return inscricao;
    }
}