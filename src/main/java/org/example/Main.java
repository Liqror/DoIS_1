package org.example;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


//D:\Development-of-integration-systems\lab_1_1\1.txt

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JsonReader jsonReader = new JsonReader();

        // Создаем файл для логирования
        try (PrintWriter logWriter = new PrintWriter(new FileWriter("log.txt", true))) {

            while (true) {

                try {
                    // Задержка в 2 секунды (2000 миллисекунд)
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }

                // Меню для выбора действия
                System.out.println("__________________________________________");
                System.out.println("Выберите действие:");
                System.out.println("1. Получение данных по URL");
                System.out.println("2. Получение данных из файла");
                System.out.println("3. Вывести все таблицы (только названия)");
                System.out.println("4. Удалить все таблицы");
//                System.out.println("3. Получение данных из файла со всеми ссылками");
                System.out.println("0. Выход из программы");
                System.out.println("__________________________________________");
                System.out.print("Введите число: ");

                int choice = -1; // Инициализация переменной для выбора

                try {
                    choice = scanner.nextInt();  // Попытка прочитать число
                    scanner.nextLine();  // Потребляется оставшийся перевод строки
                } catch (InputMismatchException e) {
                    System.out.println("Ошибка: Введите корректное число!");
                    scanner.nextLine();  // Очищаем некорректный ввод из буфера
                    continue;  // Возвращаемся к началу цикла, чтобы снова запросить ввод
                }

                // Получаем текущие дату и время
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                // Записываем выбор пользователя в лог
                logWriter.println(now.format(formatter) + " Пользователь выбрал действие: " + choice);

                // Подключение к базе данных
                Connection conn = null;
                conn = PostgresConnection.connect();

                // Обработка выбора пользователя
                switch (choice) {
                    case 1:
                        // Пользователь выбрал получение данных по URL
                        System.out.print("Введите URL: ");
                        String url = scanner.nextLine();

                        // Логирование URL
                        logWriter.println(now.format(formatter) + " Пользователь ввел URL: " + url);

                        try {
                            System.out.println("Программа получает данные по указанному URL...");
                            logWriter.println(now.format(formatter) + " Программа получает данные по URL: " + url);

                            // Получение данных JSON с URL
                            Object jsonData = jsonReader.readJsonFromUrl(url);

                            try {
                                String tableName = url.replaceAll("[^a-zA-Z0-9]", "_");  // Генерация имени таблицы на основе URL

                                if (jsonData instanceof JSONObject) {
                                    JSONObject jsonObject = (JSONObject) jsonData;
//                                    System.out.println("Данные получены успешно (JSONObject)!");
//                                    System.out.println(jsonObject.toString(2));
                                    logWriter.println(now.format(formatter) + " Данные успешно получены (JSONObject)");

                                    // Создаем таблицу из JSONObject
                                    JsonToPostgres.createTableFromJsonObject(conn, tableName, jsonObject);

                                } else if (jsonData instanceof JSONArray) {
                                    JSONArray jsonArray = (JSONArray) jsonData;
//                                    System.out.println("Данные получены успешно (JSONArray)!");
//                                    System.out.println(jsonArray.toString(2));  // Вывод массива JSON
                                    logWriter.println(now.format(formatter) + " Данные успешно получены (JSONArray)");

                                    // Создаем таблицу из JSONArray
                                    JsonToPostgres.createTableFromJsonArray(conn, tableName, jsonArray);

                                } else {
                                    System.out.println("Ошибка: Невозможно распознать формат данных.");
                                    logWriter.println(now.format(formatter) + " Ошибка: Невозможно распознать формат данных.");
                                }

                                // После создания таблицы вызываем метод для вывода её структуры
                                ListTables.printTableStructure(conn, tableName);

                                // Выводим данные таблицы
                                ListTables.printTableData(conn, tableName);

                            } catch (SQLException e) {
                                System.err.println("Ошибка при работе с базой данных: " + e.getMessage());
                                e.printStackTrace();
                            }

                        } catch (IOException e) {
                            // Логирование ошибки
                            System.err.println("Ошибка при получении данных: " + e.getMessage());
                            logWriter.println(now.format(formatter) + " Ошибка при получении данных: " + e.getMessage());
                        }
                        break;

                    case 2:
                        // Пользователь выбрал получение данных из файла
                        System.out.print("Введите путь к файлу: ");
                        String filePath = scanner.nextLine();

                        // Логирование пути к файлу
                        logWriter.println(now.format(formatter) + " Пользователь ввел путь к файлу: " + filePath);

                        try {
                            System.out.println("Программа считывает данные из файла...");
                            logWriter.println(now.format(formatter) + " Программа считывает данные из файла: " + filePath);

                            JSONObject json = jsonReader.readJsonFromFile(filePath);
                            System.out.println("Данные получены успешно!");
                            System.out.println(json.toString(2));

                            logWriter.println(now.format(formatter) + " Данные успешно получены");

                        } catch (IOException e) {
                            // Логирование ошибки
                            System.err.println("Ошибка при получении данных: " + e.getMessage());
                            logWriter.println(now.format(formatter) + " Ошибка при получении данных: " + e.getMessage());
                        }
                        break;

                    case 3:
                        // Вывод всех таблиц (только названия)
                        System.out.println("Список таблиц в базе данных:");
                        List<String> tableNames = ListTables.getAllTableNames(conn);
                        for (String tableName : tableNames) {
                            System.out.println(tableName);
                        }
                        break;

                    case 4:
                        // Удаление всех таблиц
                        System.out.println("Все таблицы будут удалены. Подтвердите действие (y/n): ");
                        String confirmation = scanner.nextLine();
                        if (confirmation.equalsIgnoreCase("y")) {
                            ListTables.deleteAllTables(conn);
                            System.out.println("Все таблицы были успешно удалены.");
                        } else {
                            System.out.println("Операция отменена.");
                        }
                        break;

                    case 0:
                        System.out.println("Выход из программы...");
                        logWriter.println(now.format(formatter) + " Программа завершена пользователем.");
                        logWriter.println();
                        logWriter.close(); // Закрытие лога
                        try {
                            conn.close();  // Закрытие соединения с базой данных
                        } catch (SQLException e) {
                            System.err.println("Ошибка при закрытии соединения с базой данных: " + e.getMessage());
                        }
                        return; // Завершение программы
                    default:
                        // Если введено неверное число
                        System.out.println("Неверный выбор. Попробуйте снова.");
                        logWriter.println(now.format(formatter) + " Неверный выбор пользователя.");
                }
            }
        } catch (IOException e) {
            // Обработка ошибок при работе с файлом
            System.err.println("Ошибка при записи лога: " + e.getMessage());
        }
    }
}