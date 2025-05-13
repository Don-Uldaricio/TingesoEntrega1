package com.tingeso.entrega1;

import com.tingeso.entrega1.entities.Arancel;
import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.entities.Estudiante;
import com.tingeso.entrega1.repositories.ArancelRepository;
import com.tingeso.entrega1.repositories.CuotaRepository;
import com.tingeso.entrega1.repositories.EstudianteRepository;
import com.tingeso.entrega1.services.ArancelService;
import com.tingeso.entrega1.services.CuotaService;
import com.tingeso.entrega1.services.EstudianteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ArancelServiceTest {

    @Autowired
    private ArancelService arancelService;

    @MockBean
    private ArancelRepository arancelRepository;

    @MockBean
    private CuotaService cuotaService;

    @MockBean
    private CuotaRepository cuotaRepository;

    @MockBean
    private EstudianteService estudianteService;

    @MockBean
    EstudianteRepository estudianteRepository;

    @Test
    public void testCrearArancel() {
        // Setup
        Estudiante estudiante = new Estudiante();
        estudiante.setRut("12345678-9");
        estudiante.setNumeroCuotas(Integer.valueOf(5));
        estudiante.setTipoColegio("Municipal");
        estudiante.setEgreso(Integer.valueOf(2020));

        // Ejecutamos
        arancelService.crearArancel(estudiante);

        // Afirmamos que se llamó al método save() del repositorio
        verify(arancelRepository, times(1)).save(any(Arancel.class));
    }

    @Test
    public void testDiferenciaFechaActual() {
        int fecha = 2020;
        int expectedDifference = LocalDate.now().getYear() - fecha;

        int result = arancelService.diferenciaFechaActual(fecha);
        assertEquals(expectedDifference, result);
    }

    @Test
    public void testCalcularDescuento_Municipal() {
        String tipoColegio = "Municipal";
        int aniosEgreso = 3;

        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);

        float result = arancelService.calcularDescuento(tipoColegio, aniosEgreso);
        assertEquals("0.24", df.format(result));  // Basado en la lógica del método
    }

    @Test
    public void testBuscarCuotas() {
        String rut = "12";
        ArrayList<Cuota> cuotas = new ArrayList<>();
        when(cuotaService.buscarCuotasPorRut(rut)).thenReturn(cuotas);

        ArrayList<Cuota> result = arancelService.buscarCuotas(rut);
        assertEquals(cuotas, result);
    }

    @Test
    public void testBuscarPorRut() {
        String rut = "12";
        Arancel arancel = new Arancel();
        when(arancelRepository.findByRut(rut)).thenReturn(arancel);

        Arancel result = arancelRepository.findByRut(rut);
        assertEquals(arancel, result);
    }

    @Test
    public void testActualizarArancel() {
        // Configura tus objetos de prueba
        String rut = "1234567890";
        Arancel arancel = new Arancel();
        arancel.setNumCuotas(Integer.valueOf(2)); // Establece un número de cuotas para que entre en la condición
        arancelRepository.save(arancel);
        when(arancelRepository.findByRut(rut)).thenReturn(arancel);

        ArrayList<Cuota> cuotas = new ArrayList<>();
        // Agrega cuotas a la lista de cuotas
        Cuota cuota1 = new Cuota();
        cuota1.setIdArancel(arancel.getIdArancel());
        cuota1.setMonto(Integer.valueOf(10000));
        cuotaRepository.save(cuota1);
        cuotas.add(cuota1);

        when(cuotaService.buscarCuotasPorRut(rut)).thenReturn(cuotas);

        // Llama al método que deseas probar
        arancelService.actualizarArancel(rut);

        // Verifica que las llamadas a los métodos esperados se hayan realizado
        verify(arancelRepository, times(1)).save(arancel);

        arancelRepository.delete(arancel);
        cuotaRepository.delete(cuota1);
    }


}
