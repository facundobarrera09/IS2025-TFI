package org.app;

import org.domain.controllers.ServicioUrgencia;
import org.domain.interfaces.RepositorioPacientes;
import org.domain.models.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.PriorityQueue;

@RestController
public class ModuloUrgencias {
    ServicioUrgencia servicioUrgencia;
    RepositorioPacientes repositorioPacientes;

    ModuloUrgencias() {
        this.servicioUrgencia = new ServicioUrgencia(repositorioPacientes);
    }

    @PostMapping("/registroPacientes")
    public String registrarPaciente(@RequestBody Paciente paciente) {

        this.servicioUrgencia.registrarUrgencia(
                "20-43965801-1",
                new Enfermera("xxxcvxcv", "sdfsdf"),
                "sadfadfsdfsdfsdf",
                (float) 37.0,
                NivelEmergencia.SIN_URGENCIA,
                (float) 80.0,
                (float) 120,
                new TensionArterial("120/80")
        );

        PriorityQueue<Ingreso> listaOrdenda = this.servicioUrgencia.getListaDeEspera();

        throw new ResponseStatusException(HttpStatusCode.valueOf(500));
    }

    @GetMapping("/registroPacientes")
    public String listarUrgencias() {
        throw new ResponseStatusException(HttpStatusCode.valueOf(500));
    }
}
