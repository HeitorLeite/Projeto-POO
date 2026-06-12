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

    public void exibirModalidade() {
        System.out.println("\n========== MODALIDADE ==========");
        System.out.println("Nome                : " + nome);
        System.out.println("Idade Mínima       : " + idadeMinima + " anos");
        System.out.println("Idade Máxima       : " + idadeMaxima + " anos");
        System.out.println("Limite Faltas/Mês  : " + limiteFaltasMensais);
        System.out.println("Status              : " + (ativo ? "Ativa" : "Inativa"));
        System.out.println("================================");
    }

    public boolean idadeCompativel(int idade) {
        return idade >= idadeMinima && idade <= idadeMaxima;
    }

    public void inativar() {
        this.ativo = false;
    }

    public String getNome() {
        return nome;
    }

    public int getIdadeMinima() {
        return idadeMinima;
    }

    public int getIdadeMaxima() {
        return idadeMaxima;
    }

    public int getLimiteFaltasMensais() {
        return limiteFaltasMensais;
    }

    public boolean isAtivo() {
        return ativo;
    }

}