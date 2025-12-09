package org.app.repos;

import org.domain.interfaces.IRepositorioUsuarios;
import org.domain.models.Usuario;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class RepoUsuarios implements IRepositorioUsuarios {
    Map<String, Usuario> usuarios;

    public RepoUsuarios() {
        usuarios = new HashMap<>();
    }

    @Override
    public void guardarUsuario(Usuario usuario) {
        usuarios.put(usuario.getEmail(), usuario);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarios.get(email);
    }

    @Override
    public boolean existeEmail(String email) {
        return usuarios.containsKey(email);
    }
}
