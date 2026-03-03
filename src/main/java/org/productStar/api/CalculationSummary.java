package org.productStar.api;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CalculationSummary {
    private double originalPrincipal;
    private double downPayment;
    private double actualPrincipal;
    private double totalPayment;
    private double totalInterest;
    private double overpayment;
}