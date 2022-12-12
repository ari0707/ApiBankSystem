package com.ironhack.BankSystem.service.impl;

import com.ironhack.BankSystem.dto.TransactionLocalDTO;
import com.ironhack.BankSystem.model.*;
import com.ironhack.BankSystem.repository.ITransactionRepository;
import com.ironhack.BankSystem.service.interfaces.IAccountService;
import com.ironhack.BankSystem.service.interfaces.ILocalTransactionService;
import com.ironhack.BankSystem.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import static com.ironhack.BankSystem.utils.MoneyUtil.*;
import static com.ironhack.BankSystem.utils.UserUtil.isSameUserName;

@Service
public class LocalTransactionServiceImpl implements ILocalTransactionService {

    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ITransactionService transactionService;


    public void newTransaction(long accountId, TransactionLocalDTO localTransaction) {
        Account ownerAccount = accountService.getById(accountId);
        Account targetAccount = accountService.getById(localTransaction.getTargetAccountId());

        AccountHolder targetOwner;
        if (isSameUserName(targetAccount.getPrimaryOwner().getName(), localTransaction.getTargetOwnerName())) {
            targetOwner = targetAccount.getPrimaryOwner();
        } else if (targetAccount.getSecondaryOwner() != null && isSameUserName(targetAccount.getSecondaryOwner().getName(), localTransaction.getTargetOwnerName())) {
            targetOwner = targetAccount.getSecondaryOwner();
        } else throw new IllegalArgumentException("Target owner name does not correspond to target account owner.");


        Transaction transaction = transactionRepository.save(
                new Transaction(
                        new Money(localTransaction.getTransferValue(), Currency.getInstance(localTransaction.getCurrency())),
                        ownerAccount,
                        targetAccount,
                        targetOwner)
        );
        validateTransaction(transaction);
        accountService.updateBalance(targetAccount);
        accountService.updateBalance(ownerAccount);
    }

    public void validateTransaction(Transaction transaction) {
        // Comprueba si el status es Frozen
        if (!transactionService.isAccountFrozen(transaction)) {

            if (transactionService.isTransactionTimeFraudulent(transaction.getBaseAccount()) ||
                    transactionService.isTransactionDailyAmountFraudulent(transaction.getBaseAccount())) {
                accountService.freezeAccount(transaction.getBaseAccount().getId());

                // Comprueba si el monto de la transacción no es válido.
            } else if (isTransactionAmountValid(transaction)) {
                processTransaction(transaction);

            }

        }
        accountService.save(transaction.getBaseAccount());
        accountService.save(transaction.getTargetAccount());
    }


    public void processTransaction(Transaction transaction) {
        Account baseAccount = accountService.getById(transaction.getBaseAccount().getId());
        Account targetAccount = accountService.getById(transaction.getTargetAccount().getId());

        baseAccount.setBalance(subtractMoney(baseAccount.getBalance(), transaction.getConvertedAmount()));
        targetAccount.setBalance(addMoney(targetAccount.getBalance(), transaction.getConvertedAmount()));

        accountService.save(baseAccount);
        accountService.save(targetAccount);
    }



    // Comprueba si el monto de la transferencia es menor al saldo de la cuenta
    public boolean isTransactionAmountValid(Transaction transaction) {
        return compareMoney(transaction.getBaseAccount().getBalance(), transaction.getBaseAmount()) >= 0;
    }
}
