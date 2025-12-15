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
@Table(name = "disponibilidad")
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn(name ="id_inmueble", referencedColumnName = "id" ,nullable = false)
    private Inmueble inmueble;

    @Column(name = "fecha_inicio", nullable= false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name= "reserva_directa", nullable =  false)
    private boolean reservaDirecta;

    @Enumerated(EnumType.STRING)
    @Column(name= "politica_cancelacion", nullable = false)
    private PoliticaCancelacion politicaCancelacion;


    public Disponibilidad() {}

    public Disponibilidad(Inmueble inmueble, LocalDate fechaInicio, LocalDate fechaFin, PoliticaCancelacion politicaCancelacion, boolean reservaDirecta) {
        this.inmueble = inmueble;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.politicaCancelacion = politicaCancelacion;
        this.reservaDirecta = reservaDirecta;
    }


    // Getters y setters
    public long getId() { return id; }

    public Inmueble getInmueble() { return inmueble; }
    public void setInmueble(Inmueble inmueble) { this.inmueble = inmueble; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fecha_inicio) { this.fechaInicio = fecha_inicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fecha_fin) { this.fechaFin = fecha_fin; }

    public boolean getReservaDirecta() { return reservaDirecta; }
    public void setReservaDirecta(boolean reservaDirecta) { this.reservaDirecta = reservaDirecta; }

    public PoliticaCancelacion getPoliticaCancelacion() { return politicaCancelacion; }
    public void setPoliticaCancelacion(PoliticaCancelacion politica_cancelacion) { this.politicaCancelacion = politica_cancelacion; }
}
