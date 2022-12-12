package com.ironhack.BankSystem.repository;

import com.ironhack.BankSystem.model.Admin;
import com.ironhack.BankSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAdminRepository extends IUserBaseRepository <Admin>, JpaRepository <Admin, Long> {
}
