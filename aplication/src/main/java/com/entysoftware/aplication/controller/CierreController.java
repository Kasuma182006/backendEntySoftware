package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.model.dto.cierreCaja.CierreDiaDto;
import com.entysoftware.aplication.service.service_Implement.CierreDiaServiceImp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/cierre")
public class CierreController {
    private final CierreDiaServiceImp cierreDiaServiceImp;

    public CierreController(CierreDiaServiceImp cierreDiaServiceImp){
        this.cierreDiaServiceImp = cierreDiaServiceImp;
    }
    
    @GetMapping("/cierre-dia/{idEstablecimiento}")
    public ResponseEntity<CierreDiaDto> cierreDia(@PathVariable("idEstablecimiento") Integer idEstablecimiento) {
        return cierreDiaServiceImp.cierreDia(idEstablecimiento);
    }
    
}
