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
}