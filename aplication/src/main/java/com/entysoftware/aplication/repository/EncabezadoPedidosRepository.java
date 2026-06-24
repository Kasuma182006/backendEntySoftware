package com.entysoftware.aplication.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entysoftware.aplication.model.EncabezadoPedidos;

public interface EncabezadoPedidosRepository  extends JpaRepository<EncabezadoPedidos,Integer>{
    
    @Query("SELECT u FROM EncabezadoPedidos u " +
       "JOIN FETCH u.idMesa p " +
       "JOIN FETCH u.detalles d " +
       "JOIN FETCH d.idInventario " +
       "WHERE p.idEstablecimiento = :idEstablecimiento AND u.fechaPedido = :fecha")
    List<EncabezadoPedidos> buscarPedidosDeHoyConDetalles(
        @Param("idEstablecimiento") Integer idEstablecimiento, 
        @Param("fecha") LocalDate fecha
    );
}
