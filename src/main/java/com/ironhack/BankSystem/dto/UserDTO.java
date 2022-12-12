package com.ironhack.BankSystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    @NotBlank(message = "Username cannot be empty or blank.")
    private String username;

    @NotBlank(message = "Password cannot be empty or blank.")
    private String password;

    @NotBlank(message = "Name cannot be empty or blank.")
    private String name;
}
