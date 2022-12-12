package com.ironhack.BankSystem.model;

import com.ironhack.BankSystem.enums.AccountType;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.security.NoSuchAlgorithmException;

import static com.ironhack.BankSystem.utils.MoneyUtil.newMoney;

@Entity
@Table(name = "student_checking_account")
@PrimaryKeyJoinColumn(name = "id")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class StudentCheckingAccount extends CheckingAccount{

    // Constructor solo con primary y secondary owners.
    // Establece valores por defecto para minimumBalance(0 €), monthlyMaintenanceFee(0 €), and penaltyFee(0 €).
    public StudentCheckingAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) throws NoSuchAlgorithmException {
        super(balance, primaryOwner, secondaryOwner);
        super.setAccountType(AccountType.STUDENT_CHECKING_ACCOUNT);
        setMinimumBalance(newMoney("0.00"));
        setMonthlyMaintenanceFee(newMoney("0.00"));
        setPenaltyFee(newMoney("0.00"));
    }

    // Constructor solo primary owner.
    // Establece valores por defecto para minimumBalance(0 €), monthlyMaintenanceFee(0 €), and penaltyFee(0 €).
    public StudentCheckingAccount(Money balance, AccountHolder primaryOwner) throws NoSuchAlgorithmException {
        super(balance, primaryOwner);
        super.setAccountType(AccountType.STUDENT_CHECKING_ACCOUNT);
        setMinimumBalance(newMoney("0.00"));
        setMonthlyMaintenanceFee(newMoney("0.00"));
        setPenaltyFee(newMoney("0.00"));
    }
}
