package com.ironhack.BankSystem.service.impl;

import com.ironhack.BankSystem.dto.AccountEditDTO;
import com.ironhack.BankSystem.enums.AccountStatus;
import com.ironhack.BankSystem.model.*;
import com.ironhack.BankSystem.repository.IAccountRepository;
import com.ironhack.BankSystem.service.interfaces.*;
import com.ironhack.BankSystem.model.Money;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;

import static com.ironhack.BankSystem.utils.EnumsUtil.accountStatusFromString;
import static com.ironhack.BankSystem.utils.MoneyUtil.convertCurrency;
import static com.ironhack.BankSystem.utils.MoneyUtil.newMoney;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    @Lazy
    private ISavingsAccountService savingsAccountService;

    @Autowired
    @Lazy
    private ICreditCardAccountService creditCardService;

    @Autowired
    @Lazy
    private ICheckingAccountService checkingAccountService;



    public List<Account> getAll() {
        return accountRepository.findAllJoined();
    }

    public List<Account> getAllByUsername(String username) {
        return accountRepository.findAllByUsernameJoined(username);
    }

    public Account getById(Long id) {
        var account = accountRepository.findByIdJoined(id);
        if (account.isPresent()) {
            return account.get();
        }
        throw new EntityNotFoundException("Id not found.");
    }

    public void freezeAccount(long id) {
        var account = getById(id);

        if (account.getClass() == CheckingAccount.class) {
            ((CheckingAccount) account).setAccountStatus(AccountStatus.FROZEN);
            accountRepository.save(account);
        }
        if (account.getClass() == StudentCheckingAccount.class) {
            ((StudentCheckingAccount) account).setAccountStatus(AccountStatus.FROZEN);
            accountRepository.save(account);
        }
        if (account.getClass() == SavingsAccount.class) {
            ((SavingsAccount) account).setAccountStatus(AccountStatus.FROZEN);
            accountRepository.save(account);
        }
    }


    // Para guardar la cuenta
    public void save(Account account) {
        accountRepository.save(account);
    }

    //  Para editar la cuenta
    public void edit(long id, AccountEditDTO accountEdit) {
        Account account = getById(id);

        if (accountEdit.getPrimaryOwnerUsername() != null) {
            User primaryUser = userService.getByUsername(accountEdit.getPrimaryOwnerUsername());
            if (primaryUser.getClass() == AccountHolder.class) {
                account.setPrimaryOwner((AccountHolder) primaryUser);
            } else throw new IllegalArgumentException("Username for primary owner not found");
        }

        if (accountEdit.getSecondaryOwnerUsername() != null) {
            User secondaryUser = userService.getByUsername(accountEdit.getSecondaryOwnerUsername());
            if (secondaryUser.getClass() == AccountHolder.class) {
                account.setSecondaryOwner((AccountHolder) secondaryUser);
            } else throw new IllegalArgumentException("Username for secondary owner not found");
        }

        // (Student) Checking Accounts
        if (account.getClass() == CheckingAccount.class || account.getClass() == StudentCheckingAccount.class) {
            // Status
            if (accountEdit.getAccountStatus() != null)
                ((CheckingAccount) account).setAccountStatus(accountStatusFromString(accountEdit.getAccountStatus()));

            // Currency
            if (accountEdit.getCurrency() != null) {
                account.setBalance(convertCurrency(Currency.getInstance(accountEdit.getCurrency().toUpperCase()), account.getBalance()));
                account.setPenaltyFee(convertCurrency(Currency.getInstance(accountEdit.getCurrency().toUpperCase()), account.getPenaltyFee()));
                ((CheckingAccount) account).setMinimumBalance(convertCurrency(Currency.getInstance(accountEdit.getCurrency().toUpperCase()), ((CheckingAccount) account).getMinimumBalance()));
                ((CheckingAccount) account).setMonthlyMaintenanceFee(convertCurrency(Currency.getInstance(accountEdit.getCurrency().toUpperCase()), ((CheckingAccount) account).getMonthlyMaintenanceFee()));
            }

            // Minimum Balance
            if (accountEdit.getMinimumBalance() != null && account.getClass() != StudentCheckingAccount.class)
                ((CheckingAccount) account).setMinimumBalance(new Money(accountEdit.getMinimumBalance(), ((CheckingAccount) account).getMinimumBalance().getCurrency()));

            // Maintenance Fee
            if (accountEdit.getMonthlyMaintenanceFee() != null && account.getClass() != StudentCheckingAccount.class)
                ((CheckingAccount) account).setMonthlyMaintenanceFee(new Money(accountEdit.getMonthlyMaintenanceFee(), ((CheckingAccount) account).getMonthlyMaintenanceFee().getCurrency()));

            if (accountEdit.getLastMaintenanceFee() != null)
                ((CheckingAccount) account).setLastMaintenanceFee(accountEdit.getLastMaintenanceFee());


            //  Savings Accounts
        } else if (account.getClass() == SavingsAccount.class) {
            // Status
            if (accountEdit.getAccountStatus() != null)
                ((SavingsAccount) account).setAccountStatus(accountStatusFromString(accountEdit.getAccountStatus()));

            // Currency
            if (accountEdit.getCurrency() != null) {
                account.setBalance(convertCurrency(Currency.getInstance(accountEdit.getCurrency().toUpperCase()), account.getBalance()));
                account.setPenaltyFee(convertCurrency(Currency.getInstance(accountEdit.getCurrency().toUpperCase()), account.getPenaltyFee()));
                ((SavingsAccount) account).setMinimumBalance(convertCurrency(Currency.getInstance(accountEdit.getCurrency().toUpperCase()), ((SavingsAccount) account).getMinimumBalance()));
            }

            // Minimum Balance
            if (accountEdit.getMinimumBalance() != null) {
                Money minMinimumBalanceConverted = convertCurrency(((SavingsAccount) account).getMinimumBalance(), newMoney("100"));
                Money maxMinimumBalanceConverted = convertCurrency(((SavingsAccount) account).getMinimumBalance(), newMoney("1000"));
                if ((accountEdit.getMinimumBalance().compareTo(minMinimumBalanceConverted.getAmount()) >= 0) &&
                        (accountEdit.getMinimumBalance().compareTo(maxMinimumBalanceConverted.getAmount()) <= 0)) {
                    ((SavingsAccount) account).setMinimumBalance(new Money(accountEdit.getMinimumBalance(), ((SavingsAccount) account).getMinimumBalance().getCurrency()));
                } else
                    throw new IllegalArgumentException("Savings account minimum balance must be greater than " + minMinimumBalanceConverted + ".");
            }

            // Interest Rate
            if (accountEdit.getSavingsAccountInterestRate() != null)
                ((SavingsAccount) account).setInterestRate(accountEdit.getSavingsAccountInterestRate());

            if (accountEdit.getLastInterestUpdate() != null)
                ((SavingsAccount) account).setLastInterestUpdate(accountEdit.getLastInterestUpdate());


            // Credit Cards
        } else if (account.getClass() == CreditCardAccount.class) {
            // Currency
            if (accountEdit.getCurrency() != null) {
                account.setBalance(convertCurrency(Currency.getInstance(accountEdit.getCurrency().toUpperCase()), account.getBalance()));
                account.setPenaltyFee(convertCurrency(Currency.getInstance(accountEdit.getCurrency().toUpperCase()), account.getPenaltyFee()));
                ((CreditCardAccount) account).setCreditLimit(convertCurrency(Currency.getInstance(accountEdit.getCurrency().toUpperCase()), ((CreditCardAccount) account).getCreditLimit()));
            }

            // Credit Limit
            if (accountEdit.getMinimumBalance() != null) {
                Money maxCreditLimitConverted = convertCurrency(((CreditCardAccount) account).getCreditLimit(), newMoney("100000"));
                if (accountEdit.getCreditLimit().compareTo(maxCreditLimitConverted.getAmount()) <= 0) {
                    ((CreditCardAccount) account).setCreditLimit(new Money(accountEdit.getCreditLimit(), ((CreditCardAccount) account).getCreditLimit().getCurrency()));
                } else
                    throw new IllegalArgumentException("Credit limit must be lower than " + maxCreditLimitConverted + ".");
            }

            // Interest Rate
            if (accountEdit.getCreditCardInterestRate() != null)
                ((CreditCardAccount) account).setInterestRate(accountEdit.getCreditCardInterestRate());

            if (accountEdit.getLastInterestUpdate() != null)
                ((CreditCardAccount) account).setLastInterestUpdate(accountEdit.getLastInterestUpdate());
        }

        // Account Balance
        if (accountEdit.getAccountBalance() != null)
            account.setBalance(new Money(accountEdit.getAccountBalance(), account.getBalance().getCurrency()));

        // Penalty Fee
        if (accountEdit.getPenaltyFee() != null)
            account.setPenaltyFee(new Money(accountEdit.getPenaltyFee(), account.getPenaltyFee().getCurrency()));

        if (accountEdit.getLastPenaltyFee() != null) account.setLastPenaltyFeeCheck(accountEdit.getLastPenaltyFee());

        save(account);
    }


    // Update Balance
    public void updateBalance(Account account) {
        if (account.getClass() == CheckingAccount.class && ((CheckingAccount) account).getAccountStatus() == AccountStatus.ACTIVE) {
            checkingAccountService.checkMinimumBalance((CheckingAccount) account);
            checkingAccountService.checkMaintenanceFee((CheckingAccount) account);
        } else if (account.getClass() == SavingsAccount.class && ((SavingsAccount) account).getAccountStatus() == AccountStatus.ACTIVE) {
            savingsAccountService.checkMinimumBalance((SavingsAccount) account);
            savingsAccountService.checkInterestRate((SavingsAccount) account);
        } else if (account.getClass() == CreditCardAccount.class) {
            creditCardService.checkCreditLimit((CreditCardAccount) account);
            creditCardService.checkInterestRate((CreditCardAccount) account);
        }
    }


    // Has account
    public boolean hasAccount(Long id) {
        var account = accountRepository.findByIdJoined(id);
        return account.isPresent();
    }
}
