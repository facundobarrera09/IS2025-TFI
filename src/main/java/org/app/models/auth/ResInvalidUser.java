package org.app.models.auth;

import org.app.models.IErrorResponse;
import org.app.models.ingresos.CreateIngreso;

public class ResInvalidUser implements IErrorResponse<LoginData> {

    private String event = "Login falló";
    private String reason;
    private LoginData context;

    public ResInvalidUser(String reason, LoginData context) {
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
    public LoginData getContext() {
        return context;
    }
}
