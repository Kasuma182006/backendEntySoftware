package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.customExceptions.EstablecimientoNoEncontradoException;
import com.entysoftware.aplication.customExceptions.UsuarioNoEncontradoException;
import com.entysoftware.aplication.model.dto_entrada.LoginDto;
import com.entysoftware.aplication.service.LoginInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/")
public class LoginController {

    private LoginInterface loginInterface;
    public LoginController(LoginInterface loginInterface){
        this.loginInterface = loginInterface;
    }
    
    @GetMapping("/buscar-establecimiento/{idPersona}")
    public ResponseEntity<?> getMethodName(@PathVariable("idPersona") String idpersona) {
        try{

            return loginInterface.ubicarEstablecimiento(idpersona);
        }
        catch(UsuarioNoEncontradoException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
        
    }
    

    @PostMapping("/login")
    public ResponseEntity<?> loginAdministrador(@RequestBody LoginDto usuario) {
        
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
