package org.app;

import org.app.interfaces.RepositorioPacientes;
import org.domain.Enfermera;
import org.domain.Ingreso;
import org.domain.NivelEmergencia;
import org.domain.Paciente;

import java.util.ArrayList;
import java.util.List;

public class ServicioUrgencia {

    private RepositorioPacientes DBPacientes;

    private final List<Ingreso> listaEspera;


    public ServicioUrgencia(RepositorioPacientes DBPacientes) {

        this.DBPacientes = DBPacientes;
        this.listaEspera = new ArrayList<>();
    }

    public void registrarUrgencia(String cuilPaciente, Enfermera enfermera, String informe, Float temperatura
                                  , NivelEmergencia emergencia, Float frecuenciaCardiaca,
                                  Float frecuenciaRespiratoria, Float frecuenciaDiastolica, Float frecuenciaSistolica)
    {

        Paciente paciente = DBPacientes.buscarPacientePorCuil(cuilPaciente).orElseThrow(()->new RuntimeException("Paciente no encontrado"));
        Ingreso ingreso = new Ingreso(paciente, enfermera, informe, emergencia, temperatura, frecuenciaCardiaca, frecuenciaRespiratoria, frecuenciaDiastolica, frecuenciaSistolica);

        listaEspera.add(ingreso);
    }


    public List<Ingreso> obtenerIngresosPendientes(){
        return this.listaEspera;
    }
}


