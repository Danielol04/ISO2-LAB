package com.nnm.nnm.negocio.dominio.entidades;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SOLICITUD") // Diferencia esta subclase en la tabla reserva
public class SolicitudReserva extends Reserva {

    @Column(name = "confirmada")
    private Boolean confirmada;

    public SolicitudReserva() {
        super();
    }

    public SolicitudReserva(Long idReserva, Inmueble inmueble, Inquilino inquilino,
                            LocalDate fechaInicio, LocalDate fechaFin,
                            PoliticaCancelacion politicaCancelacion, Boolean confirmada) {
        super(idReserva, inmueble, inquilino, fechaInicio, fechaFin, politicaCancelacion);
        this.confirmada = confirmada;
    }

    public Boolean getConfirmada() {
        return confirmada;
    }

    public void setConfirmada(Boolean confirmada) {
        this.confirmada = confirmada;
    }
}
