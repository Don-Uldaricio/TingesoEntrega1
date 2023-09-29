package com.tingeso.entrega1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@Table(name = "Matricula")
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idMatricula;
    private Boolean pagado;
    private Date fechaPago;
}
