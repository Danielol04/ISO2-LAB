package com.nnm.nnm.negocio.dominio.entidades;

import java.sql.Date;

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
    private Date fecha_inicio;

    @Column(name = "fecha_fin", nullable = false)
    private Date fecha_fin;

    @Column(name = "precio", nullable = false)
    private double precio;

    @Column(name= "reserva_directa", nullable =  false)
    private boolean reserva_directa;

    @Enumerated(EnumType.STRING)
    @Column(name= "politica_cancelacion", nullable = false)
    private PoliticaCancelacion politica_cancelacion;

    // Constructor vacío para JPA
    public Disponibilidad() {}

    // Constructor con todos los campos
    public Disponibilidad(long id, Inmueble inmueble, Date fecha_inicio, Date fecha_fin,
                          double precio, boolean reserva_directa, PoliticaCancelacion politica_cancelacion) {
       this.id = id;
       this.inmueble = inmueble;
       this.fecha_inicio = fecha_inicio;
       this.fecha_fin = fecha_fin;
       this.precio = precio;
       this.reserva_directa = reserva_directa;
       this.politica_cancelacion = politica_cancelacion;
    }

    // Getters y setters
    public long getId() { return id; }

    public Inmueble getInmueble() { return inmueble; }
    public void setInmueble(Inmueble inmueble) { this.inmueble = inmueble; }

    public Date getFecha_inicio() { return fecha_inicio; }
    public void setFecha_inicio(Date fecha_inicio) { this.fecha_inicio = fecha_inicio; }

    public Date getFecha_fin() { return fecha_fin; }
    public void setFecha_fin(Date fecha_fin) { this.fecha_fin = fecha_fin; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public boolean getReserva_directa() { return reserva_directa; }
    public void setReserva_directa(boolean reserva_directa) { this.reserva_directa = reserva_directa; }

    public PoliticaCancelacion getPolitica_cancelacion() { return politica_cancelacion; }
    public void setPolitica_cancelacion(PoliticaCancelacion politica_cancelacion) { this.politica_cancelacion = politica_cancelacion; }
}
