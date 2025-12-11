package org.app.services;

import org.app.models.paciente.CreatePaciente;
import org.domain.errors.UsuarioNoAutorizado;
import org.domain.interfaces.IRepositorioPacientes;
import org.domain.models.Autoridad;
import org.domain.models.Paciente;
import org.domain.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Service
public class PacientesService {
    private IRepositorioPacientes repoPacientes;

    @Autowired
    public PacientesService(IRepositorioPacientes repoPacientes) {
        this.repoPacientes = repoPacientes;
    }

    public List<Paciente> obtenerPacientes() { return repoPacientes.obtenerPacientes(); }

    @GetMapping("/pacientes/{cuit}")
    public Optional<Paciente> obtenerPacientePorCuit(String cuit) {
        if (cuit == null || cuit.isEmpty()) {
            throw new IllegalArgumentException("cuit no puede ser nulo");
        }

        return repoPacientes.buscarPacientePorCuil(cuit);
    }

    @PostMapping("/pacientes")
    public Paciente crearPaciente(Usuario usuario, CreatePaciente form) {
        if (!usuario.getAutoridad().equals(Autoridad.ENFERMERO)) {
            throw new UsuarioNoAutorizado("El usuario no tiene permiso para realizar esta acción");
        }
        if (repoPacientes.buscarPacientePorCuil(form.getCuit()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un paciente con ese CUIT");
        }
        Paciente paciente = new Paciente(form.getCuit(), form.getApellido(), form.getNombre(), form.getDomicilio());
        repoPacientes.guardarPaciente(paciente);
        return paciente;
    }
}
