package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import mock.DBPruebaEnMemoria;
import org.app.ServicioUrgencia;
import org.domain.Enfermera;
import org.domain.Ingreso;
import org.domain.NivelEmergencia;
import org.domain.Paciente;
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

    @Given("que estan registrados los siguientes pacientes en el sistema:")
    public void queEstanRegistradosLosSiguientesPacientes(List<Map<String, String>> tabla) {
        for (Map<String, String> map : tabla) {
            String cuit = map.get("CUIT");
            String apellidoPaciente = map.get("Apellido Paciente");
            String nombrePaciente = map.get("Nombre Paciente");
            String obraSocial = map.get("Obra Social");

            Paciente paciente = new Paciente(cuit, apellidoPaciente, nombrePaciente, obraSocial);
            DBMockeada.guardarPaciente(paciente);
        }

    }

    @When("ingresa a urgencias el siguiente paciente:")
    public void ingresaAUrgenciasElSiguientePaciente(List<Map<String, String>> tabla) {
        Map<String, String> fila = tabla.getFirst();
        String cuit =  fila.get("CUIT");
        String informe = fila.get("Informe");
        float temperatura = Float.parseFloat(fila.get("Temperatura"));
        NivelEmergencia nivelEmergencia = Arrays.stream(NivelEmergencia.values()).filter(nivel -> nivel.tieneNombre(fila.get("Nivel de Emergencia"))).findFirst().orElseThrow(() -> new RuntimeException("Nivel Desconocido"));
        float frecuenciaCardiaca = Float.parseFloat(fila.get("Frecuencia Cardiaca"));
        float frecuenciaRespiratoria = Float.parseFloat(fila.get("Frecuencia Respiratoria"));
        List<Float> tensionArterial = Arrays.stream(fila.get("Tension Arterial").split("/")).map(Float::parseFloat).toList();

        try {
            servicioUrgencia.registrarUrgencia(cuit, enfermera, informe, temperatura, nivelEmergencia, frecuenciaCardiaca, frecuenciaRespiratoria, tensionArterial.get(0), tensionArterial.get(1));
        } catch (Exception e) {
            if (e.getMessage().equals("Paciente no registrado")) {
                excepcionCapturada = e;
            }
            else throw e;
        }

    }

    @Then("la lista de espera esta ordenada por cuil de la siguiente manera:")
    public void laListaDeEsperaEstaOrdenadaPorCuil(List<String> tabla) {
        String cuitEsperado = tabla.getFirst();
        List<String> cuilPendientes = servicioUrgencia.obtenerIngresosPendientes().stream()
                .map(Ingreso::getCuilPaciente).toList();

        assertThat(cuilPendientes).hasSize(1).contains(cuitEsperado);
    }

    @Then("se muestra un mensaje de error indicando 'Paciente no registrado'")
    public void seMuestraUnMensajeDeErrorIndicandoPacienteNoRegistrado() {
        assertThat(excepcionCapturada)
                .as("No exception was captured!")
                .isNotNull()
                .hasMessage("Paciente no registrado");
    }
}
