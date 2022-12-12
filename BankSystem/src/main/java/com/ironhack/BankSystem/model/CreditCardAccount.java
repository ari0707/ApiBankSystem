package com.ironhack.BankSystem.model;

import com.ironhack.BankSystem.enums.AccountType;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.ironhack.BankSystem.utils.MoneyUtil.convertCurrency;
import static com.ironhack.BankSystem.utils.MoneyUtil.newMoney;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "checking_account")
@PrimaryKeyJoinColumn(name = "id")
@ToString(callSuper = true)
public class CreditCardAccount extends Account{

    @NotNull
    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "credit_limit_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "credit_limit_currency", nullable = false))
    })
    private Money creditLimit;

    @NotNull
    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @NotNull
    @Column(name = "last_interest_update")
    private LocalDate lastInterestUpdate;

    // Constructor con primary y secondary owners.
    // Establece valores por defecto para creditLimit(100 €), interestRate(0.2), lastInterestUpdate(comienzo del próximo mes).
    public CreditCardAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
        super.setAccountType(AccountType.CREDIT_CARD);
        this.creditLimit = newMoney("100.00");
        this.interestRate = new BigDecimal("0.2000");
        this.lastInterestUpdate = getCreationDate().toLocalDate().withDayOfMonth(1).plusMonths(1);
    }

    // Constructor solo con primary owner.
    // Establece valores por defecto para creditLimit(100 €), interestRate(0.2), lastInterestUpdate(comienzo del próximo mes).
    public CreditCardAccount(Money balance, AccountHolder primaryOwner) {
        super(balance, primaryOwner);
        super.setAccountType(AccountType.CREDIT_CARD);
        this.creditLimit = newMoney("100.00");
        this.interestRate = new BigDecimal("0.2000");
        this.lastInterestUpdate = getCreationDate().toLocalDate().withDayOfMonth(1).plusMonths(1);
    }

    public void updateCurrencyValues() {
        setPenaltyFee(convertCurrency(getBalance(), getPenaltyFee()));
        setCreditLimit(convertCurrency(getBalance(), getCreditLimit()));
    }

}
