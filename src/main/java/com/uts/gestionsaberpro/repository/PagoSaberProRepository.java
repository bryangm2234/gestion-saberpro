package com.uts.gestionsaberpro.repository;

import com.uts.gestionsaberpro.entity.PagoSaberPro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoSaberProRepository extends JpaRepository<PagoSaberPro, Long> {
    List<PagoSaberPro> findByEstudianteId(Long estudianteId);
    List<PagoSaberPro> findByEstado(PagoSaberPro.EstadoPago estado);
    Optional<PagoSaberPro> findTopByEstudianteIdOrderByFechaRegistroDesc(Long estudianteId);
}
