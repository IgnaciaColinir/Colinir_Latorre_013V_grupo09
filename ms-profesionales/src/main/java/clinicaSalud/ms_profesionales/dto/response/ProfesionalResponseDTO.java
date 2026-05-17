package clinicaSalud.ms_profesionales.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfesionalResponseDTO {
    private String rut;
    private String nombre;
    private String apellido;
    private String especialidad;
    private String email;
    private String telefono;
    private int valorConsulta;
}