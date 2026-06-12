package service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.Modalidade;
import model.Turma;

public class TurmaCSV {
    public static final String ARQUIVO = "dados/turmas.csv";

    public static void salvar(Turma turma) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO, true))) {
            bw.write(turma.paraCSV());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar turma: " + e.getMessage());
        }
    }

    public static List<Turma> carregar(List<Modalidade> modalidades) {
        List<Turma> turmas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty())
                    continue;

                String[] dados = linha.split(",");

                if (dados.length < 5) {
                    System.out.println("Linha inválida ignorada: " + linha);
                    continue;
                }

                String nomeTurma      = dados[0];
                String nomeModalidade = dados[1];
                String horario        = dados[2];
                int limiteAlunos      = Integer.parseInt(dados[3].trim());
                String professoresResponsaveis = dados.length > 4 ? dados[4] : "";
                boolean ativo         = dados.length > 5 ? Boolean.parseBoolean(dados[5].trim()) : true;

                // Busca a modalidade já carregada pelo nome
                Modalidade modalidade = null;
                for (Modalidade m : modalidades) {
                    if (m.getNome().equalsIgnoreCase(nomeModalidade)) {
                        modalidade = m;
                        break;
                    }
                }

                if (modalidade == null) {
                    System.out.println("Modalidade '" + nomeModalidade + "' não encontrada para a turma '" + nomeTurma + "'. Linha ignorada.");
                    continue;
                }

                Turma turma = new Turma(nomeTurma, modalidade, horario, limiteAlunos, professoresResponsaveis);

                if (!ativo) {
                    turma.inativar();
                }

                turmas.add(turma);
            }

        } catch (IOException e) {
            System.out.println("Arquivo CSV de turmas ainda não existe.");
        }

        return turmas;
    }
}