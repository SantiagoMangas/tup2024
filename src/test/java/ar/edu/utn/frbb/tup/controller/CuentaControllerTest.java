package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.clientesException.ClienteNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CuentaControllerTest {

    @InjectMocks
    private CuentaController cuentaController;

    @Mock
    private CuentaService cuentaService;

    @Mock
    private CuentaValidator cuentaValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearCuenta_Success() throws TipoCuentaNoSoportadaException, TipoMonedaNoSoportadaException, 
            TipoCuentaAlreadyExistsException, ClienteNotFoundException, CuentaAlreadyExistsException, CampoVacioException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("P");
        cuentaDto.setDniTitular(12345678L);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(10001L);

        doNothing().when(cuentaValidator).validate(cuentaDto);
        when(cuentaService.darDeAltaCuenta(cuentaDto)).thenReturn(cuenta);

        Cuenta result = cuentaController.crearCuenta(cuentaDto);

        assertNotNull(result);
        assertEquals(10001L, result.getNumeroCuenta());
        verify(cuentaValidator, times(1)).validate(cuentaDto);
        verify(cuentaService, times(1)).darDeAltaCuenta(cuentaDto);
    }

    @Test
    void testCrearCuenta_ThrowsTipoCuentaNoSoportadaException() throws TipoCuentaNoSoportadaException, TipoMonedaNoSoportadaException, CampoVacioException, CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, ClienteNotFoundException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("X");
        cuentaDto.setMoneda("P");
        cuentaDto.setDniTitular(12345678L);

        doThrow(new TipoCuentaNoSoportadaException("El tipo de cuenta no es correcto")).when(cuentaValidator).validate(cuentaDto);

        TipoCuentaNoSoportadaException exception = assertThrows(TipoCuentaNoSoportadaException.class, () -> cuentaController.crearCuenta(cuentaDto));
        assertEquals("El tipo de cuenta no es correcto", exception.getMessage());
        verify(cuentaValidator, times(1)).validate(cuentaDto);
        verify(cuentaService, never()).darDeAltaCuenta(cuentaDto);
    }

    @Test
    void testCrearCuenta_ThrowsCampoVacioException() throws TipoCuentaNoSoportadaException, TipoMonedaNoSoportadaException, CampoVacioException, CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, ClienteNotFoundException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("");
        cuentaDto.setDniTitular(0);

        doThrow(new CampoVacioException("Error: Ingrese una moneda")).when(cuentaValidator).validate(cuentaDto);

        CampoVacioException exception = assertThrows(CampoVacioException.class, () -> cuentaController.crearCuenta(cuentaDto));
        assertEquals("Error: Ingrese una moneda", exception.getMessage());
        verify(cuentaValidator, times(1)).validate(cuentaDto);
        verify(cuentaService, never()).darDeAltaCuenta(cuentaDto);
    }

    @Test
    void testBuscarCuenta_Success() throws CuentaNotFoundException {
        long numeroCuenta = 10001L;
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(numeroCuenta);

        when(cuentaService.find(numeroCuenta)).thenReturn(cuenta);

        Cuenta result = cuentaController.buscarCuenta(numeroCuenta);

        assertNotNull(result);
        assertEquals(numeroCuenta, result.getNumeroCuenta());
        verify(cuentaService, times(1)).find(numeroCuenta);
    }

    @Test
    void testBuscarCuenta_ThrowsCuentaNotFoundException() throws CuentaNotFoundException {
        long numeroCuenta = 10001L;

        when(cuentaService.find(numeroCuenta)).thenThrow(new CuentaNotFoundException("La cuenta no existe"));

        CuentaNotFoundException exception = assertThrows(CuentaNotFoundException.class, () -> cuentaController.buscarCuenta(numeroCuenta));
        assertEquals("La cuenta no existe", exception.getMessage());
        verify(cuentaService, times(1)).find(numeroCuenta);
    }
}