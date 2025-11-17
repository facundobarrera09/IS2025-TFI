package org.app.interfaces;

import org.domain.Usuario;
import org.domain.AuthenticationException;
import java.util.List;

public interface ServicioAutenticacion {
    Usuario registrarUsuario(String email, String contraseña, String autoridad) throws IllegalArgumentException;
    Usuario iniciarSesion(String email, String contraseña) throws AuthenticationException;
    List<String> obtenerHistoriasUsuario(String autoridad);
}
