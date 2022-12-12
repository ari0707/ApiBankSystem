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
public class UserEditPasswordDTO {

    @NotBlank(message = "Current password cannot be empty.")
    private String currentPassword;

    @NotBlank(message = "New password cannot be empty or blank.")
    private String newPassword;

    @NotBlank(message = "New password cannot be empty or blank.")
    private String repeatedNewPassword;
}
