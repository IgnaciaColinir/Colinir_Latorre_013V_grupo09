package ms.pagos.ms.pagos.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PagoRequestDTO {

    @Min(value = 1, message = "El ID de la consulta debe ser mayor que 0")
    private int idConsulta;
    @NotBlank(message = "El ID del paciente es obligatorio")
    private String idPaciente; 
    @Min(value = 0, message = "El valor de la consulta no puede ser negativo")
    private double valorConsulta;
    @Min(value = 0, message = "El valor del tratamiento no puede ser negativo")
    private double valorTratamiento;
    @Min(value = 0, message = "El monto total no puede ser negativo")
    private double montoTotal;
    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;
    @NotBlank(message = "El estado del pago es obligatorio")
    private String estado;
    @NotBlank(message = "La fecha de pago es obligatoria")
    private String fechaPago;
}