package com.ironhack.BankSystem.service.impl;

import com.ironhack.BankSystem.dto.TransactionThirdPartyDTO;
import com.ironhack.BankSystem.enums.TransactionPurpose;
import com.ironhack.BankSystem.model.*;
import com.ironhack.BankSystem.repository.ITransactionRepository;
import com.ironhack.BankSystem.service.interfaces.IAccountService;
import com.ironhack.BankSystem.service.interfaces.IThirdPartyService;
import com.ironhack.BankSystem.service.interfaces.IThirdPartyTransactionService;
import com.ironhack.BankSystem.service.interfaces.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Currency;

import static com.ironhack.BankSystem.utils.EnumsUtil.transactionPurposeFromString;
import static com.ironhack.BankSystem.utils.MoneyUtil.*;

@Service
public class ThirdPartyTransactionServiceImpl implements IThirdPartyTransactionService {

    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private IThirdPartyService thirdPartyService;

    public void newTransaction(String hashedKey, TransactionThirdPartyDTO thirdPartyTransaction) {
        if (!thirdPartyService.hasHashedKey(hashedKey)) throw new IllegalArgumentException("Invalid hashed key.");

        Account targetAccount = accountService.getById(thirdPartyTransaction.getTargetAccountId());
        boolean isValidKey;
        if (targetAccount.getClass() == CheckingAccount.class) {
            isValidKey = (((CheckingAccount) targetAccount).getSecretKey().equals(thirdPartyTransaction.getSecretKey()));
        } else if (targetAccount.getClass() == StudentCheckingAccount.class) {
            isValidKey = (((StudentCheckingAccount) targetAccount).getSecretKey().equals(thirdPartyTransaction.getSecretKey()));
        } else if (targetAccount.getClass() == SavingsAccount.class) {
            isValidKey = (((SavingsAccount) targetAccount).getSecretKey().equals(thirdPartyTransaction.getSecretKey()));
        } else {
            throw new IllegalArgumentException("Account not valid for third party transactions.");
        }
        if (!isValidKey) throw new IllegalArgumentException("Account key is not valid for the targeted account.");

        Transaction transaction = transactionRepository.save(
                new Transaction(
                        new Money(thirdPartyTransaction.getTransferValue(), Currency.getInstance(thirdPartyTransaction.getCurrency())),
                        targetAccount,
                        transactionPurposeFromString(thirdPartyTransaction.getTransactionPurpose())
                )
        );
        validateTransaction(transaction);
        accountService.updateBalance(targetAccount);
    }

    public void validateTransaction(Transaction transaction) {
        // Check if frozen.
        if (!transactionService.isAccountFrozen(transaction)) {


            // Comprueba si es fraudulenta
        } if (transaction.getTransactionPurpose() == TransactionPurpose.REQUEST &&
                (transactionService.isTransactionTimeFraudulent(transaction.getTargetAccount()) ||
                        transactionService.isTransactionDailyAmountFraudulent(transaction.getTargetAccount()))) {
            accountService.freezeAccount(transaction.getTargetAccount().getId());


        //  Comprueba si el monto de la transacción no es válido
        } else if (isTransactionAmountValid(transaction)) {
            processTransaction(transaction);

        }
        accountService.save(transaction.getTargetAccount());
    }

    public void processTransaction(Transaction transaction) {
        Account targetAccount = accountService.getById(transaction.getTargetAccount().getId());

        if (transaction.getTransactionPurpose() == TransactionPurpose.REQUEST) {
            targetAccount.setBalance(subtractMoney(targetAccount.getBalance(), transaction.getConvertedAmount()));
        } else {
            targetAccount.setBalance(addMoney(targetAccount.getBalance(), transaction.getConvertedAmount()));
        }
        accountService.save(targetAccount);
    }

    public boolean isTransactionAmountValid(Transaction transaction) {
        if (transaction.getTransactionPurpose() != null && transaction.getTransactionPurpose() == TransactionPurpose.REQUEST) {
            return compareMoney(transaction.getTargetAccount().getBalance(), transaction.getBaseAmount()) >= 0;
        } else return transaction.getTransactionPurpose() != null && transaction.getTransactionPurpose() == TransactionPurpose.SEND;
    }

}
