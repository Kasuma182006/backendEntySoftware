package com.entysoftware.aplication.service;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.customExceptions.EstablecimientoNoEncontradoException;
import com.entysoftware.aplication.customExceptions.UsuarioNoEncontradoException;
import com.entysoftware.aplication.model.dto.dto_entrada.LoginDto;

public interface LoginInterface {

    public ResponseEntity<?> ubicarEstablecimiento (String identificacion)throws UsuarioNoEncontradoException;
    public ResponseEntity<?> login(LoginDto usuario)throws UsuarioNoEncontradoException, EstablecimientoNoEncontradoException;
}
