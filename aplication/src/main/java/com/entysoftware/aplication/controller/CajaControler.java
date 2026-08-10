package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.controller.controllerAdviceDto.ControllerAdviceDto;
import com.entysoftware.aplication.model.dto.baseDia.BaseInicialDto;
import com.entysoftware.aplication.model.dto.baseDia.RespuestaBaseInicialDto;
import com.entysoftware.aplication.service.CajaInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/caja")
@Slf4j
@Tag(name = "Caja", description = "Operaciones de apertura y gestión de la caja diaria de un establecimiento.")
public class CajaControler {

    private final CajaInterface cajaInterface;

    public CajaControler(CajaInterface cajaInterface){
        this.cajaInterface = cajaInterface;
    }

    @Operation(
        summary = "Registrar apertura de caja",
        description = "Registra la apertura de caja (base inicial) del día para un establecimiento, guardando el valor con el que inicia operaciones y la hora de apertura."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Apertura de caja registrada correctamente."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al registrar la apertura de caja.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PostMapping("/apertura")
    public ResponseEntity<RespuestaBaseInicialDto> registrarAperturaDia(@RequestBody BaseInicialDto base) {

        log.debug("guardando base: {} ", base);
        return cajaInterface.aperturaDia(base);
    }

}
