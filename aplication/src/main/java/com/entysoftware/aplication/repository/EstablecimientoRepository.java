package com.entysoftware.aplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entysoftware.aplication.model.models.Establecimiento;

public interface EstablecimientoRepository extends JpaRepository <Establecimiento,Integer> {

    @Query("Select u from Establecimiento u Where idPropietario = :identificacion")
    List<Establecimiento> buscarEstablecimiento(@Param("identificacion")String identificacion);

    @Modifying
    @Query("UPDATE Establecimiento e SET e.estadoEstablecimiento = :estado WHERE e.idEstablecimiento = :idEstablecimiento")
    int actualizarEstadoEstablecimiento(@Param("estado") String estado,
                                        @Param("idEstablecimiento") Integer idEstablecimiento);

}
