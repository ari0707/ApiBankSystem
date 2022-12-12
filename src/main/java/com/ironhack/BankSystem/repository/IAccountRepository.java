package com.ironhack.BankSystem.repository;

import com.ironhack.BankSystem.model.Account;
import com.ironhack.BankSystem.service.interfaces.IAccountBaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAccountRepository extends IAccountBaseRepository <Account>, JpaRepository<Account, Long> {

}
