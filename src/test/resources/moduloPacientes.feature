Feature: Registro de pacientes

  Background:
    Given existen las siguientes afiliaciones
      | CUIL          | Obra social       |
      | 20-12345678-1 | Subsidio de salud |

    And que la enfermera esta registrada:
      | Nombre Enfermera | Apellido Enfermera |
      | Susana           | Gimenez            |

  Scenario: Todos los datos provistos y una obra social existente
    When se ingresa el siguiente paciente
      | CUIL          | Apellido | Nombre | Calle        | Numero | Localidad        | Obra social       | Numero de afiliado |
      | 20-12345678-1 | Villagra | Mauro  | Siempreverde | 123    | Nueva concepción | Subsidio de salud | 20-12345678-1      |

    Then el paciente queda registrado

  Scenario: Todos los datos provistos y sin obra social
    When se ingresa el siguiente paciente
      | CUIL          | Apellido | Nombre | Calle        | Numero | Localidad        | Obra social       | Numero de afiliado |
      | 20-12345678-1 | Villagra | Mauro  | Siempreverde | 123    | Nueva concepción |                   |                    |

    Then el paciente queda registrado

  Scenario: Todos los datos provistos pero la obra social no existe
    When se ingresa el siguiente paciente
      | CUIL          | Apellido | Nombre | Calle        | Numero | Localidad        | Obra social       | Numero de afiliado |
      | 20-12345678-1 | Villagra | Mauro  | Siempreverde | 123    | Nueva concepción | OSPE              | 1234               |

    Then se muestra un mensaje de error indicando "Obra social inexistente"

  Scenario: Todos los datos provistos y una obra social existente a la cual no esta afiliado
    When se ingresa el siguiente paciente
      | CUIL          | Apellido | Nombre  | Calle       | Numero | Localidad | Obra social       | Numero de afiliado |
      | 21-87654321-2 | Barrera  | Facundo | Blas parera | 123    | El timbo  | Subsidio de salud | 21-87654321-2      |

    Then se muestra un mensaje de error indicando "Paciente no afiliado a obra social"

  Scenario Outline: Se registra el paciente con algun dato mandatorio omitido
    When se ingresa el siguiente paciente
      | CUIL   | Apellido   | Nombre   | Calle   | Numero   | Localidad   | Obra social | Numero de afiliado |
      | <CUIL> | <Apellido> | <Nombre> | <Calle> | <Numero> | <Localidad> |             |                    |

    Then se muestra un mensaje de error indicando <Mensaje de error>

    Examples:
      | CUIL          | Apellido | Nombre | Calle        | Numero   | Localidad        | Mensaje de error               |
      |               | Villagra | Mauro  | Siempreverde | 123      | Nueva concepción | CUIL no puede estar vacio      |
      | 20-12345678-1 |          | Mauro  | Siempreverde | 123      | Nueva concepción | Apellido no puede estar vacio  |
      | 20-12345678-1 | Villagra |        | Siempreverde | 123      | Nueva concepción | Nombre no puede estar vacio    |
      | 20-12345678-1 | Villagra | Mauro  |              | 123      | Nueva concepción | Calle no puede estar vacio     |
      | 20-12345678-1 | Villagra | Mauro  | Siempreverde |          | Nueva concepción | Numero no puede estar vacio    |
      | 20-12345678-1 | Villagra | Mauro  | Siempreverde | 123      |                  | Localidad no puede estar vacio |