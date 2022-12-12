package com.ironhack.BankSystem.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "account_holder")
@PrimaryKeyJoinColumn(name = "id")
@ToString(callSuper = true)
public class AccountHolder extends User {

    @NotNull
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull
    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "streetAddress", column = @Column(name = "primary_address_street", nullable = false)),
            @AttributeOverride(name = "postalCode", column = @Column(name = "primary_address_postal_code", nullable = false)),
            @AttributeOverride(name = "city", column = @Column(name = "primary_address_city", nullable = false)),
            @AttributeOverride(name = "country", column = @Column(name = "primary_address_country", nullable = false)),
    })
    private Address primaryAddress;


    @ToString.Exclude
    @OneToMany(mappedBy = "primaryOwner")
    private List<Account> primaryAccounts = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "streetAddress", column = @Column(name = "mailing_address_street")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "mailing_address_postal_code")),
            @AttributeOverride(name = "city", column = @Column(name = "mailing_address_city")),
            @AttributeOverride(name = "country", column = @Column(name = "mailing_address_country")),
    })
    private Address mailingAddress;


    @ToString.Exclude
    @OneToMany(mappedBy = "secondaryOwner")
    private List<Account> secondaryAccounts = new ArrayList<>();

    // Constructor con primary y mailing addresses.
    public AccountHolder(String username, String password, String name, @NotNull LocalDate dateOfBirth, @NotNull Address primaryAddress, Address mailingAddress) {
        super(username, password, name);
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

    // Constructor solo con primary address.
    public AccountHolder(String username, String password, String name, @NotNull LocalDate dateOfBirth, @NotNull Address primaryAddress) {
        super(username, password, name);
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
    }


}
