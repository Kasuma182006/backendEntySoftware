package com.entysoftware.aplication.service.service_Implement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.mapper.MapperPedidosDto;
import com.entysoftware.aplication.model.CuerpoPedidos;
import com.entysoftware.aplication.model.EncabezadoPedidos;
import com.entysoftware.aplication.model.Inventario;
import com.entysoftware.aplication.model.Mesas;
import com.entysoftware.aplication.model.dto.pagosDTOs.FacturaPedidoDto;
import com.entysoftware.aplication.model.dto.pagosDTOs.PagarPedidoDto;
import com.entysoftware.aplication.model.dto.pedidosDTOs.DetallesPedidoDto;
import com.entysoftware.aplication.model.dto.pedidosDTOs.PedidosDto;
import com.entysoftware.aplication.repository.EncabezadoPedidosRepository;
import com.entysoftware.aplication.repository.InventarioRepository;
import com.entysoftware.aplication.repository.MesasRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImpTest {

    @Mock
    private EncabezadoPedidosRepository encabezadoPedidosRepository;

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private MesasRepository mesasRepository;

    @Mock
    private MapperPedidosDto mapperPedidosDto;

    @InjectMocks
    private PedidoServiceImp pedidoServiceImp;

    private Mesas crearMesa(Integer id) {
        Mesas mesa = new Mesas();
        mesa.setIdMesa(id);
        return mesa;
    }

    @Nested
    class CrearPedidoTests {

        @Test
        void givenPedidoValido_whenCrearPedido_thenGuardaEncabezadoConDetallesYRetornaId() {
            // Arrange
            DetallesPedidoDto detalleDto = new DetallesPedidoDto(null, 100, null, 2);
            PedidosDto pedidoDto = new PedidosDto(null, 1, null, null, 2000, 15000, null, "sin cebolla", List.of(detalleDto));

            Mesas mesaProxy = crearMesa(1);
            Inventario producto = new Inventario(100, "Hamburguesa", 3, "desc", 12000);
            when(mesasRepository.getReferenceById(1)).thenReturn(mesaProxy);
            when(inventarioRepository.getReferenceById(100)).thenReturn(producto);
            when(encabezadoPedidosRepository.save(any(EncabezadoPedidos.class))).thenAnswer(invocation -> {
                EncabezadoPedidos encabezado = invocation.getArgument(0);
                encabezado.setIdPedido(55);
                return encabezado;
            });

            // Act
            ResponseEntity<Integer> response = pedidoServiceImp.crearPedido(pedidoDto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(55, response.getBody());

            ArgumentCaptor<EncabezadoPedidos> captor = ArgumentCaptor.forClass(EncabezadoPedidos.class);
            verify(encabezadoPedidosRepository, times(1)).save(captor.capture());
            EncabezadoPedidos guardado = captor.getValue();
            assertEquals("EFECTIVO", guardado.getTipoPago());
            assertEquals("EN ESPERA", guardado.getEstadoPedido());
            assertEquals(2000, guardado.getValorDomicilio());
            assertEquals(15000, guardado.getPrecioTotal());
            assertEquals("sin cebolla", guardado.getDescripcion());
            assertEquals(1, guardado.getDetalles().size());
            assertEquals(producto, guardado.getDetalles().get(0).getIdInventario());
            assertEquals(2, guardado.getDetalles().get(0).getCantidad());
        }

        @Test
        void givenPedidoConMultiplesProductos_whenCrearPedido_thenCreaUnCuerpoPorCadaProducto() {
            // Arrange
            DetallesPedidoDto detalle1 = new DetallesPedidoDto(null, 1, null, 1);
            DetallesPedidoDto detalle2 = new DetallesPedidoDto(null, 2, null, 3);
            PedidosDto pedidoDto = new PedidosDto(null, 1, null, null, 0, 30000, null, "", List.of(detalle1, detalle2));

            when(mesasRepository.getReferenceById(1)).thenReturn(crearMesa(1));
            when(inventarioRepository.getReferenceById(1)).thenReturn(new Inventario(1, "A", 1, "d", 1000));
            when(inventarioRepository.getReferenceById(2)).thenReturn(new Inventario(2, "B", 1, "d", 2000));
            when(encabezadoPedidosRepository.save(any(EncabezadoPedidos.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            pedidoServiceImp.crearPedido(pedidoDto);

            // Assert
            ArgumentCaptor<EncabezadoPedidos> captor = ArgumentCaptor.forClass(EncabezadoPedidos.class);
            verify(encabezadoPedidosRepository).save(captor.capture());
            assertEquals(2, captor.getValue().getDetalles().size());
            verify(inventarioRepository, times(1)).getReferenceById(1);
            verify(inventarioRepository, times(1)).getReferenceById(2);
        }
    }

    @Nested
    class PedidosHoyTests {

        @Test
        void givenPedidosDelDia_whenPedidosHoy_thenRetornaListaConDetallesMapeados() {
            // Arrange
            Integer idEstablecimiento = 1;
            Mesas mesa = crearMesa(3);
            Inventario producto = new Inventario(100, "Papas", 1, "desc", 5000);
            EncabezadoPedidos encabezado = new EncabezadoPedidos(1, mesa, "EFECTIVO", "EN ESPERA", 0, 15000, LocalDate.now(), "desc", new ArrayList<>());
            CuerpoPedidos cuerpo = new CuerpoPedidos(7, encabezado, producto, 3);
            encabezado.setDetalles(List.of(cuerpo));

            when(encabezadoPedidosRepository.buscarPedidosDeHoyConDetalles(any(), any(LocalDate.class))).thenReturn(List.of(encabezado));

            PedidosDto pedidoDtoBase = new PedidosDto(1, 3, "EFECTIVO", "EN ESPERA", 0, 15000, LocalDate.now(), "desc", null);
            when(mapperPedidosDto.pedidosToEntity(encabezado)).thenReturn(pedidoDtoBase);

            // Act
            ResponseEntity<List<PedidosDto>> response = pedidoServiceImp.pedidosHoy(idEstablecimiento);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            List<PedidosDto> body = response.getBody();
            assertNotNull(body);
            assertEquals(1, body.size());
            List<DetallesPedidoDto> detalles = body.get(0).getPedido();
            assertNotNull(detalles);
            assertEquals(1, detalles.size());
            assertEquals(7, detalles.get(0).getIdCuerpoPedido());
            assertEquals(100, detalles.get(0).getIdProducto());
            assertEquals("Papas", detalles.get(0).getNombre());
            assertEquals(3, detalles.get(0).getCantidad());
        }

        @Test
        void givenSinPedidosHoy_whenPedidosHoy_thenRetornaListaVacia() {
            // Arrange
            Integer idEstablecimiento = 2;
            when(encabezadoPedidosRepository.buscarPedidosDeHoyConDetalles(any(), any(LocalDate.class))).thenReturn(List.of());

            // Act
            ResponseEntity<List<PedidosDto>> response = pedidoServiceImp.pedidosHoy(idEstablecimiento);

            // Assert
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
            verify(mapperPedidosDto, never()).pedidosToEntity(any());
        }
    }

    @Nested
    class EditarPedidoTests {

        @Test
        void givenPedidoExistente_whenEditarPedidoConTodosLosCampos_thenActualizaTodosLosCampos() {
            // Arrange
            DetallesPedidoDto nuevoDetalle = new DetallesPedidoDto(null, 50, null, 4);
            PedidosDto editar = new PedidosDto(1, 2, "transferencia", "PAGO", 1000, 20000, null, "nueva desc", List.of(nuevoDetalle));

            EncabezadoPedidos pedidoExistente = new EncabezadoPedidos(1, crearMesa(1), "EFECTIVO", "EN ESPERA", 0, 5000, LocalDate.now(), "vieja desc", new ArrayList<>());
            when(encabezadoPedidosRepository.findById(1)).thenReturn(Optional.of(pedidoExistente));

            Mesas mesaNueva = crearMesa(2);
            Inventario productoNuevo = new Inventario(50, "Gaseosa", 1, "desc", 3000);
            when(mesasRepository.getReferenceById(2)).thenReturn(mesaNueva);
            when(inventarioRepository.getReferenceById(50)).thenReturn(productoNuevo);

            // Act
            ResponseEntity<String> response = pedidoServiceImp.editarPedido(editar);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Pedido actualizado", response.getBody());
            assertEquals(mesaNueva, pedidoExistente.getIdMesa());
            assertEquals("TRANSFERENCIA", pedidoExistente.getTipoPago());
            assertEquals("PAGO", pedidoExistente.getEstadoPedido());
            assertEquals(1000, pedidoExistente.getValorDomicilio());
            assertEquals(20000, pedidoExistente.getPrecioTotal());
            assertEquals("nueva desc", pedidoExistente.getDescripcion());
            assertEquals(1, pedidoExistente.getDetalles().size());
            assertEquals(4, pedidoExistente.getDetalles().get(0).getCantidad());
            assertEquals(productoNuevo, pedidoExistente.getDetalles().get(0).getIdInventario());
            verify(encabezadoPedidosRepository, times(1)).save(pedidoExistente);
        }

        @Test
        void givenPedidoExistenteYCamposNulos_whenEditarPedido_thenConservaValoresOriginales() {
            // Arrange
            PedidosDto editar = new PedidosDto(1, null, null, null, null, null, null, null, null);
            EncabezadoPedidos pedidoExistente = new EncabezadoPedidos(1, crearMesa(1), "EFECTIVO", "EN ESPERA", 0, 5000, LocalDate.now(), "vieja desc", new ArrayList<>());
            when(encabezadoPedidosRepository.findById(1)).thenReturn(Optional.of(pedidoExistente));

            // Act
            ResponseEntity<String> response = pedidoServiceImp.editarPedido(editar);

            // Assert
            assertEquals("Pedido actualizado", response.getBody());
            assertEquals("EFECTIVO", pedidoExistente.getTipoPago());
            assertEquals("EN ESPERA", pedidoExistente.getEstadoPedido());
            assertEquals("vieja desc", pedidoExistente.getDescripcion());
            assertTrue(pedidoExistente.getDetalles().isEmpty());
            verify(mesasRepository, never()).getReferenceById(any());
            verify(encabezadoPedidosRepository, times(1)).save(pedidoExistente);
        }

        @Test
        void givenPedidoInexistente_whenEditarPedido_thenLanzaEntityNotFoundException() {
            // Arrange
            PedidosDto editar = new PedidosDto(404, 1, null, null, null, null, null, null, null);
            when(encabezadoPedidosRepository.findById(404)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> pedidoServiceImp.editarPedido(editar));
            verify(encabezadoPedidosRepository, never()).save(any());
        }
    }

    @Nested
    class PagoPedidoTests {

        @Test
        void givenPedidoExistente_whenPagoPedido_thenActualizaEstadoYRetornaFactura() {
            // Arrange
            PagarPedidoDto pago = new PagarPedidoDto(1, 20000, "EFECTIVO");
            EncabezadoPedidos pedidoExistente = new EncabezadoPedidos(1, crearMesa(3), "EFECTIVO", "EN ESPERA", 0, 15000, LocalDate.now(), "desc", new ArrayList<>());
            when(encabezadoPedidosRepository.findById(1)).thenReturn(Optional.of(pedidoExistente));

            // Act
            ResponseEntity<FacturaPedidoDto> response = pedidoServiceImp.pagoPedido(pago);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            FacturaPedidoDto factura = response.getBody();
            assertNotNull(factura);
            assertEquals(1, factura.getIdPedido());
            assertEquals(3, factura.getIdMesa());
            assertEquals("PAGO", factura.getEstadoPedido());
            assertEquals(15000, factura.getValorTotal());
            assertEquals(5000, factura.getValorCambio());
            assertEquals("EFECTIVO", factura.getTipoPago());
            assertNotNull(factura.getFechaYHora());

            assertEquals("PAGO", pedidoExistente.getEstadoPedido());
            assertEquals("EFECTIVO", pedidoExistente.getTipoPago());
            verify(encabezadoPedidosRepository, times(1)).save(pedidoExistente);
        }

        @Test
        void givenPedidoInexistente_whenPagoPedido_thenLanzaNoSuchElementException() {
            // Arrange
            PagarPedidoDto pago = new PagarPedidoDto(99, 10000, "EFECTIVO");
            when(encabezadoPedidosRepository.findById(99)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NoSuchElementException.class, () -> pedidoServiceImp.pagoPedido(pago));
            verify(encabezadoPedidosRepository, never()).save(any());
        }
    }
}
