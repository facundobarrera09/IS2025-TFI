package org.app.repos;

import org.domain.interfaces.IRepositorioUsuarios;
import org.domain.models.Autoridad;
import org.domain.models.Enfermera;
import org.domain.models.Medico;
import org.domain.models.Usuario;
import org.domain.models.helpers.Argon2Hasher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class RepoUsuarios implements IRepositorioUsuarios {
    Map<String, Usuario> usuarios;

    public RepoUsuarios() {
        usuarios = new HashMap<>();
        usuarios.put("med@mail.com", new Usuario("med@mail.com", new Argon2Hasher().hashearContraseña("password"), Autoridad.MEDICO, new Medico("4124")));
        usuarios.put("enf@mail.com", new Usuario("enf@mail.com", new Argon2Hasher().hashearContraseña("password"), Autoridad.ENFERMERO, new Enfermera("Claudia","Gonzales")));
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
