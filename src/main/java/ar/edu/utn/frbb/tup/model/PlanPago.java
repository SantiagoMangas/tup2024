package ar.edu.utn.frbb.tup.model;

public class PlanPago {
    private int cuotaNro;
    private double cuotaMonto;

    public PlanPago(int cuotaNro, double cuotaMonto) {
        this.cuotaNro = cuotaNro;
        this.cuotaMonto = cuotaMonto;
    }

    public int getCuotaNro() {
        return cuotaNro;
    }

    public void setCuotaNro(int cuotaNro) {
        this.cuotaNro = cuotaNro;
    }

    public double getCuotaMonto() {
        return cuotaMonto;
    }

    public void setCuotaMonto(double cuotaMonto) {
        this.cuotaMonto = cuotaMonto;
    }
}
