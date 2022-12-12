package com.ironhack.BankSystem.service.impl;

import com.ironhack.BankSystem.dto.UserDTO;
import com.ironhack.BankSystem.model.Roles;
import com.ironhack.BankSystem.model.ThirdParty;
import com.ironhack.BankSystem.repository.IThirdPartyRepository;
import com.ironhack.BankSystem.service.interfaces.IRolesService;
import com.ironhack.BankSystem.service.interfaces.IThirdPartyService;
import com.ironhack.BankSystem.service.interfaces.IUserService;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThirdPartyServiceImpl implements IThirdPartyService {

    @Autowired
    private IThirdPartyRepository thirdPartyRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRolesService roleService;

    public List<ThirdParty> getAll() {
        return thirdPartyRepository.findAll();
    }


    public void newUser(UserDTO thirdParty) throws EntityExistsException {
        // Check if username already exists
        if (userService.isUsernamePresent(thirdParty.getUsername()))
            throw new EntityExistsException("Username already exists.");

        ThirdParty tp = new ThirdParty(thirdParty.getUsername(), thirdParty.getPassword(), thirdParty.getName());

        // Establece el rol de "THIRD_PARTY"
        Optional<Roles> userRole = roleService.getByName("THIRD_PARTY");
        if (userRole.isPresent()) {
            tp.getRoles().add(userRole.get());
        } else {
            roleService.newRole("THIRD_PARTY");
            Optional<Roles> newUserRole = roleService.getByName("THIRD_PARTY");
            newUserRole.ifPresent(role -> tp.getRoles().add(role));
        }
        thirdPartyRepository.save(tp);
    }


    // Método para saber si existe un thirdParty con esta hashedKey

    public boolean hasHashedKey(String hashedKey) {
        Optional<ThirdParty> thirdParty = thirdPartyRepository.findByHashedKey(hashedKey);
        return thirdParty.isPresent();
    }
}
