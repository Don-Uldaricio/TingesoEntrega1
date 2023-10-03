package com.tingeso.entrega1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "Arancel")
@NoArgsConstructor
@AllArgsConstructor
public class Arancel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idArancel;
    private Integer monto;
    private Integer numCuotas;
    private float descuento;
    private String rutEstudiante;
}
