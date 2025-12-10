package org.app.services;

import org.app.models.auth.RegistroData;
import org.app.utils.JWTUtil;
import org.app.models.auth.LoginData;
import org.app.models.auth.Sesion;
import org.domain.controllers.ControladorAutenticacion;
import org.domain.errors.UsuarioNoAutenticado;
import org.domain.models.Autoridad;
import org.domain.models.Enfermera;
import org.domain.models.Medico;
import org.domain.models.Usuario;
import org.domain.models.helpers.Argon2Hasher;
import org.app.repos.RepoUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final ControladorAutenticacion controladorAutenticacion;
    private final JWTUtil jwtUtil;
    private final RepoUsuarios repoUsuarios;

    @Autowired
    AuthService(RepoUsuarios repoUsuarios) {
        this.jwtUtil = new JWTUtil();
        this.repoUsuarios = repoUsuarios;
        this.controladorAutenticacion = new ControladorAutenticacion(repoUsuarios, new Argon2Hasher());
    }

    public Sesion iniciarSesion(LoginData form) throws UsuarioNoAutenticado {
        if (form.getEmail() == null || form.getEmail().isEmpty() || form.getContraseña() == null || form.getContraseña().isEmpty()) {
            throw new IllegalArgumentException("Email y contraseña deben estar definidos");
        }

        try {
            Usuario usuario = controladorAutenticacion.iniciarSesion(form.getEmail(), form.getContraseña());
            String token = jwtUtil.generarToken(usuario.getEmail());
            return new Sesion(token);
        } catch (UsuarioNoAutenticado e) {
            throw new UsuarioNoAutenticado("Email o contraseña incorrectos");
        }
    }

    public void registrarUsuario(RegistroData form) {
        if (form.getAutoridad() == null || form.getAutoridad().isEmpty()) {
            throw new IllegalArgumentException("autoridad debe estar definido");
        }
        if (form.getAutoridad().equals(Autoridad.MEDICO.getNombre()) && form.getMedico() == null) {
            throw new IllegalArgumentException("medico debe estar definido");
        }
        if (form.getAutoridad().equals(Autoridad.ENFERMERO.getNombre()) && form.getEnfermera() == null) {
            throw new IllegalArgumentException("enfermera debe estar definido");
        }

        if (form.getAutoridad().equals(Autoridad.MEDICO.getNombre())) {
            controladorAutenticacion.registrarUsuario(
                    form.getEmail(),
                    form.getContraseña(),
                    Autoridad.MEDICO,
                    new Medico(form.getMedico().getMatricula())
            );
        }
        else {
            controladorAutenticacion.registrarUsuario(
                    form.getEmail(),
                    form.getContraseña(),
                    Autoridad.ENFERMERO,
                    new Enfermera(form.getEnfermera().getNombre(), form.getEnfermera().getApellido())
            );
        }
    }

    public Usuario validarSesion(String authHeader) throws UsuarioNoAutenticado {
        String token = authHeader.substring(7);
        try {
            String email = jwtUtil.validarTokenYDevolverEmail(token);
            Usuario usuario = repoUsuarios.buscarPorEmail(email);
            if (usuario == null) { throw new UsuarioNoAutenticado("Usuario no encontrado"); }
            return usuario;
        }
        catch (Exception e) {
            throw new UsuarioNoAutenticado("Usuario no inicio sesion");
        }
    }

    public boolean tieneAutoridad(String authHeader, Autoridad autoridad) {
        try {
            System.out.println("Validando autoridad: " + autoridad);
            Usuario usuario = validarSesion(authHeader);
            System.out.println("Usuario: " + usuario.getEmail() + " " + usuario.getAutoridad());
            return (usuario.getAutoridad() == autoridad);
        }
        catch (UsuarioNoAutenticado e) {
            return false;
        }
    }
}
