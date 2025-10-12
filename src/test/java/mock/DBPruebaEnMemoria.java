package mock;

import org.app.interfaces.RepositorioPacientes;
import org.domain.Paciente;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DBPruebaEnMemoria implements RepositorioPacientes {

    private Map<String, Paciente> pacientes;


    public DBPruebaEnMemoria(){
        this.pacientes = new HashMap<>();
    }
    @Override

    public void guardarPaciente(Paciente paciente) {
        this.pacientes.put(paciente.getCuit(), paciente);
    }

    public Optional<Paciente> buscarPacientePorCuil(String cuit){
        return Optional.ofNullable(pacientes.get(cuit));
    }
}
