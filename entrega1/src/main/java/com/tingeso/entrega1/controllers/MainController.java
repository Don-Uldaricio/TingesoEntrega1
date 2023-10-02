package com.tingeso.entrega1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class MainController {

    @GetMapping("/main")
    public String mostrarMain() {
        return "main"; // Esto corresponde a "main.html" en templates/
    }
}
