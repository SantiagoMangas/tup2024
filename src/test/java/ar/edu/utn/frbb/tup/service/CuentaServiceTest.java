package ar.edu.utn.frbb.tup.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaServiceTest {
    // 1. Cuenta existente → debería fallar con la exception indicada
    // 2. Cuenta no soportada → debería fallar con una exception que deben generar
    // 3. Cliente ya tiene cuenta de ese tipo → debería fallar en este caso el cliente service (qué debe hacerse en para esto?)
    // 4. Cuenta creada exitosamente → debería verificarse que todas nuestras
    // dependencias fueran invocadas exitosamente
    //Chequear cuentas soportadas por el banco CA$ CC$ CAU$S
        // if (!tipoCuentaEstaSoportada(cuenta)) {...}
    @Mock
    private ClienteDao clienteDao;

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    Cuenta cuenta;

    @InjectMocks
    CuentaService cuentaService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCuentaExistente() throws TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException, CuentaAlreadyExistsException {
        Cliente mangas = new Cliente();
        mangas.setDni(45607866);
        mangas.setNombre("Santiago");
        mangas.setApellido("Mangas");
        mangas.setFechaNacimiento(LocalDate.of(2005, 1,24));
        mangas.setTipoPersona(TipoPersona.PERSONA_FISICA);
        
        when(clienteDao.find(45607866, true)).thenReturn(mangas);

        Cuenta cuenta = new Cuenta()
            .setMoneda(TipoMoneda.PESOS)
            .setBalance(24000)
            .setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        
        cuentaService.darDeAltaCuenta(cuenta, mangas.getDni());

        Cuenta cuenta2 = new Cuenta()
           .setMoneda(TipoMoneda.PESOS)
           .setBalance(24000)
           .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuenta2, mangas.getDni()));
        verify(clienteDao, times(1)).save(mangas);
        assertEquals(1, mangas.getCuentas().size());
        assertEquals(mangas, cuenta.getTitular());
    }

    @Test
    public void testCuentaNoSoportada() throws TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException, CuentaAlreadyExistsException {
        Cliente mangas = new Cliente();
        mangas.setDni(45607866);
        mangas.setNombre("Santiago");
        mangas.setApellido("Mangas");
        mangas.setFechaNacimiento(LocalDate.of(2005, 1, 24));
        mangas.setTipoPersona(TipoPersona.PERSONA_FISICA);

        when(clienteDao.find(45607866, true)).thenReturn(mangas);

        Cuenta cuentaCCUSD = new Cuenta()
                .setMoneda(TipoMoneda.DOLARES)
                .setBalance(24000)
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        cuentaService.darDeAltaCuenta(cuentaCCUSD, mangas.getDni());

        Cuenta cuentaCAPESOS = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(24000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        cuentaService.darDeAltaCuenta(cuentaCAPESOS, mangas.getDni());

        assertThrows(TipoCuentaNoSoportadaException.class, () -> cuentaService.darDeAltaCuenta(cuentaCCUSD, mangas.getDni()));
        verify(clienteDao, times(1)).save(mangas);
        assertEquals(1, mangas.getCuentas().size());
        assertEquals(mangas, cuentaCAPESOS.getTitular());
    }

    @Test 
    public void testClienteYaTieneCuentaDeEseTipo() throws TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException, CuentaAlreadyExistsException {
        Cliente mangas = new Cliente();
        mangas.setDni(45607866);
        mangas.setNombre("Santiago");
        mangas.setApellido("Mangas");
        mangas.setFechaNacimiento(LocalDate.of(2005, 1, 24));
        mangas.setTipoPersona(TipoPersona.PERSONA_FISICA);

        when(clienteDao.find(45607866, true)).thenReturn(mangas);

        Cuenta cuenta = new Cuenta()
            .setMoneda(TipoMoneda.PESOS)
            .setBalance(24000)
            .setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        
        clienteService.agregarCuenta(cuenta, mangas.getDni());

        Cuenta cuenta2 = new Cuenta()
            .setMoneda(TipoMoneda.PESOS)
            .setBalance(24000)
            .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        clienteService.agregarCuenta(cuenta, mangas.getDni());

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.agregarCuenta(cuenta2, mangas.getDni()));
        verify(clienteDao, times(1)).save(mangas);
        assertEquals(1, mangas.getCuentas().size());
        assertEquals(mangas, cuenta.getTitular());
    }
    @Test
    public void testCuentaCreadaExitosamente() throws TipoCuentaNoSoportadaException, TipoCuentaAlreadyExistsException, CuentaAlreadyExistsException {
        Cliente santi = new Cliente();
        santi.setNombre("Santi");
        santi.setApellido("Mangas");
        santi.setDni(45607866);
        santi.setFechaNacimiento(LocalDate.of(2005, 1, 24));
        santi.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta();
            cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
            cuenta.setMoneda(TipoMoneda.PESOS);
            cuenta.setBalance(3000);

        when(clienteDao.find(45607866, true)).thenReturn(santi);

        cuentaService.darDeAltaCuenta(cuenta, santi.getDni());

        verify(clienteService, times(1)).agregarCuenta(cuenta, santi.getDni());
        verify(clienteDao, times(1)).save(santi);
    }
}
