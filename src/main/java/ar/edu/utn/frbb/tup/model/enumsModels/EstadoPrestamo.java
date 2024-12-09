package ar.edu.utn.frbb.tup.model.enumsModels;

public enum EstadoPrestamo {
    APROBADO("A"),
    RECHAZADO("R");

    private final String codigo;

    EstadoPrestamo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static EstadoPrestamo fromCodigo(String codigo) {
        for (EstadoPrestamo estado : values()) {
            if (estado.getCodigo().equals(codigo)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Código de estado inválido: " + codigo);
    }
}