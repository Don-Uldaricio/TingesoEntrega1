package com.tingeso.entrega1.repositories;

import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.entities.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota, Long> {
    @Query("select c from Cuota c where c.idCuota = :idCuota")
    Cuota findById(@Param("idCuota")Integer idCuota);
}
