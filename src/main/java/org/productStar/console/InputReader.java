package org.productStar.console;                     // Пакет, в котором находится класс

import org.productStar.domain.ScheduleType;

import java.util.Scanner;

public class InputReader {                   // Класс, отвечающий за ввод и валидацию данных
    private final Scanner scanner;           // Поле для объекта Scanner

    public InputReader() {                   // Конструктор без параметров
        this.scanner = new Scanner(System.in); // Инициализируем Scanner чтением из стандартного ввода
    }

    public double readPrincipal() {          // Метод для чтения суммы кредита
        return readDouble("Введите сумму кредита:", 1000, 100_000_000); // Вызов универсального метода чтения double
    }

    public double readDownPayment(double principal) { // Метод чтения первоначального взноса
        return readDouble("Введите первоначальный взнос (0 для без взноса):", 0, principal); // Взнос от 0 до суммы кредита
    }

    public int readYears() {                 // Метод чтения срока кредита
        return readInt("Введите срок кредита в годах:", 1, 30); // Срок от 1 до 30 лет
    }

    public double readAnnualInterestRate() { // Метод чтения годовой процентной ставки
        return readDouble("Введите годовую процентную ставку (%):", 0.1, 100); // Ставка от 0.1% до 100%
    }

    public ScheduleType readScheduleType() { // Метод чтения типа графика платежей
        while (true) {                       // Бесконечный цикл до корректного ввода
            System.out.println("Выберите тип графика платежей:"); // Подсказка пользователю
            System.out.println("1 - " + ScheduleType.ANNUITY);        // Выводим аннуитетный тип
            System.out.println("2 - " + ScheduleType.DIFFERENTIATED); // Выводим дифференцированный тип
            System.out.print("Введите номер (1/2), 'annuity'/'аннуитет' или 'differentiated'/'дифференцированный': ");

            String input = scanner.nextLine(); // Читаем строку ввода пользователя
            ScheduleType type = ScheduleType.parse(input); // Пробуем распарсить строку в enum

            if (type != null) {              // Если удалось распознать тип
                return type;                 // Возвращаем найденный тип графика
            }
            System.out.println("❌ Неверный тип графика! Попробуйте снова."); // Сообщение об ошибке ввода
        }
    }

    private double readDouble(String prompt, double min, double max) { // Универсальный метод чтения double с валидацией
        while (true) {                       // Цикл до корректного значения
            System.out.print(prompt + " ");  // Выводим подсказку
            if (scanner.hasNextDouble()) {   // Проверяем, есть ли корректное число
                double value = scanner.nextDouble(); // Читаем значение double
                scanner.nextLine();          // Считываем остаток строки (перевод строки)
                if (value >= min && value <= max) { // Проверяем попадание в диапазон
                    return value;            // Возвращаем значение при успешной валидации
                }
                System.out.printf("❌ Значение должно быть от %.2f до %.2f. Попробуйте снова.%n", min, max); // Сообщаем об ошибке диапазона
            } else {                         // Если введено не число
                scanner.nextLine();          // Очищаем некорректный ввод
                System.out.println("❌ Введите корректное число!"); // Сообщаем об ошибке формата
            }
        }
    }

    private int readInt(String prompt, int min, int max) { // Универсальный метод чтения int с валидацией
        while (true) {                       // Цикл до корректного значения
            System.out.print(prompt + " ");  // Выводим подсказку пользователю
            if (scanner.hasNextInt()) {      // Проверяем, является ли ввод целым числом
                int value = scanner.nextInt(); // Читаем значение int
                scanner.nextLine();          // Очищаем перевод строки
                if (value >= min && value <= max) { // Проверяем, попадает ли значение в указанный диапазон
                    return value;            // Возвращаем валидное значение
                }
                System.out.printf("❌ Значение должно быть от %d до %d. Попробуйте снова.%n", min, max); // Ошибка диапазона
            } else {                         // Если ввод не целое число
                scanner.nextLine();          // Очищаем неверный ввод
                System.out.println("❌ Введите целое число!"); // Сообщаем об ошибке формата
            }
        }
    }
}