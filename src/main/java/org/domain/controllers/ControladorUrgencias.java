package org.domain.controllers;

import org.domain.errors.ListaDeEsperaVacia;
import org.domain.errors.PacienteYaIngresado;
import org.domain.errors.SinIngresoEnProceso;
import org.domain.interfaces.IRepositorioPacientes;
import org.domain.models.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class ControladorUrgencias {

    private final IRepositorioPacientes DBPacientes;
    private final PriorityQueue<Ingreso> listaEspera;
    private final ArrayList<Ingreso> historicoIngresos;

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
        this.historicoIngresos = new ArrayList<>();
    }

    private boolean pacienteEstaIngresado(Paciente paciente) {
        boolean resultado = false;
        for (Ingreso ingreso : historicoIngresos) {
            if (ingreso.getPaciente().equals(paciente) && ingreso.getEstado() != EstadoIngreso.FINALIZADO) {
                resultado = true;
                break;
            }
        }
        return resultado;
    }

    private Ingreso ingresoEnProgresoDeMedico(Medico medico) {
        Ingreso ingresoEnProgreso = null;

        for (Ingreso ingreso : historicoIngresos) {
            if (ingreso.getEstado() == EstadoIngreso.EN_PROCESO && ingreso.getAtencion() != null) {
                if (ingreso.getAtencion().getMedico().equals(medico)) {
                    ingresoEnProgreso = ingreso;
                    break;
                }
            }
        }

        return ingresoEnProgreso;
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
        historicoIngresos.add(ingreso);
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
        historicoIngresos.add(ingreso);
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
        if (medico == null) {
            throw new IllegalArgumentException("medico no puede ser nulo");
        }

        Ingreso ingresoEnProgreso = ingresoEnProgresoDeMedico(medico);
        if (ingresoEnProgreso != null) {
            return ingresoEnProgreso;
        }

        Ingreso ingreso = listaEspera.poll();
        if (ingreso == null) {
            throw new ListaDeEsperaVacia("No hay pacientes en la lista de espera");
        }
        ingreso.setAtencion(new Atencion(medico));
        ingreso.setEstado(EstadoIngreso.EN_PROCESO);
        return ingreso;
    }

    public void registrarInforme(Medico medico, String informe) {
        if (medico == null) {
            throw new IllegalArgumentException("medico no puede ser nulo");
        }
        if (informe == null || informe.isEmpty()) {
            throw new IllegalArgumentException("informe no puede ser nulo");
        }

        Ingreso ingreso = ingresoEnProgresoDeMedico(medico);
        if (ingreso == null) {
            throw new SinIngresoEnProceso("El medico no tiene ingresos en proceso.");
        }

        ingreso.getAtencion().setInforme(informe);
        ingreso.setEstado(EstadoIngreso.FINALIZADO);

        System.out.println("Historico de ingresos:");
        for (Ingreso i : historicoIngresos) {
            System.out.println(" - CUIT: " + i.getPaciente().getCuit()
                    + ", Fecha de entrada: " + i.getFechaIngreso().toString()
                    + ", Informe de entrada: " + i.getInforme()
                    + ", Estado: " + i.getEstado() +
                    ((i.getAtencion() != null) ?
                    ", Médico: " + i.getAtencion().getMedico().getMatricula() +
                    ", Informe: " + i.getAtencion().getInforme()
                    : "")
            );
        }
    }
}


