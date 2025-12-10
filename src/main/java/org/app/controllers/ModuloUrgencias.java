package org.app.controllers;

import org.app.exceptions.InvalidCreateIngreso;
import org.app.exceptions.InvalidFindOrCreatePaciente;
import org.app.models.ingresos.CreateIngreso;
import org.app.models.ingresos.ResInvalidCreateIngreso;
import org.app.models.ingresos.ResListaDeIngresos;
import org.app.models.paciente.ResInvalidFindOrCreatePaciente;
import org.app.services.AuthService;
import org.app.services.UrgenciasService;
import org.domain.errors.UsuarioNoAutenticado;
import org.domain.errors.UsuarioNoAutorizado;
import org.domain.errors.ListaDeEsperaVacia;
import org.domain.errors.PacienteYaIngresado;
import org.domain.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class ModuloUrgencias {
    private final AuthService authService;
    private final UrgenciasService urgenciasService;

    @Autowired
    ModuloUrgencias(AuthService authService, UrgenciasService urgenciasService) {
        this.authService = authService;
        this.urgenciasService = urgenciasService;
    }

    @PostMapping("/ingresos")
    public ResponseEntity<?> registrarUrgencia(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @RequestBody CreateIngreso form) {
        System.out.println("Registrando urgencia");
        if (!authService.tieneAutoridad(authHeader, Autoridad.ENFERMERO)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            urgenciasService.registrarUrgencia(form);
            return ResponseEntity.ok().build();
        }
        catch (InvalidFindOrCreatePaciente e) {
            return ResponseEntity.badRequest().body(
                    new ResInvalidFindOrCreatePaciente(e.getMessage(), form.getPaciente())
            );
        }
        catch (InvalidCreateIngreso e) {
            return ResponseEntity.badRequest().body(
                    new ResInvalidCreateIngreso(e.getMessage(), form)
            );
        }
        catch (PacienteYaIngresado e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/ingresos")
    public ResponseEntity<ResListaDeIngresos> listarUrgencias(@RequestHeader("Authorization") String authHeader) {
        if (!authService.tieneAutoridad(authHeader, Autoridad.ENFERMERO) && !authService.tieneAutoridad(authHeader, Autoridad.MEDICO)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ResListaDeIngresos respuesta = new ResListaDeIngresos(
                LocalDateTime.now(),
                urgenciasService.listarUrgencias()
        );

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/ingresos/reclamar")
    public ResponseEntity<?> reclamarIngreso(@RequestHeader("Authorization") String authHeader) {
        try {
            Usuario usuario = this.authService.validarSesion(authHeader);
            Ingreso ingreso = this.urgenciasService.reclamarIngreso(usuario);
            return ResponseEntity.ok(ingreso);
        }
        catch (UsuarioNoAutenticado e) {
            return ResponseEntity.status(401).build();
        }
        catch (UsuarioNoAutorizado e) {
            return ResponseEntity.status(403).build();
        }
        catch (ListaDeEsperaVacia e) {
            return ResponseEntity.badRequest().body("No hay pacientes en la lista de espera");
        }
    }
}

