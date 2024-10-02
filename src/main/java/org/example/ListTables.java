package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ListTables {

    public static void printTableStructure(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = '" + tableName + "'";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Структура таблицы " + tableName + ":");
            while (rs.next()) {
                String columnName = rs.getString("column_name");
                String dataType = rs.getString("data_type");
                System.out.println(columnName + " - " + dataType);
            }
        }
    }

    public static void printTableData(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT * FROM " + tableName;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Вывод заголовков
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();

            // Вывод данных
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }
        }
    }

    public static void printAllTables(Connection conn) throws SQLException {
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Список всех таблиц:");
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                System.out.println("- " + tableName);
            }
        }
    }

    public static void deleteAllTables(Connection conn) throws SQLException {
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Удаление всех таблиц...");
            while (rs.next()) {
                String tableName = rs.getString("table_name");
                String dropSQL = "DROP TABLE IF EXISTS " + tableName + " CASCADE";
                try (Statement dropStmt = conn.createStatement()) {
                    dropStmt.execute(dropSQL);
                    System.out.println("Таблица " + tableName + " удалена.");
                }
            }
        }
    }

}
