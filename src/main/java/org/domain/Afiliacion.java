package org.domain;

public class Afiliacion {
    private String obraSocial;
    private String numeroAfiliado;

    public Afiliacion(String obraSocial, String numeroAfiliado) {
        this.obraSocial = obraSocial;
        this.numeroAfiliado = numeroAfiliado;
    }

    public String getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }

    public String getNumeroAfiliado() {
        return numeroAfiliado;
    }

    public void setNumeroAfiliado(String numeroAfiliado) {
        this.numeroAfiliado = numeroAfiliado;
    }
}
