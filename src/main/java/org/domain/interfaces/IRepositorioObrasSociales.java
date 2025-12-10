package org.domain.interfaces;

import org.domain.errors.ElementoNoEncontrado;
import org.domain.models.ObraSocial;

import java.util.List;

public interface IRepositorioObrasSociales {
    public List<ObraSocial> obtenerObrasSociales();
    public ObraSocial obtenerObraSocialPorNombre(String nombre) throws ElementoNoEncontrado;
}
