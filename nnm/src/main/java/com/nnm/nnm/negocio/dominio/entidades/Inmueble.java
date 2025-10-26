package com.nnm.nnm.negocio.dominio.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "inmueble")
public class Inmueble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "Id", nullable = false, unique = true)
    private long Id;

    @Column(name= "username_propietario", nullable = false)
    private String username_propietario;

    @Column(name= "tipo_inmueble", nullable= false)
    private String tipo_inmueble;

    @Column(name= "direccion", nullable = false)
    private String direccion;

    @Column(name= "localidad", nullable= false)
    private String localidad;

    @Column(name= "provincia", nullable = false)
    private String provincia;

    @Column(name= "codigo_postal", nullable = false)
    private String codigo_postal;

    @Column(name= "precio_noche", nullable = false)
    private double precio_noche;

    public Inmueble() {}

    public Inmueble(long Id, String username_propietario, String tipo_inmueble,
                    String direccion, String localidad, String provincia,
                    String codigo_postal, double precio_noche) {
        this.Id = Id;
        this.username_propietario = username_propietario;
        this.tipo_inmueble = tipo_inmueble;
        this.direccion = direccion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.codigo_postal = codigo_postal;
        this.precio_noche = precio_noche;
    }

    public long getId() { return Id; }
    public String getUsername_propietario() { return username_propietario; }
    public String getTipo_inmueble() { return tipo_inmueble; }
    public String getDireccion() { return direccion; }
    public String getLocalidad() { return localidad; }
    public String getProvincia() { return provincia; }
    public String getCodigo_postal() { return codigo_postal; }
    public double getPrecio_noche() { return precio_noche; }

    public void setUsername_propietario(String username_propietario) { this.username_propietario = username_propietario; }
    public void setTipo_inmueble(String tipo_inmueble) { this.tipo_inmueble = tipo_inmueble; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public void setCodigo_postal(String codigo_postal) { this.codigo_postal = codigo_postal; }
    public void setPrecio_noche(double precio_noche) { this.precio_noche = precio_noche; }
}
