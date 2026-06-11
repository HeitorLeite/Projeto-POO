package service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Admin;

public class AdminCSV {
    public static final String ARQUIVO = "dados/admins.csv";

    public static void salvar(Admin admin) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO, true))) {
            bw.write(admin.paraCSV());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar admin: " + e.getMessage());
        }
    }

    public static List<Admin> carregar() {

        List<Admin> admins = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty())
                    continue;

                String[] dados = linha.split(",");

                if (dados.length < 6) {
                    System.out.println("Linha invalida ignorada: " + linha);
                    continue;
                }

                String nome = dados[0];
                String cpf = dados[1];
                String email = dados[2];
                String telefone = dados.length > 3 ? dados[3] : "";
                String senha = dados[4];
                LocalDate dataNascimento = LocalDate.parse(dados[6]);
                String setor = dados.length > 6 ? dados[7] : "";
                byte permissao = dados.length > 7 ? Byte.parseByte(dados[8].trim()) : 0;

                Admin novoAdmin = new Admin(
                        nome,
                        cpf,
                        email,
                        telefone,
                        senha,
                        dataNascimento,
                        setor,
                        permissao);

                admins.add(novoAdmin);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar admins: " + e.getMessage());
        }
        return admins;
    }
}
