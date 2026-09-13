package com.entysoftware.aplication.service.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.error.DatosDuplicados;
import com.entysoftware.aplication.error.ObjetosNoEncontradosExepcion;
import com.entysoftware.aplication.mapper.MapperCategoriasDto;
import com.entysoftware.aplication.model.dto.CategoriasDto;
import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.model.models.Categoria;
import com.entysoftware.aplication.model.models.Establecimiento;
import com.entysoftware.aplication.repository.CategoriasRepository;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.service.interfaces.CategoriasInterface;
import com.entysoftware.aplication.utils.ConsultaPaginadaUtils;

import jakarta.transaction.Transactional;

@Service
public class CategoriasServiceImp implements CategoriasInterface {

    private static final String CAMPO_ORDEN = "nombre";

    private final CategoriasRepository categoriasRepository;

    private final MapperCategoriasDto mapperCategoriasDto;

    private final EstablecimientoRepository establecimientoRepository;

    public CategoriasServiceImp(CategoriasRepository categoriasRepository, MapperCategoriasDto mapperCategoriasDto,
            EstablecimientoRepository establecimientoRepository) {
        this.categoriasRepository = categoriasRepository;
        this.mapperCategoriasDto = mapperCategoriasDto;
        this.establecimientoRepository = establecimientoRepository;
    }

    /**
     * Lista paginada de categorías de un establecimiento, ordenadas alfabéticamente por nombre.
     * Sin nombre se listan todas; con nombre se filtran las que lo contengan, sin distinguir mayúsculas.
     */
    public ResponseEntity<PaginaDto<CategoriasDto>> listarCategorias(Integer idEstablecimiento, String nombre, int pagina, int tamano) {
        Pageable pageable = ConsultaPaginadaUtils.construirPageableOrdenadoAsc(pagina, tamano, CAMPO_ORDEN);

        Page<Categoria> paginaCategorias = (nombre == null || nombre.isBlank())
            ? categoriasRepository.findByIdEstablecimiento(idEstablecimiento, pageable)
            : categoriasRepository.buscarPorEstablecimientoYNombre(idEstablecimiento, nombre.trim(), pageable);

        return ResponseEntity.ok(PaginaDto.desdePage(paginaCategorias.map(mapperCategoriasDto::categoriaToDto)));
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<CategoriasDto> crearCategoria(CategoriasDto categoriaDto) {
        Establecimiento establecimiento = establecimientoRepository.findById(categoriaDto.getIdEstablecimiento())
            .orElseThrow(() -> new ObjetosNoEncontradosExepcion("El establecimiento con ID " + categoriaDto.getIdEstablecimiento() + " no existe"));

        validarNombreDisponible(establecimiento.getIdEstablecimiento(), categoriaDto.getNombre());

        Categoria categoria = mapperCategoriasDto.dtoToCategoria(categoriaDto, establecimiento);
        Categoria categoriaGuardada = categoriasRepository.save(categoria);

        return ResponseEntity.ok(mapperCategoriasDto.categoriaToDto(categoriaGuardada));
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<String> editarCategoria(CategoriasDto categoriaDto) {
        Categoria categoriaExistente = categoriasRepository.findById(categoriaDto.getIdCategoria())
            .orElseThrow(() -> new ObjetosNoEncontradosExepcion("La categoría con ID " + categoriaDto.getIdCategoria() + " no existe"));

        if (categoriaDto.getNombre() != null && !categoriaDto.getNombre().equalsIgnoreCase(categoriaExistente.getNombre())) {
            validarNombreDisponible(categoriaExistente.getIdEstablecimiento(), categoriaDto.getNombre());
            categoriaExistente.setNombre(categoriaDto.getNombre());
        }

        categoriasRepository.save(categoriaExistente);

        return ResponseEntity.ok("Categoría actualizada");
    }

    /**
     * Impide que un mismo establecimiento tenga dos categorías con el mismo nombre,
     * sin distinguir mayúsculas ni minúsculas.
     */
    private void validarNombreDisponible(Integer idEstablecimiento, String nombre) {
        if (nombre != null && categoriasRepository.existsByIdEstablecimientoAndNombreIgnoreCase(idEstablecimiento, nombre.trim())) {
            throw new DatosDuplicados("La categoría '" + nombre.trim() + "' ya existe en el establecimiento con ID " + idEstablecimiento);
        }
    }
}
