package com.ironhack.BankSystem.controller.interfaces;

import com.ironhack.BankSystem.dto.UserAccountHolderDTO;
import com.ironhack.BankSystem.dto.UserDTO;
import com.ironhack.BankSystem.dto.UserEditDTO;
import com.ironhack.BankSystem.dto.UserEditPasswordDTO;
import com.ironhack.BankSystem.model.AccountHolder;
import com.ironhack.BankSystem.model.Admin;
import com.ironhack.BankSystem.model.ThirdParty;
import com.ironhack.BankSystem.model.User;

import java.util.List;

public interface IUserController {

    List<User> getUsers();

    User getUserById(long id);

    User getUserByUsername(String username);

    List<Admin> getAdmins();

    List<AccountHolder> getAccountHolders();

    List<ThirdParty> getAThirdParties();


    void createNewAdmin(UserDTO admin);

    void createNewAccountHolder(UserAccountHolderDTO accountHolder);

    void createNewThirdParty(UserDTO thirdParty);


    void editPassword(String username, UserEditPasswordDTO userPassword);

    void editUser(String username, UserEditDTO user);
}
