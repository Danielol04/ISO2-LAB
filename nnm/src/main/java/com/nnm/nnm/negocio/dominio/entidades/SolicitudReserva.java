package com.nnm.nnm.negocio.dominio.entidades;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitud_reserva")
public class SolicitudReserva extends Reserva {
    private Boolean confirmada;

    

    public SolicitudReserva() {
    }

    public SolicitudReserva(Long idSolicitudReserva, Inmueble idInmueble, Inquilino inquilino,
            LocalDate fechaInicio, LocalDate fechaFin, PoliticaCancelacion politicaCancelacion,
            Boolean confirmada){
        super(idSolicitudReserva, idInmueble, inquilino, fechaFin, fechaFin, politicaCancelacion);
        this.confirmada = confirmada;
    }
    
    public Boolean getConfirmada() {
        return confirmada;
    }
    public void setConfirmada(Boolean confirmada) {
        this.confirmada = confirmada;
    }
}
