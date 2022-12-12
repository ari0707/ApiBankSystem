package com.ironhack.BankSystem.service.impl;

import com.ironhack.BankSystem.dto.AccountDTO;
import com.ironhack.BankSystem.dto.UserAccountHolderDTO;
import com.ironhack.BankSystem.model.AccountHolder;
import com.ironhack.BankSystem.model.Address;
import com.ironhack.BankSystem.model.Roles;
import com.ironhack.BankSystem.repository.IAccountHolderRepository;
import com.ironhack.BankSystem.service.interfaces.IAccountHolderService;
import com.ironhack.BankSystem.service.interfaces.IRolesService;
import com.ironhack.BankSystem.service.interfaces.IUserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;

@Service
public class AccountHolderServiceImpl implements IAccountHolderService {

    @Autowired
    private IAccountHolderRepository accountHolderRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRolesService roleService;


    public List<AccountHolder> getAll() {
        return accountHolderRepository.findAll();
    }


    // Agregar usuarios
    public void newUser(UserAccountHolderDTO accountHolder) throws EntityExistsException {
        if (userService.isUsernamePresent(accountHolder.getUsername()))
            throw new EntityExistsException("Username already exists.");

        Address pa = new Address(
                accountHolder.getPaStreetAddress().trim(), accountHolder.getPaPostalCode().trim(), accountHolder.getPaCity().trim(), accountHolder.getPaCountry().trim());
        Address ma = null;
        if (accountHolder.getMaStreetAddress() != null || accountHolder.getMaPostalCode() != null || accountHolder.getMaCity() != null || accountHolder.getMaCountry() != null)
            ma = new Address(accountHolder.getMaStreetAddress().trim(), accountHolder.getMaPostalCode().trim(), accountHolder.getMaCity().trim(), accountHolder.getMaCountry().trim());

        AccountHolder ah = new AccountHolder(
                accountHolder.getUsername().trim(), accountHolder.getPassword().trim(), accountHolder.getName().trim(), accountHolder.getDateOfBirth(), pa, ma);

        // Establecer el rol del usuario
        Optional<Roles> userRole = roleService.getByName("USER");
        if (userRole.isPresent()) {
            ah.getRoles().add(userRole.get());
        } else {
            roleService.newRole("USER");
            Optional<Roles> newUserRole = roleService.getByName("USER");
            newUserRole.ifPresent(role -> ah.getRoles().add(role));
        }
        accountHolderRepository.save(ah);
    }


    public AccountHolder[] findAccountHolders(AccountDTO account) throws EntityNotFoundException, IllegalArgumentException {
        // Comprueba si existe el propietario principal y la identificación y el nombre de usuario coinciden.
        Optional<AccountHolder> primaryOwner = accountHolderRepository.findByUsername(account.getPrimaryOwnerUsername());
        if (primaryOwner.isEmpty()) throw new EntityNotFoundException("Primary owner user not found by username.");


        // Comprueba si existe un propietario secundario y la identificación y el nombre de usuario coinciden.
        Optional<AccountHolder> secondaryOwner = Optional.empty();
        if (account.getSecondaryOwnerId() != null && account.getSecondaryOwnerUsername() != null) {
            secondaryOwner = accountHolderRepository.findByUsername(account.getSecondaryOwnerUsername());
            if (secondaryOwner.isEmpty()) throw new EntityNotFoundException("Secondary owner user not found by username.");

        }


        AccountHolder[] accountHolders = new AccountHolder[2];
        accountHolders[0] = primaryOwner.get();
        accountHolders[1] = secondaryOwner.orElse(null);
        return accountHolders;
    }


}
