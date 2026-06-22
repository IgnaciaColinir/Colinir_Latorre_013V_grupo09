package clinicaSalud.ms_servicios.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ServicioDTO {
    private Long idServicio;
    
    @NotBlank(message = "El nombre del servicio es obligatorio")
    private String nombre;
    
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
    
    @Min(value = 0, message = "El precio no puede ser negativo")
    private int precio;
    
    private boolean requiereAyuno;
}