package mock;

import org.domain.models.Usuario;
import org.domain.interfaces.RepositorioUsuarios;
import java.util.*;

public class RepositorioUsuariosPrueba implements RepositorioUsuarios {
    private Map<String, Usuario> usuarios = new HashMap<>();

    @Override
    public void guardarUsuario(Usuario usuario) {
        usuarios.put(usuario.getEmail(), usuario);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarios.get(email); // Devuelve null si no existe
    }

    @Override
    public boolean existeEmail(String email) {
        return usuarios.containsKey(email);
    }

    public void limpiar() {
        usuarios.clear();
    }
}