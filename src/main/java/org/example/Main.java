package org.example;

import java.io.IOException;
import java.util.Scanner;
import org.json.JSONObject;
import java.io.PrintWriter;
import java.io.FileWriter;

//D:\Development-of-integration-systems\lab_1_1\1.txt

// дата и время в лог файле!!

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

                // Записываем выбор пользователя в лог
                logWriter.println("Пользователь выбрал действие: " + choice);

                // Обработка выбора пользователя
                if (choice == 1) {
                    // Пользователь выбрал получение данных по URL
                    System.out.print("Введите URL: ");
                    String url = scanner.nextLine();

                    // Логирование URL
                    logWriter.println("Пользователь ввел URL: " + url);

                    try {
                        System.out.println("Программа получает данные по указанному URL...");
                        logWriter.println("Программа получает данные по URL: " + url);

                        JSONObject json = jsonReader.readJsonFromUrl(url);
                        System.out.println("Данные получены успешно!");
                        System.out.println(json.toString(2));

                        logWriter.println("Данные успешно получены");

                    } catch (IOException e) {
                        // Логирование ошибки
                        System.err.println("Ошибка при получении данных: " + e.getMessage());
                        logWriter.println("Ошибка при получении данных: " + e.getMessage());
                    }

                } else if (choice == 2) {
                    // Пользователь выбрал получение данных из файла
                    System.out.print("Введите путь к файлу: ");
                    String filePath = scanner.nextLine();

                    // Логирование пути к файлу
                    logWriter.println("Пользователь ввел путь к файлу: " + filePath);

                    try {
                        System.out.println("Программа считывает данные из файла...");
                        logWriter.println("Программа считывает данные из файла: " + filePath);

                        JSONObject json = jsonReader.readJsonFromFile(filePath);
                        System.out.println("Данные получены успешно!");
                        System.out.println(json.toString(2));

                        logWriter.println("Данные успешно получены");

                    } catch (IOException e) {
                        // Логирование ошибки
                        System.err.println("Ошибка при получении данных: " + e.getMessage());
                        logWriter.println("Ошибка при получении данных: " + e.getMessage());
                    }

                } else if (choice == 0) {
                    // Пользователь выбрал выход из программы
                    System.out.println("Завершение программы.");
                    logWriter.println("Пользователь завершил программу.");
                    break;  // Выход из цикла и завершение программы

                } else {
                    // Если введено неверное число
                    System.out.println("Неверный выбор. Попробуйте снова.");
                    logWriter.println("Неверный выбор пользователя.");
                }
            }

        } catch (IOException e) {
            // Обработка ошибок при работе с файлом
            System.err.println("Ошибка при записи лога: " + e.getMessage());
        }
    }
}