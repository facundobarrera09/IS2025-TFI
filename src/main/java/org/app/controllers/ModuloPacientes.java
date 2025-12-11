package org.app.controllers;

import org.app.services.PacientesService;
import org.domain.models.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ModuloPacientes {
    private PacientesService pacientesService;

    @Autowired
    public ModuloPacientes(PacientesService pacientesService) {
        this.pacientesService = pacientesService;
    }

    @GetMapping("/pacientes")
    public ResponseEntity<?> obtenerPacientes() { return ResponseEntity.internalServerError().build(); }

    @GetMapping("/pacientes/{cuit}")
    public ResponseEntity<?> obtenerPacientePorCuit(@PathVariable("cuit") String cuit) { return ResponseEntity.internalServerError().build(); }

    @PostMapping("/pacientes")
    public ResponseEntity<?> crearPaciente() { return ResponseEntity.internalServerError().build(); }
}
