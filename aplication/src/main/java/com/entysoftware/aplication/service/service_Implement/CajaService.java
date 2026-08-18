package com.entysoftware.aplication.service.service_Implement;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.entysoftware.aplication.mapper.MapperEstablecimientoDto;
import com.entysoftware.aplication.model.BaseInicial;
import com.entysoftware.aplication.model.Establecimiento;
import com.entysoftware.aplication.model.dto.EstablecimientoEstadoCajaDto;
import com.entysoftware.aplication.model.dto.baseDia.BaseInicialDto;
import com.entysoftware.aplication.model.dto.baseDia.RespuestaBaseInicialDto;
import com.entysoftware.aplication.customExceptions.EstablecimientoNoEncontradoException;
import com.entysoftware.aplication.repository.BaseInicialRepository;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.service.CajaInterface;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CajaService implements CajaInterface {

    private final BaseInicialRepository baseInicialRepository;
    private final EstablecimientoRepository establecimientoRepository;
    private final MapperEstablecimientoDto mapperEstablecimientoDto;

    public CajaService (BaseInicialRepository baseInicialRepository, EstablecimientoRepository establecimientoRepository, MapperEstablecimientoDto mapperEstablecimientoDto){
        this.baseInicialRepository = baseInicialRepository;
        this.establecimientoRepository = establecimientoRepository;
        this.mapperEstablecimientoDto = mapperEstablecimientoDto;
    }

    public ResponseEntity<RespuestaBaseInicialDto> aperturaDia(BaseInicialDto base){
        LocalDate hoy = LocalDate.now();
        LocalTime hora = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);

        BaseInicial baseInicial = new BaseInicial(null,base.getIdEstablecimiento(),base.getValor(),hoy,hora);
        BaseInicial registroBase = baseInicialRepository.save(baseInicial);
        RespuestaBaseInicialDto respuesta = new RespuestaBaseInicialDto(true,registroBase.getValorBaseInicial(),registroBase.getHora(),registroBase.getFecha());
        log.debug("Se ha registrado la base del dia correctamente");
        return ResponseEntity.ok(respuesta);
    }

    public ResponseEntity<EstablecimientoEstadoCajaDto> establecimientoEstadoCaja(Integer idEstablecimiento){
        Establecimiento establecimiento = establecimientoRepository.findById(idEstablecimiento)
            .orElseThrow(() -> new EstablecimientoNoEncontradoException("No se ha encontrado el establecimiento con ID " + idEstablecimiento));
        EstablecimientoEstadoCajaDto respuesta = mapperEstablecimientoDto.establecimientoToEstadoCajaDto(establecimiento);
        log.debug("Se ha consultado el estado del establecimiento con ID {}", idEstablecimiento);
        return ResponseEntity.ok(respuesta);
    }

}
