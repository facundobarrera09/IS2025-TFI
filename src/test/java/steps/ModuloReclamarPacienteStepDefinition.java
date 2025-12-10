package steps;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mock.RepoPacientesParaPruebas;
import org.domain.controllers.ControladorUrgencias;
import org.domain.errors.ListaDeEsperaVacia;
import org.domain.interfaces.IRepositorioPacientes;
import org.domain.models.*;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import static org.assertj.core.api.Assertions.assertThat;

public class ModuloReclamarPacienteStepDefinition {

    private final IRepositorioPacientes repositorioPacientes;
    private final ControladorUrgencias controladorUrgencias;

    private Medico medico;
    private Ingreso ingresoReclamado;

    private Exception excepcionCapturada;

    public ModuloReclamarPacienteStepDefinition() {
        this.repositorioPacientes = new RepoPacientesParaPruebas();
        this.controladorUrgencias = new ControladorUrgencias(repositorioPacientes);
    }

    @Given("existe la siguiente lista de espera")
    public void existeLaSiguienteListaDeEspera(List<Map<String, String>> tabla) {
        for (Map<String, String> fila : tabla) {
            Paciente paciente = new Paciente(
                    fila.get("CUIT"),
                    fila.get("Apellido"),
                    fila.get("Nombre"),
                    new Domicilio("Calle", "123", "Localidad")
            );
            repositorioPacientes.guardarPaciente(paciente);
            controladorUrgencias.registrarUrgencia(
                    paciente.getCuit(),
                    new Enfermera("Claudia", "Gonzales"),
                    fila.get("Informe"),
                    36.5f,
                    NivelEmergencia.buscarPorNombre(fila.get("Nivel de emergencia")),
                    80f,
                    120f,
                    new TensionArterial("120/80")
            );
        }
    }

    @When("el médico reclama un paciente")
    public void elMédicoReclamaUnPaciente() {
        try {
            ingresoReclamado = controladorUrgencias.reclamarIngreso(medico);
        }
        catch (ListaDeEsperaVacia e) {
            this.excepcionCapturada = e;
        }
    }

    @Given("el médico inicio sesión")
    public void elMédicoInicioSesión() {
        this.medico = new Medico("4214");
    }

    @Then("se obtiene el paciente con cuit {string}")
    public void seObtieneElPacienteConCuit(String cuit) {
        assertThat(ingresoReclamado.getPaciente().getCuit()).isEqualTo(cuit);
    }

    @And("el nuevo estado del ingreso es EN_PROCESO")
    public void elNuevoEstadoDelIngresoEsEN_PROCESO() {
        assertThat(ingresoReclamado.getEstado()).isEqualTo(EstadoIngreso.EN_PROCESO);
    }

    @And("el paciente con CUIT {string} ya no está en la lista de espera")
    public void elPacienteConCUITYaNoEstáEnLaListaDeEspera(String cuit) {
        PriorityQueue<Ingreso> listaDeEspera = controladorUrgencias.getListaDeEspera();

        boolean encontrado = false;
        for (Ingreso ingreso : listaDeEspera) {
            if (ingreso.getPaciente().getCuit().equals(cuit)) {
                encontrado = true;
            }
        }

        assertThat(encontrado).isFalse();
    }

    @Then("se advierte que no hay pacientes en espera")
    public void seAdvierteQueNoHayPacientesEnEspera() {
        assertThat(excepcionCapturada).isInstanceOf(ListaDeEsperaVacia.class);
        assertThat(excepcionCapturada.getMessage()).isEqualTo("No hay pacientes en la lista de espera");
    }
}
