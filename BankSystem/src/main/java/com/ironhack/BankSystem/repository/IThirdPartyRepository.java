package com.ironhack.BankSystem.repository;

import com.ironhack.BankSystem.model.ThirdParty;
import com.ironhack.BankSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IThirdPartyRepository extends IUserBaseRepository <ThirdParty>, JpaRepository<ThirdParty, Long> {

    Optional<ThirdParty> findByHashedKey(String hashedKey);
}
