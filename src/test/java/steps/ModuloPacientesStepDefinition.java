package steps;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.*;

import mock.DBPruebaEnMemoria;
import org.domain.*;

public class ModuloPacientesStepDefinition {

    private final DBPruebaEnMemoria DBMockeada;
    private Enfermera enfermera;

    public ModuloPacientesStepDefinition(){
        this.DBMockeada = new DBPruebaEnMemoria();
    }

    @Given("que la enfermera esta registrada:")
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
        String obraSocial = tabla.getFirst().get("Obra social");
        String numeroAfiliado = tabla.getFirst().get("Numero de afiliado");

        Paciente nuevoPaciente = new Paciente(cuil, apellido, nombre, new Domicilio(calle, numero, localidad), new Afiliacion(obraSocial, numeroAfiliado));
        DBMockeada.guardarPaciente(nuevoPaciente);
        
        throw new PendingException();
    }

    @Then("el paciente queda registrado")
    public void elPacienteQuedaRegistrado() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Given("existen las siguientes afiliaciones")
    public void existenLasSiguientesAfiliaciones() {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
