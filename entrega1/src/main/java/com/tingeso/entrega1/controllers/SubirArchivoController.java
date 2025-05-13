package com.tingeso.entrega1.controllers;

import com.tingeso.entrega1.services.SubirArchivoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
@RequestMapping
public class SubirArchivoController {

    private final SubirArchivoService subirArchivoService;

    public SubirArchivoController(SubirArchivoService subirArchivoService) {
        this.subirArchivoService = subirArchivoService;
    }

    @PostMapping("/subirArchivo")
    public String procesarArchivo(@RequestParam("archivo") MultipartFile archivo) {
        if (archivo.isEmpty()) {
            return "error";
        }

        File archivoTemporal;
        try {
            archivoTemporal = File.createTempFile("temp", ".csv");
            try (FileOutputStream fos = new FileOutputStream(archivoTemporal)) {
                fos.write(archivo.getBytes());
            }

            subirArchivoService.leerArchivo(archivoTemporal.getAbsolutePath());

            return "importarNotas";
        } catch (IOException e) {
            return "error";
        }
    }
}
