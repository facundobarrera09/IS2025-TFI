Feature: Módulo de Urgencias
  Como enfermera
  Quiero registrar las admisiones de pacientes a urgencias
  Para determinar qué pacientes tienen mayor prioridad de atención

  Background:
    Given que existe una enfermera identificada con id "enf-001”
    And que el sistema genera automáticamente la "fecha de ingreso" al registrar una admisión

 # 1. Paciente existe -> admisión registrada y en cola
  Scenario: Registrar ingreso de paciente existente
    Given que el paciente con documento "DNI-123" existe en el sistema
    When la enfermera "enf-001" registra el ingreso del paciente "DNI-123" con:
      | informe                   | Nivel de emergencia | temperatura | frecuenciaCardiaca | frecuenciaRespiratoria | tensionSistolica | tensionDiastolica |
      | "Dolor abdominal leve"    | "Urgencia"          | 36.7        | 72                 | 18                     | 120              | 80                |
    Then el ingreso se guarda con estado "PENDIENTE".
    And el nivel de emergencia "Urgencia" queda asignado al ingreso.
    And el ingreso queda en la cola de atención.
    And la enfermera responsable del ingreso es "enf-001".


  # 2. Paciente no existe -> crear paciente antes de registrar ingreso
  Scenario: Registrar ingreso cuando el paciente no existe
    Given que no existe un paciente con documento "DNI-999" en el sistema
    When la enfermera "enf-001" intenta registrar el ingreso del paciente "DNI-999" con datos válidos:
      | informe                | Nivel de emergencia | temperatura | frecuenciaCardiaca | frecuenciaRespiratoria | tensionSistolica | tensionDiastolica |
      | "Traumatismo leve"     | "Urgencia Menor"    | 37.0        | 68                 | 16                     | 110              | 70                |
    Then el sistema crea el paciente con documento "DNI-999".
    And el ingreso se registra con estado "PENDIENTE".
    And el ingreso queda en la cola de atención.

  # 3. Falta dato obligatorio -> error indicando cuál falta
  Scenario: Error al omitir dato mandatorio en el ingreso
    Given que el paciente con documento "DNI-123" existe en el sistema
    When la enfermera "enf-001" intenta registrar el ingreso del paciente "DNI-123" omitiendo el campo "informe"
    Then el sistema rechaza el registro de ingreso.
    And se muestra un mensaje de error indicando "informe es obligatorio".

  # 4. Valores negativos en frecuencias -> error
  Scenario: Error cuando frecuencia cardiaca o respiratoria son negativas
    Given que el paciente con documento "DNI-123" existe en el sistema
    When la enfermera "enf-001" intenta registrar el ingreso con frecuenciaCardiaca = -10 y frecuenciaRespiratoria = 20
    Then el sistema rechaza el registro de ingreso.
    And se muestra un mensaje de error indicando "frecuencia cardíaca no puede ser negativa".

  Scenario: Error cuando frecuencia respiratoria es negativa
    Given que el paciente con documento "DNI-123" existe en el sistema
    When la enfermera "enf-001" intenta registrar el ingreso con frecuenciaCardiaca = 80 y frecuenciaRespiratoria = -5
    Then el sistema rechaza el registro de ingreso.
    And se muestra un mensaje de error indicando "frecuencia respiratoria no puede ser negativa".


 # 5. Orden de prioridad: A con mayor prioridad que B -> A atendido antes que B
  Scenario: Nuevo ingreso con mayor prioridad que paciente en espera
    Given en la cola de espera existe un paciente "DNI-200" con nivel de emergencia "Urgencia Menor" y fechaIngreso "2025-10-07T10:00:00"
    When la enfermera "enf-001" registra el ingreso del paciente "DNI-201" con nivel de emergencia "Crítica" y fechaIngreso generada por el sistema
    Then el nuevo ingreso "DNI-201" debe tener prioridad por encima de "DNI-200".
    And al ordenar la cola, "DNI-201" aparece antes que "DNI-200".


  # 6. Orden de prioridad: A con menor prioridad que B -> B atendido antes que A
  Scenario: Nuevo ingreso con menor prioridad que paciente en espera
    Given en la cola de espera existe un paciente "DNI-300" con nivel de emergencia "Emergencia" y fechaIngreso "2025-10-07T09:00:00"
    When la enfermera "enf-001" registra el ingreso del paciente "DNI-301" con nivel de emergencia "Sin Urgencia" and fechaIngreso generada por el sistema
    Then "DNI-300" mantiene prioridad por encima de "DNI-301".
    And al ordenar la cola, "DNI-300" aparece antes que "DNI-301".

 # 7. Orden de prioridad cuando niveles son iguales -> atender primero el que llegó antes
  Scenario: Misma prioridad, ordenar por fecha y hora de ingreso
    Given en la cola de espera existe un paciente "DNI-400" con nivel de emergencia "Urgencia" y fechaIngreso "2025-10-07T08:30:00"
    When la enfermera "enf-001" registra el ingreso del paciente "DNI-401" con nivel de emergencia "Urgencia" y la fecha de ingreso generada por el sistema es "2025-10-07T09:00:00"
    Then al ordenar la cola por prioridad y fechaIngreso, "DNI-400" aparece antes que "DNI-401".

  # 8. Verificar formato de tensión arterial y campos mandatorios (combinado)
  Scenario: Validar formato de tensión y campos mandatorios
    Given que el paciente con documento "DNI-123" existe en el sistema
    When la enfermera "enf-001" registra el ingreso con tensión "120/80" y todos los campos mandatorios completados
    Then el ingreso se registra correctamente con tensión sistólica=120 y diastólica=80.
