package org.productStar;                         // Пакет, содержащий аннуитетный калькулятор

import java.util.ArrayList;
import java.util.List;

public class AnnuityCalculator implements ICalculator { // Класс аннуитетного калькулятора, реализующий ICalculator
    private double principal;                    // Сумма кредита (основной долг)
    private double annualInterestRate;           // Годовая процентная ставка
    private int years;                           // Срок кредита в годах
    private List<Payment> payments;              // Список платежей (график)

    @Override
    public void setPrincipal(double principal) { // Установка суммы кредита
        this.principal = principal;
    }

    @Override
    public void setAnnualInterestRate(double annualInterestRate) { // Установка годовой ставки
        this.annualInterestRate = annualInterestRate;
    }

    @Override
    public void setYears(int years) {            // Установка срока в годах
        this.years = years;
    }

    @Override
    public void calculatePayments() {            // Расчёт графика аннуитетных платежей
        double monthlyRate = annualInterestRate / 12 / 100; // Вычисляем месячную процентную ставку
        int totalMonths = years * 12;            // Общее количество месяцев кредита
        double monthlyPayment = principal * (monthlyRate * Math.pow(1 + monthlyRate, totalMonths))
                / (Math.pow(1 + monthlyRate, totalMonths) - 1); // Формула аннуитетного платежа
        payments = new ArrayList<>();            // Инициализируем список платежей

        double remainingDebt = principal;        // Текущий остаток долга

        for (int month = 1; month <= totalMonths; month++) { // Цикл по всем месяцам
            double interestPayment = remainingDebt * monthlyRate; // Считаем проценты от остатка долга
            double principalPayment = monthlyPayment - interestPayment; // Основной долг = платёж - проценты
            remainingDebt -= principalPayment;   // Обновляем остаток долга после погашения
            if (remainingDebt < 0) {             // Если остаток стал отрицательным из-за округлений
                remainingDebt = 0;               // Принудительно обнуляем остаток
            }
            payments.add(new Payment(month, principalPayment, interestPayment)); // Добавляем платёж в список
        }
    }

    @Override
    public double getTotalPayment() {            // Получение общей суммы всех платежей
        return payments.stream()                 // Берём поток платежей
                .mapToDouble(Payment::getTotalPayment) // Преобразуем в поток double по сумме платежа
                .sum();                          // Складываем все значения
    }

    @Override
    public double getTotalInterest() {           // Получение общей суммы процентов
        return payments.stream()                 // Поток платежей
                .mapToDouble(Payment::getInterestPayment) // Берём только проценты
                .sum();                          // Складываем проценты
    }

    @Override
    public List<Payment> getPaymentsSchedule() { // Получение графика платежей
        return payments;                        // Возвращаем список платежей
    }
}