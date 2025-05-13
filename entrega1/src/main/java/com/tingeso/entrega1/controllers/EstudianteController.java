package com.tingeso.entrega1.controllers;

import com.tingeso.entrega1.entities.Arancel;
import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.entities.Estudiante;
import com.tingeso.entrega1.services.EstudianteService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

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
        estudianteService.generarPlanilla(rut);
        Estudiante estudiante = estudianteService.findByRut(rut);
        Arancel arancel = estudianteService.buscarArancelPorRut(rut);
        ArrayList<Cuota> cuotasEstudiante = estudianteService.buscarCuotasPorRut(rut);
        ArrayList<Integer> datosArancel = estudianteService.datosPagoArancel(rut);
        model.addAttribute("cuotasEstudiante", cuotasEstudiante);
        model.addAttribute("arancel", arancel);
        model.addAttribute("estudiante", estudiante);
        model.addAttribute("datosArancel", datosArancel);
        return "cuotaEstudiante";
    }

    @PostMapping("/guardarEstudiante")
    public String guardarEstudiante(@ModelAttribute Estudiante estudiante) {
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

    @GetMapping("/importarNotas")
    public String vistaImportarNotas() {
        return "importarNotas";
