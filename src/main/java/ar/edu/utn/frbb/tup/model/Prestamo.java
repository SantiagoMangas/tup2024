package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.enumsModels.TipoMoneda;

public class Prestamo {
    private long id;
    private long numeroCliente;
    private int plazoMeses;
    private long montoPedido;
    private long montoConIntereses;
    private long saldoRestante; // Saldo deudor a pagar
    private long valorCuota;
    private int cuotasPagas;
    private TipoMoneda moneda;

    public Prestamo() {
    }

    public Prestamo(long numeroCliente, int plazoMeses, long montoPedido, TipoMoneda moneda) {
        this.numeroCliente = numeroCliente;
        this.plazoMeses = plazoMeses;
        this.montoPedido = montoPedido;
        this.montoConIntereses = (long) (montoPedido * 1.1); //interés fijo del 10% con conversion a long para trabajar con valores enteros
        this.valorCuota = (long) (this.montoConIntereses / this.plazoMeses); // Valor de la cuota fija 
        this.cuotasPagas = 0;
        this.saldoRestante = this.montoConIntereses; // Saldo deudor a pagar
        this.cuotasPagas = 0;
        this.moneda = moneda;
    }

    public void pagarCuota() {
        this.saldoRestante = this.saldoRestante - this.valorCuota;
        this.cuotasPagas++;
    } // Método para pagar una cuota

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(int plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public long getMontoPedido() {
        return montoPedido;
    }

    public void setMontoPedido(long montoPedido) {
        this.montoPedido = montoPedido;
    }

    public long getMontoConIntereses() {
        return montoConIntereses;
    }

    public void setMontoConIntereses(long montoConIntereses) {
        this.montoConIntereses = montoConIntereses;
    }

    public long getSaldoRestante() {
        return saldoRestante;
    }

    public void setSaldoRestante(long saldoRestante) {
        this.saldoRestante = saldoRestante;
    }

    public long getValorCuota() {
        return valorCuota;
    }

    public void setValorCuota(long valorCuota) {
        this.valorCuota = valorCuota;
    }

    public int getCuotasPagas() {
        return cuotasPagas;
    }

    public void setCuotasPagas(int cuotasPagas) {
        this.cuotasPagas = cuotasPagas;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }
    
}
