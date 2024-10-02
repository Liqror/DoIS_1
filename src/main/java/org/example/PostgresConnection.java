package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {

    // Метод для подключения к базе данных
    public static Connection connect() {
        Connection conn = null;
        try {
            // Строка подключения к вашей базе данных
            String url = "jdbc:postgresql://localhost:5432/new_database";
            String user = "postgres";
            String password = "qwerty";

            // Подключение к базе данных
            conn = DriverManager.getConnection(url, user, password);
//            System.out.println("Подключение к базе данных успешно!");

        } catch (SQLException e) {
            System.err.println("Ошибка подключения к базе данных: " + e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        // Подключение к базе данных через метод connect
        Connection conn = connect();

        // Здесь можно выполнять операции с базой данных
        if (conn != null) {
            System.out.println("Операции с базой данных могут быть выполнены.");
        }
    }
}
