package com.entysoftware.aplication.controller.controllers;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.controller.controllerAdviceDto.ControllerAdviceDto;
import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.model.dto.cierreCaja.CierreDiaDto;
import com.entysoftware.aplication.model.dto.cierreCaja.CuadreCajaDto;
import com.entysoftware.aplication.service.interfaces.CierreCajaInterface;
import com.entysoftware.aplication.service.interfaces.CierreDiaInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/cierre")
@Tag(name = "Cierre de Caja", description = "Cálculo del resumen de cierre de caja del día y registro del cuadre de caja para un establecimiento.")
public class CierreController {
    private final CierreDiaInterface cierreDiaInterface;

    private final CierreCajaInterface cierreCajaInterface;

    public CierreController(CierreDiaInterface cierreDiaInterface, CierreCajaInterface cierreCajaInterface){
        this.cierreDiaInterface = cierreDiaInterface;
        this.cierreCajaInterface = cierreCajaInterface;
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

    @Operation(
        summary = "Registrar el cuadre de caja",
        description = "Guarda el cuadre de caja del establecimiento indicado en el cuerpo de la petición, con las cantidades contadas en transferencia, efectivo y tarjeta. La fecha la asigna el backend automáticamente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cuadre de caja registrado correctamente."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido, está mal formado, o algún campo no pasó las validaciones (obligatorio o valor negativo). El mensaje puede venir como una lista con un texto por cada campo inválido.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-16T18:05:00.123\",\"status\":\"400\",\"error\":\"Bad Request\",\"mensaje\":[\"El campo cantidadEfectivo no puede ser un valor negativo\",\"El campo idEstablecimiento es obligatorio\"],\"path\":\"/cierre/cuadre-caja\"}"))),
        @ApiResponse(responseCode = "404", description = "El establecimiento indicado no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-16T18:05:00.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"El establecimiento con ID 1 no existe\",\"path\":\"/cierre/cuadre-caja\"}"))),
        @ApiResponse(responseCode = "409", description = "Ya existe un cuadre de caja registrado hoy para ese establecimiento.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-16T18:05:00.123\",\"status\":\"409\",\"error\":\"Conflict\",\"mensaje\":\"Ya existe un cuadre de caja para el establecimiento con ID 1 en la fecha 2026-09-16\",\"path\":\"/cierre/cuadre-caja\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al registrar el cuadre de caja.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PostMapping("/cuadre-caja")
    public ResponseEntity<CuadreCajaDto> cuadreCaja(@Valid @RequestBody CuadreCajaDto cuadreCaja) {
        return cierreCajaInterface.cuadreCaja(cuadreCaja);
    }

    @Operation(
        summary = "Listar cierres de caja de un establecimiento (paginado y filtrado por fecha)",
        description = "Devuelve una página de cierres de caja del establecimiento indicado, del más reciente al más antiguo. "
            + "Filtro de fecha: es obligatorio enviar al menos 'fechaInicio'; enviando solo esa fecha se filtra únicamente ese día (la lista devuelta contendrá como máximo un cierre); enviando también 'fechaFin' se filtra el rango entre ambas fechas (inclusivo). "
            + "El tamaño de página máximo es 100. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de cierres de caja obtenida correctamente (el contenido puede estar vacío)."),
        @ApiResponse(responseCode = "400", description = "Falta la fecha de inicio, el rango de fechas es inválido, o algún parámetro está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-26T14:32:05.123\",\"status\":\"400\",\"error\":\"Bad Request\",\"mensaje\":\"La fecha de inicio 2026-09-12 no puede ser posterior a la fecha fin 2026-09-01\",\"path\":\"/cierre/listar-cierres-caja/1\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al consultar los cierres de caja.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @GetMapping("/listar-cierres-caja/{idEstablecimiento}")
    public ResponseEntity<PaginaDto<CuadreCajaDto>> listarCierresCaja(
        @Parameter(description = "Identificador del establecimiento cuyos cierres de caja se desean listar.", example = "1", required = true)
        @PathVariable("idEstablecimiento") Integer idEstablecimiento,
        @Parameter(description = "Fecha a consultar, o inicio del rango si se envía 'fechaFin' (formato yyyy-MM-dd). Obligatoria.", example = "2026-09-01", required = true)
        @RequestParam(name = "fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
        @Parameter(description = "Fin del rango, inclusivo (formato yyyy-MM-dd). Opcional: sin ella se filtra únicamente 'fechaInicio'.", example = "2026-09-12")
        @RequestParam(name = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
        @Parameter(description = "Número de página a consultar (inicia en 0).", example = "0")
        @RequestParam(name = "pagina", defaultValue = "0") int pagina,
        @Parameter(description = "Cantidad de elementos por página (máximo 100).", example = "10")
        @RequestParam(name = "tamano", defaultValue = "10") int tamano) {
        return cierreCajaInterface.listarCierresCaja(idEstablecimiento, fechaInicio, fechaFin, pagina, tamano);
    }

}
