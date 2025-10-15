package org.domain;

public enum NivelEmergencia {
    CRITICA("Critica"),
    EMERGENCIA("Emergencia"),
    URGENCIA("Urgenica"),
    URGENCIA_MENOR("Urgencia Menor"),
    SIN_URGENCIA("Sin Urgencia");

    String nombre;

    NivelEmergencia(String nombre){
        this.nombre = nombre;
    }

    public boolean tieneNombre(String nombre){
        return this.nombre.equals(nombre);
    }

    public static NivelEmergencia buscarPorNombre(String nombre) {
        for (NivelEmergencia nivel : values()) {
            if (nivel.tieneNombre(nombre)) {
                return nivel;
            }
        }
        return null;
    }
}
