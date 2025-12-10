package org.domain.controllers;

import org.domain.interfaces.IRepositorioUsuarios;
import org.domain.interfaces.IControladorAutenticacion;
import org.domain.interfaces.helpers.IPasswordHasher;
import org.domain.models.Autoridad;
import org.domain.models.Enfermera;
import org.domain.models.Medico;
import org.domain.models.Usuario;
import org.domain.errors.UsuarioNoAutenticado;
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

    public void validarDatosDeRegistro(String email, String contraseña){
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email debe estar definido");
        }
        if (contraseña == null || contraseña.isEmpty()) {
            throw new IllegalArgumentException("contraseña debe estar definido");
        }

        // Verificar si el email ya existe
        if (repoUsuarios.existeEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Validar contraseña
        if (contraseña.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
    }

    @Override
    public Usuario registrarUsuario(String email, String contraseña, Autoridad autoridad) throws IllegalArgumentException {
        validarDatosDeRegistro(email, contraseña);

        // Crear y guardar usuario
        Usuario usuario = new Usuario(email, hasher.hashearContraseña(contraseña), autoridad);
        repoUsuarios.guardarUsuario(usuario);

        return usuario;
    }

    public Usuario registrarUsuario(String email, String contraseña, Autoridad autoridad, Medico medico) throws IllegalArgumentException {
        validarDatosDeRegistro(email, contraseña);

        // Crear y guardar usuario
        Usuario usuario = new Usuario(email, hasher.hashearContraseña(contraseña), autoridad, medico);
        repoUsuarios.guardarUsuario(usuario);

        return usuario;
    }

    public Usuario registrarUsuario(String email, String contraseña, Autoridad autoridad, Enfermera enfermera) throws IllegalArgumentException {
        validarDatosDeRegistro(email, contraseña);

        // Crear y guardar usuario
        Usuario usuario = new Usuario(email, hasher.hashearContraseña(contraseña), autoridad, enfermera);
        repoUsuarios.guardarUsuario(usuario);

        return usuario;
    }

    @Override
    public Usuario iniciarSesion(String email, String contraseña) throws UsuarioNoAutenticado {
        Usuario usuario = repoUsuarios.buscarPorEmail(email);

        // Verificar que el usuario existe
        if (usuario == null) {
            throw new UsuarioNoAutenticado("Usuario o contraseña inválidos");
        }

        // Comparar los hashes
        if (!hasher.chequearHash(usuario.getHashContraseña(), contraseña)) {
            throw new UsuarioNoAutenticado("Usuario o contraseña inválidos");
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