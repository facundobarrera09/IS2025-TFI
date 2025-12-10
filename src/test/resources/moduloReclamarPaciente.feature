Feature: Reclamar Paciente
  Como médico
  Quiero reclamar el próximo paciente que debe ser atendido
  Para sacarlo de la lista de espera y poder registrar un informe de atención

  Background:
    Given el médico inicio sesión

  Scenario: Hay pacientes en la lista de espera
    Given existe la siguiente lista de espera
    | CUIT          | Apellido | Nombre  | Informe                 | Nivel de emergencia |
    | 20-43216549-1 | Barrera  | Facundo | Dolor de cabeza intenso | Critica             |
    | 20-43772929-9 | Villagra | Mauro   | Dolor de cabeza medio   | Emergencia          |
    | 21-88544755-2 | Vera     | Maia    | Dolor de cabeza leve    | Urgencia            |

    When el médico reclama un paciente

    Then se obtiene el paciente con cuit "20-43216549-1"
    And el nuevo estado del ingreso es EN_PROCESO
    And el paciente con CUIT "20-43216549-1" ya no está en la lista de espera

  Scenario: No hay pacientes en la lista de espera
    Given existe la siguiente lista de espera
      | CUIT     | Fecha de ingreso | Informe | Nivel de emergencia |

    When el médico reclama un paciente

    Then se advierte que no hay pacientes en espera