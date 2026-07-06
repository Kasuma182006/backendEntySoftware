package com.entysoftware.aplication.service.service_Implement;


import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.model.dto.CierreDiaDto;
import com.entysoftware.aplication.repository.BaseInicialRepository;
import com.entysoftware.aplication.repository.CostosRepository;
import com.entysoftware.aplication.repository.EncabezadoPedidosRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CierreDiaServiceImp {

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
        //ventas y base inicial
        Integer ventasTotalesEfectivo = encabezadoPedidosRepository.sumarIngresosEfectivoDelDia(idEstablecimiento, fechaHoy);
        Integer ventasTotalesTrasferencia = encabezadoPedidosRepository.sumarIngresosTransferenciaDelDia(idEstablecimiento, fechaHoy);
        Integer baseInicial = baseInicialRepository.buscarValorBaseInicialHoy(idEstablecimiento, fechaHoy);
        Integer ventaTotalDelDiaSinCostos = ventasTotalesEfectivo + ventasTotalesTrasferencia + baseInicial;
       //costos 
        Integer costosTotalesEfectivo = costosRepository.sumarGastosEfectivoDelDia(idEstablecimiento, fechaHoy);
        Integer costosTotalesTrasferencia = costosRepository.sumarGastosTransferenciaDelDia(idEstablecimiento, fechaHoy);
        Integer costoTotalDelDia = costosTotalesEfectivo + costosTotalesTrasferencia;


        Integer ventaTotalNetaDelDia = ventaTotalDelDiaSinCostos - costoTotalDelDia;
        Integer ventasEnTrasferenciaNeta = ventasTotalesTrasferencia - costosTotalesTrasferencia;
        Integer ventasEnEfectivoNeta = ventasTotalesEfectivo - costosTotalesEfectivo;

        CierreDiaDto cierreDelDia = new CierreDiaDto(encabezadoPedidosRepository.contarPedidosDelDia(idEstablecimiento, fechaHoy), ventasTotalesTrasferencia, ventasTotalesEfectivo, baseInicial, ventaTotalDelDiaSinCostos, costosTotalesEfectivo, costosTotalesTrasferencia, costoTotalDelDia, ventasEnEfectivoNeta, ventasEnTrasferenciaNeta, ventaTotalNetaDelDia);

        
        return ResponseEntity.ok(cierreDelDia);
    }
}
