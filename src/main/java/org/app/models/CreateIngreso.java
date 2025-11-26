package org.app.models;

import org.app.models.paciente.FindOrCreatePaciente;
import org.app.models.shared.FindByUUID;

public class CreateIngreso {
    private FindOrCreatePaciente paciente;
    private FindByUUID enfermera;
    private String informe;
    private Float temperatura;
    private String nivel;
    private Float frecuenciaCardiaca;
    private Float frecuenciaRespiratoria;
    private String tensionArterial;

    public FindOrCreatePaciente getPaciente() {
        return paciente;
    }

    public FindByUUID getEnfermera() {
        return enfermera;
    }

    public String getInforme() {
        return informe;
    }

    public Float getTemperatura() {
        return temperatura;
    }

    public String getNivel() {
        return nivel;
    }

    public Float getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public Float getFrecuenciaRespiratoria() {
        return frecuenciaRespiratoria;
    }

    public String getTensionArterial() {
        return tensionArterial;
    }
}
