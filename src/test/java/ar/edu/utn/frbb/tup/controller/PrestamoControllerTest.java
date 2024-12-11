package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.controller.validator.PrestamoValidator;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.PrestamoResultado;
import ar.edu.utn.frbb.tup.model.enumsModels.EstadoPrestamo;
import ar.edu.utn.frbb.tup.model.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.clientesException.ClienteNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.NoAlcanzaException;
import ar.edu.utn.frbb.tup.model.exception.prestamosException.PrestamoNotFoundException;
import ar.edu.utn.frbb.tup.service.PrestamoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrestamoControllerTest {

    @InjectMocks
    private PrestamoController prestamoController;

    @Mock
    private PrestamoValidator prestamoValidator;

    @Mock
    private PrestamoService prestamoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearPrestamo_Success() throws CampoVacioException, TipoMonedaNoSoportadaException, CuentaNotFoundException, ClienteNotFoundException {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(12345678L);
        prestamoDto.setMonto(100000);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMoneda("P");

        PrestamoResultado resultado = new PrestamoResultado();
        resultado.setEstado(EstadoPrestamo.APROBADO);
        resultado.setMensaje("El monto del préstamo fue acreditado en su cuenta.");

        doNothing().when(prestamoValidator).validate(prestamoDto);
        when(prestamoService.solicitarPrestamo(prestamoDto)).thenReturn(resultado);

        PrestamoResultado result = prestamoController.crearPrestamo(prestamoDto);

        assertNotNull(result);
        assertEquals(EstadoPrestamo.APROBADO, result.getEstado());
        verify(prestamoValidator, times(1)).validate(prestamoDto);
        verify(prestamoService, times(1)).solicitarPrestamo(prestamoDto);
    }

    @Test
    void testCrearPrestamo_ThrowsCampoVacioException() throws TipoMonedaNoSoportadaException, CampoVacioException, CuentaNotFoundException, ClienteNotFoundException {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setNumeroCliente(0); // Invalid client number

        doThrow(new CampoVacioException("Error: Ingrese un número de cliente válido")).when(prestamoValidator).validate(prestamoDto);

        CampoVacioException exception = assertThrows(CampoVacioException.class, () -> prestamoController.crearPrestamo(prestamoDto));
        assertEquals("Error: Ingrese un número de cliente válido", exception.getMessage());
        verify(prestamoValidator, times(1)).validate(prestamoDto);
        verify(prestamoService, never()).solicitarPrestamo(prestamoDto);
    }

    @Test
    void testBuscarPrestamoPorDni_Success() throws ClienteNotFoundException {
        long dni = 12345678L;
        List<Prestamo> prestamos = new ArrayList<>();
        Prestamo prestamo1 = new Prestamo();
        Prestamo prestamo2 = new Prestamo();
        prestamos.add(prestamo1);
        prestamos.add(prestamo2);

        when(prestamoService.getPrestamosByCliente(dni)).thenReturn(prestamos);

        
        List<Prestamo> result = prestamoController.buscarPrestamoPorDni(dni);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(prestamoService, times(1)).getPrestamosByCliente(dni);
    }

    @Test
    void testBuscarPrestamoPorDni_ThrowsClienteNotFoundException() throws ClienteNotFoundException {
        long dni = 12345678L;

        when(prestamoService.getPrestamosByCliente(dni)).thenThrow(new ClienteNotFoundException("Cliente no encontrado"));

        ClienteNotFoundException exception = assertThrows(ClienteNotFoundException.class, () -> prestamoController.buscarPrestamoPorDni(dni));
        assertEquals("Cliente no encontrado", exception.getMessage());
        verify(prestamoService, times(1)).getPrestamosByCliente(dni);
    }

    @Test
    void testPagarCuota_Success() throws PrestamoNotFoundException, NoAlcanzaException, CuentaNotFoundException {
        long prestamoId = 1L;
        Prestamo prestamo = new Prestamo();
        prestamo.setId(prestamoId);

        when(prestamoService.pagarCuota(prestamoId)).thenReturn(prestamo);

        
        Prestamo result = prestamoController.pagarCuota(prestamoId);

        assertNotNull(result);
        assertEquals(prestamoId, result.getId());
        verify(prestamoService, times(1)).pagarCuota(prestamoId);
    }

    @Test
    void testPagarCuota_ThrowsPrestamoNotFoundException() throws PrestamoNotFoundException, NoAlcanzaException, CuentaNotFoundException {
        long prestamoId = 1L;

        when(prestamoService.pagarCuota(prestamoId)).thenThrow(new PrestamoNotFoundException("El préstamo no existe"));

        PrestamoNotFoundException exception = assertThrows(PrestamoNotFoundException.class, () -> prestamoController.pagarCuota(prestamoId));
        assertEquals("El préstamo no existe", exception.getMessage());
        verify(prestamoService, times(1)).pagarCuota(prestamoId);
    }

    @Test
    void testPagarCuota_ThrowsNoAlcanzaException() throws PrestamoNotFoundException, NoAlcanzaException, CuentaNotFoundException {
        long prestamoId = 1L;

        when(prestamoService.pagarCuota(prestamoId)).thenThrow(new NoAlcanzaException("No hay suficiente saldo en la cuenta"));

        NoAlcanzaException exception = assertThrows(NoAlcanzaException.class, () -> prestamoController.pagarCuota(prestamoId));
        assertEquals("No hay suficiente saldo en la cuenta", exception.getMessage());
        verify(prestamoService, times(1)).pagarCuota(prestamoId);
    }
}