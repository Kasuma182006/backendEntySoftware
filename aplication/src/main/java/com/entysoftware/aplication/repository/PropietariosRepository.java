package com.entysoftware.aplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entysoftware.aplication.model.models.Propietario;


public interface PropietariosRepository extends JpaRepository<Propietario,String> {
    
    @Query("SELECT u From Propietario u Where u.idPropietario = :id_propietario AND u.password = :password")
    Propietario loginPropietario(@Param("id_propietario") String id_propietario, @Param("password") String password);
}
