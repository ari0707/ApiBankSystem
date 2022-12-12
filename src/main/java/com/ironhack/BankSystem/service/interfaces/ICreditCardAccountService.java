package com.ironhack.BankSystem.service.interfaces;

import com.ironhack.BankSystem.dto.AccountDTO;
import com.ironhack.BankSystem.model.CreditCardAccount;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface ICreditCardAccountService {

    List<CreditCardAccount> getAll();

    void newAccount(AccountDTO creditCard) throws EntityNotFoundException, IllegalArgumentException;

    void checkInterestRate(CreditCardAccount savingsAccount);

    void checkCreditLimit(CreditCardAccount savingsAccount);
}
