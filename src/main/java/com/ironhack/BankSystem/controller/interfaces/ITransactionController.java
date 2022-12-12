package com.ironhack.BankSystem.controller.interfaces;

import com.ironhack.BankSystem.dto.TransactionLocalDTO;
import com.ironhack.BankSystem.dto.TransactionThirdPartyDTO;
import com.ironhack.BankSystem.model.Transaction;

import java.util.List;

public interface ITransactionController {

    List<Transaction> getTransactions(long id);


    Transaction getTransactionsById(long accountId, long transactionId);

    void createThirdPartyTransaction(String hashedKey, TransactionThirdPartyDTO thirdPartyTransaction);
}
