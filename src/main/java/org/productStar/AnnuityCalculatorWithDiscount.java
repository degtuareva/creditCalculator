package org.productStar;                         // Пакет с аннуитетным калькулятором с первоначальным взносом

import java.util.ArrayList;                      // Импорт ArrayList
import java.util.List;                           // Импорт List

public class AnnuityCalculatorWithDiscount implements IDiscount { // Калькулятор с учётом первоначального взноса
    private double principal;                    // Сумма займа ПОСЛЕ вычета первоначального взноса
    private double annualInterestRate;           // Годовая процентная ставка
    private double discount;                     // Первоначальный взнос
    private int years;                           // Срок кредита в годах
    private List<Payment> payments;              // Список платежей по кредиту

    @Override
    public void setDiscount(double discount) {   // Установка величины первоначального взноса
        this.discount = discount;
    }

    public double getDiscount() {                // Геттер для первоначального взноса
        return discount;
    }

    @Override
    public void setPrincipal(double principal) { // Установка суммы займа (уже уменьшенной)
        this.principal = principal;
    }

    @Override
    public void setAnnualInterestRate(double annualInterestRate) { // Установка годовой ставки
        this.annualInterestRate = annualInterestRate;
    }

    @Override
    public void setYears(int years) {            // Установка срока займа
        this.years = years;
    }

    @Override
    public void calculatePayments() {            // Расчёт графика аннуитетных платежей с взносом
        if (principal <= 0) {                    // Если фактическая сумма займа не положительная
            payments = new ArrayList<>();        // Инициализируем пустой список
            return;                              // Выходим, так как считать нечего
        }

        double monthlyRate = annualInterestRate / 12 / 100; // Месячная процентная ставка
        int totalMonths = years * 12;            // Общее число месяцев займа
        double monthlyPayment = principal * (monthlyRate * Math.pow(1 + monthlyRate, totalMonths))
                / (Math.pow(1 + monthlyRate, totalMonths) - 1); // Формула аннуитетного платежа
        payments = new ArrayList<>();            // Инициализируем список платежей

        double remainingDebt = principal;        // Остаток долга на начало

        for (int month = 1; month <= totalMonths; month++) { // Цикл по месяцам
            double interestPayment = remainingDebt * monthlyRate; // Платёж по процентам
            double principalPayment = monthlyPayment - interestPayment; // Погашение основного долга
            remainingDebt -= principalPayment;   // Обновляем остаток долга
            if (remainingDebt < 0) {             // Если остаток стал отрицательным из-за округлений
                remainingDebt = 0;               // Обнуляем остаток
            }
            payments.add(new Payment(month, principalPayment, interestPayment)); // Добавляем платёж в список
        }
    }

    @Override
    public double getTotalPayment() {            // Общая сумма всех платежей
        return payments.stream()
                .mapToDouble(Payment::getTotalPayment)
                .sum();
    }

    @Override
    public double getTotalInterest() {           // Общая сумма всех процентов
        return payments.stream()
                .mapToDouble(Payment::getInterestPayment)
                .sum();
    }

    @Override
    public List<Payment> getPaymentsSchedule() { // График платежей
        return payments;
    }
}
