package com.tingeso.entrega1.services;

import com.tingeso.entrega1.entities.Arancel;
import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.repositories.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CuotaService {
    @Autowired
    CuotaRepository cuotaRepository;

    public void crearCuotas(Arancel arancel) {
        int valorCuota = arancel.getMonto() / arancel.getNumCuotas();
        for (int i = 0; i < arancel.getNumCuotas(); i++) {
            Cuota cuota = new Cuota();
            cuota.setMonto(valorCuota);
            cuota.setDescuento(0);
            cuota.setPagado(false);
            cuota.setFechaPago("NO PAGADA");
            cuota.setIdArancel(arancel.getIdArancel());

            // Seteamos la fecha de expiración de cada cuota
            if (i < 9) {
                cuota.setFechaExp(LocalDate.now().getYear() +"-0"+ (i + 1) +"-10");
            } else {
                cuota.setFechaExp(LocalDate.now().getYear() +"-10" +"-10");
            }
            cuotaRepository.save(cuota);
        }
    }
}
