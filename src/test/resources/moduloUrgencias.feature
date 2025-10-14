Feature: Modulo de Urgencia
  Esta feature esta relacionada al registro de ingresos de pacientes en la sala de urgencias
  respetando su nivel de prioridad y el horario de llegada

  Background:
    Given que la enfermera esta registrada:
      | Nombre Enfermera | Apellido Enfermera |
      | Susana           | Gimenez            |
    And que están registrados los siguientes pacientes en el sistema:
      | CUIT          | Apellido Paciente | Nombre Paciente | Obra Social       |
      | 20-43772929-9 | Villagra          | Mauro           | Subsidio de salud |
      | 26-12345678-0 | Perez             | Maria           | Swiss medical     |

    # 1. Paciente existe -> admisión registrada en cola
  Scenario: Ingreso del primer paciente a la lista de espera de urgencias
    When ingresa a la guardia el siguiente paciente:
      | CUIT          | Informe          | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 20-43772929-9 | Le agarro dengue | Emergencia          | 38          | 70                  | 15                      | 120/80           |

    Then la lista de espera esta ordenada por CUIT de la siguiente manera:
      | 20-43772929-9 |

    # 2. Paciente no existe -> capturamos "Paciente no registrado"
  Scenario: Ingreso del primer paciente no existente a la lista de espera de urgencias
    When ingresa a la guardia el siguiente paciente:
      | CUIT          | Informe          | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 20-43965801-9 | Le agarro dengue | Emergencia          | 38          | 70                  | 15                      | 120/80           |

    Then se muestra un mensaje de error indicando "Paciente no registrado"

    # 3. Falta dato obligatorio
  Scenario: Ingreso del paciente a la lista de espera de urgencias con datos incompletos
    When ingresa a la guardia el siguiente paciente:
      | CUIT          | Informe          | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 20-43772929-9 | Le agarro dengue | Emergencia          | 38          | 70                  | 15                      |                  |

    Then se muestra un mensaje de error indicando "Tensión Arterial no puede ser nulo"

    # 4. Valores negativos de frecuencia
  Scenario: Ingreso del paciente a la lista de espera de urgencia
  con frecuencia cardíaca o frecuencia Respiratoria negativa
    Given que están registrados los siguientes pacientes en el sistema:
      | CUIT          | Apellido Paciente | Nombre Paciente | Obra Social       |
      | 20-43772929-9 | Villagra          | Mauro           | Subsidio de salud |
      | 26-12345678-0 | Perez             | Maria           | Swiss medical     |

    When ingresa a la guardia el siguiente paciente:
      | CUIT          | Informe          | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 20-43111111-9 | Le agarro dengue | Emergencia          | 38          | -70                 | 15                      |                  |

    Then se muestra un mensaje de error indicando "La frecuencia cardiaca no puede ser negativa"

    # 5. Orden de prioridad: baja prioridad (ya esta) -> media prioridad (se ingresa) -> alta prioridad (ya esta)
  Scenario: Ingreso de pacientes con diferente niveles de emergencia
    Given que están registrados los siguientes pacientes en la lista de espera:
      | CUIT          | Apellido Paciente | Nombre Paciente | Nivel de emergencia |
      | 20-43772929-9 | Villagra          | Mauro           | Sin Urgencia        |

    When ingresa a la guardia el siguiente paciente:
      | CUIT          | Informe          | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 20-43111111-9 | Le agarro dengue | Emergencia          | 38          | -70                 | 15                      |                  |

    Then la lista de espera esta ordenada por nivel de emergencia de la siguiente manera:
      | 20-43111111-9 |
      | 20-43772929-9 |

    # 6. Orden de prioridad: baja prioridad (ya esta) -> baja prioridad (ya esta) -> baja prioridad (se ingresa)

    # 7. Verificar formato de tensión arterial y campos mandatorios (combinado)

