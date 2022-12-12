package com.ironhack.BankSystem.controller.impl;

import com.ironhack.BankSystem.controller.interfaces.ITransactionController;
import com.ironhack.BankSystem.dto.TransactionLocalDTO;
import com.ironhack.BankSystem.dto.TransactionThirdPartyDTO;
import com.ironhack.BankSystem.model.Transaction;
import com.ironhack.BankSystem.service.interfaces.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class TransactionControllerImpl implements ITransactionController {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private ILocalTransactionService localTransactionService;

    @Autowired
    private IThirdPartyTransactionService thirdPartyTransactionService;

    @Autowired
    private IInterestTransactionService interestTransactionService;

    @Autowired
    private IMaintenanceFeeTransactionService maintenanceFeeTransactionService;

    @Autowired
    private IPenaltyFeeTransactionService penaltyFeeTransactionService;

    @Autowired
    private IThirdPartyService thirdPartyService;

    @GetMapping("/{account_id}/transactions")
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> getTransactions(@PathVariable("account_id") long id) {
        try {
            accountService.updateBalance(accountService.getById(id));
            return transactionService.getAllByAccountId(id);
        } catch (EntityNotFoundException e2) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e2.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/{account_id}/transactions/{transaction_id}")
    @ResponseStatus(HttpStatus.OK)
    public Transaction getTransactionsById(@PathVariable("account_id") long accountId, @PathVariable("transaction_id") long transactionId) {
        try {
            accountService.updateBalance(accountService.getById(accountId));
            return transactionService.getById(accountId, transactionId);
        } catch (EntityNotFoundException e1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e1.getMessage());
        } catch (IllegalArgumentException e2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e2.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


//    //  POST ACCOUNT Methods
//    //  Add Account Specific Local Transaction [ADMIN / Specific USER]
//    @PostMapping("/{account_id}/transactions/new_local_transaction")
//    @ResponseStatus(HttpStatus.CREATED)
//    public void createLocalTransaction(@PathVariable("account_id") long id, @RequestBody @Valid TransactionLocalDTO localTransaction) {
//        try {
//            localTransactionService.newTransaction(id, localTransaction);
//        } catch (EntityNotFoundException e1) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e1.getMessage());
//        } catch (IllegalArgumentException e2) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e2.getMessage());
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }


    @PostMapping("/transactions/new_third_party_transaction")
    @ResponseStatus(HttpStatus.CREATED)
    public void createThirdPartyTransaction(@RequestHeader(value = "hashedKey") String hashedKey, @RequestBody @Valid TransactionThirdPartyDTO thirdPartyTransaction) {
        try {
            thirdPartyTransactionService.newTransaction(hashedKey, thirdPartyTransaction);
        } catch (EntityNotFoundException | IllegalArgumentException e1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e1.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
