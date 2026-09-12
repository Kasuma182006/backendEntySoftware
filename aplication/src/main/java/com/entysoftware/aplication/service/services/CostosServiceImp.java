package com.entysoftware.aplication.service.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.error.ObjetosNoEncontradosExepcion;
import com.entysoftware.aplication.mapper.MapperCostosDto;
import com.entysoftware.aplication.model.dto.CostosDto;
import com.entysoftware.aplication.model.models.Costo;
import com.entysoftware.aplication.model.models.Establecimiento;
import com.entysoftware.aplication.repository.CostosRepository;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.service.interfaces.CostosInterface;

import jakarta.transaction.Transactional;

@Service
public class CostosServiceImp implements CostosInterface {

    private final CostosRepository costosRepository;

    private final MapperCostosDto mapperCostosDto;

    private final EstablecimientoRepository establecimientoRepository;

    public CostosServiceImp(CostosRepository costosRepository, MapperCostosDto mapperCostosDto,
            EstablecimientoRepository establecimientoRepository) {
        this.costosRepository = costosRepository;
        this.mapperCostosDto = mapperCostosDto;
        this.establecimientoRepository = establecimientoRepository;
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<String> crearCosto(CostosDto costoDto) {
        Establecimiento establecimiento = establecimientoRepository.findById(costoDto.getIdEstablecimiento())
            .orElseThrow(() -> new ObjetosNoEncontradosExepcion("El establecimiento con ID " + costoDto.getIdEstablecimiento() + " no existe"));

        Costo costo = mapperCostosDto.dtoToCostos(costoDto, establecimiento);
        Costo costoGuardado = costosRepository.save(costo);

        return ResponseEntity.ok("El costo se ha registrado correctamente por un valor de " + costoGuardado.getValorCosto());
    }
}
