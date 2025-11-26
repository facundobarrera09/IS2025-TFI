package org.domain.interfaces;

import org.domain.models.Afiliacion;

import java.util.List;

public interface IRepositorioAfiliaciones {
    public List<Afiliacion> obtenerAfilicionesPorNumeroAfiliado(String numeroDeAfiliado);

}
