package ms.agenda.agenda.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

// DTO estándar para devolver errores al cliente
@Data
@Builder
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    // Para validaciones por campo, por ejemplo: {"costo": "debe ser mayor a 0"}
    private Map<String, String> details;
}