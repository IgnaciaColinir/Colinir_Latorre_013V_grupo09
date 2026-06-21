package ms.usuarios.ms.usuarios.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private String rut;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String cargo;

}