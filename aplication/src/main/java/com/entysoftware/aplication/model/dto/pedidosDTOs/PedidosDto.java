package com.entysoftware.aplication.model.dto.pedidosDTOs;

import java.time.LocalDate;
import java.util.List;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidosDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idPedido;

    private Integer idMesa;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String tipoPago; 

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String estadoPedido; 

    private Integer valorDomicilio; 
    private Integer precioTotal; 

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaPedido; 

    private String descripcion; 

    private List<DetallesPedidoDto> pedido; 


}
