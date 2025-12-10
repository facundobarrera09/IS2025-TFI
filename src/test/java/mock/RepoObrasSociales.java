package mock;

import org.domain.errors.ElementoNoEncontrado;
import org.domain.interfaces.IRepositorioObrasSociales;
import org.domain.models.ObraSocial;

import java.util.*;

public class RepoObrasSociales implements IRepositorioObrasSociales {

    List<ObraSocial> obrasSociales = new ArrayList<>();

    @Override
    public List<ObraSocial> obtenerObrasSociales() {
        return obrasSociales;
    }

    @Override
    public ObraSocial obtenerObraSocialPorNombre(String nombre) throws ElementoNoEncontrado {
        for (ObraSocial obraSocial : obrasSociales) {
            if (obraSocial.getNombre().equals(nombre)) {
                return obraSocial;
            }
        }
        throw new ElementoNoEncontrado("Obra social inexistente");
    }

    public void inicializar(List<ObraSocial> obrasSociales) {
        this.obrasSociales = obrasSociales;
    }
}
