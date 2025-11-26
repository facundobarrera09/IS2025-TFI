package org.domain.interfaces;


import org.domain.models.Usuario;

public interface RepositorioUsuarios {
    // Usar solo un método para guardar
    void guardarUsuario(Usuario usuario);
    // Usar solo un método para buscar
    Usuario buscarPorEmail(String email);
    // Usar solo un método para verificar existencia
    boolean existeEmail(String email);
}
