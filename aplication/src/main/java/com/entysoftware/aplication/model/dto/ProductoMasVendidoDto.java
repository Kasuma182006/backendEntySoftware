package com.entysoftware.aplication.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductoMasVendidoDto {
    private String nombreProducto;
    private Long cantidadVendida;
}
