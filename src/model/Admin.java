package model;

import enums.NivelAcesso;
import java.time.LocalDate;
import java.time.Period;

public class Admin extends Usuario {

    private LocalDate dataNascimento;
    private String setor;
    private NivelAcesso nivelAcesso;

    public Admin(String nome, String cpf, String email, String telefone, String senha, LocalDate dataNascimento,
            String setor, NivelAcesso nivelAcesso) {
        super(nome, cpf, email, telefone, senha);

        this.dataNascimento = dataNascimento;
        this.setor = setor;
        this.nivelAcesso = nivelAcesso;
    }

    @Override
    public void exibirPerfil() {
        System.out.println("\n========== PERFIL DO ADMIN ==========");
        System.out.println("Nome       : " + getNome());
        System.out.println("CPF        : " + getCpf());
        System.out.println("Email      : " + getEmail());
        System.out.println("Idade      : " + getIdade() + " anos");
        System.out.println("Setor      : " + getSetor());
        System.out.println("Nivel      : " + nivelAcesso + " (" + nivelAcesso.getDescricao() + ")");
        System.out.println("======================================");

        if (!temAcessoTotal()) {
            System.out.println("Atencao: este administrador tem apenas acesso PARCIAL.");
        }
    }

    public boolean temAcessoTotal() {
        return nivelAcesso == NivelAcesso.TOTAL;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public int getIdade() {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public String getSetor() {
        return setor;
    }

    public NivelAcesso getNivelAcesso() {
        return nivelAcesso;
    }

    @Override
    public String paraCSV() {
        return getNome() + ","
                + getCpf() + ","
                + getEmail() + ","
                + getTelefone() + ","
                + getSenha() + ","
                + "ADMIN" + ","
                + dataNascimento + ","
                + setor + ","
                + nivelAcesso;
    }

    @Override
    public String getCabecalhoCSV() {
        return "nome,cpf,email,telefone,senha,tipo,dataNascimento,setor,nivelAcesso";
    }
}