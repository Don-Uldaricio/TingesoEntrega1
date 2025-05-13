package com.tingeso.entrega1.controllers;

import com.tingeso.entrega1.entities.Estudiante;
import com.tingeso.entrega1.repositories.EstudianteRepository;
import com.tingeso.entrega1.services.AsistenciaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/asistencia")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;
    private final EstudianteRepository estudianteRepository;

    // Constantes para evitar literales duplicados
    private static final String ATTR_MENSAJE = "mensaje";
    private static final String ATTR_TIPO = "tipo";
    private static final String TIPO_ERROR = "error";
    private static final String TIPO_SUCCESS = "success";

    public AsistenciaController(AsistenciaService asistenciaService, EstudianteRepository estudianteRepository) {
        this.asistenciaService = asistenciaService;
        this.estudianteRepository = estudianteRepository;
    }

    @GetMapping
    public String mostrarFormularioAsistencia() {
        return "asistencia-upload";
    }

    @PostMapping("/upload")
    public String importarAsistencia(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute(ATTR_MENSAJE, "Por favor seleccione un archivo para cargar");
            redirectAttributes.addFlashAttribute(ATTR_TIPO, TIPO_ERROR);
            return "redirect:/asistencia";
        }

        if (asistenciaService.importarAsistencia(file)) {
            redirectAttributes.addFlashAttribute(ATTR_MENSAJE, "Archivo cargado correctamente");
            redirectAttributes.addFlashAttribute(ATTR_TIPO, TIPO_SUCCESS);
        } else {
            redirectAttributes.addFlashAttribute(ATTR_MENSAJE, "Error al procesar el archivo");
            redirectAttributes.addFlashAttribute(ATTR_TIPO, TIPO_ERROR);
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
            model.addAttribute(ATTR_TIPO, TIPO_ERROR);
            model.addAttribute(ATTR_MENSAJE, "No se encontró estudiante con el RUT: " + rut);
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
