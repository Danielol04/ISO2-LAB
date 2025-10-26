package  com.nnm.nnm.negocio.dominio.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;


@Entity
@Table(name = "propietario")
@PrimaryKeyJoinColumn(name = "username")
public class Propietario extends Usuario {

    @OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL)
    private List<Inmueble> inmuebles;

    public Propietario() {}
    public Propietario(String username, String password, String correo, String nombre, String apellidos, String direccion) {
        super(username, password, correo, nombre, apellidos, direccion);
    }

    public List<Inmueble> getInmuebles() {
        return inmuebles;
    }
    public void setInmuebles(List<Inmueble> inmuebles) {
        this.inmuebles = inmuebles;
    }
    public void añadirInmueble(Inmueble inmueble) {
        this.inmuebles.add(inmueble);
    }

}
