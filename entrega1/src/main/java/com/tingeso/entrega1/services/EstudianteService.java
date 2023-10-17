package com.tingeso.entrega1.services;

import com.tingeso.entrega1.entities.Arancel;
import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.entities.Estudiante;
import com.tingeso.entrega1.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class EstudianteService {
    @Autowired
    EstudianteRepository estudianteRepository;

    @Autowired
    ArancelService arancelService;

    // Método que busca en el repositorio un estudiante por su rut
    public Estudiante findByRut(String rut){
        return estudianteRepository.findByRut(rut);
    }

    /*
    Método que se encarga de guardar un estudiante en la BD e invocar el método
    crearArancel del arancelService para la creación de cuotas.
     */
    public void guardarEstudiante(Estudiante e) {
        e.setPromedioNotas(0f);
        e.setNumeroExamenes(0);
        estudianteRepository.save(e);
        arancelService.crearArancel(e);
    }

    public Arancel buscarArancelPorRut(String rut) {
        if (estudianteRepository.findByRut(rut) != null) {
            return arancelService.buscarPorRut(rut);
        }
        return null;
    }

    public ArrayList<Cuota> buscarCuotasPorRut(String rut) {
        if (estudianteRepository.findByRut(rut) != null) {
            return arancelService.buscarCuotas(rut);
        }
        return null;
    }

    // Método que se encarga de actualizar el monto del arancel y las cuotas a pagar
    public void generarPlanilla(String rut) {
        arancelService.actualizarArancel(rut);
    }

    public ArrayList<Integer> datosPagoArancel(String rut) {
        return arancelService.calcularDatosArancel(rut);
    }

    // Método que se encarga de generar el descuento por promedio de notas de examenes
    public void calcularDescuentoNotas(String[] datos) {
        // Aumentamos el número de exámenes que ha dado a uno y lo guardamos en la BD
        Estudiante estudiante = findByRut(datos[0]);
        estudiante.setNumeroExamenes(estudiante.getNumeroExamenes() + 1);
        estudianteRepository.save(estudiante);

        // Sacamos los datos del String de entrada a la función
        String fechaPrueba = datos[1];
        float promedioNotas = Float.parseFloat(datos[2]);

        // Transformamos la fecha según formato para extraer el día del mes y aplicar descuento al siguiente mes
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fecha = LocalDate.parse(fechaPrueba, formato);
        int mesExamen = fecha.getMonthValue();

        // Si es un mes válido aplicamos descuento (ya que el ultimo mes del sistema es el 10)
        if (mesExamen < 10) {
            arancelService.calcularDescuentoArancel(mesExamen, datos[0], promedioNotas);
        }
    }

}
