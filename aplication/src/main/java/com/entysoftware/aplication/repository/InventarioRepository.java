package com.entysoftware.aplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entysoftware.aplication.model.Inventario;
import java.util.List;


@Repository
public interface InventarioRepository extends JpaRepository<Inventario,Integer> {
   @Query("SELECT i FROM Inventario i WHERE i.FK_categoria = :categoriaId")
    List<Inventario> findByFK_categoria(@Param("categoriaId") int fK_categoria);
}
