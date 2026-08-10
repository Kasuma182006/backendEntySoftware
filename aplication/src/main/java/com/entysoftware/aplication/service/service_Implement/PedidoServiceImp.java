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
import com.entysoftware.aplication.model.EncabezadoPedidos;
import com.entysoftware.aplication.model.Mesas;
import com.entysoftware.aplication.model.dto.pagosDTOs.FacturaPedidoDto;
import com.entysoftware.aplication.model.dto.pagosDTOs.PagarPedidoDto;
import com.entysoftware.aplication.model.dto.pedidosDTOs.DetallesPedidoDto;
import com.entysoftware.aplication.model.dto.pedidosDTOs.PedidosDto;
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

    private final MapperPedidosDto mapperPedidosDto;

    public PedidoServiceImp (EncabezadoPedidosRepository encabezadoPedidosRepository,InventarioRepository inventarioRepository,MesasRepository mesasRepository,MapperPedidosDto mapperPedidosDto){
        this.encabezadoPedidosRepository = encabezadoPedidosRepository;
        this.inventarioRepository = inventarioRepository;
        this.mesasRepository = mesasRepository;
        this.mapperPedidosDto = mapperPedidosDto;
    }


    @Transactional
    public ResponseEntity<Integer> crearPedido(PedidosDto pedido){

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

        List<CuerpoPedidos> listaCuerpoPedido = crearDetallesPedido(pedido.getPedido(), encabezadoPedido);

        encabezadoPedido.setDetalles(listaCuerpoPedido);
        log.debug("lista de productos pedidos: {}", listaCuerpoPedido);
        EncabezadoPedidos encabezadoGuardado = encabezadoPedidosRepository.save(encabezadoPedido);

        log.debug("se han creado los cuerpos del pedido en la base de datos");

        return ResponseEntity.ok(encabezadoGuardado.getIdPedido());
    }

    private List<CuerpoPedidos> crearDetallesPedido(List<DetallesPedidoDto> detalles, EncabezadoPedidos encabezadoPedido){
        return detalles.stream()
                        .map(cuerpoPedido -> new CuerpoPedidos(
                            null,
                            encabezadoPedido,
                            inventarioRepository.getReferenceById(cuerpoPedido.getIdProducto()),
                            cuerpoPedido.getCantidad()
                        ))
                        .toList();
    }

    public ResponseEntity<List<PedidosDto>> pedidosHoy (Integer idEstablecimiento){

        List<EncabezadoPedidos> pedido = encabezadoPedidosRepository.buscarPedidosDeHoyConDetalles(idEstablecimiento, LocalDate.now());

        List<PedidosDto> listaPedidosDto = pedido.stream()
                                                  .map(this::convertirAPedidoDto)
                                                  .toList();

        return ResponseEntity.ok(listaPedidosDto);

    }

    private PedidosDto convertirAPedidoDto(EncabezadoPedidos encabezadoPedido){
        PedidosDto dto = mapperPedidosDto.pedidosToEntity(encabezadoPedido);

        List<DetallesPedidoDto> detallesDto = encabezadoPedido.getDetalles().stream()
                                                                .map(cuerpo -> new DetallesPedidoDto(
                                                                    cuerpo.getIdCuerpo(),
                                                                    cuerpo.getIdInventario().getIdInventario(),
                                                                    cuerpo.getIdInventario().getNombre(),
                                                                    cuerpo.getCantidad()
                                                                ))
                                                                .toList();

        dto.setPedido(detallesDto);

        return dto;
    }


    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<String> editarPedido(PedidosDto editarPedido) {

        EncabezadoPedidos pedidoExistente = encabezadoPedidosRepository.findById(editarPedido.getIdPedido())
            .orElseThrow(() -> new EntityNotFoundException("El pedido con ID " + editarPedido.getIdPedido() + " no existe"));

        if (editarPedido.getIdMesa() != null) {
            pedidoExistente.setIdMesa(mesasRepository.getReferenceById(editarPedido.getIdMesa()));
        }
        if (editarPedido.getTipoPago() != null) {
            pedidoExistente.setTipoPago(editarPedido.getTipoPago().toUpperCase());
        }
        if (editarPedido.getEstadoPedido() != null) {
            pedidoExistente.setEstadoPedido(editarPedido.getEstadoPedido());
        }
        if (editarPedido.getValorDomicilio() != null) {
            pedidoExistente.setValorDomicilio(editarPedido.getValorDomicilio());
        }
        if (editarPedido.getPrecioTotal() != null) {
            pedidoExistente.setPrecioTotal(editarPedido.getPrecioTotal());
        }
        if (editarPedido.getDescripcion() != null) {
            pedidoExistente.setDescripcion(editarPedido.getDescripcion());
        }

        if (editarPedido.getPedido() != null && !editarPedido.getPedido().isEmpty()) {
            List<CuerpoPedidos> listaCuerpoPedido = editarPedido.getPedido().stream()
                .map(cuerpoPedido -> new CuerpoPedidos(
                    cuerpoPedido.getIdCuerpoPedido(),
                    pedidoExistente,
                    inventarioRepository.getReferenceById(cuerpoPedido.getIdProducto()),
                    cuerpoPedido.getCantidad()
                ))
                .toList();

            pedidoExistente.getDetalles().clear();
            pedidoExistente.getDetalles().addAll(listaCuerpoPedido);
        }

        encabezadoPedidosRepository.save(pedidoExistente);

        return ResponseEntity.ok("Pedido actualizado");

    }

    public ResponseEntity<FacturaPedidoDto> pagoPedido(PagarPedidoDto pago){
        Optional<EncabezadoPedidos> pedidoOptional = encabezadoPedidosRepository.findById(pago.getIdPedido());
        EncabezadoPedidos pedido = pedidoOptional.orElseThrow();

        int calcularCambio = pago.getPagoPedido() - pedido.getPrecioTotal();
        pedido.setEstadoPedido(ESTADO_PEDIDO_PAGADO);
        pedido.setTipoPago(pago.getTipoPago());
        encabezadoPedidosRepository.save(pedido);

        DateTimeFormatter formato = DateTimeFormatter.ofPattern(FORMATO_FECHA_HORA_FACTURA);
        String fechaYHora = LocalDateTime.now().format(formato);

        return ResponseEntity.ok(new FacturaPedidoDto(pago.getIdPedido(),pedido.getIdMesa().getIdMesa(),pedido.getEstadoPedido(),pedido.getPrecioTotal(),calcularCambio,pedido.getTipoPago(),fechaYHora));
    }

}
