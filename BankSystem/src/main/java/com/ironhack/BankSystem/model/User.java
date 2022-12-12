package com.ironhack.BankSystem.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.ironhack.BankSystem.utils.EncryptedKeysUtil.encryptedKey;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @ToString.Exclude
    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "username", unique = true)
    private String username;


    @ToString.Include(name = "name")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Roles> roles = new HashSet<>();

    public User(@NotNull String username, String password, @NotNull String name) {
        this.username = username;
        this.password = encryptedKey(password);
        this.name = name;
    }
}
