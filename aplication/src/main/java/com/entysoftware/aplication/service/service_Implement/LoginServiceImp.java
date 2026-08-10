package com.entysoftware.aplication.service.service_Implement;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
import com.entysoftware.aplication.service.LoginInterface;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class LoginServiceImp implements LoginInterface {

    private static final String ROL_ADMINISTRADOR = "administrador";

    private final EstablecimientoRepository establecimientoRepository;

    private final EmpleadosRepository empleadosrepository;

    private final PropietariosRepository propietariosRepository;

    private final MesasRepository mesasRepository;

    private final CategoriasRepository categoriasRepository;

    private final InventarioRepository inventarioRepository;

    private final JwtService jwtService;

    public LoginServiceImp(EstablecimientoRepository establecimientoRepository, EmpleadosRepository empleadosRepository, PropietariosRepository propietariosRepository,MesasRepository mesasRepository, CategoriasRepository categoriasRepository,InventarioRepository inventarioRepository, JwtService jwtService){
        this.establecimientoRepository = establecimientoRepository;
        this.empleadosrepository = empleadosRepository;
        this.propietariosRepository = propietariosRepository;
        this.mesasRepository = mesasRepository;
        this.categoriasRepository = categoriasRepository;
        this.inventarioRepository = inventarioRepository;
        this.jwtService = jwtService;
    }

    @SuppressWarnings("null")
    public ResponseEntity<List<EstablecimientosDto>> ubicarEstablecimiento(String identificacion){

        List<Establecimiento> listaEstablecimientosPropietario = establecimientoRepository.buscarEstablecimiento(identificacion);
        if (!listaEstablecimientosPropietario.isEmpty()) {
            List<EstablecimientosDto> establecimientosDelPropietarioDto = mapearEstablecimientosADto(listaEstablecimientosPropietario);
            log.debug("Lista de establecimientos del propietario:{} ",establecimientosDelPropietarioDto);
            return ResponseEntity.ok().body(establecimientosDelPropietarioDto);
        }

        List<Empleados> listaEstablecimientoEmpleado = empleadosrepository.establecimientosEmpleados(identificacion);
        if(listaEstablecimientoEmpleado.isEmpty()) throw new UsuarioNoEncontradoException("No se han encontrado coincidencias");

        List<Integer> listaEstablecimientosId = listaEstablecimientoEmpleado.stream()
                                                                                .map(Empleados::getIdEstablecimiento)
                                                                                .toList();

        List<Establecimiento> establecimientosRecogidosDeLaTablaEstablecimiento = establecimientoRepository.findAllById(listaEstablecimientosId);

        List<EstablecimientosDto> listaDtoEstablecimiento = mapearEstablecimientosADto(establecimientosRecogidosDeLaTablaEstablecimiento);

        log.debug("Lista de establecimientos de empleado:{} ",listaDtoEstablecimiento);
        return ResponseEntity.ok().body(listaDtoEstablecimiento);
    }

    private List<EstablecimientosDto> mapearEstablecimientosADto(List<Establecimiento> establecimientos){
        return establecimientos.stream()
                                .map(establecimiento -> new EstablecimientosDto(establecimiento.getNombreEstablecimiento(), establecimiento.getIdEstablecimiento()))
                                .toList();
    }

    @SuppressWarnings("null")
    public ResponseEntity<LoginSuccesfulDto> login(LoginDto usuario){
        Integer idEstablecimiento = Integer.valueOf(usuario.getId_establecimiento());

        Establecimiento establecimientoSeleccionado = establecimientoRepository.findById(idEstablecimiento).orElseThrow(()-> new EstablecimientoNoEncontradoException("No se ha encontrado el ID del establecimiento"));
        Propietarios propietario = propietariosRepository.loginPropietario(usuario.getIdentificacion(),usuario.getPassword());
        List<Mesas> listaMesas = mesasRepository.findByFK_id_establecimiento(establecimientoSeleccionado.getIdEstablecimiento());
        List<Categorias> listaCategorias = categoriasRepository.findByFk_id_establecimiento(establecimientoSeleccionado.getIdEstablecimiento());
        List<Inventario> listaInventario = listaCategorias.stream()
                                                           .flatMap(categoria -> inventarioRepository.findByFK_categoria(categoria.getId()).stream())
                                                           .toList();

        if (propietario == null){
            Empleados empleado = empleadosrepository.loginEmpleado(idEstablecimiento,usuario.getIdentificacion(),usuario.getPassword());
            if(empleado == null)throw new UsuarioNoEncontradoException("No se han encontrado resultados");

            String token = jwtService.generarToken(empleado.getNumeroIdentificacion(),empleado.getRol());
            LoginSuccesfulDto dtoLogin = construirRespuestaLogin(empleado.getNumeroIdentificacion(),empleado.getNombre(),empleado.getRol(),establecimientoSeleccionado,listaMesas,listaCategorias,listaInventario,token);
            log.debug("Login exitoso: {}",dtoLogin.getNombre());
            return ResponseEntity.ok(dtoLogin);
        }

        String token = jwtService.generarToken(propietario.getIdPropietario(),ROL_ADMINISTRADOR);
        LoginSuccesfulDto dtoLogin = construirRespuestaLogin(propietario.getIdPropietario(),propietario.getNombre(),ROL_ADMINISTRADOR,establecimientoSeleccionado,listaMesas,listaCategorias,listaInventario,token);
        log.debug("Login exitoso: {}",dtoLogin.getNombre());
        return ResponseEntity.ok(dtoLogin);
    }

    private LoginSuccesfulDto construirRespuestaLogin(String numeroIdentificacion, String nombre, String rol, Establecimiento establecimiento, List<Mesas> listaMesas, List<Categorias> listaCategorias, List<Inventario> listaInventario, String token){
        return new LoginSuccesfulDto(numeroIdentificacion,nombre,rol,establecimiento.getIdEstablecimiento(),establecimiento.getEstadoEstablecimiento(),establecimiento.getNombreEstablecimiento(),listaMesas,listaCategorias,listaInventario,token);
    }

}
