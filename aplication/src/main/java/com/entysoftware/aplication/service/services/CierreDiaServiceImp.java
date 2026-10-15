package com.entysoftware.aplication.service.services;


import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.model.dto.cierreCaja.CierreDiaDto;
import com.entysoftware.aplication.repository.BaseInicialRepository;
import com.entysoftware.aplication.repository.CostosRepository;
import com.entysoftware.aplication.repository.EncabezadoPedidosRepository;
import com.entysoftware.aplication.service.interfaces.CierreDiaInterface;

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

        return new VentasDelDia(ventasTotalesEfectivo, ventasTotalesTrasferencia, baseInicial);
    }

    private CostosDelDia calcularCostosDelDia(Integer idEstablecimiento, LocalDate fechaHoy){
        Integer costosTotalesEfectivo = costosRepository.sumarGastosEfectivoDelDia(idEstablecimiento, fechaHoy);
        Integer costosTotalesTrasferencia = costosRepository.sumarGastosTransferenciaDelDia(idEstablecimiento, fechaHoy);


        return new CostosDelDia(costosTotalesEfectivo, costosTotalesTrasferencia);
    }

    private CierreDiaDto construirCierreDiaDto(Integer idEstablecimiento, LocalDate fechaHoy, VentasDelDia ventas, CostosDelDia costos){
        Integer ventasEnTrasferenciaNeta = ventas.ventasTotalesTrasferencia() - costos.costosTotalesTrasferencia();
        // La base inicial está físicamente en la caja, así que hace parte del efectivo esperado al cuadrar
        Integer ventasEnEfectivoNeta = ventas.baseInicial() + ventas.ventasTotalesEfectivo() - costos.costosTotalesEfectivo();

        return new CierreDiaDto(encabezadoPedidosRepository.contarPedidosDelDia(idEstablecimiento, fechaHoy),
                                 ventas.ventasTotalesTrasferencia,
                                 ventas.ventasTotalesEfectivo,
                                 ventas.baseInicial,
                                 costos.costosTotalesEfectivo,
                                 costos.costosTotalesTrasferencia,
                                 ventasEnEfectivoNeta,
                                 ventasEnTrasferenciaNeta,
                                 encabezadoPedidosRepository.sumarIngresosTarjetaDelDia(idEstablecimiento, fechaHoy),
                                 encabezadoPedidosRepository.sumarIngresoDomicilio(idEstablecimiento, fechaHoy)
                                );
    }

    private record VentasDelDia(Integer ventasTotalesEfectivo, Integer ventasTotalesTrasferencia, Integer baseInicial) {}

    private record CostosDelDia(Integer costosTotalesEfectivo, Integer costosTotalesTrasferencia) {}
}
