package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.model.exception.TipoPersonaErroneoException;
import ar.edu.utn.frbb.tup.model.exception.clientesException.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.clientesException.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.model.exception.clientesException.ClienteNotFoundException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteControllerTest {

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ClienteValidator clienteValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearCliente_Success() throws TipoPersonaErroneoException, ClienteMenorDeEdadException, ClienteAlreadyExistsException, CampoVacioException {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setFechaNacimiento("2000-01-01");
        clienteDto.setTipoPersona("F");
        clienteDto.setBanco("Banco Prueba");

        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);

        doNothing().when(clienteValidator).validate(clienteDto);
        when(clienteService.darDeAltaCliente(clienteDto)).thenReturn(cliente);

        Cliente result = clienteController.crearCliente(clienteDto);

        assertNotNull(result);
        assertEquals(12345678L, result.getDni());
        verify(clienteValidator, times(1)).validate(clienteDto);
        verify(clienteService, times(1)).darDeAltaCliente(clienteDto);
    }

    @Test
    void testBuscarClientePorDni_Success() throws ClienteNotFoundException {
        long dni = 12345678L;
        Cliente cliente = new Cliente();
        cliente.setDni(dni);

        when(clienteService.buscarClientePorDni(dni)).thenReturn(cliente);

        Cliente result = clienteController.buscarClientePorDni(dni);

        assertNotNull(result);
        assertEquals(dni, result.getDni());
        verify(clienteService, times(1)).buscarClientePorDni(dni);
    }

    @Test
    void testBuscarClientePorDni_ThrowsClienteNotFoundException() throws ClienteNotFoundException {
        long dni = 12345678L;

        when(clienteService.buscarClientePorDni(dni)).thenThrow(new ClienteNotFoundException("El cliente no existe"));

        ClienteNotFoundException exception = assertThrows(ClienteNotFoundException.class, () -> clienteController.buscarClientePorDni(dni));
        assertEquals("El cliente no existe", exception.getMessage());
        verify(clienteService, times(1)).buscarClientePorDni(dni);
    }
}