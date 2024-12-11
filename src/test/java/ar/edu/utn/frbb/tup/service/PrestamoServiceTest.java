package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.controller.validator.PrestamoValidator;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.PrestamoResultado;
import ar.edu.utn.frbb.tup.model.enumsModels.EstadoPrestamo;
import ar.edu.utn.frbb.tup.model.enumsModels.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.clientesException.ClienteNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.NoAlcanzaException;
import ar.edu.utn.frbb.tup.model.exception.prestamosException.PrestamoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.PrestamoDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PrestamoServiceTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private CuentaService cuentaService;

    @Mock
    private PrestamoDao prestamoDao;
    
    @Mock
    private ScoreCreditService randomEstadoService;

    @InjectMocks
    private PrestamoValidator prestamoValidator;    

    @InjectMocks
    private PrestamoService prestamoService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(prestamoService).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void solicitarPrestamo_Aprobado() throws ClienteNotFoundException, CuentaNotFoundException {
        PrestamoDto prestamoDto = new PrestamoDto(12345678L, 1000L, 12, "P");

        when(randomEstadoService.verifyEstado(prestamoDto.getNumeroCliente())).thenReturn(true);
        doNothing().when(clienteService).agregarPrestamo(any(Prestamo.class), anyLong());
        doNothing().when(cuentaService).actualizarCuenta(any(Prestamo.class));

        PrestamoResultado resultado = prestamoService.solicitarPrestamo(prestamoDto);

        assertNotNull(resultado);
        assertEquals(EstadoPrestamo.APROBADO, resultado.getEstado());
        assertNotNull(resultado.getPlanPago());
        verify(prestamoDao).save(any(Prestamo.class));
    }

    @Test
    void solicitarPrestamo_RechazadoPorEstado() throws CuentaNotFoundException, ClienteNotFoundException {
        PrestamoDto prestamoDto = new PrestamoDto(12345678L, 1000L, 12, "P");

        when(randomEstadoService.verifyEstado(prestamoDto.getNumeroCliente())).thenReturn(false);

        PrestamoResultado resultado = prestamoService.solicitarPrestamo(prestamoDto);

        assertNotNull(resultado);
        assertEquals(EstadoPrestamo.RECHAZADO, resultado.getEstado());
        verify(prestamoDao, never()).save(any(Prestamo.class));
    }

    @Test
    void pagarCuota_Exito() throws PrestamoNotFoundException, CuentaNotFoundException, NoAlcanzaException {
        Prestamo prestamo = new Prestamo(12345678L, 12, 1000L, TipoMoneda.PESOS);
        prestamo.setId(1L);

        when(prestamoDao.find(1L)).thenReturn(prestamo);
        doNothing().when(cuentaService).pagarCuotaPrestamo(prestamo);

        Prestamo resultado = prestamoService.pagarCuota(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.getCuotasPagas());
        verify(prestamoDao).save(prestamo);
    }

    @Test
    void pagarCuota_PrestamoNoEncontrado() {
        when(prestamoDao.find(1L)).thenReturn(null);

        assertThrows(PrestamoNotFoundException.class, () -> prestamoService.pagarCuota(1L));
    }

    @Test
    void getPrestamosByCliente_Success() throws ClienteNotFoundException {
        long dni = 12345678L;
        Prestamo prestamo = new Prestamo(dni, 12, 1000L, TipoMoneda.PESOS);

        when(clienteService.buscarClientePorDni(dni)).thenReturn(null);
        when(prestamoDao.getPrestamosByCliente(dni)).thenReturn(Collections.singletonList(prestamo));

        List<Prestamo> prestamos = prestamoService.getPrestamosByCliente(dni);

        assertNotNull(prestamos);
        assertEquals(1, prestamos.size());
        assertEquals(dni, prestamos.get(0).getNumeroCliente());
    }
}
