package com.tingeso.entrega1;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
@Table(name = "Matricula")
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idMatricula;
    private Boolean pagado;
    private Date fechaPago;
}
