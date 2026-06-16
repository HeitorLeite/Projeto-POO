package model;

import interfaces.Persistivel;

public class Polo implements Persistivel {

    private String nome;
    private String endereco;
    private boolean ativo;

    public Polo(String nome, String endereco) {
        this.nome = nome;
        this.endereco = endereco;
        this.ativo = true;
    }

    public void exibirPolo() {
        System.out.println("\n========== POLO ==========");
        System.out.println("Nome     : " + nome);
        System.out.println("Endereco : " + endereco);
        System.out.println("Status   : " + (ativo ? "Ativo" : "Inativo"));
        System.out.println("===========================");
    }

    public void inativar() {
        this.ativo = false;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public boolean isAtivo() {
        return ativo;
    }

    @Override
    public String paraCSV() {
        return nome + "," + endereco + "," + ativo;
    }

    @Override
    public String getCabecalhoCSV() {
        return "nome,endereco,ativo";
    }
}