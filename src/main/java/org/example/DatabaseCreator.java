package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreator {

    public static void createDatabase(String dbName, String user, String password) {
        Connection conn = null;
        Statement stmt = null;
        try {
            // Подключение к существующей базе данных 'postgres'
            String url = "jdbc:postgresql://localhost:5432/postgres";
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Подключение к базе данных 'postgres' успешно!");

            // Создание новой базы данных
            stmt = conn.createStatement();
            String sql = "CREATE DATABASE " + dbName;
            stmt.executeUpdate(sql);
            System.out.println("База данных '" + dbName + "' успешно создана!");

        } catch (SQLException e) {
            System.err.println("Ошибка при создании базы данных: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Ошибка при закрытии ресурсов: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // Имя новой базы данных
        String dbName = "new_database";
        // Имя пользователя и пароль для подключения
        String user = "postgres";  // Если имя
        String password = "qwerty";  // Укажите правильный пароль

        // Вызов метода для создания базы данных
        createDatabase(dbName, user, password);
    }
}
