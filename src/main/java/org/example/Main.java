package org.example;

import java.io.IOException;
import java.util.Scanner;
import org.json.JSONObject;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//D:\Development-of-integration-systems\lab_1_1\1.txt

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JsonReader jsonReader = new JsonReader();

        // Создаем файл для логирования
        try (PrintWriter logWriter = new PrintWriter(new FileWriter("log.txt", true))) {

            while (true) {
                // Меню для выбора действия
                System.out.println("Выберите действие:");
                System.out.println("1. Получение данных по URL");
                System.out.println("2. Получение данных из файла");
                System.out.println("0. Выход из программы");
                System.out.print("Введите число: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Потребляется оставшийся перевод строки

                // Получаем текущие дату и время
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                // Записываем выбор пользователя в лог
                logWriter.println();
                logWriter.println(now.format(formatter) + " Пользователь выбрал действие: " + choice);

                // Обработка выбора пользователя
                if (choice == 1) {
                    // Пользователь выбрал получение данных по URL
                    System.out.print("Введите URL: ");
                    String url = scanner.nextLine();

                    // Логирование URL
                    logWriter.println(now.format(formatter) + " Пользователь ввел URL: " + url);

                    try {
                        System.out.println("Программа получает данные по указанному URL...");
                        logWriter.println(now.format(formatter) + " Программа получает данные по URL: " + url);

                        JSONObject json = jsonReader.readJsonFromUrl(url);
                        System.out.println("Данные получены успешно!");
                        System.out.println(json.toString(2));

                        logWriter.println(now.format(formatter) + " Данные успешно получены");

                    } catch (IOException e) {
                        // Логирование ошибки
                        System.err.println("Ошибка при получении данных: " + e.getMessage());
                        logWriter.println(now.format(formatter) + " Ошибка при получении данных: " + e.getMessage());
                    }

                } else if (choice == 2) {
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

                } else if (choice == 0) {
                    // Пользователь выбрал выход из программы
                    System.out.println("Завершение программы.");
                    logWriter.println(now.format(formatter) + " Пользователь завершил программу.");
                    break;  // Выход из цикла и завершение программы

                } else {
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