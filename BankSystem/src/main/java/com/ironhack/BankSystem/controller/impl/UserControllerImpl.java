package com.ironhack.BankSystem.controller.impl;

import com.ironhack.BankSystem.controller.interfaces.IUserController;
import com.ironhack.BankSystem.dto.UserAccountHolderDTO;
import com.ironhack.BankSystem.dto.UserDTO;
import com.ironhack.BankSystem.dto.UserEditDTO;
import com.ironhack.BankSystem.dto.UserEditPasswordDTO;
import com.ironhack.BankSystem.model.AccountHolder;
import com.ironhack.BankSystem.model.Admin;
import com.ironhack.BankSystem.model.ThirdParty;
import com.ironhack.BankSystem.model.User;
import com.ironhack.BankSystem.service.interfaces.IAccountHolderService;
import com.ironhack.BankSystem.service.interfaces.IAdminService;
import com.ironhack.BankSystem.service.interfaces.IThirdPartyService;
import com.ironhack.BankSystem.service.interfaces.IUserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserControllerImpl implements IUserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAdminService adminService;

    @Autowired
    private IAccountHolderService accountHolderService;

    @Autowired
    private IThirdPartyService thirdPartyService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        try {
            return userService.getAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable("id") long id) {
        try {
            return userService.getById(id);
        } catch (EntityNotFoundException e1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e1.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserByUsername(@PathVariable("username") String username) {
        try {
            return userService.getByUsername(username);
        } catch (EntityNotFoundException e2) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e2.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/admins")
    @ResponseStatus(HttpStatus.OK)
    public List<Admin> getAdmins() {
        try {
            return adminService.getAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/account_holders")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountHolder> getAccountHolders() {
        try {
            return accountHolderService.getAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/third_parties")
    @ResponseStatus(HttpStatus.OK)
    public List<ThirdParty> getAThirdParties() {
        try {
            return thirdPartyService.getAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PostMapping("/new_admin")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewAdmin(@RequestBody @Valid UserDTO admin) {
        try {
            adminService.newUser(admin);
        } catch (EntityExistsException e1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e1.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewAccountHolder(@RequestBody @Valid UserAccountHolderDTO accountHolder) {
        try {
            accountHolderService.newUser(accountHolder);
        } catch (EntityExistsException e1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e1.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/new_third_party")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewThirdParty(@RequestBody @Valid UserDTO thirdParty) {
        try {
            thirdPartyService.newUser(thirdParty);
        } catch (EntityExistsException e1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e1.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @PatchMapping("/{username}/change_password")
    @ResponseStatus(HttpStatus.OK)
    public void editPassword(@PathVariable("username") String username, @RequestBody @Valid UserEditPasswordDTO userPassword) {
        try {
            userService.editPassword(username, userPassword);
        } catch (IllegalArgumentException e1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e1.getMessage());
        } catch (EntityNotFoundException e2) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e2.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/edit/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void editUser(@PathVariable("username") String username, @RequestBody @Valid UserEditDTO user) {
        try {
            userService.edit(username, user);
        } catch (IllegalArgumentException e1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e1.getMessage());
        } catch (EntityNotFoundException e2) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e2.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
