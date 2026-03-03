package org.productStar.db;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="credit_calculation")
public class CreditCalculationEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private double originalPrincipal;
    private double downPayment;
    private double actualPrincipal;
    private double totalPayment;
    private double totalInterest;
    private double overpayment;

    private String scheduleType;
    private LocalDateTime createdAt;
}