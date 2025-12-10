package org.domain;

import org.domain.models.Domicilio;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DomicilioTest {


    private final String CALLE_VALIDA = "Siempreverde";
    private final String NUMERO_VALIDO = "123";
    private final String LOCALIDAD_VALIDA = "Nueva concepción";


    @Test
    void domicilioSeCreaCorrectamenteConDatosValidos() {
        assertDoesNotThrow(() -> {
            new Domicilio(CALLE_VALIDA, NUMERO_VALIDO, LOCALIDAD_VALIDA);
        }, "No debería lanzar excepción con datos de domicilio válidos.");
    }


    @Test
    void lanzamientoDeExcepcionSiLaCalleEsNula() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Domicilio(null, NUMERO_VALIDO, LOCALIDAD_VALIDA);
        });
        assertEquals("Calle no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiLaCalleEsVacia() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Domicilio("", NUMERO_VALIDO, LOCALIDAD_VALIDA);
        });
        assertEquals("Calle no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiElNumeroEsNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Domicilio(CALLE_VALIDA, null, LOCALIDAD_VALIDA);
        });
        assertEquals("Numero no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiElNumeroEsVacio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Domicilio(CALLE_VALIDA, "", LOCALIDAD_VALIDA);
        });
        assertEquals("Numero no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiLaLocalidadEsNula() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Domicilio(CALLE_VALIDA, NUMERO_VALIDO, null);
        });
        assertEquals("Localidad no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiLaLocalidadEsVacia() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Domicilio(CALLE_VALIDA, NUMERO_VALIDO, "");
        });
        assertEquals("Localidad no puede estar vacio", exception.getMessage());
    }
}
