package com.entysoftware.aplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entysoftware.aplication.model.Propietarios;

@Repository
public interface PropietariosRepository extends JpaRepository<Propietarios,String> {
    
    @Query("SELECT u From Propietarios u Where u.id_propietario = :id_propietario AND u.password = :password")
    Propietarios loginPropietario(@Param("id_propietario") String id_propietario, @Param("password") String password);
}
