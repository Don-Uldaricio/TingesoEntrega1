package com.tingeso.entrega1.controllers;

import com.tingeso.entrega1.services.ArancelService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ArancelController {

    private final ArancelService arancelService;

    public ArancelController(ArancelService arancelService) {
        this.arancelService = arancelService;
    }
}
