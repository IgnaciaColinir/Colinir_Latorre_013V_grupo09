package ms.usuarios.ms.usuarios.exceptions;

// Excepción personalizada para cuando no se encuentra un usuario
public class UsuarioNotFoundException extends RuntimeException {

    public UsuarioNotFoundException(String mensaje) {
        super(mensaje);
    }
}