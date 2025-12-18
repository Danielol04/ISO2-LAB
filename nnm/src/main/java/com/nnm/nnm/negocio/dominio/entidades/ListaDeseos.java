package com.nnm.nnm.negocio.dominio.entidades;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "lista_deseos")

public class ListaDeseos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true)
    private long id;

    @OneToOne
    @JoinColumn(name= "username_inquilino", referencedColumnName = "username", nullable = false, unique = true)
    private Inquilino inquilino;

    @ManyToMany
    @JoinTable(name = "lista_deseos_inmueble",joinColumns = @JoinColumn(name = "id_lista_deseos"),
    inverseJoinColumns = @JoinColumn(name = "id_inmueble"))
    private Set<Inmueble> inmuebles = new HashSet<>();


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Inquilino getInquilino() {
        return inquilino;
    }
    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
    }
    public Set<Inmueble> getInmuebles() {
        return inmuebles;
    }
}
