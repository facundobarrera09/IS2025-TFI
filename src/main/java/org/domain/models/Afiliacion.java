package org.domain.models;

import org.domain.errors.InvalidInsurance;
import org.domain.interfaces.IRepositorioAfiliaciones;

import java.util.List;

public class Afiliacion {
    private ObraSocial obraSocial;
    private String numeroAfiliado;

    private IRepositorioAfiliaciones repoAfiliaciones;

    public Afiliacion(ObraSocial obraSocial, String numeroAfiliado) {
        if (obraSocial == null || numeroAfiliado == null || numeroAfiliado.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.obraSocial = obraSocial;
        this.numeroAfiliado = numeroAfiliado;
    }

    public Afiliacion(IRepositorioAfiliaciones repoAfiliaciones, ObraSocial obraSocial, String numeroAfiliado) {
        if (repoAfiliaciones == null || obraSocial == null || numeroAfiliado == null || numeroAfiliado.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.repoAfiliaciones = repoAfiliaciones;
        this.obraSocial = obraSocial;
        this.numeroAfiliado = numeroAfiliado;

        validarAfiliacion(obraSocial, numeroAfiliado);
    }

    private void validarAfiliacion(ObraSocial obraSocial, String numeroAfiliado) {
        List<Afiliacion> afiliaciones = repoAfiliaciones.obtenerAfilicionesPorNumeroAfiliado(numeroAfiliado);
        boolean afiliacionEncontrada = false;
        for (Afiliacion afiliacion : afiliaciones) {
            if (afiliacion.getObraSocial().getNombre().equals(obraSocial.getNombre()) &&
                    afiliacion.getNumeroAfiliado().equals(numeroAfiliado)) {
                afiliacionEncontrada = true;
                break;
            }
        }
        if (!afiliacionEncontrada) {
            throw new InvalidInsurance("Paciente no afiliado a obra social");
        }
    }

    public String getNumeroAfiliado() {
        return numeroAfiliado;
    }

    public ObraSocial getObraSocial() {
        return obraSocial;
    }
}
