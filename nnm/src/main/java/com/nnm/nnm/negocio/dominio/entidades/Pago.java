package com.nnm.nnm.negocio.dominio.entidades;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
@Entity
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_reserva", referencedColumnName = "id", nullable = false)
    private Reserva reserva;
   
    @Column(name = "referencia", nullable = false)
    private UUID referencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    public Pago() {
    }

    public Pago(Long id, Reserva reserva, UUID referencia, MetodoPago metodoPago) {
        this.id = id;
        this.reserva = reserva;
        this.referencia = referencia;
        this.metodoPago = metodoPago;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Reserva getReserva() {
        return reserva;
    }
    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
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
