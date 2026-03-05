package org.productStar.domain;                     // Пакет, в котором находится enum ScheduleType

public enum ScheduleType {                  // Объявление перечисления типов графиков платежей
    ANNUITY("annuity", "аннуитет"),         // Константа для аннуитетного графика, с англ. и рус. строками
    DIFFERENTIATED("differentiated", "дифференцированный"); // Константа для дифференцированного графика

    private final String englishValue;      // Поле: английское текстовое представление типа
    private final String russianValue;      // Поле: русское текстовое представление типа

    ScheduleType(String english, String russian) { // Конструктор enum с двумя параметрами
        this.englishValue = english;        // Сохраняем английское значение
        this.russianValue = russian;        // Сохраняем русское значение
    }

    public static ScheduleType parse(String input) { // Статический метод для разбора строки в ScheduleType
        String cleanInput = input.trim().toLowerCase(); // Очищаем пробелы по краям и приводим к нижнему регистру

        // ===== Поддержка ввода цифрами =====
        if (cleanInput.equals("1")) {       // Если пользователь ввёл "1"
            return ANNUITY;                 // Возвращаем аннуитетный тип
        }
        if (cleanInput.equals("2")) {       // Если пользователь ввёл "2"
            return DIFFERENTIATED;          // Возвращаем дифференцированный тип
        }

        // ===== Поддержка текстового ввода (ru/en) =====
        for (ScheduleType type : values()) {           // Перебираем все значения enum
            if (cleanInput.equals(type.englishValue)   // Если строка совпала с английским названием
                    || cleanInput.equals(type.russianValue) // Или совпала с русским названием
                    || cleanInput.equals(type.name().toLowerCase())) { // Или совпала с именем enum (ANNUITY, DIFFERENTIATED)
                return type;                  // Возвращаем найденный тип
            }
        }

        return null;                          // Если ничего не подошло — возвращаем null (ошибка ввода)
    }

    @Override
    public String toString() {                // Переопределяем метод toString()
        return russianValue;                  // По умолчанию возвращаем русское название типа
    }
}