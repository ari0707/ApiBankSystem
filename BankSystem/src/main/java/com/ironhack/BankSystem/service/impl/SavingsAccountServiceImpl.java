package com.ironhack.BankSystem.service.impl;

import com.ironhack.BankSystem.dto.AccountDTO;
import com.ironhack.BankSystem.enums.AccountStatus;
import com.ironhack.BankSystem.model.AccountHolder;
import com.ironhack.BankSystem.model.SavingsAccount;
import com.ironhack.BankSystem.repository.ISavingsAccountRepository;
import com.ironhack.BankSystem.service.interfaces.IAccountHolderService;
import com.ironhack.BankSystem.service.interfaces.IInterestTransactionService;
import com.ironhack.BankSystem.service.interfaces.IPenaltyFeeTransactionService;
import com.ironhack.BankSystem.service.interfaces.ISavingsAccountService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;

import static com.ironhack.BankSystem.utils.DataTimeUtil.dateTimeNow;
import static com.ironhack.BankSystem.utils.MoneyUtil.compareMoney;
import static com.ironhack.BankSystem.utils.MoneyUtil.newMoney;

@Service
public class SavingsAccountServiceImpl implements ISavingsAccountService {

    @Autowired
    private ISavingsAccountRepository savingsAccountRepository;

    @Autowired
    private IAccountHolderService accountHolderService;

    @Autowired
    private IInterestTransactionService interestTransactionService;

    @Autowired
    private IPenaltyFeeTransactionService penaltyFeeTransactionService;

    public List<SavingsAccount> getAll() {
        return savingsAccountRepository.findAllJoined();
    }

    public void newAccount(AccountDTO savingsAccount) throws EntityNotFoundException, IllegalArgumentException, NoSuchAlgorithmException {
        // verifica la identidad de ambos propietarios de la cuenta.
        AccountHolder[] accountHolders = accountHolderService.findAccountHolders(savingsAccount);

        SavingsAccount sa = new SavingsAccount(newMoney(savingsAccount.getInitialBalance().toString(), savingsAccount.getCurrency().toUpperCase()), accountHolders[0], accountHolders[1]);
        sa.updateCurrencyValues();

        savingsAccountRepository.save(sa);
    }


    public void checkInterestRate(SavingsAccount savingsAccount) {
        LocalDate lastInterestDate = savingsAccount.getLastInterestUpdate();

        if (savingsAccount.getAccountStatus() == AccountStatus.ACTIVE &&
                lastInterestDate.plusYears(1).isBefore(dateTimeNow().toLocalDate()))
            interestTransactionService.newTransaction(savingsAccount.getId());
    }


    public void checkMinimumBalance(SavingsAccount savingsAccount) {
        LocalDate lastPenaltyFee = savingsAccount.getLastPenaltyFeeCheck();

        if (savingsAccount.getAccountStatus() == AccountStatus.ACTIVE &&
                compareMoney(savingsAccount.getBalance(), savingsAccount.getMinimumBalance()) < 0) {

            if (lastPenaltyFee.plusMonths(1).isBefore(dateTimeNow().toLocalDate())) {
                penaltyFeeTransactionService.newTransaction(savingsAccount.getId());
            }
        } else if (savingsAccount.getLastPenaltyFeeCheck().isBefore(dateTimeNow().toLocalDate().minusMonths(1).minusDays(1))) {
            savingsAccount.setLastPenaltyFeeCheck(dateTimeNow().toLocalDate().minusMonths(1).minusDays(1));
            savingsAccountRepository.save(savingsAccount);
        }
    }
}
