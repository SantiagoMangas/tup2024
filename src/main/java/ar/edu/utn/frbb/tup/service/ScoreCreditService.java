package ar.edu.utn.frbb.tup.service;

import org.springframework.stereotype.Service;
import java.util.Random;

/**
 * Simula un sistema de calificación crediticia.
 * Devuelve `false` aproximadamente 1/3 de las veces para indicar
 * que el cliente tiene una mala calificación crediticia.
 */

@Service
public class ScoreCreditService {
    private final Random random = new Random();

    public boolean verifyEstado(long dni) {
        if (dni <= 0) {
            throw new IllegalArgumentException("DNI inválido");
        }
        return random.nextDouble() >= 1.0 / 3.0;
    }
}