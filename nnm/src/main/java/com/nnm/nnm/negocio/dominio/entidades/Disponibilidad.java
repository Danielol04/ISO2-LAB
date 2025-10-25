package com.nnm.nnm.negocio.dominio.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "Disponibilidad")
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id", nullable = false, unique = true)
    private long Id;

    @Column(name ="id_inmueble", nullable = false)
    private long id_inmueble; // mantenemos la columna de la base de datos

    @Column(name = "fecha_inicio", nullable= false)
    private String fecha_inicio;

    @Column(name = "fecha_fin", nullable = false)
    private String fecha_fin;

    @Column(name = "precio", nullable = false)
    private double precio;

    @Column(name= "reserva_directa", nullable =  false)
    private boolean reserva_directa;

    @Column( name= "politica_cancelacion", nullable = false)
    private String politica_cancelacion;

    public Disponibilidad(){}

    public Disponibilidad(long Id, long id_inmueble, String fecha_inicio, String fecha_fin, double precio, boolean reserva_directa, String politica_cancelacion ){
       this.Id = Id;
       this.id_inmueble = id_inmueble;
       this.fecha_inicio = fecha_inicio;
       this.fecha_fin = fecha_fin;
       this.precio = precio;
       this.reserva_directa = reserva_directa;
       this.politica_cancelacion = politica_cancelacion;
    }

    // Getters y setters
    public long getId() { return Id; }
    public long getId_inmueble() { return id_inmueble; }
    public void setId_inmueble(long id_inmueble) { this.id_inmueble = id_inmueble; }
    public String getFecha_inicio() { return fecha_inicio; }
    public void setFecha_inicio(String fecha_inicio) { this.fecha_inicio = fecha_inicio; }
    public String getFecha_fin() { return fecha_fin; }
    public void setFecha_fin(String fecha_fin) { this.fecha_fin = fecha_fin; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public boolean getReserva_directa() { return reserva_directa; }
    public void setReserva_directa(boolean reserva_directa) { this.reserva_directa = reserva_directa; }
    public String getPolitica_cancelacion() { return politica_cancelacion; }
    public void setPolitica_cancelacion(String politica_cancelacion) { this.politica_cancelacion = politica_cancelacion; }
}
