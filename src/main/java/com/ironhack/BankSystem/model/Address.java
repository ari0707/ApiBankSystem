package com.ironhack.BankSystem.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Address {

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String streetAddress;



}
