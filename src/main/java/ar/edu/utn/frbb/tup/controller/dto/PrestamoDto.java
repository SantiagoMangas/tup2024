package ar.edu.utn.frbb.tup.controller.dto;

import java.math.BigDecimal;

public class PrestamoDto {
    private long numeroCliente;
    private BigDecimal monto;
    private int plazoMeses;
    private String moneda;

    public PrestamoDto() {
    }

    public PrestamoDto(long numeroCliente, BigDecimal monto, int plazoMeses, String moneda) {
        this.numeroCliente = numeroCliente;
        this.monto = monto;
        this.plazoMeses = plazoMeses;
        this.moneda = moneda;
    }

    public long getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(int plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
}
