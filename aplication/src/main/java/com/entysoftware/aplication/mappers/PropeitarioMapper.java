package com.entysoftware.aplication.mappers;

import org.springframework.stereotype.Component;

import com.entysoftware.aplication.model.Propietarios;
import com.entysoftware.aplication.model.dto_entrada.LoginDto;

@Component
public class PropeitarioMapper{
    public Propietarios dtoToPropietario(LoginDto propietario){
        return new Propietarios(propietario.getIdentificacion(),null,propietario.getPassword(),null,null,null);
    }

    public LoginDto propietariosToDTO(Propietarios propeitario){
        return new LoginDto(null, null,null);
    }
}
