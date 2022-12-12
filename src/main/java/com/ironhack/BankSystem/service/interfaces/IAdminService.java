package com.ironhack.BankSystem.service.interfaces;

import com.ironhack.BankSystem.dto.UserDTO;
import com.ironhack.BankSystem.model.Admin;
import jakarta.persistence.EntityExistsException;

import java.util.List;

public interface IAdminService {

    List<Admin> getAll();

    void newUser(UserDTO admin) throws EntityExistsException;
}
