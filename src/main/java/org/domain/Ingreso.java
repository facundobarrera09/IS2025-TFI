package org.domain;

import java.time.LocalDateTime;

public class Ingreso {
    Paciente paciente;
    Enfermera enfermera;
    LocalDateTime fechaIngreso;
    String informe;
    NivelEmergencia emergencia;
    EstadoIngreso estado;
    Float temperatura;
    Float frecuenciaCardiaca;
    Float frecuenciaRespiratoria;
    Float frecuenciaDiastolica;
    Float frecuenciaSistolica;

public Ingreso(Paciente paciente, Enfermera enfermera, String informe, NivelEmergencia emergencia,
               Float temperatura, Float frecuenciaCardiaca, Float frecuenciaRespiratoria, Float frecuenciaDiastolica,
               Float frecuenciaSistolica) {
    this.paciente = paciente;
    this.enfermera = enfermera;
    this.fechaIngreso = LocalDateTime.now();
    this.informe = informe;
    this.emergencia = emergencia;
    this.estado = EstadoIngreso.PENDIENTE;
    this.temperatura = temperatura;
    this.frecuenciaCardiaca = frecuenciaCardiaca;
    this.frecuenciaRespiratoria = frecuenciaRespiratoria;
    this.frecuenciaDiastolica = frecuenciaDiastolica;
    this.frecuenciaSistolica = frecuenciaSistolica;

}


    public String getCuilPaciente(){
        return this.paciente.getCuit();
    }
}
