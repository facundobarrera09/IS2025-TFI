package org.app.services;

import org.domain.models.Paciente;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
public class PacientesService {

    public List<Paciente> obtenerPacientes() { return List.of(); }

    @GetMapping("/pacientes/{cuit}")
    public Paciente obtenerPacientePorCuit(String cuit) { return null; }

    @PostMapping("/pacientes")
    public Paciente crearPaciente() { return null; }
}
