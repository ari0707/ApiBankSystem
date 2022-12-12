package com.ironhack.BankSystem.service.impl;

import com.ironhack.BankSystem.dto.AccountDTO;
import com.ironhack.BankSystem.model.AccountHolder;
import com.ironhack.BankSystem.model.CreditCardAccount;
import com.ironhack.BankSystem.repository.ICreditCardAccountRepository;
import com.ironhack.BankSystem.service.interfaces.IAccountHolderService;
import com.ironhack.BankSystem.service.interfaces.ICreditCardAccountService;
import com.ironhack.BankSystem.service.interfaces.IInterestTransactionService;
import com.ironhack.BankSystem.service.interfaces.IPenaltyFeeTransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.ironhack.BankSystem.utils.DataTimeUtil.dateTimeNow;
import static com.ironhack.BankSystem.utils.MoneyUtil.compareMoney;
import static com.ironhack.BankSystem.utils.MoneyUtil.newMoney;

@Service
public class CreditCardAccountServiceImpl implements ICreditCardAccountService {

    @Autowired
    private ICreditCardAccountRepository creditCardRepository;

    @Autowired
    private IAccountHolderService accountHolderService;

    @Autowired
    private IInterestTransactionService interestTransactionService;

    @Autowired
    private IPenaltyFeeTransactionService penaltyFeeTransactionService;

    public List<CreditCardAccount> getAll() {
        return creditCardRepository.findAllJoined();
    }


    public void newAccount(AccountDTO creditCard) throws EntityNotFoundException, IllegalArgumentException {
        // Verifica la identidad de ambos propietarios de la cuenta.
        AccountHolder[] accountHolders = accountHolderService.findAccountHolders(creditCard);

        CreditCardAccount cc = new CreditCardAccount(newMoney(creditCard.getInitialBalance().toString(), creditCard.getCurrency().toUpperCase()), accountHolders[0], accountHolders[1]);
        cc.updateCurrencyValues(); // convierte los valores si la moneda del saldo principal es diferente.

        creditCardRepository.save(cc);
    }


    public void checkInterestRate(CreditCardAccount creditCard) {
        LocalDate lastInterestDate = creditCard.getLastInterestUpdate();

        if (lastInterestDate.plusMonths(1).isBefore(dateTimeNow().toLocalDate()))
            interestTransactionService.newTransaction(creditCard.getId());
    }


    public void checkCreditLimit(CreditCardAccount creditCard) {
        LocalDate lastPenaltyFee = creditCard.getLastPenaltyFeeCheck();

        if (compareMoney(creditCard.getBalance(), creditCard.getCreditLimit()) < 0) {

            if (lastPenaltyFee.plusMonths(1).isBefore(dateTimeNow().toLocalDate())) {
                penaltyFeeTransactionService.newTransaction(creditCard.getId());
            }
        } else if (creditCard.getLastPenaltyFeeCheck().isBefore(dateTimeNow().toLocalDate().minusMonths(1).minusDays(1))) {
            creditCard.setLastPenaltyFeeCheck(dateTimeNow().toLocalDate().minusMonths(1).minusDays(1));
            creditCardRepository.save(creditCard);
        }
    }

}
