package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.CuentaController;
import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.enumsModels.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enumsModels.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.*;
import ar.edu.utn.frbb.tup.model.exception.clientesException.ClienteNotFoundException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CuentaServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private ClienteService clienteService;

    @Mock
    private CuentaValidator cuentaValidator; 

    @InjectMocks
    private CuentaService cuentaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void darDeAltaCuenta_Success() throws CuentaAlreadyExistsException, TipoCuentaNoSoportadaException, TipoCuentaAlreadyExistsException, ClienteNotFoundException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(12345678L);
        cuentaDto.setTipoCuenta("A");
        cuentaDto.setMoneda("P");

        when(cuentaDao.find(anyLong())).thenReturn(null);
        doNothing().when(clienteService).agregarCuenta(any(Cuenta.class), anyLong());

        Cuenta result = cuentaService.darDeAltaCuenta(cuentaDto);

        assertNotNull(result);
        assertEquals(TipoCuenta.CAJA_AHORRO, result.getTipoCuenta());
        assertEquals(TipoMoneda.PESOS, result.getMoneda());
        assertEquals(12345678L, result.getTitular());

        verify(cuentaDao).save(any(Cuenta.class));
    }

    @Test
    void darDeAltaCuenta_CuentaAlreadyExists() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(12345678L);
        cuentaDto.setTipoCuenta("A");
        cuentaDto.setMoneda("P");

        when(cuentaDao.find(anyLong())).thenReturn(new Cuenta());

        assertThrows(CuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
    }

    @Test
    void darDeAltaCuenta_TipoCuentaNoSoportada() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(12345678L);
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("D");

        when(cuentaDao.find(anyLong())).thenReturn(null);

        assertThrows(TipoCuentaNoSoportadaException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
    }


    @Test
    void actualizarCuenta_Success() throws CuentaNotFoundException, NoAlcanzaException {
        Prestamo prestamo = new Prestamo();
        prestamo.setMontoPedido(1000L);
        prestamo.setMoneda(TipoMoneda.PESOS);

        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(500L);
        cuenta.setMoneda(TipoMoneda.PESOS);

        when(cuentaDao.buscarPorMoneda(TipoMoneda.PESOS)).thenReturn(cuenta);

        cuentaService.actualizarCuenta(prestamo);

        assertEquals(1500L, cuenta.getBalance());
        verify(cuentaDao).save(cuenta);
    }

    @Test
    void find_Success() throws CuentaNotFoundException {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123L);

        when(cuentaDao.find(123L)).thenReturn(cuenta);

        Cuenta result = cuentaService.find(123L);

        assertNotNull(result);
        assertEquals(123L, result.getNumeroCuenta());
    }

    @Test
    void find_CuentaNotFound() {
        when(cuentaDao.find(123L)).thenReturn(null);

        assertThrows(CuentaNotFoundException.class, () -> cuentaService.find(123L));
    }

    @Test
    void pagarCuotaPrestamo_Success() throws NoAlcanzaException, CuentaNotFoundException {
        Prestamo prestamo = new Prestamo();
        prestamo.setValorCuota(100L);
        prestamo.setMoneda(TipoMoneda.PESOS);

        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(500L);
        cuenta.setMoneda(TipoMoneda.PESOS);

        when(cuentaDao.buscarPorMoneda(TipoMoneda.PESOS)).thenReturn(cuenta);

        cuentaService.pagarCuotaPrestamo(prestamo);

        assertEquals(400L, cuenta.getBalance());
        verify(cuentaDao).save(cuenta);
    }

    @Test
    void pagarCuotaPrestamo_NoAlcanza() {
        Prestamo prestamo = new Prestamo();
        prestamo.setValorCuota(1000L);
        prestamo.setMoneda(TipoMoneda.PESOS);

        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(500L);
        cuenta.setMoneda(TipoMoneda.PESOS);

        when(cuentaDao.buscarPorMoneda(TipoMoneda.PESOS)).thenReturn(cuenta);

        assertThrows(NoAlcanzaException.class, () -> cuentaService.pagarCuotaPrestamo(prestamo));
    }

    @Test
    void darDeAltaCuenta_CuentaCorrientePesos_Success() throws Exception {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(12345678L);
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("P");

        when(cuentaDao.find(anyLong())).thenReturn(null);
        doNothing().when(clienteService).agregarCuenta(any(Cuenta.class), anyLong());

        Cuenta result = cuentaService.darDeAltaCuenta(cuentaDto);

        assertNotNull(result);
        assertEquals(TipoCuenta.CUENTA_CORRIENTE, result.getTipoCuenta());
        assertEquals(TipoMoneda.PESOS, result.getMoneda());
        assertEquals(12345678L, result.getTitular());

        verify(cuentaDao).save(any(Cuenta.class));
    }

    @Test
    void darDeAltaCuenta_CajaAhorroDolares_Success() throws Exception {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(12345678L);
        cuentaDto.setTipoCuenta("A");
        cuentaDto.setMoneda("D");

        when(cuentaDao.find(anyLong())).thenReturn(null);
        doNothing().when(clienteService).agregarCuenta(any(Cuenta.class), anyLong());

        Cuenta result = cuentaService.darDeAltaCuenta(cuentaDto);

        assertNotNull(result);
        assertEquals(TipoCuenta.CAJA_AHORRO, result.getTipoCuenta());
        assertEquals(TipoMoneda.DOLARES, result.getMoneda());
        assertEquals(12345678L, result.getTitular());

        verify(cuentaDao).save(any(Cuenta.class));
    }

    @Test
    void darDeAltaCuenta_CuentaCorrienteDolares_ThrowsTipoCuentaNoSoportadaException() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(12345678L);
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("D");

        when(cuentaDao.find(anyLong())).thenReturn(null);

        assertThrows(TipoCuentaNoSoportadaException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
    }

    @Test
    void tipoCuentaEstaSoportada_ValidCombinations_ReturnsTrue() {
        Cuenta cuentaCorrientePesos = new Cuenta(TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.PESOS);
        Cuenta cajaAhorroPesos = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS);
        Cuenta cajaAhorroDolares = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.DOLARES);

        assertTrue(cuentaService.tipoCuentaEstaSoportada(cuentaCorrientePesos));
        assertTrue(cuentaService.tipoCuentaEstaSoportada(cajaAhorroPesos));
        assertTrue(cuentaService.tipoCuentaEstaSoportada(cajaAhorroDolares));
    }

    @Test
    void tipoCuentaEstaSoportada_InvalidCombinations_ReturnsFalse() {
        Cuenta cuentaCorrienteDolares = new Cuenta(TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.DOLARES);

        assertFalse(cuentaService.tipoCuentaEstaSoportada(cuentaCorrienteDolares));
    }

    @Test
    void buscarPorMoneda_CuentaExists_Returnscuenta() throws CuentaNotFoundException {
        TipoMoneda moneda = TipoMoneda.PESOS;
        Cuenta expectedCuenta = new Cuenta(TipoCuenta.CAJA_AHORRO, moneda);

        when(cuentaDao.buscarPorMoneda(moneda)).thenReturn(expectedCuenta);

        Cuenta result = cuentaService.buscarPorMoneda(moneda);

        assertNotNull(result);
        assertEquals(expectedCuenta, result);
    }

    @Test
    void buscarPorMoneda_CuentaNotFound_ThrowsCuentaNotFoundException() {
        TipoMoneda moneda = TipoMoneda.DOLARES;

        when(cuentaDao.buscarPorMoneda(moneda)).thenReturn(null);

        assertThrows(CuentaNotFoundException.class, () -> cuentaService.buscarPorMoneda(moneda));
    }

    @Test
    void pagarCuotaPrestamo_SufficientBalance_Success() throws NoAlcanzaException, CuentaNotFoundException {
        Prestamo prestamo = new Prestamo();
        prestamo.setValorCuota(100L);
        prestamo.setMoneda(TipoMoneda.PESOS);

        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(500L);
        cuenta.setMoneda(TipoMoneda.PESOS);

        when(cuentaDao.buscarPorMoneda(TipoMoneda.PESOS)).thenReturn(cuenta);

        cuentaService.pagarCuotaPrestamo(prestamo);

        assertEquals(400L, cuenta.getBalance());
        verify(cuentaDao).save(cuenta);
    }

    @Test
    void pagarCuotaPrestamo_InsufficientBalance_ThrowsNoAlcanzaException() {
        Prestamo prestamo = new Prestamo();
        prestamo.setValorCuota(600L);
        prestamo.setMoneda(TipoMoneda.PESOS);

        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(500L);
        cuenta.setMoneda(TipoMoneda.PESOS);

        when(cuentaDao.buscarPorMoneda(TipoMoneda.PESOS)).thenReturn(cuenta);

        assertThrows(NoAlcanzaException.class, () -> cuentaService.pagarCuotaPrestamo(prestamo));
    }

    @Test
    void darDeAltaCuenta_InvalidMoneda() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(12345678L);
        cuentaDto.setTipoCuenta("A");
        cuentaDto.setMoneda("E");

        assertThrows(IllegalArgumentException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
    }

    @Test
    void darDeAltaCuenta_ClienteNotFound() throws ClienteNotFoundException, TipoCuentaAlreadyExistsException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(12345678L);
        cuentaDto.setTipoCuenta("A");
        cuentaDto.setMoneda("P");

        when(cuentaDao.find(anyLong())).thenReturn(null);
        doThrow(new ClienteNotFoundException("Cliente no encontrado")).when(clienteService).agregarCuenta(any(Cuenta.class), anyLong());

        assertThrows(ClienteNotFoundException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
    }

    @Test
    void darDeAltaCuenta_TipoCuentaAlreadyExists() throws ClienteNotFoundException, TipoCuentaAlreadyExistsException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(12345678L);
        cuentaDto.setTipoCuenta("A");
        cuentaDto.setMoneda("P");

        when(cuentaDao.find(anyLong())).thenReturn(null);
        doThrow(new TipoCuentaAlreadyExistsException("El cliente ya posee una cuenta de ese tipo y moneda")).when(clienteService).agregarCuenta(any(Cuenta.class), anyLong());

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
    }

    @Test
    void actualizarCuenta_CuentaNotFound() {
        Prestamo prestamo = new Prestamo();
        prestamo.setMontoPedido(1000L);
        prestamo.setMoneda(TipoMoneda.PESOS);

        when(cuentaDao.buscarPorMoneda(TipoMoneda.PESOS)).thenReturn(null);

        assertThrows(CuentaNotFoundException.class, () -> cuentaService.actualizarCuenta(prestamo));
    }

    @Test
    void pagarCuotaPrestamo_ExactAmount() throws NoAlcanzaException, CuentaNotFoundException {
        Prestamo prestamo = new Prestamo();
        prestamo.setValorCuota(500L);
        prestamo.setMoneda(TipoMoneda.PESOS);

        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(500L);
        cuenta.setMoneda(TipoMoneda.PESOS);

        when(cuentaDao.buscarPorMoneda(TipoMoneda.PESOS)).thenReturn(cuenta);

        cuentaService.pagarCuotaPrestamo(prestamo);

        assertEquals(0L, cuenta.getBalance());
        verify(cuentaDao).save(cuenta);
    }

    @Test
    void tipoCuentaEstaSoportada_AllCombinations() {
        assertTrue(cuentaService.tipoCuentaEstaSoportada(new Cuenta(TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.PESOS)));
        assertTrue(cuentaService.tipoCuentaEstaSoportada(new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS)));
        assertTrue(cuentaService.tipoCuentaEstaSoportada(new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.DOLARES)));
        assertFalse(cuentaService.tipoCuentaEstaSoportada(new Cuenta(TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.DOLARES)));
    }
}