package util;

import interfaces.Persistivel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorArquivo {

    private static final String PASTA_DADOS = "dados";

    private static void garantirPastaDados() {
        File dir = new File(PASTA_DADOS);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static <T extends Persistivel> void salvar(List<T> lista, String nomeArquivo) {
        garantirPastaDados();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivo))) {

            if (!lista.isEmpty()) {
                bw.write(lista.get(0).getCabecalhoCSV());
                bw.newLine();
            }

            for (T item : lista) {
                bw.write(item.paraCSV());
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo " + nomeArquivo + ": " + e.getMessage());
        }
    }

    public static void adicionar(Persistivel item, String nomeArquivo) {
        garantirPastaDados();

        File arquivo = new File(nomeArquivo);
        boolean existia = arquivo.exists() && arquivo.length() > 0;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivo, true))) {

            if (!existia) {
                bw.write(item.getCabecalhoCSV());
                bw.newLine();
            }

            bw.write(item.paraCSV());
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Erro ao salvar em " + nomeArquivo + ": " + e.getMessage());
        }
    }

    public static List<String[]> carregar(String nomeArquivo) {

        List<String[]> linhas = new ArrayList<>();
        File arquivo = new File(nomeArquivo);

        if (!arquivo.exists()) {
            return linhas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {

            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {

                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                if (linha.trim().isEmpty()) {
                    continue;
                }

                linhas.add(linha.split(","));
            }

        } catch (IOException e) {
            System.out.println("Erro ao carregar arquivo " + nomeArquivo + ": " + e.getMessage());
        }

        return linhas;
    }

    public static void salvarRelatorioPresenca(String[][] presenca, String nomeArquivo) {
        garantirPastaDados();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivo))) {

            bw.write("aluno,status");
            bw.newLine();

            for (String[] linha : presenca) {
                bw.write(linha[0] + "," + linha[1]);
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Erro ao salvar relatorio de presenca: " + e.getMessage());
        }
    }
}