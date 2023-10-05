package com.tingeso.entrega1.controllers;

import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.services.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.tingeso.entrega1.entities.Estudiante;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @GetMapping("/cuotaEstudiante")
    public String mostrarVistaCuotas() {
        return "cuotaEstudiante";
    }

    @PostMapping("/actualizarCuotasEstudiante")
    public String actualizarCuotasEstudiante(Model model, @Param("rut") String rut) {
        Estudiante estudiante = estudianteService.findByRut(rut);
        ArrayList<Cuota> cuotasEstudiante = estudianteService.buscarCuotasPorRut(rut);
        estudianteService.generarPlanilla(rut);
        model.addAttribute("cuotasEstudiante", cuotasEstudiante);
        model.addAttribute("estudiante", estudiante);
        return "cuotaEstudiante";
    }

    // En tu controlador
    @PostMapping("/guardarEstudiante")
    public String guardarEstudiante(@ModelAttribute Estudiante estudiante) {
        estudiante.setPromedioNotas(0f);
        estudianteService.guardarEstudiante(estudiante);
        return "redirect:/ingresoEstudiante";
    }

    @GetMapping("/registrarPago")
    public String mostrarVistaPago() {
        return "registrarPago";
    }

    @PostMapping("/generarPlanillaPago")
    public String generarPlanillaPago(Model model, @Param("rut") String rut) {
        ArrayList<Cuota> cuotasEstudiante = estudianteService.buscarCuotasPorRut(rut);
        estudianteService.generarPlanilla(rut);
        model.addAttribute("cuotasEstudiante", cuotasEstudiante);
        return "registrarPago";
    }

}
