package org.domain.models;

public class Domicilio {
    private String calle;
    private String numero;
    private String localidad;

    public Domicilio() {

    }
    public Domicilio(String calle, String numero, String localidad) {
        if (calle == null || calle.isEmpty()) {
            throw new IllegalArgumentException("Calle no puede estar vacio");
        }
        if (numero == null || numero.isEmpty()) {
            throw new IllegalArgumentException("Numero no puede estar vacio");
        }
        if (localidad == null || localidad.isEmpty()) {
            throw new IllegalArgumentException("Localidad no puede estar vacio");
        }

        this.calle = calle;
        this.numero = numero;
        this.localidad = localidad;
    }


    public String getLocalidad() {
        return localidad;
    }
    public String getCalle() { return calle;}
    public String getNumero() { return numero;}

    public void setCalle(String calle) { this.calle = calle;}
    public void setNumero(String numero) { this.numero = numero;}
    public void setLocalidad(String localidad) { this.localidad = localidad;}

}
