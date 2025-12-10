package org.app.controllers;

import org.app.models.auth.LoginData;
import org.app.models.auth.RegistroData;
import org.app.models.auth.ResInvalidUser;
import org.app.services.AuthService;
import org.domain.errors.UsuarioNoAutenticado;
import org.domain.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModuloAutenticacion {
    private final AuthService authService;

    @Autowired
    ModuloAutenticacion(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody LoginData form) {
        try {
            return ResponseEntity.ok(authService.iniciarSesion(form));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ResInvalidUser("Email y contraseña deben estar definidos", form)
            );
        }
        catch (UsuarioNoAutenticado e) {
            return ResponseEntity.badRequest().body(
                    new ResInvalidUser("Email o contraseña incorrectos", form)
            );
        }
    }

    @PostMapping("/usuarios")
    public ResponseEntity<?> registrarUsuario(@RequestHeader("Authorization") String authHeader, @RequestBody RegistroData form) {
        try {
            authService.validarSesion(authHeader);
            authService.registrarUsuario(form);
            return ResponseEntity.noContent().build();
        }
        catch (UsuarioNoAutenticado e) {
            return ResponseEntity.status(401).build();
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
