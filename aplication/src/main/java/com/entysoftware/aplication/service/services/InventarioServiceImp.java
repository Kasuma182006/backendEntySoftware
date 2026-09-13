package com.entysoftware.aplication.service.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.error.DatosDuplicados;
import com.entysoftware.aplication.error.ObjetosNoEncontradosExepcion;
import com.entysoftware.aplication.mapper.MapperInventarioDto;
import com.entysoftware.aplication.model.dto.InventarioDto;
import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.model.models.Categoria;
import com.entysoftware.aplication.model.models.Inventario;
import com.entysoftware.aplication.repository.CategoriasRepository;
import com.entysoftware.aplication.repository.InventarioRepository;
import com.entysoftware.aplication.service.interfaces.InventarioInterface;
import com.entysoftware.aplication.utils.ConsultaPaginadaUtils;

import jakarta.transaction.Transactional;

@Service
public class InventarioServiceImp implements InventarioInterface {

    private static final String CAMPO_ORDEN = "nombre";

    private final InventarioRepository inventarioRepository;

    private final MapperInventarioDto mapperInventarioDto;

    private final CategoriasRepository categoriasRepository;

    public InventarioServiceImp(InventarioRepository inventarioRepository, MapperInventarioDto mapperInventarioDto,
            CategoriasRepository categoriasRepository) {
        this.inventarioRepository = inventarioRepository;
        this.mapperInventarioDto = mapperInventarioDto;
        this.categoriasRepository = categoriasRepository;
    }

    /**
     * Lista paginada de los productos de un establecimiento, ordenados alfabéticamente por nombre.
     * El establecimiento se resuelve a través de la categoría de cada producto.
     */
    public ResponseEntity<PaginaDto<InventarioDto>> listarProductos(Integer idEstablecimiento, int pagina, int tamano) {
        Pageable pageable = ConsultaPaginadaUtils.construirPageableOrdenadoAsc(pagina, tamano, CAMPO_ORDEN);

        Page<Inventario> paginaProductos = inventarioRepository.buscarPorEstablecimiento(idEstablecimiento, pageable);

        return ResponseEntity.ok(PaginaDto.desdePage(paginaProductos.map(mapperInventarioDto::InventarioToDto)));
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<InventarioDto> crearProducto(InventarioDto productoDto) {
        Categoria categoria = buscarCategoria(productoDto.getCategoria());

        validarNombreDisponible(categoria.getId(), productoDto.getNombre());

        Inventario producto = mapperInventarioDto.dtoToInventario(productoDto, categoria);
        Inventario productoGuardado = inventarioRepository.save(producto);

        return ResponseEntity.ok(mapperInventarioDto.InventarioToDto(productoGuardado));
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<String> editarProducto(InventarioDto productoDto) {
        Inventario productoExistente = inventarioRepository.findById(productoDto.getIdInventario())
            .orElseThrow(() -> new ObjetosNoEncontradosExepcion("El producto con ID " + productoDto.getIdInventario() + " no existe"));

        Integer categoriaFinal = productoExistente.getCategoria();
        if (productoDto.getCategoria() != null && !productoDto.getCategoria().equals(productoExistente.getCategoria())) {
            categoriaFinal = buscarCategoria(productoDto.getCategoria()).getId();
        }

        String nombreFinal = productoDto.getNombre() != null ? productoDto.getNombre() : productoExistente.getNombre();

        boolean cambiaCategoria = !categoriaFinal.equals(productoExistente.getCategoria());
        boolean cambiaNombre = !nombreFinal.equalsIgnoreCase(productoExistente.getNombre());
        if (cambiaCategoria || cambiaNombre) {
            validarNombreDisponible(categoriaFinal, nombreFinal);
        }

        productoExistente.setCategoria(categoriaFinal);
        productoExistente.setNombre(nombreFinal);
        if (productoDto.getDescripcion() != null) {
            productoExistente.setDescripcion(productoDto.getDescripcion());
        }
        if (productoDto.getPrecio() != null) {
            productoExistente.setPrecio(productoDto.getPrecio());
        }

        inventarioRepository.save(productoExistente);

        return ResponseEntity.ok("Producto actualizado");
    }

    @SuppressWarnings("null")
    private Categoria buscarCategoria(Integer idCategoria) {
        if (idCategoria == null) {
            throw new ObjetosNoEncontradosExepcion("Debe indicar la categoría del producto");
        }

        return categoriasRepository.findById(idCategoria)
            .orElseThrow(() -> new ObjetosNoEncontradosExepcion("La categoría con ID " + idCategoria + " no existe"));
    }

    /**
     * Impide que una misma categoría tenga dos productos con el mismo nombre, sin distinguir
     * mayúsculas ni minúsculas. Como cada categoría pertenece a un solo establecimiento,
     * la validación queda acotada también a ese establecimiento.
     */
    private void validarNombreDisponible(Integer idCategoria, String nombre) {
        if (nombre != null && inventarioRepository.existsByCategoriaAndNombreIgnoreCase(idCategoria, nombre.trim())) {
            throw new DatosDuplicados("El producto '" + nombre.trim() + "' ya existe en la categoría con ID " + idCategoria);
        }
    }
}
