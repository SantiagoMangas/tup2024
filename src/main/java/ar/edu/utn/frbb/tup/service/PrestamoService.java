package ar.edu.utn.frbb.tup.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.model.PlanPago;

@Service
public class PrestamoService {


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
