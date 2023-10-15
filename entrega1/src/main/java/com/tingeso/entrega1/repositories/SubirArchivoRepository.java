package com.tingeso.entrega1.repositories;

import com.tingeso.entrega1.entities.SubirArchivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubirArchivoRepository extends JpaRepository<SubirArchivo, Long> {
}
