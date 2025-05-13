package com.tingeso.entrega1.services;

import com.tingeso.entrega1.entities.Arancel;
import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.repositories.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class CuotaService {
    @Autowired
    CuotaRepository cuotaRepository;

    public Cuota buscarCuotaPorId(Integer idCuota) {
        return cuotaRepository.findById(idCuota);
    }

    public void guardarCuota(Cuota c) {
        cuotaRepository.save(c);
    }

    public void crearCuotas(Arancel arancel) {
        int valorCuota = arancel.getMonto() / arancel.getNumCuotas();
        for (int i = 0; i < arancel.getNumCuotas(); i++) {
            Cuota cuota = new Cuota();
            cuota.setNumeroCuota(i + 1);
            cuota.setMonto(valorCuota);
            cuota.setDescuento(0);
            cuota.setPagado(false);
            cuota.setFechaPago("");
            cuota.setIdArancel(arancel.getIdArancel());

            if (i < 9) {
                cuota.setFechaExp(LocalDate.now().getYear() + "-0" + (i + 1) + "-10");
            } else {
                cuota.setFechaExp(LocalDate.now().getYear() + "-10-10");
            }
            cuotaRepository.save(cuota);
        }
    }

    public ArrayList<Cuota> buscarCuotasPorRut(String rut) {
        return cuotaRepository.findByRutEstudiante(rut);
    }

    public ArrayList<Cuota> listarCuotas(Long idArancel) {
        ArrayList<Cuota> cuotas = new ArrayList<>();
        for (Cuota c : cuotaRepository.findAll()) {
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

    public void actualizarCuotas(ArrayList<Cuota> cuotas) {
        for (Cuota c : cuotas) {
            if (!c.getPagado()) {
                calcularAtrasoCuota(c);
                cuotaRepository.save(c);
            }
        }
    }

    public void calcularAtrasoCuota(Cuota cuota) {
        int mesesAtraso = calcularMesesAtraso(cuota);
        if (mesesAtraso == 0) {
            cuota.setInteres(0);
        } else if (mesesAtraso == 1) {
            cuota.setInteres(0.03f);
        } else if (mesesAtraso == 2) {
            cuota.setInteres(0.06f);
        } else if (mesesAtraso == 3) {
            cuota.setInteres(0.09f);
        } else {
            cuota.setInteres(0.15f);
        }
        cuota.setMesesAtraso(mesesAtraso);
    }

    public int calcularMesesAtraso(Cuota cuota) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaExpiracion = LocalDate.parse(cuota.getFechaExp(), formato);
        LocalDate fechaActual = LocalDate.now();
        Period diferencia = Period.between(fechaExpiracion, fechaActual);
        return diferencia.getMonths();
    }

    public void calcularDescuentoCuota(Cuota cuota, Float descuento) {
        cuota.setDescuento(descuento);
        cuotaRepository.save(cuota);
    }
}
