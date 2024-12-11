package ar.edu.utn.frbb.tup.validator;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.TipoCuentaNoSoportadaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CuentaValidatorTest {

    private CuentaValidator cuentaValidator;

    @BeforeEach
    void setUp() {
        cuentaValidator = new CuentaValidator();
    }

    @Test
    void validateDatosCompletos_Success() throws CampoVacioException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("P");
        cuentaDto.setDniTitular(12345678L);

        assertDoesNotThrow(() -> cuentaValidator.validateDatosCompletos(cuentaDto));
    }

    @Test
    void validateDatosCompletos_MissingFields() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta(""); // Falta el tipo de cuenta

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> cuentaValidator.validateDatosCompletos(cuentaDto));

        assertEquals("Error: Ingrese un tipo de cuenta ", exception.getMessage());
    }

    @Test
    void validateTipoCuenta_Success() throws TipoCuentaNoSoportadaException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("A"); // Tipo válido: "A" = Ahorro

        assertDoesNotThrow(() -> cuentaValidator.validateTipoCuenta(cuentaDto));
    }

    @Test
    void validateTipoCuenta_InvalidType() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("X"); // Tipo inválido

        TipoCuentaNoSoportadaException exception = assertThrows(TipoCuentaNoSoportadaException.class, 
            () -> cuentaValidator.validateTipoCuenta(cuentaDto));

        assertEquals("El tipo de cuenta no es correcto", exception.getMessage());
    }

    @Test
    void validateMoneda_Success() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setMoneda("D"); // Moneda válida: "D" = Dólares

        assertDoesNotThrow(() -> cuentaValidator.validateMoneda(cuentaDto));
    }

    @Test
    void validateMoneda_InvalidCurrency() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setMoneda("E"); // Moneda inválida

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> cuentaValidator.validateMoneda(cuentaDto));

        assertEquals("El tipo de moneda no es correcto", exception.getMessage());
    }

    @Test
    void validate_Success() throws TipoCuentaNoSoportadaException, TipoMonedaNoSoportadaException, CampoVacioException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("P");
        cuentaDto.setDniTitular(12345678L);

        assertDoesNotThrow(() -> cuentaValidator.validate(cuentaDto));
    }

    @Test
    void validate_InvalidTipoCuenta() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("X"); // Tipo cuenta inválido
        cuentaDto.setMoneda("P");
        cuentaDto.setDniTitular(12345678L);

        TipoCuentaNoSoportadaException exception = assertThrows(TipoCuentaNoSoportadaException.class, 
            () -> cuentaValidator.validate(cuentaDto));

        assertEquals("El tipo de cuenta no es correcto", exception.getMessage());
    }

    @Test
    void validate_InvalidMoneda() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("E"); // Moneda inválida
        cuentaDto.setDniTitular(12345678L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> cuentaValidator.validate(cuentaDto));

        assertEquals("El tipo de moneda no es correcto", exception.getMessage());
    }

    @Test
    void validate_MissingDniTitular() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("P");
        cuentaDto.setDniTitular(0); // DniTitular no válido

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> cuentaValidator.validate(cuentaDto));

        assertEquals("Error: Ingrese un dni", exception.getMessage());
    }
}
