package com.cocinaCasera.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = System.getenv()
            .getOrDefault("DB_URL",
                    "jdbc:mysql://localhost:3306/cocina_casera?serverTimezone=UTC");

    private static final String USUARIO = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    private ConexionBD() {
    }

    public static Connection conectar() throws SQLException {

        if (USUARIO == null || PASSWORD == null) {
            throw new SQLException(
                    "Configura las variables de entorno DB_USER y DB_PASSWORD.");
        }

        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}