package ar.edu.utn.frbb.tup.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.PlanPago;
import ar.edu.utn.frbb.tup.model.PrestamoResultado;
import ar.edu.utn.frbb.tup.persistence.PrestamoDao;

@Service
public class PrestamoService {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private PrestamoDao prestamoDao;

    @Autowired
    private RandomEstadoService randomEstadoService;

    public PrestamoResultado solicitarPrestamo(PrestamoDto prestamoDto) 
        throws CuentaNotFoundException, ClienteNotFoundException {
            
        }

    public List<PlanPago> calcularPlanPago(int plazoMeses, double montoConIntereses) {
        List<PlanPago> plan = new ArrayList<>();
        double montoCuota = montoConIntereses / plazoMeses;

        for (int i = 1; i <= plazoMeses; i++) {
            plan.add(new PlanPago(i, montoCuota));
        }
        return plan;
    }
    //Esto genera una lista de objetos PlanPago para cada cuota. 
}
