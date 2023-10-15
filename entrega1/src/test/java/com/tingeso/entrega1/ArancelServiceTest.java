package com.tingeso.entrega1;

import com.tingeso.entrega1.entities.Arancel;
import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.entities.Estudiante;
import com.tingeso.entrega1.repositories.ArancelRepository;
import com.tingeso.entrega1.services.ArancelService;
import com.tingeso.entrega1.services.CuotaService;
import com.tingeso.entrega1.services.EstudianteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
    private EstudianteService estudianteService;

    @Test
    public void testCrearArancel() {
        // Setup
        Estudiante estudiante = new Estudiante();
        estudiante.setRut("12345678-9");
        estudiante.setNumeroCuotas(5);
        estudiante.setTipoColegio("Municipal");
        estudiante.setEgreso(2020);

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
}
