package com.entysoftware.aplication.service.service_Implement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.customExceptions.EstablecimientoNoEncontradoException;
import com.entysoftware.aplication.customExceptions.UsuarioNoEncontradoException;
import com.entysoftware.aplication.model.Categorias;
import com.entysoftware.aplication.model.Empleados;
import com.entysoftware.aplication.model.Establecimiento;
import com.entysoftware.aplication.model.Inventario;
import com.entysoftware.aplication.model.Mesas;
import com.entysoftware.aplication.model.Propietarios;
import com.entysoftware.aplication.model.dto.loginDto.EstablecimientosDto;
import com.entysoftware.aplication.model.dto.loginDto.LoginDto;
import com.entysoftware.aplication.model.dto.loginDto.LoginSuccesfulDto;
import com.entysoftware.aplication.repository.CategoriasRepository;
import com.entysoftware.aplication.repository.EmpleadosRepository;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.repository.InventarioRepository;
import com.entysoftware.aplication.repository.MesasRepository;
import com.entysoftware.aplication.repository.PropietariosRepository;
import com.entysoftware.aplication.security.JwtService;

@ExtendWith(MockitoExtension.class)
class LoginServiceImpTest {

    @Mock
    private EstablecimientoRepository establecimientoRepository;

    @Mock
    private EmpleadosRepository empleadosRepository;

    @Mock
    private PropietariosRepository propietariosRepository;

    @Mock
    private MesasRepository mesasRepository;

    @Mock
    private CategoriasRepository categoriasRepository;

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginServiceImp loginServiceImp;

    private Establecimiento crearEstablecimiento(Integer id) {
        return new Establecimiento(id, "Restaurante Central", "prop-1", LocalDate.of(2024, 1, 1), "ACTIVO");
    }

    @Nested
    class UbicarEstablecimientoTests {

        @Test
        void givenIdentificacionDePropietario_whenUbicarEstablecimiento_thenRetornaEstablecimientosDelPropietario() {
            // Arrange
            String identificacion = "prop-1";
            Establecimiento establecimiento = crearEstablecimiento(1);
            when(establecimientoRepository.buscarEstablecimiento(identificacion)).thenReturn(List.of(establecimiento));

            // Act
            ResponseEntity<List<EstablecimientosDto>> response = loginServiceImp.ubicarEstablecimiento(identificacion);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            List<EstablecimientosDto> body = response.getBody();
            assertNotNull(body);
            assertEquals(1, body.size());
            assertEquals("Restaurante Central", body.get(0).getNombre_establecimiento());
            assertEquals(1, body.get(0).getId_establecimiento());
            verify(empleadosRepository, never()).establecimientosEmpleados(anyString());
        }

        @Test
        void givenIdentificacionDeEmpleado_whenUbicarEstablecimiento_thenRetornaEstablecimientosDelEmpleado() {
            // Arrange
            String identificacion = "emp-1";
            when(establecimientoRepository.buscarEstablecimiento(identificacion)).thenReturn(Collections.emptyList());

            Empleados empleado = new Empleados(1, "emp-1", 2, "Juan", "1234", "mesero");
            when(empleadosRepository.establecimientosEmpleados(identificacion)).thenReturn(List.of(empleado));

            Establecimiento establecimiento = crearEstablecimiento(2);
            when(establecimientoRepository.findAllById(List.of(2))).thenReturn(List.of(establecimiento));

            // Act
            ResponseEntity<List<EstablecimientosDto>> response = loginServiceImp.ubicarEstablecimiento(identificacion);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            List<EstablecimientosDto> body = response.getBody();
            assertNotNull(body);
            assertEquals(1, body.size());
            assertEquals(2, body.get(0).getId_establecimiento());
            verify(establecimientoRepository, times(1)).findAllById(List.of(2));
        }

        @Test
        void givenIdentificacionSinCoincidencias_whenUbicarEstablecimiento_thenLanzaUsuarioNoEncontradoException() {
            // Arrange
            String identificacion = "desconocido";
            when(establecimientoRepository.buscarEstablecimiento(identificacion)).thenReturn(Collections.emptyList());
            when(empleadosRepository.establecimientosEmpleados(identificacion)).thenReturn(Collections.emptyList());

            // Act & Assert
            assertThrows(UsuarioNoEncontradoException.class, () -> loginServiceImp.ubicarEstablecimiento(identificacion));
            verify(establecimientoRepository, never()).findAllById(any());
        }
    }

    @Nested
    class LoginTests {

        @Test
        void givenEstablecimientoInexistente_whenLogin_thenLanzaEstablecimientoNoEncontradoException() {
            // Arrange
            LoginDto loginDto = new LoginDto("prop-1", "1234", "99");
            when(establecimientoRepository.findById(99)).thenReturn(java.util.Optional.empty());

            // Act & Assert
            assertThrows(EstablecimientoNoEncontradoException.class, () -> loginServiceImp.login(loginDto));
            verify(propietariosRepository, never()).loginPropietario(anyString(), anyString());
        }

        @Test
        void givenCredencialesDePropietario_whenLogin_thenRetornaLoginExitosoComoAdministrador() {
            // Arrange
            LoginDto loginDto = new LoginDto("prop-1", "1234", "1");
            Establecimiento establecimiento = crearEstablecimiento(1);
            when(establecimientoRepository.findById(1)).thenReturn(java.util.Optional.of(establecimiento));

            Propietarios propietario = new Propietarios("prop-1", "Carlos", "1234", LocalDate.of(2024, 1, 1), null, "ACTIVO");
            when(propietariosRepository.loginPropietario("prop-1", "1234")).thenReturn(propietario);

            Mesas mesa = new Mesas();
            mesa.setIdMesa(10);
            when(mesasRepository.findByFK_id_establecimiento(1)).thenReturn(List.of(mesa));

            Categorias categoria = new Categorias(5, "Bebidas", 1);
            when(categoriasRepository.findByFk_id_establecimiento(1)).thenReturn(List.of(categoria));

            Inventario inventario = new Inventario(20, "Gaseosa", 5, "Bebida fria", 3000);
            when(inventarioRepository.findByFK_categoria(5)).thenReturn(List.of(inventario));

            when(jwtService.generarToken("prop-1", "administrador")).thenReturn("token-propietario");

            // Act
            ResponseEntity<LoginSuccesfulDto> response = loginServiceImp.login(loginDto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            LoginSuccesfulDto body = response.getBody();
            assertNotNull(body);
            assertEquals("prop-1", body.getNumero_identificacion());
            assertEquals("Carlos", body.getNombre());
            assertEquals("administrador", body.getRol());
            assertEquals(1, body.getId_establecimiento());
            assertEquals("token-propietario", body.getToken());
            assertEquals(1, body.getMesas().size());
            assertEquals(1, body.getCategorias().size());
            assertEquals(1, body.getInventario().size());
            verify(empleadosRepository, never()).loginEmpleado(any(), anyString(), anyString());
        }

        @Test
        void givenCredencialesDeEmpleado_whenLogin_thenRetornaLoginExitosoComoEmpleado() {
            // Arrange
            LoginDto loginDto = new LoginDto("emp-1", "abcd", "1");
            Establecimiento establecimiento = crearEstablecimiento(1);
            when(establecimientoRepository.findById(1)).thenReturn(java.util.Optional.of(establecimiento));
            when(propietariosRepository.loginPropietario("emp-1", "abcd")).thenReturn(null);
            when(mesasRepository.findByFK_id_establecimiento(1)).thenReturn(Collections.emptyList());
            when(categoriasRepository.findByFk_id_establecimiento(1)).thenReturn(Collections.emptyList());

            Empleados empleado = new Empleados(3, "emp-1", 1, "Ana", "abcd", "mesero");
            when(empleadosRepository.loginEmpleado(1, "emp-1", "abcd")).thenReturn(empleado);
            when(jwtService.generarToken("emp-1", "mesero")).thenReturn("token-empleado");

            // Act
            ResponseEntity<LoginSuccesfulDto> response = loginServiceImp.login(loginDto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            LoginSuccesfulDto body = response.getBody();
            assertNotNull(body);
            assertEquals("emp-1", body.getNumero_identificacion());
            assertEquals("Ana", body.getNombre());
            assertEquals("mesero", body.getRol());
            assertEquals("token-empleado", body.getToken());
            assertTrue(body.getMesas().isEmpty());
        }

        @Test
        void givenCredencialesInvalidas_whenLogin_thenLanzaUsuarioNoEncontradoException() {
            // Arrange
            LoginDto loginDto = new LoginDto("desconocido", "wrong", "1");
            Establecimiento establecimiento = crearEstablecimiento(1);
            when(establecimientoRepository.findById(1)).thenReturn(java.util.Optional.of(establecimiento));
            when(propietariosRepository.loginPropietario("desconocido", "wrong")).thenReturn(null);
            when(mesasRepository.findByFK_id_establecimiento(1)).thenReturn(Collections.emptyList());
            when(categoriasRepository.findByFk_id_establecimiento(1)).thenReturn(Collections.emptyList());
            when(empleadosRepository.loginEmpleado(1, "desconocido", "wrong")).thenReturn(null);

            // Act & Assert
            assertThrows(UsuarioNoEncontradoException.class, () -> loginServiceImp.login(loginDto));
            verify(jwtService, never()).generarToken(anyString(), anyString());
        }
    }
}
