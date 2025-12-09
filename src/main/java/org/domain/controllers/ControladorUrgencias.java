package org.domain.controllers;

import org.domain.interfaces.IRepositorioPacientes;
import org.domain.models.*;

import java.time.LocalDateTime;
import java.util.PriorityQueue;

public class ControladorUrgencias {

    private final IRepositorioPacientes DBPacientes;
    private final PriorityQueue<Ingreso> listaEspera;

    public ControladorUrgencias(IRepositorioPacientes DBPacientes) {
        this.DBPacientes = DBPacientes;
        this.listaEspera = new PriorityQueue<>((a, b) -> {
            boolean aExcedioFechaMaxima = a.getFechaMaxima().isAfter(LocalDateTime.now());
            boolean bExcedioFechaMaxima = b.getFechaMaxima().isAfter(LocalDateTime.now());

            if ((aExcedioFechaMaxima && bExcedioFechaMaxima) || (!aExcedioFechaMaxima && !bExcedioFechaMaxima)) {
                int comparacionEmergencia = -a.getNivelEmergencia().compararCon(b.getNivelEmergencia());
                if (comparacionEmergencia != 0) {
                    return comparacionEmergencia;
                }
                // Si tienen el mismo nivel de emergencia, el que llegó antes tiene prioridad
                return a.getFechaIngreso().compareTo(b.getFechaIngreso());
            }

            return aExcedioFechaMaxima ? 1 : -1;
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

    public void registrarUrgencia(
            String cuilPaciente,
            Enfermera enfermera,
            String informe,
            Float temperatura,
            NivelEmergencia emergencia,
            Float frecuenciaCardiaca,
            Float frecuenciaRespiratoria,
            TensionArterial tensionArterial,
            LocalDateTime fechaIngreso
    ) {
        Paciente paciente = DBPacientes
                .buscarPacientePorCuil(cuilPaciente)
                .orElseThrow(() -> new RuntimeException("Paciente no registrado"));

        Ingreso ingreso = new Ingreso(paciente, enfermera, informe, emergencia, temperatura, frecuenciaCardiaca, frecuenciaRespiratoria, tensionArterial);
        ingreso.setFechaIngreso(fechaIngreso);

        listaEspera.offer(ingreso);
    }

    public PriorityQueue<Ingreso> getListaDeEspera(){
        return this.listaEspera;
    }

    public PriorityQueue<Ingreso> getListaDeEspera(LocalDateTime fechaActual){
        PriorityQueue<Ingreso> listaEspera = new PriorityQueue<Ingreso>((a, b) -> {
            boolean aExcedioFechaMaxima = a.getFechaMaxima().isAfter(fechaActual);
            boolean bExcedioFechaMaxima = b.getFechaMaxima().isAfter(fechaActual);

            if ((aExcedioFechaMaxima && bExcedioFechaMaxima) || (!aExcedioFechaMaxima && !bExcedioFechaMaxima)) {
                return -a.getNivelEmergencia().compararCon(b.getNivelEmergencia());
            }

            return aExcedioFechaMaxima ? 1 : -1;
        });
        listaEspera.addAll(this.listaEspera);

        return listaEspera;
    }
}


