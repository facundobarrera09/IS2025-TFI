package org.app;

import org.app.interfaces.RepositorioPacientes;

public class ServicioUrgencia {

    private RepositorioPacientes DBPacientes;

    public ServicioUrgencia(RepositorioPacientes DBPacientes) {
        this.DBPacientes = DBPacientes;
    }

    public void registrarUrgencia(){
        throw new UnsupportedOperationException("Metodo aun no desarrollado.");
    }
}
