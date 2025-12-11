package org.app.controllers;

import org.app.models.paciente.CreatePaciente;
import org.app.services.AuthService;
import org.app.services.PacientesService;
import org.domain.errors.UsuarioNoAutenticado;
import org.domain.errors.UsuarioNoAutorizado;
import org.domain.models.Paciente;
import org.domain.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    public ResponseEntity<?> obtenerPacientes(@RequestHeader("Authorization") String authHeader) {
        try {
            Usuario usuario = authService.validarSesion(authHeader);
            List<Paciente> pacientes = pacientesService.obtenerPacientes();
            return ResponseEntity.ok(pacientes);
        } catch (UsuarioNoAutenticado e) {
            return  ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/pacientes/{cuit}")
    public ResponseEntity<?> obtenerPacientePorCuit(@RequestHeader("Authorization") String authHeader, @PathVariable("cuit") String cuit) {
        try {
            Usuario usuario = authService.validarSesion(authHeader);
            Optional<Paciente> paciente = pacientesService.obtenerPacientePorCuit(cuit);
            return ResponseEntity.ok().body(paciente.get());
        }
        catch (IllegalArgumentException | NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        catch (UsuarioNoAutenticado e) {
            return ResponseEntity.status(401).build();
        }
    }

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
