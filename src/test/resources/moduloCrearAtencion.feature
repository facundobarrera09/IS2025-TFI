Feature: Crear atencion
  Como médico
  Quiero registrar un informe de atención de un paciente ingresado en urgencias que he reclamado
  Para dejar constancia de la atención brindada a dicho paciente

  Background:
    Given el médico inicio sesión en el sistema
    And existe un ingreso siendo procesado

  Scenario: El médico ingresa el informe del estudio
    When el médico ingresa en informe
    Then el informe queda registrado
    And el nuevo estado del ingreso es FINALIZADO

  Scenario: El medico no ingreso el informe
    When el médico no ingresa el informe
    Then se alerta que el informe es obligatorio
