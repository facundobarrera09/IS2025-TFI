package steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.util.List;
import java.util.Map;

import mock.DBPruebaEnMemoria;
import org.app.ServicioUrgencia;
import org.domain.Enfermera;
import org.domain.Paciente;

public class ModuloUrgenciasStepDefinition {

    private Enfermera enfermera;
    private DBPruebaEnMemoria DBMockeada;
    private ServicioUrgencia servicioUrgencia;


    public ModuloUrgenciasStepDefinition(){
        this.DBMockeada = new DBPruebaEnMemoria();
        this.servicioUrgencia = new ServicioUrgencia(DBMockeada);
    }

    @Given("que la enfermera esta registrada:")
    public void queLaEnfermeraEstaRegistrada(List<Map<String, String>> tabla) {
      String nombre = tabla.get(0).get("nombre");
      String apellido = tabla.get(0).get("apellido");

      enfermera = new Enfermera(nombre, apellido);
    }

    @Given("que estan registrados los siguientes pacientes en el sistema:")
    public void queEstanRegistradosLosSiguientesPacientes(List<Map<String, String>> tabla) {
        for (Map<String, String> map : tabla) {
            String cuit = map.get("CUIT");
            String apellidoPaciente = map.get("Apellido Paciente");
            String nombrePaciente = map.get("Nombre Paciente");
            String obraSocial = map.get("Obra Social");

            Paciente paciente = new Paciente(cuit, apellidoPaciente, nombrePaciente, obraSocial);
        }

    }

    @When("ingresa a urgencias el siguiente paciente:")
    public void ingresaAUrgenciasElSiguientePaciente() {
    }

    @Then("la lista de espera esta ordenada por cuil de la siguiente manera:")
    public void laListaDeEsperaEstaOrdenadaPorCuil() {
    }
}
