package com.tingeso.entrega1.services;

import com.tingeso.entrega1.entities.Arancel;
import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.repositories.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CuotaService {
    @Autowired
    CuotaRepository cuotaRepository;

    public Cuota buscarCuotaPorId(Integer idCuota) { return cuotaRepository.findById(idCuota);}

    public Cuota guardarCuota(Cuota c) { return cuotaRepository.save(c); }

    public void crearCuotas(Arancel arancel) {
        int valorCuota = arancel.getMonto() / arancel.getNumCuotas();
        for (int i = 0; i < arancel.getNumCuotas(); i++) {
            Cuota cuota = new Cuota();
            cuota.setNumeroCuota(i+1);
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

    public ArrayList<Cuota> listarCuotas(Long idArancel) {
        ArrayList<Cuota> cuotas = new ArrayList<>();
        for (Cuota c: cuotaRepository.findAll()) {
            if (Objects.equals(c.getIdArancel(), idArancel)) {
                cuotas.add(c);
            }
        }
        return cuotas;
    }

    public Cuota pagarCuota(Cuota c) {
        c.setPagado(true);
        c.setFechaPago(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return c;
    }
}
