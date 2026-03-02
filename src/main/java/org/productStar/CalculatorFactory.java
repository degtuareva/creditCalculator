package org.productStar;                         // Пакет с фабрикой калькуляторов

public final class CalculatorFactory {           // Финальный класс фабрики (нельзя наследовать)

    private CalculatorFactory() {                // Приватный конструктор — запрещаем создание экземпляров
        // Ничего не делаем, утилитный класс
    }

    public static ICalculator create(ScheduleType type, boolean hasDiscount) {
        // Статический фабричный метод, создающий реализацию ICalculator
        return switch (type) {                   // Используем switch по типу графика
            case ANNUITY -> hasDiscount          // Если аннуитетный и есть первоначальный взнос
                    ? new AnnuityCalculatorWithDiscount()      // Возвращаем аннуитетный с первоначальным взносом
                    : new AnnuityCalculator();                 // Иначе обычный аннуитетный
            case DIFFERENTIATED ->
                    new DifferentiatedCalculator(); // Для дифференцированного — соответствующий калькулятор
        };
    }
}
