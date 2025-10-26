package com.nnm.nnm.negocio.dominio.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "inquilino")

public class Inquilino extends Usuario {
	@Id
	private Long id;
	
	@OneToOne
	@JoinColumn(name="username")
	private Usuario usuario;
	
    public Inquilino(String login, String pass, String nombre, String apellidos, String direccion) {
        super(login, pass, nombre, apellidos, direccion);
    }
/*
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
*/
}
