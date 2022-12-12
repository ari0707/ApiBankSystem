package com.ironhack.BankSystem.model;


import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "id")
@ToString(callSuper = true)
public class Admin extends User{

    public Admin(String username, String password, String name) {

        super(username, password, name);
    }
}
