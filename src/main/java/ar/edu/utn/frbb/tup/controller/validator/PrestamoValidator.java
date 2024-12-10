package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.CampoVacioException;
import org.springframework.stereotype.Component;

@Component
public class PrestamoValidator {

    // Validar que el monto no esté vacío o negativo
    public void validate(PrestamoDto prestamoDto) throws TipoMonedaNoSoportadaException, CampoVacioException {
        validateDatosCompletos(prestamoDto);
        validateMoneda(prestamoDto);
    }

    // Validar que el tipo de moneda esté soportado
    public void validateMoneda(PrestamoDto prestamoDto) throws TipoMonedaNoSoportadaException {
        if ((!"P".equals(prestamoDto.getMoneda()) && !"D".equals(prestamoDto.getMoneda()))) {
            throw new TipoMonedaNoSoportadaException("El tipo de moneda no es correcto o es nulo");
        }
    }

    // Validar otros campos
    public void validateDatosCompletos(PrestamoDto prestamoDto) throws CampoVacioException, TipoMonedaNoSoportadaException {
        // número de cliente
        if (prestamoDto.getNumeroCliente() <= 0) {
            throw new CampoVacioException("Error: Ingrese un número de cliente válido");
        }

        //plazo en meses
        if (prestamoDto.getPlazoMeses() <= 0) {
            throw new CampoVacioException("Error: Ingrese un plazo en meses válido");
        }

        // monto de préstamo
        if (prestamoDto.getMonto() <= 0) {
            throw new CampoVacioException("Error: Ingrese un monto de préstamo válido");
        }

        // moneda
        if (!"P".equalsIgnoreCase(prestamoDto.getMoneda()) && !"D".equalsIgnoreCase(prestamoDto.getMoneda())) {
            throw new TipoMonedaNoSoportadaException("El tipo de moneda no es correcto o es nulo");
        }
    }
}