package com.tingeso.entrega1.repositories;

import com.tingeso.entrega1.entities.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    @Query("select e from Estudiante e where e.rut = :rut")
    Estudiante findByRut(@Param("rut")String rut);


}
