package com.ironhack.BankSystem.repository;

import com.ironhack.BankSystem.model.Account;
import com.ironhack.BankSystem.model.SavingsAccount;
import com.ironhack.BankSystem.service.interfaces.IAccountBaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISavingsAccountRepository extends IAccountBaseRepository <SavingsAccount>, JpaRepository<SavingsAccount, Long> {
}
