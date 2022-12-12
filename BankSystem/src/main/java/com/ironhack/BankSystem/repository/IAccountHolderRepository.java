package com.ironhack.BankSystem.repository;

import com.ironhack.BankSystem.model.AccountHolder;
import com.ironhack.BankSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccountHolderRepository extends IUserBaseRepository <AccountHolder>, JpaRepository <AccountHolder, Long> {

}
