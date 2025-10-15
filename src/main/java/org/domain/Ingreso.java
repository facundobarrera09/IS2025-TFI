package org.domain;

import java.time.LocalDateTime;

public class Ingreso {
    Paciente paciente;
    Enfermera enfermera;
    LocalDateTime fechaIngreso;
    String informe;
    NivelEmergencia nivelEmergencia;
    EstadoIngreso estado;
    Float temperatura;
    Float frecuenciaCardiaca;
    Float frecuenciaRespiratoria;
    TensionArterial tensionArterial;

    public Ingreso(Paciente paciente, Enfermera enfermera, String informe, NivelEmergencia nivelEmergencia,
                   Float temperatura, Float frecuenciaCardiaca, Float frecuenciaRespiratoria, TensionArterial tensionArterial) {
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente no puede ser nulo");
        }
        if (enfermera == null) {
            throw new IllegalArgumentException("Enferma no puede ser nulo");
        }
        if (informe == null) {
            throw new IllegalArgumentException("Informe no puede ser nulo");
        }
        if (nivelEmergencia == null) {
            throw new IllegalArgumentException("Nivel de emergencia no puede ser nulo");
        }
        //if (temperatura == null) {
        //    throw new IllegalArgumentException("Temperatura no puede ser nulo");
        //}
        if (frecuenciaCardiaca == null) {
            throw new IllegalArgumentException("Frecuencia cardíaca no puede ser nulo");
        }
        if (frecuenciaCardiaca < 0) {
            throw new IllegalArgumentException("La frecuencia cardiaca no puede ser negativa");
        }
        if (frecuenciaRespiratoria == null) {
            throw new IllegalArgumentException("Frecuencia respiratoria no puede ser nulo");
        }
        if (frecuenciaRespiratoria < 0) {
            throw new IllegalArgumentException("La frecuencia respiratoria no puede ser negativa");
        }
        if (tensionArterial == null) {
            throw new IllegalArgumentException("Tensión arterial no puede ser nulo");
        }

        this.paciente = paciente;
        this.enfermera = enfermera;
        this.fechaIngreso = LocalDateTime.now();
        this.informe = informe;
        this.nivelEmergencia = nivelEmergencia;
        this.temperatura = temperatura;
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.frecuenciaRespiratoria = frecuenciaRespiratoria;
        this.tensionArterial = tensionArterial;

        this.estado = EstadoIngreso.PENDIENTE;
    }

    public String getCuilPaciente(){
        return this.paciente.getCuit();
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Enfermera getEnfermera() {
        return enfermera;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public String getInforme() {
        return informe;
    }

    public NivelEmergencia getNivelEmergencia() {
        return nivelEmergencia;
    }

    public EstadoIngreso getEstado() {
        return estado;
    }

    public Float getTemperatura() {
        return temperatura;
    }

    public Float getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public Float getFrecuenciaRespiratoria() {
        return frecuenciaRespiratoria;
    }

    public TensionArterial getTensionArterial() {
        return tensionArterial;
    }
}
