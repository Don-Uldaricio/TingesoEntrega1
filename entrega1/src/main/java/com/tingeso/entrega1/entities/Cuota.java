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
    private Integer numeroCuota;
    private Integer monto;
    private float descuento;
    private int mesesAtraso;
    private float interes;
    private Boolean pagado;
    private String fechaPago;
    private String fechaExp;
    private Long idArancel;

    public int getMontoFinal() {
        return (int) (monto * (1 - getDescuento() + getInteres()));
    }
}
