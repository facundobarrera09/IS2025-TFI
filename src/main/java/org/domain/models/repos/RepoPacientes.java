package org.domain.models.repos;

import org.domain.interfaces.RepositorioPacientes;
import org.domain.models.Domicilio;
import org.domain.models.Paciente;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RepoPacientes implements RepositorioPacientes {

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

    public Map<String, Paciente> getPacientes() {
        return pacientes;
    }
}
