package com.ironhack.BankSystem.repository;

import com.ironhack.BankSystem.model.Account;
import com.ironhack.BankSystem.model.CheckingAccount;
import com.ironhack.BankSystem.service.interfaces.IAccountBaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICheckingAccountRepository extends IAccountBaseRepository<CheckingAccount>, JpaRepository<CheckingAccount, Long> {
}
