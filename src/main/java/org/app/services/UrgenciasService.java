package org.app.services;

import org.app.exceptions.InvalidCreateIngreso;
import org.app.exceptions.InvalidFindOrCreatePaciente;
import org.app.models.ingresos.CreateIngreso;
import org.app.models.paciente.FindOrCreatePaciente;
import org.domain.controllers.ControladorUrgencias;
import org.domain.errors.UsuarioNoAutorizado;
import org.domain.interfaces.IRepositorioEnfermeras;
import org.domain.interfaces.IRepositorioPacientes;
import org.domain.models.*;
import org.app.repos.RepoEnfermeras;
import org.app.repos.RepoPacientes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.UUID;

@Service
public class UrgenciasService {
    ControladorUrgencias controladorUrgencia;
    IRepositorioPacientes repositorioPacientes;
//    IRepositorioEnfermeras repositorioEnfermeras;

    @Autowired
    UrgenciasService(IRepositorioPacientes repoPacientes) {
        this.repositorioPacientes = repoPacientes;
//        this.repositorioEnfermeras = new RepoEnfermeras();
        this.controladorUrgencia = new ControladorUrgencias(repositorioPacientes);
    }

    public void registrarUrgencia(Usuario usuario, CreateIngreso form) {
        if (!usuario.getAutoridad().equals(Autoridad.ENFERMERO)) {
            throw new UsuarioNoAutorizado("El usuario no tiene permiso para realizar esta acción");
        }

        FindOrCreatePaciente formPaciente = form.getPaciente();
        Paciente paciente;

        try {
            paciente = repositorioPacientes.buscarOCrearPaciente(formPaciente);
        }
        catch (IllegalArgumentException e) {
            throw new InvalidFindOrCreatePaciente(e.getMessage());
        }

//        Optional<Enfermera> enfermera = repositorioEnfermeras.obtenerEnfermera(UUID.fromString(form.getEnfermera().getUuid()));
//        if (enfermera.isEmpty()) {
//            throw new InvalidCreateIngreso("Enfermera con ese uuid no existe");
//        }

        Enfermera enfermera = usuario.getEnfermera();

        try {
            this.controladorUrgencia.registrarUrgencia(
                    paciente.getCuit(),
                    enfermera,
                    form.getInforme(),
                    form.getTemperatura(),
                    NivelEmergencia.buscarPorNombre(form.getNivel()),
                    form.getFrecuenciaCardiaca(),
                    form.getFrecuenciaRespiratoria(),
                    new TensionArterial(form.getTensionArterial())
            );
        }
        catch (IllegalArgumentException e) {
            throw new InvalidCreateIngreso(e.getMessage());
        }
    }

    public PriorityQueue<Ingreso> listarUrgencias() {
        return this.controladorUrgencia.getListaDeEspera();
    }

    public Ingreso reclamarIngreso(Usuario usuario) {
        if (usuario.getAutoridad() != Autoridad.MEDICO) {
            throw new UsuarioNoAutorizado(UsuarioNoAutorizado.PROHIBIDO);
        }

        return this.controladorUrgencia.reclamarIngreso(usuario.getMedico());
    }

    public void registrarInforme(Usuario usuario, String informe) {
        if (usuario.getAutoridad() != Autoridad.MEDICO) {
            throw new UsuarioNoAutorizado(UsuarioNoAutorizado.PROHIBIDO);
        }

        this.controladorUrgencia.registrarInforme(usuario.getMedico(), informe);
    }

    public List<Ingreso> obtenerIngresos() {
        return this.controladorUrgencia.obtenerIngresos();
    }
}
