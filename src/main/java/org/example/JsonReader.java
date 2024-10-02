package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class JsonReader {

    // Метод для чтения данных из URL
    public Object readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }

            String jsonText = sb.toString().trim();
            try {
                // Проверяем первый символ
                if (jsonText.startsWith("{")) {
                    return new JSONObject(jsonText);  // Если это объект JSON
                } else if (jsonText.startsWith("[")) {
                    return new JSONArray(jsonText);  // Если это массив JSON
                } else {
                    throw new JSONException("Некорректный формат JSON");
                }
            } catch (JSONException e) {
                System.err.println("Ошибка: Некорректный формат JSON. " + e.getMessage());
                return null;  // Или выбросить исключение дальше
            }
        }
    }

    // Метод для чтения JSON из файла
    public JSONObject readJsonFromFile(String filePath) throws IOException {
        try (BufferedReader rd = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            return new JSONObject(sb.toString());
        }
    }

    // Метод для чтения JSON данных из файла с ссылками
    public List<Object> readJsonsFromLinksFile(String linksFilePath) throws IOException {
        List<Object> jsonObjects = new ArrayList<>();

        // Чтение файла с ссылками
        try (BufferedReader rd = new BufferedReader(new FileReader(linksFilePath))) {
            String link;
            while ((link = rd.readLine()) != null) {
                try {
                    // Получение JSON по каждой ссылке
                    Object json = readJsonFromUrl(link.trim());

                    // Добавление в список
                    if (json != null) {
                        jsonObjects.add(json);
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка при получении данных по ссылке: " + link + ". Сообщение: " + e.getMessage());
                }
            }
        }
        return jsonObjects;
    }
}
