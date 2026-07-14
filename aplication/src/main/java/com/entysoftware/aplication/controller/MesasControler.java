package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.model.dto.MesasDto;
import com.entysoftware.aplication.service.service_Implement.MesasServiceImp;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/mesas")
public class MesasControler {
    private final MesasServiceImp mesasService;
    public MesasControler(MesasServiceImp mesasService){
        this.mesasService = mesasService;
    }
    @GetMapping("/listar-mesas/{idEstablecimiento}")
    public ResponseEntity<List<MesasDto>> listarMesas(@PathVariable("idEstablecimiento") Integer idEstablecimiento) {
        return mesasService.listarMesas(idEstablecimiento);
    }

    @PostMapping("/crear-mesa")
    public ResponseEntity<MesasDto> crearMesa(@RequestBody MesasDto mesa) {
        return mesasService.crearMesa(mesa);
    }
 
    @PatchMapping("/editar-mesa")
    public ResponseEntity<String> editarMesa(@RequestBody MesasDto mesa) {
        return mesasService.editarMesa(mesa);
    }

    @DeleteMapping("/eliminar-mesa/{idMesa}")
    public ResponseEntity<String> eliminarMesa(@PathVariable("idMesa") Integer idMesa) {
        return mesasService.eliminarMesa(idMesa);
    }

}
