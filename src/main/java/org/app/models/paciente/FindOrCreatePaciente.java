package org.app.models.paciente;

import org.domain.models.Afiliacion;
import org.domain.models.Domicilio;

public class FindOrCreatePaciente {
    private String cuit;
    private String apellido;
    private String nombre;
    private Afiliacion afiliacion;
    private Domicilio domicilio;

    public FindOrCreatePaciente(String cuit, String apellido, String nombre, Domicilio domicilio) {
        this.cuit = cuit;
        this.apellido = apellido;
        this.nombre = nombre;
        this.domicilio = domicilio;
    }
    public FindOrCreatePaciente(){

    }
    public String getCuit() {
        return cuit;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setCuit(String cuit) {this.cuit = cuit;}
    public void setApellido(String apellido) {this.apellido = apellido;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public void setDomicilio(Domicilio domicilio) {this.domicilio = domicilio;}
}
