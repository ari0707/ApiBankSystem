package com.ironhack.BankSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.BankSystem.enums.AccountType;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.ironhack.BankSystem.utils.DataTimeUtil.dateTimeNow;
import static com.ironhack.BankSystem.utils.MoneyUtil.newMoney;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "account")
@Inheritance(strategy = InheritanceType.JOINED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    @NotNull
    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "balance_currency", nullable = false))
    })
    private Money balance;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_owner_id")
    private AccountHolder primaryOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secondary_owner_id")
    private AccountHolder secondaryOwner;

    @NotNull
    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "penaltyFee_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "penaltyFee_currency", nullable = false))
    })
    private Money penaltyFee;

    @NotNull
    @Column(name = "last_penalty_fee_check")
    private LocalDate lastPenaltyFeeCheck;

    @NotNull
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "targetAccount")
    private List<Transaction> incomingTransactions = new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "baseAccount")
    private List<Transaction> outgoingTransactions = new ArrayList<>();

    // Constructor solo con primary y secondary owners
    public Account(@NotNull Money balance, @NotNull AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.penaltyFee = newMoney("40.00");
        this.creationDate = dateTimeNow();
        this.lastPenaltyFeeCheck = getCreationDate().toLocalDate().withDayOfMonth(1);
    }

    // Constructor solo con primary owner
    public Account(@NotNull Money balance, @NotNull AccountHolder primaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.penaltyFee = newMoney("40.00");
        this.creationDate = dateTimeNow();
        this.lastPenaltyFeeCheck = getCreationDate().toLocalDate().withDayOfMonth(1);
    }

    //Método que devuelve todas las transacciones ordenadas por fecha de operación
    @JsonIgnore
    public List<Transaction> getAllTransactionsOrdered() {
        List<Transaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(incomingTransactions);
        allTransactions.addAll(outgoingTransactions);
        allTransactions.sort(Comparator.comparing(Transaction::getOperationDate).reversed());
        return allTransactions;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "accountType = " + accountType + ", " +
                "balance = " + balance + ", " +
                "primaryOwner = " + primaryOwner.getId() + ": " + primaryOwner.getUsername() + ", " +
                (secondaryOwner != null ?
                        "secondaryOwner = " + secondaryOwner.getId() + ": " + secondaryOwner.getUsername() + ", " :
                        ""
                ) +
                "penaltyFee = " + penaltyFee + ", " +
                "lastPenaltyFeeCheck = " + lastPenaltyFeeCheck + ", " +
                "creationDate = " + creationDate + ")";
    }

}
