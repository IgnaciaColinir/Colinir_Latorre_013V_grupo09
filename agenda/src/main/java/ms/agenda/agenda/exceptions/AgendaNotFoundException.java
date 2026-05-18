package ms.agenda.agenda.exceptions;

// Excepción personalizada para cuando no se encuentra un Pokémon
public class AgendaNotFoundException extends RuntimeException {

    public AgendaNotFoundException(String mensaje) {
        super(mensaje);
    }
}