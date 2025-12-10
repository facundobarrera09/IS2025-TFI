package org.app.controllers;

import org.app.models.auth.LoginData;
import org.app.models.auth.ResInvalidUser;
import org.app.services.AuthService;
import org.domain.errors.UsuarioNoAutenticado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
}
