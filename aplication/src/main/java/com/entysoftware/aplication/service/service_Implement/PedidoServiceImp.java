package com.entysoftware.aplication.service.service_Implement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.mapper.MapperPedidosDto;
import com.entysoftware.aplication.model.CuerpoPedidos;
import com.entysoftware.aplication.model.CuerpoPedidosAdiciones;
import com.entysoftware.aplication.model.EncabezadoPedidos;
import com.entysoftware.aplication.model.Mesas;
import com.entysoftware.aplication.model.dto.pagosDTOs.FacturaPedidoDto;
import com.entysoftware.aplication.model.dto.pagosDTOs.PagarPedidoDto;
import com.entysoftware.aplication.model.dto.pedidosDTOs.AdicionPedidoDto;
import com.entysoftware.aplication.model.dto.pedidosDTOs.DetallesPedidoDto;
import com.entysoftware.aplication.model.dto.pedidosDTOs.PedidosDto;
import com.entysoftware.aplication.repository.AdicionesRepository;
import com.entysoftware.aplication.repository.EncabezadoPedidosRepository;
import com.entysoftware.aplication.repository.InventarioRepository;
import com.entysoftware.aplication.repository.MesasRepository;
import com.entysoftware.aplication.service.PedidosInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PedidoServiceImp implements PedidosInterface {

    private static final String TIPO_PAGO_EFECTIVO = "EFECTIVO";
    private static final String ESTADO_PEDIDO_EN_ESPERA = "EN ESPERA";
    private static final String ESTADO_PEDIDO_PAGADO = "PAGO";
    private static final String FORMATO_FECHA_HORA_FACTURA = "yyyy-MM-dd HH:mm";

    private final EncabezadoPedidosRepository encabezadoPedidosRepository;

    private final InventarioRepository inventarioRepository;

    private final MesasRepository mesasRepository;

    private final AdicionesRepository adicionesRepository;

    private final MapperPedidosDto mapperPedidosDto;

    public PedidoServiceImp(EncabezadoPedidosRepository encabezadoPedidosRepository,
                            InventarioRepository inventarioRepository,
                            MesasRepository mesasRepository,
                            AdicionesRepository adicionesRepository,
                            MapperPedidosDto mapperPedidosDto) {
        this.encabezadoPedidosRepository = encabezadoPedidosRepository;
        this.inventarioRepository = inventarioRepository;
        this.mesasRepository = mesasRepository;
        this.adicionesRepository = adicionesRepository;
        this.mapperPedidosDto = mapperPedidosDto;
    }


    // ----------------------------------------------------------------------
    // Crear
    // ----------------------------------------------------------------------

    @Transactional
    public ResponseEntity<Integer> crearPedido(PedidosDto pedido) {

        Mesas mesaProxy = mesasRepository.getReferenceById(pedido.getIdMesa());

        EncabezadoPedidos encabezadoPedido = new EncabezadoPedidos(
            null,
            mesaProxy,
            TIPO_PAGO_EFECTIVO,
            ESTADO_PEDIDO_EN_ESPERA,
            pedido.getValorDomicilio(),
            pedido.getPrecioTotal(),
            LocalDate.now(),
            pedido.getDescripcion(),
            new ArrayList<>()
        );

        List<CuerpoPedidos> listaCuerpoPedido = construirDetallesPedido(pedido.getPedido(), encabezadoPedido);
        encabezadoPedido.setDetalles(listaCuerpoPedido);

        log.debug("lista de productos pedidos: {}", listaCuerpoPedido);
        // Cascada: EncabezadoPedidos -> CuerpoPedidos -> CuerpoPedidosAdiciones.
        EncabezadoPedidos encabezadoGuardado = encabezadoPedidosRepository.save(encabezadoPedido);

        log.debug("se han creado los cuerpos del pedido y sus adiciones en la base de datos");

        return ResponseEntity.ok(encabezadoGuardado.getIdPedido());
    }


    // ----------------------------------------------------------------------
    // Consultar
    // ----------------------------------------------------------------------

    public ResponseEntity<List<PedidosDto>> pedidosHoy(Integer idEstablecimiento) {

        List<EncabezadoPedidos> pedidos =
            encabezadoPedidosRepository.buscarPedidosDeHoyConDetalles(idEstablecimiento, LocalDate.now());

        List<PedidosDto> listaPedidosDto = pedidos.stream()
                                                  .map(this::convertirAPedidoDto)
                                                  .toList();

        return ResponseEntity.ok(listaPedidosDto);
    }

    private PedidosDto convertirAPedidoDto(EncabezadoPedidos encabezadoPedido) {
        PedidosDto dto = mapperPedidosDto.pedidosToEntity(encabezadoPedido);
        dto.setPedido(encabezadoPedido.getDetalles().stream()
                                      .map(this::convertirADetalleDto)
                                      .toList());
        return dto;
    }

    private DetallesPedidoDto convertirADetalleDto(CuerpoPedidos cuerpo) {
        DetallesPedidoDto dto = new DetallesPedidoDto(
            cuerpo.getIdCuerpo(),
            cuerpo.getIdInventario().getIdInventario(),
            cuerpo.getIdInventario().getNombre(),
            cuerpo.getCantidad()
        );
        dto.setAdiciones(convertirAdicionesDto(cuerpo.getAdiciones()));
        return dto;
    }

    private List<AdicionPedidoDto> convertirAdicionesDto(List<CuerpoPedidosAdiciones> adiciones) {
        if (adiciones == null) {
            return new ArrayList<>();
        }
        return adiciones.stream()
                        .map(adicion -> new AdicionPedidoDto(
                            adicion.getAdicion().getIdAdicion(),
                            adicion.getAdicion().getNombre(),
                            adicion.getCantidadAdicion()
                        ))
                        .toList();
    }


    // ----------------------------------------------------------------------
    // Editar
    // ----------------------------------------------------------------------

    @Transactional
    public ResponseEntity<String> editarPedido(PedidosDto editarPedido) {

        EncabezadoPedidos pedidoExistente = encabezadoPedidosRepository.findById(editarPedido.getIdPedido())
            .orElseThrow(() -> new EntityNotFoundException(
                "El pedido con ID " + editarPedido.getIdPedido() + " no existe"));

        actualizarDatosEncabezado(pedidoExistente, editarPedido);

        if (tieneDetalles(editarPedido)) {
            reemplazarDetalles(pedidoExistente, editarPedido.getPedido());
        }

        encabezadoPedidosRepository.save(pedidoExistente);

        return ResponseEntity.ok("Pedido actualizado");
    }

    private void actualizarDatosEncabezado(EncabezadoPedidos pedido, PedidosDto cambios) {
        if (cambios.getIdMesa() != null) {
            pedido.setIdMesa(mesasRepository.getReferenceById(cambios.getIdMesa()));
        }
        if (cambios.getTipoPago() != null) {
            pedido.setTipoPago(cambios.getTipoPago().toUpperCase());
        }
        if (cambios.getEstadoPedido() != null) {
            pedido.setEstadoPedido(cambios.getEstadoPedido());
        }
        if (cambios.getValorDomicilio() != null) {
            pedido.setValorDomicilio(cambios.getValorDomicilio());
        }
        if (cambios.getPrecioTotal() != null) {
            pedido.setPrecioTotal(cambios.getPrecioTotal());
        }
        if (cambios.getDescripcion() != null) {
            pedido.setDescripcion(cambios.getDescripcion());
        }
    }

    private boolean tieneDetalles(PedidosDto pedido) {
        return pedido.getPedido() != null && !pedido.getPedido().isEmpty();
    }

    /**
     * Reemplaza por completo las líneas del pedido y sus adiciones.
     * orphanRemoval en EncabezadoPedidos.detalles y en CuerpoPedidos.adiciones
     * borra en cascada las filas que dejan de estar en las colecciones.
     */
    private void reemplazarDetalles(EncabezadoPedidos pedido, List<DetallesPedidoDto> nuevosDetalles) {
        List<CuerpoPedidos> lineas = construirDetallesPedido(nuevosDetalles, pedido);
        pedido.getDetalles().clear();
        pedido.getDetalles().addAll(lineas);
    }


    // ----------------------------------------------------------------------
    // Construcción del grafo detalle + adiciones (compartido crear / editar)
    // ----------------------------------------------------------------------

    private List<CuerpoPedidos> construirDetallesPedido(List<DetallesPedidoDto> detalles,
                                                        EncabezadoPedidos encabezado) {
        return detalles.stream()
                       .map(detalle -> construirCuerpoPedido(detalle, encabezado))
                       .toList();
    }

    private CuerpoPedidos construirCuerpoPedido(DetallesPedidoDto detalle, EncabezadoPedidos encabezado) {
        CuerpoPedidos cuerpo = new CuerpoPedidos(
            detalle.getIdCuerpoPedido(),
            encabezado,
            inventarioRepository.getReferenceById(detalle.getIdProducto()),
            detalle.getCantidad()
        );
        cuerpo.reemplazarAdiciones(construirAdiciones(detalle.getAdiciones()));
        return cuerpo;
    }

    private List<CuerpoPedidosAdiciones> construirAdiciones(List<AdicionPedidoDto> adiciones) {
        if (adiciones == null || adiciones.isEmpty()) {
            return new ArrayList<>();
        }
        return adiciones.stream()
                        .map(this::construirAdicion)
                        .toList();
    }

    private CuerpoPedidosAdiciones construirAdicion(AdicionPedidoDto adicion) {
        CuerpoPedidosAdiciones fila = new CuerpoPedidosAdiciones();
        fila.setAdicion(adicionesRepository.getReferenceById(adicion.getIdAdicion()));
        fila.setCantidadAdicion(adicion.getCantidad());
        return fila; // el back-reference a CuerpoPedidos lo fija cuerpo.reemplazarAdiciones(...)
    }


    // ----------------------------------------------------------------------
    // Pago
    // ----------------------------------------------------------------------

    public ResponseEntity<FacturaPedidoDto> pagoPedido(PagarPedidoDto pago) {
        Optional<EncabezadoPedidos> pedidoOptional = encabezadoPedidosRepository.findById(pago.getIdPedido());
        EncabezadoPedidos pedido = pedidoOptional.orElseThrow();

        int calcularCambio = pago.getPagoPedido() - pedido.getPrecioTotal();
        pedido.setEstadoPedido(ESTADO_PEDIDO_PAGADO);
        pedido.setTipoPago(pago.getTipoPago());
        encabezadoPedidosRepository.save(pedido);

        DateTimeFormatter formato = DateTimeFormatter.ofPattern(FORMATO_FECHA_HORA_FACTURA);
        String fechaYHora = LocalDateTime.now().format(formato);

        return ResponseEntity.ok(new FacturaPedidoDto(pago.getIdPedido(), pedido.getIdMesa().getIdMesa(),
            pedido.getEstadoPedido(), pedido.getPrecioTotal(), calcularCambio, pedido.getTipoPago(), fechaYHora));
    }

}
