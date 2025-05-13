package com.tingeso.entrega1.controllers;

import com.tingeso.entrega1.entities.Cuota;
import com.tingeso.entrega1.services.CuotaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class CuotaController {

    private final CuotaService cuotaService;

    public CuotaController(CuotaService cuotaService) {
        this.cuotaService = cuotaService;
    }

    @PostMapping("/pagarCuota")
    public String modificarCuota(@RequestParam Integer idCuota) {
        Cuota cuota = cuotaService.buscarCuotaPorId(idCuota);
        if (cuota != null) {
            cuota = cuotaService.pagarCuota(cuota);
            cuotaService.guardarCuota(cuota);
        }
        return "redirect:/registrarPago";
    }
}
