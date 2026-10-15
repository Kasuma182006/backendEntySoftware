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

    @Schema(description = "Total de costos/gastos pagados en efectivo durante el día.", example = "60000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadCostosEnEfectivo;

    @Schema(description = "Total de costos/gastos pagados por transferencia durante el día.", example = "20000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadCostosEnTrasferencia;
    
    @Schema(description = "Efectivo que debería haber en caja al cierre: base inicial + ingresos en efectivo - costos pagados en efectivo. Es el valor a comparar con el efectivo contado en el cuadre.", example = "310000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadIngresosTotalesEnEfectivoNeta;

    @Schema(description = "Ingresos netos por transferencia del día, luego de restar los costos pagados por transferencia.", example = "430000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadIngresosEnTrasferenciaNeta;

    @Schema(description = "Ingresos netos por tarjeta", example = "30000",requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadIngresosEnTarjeta;

    @Schema(description = "Total cobrado por domicilios en los pedidos pagados del día. No hace parte de los ingresos anteriores porque corresponde al domiciliario.", example = "25000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadDomicilios;


}

