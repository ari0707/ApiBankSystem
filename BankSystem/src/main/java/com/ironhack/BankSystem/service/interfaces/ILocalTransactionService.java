package com.ironhack.BankSystem.service.interfaces;

import com.ironhack.BankSystem.dto.TransactionLocalDTO;
import com.ironhack.BankSystem.model.Transaction;

public interface ILocalTransactionService {

    void newTransaction(long accountId, TransactionLocalDTO localTransaction);

    void validateTransaction(Transaction transaction);

    void processTransaction(Transaction transaction);

    boolean isTransactionAmountValid(Transaction transaction);
}
