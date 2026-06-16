package service;

import java.io.*;
import model.Chamada;

public class ChamadaCSV {

    public static final String ARQUIVO = "dados/chamadas.csv";

    public static void salvar(Chamada chamada) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO, true))) {

            for (String[] registro : chamada.getRegistros()) {

                StringBuilder linha = new StringBuilder();
                linha.append(chamada.getTurma().getNome()).append(",");

                for (int i = 0; i < registro.length; i++) {
                    linha.append(registro[i]);
                    if (i < registro.length - 1) {
                        linha.append(",");
                    }
                }

                bw.write(linha.toString());
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Erro ao salvar chamada: " + e.getMessage());
        }
    }
}