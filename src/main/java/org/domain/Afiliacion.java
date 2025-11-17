package org.domain;

import org.app.errors.InvalidInsurance;
import org.app.interfaces.IRepositorioAfiliaciones;
import org.app.interfaces.IRepositorioObrasSociales;

import java.util.List;

public class Afiliacion {
    private ObraSocial obraSocial;
    private String numeroAfiliado;

    private IRepositorioAfiliaciones repoAfiliaciones;

    public Afiliacion(ObraSocial obraSocial, String numeroAfiliado) {
        if (obraSocial == null || numeroAfiliado == null) {
            throw new IllegalArgumentException();
        }
        this.obraSocial = obraSocial;
        this.numeroAfiliado = numeroAfiliado;
    }

    public Afiliacion(IRepositorioAfiliaciones repoAfiliaciones, ObraSocial obraSocial, String numeroAfiliado) {
        if (repoAfiliaciones == null || obraSocial == null || numeroAfiliado == null) {
            throw new IllegalArgumentException();
        }

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

        this.repoAfiliaciones = repoAfiliaciones;
        this.obraSocial = obraSocial;
        this.numeroAfiliado = numeroAfiliado;
    }

    public String getNumeroAfiliado() {
        return numeroAfiliado;
    }

    public void setNumeroAfiliado(String numeroAfiliado) {
        this.numeroAfiliado = numeroAfiliado;
    }

    public ObraSocial getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(ObraSocial obraSocial) {
        this.obraSocial = obraSocial;
    }
}
