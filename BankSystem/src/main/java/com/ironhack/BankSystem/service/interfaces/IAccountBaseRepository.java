package com.ironhack.BankSystem.service.interfaces;

import com.ironhack.BankSystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface IAccountBaseRepository <T extends Account> extends JpaRepository<T, Long> {

    @Query("SELECT e FROM #{#entityName} e " +
            "LEFT JOIN FETCH e.primaryOwner p " +
            "LEFT JOIN FETCH e.secondaryOwner s")
    List <T> findAllJoined();

    @Query("SELECT e FROM #{#entityName} e " +
            "LEFT JOIN FETCH e.primaryOwner p " +
            "LEFT JOIN FETCH e.secondaryOwner s " +
            "WHERE e.id = :id")
    Optional<T> findByIdJoined(long id);

    @Query("SELECT e FROM #{#entityName} e " +
            "LEFT JOIN FETCH e.primaryOwner p " +
            "LEFT JOIN FETCH e.secondaryOwner s " +
            "WHERE p.username = :username OR " +
            "s.username = :username")
    List <T> findAllByUsernameJoined(String username);
}
