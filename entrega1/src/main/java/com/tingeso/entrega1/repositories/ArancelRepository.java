package com.tingeso.entrega1.repositories;

import com.tingeso.entrega1.entities.Arancel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArancelRepository extends JpaRepository<Arancel, Long> {
}
