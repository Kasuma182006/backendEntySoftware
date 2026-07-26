package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.model.dto.baseDia.BaseInicialDto;
import com.entysoftware.aplication.model.dto.baseDia.RespuestaBaseInicialDto;
import com.entysoftware.aplication.service.CajaInterface;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/caja")
@Slf4j
public class CajaControler {
    
    private CajaInterface cajaInterface;

    public CajaControler(CajaInterface cajaInterface){
        this.cajaInterface = cajaInterface;
    }

    @PostMapping("/apertura")
    public ResponseEntity<RespuestaBaseInicialDto> postMethodName(@RequestBody BaseInicialDto base) {
        
        log.debug("guardando base: {} ", base);
        return cajaInterface.aperturaDia(base);
    }
    

}
