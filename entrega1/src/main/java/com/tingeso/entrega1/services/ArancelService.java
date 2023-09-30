package com.tingeso.entrega1.services;

import com.tingeso.entrega1.repositories.ArancelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ArancelService {
    @Autowired
    ArancelRepository arancelRepository;
}
