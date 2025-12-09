package org.app.repos;

import org.domain.interfaces.IRepositorioEnfermeras;
import org.domain.models.Enfermera;

import java.util.*;

public class RepoEnfermeras implements IRepositorioEnfermeras {

    Map<UUID, Enfermera> enfermeras = new HashMap<>();

    public RepoEnfermeras() {
        UUID uuid = UUID.fromString("4c300fec-ed8f-4365-ac77-3d5b70a4e990");
        enfermeras.put(uuid, new Enfermera(uuid, "Claudia", "Gonzales"));
    }

    @Override
    public Map<UUID, Enfermera> obtenerEnfermeras() {
        return enfermeras;
    }

    @Override
    public Optional<Enfermera> obtenerEnfermera(UUID id) {
        return Optional.of(enfermeras.get(id));
    }
}
