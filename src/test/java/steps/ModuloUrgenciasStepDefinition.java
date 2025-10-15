package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

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

    @When("ingresa a la guardia el siguiente paciente:")
    public void ingresaAUrgenciasElSiguientePaciente(List<Map<String, String>> tabla) {
        Map<String, String> fila = tabla.getFirst();

        try {
            String cuit =  fila.get("CUIT");
            String informe = fila.get("Informe");
            Float temperatura = fila.get("Temperatura") != null ? Float.parseFloat(fila.get("Temperatura")) : null;
            NivelEmergencia nivelEmergencia = NivelEmergencia.buscarPorNombre(fila.get("Nivel de Emergencia"));
            Float frecuenciaCardiaca = fila.get("Frecuencia Cardiaca") != null ? Float.parseFloat(fila.get("Frecuencia Cardiaca")) : null;
            Float frecuenciaRespiratoria = fila.get("Frecuencia Respiratoria") != null ? Float.parseFloat(fila.get("Frecuencia Respiratoria")) : null;
            String tensionArterial = fila.get("Tension Arterial");

            servicioUrgencia.registrarUrgencia(
                    cuit, enfermera, informe, temperatura, nivelEmergencia,
                    frecuenciaCardiaca, frecuenciaRespiratoria, new TensionArterial(tensionArterial)
            );
        } catch (Exception e) {
            excepcionCapturada = e;
        }
    }

    @When("que están ingresados en la guardia los siguientes pacientes:")
    public void estanIngresadosEnLaGuardiaLosSiguientesPacientes(List<Map<String, String>> tabla) {
        //Map<String, String> fila = tabla.getFirst();

        for (Map<String, String> fila : tabla) {
            try {
                String cuit =  fila.get("CUIT");
                String informe = fila.get("Informe");
                Float temperatura = fila.get("Temperatura") != null ? Float.parseFloat(fila.get("Temperatura")) : null;
                NivelEmergencia nivelEmergencia = NivelEmergencia.buscarPorNombre(fila.get("Nivel de Emergencia"));
                Float frecuenciaCardiaca = fila.get("Frecuencia Cardiaca") != null ? Float.parseFloat(fila.get("Frecuencia Cardiaca")) : null;
                Float frecuenciaRespiratoria = fila.get("Frecuencia Respiratoria") != null ? Float.parseFloat(fila.get("Frecuencia Respiratoria")) : null;
                String tensionArterial = fila.get("Tension Arterial");

                servicioUrgencia.registrarUrgencia(
                        cuit, enfermera, informe, temperatura, nivelEmergencia,
                        frecuenciaCardiaca, frecuenciaRespiratoria, new TensionArterial(tensionArterial)
                );
            } catch (Exception e) {
                excepcionCapturada = e;
            }
        }
    }

    @Then("la lista de espera esta ordenada por nivel de emergencia de la siguiente manera:")
    public void laListaDeEsperaEstaOrdenadaDeLaSiguienteManera(List<String> ordenDeCUITsEsperado) {
        var listaDeEspera = new PriorityQueue<>(servicioUrgencia.getListaDeEspera());

        assertThat(listaDeEspera).hasSize(ordenDeCUITsEsperado.size());

        for (String CUIT : ordenDeCUITsEsperado) {
            assertThat(Objects.requireNonNull(listaDeEspera.poll()).getPaciente().getCuit())
                    .isEqualTo(CUIT);
        }
    }

    @Then("se muestra un mensaje de error indicando {string}")
    public void seMuestraUnMensajeDeErrorIndicandoPacienteNoRegistrado(String expectedError) {
        assertThat(excepcionCapturada)
                .as("No exception was captured!")
                .isNotNull()
                .hasMessage(expectedError);
    }
}
