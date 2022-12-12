package com.ironhack.BankSystem.controller.interfaces;

import com.ironhack.BankSystem.dto.TransactionLocalDTO;
import com.ironhack.BankSystem.dto.TransactionThirdPartyDTO;
import com.ironhack.BankSystem.model.Transaction;

import java.util.List;

public interface ITransactionController {

    // Transacción específica de la cuenta
    List<Transaction> getTransactions(long id);

    // Transacción específica por usuario por Id
    Transaction getTransactionsById(long accountId, long transactionId);


//    // POST TRANSACTION Methods
//    // Add Account Specific Local Transaction [ADMIN / Specific USER]
//    void createLocalTransaction(long id, TransactionLocalDTO localTransaction);

    // Agregar transacción de terceros específica de la cuenta
    void createThirdPartyTransaction(String hashedKey, TransactionThirdPartyDTO thirdPartyTransaction);
}
