package com.entysoftware.aplication.service.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.error.ObjetosNoEncontradosExepcion;
import com.entysoftware.aplication.mapper.MapperAdicionesDto;
import com.entysoftware.aplication.model.dto.AdicionesDto;
import com.entysoftware.aplication.model.models.Adicion;
import com.entysoftware.aplication.model.models.Establecimiento;
import com.entysoftware.aplication.repository.AdicionesRepository;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.service.interfaces.AdicionesInterface;

import jakarta.transaction.Transactional;


@Service
public class AdicionesServiceImp implements AdicionesInterface {

    private final AdicionesRepository adicionesRepository;

    private final MapperAdicionesDto mapperAdicionesDto;

    private final EstablecimientoRepository establecimientoRepository;

    public AdicionesServiceImp(AdicionesRepository adicionesRepository, MapperAdicionesDto mapperAdicionesDto,
            EstablecimientoRepository establecimientoRepository) {
        this.adicionesRepository = adicionesRepository;
        this.mapperAdicionesDto = mapperAdicionesDto;
        this.establecimientoRepository = establecimientoRepository;
    }

    public ResponseEntity<List<AdicionesDto>> listarAdiciones(Integer idEstablecimiento) {
        List<Adicion> listaAdiciones = adicionesRepository.findByIdEstablecimiento(idEstablecimiento);
        List<AdicionesDto> listaAdicionesDto = listaAdiciones.stream()
                                                               .map(mapperAdicionesDto::adicionesToDto)
                                                               .toList();

        return ResponseEntity.ok(listaAdicionesDto);
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<AdicionesDto> crearAdicion(AdicionesDto adicionDto) {
        Establecimiento establecimiento = establecimientoRepository.findById(adicionDto.getIdEstablecimiento())
            .orElseThrow(() -> new ObjetosNoEncontradosExepcion("El establecimiento con ID " + adicionDto.getIdEstablecimiento() + " no existe"));

        Adicion adicion = mapperAdicionesDto.dtoToAdiciones(adicionDto, establecimiento);
        Adicion adicionGuardada = adicionesRepository.save(adicion);

        return ResponseEntity.ok(mapperAdicionesDto.adicionesToDto(adicionGuardada));
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<String> editarAdicion(AdicionesDto adicionDto) {
        Adicion adicionExistente = adicionesRepository.findById(adicionDto.getIdAdicion())
            .orElseThrow(() -> new ObjetosNoEncontradosExepcion("La adición con ID " + adicionDto.getIdAdicion() + " no existe"));

        if (adicionDto.getNombre() != null) {
            adicionExistente.setNombre(adicionDto.getNombre());
        }
        if (adicionDto.getPrecioAdicion() != null) {
            adicionExistente.setPrecioAdicion(adicionDto.getPrecioAdicion());
        }

        adicionesRepository.save(adicionExistente);

        return ResponseEntity.ok("Adición actualizada");
    }

/* Recuerda que debes crear en la DB el campo estado de la adición para no tener que eliminarla de forma permantente de la DB
    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<String> eliminarAdicion(Integer idAdicion) {
        if (!adicionesRepository.existsById(idAdicion)) {
            throw new ObjetosNoEncontradosExepcion("La adición con ID " + idAdicion + " no existe");
        }

        adicionesRepository.deleteById(idAdicion);

        return ResponseEntity.ok("Adición eliminada");
    }*/
}
