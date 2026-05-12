package ms.paciente.ms.paciente.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PacienteRequestDTO {

    @NotBlank(message = "El tratamiento del paciente no puede estar vacío")
    private String tratamiento;
    @NotBlank(message = "El rut del paciente no puede estar vacío")
    private String rut;
    @NotBlank(message = "El nombre del paciente no puede estar vacío")
    private String nombre;
    @NotBlank(message = "El apellido del paciente no puede estar vacío")
    private String apellido;
    @NotBlank(message = "La dirección del paciente no puede estar vacía")
    private String direccion;
    @NotBlank(message = "El teléfono del paciente no puede estar vacío")
    private String telefono;
    @NotBlank(message = "El email del paciente no puede estar vacío")
    private String email;
    @Min(value = 0, message = "El valor del tratamiento debe ser mayor o igual a cero")
    private int valorTratamiento;

}