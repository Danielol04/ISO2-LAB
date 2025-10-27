package com.nnm.nnm.persistencia;

import org.springframework.stereotype.Repository;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import java.util.List;

@Repository
public class InmuebleDAO extends EntidadDAO<Inmueble, Long> {

    public InmuebleDAO() {
        super(Inmueble.class);
    }

    public List<Inmueble> findByPropietario(String usernamePropietario) {
        String jpql = "FROM Inmueble i WHERE i.propietario.username = :username";
        return gestorBD.select(jpql, Inmueble.class)
                       .stream()
                       .filter(i -> i.getPropietario().getUsername().equals(usernamePropietario))
                       .toList();
    }
    @Override
    public Inmueble findById(Long id) {
        String jpql = "FROM Inmueble i WHERE i.id = :id";
        return gestorBD.selectSingle(jpql, Inmueble.class, "id", id);
    }

}
