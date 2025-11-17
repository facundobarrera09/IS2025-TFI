package org.app.interfaces;

import org.app.errors.ElementNotFound;
import org.domain.ObraSocial;

import java.util.List;
import java.util.Optional;

public interface IRepositorioObrasSociales {
    public List<ObraSocial> obtenerObrasSociales();
    public ObraSocial obtenerObraSocialPorNombre(String nombre) throws ElementNotFound;
}
