package com.restaurante.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * FACTORY METHOD: centraliza e encapsula a criação de conexões com o banco.
 * O restante do sistema não sabe (nem precisa saber) como uma Connection é montada.
 */
public class ConnectionFactory {

    private static final String URL = "jdbc:mysql://localhost:3306/restaurante?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection criarConexao() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }
}
