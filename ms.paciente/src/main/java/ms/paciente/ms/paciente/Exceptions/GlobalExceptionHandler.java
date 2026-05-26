package ms.paciente.ms.paciente.Exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

// Esta clase captura errores de todos los controllers
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja errores de validación producidos por @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponsep> manejarValidaciones(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        ErrorResponsep error = new ErrorResponsep(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                mensaje,
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Maneja Paciente no encontrado
    @ExceptionHandler(PacienteNotFoundException.class)
    public ResponseEntity<ErrorResponsep> manejarPacienteNoEncontrado(
            PacienteNotFoundException ex,
            HttpServletRequest request
    ) {
        ErrorResponsep error = new ErrorResponsep(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Maneja cualquier error inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponsep> manejarErrorGeneral(
            Exception ex,
            HttpServletRequest request
    ) {
        String detalleError = ex.getClass().getSimpleName() + ": " + ex.getMessage();

        ErrorResponsep error = new ErrorResponsep(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                detalleError, // <--- Ahora Postman mostrará el error técnico real aquí
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
}
}