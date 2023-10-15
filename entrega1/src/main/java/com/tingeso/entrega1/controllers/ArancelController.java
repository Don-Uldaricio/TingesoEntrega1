package com.tingeso.entrega1.controllers;

import com.tingeso.entrega1.services.ArancelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ArancelController {
    @Autowired
    private ArancelService arancelService;
}
