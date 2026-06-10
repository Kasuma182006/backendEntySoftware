package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.model.dto.BaseInicialDto;
import com.entysoftware.aplication.service.CajaInterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/caja")
public class CajaControler {
    
    private CajaInterface cajaInterface;

    public CajaControler(CajaInterface cajaInterface){
        this.cajaInterface = cajaInterface;
    }

    @PostMapping("/apertura")
    public ResponseEntity<?> postMethodName(@RequestBody BaseInicialDto base) {
        
        
        return cajaInterface.aperturaDia(base);
    }
    

}
