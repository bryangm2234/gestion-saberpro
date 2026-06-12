package com.uts.gestionsaberpro.repository;

import com.uts.gestionsaberpro.entity.Coordinador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoordinadorRepository extends JpaRepository<Coordinador, Long> {
    Optional<Coordinador> findByUsuarioCorreo(String correo);
}
