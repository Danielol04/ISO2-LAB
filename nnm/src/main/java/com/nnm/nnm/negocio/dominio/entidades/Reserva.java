package com.nnm.nnm.negocio.dominio.entidades;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @Column(name = "id_reserva", nullable = false, unique = true)
    private Long idReserva;
    @OneToMany
    @JoinColumn(name="id_inmueble", referencedColumnName = "id")
    private Long id_inmueble;
    @OneToMany
    @JoinColumn(name="username_inquilino", referencedColumnName = "username")
    private String username_inquilino;
    @Column(name="fecha_inicio", nullable = false, unique = false)
    private Date fecha_inicio;
    @Column(name="fecha_fin", nullable = false, unique = false)
    private Date fecha_fin;
    @Column(name="politica_cancelacion", nullable = false, unique = false)
    private String politica_cancelacion;
}
