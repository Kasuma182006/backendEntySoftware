package com.entysoftware.aplication.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entysoftware.aplication.model.Costos;

public interface CostosRepository extends JpaRepository<Costos, Integer> {

    @Query("SELECT COALESCE(SUM(c.valorCosto), 0) FROM Costos c " +
       "WHERE c.idEstablecimiento = :idEstablecimiento " +
       "AND c.fecha = :fecha AND c.tipoPago = 'TRANSFERENCIA'")
    Integer sumarGastosTransferenciaDelDia(
        @Param("idEstablecimiento") Integer idEstablecimiento,
        @Param("fecha") LocalDate fecha
    );

    @Query("SELECT COALESCE(SUM(c.valorCosto), 0) FROM Costos c " +
       "WHERE c.idEstablecimiento = :idEstablecimiento " +
       "AND c.fecha = :fecha AND c.tipoPago = 'EFECTIVO'")
    Integer sumarGastosEfectivoDelDia(
        @Param("idEstablecimiento") Integer idEstablecimiento,
        @Param("fecha") LocalDate fecha
    );
}
