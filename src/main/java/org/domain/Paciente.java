package org.domain;


public class Paciente {
    private String cuit;
    private String apellido;
    private String nombre;
    private Afiliacion obraSocial;
    private Domicilio domicilio;

    public Paciente(String cuit, String apellidoPaciente, String nombrePaciente, String obraSocial) {
        this.cuit = cuit;
        this.nombre = nombrePaciente;
        this.apellido = apellidoPaciente;
        //this.obraSocial = RepoObrasSociales.getByName(obraSocial);
    }

    public Paciente(String CUIT, String apellido, String nombre, Domicilio domicilio, Afiliacion obraSocial) {
        this.cuit = CUIT;
        this.nombre = nombre;
        this.apellido = apellido;
        this.obraSocial = obraSocial;
        this.domicilio = domicilio;
    }

    public String getCuit() {
        return cuit;
    }

}
