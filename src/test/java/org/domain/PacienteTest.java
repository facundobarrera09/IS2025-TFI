package org.domain;

import org.domain.models.Afiliacion;
import org.domain.models.Domicilio;
import org.domain.models.Paciente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PacienteTest {

    //PREPARACIÓN
    @Mock
    private Afiliacion afiliacionMock;
    @Mock
    private Domicilio domicilioMock;

    //datos validos
    private final String CUIT_VALIDO = "20-12345678-1";
    private final String APELLIDO_VALIDO = "Villagra";
    private final String NOMBRE_VALIDO = "Mauro";

    //TESTS DE EXITO

    @Test
    void pacienteSeCreaCorrectamenteConAfiliacion() {

        //PREPARACIÓN arriba

        // EJECUCIÓN
        Paciente paciente = assertDoesNotThrow(() -> new Paciente(
                CUIT_VALIDO,
                APELLIDO_VALIDO,
                NOMBRE_VALIDO,
                domicilioMock,
                afiliacionMock
        ), "La creación del paciente con todos los datos debería ser exitosa.");

        // VERIFICACIÓN

        // Verificación de la integridad del objeto
        assertNotNull(paciente, "El objeto paciente no debe ser nulo después de la ejecución.");
        assertEquals(CUIT_VALIDO, paciente.getCuit(), "El CUIT debe ser asignado correctamente.");

        // Verificación de Interacción (Asegurar que los Mocks fueron pasados y usados)
        assertNotNull(domicilioMock);
        assertNotNull(afiliacionMock);
    }

    // TESTS DE FALLO

    @Test
    void lanzamientoDeExcepcionSiElCUILesNulo() {
        // PREPARACIÓN arriba

        // EJECUCIÓN y VERIFICACIÓN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Paciente(null, APELLIDO_VALIDO, NOMBRE_VALIDO, domicilioMock, afiliacionMock);
        });

        // Verificación del mensaje con la validacion de datos de la clase paciente
        assertEquals("CUIL no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiElCUILesVacio() {
        // PREPARACIÓN arriba

        // EJECUCIÓN y VERIFICACIÓN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Paciente("", APELLIDO_VALIDO, NOMBRE_VALIDO, domicilioMock, afiliacionMock);
        });
        assertEquals("CUIL no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiElApellidoEsVacio() {
        // PREPARACIÓN arriba

        // EJECUCIÓN y VERIFICACIÓN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Paciente(CUIT_VALIDO, "", NOMBRE_VALIDO, domicilioMock, afiliacionMock);
        });
        assertEquals("Apellido no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiElNombreEsNulo() {
        // PREPARACIÓN arriba

        // EJECUCIÓN y VERIFICACIÓN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Paciente(CUIT_VALIDO, APELLIDO_VALIDO, null, domicilioMock, afiliacionMock);
        });
        assertEquals("Nombre no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiElNombreEsVacio() {
        // PREPARACIÓN

        // EJECUCIÓN y VERIFICACIÓN
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Paciente(CUIT_VALIDO, APELLIDO_VALIDO, "", domicilioMock, afiliacionMock);
        });
        assertEquals("Nombre no puede estar vacio", exception.getMessage());
    }
}