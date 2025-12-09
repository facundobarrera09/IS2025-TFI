package mock;

import org.app.models.paciente.FindOrCreatePaciente;
import org.domain.interfaces.IRepositorioPacientes;
import org.domain.models.Paciente;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DBPruebaEnMemoria implements IRepositorioPacientes {

    private Map<String, Paciente> pacientes;

    public DBPruebaEnMemoria(){
        this.pacientes = new HashMap<>();
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
        return null;
    }

    public Map<String, Paciente> getPacientes() {
        return pacientes;
    }
}
