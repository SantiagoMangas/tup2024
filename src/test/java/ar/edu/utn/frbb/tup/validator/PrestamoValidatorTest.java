package ar.edu.utn.frbb.tup.validator;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.controller.validator.PrestamoValidator;
import ar.edu.utn.frbb.tup.model.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNoSoportadaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrestamoValidatorTest {

    private PrestamoValidator prestamoValidator;

    @BeforeEach
    void setUp() {
        prestamoValidator = new PrestamoValidator();
    }

    @Test
    void validateDatosCompletos_Success() throws CampoVacioException {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(123456);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMonto(50000);
        prestamoDto.setMoneda("P");

        assertDoesNotThrow(() -> prestamoValidator.validateDatosCompletos(prestamoDto));
    }

    @Test
    void validateDatosCompletos_MissingNumeroCliente() {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(0); // Número de cliente inválido
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMonto(50000);
        prestamoDto.setMoneda("P");

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> prestamoValidator.validateDatosCompletos(prestamoDto));

        assertEquals("Error: Ingrese un número de cliente válido", exception.getMessage());
    }

    @Test
    void validateDatosCompletos_MissingMonto() {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(123456);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMonto(0); // Monto no válido
        prestamoDto.setMoneda("P");

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> prestamoValidator.validateDatosCompletos(prestamoDto));

        assertEquals("Error: Ingrese un monto de préstamo válido", exception.getMessage());
    }

    @Test
    void validateDatosCompletos_MissingMoneda() {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(123456);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMonto(50000);
        prestamoDto.setMoneda(""); // Moneda vacía

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> prestamoValidator.validateDatosCompletos(prestamoDto));

        assertEquals("Error: Ingrese una moneda válida", exception.getMessage());
    }

    @Test
    void validateMoneda_Success() throws TipoMonedaNoSoportadaException {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setMoneda("P");

        assertDoesNotThrow(() -> prestamoValidator.validateMoneda(prestamoDto));
    }

    @Test
    void validateMoneda_InvalidMoneda() {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setMoneda("E"); // Moneda no soportada

        TipoMonedaNoSoportadaException exception = assertThrows(TipoMonedaNoSoportadaException.class, 
            () -> prestamoValidator.validateMoneda(prestamoDto));

        assertEquals("El tipo de moneda no es correcto o es nulo", exception.getMessage());
    }

    @Test
    void validate_Success() throws CampoVacioException, TipoMonedaNoSoportadaException {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(123456);
        prestamoDto.setPlazoMeses(24);
        prestamoDto.setMonto(100000);
        prestamoDto.setMoneda("D");

        assertDoesNotThrow(() -> prestamoValidator.validate(prestamoDto));
    }

    @Test
    void validate_InvalidMoneda() {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(123456);
        prestamoDto.setPlazoMeses(24);
        prestamoDto.setMonto(100000);
        prestamoDto.setMoneda("X"); // Moneda inválida

        TipoMonedaNoSoportadaException exception = assertThrows(TipoMonedaNoSoportadaException.class, 
            () -> prestamoValidator.validate(prestamoDto));

        assertEquals("El tipo de moneda no es correcto o es nulo", exception.getMessage());
    }

    @Test
    void validate_MissingMonto() {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(123456);
        prestamoDto.setPlazoMeses(24);
        prestamoDto.setMonto(0); // Monto no válido
        prestamoDto.setMoneda("D");

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> prestamoValidator.validate(prestamoDto));

        assertEquals("Error: Ingrese un monto de préstamo válido", exception.getMessage());
    }
}
