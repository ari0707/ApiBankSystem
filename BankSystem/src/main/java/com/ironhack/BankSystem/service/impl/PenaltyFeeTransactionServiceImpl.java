package com.ironhack.BankSystem.service.impl;

import com.ironhack.BankSystem.model.Account;
import com.ironhack.BankSystem.model.Money;
import com.ironhack.BankSystem.model.Transaction;
import com.ironhack.BankSystem.repository.ITransactionRepository;
import com.ironhack.BankSystem.service.interfaces.IAccountService;
import com.ironhack.BankSystem.service.interfaces.IPenaltyFeeTransactionService;
import com.ironhack.BankSystem.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ironhack.BankSystem.utils.MoneyUtil.compareMoney;
import static com.ironhack.BankSystem.utils.MoneyUtil.subtractMoney;

@Service
public class PenaltyFeeTransactionServiceImpl implements IPenaltyFeeTransactionService {

    @Autowired
    private ITransactionRepository transactionRepository;


    @Autowired
    private IAccountService accountService;

    @Autowired
    private ITransactionService transactionService;

    public void newTransaction(long accountId) {
        Account account = accountService.getById(accountId);

        Money PenaltyFeeAmount = account.getPenaltyFee();

        Transaction transaction = transactionRepository.save(
                new Transaction(PenaltyFeeAmount, account)
        );
        validateTransaction(transaction);
        accountService.updateBalance(account);
    }

    public void newTransaction(long accountId, Money remaining) {
        Account account = accountService.getById(accountId);

        Transaction transaction = transactionRepository.save(
                new Transaction(remaining, account)
        );
        processTransaction(transaction);
    }


    public void validateTransaction(Transaction transaction) {
        // Comprueba si el estado es frozen
        if (!transactionService.isAccountFrozen(transaction)) {
            if (!isTransactionAmountValid(transaction)) {
                accountService.freezeAccount(transaction.getTargetAccount().getId());
                newTransaction(transaction.getTargetAccount().getId(), transaction.getTargetAccount().getBalance());

            } else {
                processTransaction(transaction);

            }
        }
        accountService.save(transaction.getTargetAccount());
    }

    public void processTransaction(Transaction transaction) {
        Account account = accountService.getById(transaction.getTargetAccount().getId());

        account.setBalance(subtractMoney(account.getBalance(), transaction.getConvertedAmount()));
        account.setLastPenaltyFeeCheck(account.getLastPenaltyFeeCheck().plusMonths(1));

        accountService.save(account);
    }

    // monto de la transferencia no sea menor al saldo de cuenta y cuenta y que no esté en estado frozen
    public boolean isTransactionAmountValid(Transaction transaction) {
        return compareMoney(transaction.getTargetAccount().getBalance(), transaction.getBaseAmount()) >= 0;
    }
}
