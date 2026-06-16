package enums;

public enum StatusJustificativa {

    PENDENTE("Aguardando avaliação"),
    APROVADA("Justificativa aprovada"),
    RECUSADA("Justificativa recusada");

    private final String descricao;

    StatusJustificativa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}