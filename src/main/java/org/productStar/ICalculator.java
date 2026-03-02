package org.productStar;                     // Пакет с интерфейсом ICalculator

import java.util.List;                       // Импорт списка для графика платежей

public interface ICalculator {               // Интерфейс общего калькулятора графиков

    void setPrincipal(double principal);     // Метод установки суммы кредита (основного долга)

    void setAnnualInterestRate(double annualInterestRate); // Метод установки годовой процентной ставки

    void setYears(int years);                // Метод установки срока кредита в годах

    void calculatePayments();                // Метод для расчёта графика платежей

    double getTotalPayment();                // Метод получения общей суммы всех платежей

    double getTotalInterest();               // Метод получения общей суммы процентов

    List<Payment> getPaymentsSchedule();     // Метод получения списка всех платежей (графика)
}
