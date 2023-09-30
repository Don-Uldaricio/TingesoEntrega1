package com.tingeso.entrega1.services;

import com.tingeso.entrega1.repositories.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReporteService {
    @Autowired
    ReporteRepository reporteRepository;
}
