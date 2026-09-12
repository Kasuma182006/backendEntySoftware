package com.entysoftware.aplication.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entysoftware.aplication.model.models.PagoVoucher;

public interface PagosVoucherRepository extends JpaRepository<PagoVoucher, Integer> {

    Page<PagoVoucher> findByIdEstablecimiento(Integer idEstablecimiento, Pageable pageable);

    @Query("SELECT v FROM PagoVoucher v " +
           "WHERE v.idEstablecimiento = :idEstablecimiento " +
           "AND v.fecha >= :desde AND v.fecha < :hasta")
    Page<PagoVoucher> buscarPorEstablecimientoYRangoFechas(
        @Param("idEstablecimiento") Integer idEstablecimiento,
        @Param("desde") LocalDateTime desde,
        @Param("hasta") LocalDateTime hasta,
        Pageable pageable
    );
}
