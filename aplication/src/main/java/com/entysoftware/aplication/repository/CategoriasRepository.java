package com.entysoftware.aplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.entysoftware.aplication.model.Categorias;
import java.util.List;



public interface CategoriasRepository extends JpaRepository<Categorias,Integer>{
    @Query("SELECT c FROM Categorias c WHERE c.Fk_id_establecimiento = :idEstablecimiento")
    List<Categorias> findByFk_id_establecimiento(@Param("idEstablecimiento") int fk_id_establecimiento);
}
