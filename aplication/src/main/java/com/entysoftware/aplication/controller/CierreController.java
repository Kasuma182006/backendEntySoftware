package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.controller.controllerAdviceDto.ControllerAdviceDto;
import com.entysoftware.aplication.model.dto.cierreCaja.CierreDiaDto;
import com.entysoftware.aplication.service.CierreDiaInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/cierre")
@Tag(name = "Cierre de Caja", description = "Cálculo del resumen de cierre de caja del día para un establecimiento.")
public class CierreController {
    private final CierreDiaInterface cierreDiaInterface;

    public CierreController(CierreDiaInterface cierreDiaInterface){
        this.cierreDiaInterface = cierreDiaInterface;
    }

    @Operation(
        summary = "Obtener el cierre de caja del día",
        description = "Calcula y devuelve el resumen de cierre de caja del día actual para el establecimiento indicado: pedidos realizados, ingresos, costos y ganancias netas."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cierre de caja calculado correctamente."),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al calcular el cierre de caja (por ejemplo, si aún no se ha registrado la base inicial del día).", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @GetMapping("/cierre-dia/{idEstablecimiento}")
    public ResponseEntity<CierreDiaDto> cierreDia(
        @Parameter(description = "Identificador del establecimiento para el cual se calcula el cierre de caja.", example = "1", required = true)
        @PathVariable("idEstablecimiento") Integer idEstablecimiento) {
        return cierreDiaInterface.cierreDia(idEstablecimiento);
    }

}
