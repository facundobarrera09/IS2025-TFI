package mock;

import org.domain.interfaces.IRepositorioAfiliaciones;
import org.domain.models.Afiliacion;

import java.util.ArrayList;
import java.util.List;

public class RepoAfiliacionesMemoria implements IRepositorioAfiliaciones {

    List<Afiliacion> afiliaciones = new ArrayList<>();

    @Override
    public List<Afiliacion> obtenerAfilicionesPorNumeroAfiliado(String numeroDeAfiliado) {
        return afiliaciones.stream()
                .filter(afiliacion -> afiliacion.getNumeroAfiliado().equals(numeroDeAfiliado))
                .toList();
    }

    public void inicilizarDB(List<Afiliacion> afiliaciones) {
        this.afiliaciones = afiliaciones;
    }
}
