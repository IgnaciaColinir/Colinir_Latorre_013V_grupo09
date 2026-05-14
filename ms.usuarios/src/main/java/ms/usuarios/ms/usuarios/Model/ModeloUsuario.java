package ms.usuarios.ms.usuarios.Model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModeloUsuario {
    
    private int id;

    @NotBlank(message = "El campo rut no puede estar vacío")
    private String rut;

    @NotBlank(message = "El campo nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El campo apellido no puede estar vacío")
    private String apellido;

    @NotBlank(message = "El campo email no puede estar vacío")
    private String email;

    @NotBlank(message = "El campo password no puede estar vacío")
    private String password;
    
    @NotBlank(message = "El campo cargo no puede estar vacío")
    private String cargo;


}
