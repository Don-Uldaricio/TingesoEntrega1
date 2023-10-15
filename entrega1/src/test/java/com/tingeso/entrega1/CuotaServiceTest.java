package com.tingeso.entrega1;

import com.tingeso.entrega1.entities.Arancel;
import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.repositories.CuotaRepository;
import com.tingeso.entrega1.services.CuotaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CuotaServiceTest {

    @Autowired
    private CuotaService cuotaService;

    @MockBean
    private CuotaRepository cuotaRepository;

    @Test
    public void testBuscarCuotaPorId() {
        // Setup
        Integer idCuota = 1;
        Cuota cuota = new Cuota();
        when(cuotaRepository.findById(idCuota)).thenReturn(cuota);

        // Ejecutamos
        Cuota result = cuotaService.buscarCuotaPorId(idCuota);

        // Afirmamos
        assertEquals(cuota, result);
    }

    @Test
    public void testGuardarCuota() {
        // Setup
        Cuota cuota = new Cuota();

        // Ejecutamos
        cuotaService.guardarCuota(cuota);

        // Afirmamos que se llamó al método save() del repositorio
        verify(cuotaRepository, times(1)).save(cuota);
    }

    @Test
    public void testCrearCuotas() {
        // Setup
        Arancel arancel = new Arancel();
        arancel.setNumCuotas(5);
        arancel.setMonto(10000);

        // Ejecutamos
        cuotaService.crearCuotas(arancel);

        // Afirmamos que se llamó al método save() del repositorio 5 veces (por el número de cuotas)
        verify(cuotaRepository, times(5)).save(any(Cuota.class));
    }

    @Test
    public void testBuscarCuotasPorRut() {
        // Setup
        String rut = "12345678-9";
        ArrayList<Cuota> cuotas = new ArrayList<>();
        when(cuotaRepository.findByRutEstudiante(rut)).thenReturn(cuotas);

        // Ejecutamos
        ArrayList<Cuota> result = cuotaService.buscarCuotasPorRut(rut);

        // Afirmamos
        assertEquals(cuotas, result);
    }

    @Test
    public void testListarCuotas() {
        // Setup
        Long idArancel = 1L;
        ArrayList<Cuota> cuotas = new ArrayList<>();
        when(cuotaRepository.findByIdArancel(idArancel)).thenReturn(cuotas);

        // Ejecutamos
        ArrayList<Cuota> result = cuotaService.listarCuotas(idArancel);

        // Afirmamos
        assertEquals(cuotas, result);
    }

    @Test
    public void testPagarCuota() {
        // Setup
        Cuota cuota = new Cuota();
        cuota.setPagado(false);

        // Ejecutamos
        cuotaService.pagarCuota(cuota);

        // Afirmamos
        assertTrue(cuota.getPagado());
        assertNotNull(cuota.getFechaPago());
    }

    @Test
    public void testActualizarCuotas() {
        // Setup
        ArrayList<Cuota> cuotas = new ArrayList<>();
        Cuota cuota1 = new Cuota();
        Cuota cuota2 = new Cuota();
        cuota1.setPagado(false);
        cuota2.setPagado(false);
        cuota1.setFechaExp("2023-05-05");
        cuota2.setFechaExp("2023-06-05");
        cuotas.add(cuota1);
        cuotas.add(cuota2);

        // Ejecutamos
        cuotaService.actualizarCuotas(cuotas);

        assertEquals(5, cuotaService.calcularMesesAtraso(cuota1));
        assertEquals(4, cuotaService.calcularMesesAtraso(cuota2));
    }

    @Test
    public void testCalcularAtrasoCuota() {
        // Setup
        Cuota cuota = new Cuota();
        cuota.setFechaExp(String.valueOf(LocalDate.now().minusMonths(3)));  // Establecemos una fecha de expiración 3 meses atrás

        // Ejecutamos
        cuotaService.calcularAtrasoCuota(cuota);

        // Afirmamos
        assertEquals(3, cuota.getMesesAtraso());  // Debería haber 3 meses de atraso
        assertTrue(cuota.getInteres() > 0);       // Debería haber intereses aplicados
    }

    @Test
    public void testCalcularMesesAtraso() {
        // Setup
        Cuota cuota = new Cuota();
        cuota.setFechaExp(String.valueOf(LocalDate.now().minusMonths(2)));  // Establecemos una fecha de expiración 2 meses atrás

        // Ejecutamos
        int mesesAtraso = cuotaService.calcularMesesAtraso(cuota);

        // Afirmamos
        assertEquals(2, mesesAtraso);
    }

    @Test
    public void testCalcularDescuentoCuota() {
        // Setup
        Cuota cuota = new Cuota();
        cuota.setMonto(1000);
        Float descuento = 0.1f;  // 10% de descuento

        // Ejecutamos
        cuotaService.calcularDescuentoCuota(cuota, descuento);

        // Afirmamos
        assertEquals(0.1f, cuota.getDescuento());  // 10% de 1000 es 100
    }

}
