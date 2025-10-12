package org.domain;

public class Paciente {
    private String cuit;
    private String apellidoPaciente;
    private String nombrePaciente;
    private String obraSocial;

    public Paciente(String cuit, String apellidoPaciente, String nombrePaciente, String obraSocial) {
        this.cuit = cuit;
        this.nombrePaciente = nombrePaciente;
        this.apellidoPaciente = apellidoPaciente;
        this.obraSocial = obraSocial;
    }

    public String getCuit() {
        return cuit;
    }

}
