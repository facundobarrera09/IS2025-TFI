package steps;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import mock.DBPruebaEnMemoria;
import org.app.ServicioUrgencia;
import org.domain.*;
import static org.assertj.core.api.Assertions.*;

public class ModuloUrgenciasStepDefinition {
    private Enfermera enfermera;
    private final DBPruebaEnMemoria DBMockeada;
    private final ServicioUrgencia servicioUrgencia;
    private Exception excepcionCapturada;
    public ModuloUrgenciasStepDefinition(){
        this.DBMockeada = new DBPruebaEnMemoria();
        this.servicioUrgencia = new ServicioUrgencia(DBMockeada);
    }

    @Given("que la enfermera esta registrada:")
    public void queLaEnfermeraEstaRegistrada(List<Map<String, String>> tabla) {
        String nombre = tabla.getFirst().get("nombre");
        String apellido = tabla.getFirst().get("apellido");
        enfermera = new Enfermera(nombre, apellido);
    }
    @Given("que están registrados los siguientes pacientes en el sistema:")
    public void queEstanRegistradosLosSiguientesPacientes(List<Map<String, String>> tabla)
    {
        for (Map<String, String> map : tabla)
        {
            String cuit = map.get("CUIT");
            String apellidoPaciente = map.get("Apellido Paciente");
            String nombrePaciente = map.get("Nombre Paciente");
            String obraSocial = map.get("Obra Social");
            Paciente paciente = new Paciente(cuit, apellidoPaciente, nombrePaciente, obraSocial);
            DBMockeada.guardarPaciente(paciente);
        }
    }

    @Given("que están registrados los siguientes pacientes en la lista de espera:")
    public void queEstanRegistradosLosSiguientesPacientesEnLaListaDeEspera(List<Map<String, String>> tabla) {
        for (Map<String, String> map : tabla) {
            String cuit = map.get("CUIT");
            String apellidoPaciente = map.get("Apellido Paciente");
            String nombrePaciente = map.get("Nombre Paciente");
            String obraSocial = "Obra Social Default"; // Valor por defecto
            Paciente paciente = new Paciente(cuit, apellidoPaciente, nombrePaciente, obraSocial);
            DBMockeada.guardarPaciente(paciente);

            // Registrar el paciente en la lista de espera con su nivel de emergencia
            String nivelEmergenciaStr = map.get("Nivel de emergencia");
            NivelEmergencia nivelEmergencia = Arrays.stream(NivelEmergencia.values())
                    .filter(nivel -> nivel.tieneNombre(nivelEmergenciaStr))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Nivel Desconocido"));

            // Registrar la urgencia con valores por defecto
            servicioUrgencia.registrarUrgencia(
                    cuit,
                    enfermera,
                    "Informe inicial",
                    36.5f,
                    nivelEmergencia,
                    80.0f,
                    16.0f,
                    new TensionArterial("120/80")
            );
        }
    }

    @When("ingresa a la guardia el siguiente paciente:") public void ingresaAUrgenciasElSiguientePaciente(List<Map<String, String>> tabla) {
        Map<String, String> fila = tabla.getFirst();
        try { String cuit = fila.get("CUIT");
            String informe = fila.get("Informe");
            float temperatura = Float.parseFloat(fila.get("Temperatura"));
            NivelEmergencia nivelEmergencia = Arrays.stream(NivelEmergencia.values()).filter(nivel -> nivel.tieneNombre(fila.get("Nivel de Emergencia"))).findFirst().orElseThrow(() -> new RuntimeException("Nivel Desconocido"));
            float frecuenciaCardiaca = Float.parseFloat(fila.get("Frecuencia Cardiaca"));
            float frecuenciaRespiratoria = Float.parseFloat(fila.get("Frecuencia Respiratoria"));
            String tensionArterial = fila.get("Tension Arterial");
            servicioUrgencia.registrarUrgencia( cuit, enfermera, informe, temperatura, nivelEmergencia, frecuenciaCardiaca, frecuenciaRespiratoria, new TensionArterial(tensionArterial) );
        }
        catch (Exception e) { excepcionCapturada = e;
        }
    }
    @Then("la lista de espera esta ordenada por CUIT de la siguiente manera:")
    public void laListaDeEsperaEstaOrdenadaPorCuit(List<String> tabla) {
        String cuitEsperado = tabla.getFirst(); List<String> cuilPendientes = servicioUrgencia.obtenerIngresosPendientes().stream() .map(Ingreso::getCuilPaciente).toList(); assertThat(cuilPendientes).hasSize(1).contains(cuitEsperado);
    }

    @Then("la lista de espera esta ordenada por nivel de emergencia de la siguiente manera:")
    public void laListaDeEsperaEstaOrdenadaPorNivelDeEmergenciaDeLaSiguienteManera(List<String> tabla) {
        List<String> cuitsEsperados = tabla;
        List<String> cuitsActuales = servicioUrgencia.obtenerIngresosPendientes().stream()
                .map(Ingreso::getCuilPaciente)
                .toList();

        assertThat(cuitsActuales)
                .hasSameSizeAs(cuitsEsperados)
                .containsExactlyElementsOf(cuitsEsperados);
    }


    @Then("se muestra un mensaje de error indicando {string}")
    public void seMuestraUnMensajeDeErrorIndicandoPacienteNoRegistrado(String expectedError) {
        assertThat(excepcionCapturada) .as("No exception was captured!") .isNotNull() .hasMessage(expectedError);
    }

}

























































































