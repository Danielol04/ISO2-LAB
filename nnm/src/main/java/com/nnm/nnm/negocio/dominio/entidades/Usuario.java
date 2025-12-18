package com.nnm.nnm.negocio.dominio.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "usuario")
public abstract class Usuario {
    @Id 
    @Column(name ="username", nullable = false, unique = true)
    protected String username;
    @Column(name = "password",nullable = false)
    protected String password;
    @Column(name = "correo", nullable = false)
    protected String correo;
    @Column(name = "nombre", nullable = false)
    protected String nombre;
    @Column(name = "apellidos", nullable = false)
    protected String apellidos;
    @Column(name = "direccion", nullable = false)
    protected String direccion;

    public Usuario() {}
    
    public Usuario(String username, String password, String correo, String nombre, String apellidos, String direccion) {
        this.username = username;
        this.password = password;
        this.correo = correo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

}