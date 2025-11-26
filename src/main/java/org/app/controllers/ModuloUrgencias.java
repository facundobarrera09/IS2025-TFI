package org.app.controllers;

import org.app.models.CreateIngreso;
import org.app.models.paciente.FindOrCreatePaciente;
import org.domain.controllers.ServicioUrgencia;
import org.domain.interfaces.IRepositorioEnfermeras;
import org.domain.interfaces.RepositorioPacientes;
import org.domain.models.*;
import org.domain.models.repos.RepoEnfermeras;
import org.domain.models.repos.RepoPacientes;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public void registrarPaciente(@RequestBody CreateIngreso form) {

        FindOrCreatePaciente formPaciente = form.getPaciente();
        Optional<Paciente> paciente = repositorioPacientes.buscarPacientePorCuil(formPaciente.getCuit());
        if (paciente.isEmpty()) {
            paciente = Optional.of(new Paciente(
                    formPaciente.getCuit(),
                    formPaciente.getApellido(),
                    formPaciente.getNombre(),
                    formPaciente.getDomicilio()
            ));
            repositorioPacientes.guardarPaciente(paciente.get());
        }

        System.out.println("Paciente registrado");

        Optional<Enfermera> enfermera = repositorioEnfermeras.obtenerEnfermera(UUID.fromString(form.getEnfermera().getUuid()));
        if (enfermera.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Enfermera con ese uuid no existe");
        }

        System.out.println("Enfermera encontrada");

        try {
            this.servicioUrgencia.registrarUrgencia(
                    paciente.get().getCuit(),
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
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), e.getMessage());
        }

        System.out.println("Ingreso registrado");
    }

    @GetMapping("/registroPacientes")
    public String listarUrgencias() {
        throw new ResponseStatusException(HttpStatusCode.valueOf(500));
    }
}
