package com.entysoftware.aplication.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetallesPedidoDto
{   
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Integer idCuerpoPedido;

    Integer idProducto;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    String nombre;
    
    Integer cantidad;

 }
