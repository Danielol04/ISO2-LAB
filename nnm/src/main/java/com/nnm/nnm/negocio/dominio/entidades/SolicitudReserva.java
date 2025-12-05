package com.nnm.nnm.negocio.dominio.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitud_reserva")
public class SolicitudReserva {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "id_reserva", nullable = false)
    private Long idReserva;

    @Column(name = "confirmada", nullable = false)
    private Boolean confirmada=false;

    public SolicitudReserva() {
    }

    public SolicitudReserva(Long id,Long idReserva) {
        this.id = id;
        this.idReserva = idReserva;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getIdReserva() {
        return idReserva;
    }
    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }
    public Boolean getConfirmada() {
        return confirmada;
    }

    public void setConfirmada(Boolean confirmada) {
        this.confirmada = confirmada;
    }
}