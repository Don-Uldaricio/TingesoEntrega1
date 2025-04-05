package com.tingeso.entrega1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "Estudiante")
@NoArgsConstructor
@AllArgsConstructor
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idEstudiante;
    private String rut;
    private String apellidos;
    private String nombres;
    private String fechaNacimiento;
    private String tipoColegio;
    private String nombreColegio;
    private Integer egreso;
    private Integer numeroCuotas;
    private Integer numeroExamenes;
    private Float promedioNotas;

    @ElementCollection
    @CollectionTable(name = "asistencia_mensual", joinColumns = @JoinColumn(name = "estudiante_id"))
    @Column(name = "porcentaje_asistencia")
    private List<Float> asistenciaMensual = new ArrayList<>(); // Inicializamos una lista vacía sin tamaño fijo
}