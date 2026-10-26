package com.biblioteca.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexao {

    private static final Properties props = new Properties();

    // Bloco estatico: carrega o ficheiro de propriedades uma unica vez
    static {
        try (InputStream in = Conexao.class
                .getClassLoader()
                .getResourceAsStream("database.properties")) {

            if (in == null) {
                throw new RuntimeException("database.properties nao encontrado em src/main/resources");
            }
            props.load(in);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar database.properties", e);
        }
    }

    /**
     * Abre e devolve uma nova conexao com a base de dados.
     * Quem chama e responsavel por fechar (try-with-resources).
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
        );
    }

    /** Teste rapido da conexao. */
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println(" Conectado com sucesso!");
            System.out.println("   Catalogo : " + conn.getCatalog());
            System.out.println("   URL      : " + conn.getMetaData().getURL());
            System.out.println("   Driver   : " + conn.getMetaData().getDriverName());
            System.out.println("   Versao   : " + conn.getMetaData().getDriverVersion());
        } catch (SQLException e) {
            System.err.println(" Falha na conexao: " + e.getMessage());
            e.printStackTrace();
        }
    }
}