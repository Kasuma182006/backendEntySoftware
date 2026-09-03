package com.entysoftware.aplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entysoftware.aplication.model.models.Adiciones;

public interface AdicionesRepository extends JpaRepository<Adiciones, Integer> {

    List<Adiciones> findByIdEstablecimiento(Integer idEstablecimiento);
}
