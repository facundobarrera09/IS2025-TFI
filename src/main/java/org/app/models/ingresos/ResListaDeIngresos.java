package org.app.models.ingresos;

import org.domain.models.Ingreso;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.PriorityQueue;

public class ResListaDeIngresos {
    private LocalDateTime fechaDeConsulta;
    private PriorityQueue<Ingreso> listaDeIngresos;

    public ResListaDeIngresos(LocalDateTime fechaDeConsulta, PriorityQueue<Ingreso> listaDeIngresos) {
        this.fechaDeConsulta = fechaDeConsulta;
        this.listaDeIngresos = listaDeIngresos;
    }

    public LocalDateTime getFechaDeConsulta() {
        return fechaDeConsulta;
    }

    public PriorityQueue<Ingreso> getListaDeIngresos() {
        return listaDeIngresos;
    }
}
