package com.ironhack.BankSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", unique = true)
    private String name;

    @ToString.Exclude
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)

    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public Roles(@NotNull String name) {

        this.name = name;
    }
}
