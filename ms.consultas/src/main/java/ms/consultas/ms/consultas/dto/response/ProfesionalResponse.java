package ms.consultas.ms.consultas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfesionalResponse {
    private String rut;
    private String nombre;
    private String apellido;
    private String especialidad;
    private String email;
    private String telefono;
    private double valorConsulta;
}
