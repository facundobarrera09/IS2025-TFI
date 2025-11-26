package org.app.models.paciente;

import org.app.models.IErrorResponse;

public class ResInvalidFindOrCreatePaciente implements IErrorResponse<FindOrCreatePaciente> {
    private final String event = "Buscar o crear paciente falló";
    private final String reason;
    private final FindOrCreatePaciente context;

    public ResInvalidFindOrCreatePaciente(String reason, FindOrCreatePaciente context) {
        this.reason = "La busqueda o creación del paciente falló: " + reason;
        this.context = context;
    }

    @Override
    public String getEvent() {
        return event;
    }

    public String getReason() {
        return reason;
    }

    public FindOrCreatePaciente getContext() {
        return context;
    }
}
