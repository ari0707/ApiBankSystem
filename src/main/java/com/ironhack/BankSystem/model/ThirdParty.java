package com.ironhack.BankSystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import static com.ironhack.BankSystem.utils.EncryptedKeysUtil.encryptedKey;
import static com.ironhack.BankSystem.utils.EncryptedKeysUtil.generateSecretKey;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "third_party")
@PrimaryKeyJoinColumn(name = "id")
public class ThirdParty extends User {

    @NotNull
    @Column(name = "hashed_key")
    private String hashedKey;

    public ThirdParty(String username, String password, String name) {
        super(username, password, name);
        // Genera una encrypted key desde username y randomly generated key. Si falla, utiliza el nombre de usuario y la contraseña.
        try {
            this.hashedKey = encryptedKey(username + generateSecretKey());
        } catch (Exception e) {
            this.hashedKey = encryptedKey(username + password);
        }
    }
}
