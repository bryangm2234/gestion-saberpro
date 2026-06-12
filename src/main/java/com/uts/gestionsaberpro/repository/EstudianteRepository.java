package com.uts.gestionsaberpro.repository;

import com.uts.gestionsaberpro.entity.Estudiante;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    Optional<Estudiante> findByUsuarioCorreo(String correo);
    Optional<Estudiante> findByUsuarioDocumento(String documento);

    List<Estudiante> findByProgramaId(Long programaId);

    @Query("SELECT e FROM Estudiante e " +
           "WHERE e.programa.id = :progId " +
           "ORDER BY e.usuario.primerApellido")
    List<Estudiante> findByProgramaIdOrdenado(@Param("progId") Long progId);

    long countByProgramaId(Long programaId);

    @Query("SELECT COUNT(e) FROM Estudiante e WHERE e.programa.id = :progId AND SIZE(e.resultados) > 0")
    long countConResultadosByPrograma(@Param("progId") Long progId);
}
