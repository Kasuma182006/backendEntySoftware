package com.entysoftware.aplication.service.service_Implement;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.entysoftware.aplication.model.BaseInicial;
import com.entysoftware.aplication.model.dto.baseDia.BaseInicialDto;
import com.entysoftware.aplication.model.dto.baseDia.RespuestaBaseInicialDto;
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
    
    public ResponseEntity<RespuestaBaseInicialDto> aperturaDia(BaseInicialDto base){
        LocalDate hoy = LocalDate.now();
        LocalTime hora = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);

        /*if (baseInicialRepository.validarExistenciaBaseInicial(base.getFk_id_establecimiento(), hoy) != null) {
            
            throw new BaseYaRegistrada("Ya se encuentra una base registrada en el día de hoy");

        }*/

        BaseInicial baseInicial = new BaseInicial(null,base.getIdEstablecimiento(),base.getValor(),hoy,hora);
        BaseInicial registroBase = baseInicialRepository.save(baseInicial);
        RespuestaBaseInicialDto respuesta = new RespuestaBaseInicialDto(true,registroBase.getValorBaseInicial(),registroBase.getHora(),registroBase.getFecha());
        log.debug("Se ha registrado la base del dia correctamente");
        return ResponseEntity.ok(respuesta);
    } 

}
