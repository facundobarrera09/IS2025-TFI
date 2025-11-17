package org.domain;

import mock.RepoAfiliacionesMemoria;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AfiliacionTest {
    @Mock
    RepoAfiliacionesMemoria repoAfiliacionesMemoria;

    @Test
    public void datosValidos() {
        ObraSocial obraSocial = mock(ObraSocial.class);
        String numeroAfiliado = "123456789";

        Afiliacion afiliacion = new Afiliacion(obraSocial, numeroAfiliado);

        assertNotNull(afiliacion);
    }

    @Test
    public void sinObraSocial() {
        ObraSocial obraSocial = null;
        String numeroAfiliado = "123456789";

        assertThrows(IllegalArgumentException.class, () -> new Afiliacion(obraSocial, numeroAfiliado));
    }

    @Test
    public void afiliacionExisteEnRepo() {
        // SETUP
        ObraSocial obraSocial = mock(ObraSocial.class);
        when(obraSocial.getNombre()).thenReturn("Subsidio de salud");

        ObraSocial obraSocial2 = mock(ObraSocial.class);
        when(obraSocial2.getNombre()).thenReturn("Mora");

        RepoAfiliacionesMemoria repoAfiliacionesMemoria = mock(RepoAfiliacionesMemoria.class);
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

}
