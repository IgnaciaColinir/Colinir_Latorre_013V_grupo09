package ms.consultas.ms.consultas.exceptions;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

// Esta clase captura errores de todos los controllers
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja errores de validación producidos por @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> manejarValidaciones(
                MethodArgumentNotValidException ex,
                HttpServletRequest request
        ) {
        // 1. Creamos un mapa clásico vacío
        Map<String, String> detallesErrores = new java.util.HashMap<>();

        // 2. Recorremos los errores uno por uno de forma segura
        ex.getBindingResult().getFieldErrors().forEach(error -> {
                // Si el campo ya tiene un error registrado, no lo sobrescribe
                detallesErrores.putIfAbsent(error.getField(), error.getDefaultMessage());
        });

        // 3. Mensaje general de la excepción
        String mensajeGeneral = "Error de validación en los campos enviados.";

        // 4. Construimos la respuesta con tu molde .builder()
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(mensajeGeneral)
                .path(request.getRequestURI())
                .details(detallesErrores) // Inyectamos el mapa limpio
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

    // Maneja Pokémon no encontrado
    @ExceptionHandler(ConsultaNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarConsultaNoEncontrado(
            ConsultaNotFoundException ex,
            HttpServletRequest request
    ) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Maneja cualquier error inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErrorGeneral(Exception ex, HttpServletRequest request) {
        // Esto es lo que nos dirá si es una conexión fallida, un nulo o un error de Feign
        String causaReal = ex.getClass().getSimpleName() + ": " + ex.getMessage();
        
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message(causaReal) // ¡Aquí está la magia!
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
        
        @ExceptionHandler(feign.FeignException.class)
        public ResponseEntity<ErrorResponse> manejarFeignException(feign.FeignException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.status())
                .error("Error en comunicación con Microservicio")
                .message("El servicio externo respondió con error: " + ex.contentUTF8())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(ex.status()).body(error);
        }
}