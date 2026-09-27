package com.entysoftware.aplication.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entysoftware.aplication.model.models.EncabezadoPedido;

public interface EncabezadoPedidosRepository  extends JpaRepository<EncabezadoPedido,Integer>{

    @Query("SELECT e FROM EncabezadoPedido e " +
       "WHERE e.idMesa.idEstablecimiento = :idEstablecimiento " +
       "AND e.fechaPedido >= :desde AND e.fechaPedido < :hasta")
    Page<EncabezadoPedido> buscarPorEstablecimientoYRangoFechas(
        @Param("idEstablecimiento") Integer idEstablecimiento,
        @Param("desde") LocalDate desde,
        @Param("hasta") LocalDate hasta,
        Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(e.precioTotal), 0) FROM EncabezadoPedido e " +
       "WHERE e.idMesa.idEstablecimiento = :idEstablecimiento " +
       "AND e.fechaPedido >= :desde AND e.fechaPedido < :hasta")
    Integer sumarPrecioTotalRangoFechas(
        @Param("idEstablecimiento") Integer idEstablecimiento,
        @Param("desde") LocalDate desde,
        @Param("hasta") LocalDate hasta
    );

    @Query("SELECT COUNT(e) FROM EncabezadoPedido e " +
       "WHERE e.idMesa.idEstablecimiento = :idEstablecimiento " +
       "AND e.fechaPedido >= :desde AND e.fechaPedido < :hasta")
    Long contarPedidosRangoFechas(
        @Param("idEstablecimiento") Integer idEstablecimiento,
        @Param("desde") LocalDate desde,
        @Param("hasta") LocalDate hasta
    );

    @Query("SELECT COALESCE(SUM(e.precioTotal), 0) FROM EncabezadoPedido e " +
       "WHERE e.idMesa.idEstablecimiento = :idEstablecimiento " +
       "AND e.fechaPedido >= :desde AND e.fechaPedido < :hasta AND e.tipoPago = :tipoPago")
    Integer sumarPrecioTotalPorTipoPagoRangoFechas(
        @Param("idEstablecimiento") Integer idEstablecimiento,
        @Param("desde") LocalDate desde,
        @Param("hasta") LocalDate hasta,
        @Param("tipoPago") String tipoPago
    );

    @Query("SELECT u FROM EncabezadoPedido u " +
       "JOIN FETCH u.idMesa p " +
       "JOIN FETCH u.detalles d " +
       "JOIN FETCH d.idInventario " +
       "WHERE p.idEstablecimiento = :idEstablecimiento AND u.fechaPedido = :fecha")
    List<EncabezadoPedido> buscarPedidosDeHoyConDetalles(
        @Param("idEstablecimiento") Integer idEstablecimiento,
        @Param("fecha") LocalDate fecha
    );

    @Query("SELECT COUNT(u) FROM EncabezadoPedido u " +
       "WHERE u.idMesa.idEstablecimiento = :idEstablecimiento AND u.fechaPedido = :fecha")
    Integer contarPedidosDelDia(
        @Param("idEstablecimiento") Integer idEstablecimiento,
        @Param("fecha") LocalDate fecha
    );

    @Query("SELECT COALESCE(SUM(u.precioTotal), 0)  - COALESCE(SUM(u.valorDomicilio),0) FROM EncabezadoPedido u " +
       "WHERE u.idMesa.idEstablecimiento = :idEstablecimiento " +
       "AND u.fechaPedido = :fecha AND u.tipoPago = 'TRANSFERENCIA'")
    Integer sumarIngresosTransferenciaDelDia(
        @Param("idEstablecimiento") Integer idEstablecimiento,
        @Param("fecha") LocalDate fecha
    );

    @Query("SELECT COALESCE(SUM(u.valorDomicilio), 0) FROM EncabezadoPedido u " +
       "WHERE u.idMesa.idEstablecimiento = :idEstablecimiento " +
       "AND u.fechaPedido = :fecha")
    Integer sumarIngresoDomicilio(
        @Param("idEstablecimiento") Integer idEstablecimiento,
        @Param("fecha") LocalDate fecha
    );


    @Query("SELECT COALESCE(SUM(u.precioTotal), 0) - COALESCE(SUM(u.valorDomicilio),0) FROM EncabezadoPedido u " +
       "WHERE u.idMesa.idEstablecimiento = :idEstablecimiento " +
       "AND u.fechaPedido = :fecha AND u.tipoPago = 'EFECTIVO'")
    Integer sumarIngresosEfectivoDelDia(
        @Param("idEstablecimiento") Integer idEstablecimiento,
        @Param("fecha") LocalDate fecha
    );
}
