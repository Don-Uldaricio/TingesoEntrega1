package com.tingeso.entrega1;

import com.tingeso.entrega1.entities.Estudiante;
import com.tingeso.entrega1.services.EstudianteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EstudianteTest {
    @Autowired
    private EstudianteService estudianteService;

    @Test
    public void testConstructor() {
        Estudiante estudiante = estudianteService.findByRut("12345");
        assertNotNull(estudiante);
    }

    @Test
    public void testGettersAndSetters() {
        Estudiante estudiante = new Estudiante();
        estudiante.setRut("12345678-9");
        estudiante.setApellidos("Apellido");
        estudiante.setNombres("Nombre");
        estudiante.setFechaNacimiento("2000-01-01");

        assertEquals("12345678-9", estudiante.getRut());
        assertEquals("Apellido", estudiante.getApellidos());
        assertEquals("Nombre", estudiante.getNombres());
        assertEquals("2000-01-01", estudiante.getFechaNacimiento());
    }
}
