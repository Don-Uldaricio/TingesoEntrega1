package com.tingeso.entrega1.services;

import com.tingeso.entrega1.entities.Arancel;
import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.entities.Estudiante;
import com.tingeso.entrega1.repositories.ArancelRepository;
import com.tingeso.entrega1.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ArancelService {
    @Autowired
    private ArancelRepository arancelRepository;

    @Autowired
    private CuotaService cuotaService;

    public void crearArancel(Estudiante e) {
        Arancel arancel = new Arancel();
        if (e.getNumeroCuotas() == 0) {
            arancel.setMonto((int) (1500000 * 0.5));
        } else {
            String tipoColegio = e.getTipoColegio();
            int aniosEgreso = diferenciaFechaActual(e.getEgreso());
            arancel.setDescuento(calcularDescuento(tipoColegio, aniosEgreso));
            arancel.setMonto((int) (1500000 * (1 - arancel.getDescuento())));
        }
        arancel.setRutEstudiante(e.getRut());
        arancel.setNumCuotas(e.getNumeroCuotas());
        arancelRepository.save(arancel);
        cuotaService.crearCuotas(arancel);
    }

    public int diferenciaFechaActual(int fecha) {
        return LocalDate.now().getYear() - fecha;
    }

    public Float calcularDescuento(String tipoColegio, int aniosEgreso) {
        float descuento = 0;

        // Calculamos descuento por tipo de colegio
        if (Objects.equals(tipoColegio, "Municipal")) {
            descuento += 0.2f;
        } else if (Objects.equals(tipoColegio, "Subvencionado")) {
            descuento += 0.1f;
        }

        // Calculamos descuento por años de egreso del colegio
        if (aniosEgreso == 0) {
            descuento += 0.15f;
        } else if (aniosEgreso >= 1 && aniosEgreso <= 2) {
            descuento += 0.08f;
        } else if (aniosEgreso >= 3 && aniosEgreso <= 4) {
            descuento += 0.04f;
        }

        return descuento;
    }

    public Arancel buscarPorRut(String rut) {
        List<Arancel> aranceles = arancelRepository.findAll();
        for (Arancel a: aranceles) {
            if (Objects.equals(a.getRutEstudiante(), rut)) {
                return a;
            }
        }
        return null;
    }

    public ArrayList<Cuota> buscarCuotas(String rut) {
        Arancel arancel = buscarPorRut(rut);
        if (arancel != null) {
            if (arancel.getNumCuotas() != 0) {
                return cuotaService.listarCuotas(arancel.getIdArancel());
            }
        }
        return null;
    }

    public void actualizarArancel(String rut) {
        int nuevoArancel = 0;
        Arancel arancel = buscarPorRut(rut);
        System.out.println(arancel.getMonto());
        ArrayList<Cuota> cuotas = buscarCuotas(rut);
        cuotaService.actualizarCuotas(cuotas);
        for (Cuota c: cuotas) {
            nuevoArancel = nuevoArancel + (int) (c.getMonto() * (1 + c.getInteres()));
        }
        arancel.setMonto(nuevoArancel);
        arancelRepository.save(arancel);
    }

}
