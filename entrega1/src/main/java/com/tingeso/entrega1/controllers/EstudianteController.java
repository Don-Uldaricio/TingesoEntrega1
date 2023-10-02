package com.tingeso.entrega1.controllers;

import com.tingeso.entrega1.services.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.tingeso.entrega1.entities.Estudiante;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping
public class EstudianteController {
    @Autowired
    private EstudianteService estudianteService;

    @GetMapping("/ingresoEstudiante")
    public String mostrarIngresoEstudiantes(Model model) {
        Estudiante e = new Estudiante();
        model.addAttribute("Estudiante", e);
        return "ingresoEstudiante";

    }

    // En tu controlador
    @PostMapping("/guardarEstudiante")
    public String guardarEstudiante(@ModelAttribute Estudiante estudiante) {
        estudianteService.guardarEstudiante(estudiante);
        return "redirect:/ingresoEstudiante";
    }

}
