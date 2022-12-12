package com.ironhack.BankSystem.service.interfaces;

import com.ironhack.BankSystem.model.StudentCheckingAccount;

import java.util.List;

public interface IStudentCheckingAccountService {

    List<StudentCheckingAccount> getAll();

    void newAccount(StudentCheckingAccount studentCheckingAccount);
}
