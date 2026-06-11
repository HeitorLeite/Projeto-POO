package model;

import interfaces.Persistivel;
import java.time.LocalDate;
import java.time.Period;

public class Aluno extends Usuario implements Persistivel {

    private LocalDate dataNascimento;
    private String responsavelNome;
    private String responsavelTelefone;
    private int totalInscricoes;

    public Aluno(String nome, String cpf, String email, String telefone, String senha,
            LocalDate dataNascimento, String responsavelNome,
            String responsavelTelefone) {

        super(nome, cpf, email, telefone, senha);

        this.dataNascimento = dataNascimento;
        this.responsavelNome = responsavelNome;
        this.responsavelTelefone = responsavelTelefone;
        this.totalInscricoes = 0;
    }

    @Override
    public void exibirPerfil() {
        System.out.println("\n========== PERFIL DO ALUNO ==========");
        System.out.println("Nome        : " + getNome());
        System.out.println("CPF         : " + getCpf());
        System.out.println("Email       : " + getEmail());
        System.out.println("Idade       : " + getIdade() + " anos");
        System.out.println("Inscricoes  : " + totalInscricoes + "/2");

        // S? mostra respons?vel se for menor de 18 (RN007)
        if (ehMenorDeIdade()) {
            System.out.println("Respons?vel : " + responsavelNome);
            System.out.println("Telefone    : " + responsavelTelefone);
        }

        System.out.println("======================================");
    }

    @Override
    public void notificar(String mensagem) {
        super.notificar(mensagem);

        if (ehMenorDeIdade() && !responsavelTelefone.isEmpty()) {
            System.out.println(
                    "[AVISO AO RESPONSAVEL - " + responsavelNome + " | " + responsavelTelefone + "] " + mensagem);
        }
    }

    public int getIdade() {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    // Retorna true se tiver menos de 18 anos
    public boolean ehMenorDeIdade() {
        return getIdade() < 18;
    }

    public boolean podeInscrever() {
        return totalInscricoes < 2;
    }

    public void incrementarInscricoes() {
        totalInscricoes++;
    }

    public void decrementarInscricoes() {
        if (totalInscricoes > 0) {
            totalInscricoes--;
        }
    }

    @Override
    public String paraCSV() {
        return getNome() + ","
                + getCpf() + ","
                + getEmail() + ","
                + getTelefone() + ","
                + getSenha() + ","
                + "ALUNO" + ","
                + dataNascimento + ","
                + responsavelNome + ","
                + responsavelTelefone;
    }

    @Override
    public String getCabecalhoCSV() {
        return "nome,cpf,email,telefone,senha,tipo,dataNascimento,responsavelNome,responsavelTelefone";
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getResponsavelNome() {
        return responsavelNome;
    }

    public String getResponsavelTelefone() {
        return responsavelTelefone;
    }

    public int getTotalInscricoes() {
        return totalInscricoes;
    }

}