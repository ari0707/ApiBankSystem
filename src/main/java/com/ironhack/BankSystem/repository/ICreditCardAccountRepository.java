package com.ironhack.BankSystem.repository;

import com.ironhack.BankSystem.model.CreditCardAccount;
import com.ironhack.BankSystem.service.interfaces.IAccountBaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICreditCardAccountRepository extends IAccountBaseRepository<CreditCardAccount>, JpaRepository<CreditCardAccount, Long> {

}
