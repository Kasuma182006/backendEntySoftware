package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.model.dto.loginDto.EstablecimientosDto;
import com.entysoftware.aplication.model.dto.loginDto.LoginDto;
import com.entysoftware.aplication.model.dto.loginDto.LoginSuccesfulDto;
import com.entysoftware.aplication.service.LoginInterface;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

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
    public ResponseEntity<List<EstablecimientosDto>> getMethodName(@PathVariable("idPersona") String idpersona) {

        log.debug("buscando establecimientos del siguiente id ... {} ",idpersona);

            
        return loginInterface.ubicarEstablecimiento(idpersona);

        
       
        
    }
    

    @PostMapping("/login")
    public ResponseEntity<LoginSuccesfulDto> login(@RequestBody LoginDto usuario) {
        
        log.debug("login entrante: {} ", usuario);

        
        return loginInterface.login(usuario);
        
       
    }
    

}
