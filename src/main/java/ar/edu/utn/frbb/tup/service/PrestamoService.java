package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.model.PlanPago;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.PrestamoResultado;
import ar.edu.utn.frbb.tup.model.enumsModels.EstadoPrestamo;
import ar.edu.utn.frbb.tup.model.exception.clientesException.ClienteNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.cuentasException.NoAlcanzaException;
import ar.edu.utn.frbb.tup.model.exception.prestamosException.PrestamoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.PrestamoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrestamoService {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private PrestamoDao prestamoDao;

    @Autowired
    private ScoreCreditService randomEstadoService;

    public PrestamoResultado solicitarPrestamo(PrestamoDto prestamoDto) throws CuentaNotFoundException, ClienteNotFoundException {
        if (prestamoDto.getMonto() <= 0 || prestamoDto.getPlazoMeses() <= 0) {
            throw new IllegalArgumentException("El monto y el plazo deben ser mayores a cero.");
        }
    
        Prestamo prestamo = new Prestamo(prestamoDto);
    
        if (!randomEstadoService.verifyEstado(prestamo.getNumeroCliente())) {
            PrestamoResultado resultado = new PrestamoResultado();
            resultado.setEstado(EstadoPrestamo.RECHAZADO);
            resultado.setMensaje("El cliente no tiene un crédito apto para solicitar un préstamo.");
            return resultado;
        }
    
        // Realiza las operaciones necesarias con cliente y cuenta
        clienteService.agregarPrestamo(prestamo, prestamo.getNumeroCliente());
        cuentaService.actualizarCuenta(prestamo);
        prestamoDao.save(prestamo);
    
        // Ya esta el monto con intereses calculado dentro de prestamo
        double montoConIntereses = prestamo.getMontoConIntereses();
    
        PrestamoResultado resultado = new PrestamoResultado();
        resultado.setEstado(EstadoPrestamo.APROBADO);
        resultado.setMensaje("El monto del préstamo fue acreditado en su cuenta.");
        resultado.setPlanPago(calcularPlanPago(prestamo.getPlazoMeses(), montoConIntereses));
    
        return resultado;
    }

    public List<Prestamo> getPrestamosByCliente(long dni) throws ClienteNotFoundException {
        clienteService.buscarClientePorDni(dni);
        return prestamoDao.getPrestamosByCliente(dni);
    }

    public Prestamo pagarCuota(long id) throws PrestamoNotFoundException, NoAlcanzaException, CuentaNotFoundException {
        Prestamo prestamo = prestamoDao.find(id);
        if (prestamo == null) {
            throw new PrestamoNotFoundException("El préstamo no existe");
        }
        if (prestamo.estaPagado()) {
            throw new IllegalStateException("El préstamo ya está completamente pagado");
        }
        cuentaService.pagarCuotaPrestamo(prestamo);
        prestamo.pagarCuota();
        prestamoDao.save(prestamo);
        return prestamo;
    }

    public List<PlanPago> calcularPlanPago(int plazoMeses, double montoConIntereses) {
        List<PlanPago> plan = new ArrayList<>();
        double montoCuota = montoConIntereses / plazoMeses;

        for (int i = 1; i <= plazoMeses; i++) {
            plan.add(new PlanPago(i, montoCuota));
        }

        return plan;
    }
}
