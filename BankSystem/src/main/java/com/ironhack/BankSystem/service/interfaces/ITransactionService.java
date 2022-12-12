package com.ironhack.BankSystem.service.interfaces;

import com.ironhack.BankSystem.model.Account;
import com.ironhack.BankSystem.model.Money;
import com.ironhack.BankSystem.model.Transaction;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface ITransactionService {

    List<Transaction> getAllByAccountId(long AccountId);

    Transaction getById(long AccountId, long transactionId);

    boolean isAccountFrozen(Transaction transaction);

    boolean isTransactionTimeFraudulent(Account account);

    boolean isTransactionDailyAmountFraudulent(Account account);

    Money lastDayTransactions(Account account);

    Money allDailyMax(Account account);

    HashMap<LocalDate, Money> dailyTransactions(Account account);
}
