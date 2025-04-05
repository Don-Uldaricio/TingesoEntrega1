package com.tingeso.entrega1.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "Asistencia")
@NoArgsConstructor
@AllArgsConstructor
public class Asistencia {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    private Float enero;
    private Float febrero;
    private Float marzo;
    private Float abril;
    private Float mayo;
    private Float junio;
    private Float julio;
    private Float agosto;
    private Float septiembre;
    private Float octubre;
    private Float noviembre;
    private Float diciembre;
}
