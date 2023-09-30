package com.tingeso.entrega1.services;

import com.tingeso.entrega1.repositories.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuotaService {
    @Autowired
    CuotaRepository cuotaRepository;
}
