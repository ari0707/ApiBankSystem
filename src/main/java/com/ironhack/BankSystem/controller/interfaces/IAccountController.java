package com.ironhack.BankSystem.controller.interfaces;

import com.ironhack.BankSystem.dto.AccountDTO;
import com.ironhack.BankSystem.dto.AccountEditDTO;
import com.ironhack.BankSystem.model.*;
import org.springframework.security.core.Authentication;


import java.util.List;

public interface IAccountController {

    List<Account> getAccounts();

    Account getAccountById(long id);

    Money getAccountBalanceById(long id);


    List<CheckingAccount> getCheckingAccounts();


    List<StudentCheckingAccount> getStudentCheckingAccounts();


    List<SavingsAccount> getSavingsAccounts();


    List<CreditCardAccount> getCreditCards();

    void createCheckingAccount(AccountDTO checkingAccount);

    void createSavingsAccount(AccountDTO savingsAccount);

    void createCreditCard(AccountDTO creditCard);


    void editAccount(long id, AccountEditDTO accountEdit);
}
