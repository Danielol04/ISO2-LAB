package com.nnm.nnm.negocio.dominio.entidades;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "inmueble")
public class Inmueble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id", nullable = false, unique = true)
    private long id;

    @ManyToOne
    @JoinColumn(name= "username_propietario", referencedColumnName = "username" ,nullable = false)
    private Propietario propietario;
    
    @Column (name= "titulo", nullable = false)
    private String titulo;

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

    @Column(name= "numero_banos", nullable = false)
    private int numero_banos;

    @Column(name= "habitaciones", nullable = false)
    private int habitaciones;
    
    /*@Lob
    @Column(name = "foto", columnDefinition = "BLOB")
    private byte[] foto;*/


    @OneToMany(mappedBy="inmueble", cascade = CascadeType.ALL)
    private List<Disponibilidad> disponibilidades;

    public Inmueble() {}

    public Inmueble(long id, Propietario username_propietario,String titulo, String tipo_inmueble,
                    String direccion, String localidad, String provincia,
                    String codigo_postal, double precio_noche, int numero_banos, int habitaciones/* ,byte[] foto*/) {
        this.id = id;
        this.propietario = username_propietario;
        this.titulo = titulo;
        this.tipo_inmueble = tipo_inmueble;
        this.direccion = direccion;
        this.localidad = localidad;
        this.provincia = provincia;
        this.codigo_postal = codigo_postal;
        this.precio_noche = precio_noche;
        this.numero_banos = numero_banos;
        this.habitaciones = habitaciones;
        //this.foto = foto;
    }

    public long getId() { return id; }
    public Propietario getPropietario() { return propietario; }
    public String getTitulo() { return titulo; }
    public String getTipo_inmueble() { return tipo_inmueble; }
    public String getDireccion() { return direccion; }
    public String getLocalidad() { return localidad; }
    public String getProvincia() { return provincia; }
    public String getCodigo_postal() { return codigo_postal; }
    public double getPrecio_noche() { return precio_noche; }
    public int getNumero_banos() { return numero_banos; }
    public int getHabitaciones() { return habitaciones; }
    //public byte[] getFoto() { return foto; }
    public List<Disponibilidad> getDisponibilidades() { return disponibilidades; }

    public void setUsername_propietario(Propietario username_propietario) { this.propietario = username_propietario; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setTipo_inmueble(String tipo_inmueble) { this.tipo_inmueble = tipo_inmueble; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public void setCodigo_postal(String codigo_postal) { this.codigo_postal = codigo_postal; }
    public void setPrecio_noche(double precio_noche) { this.precio_noche = precio_noche; }
    public void setNumero_banos(int numero_banos) { this.numero_banos = numero_banos; }
    public void setHabitaciones(int habitaciones) { this.habitaciones = habitaciones; }
    //public void setFoto(byte[] foto) { this.foto = foto; }
    public void setDisponibilidades(List<Disponibilidad> disponibilidades) { this.disponibilidades = disponibilidades; }

}
