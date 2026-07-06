package com.entysoftware.aplication.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entysoftware.aplication.model.CuerpoPedidos;
import com.entysoftware.aplication.model.dto.ProductoMasVendidoDto;

public interface CuerpoPedidosRepository extends JpaRepository<CuerpoPedidos,Integer> {

    @Query("SELECT new com.entysoftware.aplication.model.dto.ProductoMasVendidoDto(" +
       "cp.idInventario.nombre, SUM(cp.cantidad)) " +
       "FROM CuerpoPedidos cp " +
       "WHERE cp.pedido.idMesa.idEstablecimiento = :idEstablecimiento " +
       "AND cp.pedido.fechaPedido = :fecha " +
       "GROUP BY cp.idInventario.nombre " +
       "ORDER BY SUM(cp.cantidad) DESC")
    List<ProductoMasVendidoDto> buscarTop5ProductosMasVendidosDelDia(
        @Param("idEstablecimiento") Integer idEstablecimiento,
        @Param("fecha") LocalDate fecha,
        Pageable pageable
    );
}
