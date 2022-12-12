package com.ironhack.BankSystem.service.interfaces;

import com.ironhack.BankSystem.dto.AccountEditDTO;
import com.ironhack.BankSystem.model.Account;

import java.util.List;

public interface IAccountService {

    List<Account> getAll();

    List<Account> getAllByUsername(String username);

    Account getById(Long id);

    void edit(long id, AccountEditDTO accountEdit);

    void save(Account account);

    void freezeAccount(long id);

    void updateBalance(Account account);

    boolean hasAccount(Long id);
}
