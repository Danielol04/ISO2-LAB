package com.nnm.nnm.negocio.dominio.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitud_reserva")
public class SolicitudReserva {
    @Id
    //@ManyToOne
    //@JoinColumn(name = "id_reserva", referencedColumnName = "id_reserva" )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")  
    private Long idSolicitudReserva;
    @ManyToOne
    @JoinColumn(name="id_inmueble", referencedColumnName = "id")
    private Inmueble id_inmueble;
    @Column(name="confirmada", nullable = false, unique = false)
    private Boolean confirmada;

    public SolicitudReserva() {
    }

    public SolicitudReserva(Long idSolicitudReserva, Inmueble id_inmueble, Boolean confirmada) {
        this.idSolicitudReserva = idSolicitudReserva;
        this.id_inmueble = id_inmueble;
        this.confirmada = confirmada;
    }
    public Long getIdSolicitudReserva() {
        return idSolicitudReserva;
    }
    public void setIdSolicitudReserva(Long idSolicitudReserva) {
        this.idSolicitudReserva = idSolicitudReserva;
    }
    public Inmueble getId_inmueble() {
        return id_inmueble;
    }
    public void setId_inmueble(Inmueble id_inmueble) {
        this.id_inmueble = id_inmueble;
    }
    public Boolean getConfirmada() {
        return confirmada;
    }
    public void setConfirmada(Boolean confirmada) {
        this.confirmada = confirmada;
    }
}