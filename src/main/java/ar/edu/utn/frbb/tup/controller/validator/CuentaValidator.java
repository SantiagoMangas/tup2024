package ar.edu.utn.frbb.tup.controller.validator;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;

@Component
public class CuentaValidator {

    public void validate(CuentaDto cuentaDto) {
        validateTipoCuenta(cuentaDto);
        validateTipoMoneda(cuentaDto);
    }

    private void validateTipoCuenta(CuentaDto cuentaDto) {
        if (!"C".equals(cuentaDto.getTipoCuenta()) || !"A".equals(cuentaDto.getTipoCuenta())) {
            throw new IllegalArgumentException("El tipo de cuenta no es correcto");
        } 
    }

    private void validateTipoMoneda(CuentaDto cuentaDto) {
        if (!"P".equals(cuentaDto.getTipoCuenta()) || !"D".equals(cuentaDto.getTipoCuenta())) {
            throw new IllegalArgumentException("El tipo de moneda no es correcto");
        }
    }
}
