package ms.usuarios.ms.usuarios.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponseDTO {

    private String rut;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String cargo;

}