package com.entysoftware.aplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entysoftware.aplication.model.BaseInicial;

@Repository
public interface BaseInicialRepository extends JpaRepository<BaseInicial,Integer>{
    
}
