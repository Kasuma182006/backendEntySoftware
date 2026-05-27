package com.entysoftware.aplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entysoftware.aplication.model.Establecimiento;

public interface EstablecimientoRepository extends JpaRepository <Establecimiento,Integer> {

    @Query("Select u from Establecimiento u Where FK_id_propietario = :identificacion")
    List<Establecimiento> buscarEstablecimiento(@Param("identificacion")String identificacion);
    
}
