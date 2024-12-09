package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.enumsModels.EstadoPrestamo;

public class PrestamoResultado {
    private EstadoPrestamo estado;
    private String mensaje;
    private PlanPago planPago;

    public PrestamoResultado() {
    }
    public PrestamoResultado(EstadoPrestamo estado, String mensaje, PlanPago planPago) {
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

    public PlanPago getPlanPago() {
        return planPago;
    }

    public void setPlanPago(int cuotaNro, double cuotaMonto) {
        this.planPago = new PlanPago(cuotaNro, cuotaMonto);
    }
}