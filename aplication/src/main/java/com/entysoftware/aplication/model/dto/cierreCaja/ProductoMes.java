package com.entysoftware.aplication.model.dto.cierreCaja;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoMes {
    private Integer id;
    private String nombre;
    private Integer cantidadUnidadesVendidas;
    private Integer cantidadIngresos;
}
