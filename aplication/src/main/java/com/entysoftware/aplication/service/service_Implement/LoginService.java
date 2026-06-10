package com.entysoftware.aplication.service.service_Implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.entysoftware.aplication.model.dto.dto_entrada.LoginDto;
import com.entysoftware.aplication.model.dto.dto_salida.EstablecimientosDto;
import com.entysoftware.aplication.model.dto.dto_salida.LoginSuccesfulDto;
import com.entysoftware.aplication.repository.CategoriasRepository;
import com.entysoftware.aplication.repository.EmpleadosRepository;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.repository.InventarioRepository;
import com.entysoftware.aplication.repository.MesasRepository;
import com.entysoftware.aplication.repository.PropietariosRepository;
import com.entysoftware.aplication.service.LoginInterface;


@Service
public class LoginService implements LoginInterface {
    
    
    @Autowired
    private EstablecimientoRepository establecimientoRepository;
    @Autowired
    private EmpleadosRepository empleadosrepository;
    @Autowired
    private PropietariosRepository propietariosRepository;
    @Autowired
    private MesasRepository mesasRepository;
    @Autowired 
    private CategoriasRepository categoriasRepository;
    @Autowired
    private InventarioRepository inventarioRepository;


    public ResponseEntity<?> ubicarEstablecimiento(String identificacion)throws UsuarioNoEncontradoException{
        
        List<Establecimiento> listaEstablecimientosPropietario = establecimientoRepository.buscarEstablecimiento(identificacion);
        if(listaEstablecimientosPropietario.isEmpty()){
            List<Empleados> listaEstablecimientoEmpleado = empleadosrepository.establecimientosEmpleados(identificacion);
            if(listaEstablecimientoEmpleado.isEmpty()) throw new UsuarioNoEncontradoException("No se han encontrado coincidencias");
            List<Integer> listaEstablecimientosId = listaEstablecimientoEmpleado.stream()
                                                                                    .map(Empleados::getFK_id_establecimiento)
                                                                                    .toList();                        
          
            List<Establecimiento> establecimientosRecogidosDeLaTablaEstablecimiento = establecimientoRepository.findAllById(listaEstablecimientosId);
            
            List<EstablecimientosDto> listaDtoEstablecimiento = establecimientosRecogidosDeLaTablaEstablecimiento.stream()
                                                                .map(dto -> new EstablecimientosDto(dto.getNombre_establecimiento(),dto.getId_establecimiento()))
                                                                .toList();
                                                                
            return ResponseEntity.ok().body(listaDtoEstablecimiento);



            
        }

        List<EstablecimientosDto> establecimientosDelPropietarioDto = listaEstablecimientosPropietario.stream()
                                                                    .map(dto -> new EstablecimientosDto(dto.getNombre_establecimiento(),dto.getId_establecimiento()))
                                                                    .toList();


        return ResponseEntity.ok().body(establecimientosDelPropietarioDto);
    }


    public ResponseEntity<?> login(LoginDto usuario)throws UsuarioNoEncontradoException, EstablecimientoNoEncontradoException{
        Integer idEstablecimiento = Integer.valueOf(usuario.getId_establecimiento());

        Establecimiento establecimientoSeleccionado = establecimientoRepository.findById(idEstablecimiento).orElseThrow(()-> new EstablecimientoNoEncontradoException("No se ha encontrado el ID del establecimiento"));
        Propietarios propietario = propietariosRepository.loginPropietario(usuario.getIdentificacion(),usuario.getPassword());
        List<Mesas> listaMesas = mesasRepository.findByFK_id_establecimiento(establecimientoSeleccionado.getId_establecimiento());
        List<Categorias> listaCategorias = categoriasRepository.findByFk_id_establecimiento(establecimientoSeleccionado.getId_establecimiento());
        List<Inventario> listaInventario = listaCategorias.stream()
                                                           .flatMap(categoria -> inventarioRepository.findByFK_categoria(categoria.getId()).stream())
                                                           .toList(); 
        
        if (propietario == null){
            Empleados empleado = empleadosrepository.loginEmpleado(idEstablecimiento,usuario.getIdentificacion(),usuario.getPassword());
            if(empleado == null)throw new UsuarioNoEncontradoException("No se han encontrado resultados");
           
            LoginSuccesfulDto dtoLogin = new LoginSuccesfulDto(empleado.getNumero_identificacion(),empleado.getNombre(),empleado.getRol(),establecimientoSeleccionado.getId_establecimiento(),establecimientoSeleccionado.getEstado_establecimiento(),establecimientoSeleccionado.getNombre_establecimiento(),listaMesas,listaCategorias,listaInventario); 
            return ResponseEntity.ok(dtoLogin);
        }

       
        LoginSuccesfulDto dtoLogin = new LoginSuccesfulDto(propietario.getId_propietario(),propietario.getNombre(),"administrador", establecimientoSeleccionado.getId_establecimiento(), establecimientoSeleccionado.getEstado_establecimiento(), establecimientoSeleccionado.getNombre_establecimiento(),listaMesas,listaCategorias,listaInventario);

        return ResponseEntity.ok(dtoLogin);
    }

}
