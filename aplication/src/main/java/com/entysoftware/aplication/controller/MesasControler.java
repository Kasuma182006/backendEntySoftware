package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.model.dto.MesasDto;
import com.entysoftware.aplication.service.service_Implement.MesasService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/mesas")
public class MesasControler {
    private final MesasService mesasService;
    public MesasControler(MesasService mesasService){
        this.mesasService = mesasService;
    }
    @GetMapping("/listar-mesas/{idEstablecimiento}")
    public ResponseEntity<List<MesasDto>> listarMesas(@PathVariable("idEstablecimiento") Integer idEstablecimiento) {
        return mesasService.listarMesas(idEstablecimiento);
    }
    
}
