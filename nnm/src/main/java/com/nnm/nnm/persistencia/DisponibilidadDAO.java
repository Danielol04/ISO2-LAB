package com.nnm.nnm.persistencia;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;

@Repository
public class DisponibilidadDAO extends EntidadDAO<Disponibilidad, Long> {

    public DisponibilidadDAO() {
        super(Disponibilidad.class);
    }

    public List<Disponibilidad> findByInmueble(long idInmueble) {
        String jpql = "FROM Disponibilidad d WHERE d.inmueble.id = :idInmueble";
        return gestorBD.selectList(jpql, Disponibilidad.class, "idInmueble", idInmueble);
    }

    public List<Disponibilidad> encontrarAdyacentes(long idInmueble,PoliticaCancelacion politicaCancelacion,boolean reservaDirecta, LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDate fechaInicioAdj = fechaFin.plusDays(1);
        LocalDate fechaFinAdj = fechaInicio.minusDays(1);
        String jpql ="FROM Disponibilidad d WHERE d.inmueble.id = :idInmueble AND d.politicaCancelacion = :politica_cancelacion AND (d.fechaInicio = :fechaFin OR d.fechaFin = :fechaInicio)";
    
        return gestorBD.selectList(jpql, Disponibilidad.class, 
            "idInmueble", idInmueble,
            "politica_cancelacion", politicaCancelacion,
            "reservaDirecta",reservaDirecta,
            "fechaInicio", fechaInicioAdj,
            "fechaFin", fechaFinAdj);
    }

    public Disponibilidad findDisponibilidadparaReserva(long idInmueble, LocalDate reservaInicio, LocalDate reservaFin) {
        String jpql = "FROM Disponibilidad d " +
                  "WHERE d.inmueble.id = :idInmueble " +
                  "AND d.fechaInicio <= :reservaInicio " +
                  "AND d.fechaFin >= :reservaFin";
    return gestorBD.selectSingle(jpql, Disponibilidad.class, "idInmueble", idInmueble, 
        "reservaInicio", reservaInicio, "reservaFin", reservaFin);
    }

    public void deleteAll(List<Disponibilidad> lista) {
        for (Disponibilidad d : lista) {
            gestorBD.delete(d);
        }
    }

    public void saveAll(List<Disponibilidad> lista) {
        for (Disponibilidad d : lista) {
            save(d);
        }
    }
    
    
    


}
