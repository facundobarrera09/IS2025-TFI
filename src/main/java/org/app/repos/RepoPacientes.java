package org.app.repos;

import org.app.models.paciente.FindOrCreatePaciente;
import org.domain.interfaces.IRepositorioPacientes;
import org.domain.models.Domicilio;
import org.domain.models.Paciente;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("repoPacientes")
public class RepoPacientes implements IRepositorioPacientes {

    private Map<String, Paciente> pacientes;

    public RepoPacientes(){
        this.pacientes = new HashMap<>();
        Paciente paciente = new Paciente(
                "20432165491",
                "Barrera",
                "Facundo",
                new Domicilio("Junin", "1000", "SMT")
        );
        this.pacientes.put("20432165491", paciente);
    }

    @Override
    public void guardarPaciente(Paciente paciente) {
        this.pacientes.put(paciente.getCuit(), paciente);
    }

    @Override
    public Optional<Paciente> buscarPacientePorCuil(String cuit){
        return Optional.ofNullable(pacientes.get(cuit));
    }

    @Override
    public Paciente buscarOCrearPaciente(FindOrCreatePaciente formPaciente) {
        Optional<Paciente> paciente = this.buscarPacientePorCuil(formPaciente.getCuit());
        if (paciente.isEmpty()) {
            paciente = Optional.of(new Paciente(
                    formPaciente.getCuit(),
                    formPaciente.getApellido(),
                    formPaciente.getNombre(),
                    formPaciente.getDomicilio()
            ));
            this.guardarPaciente(paciente.get());
        }

        return paciente.get();
    }

    @Override
    public List<Paciente> obtenerPacientes() {
        return new ArrayList<>(pacientes.values());
    }

    public Map<String, Paciente> getPacientes() {
        return pacientes;
    }
}
