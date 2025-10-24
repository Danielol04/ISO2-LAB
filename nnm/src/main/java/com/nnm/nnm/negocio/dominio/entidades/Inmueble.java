package com.nnm.nnm.negocio.dominio.entidades;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "inmueble")
public class Inmueble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name= "Id", nullable = false, unique = true)
    private long Id;
    @ManyToOne
    @JoinColumn(name = "username_propietario", referencedColumnName = "username", nullable = false)
    private Propietario propietario;
 
    @Column( name= "username_propietario", nullable = false)
    private String username_propietario;
    @Column( name= "tipo_inmueble", nullable= false )
    private String tipo_inmueble;
    @Column(name= "direccion", nullable = false)
    private String direccion;
    @Column(name= "localidad", nullable= false)
    private String localidad;
    @Column(name = "provincia", nullable = false)
    private String provincia;
    @Column(name = "codigo_postal", nullable = false)
    private String codigo_postal;
    @Column(name = "precio_noche", nullable = false)
    private double precio_noche;

    public Inmueble(){}

    public Inmueble(long Id, String username_propietario, String tipo_inmueble, String direccion, String localidad, String provincia, String codigo_postal, double precio_noche ){
    this.Id =Id;
    this.username_propietario = username_propietario;
    this.direccion= direccion;
    this.localidad = localidad;
    this.provincia = provincia;
    this.codigo_postal = codigo_postal;
    this.precio_noche = precio_noche;

    
    }
    public long getId(){
        return Id;
    }
    public String getUsername_propietario(){
        return username_propietario;
    }
    public String getDireccion(){
        return direccion;
    }
    public String getLocalidad(){
        return localidad;
    }
    public String getProvincia(){
        return provincia;
    }
    public String getCodigo_postal(){
        return codigo_postal;

    }
    public double getPrecio_noche(){
        return precio_noche;
    }


}
