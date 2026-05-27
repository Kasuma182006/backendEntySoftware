package com.entysoftware.aplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entysoftware.aplication.model.Empleados;

@Repository
public interface EmpleadosRepository extends JpaRepository<Empleados,Integer> {
    @Query("SELECT u FROM Empleados u WHERE u.numero_identificacion = :identificacion")
    List<Empleados> establecimientosEmpleados(@Param("identificacion") String idEstablecimiento);


    @Query("SELECT u FROM Empleados u WHERE u.FK_id_establecimiento = :idEstablecimiento AND  u.numero_identificacion = :identificacion AND u.password = :password")
    Empleados loginEmpleado(@Param("idEstablecimiento")Integer idEstablecimiento,
                                    @Param("identificacion") String identificacion,
                                    @Param("password") String password );
    
}
