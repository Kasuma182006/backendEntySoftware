package com.entysoftware.aplication.service.service_Implement;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.customExceptions.ObjetosNoEncontradosExepcion;
import com.entysoftware.aplication.mapper.MapperMesasDto;
import com.entysoftware.aplication.model.Establecimiento;
import com.entysoftware.aplication.model.Mesas;
import com.entysoftware.aplication.model.dto.MesasDto;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.repository.MesasRepository;
import com.entysoftware.aplication.service.MesasInterface;


import jakarta.transaction.Transactional;

@Service
public class MesasServiceImp implements MesasInterface {


    private final MesasRepository mesasRepository;

    private final MapperMesasDto mapperMesasDto;

    private final EstablecimientoRepository establecimientoRepository;

    public MesasServiceImp(MesasRepository mesasRepository, MapperMesasDto mapperMesasDto, EstablecimientoRepository establecimientoRepository){
        this.mapperMesasDto = mapperMesasDto;
        this.mesasRepository = mesasRepository;
        this.establecimientoRepository = establecimientoRepository;
    }

    public ResponseEntity<List<MesasDto>> listarMesas(Integer idEstablecimiento){
        List<Mesas> listaMesas = mesasRepository.findByFK_id_establecimiento(idEstablecimiento);
        List<MesasDto> listaMesasDto = listaMesas.stream()
                                                  .map(mapperMesasDto::MesasToDto)
                                                  .toList();

        return ResponseEntity.ok(listaMesasDto);
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<MesasDto> crearMesa(MesasDto mesaDto){
        Establecimiento establecimiento = establecimientoRepository.findById(mesaDto.getIdEstablecimiento())
            .orElseThrow(() -> new ObjetosNoEncontradosExepcion("El establecimiento con ID " + mesaDto.getIdEstablecimiento() + " no existe"));

        Mesas mesa = mapperMesasDto.dtoToMesas(mesaDto, establecimiento);
        Mesas mesaGuardada = mesasRepository.save(mesa);

        return ResponseEntity.ok(mapperMesasDto.MesasToDto(mesaGuardada));
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<String> editarMesa(MesasDto mesaDto){
        Mesas mesaExistente = mesasRepository.findById(mesaDto.getIdMesa())
            .orElseThrow(() -> new ObjetosNoEncontradosExepcion("La mesa con ID " + mesaDto.getIdMesa() + " no existe"));

        if (mesaDto.getNombreMesa() != null) {
            mesaExistente.setNombreMesa(mesaDto.getNombreMesa());
        }
        if (mesaDto.getOcupada() != null) {
            mesaExistente.setEstadoMesa(mesaDto.getOcupada());
        }

        mesasRepository.save(mesaExistente);

        return ResponseEntity.ok("Mesa actualizada");
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<String> eliminarMesa(Integer idMesa){
        if (!mesasRepository.existsById(idMesa)) {
            throw new ObjetosNoEncontradosExepcion("La mesa con ID " + idMesa + " no existe");
        }

        mesasRepository.deleteById(idMesa);

        return ResponseEntity.ok("Mesa eliminada");
    }

}
