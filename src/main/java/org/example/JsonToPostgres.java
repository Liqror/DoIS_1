package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;
import java.sql.SQLException;


public class JsonToPostgres {
    public static void createTableFromJsonObject(Connection conn, String tableName, JSONObject jsonObject) throws SQLException {
        // Получение ключей из JSON объекта
        Set<String> keys = jsonObject.keySet();

        // Формирование SQL запроса для создания таблицы
        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");

        for (String key : keys) {
            // Пример определения типа данных (возможно, нужно будет доработать)
            String dataType = "TEXT"; // По умолчанию тип данных текст

            Object value = jsonObject.get(key);
            if (value instanceof Integer) {
                dataType = "INTEGER";
            } else if (value instanceof Double) {
                dataType = "DOUBLE PRECISION";
            } else if (value instanceof Boolean) {
                dataType = "BOOLEAN";
            }

            // Замена дефисов на подчеркивания
            String columnName = key.replace("-", "_");

            // Добавление колонки в SQL запрос
            createTableSQL.append(columnName).append(" ").append(dataType).append(", ");
        }

        // Удаление последней запятой и добавление закрывающей скобки
        createTableSQL.setLength(createTableSQL.length() - 2);
        createTableSQL.append(");");

        // Выполнение SQL запроса
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL.toString());
        }

        // Вставка данных в таблицу
        insertDataIntoTable(conn, tableName, jsonObject);
    }

    public static void createTableFromJsonArray(Connection conn, String tableName, JSONArray jsonArray) throws SQLException {
        if (jsonArray.length() == 0) {
            System.out.println("Массив JSON пуст, таблица не будет создана.");
            return;
        }

        // Предполагаем, что структура всех объектов в массиве одинакова
        JSONObject firstObject = jsonArray.getJSONObject(0);
        createTableFromJsonObject(conn, tableName, firstObject);

        // Вставка данных в таблицу
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            insertDataIntoTable(conn, tableName, jsonObject);
        }
    }

    public static void insertDataIntoTable(Connection conn, String tableName, JSONObject jsonObject) throws SQLException {
        // Формируем SQL-запрос на вставку данных
        StringBuilder insertSQL = new StringBuilder("INSERT INTO " + tableName + " (");
        StringBuilder valuesSQL = new StringBuilder("VALUES (");

        for (String key : jsonObject.keySet()) {
            String columnName = key.replace("-", "_"); // Заменяем дефисы
            insertSQL.append(columnName).append(", ");

            Object value = jsonObject.get(key);
            if (value instanceof String) {
                valuesSQL.append("'").append(value).append("', ");
            } else if (value instanceof Integer || value instanceof Double || value instanceof Boolean) {
                valuesSQL.append(value).append(", ");
            } else if (value instanceof JSONArray) {
                // Преобразуем JSONArray в строку формата JSON
                valuesSQL.append("'").append(((JSONArray) value).toString()).append("', ");
            } else {
                // Если тип данных неизвестен, сохраняем его как строку
                valuesSQL.append("'").append(value.toString()).append("', ");
            }
        }

        // Убираем последние запятые
        insertSQL.setLength(insertSQL.length() - 2);
        valuesSQL.setLength(valuesSQL.length() - 2);

        // Завершаем запросы
        insertSQL.append(") ");
        valuesSQL.append(");");

        String finalSQL = insertSQL.toString() + valuesSQL.toString();

        // Выводим полный SQL-запрос на вставку
//        System.out.println("SQL-запрос на вставку данных: " + finalSQL);

        // Выполняем запрос
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(finalSQL);
        }
    }

}
