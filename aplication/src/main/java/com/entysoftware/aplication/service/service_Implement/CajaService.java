package com.entysoftware.aplication.service.service_Implement;

import java.time.LocalDate;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.customExceptions.BaseYaRegistrada;
import com.entysoftware.aplication.model.BaseInicial;
import com.entysoftware.aplication.model.dto.BaseInicialDto;
import com.entysoftware.aplication.repository.BaseInicialRepository;
import com.entysoftware.aplication.service.CajaInterface;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CajaService implements CajaInterface {
    
    private final BaseInicialRepository baseInicialRepository;

    public CajaService (BaseInicialRepository baseInicialRepository){
        this.baseInicialRepository = baseInicialRepository;
    }
    
    public ResponseEntity<String> aperturaDia(BaseInicialDto base){
        LocalDate hoy = LocalDate.now();
        

        if (baseInicialRepository.validarExistenciaBaseInicial(base.getFk_id_establecimiento(), hoy) != null) {
            
            throw new BaseYaRegistrada("Ya se encuentra una base registrada en el día de hoy");

        }

        BaseInicial baseInicial = new BaseInicial(null,base.getFk_id_establecimiento(),base.getValor(),hoy);
        baseInicialRepository.save(baseInicial);
        log.debug("Se ha registrado la base del dia correctamente");
        return ResponseEntity.ok("La base inicial se ha guardado Correctamente");
    } 

}
