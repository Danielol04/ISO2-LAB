package com.nnm.nnm.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nnm.nnm.negocio.dominio.entidades.Usuario;

@Repository
public interface UsuarioDAO extends JpaRepository<Usuario, String> {
}
