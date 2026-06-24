package com.entysoftware.aplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.entysoftware.aplication.model.Propietarios;


public interface PropietariosRepository extends JpaRepository<Propietarios,String> {
    
    @Query("SELECT u From Propietarios u Where u.idPropietario = :id_propietario AND u.password = :password")
    Propietarios loginPropietario(@Param("id_propietario") String id_propietario, @Param("password") String password);
}
