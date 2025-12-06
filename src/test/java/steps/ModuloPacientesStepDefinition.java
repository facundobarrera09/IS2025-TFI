package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.*;

import mock.DBPruebaEnMemoria;
import mock.RepoAfiliacionesMemoria;
import mock.RepoObrasSociales;
import org.domain.models.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ModuloPacientesStepDefinition {

    private final DBPruebaEnMemoria repoPacientes;
    private final RepoAfiliacionesMemoria repoAfiliaciones;
    private final RepoObrasSociales repoObrasSociales;
    private Enfermera enfermera;

    private Exception excepcionCapturada;

    public ModuloPacientesStepDefinition(){
        this.repoPacientes = new DBPruebaEnMemoria();
        this.repoAfiliaciones = new RepoAfiliacionesMemoria();
        this.repoObrasSociales = new RepoObrasSociales();
    }

    @Given("que la enfermera esta registrada en urgencias:")
    public void queLaEnfermeraEstaRegistrada(List<Map<String, String>> tabla) {
        String nombre = tabla.getFirst().get("nombre");
        String apellido = tabla.getFirst().get("apellido");

        enfermera = new Enfermera(nombre, apellido);
    }

    @When("se ingresa el siguiente paciente")
    public void seIngresaElSiguientePaciente(List<Map<String, String>> tabla) {
        String cuil = tabla.getFirst().get("CUIL");
        String apellido = tabla.getFirst().get("Apellido");
        String nombre = tabla.getFirst().get("Nombre");
        String calle = tabla.getFirst().get("Calle");
        String numero = tabla.getFirst().get("Numero");
        String localidad = tabla.getFirst().get("Localidad");
        String nombreObraSocial = tabla.getFirst().get("Obra social");
        String numeroAfiliado = tabla.getFirst().get("Numero de afiliado");

        try {
            Domicilio domicilio = new Domicilio(calle, numero, localidad);
            if (nombreObraSocial != null || numeroAfiliado != null) {
                ObraSocial obraSocial = repoObrasSociales.obtenerObraSocialPorNombre(nombreObraSocial);
                Afiliacion afiliacion = new Afiliacion(repoAfiliaciones, obraSocial, numeroAfiliado);
                Paciente nuevoPaciente = new Paciente(cuil, apellido, nombre, domicilio, afiliacion);
                repoPacientes.guardarPaciente(nuevoPaciente);
            }
            else {
                Paciente nuevoPaciente = new Paciente(cuil, apellido, nombre, domicilio);
                repoPacientes.guardarPaciente(nuevoPaciente);
            }

        }
        catch (Exception e) {
            this.excepcionCapturada = e;
        }
    }

    @Then("el paciente con CUIL {string} queda registrado")
    public void elPacienteQuedaRegistrado(String cuil) {
        Optional<Paciente> paciente = repoPacientes.buscarPacientePorCuil(cuil);
        assertThat(paciente).isPresent();
    }

    @Given("existen las siguientes afiliaciones")
    public void existenLasSiguientesAfiliaciones(List<Map<String, String>> tabla) {
        try {
            List<Afiliacion> afiliaciones = new ArrayList<>();

            for (Map<String, String> row : tabla) {
                String nombreObraSocial = row.get("Obra social");
                String numeroAfiliado = row.get("Numero de afiliado");

                System.out.println("añadiendo afiliado " + nombreObraSocial + " " + numeroAfiliado);

                ObraSocial obraSocial = repoObrasSociales.obtenerObraSocialPorNombre(nombreObraSocial);
                afiliaciones.add(new Afiliacion(obraSocial, numeroAfiliado));
            }

            repoAfiliaciones.inicilizarDB(afiliaciones);
        }
        catch (Exception e) {
            this.excepcionCapturada = e;
        }

    }

    @Then("se muestra un error indicando {string}")
    public void seMuestraUnMensajeDeError(String expectedError) {
        assertThat(excepcionCapturada)
                .as("No se capturó ningun excepción")
                .isNotNull()
                .hasMessage(expectedError);
    }

    @Given("exinten las siguientes obras sociales")
    public void exintenLasSiguientesObrasSociales(List<Map<String, String>> tabla) {
        List<ObraSocial> obrasSociales = new ArrayList<>();
        for (Map<String, String> row : tabla) {
            String nombreObraSocial = row.get("Obra social");
            obrasSociales.add(new ObraSocial(nombreObraSocial));
        }
        repoObrasSociales.inicializar(obrasSociales);
    }

}
