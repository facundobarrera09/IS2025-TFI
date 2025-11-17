package org.domain;

public enum NivelEmergencia {
    CRITICA("Critica",5*60, 50),
    EMERGENCIA("Emergencia",30*60, 40),
    URGENCIA("Urgencia", 60*60, 30),
    URGENCIA_MENOR("Urgencia Menor", 2*60*60, 20),
    SIN_URGENCIA("Sin Urgencia", 4*60*60, 10);

    final String nombre;
    final Integer jerarquia;
    final Integer tiempoMaximoDeEsperaEnSeg;

    NivelEmergencia(String nombre, Integer tiempoMaximoDeEsperaEnSeg, Integer jerarquia) {
        this.nombre = nombre;
        this.tiempoMaximoDeEsperaEnSeg = tiempoMaximoDeEsperaEnSeg;
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

    public Integer getTiempoMaximoDeEsperaEnSeg() {
        return tiempoMaximoDeEsperaEnSeg;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
