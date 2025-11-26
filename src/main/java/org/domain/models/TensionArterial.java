package org.domain.models;

import java.util.Arrays;
import java.util.List;

public class TensionArterial {
    private float frecuenciaSistolica;
    private float frecuenciaDiastolica;

    public TensionArterial(Float frecuenciaSistolica, Float frecuenciaDiastolica) {
        if (frecuenciaSistolica == null || frecuenciaDiastolica == null) {
            throw new IllegalArgumentException("Tensión Arterial debe estar en el formato 'frecuenciaSistolica/frecuenciaDiastolica'");
        }

        this.frecuenciaSistolica = frecuenciaSistolica;
        this.frecuenciaDiastolica = frecuenciaDiastolica;
    }

    // "120/40", "120 40", "/40"
    public TensionArterial(String tensionArterial) {
        if (tensionArterial == null || tensionArterial.isEmpty()) {
            throw new IllegalArgumentException("Tensión arterial no puede ser nulo");
        }

        List<String> tensionArterialStringList = Arrays.stream(tensionArterial.split("/")).toList();

        if (tensionArterialStringList.size() != 2) {
            throw new IllegalArgumentException("Frecuencia diastólica no puede ser nulo");
        }

        if (tensionArterialStringList.get(0).isEmpty()) {
            throw new IllegalArgumentException("Frecuencia sistólica no puede ser nulo");
        }

        this.frecuenciaSistolica = Float.parseFloat(tensionArterialStringList.get(0));
        this.frecuenciaDiastolica = Float.parseFloat(tensionArterialStringList.get(1));
    }

    public float getFrecuenciaDiastolica() {
        return frecuenciaDiastolica;
    }

    public float getFrecuenciaSistolica() {
        return frecuenciaSistolica;
    }
}
