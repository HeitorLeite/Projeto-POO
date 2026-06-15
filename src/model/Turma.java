package model;

import interfaces.Persistivel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Turma implements Persistivel {

    private String nome;
    private Modalidade modalidade;
    private String horario;
    private int limiteAlunos;
    private boolean ativo;

    private List<Aluno> alunosInscritos;
    private Queue<Aluno> listaEspera;
    private List<Professor> professoresResponsaveis;

    public Turma(String nome, Modalidade modalidade, String horario, int limiteAlunos) {
        this.nome = nome;
        this.modalidade = modalidade;
        this.horario = horario;
        this.limiteAlunos = limiteAlunos;
        this.ativo = true;
        this.alunosInscritos = new ArrayList<>();
        this.listaEspera = new LinkedList<>();
        this.professoresResponsaveis = new ArrayList<>(); // <-- novo
    }

    public boolean temVaga() {
        return alunosInscritos.size() < limiteAlunos;
    }

    public void adicionarAluno(Aluno aluno) {
        alunosInscritos.add(aluno);
    }

    public void adicionarListaEspera(Aluno aluno) {
        listaEspera.add(aluno);
    }

    public Aluno proximoDaListaEspera() {
        return listaEspera.poll();
    }

    public void inativar() {
        this.ativo = false;
    }

    public String getNome() {
        return nome;
    }

    public Modalidade getModalidade() {
        return modalidade;
    }

    public String getHorario() {
        return horario;
    }

    public int getLimiteAlunos() {
        return limiteAlunos;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public List<Aluno> getAlunosInscritos() {
        return alunosInscritos;
    }

    public Queue<Aluno> getListaEspera() {
        return listaEspera;
    }

    public void setProfessor(Professor professor) {
        professoresResponsaveis.add(professor);
    }

    public void removeProfessor(Professor professor) {
        professoresResponsaveis.remove(professor);
    }

    public List<Professor> getProfessoresResponsaveis() {
        return professoresResponsaveis;
    }

    @Override
    public String paraCSV() {
        String cpfsProfessores = professoresResponsaveis.stream()
                .map(Professor::getCpf)
                .collect(java.util.stream.Collectors.joining(";"));

        return nome + ","
                + modalidade.getNome() + ","
                + horario + ","
                + limiteAlunos + ","
                + cpfsProfessores + ","
                + ativo;
    }

    @Override
    public String getCabecalhoCSV() {
        return "nome,modalidade,horario,limiteAlunos,professoresResponsaveis,ativo";
    }
}