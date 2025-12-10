package org.domain.controllers;

import org.domain.errors.ListaDeEsperaVacia;
import org.domain.errors.PacienteYaIngresado;
import org.domain.interfaces.IRepositorioPacientes;
import org.domain.models.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class ControladorUrgencias {

    private final IRepositorioPacientes DBPacientes;
    private final PriorityQueue<Ingreso> listaEspera;
    private final ArrayList<Ingreso> historicoEspera;

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
        this.historicoEspera = new ArrayList<>();
    }

    private boolean pacienteEstaIngresado(Paciente paciente) {
        boolean resultado = false;
        for (Ingreso ingreso : historicoEspera) {
            if (ingreso.getPaciente().equals(paciente) && ingreso.getEstado() != EstadoIngreso.FINALIZADO) {
                resultado = true;
                break;
            }
        }
        return resultado;
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

        if (pacienteEstaIngresado(paciente)) {
            throw new PacienteYaIngresado("El paciente ya se encuentra ingresado");
        }

        Ingreso ingreso = new Ingreso(paciente, enfermera, informe, emergencia, temperatura, frecuenciaCardiaca, frecuenciaRespiratoria, tensionArterial);

        listaEspera.offer(ingreso);
        historicoEspera.add(ingreso);
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

        if (pacienteEstaIngresado(paciente)) {
            throw new PacienteYaIngresado("El paciente ya se encuentra ingresado");
        }

        Ingreso ingreso = new Ingreso(paciente, enfermera, informe, emergencia, temperatura, frecuenciaCardiaca, frecuenciaRespiratoria, tensionArterial);
        ingreso.setFechaIngreso(fechaIngreso);

        listaEspera.offer(ingreso);
        historicoEspera.add(ingreso);
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

    public Ingreso reclamarIngreso(Medico medico) throws ListaDeEsperaVacia {
        Ingreso ingreso = listaEspera.poll();
        if (ingreso == null) {
            throw new ListaDeEsperaVacia("No hay pacientes en la lista de espera");
        }
        ingreso.setAtencion(new Atencion(medico));
        ingreso.setEstado(EstadoIngreso.EN_PROCESO);
        historicoEspera.addFirst(ingreso);
        return ingreso;
    }
}


