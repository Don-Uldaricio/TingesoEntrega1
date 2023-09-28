package com.tingeso.entrega1;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Arancel")
public class Arancel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idArancel;
    private Float monto;
    private Integer numCuotas;
}
