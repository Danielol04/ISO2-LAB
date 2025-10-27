package com.nnm.nnm.negocio.dominio.entidades;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "reserva")
public class Reserva {
    @Id//Clave primaria
    @Column(name = "id_reserva", nullable = false, unique = true)
    private Long idReserva;
    @ManyToOne//Relacion de muchas reservas a un inmueble
    @JoinColumn(name="id_inmueble", referencedColumnName = "id")
    private Inmueble id_inmueble;
    @ManyToOne//Relacion de muchas reservas a un usuario
    @JoinColumn(name="username_inquilino", referencedColumnName = "username")
    private Usuario username_inquilino;
    @Column(name="fecha_inicio", nullable = false, unique = false)
    private Date fecha_inicio;
    @Column(name="fecha_fin", nullable = false, unique = false)
    private Date fecha_fin;
    @Column(name="politica_cancelacion", nullable = false, unique = false)
    private String politica_cancelacion;

    public Reserva() {
    }
    
    public Reserva(Long idReserva, Inmueble id_inmueble, Usuario username_inquilino, Date fecha_inicio,
        Date fecha_fin, String politica_cancelacion) {
        this.idReserva = idReserva;
        this.id_inmueble = id_inmueble;
        this.username_inquilino = username_inquilino;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.politica_cancelacion = politica_cancelacion;

    }

    public void setId(Long idReserva) {
        this.idReserva = idReserva;
    }
    public Long getId() {
        return idReserva;
    }
    public Inmueble getId_inmueble() {
        return id_inmueble;
    }
    public void setId_inmueble(Inmueble id_inmueble) {
        this.id_inmueble = id_inmueble;
    }
    public Usuario getUsername_inquilino() {
        return username_inquilino;
    }
    public void setUsername_inquilino(Usuario username_inquilino) {
        this.username_inquilino = username_inquilino;
    }
    public Date getFecha_inicio() {
        return fecha_inicio;
    }
    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }
    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }
    public String getPolitica_cancelacion() {
        return politica_cancelacion;
    }
    public void setPolitica_cancelacion(String politica_cancelacion) {
        this.politica_cancelacion = politica_cancelacion;
    }
}