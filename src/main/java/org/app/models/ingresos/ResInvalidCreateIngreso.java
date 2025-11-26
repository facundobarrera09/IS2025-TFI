package org.app.models.ingresos;

import org.app.models.IErrorResponse;

public class ResInvalidCreateIngreso implements IErrorResponse<CreateIngreso> {
    private String event = "Crear ingreso falló";
    private String reason;
    private CreateIngreso context;

    public ResInvalidCreateIngreso(String reason, CreateIngreso context) {
        this.reason = reason;
        this.context = context;
    }

    @Override
    public String getEvent() {
        return event;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public CreateIngreso getContext() {
        return context;
    }
}
