package ms.pagos.ms.pagos.Modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModeloPago {
    
    private int id; 
    @Min(value = 1, message = "El ID de la consulta debe ser mayor que 0")
    private int idConsulta;
    @NotBlank(message = "El ID del paciente es obligatorio")
    private String idPaciente; 
    @Min(value = 0, message = "El valor de la consulta no puede ser negativo")
    private double valorConsulta;
    @Min(value = 0, message = "El valor del tratamiento no puede ser negativo")
    private double valorTratamiento;
    private double montoTotal;
    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;
    @NotBlank(message = "El estado del pago es obligatorio")
    private String estado;
    private LocalDateTime fechaPago;

}
