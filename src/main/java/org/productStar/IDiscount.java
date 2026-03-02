package org.productStar;                     // Пакет, содержащий интерфейс

public interface IDiscount extends ICalculator { // Интерфейс для калькуляторов с первоначальным взносом, расширяет ICalculator

    void setDiscount(double discount);       // Метод установки величины первоначального взноса
}
