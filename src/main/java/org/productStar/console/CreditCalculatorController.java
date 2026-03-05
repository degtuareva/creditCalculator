package org.productStar.console;                     // Пакет, в котором находится контроллер

import org.productStar.domain.ScheduleType;
import org.productStar.service.CalculatorFactory;
import org.productStar.service.ICalculator;

public class CreditCalculatorController {    // Класс контроллера, оркестратор бизнес-логики

    private final InputReader inputReader;   // Поле для чтения и валидации пользовательского ввода
    private final ResultPrinter resultPrinter; // Поле для вывода результатов и графика

    public CreditCalculatorController(InputReader inputReader, ResultPrinter resultPrinter) {
        this.inputReader = inputReader;      // Инициализация поля reader через конструктор (DI)
        this.resultPrinter = resultPrinter;  // Инициализация поля printer через конструктор (DI)
    }

    public void run() {                      // Основной метод, запускающий сценарий расчёта
        double principal = inputReader.readPrincipal();         // Читаем и валидируем сумму кредита
        double downPayment = inputReader.readDownPayment(principal); // Читаем и валидируем первоначальный взнос
        int years = inputReader.readYears();                    // Читаем и валидируем срок кредита в годах
        double annualInterestRate = inputReader.readAnnualInterestRate(); // Читаем и валидируем годовую ставку
        ScheduleType scheduleType = inputReader.readScheduleType();      // Читаем и валидируем тип графика

        boolean hasDiscount = downPayment > 0;                  // Определяем, есть ли первоначальный взнос
        ICalculator calculator = CalculatorFactory.create(scheduleType, hasDiscount); // Создаём нужный калькулятор через фабрику

        double actualPrincipal = principal - downPayment;       // Вычисляем фактическую сумму займа (с учётом взноса)

        calculator.setPrincipal(actualPrincipal);               // Передаём сумму займа в калькулятор
        calculator.setAnnualInterestRate(annualInterestRate);   // Передаём годовую процентную ставку
        calculator.setYears(years);                             // Передаём срок кредита в годах
        calculator.calculatePayments();                         // Запускаем расчёт графика платежей

        resultPrinter.printResults(calculator, principal, downPayment); // Печатаем сводные результаты и переплату
        resultPrinter.printSchedule(calculator);                // Печатаем помесячный график платежей
    }
}