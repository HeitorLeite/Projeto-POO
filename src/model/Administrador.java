package model;

import enums.NivelAcesso;
import interfaces.Persistivel;

public class Administrador extends Usuario implements Persistivel {

		private NivelAcesso nivelAcesso;

		public Administrador(String nome, String cpf, String email,
							String telefone, String senha,
							NivelAcesso nivelAcesso) {

			super(nome, cpf, email, telefone, senha);
			this.nivelAcesso = nivelAcesso;
		}

		@Override
		public void exibirPerfil() {
			System.out.println("\n========== PERFIL DO ADMINISTRADOR ==========");
			System.out.println("Nome         : " + getNome());
			System.out.println("CPF          : " + getCpf());
			System.out.println("Email        : " + getEmail());
			System.out.println("Telefone     : " + getTelefone());
			System.out.println("Nivel Acesso : " + nivelAcesso);
        System.out.println("=============================================");
		}

		@Override
		public void notificar(String mensagem) {
			System.out.println("[ADMINISTRADOR - " + getNome() + "] " + mensagem);
		}

		public boolean temAcessoTotal() {
			return nivelAcesso == NivelAcesso.TOTAL;
		}

		@Override
		public String paraCSV() {
			return getNome() + ","
				+ getCpf() + ","
				+ getEmail() + ","
				+ getSenha() + ","
				+ "ADMINISTRADOR" + ","
				+ nivelAcesso;
		}

		@Override
		public String getCabecalhoCSV() {
			return "nome,cpf,email,senha,tipo,nivelAcesso";
		}

	public NivelAcesso getNivelAcesso() {
			return nivelAcesso;
		}

		public void setNivelAcesso(NivelAcesso nivelAcesso) {
			this.nivelAcesso = nivelAcesso;
		}
}
