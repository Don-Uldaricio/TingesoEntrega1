package com.tingeso.entrega1.services;

import com.tingeso.entrega1.repositories.SubirArchivoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Service
public class SubirArchivoService {

    private final SubirArchivoRepository subirArchivoRepository;
    private final EstudianteService estudianteService;
    private static final Logger logger = LoggerFactory.getLogger(SubirArchivoService.class);

    public SubirArchivoService(SubirArchivoRepository subirArchivoRepository, EstudianteService estudianteService) {
        this.subirArchivoRepository = subirArchivoRepository;
        this.estudianteService = estudianteService;
    }

    public void leerArchivo(String rutaArchivo) throws FileNotFoundException {
        try {
            File archivoCSV = new File(rutaArchivo);
            Scanner scanner = new Scanner(archivoCSV);

            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] campos = linea.split(",");

                String rut = campos[0];
                float promedioPruebas = Integer.parseInt(campos[2]);
                String fecha = campos[1];

                logger.info("rut: {}", rut);
                logger.info("fecha: {}", fecha);
                logger.info("puntaje: {}", promedioPruebas);

                estudianteService.calcularDescuentoNotas(campos);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            logger.error("Archivo CSV no encontrado.");
        }
    }
}
