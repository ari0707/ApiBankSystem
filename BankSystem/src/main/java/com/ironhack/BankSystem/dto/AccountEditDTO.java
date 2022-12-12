package com.ironhack.BankSystem.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountEditDTO {

    private String primaryOwnerUsername;

    private String secondaryOwnerUsername;

    // Para todas las cuentas excepto CreditCardAccount
    private String accountStatus;

    private String currency;

    @PositiveOrZero(message = "Balance must be positive or zero.")
    private BigDecimal accountBalance;


    @PositiveOrZero(message = "Penalty fee must be positive or zero.")
    private BigDecimal penaltyFee;

    private LocalDate lastPenaltyFee;


    @PositiveOrZero(message = "Minimum balance must be positive or zero.")
    private BigDecimal minimumBalance;


    @PositiveOrZero(message = "Credit Limit must be positive or zero.")
    private BigDecimal creditLimit;


    @PositiveOrZero(message = "Monthly maintenance fee must be positive or zero.")
    private BigDecimal monthlyMaintenanceFee;

    private LocalDate lastMaintenanceFee;


    @PositiveOrZero(message = "Savings account interest rate must be positive or zero.")
    @DecimalMax(value = "0.5", message = "Savings account interest rate must be lower than 0.5")
    private BigDecimal savingsAccountInterestRate;

    @DecimalMin(value = "0.1", message = "Credit card interest rate must be greater than 0.1")
    private BigDecimal creditCardInterestRate;

    private LocalDate lastInterestUpdate;
}
