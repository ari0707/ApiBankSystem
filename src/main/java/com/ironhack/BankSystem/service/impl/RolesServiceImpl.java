package com.ironhack.BankSystem.service.impl;

import com.ironhack.BankSystem.model.Roles;
import com.ironhack.BankSystem.repository.IRolesRepository;
import com.ironhack.BankSystem.service.interfaces.IRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.Optional;

@Service
public class RolesServiceImpl implements IRolesService {

    @Autowired
    private IRolesRepository roleRepository;

    public Optional<Roles> getByName(String name) {
        return roleRepository.findByName(name);
    }

    public void newRole(String name) {
        roleRepository.save(new Roles(name));
    }
}
