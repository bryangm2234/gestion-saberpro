package com.uts.gestionsaberpro.repository;
import com.uts.gestionsaberpro.entity.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    Optional<Docente> findByUsuarioCorreo(String correo);
    List<Docente> findByFacultadId(Long facultadId);
}
