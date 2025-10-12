package mock;

import org.app.interfaces.RepositorioPacientes;
import org.domain.Paciente;

import java.util.HashMap;
import java.util.Map;

public class DBPruebaEnMemoria implements RepositorioPacientes {

    private Map<String, Paciente> pacientes;


    public DBPruebaEnMemoria(){
        this.pacientes = new HashMap<>();
    }
    @Override

    public void guardarPaciente(Paciente paciente) {
        this.pacientes.put(paciente.getCuit(), paciente);
    }
}
