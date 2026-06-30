package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.customExceptions.EstablecimientoNoEncontradoException;
import com.entysoftware.aplication.customExceptions.UsuarioNoEncontradoException;
import com.entysoftware.aplication.model.dto.loginDto.LoginDto;
import com.entysoftware.aplication.service.LoginInterface;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/")
@Slf4j
public class LoginController {

    private LoginInterface loginInterface;
    public LoginController(LoginInterface loginInterface){
        this.loginInterface = loginInterface;
    }
    
    @GetMapping("/buscar-establecimiento/{idPersona}")
    public ResponseEntity<?> getMethodName(@PathVariable("idPersona") String idpersona) {

        log.debug("buscando establecimientos del siguiente id ... {} ",idpersona);
        try{

            
            return loginInterface.ubicarEstablecimiento(idpersona);

        }
        catch(UsuarioNoEncontradoException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
        
    }
    

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto usuario) {
        
        log.debug("login entrante: {} ", usuario);

        try{
            return loginInterface.login(usuario);
        }
        catch(UsuarioNoEncontradoException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch(EstablecimientoNoEncontradoException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    

}
