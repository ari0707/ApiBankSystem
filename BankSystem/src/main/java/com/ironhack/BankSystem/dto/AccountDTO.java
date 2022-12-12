package com.ironhack.BankSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountDTO {


    @NotNull
    @PositiveOrZero(message = "Initial balance must be positive and not null.")
    private BigDecimal initialBalance;

    @NotBlank
    @Length(min = 3, max = 3, message = "The currency code must be a valid code of 3 letters.")
    private String currency;

    @NotNull
    @Positive(message = "Primary owner id must be positive and not null.")
    private Long primaryOwnerId;

    @NotBlank(message = "Primary owner cannot be blank or empty.")
    private String primaryOwnerUsername;

    @Positive(message = "Secondary owner id must be positive and exist.")
    private Long secondaryOwnerId;

    private String secondaryOwnerUsername;
}
