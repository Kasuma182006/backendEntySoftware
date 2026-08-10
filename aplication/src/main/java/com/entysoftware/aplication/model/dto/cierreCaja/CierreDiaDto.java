package com.entysoftware.aplication.model.dto.cierreCaja;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Resumen del cierre de caja del día para un establecimiento: ventas, costos e ingresos netos discriminados por método de pago.")
public class CierreDiaDto {

    @Schema(description = "Cantidad total de pedidos realizados en el día.", example = "34", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadPedidosRealizadosHoy;

    @Schema(description = "Total de ingresos recibidos por transferencia en el día, sin restar costos.", example = "450000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadIngresosEnTrasferenciaSinRestarCostos;

    @Schema(description = "Total de ingresos recibidos en efectivo en el día, sin restar costos.", example = "320000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadIngresosTotalesEnEfectivoSinRestarCostos;

    @Schema(description = "Base inicial (apertura de caja) registrada para el día.", example = "50000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer baseInicialDelDia;

    @Schema(description = "Ganancia total del día (efectivo + transferencia + base inicial) sin restar costos.", example = "820000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadGananciaTotalDelDiaSinRestarCostos;

    @Schema(description = "Total de costos/gastos pagados en efectivo durante el día.", example = "60000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadCostosEnEfectivo;

    @Schema(description = "Total de costos/gastos pagados por transferencia durante el día.", example = "20000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadCostosEnTrasferencia;

    @Schema(description = "Suma total de todos los costos/gastos del día.", example = "80000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadCostosTotalesDelDia;

    @Schema(description = "Ingresos netos en efectivo del día, luego de restar los costos pagados en efectivo.", example = "260000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadIngresosTotalesEnEfectivoNeta;

    @Schema(description = "Ingresos netos por transferencia del día, luego de restar los costos pagados por transferencia.", example = "430000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadIngresosEnTrasferenciaNeta;

    @Schema(description = "Ganancia neta total del día, luego de restar todos los costos.", example = "740000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadGananciaNetaDelDia;

}

