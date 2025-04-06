package com.tingeso.entrega1.repositories;

import com.tingeso.entrega1.entities.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
}
