package com.entysoftware.aplication.controller.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.controller.controllerAdviceDto.ControllerAdviceDto;
import com.entysoftware.aplication.model.dto.CostosDto;
import com.entysoftware.aplication.service.interfaces.CostosInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/costos")
@Tag(name = "Costos", description = "Registro de costos (egresos) de un establecimiento.")
public class CostosController {

    private final CostosInterface costosService;

    public CostosController(CostosInterface costosService) {
        this.costosService = costosService;
    }

    @Operation(
        summary = "Registrar un costo",
        description = "Registra un nuevo costo (egreso) para el establecimiento indicado en el cuerpo de la petición. La fecha del costo la asigna el backend automáticamente. Exclusivo del rol ADMINISTRADOR. Recuerda que el campo 'TipoPago' debe ser un String como 'EFECTIVO' o 'TRANSFERENCIA' dependiendo dek tipo de costo que haya sido"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Costo registrado correctamente."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "404", description = "El establecimiento indicado no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-03T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"El establecimiento con ID 1 no existe\",\"path\":\"/costos/crear-costo\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al registrar el costo.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PostMapping("/crear-costo")
    public ResponseEntity<String> crearCosto(@RequestBody CostosDto costo) {
        return costosService.crearCosto(costo);
    }
}
