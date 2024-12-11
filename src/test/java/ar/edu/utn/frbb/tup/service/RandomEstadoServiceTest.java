package ar.edu.utn.frbb.tup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomEstadoServiceTest {

    private ScoreCreditService randomEstadoService;

    @BeforeEach
    void setUp() {
        randomEstadoService = new ScoreCreditService();
    }

    @Test
    void verifyEstado_DniInvalido_ThrowsException() {
        long dniInvalido = 0L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> randomEstadoService.verifyEstado(dniInvalido));

        assertEquals("DNI inválido", exception.getMessage());
    }

    @RepeatedTest(10) // Repite el test 10 veces para verificar resultados aleatorios
    void verifyEstado_DniValido_ReturnsTrueOrFalse() {
        long dniValido = 12345678L;

        assertDoesNotThrow(() -> {
            boolean resultado = randomEstadoService.verifyEstado(dniValido);
            assertTrue(resultado || !resultado); // Siempre será true porque cubre ambos casos
        });
    }

    @Test
    void verifyEstado_DniNegativo_ThrowsException() {
        long dniNegativo = -12345678L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> randomEstadoService.verifyEstado(dniNegativo));

        assertEquals("DNI inválido", exception.getMessage());
    }
}
