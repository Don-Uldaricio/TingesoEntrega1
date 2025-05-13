package com.tingeso.entrega1.services;

import com.tingeso.entrega1.entities.Arancel;
import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.entities.Estudiante;
import com.tingeso.entrega1.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private ArancelService arancelService;

    public void guardarEstudiante(Estudiante estudiante) {
        estudianteRepository.save(estudiante);
    }

    public Estudiante findByRut(String rut) {
        return estudianteRepository.findByRut(rut);
    }

    public void generarPlanilla(String rut) {
        Estudiante estudiante = estudianteRepository.findByRut(rut);
        arancelService.crearArancel(estudiante);
    }

    public Arancel buscarArancelPorRut(String rut) {
        return arancelService.buscarPorRut(rut);
    }

    public ArrayList<Cuota> buscarCuotasPorRut(String rut) {
        return arancelService.buscarCuotas(rut);
    }

    public ArrayList<Integer> datosPagoArancel(String rut) {
        return arancelService.calcularDatosArancel(rut);
    }

    public void calcularDescuentoNotas(String[] campos) {
        String rut = campos[0];
        Float promedio = Float.parseFloat(campos[2]);
        String[] fechaSplit = campos[1].split("/");
        int mesExamen = Integer.parseInt(fechaSplit[1]) - 1;

        arancelService.calcularDescuentoArancel(mesExamen, rut, promedio);
    }
}
