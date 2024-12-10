package ar.edu.utn.frbb.tup.model;

import java.util.List;

import ar.edu.utn.frbb.tup.model.enumsModels.EstadoPrestamo;

public class PrestamoResultado {
    private EstadoPrestamo estado;
    private String mensaje;
    private List<PlanPago> planPago;

    public PrestamoResultado() {
    }
    
    public PrestamoResultado(EstadoPrestamo estado, String mensaje, List<PlanPago> planPago) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.planPago = planPago;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setPlanPago(List<PlanPago> planPago) {
        this.planPago = planPago; 
    }    
}