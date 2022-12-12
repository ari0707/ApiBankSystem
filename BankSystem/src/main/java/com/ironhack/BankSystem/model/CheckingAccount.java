package com.ironhack.BankSystem.model;

import com.ironhack.BankSystem.enums.AccountStatus;
import com.ironhack.BankSystem.enums.AccountType;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.jetbrains.annotations.NotNull;

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
@Table(name = "checking_account")
@PrimaryKeyJoinColumn(name = "id")
@ToString(callSuper = true)
public class CheckingAccount extends Account{

    @NotNull
    @Column(name = "secret_key")
    private String secretKey;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus accountStatus;

    @NotNull
    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "min_balance_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "min_balance_currency", nullable = false))
    })
    private Money minimumBalance;

    @NotNull
    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "monthly_maintenance_fee_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "monthly_maintenance_fee_currency", nullable = false))
    })
    private Money monthlyMaintenanceFee;

    @NotNull
    @Column(name = "last_maintenance_fee")
    private LocalDate lastMaintenanceFee;

    // Constructor con primary y secondary owners.
    // Establece por default valores para minimumBalance(250 €), monthlyMaintenanceFee(12 €), lastMaintenanceFee(comienzo del próximo mes),
    // accountStatus(active), y genera secretKey(random key).
    public CheckingAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) throws NoSuchAlgorithmException {
        super(balance, primaryOwner, secondaryOwner);
        super.setAccountType(AccountType.CHECKING_ACCOUNT);
        this.minimumBalance = newMoney("250.00");
        this.monthlyMaintenanceFee = newMoney("12.00");
        this.lastMaintenanceFee = getCreationDate().toLocalDate().withDayOfMonth(1).plusMonths(1);
        this.accountStatus = AccountStatus.ACTIVE;
        this.secretKey = generateSecretKey();
    }

    // Constructor solo con primary owner.
    // Establece por default valores para minimumBalance(250 €), monthlyMaintenanceFee(12 €), lastMaintenanceFee(comienzo del próximo mes),
    // accountStatus(active), y genera secretKey(random key).
    public CheckingAccount(Money balance, AccountHolder primaryOwner) throws NoSuchAlgorithmException {
        super(balance, primaryOwner);
        super.setAccountType(AccountType.CHECKING_ACCOUNT);
        this.minimumBalance = newMoney("250.00");
        this.monthlyMaintenanceFee = newMoney("12.00");
        this.lastMaintenanceFee = getCreationDate().toLocalDate().withDayOfMonth(1).plusMonths(1);
        this.accountStatus = AccountStatus.ACTIVE;
        this.secretKey = generateSecretKey();
    }

    // Método que convierte las divisas para que tengan la misma divisa que el saldo de la cuenta.
    public void updateCurrencyValues() {
        setPenaltyFee(convertCurrency(getBalance(), getPenaltyFee()));
        setMinimumBalance(convertCurrency(getBalance(), getMinimumBalance()));
        setMonthlyMaintenanceFee(convertCurrency(getBalance(), getMonthlyMaintenanceFee()));
    }
}
