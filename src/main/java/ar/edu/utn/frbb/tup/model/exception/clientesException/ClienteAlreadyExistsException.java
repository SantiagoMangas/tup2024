package ar.edu.utn.frbb.tup.model.exception.clientesException;

public class ClienteAlreadyExistsException extends Exception {
    public ClienteAlreadyExistsException(long dni) {
        super("Ya existe un cliente con DNI " + dni);
    }
}
