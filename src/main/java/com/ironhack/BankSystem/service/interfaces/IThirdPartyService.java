package com.ironhack.BankSystem.service.interfaces;

import com.ironhack.BankSystem.dto.UserDTO;
import com.ironhack.BankSystem.model.ThirdParty;
import jakarta.persistence.EntityExistsException;

import java.util.List;

public interface IThirdPartyService {

    List<ThirdParty> getAll();

    void newUser(UserDTO thirdParty) throws EntityExistsException;


    boolean hasHashedKey(String hashedKey);

}
