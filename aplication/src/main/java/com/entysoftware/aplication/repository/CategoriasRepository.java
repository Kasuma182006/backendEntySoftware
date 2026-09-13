package com.entysoftware.aplication.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entysoftware.aplication.model.models.Categoria;

public interface CategoriasRepository extends JpaRepository<Categoria,Integer>{

    @Query("SELECT c FROM Categoria c WHERE c.idEstablecimiento = :idEstablecimiento")
    List<Categoria> findByFk_id_establecimiento(@Param("idEstablecimiento") int fk_id_establecimiento);

    Page<Categoria> findByIdEstablecimiento(Integer idEstablecimiento, Pageable pageable);

    @Query("SELECT c FROM Categoria c " +
           "WHERE c.idEstablecimiento = :idEstablecimiento " +
           "AND LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Categoria> buscarPorEstablecimientoYNombre(
        @Param("idEstablecimiento") Integer idEstablecimiento,
        @Param("nombre") String nombre,
        Pageable pageable
    );

    boolean existsByIdEstablecimientoAndNombreIgnoreCase(Integer idEstablecimiento, String nombre);
}
