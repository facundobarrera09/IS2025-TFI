package org.domain;

public class ObraSocial {
    private String uuid;
    private String nombre;

    public ObraSocial(String uuid, String nombre) {
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
