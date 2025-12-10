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


    @Mock
    private Afiliacion afiliacionMock;
    @Mock
    private Domicilio domicilioMock;


    private final String CUIT_VALIDO = "20-12345678-1";
    private final String APELLIDO_VALIDO = "Villagra";
    private final String NOMBRE_VALIDO = "Mauro";



    @Test
    void pacienteSeCreaCorrectamenteConAfiliacion() {


        Paciente paciente = assertDoesNotThrow(() -> new Paciente(
                CUIT_VALIDO,
                APELLIDO_VALIDO,
                NOMBRE_VALIDO,
                domicilioMock,
                afiliacionMock
        ), "La creación del paciente con todos los datos debería ser exitosa.");


        assertNotNull(paciente, "El objeto paciente no debe ser nulo después de la ejecución.");
        assertEquals(CUIT_VALIDO, paciente.getCuit(), "El CUIT debe ser asignado correctamente.");

        assertNotNull(domicilioMock);
        assertNotNull(afiliacionMock);
    }


    @Test
    void lanzamientoDeExcepcionSiElCUILesNulo() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Paciente(null, APELLIDO_VALIDO, NOMBRE_VALIDO, domicilioMock, afiliacionMock);
        });

        assertEquals("CUIL no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiElCUILesVacio() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Paciente("", APELLIDO_VALIDO, NOMBRE_VALIDO, domicilioMock, afiliacionMock);
        });
        assertEquals("CUIL no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiElApellidoEsVacio() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Paciente(CUIT_VALIDO, "", NOMBRE_VALIDO, domicilioMock, afiliacionMock);
        });
        assertEquals("Apellido no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiElNombreEsNulo() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Paciente(CUIT_VALIDO, APELLIDO_VALIDO, null, domicilioMock, afiliacionMock);
        });
        assertEquals("Nombre no puede estar vacio", exception.getMessage());
    }

    @Test
    void lanzamientoDeExcepcionSiElNombreEsVacio() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Paciente(CUIT_VALIDO, APELLIDO_VALIDO, "", domicilioMock, afiliacionMock);
        });
        assertEquals("Nombre no puede estar vacio", exception.getMessage());
    }
}