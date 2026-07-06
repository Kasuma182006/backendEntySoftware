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
    
    
    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<Integer> crearPedido(PedidosDto pedido){
                
        Mesas mesaProxy = mesasRepository.getReferenceById(pedido.getIdMesa());
    
        EncabezadoPedidos encabezadoPedido = new EncabezadoPedidos(
            null, 
            mesaProxy, 
            "EFECTIVO", 
            "EN ESPERA", 
            pedido.getValorDomicilio(), 
            pedido.getPrecioTotal(), 
            LocalDate.now(), 
            pedido.getDescripcion(),
            new ArrayList<>()
        ); 
        

        List<CuerpoPedidos> listaCuerpoPedido = pedido.getPedido().stream()
                                                        .map(cuerpoPedido -> new CuerpoPedidos(
                                                            null, 
                                                            encabezadoPedido, 
                                                            inventarioRepository.getReferenceById(cuerpoPedido.getIdProducto()), 
                                                            cuerpoPedido.getCantidad()   
                                                        ))
                                                        .toList();


        encabezadoPedido.setDetalles(listaCuerpoPedido);
        log.debug("lista de productos pedidos: {}", listaCuerpoPedido);
        EncabezadoPedidos encabezadoGuardado = encabezadoPedidosRepository.save(encabezadoPedido);

        

        log.debug("se han creado los cuerpos del pedido en la base de datos");

        return ResponseEntity.ok(encabezadoGuardado.getIdPedido());
    }

    public ResponseEntity<List<PedidosDto>> pedidosHoy (Integer idEstablecimiento){

        List<EncabezadoPedidos> pedido = encabezadoPedidosRepository.buscarPedidosDeHoyConDetalles(idEstablecimiento, LocalDate.now());

        List<PedidosDto> listaPedidosDto = pedido.stream()
                                                  .map (p -> {
                                                        
                                                        PedidosDto dto = mapperPedidosDto.pedidosToEntity(p);
                                                        
                                                        List<DetallesPedidoDto> detallesDto = p.getDetalles().stream()
                                                                                                .map(cuerpo -> new DetallesPedidoDto(
                                                                                                    cuerpo.getIdCuerpo(),
                                                                                                    cuerpo.getIdInventario().getIdInventario(),
                                                                                                    cuerpo.getIdInventario().getNombre(),
                                                                                                    cuerpo.getCantidad()
                                                                                                ))
                                                                                                .toList();
                                                        
                                                        
                                                        dto.setPedido(detallesDto);
                                                        
                                                        return dto;
                                                    })
                                                    .toList();
            


        
        return ResponseEntity.ok(listaPedidosDto);

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
    
    @SuppressWarnings("null")
    public ResponseEntity<FacturaPedidoDto> pagoPedido(PagarPedidoDto pago){
        Optional <EncabezadoPedidos> pedido =  encabezadoPedidosRepository.findById(pago.getIdPedido());
         
        int calcularCambio =  pago.getPagoPedido() - pedido.get().getPrecioTotal();
        pedido.get().setEstadoPedido("PAGO");
        pedido.get().setTipoPago(pago.getTipoPago());
        encabezadoPedidosRepository.save(pedido.get());
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String fechaYHora = LocalDateTime.now().format(formato);

        return ResponseEntity.ok(new FacturaPedidoDto(pago.getIdPedido(),pedido.get().getIdMesa().getIdMesa(),pedido.get().getEstadoPedido(),pedido.get().getPrecioTotal(),calcularCambio,pedido.get().getTipoPago(),fechaYHora));
    }

}
