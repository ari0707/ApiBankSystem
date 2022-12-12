package com.ironhack.BankSystem.controller.interfaces;

import com.ironhack.BankSystem.dto.AccountDTO;
import com.ironhack.BankSystem.dto.AccountEditDTO;
import com.ironhack.BankSystem.model.*;
import org.springframework.security.core.Authentication;


import java.util.List;

public interface IAccountController {

    // ======================================== GET ACCOUNT Methods ========================================
    // -------------------- All Accounts [ADMIN] / User Specific Accounts [Specific USER] --------------------
    List<Account> getAccounts();

    // -------------------- Account by Id [ADMIN] / User Specific Account by Id [Specific USER] --------------------
    Account getAccountById(long id);

    // -------------------- Account Balance by Id [ADMIN] / User Specific Account Balance by Id [Specific USER] --------------------
    Money getAccountBalanceById(long id);

    // -------------------- All Checking Accounts [ADMIN] --------------------
    List<CheckingAccount> getCheckingAccounts();

    // -------------------- All Student Checking Accounts [ADMIN] --------------------
    List<StudentCheckingAccount> getStudentCheckingAccounts();

    // -------------------- All Savings Accounts [ADMIN] --------------------
    List<SavingsAccount> getSavingsAccounts();

    // -------------------- All Credit Cards [ADMIN] --------------------
    List<CreditCardAccount> getCreditCards();


    // ======================================== POST Methods ========================================
    // -------------------- New Checking Account [ADMIN] --------------------
    void createCheckingAccount(AccountDTO checkingAccount);

    // -------------------- New Savings Account [ADMIN] --------------------
    void createSavingsAccount(AccountDTO savingsAccount);

    // -------------------- New Credit Card [ADMIN] --------------------
    void createCreditCard(AccountDTO creditCard);


    // ======================================== PATCH Methods ========================================
    void editAccount(long id, AccountEditDTO accountEdit);
}
