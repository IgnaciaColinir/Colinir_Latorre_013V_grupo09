package clinicaSalud.ms_inventario.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InsumoDTO {
    private Long idInsumo;
    
    @NotBlank(message = "El nombre del insumo es obligatorio")
    private String nombre;
    
    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;
    
    @Min(value = 0, message = "El stock actual no puede ser negativo")
    private int stockActual;
    
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    private int stockMinimo;
}