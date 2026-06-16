package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Chamada {

    private Turma turma;
    private List<String[]> registros;

    public Chamada(Turma turma) {
        this.turma = turma;
        this.registros = new ArrayList<>();
    }


    public void registrarPresenca(LocalDate data, String[][] presencas) {
    
        String[] registro = new String[1 + presencas.length * 2];
        registro[0] = data.toString();

        for (int i = 0; i < presencas.length; i++) {
            registro[1 + (i * 2)] = presencas[i][0]; // cpf
            registro[2 + (i * 2)] = presencas[i][1]; // "P" ou "F"
        }

        registros.add(registro);
    }

    public Turma getTurma() {
        return turma;
    }

    public List<String[]> getRegistros() {
        return registros;
    }
}