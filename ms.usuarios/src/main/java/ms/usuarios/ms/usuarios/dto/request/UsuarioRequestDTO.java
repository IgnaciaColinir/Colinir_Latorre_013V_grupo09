package ms.usuarios.ms.usuarios.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "El rut del usuario no puede estar vacío")
    private String rut;

    @NotBlank(message = "El nombre del usuario no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido del usuario no puede estar vacío")
    private String apellido;

    @Email(message = "El email del usuario debe ser válido")
    @NotBlank(message = "El email del usuario no puede estar vacío")
    private String email;
    
    @NotBlank(message = "La contraseña del usuario no puede estar vacía")
    private String password;

    @NotBlank(message = "El cargo del usuario no puede estar vacío")
    private String cargo;

}