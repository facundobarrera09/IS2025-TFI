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
      | 24-87654321-1 | Molina            | Marcos          | OSPE              |
      | 21-88544755-2 | Rodriguez         | Camila          | OSFATUN           |

    # 1. Paciente existe -> admisión registrada en cola
  Scenario: Ingreso del primer paciente a la lista de espera de urgencias
    When ingresa a la guardia el siguiente paciente:
      | CUIT          | Informe          | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 20-43772929-9 | Le agarro dengue | Emergencia          | 38          | 70                  | 15                      | 120/80           |

    Then la lista de espera esta ordenada por nivel de emergencia de la siguiente manera:
      | 20-43772929-9 |

    # 2. Paciente no existe -> capturamos "Paciente no registrado"
  Scenario: Ingreso del primer paciente no existente a la lista de espera de urgencias
    When ingresa a la guardia el siguiente paciente:
      | CUIT          | Informe          | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 20-43965801-9 | Le agarro dengue | Emergencia          | 38          | 70                  | 15                      | 120/80           |

    Then se muestra un mensaje de error indicando "Paciente no registrado"

    # 3. Falta dato obligatorio
  Scenario Outline: Ingreso del paciente a la lista de espera de urgencias con datos incompletos
    When ingresa a la guardia el siguiente paciente:
      | CUIT          | Informe          | Nivel de Emergencia   | Temperatura | Frecuencia Cardiaca   | Frecuencia Respiratoria   | Tension Arterial   |
      | 20-43772929-9 | <Informe>        | <Nivel de emergencia> | 38          | <Frecuencia cardiaca> | <Frecuencia respiratoria> | <Tension arterial> |

    Then se muestra un mensaje de error indicando <Mensaje de error>

    Examples:
      | Informe          | Nivel de emergencia | Frecuencia cardiaca | Frecuencia respiratoria | Tension arterial | Mensaje de error                            |
      |                  | Emergencia          | 70                  | 15                      | 120/80           | "Informe no puede ser nulo"                 |
      | Le agarro dengue |                     | 70                  | 15                      | 120/80           | "Nivel de emergencia no puede ser nulo"     |
      | Le agarro dengue | Emergencia          |                     | 15                      | 120/80           | "Frecuencia cardíaca no puede ser nulo"     |
      | Le agarro dengue | Emergencia          | 70                  |                         | 120/80           | "Frecuencia respiratoria no puede ser nulo" |
      | Le agarro dengue | Emergencia          | 70                  | 15                      |                  | "Tensión arterial no puede ser nulo"        |
      | Le agarro dengue | Emergencia          | 70                  | 15                      |    /80           | "Frecuencia sistólica no puede ser nulo"    |
      | Le agarro dengue | Emergencia          | 70                  | 15                      | 120/             | "Frecuencia diastólica no puede ser nulo"   |

    # 4. Valores negativos de frecuencia
  Scenario Outline: Ingreso del paciente a la lista de espera de urgencia con frecuencia cardíaca o frecuencia respiratoria negativa
    When ingresa a la guardia el siguiente paciente:
      | CUIT          | Informe          | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca   | Frecuencia Respiratoria   | Tension Arterial |
      | 26-12345678-0 | Le agarro dengue | Emergencia          | 38          | <Frecuencia Cardiaca> | <Frecuencia respiratoria> |  120/80          |

    Then se muestra un mensaje de error indicando <Mensaje de error>

    Examples:
      | Frecuencia Cardiaca | Frecuencia respiratoria | Mensaje de error                                    |
      | -70                 | 15                      | "La frecuencia cardiaca no puede ser negativa"      |
      | 70                  | -15                     | "La frecuencia respiratoria no puede ser negativa"  |

    # 5 y 6. Orden de prioridad: baja prioridad (ya esta) -> media prioridad (se ingresa) -> alta prioridad (ya esta)
  Scenario: Ingreso de pacientes con diferente niveles de emergencia
    Given que están ingresados en la guardia los siguientes pacientes:
      | CUIT          | Informe                     | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 20-43772929-9 | Le agarro dengue            | Emergencia          | 38          | 70                  | 15                      | 120/80           |
      | 26-12345678-0 | Dolor de cabeza persistente | Urgencia Menor      | 38          | 70                  | 15                      | 120/80           |

    When ingresa a la guardia el siguiente paciente:
      | CUIT          | Informe                     | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 24-87654321-1 | Dolor de cabeza intenso     | Urgencia            | 38          | 70                  | 15                      | 120/80           |

    Then la lista de espera esta ordenada por nivel de emergencia de la siguiente manera:
      | 20-43772929-9 |
      | 24-87654321-1 |
      | 26-12345678-0 |

    # 7. Ordenamiento de pacientes con igual prioridad
  Scenario: Ingresa un paciente de igual prioridad que uno que ya esta en la lista de espera
    Given que están ingresados en la guardia los siguientes pacientes:
      | CUIT          | Informe                     | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 20-43772929-9 | Le agarro dengue            | Emergencia          | 38          | 70                  | 15                      | 120/80           |

    When ingresa a la guardia el siguiente paciente:
      | CUIT          | Informe                     | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 24-87654321-1 | Le agarro dengue            | Emergencia          | 38          | 70                  | 15                      | 120/80           |

    Then la lista de espera esta ordenada por nivel de emergencia de la siguiente manera:
      | CUIT          |
      | 20-43772929-9 |
      | 27-44856678-1 |

    # 8. Priorización de paciente que excedieron el máximo de tiempo de espera
  Scenario: Un paciente en la lista de espera de la guardia excedió el tiempo máximo de espera
    Given que están ingresados en la guardia los siguientes pacientes:
      | CUIT          | Informe                     | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial | Hora de ingreso |
      | 20-43772929-9 | Le agarro dengue            | Emergencia          | 38          | 70                  | 15                      | 120/80           | 09:30           |
      | 21-88544755-2 | Le agarro dengue            | Emergencia          | 38          | 70                  | 15                      | 120/80           | 09:00           |
      | 24-87654321-1 | Dolor de cabeza intenso     | Urgencia            | 38          | 70                  | 15                      | 120/80           | 09:30           |
      | 26-12345678-0 | Dolor de cabeza persistente | Urgencia            | 38          | 70                  | 15                      | 120/80           | 08:30           |

    When la hora es "09:35"

    Then la lista de espera esta ordenada por nivel de emergencia de la siguiente manera:
      | 21-88544755-2 |
      | 26-12345678-0 |
      | 20-43772929-9 |
      | 24-87654321-1 |


