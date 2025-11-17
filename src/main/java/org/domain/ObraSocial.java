package org.domain;

import java.util.UUID;

public class ObraSocial {
    private String uuid;
    private String nombre;

    public ObraSocial(String uuid, String nombre) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID no puede ser nulo");
        }

        try {
            UUID.fromString(uuid);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UUID inválido");
        }

        this.uuid = uuid;
        this.nombre = nombre;
    }
    public ObraSocial(String nombre) {
        this.nombre = nombre;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
