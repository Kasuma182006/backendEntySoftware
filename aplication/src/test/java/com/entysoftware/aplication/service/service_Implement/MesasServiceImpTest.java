package com.entysoftware.aplication.service.service_Implement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.customExceptions.ObjetosNoEncontradosExepcion;
import com.entysoftware.aplication.mapper.MapperMesasDto;
import com.entysoftware.aplication.model.Establecimiento;
import com.entysoftware.aplication.model.Mesas;
import com.entysoftware.aplication.model.dto.MesasDto;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.repository.MesasRepository;

@ExtendWith(MockitoExtension.class)
class MesasServiceImpTest {

    @Mock
    private MesasRepository mesasRepository;

    @Mock
    private MapperMesasDto mapperMesasDto;

    @Mock
    private EstablecimientoRepository establecimientoRepository;

    @InjectMocks
    private MesasServiceImp mesasServiceImp;

    private Mesas crearMesa(Integer id, Integer idEstablecimiento, String nombre, Boolean estado) {
        Mesas mesa = new Mesas();
        mesa.setIdMesa(id);
        mesa.setIdEstablecimiento(idEstablecimiento);
        mesa.setNombreMesa(nombre);
        mesa.setEstadoMesa(estado);
        return mesa;
    }

    @Nested
    class ListarMesasTests {

        @Test
        void givenEstablecimientoConMesas_whenListarMesas_thenRetornaListaDeMesasDto() {
            // Arrange
            Integer idEstablecimiento = 1;
            Mesas mesa = crearMesa(1, idEstablecimiento, "Mesa 1", false);
            MesasDto mesaDto = new MesasDto(1, idEstablecimiento, "Mesa 1", false);
            when(mesasRepository.findByFK_id_establecimiento(idEstablecimiento)).thenReturn(List.of(mesa));
            when(mapperMesasDto.MesasToDto(mesa)).thenReturn(mesaDto);

            // Act
            ResponseEntity<List<MesasDto>> response = mesasServiceImp.listarMesas(idEstablecimiento);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            List<MesasDto> body = response.getBody();
            assertNotNull(body);
            assertEquals(1, body.size());
            assertEquals("Mesa 1", body.get(0).getNombreMesa());
            verify(mesasRepository, times(1)).findByFK_id_establecimiento(idEstablecimiento);
        }

        @Test
        void givenEstablecimientoSinMesas_whenListarMesas_thenRetornaListaVacia() {
            // Arrange
            Integer idEstablecimiento = 2;
            when(mesasRepository.findByFK_id_establecimiento(idEstablecimiento)).thenReturn(Collections.emptyList());

            // Act
            ResponseEntity<List<MesasDto>> response = mesasServiceImp.listarMesas(idEstablecimiento);

            // Assert
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
            verify(mapperMesasDto, never()).MesasToDto(any());
        }
    }

    @Nested
    class CrearMesaTests {

        @Test
        void givenEstablecimientoExistente_whenCrearMesa_thenRetornaMesaCreada() {
            // Arrange
            MesasDto mesaDto = new MesasDto(null, 1, "Mesa 5", false);
            Establecimiento establecimiento = new Establecimiento(1, "Restaurante", "prop-1", null, "ACTIVO");
            Mesas mesaSinGuardar = crearMesa(null, 1, "Mesa 5", false);
            Mesas mesaGuardada = crearMesa(9, 1, "Mesa 5", false);
            MesasDto mesaDtoGuardada = new MesasDto(9, 1, "Mesa 5", false);

            when(establecimientoRepository.findById(1)).thenReturn(Optional.of(establecimiento));
            when(mapperMesasDto.dtoToMesas(mesaDto, establecimiento)).thenReturn(mesaSinGuardar);
            when(mesasRepository.save(mesaSinGuardar)).thenReturn(mesaGuardada);
            when(mapperMesasDto.MesasToDto(mesaGuardada)).thenReturn(mesaDtoGuardada);

            // Act
            ResponseEntity<MesasDto> response = mesasServiceImp.crearMesa(mesaDto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(9, response.getBody().getIdMesa());
            assertEquals("Mesa 5", response.getBody().getNombreMesa());
            verify(mesasRepository, times(1)).save(mesaSinGuardar);
        }

        @Test
        void givenEstablecimientoInexistente_whenCrearMesa_thenLanzaObjetosNoEncontradosExepcion() {
            // Arrange
            MesasDto mesaDto = new MesasDto(null, 99, "Mesa 5", false);
            when(establecimientoRepository.findById(99)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ObjetosNoEncontradosExepcion.class, () -> mesasServiceImp.crearMesa(mesaDto));
            verify(mesasRepository, never()).save(any());
        }
    }

    @Nested
    class EditarMesaTests {

        @Test
        void givenMesaExistente_whenEditarMesaConNombreYEstado_thenActualizaYRetornaMensajeExito() {
            // Arrange
            MesasDto mesaDto = new MesasDto(1, 1, "Mesa Renombrada", true);
            Mesas mesaExistente = crearMesa(1, 1, "Mesa 1", false);
            when(mesasRepository.findById(1)).thenReturn(Optional.of(mesaExistente));

            // Act
            ResponseEntity<String> response = mesasServiceImp.editarMesa(mesaDto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Mesa actualizada", response.getBody());
            assertEquals("Mesa Renombrada", mesaExistente.getNombreMesa());
            assertTrue(mesaExistente.getEstadoMesa());
            verify(mesasRepository, times(1)).save(mesaExistente);
        }

        @Test
        void givenMesaExistenteConCamposNulos_whenEditarMesa_thenConservaValoresOriginales() {
            // Arrange
            MesasDto mesaDto = new MesasDto(1, 1, null, null);
            Mesas mesaExistente = crearMesa(1, 1, "Mesa 1", false);
            when(mesasRepository.findById(1)).thenReturn(Optional.of(mesaExistente));

            // Act
            ResponseEntity<String> response = mesasServiceImp.editarMesa(mesaDto);

            // Assert
            assertEquals("Mesa actualizada", response.getBody());
            assertEquals("Mesa 1", mesaExistente.getNombreMesa());
            assertFalse(mesaExistente.getEstadoMesa());
            verify(mesasRepository, times(1)).save(mesaExistente);
        }

        @Test
        void givenMesaInexistente_whenEditarMesa_thenLanzaObjetosNoEncontradosExepcion() {
            // Arrange
            MesasDto mesaDto = new MesasDto(50, 1, "Mesa X", true);
            when(mesasRepository.findById(50)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ObjetosNoEncontradosExepcion.class, () -> mesasServiceImp.editarMesa(mesaDto));
            verify(mesasRepository, never()).save(any());
        }
    }

    @Nested
    class EliminarMesaTests {

        @Test
        void givenMesaExistente_whenEliminarMesa_thenEliminaYRetornaMensajeExito() {
            // Arrange
            Integer idMesa = 1;
            when(mesasRepository.existsById(idMesa)).thenReturn(true);

            // Act
            ResponseEntity<String> response = mesasServiceImp.eliminarMesa(idMesa);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Mesa eliminada", response.getBody());
            verify(mesasRepository, times(1)).deleteById(idMesa);
        }

        @Test
        void givenMesaInexistente_whenEliminarMesa_thenLanzaObjetosNoEncontradosExepcionYNoElimina() {
            // Arrange
            Integer idMesa = 404;
            when(mesasRepository.existsById(idMesa)).thenReturn(false);

            // Act & Assert
            assertThrows(ObjetosNoEncontradosExepcion.class, () -> mesasServiceImp.eliminarMesa(idMesa));
            verify(mesasRepository, never()).deleteById(any());
        }
    }
}
