package com.nnm.nnm.negocio.dominio.entidades;
import jakarta.persistence.*;g
@Entity
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
