package com.entysoftware.aplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entysoftware.aplication.model.models.Adicion;

public interface AdicionesRepository extends JpaRepository<Adicion, Integer> {

    List<Adicion> findByIdEstablecimiento(Integer idEstablecimiento);
}
