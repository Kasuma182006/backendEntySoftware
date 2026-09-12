package com.entysoftware.aplication.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entysoftware.aplication.model.models.PagoTransferencia;

public interface PagosTransferenciaRepository extends JpaRepository<PagoTransferencia, Integer> {

    Page<PagoTransferencia> findByIdEstablecimiento(Integer idEstablecimiento, Pageable pageable);

    @Query("SELECT t FROM PagoTransferencia t " +
           "WHERE t.idEstablecimiento = :idEstablecimiento " +
           "AND t.fecha >= :desde AND t.fecha < :hasta")
    Page<PagoTransferencia> buscarPorEstablecimientoYRangoFechas(
        @Param("idEstablecimiento") Integer idEstablecimiento,
        @Param("desde") LocalDateTime desde,
        @Param("hasta") LocalDateTime hasta,
        Pageable pageable
    );
}
