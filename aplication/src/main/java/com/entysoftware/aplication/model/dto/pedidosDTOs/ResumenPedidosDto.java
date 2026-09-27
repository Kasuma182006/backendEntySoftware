package com.entysoftware.aplication.model.dto.pedidosDTOs;

import com.entysoftware.aplication.model.dto.PaginaDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Página de pedidos de un establecimiento junto con el resumen financiero del total filtrado (independiente de la paginación).")
public class ResumenPedidosDto {

    @Schema(description = "Página de pedidos que cumplen el filtro.")
    private PaginaDto<PedidosDto> pedidos;

    @Schema(description = "Suma del precio total de todos los pedidos filtrados.", example = "1250000")
    private Integer total;

    @Schema(description = "Precio promedio por pedido, entre todos los pedidos filtrados.", example = "45000")
    private Integer promedio;

    @Schema(description = "Suma del precio total de los pedidos pagados en efectivo.", example = "800000")
    private Integer efectivo;

    @Schema(description = "Suma del precio total de los pedidos pagados con tarjeta.", example = "300000")
    private Integer tarjeta;

    @Schema(description = "Suma del precio total de los pedidos pagados por transferencia.", example = "150000")
    private Integer transferencia;
}
