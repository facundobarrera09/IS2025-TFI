package org.domain.models.helpers;

import org.domain.interfaces.helpers.IPasswordHasher;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class Argon2Hasher implements IPasswordHasher {
    Argon2PasswordEncoder encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    @Override
    public String hashearContraseña(String contraseña) {
        return encoder.encode(contraseña);
    }

    @Override
    public boolean chequearHash(String hash, String contraseña) {
        return encoder.matches(contraseña, hash);
    }
}
