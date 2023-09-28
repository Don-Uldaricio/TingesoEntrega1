package com.tingeso.entrega1;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
@Table(name = "Estudiante")
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idEstudiante;
    private String rut;
    private String apellidos;
    private String nombres;
    private Date fechaNacimiento;
    private String tipoColegio;
    private String NombreColegio;
    private Integer egreso;
    private Float promedioNotas;
}
