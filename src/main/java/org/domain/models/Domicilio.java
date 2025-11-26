package org.domain.models;

public class Domicilio {
    private String calle;
    private String numero;
    private String localidad;

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
}
