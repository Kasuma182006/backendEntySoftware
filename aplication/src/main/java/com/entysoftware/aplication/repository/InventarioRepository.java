package com.entysoftware.aplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.entysoftware.aplication.model.Inventario;
import java.util.List;



public interface InventarioRepository extends JpaRepository<Inventario,Integer> {
   @Query("SELECT i FROM Inventario i WHERE i.categoria = :categoriaId")
    List<Inventario> findByFK_categoria(@Param("categoriaId") int fK_categoria);

    @Query("SELECT i FROM Inventario i JOIN Categorias c ON i.categoria = c.id WHERE c.idEstablecimiento = :idEstablecimiento")
    List<Inventario> findByFK_id_establecimiento(@Param("idEstablecimiento") int idEstablecimiento);
}
