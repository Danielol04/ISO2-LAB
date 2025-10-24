package com.nnm.nnm.negocio.dominio.entidades;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@Entity
@Table(name = "Disponibilidad")
public class Disponibilidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="Id", nullable = false, unique = true)
    private long Id;

    @ManyToOne
    @JoinColumn(name= "id_inmueble", referencedColumnName = "id_inmueble", nullable = false)

    @Column(name ="id_inmueble", nullable = false)
    private long id_inmueble;

    @Column(name = "fecha_inicio", nullable= false)
    private String fecha_inicio;

    @Column(name = "fecha_fin", nullable = false)
    private String fecha_fin;

    @Column(name = "precio", nullable = false)
    private double precio;

    @Column(name= "reserva_directa", nullable =  false)
    private boolean reserva_directa;

    @Column( name= "politica_cancelacion", nullable = false)
    private String politica_cancelacion;

    public Disponibilidad(){}
    
    public Disponibilidad(long Id, long id_mueble, String fecha_inicio, String fecha_fin, double precio, boolean reserva_directa, String politica_cancelacion ){
       this.Id= Id;
       this.id_inmueble = id_mueble;
       this.fecha_inicio= fecha_inicio;
       this.fecha_fin= fecha_fin;
       this.precio= precio;
       this.reserva_directa = reserva_directa;
       this.politica_cancelacion = politica_cancelacion;

    }

    public long getId(){
        return Id;
    }
    public long getId_inmueble(){
        return id_inmueble;
    }
    public String getFecha_inicio(){
        return fecha_inicio;
    }
    public String getFecha_fin(){
        return fecha_fin;
    }
    public double getPrecio(){
        return precio;
    }
    public boolean getReserva_directa(){
        return reserva_directa;
    }
    public String getPolitica_cancelacion(){
        return politica_cancelacion;
    }


}
