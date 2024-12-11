package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.enumsModels.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enumsModels.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.clientesException.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.clientesException.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.clientesException.ClienteNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void darDeAltaCliente_Success() throws ClienteAlreadyExistsException, ClienteMenorDeEdadException {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(12345678L);
        clienteDto.setNombre("John");
        clienteDto.setApellido("Doe");
        clienteDto.setFechaNacimiento("1990-01-01");
        clienteDto.setTipoPersona("F");
        clienteDto.setBanco("Banco Test");

        when(clienteDao.find(12345678L, false)).thenReturn(null);

        Cliente result = clienteService.darDeAltaCliente(clienteDto);

        assertNotNull(result);
        assertEquals(12345678L, result.getDni());
        assertEquals("John", result.getNombre());
        assertEquals("Doe", result.getApellido());
        assertEquals(LocalDate.of(1990, 1, 1), result.getFechaNacimiento());

        verify(clienteDao).save(any(Cliente.class));
    }

    @Test
    void darDeAltaCliente_ClienteAlreadyExists() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(12345678L);
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Pérez");
        clienteDto.setFechaNacimiento("1990-01-01");
        clienteDto.setTipoPersona("F");
        clienteDto.setBanco("BancoTest");

        Cliente clienteExistente = new Cliente(clienteDto);

        when(clienteDao.find(12345678L, false)).thenReturn(clienteExistente);

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(clienteDto));
    }

    @Test
    void darDeAltaCliente_ClienteMenorDeEdad() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(12345678L);
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Pérez");
        clienteDto.setFechaNacimiento("2010-01-01");
        clienteDto.setTipoPersona("F");
        clienteDto.setBanco("BancoTest");

        when(clienteDao.find(12345678L, false)).thenReturn(null);

        assertThrows(ClienteMenorDeEdadException.class, () -> clienteService.darDeAltaCliente(clienteDto));
    }

    @Test
    void agregarCuenta_Success() throws TipoCuentaAlreadyExistsException, ClienteNotFoundException {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);

        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);

        when(clienteDao.find(12345678L, true)).thenReturn(cliente);

        clienteService.agregarCuenta(cuenta, 12345678L);

        assertTrue(cliente.getCuentas().contains(cuenta));
        verify(clienteDao).save(cliente);
    }

    @Test
    void buscarClientePorDni_Success() throws ClienteNotFoundException {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);

        when(clienteDao.find(12345678L, true)).thenReturn(cliente);

        Cliente result = clienteService.buscarClientePorDni(12345678L);

        assertNotNull(result);
        assertEquals(12345678L, result.getDni());
    }

    @Test
    void buscarClientePorDni_ClienteNotFound() {
        when(clienteDao.find(12345678L, true)).thenReturn(null);

        assertThrows(ClienteNotFoundException.class, () -> clienteService.buscarClientePorDni(12345678L));
    }

    @Test
    void agregarCuenta_TipoCuentaAlreadyExists() {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);
        Cuenta cuentaExistente = new Cuenta();
        cuentaExistente.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuentaExistente.setMoneda(TipoMoneda.PESOS);
        cliente.addCuenta(cuentaExistente);

        Cuenta nuevaCuenta = new Cuenta();
        nuevaCuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        nuevaCuenta.setMoneda(TipoMoneda.PESOS);

        when(clienteDao.find(12345678L, true)).thenReturn(cliente);

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.agregarCuenta(nuevaCuenta, 12345678L));
    }

    @Test
    void agregarCuenta_ClienteNotFound() {
        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);

        when(clienteDao.find(12345678L, true)).thenReturn(null);

        assertThrows(ClienteNotFoundException.class, () -> clienteService.agregarCuenta(cuenta, 12345678L));
    }

    @Test
    void agregarPrestamo_Success() throws ClienteNotFoundException, CuentaNotFoundException {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);
        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cliente.addCuenta(cuenta);

        Prestamo prestamo = new Prestamo();
        prestamo.setMoneda(TipoMoneda.PESOS);

        when(clienteDao.find(12345678L, true)).thenReturn(cliente);

        clienteService.agregarPrestamo(prestamo, 12345678L);

        assertTrue(cliente.getPrestamos().contains(prestamo));
        verify(clienteDao).save(cliente);
    }

    @Test
    void agregarPrestamo_ClienteNotFound() {
        Prestamo prestamo = new Prestamo();
        prestamo.setMoneda(TipoMoneda.PESOS);

        when(clienteDao.find(12345678L, true)).thenReturn(null);

        assertThrows(ClienteNotFoundException.class, () -> clienteService.agregarPrestamo(prestamo, 12345678L));
    }

    @Test
    void agregarPrestamo_CuentaNotFound() {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);

        Prestamo prestamo = new Prestamo();
        prestamo.setMoneda(TipoMoneda.DOLARES);

        when(clienteDao.find(12345678L, true)).thenReturn(cliente);

        assertThrows(CuentaNotFoundException.class, () -> clienteService.agregarPrestamo(prestamo, 12345678L));
    }

    @Test
    void darDeAltaCliente_EdgeCase_18YearsOld() throws ClienteAlreadyExistsException, ClienteMenorDeEdadException {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(12345678L);
        clienteDto.setNombre("John");
        clienteDto.setApellido("Doe");
        clienteDto.setFechaNacimiento(LocalDate.now().minusYears(18).toString());
        clienteDto.setTipoPersona("F");
        clienteDto.setBanco("Banco Test");

        when(clienteDao.find(12345678L, false)).thenReturn(null);

        Cliente result = clienteService.darDeAltaCliente(clienteDto);

        assertNotNull(result);
        assertEquals(18, result.getEdad());
    }

    @Test
    void agregarCuenta_MaximumAccounts() throws TipoCuentaAlreadyExistsException, ClienteNotFoundException {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);

        cliente.addCuenta(new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS));
        cliente.addCuenta(new Cuenta(TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.DOLARES));

        Cuenta nuevaCuenta = new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.DOLARES);

        when(clienteDao.find(12345678L, true)).thenReturn(cliente);

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.agregarCuenta(nuevaCuenta, 12345678L));
    }

    @Test
    void darDeAltaCliente_EdgeCase_17YearsAnd364Days() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(12345678L);
        clienteDto.setNombre("Alice");
        clienteDto.setApellido("Young");
        clienteDto.setFechaNacimiento(LocalDate.now().minusYears(18).plusDays(1).toString());
        clienteDto.setTipoPersona("F");
        clienteDto.setBanco("Banco Test");

        when(clienteDao.find(12345678L, false)).thenReturn(null);

        assertThrows(ClienteMenorDeEdadException.class, () -> clienteService.darDeAltaCliente(clienteDto));
    }

    @Test
    void agregarCuenta_DifferentCurrencySameType() throws TipoCuentaAlreadyExistsException, ClienteNotFoundException {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);
        Cuenta cuentaPesos = new Cuenta();
        cuentaPesos.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuentaPesos.setMoneda(TipoMoneda.PESOS);
        cliente.addCuenta(cuentaPesos);

        Cuenta cuentaDolares = new Cuenta();
        cuentaDolares.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuentaDolares.setMoneda(TipoMoneda.DOLARES);

        when(clienteDao.find(12345678L, true)).thenReturn(cliente);

        clienteService.agregarCuenta(cuentaDolares, 12345678L);

        assertTrue(cliente.getCuentas().contains(cuentaDolares));
        verify(clienteDao).save(cliente);
    }

    @Test
    void agregarPrestamo_MultipleLoans() throws ClienteNotFoundException, CuentaNotFoundException {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);
        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cliente.addCuenta(cuenta);

        Prestamo prestamo1 = new Prestamo();
        prestamo1.setMoneda(TipoMoneda.PESOS);
        Prestamo prestamo2 = new Prestamo();
        prestamo2.setMoneda(TipoMoneda.PESOS);

        when(clienteDao.find(12345678L, true)).thenReturn(cliente);

        clienteService.agregarPrestamo(prestamo1, 12345678L);
        clienteService.agregarPrestamo(prestamo2, 12345678L);

        assertEquals(2, cliente.getPrestamos().size());
        assertTrue(cliente.getPrestamos().contains(prestamo1));
        assertTrue(cliente.getPrestamos().contains(prestamo2));
        verify(clienteDao, times(2)).save(cliente);
    }

    @Test
    void buscarClientePorDni_NonExistentClient() {
        when(clienteDao.find(99999999L, true)).thenReturn(null);

        assertThrows(ClienteNotFoundException.class, () -> clienteService.buscarClientePorDni(99999999L));
    }

    @Test
    void agregarCuenta_MaximumAccountsReached() {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);
        cliente.addCuenta(new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.PESOS));
        cliente.addCuenta(new Cuenta(TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.PESOS));
        cliente.addCuenta(new Cuenta(TipoCuenta.CAJA_AHORRO, TipoMoneda.DOLARES));

        Cuenta nuevaCuenta = new Cuenta(TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.DOLARES);

        when(clienteDao.find(12345678L, true)).thenReturn(cliente);

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.agregarCuenta(nuevaCuenta, 12345678L));
    }
}