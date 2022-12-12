package com.ironhack.BankSystem.service.interfaces;

import com.ironhack.BankSystem.model.Money;
import com.ironhack.BankSystem.model.Transaction;

public interface IMaintenanceFeeTransactionService {

    void newTransaction(long accountId);

    void newTransaction(long accountId, Money remaining);


    void validateTransaction(Transaction transaction);

    void processTransaction(Transaction transaction);

    boolean isTransactionAmountValid(Transaction transaction);
}
