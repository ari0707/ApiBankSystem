package com.ironhack.BankSystem.repository;

import com.ironhack.BankSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface IUserBaseRepository <T extends User> extends JpaRepository<T, Long> {

    Optional<T> findByUsername(String username);
}
