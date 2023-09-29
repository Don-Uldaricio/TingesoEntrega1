package com.tingeso.entrega1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@Table(name = "Cuota")
@NoArgsConstructor
@AllArgsConstructor
public class Cuota {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCuota;
    private Float monto;
    private Boolean pagado;
    private Date fechaPago;
    private Date fechaExp;
}
