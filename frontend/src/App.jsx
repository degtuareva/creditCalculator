import { useState } from "react";
import "./App.css";
import { API_BASE_URL } from "./config.js";

const SCHEDULE_TYPES = {
    ANNUITY: "ANNUITY",
    DIFFERENTIATED: "DIFFERENTIATED",
};

function App() {
    const [principal, setPrincipal] = useState(100000);
    const [downPayment, setDownPayment] = useState(0);
    const [years, setYears] = useState(5);
    const [rate, setRate] = useState(12);
    const [scheduleType, setScheduleType] = useState(SCHEDULE_TYPES.ANNUITY);

    const [summary, setSummary] = useState(null);
    const [payments, setPayments] = useState([]);
    const [error, setError] = useState("");

    const handleCalculate = async () => {
        const p = Number(principal);
        const d = Number(downPayment);
        const y = Number(years);
        const r = Number(rate);

        const errors = [];
        if (!Number.isFinite(p) || p <= 0)
            errors.push("Сумма кредита должна быть > 0.");
        if (!Number.isFinite(d) || d < 0)
            errors.push("Первоначальный взнос не может быть отрицательным.");
        if (d > p)
            errors.push("Первоначальный взнос не может быть больше суммы кредита.");
        if (!Number.isInteger(y) || y <= 0)
            errors.push("Срок кредита (лет) должен быть целым числом > 0.");
        if (!Number.isFinite(r) || r <= 0)
            errors.push("Ставка должна быть > 0.");

        if (errors.length > 0) {
            setError(errors.join(" "));
            setSummary(null);
            setPayments([]);
            return;
        }

        setError("");
        setSummary(null);
        setPayments([]);

        const body = {
            principal: Number(principal),
            downPayment: Number(downPayment),
            years: Number(years),
            annualInterestRate: Number(rate),
            scheduleType,
        };

        try {
            console.log("API_BASE_URL =", API_BASE_URL);
            console.log("REQUEST BODY =", body);

            const response = await fetch(
                `${API_BASE_URL}/api/credit/calculate`,
                {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(body),
                }
            );
            console.log("📡 FETCH RESPONSE:", response.status, response.statusText);

            if (!response.ok) {
                const text = await response.text().catch(() => "");
                console.error("Server error:", response.status, text);
                setError("Ошибка на сервере: " + response.status);
                return;
            }

            const data = await response.json();
            setSummary(data.summary);
            setPayments(data.payments);
        } catch (error) {
            console.error("❌ FETCH ERROR:", error);
            console.error("❌ NETWORK STATE:", {
                url: `${API_BASE_URL}/api/credit/calculate`,
                apiBaseUrl: API_BASE_URL
            });
        }
    };

    return (
        <div className="app">
            <header className="app-header">
                <h1>Кредитный калькулятор</h1>
                <p>React + Spring Boot</p>
            </header>

            <main className="app-main">
                <section className="card">
                    <h2>Параметры кредита</h2>
                    <div className="form-grid">
                        <label className="form-field">
                            <span>Сумма кредита</span>
                            <input
                                type="number"
                                value={principal}
                                onChange={(e) => setPrincipal(e.target.value)}
                            />
                        </label>

                        <label className="form-field">
                            <span>Первоначальный взнос</span>
                            <input
                                type="number"
                                value={downPayment}
                                onChange={(e) => setDownPayment(e.target.value)}
                            />
                        </label>

                        <label className="form-field">
                            <span>Срок (лет)</span>
                            <input
                                type="number"
                                value={years}
                                onChange={(e) => setYears(e.target.value)}
                            />
                        </label>

                        <label className="form-field">
                            <span>Ставка (% годовых)</span>
                            <input
                                type="number"
                                value={rate}
                                onChange={(e) => setRate(e.target.value)}
                            />
                        </label>

                        <label className="form-field">
                            <span>Тип графика</span>
                            <select
                                value={scheduleType}
                                onChange={(e) => setScheduleType(e.target.value)}
                            >
                                <option value={SCHEDULE_TYPES.ANNUITY}>Аннуитетный</option>
                                <option value={SCHEDULE_TYPES.DIFFERENTIATED}>
                                    Дифференцированный
                                </option>
                            </select>
                        </label>
                    </div>

                    <button className="btn-primary" onClick={handleCalculate}>
                        Рассчитать
                    </button>

                    {error && <div className="error">{error}</div>}
                </section>

                {summary && (
                    <section className="card">
                        <h2>Результаты</h2>
                        <div className="summary-grid">
                            <div>
                                <span className="label">Сумма кредита:</span>
                                <span className="value">
                  {summary.originalPrincipal.toFixed(2)} ₽
                </span>
                            </div>
                            <div>
                                <span className="label">Первоначальный взнос:</span>
                                <span className="value">
                  {summary.downPayment.toFixed(2)} ₽
                </span>
                            </div>
                            <div>
                                <span className="label">Сумма к выплате:</span>
                                <span className="value">
                  {summary.actualPrincipal.toFixed(2)} ₽
                </span>
                            </div>
                            <div>
                                <span className="label">Общая сумма выплат:</span>
                                <span className="value">
                  {summary.totalPayment.toFixed(2)} ₽
                </span>
                            </div>
                            <div>
                                <span className="label">Переплата:</span>
                                <span className="value">
                  {summary.overpayment.toFixed(2)} ₽
                </span>
                            </div>
                            <div>
                                <span className="label">Проценты:</span>
                                <span className="value">
                  {summary.totalInterest.toFixed(2)} ₽
                </span>
                            </div>
                        </div>
                    </section>
                )}

                {payments.length > 0 && (
                    <section className="card">
                        <h2>График платежей</h2>
                        <div className="table-wrapper">
                            <table className="schedule-table">
                                <thead>
                                <tr>
                                    <th>Месяц</th>
                                    <th>Основной долг</th>
                                    <th>Проценты</th>
                                    <th>Общий платёж</th>
                                </tr>
                                </thead>
                                <tbody>
                                {payments.map((p) => (
                                    <tr key={p.month}>
                                        <td>{p.month}</td>
                                        <td>{p.principalPayment.toFixed(2)}</td>
                                        <td>{p.interestPayment.toFixed(2)}</td>
                                        <td>{p.totalPayment.toFixed(2)}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </section>
                )}
            </main>
        </div>
    );
}

export default App;
