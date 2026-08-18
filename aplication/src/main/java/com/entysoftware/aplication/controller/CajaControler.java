package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.controller.controllerAdviceDto.ControllerAdviceDto;
import com.entysoftware.aplication.model.dto.EstablecimientoEstadoCajaDto;
import com.entysoftware.aplication.model.dto.baseDia.BaseInicialDto;
import com.entysoftware.aplication.model.dto.baseDia.RespuestaBaseInicialDto;
import com.entysoftware.aplication.service.CajaInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Operation(
        summary = "Consultar estado de caja del establecimiento",
        description = "Devuelve el identificador, nombre y estado del establecimiento indicado, para conocer el estado de la caja."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado del establecimiento obtenido correctamente."),
        @ApiResponse(responseCode = "404", description = "El establecimiento indicado no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-08-10T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"No se ha encontrado el establecimiento con ID 1\",\"path\":\"/caja/establecimiento-estado-caja/1\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al consultar el estado del establecimiento.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @GetMapping("/establecimiento-estado-caja/{idEstablecimiento}")
    public ResponseEntity<EstablecimientoEstadoCajaDto> establecimientoEstadoCaja(
        @Parameter(description = "Identificador del establecimiento cuyo estado se desea consultar.", example = "1", required = true)
        @PathVariable("idEstablecimiento") Integer idEstablecimiento) {
        return cajaInterface.establecimientoEstadoCaja(idEstablecimiento);
    }

}
