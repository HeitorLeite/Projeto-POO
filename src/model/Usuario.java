package model;

import interfaces.Notificavel;

public abstract class Usuario implements Notificavel {

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

    public boolean autenticar(String cpfDigitado, String senhaDigitada) {
        return this.cpf.equals(cpfDigitado) && this.senha.equals(senhaDigitada);
    }

    @Override
    public void notificar(String mensagem) {
        System.out.println("[NOTIFICA??O para " + nome + "] " + mensagem);
    }


    public String getNome()  { return nome; }
    public String getCpf()   { return cpf; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getSenha() { return senha; }

    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

}