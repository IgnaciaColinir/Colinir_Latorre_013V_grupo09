package ms.agenda.agenda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfesionalResponse {
    private String rut;
    private String nombre;
    private String apellido;
    private String especialidad;
    private String email;
    private String telefono;
    private int valorConsulta;
}
