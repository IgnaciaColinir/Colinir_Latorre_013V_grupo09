package ms.paciente.ms.paciente.Exceptions;

// Excepción personalizada para cuando no se encuentra un Pokémon
public class PacienteNotFoundException extends RuntimeException {

    public PacienteNotFoundException(String mensaje) {
        super(mensaje);
    }
}