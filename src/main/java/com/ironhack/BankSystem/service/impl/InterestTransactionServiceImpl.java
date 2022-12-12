package com.ironhack.BankSystem.service.impl;

import com.ironhack.BankSystem.model.*;
import com.ironhack.BankSystem.repository.ITransactionRepository;
import com.ironhack.BankSystem.service.interfaces.IAccountService;
import com.ironhack.BankSystem.service.interfaces.IInterestTransactionService;
import com.ironhack.BankSystem.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.ironhack.BankSystem.utils.MoneyUtil.addMoney;

@Service
public class InterestTransactionServiceImpl implements IInterestTransactionService {

    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ITransactionService transactionService;

    public void newTransaction(long accountId) {
        Account account = accountService.getById(accountId);

        Money interestAmount;
        //Cuenta Savings
        if (account.getClass() == SavingsAccount.class) {
            BigDecimal interestRate = ((SavingsAccount) account).getInterestRate();
            interestAmount = new Money(account.getBalance().getAmount().multiply(interestRate), account.getBalance().getCurrency());

            //Credit Card
        } else if (account.getClass() == CreditCardAccount.class) {
            BigDecimal interestRate = ((CreditCardAccount) account).getInterestRate().divide(new BigDecimal("12"), 4, RoundingMode.HALF_EVEN);
            interestAmount = new Money(account.getBalance().getAmount().multiply(interestRate), account.getBalance().getCurrency());

        } else throw new IllegalArgumentException("Error when using account");

        Transaction transaction = transactionRepository.save(
                new Transaction(
                        interestAmount,
                        account
                )
        );
        validateTransaction(transaction);
        accountService.updateBalance(account);
    }


    // VALIDATE TRANSACTION Methods
    public void validateTransaction(Transaction transaction) {
        // Check if frozen.
        if (!transactionService.isAccountFrozen(transaction)) {
            processTransaction(transaction);
        }
        accountService.save(transaction.getTargetAccount());
    }

    //  PROCESS TRANSACTION Methods
    public void processTransaction(Transaction transaction) {
        Account account = accountService.getById(transaction.getTargetAccount().getId());

        account.setBalance(addMoney(account.getBalance(), transaction.getConvertedAmount()));

        // update next interest date
        if (account.getClass() == SavingsAccount.class) {
            ((SavingsAccount) account).setLastInterestUpdate(((SavingsAccount) account).getLastInterestUpdate().plusYears(1));
        } else if (account.getClass() == CreditCardAccount.class) {
            ((CreditCardAccount) account).setLastInterestUpdate(((CreditCardAccount) account).getLastInterestUpdate().plusMonths(1));
        }
        accountService.save(account);
    }

}
