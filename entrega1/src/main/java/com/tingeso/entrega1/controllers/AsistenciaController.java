package com.tingeso.entrega1.controllers;

import com.tingeso.entrega1.entities.Estudiante;
import com.tingeso.entrega1.repositories.EstudianteRepository;
import com.tingeso.entrega1.services.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
@RequestMapping("/asistencia")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @GetMapping
    public String mostrarFormularioAsistencia() {
        return "asistencia-upload";
    }

    @PostMapping("/upload")
    public String importarAsistencia(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "Por favor seleccione un archivo para cargar");
            redirectAttributes.addFlashAttribute("tipo", "error");
            return "redirect:/asistencia";
        }

        if (asistenciaService.importarAsistencia(file)) {
            redirectAttributes.addFlashAttribute("mensaje", "Archivo cargado correctamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Error al procesar el archivo");
            redirectAttributes.addFlashAttribute("tipo", "error");
        }

        return "redirect:/asistencia";
    }

    @GetMapping("/ver")
    public String mostrarFormularioBusqueda() {
        return "asistencia-buscar";
    }

    @GetMapping("/ver/{rut}")
    public String verAsistenciaPorRut(@PathVariable String rut, Model model) {
        Estudiante estudiante = estudianteRepository.findByRut(rut);

        if (estudiante == null) {
            model.addAttribute("error", "No se encontró estudiante con el RUT: " + rut);
            return "asistencia-buscar";
        }

        model.addAttribute("estudiante", estudiante);

        // Nombres de los meses para la visualización
        String[] nombresMeses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        model.addAttribute("meses", nombresMeses);

        return "asistencia-detalle";
    }

    @PostMapping("/buscar")
    public String buscarEstudiante(@RequestParam("rut") String rut) {
        return "redirect:/asistencia/ver/" + rut;
    }
}