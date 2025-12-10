package org.domain.interfaces;

import org.domain.models.Autoridad;
import org.domain.models.Usuario;
import org.domain.errors.UsuarioNoAutenticado;
import java.util.List;

public interface IControladorAutenticacion {
    Usuario registrarUsuario(String email, String contraseña, Autoridad autoridad) throws IllegalArgumentException;
    Usuario iniciarSesion(String email, String contraseña) throws UsuarioNoAutenticado;
    List<String> obtenerHistoriasUsuario(Autoridad autoridad);
}
