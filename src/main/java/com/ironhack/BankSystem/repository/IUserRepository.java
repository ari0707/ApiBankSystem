package com.ironhack.BankSystem.repository;

import com.ironhack.BankSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends IUserBaseRepository <User>, JpaRepository <User, Long> {


}
