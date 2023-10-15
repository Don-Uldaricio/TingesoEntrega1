package com.tingeso.entrega1;

import com.tingeso.entrega1.services.SubirArchivoService;
import com.tingeso.entrega1.services.EstudianteService;
import com.tingeso.entrega1.repositories.SubirArchivoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SubirArchivoServiceTest {

    @Autowired
    private SubirArchivoService subirArchivoService;

    @MockBean
    private EstudianteService estudianteService;

}
