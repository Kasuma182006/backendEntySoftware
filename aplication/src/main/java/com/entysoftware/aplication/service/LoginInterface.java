package com.entysoftware.aplication.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.loginDto.EstablecimientosDto;
import com.entysoftware.aplication.model.dto.loginDto.LoginDto;
import com.entysoftware.aplication.model.dto.loginDto.LoginSuccesfulDto;

public interface LoginInterface {

    public ResponseEntity<List<EstablecimientosDto>> ubicarEstablecimiento (String identificacion);
    public ResponseEntity<LoginSuccesfulDto> login(LoginDto usuario);
}
