package com.ironhack.BankSystem.service.interfaces;

import com.ironhack.BankSystem.dto.AccountDTO;
import com.ironhack.BankSystem.dto.UserAccountHolderDTO;
import com.ironhack.BankSystem.model.AccountHolder;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface IAccountHolderService {

    List<AccountHolder> getAll();

    void newUser(UserAccountHolderDTO accountHolder) throws EntityExistsException;

    AccountHolder[] findAccountHolders(AccountDTO account) throws EntityNotFoundException, IllegalArgumentException;
}
