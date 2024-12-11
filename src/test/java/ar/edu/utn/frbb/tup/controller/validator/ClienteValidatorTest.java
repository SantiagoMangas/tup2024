package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.model.exception.TipoPersonaErroneoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteValidatorTest {

    private ClienteValidator clienteValidator;

    @BeforeEach
    void setUp() {
        clienteValidator = new ClienteValidator();
    }

    @Test
    void validateDatosCompletos_Success() throws CampoVacioException {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("John");
        clienteDto.setApellido("Doe");
        clienteDto.setFechaNacimiento("1990-01-01");
        clienteDto.setBanco("Banco Test");
        clienteDto.setTipoPersona("F");

        assertDoesNotThrow(() -> clienteValidator.validateDatosCompletos(clienteDto));
    }

    @Test
    void validateDatosCompletos_MissingFields() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre(""); 

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> clienteValidator.validateDatosCompletos(clienteDto));

        assertEquals("Error: Ingrese un nombre", exception.getMessage());
    }

    @Test
    void validateTipoPersona_Success() throws TipoPersonaErroneoException {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setTipoPersona("F");

        assertDoesNotThrow(() -> clienteValidator.validateTipoPersona(clienteDto));
    }

    @Test
    void validateTipoPersona_InvalidType() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setTipoPersona("X");

        TipoPersonaErroneoException exception = assertThrows(TipoPersonaErroneoException.class, 
            () -> clienteValidator.validateTipoPersona(clienteDto));

        assertEquals("Error: El tipo de persona no es correcto", exception.getMessage());
    }

    @Test
    void validateFechaNacimiento_Success() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setFechaNacimiento("2000-01-01");

        assertDoesNotThrow(() -> clienteValidator.validateFechaNacimiento(clienteDto));
    }

    @Test
    void validateFechaNacimiento_InvalidFormat() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setFechaNacimiento("invalid-date");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> clienteValidator.validateFechaNacimiento(clienteDto));

        assertEquals("Error en el formato de fecha", exception.getMessage());
    }

    @Test
    void validate_InvalidTypePersona() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setTipoPersona("Z");
        clienteDto.setFechaNacimiento("2000-01-01");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> clienteValidator.validate(clienteDto));

        assertEquals("El tipo de persona no es correcto", exception.getMessage());
    }
}
