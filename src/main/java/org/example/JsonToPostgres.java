package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class JsonToPostgres {

    // Метод для создания таблицы из JSON-объекта
    public static void createTableFromJsonObject(Connection conn, String tableName, JSONObject jsonObject) throws SQLException {
        Statement stmt = conn.createStatement();
        StringBuilder createQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");

        // Проходим по всем ключам JSON объекта и создаем столбцы
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            String columnType = "TEXT";  // Простое отображение всех данных как текст

            createQuery.append(key).append(" ").append(columnType).append(", ");
        }

        createQuery.delete(createQuery.length() - 2, createQuery.length());  // Убираем последнюю запятую
        createQuery.append(")");

//        System.out.println("Создание таблицы: " + createQuery);  // Логирование SQL-запроса
        stmt.executeUpdate(createQuery.toString());
        stmt.close();

        // Вставляем данные, если это требуется
        insertIntoTable(conn, tableName, jsonObject); // Вставляем данные из JSON-объекта
    }


    // Метод для определения типа данных
    private static String determineDataType(Object value) {
        if (value instanceof Number) {
            return "BIGINT"; // Или "INTEGER", в зависимости от вашего выбора
        } else if (value instanceof String) {
            return "TEXT";
        } else if (value instanceof JSONArray) {
            return "TEXT"; // Или создайте отдельную таблицу для массивов
        }
        return "TEXT"; // По умолчанию
    }

    // Метод для вставки данных из JSON-объекта в таблицу
    public static void insertIntoTable(Connection conn, String tableName, JSONObject jsonObject) throws SQLException {
        Statement stmt = conn.createStatement();
        StringBuilder insertQuery = new StringBuilder("INSERT INTO " + tableName + " (");

        // Проходим по ключам и подготавливаем SQL для вставки данных
        StringBuilder valuesPart = new StringBuilder(" VALUES (");

        for (String key : jsonObject.keySet()) {
            insertQuery.append(key).append(", ");
            Object value = jsonObject.get(key);

            // Обрабатываем значения в зависимости от их типа
            if (value instanceof JSONArray) {
                valuesPart.append("'").append(value.toString()).append("', "); // Обрабатываем массив как строку
            } else {
                valuesPart.append("'").append(value).append("', ");
            }
        }

        insertQuery.delete(insertQuery.length() - 2, insertQuery.length());  // Убираем последнюю запятую
        valuesPart.delete(valuesPart.length() - 2, valuesPart.length());  // Убираем последнюю запятую

        insertQuery.append(")").append(valuesPart).append(")");

        System.out.println("Вставка данных: " + insertQuery);  // Логирование SQL-запроса
        stmt.executeUpdate(insertQuery.toString());
        stmt.close();
    }

    // Метод для создания таблицы из массива JSON
    public static void createTableFromJsonArray(Connection conn, String tableName, JSONArray jsonArray) throws SQLException {
        if (jsonArray.length() == 0) return;  // Если массив пустой, ничего не делаем

        JSONObject firstObject = jsonArray.getJSONObject(0);
        createTableFromJsonObject(conn, tableName, firstObject);

        // Вставляем все объекты из массива в таблицу
        for (int i = 0; i < jsonArray.length(); i++) {
            insertIntoTable(conn, tableName, jsonArray.getJSONObject(i));
        }
    }
}
