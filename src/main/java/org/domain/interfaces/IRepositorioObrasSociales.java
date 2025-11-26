package org.domain.interfaces;

import org.domain.errors.ElementNotFound;
import org.domain.models.ObraSocial;

import java.util.List;

public interface IRepositorioObrasSociales {
    public List<ObraSocial> obtenerObrasSociales();
    public ObraSocial obtenerObraSocialPorNombre(String nombre) throws ElementNotFound;
}
