package org.domain.interfaces;

import org.domain.models.Usuario;
import org.domain.models.AuthenticationException;
import java.util.List;

public interface ServicioAutenticacion {
    Usuario registrarUsuario(String email, String contraseña, String autoridad) throws IllegalArgumentException;
    Usuario iniciarSesion(String email, String contraseña) throws AuthenticationException;
    List<String> obtenerHistoriasUsuario(String autoridad);
}
