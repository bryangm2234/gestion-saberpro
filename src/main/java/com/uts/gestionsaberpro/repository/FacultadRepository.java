package com.uts.gestionsaberpro.repository;
import com.uts.gestionsaberpro.entity.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FacultadRepository extends JpaRepository<Facultad, Long> {}
