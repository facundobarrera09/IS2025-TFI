package org.app.controllers;

import org.app.models.ingresos.CreateIngreso;
import org.app.models.ingresos.ResInvalidCreateIngreso;
import org.app.models.ingresos.ResListaDeIngresos;
import org.app.models.paciente.FindOrCreatePaciente;
import org.app.models.paciente.ResInvalidFindOrCreatePaciente;
import org.domain.controllers.ServicioUrgencia;
import org.domain.interfaces.IRepositorioEnfermeras;
import org.domain.interfaces.RepositorioPacientes;
import org.domain.models.*;
import org.domain.models.repos.RepoEnfermeras;
import org.domain.models.repos.RepoPacientes;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.UUID;

@RestController
public class ModuloUrgencias {
    ServicioUrgencia servicioUrgencia;
    RepositorioPacientes repositorioPacientes;
    IRepositorioEnfermeras repositorioEnfermeras;

    ModuloUrgencias() {
        this.repositorioPacientes = new RepoPacientes();
        this.servicioUrgencia = new ServicioUrgencia(repositorioPacientes);
        this.repositorioEnfermeras = new RepoEnfermeras();
    }

    @PostMapping("/ingresos")
    public ResponseEntity<?> registrarPaciente(@RequestBody CreateIngreso form) {

        FindOrCreatePaciente formPaciente = form.getPaciente();
        Paciente paciente;
        try {
            paciente = repositorioPacientes.buscarOCrearPaciente(formPaciente);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ResInvalidFindOrCreatePaciente(e.getMessage(), form.getPaciente())
            );
        }

        Optional<Enfermera> enfermera = repositorioEnfermeras.obtenerEnfermera(UUID.fromString(form.getEnfermera().getUuid()));
        if (enfermera.isEmpty()) {
            return ResponseEntity.badRequest().body("Enfermera con ese uuid no existe");
        }

        try {
            this.servicioUrgencia.registrarUrgencia(
                    paciente.getCuit(),
                    enfermera.get(),
                    form.getInforme(),
                    form.getTemperatura(),
                    NivelEmergencia.buscarPorNombre(form.getNivel()),
                    form.getFrecuenciaCardiaca(),
                    form.getFrecuenciaRespiratoria(),
                    new TensionArterial(form.getTensionArterial())
            );
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ResInvalidCreateIngreso(e.getMessage(), form)
            );
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/registroPacientes")
    public String listarUrgencias() {
        throw new ResponseStatusException(HttpStatusCode.valueOf(500));
    }
}
