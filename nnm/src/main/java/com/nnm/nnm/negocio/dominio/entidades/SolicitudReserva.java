package com.nnm.nnm.negocio.dominio.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitud_reserva")
public class SolicitudReserva extends Reserva {

    @Column(name = "confirmada", nullable = false)
    private Boolean confirmada;

    public SolicitudReserva() {
        this.confirmada = false; // por defecto no confirmada
    }

    // Constructor que copia los datos de la reserva
    public SolicitudReserva(Reserva reserva) {
        super(reserva.getId(), reserva.getInmueble(), reserva.getInquilino(),
              reserva.getFechaInicio(), reserva.getFechaFin(), reserva.getPoliticaCancelacion());
        this.confirmada = false;
    }

    public Boolean getConfirmada() {
        return confirmada;
    }

    public void setConfirmada(Boolean confirmada) {
        this.confirmada = confirmada;
    }
}