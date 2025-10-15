package org.app;

import org.app.interfaces.RepositorioPacientes;
import org.domain.*;

import java.util.PriorityQueue;

public class ServicioUrgencia {

    private final PriorityQueue<Ingreso> listaEspera;
    private final RepositorioPacientes DBPacientes;

    public ServicioUrgencia(RepositorioPacientes DBPacientes) {
        this.DBPacientes = DBPacientes;
        this.listaEspera = new PriorityQueue<>((a, b) -> {
            return -a.getNivelEmergencia().compararCon(b.getNivelEmergencia());
        });
    }

    public void registrarUrgencia(
        String cuilPaciente,
        Enfermera enfermera,
        String informe,
        Float temperatura,
        NivelEmergencia emergencia,
        Float frecuenciaCardiaca,
        Float frecuenciaRespiratoria,
        TensionArterial tensionArterial
    ) {
        Paciente paciente = DBPacientes
            .buscarPacientePorCuil(cuilPaciente)
            .orElseThrow(() -> new RuntimeException("Paciente no registrado"));

        Ingreso ingreso = new Ingreso(paciente, enfermera, informe, emergencia, temperatura, frecuenciaCardiaca, frecuenciaRespiratoria, tensionArterial);

        listaEspera.offer(ingreso);
    }

    public PriorityQueue<Ingreso> getListaDeEspera(){
        return this.listaEspera;
    }
}


