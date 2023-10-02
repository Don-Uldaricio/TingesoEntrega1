package com.tingeso.entrega1.services;

import com.tingeso.entrega1.entities.Estudiante;
import com.tingeso.entrega1.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class EstudianteService {
    @Autowired
    EstudianteRepository estudianteRepository;

    public ArrayList<Estudiante> getEstudiantes(){
        return (ArrayList<Estudiante>) estudianteRepository.findAll();
    }

    public Estudiante findByRut(String rut){
        return estudianteRepository.findByRut(rut);
    }

    public Estudiante guardarEstudiante(Estudiante e) { return estudianteRepository.save(e); }
}
