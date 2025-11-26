package org.domain.models;

public class Paciente {
    private String cuit;
    private String apellido;
    private String nombre;
    private Afiliacion afiliacion;
    private Domicilio domicilio;

    private void validarDatos(String cuit, String apellido, String nombre) {
        if (cuit == null || cuit.isEmpty()) {
            throw new IllegalArgumentException("CUIL no puede estar vacio");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new IllegalArgumentException("Apellido no puede estar vacio");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede estar vacio");
        }
    }

    public Paciente(String CUIT, String apellido, String nombre, Afiliacion afiliacion) {
        validarDatos(CUIT, apellido, nombre);
        this.cuit = CUIT;
        this.nombre = nombre;
        this.apellido = apellido;
        this.afiliacion = afiliacion;
    }

    public Paciente(String CUIT, String apellido, String nombre, Domicilio domicilio, Afiliacion afiliacion) {
        validarDatos(CUIT, apellido, nombre);
        this.cuit = CUIT;
        this.nombre = nombre;
        this.apellido = apellido;
        this.domicilio = domicilio;
        this.afiliacion = afiliacion;
    }

    public Paciente(String CUIT, String apellido, String nombre, Domicilio domicilio) {
        validarDatos(CUIT, apellido, nombre);
        this.cuit = CUIT;
        this.nombre = nombre;
        this.apellido = apellido;
        this.domicilio = domicilio;
    }

    public String getCuit() {
        return cuit;
    }
    public StringgetApellido() {
        return apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public Afiliacion getAfiliacion() {
        return afiliacion;
    }

}
