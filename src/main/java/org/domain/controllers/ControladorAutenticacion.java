package org.domain.controllers;

import org.domain.interfaces.IRepositorioUsuarios;
import org.domain.interfaces.IControladorAutenticacion;
import org.domain.interfaces.helpers.IPasswordHasher;
import org.domain.models.Autoridad;
import org.domain.models.Usuario;
import org.domain.errors.AuthenticationException;
import java.util.*;

public class ControladorAutenticacion implements IControladorAutenticacion {

    private final IRepositorioUsuarios repoUsuarios;
    private final IPasswordHasher hasher;

    public ControladorAutenticacion(IRepositorioUsuarios repositorio, IPasswordHasher passwordHasher) {
        this.hasher = passwordHasher;
        this.repoUsuarios = repositorio;

//        this.repoUsuarios.guardarUsuario(new Usuario("enf@mail.com", passwordHasher.hashearContraseña("password"), Autoridad.ENFERMERO));
//        this.repoUsuarios.guardarUsuario(new Usuario("med@mail.com", passwordHasher.hashearContraseña("password"), Autoridad.MEDICO));
    }

    @Override
    public Usuario registrarUsuario(String email, String contraseña, Autoridad autoridad) throws IllegalArgumentException {

        // Verificar si el email ya existe
        if (repoUsuarios.existeEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Validar contraseña
        if (contraseña.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }

        // Crear y guardar usuario
        Usuario usuario = new Usuario(email, hasher.hashearContraseña(contraseña), autoridad);
        repoUsuarios.guardarUsuario(usuario);

        return usuario;
    }

    @Override
    public Usuario iniciarSesion(String email, String contraseña) throws AuthenticationException {
        Usuario usuario = repoUsuarios.buscarPorEmail(email);

        // Verificar que el usuario existe
        if (usuario == null) {
            throw new AuthenticationException("Usuario o contraseña inválidos");
        }

        // Comparar los hashes
        if (!hasher.chequearHash(usuario.getHashContraseña(), contraseña)) {
            throw new AuthenticationException("Usuario o contraseña inválidos");
        }

        return usuario;
    }



    @Override
    public List<String> obtenerHistoriasUsuario(Autoridad autoridad) {
        return switch (autoridad) {
            case Autoridad.MEDICO -> Arrays.asList("IS2025-003", "ES2025-004");
            case Autoridad.ENFERMERO -> Arrays.asList("IS2025-001", "IS2025-002");
            default -> new ArrayList<>();
        };
    }
}