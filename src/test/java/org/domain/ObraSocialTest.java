package org.domain;

import org.domain.models.ObraSocial;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class ObraSocialTest {
    @Test
    public void uuidYNombreValido() {
        String uuid = UUID.randomUUID().toString();
        String nombre = "Subsidio de salud";

        ObraSocial obraSocial = new ObraSocial(uuid, nombre);
    }

    @Test
    public void soloNombre(){
        new ObraSocial("Subsidio de salud");
    }

    @Test
    public void uuidInvalidoTiraError() {
        String uuid = "uuid inválido";
        String nombre = "OSPE";

        try {
            new ObraSocial(uuid, nombre);
            fail();
        }
        catch (Exception e) {
            assertInstanceOf(IllegalArgumentException.class, e);
            assertEquals("UUID inválido", e.getMessage());
        }
    }

    @Test
    public void uuidVacioTiraError() {
        String uuid = "";
        String nombre = "OSPE";

        try {
            new ObraSocial(uuid, nombre);
            fail();
        } catch (Exception e) {
            assertInstanceOf(IllegalArgumentException.class, e);
            assertEquals("UUID no puede estar vacio", e.getMessage());
        }
    }

    @Test
    public void uuidNuloTiraError() {
        String nombre = "OSPE";
        try {
            new ObraSocial(null, nombre);
            fail();
        }
        catch (Exception e) {
            assertInstanceOf(IllegalArgumentException.class, e);
            assertEquals("UUID no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void nombreVacioTiraError() {
        String nombre = "";
        String uuid = UUID.randomUUID().toString();
        try {
            new ObraSocial(uuid, nombre);
            fail();
        }
        catch (Exception e) {
            assertInstanceOf(IllegalArgumentException.class, e);
            assertEquals("Nombre no puede estar vacio", e.getMessage());
        }
    }

    @Test
    public void nombreNuloTiraError() {
        String uuid = UUID.randomUUID().toString();
        try {
            new ObraSocial(uuid, null);
            fail();
        }
        catch (Exception e) {
            assertInstanceOf(IllegalArgumentException.class, e);
            assertEquals("Nombre no puede ser nulo", e.getMessage());
        }
    }
}
