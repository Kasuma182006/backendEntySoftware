package com.entysoftware.aplication.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entysoftware.aplication.model.models.EncabezadoPedido;

public interface EncabezadoPedidosRepository  extends JpaRepository<EncabezadoPedido,Integer>{
    
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
