package com.ironhack.BankSystem.service.interfaces;

import com.ironhack.BankSystem.dto.AccountDTO;
import com.ironhack.BankSystem.model.SavingsAccount;
import jakarta.persistence.EntityNotFoundException;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ISavingsAccountService {

    List<SavingsAccount> getAll();

    void newAccount(AccountDTO savingsAccount) throws EntityNotFoundException, IllegalArgumentException, NoSuchAlgorithmException;

    void checkInterestRate(SavingsAccount savingsAccount);

    void checkMinimumBalance(SavingsAccount savingsAccount);
}
