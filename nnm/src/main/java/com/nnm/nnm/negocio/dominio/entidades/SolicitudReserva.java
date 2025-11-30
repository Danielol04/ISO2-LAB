package com.nnm.nnm.negocio.dominio.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitud_reserva")
@PrimaryKeyJoinColumn(name = "id")
public class SolicitudReserva extends Reserva {

    @Column(name = "confirmada", nullable = false)
    private Boolean confirmada;

    public SolicitudReserva() {
        this.confirmada = false;
    }

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