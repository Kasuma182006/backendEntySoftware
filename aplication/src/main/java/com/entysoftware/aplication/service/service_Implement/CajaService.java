package com.entysoftware.aplication.service.service_Implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.model.BaseInicial;
import com.entysoftware.aplication.model.dto.BaseInicialDto;
import com.entysoftware.aplication.repository.BaseInicialRepository;
import com.entysoftware.aplication.service.CajaInterface;

@Service
public class CajaService implements CajaInterface {
    
    @Autowired
    private BaseInicialRepository baseInicialRepository;
    public ResponseEntity<String> aperturaDia(BaseInicialDto base){

        BaseInicial baseInicial = new BaseInicial(null,base.getFk_id_establecimiento(),base.getValor(),null);
        baseInicialRepository.save(baseInicial);

        return ResponseEntity.ok("La base inicial se ha guardado Correctamente");
    } 

}
