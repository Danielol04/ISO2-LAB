package com.nnm.nnm.negocio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.persistencia.GestorBD;

@Service
public class GestorInmuebles {

    @Autowired
    private GestorBD gestorBD;

    // MÉTODOS EXISTENTES (para VentanaDisponibilidad y VentanaInmueble)
    public void registrarInmueble(Inmueble inmueble) {
        gestorBD.insert(inmueble);
    }

    public List<Inmueble> listarInmuebles() {
        return gestorBD.select("SELECT i FROM Inmueble i", Inmueble.class);
    }

    public Inmueble obtenerInmueblePorId(long id) {
        return gestorBD.selectSingle("SELECT i FROM Inmueble i WHERE i.id = :id", Inmueble.class, "id", id);
    }

    public List<Inmueble> listarInmueblesPorPropietario(String propietarioUsername) {
        return gestorBD.selectList("SELECT i FROM Inmueble i WHERE i.propietario.username = :username",
                Inmueble.class, "username", propietarioUsername);
    }

    // --- NUEVO MÉTODO para búsqueda rápida (compatible con /buscar) ---
    public List<Inmueble> buscarFiltradoRapido(String destino, Integer habitaciones, Integer banos) {
        String jpql = "SELECT i FROM Inmueble i WHERE 1=1";

        int count = 0;
        String[] paramNames = new String[3];
        Object[] values = new Object[3];

        if (destino != null && !destino.isEmpty()) {
            jpql += " AND (i.localidad LIKE :destino OR i.provincia LIKE :destino)";
            paramNames[count] = "destino";
            values[count++] = "%" + destino + "%";
        }

        if (habitaciones != null) {
            jpql += " AND i.habitaciones >= :habitaciones";
            paramNames[count] = "habitaciones";
            values[count++] = habitaciones;
        }

        if (banos != null) {
            jpql += " AND i.numero_banos >= :banos";
            paramNames[count] = "banos";
            values[count++] = banos;
        }

        // Reducir arrays si hay menos parámetros
        String[] finalParamNames = new String[count];
        Object[] finalValues = new Object[count];
        System.arraycopy(paramNames, 0, finalParamNames, 0, count);
        System.arraycopy(values, 0, finalValues, 0, count);

        return gestorBD.selectListConParametros(jpql, Inmueble.class, finalParamNames, finalValues);
    }
}
