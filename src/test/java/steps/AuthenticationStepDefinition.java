package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.Assert.*;
import java.util.*;
import org.domain.models.Usuario;
import org.domain.models.AuthenticationException;
import org.domain.interfaces.ServicioAutenticacion;
import org.domain.interfaces.ServicioAutenticacionImpl;
import mock.RepositorioUsuariosPrueba;

public class AuthenticationStepDefinition {

    private ServicioAutenticacion servicioAuth;
    // Usar la implementación concreta del mock
    private RepositorioUsuariosPrueba repositorioPrueba = new RepositorioUsuariosPrueba();
    private String resultadoRegistro;
    private String resultadoLogin;
    private boolean loginExitoso;
    private List<String> historiasUsuarioDisponibles;
    private Usuario usuarioRegistrado;

    @Given("que el sistema tiene los siguientes usuarios registrados:")
    public void que_el_sistema_tiene_los_siguientes_usuarios_registrados(io.cucumber.datatable.DataTable dataTable) {
        System.out.println("=== INICIALIZANDO BACKGROUND ===");
        // Inicializar el servicio con la implementación del mock
        servicioAuth = new ServicioAutenticacionImpl(repositorioPrueba);

        List<Map<String, String>> usuarios = dataTable.asMaps();
        for (Map<String, String> usuario : usuarios) {
            String email = usuario.get("email");
            String contraseña = usuario.get("contraseña");
            String autoridad = usuario.get("autoridad");

            System.out.println("Precargando usuario: " + email + ", " + autoridad);

            try {
                // Registrar los usuarios en el repositorio de prueba
                // El servicioAuth.registrarUsuario internamente llama a repositorio.guardarUsuario(usuario)
                Usuario u = servicioAuth.registrarUsuario(email, contraseña, autoridad);
                // NOTA: Para el Background, necesitamos el hash precalculado del mock,
                // así que el llamado a registrarUsuario es suficiente si el hash es fijo.
                System.out.println("Usuario precargado: " + email);
            } catch (IllegalArgumentException e) {
                System.out.println("Error precargando " + email + ": " + e.getMessage());
            }
        }
        System.out.println("=== BACKGROUND COMPLETADO ===");
    }

    @When("me registro con los siguientes datos:")
    public void me_registro_con_los_siguientes_datos(io.cucumber.datatable.DataTable dataTable) {
        System.out.println("=== INICIANDO REGISTRO ===");

        List<Map<String, String>> listaDatos = dataTable.asMaps();
        if (listaDatos.isEmpty()) {
            throw new IllegalArgumentException("No se proporcionaron datos de registro.");
        }
        Map<String, String> datos = listaDatos.get(0);

        String email = datos.get("email");
        String contraseña = datos.get("contraseña");
        String autoridad = datos.get("autoridad");

        System.out.println("Datos registro - Email: " + email + ", Autoridad: " + autoridad);

        try {
            usuarioRegistrado = servicioAuth.registrarUsuario(email, contraseña, autoridad);
            resultadoRegistro = "Registro exitoso";
            System.out.println("Registro EXITOSO: " + email);
        } catch (IllegalArgumentException e) {
            resultadoRegistro = e.getMessage();
            System.out.println("Registro FALLIDO: " + e.getMessage());
        }
        System.out.println("=== REGISTRO FINALIZADO ===");
    }

    @Then("mi contraseña es hasheada usando ARGON2ID o Bcrypt")
    public void mi_contraseña_es_hasheada_usando_ARGON2ID_o_Bcrypt() {
        System.out.println("=== VERIFICANDO HASH ===");
        assertNotNull("El usuario debe estar registrado", usuarioRegistrado);
        String contraseñaAlmacenada = usuarioRegistrado.getContraseñaHash();

        System.out.println("Contraseña almacenada: " + contraseñaAlmacenada);

        assertNotEquals("La contraseña no debe ser el texto original",
                "NewSecurePass123", contraseñaAlmacenada);

        // Verificar que es un hash válido
        boolean esHashValido = contraseñaAlmacenada.startsWith("$argon2id$") ||
                contraseñaAlmacenada.startsWith("$2a$") ||
                contraseñaAlmacenada.startsWith("$2b$");

        System.out.println("Es hash válido: " + esHashValido);
        assertTrue("La contraseña debe usar ARGON2ID o Bcrypt. Hash obtenido: " + contraseñaAlmacenada,
                esHashValido);
        System.out.println("=== VERIFICACIÓN HASH EXITOSA ===");
    }

    @Then("veo el mensaje {string}")
    public void veo_el_mensaje(String mensajeEsperado) {
        System.out.println("Verificando mensaje. Esperado: '" + mensajeEsperado + "', Actual: '" + resultadoRegistro + "'");
        assertEquals(mensajeEsperado, resultadoRegistro);
    }

    @Then("soy redirigido a la página de login")
    public void soy_redirigido_a_la_página_de_login() {
        System.out.println("Verificando redirección a login");
        assertEquals("Debe mostrar registro exitoso para redirigir",
                "Registro exitoso", resultadoRegistro);
    }

    @Then("veo un mensaje de error {string}")
    public void veo_un_mensaje_de_error(String mensajeError) {
        System.out.println("Verificando error. Esperado: '" + mensajeError + "', Actual: '" + resultadoRegistro + "'");
        assertEquals(mensajeError, resultadoRegistro);
    }

    @When("inicio sesión con:")
    public void inicio_sesión_con(io.cucumber.datatable.DataTable dataTable) {
        System.out.println("=== INICIANDO LOGIN ===");
        Map<String, String> credenciales = dataTable.asMaps().get(0);

        String email = credenciales.get("email");
        String contraseña = credenciales.get("contraseña");

        System.out.println("Intentando login con: " + email);

        try {
            Usuario usuario = servicioAuth.iniciarSesion(email, contraseña);
            loginExitoso = true;
            historiasUsuarioDisponibles = servicioAuth.obtenerHistoriasUsuario(usuario.getAutoridad());
            resultadoLogin = "Login exitoso";
            System.out.println("Login EXITOSO: " + email);
            System.out.println("Historias disponibles: " + historiasUsuarioDisponibles);
        } catch (AuthenticationException e) {
            loginExitoso = false;
            resultadoLogin = e.getMessage();
            System.out.println("Login FALLIDO: " + e.getMessage());
        }
        System.out.println("=== LOGIN FINALIZADO ===");
    }

    @Then("soy redirigido al dashboard")
    public void soy_redirigido_al_dashboard() {
        System.out.println("Verificando redirección al dashboard. Login exitoso: " + loginExitoso);
        assertTrue("El login debe ser exitoso para redirigir al dashboard. Error: " + resultadoLogin,
                loginExitoso);
    }

    @Then("puedo ver las historias de usuario: {string} y {string}")
    public void puedo_ver_las_historias_de_usuario_y(String historia1, String historia2) {
        System.out.println("Verificando historias. Disponibles: " + historiasUsuarioDisponibles);
        assertNotNull("Las historias de usuario deben estar disponibles", historiasUsuarioDisponibles);
        assertTrue("Debe tener acceso a " + historia1 + ". Historias disponibles: " + historiasUsuarioDisponibles,
                historiasUsuarioDisponibles.contains(historia1));
        assertTrue("Debe tener acceso a " + historia2 + ". Historias disponibles: " + historiasUsuarioDisponibles,
                historiasUsuarioDisponibles.contains(historia2));
    }

    @Then("veo el mensaje de error {string}")
    public void veo_el_mensaje_de_error(String mensajeError) {
        System.out.println("Verificando error login. Esperado: '" + mensajeError + "', Actual: '" + resultadoLogin + "'");
        assertEquals(mensajeError, resultadoLogin);
    }
}