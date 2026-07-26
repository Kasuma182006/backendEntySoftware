package com.entysoftware.aplication.model.dto.cierreCaja;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CierreDiaDto {
    private Integer cantidadPedidosRealizadosHoy;
    private Integer cantidadIngresosEnTrasferenciaSinRestarCostos;
    private Integer cantidadIngresosTotalesEnEfectivoSinRestarCostos;
    private Integer baseInicialDelDia;
    private Integer cantidadGananciaTotalDelDiaSinRestarCostos;
    private Integer cantidadCostosEnEfectivo;
    private Integer cantidadCostosEnTrasferencia;
    private Integer cantidadCostosTotalesDelDia;
    private Integer cantidadIngresosTotalesEnEfectivoNeta;
    private Integer cantidadIngresosEnTrasferenciaNeta;
    private Integer cantidadGananciaNetaDelDia;


}   
