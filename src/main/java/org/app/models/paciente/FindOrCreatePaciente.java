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
}
