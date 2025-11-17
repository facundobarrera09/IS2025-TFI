package mock;

import org.app.errors.ElementNotFound;
import org.app.interfaces.IRepositorioObrasSociales;
import org.domain.ObraSocial;

import java.util.*;

public class RepoObrasSociales implements IRepositorioObrasSociales {

    List<ObraSocial> obrasSociales = new ArrayList<>();

    @Override
    public List<ObraSocial> obtenerObrasSociales() {
        return obrasSociales;
    }

    @Override
    public ObraSocial obtenerObraSocialPorNombre(String nombre) throws ElementNotFound {
        for (ObraSocial obraSocial : obrasSociales) {
            if (obraSocial.getNombre().equals(nombre)) {
                return obraSocial;
            }
        }
        throw new ElementNotFound("Obra social inexistente");
    }

    public void inicializar(List<ObraSocial> obrasSociales) {
        this.obrasSociales = obrasSociales;
    }
}
