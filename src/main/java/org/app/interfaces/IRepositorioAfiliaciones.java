package org.app.interfaces;

import org.domain.Afiliacion;

import java.util.List;

public interface IRepositorioAfiliaciones {
    public List<Afiliacion> obtenerAfilicionesPorNumeroAfiliado(String numeroDeAfiliado);

}
