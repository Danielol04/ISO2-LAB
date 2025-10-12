package com.nnm.nnm.negocio.dominio.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "inquilino")
public class Inquilino extends Usuario {

    public Inquilino(String login, String pass, String nombre, String apellidos, String direccion) {
        super(login, pass, nombre, apellidos, direccion);
    }
}