package org.productStar.api;

import org.productStar.db.CreditCalculationEntity;
import org.productStar.db.CreditCalculationRepository;
import org.productStar.domain.ScheduleType;
import org.productStar.service.CalculatorFactory;
import org.productStar.service.ICalculator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/credit")
//@CrossOrigin(origins = "*") // на время отладки можно разрешить всех
@CrossOrigin(origins = {"http://localhost:3000", "capacitor://localhost", "*"})
public class CreditController {

    private final CreditCalculationRepository repository;

    public CreditController(CreditCalculationRepository repository) {
        this.repository = repository;
    }

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

        CreditCalculationEntity entity = new CreditCalculationEntity();
        entity.setOriginalPrincipal(principal);
        entity.setDownPayment(downPayment);
        entity.setActualPrincipal(actualPrincipal);
        entity.setTotalPayment(totalPayment);
        entity.setTotalInterest(totalInterest);
        entity.setOverpayment(overpayment);
        entity.setScheduleType(type.name());
        entity.setCreatedAt(LocalDateTime.now());

        // Временно не сохраняем, чтобы не упираться в права PostgreSQL
        // repository.save(entity);

        return ResponseEntity.ok(response);
    }
}
