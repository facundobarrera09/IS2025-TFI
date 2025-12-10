package org.app.interfaces;

import org.domain.interfaces.helpers.IPasswordHasher;
import org.domain.models.Autoridad;
import org.domain.models.Usuario;
import org.domain.errors.UsuarioNoAutenticado;
import org.domain.interfaces.IRepositorioUsuarios;
import org.domain.controllers.ControladorAutenticacion;
import org.domain.models.helpers.PasswordHasherPorMapeo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioAutenticacionImplTest {

    @Mock
    private IRepositorioUsuarios repositorio;

    @Spy
    private IPasswordHasher hasher = new PasswordHasherPorMapeo();

    @InjectMocks
    private ControladorAutenticacion servicioAutenticacion;

    @BeforeEach
    void setUp() {
        // Limpiar invocaciones previas de los mocks para asegurar estado limpio entre tests
        clearInvocations(repositorio);
    }

    @Nested
    class RegistroUsuarioTests {

        @Test
        void deberiaRegistrarUsuarioConDatosValidos() throws IllegalArgumentException {
            // Arrange
            String email = "usuario@hospital.com";
            String contraseña = "SecurePass123";
            Autoridad autoridad = Autoridad.MEDICO;

            when(repositorio.existeEmail(email)).thenReturn(false);

            // Act
            Usuario usuario = servicioAutenticacion.registrarUsuario(email, contraseña, autoridad);

            // Assert
            assertNotNull(usuario);
            assertEquals(email, usuario.getEmail());
            assertEquals(autoridad, usuario.getAutoridad());
            assertNotEquals(contraseña, usuario.getHashContraseña());
            assertTrue(usuario.getHashContraseña().startsWith("$argon2id$") ||
                      usuario.getHashContraseña().startsWith("$2a$") ||
                      usuario.getHashContraseña().startsWith("$2b$"));

            verify(repositorio).existeEmail(email);
            verify(repositorio).guardarUsuario(usuario);
        }

        @Test
        void deberiaLanzarExcepcionConEmailInvalido() {
            // Arrange
            String email = "email-sin-arroba";
            String contraseña = "SecurePass123";
            Autoridad autoridad = Autoridad.MEDICO;

            // Act & Assert
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> servicioAutenticacion.registrarUsuario(email, contraseña, autoridad)
            );

            assertEquals("Formato de email inválido", excepcion.getMessage());
            verify(repositorio, never()).guardarUsuario(any(Usuario.class));
        }

        @Test
        void deberiaLanzarExcepcionConEmailVacio() {
            // Arrange
            String email = "";
            String contraseña = "SecurePass123";
            Autoridad autoridad = Autoridad.MEDICO;

            // Act & Assert
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> servicioAutenticacion.registrarUsuario(email, contraseña, autoridad)
            );

            assertEquals("Formato de email inválido", excepcion.getMessage());
            verify(repositorio, never()).guardarUsuario(any(Usuario.class));
        }

        @Test
        void deberiaLanzarExcepcionConContraseñaCorta() {
            // Arrange
            String email = "usuario@hospital.com";
            String contraseña = "Short7";
            Autoridad autoridad = Autoridad.MEDICO;

            // Act & Assert
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> servicioAutenticacion.registrarUsuario(email, contraseña, autoridad)
            );

            assertEquals("La contraseña debe tener al menos 8 caracteres", excepcion.getMessage());
            verify(repositorio, never()).guardarUsuario(any(Usuario.class));
        }

        @Test
        void deberiaAceptarContraseñaExactamenteDe8Caracteres() throws IllegalArgumentException {
            // Arrange
            String email = "usuario@hospital.com";
            String contraseña = "12345678";
            Autoridad autoridad = Autoridad.MEDICO;

            when(repositorio.existeEmail(email)).thenReturn(false);

            // Act
            Usuario usuario = servicioAutenticacion.registrarUsuario(email, contraseña, autoridad);

            // Assert
            assertNotNull(usuario);
            assertEquals(email, usuario.getEmail());
            verify(repositorio).existeEmail(email);
            verify(repositorio).guardarUsuario(usuario);
        }

        @Test
        void deberiaLanzarExcepcionConEmailDuplicado() {
            // Arrange
            String email = "usuario@hospital.com";
            String contraseña = "SecurePass123";
            Autoridad autoridad = Autoridad.MEDICO;

            when(repositorio.existeEmail(email)).thenReturn(true);

            // Act & Assert
            IllegalArgumentException excepcion = assertThrows(
                IllegalArgumentException.class,
                () -> servicioAutenticacion.registrarUsuario(email, contraseña, autoridad)
            );

            assertEquals("El email ya está registrado", excepcion.getMessage());
            verify(repositorio).existeEmail(email);
            verify(repositorio, never()).guardarUsuario(any(Usuario.class));
        }

        @Test
        void deberiaGenerarHashConFormatoCorrecto() throws IllegalArgumentException {
            // Arrange
            String email = "usuario@hospital.com";
            String contraseña = "SecurePass123";
            Autoridad autoridad = Autoridad.MEDICO;

            when(repositorio.existeEmail(email)).thenReturn(false);

            // Act
            Usuario usuario = servicioAutenticacion.registrarUsuario(email, contraseña, autoridad);

            // Assert
            String hash = usuario.getHashContraseña();
            assertTrue(hash.startsWith("$argon2id$") || hash.startsWith("$2a$") || hash.startsWith("$2b$"));
            assertEquals("$argon2id$v=19$m=65536,t=3,p=4$c29tZXNhbHQ$RdescudvJCsgt3ub+b+dWRWJTmaaJObG", hash);
        }
    }

    @Nested
    class InicioSesionTests {

        @Test
        void deberiaIniciarSesionConCredencialesCorrectas() throws UsuarioNoAutenticado {
            // Arrange
            String email = "medico@hospital.com";
            String contraseña = "SecurePass123";
            String hashEsperado = "$argon2id$v=19$m=65536,t=3,p=4$c29tZXNhbHQ$RdescudvJCsgt3ub+b+dWRWJTmaaJObG";
            Usuario usuarioMock = new Usuario(email, hashEsperado, Autoridad.MEDICO);

            when(repositorio.buscarPorEmail(email)).thenReturn(usuarioMock);

            // Act
            Usuario usuario = servicioAutenticacion.iniciarSesion(email, contraseña);

            // Assert
            assertNotNull(usuario);
            assertEquals(email, usuario.getEmail());
            assertEquals(Autoridad.MEDICO, usuario.getAutoridad());
            verify(repositorio).buscarPorEmail(email);
        }

        @Test
        void deberiaLanzarExcepcionConEmailInexistente() {
            // Arrange
            String email = "inexistente@hospital.com";
            String contraseña = "SecurePass123";

            when(repositorio.buscarPorEmail(email)).thenReturn(null);

            // Act & Assert
            UsuarioNoAutenticado excepcion = assertThrows(
                UsuarioNoAutenticado.class,
                () -> servicioAutenticacion.iniciarSesion(email, contraseña)
            );

            assertEquals("Usuario o contraseña inválidos", excepcion.getMessage());
            verify(repositorio).buscarPorEmail(email);
        }

        @Test
        void deberiaLanzarExcepcionConContraseñaIncorrecta() {
            // Arrange
            String email = "medico@hospital.com";
            String contraseñaIncorrecta = "OtraPass123";
            String hashEsperado = "$argon2id$v=19$m=65536,t=3,p=4$c29tZXNhbHQ$RdescudvJCsgt3ub+b+dWRWJTmaaJObG";
            Usuario usuarioMock = new Usuario(email, hashEsperado, Autoridad.MEDICO);

            when(repositorio.buscarPorEmail(email)).thenReturn(usuarioMock);

            // Act & Assert
            UsuarioNoAutenticado excepcion = assertThrows(
                UsuarioNoAutenticado.class,
                () -> servicioAutenticacion.iniciarSesion(email, contraseñaIncorrecta)
            );

            assertEquals("Usuario o contraseña inválidos", excepcion.getMessage());
            verify(repositorio).buscarPorEmail(email);
        }

        @Test
        void deberiaLanzarExcepcionConEmailVacio() {
            // Arrange
            String email = "";
            String contraseña = "SecurePass123";

            when(repositorio.buscarPorEmail(email)).thenReturn(null);

            // Act & Assert
            UsuarioNoAutenticado excepcion = assertThrows(
                UsuarioNoAutenticado.class,
                () -> servicioAutenticacion.iniciarSesion(email, contraseña)
            );

            assertEquals("Usuario o contraseña inválidos", excepcion.getMessage());
            verify(repositorio).buscarPorEmail(email);
        }

        @Test
        void deberiaLanzarExcepcionConContraseñaVacia() {
            // Arrange
            String email = "medico@hospital.com";
            String contraseña = "";
            String hashEsperado = "$argon2id$v=19$m=65536,t=3,p=4$c29tZXNhbHQ$RdescudvJCsgt3ub+b+dWRWJTmaaJObG";
            Usuario usuarioMock = new Usuario(email, hashEsperado, Autoridad.MEDICO);

            when(repositorio.buscarPorEmail(email)).thenReturn(usuarioMock);

            // Act & Assert
            UsuarioNoAutenticado excepcion = assertThrows(
                UsuarioNoAutenticado.class,
                () -> servicioAutenticacion.iniciarSesion(email, contraseña)
            );

            assertEquals("Usuario o contraseña inválidos", excepcion.getMessage());
            verify(repositorio).buscarPorEmail(email);
        }

        @Test
        void deberiaIniciarSesionConMultiplesUsuariosDistintos() throws UsuarioNoAutenticado {
            // Arrange
            String emailMedico = "medico@hospital.com";
            String contraseñaMedico = "SecurePass123";
            String hashMedico = "$argon2id$v=19$m=65536,t=3,p=4$c29tZXNhbHQ$RdescudvJCsgt3ub+b+dWRWJTmaaJObG";
            Usuario usuarioMedico = new Usuario(emailMedico, hashMedico, Autoridad.MEDICO);

            String emailEnfermero = "enfermero@hospital.com";
            String contraseñaEnfermero = "NursePass456";
            String hashEnfermero = "$argon2id$v=19$m=65536,t=3,p=4$c29tZXNhbHQ$RdescudvJCsgt3ub+b+dWRWJTmaaJObH";
            Usuario usuarioEnfermero = new Usuario(emailEnfermero, hashEnfermero, Autoridad.ENFERMERO);

            when(repositorio.buscarPorEmail(emailMedico)).thenReturn(usuarioMedico);
            when(repositorio.buscarPorEmail(emailEnfermero)).thenReturn(usuarioEnfermero);

            // Act
            Usuario resultadoMedico = servicioAutenticacion.iniciarSesion(emailMedico, contraseñaMedico);
            Usuario resultadoEnfermero = servicioAutenticacion.iniciarSesion(emailEnfermero, contraseñaEnfermero);

            // Assert
            assertNotNull(resultadoMedico);
            assertEquals(emailMedico, resultadoMedico.getEmail());
            assertEquals(Autoridad.MEDICO, resultadoMedico.getAutoridad());

            assertNotNull(resultadoEnfermero);
            assertEquals(emailEnfermero, resultadoEnfermero.getEmail());
            assertEquals(Autoridad.ENFERMERO, resultadoEnfermero.getAutoridad());

            verify(repositorio).buscarPorEmail(emailMedico);
            verify(repositorio).buscarPorEmail(emailEnfermero);
        }
    }
}
