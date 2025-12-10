package steps;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mock.RepoPacientesParaPruebas;
import org.domain.controllers.ControladorUrgencias;
import org.domain.interfaces.IRepositorioPacientes;
import org.domain.models.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ModuloCrearAtencionStepDefinition {

    private IRepositorioPacientes repositorioPacientes;
    private ControladorUrgencias controladorUrgencias;

    private Medico medico;
    private Ingreso ingreso;

    private Exception excepcionCapturada;

    public ModuloCrearAtencionStepDefinition() {
        repositorioPacientes = new RepoPacientesParaPruebas();
        controladorUrgencias = new ControladorUrgencias(repositorioPacientes);
    }

    @And("existe un ingreso siendo procesado")
    public void existeUnIngresoSiendoProcesado() {
        Paciente paciente = new Paciente(
                "20-43216547-1",
                "Barrera",
                "Facundo",
                new Domicilio("Calle", "123", "Localidad")
        );
        repositorioPacientes.guardarPaciente(paciente);
        controladorUrgencias.registrarUrgencia(
                paciente.getCuit(),
                new Enfermera("Claudia", "Gonzales"),
                "Informe de entrada",
                36.5f,
                NivelEmergencia.EMERGENCIA,
                80f,
                120f,
                new TensionArterial("120/80")
        );
        ingreso = controladorUrgencias.reclamarIngreso(medico);
    }

    @When("el médico ingresa en informe")
    public void elMédicoIngresaEnInforme() {
        controladorUrgencias.registrarInforme(medico, "Algun informe válido");
    }

    @Then("el informe queda registrado")
    public void elInformeQuedaRegistrado() {
        assertThat(ingreso.getAtencion()).isNotNull();
        assertThat(ingreso.getAtencion().getInforme()).isNotNull();
        assertThat(ingreso.getAtencion().getInforme()).isNotEqualTo("");
    }

    @And("el nuevo estado del ingreso es FINALIZADO")
    public void elNuevoEstadoDelIngresoEsFINALIZADO() {
        assertThat(ingreso.getEstado()).isEqualTo(EstadoIngreso.FINALIZADO);
    }

    @Given("el médico inicio sesión en el sistema")
    public void elMédicoInicioSesiónEnElSistema() {
        this.medico = new Medico("4214");
    }

    @When("el médico no ingresa el informe")
    public void elMédicoNoIngresaElInforme() {
        try {
            controladorUrgencias.registrarInforme(medico, "");
        }
        catch (IllegalArgumentException e) {
            this.excepcionCapturada = e;
        }
    }

    @Then("se alerta que el informe es obligatorio")
    public void seAlertaQueElInformeEsObligatorio() {
        assertThat(excepcionCapturada).isNotNull();
        assertThat(excepcionCapturada).isInstanceOf(IllegalArgumentException.class);
        assertThat(excepcionCapturada).hasMessage("informe no puede ser nulo");
    }
}
