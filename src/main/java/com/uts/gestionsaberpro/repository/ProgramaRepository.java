package com.uts.gestionsaberpro.repository;

import com.uts.gestionsaberpro.entity.Programa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramaRepository extends JpaRepository<Programa, Long> {
    List<Programa> findByActivoTrue();
}
