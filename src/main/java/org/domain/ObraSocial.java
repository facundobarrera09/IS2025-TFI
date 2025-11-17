package org.domain;

import java.util.UUID;

public class ObraSocial {
    private String uuid;
    private String nombre;

    public ObraSocial(String uuid, String nombre) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID no puede ser nulo");
        }
        if (uuid.isEmpty()) {
            throw new IllegalArgumentException("UUID no puede estar vacio");
        }
        if (nombre == null) {
            throw new IllegalArgumentException("Nombre no puede ser nulo");
        }
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede estar vacio");
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
        this(UUID.randomUUID().toString(), nombre);
    }

    public String getUuid() {
        return uuid;
    }

    public String getNombre() {
        return nombre;
    }
}
