package com.tingeso.entrega1.repositories;

import com.tingeso.entrega1.entities.Cuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota, Long> {
}
