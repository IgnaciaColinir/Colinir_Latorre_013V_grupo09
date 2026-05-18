package ms.agenda.agenda.Model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelAgenda {

    
    private int id;
    @NotBlank(message = "El campo fecha es obligatorio")
    private String fecha;
    @NotBlank(message = "El campo hora es obligatorio")
    private String hora;
    @NotBlank(message = "El campo idProfesional es obligatorio")
    private String idProfesional;
    @NotBlank(message = "El campo idPaciente es obligatorio")
    private String idPaciente;
    @NotBlank(message = "El campo estado es obligatorio")
    private String estado;

}
