package com.nnm.nnm.negocio.dominio.entidades;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "inquilino")
@PrimaryKeyJoinColumn(name = "username") 
public class Inquilino extends Usuario {

    @OneToOne
    @JoinColumn(name = "id_lista_deseos", referencedColumnName = "id")
    private ListaDeseos listaDeseos;

    @OneToMany(mappedBy = "inquilino", cascade = CascadeType.ALL)
    public List<Reserva> reservas;

    public Inquilino() {}
    public Inquilino(String username, String password, String correo, String nombre, String apellidos, String direccion) {
        super(username, password, correo, nombre, apellidos, direccion);
    }

    public ListaDeseos getListaDeseos() {
        return listaDeseos;
    }
    public void setListaDeseos(ListaDeseos listaDeseos) {
        this.listaDeseos = listaDeseos;
    }
    public List<Reserva> getReservas() {
        return reservas;
    }
    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
    public void añadirReserva(Reserva reserva) {
        this.reservas.add(reserva);
    }

}
