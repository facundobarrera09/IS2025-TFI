package org.app.controllers;

import org.app.models.paciente.CreatePaciente;
import org.app.services.AuthService;
import org.app.services.PacientesService;
import org.domain.errors.UsuarioNoAutenticado;
import org.domain.errors.UsuarioNoAutorizado;
import org.domain.models.Paciente;
import org.domain.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ModuloPacientes {
    private AuthService authService;
    private final PacientesService pacientesService;

    @Autowired
    public ModuloPacientes(PacientesService pacientesService, AuthService authService) {
        this.pacientesService = pacientesService;
        this.authService = authService;
    }

    @GetMapping("/pacientes")
    public ResponseEntity<?> obtenerPacientes() { return ResponseEntity.internalServerError().build(); }

    @GetMapping("/pacientes/{cuit}")
    public ResponseEntity<?> obtenerPacientePorCuit(@PathVariable("cuit") String cuit) { return ResponseEntity.internalServerError().build(); }

    @PostMapping("/pacientes")
    public ResponseEntity<?> crearPaciente(@RequestHeader("Authorization") String authHeader, @RequestBody CreatePaciente form) {
        try {
            Usuario usuario = authService.validarSesion(authHeader);
            Paciente paciente = pacientesService.crearPaciente(usuario, form);
            return ResponseEntity.status(201).body(paciente);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (UsuarioNoAutenticado e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
        catch (UsuarioNoAutorizado e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}
