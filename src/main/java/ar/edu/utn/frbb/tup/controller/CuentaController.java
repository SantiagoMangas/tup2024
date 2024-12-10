package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.TipoMonedaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.model.exception.clientesException.ClienteNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.*;
import ar.edu.utn.frbb.tup.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cuenta")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CuentaValidator cuentaValidator;

    @PostMapping
    public Cuenta crearCuenta(@RequestBody CuentaDto cuentaDto) 
            throws TipoCuentaNoSoportadaException, 
                   TipoMonedaNoSoportadaException, 
                   TipoCuentaAlreadyExistsException, 
                   ClienteNotFoundException, 
                   CuentaAlreadyExistsException, 
                   CampoVacioException {
        cuentaValidator.validate(cuentaDto);
        return cuentaService.darDeAltaCuenta(cuentaDto);
    }

    @GetMapping("/{numeroCuenta}")
    public Cuenta buscarCuenta(@PathVariable long numeroCuenta) throws CuentaNotFoundException {
        return cuentaService.find(numeroCuenta);
    }
}
