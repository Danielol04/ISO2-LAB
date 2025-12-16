package com.nnm.nnm.negocio.dominio.entidades;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true)
    private Long id;

    @Column(name = "idReserva", nullable = false)
    private long idReserva;
   
    @Column(name = "referencia", nullable = false)
    private UUID referencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    public Pago() {
    }

    public Pago(Long id, Long idReserva, UUID referencia, MetodoPago metodoPago) {
        this.id = id;
        this.idReserva = idReserva;
        this.referencia = referencia;
        this.metodoPago = metodoPago;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getReserva() {
        return idReserva;
    }
    public void setReserva(Long idReserva) {
        this.idReserva = idReserva;
    }
    public UUID getReferencia() {
        return referencia;
    }
    public void setReferencia(UUID referencia) {
        this.referencia = referencia;
    }
    public MetodoPago getMetodoPago() {
        return metodoPago;
    }
    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

}
