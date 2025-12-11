package org.app.controllers;

import org.apache.coyote.Response;
import org.app.exceptions.InvalidCreateIngreso;
import org.app.exceptions.InvalidFindOrCreatePaciente;
import org.app.models.ingresos.AddInforme;
import org.app.models.ingresos.CreateIngreso;
import org.app.models.ingresos.ResInvalidCreateIngreso;
import org.app.models.ingresos.ResListaDeIngresos;
import org.app.models.paciente.ResInvalidFindOrCreatePaciente;
import org.app.services.AuthService;
import org.app.services.UrgenciasService;
import org.domain.errors.*;
import org.domain.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
        try {
            Usuario usuario = authService.validarSesion(authHeader);
            urgenciasService.registrarUrgencia(usuario, form);
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
        } catch (UsuarioNoAutenticado e) {
            return ResponseEntity.status(403).body(e.getMessage());
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

    @GetMapping("/ingresos/todos")
    public ResponseEntity<?> obtenerIngresos(@RequestHeader("Authorization") String authHeader) {
        try {
            Usuario usuario = this.authService.validarSesion(authHeader);
            List<Ingreso> ingresos = urgenciasService.obtenerIngresos();
            return ResponseEntity.ok(ingresos);
        }
        catch (UsuarioNoAutenticado e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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

    @PostMapping("/ingresos/informe")
    public ResponseEntity<?> registrarInforme(@RequestHeader("Authorization") String authHeader, @RequestBody AddInforme form) {
        try {
            Usuario usuario = authService.validarSesion(authHeader);
            urgenciasService.registrarInforme(usuario, form.getInforme());
            return ResponseEntity.noContent().build();
        }
        catch (UsuarioNoAutenticado e) {
            return ResponseEntity.status(401).build();
        }
        catch (IllegalArgumentException | SinIngresoEnProceso e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (UsuarioNoAutorizado e) {
            return ResponseEntity.status(403).build();
        }
    }
}

