package ms.consultas.ms.consultas.exceptions;

// Excepción personalizada para cuando no se encuentra un Pokémon
public class ConsultaNotFoundException extends RuntimeException {

    public ConsultaNotFoundException(String mensaje) {
        super(mensaje);
    }
}