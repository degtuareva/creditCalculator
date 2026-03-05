# Credit Calculator (Консольный и веб‑калькулятор кредита)

Учебный проект на Java: изначально консольное приложение для расчёта кредитного графика, затем доработанное до full‑stack‑приложения с backend на Spring Boot и frontend на React.

Поддерживаются аннуитетный и дифференцированный графики, первоначальный взнос, расчёт переплаты и удобная валидация ввода.

---

## Вариант 1. Консольный кредитный калькулятор

### Возможности

- Расчёт **аннуитетного графика** платежей.
- Расчёт **аннуитетного графика с первоначальным взносом**.
- Расчёт **дифференцированного графика** (реализовано отдельным калькулятором).
- Поддержка двух типов ввода типа графика:
  - по номеру: `1` — аннуитет, `2` — дифференцированный;
  - по тексту: `annuity` / `аннуитет`, `differentiated` / `дифференцированный`.
- Валидация пользовательского ввода:
  - сумма кредита, первоначальный взнос, срок, процентная ставка;
  - защита от отрицательных значений и некорректного формата.
- Расчёт и вывод:
  - общей суммы выплат;
  - общей суммы процентов;
  - **суммы переплаты** и её процента.
- Печать помесячного графика:
  - месяц;
  - платёж по основному долгу;
  - платёж по процентам;
  - общий платеж.

### Технологии

- Java 17+ (можно и ниже, если скорректировать синтаксис `switch`).
- Консольный ввод/вывод (`Scanner`, `System.out`).
- Архитектура по принципам **SOLID**.

### Архитектура консольного проекта

Проект разделён на логические слои, каждый класс отвечает за одну зону ответственности.

#### Основные классы

- `Application`  
  Точка входа в приложение. Настраивает зависимости и запускает контроллер.

- `CreditCalculatorController`  
  Оркестратор сценария: запрашивает данные, создаёт нужный калькулятор, запускает расчёт, выводит результаты.

- `InputReader`  
  Отвечает за чтение и валидацию пользовательского ввода (сумма, взнос, срок, ставка, тип графика).

- `ResultPrinter`  
  Отвечает за форматированный вывод:
  - сводных результатов (переплата, проценты, суммы);
  - помесячного графика платежей.

- `CalculatorFactory`  
  Создаёт нужную реализацию `ICalculator` на основе `ScheduleType` и наличия первоначального взноса.

#### Модель и интерфейсы

- `Payment`  
  DTO одного платежа: месяц, погашение основного долга, проценты, общий платеж.

- `ICalculator`  
  Общий контракт для всех калькуляторов:
  - `setPrincipal(double)`;
  - `setAnnualInterestRate(double)`;
  - `setYears(int)`;
  - `calculatePayments()`;
  - `getTotalPayment()`;
  - `getTotalInterest()`;
  - `getPaymentsSchedule()`.

- `IDiscount`  
  Расширяет `ICalculator` для калькуляторов с первоначальным взносом:
  - добавляет `setDiscount(double)`.

- `ScheduleType` (enum)  
  Тип графика: `ANNUITY`, `DIFFERENTIATED`.  
  Умеет парсить строку пользователя (`parse(String)`), поддерживает русские и английские названия.

#### Реализации калькуляторов

- `AnnuityCalculator`  
  Классический аннуитетный график без первоначального взноса.  
  Считает ежемесячный платеж по стандартной формуле аннуитета и строит помесячный график.

- `AnnuityCalculatorWithDiscount`  
  Аннуитетный график с учётом первоначального взноса.  
  Работает с суммой кредита **после** вычета взноса и строит корректный график.

- `DifferentiatedCalculator`  
  Дифференцированный график:
  - ежемесячный платёж по основному долгу — постоянный;
  - проценты начисляются на остаток долга;
  - ежемесячный платёж по итогу уменьшается со временем.

### Принципы SOLID

- **S (Single Responsibility)**  
  `InputReader` — только ввод и валидация;  
  `ResultPrinter` — только вывод;  
  `CreditCalculatorController` — только координация;  
  калькуляторы — только расчёт графика.

- **O (Open–Closed)**  
  Для добавления нового типа графика достаточно:
  - добавить новый элемент в `ScheduleType`;
  - реализовать новый класс `SomeCalculator implements ICalculator`;
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

### Как запустить консольную версию

1. Склонировать репозиторий:

   ```bash
   git clone https://github.com/degtuareva/creditCalculator.git
   cd creditCalculator
Вариант 2. Веб‑приложение (Spring Boot + React + PWA)
Поверх существующей бизнес‑логики добавлен REST‑слой на Spring Boot и интерфейс на React (Vite). Фронтенд можно запускать как обычный веб‑сайт и устанавливать как PWA‑приложение на телефон/ПК.

Стек
Backend:

Java 21

Spring Boot (Web, Data JPA)

PostgreSQL

Gradle

Frontend:

React + Vite

Fetch API

vite-plugin-pwa для PWA

Структура проекта (общее)
text
.
├── build.gradle                     # Gradle-конфигурация backend
├── src/
│   └── main/java/org/productStar
│       ├── CreditApplication.java   # Spring Boot entrypoint
│       ├── AnnuityCalculator*.java
│       ├── DifferentiatedCalculator.java
│       ├── Payment.java
│       ├── ScheduleType.java
│       ├── CalculatorFactory.java
│       └── api/
│           ├── CalculationRequest.java
│           ├── CalculationSummary.java
│           ├── CalculationResponse.java
│           └── CreditController.java
├── src/main/resources/
│   ├── application.yml              # общая конфигурация
│   ├── application-local.yml        # профиль для локального запуска
│   └── application-docker.yml       # профиль для Docker
└── frontend/                        # React + Vite
├── package.json
├── vite.config.ts               # Vite + VitePWA конфиг
├── public/
│   └── icons/
│       ├── icon-192x192.png
│       └── icon-512x512.png
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

CalculationSummary – агрегированная сводка.

CalculationResponse – сводка + список Payment.

Порты и профили:

text
# application.yml
server:
port: 8081

spring:
jpa:
hibernate:
ddl-auto: update
show-sql: true

# application-local.yml
spring:
datasource:
url: jdbc:postgresql://localhost:5432/creditdb
username: credituser
password: credituser
driver-class-name: org.postgresql.Driver

# application-docker.yml
spring:
datasource:
url: jdbc:postgresql://db:5432/creditdb
username: credituser
password: creditpass
driver-class-name: org.postgresql.Driver
Frontend (React + Vite + PWA)
В frontend/src/App.jsx реализована форма ввода параметров кредита и таблица с результатами.

Запрос к backend:

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
Стили подключаются через App.css:

js
import "./App.css";
PWA‑конфигурация (vite-plugin-pwa)
В frontend/vite.config.ts подключен vite-plugin-pwa, который генерирует manifest.json и service worker, позволяя установить фронтенд как PWA:

ts
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react-swc';
import { VitePWA } from 'vite-plugin-pwa';

export default defineConfig({
plugins: [
react(),
VitePWA({
registerType: 'autoUpdate',
manifest: {
id: '/',
name: 'Credit Calculator',
short_name: 'CreditCalc',
description: 'Калькулятор кредитов',
start_url: '/',
display: 'standalone',
theme_color: '#0f172a',
background_color: '#0f172a',
icons: [
{
src: '/icons/icon-192x192.png',
sizes: '192x192',
type: 'image/png',
},
{
src: '/icons/icon-512x512.png',
sizes: '512x512',
type: 'image/png',
},
],
},
}),
],
});
Иконки хранятся в frontend/public/icons:

icon-192x192.png

icon-512x512.png

После сборки фронтенд можно:

открыть в браузере и установить как PWA (Chrome → «Установить приложение» / «Добавить на главный экран»);

на телефоне – через браузер добавить на экран как «приложение».

Запуск веб‑версии
Вариант A. Локальный запуск (без Docker)
1. Backend
   bash
   ./gradlew bootRun --args='--spring.profiles.active=local'
# или, если в application.yml активен профиль local по умолчанию:
# ./gradlew bootRun
Backend поднимется на http://localhost:8081.

2. Frontend (dev‑режим)
   bash
   cd frontend
   npm install      # при первом запуске
   npm run dev
   Vite выведет адрес, например: http://localhost:5174/.

Дальше:

открыть http://localhost:5174/ в браузере,

заполнить форму, нажать «Рассчитать»,

увидеть сводку и график, загруженные с backend.

Вариант B. Всё через Docker (backend + Postgres + фронт как статика)
docker-compose.yml:

text
version: "3.8"

services:
db:
image: postgres:16
container_name: credit-postgres
environment:
POSTGRES_DB: creditdb
POSTGRES_USER: credituser
POSTGRES_PASSWORD: creditpass
ports:
- "5432:5432"
volumes:
- pgdata:/var/lib/postgresql/data
restart: unless-stopped

app:
build: .
container_name: credit-app
depends_on:
- db
ports:
- "8081:8081"
environment:
SPRING_PROFILES_ACTIVE: docker
restart: unless-stopped

volumes:
pgdata:
Сборка и запуск:

bash
./gradlew clean bootJar          # собрать backend + фронт-артефакты
docker compose build             # собрать образы
docker compose up -d             # поднять контейнеры
Приложение будет доступно по адресу: http://localhost:8081/.

Вариант C. Просмотр фронтенда как PWA (npm preview)
Для проверки PWA‑иконок и установки:

bash
cd frontend
npm run build
npm run preview
Vite выведет адрес вида http://localhost:4173/ (порт может отличаться).

Дальше:

открыть в браузере http://localhost:4173/;

в DevTools → Application проверить Manifest (name, icons) и Service Workers;

в Chrome – установить приложение через значок в адресной строке или на телефоне через «Добавить на главный экран».

