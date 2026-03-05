package org.productStar.domain;                         // Пакет с моделью Payment

public class Payment {                           // Класс, описывающий один платёж по кредиту

    private final int month;                     // Номер месяца (1, 2, 3, ...)

    private final double principalPayment;       // Сумма погашения основного долга в этом платеже

    private final double interestPayment;        // Сумма процентов в этом платеже

    private final double totalPayment;           // Общая сумма платежа (основной долг + проценты)

    public Payment(int month, double principalPayment, double interestPayment) { // Конструктор платежа
        this.month = month;                      // Сохраняем номер месяца
        this.principalPayment = principalPayment; // Сохраняем погашение основного долга
        this.interestPayment = interestPayment;  // Сохраняем сумму процентов
        this.totalPayment = principalPayment + interestPayment; // Вычисляем общий платёж
    }

    public int getMonth() {                      // Геттер для номера месяца
        return month;
    }

    public double getPrincipalPayment() {        // Геттер для погашения основного долга
        return principalPayment;
    }

    public double getInterestPayment() {         // Геттер для суммы процентов
        return interestPayment;
    }

    public double getTotalPayment() {            // Геттер для общей суммы платежа
        return totalPayment;
    }
}