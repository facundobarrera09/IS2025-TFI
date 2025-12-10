package org.domain.models;

public enum Autoridad {
    MEDICO("médico"),
    ENFERMERO("enfermera");

    private final String nombre;

    Autoridad(String nombre) {
        this.nombre = nombre;
    }

    public static Autoridad buscarPorNombre(String nombre) {
        for (Autoridad autoridad : values()) {
            if (autoridad.nombre.equals(nombre)) {
                return autoridad;
            }
        }
        return null;
    }

    public String getNombre() {
        return nombre;
    }
}
