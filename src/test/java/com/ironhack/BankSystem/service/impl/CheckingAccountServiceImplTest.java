package com.ironhack.BankSystem.service.impl;

import com.ironhack.BankSystem.model.AccountHolder;
import com.ironhack.BankSystem.model.Address;
import com.ironhack.BankSystem.model.CheckingAccount;
import com.ironhack.BankSystem.model.StudentCheckingAccount;
import com.ironhack.BankSystem.repository.ICheckingAccountRepository;
import com.ironhack.BankSystem.service.interfaces.ICheckingAccountService;
import com.ironhack.BankSystem.service.interfaces.IStudentCheckingAccountService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;

import static com.ironhack.BankSystem.utils.MoneyUtil.newMoney;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CheckingAccountServiceImplTest {

    @InjectMocks
    private ICheckingAccountService checkingAccountService = new CheckingAccountServiceImpl();

    @Mock
    private ICheckingAccountRepository checkingAccountRepository;

    @Mock
    private IStudentCheckingAccountService studentCheckingAccountService;

    @Test

    void testGetAll_getFromCheckingAccounts() throws NoSuchAlgorithmException {
        var pa = new Address("Spain", "Madrid", "28017", "Goya");
        var ca = new CheckingAccount(newMoney("1000"), new AccountHolder("Ari", "12345", "Ariana", LocalDate.parse("1992-12-20"), pa));
        ca.setId(1L);
        var sca = new StudentCheckingAccount(newMoney("200"), new AccountHolder("Sofi", "12345", "Sofia", LocalDate.parse("1982-07-10"), pa));
        sca.setId(2L);
        when(checkingAccountRepository.findAllJoined()).thenReturn(List.of(ca, sca));
        when(studentCheckingAccountService.getAll()).thenReturn(List.of(sca));

        var checkingAccounts = checkingAccountService.getAll();

        assertTrue(checkingAccounts.contains(ca));
        assertFalse(checkingAccounts.contains(sca));
    }

}