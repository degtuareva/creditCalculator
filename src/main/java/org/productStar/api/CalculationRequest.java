package org.productStar.api;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CalculationRequest {
    private double principal;
    private double downPayment;
    private int years;
    private double annualInterestRate;
    private String scheduleType; // "ANNUITY" или "DIFFERENTIATED"
}