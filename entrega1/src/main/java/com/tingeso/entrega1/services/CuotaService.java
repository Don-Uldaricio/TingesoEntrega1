package com.tingeso.entrega1.services;

import com.tingeso.entrega1.entities.Arancel;
import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.repositories.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CuotaService {
    @Autowired
    CuotaRepository cuotaRepository;

    public Cuota buscarCuotaPorId(Integer idCuota) { return cuotaRepository.findById(idCuota);}

    public void guardarCuota(Cuota c) { cuotaRepository.save(c); }

    // Método invocado por ArancelService que se encarga de crear las cuotas
    public void crearCuotas(Arancel arancel) {
        int valorCuota = arancel.getMonto() / arancel.getNumCuotas();
        for (int i = 0; i < arancel.getNumCuotas(); i++) {
            Cuota cuota = new Cuota();
            cuota.setNumeroCuota(i+1);
            cuota.setMonto(valorCuota);
            cuota.setDescuento(0);
            cuota.setPagado(false);
            cuota.setFechaPago("");
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

    public ArrayList<Cuota> buscarCuotasPorRut(String rut) {
        return cuotaRepository.findByRutEstudiante(rut);
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

    // Cambia el estado de una cuota a pagado y setea la fecha en la que se realizó
    public Cuota pagarCuota(Cuota c) {
        c.setPagado(true);
        c.setFechaPago(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return c;
    }

    // Actualiza el valor de las cuotas calculando sus meses de atraso y el interés
    public void actualizarCuotas(ArrayList<Cuota> cuotas) {
        for (Cuota c: cuotas) {
            if (!c.getPagado()) {
                calcularAtrasoCuota(c);
                cuotaRepository.save(c);
            }
        }
    }

    // Calcula el interés de una cuota por meses de atraso
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

    // Calcula los meses de atraso de una cuota
    public int calcularMesesAtraso(Cuota cuota) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaExpiracion = LocalDate.parse(cuota.getFechaExp(), formato);
        LocalDate fechaActual = LocalDate.now();

        // Si la cuota aún no está vencida, retornar 0 (no hay atraso)
        if (fechaActual.isBefore(fechaExpiracion)) {
            return 0;
        }

        // Calculamos la diferencia total en meses
        long mesesDiferencia = ChronoUnit.MONTHS.between(fechaExpiracion, fechaActual);

        return (int) mesesDiferencia;
    }

    // Aplica descuento a una cuota por haber rendido una prueba
    public void calcularDescuentoCuota(Cuota cuota, Float descuento) {
        cuota.setDescuento(descuento);
        cuotaRepository.save(cuota);
    }
}
