package org.productStar;                 // Пакет, в котором лежит класс Application

public class Application {               // Объявление публичного класса Application

    public static void main(String[] args) { // Точка входа в приложение (метод main)
        InputReader inputReader = new InputReader();     // Создаём объект для чтения и валидации ввода
        ResultPrinter resultPrinter = new ResultPrinter(); // Создаём объект для вывода результатов
        CreditCalculatorController controller =          // Создаём контроллер, который управляет процессом
                new CreditCalculatorController(inputReader, resultPrinter);

        controller.run();                // Запускаем основной сценарий работы программы
    }
}
