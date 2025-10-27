package com.nnm.nnm.negocio.dominio.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
@Entity
@Table(name = "lista_deseos")

public class ListaDeseos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id", nullable = false, unique = true)
    private long Id;

    @ManyToMany
    @JoinColumn(name= "username_inquilino", referencedColumnName = "username_inquilino", nullable = false)

    @Column(name= "username_inquilino", nullable= false)
    private String username_inquilino;

    @ManyToMany
    @JoinColumn(name= "id_inmueble", referencedColumnName = "id_inmueble", nullable = false)
    
    @Column( name= "id_inmueble", nullable= false)
    private long id_inmueble;



}
