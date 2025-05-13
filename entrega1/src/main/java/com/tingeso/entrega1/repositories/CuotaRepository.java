package com.tingeso.entrega1.repositories;

import com.tingeso.entrega1.entities.Cuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota, Long> {
    @Query("select c from Cuota c where c.idCuota = :idCuota")
    Cuota findById(@Param("idCuota")Integer idCuota);

    @Query("select c from Cuota c where c.idArancel = :idArancel")
    ArrayList<Cuota> findByIdArancel(@Param("idArancel")Long idArancel);

    @Query("select c from Cuota c, Arancel a where a.rutEstudiante = :rut and c.idArancel = a.idArancel")
    ArrayList<Cuota> findByRutEstudiante(@Param("rut")String rut);
}
