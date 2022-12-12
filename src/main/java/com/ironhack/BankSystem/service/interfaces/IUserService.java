package com.ironhack.BankSystem.service.interfaces;

import com.ironhack.BankSystem.dto.UserEditDTO;
import com.ironhack.BankSystem.dto.UserEditPasswordDTO;
import com.ironhack.BankSystem.model.User;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface IUserService {

    List<User> getAll();

    User getById(Long id) throws EntityNotFoundException;

    User getByUsername(String username) throws EntityNotFoundException;


    void editPassword(String username, UserEditPasswordDTO userPassword) throws EntityNotFoundException;

    void edit(String username, UserEditDTO user) throws EntityNotFoundException;


    boolean isUsernamePresent(String username);

}
