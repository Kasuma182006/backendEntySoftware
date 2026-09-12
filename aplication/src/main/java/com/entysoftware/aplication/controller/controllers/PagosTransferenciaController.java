package com.entysoftware.aplication.controller.controllers;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.controller.controllerAdviceDto.ControllerAdviceDto;
import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.model.dto.PagosTransferenciaDto;
import com.entysoftware.aplication.service.interfaces.PagosTransferenciaInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/pagos-transferencia")
@Tag(name = "Pagos por transferencia", description = "Gestión CRUD de los pagos por transferencia recibidos por un establecimiento.")
public class PagosTransferenciaController {

    private final PagosTransferenciaInterface pagosTransferenciaService;

    public PagosTransferenciaController(PagosTransferenciaInterface pagosTransferenciaService) {
        this.pagosTransferenciaService = pagosTransferenciaService;
    }

    @Operation(
        summary = "Listar pagos por transferencia de un establecimiento (paginado y filtrado por fecha)",
        description = "Devuelve una página de pagos por transferencia del establecimiento indicado, ordenados del más reciente al más antiguo. "
            + "Filtro de fecha: sin fechas se listan todos; enviando solo 'fechaInicio' o solo 'fechaFin' se filtra ese día; enviando ambas se filtra el rango (inclusivo). "
            + "El tamaño de página máximo es 100. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de pagos por transferencia obtenida correctamente (el contenido puede estar vacío)."),
        @ApiResponse(responseCode = "400", description = "El rango de fechas es inválido o algún parámetro está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-12T14:32:05.123\",\"status\":\"400\",\"error\":\"Bad Request\",\"mensaje\":\"La fecha de inicio 2026-09-12 no puede ser posterior a la fecha fin 2026-09-01\",\"path\":\"/pagos-transferencia/listar-pagos-transferencia/1\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al consultar los pagos por transferencia.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @GetMapping("/listar-pagos-transferencia/{idEstablecimiento}")
    public ResponseEntity<PaginaDto<PagosTransferenciaDto>> listarPagosTransferencia(
        @Parameter(description = "Identificador del establecimiento cuyos pagos por transferencia se desean listar.", example = "1", required = true)
        @PathVariable("idEstablecimiento") Integer idEstablecimiento,
        @Parameter(description = "Fecha inicial del filtro (formato yyyy-MM-dd). Opcional.", example = "2026-09-01")
        @RequestParam(name = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
        @Parameter(description = "Fecha final del filtro, inclusiva (formato yyyy-MM-dd). Opcional.", example = "2026-09-12")
        @RequestParam(name = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
        @Parameter(description = "Número de página a consultar (inicia en 0).", example = "0")
        @RequestParam(name = "pagina", defaultValue = "0") int pagina,
        @Parameter(description = "Cantidad de elementos por página (máximo 100).", example = "10")
        @RequestParam(name = "tamano", defaultValue = "10") int tamano) {
        return pagosTransferenciaService.listarPagosTransferencia(idEstablecimiento, fechaInicio, fechaFin, pagina, tamano);
    }

    @Operation(
        summary = "Registrar un pago por transferencia",
        description = "Registra un nuevo pago por transferencia para el establecimiento indicado en el cuerpo de la petición. La fecha la asigna el backend automáticamente. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago por transferencia registrado correctamente."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "404", description = "El establecimiento indicado no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-12T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"El establecimiento con ID 1 no existe\",\"path\":\"/pagos-transferencia/crear-pago-transferencia\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al registrar el pago por transferencia.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PostMapping("/crear-pago-transferencia")
    public ResponseEntity<PagosTransferenciaDto> crearPagoTransferencia(@RequestBody PagosTransferenciaDto pagoTransferencia) {
        return pagosTransferenciaService.crearPagoTransferencia(pagoTransferencia);
    }

    @Operation(
        summary = "Editar un pago por transferencia",
        description = "Actualiza parcialmente los datos de un pago por transferencia existente. Los campos enviados como null se conservan sin cambios; el campo idTransferencia es obligatorio para identificar el registro. El establecimiento y la fecha no se modifican. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago por transferencia actualizado correctamente."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "404", description = "El pago por transferencia indicado no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-12T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"El pago por transferencia con ID 12 no existe\",\"path\":\"/pagos-transferencia/editar-pago-transferencia\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al editar el pago por transferencia.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PatchMapping("/editar-pago-transferencia")
    public ResponseEntity<String> editarPagoTransferencia(@RequestBody PagosTransferenciaDto pagoTransferencia) {
        return pagosTransferenciaService.editarPagoTransferencia(pagoTransferencia);
    }

    @Operation(
        summary = "Eliminar un pago por transferencia",
        description = "Elimina de forma permanente el pago por transferencia indicado por su identificador. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago por transferencia eliminado correctamente."),
        @ApiResponse(responseCode = "404", description = "El pago por transferencia indicado no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-12T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"El pago por transferencia con ID 12 no existe\",\"path\":\"/pagos-transferencia/eliminar-pago-transferencia/12\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al eliminar el pago por transferencia.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @DeleteMapping("/eliminar-pago-transferencia/{idTransferencia}")
    public ResponseEntity<String> eliminarPagoTransferencia(
        @Parameter(description = "Identificador del pago por transferencia a eliminar.", example = "12", required = true)
        @PathVariable("idTransferencia") Integer idTransferencia) {
        return pagosTransferenciaService.eliminarPagoTransferencia(idTransferencia);
    }
}
