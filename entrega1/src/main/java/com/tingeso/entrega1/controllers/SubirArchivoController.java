package com.tingeso.entrega1.controllers;

import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.services.SubirArchivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

@Controller
@RequestMapping
public class SubirArchivoController {
    @Autowired
    private SubirArchivoService subirArchivoService;

    @PostMapping("/subirArchivo")
    public String procesarArchivo(@RequestParam("archivo") MultipartFile archivo) {
        if (archivo.isEmpty()) {
            return "error";
        }

        // Creamos un archivo temporal para guardar el contenido del MultipartFile
        File archivoTemporal;
        try {
            archivoTemporal = File.createTempFile("temp", ".csv");
            try (FileOutputStream fos = new FileOutputStream(archivoTemporal)) {
                fos.write(archivo.getBytes());
            }

            // Llamar al método leerArchivo
            subirArchivoService.leerArchivo(archivoTemporal.getAbsolutePath());

            return "importarNotas";
        } catch (IOException e) {
            return "error";
        }
    }
}
