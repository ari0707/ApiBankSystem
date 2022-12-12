package com.ironhack.BankSystem.model;



import com.ironhack.BankSystem.enums.Status;
import com.ironhack.BankSystem.enums.TransactionPurpose;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.ironhack.BankSystem.utils.DataTimeUtil.dateTimeNow;
import static com.ironhack.BankSystem.utils.MoneyUtil.convertCurrency;

@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "base_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "base_currency", nullable = false))
    })
    private Money baseAmount;

    @NotNull
    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "converted_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "converted_currency", nullable = false))
    })
    private Money convertedAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_account_id")
    private Account baseAccount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_account_id")
    private Account targetAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_owner_id")
    private AccountHolder targetOwner;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_purpose")
    private TransactionPurpose transactionPurpose;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @NotNull
    @Column(name = "operation_date")
    private LocalDateTime operationDate;


    // Constructor con target, base accounts y target owner para Local Transactions
    // Permite convertir el monto para ajustarlo a la cuenta de destino(targetAccount), establecer el estado de procesamiento
    // y establecer la fecha de operación
    public Transaction(Money baseAmount, Account baseAccount, Account targetAccount, AccountHolder targetOwner) {
        this.baseAmount = baseAmount;
        this.convertedAmount = convertCurrency(targetAccount.getBalance(), baseAmount);
        this.baseAccount = baseAccount;
        this.targetAccount = targetAccount;
        this.targetOwner = targetOwner;
        this.status = Status.PROCESSING;
        this.operationDate = dateTimeNow();
    }

    // Constructor target account y transaction purpose para third party transactions
    // Permite convertir el monto para ajustarlo a la cuenta de destino(targetAccount), establecer el estado de procesamiento
    // y establecer la fecha de operación
    public Transaction(Money baseAmount, Account targetAccount, TransactionPurpose transactionPurpose) {
        this.baseAmount = baseAmount;
        this.convertedAmount = convertCurrency(targetAccount.getBalance(), baseAmount);
        this.targetAccount = targetAccount;
        this.transactionPurpose = transactionPurpose;
        this.status = Status.PROCESSING;
        this.operationDate = dateTimeNow();
    }

    // Constructor solo con target account.
    // Permite convertir el monto para ajustarlo a la cuenta de destino(targetAccount), establecer el estado de procesamiento
    // y establecer la fecha de operación
    public Transaction(Money baseAmount, Account targetAccount) {
        this.baseAmount = baseAmount;
        this.convertedAmount = convertCurrency(targetAccount.getBalance(), baseAmount);
        this.targetAccount = targetAccount;
        this.status = Status.PROCESSING;
        this.operationDate = dateTimeNow();
    }
}
