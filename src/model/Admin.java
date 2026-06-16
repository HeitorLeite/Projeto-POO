package model;

import interfaces.Persistivel;
import java.time.LocalDate;
import java.time.Period;

public class Admin extends Usuario implements Persistivel {

    private LocalDate dataNascimento;
    private String setor;
    private byte permissao;

    public Admin(String nome, String cpf, String email, String telefone, String senha, LocalDate dataNascimento,
            String setor, byte permissao) {
        super(nome, cpf, email, telefone, senha);

        this.dataNascimento = dataNascimento;
        this.setor = setor;
        this.permissao = permissao;
    }

    @Override
    public void exibirPerfil() {
        System.out.println("\n========== PERFIL DO ADMIN ==========");
        System.out.println("Nome   : " + getNome());
        System.out.println("CPF    : " + getCpf());
        System.out.println("Email  : " + getEmail());
        System.out.println("Idade  : " + getIdade() + " anos");
        System.out.println("Setor  : " + getSetor());
        System.out.println("Permissao  : " + getPermissao());
        System.out.println("====================================");

        if (permissao == 0) {
            System.out.println("Voce nao tem permissao para acessar as funcionalidades administrativas.");
            return;
        }

        System.out.println("======================================");
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

    public byte getPermissao() {
        return permissao;
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
                + permissao;
    }

    @Override
    public String getCabecalhoCSV() {
        return "nome,cpf,email,telefone,senha,tipo,dataNascimento,setor,permissao";
    }
}
