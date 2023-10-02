package com.tingeso.entrega1.controllers;

import com.tingeso.entrega1.services.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.tingeso.entrega1.entities.Estudiante;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping
public class EstudianteController {
    @Autowired
    private EstudianteService estudianteService;

    @GetMapping("/ingresoEstudiantes")
    public String mostrarIngresoEstudiantes(Model model) {
        Estudiante e = new Estudiante();
        model.addAttribute("Estudiante", e);
        return "ingresoEstudiantes";

    }



}
