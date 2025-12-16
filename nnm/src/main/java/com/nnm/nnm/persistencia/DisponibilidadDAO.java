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
        LocalDate fechaFinAdj = fechaFin.plusDays(1);
        LocalDate fechaInicioAdj = fechaInicio.minusDays(1);
        String jpql ="FROM Disponibilidad d WHERE d.inmueble.id = :idInmueble AND d.politicaCancelacion = :politica_cancelacion AND d.reservaDirecta = :reservaDirecta AND (d.fechaInicio = :diaDespuesFin OR d.fechaFin = :diaAntesInicio)";
    
        return gestorBD.selectList(jpql, Disponibilidad.class, 
            "idInmueble", idInmueble,
            "politica_cancelacion", politicaCancelacion,
            "reservaDirecta",reservaDirecta,
            "diaAntesInicio", fechaInicioAdj,
            "diaDespuesFin", fechaFinAdj);
    }

    public Disponibilidad findDisponibilidadparaReserva(long idInmueble, LocalDate reservaInicio, LocalDate reservaFin) {
        String jpql = "FROM Disponibilidad d " +
                  "WHERE d.inmueble.id = :idInmueble " +
                  "AND d.fechaInicio <= :reservaInicio " +
                  "AND d.fechaFin >= :reservaFin";
    return gestorBD.selectSingle(jpql, Disponibilidad.class, "idInmueble", idInmueble, 
        "reservaInicio", reservaInicio, "reservaFin", reservaFin);
    }
}
