package org.domain;

import java.util.ArrayList;
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

    public TensionArterial(String tensionArterial) {
        if (tensionArterial == null || tensionArterial.isEmpty()) {
            throw new IllegalArgumentException("Tensión Arterial no puede ser nulo");
        }

        List<Float> tensionArterialList = Arrays.stream(tensionArterial.split("/"))
                .map(Float::parseFloat)
                .toList();

        if (tensionArterialList.size() != 2) {
            throw new IllegalArgumentException("Tensión Arterial debe estar en el formato 'frecuenciaSistolica/frecuenciaDiastolica'");
        }

        this.frecuenciaSistolica = tensionArterialList.get(0);
        this.frecuenciaDiastolica = tensionArterialList.get(1);
    }

    public float getFrecuenciaDiastolica() {
        return frecuenciaDiastolica;
    }

    public float getFrecuenciaSistolica() {
        return frecuenciaSistolica;
    }
}
