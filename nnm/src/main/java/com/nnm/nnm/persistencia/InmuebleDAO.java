package com.nnm.nnm.persistencia;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;

@Repository
public class InmuebleDAO extends EntidadDAO<Inmueble, Long> {

    public InmuebleDAO() {
        super(Inmueble.class);
    }

    public List<Inmueble> findByPropietario(String username) {
        String jpql = "FROM Inmueble i WHERE i.propietario.username = :username";
        return gestorBD.selectList(jpql, Inmueble.class, "username", username);
    }
    @Override
    public Inmueble findById(Long id) {
        String jpql = "FROM Inmueble i WHERE i.id = :id";
        return gestorBD.selectSingle(jpql, Inmueble.class, "id", id);
    }

}
