# Credit Calculator (Консольный кредитный калькулятор)

Консольное Java‑приложение для расчёта кредитного графика.  
Поддерживает аннуитетный и дифференцированный графики, первоначальный взнос, расчёт переплаты и удобную валидацию ввода.

## Возможности

- Расчёт **аннуитетного графика** платежей
- Расчёт **аннуитетного графика с первоначальным взносом**
- Расчёт **дифференцированного графика** (подготовлено архитектурно, реализовано через отдельный калькулятор)
- Поддержка двух типов ввода типа графика:
    - по номеру: `1` — аннуитет, `2` — дифференцированный
    - по тексту: `annuity` / `аннуитет`, `differentiated` / `дифференцированный`
- Валидация пользовательского ввода:
    - сумма кредита, первоначальный взнос, срок, процентная ставка
    - защита от отрицательных значений и некорректного формата
- Расчёт и вывод:
    - общей суммы выплат
    - общей суммы процентов
    - **суммы переплаты** и её процента
- Печать помесячного графика:
    - месяц
    - платёж по основному долгу
    - платёж по процентам
    - общий платеж

## Технологии

- Java 17+ (можно и ниже, если скорректировать синтаксис `switch`)
- Консольный ввод/вывод (`Scanner`, `System.out`)
- Архитектура по принципам **SOLID**

## Архитектура проекта

Проект разделён на логические слои, каждый класс отвечает за одну зону ответственности.

### Основные классы

- `Application`  
  Точка входа в приложение. Настраивает зависимости и запускает контроллер.

- `CreditCalculatorController`  
  Оркестратор сценария: запрашивает данные, создаёт нужный калькулятор, запускает расчёт, выводит результаты.

- `InputReader`  
  Отвечает за чтение и валидацию пользовательского ввода (сумма, взнос, срок, ставка, тип графика).

- `ResultPrinter`  
  Отвечает за форматированный вывод:
    - сводных результатов (переплата, проценты, суммы)
    - помесячного графика платежей.

- `CalculatorFactory`  
  Создаёт нужную реализацию `ICalculator` на основе `ScheduleType` и наличия первоначального взноса.

### Модель и интерфейсы

- `Payment`  
  DTO одного платежа: месяц, погашение основного долга, проценты, общий платеж.

- `ICalculator`  
  Общий контракт для всех калькуляторов:
    - `setPrincipal(double)`
    - `setAnnualInterestRate(double)`
    - `setYears(int)`
    - `calculatePayments()`
    - `getTotalPayment()`
    - `getTotalInterest()`
    - `getPaymentsSchedule()`

- `IDiscount`  
  Расширяет `ICalculator` для калькуляторов с первоначальным взносом:
    - добавляет `setDiscount(double)`.

- `ScheduleType` (enum)  
  Тип графика: `ANNUITY`, `DIFFERENTIATED`.  
  Умеет парсить строку пользователя (`parse(String)`), поддерживает русские и английские названия.

### Реализации калькуляторов

- `AnnuityCalculator`  
  Классический аннуитетный график без первоначального взноса.  
  Считает ежемесячный платеж по стандартной формуле аннуитета и строит помесячный график.

- `AnnuityCalculatorWithDiscount`  
  Аннуитетный график с учётом первоначального взноса.  
  Работает с суммой кредита **после** вычета взноса и строит корректный график.

- `DifferentiatedCalculator`  
  Дифференцированный график:
    - ежемесячный платёж по основному долгу — постоянный
    - проценты начисляются на остаток долга
    - ежемесячный платёж по итогу уменьшается со временем.

## Принципы SOLID

Проект спроектирован с опорой на SOLID:

- **S (Single Responsibility)**
    - `InputReader` — только ввод и валидация
    - `ResultPrinter` — только вывод
    - `CreditCalculatorController` — только координация
    - калькуляторы — только расчёт графика.

- **O (Open–Closed)**  
  Для добавления нового типа графика достаточно:
    - добавить новый `enum` в `ScheduleType`
    - реализовать новый класс `SomeCalculator implements ICalculator`
    - расширить `CalculatorFactory`.  
      Остальные классы не изменяются.

- **L (Liskov Substitution)**  
  Везде, где ожидается `ICalculator`, можно передать любую реализацию:  
  `AnnuityCalculator`, `AnnuityCalculatorWithDiscount`, `DifferentiatedCalculator`.

- **I (Interface Segregation)**  
  `ICalculator` — базовый контракт,  
  `IDiscount` — отдельный интерфейс только для калькуляторов с первоначальным взносом.

- **D (Dependency Inversion)**  
  `CreditCalculatorController` работает с абстракцией `ICalculator`, а конкретный класс создаёт `CalculatorFactory`.

## Как запустить

1. Склонировать репозиторий:

   ```bash
   git clone https://github.com/<your-account>/<https://github.com/degtuareva/creditCalculator.git>.git
   cd <your-repo>

Веб‑приложение (Spring Boot + React)
Поверх существующей бизнес‑логики добавлен REST‑слой на Spring Boot и интерфейс на React.

Стек
Backend:

Java 21

Spring Boot (Web)

Frontend:

React + Vite

Fetch API

Структура проекта (общая идея)
text
.
├── build.gradle # Gradle‑конфигурация backend
├── src/
│ └── main/java/org/productStar
│ ├── CreditApplication.java
│ ├── AnnuityCalculator*.java
│ ├── DifferentiatedCalculator.java
│ ├── Payment.java
│ ├── ScheduleType.java
│ ├── CalculatorFactory.java
│ └── api/
│ ├── CalculationRequest.java
│ ├── CalculationSummary.java
│ ├── CalculationResponse.java
│ └── CreditController.java
└── frontend/
├── package.json
└── src/
├── main.jsx
├── App.jsx
├── App.css
└── index.css
Backend (REST API)
CreditApplication.java – точка входа Spring Boot.

Основной контроллер:

java
@RestController
@RequestMapping("/api/credit")
@CrossOrigin(origins = "http://localhost:5174") // адрес React dev-сервера
public class CreditController {

    @PostMapping("/calculate")
    public ResponseEntity<CalculationResponse> calculate(@RequestBody CalculationRequest request) {
        // валидация входных данных
        // выбор калькулятора через CalculatorFactory
        // расчёт графика и сводки
        // возврат CalculationResponse
    }

}
DTO:

CalculationRequest – вход: principal, downPayment, years, annualInterestRate, scheduleType.

CalculationSummary – сводка.

CalculationResponse – сводка + список Payment.

Порт backend задаётся в application.yml:

text
server:
port: 8081
Frontend (React + Vite)
В frontend/src/App.jsx реализована форма ввода параметров кредита и таблица с результатами. Запрос к backend:

js
const response = await fetch("http://localhost:8081/api/credit/calculate", {
method: "POST",
headers: { "Content-Type": "application/json" },
body: JSON.stringify({
principal: Number(principal),
downPayment: Number(downPayment),
years: Number(years),
annualInterestRate: Number(rate),
scheduleType, // "ANNUITY" или "DIFFERENTIATED"
}),
});
CORS для фронта разрешён в CreditController через @CrossOrigin.

Стили хранятся в App.css и подключаются в App.jsx:

js
import "./App.css";
Запуск веб‑версии
Запуск backend:

bash
./gradlew bootRun
Backend поднимется на http://localhost:8081.

Запуск frontend:

bash
cd frontend
npm install # один раз
npm run dev
Vite выведет локальный адрес, например: http://localhost:5174/.

Открыть в браузере адрес фронтенда (например, http://localhost:5174).
Заполнить форму → нажать «Рассчитать» → увидеть сводку и график, которые приходят с backend.

