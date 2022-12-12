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
    // ======================================== GET Methods ========================================
    // -------------------- Global USER [ADMIN] --------------------
    List<User> getUsers();

    User getUserById(long id);

    User getUserByUsername(String username);

    // -------------------- Admins [ADMIN] --------------------
    List<Admin> getAdmins();

    // -------------------- Account Holder [ADMIN] --------------------
    List<AccountHolder> getAccountHolders();

    // -------------------- Third Parties [ADMIN] --------------------
    List<ThirdParty> getAThirdParties();


    // ======================================== POST Methods ========================================
    // -------------------- New Admin [ADMIN] --------------------
    void createNewAdmin(UserDTO admin);

    // -------------------- New Account Holder [PUBLIC] --------------------
    void createNewAccountHolder(UserAccountHolderDTO accountHolder);

    // -------------------- New Third Party [ADMIN] --------------------
    void createNewThirdParty(UserDTO thirdParty);


    // ======================================== PATCH Methods ========================================
    void editPassword(String username, UserEditPasswordDTO userPassword);

    void editUser(String username, UserEditDTO user);
}
