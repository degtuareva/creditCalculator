package org.productStar.console;                         // Пакет с классом ResultPrinter

import org.productStar.domain.Payment;
import org.productStar.service.ICalculator;

public class ResultPrinter {                     // Класс, отвечающий за вывод результатов пользователю

    public void printResults(ICalculator calculator, double originalPrincipal, double downPayment) {
        double totalPayments = calculator.getTotalPayment();   // Получаем общую сумму всех платежей
        double totalInterest = calculator.getTotalInterest();  // Получаем общую сумму процентов
        double actualPrincipal = originalPrincipal - downPayment; // Рассчитываем фактическую сумму займа
        double overpayment = totalPayments - actualPrincipal;  // Считаем переплату по кредиту

        System.out.println("\n📊 РЕЗУЛЬТАТЫ РАСЧЕТА:");         // Заголовок блока результатов
        System.out.printf("💳 Сумма кредита: %.2f руб.%n", originalPrincipal); // Исходная сумма кредита
        System.out.printf("💰 Первоначальный взнос: %.2f руб.%n", downPayment); // Первоначальный взнос
        System.out.printf("🏦 Сумма к выплате: %.2f руб.%n", actualPrincipal);  // Сумма, которая реально берётся в долг
        System.out.printf("📈 Общая сумма выплат: %.2f руб.%n", totalPayments); // Общая сумма всех платежей
        System.out.printf("💸 Сумма переплат: %.2f руб. (%.1f%% от суммы к выплате)%n",
                overpayment, (overpayment / actualPrincipal) * 100);            // Переплата и её процент к сумме займа
        System.out.printf("📉 Переплата от общей суммы кредита: %.1f%% %n",
                (overpayment / originalPrincipal) * 100);                       // Переплата в процентах от исходной суммы
        System.out.printf("💵 Общая сумма процентов: %.2f руб.%n", totalInterest); // Общая сумма процентов
        System.out.println();                              // Пустая строка для отделения блоков
    }

    public void printSchedule(ICalculator calculator) {    // Метод для вывода помесячного графика платежей
        System.out.println("📋 ГРАФИК ПЛАТЕЖЕЙ:");         // Заголовок для таблицы графика
        System.out.printf("%-8s %-18s %-18s %-18s%n", "Месяц", "Основной долг", "Проценты", "Общий платеж");
        // Выводим шапку таблицы с выравниванием колонок
        System.out.println("─────────────────────────────────────────────────────────────"); // Разделительная линия

        for (Payment payment : calculator.getPaymentsSchedule()) { // Итерируемся по всем платежам в графике
            System.out.printf("%-8d %-18.2f %-18.2f %-18.2f%n",
                    payment.getMonth(),               // Номер месяца
                    payment.getPrincipalPayment(),    // Сумма погашения основного долга
                    payment.getInterestPayment(),     // Сумма процентного платежа
                    payment.getTotalPayment());       // Общий платёж за месяц
        }
    }
}