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
import com.entysoftware.aplication.model.dto.PagosVoucherDto;
import com.entysoftware.aplication.service.interfaces.PagosVoucherInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/pagos-voucher")
@Tag(name = "Pagos con voucher", description = "Gestión CRUD de los pagos con voucher de tarjeta recibidos por un establecimiento.")
public class PagosVoucherController {

    private final PagosVoucherInterface pagosVoucherService;

    public PagosVoucherController(PagosVoucherInterface pagosVoucherService) {
        this.pagosVoucherService = pagosVoucherService;
    }

    @Operation(
        summary = "Listar pagos con voucher de un establecimiento (paginado y filtrado por fecha)",
        description = "Devuelve una página de pagos con voucher del establecimiento indicado, ordenados del más reciente al más antiguo. "
            + "Filtro de fecha: sin fechas se listan todos; enviando solo 'fechaInicio' o solo 'fechaFin' se filtra ese día; enviando ambas se filtra el rango (inclusivo). "
            + "El tamaño de página máximo es 100. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de pagos con voucher obtenida correctamente (el contenido puede estar vacío)."),
        @ApiResponse(responseCode = "400", description = "El rango de fechas es inválido o algún parámetro está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-12T14:32:05.123\",\"status\":\"400\",\"error\":\"Bad Request\",\"mensaje\":\"La fecha de inicio 2026-09-12 no puede ser posterior a la fecha fin 2026-09-01\",\"path\":\"/pagos-voucher/listar-pagos-voucher/1\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al consultar los pagos con voucher.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @GetMapping("/listar-pagos-voucher/{idEstablecimiento}")
    public ResponseEntity<PaginaDto<PagosVoucherDto>> listarPagosVoucher(
        @Parameter(description = "Identificador del establecimiento cuyos pagos con voucher se desean listar.", example = "1", required = true)
        @PathVariable("idEstablecimiento") Integer idEstablecimiento,
        @Parameter(description = "Fecha inicial del filtro (formato yyyy-MM-dd). Opcional.", example = "2026-09-01")
        @RequestParam(name = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
        @Parameter(description = "Fecha final del filtro, inclusiva (formato yyyy-MM-dd). Opcional.", example = "2026-09-12")
        @RequestParam(name = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
        @Parameter(description = "Número de página a consultar (inicia en 0).", example = "0")
        @RequestParam(name = "pagina", defaultValue = "0") int pagina,
        @Parameter(description = "Cantidad de elementos por página (máximo 100).", example = "10")
        @RequestParam(name = "tamano", defaultValue = "10") int tamano) {
        return pagosVoucherService.listarPagosVoucher(idEstablecimiento, fechaInicio, fechaFin, pagina, tamano);
    }

    @Operation(
        summary = "Registrar un pago con voucher",
        description = "Registra un nuevo pago con voucher para el establecimiento indicado en el cuerpo de la petición. La fecha (con hora y minutos, sin segundos) la asigna el backend automáticamente. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago con voucher registrado correctamente."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "404", description = "El establecimiento indicado no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-12T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"El establecimiento con ID 1 no existe\",\"path\":\"/pagos-voucher/crear-pago-voucher\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al registrar el pago con voucher.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PostMapping("/crear-pago-voucher")
    public ResponseEntity<PagosVoucherDto> crearPagoVoucher(@RequestBody PagosVoucherDto pagoVoucher) {
        return pagosVoucherService.crearPagoVoucher(pagoVoucher);
    }

    @Operation(
        summary = "Editar un pago con voucher",
        description = "Actualiza parcialmente un pago con voucher existente. Los campos enviados como null se conservan sin cambios; el campo idVoucher es obligatorio para identificar el registro. El establecimiento y la fecha no se modifican. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago con voucher actualizado correctamente."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "404", description = "El pago con voucher indicado no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-12T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"El pago con voucher con ID 8 no existe\",\"path\":\"/pagos-voucher/editar-pago-voucher\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al editar el pago con voucher.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PatchMapping("/editar-pago-voucher")
    public ResponseEntity<String> editarPagoVoucher(@RequestBody PagosVoucherDto pagoVoucher) {
        return pagosVoucherService.editarPagoVoucher(pagoVoucher);
    }

    @Operation(
        summary = "Eliminar un pago con voucher",
        description = "Elimina de forma permanente el pago con voucher indicado por su identificador. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago con voucher eliminado correctamente."),
        @ApiResponse(responseCode = "404", description = "El pago con voucher indicado no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-12T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"El pago con voucher con ID 8 no existe\",\"path\":\"/pagos-voucher/eliminar-pago-voucher/8\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al eliminar el pago con voucher.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @DeleteMapping("/eliminar-pago-voucher/{idVoucher}")
    public ResponseEntity<String> eliminarPagoVoucher(
        @Parameter(description = "Identificador del pago con voucher a eliminar.", example = "8", required = true)
        @PathVariable("idVoucher") Integer idVoucher) {
        return pagosVoucherService.eliminarPagoVoucher(idVoucher);
    }
}
