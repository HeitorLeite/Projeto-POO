package service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Aluno;

public class AlunoCSV {

    private static final String ARQUIVO = "dados/alunos.csv";

    public static void salvar(Aluno aluno) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO, true))) {
            bw.write(aluno.paraCSV());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar aluno: " + e.getMessage());
        }
    }

    public static List<Aluno> carregar() {

        List<Aluno> alunos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {

            String linha;

            while ((linha = br.readLine()) != null) {

                if (linha.trim().isEmpty())
                    continue;

                String[] dados = linha.split(",");

                if (dados.length < 6) {
                    System.out.println("Linha inv?lida ignorada: " + linha);
                    continue;
                }

                String nome = dados[0];
                String cpf = dados[1];
                String email = dados[2];
                String telefone = dados[3]; // não foi salvo no CSV
                String senha = dados[4];
                // dados[4] é "ALUNO" — ignorado
                LocalDate dataNascimento = LocalDate.parse(dados[6]);
                String responsavelNome = dados.length > 6 ? dados[7] : "";
                String responsavelTelefone = dados.length > 7 ? dados[8] : "";

                Aluno aluno = new Aluno(
                        nome,
                        cpf,
                        email,
                        telefone,
                        senha,
                        dataNascimento,
                        responsavelNome,
                        responsavelTelefone);

                alunos.add(aluno);
            }

        } catch (IOException e) {
            System.out.println("Arquivo CSV ainda n?o existe.");
        }

        return alunos;
    }
}