package com.ironhack.BankSystem.service.impl;

import com.ironhack.BankSystem.dto.UserDTO;
import com.ironhack.BankSystem.model.Admin;
import com.ironhack.BankSystem.model.Roles;
import com.ironhack.BankSystem.repository.IAdminRepository;
import com.ironhack.BankSystem.service.interfaces.IAdminService;
import com.ironhack.BankSystem.service.interfaces.IRolesService;
import com.ironhack.BankSystem.service.interfaces.IUserService;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private IAdminRepository adminRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRolesService roleService;

    public List<Admin> getAll() {

        return adminRepository.findAll();
    }


    public void newUser(UserDTO admin) throws EntityExistsException {
        // Check if username already exists
        if (userService.isUsernamePresent(admin.getUsername()))
            throw new EntityExistsException("Username already exists.");

        Admin a = new Admin(admin.getUsername(), admin.getPassword(), admin.getName());

        // Establece el rol de Admin
        Optional<Roles> userRole = roleService.getByName("ADMIN");
        if (userRole.isPresent()) {
            a.getRoles().add(userRole.get());
        } else {
            roleService.newRole("ADMIN");
            Optional<Roles> newUserRole = roleService.getByName("ADMIN");
            newUserRole.ifPresent(role -> a.getRoles().add(role));
        }
        adminRepository.save(a);
    }
}
