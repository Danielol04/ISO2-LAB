package com.nnm.nnm.negocio.dominio.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitud_reserva")
public class SolicitudReserva {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @OneToOne(optional = true)
    @JoinColumn(name = "reserva", referencedColumnName = "id")
    private Reserva reserva;

    @Column(name = "confirmada", nullable = false)
    private Boolean confirmada = false;

    @Column(name = "precioTotal", nullable = false)
    private Double precioTotal;

    @Column(name = "noches", nullable = false)
    private long noches;

    @Column(name = "hora", nullable = false)
    private LocalDateTime fechaCreacion; 

    public SolicitudReserva() {
    }

    public SolicitudReserva(Long id, Reserva reserva, Double precioTotal, long noches, LocalDateTime fechaCreacion) {
        this.id = id;
        this.reserva = reserva;
        this.precioTotal = precioTotal;
        this.noches = noches;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {return id;}	
    public void setId(Long id) {this.id = id;}

    public Reserva getReserva() {return reserva;}
    public void setReserva(Reserva reserva) {this.reserva = reserva;}

    public Boolean getConfirmada() {return confirmada;}
    public void setConfirmada(Boolean confirmada) {this.confirmada = confirmada;}

    public Double getPrecioTotal() {return precioTotal;}
    public void setPrecioTotal(Double precioTotal) {this.precioTotal = precioTotal;}

    public long getNoches() {return noches;}
    public void setNoches(long noches) {this.noches = noches;}

    public LocalDateTime getFechaCreacion() {return fechaCreacion;}
    public void setFechaCreacion(LocalDateTime fechaCreacion) {this.fechaCreacion = fechaCreacion;}
}