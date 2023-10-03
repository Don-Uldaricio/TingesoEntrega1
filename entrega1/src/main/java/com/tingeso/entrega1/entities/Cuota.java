package com.tingeso.entrega1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "Cuota")
@NoArgsConstructor
@AllArgsConstructor
public class Cuota {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCuota;
    private Integer monto;
    private Integer descuento;
    private Boolean pagado;
    private String fechaPago;
    private String fechaExp;
    private Long idArancel;
}
