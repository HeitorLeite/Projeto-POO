package service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Professor;

public class ProfessorCSV {
    public static final String ARQUIVO = "dados/professores.csv";

    public static void salvar(Professor professor) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO, true))) {
            bw.write(professor.paraCSV());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar professor: " + e.getMessage());
        }
    }

    public static List<Professor> carregar() {
        List<Professor> professores = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty())
                    continue;

                String[] dados = linha.split(",");

                if (dados.length < 8) {
                    System.out.println("Linha invalida ignorada: " + linha);
                    continue;
                }

                String nome = dados[0];
                String cpf = dados[1];
                String email = dados[2];
                String telefone = dados.length > 3 ? dados[3] : "";
                String senha = dados[4];
                LocalDate dataNascimento = LocalDate.parse(dados[6]);
                String modalidade = dados.length > 6 ? dados[7] : "";

                Professor novoProfessor = new Professor(
                        nome,
                        cpf,
                        email,
                        telefone,
                        senha,
                        dataNascimento,
                        modalidade);

                professores.add(novoProfessor);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar professores: " + e.getMessage());
        }

        return professores;
    }
}
