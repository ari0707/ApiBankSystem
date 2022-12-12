package com.ironhack.BankSystem.service.interfaces;

import com.ironhack.BankSystem.model.Transaction;

public interface IInterestTransactionService {

    void newTransaction(long accountId);

    void validateTransaction(Transaction transaction);

    void processTransaction(Transaction transaction);
}
