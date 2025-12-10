package org.domain.models;

public class Atencion {
    private Medico medico;
    private String informe;

    public Atencion(Medico medico) {
        this.medico = medico;
    }

    public Atencion(Medico medico, String informe) {
        this.medico = medico;
        this.informe = informe;
    }

    public Medico getMedico() {
        return medico;
    }

    public String getInforme() {
        return informe;
    }

    public void setInforme(String informe) {
        this.informe = informe;
    }
}
