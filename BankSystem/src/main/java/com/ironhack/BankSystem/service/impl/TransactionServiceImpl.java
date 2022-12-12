package com.ironhack.BankSystem.service.impl;

import com.ironhack.BankSystem.enums.AccountStatus;
import com.ironhack.BankSystem.model.*;
import com.ironhack.BankSystem.repository.ITransactionRepository;
import com.ironhack.BankSystem.service.interfaces.IAccountService;
import com.ironhack.BankSystem.service.interfaces.ITransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.ironhack.BankSystem.utils.MoneyUtil.*;

@Service
public class TransactionServiceImpl implements ITransactionService {

    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private IAccountService accountService;

    public List<Transaction> getAllByAccountId(long accountId) {
        if (accountService.hasAccount(accountId))
            return transactionRepository.findAllByAccountIdJoined(accountId);

        throw new EntityNotFoundException("Account not found.");
    }


    public Transaction getById(long accountId, long transactionId) {
        if (!accountService.hasAccount(accountId)) throw new EntityNotFoundException("Account not found.");

        var transaction = transactionRepository.findByIdJoined(transactionId);
        if (transaction.isPresent()) {
            if ((transaction.get().getBaseAccount() == null || transaction.get().getBaseAccount().getId() != accountId) &&
                    transaction.get().getTargetAccount().getId() != accountId)
                throw new IllegalArgumentException("Transaction does not exist in defined account.");

            return transaction.get();
        }
        throw new EntityNotFoundException("Transaction not found.");
    }


    // Comprueba el estado de la cuenta
    public boolean isAccountFrozen(Transaction transaction) {
        Account targetAccount = transaction.getTargetAccount();
        Account baseAccount = transaction.getBaseAccount();

        if (targetAccount.getClass() == CheckingAccount.class) {
            if (((CheckingAccount) targetAccount).getAccountStatus() == AccountStatus.FROZEN) return true;
        } else if (targetAccount.getClass() == StudentCheckingAccount.class) {
            if (((StudentCheckingAccount) targetAccount).getAccountStatus() == AccountStatus.FROZEN) return true;
        } else if (targetAccount.getClass() == SavingsAccount.class) {
            if (((SavingsAccount) targetAccount).getAccountStatus() == AccountStatus.FROZEN) return true;
        }

        if (baseAccount != null) {
            if (baseAccount.getClass() == CheckingAccount.class) {
                return ((CheckingAccount) baseAccount).getAccountStatus() == AccountStatus.FROZEN;
            } else if (baseAccount.getClass() == StudentCheckingAccount.class) {
                return ((StudentCheckingAccount) baseAccount).getAccountStatus() == AccountStatus.FROZEN;
            } else if (baseAccount.getClass() == SavingsAccount.class) {
                return ((SavingsAccount) baseAccount).getAccountStatus() == AccountStatus.FROZEN;
            }
        }
        return false;
    }


    // Comprobar si el tiempo de la transacción es fraudulento
    public boolean isTransactionTimeFraudulent(Account account) {
        List<Transaction> transactionList = account.getAllTransactionsOrdered();
        if (transactionList.size() <= 1) return false;
        // Es verdadero si la última transacción realizada fue hace menos de un segundo.
        return transactionList.get(1).getOperationDate().plusSeconds(1).isAfter(transactionList.get(0).getOperationDate());
    }


    // Comprueba si el importe diario de la transacción es fraudulento
    public boolean isTransactionDailyAmountFraudulent(Account account) {
        Money totalDayTransaction = lastDayTransactions(account);
        Money dailyMax = allDailyMax(account);
        Money baseMaxTransaction = convertCurrency(account.getBalance(), newMoney("1000"));
        Money dailyMaxTransaction = new Money(dailyMax.getAmount().multiply(new BigDecimal("1.5")), account.getBalance().getCurrency());

        return compareMoney(totalDayTransaction, dailyMaxTransaction) > 0 && // más de 1.5 transacción diaria máxima
                compareMoney(totalDayTransaction, baseMaxTransaction) > 0;  // mayor a 1000€
    }

    //importe de la transacción del último día
    public Money lastDayTransactions(Account account) {
        HashMap<LocalDate, Money> dailyTransactions = dailyTransactions(account);
        Optional<Map.Entry<LocalDate, Money>> lastDailyMoney = dailyTransactions.entrySet().stream().max(Map.Entry.comparingByKey());
        return lastDailyMoney.map(Map.Entry::getValue).orElse(null);
    }

    // monto máximo de transacción en un día
    public Money allDailyMax(Account account) {
        HashMap<LocalDate, Money> dailyTransactions = dailyTransactions(account);
        if (dailyTransactions.size() > 1) dailyTransactions.put(LocalDate.now(), newMoney("0"));
        Optional<Map.Entry<LocalDate, Money>> maxDailyMoney = dailyTransactions.entrySet().stream().max(
                Comparator.comparing((Map.Entry<LocalDate, Money> e) -> e.getValue().getAmount())
        );
        return maxDailyMoney.map(Map.Entry::getValue).orElse(null);
    }

    // monto de la transacción por día
    public HashMap<LocalDate, Money> dailyTransactions(Account account) {
        HashMap<LocalDate, Money> dailyTransactions = new HashMap<>();
        for (Transaction transaction : account.getAllTransactionsOrdered()) {
            if (!dailyTransactions.containsKey(transaction.getOperationDate().toLocalDate())) {
                dailyTransactions.put(
                        transaction.getOperationDate().toLocalDate(),
                        convertCurrency(account.getBalance(), transaction.getBaseAmount())
                );
            } else {
                dailyTransactions.put(
                        transaction.getOperationDate().toLocalDate(),
                        addMoney(dailyTransactions.get(transaction.getOperationDate().toLocalDate()), convertCurrency(account.getBalance(), transaction.getBaseAmount()))
                );
            }
        }
        return dailyTransactions;
    }

}
