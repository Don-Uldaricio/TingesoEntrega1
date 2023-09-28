package com.tingeso.entrega1;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
@Table(name = "Cuota")
public class Cuota {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCuota;
    private Float monto;
    private Boolean pagado;
    private Date fechaPago;
    private Date fechaExp;
}
