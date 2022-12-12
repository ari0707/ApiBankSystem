package com.ironhack.BankSystem.service.impl;

import com.ironhack.BankSystem.model.AccountHolder;
import com.ironhack.BankSystem.model.Address;
import com.ironhack.BankSystem.model.CheckingAccount;
import com.ironhack.BankSystem.model.Transaction;
import com.ironhack.BankSystem.service.interfaces.IPenaltyFeeTransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import static com.ironhack.BankSystem.utils.MoneyUtil.newMoney;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PenaltyFeeTransactionServiceImplTest {
    @InjectMocks
    @Spy
    private IPenaltyFeeTransactionService penaltyFeeTransactionService = new PenaltyFeeTransactionServiceImpl();

    @Test
    void testIsTransactionAmountValid_validAmount_true() throws NoSuchAlgorithmException {
        var pa = new Address("Spain", "Madrid", "28014", "Goya");
        var ah = new AccountHolder("Manu", "12345", "Manuel", LocalDate.parse("1998-12-01"), pa);
        var ca = new CheckingAccount(newMoney("100"), ah);
        ca.setId(1L);
        var transaction = new Transaction(newMoney("70"), ca);

        assertTrue(penaltyFeeTransactionService.isTransactionAmountValid(transaction));
    }
}