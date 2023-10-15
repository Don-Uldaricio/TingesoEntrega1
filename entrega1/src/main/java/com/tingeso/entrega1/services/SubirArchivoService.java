package com.tingeso.entrega1.services;

import com.tingeso.entrega1.repositories.SubirArchivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Service
public class SubirArchivoService {
    @Autowired
    SubirArchivoRepository subirArchivoRepository;

    @Autowired
    EstudianteService estudianteService;
    
    public void leerArchivo(String rutaArchivo) throws FileNotFoundException {
        try {
            // Abre el archivo CSV
            File archivoCSV = new File(rutaArchivo);
            Scanner scanner = new Scanner(archivoCSV);

            // Itera a través de las líneas del archivo CSV
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();

                // Divide la línea en campos utilizando la coma como separador
                String[] campos = linea.split(",");

                // Accede a los valores de los campos
                String rut = campos[0];
                float promedioPruebas = Integer.parseInt(campos[2]);
                String fecha = campos[1];



                // Hacer algo con los datos
                System.out.println("rut: " + rut);
                System.out.println("fecha: " + fecha);
                System.out.println("puntaje: " + promedioPruebas);
                estudianteService.calcularDescuentoNotas(campos);
            }

            // Cierra el scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Archivo CSV no encontrado.");
        }
    }
}
