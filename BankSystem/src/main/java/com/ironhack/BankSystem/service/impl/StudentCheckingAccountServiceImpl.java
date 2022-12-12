package com.ironhack.BankSystem.service.impl;

import com.ironhack.BankSystem.dto.AccountDTO;
import com.ironhack.BankSystem.enums.AccountStatus;
import com.ironhack.BankSystem.model.AccountHolder;
import com.ironhack.BankSystem.model.CheckingAccount;
import com.ironhack.BankSystem.model.StudentCheckingAccount;
import com.ironhack.BankSystem.repository.ICheckingAccountRepository;
import com.ironhack.BankSystem.repository.IStudentCheckingAccountRepository;
import com.ironhack.BankSystem.service.interfaces.IAccountHolderService;
import com.ironhack.BankSystem.service.interfaces.IStudentCheckingAccountService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentCheckingAccountServiceImpl implements IStudentCheckingAccountService {

    @Autowired
    private IStudentCheckingAccountRepository studentCheckingAccountRepository;

    public List<StudentCheckingAccount> getAll() {
        return studentCheckingAccountRepository.findAllJoined();
    }

    public void newAccount(StudentCheckingAccount studentCheckingAccount) {
        studentCheckingAccountRepository.save(studentCheckingAccount);
    }
}
