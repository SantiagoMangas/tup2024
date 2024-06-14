package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testClienteMenor18Años() {
        Cliente clienteMenorDeEdad = new Cliente();
        clienteMenorDeEdad.setFechaNacimiento(LocalDate.of(2020, 2, 7));
        assertThrows(IllegalArgumentException.class, () -> clienteService.darDeAltaCliente(clienteMenorDeEdad));
    }

    @Test
    public void testClienteSuccess() throws ClienteAlreadyExistsException {
        Cliente cliente = new Cliente();
        cliente.setFechaNacimiento(LocalDate.of(1978,3,25));
        cliente.setDni(29857643);
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);
        clienteService.darDeAltaCliente(cliente);

        verify(clienteDao, times(1)).save(cliente);
    }

    @Test
    public void testClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456437);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        when(clienteDao.find(26456437, false)).thenReturn(new Cliente());

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(pepeRino));
    }

    @Test
    public void testAgregarCuentaAClienteSuccess() throws TipoCuentaAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456439);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, true)).thenReturn(pepeRino);

        clienteService.agregarCuenta(cuenta, pepeRino.getDni());

        verify(clienteDao, times(1)).save(pepeRino);

        assertEquals(1, pepeRino.getCuentas().size());
        assertEquals(pepeRino, cuenta.getTitular());

    }


    @Test
    public void testAgregarCuentaAClienteDuplicada() throws TipoCuentaAlreadyExistsException {
        Cliente luciano = new Cliente();
        luciano.setDni(26456439);
        luciano.setNombre("Pepe");
        luciano.setApellido("Rino");
        luciano.setFechaNacimiento(LocalDate.of(1978, 3,25));
        luciano.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, true)).thenReturn(luciano);

        clienteService.agregarCuenta(cuenta, luciano.getDni());

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.agregarCuenta(cuenta2, luciano.getDni()));
        verify(clienteDao, times(1)).save(luciano);
        assertEquals(1, luciano.getCuentas().size());
        assertEquals(luciano, cuenta.getTitular());

    }

    @Test
    public void testAgregarCACC() throws TipoCuentaAlreadyExistsException {
        Cliente santiago = new Cliente();
        santiago.setDni(45607866);
        santiago.setNombre("Santiago");
        santiago.setApellido("Mangas");
        santiago.setFechaNacimiento(LocalDate.of(2005, 1,24));
        santiago.setTipoPersona(TipoPersona.PERSONA_FISICA);

        when(clienteDao.find(45607866, true)).thenReturn(santiago);

        Cuenta cuentaCA = new Cuenta()
            .setMoneda(TipoMoneda.PESOS)
            .setBalance(20000)
            .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        clienteService.agregarCuenta(cuentaCA, santiago.getDni());
        
        Cuenta cuentaCC = new Cuenta()
            .setMoneda(TipoMoneda.PESOS)
            .setBalance(20000)
            .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        
        clienteService.agregarCuenta(cuentaCC, santiago.getDni());
            
        verify(clienteDao, times(2)).save(santiago);
        assertEquals(2, santiago.getCuentas().size());
        assertEquals(santiago, cuentaCA.getTitular());
        assertEquals(santiago, cuentaCC.getTitular()); 
        assertFalse(true, "Fail on purpose");
        //assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(santiago));
    }

    @Test
    public void testAgregarCACAU() throws TipoCuentaAlreadyExistsException {
        Cliente santiago = new Cliente();
        santiago.setDni(45607866);
        santiago.setNombre("Santiago");
        santiago.setApellido("Mangas");
        santiago.setFechaNacimiento(LocalDate.of(2005, 1,24));
        santiago.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuentaCA = new Cuenta()
            .setMoneda(TipoMoneda.PESOS)
            .setBalance(20000)
            .setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        Cuenta cuentaCAU = new Cuenta()
            .setMoneda(TipoMoneda.DOLARES)
            .setBalance(20000)
            .setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        
        clienteService.agregarCuenta(cuentaCA, santiago.getDni());
        clienteService.agregarCuenta(cuentaCAU, santiago.getDni());
                
        verify(clienteDao, times(2)).save(santiago);
        assertEquals(2, santiago.getCuentas().size());
        assertEquals(santiago, cuentaCA.getTitular());
        assertEquals(santiago, cuentaCAU.getTitular()); 
    }

    @Test
    public void testBuscarPorDniCasoExito(){
        Cliente santiago = new Cliente();
        santiago.setDni(45607866);
        santiago.setNombre("Santiago");
        santiago.setApellido("Mangas");
        santiago.setFechaNacimiento(LocalDate.of(2005, 1, 24));
        santiago.setTipoPersona(TipoPersona.PERSONA_FISICA);

        when(clienteDao.find(45607866,true)).thenReturn(santiago);
        
        Cliente clienteTest = clienteService.buscarClientePorDni(45607866);

        assertEquals(santiago, clienteTest);
    }

    @Test
    public void testBuscarPorDniCasoFalla(){

        when(clienteDao.find(45607866,true)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> clienteService.buscarClientePorDni(45607866));
    } 
}