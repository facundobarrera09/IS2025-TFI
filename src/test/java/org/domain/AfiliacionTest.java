package org.domain;

import mock.RepoAfiliacionesMemoria;
import static org.mockito.Mockito.*;

import org.domain.errors.AfiliacionNoExistente;
import org.domain.models.Afiliacion;
import org.domain.models.ObraSocial;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AfiliacionTest {
    @Mock
    private RepoAfiliacionesMemoria repoAfiliacionesMemoria;

    @Mock
    private ObraSocial obraSocial;
    @Mock
    private ObraSocial obraSocial2;

    // Afiliacion(ObraSocial obraSocial, String numeroAfiliado)

    @Test
    public void datosValidos() {
        String numeroAfiliado = "123456789";

        Afiliacion afiliacion = new Afiliacion(obraSocial, numeroAfiliado);

        assertNotNull(afiliacion);
    }

    @Test
    public void sinObraSocialTiraError() {
        String numeroAfiliado = "123456789";

        assertThrows(IllegalArgumentException.class, () -> new Afiliacion(null, numeroAfiliado));
    }

    @Test
    public void sinNumeroDeAfiliadoTiraError() {
        assertThrows(IllegalArgumentException.class, () -> new Afiliacion(obraSocial, null));
    }

    @Test
    public void numeroDeAfiliadoVacioTiraError() {
        assertThrows(IllegalArgumentException.class, () -> new Afiliacion(obraSocial, ""));
    }

    // validarAfiliacion(ObraSocial obraSocial, String numeroAfiliado)

    @Test
    public void afiliacionExisteEnRepo() {
        // SETUP
        when(obraSocial.getNombre()).thenReturn("Subsidio de salud");
        when(obraSocial2.getNombre()).thenReturn("Mora");

        when(
                repoAfiliacionesMemoria.obtenerAfilicionesPorNumeroAfiliado(anyString())
        ).thenReturn(
                new ArrayList<Afiliacion>(List.of(new Afiliacion[]{
                        new Afiliacion(obraSocial, "1234"),
                        new Afiliacion(obraSocial2, "1234")
                }))
        );

        // EJECUCIÓN
        Afiliacion afiliacion = new Afiliacion(repoAfiliacionesMemoria, obraSocial2, "1234");

        // VERIFICACIÓN

        assertNotNull(afiliacion);
        verify(obraSocial, times(1)).getNombre();
        verify(obraSocial2, times(3)).getNombre();
    }

    @Test
    public void afiliacionNoExisteEnRepoTiraError() {
        when(repoAfiliacionesMemoria.obtenerAfilicionesPorNumeroAfiliado(anyString()))
                .thenReturn(new ArrayList<>());

        try {
            Afiliacion af = new Afiliacion(repoAfiliacionesMemoria, obraSocial, "1234");
            assertNull(af);
        }
        catch (AfiliacionNoExistente e) {
            assertInstanceOf(AfiliacionNoExistente.class, e);
            assertEquals("Paciente no afiliado a obra social", e.getMessage());
        }
    }

    @Test
    public void repoEsNuloTiraError() {
        assertThrows(IllegalArgumentException.class,
                () -> new Afiliacion(null, obraSocial, "1234")
        );
    }
}
