package enums;

public enum StatusInscricao {

    ATIVA("Inscrição ativa"),
    CANCELADA("Inscrição cancelada"),
    LISTA_ESPERA("Aguardando vaga na lista de espera"),
    PROCESSO_SELETIVO("Em processo seletivo");

    private final String descricao;

    StatusInscricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}