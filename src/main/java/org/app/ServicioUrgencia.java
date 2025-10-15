package org.app;

import org.app.interfaces.RepositorioPacientes;
import org.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServicioUrgencia {

    private final List<Ingreso> listaEspera;
    private final RepositorioPacientes DBPacientes;

    public ServicioUrgencia(RepositorioPacientes DBPacientes) {
        this.DBPacientes = DBPacientes;
        this.listaEspera = new ArrayList<>();
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

        listaEspera.add(ingreso);

    }

    public List<Ingreso> obtenerIngresosPendientes(){

        return this.listaEspera;
    }

}


