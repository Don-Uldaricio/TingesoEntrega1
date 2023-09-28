package com.tingeso.entrega1;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
@Table(name = "Reporte")
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idReporte;
    private Date fechaCreacion;
}
