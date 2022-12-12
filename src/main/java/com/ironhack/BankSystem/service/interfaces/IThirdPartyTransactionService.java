package com.ironhack.BankSystem.service.interfaces;

import com.ironhack.BankSystem.dto.TransactionThirdPartyDTO;
import com.ironhack.BankSystem.model.Transaction;

public interface IThirdPartyTransactionService {

    void newTransaction(String hashedKey, TransactionThirdPartyDTO thirdPartyTransaction);

    void validateTransaction(Transaction transaction);

    void processTransaction(Transaction transaction);

    boolean isTransactionAmountValid(Transaction transaction);
}
