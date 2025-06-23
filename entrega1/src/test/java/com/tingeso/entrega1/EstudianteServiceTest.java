package com.tingeso.entrega1;

import com.tingeso.entrega1.entities.Arancel;
import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.entities.Estudiante;
import com.tingeso.entrega1.repositories.EstudianteRepository;
import com.tingeso.entrega1.services.ArancelService;
import com.tingeso.entrega1.services.EstudianteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EstudianteServiceTest {
    @Autowired
    private EstudianteService estudianteService;

    @MockBean
    private EstudianteRepository estudianteRepository;

    @MockBean
    private ArancelService arancelService;

    @Test
    public void testFindByRut() {
        // Setup
        Estudiante estudiante = new Estudiante();
        estudiante.setRut("12345678-9");
        when(estudianteRepository.findByRut(anyString())).thenReturn(estudiante);

        // Execute
        Estudiante result = estudianteService.findByRut("12345678-9");

        // Assert
        assertNotNull(result);
        assertEquals("12345678-9", result.getRut());
    }

    @Test
    public void testGuardarEstudiante() {
        // Setup
        Estudiante estudiante = new Estudiante();
        estudiante.setRut("12345678-9");

        // Simulamos que el estudiante se guarda correctamente
        when(estudianteRepository.save(any(Estudiante.class))).thenReturn(estudiante);

        // Ejecutamos
        estudianteService.guardarEstudiante(estudiante);

        // Afirmamos que el promedio de notas y el número de exámenes estén configurados correctamente
        assertEquals(0f, estudiante.getPromedioNotas());
        assertEquals(0, estudiante.getNumeroExamenes());

        // Afirmamos que se llamó al método save() del repositorio
        verify(estudianteRepository, times(1)).save(estudiante);

        // Afirmamos que se llamó al método crearArancel() de arancelService
        //verify(null, times(1)).crearArancel(estudiante);
    }

    @Test
    public void testBuscarArancelPorRut_WithExistingStudent() {
        // Setup
        String rut = "12345678-9";
        Estudiante estudiante = new Estudiante();
        estudiante.setRut(rut);
        Arancel arancel = new Arancel(); // Asumiendo que Arancel es una entidad, deberías adaptar esto según tu diseño.

        when(estudianteRepository.findByRut(rut)).thenReturn(estudiante);
        when(arancelService.buscarPorRut(rut)).thenReturn(arancel);

        // Ejecutamos
        Arancel result = estudianteService.buscarArancelPorRut(rut);

        // Afirmamos
        assertNotNull(result);
        assertEquals(arancel, result);
    }

    @Test
    public void testBuscarArancelPorRut_WithNonExistingStudent() {
        // Setup
        String rut = "12345678-9";

        when(estudianteRepository.findByRut(rut)).thenReturn(null);

        // Ejecutamos
        Arancel result = estudianteService.buscarArancelPorRut(rut);

        // Afirmamos
        assertNull(result);
    }

    @Test
    public void testBuscarCuotasPorRut_WithExistingStudent() {
        // Setup
        String rut = "12345678-9";
        Estudiante estudiante = new Estudiante();
        estudiante.setRut(rut);
        ArrayList<Cuota> cuotas = new ArrayList<>(); // Asumiendo que Cuota es una entidad, deberías adaptar esto según tu diseño.

        when(estudianteRepository.findByRut(rut)).thenReturn(estudiante);
        when(arancelService.buscarCuotas(rut)).thenReturn(cuotas);

        // Ejecutamos
        ArrayList<Cuota> result = estudianteService.buscarCuotasPorRut(rut);

        // Afirmamos
        assertNotNull(result);
        assertEquals(cuotas, result);
    }

    @Test
    public void testBuscarCuotasPorRut_WithNonExistingStudent() {
        // Setup
        String rut = "12345678-9";

        when(estudianteRepository.findByRut(rut)).thenReturn(null);

        // Ejecutamos
        ArrayList<Cuota> result = estudianteService.buscarCuotasPorRut(rut);

        // Afirmamos
        assertNull(result);
    }

    @Test
    public void testGenerarPlanilla() {
        // Setup
        String rut = "12345678-9";

        // Ejecutamos
        estudianteService.generarPlanilla(rut);

        // Afirmamos que se llamó al método actualizarArancel() de arancelService
        verify(arancelService, times(1)).actualizarArancel(rut);
    }

    @Test
    public void testDatosPagoArancel() {
        // Setup
        String rut = "12345678-9";
        ArrayList<Integer> datosArancel = new ArrayList<>();
        datosArancel.add(Integer.valueOf(1000));
        datosArancel.add(Integer.valueOf(2000));

        when(arancelService.calcularDatosArancel(rut)).thenReturn(datosArancel);

        // Ejecutamos
        ArrayList<Integer> result = estudianteService.datosPagoArancel(rut);

        // Afirmamos
        assertNotNull(result);
        assertEquals(datosArancel, result);
    }

    @Test
    public void testCalcularDescuentoNotas() {
        // Setup
        String[] datos = {"12345678-9", "2023-05-15", "5.0"};
        Estudiante estudiante = new Estudiante();
        estudiante.setRut(datos[0]);
        estudiante.setNumeroExamenes(Integer.valueOf(0));

        when(estudianteRepository.findByRut(datos[0])).thenReturn(estudiante);
        when(estudianteRepository.save(any(Estudiante.class))).thenReturn(estudiante);

        // Ejecutamos
        estudianteService.calcularDescuentoNotas(datos);

        // Afirmamos que el número de exámenes del estudiante se incrementó en 1
        assertEquals(1, estudiante.getNumeroExamenes());

        // Afirmamos que se llamó al método calcularDescuentoArancel() de arancelService
        verify(arancelService, times(1)).calcularDescuentoArancel(Integer.valueOf(anyInt()), eq(datos[0]), Float.valueOf(anyFloat()));
    }


}
