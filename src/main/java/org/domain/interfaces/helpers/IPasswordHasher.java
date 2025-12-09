package org.domain.interfaces.helpers;

public interface IPasswordHasher {
    public String hashearContraseña(String contraseña);
    public boolean chequearHash(String hash, String contraseña);
}
