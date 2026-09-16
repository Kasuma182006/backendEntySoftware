package com.entysoftware.aplication.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entysoftware.aplication.model.models.CierreCaja;

public interface CierreCajaRepository extends JpaRepository<CierreCaja, Integer> {

    @Query("SELECT COUNT(c) > 0 FROM CierreCaja c " +
           "WHERE c.idEstablecimiento = :idEstablecimiento " +
           "AND FUNCTION('DATE', c.fecha) = :fecha")
    boolean existsPorEstablecimientoYFecha(
        @Param("idEstablecimiento") Integer idEstablecimiento,
        @Param("fecha") LocalDate fecha
    );
}
