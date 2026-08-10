package com.entysoftware.aplication.service.service_Implement;


import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.model.dto.cierreCaja.CierreDiaDto;
import com.entysoftware.aplication.repository.BaseInicialRepository;
import com.entysoftware.aplication.repository.CostosRepository;
import com.entysoftware.aplication.repository.EncabezadoPedidosRepository;
import com.entysoftware.aplication.service.CierreDiaInterface;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CierreDiaServiceImp implements CierreDiaInterface {

    private final EncabezadoPedidosRepository encabezadoPedidosRepository;
    private final CostosRepository costosRepository;
    private final BaseInicialRepository baseInicialRepository;

    public CierreDiaServiceImp(BaseInicialRepository baseInicialRepository,EncabezadoPedidosRepository encabezadoPedidosRepository,CostosRepository costosRepository){
        this.encabezadoPedidosRepository = encabezadoPedidosRepository;
        this.costosRepository = costosRepository;
        this.baseInicialRepository = baseInicialRepository;
    }

    public ResponseEntity<CierreDiaDto> cierreDia(Integer idEstablecimiento){
        LocalDate fechaHoy = LocalDate.now();

        VentasDelDia ventas = calcularVentasDelDia(idEstablecimiento, fechaHoy);
        CostosDelDia costos = calcularCostosDelDia(idEstablecimiento, fechaHoy);

        CierreDiaDto cierreDelDia = construirCierreDiaDto(idEstablecimiento, fechaHoy, ventas, costos);

        return ResponseEntity.ok(cierreDelDia);
    }

    private VentasDelDia calcularVentasDelDia(Integer idEstablecimiento, LocalDate fechaHoy){
        Integer ventasTotalesEfectivo = encabezadoPedidosRepository.sumarIngresosEfectivoDelDia(idEstablecimiento, fechaHoy);
        Integer ventasTotalesTrasferencia = encabezadoPedidosRepository.sumarIngresosTransferenciaDelDia(idEstablecimiento, fechaHoy);
        Integer baseInicial = baseInicialRepository.buscarValorBaseInicialHoy(idEstablecimiento, fechaHoy);
        Integer ventaTotalDelDiaSinCostos = ventasTotalesEfectivo + ventasTotalesTrasferencia + baseInicial;

        return new VentasDelDia(ventasTotalesEfectivo, ventasTotalesTrasferencia, baseInicial, ventaTotalDelDiaSinCostos);
    }

    private CostosDelDia calcularCostosDelDia(Integer idEstablecimiento, LocalDate fechaHoy){
        Integer costosTotalesEfectivo = costosRepository.sumarGastosEfectivoDelDia(idEstablecimiento, fechaHoy);
        Integer costosTotalesTrasferencia = costosRepository.sumarGastosTransferenciaDelDia(idEstablecimiento, fechaHoy);
        Integer costoTotalDelDia = costosTotalesEfectivo + costosTotalesTrasferencia;

        return new CostosDelDia(costosTotalesEfectivo, costosTotalesTrasferencia, costoTotalDelDia);
    }

    private CierreDiaDto construirCierreDiaDto(Integer idEstablecimiento, LocalDate fechaHoy, VentasDelDia ventas, CostosDelDia costos){
        Integer ventaTotalNetaDelDia = ventas.ventaTotalDelDiaSinCostos() - costos.costoTotalDelDia();
        Integer ventasEnTrasferenciaNeta = ventas.ventasTotalesTrasferencia() - costos.costosTotalesTrasferencia();
        Integer ventasEnEfectivoNeta = ventas.ventasTotalesEfectivo() - costos.costosTotalesEfectivo();

        return new CierreDiaDto(
            encabezadoPedidosRepository.contarPedidosDelDia(idEstablecimiento, fechaHoy),
            ventas.ventasTotalesTrasferencia(),
            ventas.ventasTotalesEfectivo(),
            ventas.baseInicial(),
            ventas.ventaTotalDelDiaSinCostos(),
            costos.costosTotalesEfectivo(),
            costos.costosTotalesTrasferencia(),
            costos.costoTotalDelDia(),
            ventasEnEfectivoNeta,
            ventasEnTrasferenciaNeta,
            ventaTotalNetaDelDia
        );
    }

    private record VentasDelDia(Integer ventasTotalesEfectivo, Integer ventasTotalesTrasferencia, Integer baseInicial, Integer ventaTotalDelDiaSinCostos) {}

    private record CostosDelDia(Integer costosTotalesEfectivo, Integer costosTotalesTrasferencia, Integer costoTotalDelDia) {}
}
