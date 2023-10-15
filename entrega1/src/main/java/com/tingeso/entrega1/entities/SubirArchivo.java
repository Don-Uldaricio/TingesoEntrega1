package com.tingeso.entrega1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "SubirArchivo")
@NoArgsConstructor
@AllArgsConstructor
public class SubirArchivo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idSubirArchivo;
}
