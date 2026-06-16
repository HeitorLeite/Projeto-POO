package model;

import interfaces.Notificavel;
import interfaces.Persistivel;

public abstract class Usuario implements Notificavel, Persistivel {

    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String senha;

    public Usuario(String nome, String cpf, String email, String telefone, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
    }

    public abstract void exibirPerfil();

    @Override
    public abstract String paraCSV();

    @Override
    public abstract String getCabecalhoCSV();

    public boolean autenticar(String cpfDigitado, String senhaDigitada) {
        return this.cpf.equals(cpfDigitado) && this.senha.equals(senhaDigitada);
    }

    @Override
    public void notificar(String mensagem) {
        System.out.println("[NOTIFICACAO para " + nome + "] " + mensagem);
    }

    @Override
    public String getContatoNotificacao() {
        return telefone;
    }

    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getSenha() { return senha; }

    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}