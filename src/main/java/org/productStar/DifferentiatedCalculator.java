package org.productStar;                         // Пакет с дифференцированным калькулятором

import java.util.ArrayList;                      // Импорт ArrayList
import java.util.List;                           // Импорт List

public class DifferentiatedCalculator implements ICalculator { // Калькулятор дифференцированного графика
    private double principal;                    // Сумма кредита
    private double annualInterestRate;           // Годовая процентная ставка
    private int years;                           // Срок кредита в годах
    private List<Payment> payments;              // Список платежей

    @Override
    public void setPrincipal(double principal) { // Установка суммы кредита
        this.principal = principal;
    }

    @Override
    public void setAnnualInterestRate(double annualInterestRate) { // Установка годовой ставки
        this.annualInterestRate = annualInterestRate;
    }

    @Override
    public void setYears(int years) {            // Установка срока кредита в годах
        this.years = years;
    }

    @Override
    public void calculatePayments() {            // Расчёт дифференцированного графика платежей
        double monthlyRate = annualInterestRate / 12 / 100; // Месячная ставка
        int totalMonths = years * 12;            // Общее число месяцев
        double remainingDebt = principal;        // Остаток долга

        payments = new ArrayList<>();            // Инициализация списка платежей

        double equalPrincipalPayment = principal / totalMonths; // Равный платёж по основному долгу каждый месяц

        for (int month = 1; month <= totalMonths; month++) { // Цикл по месяцам
            double interestPayment = remainingDebt * monthlyRate; // Проценты на текущий остаток долга
            double principalPayment = Math.min(equalPrincipalPayment, remainingDebt); // Погашаем не больше остатка
            remainingDebt -= principalPayment;   // Обновляем остаток долга после погашения
            if (remainingDebt < 0) {             // Защита от отрицательного остатка
                remainingDebt = 0;
            }
            payments.add(new Payment(month, principalPayment, interestPayment)); // Добавляем платёж в график
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
