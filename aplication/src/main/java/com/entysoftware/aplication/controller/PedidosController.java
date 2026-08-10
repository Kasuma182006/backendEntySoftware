package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.controller.controllerAdviceDto.ControllerAdviceDto;
import com.entysoftware.aplication.model.dto.pagosDTOs.FacturaPedidoDto;
import com.entysoftware.aplication.model.dto.pagosDTOs.PagarPedidoDto;
import com.entysoftware.aplication.model.dto.pedidosDTOs.PedidosDto;
import com.entysoftware.aplication.service.PedidosInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;




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


}
