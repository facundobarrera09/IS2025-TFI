package org.domain.models;

import java.util.UUID;

public class Enfermera {
    private UUID uuid;
    private String nombre;
    private String apellido;

    public Enfermera(UUID uuid, String nombre, String apellido){
        this.uuid = uuid;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Enfermera(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }
}
