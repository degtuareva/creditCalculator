package org.productStar.api;

import lombok.Getter;
import lombok.Setter;
import org.productStar.domain.Payment;

import java.util.List;

@Setter
@Getter
public class CalculationResponse {
    private CalculationSummary summary;
    private List<Payment> payments;
}