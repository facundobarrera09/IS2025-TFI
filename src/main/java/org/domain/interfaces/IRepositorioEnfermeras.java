package org.domain.interfaces;

import org.domain.models.Enfermera;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface IRepositorioEnfermeras {
    public Map<UUID, Enfermera> obtenerEnfermeras();
    public Optional<Enfermera> obtenerEnfermera(UUID id);
}
