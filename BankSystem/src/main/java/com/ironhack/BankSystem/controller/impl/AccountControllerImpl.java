package com.ironhack.BankSystem.controller.impl;

import com.ironhack.BankSystem.controller.interfaces.IAccountController;
import com.ironhack.BankSystem.dto.AccountDTO;
import com.ironhack.BankSystem.dto.AccountEditDTO;
import com.ironhack.BankSystem.model.*;
import com.ironhack.BankSystem.service.interfaces.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;

import javax.security.auth.login.LoginException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountControllerImpl implements IAccountController  {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ICheckingAccountService checkingAccountService;

    @Autowired
    private IStudentCheckingAccountService studentCheckingAccountService;

    @Autowired
    private ISavingsAccountService savingsAccountService;

    @Autowired
    private ICreditCardAccountService creditCardService;



    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAccounts() {
        try {
            return accountService.getAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping("/{account_id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccountById(@PathVariable("account_id") long id) {
        try {
            var account = accountService.getById(id);
            accountService.updateBalance(account);
            return account;
        } catch (EntityNotFoundException e2) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e2.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping("/{account_id}/balance")
    @ResponseStatus(HttpStatus.OK)
    public Money getAccountBalanceById(@PathVariable("account_id") long id) {
        try {
            var account = accountService.getById(id);
            accountService.updateBalance(account);
            return account.getBalance();
        } catch (EntityNotFoundException e2) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e2.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping("/checking_accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<CheckingAccount> getCheckingAccounts() {
        try {
            return checkingAccountService.getAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/student_checking_accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentCheckingAccount> getStudentCheckingAccounts() {
        try {
            return studentCheckingAccountService.getAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping("/savings_accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<SavingsAccount> getSavingsAccounts() {
        try {
            return savingsAccountService.getAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @GetMapping("/credit_cards")
    @ResponseStatus(HttpStatus.OK)
    public List<CreditCardAccount> getCreditCards() {
        try {
            return creditCardService.getAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PostMapping("/new_checking_account")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCheckingAccount(@RequestBody @Valid AccountDTO checkingAccount) {
        try {
            checkingAccountService.newAccount(checkingAccount);
        } catch (EntityNotFoundException | IllegalArgumentException e1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e1.getMessage());
        } catch (NoSuchAlgorithmException e3) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e3.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/new_savings_account")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSavingsAccount(@RequestBody @Valid AccountDTO savingsAccount) {
        try {
            savingsAccountService.newAccount(savingsAccount);
        } catch (EntityNotFoundException | IllegalArgumentException e1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e1.getMessage());
        } catch (NoSuchAlgorithmException e3) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e3.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/new_credit_card")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCreditCard(@RequestBody @Valid AccountDTO creditCard) {
        try {
            creditCardService.newAccount(creditCard);
        } catch (EntityNotFoundException | IllegalArgumentException e1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e1.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PatchMapping("/edit/account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void editAccount(@PathVariable("id") long id, @RequestBody @Valid AccountEditDTO accountEdit) {
        try {
            accountService.edit(id, accountEdit);
        } catch (IllegalArgumentException e1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e1.getMessage());
        } catch (EntityNotFoundException e2) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e2.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }



}

