package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.controller.validator.PrestamoValidator;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.PrestamoResultado;
import ar.edu.utn.frbb.tup.model.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.clientesException.ClienteNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.NoAlcanzaException;
import ar.edu.utn.frbb.tup.model.exception.prestamosException.PrestamoNotFoundException;
import ar.edu.utn.frbb.tup.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamo")
public class PrestamoController {

    @Autowired
    private PrestamoValidator prestamoValidator;

    @Autowired
    private PrestamoService prestamoService;

    @PostMapping
    public PrestamoResultado crearPrestamo(@RequestBody PrestamoDto prestamoDto) 
            throws CuentaNotFoundException, ClienteNotFoundException, TipoMonedaNoSoportadaException, CampoVacioException {
        prestamoValidator.validate(prestamoDto); 
        PrestamoResultado prestamoResultado = prestamoService.solicitarPrestamo(prestamoDto);
        return prestamoResultado;
    }

    @GetMapping("/{dni}")
    public List<Prestamo> buscarPrestamoPorDni(@PathVariable long dni) throws ClienteNotFoundException {
        return prestamoService.getPrestamosByCliente(dni);
    }

    @PostMapping("/{id}")
    public Prestamo pagarCuota(@PathVariable long id) 
            throws NoAlcanzaException, PrestamoNotFoundException, CuentaNotFoundException {
        return prestamoService.pagarCuota(id);
    }
}
