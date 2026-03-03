package org.productStar.api;

import org.productStar.CalculatorFactory;
import org.productStar.ICalculator;
import org.productStar.ScheduleType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/credit")
@CrossOrigin(origins = "http://localhost:5174") // React dev-сервер
public class CreditController {

    @PostMapping("/calculate")
    public ResponseEntity<CalculationResponse> calculate(@RequestBody CalculationRequest request) {
        if (request.getPrincipal() <= 0 ||
                request.getYears() <= 0 ||
                request.getAnnualInterestRate() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        if (request.getDownPayment() < 0 || request.getDownPayment() > request.getPrincipal()) {
            return ResponseEntity.badRequest().build();
        }

        double principal = request.getPrincipal();
        double downPayment = request.getDownPayment();
        double actualPrincipal = principal - downPayment;

        ScheduleType type = ScheduleType.valueOf(request.getScheduleType());
        boolean hasDiscount = downPayment > 0;

        ICalculator calculator = CalculatorFactory.create(type, hasDiscount);
        calculator.setPrincipal(actualPrincipal);
        calculator.setAnnualInterestRate(request.getAnnualInterestRate());
        calculator.setYears(request.getYears());
        calculator.calculatePayments();

        double totalPayment = calculator.getTotalPayment();
        double totalInterest = calculator.getTotalInterest();
        double overpayment = totalPayment - actualPrincipal;

        CalculationSummary summary = new CalculationSummary();
        summary.setOriginalPrincipal(principal);
        summary.setDownPayment(downPayment);
        summary.setActualPrincipal(actualPrincipal);
        summary.setTotalPayment(totalPayment);
        summary.setTotalInterest(totalInterest);
        summary.setOverpayment(overpayment);

        CalculationResponse response = new CalculationResponse();
        response.setSummary(summary);
        response.setPayments(calculator.getPaymentsSchedule());

        return ResponseEntity.ok(response);
    }
}