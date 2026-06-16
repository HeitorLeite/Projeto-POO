package enums;

public enum NivelAcesso {

    TOTAL("Acesso total às funcionalidades administrativas"),
    PARCIAL("Acesso parcial às funcionalidades administrativas");

    private final String descricao;

    NivelAcesso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}