package com.tingeso.entrega1.services;

import com.tingeso.entrega1.entities.Arancel;
import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.entities.Estudiante;
import com.tingeso.entrega1.repositories.ArancelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ArancelService {
    @Autowired
    private ArancelRepository arancelRepository;

    @Autowired
    private CuotaService cuotaService;

    // Método que crea el arancel de un estudiante y las cuotas asociadas a esta
    public void crearArancel(Estudiante e) {
        Arancel arancel = new Arancel();
        if (e.getNumeroCuotas() == 0) {
            arancel.setMonto((int) (1500000 * 0.5));
            arancel.setNumCuotas(0);
            arancel.setRutEstudiante(e.getRut());
            arancelRepository.save(arancel);
        } else {
            String tipoColegio = e.getTipoColegio();
            int aniosEgreso = diferenciaFechaActual(e.getEgreso());
            arancel.setDescuento(calcularDescuento(tipoColegio, aniosEgreso));
            arancel.setMonto((int) (1500000 * (1 - arancel.getDescuento())));
            arancel.setRutEstudiante(e.getRut());
            arancel.setNumCuotas(e.getNumeroCuotas());
            arancelRepository.save(arancel);
            cuotaService.crearCuotas(arancel);
        }
    }

    // Calcula la diferencia de años de la fecha actual con una fecha entregada
    public int diferenciaFechaActual(int fecha) {
        return LocalDate.now().getYear() - fecha;
    }

    // Calcula el descuento de un estudiante en base al tipo de colegio y años desde que egresó
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
        return new ArrayList<>();
    }

    // Se encarga de actualizar el arancel y las cuotas asociadas
    public void actualizarArancel(String rut) {
        int nuevoArancel = 0;
        Arancel arancel = buscarPorRut(rut);
        if (arancel != null) {
            if (arancel.getNumCuotas() != 0) {
                ArrayList<Cuota> cuotas = buscarCuotas(rut);
                cuotaService.actualizarCuotas(cuotas);
                for (Cuota c: cuotas) {
                    nuevoArancel = nuevoArancel + (int) (c.getMonto() * (1 + c.getInteres()));
                }
                arancel.setMonto(nuevoArancel);
                arancelRepository.save(arancel);
            }
        }
    }

    // Calcula datos del arancel
    public ArrayList<Integer> calcularDatosArancel(String rut) {
        Arancel arancel = buscarPorRut(rut);
        ArrayList<Integer> datosArancel = new ArrayList<>();
        if (arancel.getNumCuotas() != 0) {
            ArrayList<Integer> datosCuotas = calcularDatosCuotas(rut);
            datosArancel.add(datosCuotas.get(0)); // Monto pagado
            datosArancel.add(arancel.getMonto() - datosArancel.get(0)); // Monto por pagar
            datosArancel.add(datosCuotas.get(1)); // N cuotas pagadas
            datosArancel.add(datosCuotas.get(2)); // N cuotas atrasadas
            return datosArancel;
        }
        return null;
    }

    // Calcula datos de las cuotas útiles en el reporte
    public ArrayList<Integer> calcularDatosCuotas(String rut) {
        ArrayList<Cuota> cuotas = buscarCuotas(rut);
        ArrayList<Integer> cuotasPagadas = new ArrayList<>(); // Monto pagado y cantidad de cuotas pagadas
        cuotasPagadas.add(0); // Monto pagado
        cuotasPagadas.add(0); // Numero de cuotas pagadas
        cuotasPagadas.add(0); // Numero cuotas atrasadas
        for (Cuota c: cuotas) {
            if (c.getPagado()) {
                cuotasPagadas.set(0, cuotasPagadas.get(0) + (int)(c.getMonto()*(1 + c.getInteres() - c.getDescuento())));
                cuotasPagadas.set(1, cuotasPagadas.get(1) + 1);
            }
            if (c.getMesesAtraso() != 0) {
                cuotasPagadas.set(2, cuotasPagadas.get(2) + 1);
            }
        }
        return cuotasPagadas;
    }

    // Calcula descuento en el arancel por examen rendido por el estudiante
    public void calcularDescuentoArancel(Integer mesExamen, String rut, Float promedio) {
        Cuota cuotaMes = null;
        float descuento = 0;
        ArrayList<Cuota> cuotas = buscarCuotas(rut);
        for (Cuota c: cuotas) {
            if (Integer.parseInt(c.getFechaExp().split("-")[1]) == mesExamen + 1) {
                cuotaMes = c;
            }
        }
        assert cuotaMes != null;
        if (!cuotaMes.getPagado()) {
            if (promedio >= 950 && promedio <= 1000) {
                descuento = 0.1f;
            } else if (promedio >= 900 && promedio <= 949) {
                descuento = 0.05f;
            } else if (promedio >= 850 && promedio <= 899) {
                descuento = 0.02f;
            }
        }
        cuotaService.calcularDescuentoCuota(cuotaMes, descuento);
    }

}
