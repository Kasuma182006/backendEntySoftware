package com.entysoftware.aplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entysoftware.aplication.model.models.Categoria;

import java.util.List;




public interface CategoriasRepository extends JpaRepository<Categoria,Integer>{
    @Query("SELECT c FROM Categoria c WHERE c.idEstablecimiento = :idEstablecimiento")
    List<Categoria> findByFk_id_establecimiento(@Param("idEstablecimiento") int fk_id_establecimiento);

   
}
