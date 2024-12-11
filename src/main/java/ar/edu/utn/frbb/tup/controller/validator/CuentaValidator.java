package ar.edu.utn.frbb.tup.controller.validator;

import org.springframework.stereotype.Component;
import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.TipoCuentaNoSoportadaException;

@Component
public class CuentaValidator {

    public void validate(CuentaDto cuentaDto) throws TipoCuentaNoSoportadaException, TipoMonedaNoSoportadaException, CampoVacioException {
        validateDatosCompletos(cuentaDto);
        validateTipoCuenta(cuentaDto);
        validateMoneda(cuentaDto);
    }

    public void validateTipoCuenta(CuentaDto cuentaDto) throws TipoCuentaNoSoportadaException {
        if (!"C".equals(cuentaDto.getTipoCuenta()) && !"A".equals(cuentaDto.getTipoCuenta())) {
            throw new TipoCuentaNoSoportadaException("El tipo de cuenta no es correcto");
        }
    }
    
    public void validateMoneda(CuentaDto cuentaDto) {
        if (!"P".equals(cuentaDto.getMoneda()) && !"D".equals(cuentaDto.getMoneda())) {
            throw new IllegalArgumentException("El tipo de moneda no es correcto");
        }
    }
    public void validateDatosCompletos(CuentaDto cuentaDto) throws CampoVacioException {
        if (cuentaDto.getTipoCuenta() == null || cuentaDto.getTipoCuenta().isEmpty()) { throw new CampoVacioException("Error: Ingrese un tipo de cuenta ");}
        if (cuentaDto.getMoneda() == null || cuentaDto.getMoneda().isEmpty()) { throw new CampoVacioException("Error: Ingrese una moneda");}
        if (cuentaDto.getDniTitular() == 0) { throw new CampoVacioException("Error: Ingrese un dni");}
    }
}
