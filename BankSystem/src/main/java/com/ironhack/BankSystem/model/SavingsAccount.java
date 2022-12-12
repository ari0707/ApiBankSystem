package com.ironhack.BankSystem.model;

import com.ironhack.BankSystem.enums.AccountStatus;
import com.ironhack.BankSystem.enums.AccountType;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import static com.ironhack.BankSystem.utils.EncryptedKeysUtil.generateSecretKey;
import static com.ironhack.BankSystem.utils.MoneyUtil.convertCurrency;
import static com.ironhack.BankSystem.utils.MoneyUtil.newMoney;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "savings_account")
@PrimaryKeyJoinColumn(name = "id")
@ToString(callSuper = true)
public class SavingsAccount extends Account{

    @NotNull
    @Column(name = "secret_key")
    private String secretKey;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus accountStatus;

    @NotNull
    @Column(name = "interest_rate", precision = 16, scale = 4)
    private BigDecimal interestRate;

    @NotNull
    @Column(name = "last_interest_update")
    private LocalDate lastInterestUpdate;

    @NotNull
    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "min_balance_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "min_balance_currency", nullable = false))
    })
    private Money minimumBalance;


    // Constructor con primary y secondary owners.
    // Establece valores por defecto para minimumBalance(1000 €), interestRate(0.0025), lastInterestUpdate(comienzo del próximo mes),
    // accountStatus(active), y genera secretKey(random key).
    public SavingsAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) throws NoSuchAlgorithmException {
        super(balance, primaryOwner, secondaryOwner);
        super.setAccountType(AccountType.SAVINGS_ACCOUNT);
        this.minimumBalance = newMoney("1000.00");
        this.interestRate = new BigDecimal("0.0025");
        this.lastInterestUpdate = getCreationDate().toLocalDate().withDayOfMonth(1).plusMonths(1);
        this.accountStatus = AccountStatus.ACTIVE;
        this.secretKey = generateSecretKey();
    }

    // Constructor solo con primary owner.
    // Establece valores por defecto para minimumBalance(1000 €), interestRate(0.0025), lastInterestUpdate(comienzo del próximo mes),
    // accountStatus(active), y genera secretKey(random key).
    public SavingsAccount(Money balance, AccountHolder primaryOwner) throws NoSuchAlgorithmException {
        super(balance, primaryOwner);
        super.setAccountType(AccountType.SAVINGS_ACCOUNT);
        this.minimumBalance = newMoney("1000.00");
        this.interestRate = new BigDecimal("0.0025");
        this.lastInterestUpdate = getCreationDate().toLocalDate().withDayOfMonth(1).plusMonths(1);
        this.accountStatus = AccountStatus.ACTIVE;
        this.secretKey = generateSecretKey();
    }

    public void updateCurrencyValues() {
        setPenaltyFee(convertCurrency(getBalance(), getPenaltyFee()));
        setMinimumBalance(convertCurrency(getBalance(), getMinimumBalance()));
    }
}
