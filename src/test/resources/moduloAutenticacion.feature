Feature: Módulo de Autenticación
  Esta feature está relacionada al registro e inicio de sesión de usuarios en el sistema
  respetando los requisitos de seguridad y asignación de permisos por rol

  Background:
    Given que el sistema tiene los siguientes usuarios registrados:
      | email                 | contraseña      | autoridad |
      | medico@hospital.com   | SecurePass123   | medico    |
      | enfermero@hospital.com | NursePass456 | enfermero |

  # 1. Registro exitoso
  Scenario: Registro exitoso de un nuevo usuario médico
    When me registro con los siguientes datos:
      | email             | contraseña       | autoridad |
      | nuevo@hospital.com| NewSecurePass123 | medico    |
    Then mi contraseña es hasheada usando ARGON2ID o Bcrypt
    And veo el mensaje "Registro exitoso"
    And soy redirigido a la página de login

  # 2. Registro con email inválido
  Scenario: Registro con formato de email inválido
    When me registro con los siguientes datos:
      | email          | contraseña      | autoridad |
      | emailinvalido  | SecurePass123   | médico    |
    Then veo un mensaje de error "Formato de email inválido"

  # 3. Registro con contraseña corta
  Scenario: Registro con contraseña de menos de 8 caracteres
    When me registro con los siguientes datos:
      | email              | contraseña | autoridad |
      | test@hospital.com | Short7     | médico    |
    Then veo un mensaje de error "La contraseña debe tener al menos 8 caracteres"

  # 4. Login exitoso
  Scenario: Inicio de sesión exitoso como médico
    When inicio sesión con:
      | email              | contraseña    |
      | medico@hospital.com | SecurePass123 |
    Then soy redirigido al dashboard
    And puedo ver las historias de usuario: IS2025-003 y ES2025-004

  # 5. Login con credenciales incorrectas
  Scenario: Inicio de sesión con credenciales inválidas
    When inicio sesión con:
      | email              | contraseña    |
      | medico@hospital.com | 123456     |
    Then veo el mensaje de error "Usuario o contraseña inválidos"

  # 6. Verificación de permisos para enfermero
  Scenario: Login exitoso como enfermero verifica permisos
    When inicio sesión con:
      | email                 | contraseña  |
      | enfermero@hospital.com | NursePass456 |
    Then soy redirigido al dashboard
    And puedo ver las historias de usuario: IS2025-001 e IS2025-002

  # 7. Intento de login con usuario no registrado
  Scenario: Inicio de sesión con usuario no registrado
    When inicio sesión con:
      | email             | contraseña    |
      | noexiste@test.com | SomePass123 |
    Then veo el mensaje de error "Usuario o contraseña inválidos"