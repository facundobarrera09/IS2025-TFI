package org.domain;

public enum NivelEmergencia {
    CRITICA("Critica", 50),
    EMERGENCIA("Emergencia", 40),
    URGENCIA("Urgencia", 30),
    URGENCIA_MENOR("Urgencia Menor", 20),
    SIN_URGENCIA("Sin Urgencia", 10);

    final String nombre;
    final Integer jerarquia;

    NivelEmergencia(String nombre, Integer jerarquia) {
        this.nombre = nombre;
        this.jerarquia = jerarquia;
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

    /**
     * Si this.jerarquia > nivel.jerarquia, devuelve un entero positivo.
     * Si this.jerarquia = nivel.jerarquia, devuelve cero.
     * Si this.jerarquia < nivel.jerarquia, devuelve un entero negativo.
     * */
    public Integer compararCon(NivelEmergencia nivel) {
        return this.jerarquia - nivel.jerarquia;
    }

    public Integer getJerarquia() {
        return jerarquia;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
