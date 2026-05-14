package ms.usuarios.ms.usuarios.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "El rut del paciente no puede estar vacío")
    private String rut;
    @NotBlank(message = "El nombre del paciente no puede estar vacío")
    private String nombre;
    @NotBlank(message = "El apellido del paciente no puede estar vacío")
    private String apellido;
    @NotBlank(message = "El email del paciente no puede estar vacío")
    private String email;
    @NotBlank(message = "La contraseña del paciente no puede estar vacía")
    private String password;
    @NotBlank(message = "El cargo del paciente no puede estar vacío")
    private String cargo;

}