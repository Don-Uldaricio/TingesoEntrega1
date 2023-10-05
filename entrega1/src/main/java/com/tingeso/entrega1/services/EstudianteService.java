package com.tingeso.entrega1.services;

import com.tingeso.entrega1.entities.Arancel;
import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.entities.Estudiante;
import com.tingeso.entrega1.repositories.ArancelRepository;
import com.tingeso.entrega1.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class EstudianteService {
    @Autowired
    EstudianteRepository estudianteRepository;

    @Autowired
    ArancelService arancelService;

    public ArrayList<Estudiante> getEstudiantes(){
        return (ArrayList<Estudiante>) estudianteRepository.findAll();
    }

    public Estudiante findByRut(String rut){
        return estudianteRepository.findByRut(rut);
    }

    public void guardarEstudiante(Estudiante e) {
        estudianteRepository.save(e);
        arancelService.crearArancel(e);
    }

    public ArrayList<Cuota> buscarCuotasPorRut(String rut) {
        if (estudianteRepository.findByRut(rut) != null) {
            return arancelService.buscarCuotas(rut);
        }
        return null;
    }

    public void generarPlanilla(String rut) {
        arancelService.actualizarArancel(rut);
    }

}
