package com.entysoftware.aplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entysoftware.aplication.model.models.Empleado;


public interface EmpleadosRepository extends JpaRepository<Empleado,Integer> {
    @Query("SELECT u FROM Empleado u WHERE u.numeroIdentificacion = :identificacion")
    List<Empleado> establecimientosEmpleados(@Param("identificacion") String idEstablecimiento);


    @Query("SELECT u FROM Empleado u WHERE u.idEstablecimiento = :idEstablecimiento AND  u.numeroIdentificacion = :identificacion AND u.password = :password")
    Empleado loginEmpleado(@Param("idEstablecimiento")Integer idEstablecimiento,
                                    @Param("identificacion") String identificacion,
                                    @Param("password") String password );
    
}
