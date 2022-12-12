package com.ironhack.BankSystem.service.interfaces;

import com.ironhack.BankSystem.dto.AccountDTO;
import com.ironhack.BankSystem.model.CheckingAccount;
import jakarta.persistence.EntityNotFoundException;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ICheckingAccountService {

    List<CheckingAccount> getAll();

    void newAccount(AccountDTO checkingAccount) throws EntityNotFoundException, IllegalArgumentException, NoSuchAlgorithmException;

    void checkMaintenanceFee(CheckingAccount checkingAccount);

    void checkMinimumBalance(CheckingAccount checkingAccount);
}
