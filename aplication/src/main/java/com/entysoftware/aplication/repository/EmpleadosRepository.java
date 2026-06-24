package com.entysoftware.aplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.entysoftware.aplication.model.Empleados;


public interface EmpleadosRepository extends JpaRepository<Empleados,Integer> {
    @Query("SELECT u FROM Empleados u WHERE u.numeroIdentificacion = :identificacion")
    List<Empleados> establecimientosEmpleados(@Param("identificacion") String idEstablecimiento);


    @Query("SELECT u FROM Empleados u WHERE u.idEstablecimiento = :idEstablecimiento AND  u.numeroIdentificacion = :identificacion AND u.password = :password")
    Empleados loginEmpleado(@Param("idEstablecimiento")Integer idEstablecimiento,
                                    @Param("identificacion") String identificacion,
                                    @Param("password") String password );
    
}
