package clinicaSalud.ms_profesionales.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfesionalRequestDTO {

    @NotBlank(message = "El rut no puede estar vacío")
    private String rut;
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;
    @NotBlank(message = "La especialidad no puede estar vacía")
    private String especialidad;
    @NotBlank(message = "El email no puede estar vacío")
    private String email;
    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;
    @Min(value = 0, message = "El valor de la consulta debe ser mayor o igual a cero")
    private int valorConsulta;
}