package com.nnm.nnm.negocio.dominio.entidades;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "reserva")
public class Reserva {
    @Id//Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne//Relacion de muchas reservas a un inmueble
    @JoinColumn(name="id_inmueble", referencedColumnName = "id")
    private Inmueble Inmueble;

    @ManyToOne//Relacion de muchas reservas a un usuario
    @JoinColumn(name="inquilino", referencedColumnName = "username")
    private Inquilino inquilino;

    @Column(name="fecha_inicio", nullable = false, unique = false)
    private LocalDate fechaInicio;
    @Column(name="fecha_fin", nullable = false, unique = false)
    private LocalDate fechaFin;
    @Enumerated(EnumType.STRING)
    @Column(name= "politica_cancelacion", nullable = false)
    private PoliticaCancelacion politicaCancelacion;


    public Reserva() {
    }
    
    public Reserva(Long idReserva, Inmueble Inmueble, Inquilino inquilino, LocalDate fechaInicio,
        LocalDate fechaFin, PoliticaCancelacion politicaCancelacion) {
        this.id = idReserva;
        this.Inmueble = Inmueble;
        this.inquilino = inquilino;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.politicaCancelacion = politicaCancelacion;
    }

    public void setId(Long idReserva) {
        this.id = idReserva;
    }
    public Long getId() {
        return id;
    }
    public Inmueble getInmueble() {
        return Inmueble;
    }
    public void setIdInmueble(Inmueble Inmueble) {
        this.Inmueble = Inmueble;
    }
    public Inquilino getInquilino() {
        return inquilino;
    }
    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
    }
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    public PoliticaCancelacion getPoliticaCancelacion() {
        return politicaCancelacion;
    }
    public void setPoliticaCancelacion(PoliticaCancelacion politicaCancelacion) {
        this.politicaCancelacion = politicaCancelacion;
    }

    public boolean isPagado(){
        // Lógica para determinar si la reserva ha sido pagada
        return false; // Valor por defecto, implementar según la lógica de negocio
    }

    public boolean isActiva(){
        // Lógica para determinar si la reserva está activa
        return false; // Valor por defecto, implementar según la lógica de negocio
    }
}