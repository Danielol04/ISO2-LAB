package com.nnm.nnm.persistencia;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class InmuebleDAO {

    @PersistenceContext
    private EntityManager em;

    public void save(Inmueble inmueble) {
        em.persist(inmueble);
    }

    public List<Inmueble> findAll() {
        return em.createQuery("SELECT i FROM Inmueble i", Inmueble.class)
                 .getResultList();
    }

    public Inmueble findById(Long id) {
        return em.find(Inmueble.class, id);
    }

    public List<Inmueble> findByPropietario(String username) {
        return em.createQuery("SELECT i FROM Inmueble i WHERE i.propietario.username = :username", Inmueble.class)
                 .setParameter("username", username)
                 .getResultList();
    }

    /**
     * Búsqueda filtrada completa (equivalente al anterior selectList).
     */
    public List<Inmueble> buscarFiltrado(
            String localidad,
            String provincia,
            String titulo,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            String tipo,
            Integer habitaciones,
            Integer banos,
            Boolean reservaDirecta,
            PoliticaCancelacion politica
    ) {
        List<Inmueble> inmuebles = em.createQuery("SELECT DISTINCT i FROM Inmueble i LEFT JOIN FETCH i.disponibilidades d", Inmueble.class)
                                     .getResultList();

        return inmuebles.stream()
                .filter(i -> localidad == null || localidad.isEmpty() ||
                        i.getLocalidad().toLowerCase().contains(localidad.toLowerCase()))
                .filter(i -> provincia == null || provincia.isEmpty() ||
                        i.getProvincia().toLowerCase().contains(provincia.toLowerCase()))
                .filter(i -> titulo == null || titulo.isEmpty() ||
                        i.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .filter(i -> tipo == null || tipo.isEmpty() ||
                        i.getTipo_inmueble().toLowerCase().contains(tipo.toLowerCase()))
                .filter(i -> habitaciones == null || i.getHabitaciones() == habitaciones)
                .filter(i -> banos == null || i.getNumero_banos() == banos)
                .filter(i -> reservaDirecta == null || i.getDisponibilidades().stream()
                        .anyMatch(d -> d.getReservaDirecta() == reservaDirecta))
                .filter(i -> politica == null || i.getDisponibilidades().stream()
                        .anyMatch(d -> d.getPoliticaCancelacion() == politica))
                .filter(i -> fechaInicio == null || i.getDisponibilidades().stream()
                        .anyMatch(d -> !d.getFechaInicio().isAfter(fechaInicio)))
                .filter(i -> fechaFin == null || i.getDisponibilidades().stream()
                        .anyMatch(d -> !d.getFechaFin().isBefore(fechaFin)))
                .collect(Collectors.toList());
    }
}
