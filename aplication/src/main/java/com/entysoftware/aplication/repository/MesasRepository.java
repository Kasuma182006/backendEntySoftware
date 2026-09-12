package com.entysoftware.aplication.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entysoftware.aplication.model.models.Mesa;

import java.util.List;



public interface MesasRepository extends JpaRepository<Mesa,Integer>{
    @Query("SELECT m FROM Mesa m WHERE m.idEstablecimiento = :idEstablecimiento AND m.eliminada = false ")
    List<Mesa> findByFK_id_establecimiento(@Param("idEstablecimiento") int idEstablecimiento);
}
