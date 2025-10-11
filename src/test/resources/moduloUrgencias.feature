Feature: Modulo de Urgencia
  Esta feature esta relacionada al registro de ingresos de pacientes en la sala de urgencias
  respetando su nivel de prioridad y el horario de llegada

  Background:
    Given que la enfermera esta registrada:
      | Nombre Enfermera | Apellido Enfermera |
      | Susana           | Gimenez            |


  Scenario: Ingreso del primer paciente a la lista de espera de urgencias
    Given que estan registrados los siguientes pacientes en el sistema:
      | CUIT          | Apellido Paciente | Nombre Paciente | Obra Social       |
      | 20-43772929-9 | Villagra          | Mauro           | Subsidio de salud |
      | 26-12345678-0 | Perez             | Maria           | Swiss medical     |

    When ingresa a urgencias el siguiente paciente:
      | CUIT          | Informe          | Nivel de Emergencia | Temperatura | Frecuencia Cardiaca | Frecuencia Respiratoria | Tension Arterial |
      | 20-43772929-9 | Le agarro dengue | Emergencia          | 38          | 70                  | 15                      | 120/80           |

    Then la lista de espera esta ordenada por cuil de la siguiente manera:
      | 20-43772929-9 |



