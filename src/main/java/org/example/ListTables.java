package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ListTables {

    // Метод для вывода структуры таблицы
    public static void printTableStructure(Connection conn, String tableName) {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                    "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = '"
                            + tableName.toLowerCase() + "'");

            System.out.println("Структура таблицы: " + tableName);
            while (rs.next()) {
                String columnName = rs.getString("column_name");
                String dataType = rs.getString("data_type");
                System.out.println("Столбец: " + columnName + ", Тип данных: " + dataType);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при запросе структуры таблицы: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Метод для вывода всех данных из таблицы
    public static void printTableData(Connection conn, String tableName) {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName.toLowerCase());

            // Получаем метаданные для динамического вывода всех колонок
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            System.out.println("\nДанные из таблицы: " + tableName);

            // Выводим названия колонок
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();

            // Выводим строки таблицы
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при запросе данных таблицы: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Метод для получения всех названий таблиц
    public static List<String> getAllTableNames(Connection conn) {
        List<String> tableNames = new ArrayList<>();
        String query = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE';";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                tableNames.add(rs.getString("table_name"));
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка таблиц: " + e.getMessage());
        }
        return tableNames;
    }

    // Метод для удаления всех таблиц
    public static void deleteAllTables(Connection conn) {
        List<String> tableNames = getAllTableNames(conn);

        for (String tableName : tableNames) {
            try (Statement stmt = conn.createStatement()) {
                String deleteQuery = "DROP TABLE IF EXISTS " + tableName + " CASCADE;";
                stmt.executeUpdate(deleteQuery);
                System.out.println("Таблица " + tableName + " успешно удалена.");
            } catch (SQLException e) {
                System.err.println("Ошибка при удалении таблицы " + tableName + ": " + e.getMessage());
            }
        }
    }

}
