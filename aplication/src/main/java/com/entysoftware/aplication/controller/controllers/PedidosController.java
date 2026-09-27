package com.entysoftware.aplication.controller.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.controller.controllerAdviceDto.ControllerAdviceDto;
import com.entysoftware.aplication.model.dto.pagosDTOs.FacturaPedidoDto;
import com.entysoftware.aplication.model.dto.pagosDTOs.PagarPedidoDto;
import com.entysoftware.aplication.model.dto.pedidosDTOs.PedidosDto;
import com.entysoftware.aplication.model.dto.pedidosDTOs.ResumenPedidosDto;
import com.entysoftware.aplication.service.interfaces.PedidosInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Creación, consulta, edición y pago de pedidos asociados a las mesas de un establecimiento.")
public class PedidosController {

    private final PedidosInterface pedidosInterface;

    public PedidosController(PedidosInterface pedidosInterface){
        this.pedidosInterface = pedidosInterface;
    }

    @Operation(
        summary = "Crear un pedido",
        description = "Registra un nuevo pedido para una mesa, junto con el detalle de productos solicitados. El pedido se crea con estado 'EN ESPERA' y tipo de pago 'EFECTIVO' por defecto. Devuelve el identificador del pedido creado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido creado correctamente. Se retorna el identificador del pedido."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al crear el pedido (por ejemplo, si la mesa o alguno de los productos indicados no existe).", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PostMapping("/crear-pedido")
    public ResponseEntity<Integer> crearPedido(@RequestBody PedidosDto pedido) {
        return pedidosInterface.crearPedido(pedido);
    }


    @Operation(
        summary = "Listar pedidos del día",
        description = "Devuelve todos los pedidos realizados en la fecha actual para el establecimiento indicado, incluyendo el detalle de productos de cada pedido."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de pedidos del día obtenido correctamente (puede estar vacío)."),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al consultar los pedidos del día.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @GetMapping("/pedidos-hoy/{idEstablecimiento}")
    public ResponseEntity<List<PedidosDto>> pedidosHoy(
        @Parameter(description = "Identificador del establecimiento cuyos pedidos del día se desean consultar.", example = "1", required = true)
        @PathVariable("idEstablecimiento")Integer idEstablecimiento){
        return pedidosInterface.pedidosHoy(idEstablecimiento);
    }


    @Operation(
        summary = "Editar un pedido",
        description = "Actualiza parcialmente los datos de un pedido existente (mesa, tipo de pago, estado, valores, descripción y/o detalle de productos). Los campos enviados como null se conservan sin cambios; el campo idPedido es obligatorio para identificar el registro a actualizar."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido actualizado correctamente."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al editar el pedido (por ejemplo, si el pedido indicado no existe).", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PatchMapping("/editar-pedido")
    public ResponseEntity<String> editarPedido(@RequestBody PedidosDto editarPedido){
        return pedidosInterface.editarPedido(editarPedido);
    }

    @Operation(
        summary = "Pagar un pedido",
        description = "Registra el pago de un pedido existente, calcula el cambio a devolver, actualiza su estado a 'PAGO' y genera la factura correspondiente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago registrado correctamente. Se retorna la factura del pedido."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al procesar el pago (por ejemplo, si el pedido indicado no existe).", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PostMapping("/pagar-pedido")
    public ResponseEntity<FacturaPedidoDto> pagarPedido(@RequestBody PagarPedidoDto pagoPedido) {
        return pedidosInterface.pagoPedido(pagoPedido);
    }

    @Operation(
        summary = "Listar pedidos de un establecimiento (paginado y filtrado por fecha)",
        description = "Devuelve una página de pedidos del establecimiento indicado (a través de sus mesas), del más reciente al más antiguo, incluyendo el detalle de productos de cada pedido, junto con el resumen financiero (total, promedio y sumas por tipo de pago: efectivo, tarjeta y transferencia) de todos los pedidos que cumplen el filtro, sin importar la página consultada. "
            + "Filtro de fecha: es obligatorio enviar al menos 'fechaInicio'; enviando solo esa fecha se filtra únicamente ese día; enviando también 'fechaFin' se filtra el rango entre ambas fechas (inclusivo). "
            + "El tamaño de página máximo es 100. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de pedidos y resumen financiero obtenidos correctamente (el contenido puede estar vacío)."),
        @ApiResponse(responseCode = "400", description = "Falta la fecha de inicio, el rango de fechas es inválido, o algún parámetro está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-26T14:32:05.123\",\"status\":\"400\",\"error\":\"Bad Request\",\"mensaje\":\"La fecha de inicio 2026-09-12 no puede ser posterior a la fecha fin 2026-09-01\",\"path\":\"/pedidos/listar-pedidos/1\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al consultar los pedidos.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @GetMapping("/listar-pedidos/{idEstablecimiento}")
    public ResponseEntity<ResumenPedidosDto> listarPedidos(
        @Parameter(description = "Identificador del establecimiento cuyos pedidos se desean listar.", example = "1", required = true)
        @PathVariable("idEstablecimiento") Integer idEstablecimiento,
        @Parameter(description = "Fecha a consultar, o inicio del rango si se envía 'fechaFin' (formato yyyy-MM-dd). Obligatoria.", example = "2026-09-01", required = true)
        @RequestParam(name = "fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
        @Parameter(description = "Fin del rango, inclusivo (formato yyyy-MM-dd). Opcional: sin ella se filtra únicamente 'fechaInicio'.", example = "2026-09-12")
        @RequestParam(name = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
        @Parameter(description = "Número de página a consultar (inicia en 0).", example = "0")
        @RequestParam(name = "pagina", defaultValue = "0") int pagina,
        @Parameter(description = "Cantidad de elementos por página (máximo 100).", example = "10")
        @RequestParam(name = "tamano", defaultValue = "10") int tamano) {
        return pedidosInterface.listarPedidos(idEstablecimiento, fechaInicio, fechaFin, pagina, tamano);
    }

}
