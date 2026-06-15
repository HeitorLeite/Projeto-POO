package model;

import interfaces.Persistivel;
import java.time.LocalDate;
import java.time.Period;

public class Professor extends Usuario implements Persistivel {

    private LocalDate dataNascimento;
    private String modalidade;

    public Professor(String nome, String cpf, String email, String telefone, String senha, LocalDate dataNascimento,
            String modalidade) {
        super(nome, cpf, email, telefone, senha);
        this.dataNascimento = dataNascimento;
        this.modalidade = modalidade;
    }

    @Override
    public void exibirPerfil() {
        System.out.println("\n========== PERFIL DO PROFESSOR ==========");
        System.out.println("Nome        : " + getNome());
        System.out.println("CPF         : " + getCpf());
        System.out.println("Email       : " + getEmail());
        System.out.println("Idade       : " + getIdade() + " anos");
        System.out.println("modalidade: " + getModalidade());
        System.out.println("========================================");
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public int getIdade() {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public String getModalidade() {
        return modalidade;
    }

    @Override
    public String paraCSV() {
        return getNome() + ","
                + getCpf() + ","
                + getEmail() + ","
                + getTelefone() + ","
                + getSenha() + ","
                + "PROFESSOR" + ","
                + dataNascimento + ","
                + modalidade;
    }

    @Override
    public String getCabecalhoCSV() {
        return "nome,cPF,email,telefone,senha,tipo,dataNascimento,modalidade";
    }
}