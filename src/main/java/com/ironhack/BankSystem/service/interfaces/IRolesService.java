package com.ironhack.BankSystem.service.interfaces;

import com.ironhack.BankSystem.model.Roles;

import java.util.Optional;

public interface IRolesService {

    Optional<Roles> getByName(String name);

    void newRole(String name);

}
