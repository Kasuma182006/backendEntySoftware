package com.entysoftware.aplication.repository;


import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.entysoftware.aplication.model.BaseInicial;


public interface BaseInicialRepository extends JpaRepository<BaseInicial,Integer>{
 @Query("SELECT u FROM BaseInicial u WHERE u.idEstablecimiento = :idForanea AND u.fecha =:fechaBusqueda")
    BaseInicial validarExistenciaBaseInicial(
        @Param("idForanea") Integer idForanea, 
        @Param("fechaBusqueda") LocalDate fechaBusqueda
    );

    @Query("SELECT u.valorBaseInicial FROM BaseInicial u WHERE u.idEstablecimiento = :idForanea AND u.fecha =:fechaBusqueda")
    Integer buscarValorBaseInicialHoy (@Param("idForanea") Integer idForanea,@Param("fechaBusqueda") LocalDate fechaBusqueda);

}
