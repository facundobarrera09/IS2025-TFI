package org.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class ObraSocialTest {
    @Test
    public void uuidYNombreValido() {
        String uuid = UUID.randomUUID().toString();
        String nombre = "Subsidio de salud";

        ObraSocial obraSocial = new ObraSocial(uuid, nombre);

        assertNotNull(obraSocial);
    }

    @Test
    public void uuidInvalido() {
        String uuid = "uuid inválido";
        String nombre = "OSPE";

        try {
            new ObraSocial(uuid, nombre);
        }
        catch (Exception e) {
            assertInstanceOf(IllegalArgumentException.class, e);
            assertEquals("UUID inválido", e.getMessage());
        }
    }
}
