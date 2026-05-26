package ms.consultas.ms.consultas.exceptions;

public class ConsultaNotFoundException extends RuntimeException {

    public ConsultaNotFoundException(String mensaje) {
        super(mensaje);
    }
}