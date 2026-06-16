package service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.Modalidade;

public class ModalidadeCSV {

    public static final String ARQUIVO = "dados/modalidades.csv";

    public static void salvar(Modalidade modalidade) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO, true))) {
            bw.write(modalidade.paraCSV());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar modalidade: " + e.getMessage());
        }
    }

    public static List<Modalidade> carregar() {
        List<Modalidade> modalidades = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty())
                    continue;

                String[] dados = linha.split(",");

                if (dados.length < 4) {
                    System.out.println("Linha invalida ignorada: " + linha);
                    continue;
                }

                String nome = dados[0];
                int idadeMinima = Integer.parseInt(dados[1]);
                int idadeMaxima = Integer.parseInt(dados[2]);
                int limiteFaltasMensais = Integer.parseInt(dados[3]);
                boolean ativo = dados.length > 4 ? Boolean.parseBoolean(dados[4]) : true;

                Modalidade modalidade = new Modalidade(nome, idadeMinima, idadeMaxima, limiteFaltasMensais);

                if (!ativo) {
                    modalidade.inativar();
                }

                modalidades.add(modalidade);
            }

        } catch (IOException e) {
            System.out.println("Arquivo CSV de modalidades ainda nao existe.");
        }

        return modalidades;
    }
}