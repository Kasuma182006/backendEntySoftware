package com.entysoftware.aplication.model.dto.cierreCaja;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Datos que se pintan en el PDF del cierre de caja: el resumen calculado por el sistema
 * (lo esperado) junto a lo que contó el cajero en el cuadre (lo contado).
 */
public record ReporteCierreCaja(
    Integer idCierreCaja,
    String nombreEstablecimiento,
    LocalDateTime fechaCuadre,
    LocalTime horaApertura,
    CierreDiaDto cierreDia,
    Integer contadoEfectivo,
    Integer contadoTransferencia,
    Integer contadoTarjeta
) {

    public Integer esperadoEfectivo() {
        return cierreDia.getCantidadIngresosTotalesEnEfectivoNeta();
    }

    public Integer esperadoTransferencia() {
        return cierreDia.getCantidadIngresosEnTrasferenciaNeta();
    }

    public Integer esperadoTarjeta() {
        return cierreDia.getCantidadIngresosEnTarjeta();
    }

    public Integer esperadoTotal() {
        return esperadoEfectivo() + esperadoTransferencia() + esperadoTarjeta();
    }

    public Integer contadoTotal() {
        return contadoEfectivo + contadoTransferencia + contadoTarjeta;
    }

    public List<FilaCuadre> filasCuadre() {
        return List.of(
            new FilaCuadre("Efectivo", esperadoEfectivo(), contadoEfectivo),
            new FilaCuadre("Transferencia", esperadoTransferencia(), contadoTransferencia),
            new FilaCuadre("Tarjeta", esperadoTarjeta(), contadoTarjeta)
        );
    }

    public FilaCuadre filaTotal() {
        return new FilaCuadre("Total", esperadoTotal(), contadoTotal());
    }

    public Integer ventasTotales() {
        return cierreDia.getCantidadIngresosTotalesEnEfectivoSinRestarCostos()
            + cierreDia.getCantidadIngresosEnTrasferenciaSinRestarCostos()
            + cierreDia.getCantidadIngresosEnTarjeta();
    }

    public Integer costosTotales() {
        return cierreDia.getCantidadCostosEnEfectivo() + cierreDia.getCantidadCostosEnTrasferencia();
    }

    /** Una fila de la tabla de cuadre: lo esperado por el sistema contra lo contado por el cajero. */
    public record FilaCuadre(String metodo, Integer esperado, Integer contado) {

        public Integer diferencia() {
            return contado - esperado;
        }

        public Integer diferenciaAbsoluta() {
            return Math.abs(diferencia());
        }

        /** CUADRA, FALTANTE o SOBRANTE, según el signo de la diferencia. */
        public String estado() {
            int diferencia = diferencia();
            if (diferencia == 0) {
                return "CUADRA";
            }
            return diferencia < 0 ? "FALTANTE" : "SOBRANTE";
        }
    }
}
