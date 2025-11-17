package org.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DomicilioTest {

    //PREPARACIÓN

    private final String CALLE_VALIDA = "Siempreverde";
    private final String NUMERO_VALIDO = "123";
    private final String LOCALIDAD_VALIDA = "Nueva concepción";

    // TEST DE ÉXITO

    @Test
    void domicilioSeCreaCorrectamenteConDatosValidos() {
        // EJECUCIÓN y VERIFICACIÓN
        assertDoesNotThrow(() -> {
            new Domicilio(CALLE_VALIDA, NUMERO_VALIDO, LOCALIDAD_VALIDA);
        }, "No debería lanzar excepción con datos de domicilio válidos.");
    }

    // TEST DE FALLO

    @Test
    void lanzamientoDeExcepcionSiLaCalleEsNula() {
        // EJECUCIÓN y VERIFICACIÓN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Domicilio(null, NUMERO_VALIDO, LOCALIDAD_VALIDA);
        });
        assertEquals("Calle no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiLaCalleEsVacia() {
        // EJECUCIÓN y VERIFICACIÓN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Domicilio("", NUMERO_VALIDO, LOCALIDAD_VALIDA);
        });
        assertEquals("Calle no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiElNumeroEsNulo() {
        // EJECUCIÓN y VERIFICACIÓN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Domicilio(CALLE_VALIDA, null, LOCALIDAD_VALIDA);
        });
        assertEquals("Numero no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiElNumeroEsVacio() {
        // EJECUCIÓN y VERIFICACIÓN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Domicilio(CALLE_VALIDA, "", LOCALIDAD_VALIDA);
        });
        assertEquals("Numero no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiLaLocalidadEsNula() {
        // EJECUCIÓN y VERIFICACIÓN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Domicilio(CALLE_VALIDA, NUMERO_VALIDO, null);
        });
        assertEquals("Localidad no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiLaLocalidadEsVacia() {
        // EJECUCIÓN y VERIFICACIÓN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Domicilio(CALLE_VALIDA, NUMERO_VALIDO, "");
        });
        assertEquals("Localidad no puede estar vacio", exception.getMessage());
    }
}
