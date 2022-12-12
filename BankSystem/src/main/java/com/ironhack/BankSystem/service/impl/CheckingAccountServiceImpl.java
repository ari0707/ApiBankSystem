package com.ironhack.BankSystem.service.impl;

import com.ironhack.BankSystem.dto.AccountDTO;
import com.ironhack.BankSystem.enums.AccountStatus;
import com.ironhack.BankSystem.model.AccountHolder;
import com.ironhack.BankSystem.model.CheckingAccount;
import com.ironhack.BankSystem.model.StudentCheckingAccount;
import com.ironhack.BankSystem.repository.ICheckingAccountRepository;
import com.ironhack.BankSystem.service.interfaces.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ironhack.BankSystem.utils.DataTimeUtil.dateTimeNow;
import static com.ironhack.BankSystem.utils.MoneyUtil.compareMoney;
import static com.ironhack.BankSystem.utils.MoneyUtil.newMoney;

@Service
public class CheckingAccountServiceImpl implements ICheckingAccountService {

    @Autowired
    private ICheckingAccountRepository checkingAccountRepository;

    @Autowired
    private IAccountHolderService accountHolderService;

    @Autowired
    private IStudentCheckingAccountService studentCheckingAccountService;

    @Autowired
    private IMaintenanceFeeTransactionService maintenanceFeeTransactionService;

    @Autowired
    private IPenaltyFeeTransactionService penaltyFeeTransactionService;


    public List<CheckingAccount> getAll() {
        ArrayList<CheckingAccount> checkingAccounts = new ArrayList<>(checkingAccountRepository.findAllJoined());
        ArrayList<StudentCheckingAccount> studentCheckingAccounts = new ArrayList<>(studentCheckingAccountService.getAll());
        checkingAccounts.removeAll(studentCheckingAccounts);
        return checkingAccounts;
    }

    // Para crear una nueva cuenta
    public void newAccount(AccountDTO checkingAccount) throws EntityNotFoundException, IllegalArgumentException, NoSuchAlgorithmException {
        // Verifica la identidad de ambos propietarios de la cuenta.
        AccountHolder[] accountHolders = accountHolderService.findAccountHolders(checkingAccount);

        if (accountHolders[0].getDateOfBirth().plusYears(25).isBefore(LocalDate.now())) {
            // Mayores de 24 años
            CheckingAccount ca = new CheckingAccount(newMoney(checkingAccount.getInitialBalance().toString(), checkingAccount.getCurrency().toUpperCase()), accountHolders[0], accountHolders[1]);
            ca.updateCurrencyValues();
            checkingAccountRepository.save(ca);
        } else {
            // Menores de 24 años
            StudentCheckingAccount sca = new StudentCheckingAccount(newMoney(checkingAccount.getInitialBalance().toString(), checkingAccount.getCurrency().toUpperCase()), accountHolders[0], accountHolders[1]);
            sca.updateCurrencyValues();
            studentCheckingAccountService.newAccount(sca);
        }
    }


    public void checkMaintenanceFee(CheckingAccount checkingAccount) {
        LocalDate lastMaintenanceDate = checkingAccount.getLastMaintenanceFee();

        if (checkingAccount.getAccountStatus() == AccountStatus.ACTIVE &&
                lastMaintenanceDate.plusMonths(1).isBefore(dateTimeNow().toLocalDate())) {
            maintenanceFeeTransactionService.newTransaction(checkingAccount.getId());
        }
    }


    public void checkMinimumBalance(CheckingAccount checkingAccount) {
        LocalDate lastPenaltyFee = checkingAccount.getLastPenaltyFeeCheck();

        if (checkingAccount.getAccountStatus() == AccountStatus.ACTIVE &&
                compareMoney(checkingAccount.getBalance(), checkingAccount.getMinimumBalance()) < 0) {

            if (lastPenaltyFee.plusMonths(1).isBefore(dateTimeNow().toLocalDate())) {
                penaltyFeeTransactionService.newTransaction(checkingAccount.getId());
            }
        } else if (checkingAccount.getLastPenaltyFeeCheck().isBefore(dateTimeNow().toLocalDate().minusMonths(1).minusDays(1))) {
            checkingAccount.setLastPenaltyFeeCheck(dateTimeNow().toLocalDate().minusMonths(1).minusDays(1));
            checkingAccountRepository.save(checkingAccount);
        }
    }
}
