package com.uts.gestionsaberpro.repository;

import com.uts.gestionsaberpro.entity.ResultadoSaberPro;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultadoRepository extends JpaRepository<ResultadoSaberPro, Long> {

    @EntityGraph(attributePaths = {"estudiante", "estudiante.usuario", "estudiante.programa"})
    List<ResultadoSaberPro> findByEstudianteId(Long estudianteId);

    @EntityGraph(attributePaths = {"estudiante", "estudiante.usuario", "estudiante.programa"})
    Optional<ResultadoSaberPro> findTopByEstudianteIdOrderByFechaExamenDesc(Long estudianteId);

    @EntityGraph(attributePaths = {"estudiante", "estudiante.usuario", "estudiante.programa"})
    @Query("SELECT r FROM ResultadoSaberPro r WHERE r.tieneBeneficio = true ORDER BY r.puntajeGlobal DESC")
    List<ResultadoSaberPro> findBeneficiarios();

    @EntityGraph(attributePaths = {"estudiante", "estudiante.usuario", "estudiante.programa"})
    @Query("SELECT r FROM ResultadoSaberPro r WHERE r.estudiante.programa.id = :progId ORDER BY r.puntajeGlobal DESC")
    List<ResultadoSaberPro> findByProgramaId(@Param("progId") Long progId);

    @EntityGraph(attributePaths = {"estudiante", "estudiante.usuario", "estudiante.programa"})
    List<ResultadoSaberPro> findAll();
}
