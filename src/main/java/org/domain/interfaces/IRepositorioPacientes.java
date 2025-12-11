package org.domain.interfaces;
import org.app.models.paciente.FindOrCreatePaciente;
import org.domain.models.Paciente;

import java.util.List;
import java.util.Optional;

public interface IRepositorioPacientes {
    public void guardarPaciente(Paciente paciente);
    public Optional<Paciente> buscarPacientePorCuil(String cuit);
    public Paciente buscarOCrearPaciente(FindOrCreatePaciente formPaciente);
    public List<Paciente> obtenerPacientes();
}
