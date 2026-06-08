package model;

public class Modalidade {
    private String nome;
    private int idadeMinima;
    private int idadeMaxima;
    private int limiteFaltasMensais;
    private boolean ativo;

    public Modalidade(String nome, int idadeMinima, int idadeMaxima, int limiteFaltasMensais) {
        this.nome = nome;
        this.idadeMinima = idadeMinima;
        this.idadeMaxima = idadeMaxima;
        this.limiteFaltasMensais = limiteFaltasMensais;
        this.ativo = true;
    }
    
    public boolean idadeCompativel(int idade){
        return idade >= idadeMinima && idade <= idadeMaxima;
    }

    public void inativar(){
        this.ativo = false;
    }

    public String getNome() {return nome;}
    public int getIdadeMinima() {return idadeMinima;}
    public int getIdadeMaxima() {return idadeMaxima;}
    public int getLimiteFaltasMensais() {return limiteFaltasMensais;}
    public boolean isAtivo() {return ativo;}
}