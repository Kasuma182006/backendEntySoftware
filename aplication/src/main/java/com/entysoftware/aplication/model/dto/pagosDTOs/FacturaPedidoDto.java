package com.entysoftware.aplication.model.dto.pagosDTOs;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Factura generada tras registrar el pago de un pedido.")
public class FacturaPedidoDto {

    @Schema(description = "Identificador del pedido pagado.", example = "45", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idPedido;

    @Schema(description = "Identificador de la mesa asociada al pedido.", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idMesa;

    @Schema(description = "Estado del pedido luego del pago.", example = "PAGO", requiredMode = Schema.RequiredMode.REQUIRED)
    private String estadoPedido;

    @Schema(description = "Valor total del pedido facturado.", example = "48000", requiredMode = Schema.RequiredMode.REQUIRED)
    private int valorTotal;

    @Schema(description = "Cambio (vueltas) a devolver al cliente, resultado de restar el valor total al valor pagado.", example = "2000", requiredMode = Schema.RequiredMode.REQUIRED)
    private int valorCambio;

    @Schema(description = "Método de pago utilizado.", example = "EFECTIVO", allowableValues = {"EFECTIVO", "TRANSFERENCIA"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoPago;

    @Schema(description = "Fecha y hora en que se generó la factura, en formato 'yyyy-MM-dd HH:mm'.", example = "2026-08-10 14:32", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fechaYHora;
}
